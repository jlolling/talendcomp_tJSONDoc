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
		JsonNode refNode = JsonDocument.createByJsonString(nodeStr1).getRootNode();
		JsonNode testNode = JsonDocument.createByJsonString(nodeStr2).getRootNode();
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
		JsonNode refNode = JsonDocument.createByJsonString(nodeStr1).getRootNode();
		JsonNode testNode = JsonDocument.createByJsonString(nodeStr2).getRootNode();
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
		JsonNode refNode = JsonDocument.createByJsonString(nodeStr1).getRootNode();
		JsonNode testNode = JsonDocument.createByJsonString(nodeStr2).getRootNode();
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
		JsonNode refNode = JsonDocument.createByJsonString(nodeStr1).getRootNode();
		JsonNode testNode = JsonDocument.createByJsonString(nodeStr2).getRootNode();
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
		JsonNode refNode = JsonDocument.createByJsonString(nodeStr1).getRootNode();
		JsonNode testNode = JsonDocument.createByJsonString(nodeStr2).getRootNode();
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
		JsonNode refNode = JsonDocument.createByJsonString(nodeStr1).getRootNode();
		JsonNode testNode = JsonDocument.createByJsonString(nodeStr2).getRootNode();
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
		JsonNode refNode = JsonDocument.createByJsonString(nodeStr1).getRootNode();
		JsonNode testNode = JsonDocument.createByJsonString(nodeStr2).getRootNode();
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
		JsonNode refNode = JsonDocument.createByJsonString(nodeStr1).getRootNode();
		JsonNode testNode = JsonDocument.createByJsonString(nodeStr2).getRootNode();
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
		JsonNode refNode = JsonDocument.createByJsonString(nodeStr1).getRootNode();
		JsonNode testNode = JsonDocument.createByJsonString(nodeStr2).getRootNode();
		Diff comp = new Diff();
		comp.setIgnoreArrayIndex(true);
		comp.setSortKeyAttribute("key");
		List<Diff.Difference> result = comp.findDifference(refNode, testNode);
		for (Diff.Difference diff : result) {
			System.out.println(diff);
		}
		assertEquals(2, result.size());
	}

	@Test
	public void testDiffComplex() throws Exception {
		System.out.println("#### testDiffArraysByKeys2 ...");
		String nodeStr1 = "{\n"
			    + "  \"merge_source_product_id\" : 1371066279,\n"
			    + "  \"products\" : [ {\n"
			    + "    \"product_id\" : 1,\n"
			    + "    \"source\" : \"navi\",\n"
			    + "    \"rightsownerships\" : [ {\n"
			    + "      \"rights_ownership_id\" : 11,\n"
			    + "      \"source\" : \"navi\",\n"
			    + "      \"process_status\" : \"rights_ownership_deactivated\",\n"
			    + "      \"tu_id\" : 1156,\n"
			    + "      \"valid_from\" : \"2016-01-01\",\n"
			    + "      \"valid_to\" : \"2999-01-01\",\n"
			    + "      \"region_include\" : [ 7391, 7393, 7394, 7395, 7396 ],\n"
			    + "      \"usage_include\" : [ 10406, 10435 ]\n"
			    + "    }, \n"
			    + "    {\n"
			    + "      \"rights_ownership_id\" : 12,\n"
			    + "      \"source\" : \"navi\",\n"
			    + "      \"process_status\" : \"rights_ownership_in_conflict\",\n"
			    + "      \"tu_id\" : 1156,\n"
			    + "      \"valid_from\" : \"2016-01-01\",\n"
			    + "      \"valid_to\" : \"2999-01-01\",\n"
			    + "      \"region_include\" : [1234],\n"
			    + "      \"usage_include\" : [ 10406, 10435 ]\n"
			    + "    },\n"
			    + "    {\n"
			    + "      \"rights_ownership_id\" : 14,\n"
			    + "      \"source\" : \"navi\",\n"
			    + "      \"process_status\" : \"rights_ownership_preliminarily_verified\",\n"
			    + "      \"tu_id\" : 1156,\n"
			    + "      \"valid_from\" : \"2015-01-01\",\n"
			    + "      \"valid_to\" : \"2015-01-02\",\n"
			    + "      \"region_include\" : [7393],\n"
			    + "      \"usage_include\" : [ 10406 ]\n"
			    + "    }, \n"
			    + "    {\n"
			    + "      \"rights_ownership_id\" : 15,\n"
			    + "      \"source\" : \"navi\",\n"
			    + "      \"process_status\" : \"rights_ownership_created\",\n"
			    + "      \"tu_id\" : 1156,\n"
			    + "      \"valid_from\" : \"2016-01-01\",\n"
			    + "      \"valid_to\" : \"2999-01-01\",\n"
			    + "      \"region_include\" : [ 7391, 7393, 7394, 7395, 7396 ],\n"
			    + "      \"usage_include\" : [ 10406, 10435 ]\n"
			    + "    } \n"
			    + "    ]\n"
			    + "  }, {\n"
			    + "    \"product_id\" : 2,\n"
			    + "    \"source\" : \"core\",\n"
			    + "    \"rightsownerships\" : [ {\n"
			    + "      \"rights_ownership_id\" : 21,\n"
			    + "      \"process_status\" : \"rights_ownership_created\",\n"
			    + "      \"source\" : \"core\",\n"
			    + "      \"tu_id\" : 1156,\n"
			    + "      \"valid_from\" : \"2016-01-01\",\n"
			    + "      \"valid_to\" : \"2999-01-01\",\n"
			    + "      \"region_include\" : [ 7391, 7393, 7394, 7395, 7396 ],\n"
			    + "      \"usage_include\" : [ 10406, 10435 ]\n"
			    + "    } ]\n"
			    + "  }, {\n"
			    + "    \"product_id\" : 3,\n"
			    + "    \"source\" : \"navi\",\n"
			    + "    \"rightsownerships\" : [ {\n"
			    + "      \"rights_ownership_id\" : 31,\n"
			    + "      \"process_status\" : \"rights_ownership_created\",\n"
			    + "      \"source\" : \"navi\",\n"
			    + "      \"tu_id\" : 1156,\n"
			    + "      \"valid_from\" : \"2016-01-01\",\n"
			    + "      \"valid_to\" : \"2999-01-01\",\n"
			    + "      \"region_include\" : [ 7391, 7393, 7394, 7395, 7396 ],\n"
			    + "      \"usage_include\" : [ 10406, 10435 ]\n"
			    + "    } ]\n"
			    + "  }, {\n"
			    + "    \"product_id\" : 4,\n"
			    + "    \"source\" : \"navi\",\n"
			    + "    \"rightsownerships\" : [ {\n"
			    + "      \"rights_ownership_id\" : 41,\n"
			    + "      \"process_status\" : \"rights_ownership_created\",\n"
			    + "      \"source\" : \"navi\",\n"
			    + "      \"tu_id\" : 1156,\n"
			    + "      \"valid_from\" : \"2011-01-01\",\n"
			    + "      \"valid_to\" : \"2011-01-01\",\n"
			    + "      \"region_include\" : [ 7391, 7393, 7394, 7395, 7396 ],\n"
			    + "      \"usage_include\" : [ 10406, 10435 ]\n"
			    + "    } ]\n"
			    + "  } ]\n"
			    + "}";
		String nodeStr2 = "{\n"
			    + "  \"merge_source_product_id\" : 137106627,\n"
			    + "  \"products\" : [ {\n"
			    + "    \"product_id\" : 1,\n"
			    + "    \"source\" : \"navi\",\n"
			    + "    \"rightsownerships\" : [ {\n"
			    + "      \"rights_ownership_id\" : 11,\n"
			    + "      \"source\" : \"navi\",\n"
			    + "      \"process_status\" : \"rights_ownership_deactivated\",\n"
			    + "      \"tu_id\" : 1156,\n"
			    + "      \"valid_from\" : \"2016-01-01\",\n"
			    + "      \"valid_to\" : \"2999-01-01\",\n"
			    + "      \"region_include\" : [ 7391, 7393, 7394, 7395, 7396 ],\n"
			    + "      \"usage_include\" : [ 10406, 10435 ]\n"
			    + "    }, \n"
			    + "    {\n"
			    + "      \"rights_ownership_id\" : 12,\n"
			    + "      \"source\" : \"navi\",\n"
			    + "      \"process_status\" : \"rights_ownership_in_conflict\",\n"
			    + "      \"tu_id\" : 1156,\n"
			    + "      \"valid_from\" : \"2016-01-01\",\n"
			    + "      \"valid_to\" : \"2999-01-01\",\n"
			    + "      \"region_include\" : [1234],\n"
			    + "      \"usage_include\" : [ 10406, 10435 ]\n"
			    + "    },\n"
			    + "    {\n"
			    + "      \"rights_ownership_id\" : 14,\n"
			    + "      \"source\" : \"navi\",\n"
			    + "      \"process_status\" : \"rights_ownership_preliminarily_verified\",\n"
			    + "      \"tu_id\" : 1156,\n"
			    + "      \"valid_from\" : \"2015-01-01\",\n"
			    + "      \"valid_to\" : \"2015-01-02\",\n"
			    + "      \"region_include\" : [7393],\n"
			    + "      \"usage_include\" : [ 10406 ]\n"
			    + "    }, \n"
			    + "    {\n"
			    + "      \"rights_ownership_id\" : 15,\n"
			    + "      \"source\" : \"navi\",\n"
			    + "      \"process_status\" : \"rights_ownership_created\",\n"
			    + "      \"tu_id\" : 1156,\n"
			    + "      \"valid_from\" : \"2016-01-01\",\n"
			    + "      \"valid_to\" : \"2999-01-01\",\n"
			    + "      \"region_include\" : [ 7391, 7393, 7394, 7395, 7396 ],\n"
			    + "      \"usage_include\" : [ 10406, 10435 ]\n"
			    + "    } \n"
			    + "    ]\n"
			    + "  }, {\n"
			    + "    \"product_id\" : 2,\n"
			    + "    \"source\" : \"core\",\n"
			    + "    \"rightsownerships\" : [ {\n"
			    + "      \"rights_ownership_id\" : 21,\n"
			    + "      \"process_status\" : \"rights_ownership_created\",\n"
			    + "      \"source\" : \"core\",\n"
			    + "      \"tu_id\" : 1156,\n"
			    + "      \"valid_from\" : \"2016-01-01\",\n"
			    + "      \"valid_to\" : \"2999-01-01\",\n"
			    + "      \"region_include\" : [ 7391, 7393, 7394, 7395, 7396 ],\n"
			    + "      \"usage_include\" : [ 10406, 10435 ]\n"
			    + "    } ]\n"
			    + "  }, {\n"
			    + "    \"product_id\" : 3,\n"
			    + "    \"source\" : \"navi\",\n"
			    + "    \"rightsownerships\" : [ {\n"
			    + "      \"rights_ownership_id\" : 31,\n"
			    + "      \"process_status\" : \"rights_ownership_created\",\n"
			    + "      \"source\" : \"navi\",\n"
			    + "      \"tu_id\" : 1156,\n"
			    + "      \"valid_from\" : \"2016-01-01\",\n"
			    + "      \"valid_to\" : \"2999-01-01\",\n"
			    + "      \"region_include\" : [ 7391, 7393, 7394, 7395, 7396 ],\n"
			    + "      \"usage_include\" : [ 10406, 10435 ]\n"
			    + "    } ]\n"
			    + "  }, {\n"
			    + "    \"product_id\" : 4,\n"
			    + "    \"source\" : \"navi\",\n"
			    + "    \"rightsownerships\" : [ {\n"
			    + "      \"rights_ownership_id\" : 41,\n"
			    + "      \"process_status\" : \"rights_ownership_created\",\n"
			    + "      \"source\" : \"navi\",\n"
			    + "      \"tu_id\" : 1156,\n"
			    + "      \"valid_from\" : \"2011-01-01\",\n"
			    + "      \"valid_to\" : \"2011-01-01\",\n"
			    + "      \"region_include\" : [ 7391, 7393, 7394, 7395, 7396 ],\n"
			    + "      \"usage_include\" : [ 10406, 10435 ]\n"
			    + "    } ]\n"
			    + "  } ]\n"
			    + "}";
		JsonNode refNode = JsonDocument.createByJsonString(nodeStr1).getRootNode();
		JsonNode testNode = JsonDocument.createByJsonString(nodeStr2).getRootNode();
		Diff comp = new Diff();
		comp.setIgnoreArrayIndex(true);
		comp.setTakeEmptyLikeNull(true);
		comp.setSortKeyAttribute(null);
		comp.setReferenceNode(refNode);
		comp.setRefJsonPath(null);
		comp.setTestNode(testNode);
		comp.setTestJsonPath(null);
		comp.executeDiff();
		List<Diff.Difference> result = comp.getResult();
		for (Diff.Difference diff : result) {
			System.out.println("JsonPath=" + diff.getJsonPath());
			System.out.println("Ref-Value=" + diff.getRefValue());
			System.out.println("Test-Value=" + diff.getTestValue());
		}
		assertEquals(1, result.size());
	}

}
