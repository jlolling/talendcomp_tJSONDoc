package de.jlo.talendcomp.json;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class ResourceUtil {

	public static String readTextResource(String resourcePath) throws Exception {
		if (resourcePath == null || resourcePath.trim().isEmpty()) {
			throw new IllegalArgumentException("resourcePath cannot be null or empty");
		}
		StringBuilder content = new StringBuilder();
		InputStream is = ResourceUtil.class.getResourceAsStream(resourcePath);
		if (is == null) {
			throw new Exception("Resource: " + resourcePath + " does not exist.");
		}
		BufferedReader r = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		try {
			String line = null;
			boolean firstLoop = true;
			while ((line = r.readLine()) != null) {
				if (firstLoop) {
					firstLoop = false;
				} else {
					content.append("\n");
				}
				content.append(line);
			}
		} catch (Exception e) {
			throw new Exception("Read resource: " + resourcePath + " failed: " + e.getMessage(), e);
		} finally {
			if (r != null) {
				r.close();
			}
		}
		return content.toString();
	}
	
}
