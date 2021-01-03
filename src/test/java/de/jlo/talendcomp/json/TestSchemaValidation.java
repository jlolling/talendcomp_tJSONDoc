package de.jlo.talendcomp.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.ValidationMessage;

public class TestSchemaValidation {
	
	String jsonDocStr = "{\n"
		    + "   \"created_byX\" : 101,\n"
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
		    + "         \"title\" : \"Jan\",\n"
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
		    + "            \"default_selectionX\" : false\n"
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

	@Test
	public void testReadResource() throws Exception {
		String actual = ResourceUtil.readTextResource("/participation_testdata.json").trim();
		System.out.println(actual);
		String expected = jsonDocStr.trim();
		assertEquals("read data wrong", expected, actual);
	}
	
	@Test
	public void testValidateV4() throws Exception {
		JsonNode schemaNode = JsonDocument.parse(ResourceUtil.readTextResource("/participation_schema_v04.json"));
		JsonNode dataNode = JsonDocument.parse(ResourceUtil.readTextResource("/participation_testdata.json"));
		String schemaId = "0";
		JsonDocument.setJsonSchema(schemaId, schemaNode);
		JsonDocument doc = new JsonDocument(dataNode);
		System.out.println(doc.validate(schemaId));
        assertTrue(true);
	}

	@Test
	public void testValidateV201909() throws Exception {
		JsonNode schemaNode = JsonDocument.parse(ResourceUtil.readTextResource("/participation_schema_v201909.json"));
		JsonNode dataNode = JsonDocument.parse(ResourceUtil.readTextResource("/participation_testdata.json"));
		String schemaId = "0";
		JsonDocument.setJsonSchema(schemaId, schemaNode);
		JsonDocument doc = new JsonDocument(dataNode);
		System.out.println(doc.validate(schemaId));
        assertTrue(true);
	}

	@Test
	public void testValidateV6() throws Exception {
		JsonNode schemaNode = JsonDocument.parse(ResourceUtil.readTextResource("/participation_schema_v06.json"));
		JsonNode dataNode = JsonDocument.parse(ResourceUtil.readTextResource("/participation_testdata.json"));
		String schemaId = "0";
		JsonDocument.setJsonSchema(schemaId, schemaNode);
		JsonDocument doc = new JsonDocument(dataNode);
		System.out.println(doc.validate(schemaId));
        assertTrue(true);
	}

	@Test
	public void testValidateV7() throws Exception {
		JsonNode schemaNode = JsonDocument.parse(ResourceUtil.readTextResource("/participation_schema_v07.json"));
		JsonNode dataNode = JsonDocument.parse(ResourceUtil.readTextResource("/participation_testdata.json"));
		String schemaId = "0";
		JsonDocument.setJsonSchema(schemaId, schemaNode);
		JsonDocument doc = new JsonDocument(dataNode);
		System.out.println(doc.validate(schemaId));
        assertTrue(true);
	}

	@Test
	public void testValidateUsingJsonDocument() throws Exception {
		JsonDocument doc = new JsonDocument(ResourceUtil.readTextResource("/participation_testdata.json"));
		String schemaId = "project.jobName";
		JsonDocument.setJsonSchema(schemaId, ResourceUtil.readTextResource("/participation_schema_v04.json"));
		doc.validate(schemaId);
		Set<ValidationMessage> report = doc.getLastValidationReport();
		for (com.networknt.schema.ValidationMessage m : report) {
			System.out.println("type: " + m.getType());
			System.out.println("path: " + m.getPath());
			System.out.println("code: " + m.getCode());
			System.out.println("details: " + m.getDetails());
			System.out.println("arguments: " + Util.toString(m.getArguments()));
			System.out.println("message: " + m.getMessage());
			System.out.println("-----------------------------------------------------");
		}
		assertEquals(6, report.size());
	}

	@Test
	public void testValidateV7WithDefinitions() throws Exception {
		JsonNode schemaNode = JsonDocument.parse(ResourceUtil.readTextResource("/addresses_schema_with_definitions.json"));
		JsonNode dataNode = JsonDocument.parse(ResourceUtil.readTextResource("/addresses_testdata.json"));
		String schemaId = "0";
		JsonDocument.setJsonSchema(schemaId, schemaNode);
		JsonDocument doc = new JsonDocument(dataNode);
		System.out.println(doc.validate(schemaId));
        assertTrue(true);
	}

	@Test
	public void testValidateV7WithReferences() throws Exception {
		JsonNode schemaNode = JsonDocument.parse(ResourceUtil.readTextResource("/addresses_schema_with_external_refs.json"));
		JsonNode dataNode = JsonDocument.parse(ResourceUtil.readTextResource("/addresses_testdata.json"));
		String schemaId = "0";
		JsonDocument.setJsonSchema(schemaId, schemaNode);
		JsonDocument doc = new JsonDocument(dataNode);
		System.out.println(doc.validate(schemaId));
        assertTrue(true);
	}

}
