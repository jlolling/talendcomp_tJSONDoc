package de.jlo.talendcomp.json;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;

public class JsonDiff {

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

	/**
	 * Find the differences between to JsonNodes in the deep
	 * @param reference node which is the reference
	 * @param test node is the test object
	 * @return List of Differences
	 */
	public List<Difference> findDifference(JsonNode reference, JsonNode test) {
		return findDifference(null, reference, test, null);
	}
	
	private List<Difference> findDifference(String parentPath, JsonNode reference, JsonNode test, List<Difference> listDiffs) {
		if (parentPath == null) {
			parentPath = "$";
		}
		if (listDiffs == null) {
			listDiffs = new ArrayList<Difference>();
		}
		if (reference instanceof ObjectNode) {
			if (test instanceof ObjectNode) {
				ObjectNode rn = (ObjectNode) reference;
				ObjectNode tn = (ObjectNode) test;
				// Test ref->test
				{
					Iterator<Map.Entry<String, JsonNode>> fi = rn.fields(); 
					while (fi.hasNext()) {
						Map.Entry<String, JsonNode> entry = fi.next();
						// check if field exists in test node
						String newParentPath = parentPath + "." + entry.getKey();
						if (listDiffs.contains(newParentPath) == false) {
							if (tn.has(entry.getKey())) {
								JsonNode tnValue = tn.get(entry.getKey());
								if (entry.getValue() != null && tnValue != null) {
									findDifference(newParentPath, entry.getValue(), tnValue, listDiffs);
								} else if (entry.getValue() == null && tnValue != null) {
									Difference diff = new Difference();
									diff.setJsonPath(newParentPath);
									diff.setRefValue(NullNode.getInstance());
									diff.setTestValue(tnValue);
									listDiffs.add(diff);
								} else if (entry.getValue() != null && tnValue == null) {
									Difference diff = new Difference();
									diff.setJsonPath(newParentPath);
									diff.setRefValue(entry.getValue());
									diff.setTestValue(NullNode.getInstance());
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
				// Test test->ref
				{
					Iterator<Map.Entry<String, JsonNode>> fi = tn.fields(); 
					while (fi.hasNext()) {
						Map.Entry<String, JsonNode> entry = fi.next();
						// check if field exists in test node
						String newParentPath = parentPath + "." + entry.getKey();
						if (listDiffs.contains(newParentPath) == false) {
							if (rn.has(entry.getKey())) {
								JsonNode rnValue = rn.get(entry.getKey());
								if (entry.getValue() != null && rnValue != null) {
									findDifference(newParentPath, rnValue, entry.getValue(), listDiffs);
								} else if (rnValue != null && entry.getValue() == null) {
									Difference diff = new Difference();
									diff.setJsonPath(newParentPath);
									diff.setRefValue(rnValue);
									diff.setTestValue(NullNode.getInstance());
									listDiffs.add(diff);
								} else if (rnValue == null && entry.getValue() != null) {
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
			} else {
				Difference diff = new Difference();
				diff.setJsonPath(parentPath);
				diff.setTypeMismatch(true);
				diff.setRefValue(reference);
				diff.setTestValue(test);
				if (listDiffs.contains(diff) == false) {
					listDiffs.add(diff);
				}
			}
		} else if (reference instanceof ArrayNode) {
			if (test instanceof ArrayNode) {
				ArrayNode rn = (ArrayNode) reference;
				ArrayNode tn = (ArrayNode) test;
				if (rn.size() >= tn.size()) {
					for (int i = 0; i < rn.size(); i++) {
						String newParentPath = parentPath + "[" + i + "]";
						JsonNode rvn = rn.get(i);
						if (i < tn.size()) {
							JsonNode tvn = tn.get(i);
							findDifference(newParentPath, rvn, tvn, listDiffs);
						} else {
							Difference diff = new Difference();
							diff.setJsonPath(newParentPath);
							diff.setRefValue(rvn);
							diff.setTestValue(NullNode.getInstance());
							if (listDiffs.contains(diff) == false) {
								listDiffs.add(diff);
							}
						}
					}
				} else {
					for (int i = 0; i < tn.size(); i++) {
						String newParentPath = parentPath + "[" + i + "]";
						JsonNode tvn = tn.get(i);
						if (i < rn.size()) {
							JsonNode rvn = tn.get(i);
							findDifference(newParentPath, rvn, tvn, listDiffs);
						} else {
							Difference diff = new Difference();
							diff.setJsonPath(newParentPath);
							diff.setRefValue(NullNode.getInstance());
							diff.setTestValue(tvn);
							if (listDiffs.contains(diff) == false) {
								listDiffs.add(diff);
							}
						}
					}
				}
			} else {
				Difference diff = new Difference();
				diff.setJsonPath(parentPath);
				diff.setTypeMismatch(true);
				diff.setRefValue(reference);
				diff.setTestValue(test);
				if (listDiffs.contains(diff) == false) {
					listDiffs.add(diff);
				}
			}
		} else if (reference instanceof ValueNode) {
			if (test instanceof ValueNode) {
				ValueNode rv = (ValueNode) reference;
				ValueNode tv = (ValueNode) test;
				if (tv.equals(rv) == false) {
					Difference diff = new Difference();
					diff.setJsonPath(parentPath);
					diff.setRefValue(rv);
					diff.setTestValue(tv);
					if (listDiffs.contains(diff) == false) {
						listDiffs.add(diff);
					}
				}
			} else {
				Difference diff = new Difference();
				diff.setJsonPath(parentPath);
				diff.setTypeMismatch(true);
				diff.setRefValue(reference);
				diff.setTestValue(test);
				if (listDiffs.contains(diff) == false) {
					listDiffs.add(diff);
				}
			}
		}
		return listDiffs;
	}

}
