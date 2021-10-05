package de.jlo.talendcomp.json.streaming;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;

public class JsonStreamParser {
	
	private JsonFactory factory = new JsonFactory();
	private JsonParser parser = null;
	// stack to hold the levels of the objects 
	private Stack<String> stack = new Stack<String>();
	private int currentLoopIndex = 0;
	private String loopPath = null;
	private Map<String, String> columnExpectedPathMap = new HashMap<>();
	private Set<String> expectedPathSet = new TreeSet<String>();
	private TreeMap<String, StringBuilder> currentPathContentMap = new TreeMap<>();
	private Map<String, Integer> expectedPathOccurence = new HashMap<>();
	private List<String> keysToDel = new ArrayList<String>();
	private boolean streamEnded = false;
	private ObjectMapper objectMapper = new ObjectMapper();
	private boolean firstToken = true;
	private int jsonLevel = 0;
	private int currLoopPathLevel = -1;
	private static String loopPathDummyName = "#LOOP";
	
	public void addColumnAttrPath(String name, String attrPath) {
		if (loopPath == null) {
			throw new IllegalStateException("Loop path must be set before!");
		}
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("name must not be null or empty");
		}
		if (attrPath == null || attrPath.trim().isEmpty()) {
			throw new IllegalArgumentException("attrPath must not be null or empty");
		}
		if (attrPath.startsWith("$") == false) {
			attrPath = loopPath + "." + attrPath;
		}
		columnExpectedPathMap.put(name, attrPath);
		expectedPathSet.add(attrPath);
	}
	
	public void setInputFile(String filePath) throws Exception {
		if (filePath == null || filePath.trim().isEmpty()) {
			throw new IllegalArgumentException("filePath must be null or empty!");
		}
		File f = new File(filePath);
		if (f.exists() == false) {
			throw new Exception("Input file: " + filePath + " does not exists!");
		}
		reset();
		parser = factory.createParser(f);
	}
	
	private void reset() {
		currentPathContentMap.clear();
		expectedPathOccurence.clear();
		currentLoopIndex = 0;
	}
	
	public void setInputStream(InputStream in) throws Exception {
		if (in == null) {
			throw new IllegalArgumentException("InputStream cannot be null!");
		}
		reset();
		parser = factory.createParser(in);
	}
	
	public void setInputResource(String inputResourceName) throws Exception {
		if (inputResourceName == null || inputResourceName.trim().isEmpty()) {
			throw new IllegalArgumentException("Input resource name cannot be null or empty!");
		}
		reset();
		if (inputResourceName.startsWith("/") == false) {
			inputResourceName = "/" + inputResourceName;
		}
		InputStream in = this.getClass().getResourceAsStream(inputResourceName.trim());
		if (in == null) {
			throw new Exception("There is no input resource with the name: " + inputResourceName);
		}
		parser = factory.createParser(in);
	}
	
	public static int getKeyLevel(String key) {
		int level = 0;
		char c = '°';
		for (int i = 0, n = key.length(); i < n; i++) {
			c = key.charAt(i);
			if (c == '$') {
				level++;
			} else if (c == '[') {
				level++;
			} else if (c == '.') {
				level++;
			}
		}
		// after the loop c contains the last char
		if (c != ']') {
			// we do not have an array as last element
			// in this case it is a attribute and we have to lower the level to reach the 
			// object carring this attribute
			if (level > 1) {
				level--;
			}
		}
		return level;
	}
	
	private void clearPathContentMap() {
		// prepare for next record
		for (String key : keysToDel) {
			currentPathContentMap.remove(key);
		}
		keysToDel.clear();
	}
	
	private void collectKeysToDelete() {
		// prepare for next record
		for (String key : currentPathContentMap.keySet()) {
			int keyLevel = getKeyLevel(key);
			if (keyLevel > jsonLevel) {
				if (keysToDel.contains(key) == false) {
					keysToDel.add(key);
				}
			}
		}
	}
		
	private void incrementJsonLevel() {
		jsonLevel++;
	}
	
	private void decrementJsonLevel() {
		jsonLevel--;
		collectKeysToDelete();
	}
	
	/**
	 * parse the stream
	 * @return true if an end of an loop element or the end was found
	 * @throws Exception
	 */
	public boolean parseStream() throws Exception {
		if (parser == null) {
			throw new IllegalArgumentException("Parser not initialized.");
		}
		if (loopPath == null) {
			throw new IllegalArgumentException("Loop-path not set.");
		}
		if (columnExpectedPathMap.isEmpty()) {
			throw new IllegalArgumentException("Expected path to column mapping is empty!");
		}
		if (expectedPathSet.isEmpty()) {
			throw new IllegalArgumentException("Expected path set is empty!");
		}
		// prepare for next record
		clearPathContentMap();
		boolean endReached = false;
		JsonToken token = null;
		String name = null;
		while ((token = parser.nextToken()) != null) {
			name = parser.getCurrentName();
			if (token == JsonToken.START_OBJECT) {
				if (firstToken) {
					firstToken = false;
					push("$");
				}
				if (name == null) {
					// in case of an object within an array
					name = "";
				}
				String path = push(name);
				// check if the path is expected, start collecting tokens
				incrementJsonLevel();
				appendObject(path, "{");
			} else if (token == JsonToken.START_ARRAY) {
				if (firstToken) {
					firstToken = false;
					push("$");
				}
				if (name == null) {
					// in case of an array within an array
					name = "";
				}
				// check if the path is expected, start collecting tokens
				incrementJsonLevel();
				appendContent(getCurrentStackPath(), "["); // the start of the array applies to the former object
				push(name + "[*]");
			} else if (token == JsonToken.FIELD_NAME) {
				appendName(getCurrentStackPath(), "\"" + parser.getText() + "\":");
			} else if (token == JsonToken.VALUE_TRUE || token == JsonToken.VALUE_FALSE) {	
				appendValue(getCurrentStackPath() + "." + parser.getCurrentName(), parser.getText());
			} else if (token == JsonToken.VALUE_NULL) {
				appendValue(getCurrentStackPath() + "." + parser.getCurrentName(), "null");
			} else if (token == JsonToken.VALUE_NUMBER_FLOAT) {
				appendValue(getCurrentStackPath() + "." + parser.getCurrentName(), parser.getText());
			} else if (token == JsonToken.VALUE_NUMBER_INT) {
				appendValue(getCurrentStackPath() + "." + parser.getCurrentName(), parser.getText());
			} else if (token == JsonToken.VALUE_STRING) {
				appendValue(getCurrentStackPath() + "." + parser.getCurrentName(), getJsonStringValue(parser.getText()));
			} else if (token == JsonToken.END_OBJECT) {
				String path = pop();
				appendContent(path, "}");
				decrementJsonLevel();
				// check if we have reached the loop path end
				if (loopPath.equals(path)) {
					if (currLoopPathLevel == -1) {
						currLoopPathLevel = jsonLevel;
						currentLoopIndex++;
						endReached = true;
						break;
					} else if (currLoopPathLevel == jsonLevel) {
						currentLoopIndex++;
						endReached = true;
						break;
					}
				}
			} else if (token == JsonToken.END_ARRAY) {
				String path = pop();
				appendContent(getCurrentStackPath(), "]"); // the end of the array applies to the former object
				decrementJsonLevel();
				// check if we have reached the loop path end
				if (loopPath.equals(path)) {
					if (currLoopPathLevel == -1) {
						currLoopPathLevel = jsonLevel;
						currentLoopIndex++;
						endReached = true;
						break;
					} else if (currLoopPathLevel == jsonLevel) {
						currentLoopIndex++;
						endReached = true;
						break;
					}
				}
			}
		}
		if (token == null) {
			streamEnded = true;
		}
		return endReached;
	}
	
	private String getJsonStringValue(String rawText) throws JsonProcessingException {
		return objectMapper.writeValueAsString(TextNode.valueOf(rawText));
	}
	
	private void appendContent(String path, String value) {
		for (String ep : expectedPathSet) {
			if (isMatchingSubpath(path,ep)) {
				if (keysToDel.contains(ep)) {
					currentPathContentMap.remove(ep);
					keysToDel.remove(ep);
				} else {
				}
				StringBuilder sb = currentPathContentMap.get(ep);
				if (sb == null) {
					sb = new StringBuilder(value);
					currentPathContentMap.put(ep, sb);
				} else {
					sb.append(value);
				}
			}
		}
	}
	
	public static boolean isMatchingSubpath(String path, String expectedPath) {
		if (path.equals(expectedPath)) {
			return true;
		}
		int pos = -1;
		String ep = "";
		while (true) {
			pos = path.indexOf(".", pos + 1);
			if (pos == -1) {
				break;
			} else {
				ep = path.substring(0, pos);
				if (expectedPath.equals(ep)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private void appendName(String path, String name) {
		for (String ep : expectedPathSet) {
			if (isMatchingSubpath(path, ep)) {
				StringBuilder sb = currentPathContentMap.get(ep);
				if (sb == null) {
					sb = new StringBuilder(name);
					currentPathContentMap.put(ep, sb);
				} else {
					if (sb.toString().endsWith("{") == false) {
						sb.append(",");
					}
					sb.append(name);
				}
			}
		}
	}

	private void appendObject(String path, String object) {
		for (String ep : expectedPathSet) {
			if (isMatchingSubpath(path, ep)) {
				if (keysToDel.contains(ep)) {
					currentPathContentMap.remove(ep);
					keysToDel.remove(ep);
				}
				StringBuilder sb = currentPathContentMap.get(ep);
				if (sb == null) {
					sb = new StringBuilder(object);
					currentPathContentMap.put(ep, sb);
					Integer counter = expectedPathOccurence.get(ep);
					if (counter == null) {
						counter = 1;
					} else {
						counter = counter + 1;
					}
					expectedPathOccurence.put(ep, counter);
				} else {
					if (sb.toString().endsWith("}")) {
						sb.append(",");
					}
					sb.append(object);
				}
			}
		}
	}

	private void appendValue(String path, String value) {
		for (String ep : expectedPathSet) {
			if (isMatchingSubpath(path, ep)) {
				if (keysToDel.contains(ep)) {
					currentPathContentMap.remove(ep);
					keysToDel.remove(ep);
				} else {
				}
				StringBuilder sb = currentPathContentMap.get(ep);
				if (sb == null) {
					sb = new StringBuilder(value);
					currentPathContentMap.put(ep, sb);
					Integer counter = expectedPathOccurence.get(ep);
					if (counter == null) {
						counter = 1;
					} else {
						counter = counter + 1;
					}
					expectedPathOccurence.put(ep, counter);
				} else {
					if (sb.toString().endsWith(":") == false && sb.toString().endsWith("[") == false) {
						// we have no attribute name before, it must be an value array
						sb.append(",");
					}
					sb.append(value);
				}
			}
		}
	}

	private String push(String name) {
		stack.push(name);
		return getCurrentStackPath();
	}

	private String pop() {
		String path = getCurrentStackPath();
		stack.pop();
		return path;
	}
	
	/**
	 * parse the input until the next search path is found
	 * @return true if found, false if at the end of the document
	 * @throws Exception
	 */
	public boolean next() throws Exception {
		boolean found = parseStream();
		if (streamEnded) {
			return false;
		} else {
			return found;
		}
	}
	
	private String getCurrentStackPath() {
		int size = stack.size();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			String name = stack.get(i);
			if (name.isEmpty()) {
				continue;
			}
			if (i > 0 && name.startsWith("[") == false) {
				sb.append(".");
			}
			sb.append(name);
		}
		return sb.toString();
	}

	public int getCurrentLoopIndex() {
		return currentLoopIndex;
	}

	public void setLoopPath(String loopPath) {
		if (loopPath == null || loopPath.trim().isEmpty()) {
			throw new IllegalArgumentException("loop path must not be null or empty!");
		}
		this.loopPath = loopPath;
		addColumnAttrPath(loopPathDummyName, loopPath);
	}
	
	public String getValue(String name) {
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("name must not be null or empty");
		}
		String path = columnExpectedPathMap.get(name);
		if (path == null) {
			throw new IllegalArgumentException("name does not exist in configuration");
		}
		StringBuilder sb = currentPathContentMap.get(path);
		if (sb != null) {
			String value = sb.toString();
			if ("null".equals(value)) {
				return null;
			}
			if (value.startsWith("\"") && value.endsWith("\"")) {
				value = value.substring(1, value.length() - 1);
			}
			return value;
		} else {
			return null;
		}
	}
	
	public int getCountOccurence(String name) {
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("name must not be null or empty");
		}
		String path = columnExpectedPathMap.get(name);
		if (path == null) {
			throw new IllegalArgumentException("name does not exist in configuration");
		}
		Integer counter = expectedPathOccurence.get(path);
		if (counter == null) {
			return 0;
		} else {
			return counter.intValue();
		}
	}

	public String getLoopJsonString() {
		return getValue(loopPathDummyName);
	}
	
	public JsonNode getLoopJsonNode() throws Exception {
		String jsonString = getValue(loopPathDummyName);
		if (jsonString != null) {
			return objectMapper.readTree(jsonString);
		} else {
			return null;
		}
	}
		
}
