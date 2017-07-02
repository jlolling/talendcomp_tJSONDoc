package de.jlo.talendcomp.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;

import de.cimt.talendcomp.test.TalendFakeJob;

public class TestInput extends TalendFakeJob {
	
	public static class row1Struct {

		public Integer int_val;

		public String string_val;

		public java.util.Date date_val;

	}

	@Before
	public void setupDocument() {
		System.out.println("Setup document...");
		try {
			String filePath = "/Data/Talend/testdata/json/test_1.json";
			java.io.File jsonFile = null;
			if (filePath != null && filePath.trim().isEmpty() == false) {
				jsonFile = new java.io.File(filePath);
			}
			de.jlo.talendcomp.json.JsonDocument tJSONDocOpen_1 = new de.jlo.talendcomp.json.JsonDocument(
					jsonFile);
			globalMap.put("tJSONDocOpen_1", tJSONDocOpen_1);
			globalMap.put("tJSONDocOpen_1_CURRENT_NODE",
					tJSONDocOpen_1.getRootNode());
			globalMap.put("tJSONDocOpen_1_ROOT_NODE",
					tJSONDocOpen_1.getRootNode());
		} catch (Exception e) {
			globalMap.put("tJSONDocOpen_1_ERROR_MESSAGE",
					e.getMessage());
			assertTrue("Failed: " + e.getMessage(), false);
		}
		
	}
	
	@Test
	public void testJsonPath() throws Exception {
		System.out.println("testJsonPath...");
		de.jlo.talendcomp.json.JsonDocument tJSONDocInput_1 = (de.jlo.talendcomp.json.JsonDocument) globalMap
				.get("tJSONDocOpen_1");
		com.fasterxml.jackson.databind.JsonNode node = tJSONDocInput_1
				.getNode("$.test.object[*].demo[*]");
		System.out.println(node);
		assertNotNull(node);
	}
	
	@Test
	public void test_tJSONDocInput() throws Exception {
		System.out.println("test_tJSONDocInput...");
		row1Struct row1 = null;
		row1 = new row1Struct();

		de.jlo.talendcomp.json.JsonDocument tJSONDocInput_1 = (de.jlo.talendcomp.json.JsonDocument) globalMap
				.get("tJSONDocOpen_1");
		if (tJSONDocInput_1 != null) {
			try {
				tJSONDocInput_1 = (de.jlo.talendcomp.json.JsonDocument) globalMap
						.get("tJSONDocOpen_1");
				globalMap.put("tJSONDocInput_1", tJSONDocInput_1);
				// get the parent object we have to dock on
				com.fasterxml.jackson.databind.JsonNode entryNode_tJSONDocInput_1 = (com.fasterxml.jackson.databind.JsonNode) globalMap
						.get("tJSONDocOpen_1_CURRENT_NODE");
				String jsonPath = "$.test.object[*].demo[*]";
				// take care the path will be created as array
				com.fasterxml.jackson.databind.JsonNode node = tJSONDocInput_1
						.getNode(entryNode_tJSONDocInput_1, jsonPath,
								false);
				com.fasterxml.jackson.databind.node.ArrayNode parentNode = null;
				if (node instanceof com.fasterxml.jackson.databind.node.ArrayNode) {
					parentNode = (com.fasterxml.jackson.databind.node.ArrayNode) node;
				} else if (node instanceof com.fasterxml.jackson.databind.node.ObjectNode) {
					throw new Exception(
							"Actually an ArrayNode is expected, but there is already an ObjectNode at the path: "
									+ jsonPath
									+ ". Check the configuration.");
				}
				if (parentNode == null) {
					throw new Exception("JSONPath: " + jsonPath
							+ " does not exists!");
				}
				globalMap
						.put("parent_node_tJSONDocInput_1", parentNode);
				globalMap.put("tJSONDocInput_1_NB_LINE", 0);
			} catch (Exception e) {
				globalMap.put("tJSONDocInput_1_ERROR_MESSAGE",
						e.getMessage());
				throw e;
			}
		} else {
			throw new Exception(
					"No JSON document received from the parent component: tJSONDocOpen_1");
		}
		com.fasterxml.jackson.databind.node.ArrayNode parentNode_tJSONDocInput_1 = null;
		try { // start block for tJSONDocInput_1
				// get the parent node
			com.fasterxml.jackson.databind.JsonNode node = (com.fasterxml.jackson.databind.JsonNode) globalMap
					.get("parent_node_tJSONDocInput_1");
			if (node instanceof com.fasterxml.jackson.databind.node.ArrayNode) {
				parentNode_tJSONDocInput_1 = (com.fasterxml.jackson.databind.node.ArrayNode) node;
			} else if (node instanceof com.fasterxml.jackson.databind.node.ObjectNode) {
				throw new Exception(
						"Actually as parent an ArrayNode is expected, but there is already an ObjectNode from the component: "
								+ tJSONDocInput_1
								+ ". Check the configuration.");
			}
		} catch (Exception e) {
			globalMap.put("tJSONDocInput_1_ERROR_MESSAGE",
					e.getMessage());
			throw e;
		}
		java.util.List<com.fasterxml.jackson.databind.JsonNode> listNodes_tJSONDocInput_1 = tJSONDocInput_1
				.getArrayValuesAsList(parentNode_tJSONDocInput_1);
		for (com.fasterxml.jackson.databind.JsonNode currentNode : listNodes_tJSONDocInput_1) {
			// loop over the given children of the parent node or the
			// values of the array
			
			try {
				row1.int_val = tJSONDocInput_1.getValueAsInteger(
						currentNode, "integer-value", true, false, null);
				
				System.out.println(row1.int_val);
				
				row1.string_val = tJSONDocInput_1.getValueAsString(
						currentNode, "string_val", true, false, null);
				row1.date_val = tJSONDocInput_1.getValueAsDate(
						currentNode, "date_val", true, false, null,
						"dd-MM-yyyy");
				globalMap.put("tJSONDocInput_1_CURRENT_NODE",
						currentNode);
				Integer counter = (Integer) globalMap
						.get("tJSONDocInput_1_NB_LINE");
				globalMap.put("tJSONDocInput_1_NB_LINE",
						counter.intValue() + 1);
			} catch (Exception e) {
				globalMap.put("tJSONDocInput_1_ERROR_MESSAGE",
						e.getMessage());
				throw e;
			}
		}
		assertTrue(true);
	}

	@Test
	public void testCheckNullNode() throws Exception {
		String json = "{\"a1\" : \"v1\", \"nullAttr\" : null}";
		JsonDocument doc = new JsonDocument(json);
		JsonNode parent = doc.getNode("$");
		String jsonResult = doc.getJsonString(false, true);
		System.out.println(jsonResult);
		boolean checkNullWorked = false;
		try {
			doc.getValueAsString(parent, "nullAttr", false, false, null);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			checkNullWorked = true;
		}
		assertTrue("Null check does not work", checkNullWorked);
	}

	@Test
	public void testReadJsonPath() throws Exception {
		String json = "{\"a\" : \"v1\", \"b\" : {\"c\": \"x\"}}";
		JsonDocument doc = new JsonDocument(json);
		JsonNode parent = doc.getNode("$");
		String jsonResult = doc.getJsonString(false, true);
		System.out.println(jsonResult);
		String actual = null;
		String expected = "x";
		actual = doc.getValueAsString(parent, "b.c", false, false, null);
		assertEquals("Not correct return value", actual, expected);
	}

	@Test
	public void testReadJsonPathWithAbsolutBackRef() throws Exception {
		String json = "{\"a\" : \"v1\", \"b\" : {\"c\": \"x\"}}";
		JsonDocument doc = new JsonDocument(json);
		JsonNode parent = doc.getNode("$.b");
		String jsonResult = doc.getJsonString(parent, false, true);
		System.out.println(jsonResult);
		String actual = null;
		String expected = "v1";
		actual = doc.getValueAsString(parent, "$.a", false, false, null);
		assertEquals("Not correct return value", actual, expected);
	}

	@Test
	public void testReadJsonPathUseMissing() throws Exception {
		String json = "{\"a\" : \"v1\", \"b\" : {\"c\": \"x\"}}";
		JsonDocument doc = new JsonDocument(json);
		JsonNode parent = doc.getNode("$");
		String jsonResult = doc.getJsonString(false, true);
		System.out.println(jsonResult);
		String expected = "replacement";
		String actual = doc.getValueAsString(parent, "b.missing", false, true, "replacement");
		assertEquals("Not correct return value", expected, actual);
	}

	@Test
	public void testCheckMultiLineText() throws Exception {
		String expected = "line1\"line2\\nline3 \\ \\\" ";
		System.out.println("expected=" + expected);
		String value = JsonDocument.escape(expected);
		System.out.println("escaped value=" + value);
		String json = "{\"multiline\" : \"" + value + "\"}";
		JsonDocument doc = new JsonDocument(json);
		JsonNode parent = doc.getNode("$");
		String jsonResult = doc.getJsonString(false, true);
		System.out.println(jsonResult);
		String actual = doc.getValueAsString(parent, "multiline", false, false, null);
		System.out.println("actual=" + actual);
		assertEquals(expected, actual);
	}

	@Test
	public void testCheckMissingNode() throws Exception {
		String json = "{\"a1\" : \"v1\", \"a2\" : null}";
		JsonDocument doc = new JsonDocument(json);
		JsonNode parent = doc.getNode("$");
		String jsonResult = doc.getJsonString(false, true);
		System.out.println(jsonResult);
		String missingAttrString = doc.getValueAsString(parent, "missingAttr", false, true, "xxx");
		System.out.println("missingAttr replacement: " + missingAttrString);
		assertEquals("Missing node value failed", "xxx", missingAttrString);
		boolean checkMissingWorked = false;
		try {
			doc.getValueAsString(parent, "missingAttr", true, false, null);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			checkMissingWorked = true;
		}
		assertTrue("Missing check does not work", checkMissingWorked);
		checkMissingWorked = false;
		try {
			doc.getValueAsString(parent, "missingAttr", false, true, null);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			checkMissingWorked = true;
		}
		assertTrue("Missing check: not null check for replacement does not work", checkMissingWorked);
	}
		
	@Test
	public void testGetNodeNullSave() throws Exception {
		String json = "{\n"
			    + "	\"attr1\" : null,\n"
			    + "	\"attr2\" : [1,2,3,4]\n"
			    + "}";
		JsonDocument doc = new JsonDocument(json);
		JsonNode parent = doc.getNode(doc.getRootNode(), "nix.dummy", false);
		if (parent == null) {
			assertTrue(true);
		}
	}

	@Test
	public void testReturnArrayAsString() throws Exception {
		String json = "{\n"
			    + "	\"attr1\" : \"test\",\n"
			    + "	\"attr_array\" : [1,2,3,4]\n"
			    + "}";
		JsonDocument doc = new JsonDocument(json);
		JsonNode parent = doc.getNode(doc.getRootNode(), "$", false);
		List<JsonNode> result = doc.getArrayValuesAsList(parent, false, true);
		for (JsonNode node : result) {
			System.out.println(node);
			String expected = "[1,2,3,4]";
			String actual = doc.getValueAsString(node, "attr_array", true, true, null);
			System.out.println("attr_array=" + actual);
			assertEquals(expected, actual);
		}
		assertTrue(true);
	}

	@Test
	public void testReturnDateAsString() throws Exception {
		String json = "{\n"
			    + "	\"attr_date\" : \"Mar 01, 2017 11:12:58 PM\"\n"
			    + "}";
		JsonDocument doc = new JsonDocument(json);
		JsonNode parent = doc.getNode(doc.getRootNode(), "$", false);
		List<JsonNode> result = doc.getArrayValuesAsList(parent, false, true);
		for (JsonNode node : result) {
			System.out.println(node);
			String expectedStr = "2017-03-01 23:12:58";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date expected = sdf.parse(expectedStr);
			Date actual = doc.getValueAsDate(node, "attr_date", true, true, null, null);
			System.out.println("attr_date=" + actual);
			assertEquals(expected, actual);
		}
		assertTrue(true);
	}

}
