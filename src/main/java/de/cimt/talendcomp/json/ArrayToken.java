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
