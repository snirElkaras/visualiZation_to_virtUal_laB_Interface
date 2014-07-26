package parsers.planParser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import parsers.IParse;
import com.google.gson.Gson;



/**
 * 
 * @author Aviel and Chen
 * 
 */
public class PlanViewParser implements IParse {
	

	public PlanViewParser(){
		
	}

	private static int index = 0;

	@Override
	public JSONObject parse(String fileContent) {
		Gson gson = new Gson();
		String probName = null;
		Node root = null;
		List<DataStructure> dataStructure = null;
		
		// get root from the file content
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputStream is = new ByteArrayInputStream(fileContent.getBytes("UTF-8"));
			Document doc = db.parse(is);
			doc.getDocumentElement().normalize();
			NodeList nodeLstRoot = doc.getElementsByTagName("ROOT");

			root = nodeLstRoot.item(0);
			NamedNodeMap attributeMap = root.getAttributes();
			probName = attributeMap.getNamedItem("probName").toString();
		}
		catch (Exception e) {
			return null;
		}
		
		if (probName.toLowerCase().contains(GlobalVariables.unknown_acid_probName)){
			dataStructure = (List<DataStructure>) handleUnknownAcidProblem(root);
		} else 
			if (probName.toLowerCase().contains(GlobalVariables.oracle_probName)){
				dataStructure = (List<DataStructure>) handleOracleProblem(root);
			}
		
		
		String dataStructureAsJson = gson.toJson(dataStructure);
		
		StringBuilder sb = indentJson(root, dataStructureAsJson);
		JSONObject jsonAns = null;
		try {
			jsonAns = new JSONObject(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonAns;

	}

	private StringBuilder indentJson(Node root, String dataStructureAsJson) {
		boolean inQuote = false;
		StringBuilder sb = new StringBuilder();
		sb.append("{\n");
		sb.append("\t\"name\": \"root\",\n");
		NamedNodeMap attributeMap = root.getAttributes();
		for (int i = 0; i < attributeMap.getLength(); i++) {
			Node att = attributeMap.item(i);
			String attName = att.getNodeName();
			String attValue = att.getNodeValue();
			sb.append("\t\"" + attName + "\": \"" + attValue + "\",\n");
		}
		sb.append("\t\"children\": ");
		int indent = 1;
		for (int i = 0; i < dataStructureAsJson.length(); i++) {
			switch (dataStructureAsJson.charAt(i)) {
			case '{':
				sb.append(dataStructureAsJson.charAt(i));
				sb.append("\n");
				indent++;
			
				for (int j = 0; j < indent; j++) {
					sb.append("\t");
				}
				break;
			case ',':
				if (inQuote) 
					break;
				sb.append(dataStructureAsJson.charAt(i));
				sb.append("\n");
				for (int j = 0; j < indent; j++) {
					sb.append("\t");
				}
				break;
			case '}':
				sb.append("\n");
				indent--;
				for (int j = 0; j < indent; j++) {
					sb.append("\t");
				}
				sb.append(dataStructureAsJson.charAt(i));
				break;
			case '"':
				inQuote = !inQuote;
			default:
				sb.append(dataStructureAsJson.charAt(i));
				break;
			}		
		}
		sb.append("\n}\n");
		return sb;
	}

	private List<? extends DataStructure> handleOracleProblem(Node root) {
		List<OracleDataToJson> dataStructure = new ArrayList<>();
		CheckReasonableResult crResult = XMLReader.checkReasonable(root);
		List<State> stateList = crResult.getListOfStates();
		int index = 0;
		for (Iterator iterator = stateList.iterator(); iterator.hasNext();) {
			State state = (State) iterator.next();
			String side;
			if (stateList.size() / 2 > index) {
				side = "first";
			} else {
				side = "second";
			}
			
			dataStructure.add(convertStateToJsonState(state, side));
			index++;
		}
		return dataStructure;
	}

	private List<? extends DataStructure> handleUnknownAcidProblem(Node root) {
		XMLTranslate.TranslateTree(root, GlobalVariables.unknown_acid_probName);
		IDsStatus.init_IDs_states();
		NodeList nodeLst = root.getChildNodes();
		int num = 0;
		for (int i = 0; i < nodeLst.getLength(); i++) {
			if (i % 2 != 0) {
				GlobalVariables.num_of_leafs_analyzed = 0;
				Node node = nodeLst.item(i);
				XMLReader.InOrder_Accumulate_Tree_rec(node, num, GlobalVariables.m_material_Unknown_Acid);
				num += 10000;
			}
		}
		
		// create list of UnknownAcidDataToJson from root
		List<UnknownAcidDataToJson> dataStructure = new ArrayList<>();
		nodeLst = root.getChildNodes();
		String side = "";
		for (int i = 0; i < nodeLst.getLength(); i++) {
			if (i % 2 != 0) {
				Node node = nodeLst.item(i);
				int numberOfChildren = nodeLst.getLength() / 2;
				if (i < numberOfChildren / 2){
					side = "first";
				} else {
					side = "second";
				}
				UnknownAcidDataToJson currData = buildUnknownAcidData(node, side);
				dataStructure.add(currData);
			}
		}
		return dataStructure;
	}

	private UnknownAcidDataToJson buildUnknownAcidData(Node node, String side) {
		
		UnknownAcidDataToJson currDataNode = new UnknownAcidDataToJson(node.getAttributes(), side);
        Node left_node = node.getChildNodes().item(1);
        UnknownAcidDataToJson left = null;
        if (left_node != null){
        	left = buildUnknownAcidData(left_node, "first");
            currDataNode.getChildren().add(left);
        }
        Node right_node = node.getChildNodes().item(3);
        UnknownAcidDataToJson right = null;
        if (right_node != null){
        	right = buildUnknownAcidData(right_node, "second");
            currDataNode.getChildren().add(right);

        }
        
		return currDataNode;
	}

	private static  OracleDataToJson convertStateToJsonState(State state, String side) {
		OracleDataToJson ans = new OracleDataToJson();
		if (state.firstChild != null){
			ans.getChildren().add(convertStateToJsonState(state.firstChild, "first"));
		}
		if (state.secondChild != null){
			ans.getChildren().add(convertStateToJsonState(state.secondChild, "second"));
		}
		
		ans.setInformation(state.information);
		ans.setEpsilon(state.epsilon);
		ans.setName("name" + index);
		ans.setSide(side);
		index++;
		return ans;		
	}

}
