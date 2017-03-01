package de.cimt.talendcomp.json;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import de.cimt.talendcomp.json.ops.AutoAssign;

public class TestAutoAssign {
	
	private com.fasterxml.jackson.databind.JsonNode sourceRootNode = null;
	private com.fasterxml.jackson.databind.JsonNode targetRootNode = null;

	public void setUpObjectToObject() throws Exception {
		File testFile = new File("/Volumes/Data/Talend/testdata/json/auto_assign_input_object_to_object.json");
		de.cimt.talendcomp.json.JsonDocument doc1 = new de.cimt.talendcomp.json.JsonDocument(testFile);
		sourceRootNode = doc1.getRootNode();
		targetRootNode = sourceRootNode;
	}

	public void setUpObjectToArray() throws Exception {
		File testFile = new File("/Volumes/Data/Talend/testdata/json/auto_assign_input_object_to_array.json");
		de.cimt.talendcomp.json.JsonDocument doc1 = new de.cimt.talendcomp.json.JsonDocument(testFile);
		sourceRootNode = doc1.getRootNode();
		targetRootNode = sourceRootNode;
	}

	@Test
	public void testObjectToObject() throws Exception {
		setUpObjectToObject();
		AutoAssign am = new AutoAssign();
		am.setSourceNode(sourceRootNode);
		am.setTargetNode(targetRootNode);
		am.setSourceLoopPath("$.products[*].rightsownerships[*]");
		am.setTargetLoopPath("$.result.conflicts[*].conflict_parties[*]");
		am.setSourceIdentifier("rights_ownership_id");
		am.setTargetIdentifier("rights_ownership_id");
		am.setTargetMountAttribute("rights_ownership_object", false, true);
		am.executeMerge();
		System.out.println("count source nodes: " + am.getCountSourceNodes());
		System.out.println("count target nodes: " + am.getCountTargetNodes());
		System.out.println("count assigned: " + am.getCountAssigned());
		JsonDocument result = new JsonDocument(targetRootNode);
		System.out.println(result.getJsonString(true, false));
		assertEquals(10, am.getCountAssigned());
	}

	@Test
	public void testObjectToArray() throws Exception {
		setUpObjectToArray();
		AutoAssign am = new AutoAssign();
		am.setSourceNode(sourceRootNode);
		am.setTargetNode(targetRootNode);
		am.setSourceLoopPath("$.products[*].rightsownerships[*]");
		am.setTargetLoopPath("$.result.conflicts[*].conflict_parties");
		am.setSourceIdentifier("rights_ownership_id");
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

}
