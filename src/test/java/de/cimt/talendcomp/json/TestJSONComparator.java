package de.cimt.talendcomp.json;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class TestJSONComparator {
	
	@Test
	public void testIntersectArray() {
		System.out.println("testIntersectArray...");
		String array1 = "[10,{\"a\":33},30,40]";
		String array2 = "[11,{\"a\":33},31,40,\"x\"]";
		JsonDocument ad1 = new JsonDocument(array1);
		JsonDocument ad2 = new JsonDocument(array2);
		JSONComparator comp = new JSONComparator();
		JsonNode result = comp.intersect((ArrayNode) ad1.getRootNode(), (ArrayNode) ad2.getRootNode());
		int expected = 2;
		int actual = result.size();
		System.out.println(result);
		assertEquals(expected, actual);
	}

	@Test
	public void testDifferenceArray() {
		System.out.println("testDifferenceArray...");
		String array1 = "[10,{\"a\":33},30,40]";
		String array2 = "[11,{\"a\":33},31,40,\"x\"]";
		JsonDocument ad1 = new JsonDocument(array1);
		JsonDocument ad2 = new JsonDocument(array2);
		JSONComparator comp = new JSONComparator();
		JsonNode result = comp.difference((ArrayNode) ad1.getRootNode(), (ArrayNode) ad2.getRootNode());
		int expected = 5;
		int actual = result.size();
		System.out.println(result);
		assertEquals(expected, actual);
	}

}
