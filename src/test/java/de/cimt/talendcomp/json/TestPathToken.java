package de.cimt.talendcomp.json;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;

public class TestPathToken {

	@Test
	public void testParseTokens() throws Exception {
		String jsonPath = "[0].bo.person[1].address[2][3].street[4]";
		List<PathToken> result = PathToken.parse(jsonPath);
		for (PathToken t : result) {
			System.out.println(t.toString() + ": " + t.getPath());
		}
		assertEquals(9, result.size());
	}
	
	@Test
	public void testCreateJSON() throws Exception {
		JsonDocument doc = new JsonDocument(true);
		String jsonPath = "[0].bo.person[0].address[2][3].street[4]";
		JsonNode node = doc.getNode(jsonPath, true);
		System.out.println(doc.getJsonString(true, false));
		String expected = "[{\"bo\":{\"person\":[{\"address\":[[{\"street\":[{}]}]]}]}}]";
		String actual = doc.getJsonString(false, false);
		assertEquals("Created JSON is not correct", expected, actual);
	}

	@Test
	public void testAddArrayToOjectJSON() throws Exception {
		JsonDocument doc = new JsonDocument("[{\"bo\":{\"person\":null}}]");
		String jsonPath = "[0].bo.person[0].address[2][3].street[4]";
		JsonNode node = doc.getNode(jsonPath, true);
		System.out.println(doc.getJsonString(true, false));
		String expected = "[{\"bo\":{\"person\":[{\"address\":[[{\"street\":[{}]}]]}]}}]";
		String actual = doc.getJsonString(false, false);
		assertEquals("Created JSON is not correct", expected, actual);
	}

	@Test
	public void testAddArrayToArrayJSON() throws Exception {
		JsonDocument doc = new JsonDocument("[{\"bo\":{\"person\":[{\"address\":[]}]}}]");
		String jsonPath = "[0].bo.person[0].address[2][3].street[4]";
		JsonNode node = doc.getNode(jsonPath, true);
		System.out.println(doc.getJsonString(true, false));
		String expected = "[{\"bo\":{\"person\":[{\"address\":[[{\"street\":[{}]}]]}]}}]";
		String actual = doc.getJsonString(false, false);
		assertEquals("Created JSON is not correct", expected, actual);
	}

}
