
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import au.com.bytecode.opencsv.CSVReader;

public class Main {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {

		Reader reader = new FileReader("C:/Users/snir/Documents/GitHub/visualiZation_to_virtUal_laB_Interface/TemporalParser/src/vlab_log_example.txt");
		CSVReader csvReader = new CSVReader(reader,',');
		try {
			List<String[]> split = csvReader.readAll();
			List<String> xmlElementsAsList = new ArrayList<String>();
			for (String[] stringList : split) {
				for (String str : stringList) {
					if(str!=null && str.startsWith("<event>")){						
						xmlElementsAsList.add(str);
					}
				}
			}
			ArrayList<WorkbenchAddFlask> addFlaskEventsList = new ArrayList<WorkbenchAddFlask>();
			ArrayList<SolutionMix> solutionMixEventList = new ArrayList<SolutionMix>();
			SAXBuilder builder;
			for (int i = 0; i < xmlElementsAsList.size(); i++) {	
				builder = new SAXBuilder();
				String event = xmlElementsAsList.get(i);
				Document document = (Document) builder.build(new StringReader(event));
				Element rootNode = document.getRootElement();
				List<Element> eventProperties = rootNode.getChildren();
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
					src.setId(flaskElement.getChildText("id"));
					src.setName(flaskElement.getChildText("name"));
					src.setTemp(flaskElement.getChildText("temp"));
					src.setVolume(flaskElement.getChildText("volume"));
					src.setVesselType(flaskElement.getChild("vessel").getChildText("type"));
					src.setVesselVolume(flaskElement.getChild("vessel").getChildText("volume"));
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
					src.setSpecies(speciesList);
					solutionMix.setSource(src);
					Flask rcp = new Flask();
					flaskElement = description.getChild("recipient").getChild("flask"); 
					rcp.setId(flaskElement.getChildText("id"));
					rcp.setName(flaskElement.getChildText("name"));
					rcp.setTemp(flaskElement.getChildText("temp"));
					rcp.setVolume(flaskElement.getChildText("volume"));
					rcp.setVesselType(flaskElement.getChild("vessel").getChildText("type"));
					rcp.setVesselVolume(flaskElement.getChild("vessel").getChildText("volume"));
					speciesList = new ArrayList<Species>();
					speciesListElement = flaskElement.getChildren("species");
					for (Element sp : speciesListElement) {
						Species species = new Species(); 
						species.setId(sp.getChildText("id"));
						species.setName(sp.getChildText("name"));
						species.setMoles(sp.getChildText("moles"));
						species.setMass(sp.getChildText("mass"));
						species.setState(sp.getChildText("state"));
						speciesList.add(species);
					}
					rcp.setSpecies(speciesList);
					solutionMix.setRecipient(rcp);
					
					
					Flask res = new Flask();
					flaskElement = description.getChild("result").getChild("flask"); 
					res.setId(flaskElement.getChildText("id"));
					res.setName(flaskElement.getChildText("name"));
					res.setTemp(flaskElement.getChildText("temp"));
					res.setVolume(flaskElement.getChildText("volume"));
					res.setVesselType(flaskElement.getChild("vessel").getChildText("type"));
					res.setVesselVolume(flaskElement.getChild("vessel").getChildText("volume"));
					speciesList = new ArrayList<Species>();
					speciesListElement = flaskElement.getChildren("species");
					for (Element sp : speciesListElement) {
						Species species = new Species(); 
						species.setId(sp.getChildText("id"));
						species.setName(sp.getChildText("name"));
						species.setMoles(sp.getChildText("moles"));
						species.setMass(sp.getChildText("mass"));
						species.setState(sp.getChildText("state"));
						speciesList.add(species);
					}
					res.setSpecies(speciesList);
					solutionMix.setResult(res);
					solutionMix.setReadable(description.getChildText("readable"));
					solutionMixEventList.add(solutionMix);
					break;
				}
			}
			
			List<String> parsedAddFlaskElements = new ArrayList<String>(); 
			for (WorkbenchAddFlask addFlaskEvent : addFlaskEventsList) {
				parsedAddFlaskElements.add(addFlaskEvent.parse());
			}
			
			List<String> parsedSolMixElements = new ArrayList<String>(); 
			for (SolutionMix solMixEvent : solutionMixEventList) {
				parsedSolMixElements.add(solMixEvent.parse());
			}

		} catch (IOException | JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
