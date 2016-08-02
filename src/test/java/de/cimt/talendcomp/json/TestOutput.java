package de.cimt.talendcomp.json;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class TestOutput {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testEscape() throws Exception {
		String expected = "line1\"line2\nline3 \\ \" \b";
		System.out.println("expected=" + expected);
		JsonDocument doc = new JsonDocument(false);
		JsonNode parent = doc.getNode("$");
		doc.setValue((ObjectNode) parent, "escaped", expected);
		System.out.println(doc.getJsonString(true, false));
		String actual = doc.getValueAsString(parent, "escaped", false, false, null);
		System.out.println("actual=" + actual);
		assertEquals(expected, actual);
	}

	@Test
	public void testAddToArray() throws Exception {
		JsonDocument doc = new JsonDocument(false);
		JsonNode parent = doc.getNode("$");
		ArrayNode an = (ArrayNode) parent.withArray("array");
		an.add(1);
		an.add((Integer) null);
		an.add(3);
		System.out.println(doc.toString());
		assertTrue("Not enough array elements", an.size() == 3);
	}

}
