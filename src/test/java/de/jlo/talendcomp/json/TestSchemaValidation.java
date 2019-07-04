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

	String jsonSchemaStr = "{\n"
		    + "    \"$schema\": \"http://json-schema.org/draft-04/schema#\",\n"
		    + "    \"definitions\": {},\n"
		    + "    \"id\": \"http://example.com/example.json\",\n"
		    + "    \"properties\": {\n"
		    + "        \"created_by\": {\n"
		    + "            \"id\": \"/properties/created_by\",\n"
		    + "            \"type\": \"integer\"\n"
		    + "        },\n"
		    + "        \"data_status_id\": {\n"
		    + "            \"id\": \"/properties/data_status_id\",\n"
		    + "            \"type\": \"integer\"\n"
		    + "        },\n"
		    + "        \"participation\": {\n"
		    + "            \"id\": \"/properties/participation\",\n"
		    + "            \"properties\": {\n"
		    + "                \"function_id\": {\n"
		    + "                    \"id\": \"/properties/participation/properties/function_id\",\n"
		    + "                    \"type\": \"integer\"\n"
		    + "                },\n"
		    + "                \"participant\": {\n"
		    + "                    \"id\": \"/properties/participation/properties/participant\",\n"
		    + "                    \"properties\": {\n"
		    + "                        \"participant_id\": {\n"
		    + "                            \"id\": \"/properties/participation/properties/participant/properties/participant_id\",\n"
		    + "                            \"type\": \"integer\"\n"
		    + "                        }\n"
		    + "                    },\n"
		    + "                    \"type\": \"object\"\n"
		    + "                },\n"
		    + "                \"participantname\": {\n"
		    + "                    \"id\": \"/properties/participation/properties/participantname\",\n"
		    + "                    \"properties\": {\n"
		    + "                        \"firstname\": {\n"
		    + "                            \"id\": \"/properties/participation/properties/participantname/properties/firstname\",\n"
		    + "                            \"type\": [\"string\",\"null\"]\n"
		    + "                        },\n"
		    + "                        \"firstname_supplement\": {\n"
		    + "                            \"id\": \"/properties/participation/properties/participantname/properties/firstname_supplement\",\n"
		    + "                            \"type\": [\"string\",\"null\"]\n"
		    + "                        },\n"
		    + "                        \"name\": {\n"
		    + "                            \"id\": \"/properties/participation/properties/participantname/properties/name\",\n"
		    + "                            \"type\": [\"string\",\"null\"]\n"
		    + "                        },\n"
		    + "                        \"name_supplement\": {\n"
		    + "                            \"id\": \"/properties/participation/properties/participantname/properties/name_supplement\",\n"
		    + "                            \"type\": [\"string\",\"null\"]\n"
		    + "                        },\n"
		    + "                        \"nametype_id\": {\n"
		    + "                            \"id\": \"/properties/participation/properties/participantname/properties/nametype_id\",\n"
		    + "                            \"type\": [\"integer\",\"null\"]\n"
		    + "                        },\n"
		    + "                        \"participant_name_id\": {\n"
		    + "                            \"id\": \"/properties/participation/properties/participantname/properties/participant_name_id\",\n"
		    + "                            \"type\": [\"integer\",\"null\"]\n"
		    + "                        },\n"
		    + "                        \"salutation\": {\n"
		    + "                            \"id\": \"/properties/participation/properties/participantname/properties/salutation\",\n"
		    + "                            \"type\": [\"string\",\"null\"]\n"
		    + "                        },\n"
		    + "                        \"title\": {\n"
		    + "                            \"id\": \"/properties/participation/properties/participantname/properties/title\",\n"
		    + "                            \"type\": [\"string\",\"null\"]\n"
		    + "                        }\n"
		    + "                    },\n"
		    + "                    \"type\": \"object\"\n"
		    + "                },\n"
		    + "                \"participation_date\": {\n"
		    + "                    \"id\": \"/properties/participation/properties/participation_date\",\n"
		    + "                    \"type\": \"string\"\n"
		    + "                },\n"
		    + "                \"product_id\": {\n"
		    + "                    \"id\": \"/properties/participation/properties/product_id\",\n"
		    + "                    \"type\": \"integer\"\n"
		    + "                },\n"
		    + "                \"pseudonym\": {\n"
		    + "                    \"id\": \"/properties/participation/properties/pseudonym\",\n"
		    + "                    \"properties\": {\n"
		    + "                        \"firstname\": {\n"
		    + "                            \"id\": \"/properties/participation/properties/pseudonym/properties/firstname\",\n"
		    + "                            \"type\": \"string\"\n"
		    + "                        },\n"
		    + "                        \"firstname_supplement\": {\n"
		    + "                            \"id\": \"/properties/participation/properties/pseudonym/properties/firstname_supplement\",\n"
		    + "                            \"type\": \"null\"\n"
		    + "                        },\n"
		    + "                        \"name\": {\n"
		    + "                            \"id\": \"/properties/participation/properties/pseudonym/properties/name\",\n"
		    + "                            \"type\": \"string\"\n"
		    + "                        },\n"
		    + "                        \"name_supplement\": {\n"
		    + "                            \"id\": \"/properties/participation/properties/pseudonym/properties/name_supplement\",\n"
		    + "                            \"type\": \"string\"\n"
		    + "                        },\n"
		    + "                        \"nametype_id\": {\n"
		    + "                            \"id\": \"/properties/participation/properties/pseudonym/properties/nametype_id\",\n"
		    + "                            \"type\": \"integer\"\n"
		    + "                        },\n"
		    + "                        \"pseudonym_id\": {\n"
		    + "                            \"id\": \"/properties/participation/properties/pseudonym/properties/pseudonym_id\",\n"
		    + "                            \"type\": \"integer\"\n"
		    + "                        },\n"
		    + "                        \"salutation\": {\n"
		    + "                            \"id\": \"/properties/participation/properties/pseudonym/properties/salutation\",\n"
		    + "                            \"type\": \"string\"\n"
		    + "                        },\n"
		    + "                        \"title\": {\n"
		    + "                            \"id\": \"/properties/participation/properties/pseudonym/properties/title\",\n"
		    + "                            \"type\": \"string\"\n"
		    + "                        }\n"
		    + "                    },\n"
		    + "                    \"type\": \"object\"\n"
		    + "                },\n"
		    + "                \"remarks\": {\n"
		    + "                    \"id\": \"/properties/participation/properties/remarks\",\n"
		    + "                    \"items\": {\n"
		    + "                        \"id\": \"/properties/participation/properties/remarks/items\",\n"
		    + "                        \"properties\": {\n"
		    + "                            \"remark\": {\n"
		    + "                                \"id\": \"/properties/participation/properties/remarks/items/properties/remark\",\n"
		    + "                                \"type\": \"string\"\n"
		    + "                            },\n"
		    + "                            \"remark_type_id\": {\n"
		    + "                                \"id\": \"/properties/participation/properties/remarks/items/properties/remark_type_id\",\n"
		    + "                                \"type\": \"integer\"\n"
		    + "                            }\n"
		    + "                        },\n"
		    + "                        \"type\": \"object\"\n"
		    + "                    },\n"
		    + "                    \"type\": \"array\"\n"
		    + "                },\n"
		    + "                \"role_id\": {\n"
		    + "                    \"id\": \"/properties/participation/properties/role_id\",\n"
		    + "                    \"type\": \"integer\"\n"
		    + "                },\n"
		    + "                \"rolenames\": {\n"
		    + "                    \"id\": \"/properties/participation/properties/rolenames\",\n"
		    + "                    \"items\": {\n"
		    + "                        \"id\": \"/properties/participation/properties/rolenames/items\",\n"
		    + "                        \"properties\": {\n"
		    + "                            \"default_selection\": {\n"
		    + "                                \"id\": \"/properties/participation/properties/rolenames/items/properties/default_selection\",\n"
		    + "                                \"type\": \"boolean\"\n"
		    + "                            },\n"
		    + "                            \"language_id\": {\n"
		    + "                                \"id\": \"/properties/participation/properties/rolenames/items/properties/language_id\",\n"
		    + "                                \"type\": \"integer\"\n"
		    + "                            },\n"
		    + "                            \"rolename\": {\n"
		    + "                                \"id\": \"/properties/participation/properties/rolenames/items/properties/rolename\",\n"
		    + "                                \"type\": \"string\"\n"
		    + "                            }\n"
		    + "                        },\n"
		    + "                        \"additionalProperties\": false,\n"
		    + "                        \"type\": \"object\"\n"
		    + "                    },\n"
		    + "                    \"type\": \"array\"\n"
		    + "                },\n"
		    + "                \"shooting_days\": {\n"
		    + "                    \"id\": \"/properties/participation/properties/shooting_days\",\n"
		    + "                    \"type\": \"integer\"\n"
		    + "                },\n"
		    + "                \"source_id\": {\n"
		    + "                    \"id\": \"/properties/participation/properties/source_id\",\n"
		    + "                    \"type\": \"integer\"\n"
		    + "                }\n"
		    + "            },\n"
		    + "            \"type\": \"object\"\n"
		    + "        },\n"
		    + "        \"process_status_id\": {\n"
		    + "            \"id\": \"/properties/process_status_id\",\n"
		    + "            \"type\": \"integer\"\n"
		    + "        }\n"
		    + "    },\n"
		    + "    \"required\": [\"participation\"],\n"
		    + "    \"additionalProperties\": false,\n"
		    + "    \"type\": \"object\"\n"
		    + "}";
	
	@Test
	public void testValidate() throws Exception {
		JsonNode schemaNode = JsonDocument.parse(jsonSchemaStr);
		JsonNode dataNode = JsonDocument.parse(jsonDocStr);
		String schemaId = "0";
		JsonDocument.setJsonSchema(schemaId, schemaNode);
		JsonDocument doc = new JsonDocument(dataNode);
		System.out.println(doc.validate(schemaId));
        assertTrue(true);
	}

	@Test
	public void testValidateUsingJsonDocument() throws Exception {
		JsonDocument doc = new JsonDocument(jsonDocStr);
		String schemaId = "project.jobName";
		JsonDocument.setJsonSchema(schemaId, jsonSchemaStr);
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

}
