package de.cimt.talendcomp.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
	public void testCreateJSON() throws Exception {
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
	public void testAddArrayToOjectJSON() throws Exception {
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
	public void testAddArrayToArrayJSON() throws Exception {
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
	public void testReadArrayJSON() throws Exception {
		System.out.println("###### testReadArrayJSON...");
		JsonDocument doc = new JsonDocument("[{\"bo\":{\"person\":[{\"address\":[{\"street\" : \"s1\"}]}]}}]");
		String jsonPath = "[*].bo.person[0].address";
		JsonNode node = doc.getNode(jsonPath, false);
		assertNotNull(node);
		System.out.println("node: " + node);
		String expected = "[[{\"street\":\"s1\"}]]";
		String actual = node.toString();
		assertEquals("Created JSON is not correct", expected, actual);
	}

	@Test
	public void testDealWithMissing() throws Exception {
		System.out.println("###### testDealWithMissing...");
		JsonDocument doc = new JsonDocument("[{\"bo\":{\"person\":[{\"address\":[]}]}}]");
		String jsonPath = "[0].bo.person[0]";
		JsonNode node = doc.getNode(jsonPath, false);
		assertNotNull(node);
		System.out.println("node: " + node);
		JsonNode missingNode = doc.getNode(node, "missing");
		assertNull(missingNode);
	}

}
