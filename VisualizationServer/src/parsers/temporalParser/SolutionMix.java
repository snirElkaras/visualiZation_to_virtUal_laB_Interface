package parsers.temporalParser;
import org.json.JSONException;
import org.json.JSONObject;


public class SolutionMix extends VirtualLabAction{
	private String volume;
	private Flask source;
	private Flask recipient;
	private Flask result;
	private String readable;
	
	public SolutionMix(String event_id, String user, String event_session,
			String origin, String timestamp) {
		this.event_id = event_id;
		this.user = user;
		this.event_session = event_session;
		this.origin = origin;
		this.timestamp = timestamp;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public Flask getSource() {
		return source;
	}
	public void setSource(Flask source) {
		this.source = source;
	}
	public Flask getRecipient() {
		return recipient;
	}
	public void setRecipient(Flask recipient) {
		this.recipient = recipient;
	}
	public Flask getResult() {
		return result;
	}
	public void setResult(Flask result) {
		this.result = result;
	}
	public String getReadable() {
		return readable;
	}
	public void setReadable(String readable) {
		this.readable = readable;
	}
	@Override
	public JSONObject parse() {		
		JSONObject response = new JSONObject();
		try {
		response.put("event_id", event_id);
		response.put("user", user);
		response.put("event_session", event_session);
		response.put("origin", origin);
		response.put("timestamp", timestamp);
		response.put("volume", volume);
		response.put("source_flask", source.toJson());
		response.put("recipient_flask", recipient.toJson());
		response.put("result_flask", result.toJson());
		response.put("readable", readable);
		response.put("readableAmount", volume + "L");
		response.put("readableSrc", source.createReadable());
		response.put("readableRcp", recipient.createReadable());
		response.put("readableRes", result.createReadable());
		} catch (JSONException | NullPointerException e ) {
			return null;
		}
		return response;
	}

}
