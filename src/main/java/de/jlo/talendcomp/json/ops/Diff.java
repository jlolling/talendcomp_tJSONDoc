package de.jlo.talendcomp.json.ops;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ValueNode;

import de.jlo.talendcomp.json.JsonDocument;
import de.jlo.talendcomp.json.Util;

public class Diff {

	private boolean takeEmptyLikeNull = false;
	private boolean ignoreArrayIndex = false;
	private JsonNode referenceNode = null;
	private JsonNode testNode = null;
	private String refJsonPath = null;
	private String testJsonPath = null;
	private List<Difference> result = null;
	private int countDifferences = 0;
	private String rootArraySortAttribute = null;

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
	 * @return List of Differences at least an empty list
	 * @throws Exception 
	 */
	public List<Difference> findDifference(JsonNode reference, JsonNode test) throws Exception {
		return findDifference(null, reference, test, null);
	}

	/**
	 * Find the differences between to JsonNodes in the deep
	 * @param reference node which is the reference
	 * @param refJsonPath the path to the actual node use as reference node
	 * @param test node is the test object
	 * @param testJsonPath the path the actual test node
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
					Iterator<Map.Entry<String, JsonNode>> fields = rn.fields();
					while (fields.hasNext()) {
						Map.Entry<String, JsonNode> entry = fields.next();
						// check if field exists in test node
						String newParentPath = parentPath + "." + entry.getKey();
						if (containsDiff(listDiffs, newParentPath) == false) {
							if (tn.has(entry.getKey())) {
								JsonNode tnValue = tn.get(entry.getKey());
								if (Util.isNull(entry.getValue(), takeEmptyLikeNull) == false && Util.isNull(tnValue, takeEmptyLikeNull) == false) {
									findDifference(newParentPath, entry.getValue(), tnValue, listDiffs);
								} else if (Util.isNull(entry.getValue(), takeEmptyLikeNull) && Util.isNull(tnValue, takeEmptyLikeNull) == false) {
									Difference diff = new Difference();
									diff.setJsonPath(newParentPath);
									diff.setRefValue(NullNode.getInstance());
									diff.setTestValue(tnValue);
									listDiffs.add(diff);
								} else if (Util.isNull(entry.getValue(), takeEmptyLikeNull) == false && Util.isNull(tnValue, takeEmptyLikeNull)) {
									Difference diff = new Difference();
									diff.setJsonPath(newParentPath);
									diff.setRefValue(entry.getValue());
									diff.setTestValue(NullNode.getInstance());
									listDiffs.add(diff);
								}
							} else {
								if (takeEmptyLikeNull) {
									if (Util.isNull(tn.get(entry.getKey()), takeEmptyLikeNull) == false || Util.isNull(entry.getValue(), takeEmptyLikeNull) == false) {
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
					Iterator<Map.Entry<String, JsonNode>> fields = tn.fields(); 
					while (fields.hasNext()) {
						Map.Entry<String, JsonNode> entry = fields.next();
						// check if field exists in test node
						String newParentPath = parentPath + "." + entry.getKey();
						if (containsDiff(listDiffs, newParentPath) == false) {
							if (rn.has(entry.getKey())) {
								JsonNode rnValue = rn.get(entry.getKey());
								if (Util.isNull(entry.getValue(), takeEmptyLikeNull) == false && Util.isNull(rnValue, takeEmptyLikeNull) == false) {
									findDifference(newParentPath, rnValue, entry.getValue(), listDiffs);
								} else if (Util.isNull(rnValue, takeEmptyLikeNull) == false && Util.isNull(entry.getValue(), takeEmptyLikeNull)) {
									Difference diff = new Difference();
									diff.setJsonPath(newParentPath);
									diff.setRefValue(rnValue);
									diff.setTestValue(NullNode.getInstance());
									listDiffs.add(diff);
								} else if (Util.isNull(rnValue, takeEmptyLikeNull) && Util.isNull(entry.getValue(), takeEmptyLikeNull) == false) {
									Difference diff = new Difference();
									diff.setJsonPath(newParentPath);
									diff.setRefValue(NullNode.getInstance());
									diff.setTestValue(entry.getValue());
									listDiffs.add(diff);
								}
							} else {
								if (takeEmptyLikeNull) {
									if (Util.isNull(rn.get(entry.getKey()), takeEmptyLikeNull) == false || Util.isNull(entry.getValue(), takeEmptyLikeNull) == false) {
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
				if (containsDiff(listDiffs, parentPath) == false) {
					if (takeEmptyLikeNull) {
						if (Util.isNull(reference, takeEmptyLikeNull) == false || Util.isNull(test, takeEmptyLikeNull) == false) {
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
			List<JsonNode> refNodeList = arrayToList(rn);
			if (test instanceof ArrayNode) {
				ArrayNode tn = (ArrayNode) test;
				List<JsonNode> testNodeList = arrayToList(tn);
				if (refNodeList.size() >= testNodeList.size()) {
					for (int i = 0; i < refNodeList.size(); i++) {
						String newParentPath = parentPath + "[" + i + "]";
						if (containsDiff(listDiffs, newParentPath) == false) {
							JsonNode rvn = refNodeList.get(i);
							if (i < testNodeList.size()) {
								JsonNode tvn = testNodeList.get(i);
								if (ignoreArrayIndex && tvn instanceof ValueNode) {
									if (containsNode(refNodeList, tvn) == false) {
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
					for (int i = 0; i < testNodeList.size(); i++) {
						String newParentPath = parentPath + "[" + i + "]";
						if (containsDiff(listDiffs, newParentPath) == false) {
							JsonNode tvn = testNodeList.get(i);
							if (i < refNodeList.size()) {
								JsonNode rvn = testNodeList.get(i);
								if (ignoreArrayIndex && rvn instanceof ValueNode) {
									if (containsNode(testNodeList, rvn) == false) {
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
				if (containsDiff(listDiffs, parentPath) == false) {
					if (Util.isNull(test, takeEmptyLikeNull) == false || Util.isNull(rn, takeEmptyLikeNull) == false) {
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
			if (containsDiff(listDiffs, parentPath) == false) {
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
		result = listDiffs;
		return listDiffs;
	}
	
	private boolean containsDiff(List<Difference> listDiff, Object test) {
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
		if (Util.isNull(n1, takeEmptyLikeNull) == false && Util.isNull(n2, takeEmptyLikeNull) == false) {
			return n1.toString().equals(n2.toString());
		} else {
			return false;
		}
	}

	private boolean containsNode(List<JsonNode> arrayNode, JsonNode node) {
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
	
	public void setSortKeyAttribute(String keypath) {
		if (keypath != null && keypath.trim().isEmpty() == false) {
			rootArraySortAttribute = keypath;
		}
	}
	
	private List<JsonNode> arrayToList(ArrayNode arrayNode) {
		List<JsonNode> list = new ArrayList<JsonNode>();
		for (JsonNode node : arrayNode) {
			list.add(node);
		}
		if (rootArraySortAttribute != null) {
			Collections.sort(list, new Comparator<JsonNode>() {

				@Override
				public int compare(JsonNode n1, JsonNode n2) {
					JsonNode vn1 = n1.get(rootArraySortAttribute);
					JsonNode vn2 = n2.get(rootArraySortAttribute);
					if (vn1 instanceof TextNode && vn2 instanceof TextNode) {
						return vn1.asText().compareTo(vn2.asText());
					} else if (vn1 instanceof NumericNode && vn2 instanceof NumericNode) {
						return Double.valueOf(vn1.doubleValue()).compareTo(Double.valueOf(vn2.doubleValue()));
					} else {
						return 0;
					}
				}

			});
		}
		return list;
	}
	
}
