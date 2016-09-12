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
package de.cimt.talendcomp;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.FastDateFormat;

public class TolerantDateParser {
	
	private List<FastDateFormat> parsers = new ArrayList<FastDateFormat>();
	private List<String> datePatterns = new ArrayList<String>();
	private List<String> timePatterns = new ArrayList<String>();
	private static TolerantDateParser instance = null;

	public TolerantDateParser() {
		// date patterns
		datePatterns.add("yyyy-MM-dd");
		datePatterns.add("dd-MM-yyyy");
		datePatterns.add("dd/MM/yyyy");
		datePatterns.add("MM/dd/yyyy");
		datePatterns.add("dd.MM.yy");
		datePatterns.add("dd.MMM.yyyy");
		datePatterns.add("yyyyMMdd");
		datePatterns.add("MMM/dd/yyyy");
		datePatterns.add("dd.MMM.yy");
		datePatterns.add("d.M.yy");
		datePatterns.add("M/d/yy");
		datePatterns.add("d/M/yy");
		// time patterns
		timePatterns.add("HH:mm:ss.SSS");
		timePatterns.add("HH:mm:ss.S");
		timePatterns.add("HH:mm:ss");
		timePatterns.add("HH:mm");
		timePatterns.add("hh:mm aaa");
		timePatterns.add("K:mm a,z");
		timePatterns.add("h:mm a");
		timePatterns.add("hh:mm a");
		timePatterns.add("HHmmss");
		init();
	}
	
	public void init() {
		parsers.clear();
		for (String dp : datePatterns) {
			for (String tp : timePatterns) {
				parsers.add(FastDateFormat.getInstance(dp + " " + tp));
				parsers.add(FastDateFormat.getInstance(dp + "'T'" + tp));
				parsers.add(FastDateFormat.getInstance(dp + "'_'" + tp));
			}
			// it is important to set the pure date pattern at the end of the list!
			parsers.add(FastDateFormat.getInstance(dp));
		}
	}
	
	public void printPatterns() {
		if (parsers.isEmpty()) {
			throw new IllegalStateException("No parser initialised!");
		}
		for (FastDateFormat f : parsers) {
			System.out.println(f.getPattern());
		}
	}
	
	public void addDatePattern(String pattern) {
		if (datePatterns.contains(pattern) == false) {
			datePatterns.add(pattern);
		}
	}
	
	public void addTimePattern(String pattern) {
		if (timePatterns.contains(pattern) == false) {
			timePatterns.add(pattern);
		}
	}
	
	public static Date parseDate(String text) throws Exception {
		if (instance == null) {
			instance = new TolerantDateParser();
		}
		return instance.parseDateTolerant(text);
	}
	
	private Date parseDateTolerant(String text) throws Exception {
		if (parsers.isEmpty()) {
			throw new IllegalStateException("No parsers initialised!");
		}
		if (text != null && text.trim().isEmpty() == false) {
			for (FastDateFormat f : parsers) {
				try {
					return f.parse(text);
				} catch (ParseException pe) {}
			}
			throw new Exception("Text: " + text + " could not be parsed to a date. Non known pattern can be applied.");
		}
		return null;
	}
	
}
