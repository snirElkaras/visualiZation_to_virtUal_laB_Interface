package temporal.view.parser;
import org.json.simple.JSONObject;


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
	@SuppressWarnings("unchecked")
	public JSONObject toJson() {
		JSONObject species = new JSONObject();
		species.put("id", id);
		species.put("name", name);
		species.put("moles", moles);
		species.put("mass", mass);
		species.put("state", state);
		return species;
	}
}
