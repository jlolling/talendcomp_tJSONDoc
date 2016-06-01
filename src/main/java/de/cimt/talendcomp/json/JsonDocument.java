/**
 * Copyright 2015 Jan Lolling jan.lolling@gmail.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cimt.talendcomp.json;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ParseContext;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;

/**
 * Convenient class to work with the Jackson API for JSON
 */
public class JsonDocument {
	
	private JsonNode rootNode = null;
	private ObjectMapper objectMapper = new ObjectMapper();
	private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";
	private Map<String, SimpleDateFormat> dateFormatMap = new HashMap<String, SimpleDateFormat>();
	private DocumentContext rootContext = null;
	private static final Configuration JACKSON_JSON_NODE_CONFIGURATION = Configuration
            .builder()
            .mappingProvider(new JacksonMappingProvider())
            .jsonProvider(new JacksonJsonNodeJsonProvider())
            .build();
	
	public JsonDocument(boolean isArray) {
		ParseContext parseContext = JsonPath.using(JACKSON_JSON_NODE_CONFIGURATION);
		if (isArray) {
			rootContext = parseContext.parse("[]");
		} else {
			rootContext = parseContext.parse("{}");
		}
		rootNode = rootContext.read("$");
		JsonNode testNode = rootContext.read("$");
		if (rootNode != testNode) {
			throw new IllegalStateException("Clones objects detected! Use the latest Jayway library 2.2.1+");
		}
	}

	public JsonDocument(String jsonContent, boolean isArray) {
		ParseContext parseContext = JsonPath.using(JACKSON_JSON_NODE_CONFIGURATION);
		if (jsonContent != null && jsonContent.trim().isEmpty() == false) {
			rootContext = parseContext.parse(jsonContent);
		} else { 
			if (isArray) {
				rootContext = parseContext.parse("[]");
			} else {
				rootContext = parseContext.parse("{}");
			}
		}
		rootNode = rootContext.read("$");
		JsonNode testNode = rootContext.read("$");
		if (rootNode != testNode) {
			throw new IllegalStateException("Clones objects detected! Use the latest Jayway library 2.2.1+");
		}
	}
	
	public JsonDocument(File jsonFile, boolean isArray) throws Exception {
		ParseContext parseContext = JsonPath.using(JACKSON_JSON_NODE_CONFIGURATION);
		if (jsonFile != null) {
			if (jsonFile.exists() == false) {
				throw new Exception("JSON input file: " + jsonFile.getAbsolutePath() + " does not exists or is not readable!");
			}
			InputStream in = new FileInputStream(jsonFile); 
			rootContext = parseContext.parse(in);
			// parseContext closes this stream
		} else { 
			if (isArray) {
				rootContext = parseContext.parse("[]");
			} else {
				rootContext = parseContext.parse("{}");
			}
		}
		rootNode = rootContext.read("$");
		JsonNode testNode = rootContext.read("$");
		if (rootNode != testNode) {
			throw new IllegalStateException("Clones objects detected! Use the latest Jayway library 2.2.1+");
		}
	}

	public JsonDocument(JsonNode jsonNode, boolean isArray) throws Exception {
		ParseContext parseContext = JsonPath.using(JACKSON_JSON_NODE_CONFIGURATION);
		if (jsonNode != null) {
			rootContext = parseContext.parse("{}");
			rootContext.set("$", jsonNode);
		} else { 
			if (isArray) {
				rootContext = parseContext.parse("[]");
			} else {
				rootContext = parseContext.parse("{}");
			}
		}
		rootNode = jsonNode;
		JsonNode testNode = rootContext.read("$");
		if (rootNode != testNode) {
			throw new IllegalStateException("Clones objects detected! Use the latest Jayway library 2.2.1+");
		}
	}

	public DocumentContext getDocumentContext() {
		return rootContext;
	}
	
	public boolean isArray() {
		return rootNode.isArray();
	}
	
	public ObjectNode createObjectNode(String name) {
		ObjectNode child = ((ObjectNode) rootNode).objectNode();
		if (isArray()) {
			throw new IllegalStateException("Root is an array, use addObjectNode instead!");
		}
		((ObjectNode) rootNode).set(name, child);
		return child;
	}

	public ObjectNode addObjectNode(String name) {
		ObjectNode child = ((ArrayNode) rootNode).objectNode();
		if (isArray() == false) {
			throw new IllegalStateException("Root is an array, use addObjectNode instead!");
		}
		((ArrayNode) rootNode).add(child);
		return child;
	}

	public ObjectNode addObjectNode(JsonNode parent, String name) {
		if (parent instanceof ArrayNode) {
			ObjectNode child = ((ArrayNode) parent).objectNode();
			((ArrayNode) parent).add(child);
			return child;
		} else {
			throw new IllegalArgumentException("parent must be an ArrayNode!");
		}
	}

	public ArrayNode addArrayNode(JsonNode parent, String name) {
		if (parent instanceof ArrayNode) {
			ArrayNode child = ((ArrayNode) parent).arrayNode();
			((ArrayNode) parent).add(child);
			return child;
		} else {
			throw new IllegalArgumentException("parent must be an ArrayNode!");
		}
	}

	public ArrayNode createArrayNode(String name) {
		if (isArray()) {
			ArrayNode child = ((ArrayNode) rootNode).arrayNode();
			((ArrayNode) rootNode).add(child);
			return child;
		} else {
			ArrayNode child = ((ObjectNode) rootNode).arrayNode();
			((ObjectNode) rootNode).set(name, child);
			return child;
		}
	}

	@Override
	public String toString() {
		return rootNode.toString();
	}

	private List<String> getDirectPathTokens(String jsonPath) {
		List<String> tokens = new ArrayList<String>();
		fillDirectPathToken(jsonPath, tokens);
		return tokens;
	}
	
	private void fillDirectPathToken(String jsonPath, List<String> tokens) {
		int pos = jsonPath.indexOf('.');
		if (pos != -1) {
			char pc = ' ';
			while (true) {
				if (pos > 0) {
					pc = jsonPath.charAt(pos - 1);
				}
				if (pc == '@' || pc == '\\') {
					// skip over a filter condition or escaped dot
					// find next position
					int nextPos = jsonPath.indexOf('.', pos + 1);
					if (nextPos > pos) {
						// take this new position
						pos = nextPos;
					} else {
						pos = -1;
						break;
					}
				} else {
					break;
				}
			}
			if (pos != -1 && pos < jsonPath.length() - 1) {
				if (pos > 0) {
					// only use not empty tokens
					String token = jsonPath.substring(0, pos);
					tokens.add(token);
				}
				jsonPath = jsonPath.substring(pos + 1);
				fillDirectPathToken(jsonPath, tokens);
			}
		} else {
			tokens.add(jsonPath);
		}
	}

	/**
	 * returns the node start from the root
	 * @param jsonPath
	 * @return node or null if nothing found or a MissingNode was found
	 */
	public JsonNode getNode(String jsonPath) {
		try {
			JsonNode node = rootContext.read(jsonPath);
			if (node.isMissingNode()) {
				return null;
			} else {
				return node;
			}
		} catch (PathNotFoundException e) {
			return null;
		}
	}
	
	private boolean isArrayToken(String token) {
		return token.contains("[") && token.contains("]");
	}
	
	private String getArrayTokenAttributeName(String token) {
		int pos = token.indexOf("[");
		if (pos > 0) {
			return token.substring(0, pos);
		} else {
			return token;
		}
	}
	
	/**
	 * Retrieve a node by direct-path jsonPath
	 * @param jsonPath must be a path with query parts and starts with root
	 * @param create if true, missing nodes will be created
	 * @return node
	 */
	public JsonNode getNode(String jsonPath, boolean create) {
		return getNode(rootNode, jsonPath, create);
	}
	
	/**
	 * Retrieve a node by direct-path jsonPath
	 * @param parentNode node to start
	 * @param jsonPath must be a path with query parts and starts with the given parent node
	 * @param create if true, missing nodes will be created
	 * @return node
	 */
	public JsonNode getNode(JsonNode parentNode, String jsonPath, boolean create) {
		if (parentNode == null) {
			parentNode = rootNode;
		}
		JsonNode childNode = null;
		if (jsonPath == null || jsonPath.trim().isEmpty() || jsonPath.trim().equals(".")) {
			return parentNode;
		}
		List<String> tokenList = getDirectPathTokens(jsonPath);
		for (String token : tokenList) {
			if (token.startsWith("$")) {
				parentNode = rootNode;
				childNode = parentNode;
				continue; // skip the root node, parentNode is already the root
			}
			childNode = parentNode.get(token);
			if (childNode == null) {
				if (create) {
					if (parentNode instanceof ArrayNode) {
						if (isArrayToken(token)) {
							JsonNode arrayElement = ((ArrayNode) parentNode).get(0);
							if (arrayElement == null || arrayElement.isNull()) {
								childNode = ((ArrayNode) parentNode).addObject().withArray(getArrayTokenAttributeName(token));
							} else {
								childNode = ((ArrayNode) parentNode).get(0).withArray(getArrayTokenAttributeName(token));
							}
						} else {
							JsonNode arrayElement = ((ArrayNode) parentNode).get(0);
							if (arrayElement == null || arrayElement.isNull()) {
								childNode = ((ArrayNode) parentNode).addObject().with(token);
							} else {
								childNode = ((ArrayNode) parentNode).get(0).with(token);
							}
						}
					} else {
						if (isArrayToken(token)) {
							childNode = ((ObjectNode) parentNode).withArray(getArrayTokenAttributeName(token));
						} else {
							childNode = ((ObjectNode) parentNode).with(token);
						}
					}
				}
			}
			parentNode = childNode;
		}
		return childNode;
	}
	
	public DocumentContext getRootContext() {
		return rootContext;
	}
	
	public JsonNode getRootNode() {
		return rootNode;
	}
	
	public String getDateString(Date value, String pattern) {
		if (value == null) {
			return null;
		}
		if (pattern == null || pattern.trim().isEmpty()) {
			pattern = DEFAULT_DATE_PATTERN;
		}
		SimpleDateFormat sdf = dateFormatMap.get(pattern);
		if (sdf == null) {
			sdf = new SimpleDateFormat(pattern);
			dateFormatMap.put(pattern, sdf);
		}
		return sdf.format(value);
	}

	public ObjectNode setValue(ObjectNode node, String fieldName, String value) {
		node.put(fieldName, value);
		return node;
	}
	
	public JsonNode setJsonObject(ObjectNode node, String fieldName, String json) throws Exception {
		JsonNode newnode = buildNode(json);
		node.set(fieldName, newnode);
		return node;
	}

	public JsonNode setJsonObject(ObjectNode node, String fieldName, JsonNode json) throws Exception {
		node.set(fieldName, json);
		return node;
	}

	public ObjectNode setValue(ObjectNode node, String fieldName, Boolean value) {
		node.put(fieldName, value);
		return node;
	}

	public ObjectNode setValue(ObjectNode node, String fieldName, Integer value) {
		node.put(fieldName, value);
		return node;
	}

	public ObjectNode setValue(ObjectNode node, String fieldName, Long value) {
		node.put(fieldName, value);
		return node;
	}

	public ObjectNode setValue(ObjectNode node, String fieldName, BigDecimal value) {
		node.put(fieldName, value);
		return node;
	}

	public ObjectNode setValue(ObjectNode node, String fieldName, BigInteger value) {
		if (value != null) {
			node.put(fieldName, value.longValue());
		} else {
			node.set(fieldName, null);
		}
		return node;
	}

	public ObjectNode setValue(ObjectNode node, String fieldName, Double value) {
		node.put(fieldName, value);
		return node;
	}

	public ObjectNode setValue(ObjectNode node, String fieldName, Float value) {
		node.put(fieldName, value);
		return node;
	}

	public ObjectNode setValue(ObjectNode node, String fieldName, Short value) {
		node.put(fieldName, value);
		return node;
	}

	public ObjectNode setValue(ObjectNode node, String fieldName, Date value, String pattern) {
		node.put(fieldName, getDateString(value, pattern));
		return node;
	}
	
	public JsonNode buildNode(JsonNode value) throws Exception {
		if (value instanceof JsonNode) {
			return (JsonNode) value;
		} else {
			return null;
		}
	}

	public JsonNode buildNode(String value) throws Exception {
		if (value instanceof String) {
			String jsonString = (String) value;
			if (jsonString == null || jsonString.trim().isEmpty()) {
				return null;
			} else {
				return objectMapper.readTree(jsonString);
			}
		} else {
			return null;
		}
	}
	
	public String getJsonString(boolean prettyPrint) throws JsonProcessingException {
		if (prettyPrint) {
			return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
		} else {
			return objectMapper.writeValueAsString(rootNode);
		}
	}
	
	public void writeToFile(String filePath, boolean prettyPrint) throws Exception {
		if (filePath == null || filePath.trim().isEmpty()) {
			throw new IllegalArgumentException("Output file path cannot be null or empty!");
		}
		File file = new File(filePath);
		File dir = file.getParentFile();
		if (dir.exists() == false) {
			dir.mkdirs();
		}
		if (dir.exists() == false) {
			throw new Exception("Cannot create output dir: " + dir.getAbsolutePath());
		}
		BufferedWriter br = null;
		try {
			br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
			br.write(getJsonString(prettyPrint));
			br.flush();
		} finally {
			if (br != null) {
				br.close();
			}
		}
	}

}