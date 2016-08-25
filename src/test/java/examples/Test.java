package examples;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;

import de.cimt.talendcomp.json.JsonDocument;

public class Test {

	public static void main(String[] args) {
		try {
			//createJsonProduct();
			//navigate();
			//testJsonPath();
			//testJsonPathNew();
			//testBuildJson();
			//testCreateObjects();
//			testIgnoreNullValue();
			//testGetDirectPathTokens();
			int i = 0;
//			Double d = (Double) 2;
			playWithStrings();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void testGetDirectPathTokens() throws Exception {
    	DocumentContext context = JsonPath.using(JACKSON_JSON_NODE_CONFIGURATION).parse("{}");
        ObjectNode node1 = context.read("$");
        ObjectNode child1 = new ObjectNode(JsonNodeFactory.instance);
        child1.put("name", "test");
        context.put("$", "child", child1);
        ObjectNode node2 = context.read("$");
        ObjectNode child2 = context.read("$.child");
        if (node1 != node2) {
        	throw new Exception("Node1 out of sync");
        }
        if (child1 != child2) {
        	throw new Exception("child1 out of sync");
        }
        System.out.println("OK");
	}

	public static void testCreateObjects() throws Exception {
		JsonDocument doc = new JsonDocument(true);
		System.out.println(doc.toString());
		JsonNode root1 = doc.getNode("$");
		JsonNode root2 = doc.getDocumentContext().json();
		if (root1 != root2) {
			throw new Exception("Out of sync (1)");
		}
		String jsonPath = "$.store.book[0].author.names[0]";
		JsonNode n1 = doc.getNode(jsonPath, true);
		System.out.println(doc.toString());
		String jsonPath2 = "$.store.book[0].author.names[0]";
		JsonNode n2 = doc.getNode(jsonPath2, true);
		System.out.println(doc.getJsonString(true, false));
		if (n1 != n2) {
			throw new Exception("Out of sync (2)");
		}
	}
	
	public static void createJsonProduct() throws JsonProcessingException, IOException {
		ArrayNode rootNode = new ArrayNode(JsonNodeFactory.instance);
		ObjectNode pn = new ObjectNode(JsonNodeFactory.instance);
		pn
			.put("name", "Lash Base")
			.put("taxId", 1)
			.put("description", 1)
			.put("descriptionLong", 1)
			.put("active", 1)
			.put("highlight", 1)
			.put("keywords", 1)
			.put("metaTitle", 1)
			.put("availableFrom", 1)
			.put("availableTo", 1)
			.put("categories", 1)
			.withArray("similar")
				.add(2334455)
				.add(12345);
		pn
			.put("related", 1)
			.put("supplier", "Essence");
		rootNode.add(pn);
		ObjectNode mainDetailNode = pn
			.with("mainDetail");
		mainDetailNode
				.put("number", "827009")
				.put("weight", 100)
				.put("ean", "4250587776126")
				.put("releaseDate", "4250587776126")
				.put("purchaseUnit", "4250587776126");
		mainDetailNode
			.withArray("prices")
				.add(
					new ObjectNode(JsonNodeFactory.instance)
						.put("customerGroupKey", "EK")
						.put("price", 2.29d)
						.put("pseudoPrice", 2.49d));
		ObjectNode optionsNode = new ObjectNode(JsonNodeFactory.instance)
			.put("name", "dgroupSizeInfo");
		pn.with("configuratorSet");
//			.withArray("groups")
//				.add(optionsNode);
		ArrayNode optionsArrayNode = optionsNode.withArray("options");
		mainDetailNode
			.with("attributes")
				.put("dgroupSizeInfo", "9 ml")
				.put("dgroupGender", "9 ml")
				.put("dgroupSkinType", "9 ml")
				.put("dgroupSapGroup", "9 ml")
				.put("dgroupCountyOfOrigin", "9 ml")
				.put("dgroupUnitId", "9 ml")
				.put("dgroupNumberOfContentUnits", "9 ml")
				.put("dgroupCharacteristic", "9 ml")
				.put("dgroupNew", 0)
				.put("dgroupDouglasUrl", "9 ml")
				.put("dgroupDeliveryRoute", "9 ml")
				.put("dgroupAbcArticle", "9 ml")
				.put("dgroupBoxArticle", "9 ml")
				.put("dgroupArticleText", "9 ml")
				.put("dgroupLineNumber", "9 ml")
				.put("dgroupLineName", "9 ml")
				.put("dgroupDepotNumber", "9 ml")
				.put("dgroupDepotName", "9 ml")
				.put("dgroupUwgNumber", "9 ml")
				.put("dgroupUwgName", "9 ml")
				.put("dgroupWgNumber", "9 ml")
				.put("dgroupWgName", "9 ml")
				.put("dgroupArticleType", "9 ml")
				.put("dgroupImage1", "9 ml")
				.put("dgroupImage2", "9 ml")
				.put("dgroupImage3", "")
				.put("dgroupImage4", "")
				.put("dgroupIngredients", "9 ml")
				.put("dgroupUsage", "9 ml")
				.set("dgroupIdPim", null);
		ObjectMapper m = new ObjectMapper();
		System.out.println(m
				.setSerializationInclusion(Include.NON_NULL)
				.writerWithDefaultPrettyPrinter()
				.writeValueAsString(rootNode));
	}
	
	public static void testIgnoreNullValue() throws Exception {
		ObjectNode rootNode = new ObjectNode(JsonNodeFactory.instance);
		rootNode.put("nonEmpty", "value");
		rootNode.set("nullValue", null);
		rootNode.withArray("nonEmptyArray").add(1).add(2);
		rootNode.withArray("emptyArray");
		ArrayNode nonEmptyArray = new ArrayNode(JsonNodeFactory.instance);
		nonEmptyArray.add("v1").add("v2");
		ArrayNode emptyArray1 = new ArrayNode(JsonNodeFactory.instance);
		ArrayNode emptyArray2 = new ArrayNode(JsonNodeFactory.instance);
		rootNode.withArray("array").add(nonEmptyArray).add(emptyArray1.add(emptyArray2));

		ArrayNode rootArray = new ArrayNode(JsonNodeFactory.instance);
		rootArray.add(rootNode);

		ObjectMapper m = new ObjectMapper();
		System.out.println(m
				.setSerializationInclusion(Include.NON_EMPTY)
				.writerWithDefaultPrettyPrinter()
				.writeValueAsString(rootArray));
	}

	public static void createJson() throws JsonProcessingException, IOException {
		ObjectNode rootNode = new ObjectNode(JsonNodeFactory.instance);
		rootNode.put("a1", "v1");
		ArrayNode array = rootNode.arrayNode();
		rootNode.set("variants", array);

		array.objectNode().put("name", "vname2");
		array.add("hans");
		array.add("pech");

		System.out.println(rootNode);
	}

	static String json = "[\n"
			+ "	{\n"
			+ "		\"name\":\"Hypn\u00f4se\",\n"
			+ "		\"taxId\":4,\n"
			+ "		\"description\":\"Ich bin eine Meta Desciption\",\n"
			+ "		\"descriptionLong\":\"Die erste Grundierung für die Wimpern von Essence: Die Lash Base wird unter der Mascara getragen und verlängert und verdichtet die Wimpern. Auch für Kontaklinsentrger/-innen geeignet.\",\n"
			+ "		\"highlight\":true,\n"
			+ "		\"keywords\":\"keyword 1, keyword 2\",\n"
			+ "		\"metaTitle\":\"Ich bin ein Meta Title\",\n"
			+ "		\"availableFrom\":\"2015-03-11T00:00:00+0100\",\n"
			+ "		\"availableTo\":\"2015-08-18T00:00:00+0100\",\n"
			+ "		\"categories\":[],\n"
			+ "		\"similar\":[],\n"
			+ "		\"related\":[],\n"
			+ "		\"mainDetail\":\n"
			+ "		{\n"
			+ "			\"number\":\"212616\"\n"
			+ "		},\n"
			+ "		\"supplier\":\"Lanc\u00d9me\",\n"
			+ "		\"configuratorSet\":\n"
			+ "		{\n"
			+ "			\"groups\":\n"
			+ "			[\n"
			+ "				{\n"
			+ "					\"name\":\"dgroupSizeInfo\",\n"
			+ "					\"options\":\n"
			+ "					[\n"
			+ "						{\n"
			+ "							\"name\":\"30 ml\"\n"
			+ "						},\n"
			+ "						{\n"
			+ "							\"name\":\"50 ml\"\n"
			+ "						},\n"
			+ "						{\n"
			+ "							\"name\":\"75 ml\"\n"
			+ "						}\n"
			+ "					]\n"
			+ "				}\n"
			+ "			]\n"
			+ "		},\n"
			+ "		\"variants\":\n"
			+ "		[\n"
			+ "			{\n"
			+ "				\"isMain\":true,\n"
			+ "				\"active\":true,\n"
			+ "				\"number\":\"212616\",\n"
			+ "				\"weight\":60,\n"
			+ "				\"ean\":\"3147758235548\",\n"
			+ "				\"releaseDate\":\"2015-04-16T00:00:00+0200\",\n"
			+ "				\"purchaseUnit\":\"100\",\n"
			+ "				\"attribute\":\n"
			+ "				{\n"
			+ "					\"dgroupUnitId\":\"ml\",\n"
			+ "					\"dgroupNumberOfContentUnits\":\"30\",\n"
			+ "					\"dgroupGender\":\"weiblich\",\n"
			+ "					\"dgroupSkinType\":\"hell\",\n"
			+ "					\"dgroupSapGroup\":\"DCA111511\",\n"
			+ "					\"dgroupCountyOfOrigin\":\"DE\",\n"
			+ "					\"dgroupCharacteristic\":\"beruhigend\",\n"
			+ "					\"dgroupNew\":0,\n"
			+ "					\"dgroupDouglasUrl\":\"Make-up-Augen-Wimperntusche-Essence-Mascara-Lash-Base-Lash-Base_product_827009.html\",\n"
			+ "					\"dgroupDeliveryRoute\":\"2\",\n"
			+ "					\"dgroupAbcArticle\":\"B\",\n"
			+ "					\"dgroupBoxArticle\":\"1\",\n"
			+ "					\"dgroupArticleText\":\"EDP VAPO\",\n"
			+ "					\"dgroupLineNumber\":\"1234\",\n"
			+ "					\"dgroupLineName\":\"10 HYPNOSE\",\n"
			+ "					\"dgroupDepotNumber\":\"4321\",\n"
			+ "					\"dgroupDepotName\":\"3 LANCOME\",\n"
			+ "					\"dgroupUwgNumber\":\"2261\",\n"
			+ "					\"dgroupUwgName\":\"SPRAY\",\n"
			+ "					\"dgroupWgNumber\":\"3000\",\n"
			+ "					\"dgroupWgName\":\"DAMENDUFT-LINIE\",\n"
			+ "					\"dgroupArticleType\":\"Liedschatten\",\n"
			+ "					\"dgroupImage1\":\"image1.jpg\",\n"
			+ "					\"dgroupImage2\":\"image2.jpg\",\n"
			+ "					\"dgroupImage3\":\"\",\n"
			+ "					\"dgroupImage4\":\"\",\n"
			+ "					\"dgroupIngredients\":\"Inhaltsstoffe Beschreibung\",\n"
			+ "					\"dgroupUsage\":\"Anwendungsbeschreibung\",\n"
			+ "					\"dgroupIdPim\":\"2001004164\"\n"
			+ "				},\n"
			+ "				\"configuratorOptions\":\n"
			+ "				[\n"
			+ "					{\n"
			+ "						\"group\":\"dgroupSizeInfo\",\n"
			+ "						\"group\":\"\",\n"
			+ "						\"option\":\"30 ml\"\n"
			+ "					}\n"
			+ "				],\n"
			+ "				\"prices\":\n"
			+ "				[\n"
			+ "					{\n"
			+ "						\"customerGroupKey\":\"EK\",\n"
			+ "						\"price\":49.99\n"
			+ "					}\n"
			+ "				]\n"
			+ "			},\n"
			+ "			{\n"
			+ "				\"isMain\":false,\n"
			+ "				\"active\":true,\n"
			+ "				\"number\":\"212617\",\n"
			+ "				\"weight\":60,\n"
			+ "				\"ean\":\"3147758235524\",\n"
			+ "				\"releaseDate\":\"2015-04-16T00:00:00+0200\",\n"
			+ "				\"purchaseUnit\":\"100\",\n"
			+ "				\"attribute\":\n"
			+ "				{\n"
			+ "					\"dgroupUnitId\":\"ml\",\n"
			+ "					\"dgroupNumberOfContentUnits\":\"50\",\n"
			+ "					\"dgroupGender\":\"weiblich\",\n"
			+ "					\"dgroupSkinType\":\"hell\",\n"
			+ "					\"dgroupSapGroup\":\"DCA111511\",\n"
			+ "					\"dgroupCountyOfOrigin\":\"DE\",\n"
			+ "					\"dgroupCharacteristic\":\"beruhigend\",\n"
			+ "					\"dgroupNew\":0,\n"
			+ "					\"dgroupDouglasUrl\":\"Make-up-Augen-Wimperntusche-Essence-Mascara-Lash-Base-Lash-Base_product_827009.html\",\n"
			+ "					\"dgroupDeliveryRoute\":\"2\",\n"
			+ "					\"dgroupAbcArticle\":\"B\",\n"
			+ "					\"dgroupBoxArticle\":\"1\",\n"
			+ "					\"dgroupArticleText\":\"EDP VAPO\",\n"
			+ "					\"dgroupLineNumber\":\"1234\",\n"
			+ "					\"dgroupLineName\":\"10 HYPNOSE\",\n"
			+ "					\"dgroupDepotNumber\":\"4321\",\n"
			+ "					\"dgroupDepotName\":\"3 LANCOME\",\n"
			+ "					\"dgroupUwgNumber\":\"2261\",\n"
			+ "					\"dgroupUwgName\":\"SPRAY\",\n"
			+ "					\"dgroupWgNumber\":\"3000\",\n"
			+ "					\"dgroupWgName\":\"DAMENDUFT-LINIE\",\n"
			+ "					\"dgroupArticleType\":\"Liedschatten\",\n"
			+ "					\"dgroupImage1\":\"image1.jpg\",\n"
			+ "					\"dgroupImage2\":\"image2.jpg\",\n"
			+ "					\"dgroupImage3\":\"\",\n"
			+ "					\"dgroupImage4\":\"\",\n"
			+ "					\"dgroupIngredients\":\"Inhaltsstoffe Beschreibung\",\n"
			+ "					\"dgroupUsage\":\"Anwendungsbeschreibung\",\n"
			+ "					\"dgroupIdPim\":\"2001004164\"\n"
			+ "				},\n"
			+ "				\"configuratorOptions\":\n"
			+ "				[\n"
			+ "					{\n"
			+ "						\"group\":\"dgroupSizeInfo\",\n"
			+ "						\"option\":\"50 ml\"\n"
			+ "					}\n"
			+ "				],\n"
			+ "				\"prices\":\n"
			+ "				[\n"
			+ "					{\n"
			+ "						\"customerGroupKey\":\"EK\",\n"
			+ "						\"price\":79.95\n"
			+ "					}\n"
			+ "				]\n"
			+ "			},\n"
			+ "			{\n"
			+ "				\"isMain\":false,\n"
			+ "				\"number\":\"240527\",\n"
			+ "				\"active\":true,\n"
			+ "				\"weight\":60,\n"
			+ "				\"ean\":\"3147758235500\",\n"
			+ "				\"releaseDate\":\"2015-04-16T00:00:00+0200\",\n"
			+ "				\"purchaseUnit\":\"100\",\n"
			+ "				\"attribute\":\n"
			+ "				{\n"
			+ "					\"dgroupUnitId\":\"ml\",\n"
			+ "					\"dgroupNumberOfContentUnits\":\"75\",\n"
			+ "					\"dgroupGender\":\"weiblich\",\n"
			+ "					\"dgroupSkinType\":\"hell\",\n"
			+ "					\"dgroupSapGroup\":\"DCA111511\",\n"
			+ "					\"dgroupCountyOfOrigin\":\"DE\",\n"
			+ "					\"dgroupCharacteristic\":\"beruhigend\",\n"
			+ "					\"dgroupNew\":0,\n"
			+ "					\"dgroupDouglasUrl\":\"Make-up-Augen-Wimperntusche-Essence-Mascara-Lash-Base-Lash-Base_product_827009.html\",\n"
			+ "					\"dgroupDeliveryRoute\":\"2\",\n"
			+ "					\"dgroupAbcArticle\":\"B\",\n"
			+ "					\"dgroupBoxArticle\":\"1\",\n"
			+ "					\"dgroupArticleText\":\"EDP VAPO\",\n"
			+ "					\"dgroupLineNumber\":\"1234\",\n"
			+ "					\"dgroupLineName\":\"10 HYPNOSE\",\n"
			+ "					\"dgroupDepotNumber\":\"4321\",\n"
			+ "					\"dgroupDepotName\":\"3 LANCOME\",\n"
			+ "					\"dgroupUwgNumber\":\"2261\",\n"
			+ "					\"dgroupUwgName\":\"SPRAY\",\n"
			+ "					\"dgroupWgNumber\":\"3000\",\n"
			+ "					\"dgroupWgName\":\"DAMENDUFT-LINIE\",\n"
			+ "					\"dgroupArticleType\":\"Liedschatten\",\n"
			+ "					\"dgroupImage1\":\"image1.jpg\",\n"
			+ "					\"dgroupImage2\":\"image2.jpg\",\n"
			+ "					\"dgroupImage3\":\"\",\n"
			+ "					\"dgroupImage4\":\"\",\n"
			+ "					\"dgroupIngredients\":\"Inhaltsstoffe Beschreibung\",\n"
			+ "					\"dgroupUsage\":\"Anwendungsbeschreibung\",\n"
			+ "					\"dgroupIdPim\":\"2001004164\"\n" 
			+ "				},\n"
			+ "				\"configuratorOptions\":\n" 
			+ "				[\n" 
			+ "					{\n"
			+ "						\"group\":\"dgroupSizeInfo\",\n"
			+ "						\"option\":\"75 ml\"\n" 
			+ "					}\n" 
			+ "				],\n"
			+ "				\"prices\":\n" 
			+ "				[\n" 
			+ "					{\n"
			+ "						\"customerGroupKey\":\"EK\",\n"
			+ "						\"price\":98\n" 
			+ "					}\n" 
			+ "				]\n" 
			+ "			}\n"
			+ "		]\n" 
			+ "	}\n" 
			+ "]";

	public static void navigate() throws Exception {
		ObjectMapper m = new ObjectMapper();
		JsonNode root = m.readTree(json);
		// Step by step
		JsonNode product = root.get(0);
		JsonNode variantArray = product.path("variants");
		JsonNode variantNode = variantArray.get(0);
		JsonNode configOptionArrayNode = variantNode.get("configuratorOptions");
		JsonNode configOptionNode = configOptionArrayNode.get(0);
		JsonNode groupNode = configOptionNode.path("group");
		String group = null;
		if (groupNode == null) {
			System.out.println("no group node");
		} else {
			System.out.println("is missing=" + groupNode.isMissingNode());
			group = groupNode.asText();
			System.out.println("step-by-step group=" + group);
		}
		// example for a complete path
		group = root.path(0).path("variants").path(0).path("configuratorOptions").path(0).path("group").asText();
		System.out.println("all-at-once group=" + group);
	}
	
	public static final Configuration JACKSON_JSON_NODE_CONFIGURATION = Configuration
            .builder()
            .mappingProvider(new JacksonMappingProvider())
            .jsonProvider(new JacksonJsonNodeJsonProvider())
            .build();
	
	public static void testJsonPath() throws Exception {
		DocumentContext dc = JsonPath.using(JACKSON_JSON_NODE_CONFIGURATION).parse(json);
		JsonNode groupNode = dc.read("$[0].variants");
		System.out.println(groupNode);
		System.out.println();
		groupNode = dc.read("$[0].variants[0]");
		System.out.println(groupNode);
		System.out.println();
		dc.add("$[0].variants", groupNode);
		groupNode = dc.read("$[0].variants");
		System.out.println(groupNode);
	}
	
	public static void testJsonPathNew() throws Exception {
		DocumentContext dc = JsonPath.using(JACKSON_JSON_NODE_CONFIGURATION).parse("[]");
		ObjectNode n1 = new ObjectNode(JsonNodeFactory.instance);
		n1.put("a1", "v1");
		dc.add("$", n1);
		ObjectNode n2 = new ObjectNode(JsonNodeFactory.instance);
		n2.put("a21", "v21");
		dc.put("$[0].x.y","a2", n2);
		System.out.println(n1);
		ObjectNode n11 = dc.read("$[0]");
		System.out.println(n11);
		System.out.println(dc.jsonString());
	}

	public static void testBuildJson() throws Exception {
		JsonDocument doc = new JsonDocument(false);
		doc.createArrayNode("an");
		System.out.println(doc.toString());
		System.out.println(doc.getDocumentContext().jsonString());
	}

	public static void playWithStrings() {
		String test = "[\n"
		    + "      {\n"
		    + "      \"activityId\": \"sid-B21E1853-15AE-4A0E-8EE4-914F721BD4A3\",\n"
		    + "      \"activityInstanceId\": \"3aa160a4-6a19-11e6-adf4-005056af0157\",\n"
		    + "      \"errorMessage\": null,\n"
		    + "      \"executionId\": \"3aa160a3-6a19-11e6-adf4-005056af0157\",\n"
		    + "      \"id\": \"3aa160a7-6a19-11e6-adf4-005056af0157\",\n"
		    + "      \"lockExpirationTime\": \"2016-08-25T11:09:37\",\n"
		    + "      \"processDefinitionId\": \"d81761b7-54ba-11e6-9b3e-005056af0157\",\n"
		    + "      \"processDefinitionKey\": \"basisproduktFusionierenExternal\",\n"
		    + "      \"processInstanceId\": \"3aa160a0-6a19-11e6-adf4-005056af0157\",\n"
		    + "      \"retries\": null,\n"
		    + "      \"suspended\": false,\n"
		    + "      \"workerId\": \"soapUiWorker \\\" \",\n"
		    + "      \"topicName\": \"mergeBusinessObject\",\n"
		    + "      \"tenantId\": null,\n"
		    + "      \"variables\": {\"merge\":       {\n"
		    + "         \"type\": \"String\",\n"
		    + "         \"value\": \"{  \\\"source_businessobject_id\\\" : 111111,  \\\"target_businessobject_id\\\" : 222222 }\",\n"
		    + "         \"valueInfo\": {}\n"
		    + "      }},\n"
		    + "      \"priority\": 0\n"
		    + "   },\n"
		    + "      {\n"
		    + "      \"activityId\": \"sid-B21E1853-15AE-4A0E-8EE4-914F721BD4A3\",\n"
		    + "      \"activityInstanceId\": \"3567a39c-6a19-11e6-adf4-005056af0157\",\n"
		    + "      \"errorMessage\": null,\n"
		    + "      \"executionId\": \"3567a39b-6a19-11e6-adf4-005056af0157\",\n"
		    + "      \"id\": \"3567a39f-6a19-11e6-adf4-005056af0157\",\n"
		    + "      \"lockExpirationTime\": \"2016-08-25T11:09:37\",\n"
		    + "      \"processDefinitionId\": \"d81761b7-54ba-11e6-9b3e-005056af0157\",\n"
		    + "      \"processDefinitionKey\": \"basisproduktFusionierenExternal\",\n"
		    + "      \"processInstanceId\": \"3567a398-6a19-11e6-adf4-005056af0157\",\n"
		    + "      \"retries\": null,\n"
		    + "      \"suspended\": false,\n"
		    + "      \"workerId\": \"soapUiWorker\",\n"
		    + "      \"topicName\": \"mergeBusinessObject\",\n"
		    + "      \"tenantId\": null,\n"
		    + "      \"variables\": {\"merge\":       {\n"
		    + "         \"type\": \"String\",\n"
		    + "         \"value\": \"{  \\\"source_businessobject_id\\\" : 333333,  \\\"target_businessobject_id\\\" : 444444 }\",\n"
		    + "         \"valueInfo\": {}\n"
		    + "      }},\n"
		    + "      \"priority\": 0\n"
		    + "   },\n"
		    + "      {\n"
		    + "      \"activityId\": \"sid-B21E1853-15AE-4A0E-8EE4-914F721BD4A3\",\n"
		    + "      \"activityInstanceId\": \"3a06a73c-6a19-11e6-adf4-005056af0157\",\n"
		    + "      \"errorMessage\": null,\n"
		    + "      \"executionId\": \"3a06a73b-6a19-11e6-adf4-005056af0157\",\n"
		    + "      \"id\": \"3a06a73f-6a19-11e6-adf4-005056af0157\",\n"
		    + "      \"lockExpirationTime\": \"2016-08-25T11:09:37\",\n"
		    + "      \"processDefinitionId\": \"d81761b7-54ba-11e6-9b3e-005056af0157\",\n"
		    + "      \"processDefinitionKey\": \"basisproduktFusionierenExternal\",\n"
		    + "      \"processInstanceId\": \"3a06a738-6a19-11e6-adf4-005056af0157\",\n"
		    + "      \"retries\": null,\n"
		    + "      \"suspended\": false,\n"
		    + "      \"workerId\": \"soapUiWorker\",\n"
		    + "      \"topicName\": \"mergeBusinessObject\",\n"
		    + "      \"tenantId\": null,\n"
		    + "      \"variables\": {\"merge\":       {\n"
		    + "         \"type\": \"String\",\n"
		    + "         \"value\": \"{  \\\"source_businessobject_id\\\" : 555555,  \\\"target_businessobject_id\\\" : 666666 }\",\n"
		    + "         \"valueInfo\": {}\n"
		    + "      }},\n"
		    + "      \"priority\": 0\n"
		    + "   },\n"
		    + "      {\n"
		    + "      \"activityId\": \"sid-B21E1853-15AE-4A0E-8EE4-914F721BD4A3\",\n"
		    + "      \"activityInstanceId\": \"3a9eef9c-6a19-11e6-adf4-005056af0157\",\n"
		    + "      \"errorMessage\": null,\n"
		    + "      \"executionId\": \"3a9eef9b-6a19-11e6-adf4-005056af0157\",\n"
		    + "      \"id\": \"3a9eef9f-6a19-11e6-adf4-005056af0157\",\n"
		    + "      \"lockExpirationTime\": \"2016-08-25T11:09:37\",\n"
		    + "      \"processDefinitionId\": \"d81761b7-54ba-11e6-9b3e-005056af0157\",\n"
		    + "      \"processDefinitionKey\": \"basisproduktFusionierenExternal\",\n"
		    + "      \"processInstanceId\": \"3a9eef98-6a19-11e6-adf4-005056af0157\",\n"
		    + "      \"retries\": null,\n"
		    + "      \"suspended\": false,\n"
		    + "      \"workerId\": \"soapUiWorker\",\n"
		    + "      \"topicName\": \"mergeBusinessObject\",\n"
		    + "      \"tenantId\": null,\n"
		    + "      \"variables\": {\"merge\":       {\n"
		    + "         \"type\": \"String\",\n"
		    + "         \"value\": \"{  \\\"source_businessobject_id\\\" : 777777,  \\\"target_businessobject_id\\\" : 888888 }\",\n"
		    + "         \"valueInfo\": {}\n"
		    + "      }},\n"
		    + "      \"priority\": 0\n"
		    + "   },\n"
		    + "      {\n"
		    + "      \"activityId\": \"sid-B21E1853-15AE-4A0E-8EE4-914F721BD4A3\",\n"
		    + "      \"activityInstanceId\": \"3aa88c9c-6a19-11e6-adf4-005056af0157\",\n"
		    + "      \"errorMessage\": null,\n"
		    + "      \"executionId\": \"3aa88c9b-6a19-11e6-adf4-005056af0157\",\n"
		    + "      \"id\": \"3aa88c9f-6a19-11e6-adf4-005056af0157\",\n"
		    + "      \"lockExpirationTime\": \"2016-08-25T11:09:37\",\n"
		    + "      \"processDefinitionId\": \"d81761b7-54ba-11e6-9b3e-005056af0157\",\n"
		    + "      \"processDefinitionKey\": \"basisproduktFusionierenExternal\",\n"
		    + "      \"processInstanceId\": \"3aa88c98-6a19-11e6-adf4-005056af0157\",\n"
		    + "      \"retries\": null,\n"
		    + "      \"suspended\": false,\n"
		    + "      \"workerId\": \"soapUiWorker\",\n"
		    + "      \"topicName\": \"mergeBusinessObject\",\n"
		    + "      \"tenantId\": null,\n"
		    + "      \"variables\": {\"merge\":       {\n"
		    + "         \"type\": \"String\",\n"
		    + "         \"value\": \"{  \\\"source_businessobject_id\\\" : 1212121,  \\\"target_businessobject_id\\\" : 2323232 }\",\n"
		    + "         \"valueInfo\": {}\n"
		    + "      }},\n"
		    + "      \"priority\": 0\n"
		    + "   },\n"
		    + "      {\n"
		    + "      \"activityId\": \"sid-B21E1853-15AE-4A0E-8EE4-914F721BD4A3\",\n"
		    + "      \"activityInstanceId\": \"3a01ec44-6a19-11e6-adf4-005056af0157\",\n"
		    + "      \"errorMessage\": null,\n"
		    + "      \"executionId\": \"3a01ec43-6a19-11e6-adf4-005056af0157\",\n"
		    + "      \"id\": \"3a01ec47-6a19-11e6-adf4-005056af0157\",\n"
		    + "      \"lockExpirationTime\": \"2016-08-25T11:09:37\",\n"
		    + "      \"processDefinitionId\": \"d81761b7-54ba-11e6-9b3e-005056af0157\",\n"
		    + "      \"processDefinitionKey\": \"basisproduktFusionierenExternal\",\n"
		    + "      \"processInstanceId\": \"3a01ec40-6a19-11e6-adf4-005056af0157\",\n"
		    + "      \"retries\": null,\n"
		    + "      \"suspended\": false,\n"
		    + "      \"workerId\": \"soapUiWorker\",\n"
		    + "      \"topicName\": \"mergeBusinessObject\",\n"
		    + "      \"tenantId\": null,\n"
		    + "      \"variables\": {\"merge\":       {\n"
		    + "         \"type\": \"String\",\n"
		    + "         \"value\": \"{  \\\"source_businessobject_id\\\" : 34343434,  \\\"target_businessobject_id\\\" : 45454545 }\",\n"
		    + "         \"valueInfo\": {}\n"
		    + "      }},\n"
		    + "      \"priority\": 0\n"
		    + "   },\n"
		    + "      {\n"
		    + "      \"activityId\": \"sid-B21E1853-15AE-4A0E-8EE4-914F721BD4A3\",\n"
		    + "      \"activityInstanceId\": \"3ab2028c-6a19-11e6-adf4-005056af0157\",\n"
		    + "      \"errorMessage\": null,\n"
		    + "      \"executionId\": \"3ab2028b-6a19-11e6-adf4-005056af0157\",\n"
		    + "      \"id\": \"3ab2028f-6a19-11e6-adf4-005056af0157\",\n"
		    + "      \"lockExpirationTime\": \"2016-08-25T11:09:37\",\n"
		    + "      \"processDefinitionId\": \"d81761b7-54ba-11e6-9b3e-005056af0157\",\n"
		    + "      \"processDefinitionKey\": \"basisproduktFusionierenExternal\",\n"
		    + "      \"processInstanceId\": \"3ab20288-6a19-11e6-adf4-005056af0157\",\n"
		    + "      \"retries\": null,\n"
		    + "      \"suspended\": false,\n"
		    + "      \"workerId\": \"soapUiWorker\",\n"
		    + "      \"topicName\": \"mergeBusinessObject\",\n"
		    + "      \"tenantId\": null,\n"
		    + "      \"variables\": {\"merge\":       {\n"
		    + "         \"type\": \"String\",\n"
		    + "         \"value\": \"{  \\\"source_businessobject_id\\\" : 565656,  \\\"target_businessobject_id\\\" : 6767676 }\",\n"
		    + "         \"valueInfo\": {}\n"
		    + "      }},\n"
		    + "      \"priority\": 0\n"
		    + "   },\n"
		    + "      {\n"
		    + "      \"activityId\": \"sid-B21E1853-15AE-4A0E-8EE4-914F721BD4A3\",\n"
		    + "      \"activityInstanceId\": \"3a0b6234-6a19-11e6-adf4-005056af0157\",\n"
		    + "      \"errorMessage\": null,\n"
		    + "      \"executionId\": \"3a0b6233-6a19-11e6-adf4-005056af0157\",\n"
		    + "      \"id\": \"3a0b6237-6a19-11e6-adf4-005056af0157\",\n"
		    + "      \"lockExpirationTime\": \"2016-08-25T11:09:37\",\n"
		    + "      \"processDefinitionId\": \"d81761b7-54ba-11e6-9b3e-005056af0157\",\n"
		    + "      \"processDefinitionKey\": \"basisproduktFusionierenExternal\",\n"
		    + "      \"processInstanceId\": \"3a0b6230-6a19-11e6-adf4-005056af0157\",\n"
		    + "      \"retries\": null,\n"
		    + "      \"suspended\": false,\n"
		    + "      \"workerId\": \"soapUiWorker\",\n"
		    + "      \"topicName\": \"mergeBusinessObject\",\n"
		    + "      \"tenantId\": null,\n"
		    + "      \"variables\": {\"merge\":       {\n"
		    + "         \"type\": \"String\",\n"
		    + "         \"value\": \"{  \\\"source_businessobject_id\\\" : 787878,  \\\"target_businessobject_id\\\" : 898989 }\",\n"
		    + "         \"valueInfo\": {}\n"
		    + "      }},\n"
		    + "      \"priority\": 0\n"
		    + "   },\n"
		    + "      {\n"
		    + "      \"activityId\": \"sid-B21E1853-15AE-4A0E-8EE4-914F721BD4A3\",\n"
		    + "      \"activityInstanceId\": \"e01fa8e7-6a14-11e6-adf4-005056af0157\",\n"
		    + "      \"errorMessage\": null,\n"
		    + "      \"executionId\": \"e01fa8e6-6a14-11e6-adf4-005056af0157\",\n"
		    + "      \"id\": \"e01fa8ea-6a14-11e6-adf4-005056af0157\",\n"
		    + "      \"lockExpirationTime\": \"2016-08-25T11:09:37\",\n"
		    + "      \"processDefinitionId\": \"d81761b7-54ba-11e6-9b3e-005056af0157\",\n"
		    + "      \"processDefinitionKey\": \"basisproduktFusionierenExternal\",\n"
		    + "      \"processInstanceId\": \"e01fa8e3-6a14-11e6-adf4-005056af0157\",\n"
		    + "      \"retries\": null,\n"
		    + "      \"suspended\": false,\n"
		    + "      \"workerId\": \"soapUiWorker\",\n"
		    + "      \"topicName\": \"mergeBusinessObject\",\n"
		    + "      \"tenantId\": null,\n"
		    + "      \"variables\": {\"merge\":       {\n"
		    + "         \"type\": \"String\",\n"
		    + "         \"value\": \"{  \\\"source_businessobject_id\\\" : 223344,  \\\"target_businessobject_id\\\" : 334455 }\",\n"
		    + "         \"valueInfo\": {}\n"
		    + "      }},\n"
		    + "      \"priority\": 0\n"
		    + "   },\n"
		    + "      {\n"
		    + "      \"activityId\": \"sid-B21E1853-15AE-4A0E-8EE4-914F721BD4A3\",\n"
		    + "      \"activityInstanceId\": \"3aad4794-6a19-11e6-adf4-005056af0157\",\n"
		    + "      \"errorMessage\": null,\n"
		    + "      \"executionId\": \"3aad4793-6a19-11e6-adf4-005056af0157\",\n"
		    + "      \"id\": \"3aad4797-6a19-11e6-adf4-005056af0157\",\n"
		    + "      \"lockExpirationTime\": \"2016-08-25T11:09:37\",\n"
		    + "      \"processDefinitionId\": \"d81761b7-54ba-11e6-9b3e-005056af0157\",\n"
		    + "      \"processDefinitionKey\": \"basisproduktFusionierenExternal\",\n"
		    + "      \"processInstanceId\": \"3aad4790-6a19-11e6-adf4-005056af0157\",\n"
		    + "      \"retries\": null,\n"
		    + "      \"suspended\": false,\n"
		    + "      \"workerId\": \"soapUiWorker\",\n"
		    + "      \"topicName\": \"mergeBusinessObject\",\n"
		    + "      \"tenantId\": null,\n"
		    + "      \"variables\": {\"merge\":       {\n"
		    + "         \"type\": \"String\",\n"
		    + "         \"value\": \"{  \\\"source_businessobject_id\\\" : 667788,  \\\"target_businessobject_id\\\" : 778899 }\",\n"
		    + "         \"valueInfo\": {}\n"
		    + "      }},\n"
		    + "      \"priority\": 0\n"
		    + "   }\n"
		    + "]";
		System.out.println(test);
	}
}
