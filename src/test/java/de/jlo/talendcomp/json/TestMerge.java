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
		File testFile = new File("/Data/Talend/testdata/json/auto_assign_input_object_to_object.json");
		de.jlo.talendcomp.json.JsonDocument doc1 = new de.jlo.talendcomp.json.JsonDocument(testFile);
		sourceRootNode = doc1.getRootNode();
		targetRootNode = sourceRootNode;
	}

	public void setUpObjectToArray() throws Exception {
		File testFile = new File("/Data/Talend/testdata/json/auto_assign_input_object_to_array.json");
		de.jlo.talendcomp.json.JsonDocument doc1 = new de.jlo.talendcomp.json.JsonDocument(testFile);
		sourceRootNode = doc1.getRootNode();
		targetRootNode = sourceRootNode;
	}

	public void setUpObjectToArrayMissingSourceLoopPath() throws Exception {
		File testFile = new File("/Data/Talend/testdata/json/auto_assign_input_object_to_array_missing_source_loop_path.json");
		de.jlo.talendcomp.json.JsonDocument doc1 = new de.jlo.talendcomp.json.JsonDocument(testFile);
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

}
