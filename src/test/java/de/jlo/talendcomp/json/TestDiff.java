package de.jlo.talendcomp.json;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import de.jlo.talendcomp.json.ops.Diff;

public class TestDiff {

	@Test
	public void testDiffObjectsWithChangedAndAddedFields() throws Exception {
		System.out.println("#### testDiffObjectsWithChangedAndAddedFields ...");
		String nodeStr1 = "{\"a1\": 1, \"b\": \"x\", \"c\": 2}";
		String nodeStr2 = "{\"a1\": 1, \"b\": \"y\"}";
		Diff comp = new Diff();
		List<Diff.Difference> result = comp.findDifference(nodeStr1, nodeStr2);
		for (Diff.Difference diff : result) {
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
		Diff comp = new Diff();
		List<Diff.Difference> result = comp.findDifference(refNode, testNode);
		for (Diff.Difference diff : result) {
			System.out.println(diff);
		}
		assertEquals(3, result.size());
	}

	@Test
	public void testDiffArrays() throws Exception {
		System.out.println("#### testDiffArrays ...");
		String nodeStr1 = "[1,2,3,4,6]";
		String nodeStr2 = "[1,2,4,5]";
		JsonNode refNode = new JsonDocument(nodeStr1).getRootNode();
		JsonNode testNode = new JsonDocument(nodeStr2).getRootNode();
		Diff comp = new Diff();
		List<Diff.Difference> result = comp.findDifference(refNode, testNode);
		for (Diff.Difference diff : result) {
			System.out.println(diff);
		}
		assertEquals(3, result.size());
	}

	@Test
	public void testDiffArraysIgnoreIndex() throws Exception {
		System.out.println("#### testDiffArraysIgnoreIndex ...");
		String nodeStr1 = "[1,2,3,4,5]";
		String nodeStr2 = "[1,2,5,3,4]";
		JsonNode refNode = new JsonDocument(nodeStr1).getRootNode();
		JsonNode testNode = new JsonDocument(nodeStr2).getRootNode();
		Diff comp = new Diff();
		comp.setIgnoreArrayIndex(true);
		List<Diff.Difference> result = comp.findDifference(refNode, testNode);
		for (Diff.Difference diff : result) {
			System.out.println(diff);
		}
		assertEquals(0, result.size());
	}

	@Test
	public void testDiffObjectsEmptyLikeNull() throws Exception {
		System.out.println("#### testDiffObjectsEmptyLikeNull ...");
		String nodeStr1 = "{\"a\" : [], \"b\" : null}";
		String nodeStr2 = "{\"a\" : null, \"b\" : {}}";
		JsonNode refNode = new JsonDocument(nodeStr1).getRootNode();
		JsonNode testNode = new JsonDocument(nodeStr2).getRootNode();
		Diff comp = new Diff();
		comp.setTakeEmptyLikeNull(true);
		List<Diff.Difference> result = comp.findDifference(refNode, testNode);
		for (Diff.Difference diff : result) {
			System.out.println(diff);
		}
		assertEquals(0, result.size());
	}

	@Test
	public void testDiffObjectsEmptyLikeNull2() throws Exception {
		System.out.println("#### testDiffObjectsEmptyLikeNull2 ...");
		String nodeStr1 = "{\"a\" : false, \"b\" : null, \"c\" : null, \"d\" : null}";
		String nodeStr2 = "{\"a\" : null, \"b\" : [], \"c\" : null}";
		Diff comp = new Diff();
		comp.setReferenceNode(nodeStr1);
		comp.setTestNode(nodeStr2);
		comp.setTakeEmptyLikeNull(true);
		comp.executeDiff();
		List<Diff.Difference> result = comp.getResult();
		for (Diff.Difference diff : result) {
			System.out.println(diff);
		}
		assertEquals(1, result.size());
	}

	@Test
	public void testDiffArraysInObjects() throws Exception {
		System.out.println("#### testDiffArraysInObjects ...");
		String nodeStr1 = "{\"a\" : [1,2,3,4,6]}";
		String nodeStr2 = "{\"a\" : [1,2,4,5]}";
		JsonNode refNode = new JsonDocument(nodeStr1).getRootNode();
		JsonNode testNode = new JsonDocument(nodeStr2).getRootNode();
		Diff comp = new Diff();
		List<Diff.Difference> result = comp.findDifference(refNode, testNode);
		for (Diff.Difference diff : result) {
			System.out.println(diff);
		}
		assertEquals(3, result.size());
	}

	@Test
	public void testDiffArraysInObjectsApiCreated() throws Exception {
		System.out.println("#### testDiffArraysInObjectsApiCreated ...");
		String nodeStr1 = "{\"a\" : [1,2,3,4,6]}";
		String nodeStr2 = "{\"a\" : []}";
		JsonNode refNode = new JsonDocument(nodeStr1).getRootNode();
		JsonNode testNode = new JsonDocument(nodeStr2).getRootNode();
		ArrayNode arrayNode = (ArrayNode) testNode.withArray("a");
		arrayNode.add(new BigDecimal("1"));
		arrayNode.add(Long.valueOf("2"));
		arrayNode.add(4l);
		arrayNode.add(5l);
		Diff comp = new Diff();
		List<Diff.Difference> result = comp.findDifference(refNode, testNode);
		for (Diff.Difference diff : result) {
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
		Diff comp = new Diff();
		List<Diff.Difference> result = comp.findDifference(refNode, testNode);
		for (Diff.Difference diff : result) {
			System.out.println(diff);
		}
		assertEquals(3, result.size());
	}

	@Test
	public void testDiffArraysByKeys() throws Exception {
		System.out.println("#### testDiffArraysByKeys ...");
		String nodeStr1 = "[{\"key\":1},{\"key\":2},{\"key\":3},{\"key\":4}]";
		String nodeStr2 = "[{\"key\":1},{\"key\":3},{\"key\":2},{\"key\":4}]";
		JsonNode refNode = new JsonDocument(nodeStr1).getRootNode();
		JsonNode testNode = new JsonDocument(nodeStr2).getRootNode();
		Diff comp = new Diff();
		comp.setIgnoreArrayIndex(true);
		comp.setSortKeyAttribute("key");
		List<Diff.Difference> result = comp.findDifference(refNode, testNode);
		for (Diff.Difference diff : result) {
			System.out.println(diff);
		}
		assertEquals(0, result.size());
	}

	@Test
	public void testDiffArraysByKeys2() throws Exception {
		System.out.println("#### testDiffArraysByKeys2 ...");
		String nodeStr1 = "[{\"key\":1,\"a\":10,\"b\":\"x\"},{\"key\":2,\"a\":11,\"b\":\"y\"},{\"key\":3,\"a\":12},{\"key\":4,\"a\":13}]";
		String nodeStr2 = "[{\"key\":2,\"a\":21,\"b\":\"z\"},{\"key\":1,\"a\":10,\"b\":\"x\"},{\"key\":4,\"a\":13},{\"key\":3,\"a\":12}]";
		JsonNode refNode = new JsonDocument(nodeStr1).getRootNode();
		JsonNode testNode = new JsonDocument(nodeStr2).getRootNode();
		Diff comp = new Diff();
		comp.setIgnoreArrayIndex(true);
		comp.setSortKeyAttribute("key");
		List<Diff.Difference> result = comp.findDifference(refNode, testNode);
		for (Diff.Difference diff : result) {
			System.out.println(diff);
		}
		assertEquals(2, result.size());
	}

}
