package examples;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.internal.path.PathCompiler;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;

import de.cimt.talendcomp.json.JsonDocument;

public class Test2 {

	public static void main(String[] args) {
		try {
//			testJsonPathNew();
//			testGetDirectPathTokens();
			//testCompile();
			testBuildJson();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static final Configuration JACKSON_JSON_NODE_CONFIGURATION = Configuration
            .builder()
            .mappingProvider(new JacksonMappingProvider())
            .jsonProvider(new JacksonJsonNodeJsonProvider())
            .build();
	
	public static void testJsonPathNew() throws Exception {
		DocumentContext docContext = JsonPath.using(JACKSON_JSON_NODE_CONFIGURATION).parse("[]");
		JsonNode root1 = docContext.json();
		JsonNode root = docContext.read("$");
		System.out.println("6: " + root);
		JsonNode root2 = docContext.json();
		System.out.println("7: " + root2);
		if (root1 != root2) {
			System.err.println("root changed!");
		}
		
		if (root != root2) {
			System.err.println("Not the same root!");
		}

		ObjectNode nodeMadeByJackson_1 = new ObjectNode(JsonNodeFactory.instance);
		nodeMadeByJackson_1.put("a1", "v1");
		docContext.add("$", nodeMadeByJackson_1);
		
		// looks: [{"a1":"v1"}]
		System.out.println("1: " + docContext.jsonString());
		
		// now add to the first array node a new attribute referring to a new node
		ObjectNode nodeMadeByJackson_2 = new ObjectNode(JsonNodeFactory.instance);
		nodeMadeByJackson_2.put("a21", "v21");
		nodeMadeByJackson_1.set("a2", nodeMadeByJackson_2);
//		docContext.put("$[0]","a2", nodeMadeByJackson_2);
		// looks: [{"a1":"v1","a2":{"a21":"v21"}}]
		System.out.println("2: " + docContext.jsonString());
		ArrayNode arrayNode = null;
		// read back the first array node
		ObjectNode node_retrieved_from_context_1 = docContext.read("$[0]");
		System.out.println("3: " + node_retrieved_from_context_1);
		
		ObjectNode test21 = docContext.read("$[0].a2");
		System.out.println("4: " + test21);

		// now we are trying to get back the nodeMadeByJackson_2 but we get null!
		JsonNode test22 = nodeMadeByJackson_1.get("a2");
		System.out.println("5: " + test22);

		if (test21 != test22) {
			System.err.println("test2 nodes are not the same!");
		}
		// now we are trying to get back the nodeMadeByJackson_2 but we get null!
		
	}
	
	public static void testCompile() {
		PathCompiler.compile("$[0].a2");
		
	}

	public static void testBuildJson() throws Exception {
		JsonDocument doc = new JsonDocument(json);
		System.out.println(doc.toString());
		JsonNode startNode = doc.getNode("$.store.book[2]");
		System.out.println(startNode.toString());
		doc.getNode(startNode, "sales[].kf.orders", true);
		System.out.println(startNode.toString());
		JsonNode kf = doc.getNode(startNode, "sales[].kf", true);
		((ObjectNode) kf).put("orders", 123);
		System.out.println(startNode.toString());
	}

	public static String json = "{ \"store\": {\n"
		    + "    \"book\": [ \n"
		    + "      { \"category\": \"reference\",\n"
		    + "        \"author\": \"Nigel Rees\",\n"
		    + "        \"title\": \"Sayings of the Century\",\n"
		    + "        \"price\": 8.95\n"
		    + "      },\n"
		    + "      { \"category\": \"fiction\",\n"
		    + "        \"author\": \"Evelyn Waugh\",\n"
		    + "        \"title\": \"Sword of Honour\",\n"
		    + "        \"price\": 12.99\n"
		    + "      },\n"
		    + "      { \"category\": \"fiction\",\n"
		    + "        \"author\": \"Herman Melville\",\n"
		    + "        \"title\": \"Moby Dick\",\n"
		    + "        \"isbn\": \"0-553-21311-3\",\n"
		    + "        \"price\": 8.99\n"
		    + "      },\n"
		    + "      { \"category\": \"fiction\",\n"
		    + "        \"author\": \"J. R. R. Tolkien\",\n"
		    + "        \"title\": \"The Lord of the Rings\",\n"
		    + "        \"isbn\": \"0-395-19395-8\",\n"
		    + "        \"price\": 22.99\n"
		    + "      }\n"
		    + "    ],\n"
		    + "    \"bicycle\": {\n"
		    + "      \"color\": \"red\",\n"
		    + "      \"price\": 19.95\n"
		    + "    }\n"
		    + "  }\n"
		    + "}";
	
	
}
