package de.jlo.talendcomp.json.ops;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;

import de.jlo.talendcomp.json.JsonDocument;

public class Merge {
	
	private String sourceLoopPath = null;
	private String sourceIdentifier = null;
	private String targetLoopPath = null;
	private String targetIdentifier = null;
	private JsonNode sourceRootNode = null;
	private JsonNode targetRootNode = null;
	private JsonDocument sourceDoc = null;
	private JsonDocument targetDoc = null;
	private String targetMountAttribute = null;
	private boolean targetMountNodeIsArray = true;
	private boolean cloneSourceNodes = false;
	private int countAssigned = 0;
	private int countSourceNodes = 0;
	private int countTargetNodes = 0;
	private List<JsonNode> listSourceNodesNotAssigned = null;
	private boolean debug = false;
	private boolean dieIfSourceKeyNotExists = false;
	private boolean dieIfTargetKeyNotExists = false;
	private boolean dieIfSourceLoopPathNotExists = false;
	private boolean dieIfTargetLoopPathNotExists = false;
		
	public JsonNode setSourceNode(JsonNode node) throws Exception {
		this.sourceRootNode = node;
		this.sourceDoc = new JsonDocument(node);
		return this.sourceRootNode;
	}

	public JsonNode setSourceNode(String node) throws Exception {
		this.sourceDoc = new JsonDocument(node);
		this.sourceRootNode = sourceDoc.getRootNode();
		return this.sourceRootNode;
	}

	public JsonNode setTargetNode(JsonNode node) throws Exception {
		this.targetRootNode = node;
		this.targetDoc = new JsonDocument(node);
		return this.targetRootNode;
	}

	public JsonNode setTargetNode(String node) throws Exception {
		this.targetDoc = new JsonDocument(node);
		this.targetRootNode = targetDoc.getRootNode();
		return this.targetRootNode;
	}
	
	public JsonNode getTargetNode() {
		return targetRootNode;
	}

	public void setSourceLoopPath(String path) {
		this.sourceLoopPath = path;
	}
	
	public void setTargetLoopPath(String path) {
		this.targetLoopPath = path;
	}

	public String getSourceIdentifier() {
		return sourceIdentifier;
	}

	public void setSourceIdentifier(String sourceIdentifier) {
		this.sourceIdentifier = sourceIdentifier;
	}

	public String getTargetIdentifier() {
		return targetIdentifier;
	}

	public void setTargetIdentifier(String targetIdentifier) {
		this.targetIdentifier = targetIdentifier;
	}
	
	public String getTargetMountAttribute() {
		return targetMountAttribute;
	}

	public void setTargetMountAttribute(String targetMountAttribute, boolean targetMountNodeIsArray, boolean cloneSourceNodes) {
		this.targetMountAttribute = targetMountAttribute;
		this.targetMountNodeIsArray = targetMountNodeIsArray;
		this.cloneSourceNodes = cloneSourceNodes;
	}
	
	public int getCountAssigned() {
		return countAssigned;
	}

	public int getCountSourceNodes() {
		return countSourceNodes;
	}

	public int getCountTargetNodes() {
		return countTargetNodes;
	}

	public static boolean isEmpty(String s) {
		if (s == null) {
			return true;
		}
		if (s.trim().isEmpty()) {
			return true;
		}
		if (s.trim().equalsIgnoreCase("null")) {
			return true;
		}
		return false;
	}
	
	public void executeMerge() throws Exception {
		if (sourceDoc == null) {
			throw new IllegalStateException("Source node not set!");
		}
		if (targetDoc == null) {
			throw new IllegalStateException("Target node not set!");
		}
		if (isEmpty(sourceLoopPath)) {
			throw new IllegalStateException("sourceLoopPath cannot be null or empty.");
		}
		if (isEmpty(targetLoopPath)) {
			throw new IllegalStateException("targetLoopPath cannot be null or empty.");
		}
		if (isEmpty(sourceIdentifier)) {
			throw new IllegalStateException("sourceIdentifier cannot be null or empty.");
		}
		if (isEmpty(targetIdentifier)) {
			throw new IllegalStateException("targetIdentifier cannot be null or empty.");
		}
		if (isEmpty(targetMountAttribute)) {
			throw new IllegalStateException("targetMountAttribute cannot be null or empty.");
		}
		listSourceNodesNotAssigned = new ArrayList<JsonNode>();
		JsonNode sourceSearchResult = sourceDoc.getNode(sourceLoopPath);
		if (dieIfSourceLoopPathNotExists && sourceSearchResult == null) {
			throw new Exception("Source loop path does not exist. source-document:\n" + sourceDoc + "\nsourceLoopPath: " + sourceLoopPath);
		}
		List<JsonNode> sourceListNodes = sourceDoc.getArrayValuesAsList(sourceSearchResult, true);
		countSourceNodes = sourceListNodes.size();
		JsonNode targetSearchResult = targetDoc.getNode(targetLoopPath);
		if (dieIfTargetLoopPathNotExists && targetSearchResult == null) {
			throw new Exception("Target loop path does not exist. target-document:\n" + targetDoc + "\ntargetLoopPath: " + targetLoopPath);
		}
		List<JsonNode> targetListNodes = targetDoc.getArrayValuesAsList(targetSearchResult, false);
		countTargetNodes = targetListNodes.size();
		for (JsonNode sourceNode : sourceListNodes) {
			if (sourceNode.isNull()) {
				throw new Exception("Found null as source node in the search result: " + sourceSearchResult);
			}
			if ((sourceNode instanceof ObjectNode) == false) {
				throw new Exception("Found in source nodes a none-ObjectNode: " + sourceNode + " in the search result: " + sourceSearchResult);
			}
			boolean foundTarget = false;
			for (JsonNode targetNode : targetListNodes) {
				if (match(sourceNode, targetNode)) {
					// we found source and target nodes with matching keys
					// now add source to the target
					if (targetMountNodeIsArray) {
						ArrayNode targetArray = ((ObjectNode) targetNode).withArray(targetMountAttribute);
						if (contains(targetArray, sourceNode) == false) {
							// take care we do not add a node twice
							if (cloneSourceNodes) {
								targetArray.add(sourceNode.deepCopy());
							} else {
								targetArray.add(sourceNode);
							}
							countAssigned++;
						}
					} else {
						if (".".equals(targetMountAttribute)) {
							// merge all attributes from source to target node
							Iterator<Map.Entry<String, JsonNode>> it = ((ObjectNode) sourceNode).fields();
							while (it.hasNext()) {
								Map.Entry<String, JsonNode> sourceValueEntry = it.next();
								String sourceKey = sourceValueEntry.getKey();
								JsonNode sourceValue = sourceValueEntry.getValue();
								if (cloneSourceNodes) {
									((ObjectNode) targetNode).set(sourceKey, sourceValue.deepCopy());
								} else {
									((ObjectNode) targetNode).set(sourceKey, sourceValue);
								}
							}
							countAssigned++;
						} else {
							JsonNode child = ((ObjectNode) targetNode).path(targetMountAttribute);
							if (child.isNull() || child.isMissingNode()) {
								if (cloneSourceNodes) {
									((ObjectNode) targetNode).set(targetMountAttribute, sourceNode.deepCopy());
								} else {
									((ObjectNode) targetNode).set(targetMountAttribute, sourceNode);
								}
								countAssigned++;
							}
						}
					}
					foundTarget = true;
				}
			}
			if (foundTarget == false) {
				if (listSourceNodesNotAssigned.contains(sourceNode) == false) {
					listSourceNodesNotAssigned.add(sourceNode);
				}
			}
		}
	}
	
	public List<JsonNode> getListSourceNodesNotAssigned() {
		return listSourceNodesNotAssigned;
	}
	
	private boolean contains(ArrayNode arrayNode, JsonNode node) {
		for (JsonNode element : arrayNode) {
			if (element.equals(node)) {
				return true;
			}
		}
		return false;
	}

	private boolean match(JsonNode node1, JsonNode node2) throws Exception {
		if (node1 == null || node2 == null) {
			return false;
		}
		if (node1.equals(node2)) {
			return false; // prevent self assignment
		}
		JsonNode key1 = sourceDoc.getNode(node1, sourceIdentifier);
		if (key1 == null && dieIfSourceKeyNotExists) {
			throw new Exception("Source node: " + node1.toString() + " does not have the expected key attribute: " + sourceIdentifier);
		}
		JsonNode key2 = targetDoc.getNode(node2, targetIdentifier);
		if (key2 == null && dieIfTargetKeyNotExists) {
			throw new Exception("Target node: " + node2.toString() + " does not have the expected key attribute: " + targetIdentifier);
		}
		if (key2 instanceof ArrayNode) {
			boolean found = contains((ArrayNode) key2, key1);
			return found;
		} else {
			boolean found = false;
			if (key1 != null && key2 != null) {
				if (key1 instanceof ValueNode && key2 instanceof ValueNode) {
					String key1Str = key1.asText().trim();
					String key2Str = key2.asText().trim();
					found = key1Str.equals(key2Str);
				} else if (key1.equals(key2)) {
					found = true;
				}
				if (found) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	}

	public boolean isDieIfSourceKeyNotExists() {
		return dieIfSourceKeyNotExists;
	}

	public void setDieIfSourceKeyNotExists(boolean dieIfSourceKeyNotExists) {
		this.dieIfSourceKeyNotExists = dieIfSourceKeyNotExists;
	}

	public boolean isDieIfTargetKeyNotExists() {
		return dieIfTargetKeyNotExists;
	}

	public void setDieIfTargetKeyNotExists(boolean dieIfTargetKeyNotExists) {
		this.dieIfTargetKeyNotExists = dieIfTargetKeyNotExists;
	}

	public boolean isDieIfSourceLoopPathNotExists() {
		return dieIfSourceLoopPathNotExists;
	}

	public void setDieIfSourceLoopPathNotExists(boolean dieIfSourceLoopPathNotExists) {
		this.dieIfSourceLoopPathNotExists = dieIfSourceLoopPathNotExists;
	}

	public boolean isDieIfTargetLoopPathNotExists() {
		return dieIfTargetLoopPathNotExists;
	}

	public void setDieIfTargetLoopPathNotExists(boolean dieIfTargetLoopPathNotExists) {
		this.dieIfTargetLoopPathNotExists = dieIfTargetLoopPathNotExists;
	}

}
