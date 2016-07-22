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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.time.FastDateFormat;

import de.jlo.talendcomp.TolerantDateParser;

public final class TypeUtil {
	
	private final Map<String, DecimalFormat> numberformatMap = new HashMap<String, DecimalFormat>();
	
	public TypeUtil() {}
	
	public DecimalFormat getNumberFormat(String localeStr) {
		DecimalFormat nf = (DecimalFormat) numberformatMap.get(localeStr);
		if (nf == null) {
			Locale locale = new Locale(localeStr);
			nf = (DecimalFormat) NumberFormat.getInstance(locale);
			numberformatMap.put(localeStr, nf);
		}
		return nf;
	}
	
	public Object convertToDatatype(String value, String dataType, String options) throws Exception {
		if ("String".equals(dataType)) {
			if (value == null || value.isEmpty()) {
				return null;
			} else {
				return value;
			}
		} else if ("Integer".equals(dataType)) {
			return convertToInteger(value, options);
		} else if ("Boolean".equals(dataType)) {
			return convertToBoolean(value);
		} else if ("Date".equals(dataType)) {
			return convertToDate(value, options);
		} else if ("Timestamp".equals(dataType)) {
			return convertToTimestamp(value, options);
		} else if ("BigDecimal".equals(dataType)) {
			return convertToBigDecimal(value, options);
		} else if ("Double".equals(dataType)) {
			return convertToDouble(value, options);
		} else if ("Float".equals(dataType)) {
			return convertToFloat(value, options);
		} else if ("Long".equals(dataType)) {
			return convertToLong(value, options);
		} else if ("Short".equals(dataType)) {
			return convertToShort(value, options);
		} else if ("Character".equals(dataType)) {
			return convertToChar(value, options);
		} else {
			throw new Exception("Unsupported dataType:" + dataType);
		}
	}
	
	/**
	 * concerts the string format into a Date
	 * @param dateString
	 * @param pattern
	 * @return the resulting Date
	 */
	public Date convertToDate(String dateString, String pattern) throws Exception {
		if (dateString == null || dateString.isEmpty()) {
			return null;
		}
		if (pattern == null) {
			throw new Exception("pattern cannot be null or empty");
		}
		try {
			FastDateFormat sdf = FastDateFormat.getInstance(pattern);
			Date date = null;
			try {
				date = sdf.parse(dateString);
			} catch (ParseException pe) {
				date = TolerantDateParser.parseDate(dateString);
			}
			return date;
		} catch (Throwable t) {
			throw new Exception("Failed to convert string to date:" + t.getMessage(), t);
		}
	}
	
	public Timestamp convertToTimestamp(String dateString, String pattern) throws Exception {
		Date date = convertToDate(dateString, pattern);
		if (date != null) {
			return new Timestamp(date.getTime());
		} else {
			return null;
		}
	}

	public Boolean convertToBoolean(String value) throws Exception {
		if (value == null) {
			return null;
		}
		value = value.toLowerCase();
		if ("true".equals(value)) {
			return Boolean.TRUE;
		} else if ("false".equals(value)) {
			return Boolean.FALSE;
		} else if ("1".equals(value)) {
			return Boolean.TRUE;
		} else if ("0".equals(value)) {
			return Boolean.FALSE;
		} else if ("yes".equals(value)) {
			return Boolean.TRUE;
		} else if ("y".equals(value)) {
			return Boolean.TRUE;
		} else if ("sí".equals(value)) {
			return Boolean.TRUE;
		} else if ("да".equals(value)) {
			return Boolean.TRUE;
		} else if ("no".equals(value)) {
			return Boolean.FALSE;
		} else if ("нет".equals(value)) {
			return Boolean.FALSE;
		} else if ("n".equals(value)) {
			return Boolean.FALSE;
		} else if ("ja".equals(value)) {
			return Boolean.TRUE;
		} else if ("j".equals(value)) {
			return Boolean.TRUE;
		} else if ("nein".equals(value)) {
			return Boolean.FALSE;
		} else if ("oui".equals(value)) {
			return Boolean.TRUE;
		} else if ("non".equals(value)) {
			return Boolean.FALSE;
		} else if ("ok".equals(value)) {
			return Boolean.TRUE;
		} else if ("x".equals(value)) {
			return Boolean.TRUE;
		} else if (value != null) {
			return Boolean.FALSE;
		} else {
			return null;
		}
	}

	public Double convertToDouble(String value, String locale) throws Exception {
		if (value == null || value.isEmpty()) {
			return null;
		}
		DecimalFormat decfrm = getNumberFormat(locale);
		decfrm.setParseBigDecimal(false);
		return decfrm.parse(value).doubleValue();
	}

	public Integer convertToInteger(String value, String locale) throws Exception {
		if (value == null || value.isEmpty()) {
			return null;
		}
		DecimalFormat decfrm = getNumberFormat(locale);
		decfrm.setParseBigDecimal(false);
		return decfrm.parse(value).intValue();
	}
	
	public Short convertToShort(String value, String locale) throws Exception {
		if (value == null || value.isEmpty()) {
			return null;
		}
		DecimalFormat decfrm = getNumberFormat(locale);
		decfrm.setParseBigDecimal(false);
		return decfrm.parse(value).shortValue();
	}

	public Character convertToChar(String value, String locale) throws Exception {
		if (value == null || value.isEmpty()) {
			return null;
		}
		return value.charAt(0);
	}

	public Float convertToFloat(String value, String locale) throws Exception {
		if (value == null || value.isEmpty()) {
			return null;
		}
		DecimalFormat decfrm = getNumberFormat(locale);
		decfrm.setParseBigDecimal(false);
		return decfrm.parse(value).floatValue();
	}

	public Long convertToLong(String value, String locale) throws Exception {
		if (value == null || value.isEmpty()) {
			return null;
		}
		DecimalFormat decfrm = getNumberFormat(locale);
		decfrm.setParseBigDecimal(false);
		return decfrm.parse(value).longValue();
	}

	public BigDecimal convertToBigDecimal(String value, String locale) throws Exception {
		if (value == null || value.isEmpty()) {
			return null;
		}
		try {
			DecimalFormat decfrm = getNumberFormat(locale);
			decfrm.setParseBigDecimal(true);
			ParsePosition pp = new ParsePosition(0);
			return (BigDecimal) decfrm.parse(value, pp);
		} catch (RuntimeException e) {
			throw new Exception("convertToBigDecimal:" + value + " failed:" + e.getMessage(), e);
		}
	}

    public double roundScale(double value, int scale) {
    	double d = Math.pow(10, scale);
        return Math.round(value * d) / d;
    }
 
}
