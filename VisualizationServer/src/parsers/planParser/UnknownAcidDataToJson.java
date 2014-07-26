package parsers.planParser;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * 

 * @author Aviel and Chen
 *	This class responsible for holding the unknown acid Data before converting it to JSON *
 */
public class UnknownAcidDataToJson implements DataStructure{
	private String name;
	private String IDs;
	private String scd;
	private String dcd;
	private String rcd;
	private String vol;
	private String source_content;
	private String equation;
	private Boolean end_point;
	private int node_number;
	private int pos;
	private List<UnknownAcidDataToJson> children;
	private String side;

	private double ph; 
  
    public UnknownAcidDataToJson(NamedNodeMap attributeMap, String side) { 
        setChildren(new ArrayList<UnknownAcidDataToJson>());
        this.side = side;
		getAllFields(attributeMap);
    }

	private void getAllFields(NamedNodeMap attributeMap) {
		Node currNode;
		currNode = attributeMap.getNamedItem("IDs");
		if (currNode != null){
			this.setIDs(currNode.getNodeValue());
		} else {
			this.setIDs("");
		}
				
		currNode = attributeMap.getNamedItem("end_point");
		if (currNode != null){
			this.setEnd_point(currNode.getNodeValue().equals("YES") ? true : false);
		} else {
			this.setEnd_point(null);
		}
		
		currNode = attributeMap.getNamedItem("equation");
		if (currNode != null){
			this.setEquation(currNode.getNodeValue());
		} else {
			this.setEquation("");
		}
		
		
		currNode = attributeMap.getNamedItem("pos");
		if (currNode != null){
			this.setPos(Integer.parseInt(currNode.getNodeValue()));
		} else {
			this.setPos(-1);
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
			this.setScd(currNode.getNodeValue());
		} else {
			this.setScd("");
		}
		
		currNode = attributeMap.getNamedItem("dcd");
		if (currNode != null){
			this.setDcd(currNode.getNodeValue());
		} else {
			this.setDcd("");
		}
		
		currNode = attributeMap.getNamedItem("node_number");
		if (currNode != null){
			this.node_number = Integer.parseInt(currNode.getNodeValue());
		} else {
			this.node_number = -1;
		}
		
		currNode = attributeMap.getNamedItem("vol");
		if (currNode != null){
			this.setVol(currNode.getNodeValue());
		} else {
			this.setVol("");
		}
		
		currNode = attributeMap.getNamedItem("source_content");
		if (currNode != null){
			this.setSource_content(currNode.getNodeValue());
		} else {
			this.setSource_content("");
		}
		
		
		this.setName(this.ph + " PH; " + XMLReader.m_nodes_details.get(node_number + ""));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIDs() {
		return IDs;
	}

	public void setIDs(String iDs) {
		IDs = iDs;
	}

	public String getScd() {
		return scd;
	}

	public void setScd(String scd) {
		this.scd = scd;
	}

	public String getDcd() {
		return dcd;
	}

	public void setDcd(String dcd) {
		this.dcd = dcd;
	}

	public String getVol() {
		return vol;
	}

	public void setVol(String vol) {
		this.vol = vol;
	}

	public String getSource_content() {
		return source_content;
	}

	public void setSource_content(String source_content) {
		this.source_content = source_content;
	}

	public String getEquation() {
		return equation;
	}

	public void setEquation(String equation) {
		this.equation = equation;
	}

	public Boolean getEnd_point() {
		return end_point;
	}

	public void setEnd_point(Boolean end_point) {
		this.end_point = end_point;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public List<UnknownAcidDataToJson> getChildren() {
		return children;
	}

	public void setChildren(List<UnknownAcidDataToJson> children) {
		this.children = children;
	} 
    
    
}
