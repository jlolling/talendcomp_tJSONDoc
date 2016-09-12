package de.cimt.talendcomp.json;

import static org.junit.Assert.*;

import org.apache.log4j.BasicConfigurator;
import org.junit.Test;

import de.cimt.talendcomp.json.streaming.JsonStreamParser;

public class TestJsonStreamer {

	@Test
	public void testReadSimpleArray() throws Exception {
		BasicConfigurator.configure();
		JsonStreamParser p = new JsonStreamParser();
		//JsonStreamParser.enableTraceLogging(true);
		p.addColumnAttrPath("id", "$[*].id");
		p.addColumnAttrPath("name", "$[*].name");
		p.addColumnAttrPath("city", "$[*].city");
		p.addColumnAttrPath("json", "$[*]");
		p.setLoopPath("$[*]");
		p.setInputFile("/Volumes/Data/Talend/testdata/json/large_simple_array.json");
		int index = 0;
		while (p.next()) {
//			System.out.println("id=" + p.getValue("id"));
//			System.out.println("name=" + p.getValue("name"));
//			System.out.println("json=" + p.getValue("json"));
			index++;
		}
		System.out.println("loop index: " + index);
		assertTrue(p.getCurrentLoopIndex() == 1000000);
	}
	
}
