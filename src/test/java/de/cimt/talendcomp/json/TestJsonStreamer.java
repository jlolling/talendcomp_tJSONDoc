package de.cimt.talendcomp.json;

import static org.junit.Assert.*;

import org.apache.log4j.BasicConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import de.cimt.talendcomp.json.streaming.JsonStreamParser;
import de.cimt.talendcomp.json.streaming.TypeUtil;

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
		JsonStreamParser.enableTraceLogging(true);
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
		JsonStreamParser.enableTraceLogging(true);
		JsonStreamParser p = new JsonStreamParser();
		p.addColumnAttrPath("integer-value", "$.test.object[*].demo[*].integer-value");
		p.addColumnAttrPath("json", "$.test.object[*].demo[*]");
		p.setLoopPath("$.test.object[*].demo[*]");
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
		p.addColumnAttrPath("integer-value", "$.test.object[*].demo[*].integer-value");
		p.setLoopPath("$.test.object[*]");
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
		JsonStreamParser.enableTraceLogging(true);
		JsonStreamParser p = new JsonStreamParser();
		p.addColumnAttrPath("title", "$.products[*].titel");
		p.addColumnAttrPath("json", "$.products[*]");
		p.setLoopPath("$.products[*]");
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

}
