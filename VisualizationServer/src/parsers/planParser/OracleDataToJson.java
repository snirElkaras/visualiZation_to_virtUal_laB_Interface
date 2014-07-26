package parsers.planParser;
  
import java.util.ArrayList;
import java.util.List; 
  
/**
 * 
 * @author Aviel and Chen
 *	This class responsible for holding the oracle Data before converting it to JSON
 */
public class OracleDataToJson implements DataStructure{ 
  
	private String name;
	private InformationState information; 
	private List<OracleDataToJson> children;
	private double epsilon;
	private String side; 
  
    public OracleDataToJson() { 
        setChildren(new ArrayList<OracleDataToJson>());
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public InformationState getInformation() {
		return information;
	}

	public void setInformation(InformationState information) {
		this.information = information;
	}

	public List<OracleDataToJson> getChildren() {
		return children;
	}

	public void setChildren(List<OracleDataToJson> children) {
		this.children = children;
	}

	public double getEpsilon() {
		return epsilon;
	}

	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
	}

	public String getSide() {
		return side;
	}

	public void setSide(String side) {
		this.side = side;
	} 
    
    
} 