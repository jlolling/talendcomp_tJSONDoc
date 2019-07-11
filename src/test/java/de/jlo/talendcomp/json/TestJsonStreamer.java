package de.jlo.talendcomp.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.log4j.BasicConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;

import de.jlo.talendcomp.json.streaming.JsonStreamParser;

public class TestJsonStreamer {

	@BeforeClass
	public static void setupLogging() {
		System.out.println("Configure log4j");
		BasicConfigurator.configure();
		//JsonStreamParser.enableTraceLogging(true);
	}
	
	@Test
	public void testReadSimpleArray() throws Exception {
		System.out.println("Test testReadSimpleArray #################################");
//		JsonStreamParser.enableTraceLogging(true);
		JsonStreamParser p = new JsonStreamParser();
		p.setInputResource("small_simple_array.json");
		p.setLoopPath("$[*]");
		p.addColumnAttrPath("id", "id");
		p.addColumnAttrPath("name", "name");
		p.addColumnAttrPath("city", "city");
		String[] jsons = new String[] {
				"{\"city\":\"Little Rock\",\"name\":\"Wilson\",\"id\":1}",
				"{\"city\":\"Boise\",\"name\":\"Coolidge\",\"id\":null}",
				"{\"city\":\"Jackson\",\"name\":\"Nixon\",\"id\":3}"
		};
		int index = 0;
		while (p.next()) {
			System.out.println("index=" + p.getCurrentLoopIndex());
			System.out.println("id=" + TypeUtil.convertToInteger(p.getValue("id")));
			System.out.println("name=" + TypeUtil.convertToString(p.getValue("name")));
			System.out.println("city=" + TypeUtil.convertToString(p.getValue("city")));
			JsonNode json = p.getLoopJsonNode();
			System.out.println("json=" + json);
			assertEquals("Json loop node does not math for index=" + index, jsons[index], json.toString());
			System.out.println("-------------------------");
			index++;
		}
		System.out.println("loop index: " + index);
		assertTrue(index == 3);
	}
	
	@Test
	public void testReadComplexArray() throws Exception {
		System.out.println("Test testReadComplexArray #################################");
//		JsonStreamParser.enableTraceLogging(true);
		JsonStreamParser p = new JsonStreamParser();
		p.setInputResource("test_1.json");
		p.setLoopPath("$.test.object[*].demo[*]");
		p.addColumnAttrPath("integer-value", "$.test.object[*].demo[*].integer-value");
		p.addColumnAttrPath("json", "$.test.object[*].demo[*]");
		int index = 0;
		while (p.next()) {
			String iv = p.getValue("integer-value");
			if (iv == null) {
				throw new Exception("integer-value cannot be null");
			}
			System.out.println("integer-value=" + iv);
			System.out.println("json=" + p.getValue("json"));
			System.out.println("node=" + p.getLoopJsonNode());
			index++;
		}
		System.out.println("loop index: " + index);
		assertTrue(p.getCurrentLoopIndex() == 4);
	}

	@Test
	public void testReadComplexArray2() throws Exception {
		System.out.println("Test testReadComplexArray2 #################################");
		JsonStreamParser p = new JsonStreamParser();
		p.setInputResource("test_1.json");
		p.setLoopPath("$.test.object[*]");
		p.addColumnAttrPath("integer-value", "$.test.object[*].demo[*].integer-value");
		int index = 0;
		while (p.next()) {
//			System.out.println("json=" + p.getLoopJsonNode());
			index++;
		}
		System.out.println("loop index: " + index);
		assertTrue(p.getCurrentLoopIndex() == 2);
	}

	@Test
	public void testReadEscaped() throws Exception {
		System.out.println("Test testReadEscaped #################################");
//		JsonStreamParser.enableTraceLogging(true);
		JsonStreamParser p = new JsonStreamParser();
		p.setInputResource("products_small.json");
		p.setLoopPath("$.products[*]");
		p.addColumnAttrPath("title", "titel");
		p.addColumnAttrPath("json", "$.products[*]");
		int index = 0;
		while (p.next()) {
			System.out.println("json=" + p.getValue("json"));
			System.out.println("title=" + p.getValue("title"));
			index++;
		}
		System.out.println("loop index: " + index);
		assertTrue(true);
	}
	
	@Test
	public void testPathMathingFalse() {
		System.out.println("Test testPathMathingFalse #################################");
		String expath = "$.products[*].titel";
		String path = "$.products[*].titelBereinigt";
		assertTrue(JsonStreamParser.isMatchingSubpath(path, expath) == false);
	}

	@Test
	public void testPathMathingTrue1() {
		System.out.println("Test testPathMathingTrue1 #################################");
		String expath = "$.products[*]";
		String path = "$.products[*].titel";
		assertTrue(JsonStreamParser.isMatchingSubpath(path, expath));
	}

	@Test
	public void testPathMathingTrue2() {
		System.out.println("Test testPathMathingTrue2 #################################");
		String path = "$.products[*]";
		String expath = "$.products[*].titel";
		assertTrue(JsonStreamParser.isMatchingSubpath(path, expath) == false);
	}

	@Test
	public void testPathMathingNoObject() {
		System.out.println("Test testPathMathingNoObject #################################");
		String expath = "$[*]";
		String path = "$[*]";
		assertTrue(JsonStreamParser.isMatchingSubpath(path, expath));
	}

	@Test
	public void testGetKeyLevel2() {
		System.out.println("Test testGetKeyLevel2 #################################");
		String expath = "$[*]";
		int expected = 2;
		assertEquals(expected, JsonStreamParser.getKeyLevel(expath));
	}

	@Test
	public void testGetKeyLevel3() {
		System.out.println("Test testGetKeyLevel3 #################################");
		String expath = "$[*].a.b";
		int expected = 3;
		assertEquals(expected, JsonStreamParser.getKeyLevel(expath));
	}

	@Test
	public void testGetKeyLevel4() {
		System.out.println("Test testGetKeyLevel4 #################################");
		String expath = "$[*].a[*].b";
		int expected = 4;
		assertEquals(expected, JsonStreamParser.getKeyLevel(expath));
	}

	@Test
	public void testReadNestedArray() throws Exception {
		System.out.println("Test testReadNestedArray #################################");
//		JsonStreamParser.enableTraceLogging(true);
		JsonStreamParser p = new JsonStreamParser();
		p.setInputResource("multi_level_arrays.json");
		p.setLoopPath("$[*].items[*].item_data[*]");
		p.addColumnAttrPath("group_header", "$[*].items[*].group_header");
		p.addColumnAttrPath("header", "$[*].header");
		p.addColumnAttrPath("key", "item-key");
		p.addColumnAttrPath("value", "item-value");
		int index = 0;
		int sumValue = 0;
		Integer key = 0;
		int countHeaders = 0;
		long start = System.currentTimeMillis();
		while (p.next()) {
			key = TypeUtil.convertToInteger(p.getValue("key"));
			System.out.println("key=" + key);
			sumValue = (key != null ? key : 0) + sumValue;
			String header = p.getValue("header");
			System.out.println("header=" + header);
			String group_header = p.getValue("group_header");
			System.out.println("group_header=" + group_header);
			if (group_header.contains(",")) {
				throw new Exception("invalid value aggregation detected!");
			}
			if (group_header != null) {
				countHeaders++;
			}
			System.out.println("value=" + p.getValue("value"));
			System.out.println("node=" + p.getLoopJsonNode());
			index++;
		}
		long end = System.currentTimeMillis();
		System.out.println("duraction in ms: " + (end - start));
		System.out.println("loop index: " + index);
		assertEquals("Loop index does not match", 12, p.getCurrentLoopIndex());
		assertEquals("Sum value does not match", 2004, sumValue);
		assertEquals("Headers does not match", 12, countHeaders);
	}

	@Test
	public void testReadArrayWithDifferentPathes1() throws Exception {
		System.out.println("Test testReadArrayWithDifferentPathes1 #################################");
		JsonStreamParser p = new JsonStreamParser();
		p.setInputResource("test_tJSONDocStreamInput.json");
		p.setLoopPath("$[*].items[*].item_data[*]");
		p.addColumnAttrPath("global_header", "$[*].header");
		p.addColumnAttrPath("group_header", "$[*].items[*].group_header");
		p.addColumnAttrPath("item-key", "$[*].items[*].item_data[*].item-key");
		while (p.next()) {
			String globalHeader = p.getValue("global_header");
			int globalHeaderOccurence = p.getCountOccurence("global_header");
			String groupHeader = p.getValue("group_header");
			int groupHeaderOccurence = p.getCountOccurence("group_header");
			String itemKey = p.getValue("item-key");
			int itemKeyOccurence = p.getCountOccurence("item-key");
			int loopIndex = p.getCurrentLoopIndex();
			System.out.println("Index: " + loopIndex);
			System.out.println("globalHeaderOccurence: " + globalHeaderOccurence);
			System.out.println("groupHeaderOccurence: " + groupHeaderOccurence);
			System.out.println("itemKeyOccurence: " + itemKeyOccurence);
			System.out.println("global_header=" + globalHeader);
			System.out.println("group_header=" + groupHeader);
			System.out.println("item-key=" + itemKey);
			assertEquals("Data test <global_header> failed for index: " + loopIndex, "global header" + globalHeaderOccurence, globalHeader);
			assertEquals("Data test <group_header> failed for index: " + loopIndex, "group_header" + groupHeaderOccurence, groupHeader);
			assertEquals("Data test <item-key> failed for index: " + loopIndex, "" + itemKeyOccurence, itemKey);
		}
	}

	@Test
	public void testReadArrayWithDifferentPathes2() throws Exception {
		System.out.println("Test testReadArrayWithDifferentPathes2 #################################");
		JsonStreamParser p = new JsonStreamParser();
		p.setInputResource("test_tJSONDocStreamInput.json");
		p.setLoopPath("$[*].more_items[*].item_data[*]");
		p.addColumnAttrPath("global_header", "$[*].header");
		p.addColumnAttrPath("group_header", "$[*].more_items[*].more_items_group_header");
		p.addColumnAttrPath("item-key", "$[*].more_items[*].item_data[*].item-key");
		while (p.next()) {
			String globalHeader = p.getValue("global_header");
			int globalHeaderOccurence = p.getCountOccurence("global_header");
			String groupHeader = p.getValue("group_header");
			int groupHeaderOccurence = p.getCountOccurence("group_header");
			String itemKey = p.getValue("item-key");
			int itemKeyOccurence = p.getCountOccurence("item-key");
			int loopIndex = p.getCurrentLoopIndex();
			System.out.println("Index: " + loopIndex);
			System.out.println("globalHeaderOccurence: " + globalHeaderOccurence);
//			System.out.println("groupHeaderOccurence: " + groupHeaderOccurence);
//			System.out.println("itemKeyOccurence: " + itemKeyOccurence);
//			System.out.println("global_header=" + globalHeader);
//			System.out.println("group_header=" + groupHeader);
//			System.out.println("item-key=" + itemKey);
			assertEquals("Data test <group_header> failed for index: " + loopIndex, "group_header" + groupHeaderOccurence, groupHeader);
			assertEquals("Data test <item-key> failed for index: " + loopIndex, "" + itemKeyOccurence, itemKey);
			assertEquals("Data test <global_header> failed for index: " + loopIndex, "global header" + globalHeaderOccurence, globalHeader);
		}
	}

}
