package de.jlo.talendcomp.json;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class TestAddressArray {

	Map<String, Object> globalMap = new HashMap<>();
	
	@Before
	public void setupDocument() {
		System.out.println("Setup document...");
		try {
			String json = "{\n"
				    + " \"businesspartner\":{\n"
				    + "   \"businessobject_id\":1010,\n"
				    + "   \"comchannels\":[\n"
				    + "     {\n"
				    + "       \"postaladdresses\":[\n"
				    + "         {\n"
				    + "          \"info\":\"p11\",\n"
				    + "          \"name_supplement\":\"NameSupppp\",\n"
				    + "          \"street\":\"TestStreet\",\n"
				    + "          \"street_supplement\":\"streetSuppp\",\n"
				    + "          \"houseno\":111,\n"
				    + "          \"zipcode\":\"01111\",\n"
				    + "          \"city\":\"city\",\n"
				    + "          \"addresstype_sysname\":\"po_box\",\n"
				    + "          \"preferred\":false,\n"
				    + "          \"operation\":\"update\",\n"
				    + "          \"communication_channel_id\":178804\n"
				    + "         },\n"
				    + "         {\n"
				    + "          \"info\":\"p12\",\n"
				    + "          \"name_supplement\":\"NameSupp2\",\n"
				    + "          \"street\":\"TestStreet\",\n"
				    + "          \"street_supplement\":\"streetSupp2\",\n"
				    + "          \"houseno\":222,\n"
				    + "          \"zipcode\":\"01111\",\n"
				    + "          \"city\":\"city\",\n"
				    + "          \"addresstype_sysname\":\"po_box\",\n"
				    + "          \"preferred\":false,\n"
				    + "          \"operation\":\"update\",\n"
				    + "          \"communication_channel_id\":178804\n"
				    + "         }\n"
				    + "       ],\n"
				    + "       \"officeaddresses\":[\n"
				    + "         {\n"
				    + "          \"info\":\"o11\",\n"
				    + "          \"name_supplement\":\"NameSupppp\",\n"
				    + "          \"street\":\"TestStreet\",\n"
				    + "          \"street_supplement\":\"streetSuppp\",\n"
				    + "          \"houseno\":333,\n"
				    + "          \"zipcode\":\"01111\",\n"
				    + "          \"city\":\"city\",\n"
				    + "          \"addresstype_sysname\":\"po_box\",\n"
				    + "          \"preferred\":false,\n"
				    + "          \"operation\":\"update\",\n"
				    + "          \"communication_channel_id\":178804\n"
				    + "         },\n"
				    + "         {\n"
				    + "          \"info\":\"o12\",\n"
				    + "          \"name_supplement\":\"NameSupp2\",\n"
				    + "          \"street\":\"TestStreet\",\n"
				    + "          \"street_supplement\":\"streetSupp2\",\n"
				    + "          \"houseno\":444,\n"
				    + "          \"zipcode\":\"01111\",\n"
				    + "          \"city\":\"city\",\n"
				    + "          \"addresstype_sysname\":\"po_box\",\n"
				    + "          \"preferred\":false,\n"
				    + "          \"operation\":\"update\",\n"
				    + "          \"communication_channel_id\":178804\n"
				    + "         }\n"
				    + "       ]\n"
				    + "     },\n"
				    + "     {\n"
				    + "       \"postaladdresses\":[\n"
				    + "         {\n"
				    + "          \"info\":\"p21\",\n"
				    + "          \"name_supplement\":\"NameSupppp\",\n"
				    + "          \"street\":\"TestStreet\",\n"
				    + "          \"street_supplement\":\"streetSuppp\",\n"
				    + "          \"houseno\":555,\n"
				    + "          \"zipcode\":\"01111\",\n"
				    + "          \"city\":\"city\",\n"
				    + "          \"addresstype_sysname\":\"po_box\",\n"
				    + "          \"preferred\":false,\n"
				    + "          \"operation\":\"update\",\n"
				    + "          \"communication_channel_id\":178804\n"
				    + "         },\n"
				    + "         {\n"
				    + "          \"info\":\"p22\",\n"
				    + "          \"name_supplement\":\"NameSupp2\",\n"
				    + "          \"street\":\"TestStreet\",\n"
				    + "          \"street_supplement\":\"streetSupp2\",\n"
				    + "          \"houseno\":666,\n"
				    + "          \"zipcode\":\"01111\",\n"
				    + "          \"city\":\"city\",\n"
				    + "          \"addresstype_sysname\":\"po_box\",\n"
				    + "          \"preferred\":false,\n"
				    + "          \"operation\":\"update\",\n"
				    + "          \"communication_channel_id\":178804\n"
				    + "         }\n"
				    + "       ],\n"
				    + "       \"officeaddresses\":[\n"
				    + "         {\n"
				    + "          \"info\":\"o21\",\n"
				    + "          \"name_supplement\":\"NameSupppp\",\n"
				    + "          \"street\":\"TestStreet\",\n"
				    + "          \"street_supplement\":\"streetSuppp\",\n"
				    + "          \"houseno\":777,\n"
				    + "          \"zipcode\":\"01111\",\n"
				    + "          \"city\":\"city\",\n"
				    + "          \"addresstype_sysname\":\"po_box\",\n"
				    + "          \"preferred\":false,\n"
				    + "          \"operation\":\"update\",\n"
				    + "          \"communication_channel_id\":178804\n"
				    + "         },\n"
				    + "         {\n"
				    + "          \"info\":\"o22\",\n"
				    + "          \"name_supplement\":\"NameSupp2\",\n"
				    + "          \"street\":\"TestStreet\",\n"
				    + "          \"street_supplement\":\"streetSupp2\",\n"
				    + "          \"houseno\":888,\n"
				    + "          \"zipcode\":\"01111\",\n"
				    + "          \"city\":\"city\",\n"
				    + "          \"addresstype_sysname\":\"po_box\",\n"
				    + "          \"preferred\":false,\n"
				    + "          \"operation\":\"update\",\n"
				    + "          \"communication_channel_id\":178804\n"
				    + "         }\n"
				    + "       ]\n"
				    + "     }\n"
				    + "   ]\n"
				    + " }\n"
				    + "}";
			de.jlo.talendcomp.json.JsonDocument tJSONDocOpen_1 = new de.jlo.talendcomp.json.JsonDocument(
					json);
			globalMap.put("tJSONDocOpen_1", tJSONDocOpen_1);
			globalMap.put("tJSONDocOpen_1_CURRENT_NODE",
					tJSONDocOpen_1.getRootNode());
			globalMap.put("tJSONDocOpen_1_ROOT_NODE",
					tJSONDocOpen_1.getRootNode());
		} catch (Exception e) {
			globalMap.put("tJSONDocOpen_1_ERROR_MESSAGE",
					e.getMessage());
			assertTrue("Failed: " + e.getMessage(), false);
		}
		
	}

	@Test
	public void testUseJsonPathStartingWithAnObject() {
		System.out.println("testUseJsonPathStartingWithAnObject...");
		de.jlo.talendcomp.json.JsonDocument tJSONDocInput_1 = (de.jlo.talendcomp.json.JsonDocument) globalMap
				.get("tJSONDocOpen_1");
		com.fasterxml.jackson.databind.JsonNode bpnode = tJSONDocInput_1
				.getNode("$.businesspartner");
		System.out.println(bpnode);
		assertNotNull(bpnode);
		com.fasterxml.jackson.databind.JsonNode panode = tJSONDocInput_1
				.getNode(bpnode, "comchannels[0].officeaddresses");
		System.out.println(panode);
		assertNotNull(panode);
		com.fasterxml.jackson.databind.JsonNode onepanode = tJSONDocInput_1
				.getNode(panode, "[?(@.houseno == '444')]");
		System.out.println(onepanode);
		assertTrue(onepanode.size() == 1);
	}

	@Test
	public void testJumpOverArray() throws Exception {
		System.out.println("testJumpOverArray...");
		de.jlo.talendcomp.json.JsonDocument tJSONDocInput_1 = (de.jlo.talendcomp.json.JsonDocument) globalMap
				.get("tJSONDocOpen_1");
		com.fasterxml.jackson.databind.JsonNode bpnode = tJSONDocInput_1
				.getNode("$.businesspartner");
		System.out.println(bpnode);
		assertNotNull(bpnode);
		com.fasterxml.jackson.databind.JsonNode panode = tJSONDocInput_1
				.getNode(bpnode, "comchannels[1].postaladdresses", true);
		System.out.println(panode);
		assertNotNull(panode);
	}

}
