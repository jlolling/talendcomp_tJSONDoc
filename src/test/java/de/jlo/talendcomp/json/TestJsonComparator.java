package de.jlo.talendcomp.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class TestJsonComparator {
	
	@Test
	public void testIntersectArray() throws Exception {
		System.out.println("#### testIntersectArray...");
		String array1 = "[10,{\"a\":33},30,40]";
		String array2 = "[11,{\"a\":33},31,\"x\",40]";
		ArrayNode an1 = (ArrayNode) JsonDocument.buildNode(array1);
		ArrayNode an2 = (ArrayNode) JsonDocument.buildNode(array2);
		JsonComparator comp = new JsonComparator();
		JsonNode result = comp.intersect(an1, an2);
		int expected = 2;
		int actual = result.size();
		System.out.println(result);
		assertEquals(expected, actual);
	}

	@Test
	public void testDifferenceArray() throws Exception {
		System.out.println("#### testDifferenceArray...");
		String array1 = "[10,{\"a\":33},30,40]";
		String array2 = "[11,{\"a\":33},31,40,\"x\"]";
		JsonDocument ad1 = new JsonDocument(array1);
		JsonDocument ad2 = new JsonDocument(array2);
		JsonComparator comp = new JsonComparator();
		JsonNode result = comp.difference((ArrayNode) ad1.getRootNode(), (ArrayNode) ad2.getRootNode());
		int expected = 5;
		int actual = result.size();
		System.out.println(result);
		assertEquals(expected, actual);
	}

	@Test
	public void testContains() throws Exception {
		System.out.println("#### testDifferenceArray (contains) ...");
		String value = "{\"a\":33}";
		String array = "[11,{\"a\":33},31,40,\"x\"]";
		JsonDocument vndoc = new JsonDocument(value);
		JsonDocument andoc = new JsonDocument(array);
		JsonComparator comp = new JsonComparator();
		boolean actual = comp.contains((ArrayNode) andoc.getRootNode(), vndoc.getRootNode());
		assertTrue(actual);
		System.out.println("***** testDifferenceArray (contains not)...");
		value = "{\"a\":34}";
		vndoc = new JsonDocument(value);
		actual = comp.contains((ArrayNode) andoc.getRootNode(), vndoc.getRootNode());
		assertTrue(actual == false);
	}
	
	@Test
	public void testContainsWithJsonPath() throws Exception {
		System.out.println("#### testContainsWithJsonPath (contains) ...");
		String value = "{\"a\":0, \"b\":{\"key\":2}}";
		String array = "[{\"a\":11, \"b\":{\"key\":1}},{\"a\":22, \"b\":{\"key\":2}},{\"a\":33, \"b\":{\"key\":3}},{\"a\":44, \"b\":{\"key\":4}},\"x\"]";
		JsonDocument vndoc = new JsonDocument(value);
		JsonDocument andoc = new JsonDocument(array);
		JsonComparator comp = new JsonComparator();
		boolean actual = comp.contains((ArrayNode) andoc.getRootNode(), vndoc.getRootNode(), "$.b.key");
		assertTrue(actual);
	}
	
}
