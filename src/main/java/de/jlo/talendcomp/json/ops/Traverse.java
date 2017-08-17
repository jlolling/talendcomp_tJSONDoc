package de.jlo.talendcomp.json.ops;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Traverse {
	private Set<String> excludeFieldSet = new HashSet<String>();
	private String[] pathArray = new String[0];
	private JSONValue dummy = new JSONValue();
	private boolean ignoreNullValues = true;
	
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
		}
		if (valueList == null) {
			valueList = new ArrayList<JSONValue>();
		}
		Iterator<String> keys = node.fieldNames();
		Object child = null;
		while (keys.hasNext()) {
			String key = keys.next();
			if (excludeFieldSet.contains(key) == false) {
				child = node.get(key);
				if (child instanceof ObjectNode) {
					List<String> childPath = clone(keyPath);
					childPath.add(key);
					traverse((ObjectNode) child, valueList, childPath);
				} else {
					if (child != null || ignoreNullValues == false) {
						JSONValue value = new JSONValue();
						value.setKey(key);
						value.setValue(child);
					    value.setKeyPath(keyPath);
					    valueList.add(value);
					}
				}
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

	public void setIgnoreNullValues(boolean ignoreNullValues) {
		this.ignoreNullValues = ignoreNullValues;
	}

}
