package temporal.view.parser;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;


public class Flask {
	private String id;
	private String name;
	private String temp; 
	private String volume;  
	private String vesselType;
	private String vesselVolume;
	private List<Species> species;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTemp() {
		return temp;
	}
	public void setTemp(String temp) {
		this.temp = temp;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public String getVesselType() {
		return vesselType;
	}
	public void setVesselType(String vesselType) {
		this.vesselType = vesselType;
	}
	public String getVesselVolume() {
		return vesselVolume;
	}
	public void setVesselVolume(String vesselVolume) {
		this.vesselVolume = vesselVolume;
	}
	public List<Species> getSpecies() {
		return species;
	}
	public void setSpecies(List<Species> species) {
		this.species = species;
	}
	@SuppressWarnings("unchecked")
	public JSONObject toJson() {		
		JSONObject flaskJson = new JSONObject();
		flaskJson.put("id", id);
		flaskJson.put("name", name);
		flaskJson.put("temp", temp);
		flaskJson.put("volume", volume);
		flaskJson.put("vesselType", vesselType);
		flaskJson.put("vesselVolume", vesselVolume);
		List<JSONObject> speciesArray = new ArrayList<JSONObject>();
		for (int i = 0; i < species.size(); i++) {			
			speciesArray.add(species.get(i).toJson());
		}			
		flaskJson.put("species", speciesArray);
		return flaskJson;
	}
}
