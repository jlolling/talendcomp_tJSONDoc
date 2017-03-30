package de.jlo.talendcomp.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class TestJsonComparator {
	
	@Test
	public void testIntersectArray() {
		System.out.println("#### testIntersectArray...");
		String array1 = "[10,{\"a\":33},30,40]";
		String array2 = "[11,{\"a\":33},31,40,\"x\"]";
		JsonDocument ad1 = new JsonDocument(array1);
		JsonDocument ad2 = new JsonDocument(array2);
		JsonComparator comp = new JsonComparator();
		JsonNode result = comp.intersect((ArrayNode) ad1.getRootNode(), (ArrayNode) ad2.getRootNode());
		int expected = 2;
		int actual = result.size();
		System.out.println(result);
		assertEquals(expected, actual);
	}

	@Test
	public void testDifferenceArray() {
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
	public void testContains() {
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
	
	@Test
	public void testDiffObjectsWithChangedAndAddedFields() throws Exception {
		System.out.println("#### testDiffObjectsWithChangedAndAddedFields ...");
		String nodeStr1 = "{\"a1\": 1, \"b\": \"x\", \"c\": 2}";
		String nodeStr2 = "{\"a1\": 1, \"b\": \"y\"}";
		JsonNode refNode = new JsonDocument(nodeStr1).getRootNode();
		JsonNode testNode = new JsonDocument(nodeStr2).getRootNode();
		JsonComparator comp = new JsonComparator();
		List<JsonComparator.Difference> result = comp.findDifferenceTo(null, refNode, testNode, null);
		for (JsonComparator.Difference diff : result) {
			System.out.println(diff);
		}
		assertEquals(2, result.size());
	}

	@Test
	public void testDiffObjectsWithChangedAndAddedFieldsInArrays() throws Exception {
		System.out.println("#### testDiffObjectsWithChangedAndAddedFieldsInArrays ...");
		String nodeStr1 = "[{\"a1\": 1, \"b\": \"x\", \"c\": 2},{\"a1\": 2, \"b\": \"x2\", \"c\": 22}]";
		String nodeStr2 = "[{\"a1\": 1, \"b\": \"y\"}]";
		JsonNode refNode = new JsonDocument(nodeStr1).getRootNode();
		JsonNode testNode = new JsonDocument(nodeStr2).getRootNode();
		JsonComparator comp = new JsonComparator();
		List<JsonComparator.Difference> result = comp.findDifferenceTo(null, refNode, testNode, null);
		for (JsonComparator.Difference diff : result) {
			System.out.println(diff);
		}
		assertEquals(3, result.size());
	}

	@Test
	public void testDiffArrays() throws Exception {
		System.out.println("#### testDiffObjectsWithChangedAndAddedFieldsInArrays ...");
		String nodeStr1 = "[1,2,3,4,6]";
		String nodeStr2 = "[1,2,4,5]";
		JsonNode refNode = new JsonDocument(nodeStr1).getRootNode();
		JsonNode testNode = new JsonDocument(nodeStr2).getRootNode();
		JsonComparator comp = new JsonComparator();
		List<JsonComparator.Difference> result = comp.findDifferenceTo(null, refNode, testNode, null);
		for (JsonComparator.Difference diff : result) {
			System.out.println(diff);
		}
		assertEquals(3, result.size());
	}

	@Test
	public void testDiffArraysInObjects() throws Exception {
		System.out.println("#### testDiffArraysInObjects ...");
		String nodeStr1 = "{\"a\" : [1,2,3,4,6]}";
		String nodeStr2 = "{\"a\" : [1,2,4,5]}";
		JsonNode refNode = new JsonDocument(nodeStr1).getRootNode();
		JsonNode testNode = new JsonDocument(nodeStr2).getRootNode();
		JsonComparator comp = new JsonComparator();
		List<JsonComparator.Difference> result = comp.findDifferenceTo(null, refNode, testNode, null);
		for (JsonComparator.Difference diff : result) {
			System.out.println(diff);
		}
		assertEquals(3, result.size());
	}

	@Test
	public void testDiffArraysInObjects2() throws Exception {
		System.out.println("#### testDiffArraysInObjects2 ...");
		String nodeStr1 = "{\"a\" : [1,2,3,{\"x\": {\"y\":\"text1\"}},6]}";
		String nodeStr2 = "{\"a\" : [1,2,4,{\"x\": {\"y\":\"text2\"}}]}";
		JsonNode refNode = new JsonDocument(nodeStr1).getRootNode();
		JsonNode testNode = new JsonDocument(nodeStr2).getRootNode();
		JsonComparator comp = new JsonComparator();
		List<JsonComparator.Difference> result = comp.findDifferenceTo(null, refNode, testNode, null);
		for (JsonComparator.Difference diff : result) {
			System.out.println(diff);
		}
		assertEquals(3, result.size());
	}

}
