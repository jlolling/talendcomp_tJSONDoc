/**
 * Copyright 2015 Jan Lolling jan.lolling@gmail.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.jlo.talendcomp.json;

import java.util.ArrayList;
import java.util.List;

public abstract class PathToken {
	
	private PathToken previous = null; 
	private PathToken next = null; 
	
	public static List<PathToken> parse(String directJsonPath) throws Exception {
		List<PathToken> result = new ArrayList<PathToken>();
		StringBuilder text = new StringBuilder(0);
		boolean inArray = false;
		char last_c = ' ';
		PathToken lastToken = null;
		for (int i = 0, n = directJsonPath.length(); i < n; i++) {
			try {
				char c = directJsonPath.charAt(i);
				if (c == '$') {
					last_c = c;
					continue;
				}
				if (c == '[') {
					if (last_c != ']') {
						if (text.length() > 0) {
							// we expect a normal attribute here
							PathToken token = new AttributeToken(text.toString());
							result.add(token);
							text.setLength(0);
							if (lastToken != null) {
								lastToken.next = token;
								token.previous = lastToken;
							}
							lastToken = token;
						}
					} else {
						// ignore it
					}
					inArray = true;
				} else if (inArray) {
					if (c == ']') {
						PathToken token = new ArrayToken(text.toString());
						result.add(token);
						text.setLength(0);
						if (lastToken != null) {
							lastToken.next = token;
							token.previous = lastToken;
						}
						lastToken = token;
						inArray = false;
					} else {
						text.append(c);
					}
				} else if (c == ']') {
					throw new Exception("Closing bracket found without opening before");
				} else if (c == '.') {
					if (last_c != ']' && last_c != '$') {
						// we expect a normal attribute name now
						PathToken token = new AttributeToken(text.toString());
						result.add(token);
						text.setLength(0);
						if (lastToken != null) {
							lastToken.next = token;
							token.previous = lastToken;
						}
						lastToken = token;
					} else if (i == n - 1) {
						// we have a dot at the end and this is not allowed
						throw new Exception("Dot-delimiter to a next attribute found but we are at the end");
					} else {
						// ignore it
					}
				} else {
					text.append(c);
				}
				last_c = c;
			} catch (Exception e) {
				throw new Exception("Parsing failed at pos: " + i, e);
			}
		}
		if (text.length() > 0) {
			PathToken token = new AttributeToken(text.toString());
			result.add(token);
			text.setLength(0);
			if (lastToken != null) {
				lastToken.next = token;
				token.previous = lastToken;
			}
			lastToken = token;
		}
		return result;
	}

	public String getPath() {
		StringBuilder path = new StringBuilder();
		buildPath(path);
		return path.toString();
	}
	
	private void buildPath(StringBuilder path) {
		if (previous != null) {
			previous.buildPath(path);
		}
		if (this instanceof AttributeToken) {
			String name = toString();
			if (path.length() > 0) {
				path.append(".");
			}
			path.append(name);
		} else if (this instanceof ArrayToken) {
			path.append(toString());
		}
	}

	public PathToken getPrevious() {
		return previous;
	}

	public PathToken getNext() {
		return next;
	}
	
	public boolean hasNext() {
		return next != null;
	}

	public boolean isNextTokenArray() {
		return (next instanceof ArrayToken);
	}
	
}
