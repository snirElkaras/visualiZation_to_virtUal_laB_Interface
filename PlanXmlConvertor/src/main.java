import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import reasonable.*;




public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Gson gson = new Gson();
/*		State stateFather = new State();
		State stateSon1 = new State();
		State stateSon2 = new State();
		stateFather.firstChild = stateSon1;
		stateFather.secondChild = stateSon2;*/
		List<State> stateList = XMLReader.checkReasonable("data/XmlFiles/demo.xml");
		List<StateToJson> jsonStateList = new ArrayList<>();
		for (Iterator iterator = stateList.iterator(); iterator.hasNext();) {
			State state = (State) iterator.next();
			jsonStateList.add(convertStateToJsonState(state));
		}
		
		String s = gson.toJson(jsonStateList);
		StringBuilder sb = new StringBuilder();
		int indent = 0;
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
			default:
				sb.append(s.charAt(i));
				break;
			}		
		}
		
		System.out.println(sb.toString());
		try {
			FileWriter fw = new FileWriter(new File("data/JsonFiles/demo.json"));
			fw.write(sb.toString());
			fw.flush();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	private static  StateToJson convertStateToJsonState(State state) {
		StateToJson ans = new StateToJson();
		if (state.firstChild != null){
			ans.children.add(convertStateToJsonState(state.firstChild));
		}
		if (state.secondChild != null){
			ans.children.add(convertStateToJsonState(state.secondChild));
		}
		
		ans.information = state.information;
		ans.epsilon = state.epsilon;
		ans.name = "name";
		
		return ans;		
	}

}
