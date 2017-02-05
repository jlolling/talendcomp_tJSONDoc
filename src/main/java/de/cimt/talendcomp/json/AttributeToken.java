package de.cimt.talendcomp.json;

public class AttributeToken extends PathToken {

	private String name = null;
	
	public AttributeToken(String name) {
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("name cannot be null or empty");
		}
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isRoot() {
		return "$".equals(name);
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
