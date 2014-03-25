package temporal.view.parser;

import java.io.File;

import org.json.simple.JSONObject;

public interface IParse {
	public JSONObject parse(File file);
}
