import java.util.List;


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
}
