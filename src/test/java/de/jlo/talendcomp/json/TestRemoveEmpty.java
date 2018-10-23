package de.jlo.talendcomp.json;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import de.cimt.talend.mock.TalendJobMock;

public class TestRemoveEmpty extends TalendJobMock {
	
	@Test
	public void testReadNull() throws Exception {
		String json = "{\n"
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
		JsonDocument doc = new JsonDocument(json);
		ObjectNode node = (ObjectNode) doc.getNode("$.participation.pseudonym");
		Long pseudonym_id = doc.getValueAsLong(node, "pseudonym_id", true, true, null);
		System.out.println(pseudonym_id);
		assertNull("Expected null but got anything else", pseudonym_id);
	}
	
	@Test
	public void testRemoveEmptyObject() throws Exception {
		String json = "{\"a1\" : \"v1\", \"a2\" : {}}";
		JsonDocument doc = new JsonDocument(json);
		String jsonResult = doc.getJsonString(false, true);
		System.out.println(jsonResult);
		JsonNode a2 = doc.getNode("$.a2");
		JsonDocument doc2 = new JsonDocument(jsonResult);
		JsonNode a21 = doc2.getNode("$.a2");
		assertNotNull(a2);
		assertNull(a21);
	}
	
	@Test
	public void testRemoveNullValue() throws Exception {
		String json = "{\"a1\" : \"v1\", \"a2\" : null}";
		JsonDocument doc = new JsonDocument(json);
		String jsonResult = doc.getJsonString(false, true);
		System.out.println(jsonResult);
		JsonNode a2 = doc.getNode("$.a2");
		JsonDocument doc2 = new JsonDocument(jsonResult);
		JsonNode a21 = doc2.getNode("$.a2");
		assertNull(a2);
		assertNull(a21);
	}

	@Test
	public void testRemoveEmptyArray() throws Exception {
		String json = "{\"a1\" : \"v1\", \"a2\" : []}";
		JsonDocument doc = new JsonDocument(json);
		String jsonResult = doc.getJsonString(false, true);
		System.out.println(jsonResult);
		JsonNode a2 = doc.getNode("$.a2");
		JsonDocument doc2 = new JsonDocument(jsonResult);
		JsonNode a21 = doc2.getNode("$.a2");
		assertNotNull(a2);
		assertNull(a21);
	}

}
