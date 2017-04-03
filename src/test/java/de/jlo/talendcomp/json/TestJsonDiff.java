package de.jlo.talendcomp.json;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;

public class TestJsonDiff {

	@Test
	public void testDiffObjectsWithChangedAndAddedFields() throws Exception {
		System.out.println("#### testDiffObjectsWithChangedAndAddedFields ...");
		String nodeStr1 = "{\"a1\": 1, \"b\": \"x\", \"c\": 2}";
		String nodeStr2 = "{\"a1\": 1, \"b\": \"y\"}";
		JsonNode refNode = new JsonDocument(nodeStr1).getRootNode();
		JsonNode testNode = new JsonDocument(nodeStr2).getRootNode();
		JsonDiff comp = new JsonDiff();
		List<JsonDiff.Difference> result = comp.findDifference(refNode, testNode);
		for (JsonDiff.Difference diff : result) {
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
		JsonDiff comp = new JsonDiff();
		List<JsonDiff.Difference> result = comp.findDifference(refNode, testNode);
		for (JsonDiff.Difference diff : result) {
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
		JsonDiff comp = new JsonDiff();
		List<JsonDiff.Difference> result = comp.findDifference(refNode, testNode);
		for (JsonDiff.Difference diff : result) {
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
		JsonDiff comp = new JsonDiff();
		List<JsonDiff.Difference> result = comp.findDifference(refNode, testNode);
		for (JsonDiff.Difference diff : result) {
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
		JsonDiff comp = new JsonDiff();
		List<JsonDiff.Difference> result = comp.findDifference(refNode, testNode);
		for (JsonDiff.Difference diff : result) {
			System.out.println(diff);
		}
		assertEquals(3, result.size());
	}

	
}
