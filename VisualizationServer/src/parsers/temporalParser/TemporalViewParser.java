package parsers.temporalParser;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import parsers.IParse;

public class TemporalViewParser implements IParse{

	@Override
	public JSONObject parse(String fileContent) {
		JSONObject toReturn = new JSONObject();
		String[] xmlElementsAsList = fileContent.split("(?<=</event>)\n");
		ArrayList<WorkbenchAddFlask> addFlaskEventsList = new ArrayList<WorkbenchAddFlask>();
		ArrayList<SolutionMix> solutionMixEventList = new ArrayList<SolutionMix>();
		SAXBuilder builder;
		for (int i = 0; i < xmlElementsAsList.length; i++) {	
			try {
				builder = new SAXBuilder();
				String event = xmlElementsAsList[i];
				if(event==null || !event.startsWith("<event>")){						
					continue;
				}
				Document document = null;
				document = (Document) builder.build(new StringReader(event));
				Element rootNode = document.getRootElement();
				String event_id = rootNode.getChildText("id");
				String user = rootNode.getChildText("user");
				String event_session = rootNode.getChildText("session");
				String origin = rootNode.getChildText("origin");
				String timestamp = rootNode.getChild("timestamp").getChildText("millis");
				Element description = rootNode.getChild("description");
				String actionType = description.getAttribute("type").getValue();
				switch(actionType){
				case "WORKBENCH_ADD_COMPONENT":
					WorkbenchAddFlask addFlaskEvent = new WorkbenchAddFlask(event_id, user, event_session, origin, timestamp);
					addFlaskEvent.setWorkbench_id(description.getChild("workbench").getChildText("id"));
					Element component = description.getChild("component");
					Flask flask = new Flask();
					if(component.getAttributeValue("type").equals("FLASK")){
						Element flaskElement = component.getChild("flask");
						flask = buildFlask(flaskElement);
					}
					addFlaskEvent.setFlask(flask);
					addFlaskEvent.setReadable(description.getChildText("readable"));
					addFlaskEventsList.add(addFlaskEvent);
					break;
				case "SOLUTION_MIX":
					SolutionMix solutionMix = new SolutionMix(event_id, user, event_session, origin, timestamp);
					solutionMix.setVolume(description.getChildText("volume"));
					Flask src = new Flask();
					Element flaskElement = description.getChild("source").getChild("flask"); 
					src = buildFlask(flaskElement);
					solutionMix.setSource(src);
					Flask rcp = new Flask();
					flaskElement = description.getChild("recipient").getChild("flask"); 
					rcp = buildFlask(flaskElement);
					solutionMix.setRecipient(rcp);
					Flask res = new Flask();
					flaskElement = description.getChild("result").getChild("flask"); 
					res = buildFlask(flaskElement);
					solutionMix.setResult(res);
					solutionMix.setReadable(description.getChildText("readable"));
					solutionMixEventList.add(solutionMix);
					break;
				}
			} catch (JDOMException | IOException e) {
				return null;
			}
		}

		List<JSONObject> parsedAddFlaskElements = new ArrayList<JSONObject>(); 
		for (WorkbenchAddFlask addFlaskEvent : addFlaskEventsList) {
			parsedAddFlaskElements.add(addFlaskEvent.parse());
		}

		List<JSONObject> parsedSolMixElements = new ArrayList<JSONObject>(); 
		for (SolutionMix solMixEvent : solutionMixEventList) {
			parsedSolMixElements.add(solMixEvent.parse());
		}
		try {
			toReturn.put("add_flask_list", parsedAddFlaskElements);
			toReturn.put("solution_mix_list", parsedSolMixElements);
		} catch (JSONException e) {
			return null;
		}

		return toReturn;//TODO- return the 2 lists properly

	}


	private static Flask buildFlask(Element flaskElement) {
		Flask flask = new Flask();
		flask.setId(flaskElement.getChildText("id"));
		flask.setName(flaskElement.getChildText("name"));
		flask.setTemp(flaskElement.getChildText("temp"));
		flask.setVolume(flaskElement.getChildText("volume"));
		flask.setVesselType(flaskElement.getChild("vessel").getChildText("type"));
		flask.setVesselVolume(flaskElement.getChild("vessel").getChildText("volume"));
		List<Species> speciesList = new ArrayList<Species>();
		List<Element> speciesListElement = flaskElement.getChildren("species");
		for (Element sp : speciesListElement) {
			Species species = new Species(); 
			species.setId(sp.getChildText("id"));
			species.setName(sp.getChildText("name"));
			species.setMoles(sp.getChildText("moles"));
			species.setMass(sp.getChildText("mass"));
			species.setState(sp.getChildText("state"));
			speciesList.add(species);
		}
		flask.setSpecies(speciesList);
		return flask;
	}



}
