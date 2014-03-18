
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

}
