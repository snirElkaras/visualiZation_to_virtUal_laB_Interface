package temporal.view.parser;
import org.json.JSONObject;



public abstract class VirtualLabAction {
	protected String event_id;
	protected String user;
	protected String event_session;
	protected String origin;
	protected String timestamp;

	public abstract JSONObject parse();


}
