package de.cimt.talendcomp.json;

import static org.junit.Assert.assertTrue;

import org.junit.Before;

import de.cimt.talendcomp.test.TalendFakeJob;

public class TestAddressArray extends TalendFakeJob {

	
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
				    + "          \"info\":\"flad\",\n"
				    + "          \"name_supplement\":\"NameSupppp\",\n"
				    + "          \"street\":\"TestStreet\",\n"
				    + "          \"street_supplement\":\"streetSuppp\",\n"
				    + "          \"houseno\":666,\n"
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
			de.cimt.talendcomp.json.JsonDocument tJSONDocOpen_1 = new de.cimt.talendcomp.json.JsonDocument(
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

/*	@Test
	public void testJumpOverArray() {
		System.out.println("testJumpOverArray...");
		de.cimt.talendcomp.json.JsonDocument tJSONDocInput_1 = (de.cimt.talendcomp.json.JsonDocument) globalMap
				.get("tJSONDocOpen_1");
		com.fasterxml.jackson.databind.JsonNode bpnode = tJSONDocInput_1
				.getNode("$.businesspartner");
		System.out.println(bpnode);
		assertNotNull(bpnode);
		com.fasterxml.jackson.databind.JsonNode panode = tJSONDocInput_1
				.getNode("comchannels.postaladdresses");
		System.out.println(panode);
		assertNotNull(panode);
	}
*/	
}
