package parsers.temporalParser;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;


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
	public String createReadable() {
		String ans = this.name;
		StringBuilder sb = new StringBuilder();
		for (Species currSpecies : this.species) {
			sb.append(currSpecies.createReadable() + ", ");
		}
		String spcs = sb.toString();
		if(null != spcs && !spcs.equals("")){
			spcs = sb.toString().substring(0, spcs.length()-2);
			ans += " (" + spcs + ")";  
		}
		return ans;
	}

	public JSONObject toJson() {		
		JSONObject flaskJson = new JSONObject();
		try {
			flaskJson.put("vesselVolume", vesselVolume);
			flaskJson.put("id", id);
			flaskJson.put("name", name);
			flaskJson.put("temp", temp);
			flaskJson.put("volume", volume);
			flaskJson.put("vesselType", vesselType);
			List<JSONObject> speciesArray = new ArrayList<JSONObject>();
			for (int i = 0; i < species.size(); i++) {			
				speciesArray.add(species.get(i).toJson());
			}			
			flaskJson.put("species", speciesArray);
		} catch (JSONException e) {
			return null;
		}
		return flaskJson;
	}
}
