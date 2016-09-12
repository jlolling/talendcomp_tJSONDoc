package de.cimt.talendcomp.json;

import static org.junit.Assert.*;

import org.apache.log4j.BasicConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import de.cimt.talendcomp.json.streaming.JsonStreamParser;

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
		JsonStreamParser p = new JsonStreamParser();
		p.addColumnAttrPath("id", "$[*].id");
		p.addColumnAttrPath("name", "$[*].name");
		p.addColumnAttrPath("city", "$[*].city");
		p.setLoopPath("$[*]");
		p.setInputFile("/Volumes/Data/Talend/testdata/json/small_simple_array.json");
		int index = 0;
		while (p.next()) {
			System.out.println("id=" + p.getValue("id"));
			System.out.println("name=" + p.getValue("name"));
			System.out.println("city=" + p.getValue("city"));
			System.out.println("json=" + p.getLoopJsonNode());
			System.out.println("-------------------------");
			index++;
		}
		System.out.println("loop index: " + index);
		assertTrue(p.getCurrentLoopIndex() == 3);
	}
	
	@Test
	public void testReadComplexArray() throws Exception {
		System.out.println("Test testReadComplexArray #################################");
		JsonStreamParser p = new JsonStreamParser();
		p.addColumnAttrPath("integer-value", "$.test.object[*].demo[*].integer-value");
		p.setLoopPath("$.test.object[*].demo[*]");
		p.setInputFile("/Volumes/Data/Talend/testdata/json/test_1.json");
		int index = 0;
		while (p.next()) {
			System.out.println("json=" + p.getLoopJsonNode());
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
			System.out.println("json=" + p.getLoopJsonNode());
			index++;
		}
		System.out.println("loop index: " + index);
		assertTrue(p.getCurrentLoopIndex() == 2);
	}

}
