package de.jlo.talendcomp.json.ops;

import java.util.List;

public class JSONValue {
	
	private List<String> keyPath;
	private String key;
	private Object value;
	
	public String getKeyPath(String delimiter) {
		if (keyPath != null) {
			StringBuilder sb = new StringBuilder();
			boolean firstLoop = true;
			for (String key : keyPath) {
				if (firstLoop) {
					firstLoop = false;
				} else {
					sb.append(delimiter);
				}
				sb.append(key);
			}
			return sb.toString();
		} else {
			return null;
		}
	}
	
	public void setKeyPath(List<String> keyPath) {
		this.keyPath = keyPath;
	}
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public Object getValue() {
		return value;
	}
	
	public String getValueString() {
		if (value != null) {
			if (value instanceof String) {
				return (String) value;
			} else {
				return value.toString();
			}
		} else {
			return null;
		}
	}
	
	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return getKeyPath(">") + ":" + key + "=" + value;
	}
	
}
