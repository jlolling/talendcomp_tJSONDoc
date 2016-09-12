package de.cimt.talendcomp.json.streaming;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonStreamParser {
	
	private static Logger logger = Logger.getLogger(JsonStreamParser.class);
	private JsonFactory factory = new JsonFactory();
	private JsonParser parser = null;
	// stack to hold the levels of the objects 
	private Stack<String> stack = new Stack<String>();
	private int currentLoopIndex = 0;
	private String loopPath = null;
	private Map<String, String> columnExpectedPathMap = new HashMap<String, String>();
	private Set<String> expectedPathSet = new TreeSet<String>();
	private TreeMap<String, StringBuilder> currentPathContentMap = new TreeMap<String, StringBuilder>();
	private boolean streamEnded = false;
	private ObjectMapper objectMapper = new ObjectMapper();
	private boolean firstToken = true;
	private int jsonLevel = 0;
	private int loopPathLevel = -1;
	
	public static void enableTraceLogging(boolean on) {
		if (on) {
			logger.setLevel(Level.TRACE);
		} else {
			logger.setLevel(Level.INFO);
		}
	}
	
	public void addColumnAttrPath(String name, String attrPath) {
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("name must not be null or empty");
		}
		if (attrPath == null || attrPath.trim().isEmpty()) {
			throw new IllegalArgumentException("attrPath must not be null or empty");
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
		currentPathContentMap = new TreeMap<String, StringBuilder>();
		currentLoopIndex = 0;
	}
	
	public void setInputStream(InputStream in) throws Exception {
		if (in == null) {
			throw new IllegalArgumentException("InputStream must be null!");
		}
		reset();
		parser = factory.createParser(in);
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
		// prepare for next record
		currentPathContentMap = new TreeMap<String, StringBuilder>();
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
				appendContent(path, "{");
				jsonLevel++;
			} else if (token == JsonToken.START_ARRAY) {
				if (firstToken) {
					firstToken = false;
					push("$");
				}
				if (name == null) {
					// in case of an array within an array
					name = "";
				}
				String path = push(name + "[*]");
				// check if the path is expected, start collecting tokens
				appendContent(path, "[");
				jsonLevel++;
			} else if (token == JsonToken.FIELD_NAME) {
				appendName(getCurrentStackPath(), "\"" + parser.getText() + "\":");
			} else if (token == JsonToken.VALUE_TRUE || token == JsonToken.VALUE_FALSE) {
				appendContent(getCurrentStackPath() + "." + parser.getCurrentName(), parser.getText());
			} else if (token == JsonToken.VALUE_NULL) {
				appendContent(getCurrentStackPath() + "." + parser.getCurrentName(), "null");
			} else if (token == JsonToken.VALUE_NUMBER_FLOAT) {
				appendContent(getCurrentStackPath() + "." + parser.getCurrentName(), parser.getText());
			} else if (token == JsonToken.VALUE_NUMBER_INT) {
				appendContent(getCurrentStackPath() + "." + parser.getCurrentName(), parser.getText());
			} else if (token == JsonToken.VALUE_STRING) {
				appendContent(getCurrentStackPath() + "." + parser.getCurrentName(), "\"" + parser.getText().replace("\n", "\\n") + "\"");
			} else if (token == JsonToken.END_OBJECT) {
				String path = pop();
				appendContent(path, "}");
				jsonLevel--;
				// check if we have reached the loop path end
				if (logger.isTraceEnabled()) {
					logger.trace("END_OBJECT: path: " + path);
				}
				if (loopPath.equals(path)) {
					if (loopPath.equals(path)) {
						if (loopPathLevel == -1) {
							loopPathLevel = jsonLevel;
							currentLoopIndex++;
							endReached = true;
							break;
						} else if (loopPathLevel == jsonLevel) {
							currentLoopIndex++;
							endReached = true;
							break;
						} else {
							break;
						}
					}
				}
			} else if (token == JsonToken.END_ARRAY) {
				String path = pop();
				appendContent(path, "]");
				jsonLevel--;
				if (logger.isTraceEnabled()) {
					logger.trace("END_ARRAY: path: " + path);
				}
				// check if we have reached the loop path end
				if (loopPath.equals(path)) {
					if (loopPathLevel == -1) {
						loopPathLevel = jsonLevel;
						currentLoopIndex++;
						endReached = true;
						break;
					} else if (loopPathLevel == jsonLevel) {
						currentLoopIndex++;
						endReached = true;
						break;
					} else {
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
	
	private void appendContent(String path, String value) {
		if (logger.isTraceEnabled()) {
			logger.trace("appendContent: path: " + path + " value: " + value);
		}
		for (String ep : expectedPathSet) {
			if (path.startsWith(ep)) {
				if (logger.isTraceEnabled()) {
					logger.trace("	apply to ep: " + ep);
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
	
	private void appendName(String path, String name) {
		if (logger.isTraceEnabled()) {
			logger.trace("appendName: path: " + path + " name: " + name);
		}
		for (String ep : expectedPathSet) {
			if (path.startsWith(ep)) {
				if (logger.isTraceEnabled()) {
					logger.trace("	apply to ep: " + ep);
				}
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

	private String push(String name) {
		if (logger.isTraceEnabled()) {
			logger.trace("PUSH " + name);
		}
		stack.push(name);
		return getCurrentStackPath();
	}

	private String pop() {
		if (logger.isTraceEnabled()) {
			logger.trace("POP");
		}
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
		if (logger.isTraceEnabled()) {
			logger.trace("next: found: " + found + " stream ended: " + streamEnded);
		}
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
			return sb.toString();
		} else {
			return null;
		}
	}
	
	public JsonNode parseString(String jsonString) throws Exception {
		return objectMapper.readTree(jsonString);
	}
	
}
