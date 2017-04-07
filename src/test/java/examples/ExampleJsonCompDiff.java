package examples;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import de.jlo.talendcomp.json.JsonDocument;
import de.jlo.talendcomp.json.ops.Diff;

public class ExampleJsonCompDiff {

	public static void main(String[] args) throws Exception {
		File f1 = new File("/Volumes/Data/projects/gvl/testdata/result_old.json");
		File f2 = new File("/Volumes/Data/projects/gvl/testdata/result_new.json");
		JsonNode refNode = new JsonDocument(f1).getRootNode();
		JsonNode testNode = new JsonDocument(f2).getRootNode();
		Diff comp = new Diff();
		comp.setTakeEmptyLikeNull(true);
		comp.setIgnoreArrayIndex(true);
		List<Diff.Difference> result = comp.findDifference(refNode, testNode);
		for (Diff.Difference diff : result) {
			System.out.println(diff);
		}
	}

}
