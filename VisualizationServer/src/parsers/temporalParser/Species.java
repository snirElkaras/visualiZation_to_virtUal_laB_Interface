package parsers.temporalParser;
import org.json.JSONException;
import org.json.JSONObject;


public class Species {
	private String id;
	private String name;
	private String moles;
	private String mass;
	private String state;


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMoles() {
		return moles;
	}
	public void setMoles(String moles) {
		this.moles = moles;
	}
	public String getMass() {
		return mass;
	}
	public void setMass(String mass) {
		this.mass = mass;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	public String createReadable() {
		return this.moles + " moles of " + this.name;
	}
	
	public JSONObject toJson() {
		JSONObject species = new JSONObject();
		try {
			species.put("id", id);
			species.put("name", name);
			species.put("moles", moles);
			species.put("mass", mass);
			species.put("state", state);
		} catch (JSONException e) {
			return null;
		}
		return species;
	}
}
