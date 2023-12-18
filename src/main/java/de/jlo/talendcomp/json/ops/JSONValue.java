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

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

public class JSONValue {
	
	private List<String> keyPath;
	private String attributeName = null;
	private Object value;
	private boolean isValueInArray = false;
	private int level = 0;
	private boolean isJsonNode = false;
	
	public String getKeyPath(String delimiter) {
		if (delimiter == null) {
			delimiter = ".";
		}
		if (keyPath != null) {
			StringBuilder sb = new StringBuilder();
			boolean firstLoop = true;
			for (String key : keyPath) {
				if (firstLoop) {
					firstLoop = false;
				} else {
					if (key.startsWith("[") == false) {
						sb.append(delimiter);
					}
				}
				sb.append(key);
			}
			return sb.toString();
		} else {
			return null;
		}
	}
	
	public void setKeyPath(List<String> keyPath) {
		this.keyPath = keyPath;
		level = keyPath.size() - 1;
		isValueInArray = keyPath.get(keyPath.size() - 1).endsWith("]");
		for (int i = level; i >= 0; i--) {
			String name = keyPath.get(i);
			if (name.startsWith("[") == false) {
				attributeName = name;
				break;
			}
		}
	}
	
	public String getJsonPath() {
		return getKeyPath(".");
	}
	
	public Object getValue() {
		return value;
	}
	
	public String getValueString() {
		if (value != null) {
			if (value instanceof String) {
				return (String) value;
			} else if (value instanceof JsonNode) {
				if (((JsonNode) value).isTextual()) {
					return ((JsonNode) value).textValue();
				} else {
					return ((JsonNode) value).toString();
				}
			} else {
				return value.toString();
			}
		} else {
			return null;
		}
	}
	
	public void setValue(Object value) {
		this.value = value;
		if (value instanceof JsonNode) {
			isJsonNode = true;
		}
	}

	@Override
	public String toString() {
		return getKeyPath(".") + "=" + getValueString() + " | attributeName=" + attributeName + " level=" + level + " isValueInArray=" + isValueInArray + " isJsonNode=" + isJsonNode;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof JSONValue) {
			JSONValue v = (JSONValue) o;
			if (keyPath.equals(v.keyPath) && value.equals(v.value)) {
				return true;
			}
		}
		return false;
	}

	public boolean isValueInArray() {
		return isValueInArray;
	}

	public int getLevel() {
		return level;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public boolean isJsonNode() {
		return isJsonNode;
	}

}
