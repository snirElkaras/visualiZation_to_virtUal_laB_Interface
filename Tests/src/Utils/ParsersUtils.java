package Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import parsers.temporalParser.Flask;
import parsers.temporalParser.Species;

public class ParsersUtils {
	
	public static String path = "C:/Users/snir/Documents/GitHub/visualiZation_to_virtUal_laB_Interface/Tests/src/files/";
	
	public static Flask generateFlask(String name) {
		Flask flaskToReturn = new Flask();
		flaskToReturn.setId(name);
		flaskToReturn.setName("flask"+name);
		flaskToReturn.setSpecies(generateSpecies("sp"));
		flaskToReturn.setTemp("temp"+name);
		flaskToReturn.setVesselType("vesselType"+name);
		flaskToReturn.setVesselVolume("vesselVolume"+name);
		flaskToReturn.setVolume("volume"+name);
		return flaskToReturn;
	}


	public static List<Species> generateSpecies(String name) {
		List<Species> speciesList = new ArrayList<Species>();
		for (int i = 1; i < 4; i++) {
			speciesList.add(generateSingleSpecies(i+"name"));
		}
		return speciesList;
	}


	public static Species generateSingleSpecies(String name) {
		Species sp;
		sp = new Species();
		sp.setId(name+"id");
		sp.setMass(name+"mass");
		sp.setMoles(name+"moles");
		sp.setName(name+"name");
		sp.setState(name+"state");
		return sp;
	}
	
	public static String fileToString(String fileUrl) throws Exception{
		String fileContent = "";
		BufferedReader br = new BufferedReader(new FileReader(fileUrl));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			fileContent = sb.toString();
		} finally {
			br.close();
		}
		return fileContent;
	}

}


