package de.cimt.talendcomp.json;

public class ArrayToken extends PathToken {
	
	int index = 0;
	
	public ArrayToken(String text) throws Exception {
		try {
			index = Integer.parseInt(text);
		} catch (NumberFormatException e) {
			throw new Exception("The given array index is not an integer literal: " + text, e);
		}
	}
	
	public int getIndex() {
		return index;
	}

	@Override
	public String toString() {
		return "[" + index + "]";
	}

}
