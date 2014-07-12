package parsers.planParser;
  
import java.util.ArrayList;
import java.util.List; 
  
public class OracleDataToJson implements DataStructure{ 
  
	String name;
    InformationState information; 
    List<OracleDataToJson> children;
    double epsilon;
	String side; 
  
    public OracleDataToJson() { 
        children = new ArrayList<OracleDataToJson>();
    } 
    
} 