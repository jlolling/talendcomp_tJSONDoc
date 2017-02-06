package de.cimt.talendcomp.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;

public class TestPathToken {

	@Test
	public void testParseTokens() throws Exception {
		System.out.println("###### testParseTokens...");
		String jsonPath = "[0].bo.person[1].address[2][3].street[4]";
		List<PathToken> result = PathToken.parse(jsonPath);
		for (PathToken t : result) {
			System.out.println(t.toString() + ": " + t.getPath());
		}
		assertEquals(9, result.size());
		System.out.println("********* Part 2....");
		jsonPath = "$.bo.person.address[2]";
		result = PathToken.parse(jsonPath);
		for (PathToken t : result) {
			System.out.println(t.toString() + ": " + t.getPath());
		}
		assertEquals(4, result.size());
	}
	
	@Test
	public void testCreateJson() throws Exception {
		System.out.println("###### testCreateJSON...");
		JsonDocument doc = new JsonDocument(true);
		String jsonPath = "[0].bo.person[0].address[2][3].street[4]";
		JsonNode node = doc.getNode(jsonPath, true);
		assertNotNull(node);
		System.out.println("node: " + node);
		System.out.println(doc.getJsonString(true, false));
		String expected = "[{\"bo\":{\"person\":[{\"address\":[[{\"street\":[]}]]}]}}]";
		String actual = doc.getJsonString(false, false);
		assertEquals("Created JSON is not correct", expected, actual);
	}

	@Test
	public void testAddArrayToOject() throws Exception {
		System.out.println("###### testAddArrayToOjectJSON...");
		JsonDocument doc = new JsonDocument("[{\"bo\":{\"person\":null}}]");
		String jsonPath = "[0].bo.person[0].address[2][3].street[4]";
		JsonNode node = doc.getNode(jsonPath, true);
		assertNotNull(node);
		System.out.println("node: " + node);
		System.out.println("doc: " + doc.getJsonString(true, false));
		String expected = "[{\"bo\":{\"person\":[{\"address\":[[{\"street\":[]}]]}]}}]";
		String actual = doc.getJsonString(false, false);
		assertEquals("Created JSON is not correct", expected, actual);
	}

	@Test
	public void testAddArrayToArray() throws Exception {
		System.out.println("###### testAddArrayToArrayJSON...");
		JsonDocument doc = new JsonDocument("[{\"bo\":{\"person\":[{\"address\":[]}]}}]");
		String jsonPath = "[0].bo.person[0].address[2][3]";
		JsonNode node = doc.getNode(jsonPath, true);
		assertNotNull(node);
		System.out.println("node: " + node);
		System.out.println("doc: " + doc.getJsonString(true, false));
		String expected = "[{\"bo\":{\"person\":[{\"address\":[[]]}]}}]";
		String actual = doc.getJsonString(false, false);
		assertEquals("Created JSON is not correct", expected, actual);
	}

	@Test
	public void testReadArray() throws Exception {
		System.out.println("###### testReadArrayJSON...");
		JsonDocument doc = new JsonDocument("[{\"bo\":{\"person\":[{\"address\":[{\"street\" : \"s1\"}], \"type\":\"private\"}]}},{\"bo\":{\"person\":[{\"address\":[{\"street\" : \"s2\"}], \"type\":\"public\"}]}}]");
		String jsonPath = "[*].bo.person[*].address";
		JsonNode node = doc.getNode(jsonPath, false);
		assertNotNull(node);
		System.out.println("node: " + node);
		String expected = "[[{\"street\":\"s1\"}],[{\"street\":\"s2\"}]]";
		String actual = node.toString();
		assertEquals("Created JSON is not correct", expected, actual);
	}

	@Test
	public void testReadArrayAndGetObjects() throws Exception {
		System.out.println("###### testReadArrayJSON...");
		JsonDocument doc = new JsonDocument("[{\"bo\":{\"person\":[{\"address\":[{\"street\" : \"s1\"}], \"type\":\"private\"}]}},{\"bo\":{\"person\":[{\"address\":[{\"street\" : \"s2\"}], \"type\":\"public\"}]}}]");
		String jsonPath = "[*].bo.person[*].address";
		JsonNode node = doc.getNode(jsonPath, false);
		assertNotNull(node);
		System.out.println("node: " + node);
		String expected = "[[{\"street\":\"s1\"}],[{\"street\":\"s2\"}]]";
		String actual = node.toString();
		assertEquals("Created JSON is not correct", expected, actual);
		List<JsonNode> result = doc.getArrayValuesAsList(node);
		for (JsonNode n : result) {
			System.out.println(n);
			String s = n.toString();
			// check if we get the correct detail result
			assertTrue(s.startsWith("{\"street\":\"s"));
		}
		assertEquals(2, result.size());
	}

	@Test
	public void testDealWithMissing() throws Exception {
		System.out.println("###### testDealWithMissing...");
		// the missing node is mounted at the root object - to check if we accidently find the wrong one
		JsonDocument doc = new JsonDocument("[{\"bo\":{\"person\":[{\"address\":[]}]},\"missing\":\"value\"}]");
		String jsonPath = "[0].bo.person[0]";
		JsonNode node = doc.getNode(jsonPath, false);
		assertNotNull(node);
		System.out.println("node: " + node);
		// we are searching the missing attribute in first person object
		JsonNode missingNode = doc.getNode(node, "missing");
		assertNull(missingNode);
	}

}
