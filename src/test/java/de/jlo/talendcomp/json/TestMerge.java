package de.jlo.talendcomp.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import de.jlo.talendcomp.json.ops.Merge;

public class TestMerge {
	
	private com.fasterxml.jackson.databind.JsonNode sourceRootNode = null;
	private com.fasterxml.jackson.databind.JsonNode targetRootNode = null;

	public void setUpObjectToObject() throws Exception {
		String fileResource = "/auto_assign_input_object_to_object.json";
		de.jlo.talendcomp.json.JsonDocument doc1 = JsonDocument.createByResource(fileResource);
		sourceRootNode = doc1.getRootNode();
		targetRootNode = sourceRootNode;
	}

	public void setUpObjectToArray() throws Exception {
		File testFile = new File("/var/testdata/json/auto_assign_input_object_to_array.json");
		de.jlo.talendcomp.json.JsonDocument doc1 = new de.jlo.talendcomp.json.JsonDocument(testFile);
		sourceRootNode = doc1.getRootNode();
		targetRootNode = sourceRootNode;
	}

	public void setUpObjectToArrayMissingSourceLoopPath() throws Exception {
		String fileResource = "/auto_assign_input_object_to_array_missing_source_loop_path.json";
		de.jlo.talendcomp.json.JsonDocument doc1 = JsonDocument.createByResource(fileResource);
		sourceRootNode = doc1.getRootNode();
		targetRootNode = sourceRootNode;
	}

	@Test
	public void testObjectToObject() throws Exception {
		setUpObjectToObject();
		Merge am = new Merge();
		am.setDebug(true);
		am.setDieIfSourceKeyNotExists(true);
		am.setDieIfTargetKeyNotExists(true);
		am.setSourceNode(sourceRootNode);
		am.setSourceLoopPath("$.products[*].rightsownerships[*]");
		am.setSourceIdentifier("rights_ownership_id");
		am.setTargetNode(targetRootNode);
		am.setTargetLoopPath("$.result.conflicts[*].conflict_parties[*]");
		am.setTargetIdentifier("rights_ownership_id");
		am.setTargetMountAttribute("rights_ownership_object", false, true);
		am.executeMerge();
		System.out.println("count source nodes: " + am.getCountSourceNodes());
		System.out.println("count target nodes: " + am.getCountTargetNodes());
		System.out.println("count assigned: " + am.getCountAssigned());
		JsonDocument result = new JsonDocument(targetRootNode);
		System.out.println(result.getJsonString(true, false));
		assertEquals(10, am.getCountAssigned());
		// check if the correct objects are mapped
		ArrayNode array = (ArrayNode) result.getNode("$.result.conflicts[*].conflict_parties[*]");
		for (JsonNode node : array) {
			long id1 = result.getValueAsLong(node, "rights_ownership_id", false, false, null);
			JsonNode child = node.get("rights_ownership_object");
			long id2 = result.getValueAsLong(child, "rights_ownership_id", false, false, null);
			if (id1 != id2) {
				assertTrue("In node: " + node + " a not matching object found!", true);
			}
		}
	}

	@Test
	public void testObjectToArray() throws Exception {
		setUpObjectToArray();
		Merge am = new Merge();
		am.setDebug(true);
		am.setDieIfSourceKeyNotExists(true);
		am.setDieIfTargetKeyNotExists(true);
		am.setSourceNode(sourceRootNode);
		am.setSourceLoopPath("$.products[*].rightsownerships[*]");
		am.setSourceIdentifier("rights_ownership_id");
		am.setTargetNode(targetRootNode);
		am.setTargetLoopPath("$.result.conflicts[*].conflict_parties");
		am.setTargetIdentifier("rights_ownership_ids");
		am.setTargetMountAttribute("rights_ownership_objects", true, true);
		am.executeMerge();
		System.out.println("count source nodes: " + am.getCountSourceNodes());
		System.out.println("count target nodes: " + am.getCountTargetNodes());
		System.out.println("count assigned: " + am.getCountAssigned());
		JsonDocument result = new JsonDocument(targetRootNode);
		System.out.println(result.getJsonString(true, false));
		assertEquals(6, am.getCountAssigned());
	}

	@Test
	public void testObjectToArrayMissingSourceLoopPath() throws Exception {
		setUpObjectToArrayMissingSourceLoopPath();
		Merge am = new Merge();
		am.setDebug(true);
		am.setDieIfSourceKeyNotExists(true);
		am.setDieIfTargetKeyNotExists(true);
		am.setSourceNode(sourceRootNode);
		am.setSourceLoopPath("$.products[*].rightsownerships[*]");
		am.setSourceIdentifier("rights_ownership_id");
		am.setTargetNode(targetRootNode);
		am.setTargetLoopPath("$.result.conflicts[*].conflict_parties");
		am.setTargetIdentifier("rights_ownership_ids");
		am.setTargetMountAttribute("rights_ownership_objects", true, true);
		am.setDieIfSourceLoopPathNotExists(true);
		try {
			am.executeMerge();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			assertTrue(true);
			return;
		}
		assertTrue(false);
	}
	
	@Test
	public void testMergeWithoutMountAttribute() throws Exception {
		String sourceJson = "[\n"
			    + "{\n"
			    + "    \"tu_id\" : 1,\n"
			    + "    \"featured_artists\" : null,\n"
			    + "    \"nonfeatured_artists\" : null,\n"
			    + "    \"all_collectors_excluding\" : null,\n"
			    + "    \"all_roles_excluding\" : null,\n"
			    + "    \"all_usages_excluding\" : false,\n"
			    + "    \"worldwide_excluding\" : false,\n"
			    + "    \"region_includes\" : [ {\n"
			    + "      \"region_include_id\" : 11,\n"
			    + "      \"sysname\" : \"country_1\"\n"
			    + "    }\n"
			    + "    ]\n"
			    + "},\n"
			    + "{\n"
			    + "    \"tu_id\" : 2,\n"
			    + "    \"featured_artists\" : null,\n"
			    + "    \"nonfeatured_artists\" : null,\n"
			    + "    \"all_collectors_excluding\" : null,\n"
			    + "    \"all_roles_excluding\" : null,\n"
			    + "    \"all_usages_excluding\" : false,\n"
			    + "    \"worldwide_excluding\" : false,\n"
			    + "    \"region_includes\" : [ {\n"
			    + "      \"region_include_id\" : 22,\n"
			    + "      \"sysname\" : \"country_2\"\n"
			    + "    }\n"
			    + "    ]\n"
			    + "}\n"
			    + "]";
		de.jlo.talendcomp.json.JsonDocument sourceDoc = new de.jlo.talendcomp.json.JsonDocument(sourceJson);
		String targetJson = "[ \n"
			    + "{\n"
			    + "  \"mandate_conflict_id\" : 1,\n"
			    + "  \"conflicted_mandate_id\" : 1000,\n"
			    + "  \"opponent_mandate_id\" : 1001,\n"
			    + "  \"opponent_collecting_society_id\" : 12345,\n"
			    + "  \"conflict_status_sysname\" : \"mandate_conflict_solved\",\n"
			    + "  \"detected_on\" : \"2018-06-15 08:09:08.971\",\n"
			    + "  \"timerange_from\" : null,\n"
			    + "  \"timerange_to\" : null,\n"
			    + "  \"tu\" : {\n"
			    + "    \"tu_id\" : 1\n"
			    + "  }\n"
			    + "}, \n"
			    + "{\n"
			    + "  \"mandate_conflict_id\" : 2,\n"
			    + "  \"conflicted_mandate_id\" : 1000,\n"
			    + "  \"opponent_mandate_id\" : 1003,\n"
			    + "  \"opponent_collecting_society_id\" : 12345,\n"
			    + "  \"conflict_status_sysname\" : \"mandate_conflict_existing\",\n"
			    + "  \"detected_on\" : \"2018-06-15 10:50:02.814\",\n"
			    + "  \"timerange_from\" : null,\n"
			    + "  \"timerange_to\" : null,\n"
			    + "  \"tu\" : {\n"
			    + "    \"tu_id\" : 1\n"
			    + "  }\n"
			    + "}, \n"
			    + "{\n"
			    + "  \"mandate_conflict_id\" : 3,\n"
			    + "  \"conflicted_mandate_id\" : 1000,\n"
			    + "  \"opponent_mandate_id\" : 1003,\n"
			    + "  \"opponent_collecting_society_id\" : 12345,\n"
			    + "  \"conflict_status_sysname\" : \"mandate_conflict_existing\",\n"
			    + "  \"detected_on\" : \"2018-06-15 10:50:02.814\",\n"
			    + "  \"timerange_from\" : null,\n"
			    + "  \"timerange_to\" : null,\n"
			    + "  \"tu\" : {\n"
			    + "    \"tu_id\" : 2\n"
			    + "  }\n"
			    + "}, \n"
			    + "{\n"
			    + "  \"mandate_conflict_id\" : 4,\n"
			    + "  \"conflicted_mandate_id\" : 1000,\n"
			    + "  \"opponent_mandate_id\" : 1003,\n"
			    + "  \"opponent_collecting_society_id\" : 12345,\n"
			    + "  \"conflict_status_sysname\" : \"mandate_conflict_existing\",\n"
			    + "  \"detected_on\" : \"2018-06-15 10:50:02.814\",\n"
			    + "  \"timerange_from\" : null,\n"
			    + "  \"timerange_to\" : null,\n"
			    + "  \"tu\" : {\n"
			    + "    \"tu_id\" : 2\n"
			    + "  }\n"
			    + "}\n"
			    + "]";
		de.jlo.talendcomp.json.JsonDocument targetDoc = new de.jlo.talendcomp.json.JsonDocument(targetJson);
		Merge am = new Merge();
		am.setDebug(false);
		am.setDieIfSourceKeyNotExists(true);
		am.setDieIfTargetKeyNotExists(true);
		am.setSourceNode(sourceDoc.getRootNode());
		am.setSourceLoopPath("$");
		am.setSourceIdentifier("tu_id");
		am.setTargetNode(targetDoc.getRootNode());
		am.setTargetLoopPath("$[*].tu");
		am.setTargetIdentifier("tu_id");
		am.setTargetMountAttribute(".", false, false);
		am.setDieIfSourceLoopPathNotExists(true);
		am.executeMerge();
		System.out.println(targetDoc.toString());
		String expectedJson = "[\n"
			    + "  {\n"
			    + "    \"mandate_conflict_id\": 1,\n"
			    + "    \"conflicted_mandate_id\": 1000,\n"
			    + "    \"opponent_mandate_id\": 1001,\n"
			    + "    \"opponent_collecting_society_id\": 12345,\n"
			    + "    \"conflict_status_sysname\": \"mandate_conflict_solved\",\n"
			    + "    \"detected_on\": \"2018-06-15 08:09:08.971\",\n"
			    + "    \"timerange_from\": null,\n"
			    + "    \"timerange_to\": null,\n"
			    + "    \"tu\": {\n"
			    + "      \"tu_id\": 1,\n"
			    + "      \"featured_artists\": null,\n"
			    + "      \"nonfeatured_artists\": null,\n"
			    + "      \"all_collectors_excluding\": null,\n"
			    + "      \"all_roles_excluding\": null,\n"
			    + "      \"all_usages_excluding\": false,\n"
			    + "      \"worldwide_excluding\": false,\n"
			    + "      \"region_includes\": [\n"
			    + "        {\n"
			    + "          \"region_include_id\": 11,\n"
			    + "          \"sysname\": \"country_1\"\n"
			    + "        }\n"
			    + "      ]\n"
			    + "    }\n"
			    + "  },\n"
			    + "  {\n"
			    + "    \"mandate_conflict_id\": 2,\n"
			    + "    \"conflicted_mandate_id\": 1000,\n"
			    + "    \"opponent_mandate_id\": 1003,\n"
			    + "    \"opponent_collecting_society_id\": 12345,\n"
			    + "    \"conflict_status_sysname\": \"mandate_conflict_existing\",\n"
			    + "    \"detected_on\": \"2018-06-15 10:50:02.814\",\n"
			    + "    \"timerange_from\": null,\n"
			    + "    \"timerange_to\": null,\n"
			    + "    \"tu\": {\n"
			    + "      \"tu_id\": 1,\n"
			    + "      \"featured_artists\": null,\n"
			    + "      \"nonfeatured_artists\": null,\n"
			    + "      \"all_collectors_excluding\": null,\n"
			    + "      \"all_roles_excluding\": null,\n"
			    + "      \"all_usages_excluding\": false,\n"
			    + "      \"worldwide_excluding\": false,\n"
			    + "      \"region_includes\": [\n"
			    + "        {\n"
			    + "          \"region_include_id\": 11,\n"
			    + "          \"sysname\": \"country_1\"\n"
			    + "        }\n"
			    + "      ]\n"
			    + "    }\n"
			    + "  },\n"
			    + "  {\n"
			    + "    \"mandate_conflict_id\": 3,\n"
			    + "    \"conflicted_mandate_id\": 1000,\n"
			    + "    \"opponent_mandate_id\": 1003,\n"
			    + "    \"opponent_collecting_society_id\": 12345,\n"
			    + "    \"conflict_status_sysname\": \"mandate_conflict_existing\",\n"
			    + "    \"detected_on\": \"2018-06-15 10:50:02.814\",\n"
			    + "    \"timerange_from\": null,\n"
			    + "    \"timerange_to\": null,\n"
			    + "    \"tu\": {\n"
			    + "      \"tu_id\": 2,\n"
			    + "      \"featured_artists\": null,\n"
			    + "      \"nonfeatured_artists\": null,\n"
			    + "      \"all_collectors_excluding\": null,\n"
			    + "      \"all_roles_excluding\": null,\n"
			    + "      \"all_usages_excluding\": false,\n"
			    + "      \"worldwide_excluding\": false,\n"
			    + "      \"region_includes\": [\n"
			    + "        {\n"
			    + "          \"region_include_id\": 22,\n"
			    + "          \"sysname\": \"country_2\"\n"
			    + "        }\n"
			    + "      ]\n"
			    + "    }\n"
			    + "  },\n"
			    + "  {\n"
			    + "    \"mandate_conflict_id\": 4,\n"
			    + "    \"conflicted_mandate_id\": 1000,\n"
			    + "    \"opponent_mandate_id\": 1003,\n"
			    + "    \"opponent_collecting_society_id\": 12345,\n"
			    + "    \"conflict_status_sysname\": \"mandate_conflict_existing\",\n"
			    + "    \"detected_on\": \"2018-06-15 10:50:02.814\",\n"
			    + "    \"timerange_from\": null,\n"
			    + "    \"timerange_to\": null,\n"
			    + "    \"tu\": {\n"
			    + "      \"tu_id\": 2,\n"
			    + "      \"featured_artists\": null,\n"
			    + "      \"nonfeatured_artists\": null,\n"
			    + "      \"all_collectors_excluding\": null,\n"
			    + "      \"all_roles_excluding\": null,\n"
			    + "      \"all_usages_excluding\": false,\n"
			    + "      \"worldwide_excluding\": false,\n"
			    + "      \"region_includes\": [\n"
			    + "        {\n"
			    + "          \"region_include_id\": 22,\n"
			    + "          \"sysname\": \"country_2\"\n"
			    + "        }\n"
			    + "      ]\n"
			    + "    }\n"
			    + "  }\n"
			    + "]";
		de.jlo.talendcomp.json.JsonDocument expectedDoc = new de.jlo.talendcomp.json.JsonDocument(expectedJson);
		assertEquals(expectedDoc.getRootNode(), targetDoc.getRootNode());
	}

}
