package parsers.planParser;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class UnknownAcidDataToJson implements DataStructure{
	String name;
	
	String IDs;
	String scd;
	String dcd;
	String rcd;
	String vol;
	String source_content;
	String equation;
	Boolean end_point;
	int node_number;
	int pos;
	
    List<UnknownAcidDataToJson> children;
	String side;

	private Object ph; 
  
    public UnknownAcidDataToJson(NamedNodeMap attributeMap, String side) { 
        children = new ArrayList<UnknownAcidDataToJson>();
        this.side = side;
		getAllFields(attributeMap);
    }

	private void getAllFields(NamedNodeMap attributeMap) {
		Node currNode;
		currNode = attributeMap.getNamedItem("IDs");
		if (currNode != null){
			this.IDs = currNode.getNodeValue();
		} else {
			this.IDs = "";
		}
				
		currNode = attributeMap.getNamedItem("end_point");
		if (currNode != null){
			this.end_point = currNode.getNodeValue().equals("YES") ? true : false;
		} else {
			this.end_point = null;
		}
		
		currNode = attributeMap.getNamedItem("equation");
		if (currNode != null){
			this.equation = currNode.getNodeValue();
		} else {
			this.equation = "";
		}
		
		
		currNode = attributeMap.getNamedItem("pos");
		if (currNode != null){
			this.pos = Integer.parseInt(currNode.getNodeValue());
		} else {
			this.pos = -1;
		}
		
		currNode = attributeMap.getNamedItem("rcd");
		if (currNode != null){
			this.rcd = currNode.getNodeValue();
			this.ph = XMLReader.Get_PH_From_rcd(this.rcd, "H+");
		} else {
			this.rcd = "";
		}
		
		currNode = attributeMap.getNamedItem("scd");
		if (currNode != null){
			this.scd = currNode.getNodeValue();
		} else {
			this.scd = "";
		}
		
		currNode = attributeMap.getNamedItem("dcd");
		if (currNode != null){
			this.dcd = currNode.getNodeValue();
		} else {
			this.dcd = "";
		}
		
		currNode = attributeMap.getNamedItem("node_number");
		if (currNode != null){
			this.node_number = Integer.parseInt(currNode.getNodeValue());
		} else {
			this.node_number = -1;
		}
		
		currNode = attributeMap.getNamedItem("vol");
		if (currNode != null){
			this.vol = currNode.getNodeValue();
		} else {
			this.vol = "";
		}
		
		currNode = attributeMap.getNamedItem("source_content");
		if (currNode != null){
			this.source_content = currNode.getNodeValue();
		} else {
			this.source_content = "";
		}
		
		
		this.name = this.ph + " PH; " + XMLReader.m_nodes_details.get(node_number + "");
	} 
    
    
}
