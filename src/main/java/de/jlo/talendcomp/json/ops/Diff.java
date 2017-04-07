package de.jlo.talendcomp.json.ops;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;

import de.jlo.talendcomp.json.JsonDocument;

public class Diff {

	private boolean takeEmptyLikeNull = false;
	private boolean ignoreArrayIndex = false;
	private JsonNode referenceNode = null;
	private JsonNode testNode = null;
	private String refJsonPath = null;
	private String testJsonPath = null;
	private List<Difference> result = null;
	private int countDifferences = 0;

	/**
	 * A class describing one difference between 2 JsonNodes
	 * @author jan.lolling@gmail.com
	 *
	 */
	public static class Difference {
		
		private String jsonPath = null;
		private JsonNode refValue = null;
		private JsonNode testValue = null;
		private boolean typeMismatch = false;
		
		public String getJsonPath() {
			return jsonPath;
		}
		/**
		 * set the json path at which the difference was detected
		 * @param jsonPath
		 */
		public void setJsonPath(String jsonPath) {
			this.jsonPath = jsonPath;
		}
		public JsonNode getRefValue() {
			return refValue;
		}
		/**
		 * Set the reference value
		 * @param value a JsonNode representing the value
		 */
		public void setRefValue(JsonNode value) {
			this.refValue = value;
		}
		public JsonNode getTestValue() {
			return testValue;
		}
		/**
		 * Set the value from the test node which differs from the reference node 
		 * @param value a JsonNode representing the value
		 */
		public void setTestValue(JsonNode value) {
			this.testValue = value;
		}
		public boolean isTypeMismatch() {
			return typeMismatch;
		}
		/**
		 * Set to true, if the compared nodes have different types
		 * They cannot really compared 
		 * @param typeMismatch
		 */
		public void setTypeMismatch(boolean typeMismatch) {
			this.typeMismatch = typeMismatch;
		}
		
		@Override
		public String toString() {
			return jsonPath + ": ref=" + refValue + ", test=" + testValue;
		}
		
		@Override
		public boolean equals(Object object) {
			if (object instanceof Difference) {
				return jsonPath.equals(((Difference) object).jsonPath);
			} else if (object instanceof String) {
				return jsonPath.equals(object);
			} else {
				return false;
			}
		}
		
		@Override
		public int hashCode() {
			return jsonPath.hashCode();
		}

	}

	public void executeDiff() throws Exception {
		result = findDifference(referenceNode, refJsonPath, testNode, testJsonPath);
	}
	
	/**
	 * Find the differences between to JsonNodes in the deep
	 * @param reference node which is the reference
	 * @param test node is the test object
	 * @return List of Differences
	 */
	public List<Difference> findDifference(String reference, String test) throws Exception {
		return findDifference(reference, null, test, null);
	}
	
	/**
	 * Find the differences between to JsonNodes in the deep
	 * @param reference node which is the reference
	 * @param test node is the test object
	 * @return List of Differences
	 */
	public List<Difference> findDifference(String reference, String refJsonPath, String test, String testJsonPath) {
		JsonDocument refDoc = new JsonDocument(reference);
		JsonNode refNode = null;
		if (refJsonPath != null && refJsonPath.trim().isEmpty() == false) {
			refNode = refDoc.getNode(refJsonPath);
		} else {
			refNode = refDoc.getRootNode();
		}
		JsonDocument testDoc = new JsonDocument(test);
		JsonNode testNode = null;
		if (testJsonPath != null && testJsonPath.trim().isEmpty() == false) {
			testNode = testDoc.getNode(testJsonPath);
		} else {
			testNode = testDoc.getRootNode();
		}
		return findDifference(null, refNode, testNode, null);
	}

	/**
	 * Find the differences between to JsonNodes in the deep
	 * @param reference node which is the reference
	 * @param test node is the test object
	 * @return List of Differences
	 * @throws Exception 
	 */
	public List<Difference> findDifference(JsonNode reference, JsonNode test) throws Exception {
		return findDifference(null, reference, test, null);
	}

	/**
	 * Find the differences between to JsonNodes in the deep
	 * @param reference node which is the reference
	 * @param test node is the test object
	 * @return List of Differences
	 * @throws Exception 
	 */
	public List<Difference> findDifference(JsonNode reference, String refJsonPath, JsonNode test, String testJsonPath) throws Exception {
		JsonDocument refDoc = new JsonDocument(reference);
		JsonNode refNode = null;
		if (refJsonPath != null && refJsonPath.trim().isEmpty() == false) {
			refNode = refDoc.getNode(refJsonPath);
		} else {
			refNode = refDoc.getRootNode();
		}
		JsonDocument testDoc = new JsonDocument(test);
		JsonNode testNode = null;
		if (testJsonPath != null && testJsonPath.trim().isEmpty() == false) {
			testNode = testDoc.getNode(testJsonPath);
		} else {
			testNode = testDoc.getRootNode();
		}
		return findDifference(null, refNode, testNode, null);
	}
	
	private List<Difference> findDifference(String parentPath, JsonNode reference, JsonNode test, List<Difference> listDiffs) {
		if (parentPath == null) {
			parentPath = "$";
		}
		if (listDiffs == null) {
			listDiffs = new ArrayList<Difference>();
		}
		if (reference instanceof ObjectNode) {
			ObjectNode rn = (ObjectNode) reference;
			if (test instanceof ObjectNode) {
				ObjectNode tn = (ObjectNode) test;
				// Test ref->test
				{
					Iterator<Map.Entry<String, JsonNode>> fi = rn.fields();
					while (fi.hasNext()) {
						Map.Entry<String, JsonNode> entry = fi.next();
						// check if field exists in test node
						String newParentPath = parentPath + "." + entry.getKey();
						if (contains(listDiffs, newParentPath) == false) {
							if (tn.has(entry.getKey())) {
								JsonNode tnValue = tn.get(entry.getKey());
								if (isNull(entry.getValue()) == false && isNull(tnValue) == false) {
									findDifference(newParentPath, entry.getValue(), tnValue, listDiffs);
								} else if (isNull(entry.getValue()) && isNull(tnValue) == false) {
									Difference diff = new Difference();
									diff.setJsonPath(newParentPath);
									diff.setRefValue(NullNode.getInstance());
									diff.setTestValue(tnValue);
									listDiffs.add(diff);
								} else if (isNull(entry.getValue()) == false && isNull(tnValue)) {
									Difference diff = new Difference();
									diff.setJsonPath(newParentPath);
									diff.setRefValue(entry.getValue());
									diff.setTestValue(NullNode.getInstance());
									listDiffs.add(diff);
								}
							} else {
								if (takeEmptyLikeNull) {
									if (isNull(tn.get(entry.getKey())) == false || isNull(entry.getValue()) == false) {
										Difference diff = new Difference();
										diff.setJsonPath(newParentPath);
										diff.setRefValue(entry.getValue());
										listDiffs.add(diff);
									}
								} else {
									Difference diff = new Difference();
									diff.setJsonPath(newParentPath);
									diff.setRefValue(entry.getValue());
									listDiffs.add(diff);
								}
							}
						}
					}
				}
				// Test test->ref
				{
					Iterator<Map.Entry<String, JsonNode>> fi = tn.fields(); 
					while (fi.hasNext()) {
						Map.Entry<String, JsonNode> entry = fi.next();
						// check if field exists in test node
						String newParentPath = parentPath + "." + entry.getKey();
						if (contains(listDiffs, newParentPath) == false) {
							if (rn.has(entry.getKey())) {
								JsonNode rnValue = rn.get(entry.getKey());
								if (isNull(entry.getValue()) == false && isNull(rnValue) == false) {
									findDifference(newParentPath, rnValue, entry.getValue(), listDiffs);
								} else if (isNull(rnValue) == false && isNull(entry.getValue())) {
									Difference diff = new Difference();
									diff.setJsonPath(newParentPath);
									diff.setRefValue(rnValue);
									diff.setTestValue(NullNode.getInstance());
									listDiffs.add(diff);
								} else if (isNull(rnValue) && isNull(entry.getValue()) == false) {
									Difference diff = new Difference();
									diff.setJsonPath(newParentPath);
									diff.setRefValue(NullNode.getInstance());
									diff.setTestValue(entry.getValue());
									listDiffs.add(diff);
								}
							} else {
								if (takeEmptyLikeNull) {
									if (isNull(rn.get(entry.getKey())) == false || isNull(entry.getValue()) == false) {
										Difference diff = new Difference();
										diff.setJsonPath(newParentPath);
										diff.setRefValue(NullNode.getInstance());
										diff.setTestValue(entry.getValue());
										listDiffs.add(diff);
									}
								} else {
									Difference diff = new Difference();
									diff.setJsonPath(newParentPath);
									diff.setRefValue(NullNode.getInstance());
									diff.setTestValue(entry.getValue());
									listDiffs.add(diff);
								}
							}
						}
					}
				}
			} else {
				if (contains(listDiffs, parentPath) == false) {
					if (takeEmptyLikeNull) {
						if (isNull(reference) == false || isNull(test) == false) {
							Difference diff = new Difference();
							diff.setJsonPath(parentPath);
							diff.setTypeMismatch(true);
							diff.setRefValue(reference);
							diff.setTestValue(test);
							listDiffs.add(diff);
						}
					} else {
						Difference diff = new Difference();
						diff.setJsonPath(parentPath);
						diff.setTypeMismatch(true);
						diff.setRefValue(reference);
						diff.setTestValue(test);
						listDiffs.add(diff);
					}
				}
			}
		} else if (reference instanceof ArrayNode) {
			ArrayNode rn = (ArrayNode) reference;
			if (test instanceof ArrayNode) {
				ArrayNode tn = (ArrayNode) test;
				if (rn.size() >= tn.size()) {
					for (int i = 0; i < rn.size(); i++) {
						String newParentPath = parentPath + "[" + i + "]";
						if (contains(listDiffs, newParentPath) == false) {
							JsonNode rvn = rn.get(i);
							if (i < tn.size()) {
								JsonNode tvn = tn.get(i);
								if (ignoreArrayIndex && tvn instanceof ValueNode) {
									if (contains(rn, tvn) == false) {
										findDifference(newParentPath, rvn, tvn, listDiffs);
									}
								} else {
									findDifference(newParentPath, rvn, tvn, listDiffs);
								}
							} else {
								Difference diff = new Difference();
								diff.setJsonPath(newParentPath);
								diff.setRefValue(rvn);
								diff.setTestValue(NullNode.getInstance());
								listDiffs.add(diff);
							}
						}
					}
				} else {
					for (int i = 0; i < tn.size(); i++) {
						String newParentPath = parentPath + "[" + i + "]";
						if (contains(listDiffs, newParentPath) == false) {
							JsonNode tvn = tn.get(i);
							if (i < rn.size()) {
								JsonNode rvn = tn.get(i);
								if (ignoreArrayIndex && rvn instanceof ValueNode) {
									if (contains(tn, rvn) == false) {
										findDifference(newParentPath, rvn, tvn, listDiffs);
									}
								} else {
									findDifference(newParentPath, rvn, tvn, listDiffs);
								}
							} else {
								Difference diff = new Difference();
								diff.setJsonPath(newParentPath);
								diff.setRefValue(NullNode.getInstance());
								diff.setTestValue(tvn);
								listDiffs.add(diff);
							}
						}
					}
				}
			} else {
				if (contains(listDiffs, parentPath) == false) {
					if (isNull(test) == false || isNull(rn) == false) {
						Difference diff = new Difference();
						diff.setJsonPath(parentPath);
						diff.setTypeMismatch(true);
						diff.setRefValue(reference);
						diff.setTestValue(test);
						listDiffs.add(diff);
					}
				}
			}
		} else if (reference instanceof ValueNode) {
			ValueNode rv = (ValueNode) reference;
			if (contains(listDiffs, parentPath) == false) {
				if (test instanceof ValueNode) {
					ValueNode tv = (ValueNode) test;
					if (equals(tv, rv) == false) {
						Difference diff = new Difference();
						diff.setJsonPath(parentPath);
						diff.setRefValue(rv);
						diff.setTestValue(tv);
						listDiffs.add(diff);
					}
				} else {
					Difference diff = new Difference();
					diff.setJsonPath(parentPath);
					diff.setTypeMismatch(true);
					diff.setRefValue(reference);
					diff.setTestValue(test);
					listDiffs.add(diff);
				}
			}
		}
		return listDiffs;
	}
	
	private boolean contains(List<Difference> listDiff, Object test) {
		if (test instanceof String) {
			for (Difference diff : listDiff) {
				if (((String) test).equals(diff.jsonPath)) {
					return true;
				}
			}
			return false;
		} else if (test instanceof Difference) {
			for (Difference diff : listDiff) {
				if (diff.jsonPath != null && diff.jsonPath.equals(((Difference) test).jsonPath)) {
					return true;
				}
			}
			return false;
		} else {
			return false;
		}
	}
	
	private boolean equals(JsonNode n1, JsonNode n2) {
		if (isNull(n1) == false && isNull(n2) == false) {
			return n1.toString().equals(n2.toString());
		} else {
			return false;
		}
	}

	private boolean isNull(JsonNode node) {
		if (node == null) {
			return true;
		} else if (node.isNull()) {
			return true;
		} else if (node instanceof ValueNode) {
			return false;
		} else {
			if (takeEmptyLikeNull && node.size() == 0) {
				return true;
			}
		}
		return false;
	}

	private boolean contains(ArrayNode arrayNode, JsonNode node) {
		for (JsonNode element : arrayNode) {
			if (equals(element, node)) {
				return true;
			}
		}
		return false;
	}

	public boolean isTakeEmptyLikeNull() {
		return takeEmptyLikeNull;
	}

	public void setTakeEmptyLikeNull(boolean takeEmptyLikeNull) {
		this.takeEmptyLikeNull = takeEmptyLikeNull;
	}

	public boolean isIgnoreArrayIndex() {
		return ignoreArrayIndex;
	}

	public void setIgnoreArrayIndex(boolean ignoreArrayIndex) {
		this.ignoreArrayIndex = ignoreArrayIndex;
	}

	public JsonNode getReferenceNode() {
		return referenceNode;
	}

	public void setReferenceNode(JsonNode referenceNode) {
		this.referenceNode = referenceNode;
	}

	public void setReferenceNode(JsonNode reference, String jsonPath) throws Exception {
		this.referenceNode = new JsonDocument(reference).getNode(jsonPath);
	}

	public void setReferenceNode(String reference) {
		this.referenceNode = new JsonDocument(reference).getRootNode();
	}

	public void setReferenceNode(String reference, String jsonPath) {
		this.referenceNode = new JsonDocument(reference).getNode(jsonPath);
	}

	public JsonNode getTestNode() {
		return testNode;
	}

	public void setTestNode(JsonNode testNode) {
		this.testNode = testNode;
	}

	public void setTestNode(String test) {
		this.testNode = new JsonDocument(test).getRootNode();
	}

	public void setTestNode(JsonNode test, String jsonPath) throws Exception {
		this.testNode = new JsonDocument(test).getNode(jsonPath);
	}

	public void setTestNode(String test, String jsonPath) throws Exception {
		this.testNode = new JsonDocument(test).getNode(jsonPath);
	}

	public String getRefJsonPath() {
		return refJsonPath;
	}

	public void setRefJsonPath(String refJsonPath) {
		this.refJsonPath = refJsonPath;
	}

	public String getTestJsonPath() {
		return testJsonPath;
	}

	public void setTestJsonPath(String testJsonPath) {
		this.testJsonPath = testJsonPath;
	}

	public List<Difference> getResult() {
		return result;
	}

	public int getCountDifferences() {
		return countDifferences;
	}
	
}
