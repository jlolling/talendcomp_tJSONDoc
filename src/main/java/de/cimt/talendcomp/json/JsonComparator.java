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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

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

}
