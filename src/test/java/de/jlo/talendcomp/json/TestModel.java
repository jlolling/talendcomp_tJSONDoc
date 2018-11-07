package de.jlo.talendcomp.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;


public class TestModel {
	
	@Test
	public void testObjectEquals() throws Exception {
		String json1 = "{\n"
			    + "   \"created_by\" : 101,\n"
			    + "   \"data_status_id\" : 1,\n"
			    + "   \"process_status_id\" : 1,\n"
			    + "   \"participation\" : {\n"
			    + "      \"product_id\" : 100,\n"
			    + "      \"role_id\" : 1087,\n"
			    + "      \"function_id\" : 1100,\n"
			    + "      \"shooting_days\" : 12,\n"
			    + "      \"participation_date\" : \"2012-01-01T11:22:33.999\",\n"
			    + "      \"source_id\" : 102,\n"
			    + "      \"participant\" : {\n"
			    + "         \"participant_id\" : 103\n"
			    + "      },\n"
			    + "      \"pseudonym\" : {\n"
			    + "         \"pseudonym_id\" : null,\n"
			    + "         \"nametype_id\" : 5643,\n"
			    + "         \"salutation\" : null,\n"
			    + "         \"title\" : null,\n"
			    + "         \"firstname_supplement\" : null,\n"
			    + "         \"firstname\" : null,\n"
			    + "         \"name\" : \"The Symbol\",\n"
			    + "         \"name_supplement\" : null\n"
			    + "      },\n"
			    + "      \"participantname\" : {\n"
			    + "         \"participant_name_id\" : null,\n"
			    + "         \"nametype_id\" : null,\n"
			    + "         \"salutation\" : null,\n"
			    + "         \"title\" : null,\n"
			    + "         \"firstname_supplement\" : null,\n"
			    + "         \"firstname\" : null,\n"
			    + "         \"name\" : null,\n"
			    + "         \"name_supplement\" : null\n"
			    + "      },\n"
			    + "      \"rolenames\" : [\n"
			    + "         {\n"
			    + "            \"rolename\" : \"Landärztin Anna Maria Strickenbach\",\n"
			    + "            \"language_id\" : 6541,\n"
			    + "            \"default_selection\" : true\n"
			    + "         },\n"
			    + "         {\n"
			    + "            \"rolename\" : \"Country doctor Anna Maria Strickenbach\",\n"
			    + "            \"language_id\" : 6598,\n"
			    + "            \"default_selection\" : false\n"
			    + "         }\n"
			    + "      ],\n"
			    + "      \"remarks\" : [\n"
			    + "         {\n"
			    + "            \"remark\" : \"Hat nur in dieser einen Serienepisode mitgewirkt\",\n"
			    + "            \"remark_type_id\" : 4321\n"
			    + "         }\n"
			    + "      ]\n"
			    + "   }\n"
			    + "}";
		String json2 = "{\"created_by\" : 101,\n"
			    + "   \"data_status_id\" : 1,\n"
			    + "   \"process_status_id\" : 1,\n"
			    + "   \"participation\" : {\n"
			    + "      \"product_id\" : 100,\n"
			    + "      \"role_id\" : 1087,\n"
			    + "      \"function_id\" : 1100,\n"
			    + "      \"shooting_days\" : 12,\n"
			    + "      \"participation_date\" : \"2012-01-01T11:22:33.999\",\n"
			    + "      \"source_id\" : 102,\n"
			    + "      \"participant\" : {\n"
			    + "         \"participant_id\" : 103\n"
			    + "      },\n"
			    + "      \"pseudonym\" : {\n"
			    + "         \"pseudonym_id\" : null,\n"
			    + "         \"nametype_id\" : 5643,\n"
			    + "         \"salutation\" : null,\n"
			    + "         \"title\" : null,\n"
			    + "         \"firstname_supplement\" : null,\n"
			    + "         \"firstname\" : null,\n"
			    + "         \"name\" : \"The Symbol\",\n"
			    + "         \"name_supplement\" : null\n"
			    + "      },\n"
			    + "      \"participantname\" : {\n"
			    + "         \"participant_name_id\" : null,\n"
			    + "         \"nametype_id\" : null,\n"
			    + "         \"salutation\" : null,\n"
			    + "         \"title\" : null,\n"
			    + "         \"firstname_supplement\" : null,\n"
			    + "         \"firstname\" : null,\n"
			    + "         \"name\" : null,\n"
			    + "         \"name_supplement\" : null\n"
			    + "      },\n"
			    + "      \"rolenames\" : [\n"
			    + "         {\n"
			    + "            \"rolename\" : \"Landärztin Anna Maria Strickenbach\",\n"
			    + "            \"language_id\" : 6541,\n"
			    + "            \"default_selection\" : true\n"
			    + "         },\n"
			    + "         {\n"
			    + "            \"rolename\" : \"Country doctor Anna Maria Strickenbach\",\n"
			    + "            \"language_id\" : 6598,\n"
			    + "            \"default_selection\" : false\n"
			    + "         }\n"
			    + "      ],\n"
			    + "      \"remarks\" : ["
			    + "         {\n"
			    + "            \"remark\" : \"Hat nur in dieser einen Serienepisode mitgewirkt\",\n"
			    + "            \"remark_type_id\" : 4321\n"
			    + "         }"
			    + "      ]\n"
			    + "   }\n"
			    + "}";
		JsonDocument doc1 = new JsonDocument(json1);
		JsonDocument doc2 = new JsonDocument(json2);
		JsonNode r1 = doc1.getRootNode();
		JsonNode r2 = doc2.getRootNode();
		assertEquals(r1, r2);
		assertEquals(1, doc1.getCountRootObjects());
	}
	
	@Test
	public void testThreadSaveJsonDocument() throws Exception {
		String jsonTemplate = "{\"created_by\" : <value>,\n"
			    + "   \"data_status_id\" : 1,\n"
			    + "   \"process_status_id\" : 1,\n"
			    + "   \"participation\" : {\n"
			    + "      \"product_id\" : 100,\n"
			    + "      \"role_id\" : 1087,\n"
			    + "      \"function_id\" : 1100,\n"
			    + "      \"shooting_days\" : 12,\n"
			    + "      \"participation_date\" : \"2012-01-01T11:22:33.999\",\n"
			    + "      \"source_id\" : 102,\n"
			    + "      \"participant\" : {\n"
			    + "         \"participant_id\" : 103\n"
			    + "      },\n"
			    + "      \"pseudonym\" : {\n"
			    + "         \"pseudonym_id\" : null,\n"
			    + "         \"nametype_id\" : 5643,\n"
			    + "         \"salutation\" : null,\n"
			    + "         \"title\" : null,\n"
			    + "         \"firstname_supplement\" : null,\n"
			    + "         \"firstname\" : null,\n"
			    + "         \"name\" : \"The Symbol\",\n"
			    + "         \"name_supplement\" : null\n"
			    + "      },\n"
			    + "      \"participantname\" : {\n"
			    + "         \"participant_name_id\" : <value>,\n"
			    + "         \"nametype_id\" : null,\n"
			    + "         \"salutation\" : null,\n"
			    + "         \"title\" : \"<value>\",\n"
			    + "         \"firstname_supplement\" : null,\n"
			    + "         \"firstname\" : null,\n"
			    + "         \"name\" : null,\n"
			    + "         \"name_supplement\" : null\n"
			    + "      },\n"
			    + "      \"rolenames\" : [\n"
			    + "         {\n"
			    + "            \"rolename\" : \"Landärztin Anna Maria Strickenbach\",\n"
			    + "            \"language_id\" : 6541,\n"
			    + "            \"default_selection\" : true\n"
			    + "         },\n"
			    + "         {\n"
			    + "            \"rolename\" : \"Country doctor Anna Maria Strickenbach\",\n"
			    + "            \"language_id\" : 6598,\n"
			    + "            \"default_selection\" : false\n"
			    + "         }\n"
			    + "      ],\n"
			    + "      \"remarks\" : ["
			    + "         {\n"
			    + "            \"remark\" : \"Hat nur in dieser einen Serienepisode mitgewirkt\",\n"
			    + "            \"remark_type_id\" : 4321\n"
			    + "         }"
			    + "      ]\n"
			    + "   }\n"
			    + "}";
		List<JsonThread> threads = new ArrayList<>();
		int max = 2000;
		for (int i = 0; i < max; i++) {
			threads.add(new JsonThread(jsonTemplate, i));
		}
		for (int i = 0; i < max; i++) {
			threads.get(i).start();
		}
		boolean running = true;
		while (running) {
			running = false;
			for (int i = 0; i < max; i++) {
				if (threads.get(i).isAlive()) {
					running = true;
				}
			}
		}
		for (int i = 0; i < max; i++) {
			JsonThread t = threads.get(i);
			if (t.ex != null) {
				assertTrue("Exception detected: " + t.ex, false);
			}
		}
		System.out.println(threads.size() + " Threads had run to test thread save parsing.");
		assertTrue(true);
	}
	
	private static class JsonThread extends Thread {
		
		String json = null;
		int expectedIndex;
		Exception ex = null;
		
		public JsonThread(String template, int index) {
			this.expectedIndex = index;
			this.json = template.replace("<value>", String.valueOf(index));
		}
		
		@Override
		public void run() {
			int actualIndex = -1;
			JsonDocument doc = null;
			try {
				doc = new JsonDocument(json);
				Thread.sleep(1000);
				if (expectedIndex % 2 == 0) {
					actualIndex = doc.getNode("$.participation.participantname.participant_name_id").asInt();
				} else {
					actualIndex = doc.getNode("$.created_by").asInt();
				}
			} catch (Exception e) {
				ex = e; 
			}
			if (expectedIndex != actualIndex) {
				ex = new Exception("We got values from other threads! json: expcted: " + expectedIndex + " actual: " + actualIndex);
			}
		}
	}
}
