package temporal.view.parser;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.jdom2.JDOMException;
import org.json.JSONObject;

public class SnirMain {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	public static void main(String[] args) throws IOException, JDOMException {
		// TODO delete this class
		String path =  ("C:/Users/snir/Documents/GitHub/visualiZation_to_virtUal_laB_Interface/TemporalParser/files/Convert_Example1_vlab_XMLFormat.log");
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		Charset encoding = Charset.defaultCharset(); 
		String str = encoding.decode(ByteBuffer.wrap(encoded)).toString();
		JSONObject res = new TemporalViewParser().parse(str);
		System.out.println(res);
		
		
//		String event = "<event><blabla>1</blabla></event><event><blabla>2</blabla></event>";
//		String[] xmlElementsAsList = event.split("(?<=</event>)");
//		for (String string : xmlElementsAsList) {
//			System.out.println(string);
//			SAXBuilder builder = new SAXBuilder();
//			Document document = (Document) builder.build(new StringReader(string));
//			Element rootNode = document.getRootElement();
//			System.out.println(rootNode);
//		}

	}

}
