package de.jlo.talendcomp.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class TestOutput {

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
	
	@Test
	public void testConditionalUnwrapRoot() throws Exception {
		String arrayStr = "[{\"a\":1}]";
		String nodeStr = "{\"a\":1}";
		JsonDocument doc = new JsonDocument(nodeStr);
		JsonDocument docArray = new JsonDocument(arrayStr);
		JsonNode node = docArray.getConditionalUnwrappedRootNode(true, true);
		assertEquals(doc.getRootNode(), node);
		arrayStr = "[{\"a\":1},{\"a\":2}]";
		docArray = new JsonDocument(arrayStr);
		node = docArray.getConditionalUnwrappedRootNode(true, false);
		assertEquals(doc.getRootNode(), node);
		arrayStr = "[{\"a\":1},{\"a\":2}]";
		docArray = new JsonDocument(arrayStr);
		try {
			node = docArray.getConditionalUnwrappedRootNode(true, true);
			assertTrue("Did not dies but unwrap was actually not possible", false);
		} catch (Exception e) {
			assertTrue(e.getMessage().contains("Cannot remove root array because it contains more than one nodes"));
		}
		arrayStr = "[]";
		docArray = new JsonDocument(arrayStr);
		node = docArray.getConditionalUnwrappedRootNode(true, true);
		assertNull(node);
		String exp = "{}";
		String act = docArray.getJsonString(node, false, false);
		assertEquals(exp, act);
	}

	@Test
	public void testWriteBigDecimalPlain() throws Exception {
		JsonDocument doc = new JsonDocument(false);
		ObjectNode root = (ObjectNode) doc.getNode("$");
		doc.setValue(root, "test", new BigDecimal("12345678.12345678"));
		String expected = "{\"test\":12345678.12345678}";
		String actual = doc.getJsonString(root, false, false);
		System.out.println(actual);
		assertEquals("Wrong format", expected, actual);
	}
	
	@Test
	public void testWriteDoublePlain() throws Exception {
		JsonDocument doc = new JsonDocument(false);
		ObjectNode root = (ObjectNode) doc.getNode("$");
		doc.setValue(root, "test", Double.valueOf("12345678.12345"));
		String expected = "{\"test\":12345678.12345}";
		String actual = doc.getJsonString(root, false, false);
		System.out.println(actual);
		assertEquals("Wrong format", expected, actual);
	}

	
}
