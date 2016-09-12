package examples;

import java.io.File;
import java.io.IOException;
import java.util.Stack;
import java.util.TreeSet;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonMappingException;

public class Streaming {

	private Stack<String> stack = new Stack<String>();
	private TreeSet<String> set = new TreeSet<String>();
	
	public static void main(String[] args) {
		Streaming s = new Streaming();
		s.read();
	}
	
	public void read() {
		try {
			JsonFactory jfactory = new JsonFactory();

			/*** read from file ***/
			JsonParser jParser = jfactory.createParser(new File("/Volumes/Data/Talend/testdata/json/test_1.json"));

			// loop until token equal to "}"
			JsonToken token = null;
			int level = 0;
			while ((token = jParser.nextValue()) != null) {
				String fieldname = jParser.getCurrentName();
				String value = jParser.getValueAsString();
				System.out.println(token);
 				System.out.println("---------------------");
				if (token == JsonToken.START_ARRAY) {
					if (fieldname == null) {
						fieldname = "";
					}
					System.out.println("push " + level + ": "  + stack.push(fieldname));
					String path = getStackPath();
					System.out.println("path: " + path);
					level++;
				} else if (token == JsonToken.START_OBJECT) {
					if (fieldname == null) {
						fieldname = "";
					}
					System.out.println("push " + level + ": "  + stack.push(fieldname));
					System.out.println("path: " + getStackPath());
					level++;
				} else if (token == JsonToken.END_ARRAY || token == JsonToken.END_OBJECT) {
					level--;
					System.out.println("path: " + getStackPath());
					System.out.println("pop " + level + ": " + stack.pop());
				} else if (token == JsonToken.VALUE_STRING || token == JsonToken.VALUE_NUMBER_INT || token == JsonToken.VALUE_NUMBER_FLOAT) {
	 				System.out.println(getStackPath() + "." + fieldname + " : " + value);
				}
 				System.out.println("#######################");
			}
			jParser.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String getStackPath() {
		int size = stack.size();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			String name = stack.get(i);
			if (i > 0) {
				if (name.isEmpty()) {
					continue;
				}
				sb.append(".");
			} else {
				if (name.isEmpty()) {
					name = "$";
				}
			}
			sb.append(name);
		}
		return sb.toString();
	}
	
}
