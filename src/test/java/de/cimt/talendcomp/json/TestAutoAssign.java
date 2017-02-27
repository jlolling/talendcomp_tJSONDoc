package de.cimt.talendcomp.json;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import de.cimt.talendcomp.json.ops.AutoAssign;

public class TestAutoAssign {
	
	private com.fasterxml.jackson.databind.JsonNode sourceRootNode = null;
	private com.fasterxml.jackson.databind.JsonNode targetRootNode = null;

	@Before
	public void setUp() throws Exception {
		File testFile = new File("/var/testdata/json/cdh_result.json");
		de.cimt.talendcomp.json.JsonDocument doc1 = new de.cimt.talendcomp.json.JsonDocument(testFile);
		sourceRootNode = doc1.getRootNode();
		targetRootNode = sourceRootNode;
	}

	@Test
	public void test() throws Exception {
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
		assertTrue(am.getCountAssigned() == 10);
	}

}
