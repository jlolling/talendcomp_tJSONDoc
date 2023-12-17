package de.jlo.talendcomp.json;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;

import de.jlo.talendcomp.json.ops.JSONValue;
import de.jlo.talendcomp.json.ops.Traverse;


public class TestTraverse {
	
	@Test
	public void testTraverseRecursiv() throws Exception {
		String json = "{ \"_id\" : \"503d1d4794379b47ca011b57\", \"ad_id\" : 166990753, \"rate\":0.23, " +
				"\"2012-09-11\" : " +
					"{ \"publisher\" : [" +
						"{ \"MESA-MPL-emobilelv\" : \"xxx\", \"MESA-buyingtool\" : 1, \"MESA-mpe_autoonline\" : 1, \"SITE-GERMANY\" : 2, \"kleinanzeigen\" : 2, \"mob-iPhone\" : 1 }, " +
						"{ \"MESA-MPL-emobilelvX\" : 1, \"MESA-buyingtoolX\" : 1, \"MESA-mpe_autoonlineX\" : 1, \"SITE-GERMANYX\" : 2, \"kleinanzeigenX\" : 2, \"mob-iPhoneX\" : 1 } ], " +
						"\"total\" : [11,22,33] }" +
					" }";
		JsonDocument doc = new JsonDocument(json);
		JsonNode o = doc.getRootNode();
		System.out.println(o);
		Traverse helper = new Traverse();
		List<JSONValue> result = helper.traverse(o);
		int expected = 18;
		int actual = 0;
		for (JSONValue value : result) {
			System.out.println(value);
			actual++;
		}
		assertEquals(expected, actual);
	}

	@Test
	public void testTraverseNotRecursiv() throws Exception {
		String json = "{ \"_id\" : \"503d1d4794379b47ca011b57\", \"ad_id\" : 166990753, " +
				"\"2012-09-11\" : " +
					"{ \"publisher\" : [" +
						"{ \"MESA-MPL-emobilelv\" : \"xxx\", \"MESA-buyingtool\" : 1, \"MESA-mpe_autoonline\" : 1, \"SITE-GERMANY\" : 2, \"kleinanzeigen\" : 2, \"mob-iPhone\" : 1 }, " +
						"{ \"MESA-MPL-emobilelvX\" : 1, \"MESA-buyingtoolX\" : 1, \"MESA-mpe_autoonlineX\" : 1, \"SITE-GERMANYX\" : 2, \"kleinanzeigenX\" : 2, \"mob-iPhoneX\" : 1 } ], " +
						"\"total\" : [11,22,33] }" +
					" }";
		JsonDocument doc = new JsonDocument(json);
		JsonNode o = doc.getRootNode();
		System.out.println(o);
		Traverse helper = new Traverse();
		helper.setMaxLevel(1);
		List<JSONValue> result = helper.traverse(o);
		int expected = 2;
		int actual = 0;
		for (JSONValue value : result) {
			System.out.println(value);
			actual++;
		}
		assertEquals(expected, actual);
	}

}
