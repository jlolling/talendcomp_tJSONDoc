package de.cimt.talendcomp.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class JSONComparator {
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	public JSONComparator getInstance() {
		return new JSONComparator();
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
