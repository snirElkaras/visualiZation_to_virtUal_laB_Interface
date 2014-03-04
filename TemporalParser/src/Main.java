
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Scanner;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class Main {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {

		//Get scanner instance
		Scanner scanner = new Scanner(new File("C:/Users/snir/workspaceNew/TemporalParser/src/virtualLabs1.csv"));



		LogObject log;
		String line;
		while (scanner.hasNextLine()) 
		{
			line = scanner.nextLine();
			String[] split=line.split("\t");
			log  = new LogObject(split);
			SAXBuilder builder = new SAXBuilder();

			try {

				Document document = (Document) builder.build(new StringReader(log.getMessage()));
				Element rootNode = document.getRootElement();
				List list = rootNode.getChildren("semantic_event");
				Element node = (Element) list.get(0);

				System.out.println("description : " + node.getChildText("description"));
			} catch (IOException io) {
				System.out.println(io.getMessage());
			} catch (JDOMException jdomex) {
				System.out.println(jdomex.getMessage());
			}
		}

		//Do not forget to close the scanner  
		scanner.close();
	}

}
