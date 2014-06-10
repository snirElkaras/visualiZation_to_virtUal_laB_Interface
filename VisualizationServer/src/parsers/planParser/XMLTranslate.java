package parsers.planParser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.naming.NameNotFoundException;
import javax.smartcardio.ATR;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

public class XMLTranslate {

    public static void TranslateTree(Node root, String probName) {
        NodeList nodeLst = root.getChildNodes();
        //ArrayList<Node> newNodeList = new ArrayList<Node>();
        for (int i = 0; i < nodeLst.getLength(); i++) {
            if (i % 2 != 0) {
                Node node = nodeLst.item(i);
                node = TranslateTreeRec(node, probName);
                root.replaceChild(node, nodeLst.item(i));
                //newNodeList.add(node);
            }
        }
    }

    public static Node TranslateTreeRec(Node node, String probName) {

        NodeList nodeLst = node.getChildNodes();
        //parse the node
        NamedNodeMap att = node.getAttributes();

        if (att.getNamedItem("IDs") == null) {
            String sid = "";
            String did = "";
            String vol = "";
            String svol = "";
            String dvol = "";
            String rvol = "";
            String scd = "";
            String dcd = "";
            String rcd = "";
            String pos = "";
            String sc = "";
            while (att.getLength() > 0) {
                Node attNode = att.item(0);
                String attname = attNode.getNodeName();
                String attval = attNode.getNodeValue();
                if (attname.contains("_")) {
                    attname = attname.substring(0, attname.indexOf("_"));
                }
                if (attname.equalsIgnoreCase("sid")) {
                    sid = attval;
                }
                if (attname.equalsIgnoreCase("did")) {
                    did = attval;
                }
                if (attname.equalsIgnoreCase("vol")) {
                    vol = attval;
                }
                if (attname.equalsIgnoreCase("svol")) {
                    svol = attval;
                }
                if (attname.equalsIgnoreCase("dvol")) {
                    dvol = attval;
                }
                if (attname.equalsIgnoreCase("rvol")) {
                    rvol = attval;
                }
                if (attname.equalsIgnoreCase("scd")) {
                    scd = attval;
                }
                if (attname.equalsIgnoreCase("dcd")) {
                    dcd = attval;
                }
                if (attname.equalsIgnoreCase("rcd")) {
                    rcd = attval;
                }
                if (attname.equalsIgnoreCase("pos")) {
                    pos = attval;
                }
                if (attname.equalsIgnoreCase("sc")) {
                    sc = attval;
                }
                att.removeNamedItem(attNode.getNodeName());
            }

            String newIDs = "from " + sid + " to " + did;
            ((Element) node).setAttribute("IDs", newIDs);

            String newVol = vol + " / " + svol + " + " + dvol + " create " + rvol;
            if (nodeLst.getLength() > 1) {
                //there are children
                newVol = vol;
            }

            ((Element) node).setAttribute("vol", newVol);
            ((Element) node).setAttribute("scd", scd);
            ((Element) node).setAttribute("dcd", dcd);
            ((Element) node).setAttribute("rcd", rcd);
            ((Element) node).setAttribute("pos", pos);
            if (probName.equalsIgnoreCase("Unknown Acid Problem")) {
                ((Element) node).setAttribute("source_content", sc);
            }

        }
        //check if there are some children
        if (nodeLst.getLength() > 1) {
            //there are children
            if (nodeLst.getLength() <= 3) {
                //there is one child
                node = TranslateTreeRec(nodeLst.item(1), probName);
            } else {
                //there are two children
                node.replaceChild(TranslateTreeRec(nodeLst.item(1), probName), nodeLst.item(1));
                node.replaceChild(TranslateTreeRec(nodeLst.item(3), probName), nodeLst.item(3));
            }
        }
        return node;
    }

    public static String nodeToString(Node node) {
        try {
            // Set up the output transformer
            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");

            // Print the DOM node

            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(node);
            trans.transform(source, result);
            String xmlString = sw.toString();

            //System.out.println(xmlString);
            return xmlString;
        } catch (TransformerException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void ToXMLFile(Node root, String fileName) {
        try {
            // Create file 
            FileWriter fstream = new FileWriter(fileName);
            BufferedWriter out = new BufferedWriter(fstream);
            //out.write("<ROOT>\n");
            out.write(nodeToString(root));
            //NodeList nodeLst = root.getChildNodes();

            /*for (int i = 0; i < nodeLst.getLength(); i++) {
             if (i%2 != 0)
             {
             Node node = nodeLst.item(i);
             out.write(nodeToString(node));
             }
             }	
             out.write("</ROOT>");*/
            //Close the output stream
            out.close();
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }

    }
}
