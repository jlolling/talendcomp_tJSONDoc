/**
 * Copyright 2023 Jan Lolling jan.lolling@gmail.com
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
package de.jlo.talendcomp.json.ops;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;

public class Traverse {
	
	private Set<String> excludeFieldSet = new HashSet<String>();
	private String[] pathArray = new String[0];
	private JSONValue dummy = new JSONValue();
	private int maxLevel = 0;
	
	public void addExcludeFields(String commaDelimitedFieldNames) {
		String[] fields = commaDelimitedFieldNames.split(",");
		for (String field : fields) {
			excludeFieldSet.add(field);
		}
	}
	
	public void setStartPath(String parentPath) {
		if (parentPath != null && parentPath.isEmpty() == false) {
			pathArray = parentPath.split("\\.");
		} else {
			pathArray = new String[0];
		}
	}
	
	/**
	 * traverse through a JSON object (binary representation) and extracts all fields
	 * @param node JsonNode object
	 * @param parentPath parent path in form of parent1.parent2 ...
	 * @return list of JSONValue objects containing path, key and value as strings
	 */
	public List<JSONValue> traverse(JsonNode node) {
		for (String parent : pathArray) {
			Object child = null;
			if (parent.equals("$")) {
				child = node;
			} else {
				child = node.get(parent);
			}
			if (child instanceof ObjectNode) {
				node = (ObjectNode) child;
			} else {
				break;
			}
		}
		return traverse(node, null, null);
	}
	
	/**
	 * traverse through a JSON object (binary representation) and extracts all fields
	 * @param node JsonNode object
	 * @param valueList
	 * @param keyPath
	 * @return list of JSONValue objects containing path, key and value as strings
	 */
	private List<JSONValue> traverse(JsonNode node, List<de.jlo.talendcomp.json.ops.JSONValue> valueList, List<String> keyPath) {
		if (keyPath == null) {
			keyPath = new ArrayList<String>();
			keyPath.add("$");
		}
		if (valueList == null) {
			valueList = new ArrayList<JSONValue>();
		}
		if (maxLevel == 0 || ((keyPath.size() - 1) < (maxLevel + 1))) {
			if (node instanceof ObjectNode) {
				Iterator<String> keys = node.fieldNames();
				while (keys.hasNext()) {
					List<String> childPath = clone(keyPath);
					String key = keys.next();
					if (excludeFieldSet.contains(key) == false) {
						JsonNode child = node.get(key);
						childPath.add(key);
						traverse(child, valueList, childPath);
					}
				}
			} else if (node instanceof ArrayNode) {
				ArrayNode arrayNode = (ArrayNode) node;
				int count = arrayNode.size();
				for (int i = 0; i < count; i++) {
					JsonNode child = arrayNode.get(i);
					List<String> childPath = clone(keyPath);
					childPath.add("[" + i + "]");
					traverse(child, valueList, childPath);
				}
			} else if (node instanceof ValueNode) {
				JSONValue value = new JSONValue();
				value.setValue(node);
			    value.setKeyPath(keyPath);
			    valueList.add(value);
			}
		}
		return valueList;
	}
	
	public static List<String> clone(List<String> list) {
		List<String> clone = new ArrayList<String>();
		for (String s : list) {
			clone.add(s);
		}
		return clone;
	}
	
	public JSONValue getDummyValue() { 
		return dummy;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(Integer maxLevel) {
		if (maxLevel != null) {
			this.maxLevel = maxLevel;
		} else {
			this.maxLevel = 0;
		}
	}

}
