/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers.planParser;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


/**
 *
 * @author acohen12
 */
public class GlobalVariables {

    public static String m_main_form_title = "-M C S A |  Monitoring Chemistry Students Activities";
    public static String m_main_form_icon_file = "Images/name.png";
    public static String m_xml_configuration_filename = "MCSA_config.XML";
    
    public static String m_material_NaOH = "NaOH";
    public static String m_material_Unknown_Acid = "Unknown_acid";
    public static String m_material_NaOH_Display = "NaOH";
    public static String m_material_Unknown_Acid_Display = "Unknown Acid";
    public static String m_material_Oracle_Problem = "Oracle_problem";
    public static double m_end_point = 9.5;    
    
    public static Document m_xml_configuration_doc;
    public static String m_python32_path;
    public static String m_MCSA_version = "v1.8";
    public static Node m_current_root;
    public static int num_of_leafs_analyzed = 0;
    
    //******************************* Prefuse Tree *************************************//
    
    //public static int m_tree_end_point_background_color = ColorLib.rgb(200, 120, 111);
//    public static int m_tree_end_point_background_color = ColorLib.rgb(250, 128, 114);
//    public static int m_tree_edge_color = ColorLib.rgb(0, 0, 0);
//    public static int m_tree_text_color = ColorLib.rgb(0, 0, 0);
//    public static int m_tree_background_focus_item_color = ColorLib.rgb(255, 255, 0);
//    public static int m_tree_background_trace_item_color = ColorLib.rgb(252, 255, 203);
//    public static int m_tree_mouse_hover_label_size = 18;

//    public static void Update_Global_Variables_From_Configuration_XML() {
//        String[] rgb;
//
//        m_xml_configuration_doc = XMLReader.Get_XML_Document_From_Jar(GlobalVariables.m_xml_configuration_filename);
//
//        m_python32_path = XMLReader.Get_XML_DOC_Node_TextContent(m_xml_configuration_doc, "python32_path");
//
//        rgb = XMLReader.Get_XML_DOC_Node_TextContent(m_xml_configuration_doc, "tree_edge_color_RGB").split(",");
//        m_tree_edge_color = ColorLib.rgb(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
//        rgb = XMLReader.Get_XML_DOC_Node_TextContent(m_xml_configuration_doc, "tree_text_color_RGB").split(",");
//        m_tree_text_color = ColorLib.rgb(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
//        rgb = XMLReader.Get_XML_DOC_Node_TextContent(m_xml_configuration_doc, "tree_background_focus_item_color_RGB").split(",");
//        m_tree_background_focus_item_color = ColorLib.rgb(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
//    }
//    
//    public static void Clear_Tree_Data()
//    {
//        Form_StudentPresentation.JPanel_CurrentTab.m_curr_nodes.clear();
//        Form_StudentPresentation.JPanel_CurrentTab.m_number_of_nodes = 0;
//        init_IDs_states();
//    }
//    
    // init IDs state for new tree analysis
    public static void init_IDs_states() {
        for (int i = 0; i < IDsStatus.m_IDs.length; i++) {
        	IDsStatus.m_IDs[i] = new ID();
        }
    }
}
