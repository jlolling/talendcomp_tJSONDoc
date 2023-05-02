package de.jlo.talendcomp.json;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

public class SchemaValidator {
	
	private String uriString = null;
	private String schemaContent = null;
	private JsonNode schemaNode = null;
	private Set<ValidationMessage> lastValidationReport = new HashSet<>();
	private static final Map<String, JsonSchemaFactory> schemaFactoryMap = new HashMap<>();
	private static final String SCHEMA_VERSION_201909 = "https://json-schema.org/draft/2019-09/schema";
	private static final String SCHEMA_VERSION_07 = "http://json-schema.org/draft-07/schema";
	private static final String SCHEMA_VERSION_06 = "http://json-schema.org/draft-06/schema";
	private static final String SCHEMA_VERSION_04 = "http://json-schema.org/draft-04/schema";
	
	public SchemaValidator(String uri, String schemaContent) {
		this.uriString = uri;
		this.schemaContent = schemaContent;
	}
	
	
	
	/**
	 * Build a summary text from the report.
	 * @param report
	 * @return
	 */
	public static String buildValidationReportText(Set<ValidationMessage> report) {
		if (report.isEmpty()) {
			return null;
		} else {
			StringBuilder text = new StringBuilder();
			text.append("JSON schema validation found " + report.size() + " problems");
			text.append("\n");
			for (ValidationMessage m : report) {
				text.append(m.getMessage());
				text.append("\n");
			}
			return text.toString();
		}
	}

	public static JsonSchemaFactory getJsonSchemaFactoryInstance(JsonNode schemaNode) throws Exception {
		JsonNode schemaVersion = schemaNode.get("$schema");
		String uri = schemaVersion.asText();
		JsonSchemaFactory factory = schemaFactoryMap.get(uri);
		if (factory == null) {
			if (uri.startsWith(SCHEMA_VERSION_04)) {
				factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
				schemaFactoryMap.put(uri, factory);
			} else if (uri.startsWith(SCHEMA_VERSION_06)) {
				factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V6);
				schemaFactoryMap.put(uri, factory);
			} else if (uri.startsWith(SCHEMA_VERSION_07)) {
				factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
				schemaFactoryMap.put(uri, factory);
			} else if (uri.startsWith(SCHEMA_VERSION_201909)) {
				factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909);
				schemaFactoryMap.put(uri, factory);
			} else {
				throw new Exception("Unknown schema version uri: " + uri);
			}
		}
		return factory;
	}

	/**
	 * validates the current document against a schema
	 * @param schemaId the id of the json-schema
	 * @return a set of ValidationMessage, the set is empty if no problems found 
	 * @throws ProcessingException in case of the validation technical fails
	 */
	public String validate(JsonNode contentNode) throws Exception {
		lastValidationReport.clear();
		JsonSchemaFactory schemaFactory = getJsonSchemaFactoryInstance(schemaNode);
		JsonSchema v = schemaFactory.getSchema(new URI(uriString), schemaNode);
		lastValidationReport = v.validate(contentNode);
        return buildValidationReportText(lastValidationReport);
	}
	
	public Set<ValidationMessage> getLastValidationReport() {
		return lastValidationReport;
	}


}
