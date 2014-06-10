package parsers.planParser;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import parsers.IParse;



import com.google.gson.Gson;




public class PlanViewParser implements IParse {
	
	public PlanViewParser(){
		
	}

	private static int index = 0;
	/**
	 * @param args
	 */
	@Override
	public JSONObject parse(String fileContent) {
		Gson gson = new Gson();
		CheckReasonableResult crResult;
		crResult = 	XMLReader.checkReasonable(fileContent);
		List<State> stateList = crResult.getListOfStates();
		Node root = crResult.getRoot();
		List<StateToJson> jsonStateList = new ArrayList<StateToJson>();
		int index = 0;
		for (Iterator iterator = stateList.iterator(); iterator.hasNext();) {
			State state = (State) iterator.next();
			String side;
			if (stateList.size() / 2 > index) {
				side = "first";
			} else {
				side = "second";
			}
			
			jsonStateList.add(convertStateToJsonState(state, side));
			index++;
		}
		boolean inQuote = false;

		String s = gson.toJson(jsonStateList);
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
		for (int i = 0; i < s.length(); i++) {
			switch (s.charAt(i)) {
			case '{':
				sb.append(s.charAt(i));
				sb.append("\n");
				indent++;
				for (int j = 0; j < indent; j++) {
					sb.append("\t");
				}
				break;
			case ',':
				if (inQuote) 
					break;
				sb.append(s.charAt(i));
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
				sb.append(s.charAt(i));
				break;
			case '"':
				inQuote = !inQuote;
			default:
				sb.append(s.charAt(i));
				break;
			}		
		}
		sb.append("\n}\n");

		JSONObject jsonAns = null;
		try {
			jsonAns = new JSONObject(sb.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonAns;

	}

	private static  StateToJson convertStateToJsonState(State state, String side) {
		StateToJson ans = new StateToJson();
		if (state.firstChild != null){
			ans.children.add(convertStateToJsonState(state.firstChild, "first"));
		}
		if (state.secondChild != null){
			ans.children.add(convertStateToJsonState(state.secondChild, "second"));
		}
		
		ans.information = state.information;
		ans.epsilon = state.epsilon;
		ans.name = "name" + index;
		ans.side = side;
		index++;
		return ans;		
	}

}
