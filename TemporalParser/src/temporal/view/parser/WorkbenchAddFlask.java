package temporal.view.parser;
import org.json.simple.JSONObject;

public class WorkbenchAddFlask extends VirtualLabAction{
	private String workbench_id;
	private Flask flask;
	private String readable;
	
	public WorkbenchAddFlask(String event_id, String user,
			String event_session, String origin, String timestamp) {
		this.event_id = event_id;
		this.user = user;
		this.event_session = event_session;
		this.origin = origin;
		this.timestamp = timestamp;
	}
	public String getWorkbench_id() {
		return workbench_id;
	}
	public void setWorkbench_id(String workbench_id) {
		this.workbench_id = workbench_id;
	}
	public Flask getFlask() {
		return flask;
	}
	public void setFlask(Flask flask) {
		this.flask = flask;
	}
	public String getReadable() {
		return readable;
	}
	public void setReadable(String readable) {
		this.readable = readable;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject parse() {
		JSONObject response = new JSONObject();
		response.put("event_id", event_id);
		response.put("user", user);
		response.put("event_session", event_session);
		response.put("origin", origin);
		response.put("timestamp", timestamp);
		response.put("workbench_id", workbench_id);
		response.put("flask", flask.toJson());
		response.put("readable", readable);
		return response;
	}

}
