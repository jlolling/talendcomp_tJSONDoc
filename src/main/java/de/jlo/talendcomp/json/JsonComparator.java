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
package de.jlo.talendcomp.json;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;

public class JsonComparator {
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	public JsonComparator getInstance() {
		return new JsonComparator();
	}

	/**
	 * Collects the values for the both arrays have in common 
	 * @param array1
	 * @param array2
	 * @return an array which contains all values both arrays have in common
	 */
	public ArrayNode intersect(ArrayNode array1, ArrayNode array2) {
		ArrayNode result = objectMapper.createArrayNode();
		for (int i1 = 0, n1 = array1.size(); i1 < n1; i1++) {
			JsonNode node1 = array1.get(i1);
			for (int i2 = 0, n2 = array2.size(); i2 < n2; i2++) {
				JsonNode node2 = array2.get(i2);
				if (node1.equals(node2)) {
					result.add(node2);
				}
			}
		}
		return result;
	}

	/**
	 * Checks if the array contains the given value
	 * @param array the array which perhaps contains the value
	 * @param value the value to search for
	 * @return true or false
	 */
	public boolean contains(ArrayNode array, JsonNode value) {
		for (int i = 0, n = array.size(); i < n; i++) {
			JsonNode node = array.get(i);
			if (node.equals(value)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the array contains the given value
	 * @param array the array which perhaps contains the value
	 * @param value the value to search for
	 * @return true or false
	 * @throws Exception 
	 */
	public boolean contains(ArrayNode array, JsonNode value, String jsonPath) throws Exception {
		if (jsonPath == null || jsonPath.trim().isEmpty()) {
			return contains(array, value);
		} else {
			JsonDocument doc = new JsonDocument(array);
			for (int i = 0, n = array.size(); i < n; i++) {
				JsonNode nodeInArray = array.get(i);
				JsonNode child1 = doc.getNode(nodeInArray, jsonPath);
				JsonNode child2 = doc.getNode(value, jsonPath);
				if (child1.equals(child2)) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * Collects the differences between the both arrays 
	 * @param array1
	 * @param array2
	 * @return an array which contains the difference between both arrays
	 */
	public ArrayNode difference(ArrayNode array1, ArrayNode array2) {
		ArrayNode result = objectMapper.createArrayNode();
		for (int i1 = 0, n1 = array1.size(); i1 < n1; i1++) {
			JsonNode node1 = array1.get(i1);
			boolean found = false;
			for (int i2 = 0, n2 = array2.size(); i2 < n2; i2++) {
				JsonNode node2 = array2.get(i2);
				if (node1.equals(node2)) {
					found = true;
					break;
				}
			}
			if (found == false) {
				result.add(node1);
			}
		}
		// exchange the arrays
		ArrayNode x = array1;
		array1 = array2;
		array2 = x;
		for (int i1 = 0, n1 = array1.size(); i1 < n1; i1++) {
			JsonNode node1 = array1.get(i1);
			boolean found = false;
			for (int i2 = 0, n2 = array2.size(); i2 < n2; i2++) {
				JsonNode node2 = array2.get(i2);
				if (node1.equals(node2)) {
					found = true;
					break;
				}
			}
			if (found == false) {
				result.add(node1);
			}
		}
		return result;
	}

	public static class Difference {
		
		private String jsonPath = null;
		private JsonNode refValue = null;
		private JsonNode testValue = null;
		private boolean typeMismatch = false;
		
		public String getJsonPath() {
			return jsonPath;
		}
		public void setJsonPath(String jsonPath) {
			this.jsonPath = jsonPath;
		}
		public JsonNode getRefValue() {
			return refValue;
		}
		public void setRefValue(JsonNode value) {
			this.refValue = value;
		}
		public JsonNode getTestValue() {
			return testValue;
		}
		public void setTestValue(JsonNode value) {
			this.testValue = value;
		}
		public boolean isTypeMismatch() {
			return typeMismatch;
		}
		public void setTypeMismatch(boolean typeMismatch) {
			this.typeMismatch = typeMismatch;
		}
		
		@Override
		public String toString() {
			return jsonPath + ": ref=" + refValue + ", test=" + testValue;
		}
		
	}
	
	public List<Difference> findDifferenceTo(String parentPath, JsonNode reference, JsonNode test, List<Difference> listDiffs) {
		if (parentPath == null) {
			parentPath = "$";
		}
		if (listDiffs == null) {
			listDiffs = new ArrayList<JsonComparator.Difference>();
		}
		if (reference instanceof ObjectNode) {
			if (test instanceof ObjectNode) {
				ObjectNode rn = (ObjectNode) reference;
				ObjectNode tn = (ObjectNode) test;
				Iterator<Map.Entry<String, JsonNode>> fi = rn.fields(); 
				while (fi.hasNext()) {
					Map.Entry<String, JsonNode> entry = fi.next();
					// check if field exists in test node
					if (tn.has(entry.getKey())) {
						JsonNode tnValue = tn.get(entry.getKey());
						if (entry.getValue() != null && tnValue != null) {
							findDifferenceTo(parentPath + "." + entry.getKey(), entry.getValue(), tnValue, listDiffs);
						} else if (entry.getValue() == null && tnValue != null) {
							Difference diff = new Difference();
							diff.setJsonPath(parentPath + "." + entry.getKey());
							diff.setRefValue(NullNode.getInstance());
							diff.setTestValue(tnValue);
							listDiffs.add(diff);
						} else if (entry.getValue() != null && tnValue == null) {
							Difference diff = new Difference();
							diff.setJsonPath(parentPath + "." + entry.getKey());
							diff.setRefValue(entry.getValue());
							diff.setTestValue(NullNode.getInstance());
							listDiffs.add(diff);
						}
					} else {
						Difference diff = new Difference();
						diff.setJsonPath(parentPath + "." + entry.getKey());
						diff.setRefValue(entry.getValue());
						listDiffs.add(diff);
					}
				}
			} else {
				Difference diff = new Difference();
				diff.setJsonPath(parentPath);
				diff.setTypeMismatch(true);
				diff.setRefValue(reference);
				diff.setTestValue(test);
				listDiffs.add(diff);
			}
		} else if (reference instanceof ArrayNode) {
			if (test instanceof ArrayNode) {
				ArrayNode rn = (ArrayNode) reference;
				ArrayNode tn = (ArrayNode) test;
				for (int i = 0; i < rn.size(); i++) {
					String newParentPath = parentPath + "[" + i + "]";
					JsonNode rvn = rn.get(i);
					if (i < tn.size()) {
						JsonNode tvn = tn.get(i);
						findDifferenceTo(newParentPath, rvn, tvn, listDiffs);
					} else {
						Difference diff = new Difference();
						diff.setJsonPath(newParentPath);
						diff.setRefValue(rvn);
						diff.setTestValue(NullNode.getInstance());
						listDiffs.add(diff);
					}
				}
			} else {
				Difference diff = new Difference();
				diff.setJsonPath(parentPath);
				diff.setTypeMismatch(true);
				diff.setRefValue(reference);
				diff.setTestValue(test);
				listDiffs.add(diff);
			}
		} else if (reference instanceof ValueNode) {
			if (test instanceof ValueNode) {
				ValueNode rv = (ValueNode) reference;
				ValueNode tv = (ValueNode) test;
				if (tv.equals(rv) == false) {
					Difference diff = new Difference();
					diff.setJsonPath(parentPath);
					diff.setRefValue(rv);
					diff.setTestValue(tv);
					listDiffs.add(diff);
				}
			} else {
				Difference diff = new Difference();
				diff.setJsonPath(parentPath);
				diff.setTypeMismatch(true);
				diff.setRefValue(reference);
				diff.setTestValue(test);
				listDiffs.add(diff);
			}
		}
		return listDiffs;
	}
	
}
