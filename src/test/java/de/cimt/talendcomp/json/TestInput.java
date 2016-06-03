package de.cimt.talendcomp.json;

import de.cimt.talendcomp.test.TalendFakeJob;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

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
			String filePath = "/Volumes/Data/Talend/testdata/json/test_1.json";
			java.io.File jsonFile = null;
			if (filePath != null && filePath.trim().isEmpty() == false) {
				jsonFile = new java.io.File(filePath);
			}
			de.cimt.talendcomp.json.JsonDocument tJSONDocOpen_1 = new de.cimt.talendcomp.json.JsonDocument(
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
		de.cimt.talendcomp.json.JsonDocument tJSONDocInput_1 = (de.cimt.talendcomp.json.JsonDocument) globalMap
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

		de.cimt.talendcomp.json.JsonDocument tJSONDocInput_1 = (de.cimt.talendcomp.json.JsonDocument) globalMap
				.get("tJSONDocOpen_1");
		if (tJSONDocInput_1 != null) {
			try {
				tJSONDocInput_1 = (de.cimt.talendcomp.json.JsonDocument) globalMap
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
						currentNode, "integer-value", true, null);
				
				System.out.println(row1.int_val);
				
				row1.string_val = tJSONDocInput_1.getValueAsString(
						currentNode, "string_val", true, null);
				row1.date_val = tJSONDocInput_1.getValueAsDate(
						currentNode, "date_val", true, null,
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
	}

}
