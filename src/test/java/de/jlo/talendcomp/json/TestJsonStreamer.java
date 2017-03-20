package de.jlo.talendcomp.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.log4j.BasicConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import de.jlo.talendcomp.json.streaming.JsonStreamParser;
import de.jlo.talendcomp.json.streaming.TypeUtil;

public class TestJsonStreamer {

	@BeforeClass
	public static void setupLogging() {
		System.out.println("Configure log4j");
		BasicConfigurator.configure();
		JsonStreamParser.enableTraceLogging(false);
	}
	
	@Test
	public void testReadSimpleArray() throws Exception {
		System.out.println("Test testReadSimpleArray #################################");
//		JsonStreamParser.enableTraceLogging(true);
		JsonStreamParser p = new JsonStreamParser();
		p.setLoopPath("$[*]");
		p.addColumnAttrPath("id", "id");
		p.addColumnAttrPath("name", "name");
		p.addColumnAttrPath("city", "city");
		p.setInputFile("/Volumes/Data/Talend/testdata/json/small_simple_array.json");
		int index = 0;
		while (p.next()) {
			System.out.println("index=" + p.getCurrentLoopIndex());
			System.out.println("id=" + TypeUtil.convertToInteger(p.getValue("id")));
			System.out.println("name=" + TypeUtil.convertToString(p.getValue("name")));
			System.out.println("city=" + TypeUtil.convertToString(p.getValue("city")));
			System.out.println("json=" + p.getLoopJsonNode());
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
		p.setLoopPath("$.test.object[*].demo[*]");
		p.addColumnAttrPath("integer-value", "$.test.object[*].demo[*].integer-value");
		p.addColumnAttrPath("json", "$.test.object[*].demo[*]");
		p.setInputFile("/Volumes/Data/Talend/testdata/json/test_1.json");
		int index = 0;
		while (p.next()) {
			System.out.println("integer-value=" + p.getValue("integer-value"));
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
		p.setLoopPath("$.test.object[*]");
		p.addColumnAttrPath("integer-value", "$.test.object[*].demo[*].integer-value");
		p.setInputFile("/Volumes/Data/Talend/testdata/json/test_1.json");
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
		p.setLoopPath("$.products[*]");
		p.addColumnAttrPath("title", "titel");
		p.addColumnAttrPath("json", "$.products[*]");
		p.setInputFile("/Volumes/Data/Talend/testdata/json/products_small.json");
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
		String expath = "$.products[*].titel";
		String path = "$.products[*].titelBereinigt";
		assertTrue(JsonStreamParser.isMatchingSubpath(path, expath) == false);
	}

	@Test
	public void testPathMathingTrue() {
		String expath = "$.products[*]";
		String path = "$.products[*].titel";
		assertTrue(JsonStreamParser.isMatchingSubpath(path, expath));
	}

	@Test
	public void testPathMathingNoObject() {
		String expath = "$[*]";
		String path = "$[*]";
		assertTrue(JsonStreamParser.isMatchingSubpath(path, expath));
	}

	@Test
	public void testGetKeyLevelZero() {
		String expath = "$[*]";
		int expected = 2;
		assertEquals(expected, JsonStreamParser.getKeyLevel(expath));
	}

	@Test
	public void testGetKeyLevelTwo() {
		String expath = "$[*].a.b";
		int expected = 4;
		assertEquals(expected, JsonStreamParser.getKeyLevel(expath));
	}

	@Test
	public void testReadNestedArray() throws Exception {
		System.out.println("Test testReadNestedArray #################################");
//		JsonStreamParser.enableTraceLogging(true);
		JsonStreamParser p = new JsonStreamParser();
		p.setLoopPath("$[*].items[*].item_data[*]");
		p.addColumnAttrPath("group-header", "$[*].items[*].group_header");
		p.addColumnAttrPath("header", "$[*].header");
		p.addColumnAttrPath("key", "item-key");
		p.addColumnAttrPath("value", "item-value");
		p.setInputFile("/Volumes/Data/Talend/testdata/json/multi_level_arrays.json");
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
			String group_header = p.getValue("group-header");
			if (group_header.contains(",")) {
				throw new Exception("invalid value aggregation detected!");
			}
			System.out.println("group-header=" + group_header);
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

}
