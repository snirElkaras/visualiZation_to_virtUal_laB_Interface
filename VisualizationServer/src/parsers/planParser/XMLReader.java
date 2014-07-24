package parsers.planParser;

import java.io.File;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;




public class XMLReader {

	public static String m_attribute_equation = "equation";
	public static String m_attribute_end_point = "end_point";
	private static boolean firstReactionOccured = false;

	public static CheckReasonableResult checkReasonable(Node root) {
		try {

			XMLTranslate.TranslateTree(root, "oracle.xml");
			NodeList nodeLst = root.getChildNodes();
			int length = nodeLst.getLength();

			ArrayList<State> listOfStates = new ArrayList<State>();
			boolean firstReaction = false;
			for (int i = 0; i < nodeLst.getLength(); i++) {
				if (i % 2 != 0) {
					Node node = nodeLst.item(i);
					//System.out.println(node.getNodeName());
//					NamedNodeMap attributeMap = node.getAttributes();

					//System.out.println(node.getAttributes().getLength());

					State s = buildDataStructure(node);
					s.information.isHigherCompAct = true;

					//let's check which of the states are reasonable
					firstReaction = checkReasonableAndReaction(s, firstReaction);
					s.information.isDirectChildOfRoot = true;
					
					if (s.information.hasReaction == true) {
						if (firstReaction == false) {
							firstReaction = true;
						}
						s.information.isReasonable = true;
					} else {
						if (firstReaction == false) {
							s.information.isReasonable = true;
						}
					}

					listOfStates.add(s);
				}
			}
			firstReactionOccured = false;
						
			return new CheckReasonableResult(root, listOfStates);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	private static void paintTreeNodes(State s) {


		if (s.information.hasReaction == true){
			s.information.color = "Green";
		} else
			if (s.information.hasChildrenWithReaction){
				s.information.color = "Orange";
			} else
				if (firstReactionOccured) {
					s.information.color = "Red";
				}
/*		if (s.hasFirstChild()){
			paintTreeNodes(s.getFirstChild());
		}
		if (s.hasSecondChild()){
			paintTreeNodes(s.getSecondChild());
		} */

	}


	public static State buildDataStructure(Node node) {
		State ans = new State();
		NodeList nodeLst = node.getChildNodes();
		//check if there are some children
		if (nodeLst.getLength() > 1) {
			//there are children
			if (nodeLst.getLength() <= 3) {
				//there is one child
				ans.firstChild = buildDataStructure(nodeLst.item(1));
				//ans.information.hasReaction = ans.firstChild.information.hasReaction; 

				/*
				 * String nodeName = node.getNodeName(); //check if the node is C
				 */

				ans.information.totalVol = ans.firstChild.information.totalVol;

				ans.information.amount_A = ans.firstChild.information.amount_A;
				ans.information.amount_B = ans.firstChild.information.amount_B;
				ans.information.amount_C = ans.firstChild.information.amount_C;
				ans.information.amount_D = ans.firstChild.information.amount_D;

				ans.information.actualAmount_A = ans.firstChild.information.actualAmount_A;
				ans.information.actualAmount_B = ans.firstChild.information.actualAmount_B;
				ans.information.actualAmount_C = ans.firstChild.information.actualAmount_C;
				ans.information.actualAmount_D = ans.firstChild.information.actualAmount_D;

				ans.information.srcAmount_A = ans.firstChild.information.srcAmount_A;
				ans.information.srcAmount_B = ans.firstChild.information.srcAmount_B;
				ans.information.srcAmount_C = ans.firstChild.information.srcAmount_C;
				ans.information.srcAmount_D = ans.firstChild.information.srcAmount_D;

				ans.information.pos = ans.firstChild.information.pos;

				ans.information.ids = ans.firstChild.information.ids;

				ans.information.scdDesc = ans.firstChild.information.scdDesc;
				ans.information.dcdDesc = ans.firstChild.information.dcdDesc;
				ans.information.rcdDesc = ans.firstChild.information.rcdDesc;

				//let's extract the information about this state
				NamedNodeMap attributeMap = node.getAttributes();
				for (int i = 0; i < attributeMap.getLength(); i++) {
					Node att = attributeMap.item(i);
					String attName = att.getNodeName();
					String attValue = att.getNodeValue();

					if (attName == "vol"){
						ans.information.volume = attValue;
					}
					//if (attName == "pos")
					//ans.information.pos = Integer.parseInt(attValue);
				}

				ans.information.hasReaction = checkReacion(ans);
				if (ans.information.hasReaction){
					ans.information.reactionEquation = ans.get_reaction_equation();
				}

			} else {
				//there are two children
				ans.firstChild = buildDataStructure(nodeLst.item(1));
				ans.secondChild = buildDataStructure(nodeLst.item(3));
				//ans.information.hasReaction = ans.firstChild.information.hasReaction || ans.secondChild.information.hasReaction;

				/*
				 * String nodeName = node.getNodeName(); //check if the node is C
				 */

				if (ans.firstChild.information.pos > ans.secondChild.information.pos) {
					ans.information.totalVol = ans.firstChild.information.totalVol;

					ans.information.amount_A = ans.firstChild.information.amount_A;
					ans.information.amount_B = ans.firstChild.information.amount_B;
					ans.information.amount_C = ans.firstChild.information.amount_C;
					ans.information.amount_D = ans.firstChild.information.amount_D;

					ans.information.actualAmount_A = ans.firstChild.information.actualAmount_A;
					ans.information.actualAmount_B = ans.firstChild.information.actualAmount_B;
					ans.information.actualAmount_C = ans.firstChild.information.actualAmount_C;
					ans.information.actualAmount_D = ans.firstChild.information.actualAmount_D;

					ans.information.srcAmount_A = ans.firstChild.information.srcAmount_A;
					ans.information.srcAmount_B = ans.firstChild.information.srcAmount_B;
					ans.information.srcAmount_C = ans.firstChild.information.srcAmount_C;
					ans.information.srcAmount_D = ans.firstChild.information.srcAmount_D;

					ans.information.pos = ans.firstChild.information.pos;

					ans.information.ids = ans.firstChild.information.ids;

					ans.information.scdDesc = ans.firstChild.information.scdDesc;
					ans.information.dcdDesc = ans.firstChild.information.dcdDesc;
					ans.information.rcdDesc = ans.firstChild.information.rcdDesc;
				} else {
					ans.information.totalVol = ans.secondChild.information.totalVol;

					ans.information.amount_A = ans.secondChild.information.amount_A;
					ans.information.amount_B = ans.secondChild.information.amount_B;
					ans.information.amount_C = ans.secondChild.information.amount_C;
					ans.information.amount_D = ans.secondChild.information.amount_D;

					ans.information.actualAmount_A = ans.secondChild.information.actualAmount_A;
					ans.information.actualAmount_B = ans.secondChild.information.actualAmount_B;
					ans.information.actualAmount_C = ans.secondChild.information.actualAmount_C;
					ans.information.actualAmount_D = ans.secondChild.information.actualAmount_D;

					ans.information.srcAmount_A = ans.secondChild.information.srcAmount_A;
					ans.information.srcAmount_B = ans.secondChild.information.srcAmount_B;
					ans.information.srcAmount_C = ans.secondChild.information.srcAmount_C;
					ans.information.srcAmount_D = ans.secondChild.information.srcAmount_D;

					ans.information.pos = ans.secondChild.information.pos;

					ans.information.ids = ans.secondChild.information.ids;

					ans.information.scdDesc = ans.secondChild.information.scdDesc;
					ans.information.dcdDesc = ans.secondChild.information.dcdDesc;
					ans.information.rcdDesc = ans.secondChild.information.rcdDesc;
				}



				//let's extract the information about this state
				NamedNodeMap attributeMap = node.getAttributes();
				for (int i = 0; i < attributeMap.getLength(); i++) {
					Node att = attributeMap.item(i);
					String attName = att.getNodeName();
					String attValue = att.getNodeValue();

					if (attName == "vol"){
						ans.information.volume = attValue;
					}
					//if (attName == "pos")
					//ans.information.pos = Integer.parseInt(attValue);
				}

				ans.information.hasReaction = checkReacion(ans);
				if (ans.information.hasReaction){
					ans.information.reactionEquation = ans.get_reaction_equation();
				}
			}
		} else {
			//there is no children
			ans.firstChild = null;
			ans.secondChild = null;
			//let's extract the information about this state
			NamedNodeMap attributeMap = node.getAttributes();

			double volFromSource = 0;
			double destVolA = 0;
			double destVolB = 0;
			double destVolC = 0;
			double destVolD = 0;

			for (int i = 0; i < attributeMap.getLength(); i++) {
				Node att = attributeMap.item(i);
				String attName = att.getNodeName();
				String attValue = att.getNodeValue();

				if (attName == "vol") {
					int indexOfCreate = attValue.indexOf("create");
					if (indexOfCreate == -1){
						ans.information.totalVol = Double.parseDouble(attValue);
						volFromSource = Double.parseDouble(attValue);
					} else {
						ans.information.totalVol = Double.parseDouble(attValue.substring(indexOfCreate + 7));
						volFromSource = Double.parseDouble(attValue.substring(0, attValue.indexOf(" /")));
					}
					ans.information.volume = attValue;
				}


				if (attName == "rcd") {
					ans.information.actualAmount_A = getAmount(attValue, "A");
					ans.information.actualAmount_B = getAmount(attValue, "B");
					ans.information.actualAmount_C = getAmount(attValue, "C");
					ans.information.actualAmount_D = getAmount(attValue, "D");
					ans.information.rcdDesc = attValue;
				}

				if (attName == "scd") {
					ans.information.amount_A = getAmount(attValue, "A");
					ans.information.amount_B = getAmount(attValue, "B");
					ans.information.amount_C = getAmount(attValue, "C");
					ans.information.amount_D = getAmount(attValue, "D");

					ans.information.srcAmount_A = ans.information.amount_A;
					ans.information.srcAmount_B = ans.information.amount_B;
					ans.information.srcAmount_C = ans.information.amount_C;
					ans.information.srcAmount_D = ans.information.amount_D;
					ans.information.scdDesc = attValue;
				}

				if (attName == "dcd") {
					destVolA = getAmount(attValue, "A");
					destVolB = getAmount(attValue, "B");
					destVolC = getAmount(attValue, "C");
					destVolD = getAmount(attValue, "D");
					ans.information.dcdDesc = attValue;
				}


				if (attName == "pos") {
					if (attValue.equals("")){
						ans.information.pos = 999;
					} else {
						ans.information.pos = Integer.parseInt(attValue);
					}
				}

				if (attName == "IDs") {
					ans.information.ids = attValue;
				}
			}


			double totalDestVol = destVolA + destVolB + destVolC + destVolD;
			if (totalDestVol != 0) {
				ans.information.amount_A = ans.information.amount_A * volFromSource + (destVolA) * (ans.information.totalVol - volFromSource);
				ans.information.amount_B = ans.information.amount_B * volFromSource + (destVolB) * (ans.information.totalVol - volFromSource);
				ans.information.amount_C = ans.information.amount_C * volFromSource + (destVolC) * (ans.information.totalVol - volFromSource);
				ans.information.amount_D = ans.information.amount_D * volFromSource + (destVolD) * (ans.information.totalVol - volFromSource);
			} else {
				ans.information.amount_A = ans.information.amount_A * volFromSource;
				ans.information.amount_B = ans.information.amount_B * volFromSource;
				ans.information.amount_C = ans.information.amount_C * volFromSource;
				ans.information.amount_D = ans.information.amount_D * volFromSource;
			}

			//check if there was a reaction
			ans.information.hasReaction = checkReacion(ans);
			if (ans.information.hasReaction){
				ans.information.reactionEquation = ans.get_reaction_equation();
			}
			//System.out.println(node.getNodeName());
		}

		return ans;
	}

	public static double getAmount(String strVol, String solType) {
		double ans = 0;
		int endIndex = strVol.indexOf("of " + solType) - 1;
		//check if the solType is exist in the strVol
		if (endIndex >= 0) {
			int beginIndex = endIndex;
			while (strVol.charAt(beginIndex) != ',') {
				beginIndex--;
			}
			beginIndex++;
			String amount = strVol.substring(beginIndex, endIndex);
			//System.out.println("amount of" + solType + " is: " + amount);

			//let's evaluate the amount to number
			int indexOfE = amount.indexOf("E");
			if (indexOfE == -1){
				ans = Double.parseDouble(amount.substring(0,amount.indexOf("g")));
				return ans;
			}
			double numAmount = Double.parseDouble(amount.substring(0, amount.indexOf("E")));
			int indexOfMOrG = amount.indexOf("M");
			if (indexOfMOrG == -1) {
				indexOfMOrG = amount.indexOf("g");
			}
			double numOfZeros = Double.parseDouble(amount.substring(amount.indexOf("E") + 1, indexOfMOrG));
			if (-numOfZeros <= 15) {
				ans = numAmount / (Math.pow(10, -numOfZeros));
			} else {
				ans = 0;
			}
		}
		return ans;
	}


	public static boolean checkReasonableAndReaction(State s, boolean firstReaction) {
		
		if (!s.hasFirstChild() && !s.hasSecondChild()) {
			if (s.information.hasReaction) {
				if (firstReactionOccured == false) {
					firstReactionOccured = true;
				}
				s.information.isReasonable = true;
				s.information.hasChildrenWithReaction = true;
			} else {
				if (firstReactionOccured == false) {
					s.information.isReasonable = true;
				}
				s.information.hasChildrenWithReaction = false;
			}
		} else {
			boolean firstReasonable = false;
			boolean secondReasonable = false;
			boolean firsthasChildrenWithReaction = false;
			boolean secondhasChildrenWithReaction = false;
			if (s.hasFirstChild()) {
				firstReaction = checkReasonableAndReaction(s.firstChild, firstReaction);
				firstReasonable = s.firstChild.information.isReasonable;
				firsthasChildrenWithReaction = s.firstChild.information.hasChildrenWithReaction;
			}
			if (s.hasSecondChild()) {
				firstReaction = checkReasonableAndReaction(s.secondChild, firstReaction);
				secondReasonable = s.secondChild.information.isReasonable;
				secondhasChildrenWithReaction = s.secondChild.information.hasChildrenWithReaction;
			}
			s.information.isReasonable = firstReasonable || secondReasonable;
			s.information.hasChildrenWithReaction = firsthasChildrenWithReaction || secondhasChildrenWithReaction;
		}
		paintTreeNodes(s);
		return firstReactionOccured;
	}

	public static boolean checkReacion(State s) {
		boolean ans = false;

		//check if there was a reaction
		double aVol = s.information.amount_A;
		double bVol = s.information.amount_B;
		double cVol = s.information.amount_C;
		double dVol = s.information.amount_D;
		double allVol = aVol + bVol + cVol + dVol;

		double aActVol = s.information.actualAmount_A;
		double bActVol = s.information.actualAmount_B;
		double cActVol = s.information.actualAmount_C;
		double dActVol = s.information.actualAmount_D;
		double allActVol = aActVol + bActVol + cActVol + dActVol;

		DecimalFormat df = new DecimalFormat("####0.00");

		double bans = Double.parseDouble(df.format(bVol / allVol));
		double bactans = Double.parseDouble(df.format(bActVol / allActVol));

		if (Double.parseDouble(df.format(aVol / allVol)) != Double.parseDouble(df.format(aActVol / allActVol))) {
			ans = true;
		} else {
			if (Double.parseDouble(df.format(bVol / allVol)) != Double.parseDouble(df.format(bActVol / allActVol))) {
				ans = true;
			} else {
				if (Double.parseDouble(df.format(cVol / allVol)) != Double.parseDouble(df.format(cActVol / allActVol))) {
					ans = true;
				} else {
					if (Double.parseDouble(df.format(dVol / allVol)) != Double.parseDouble(df.format(dActVol / allActVol))) {
						ans = true;
					}
				}
			}
		}

		return ans;
	}

//    public static void InOrder_Accummulate_Tree_Main_rec(Node node, String problem_type) {
//        //Iterator It = vis.items();
//        //VisualItem item = (VisualItem) It.next();
//        InOrder_Accumulate_Tree_rec(node, Form_StudentPresentation.JPanel_CurrentTab.m_number_of_nodes, problem_type);
//    }

    public static void InOrder_Accumulate_Tree_rec(Node node, int num, String problem_type) {
        String node_equation = "";

        //WHY USE  "GlobalVariables.m_number_of_nodes" AND ALSO "num" we get from function invoke?
        //******************************************************************************************

        num++;
        if (num == 168) {
            String a = "";
        }
//        Form_StudentPresentation.JPanel_CurrentTab.m_number_of_nodes++;
//        Form_StudentPresentation.JPanel_CurrentTab.m_curr_nodes.put(Form_StudentPresentation.JPanel_CurrentTab.m_number_of_nodes, node);
        ((Element) node).setAttribute("node_number", num + "");

        if (node.hasChildNodes() && node.getChildNodes().getLength() > 0) { //has two childs
            //int max_number_left, max_number_right;
            Node right_node = node.getChildNodes().item(1);
            Node left_node = node.getChildNodes().item(3);
            InOrder_Accumulate_Tree_rec(right_node, num, problem_type);
            num++;
            InOrder_Accumulate_Tree_rec(left_node, num+1000, problem_type);
//            InOrder_Accumulate_Tree_rec(left_node, Form_StudentPresentation.JPanel_CurrentTab.m_number_of_nodes, problem_type);
            if (problem_type.contains(GlobalVariables.m_material_Unknown_Acid)) { // unknown acid
                node_equation = Update_Node(node);
//                Form_StudentPresentation.JPanel_CurrentTab.m_nodes_details.put(((Element) node).getAttribute("node_number"), node_equation);
                //update_m_vis(item, num, update_node(node));
                //CHECK END POINT P.H - if there is a jump in P.H between left child to right child, Set an attribute "End Point" = "YES" in nodes MAP.
                if (is_End_Point(left_node, right_node)) {
                    ((Element) node).setAttribute(m_attribute_end_point, "YES");
//                    Form_StudentPresentation.JPanel_CurrentTab.m_nodes_details.put(((Element) node).getAttribute("node_number") + "_end_point", "1");
                } else {
                    ((Element) node).setAttribute(m_attribute_end_point, "NO");
//                    Form_StudentPresentation.JPanel_CurrentTab.m_nodes_details.put(((Element) node).getAttribute("node_number") + "_end_point", "0");
                }
            } 
//            else {
//                if (problem_type.contains("Oracle problem")) {
//                    int[] source_destination_IDs = Get_Source_And_Destination_IDs_Nums(node);
//                    int[] substances = Get_ID_Substances(source_destination_IDs[1]);
//                    //((Element) node).setAttribute(m_attribute_equation, res);
//                }
//            }
        } else if (node.hasChildNodes() && node.getChildNodes().getLength() < 2) { //has one child
            Node one_node = node.getChildNodes().item(1);
            InOrder_Accumulate_Tree_rec(one_node, num, problem_type);
            if (problem_type.contains(GlobalVariables.m_material_Unknown_Acid)) {
                node_equation = Update_Node(node);
//                Form_StudentPresentation.JPanel_CurrentTab.m_nodes_details.put(((Element) node).getAttribute("node_number"), node_equation);
//                Form_StudentPresentation.JPanel_CurrentTab.m_nodes_details.put(((Element) node).getAttribute("node_number") + "_end_point", "0");
            } 
//            else {
//                if (problem_type.contains("Oracle problem")) {
//                    int iii = 0;
//                }
//            }
            //update_m_vis(item, num, update_node(node));
        } else if (!node.hasChildNodes()) { //has no child
            if (problem_type.contains(GlobalVariables.m_material_Unknown_Acid)) {
                node_equation = Update_Leaf(node);
//                Form_StudentPresentation.JPanel_CurrentTab.m_nodes_details.put(((Element) node).getAttribute("node_number"), node_equation);
//                Form_StudentPresentation.JPanel_CurrentTab.m_nodes_details.put(((Element) node).getAttribute("node_number") + "_end_point", "0");
                //CHECK END POINT P.H - if there is a jump in P.H between left child to right child, Set an attribute "End Point" = "YES" in nodes MAP.
                if (is_End_Point(node)) {
                    ((Element) node).setAttribute(m_attribute_end_point, "YES");
//                    Form_StudentPresentation.JPanel_CurrentTab.m_nodes_details.put(((Element) node).getAttribute("node_number") + "_end_point", "1");
                } else {
                    ((Element) node).setAttribute(m_attribute_end_point, "NO");
//                    Form_StudentPresentation.JPanel_CurrentTab.m_nodes_details.put(((Element) node).getAttribute("node_number") + "_end_point", "0");
                }
            } 
//            else {
//                if (problem_type.contains("Oracle problem")) {
//                    int[] source_destination_IDs = Get_Source_And_Destination_IDs_Nums(node);
//                    int[] scdCont = Get_Pouring_Substances(node, "scd");
//                    int[] dcdCont = Get_Pouring_Substances(node, "dcd");
//
//                    int[] srcSubstances = Get_ID_Substances(source_destination_IDs[0]);
//                    int[] destSubstances = Get_ID_Substances(source_destination_IDs[1]);
//
//                    //check if it is the first time to see the flasks.
//                    if (srcSubstances[0] == -1) {
//                        Update_IDs_Substances(source_destination_IDs[0], scdCont);
//                    }
//                    if (destSubstances[0] == -1) {
//                        Update_IDs_Substances(source_destination_IDs[1], dcdCont);
//                    }
//
//                    srcSubstances = Get_ID_Substances(source_destination_IDs[0]);
//                    destSubstances = Get_ID_Substances(source_destination_IDs[1]);
//
//                    int[] resCont = new int[4];
//                    if (srcSubstances[0] == 1 || destSubstances[0] == 1) {
//                        resCont[0] = 1;
//                    } else {
//                        resCont[0] = 0;
//                    }
//                    if (srcSubstances[1] == 1 || destSubstances[1] == 1) {
//                        resCont[1] = 1;
//                    } else {
//                        resCont[1] = 0;
//                    }
//                    if (srcSubstances[2] == 1 || destSubstances[2] == 1) {
//                        resCont[2] = 1;
//                    } else {
//                        resCont[2] = 0;
//                    }
//                    if (srcSubstances[3] == 1 || destSubstances[3] == 1) {
//                        resCont[3] = 1;
//                    } else {
//                        resCont[3] = 0;
//                    }
//                    Update_IDs_Substances(source_destination_IDs[1], resCont);
//                }
//            }
            //update_m_vis(item, num, update_leaf(node));           
        }
    }

    public static boolean is_End_Point(Node left_node, Node right_node) {
        String left_node_rcd = Get_Node_Vol_String(left_node, "rcd");
        String right_node_rcd = Get_Node_Vol_String(right_node, "rcd");
        double left_node_PH = Get_PH_From_rcd(left_node_rcd, "H+");
        double right_node_PH = Get_PH_From_rcd(right_node_rcd, "H+");
        if (left_node_PH > GlobalVariables.m_end_point && right_node_PH < GlobalVariables.m_end_point) {
            {
                return true;
            }
        }
        return false;
    }

    public static boolean is_End_Point(Node current_node) {
        String node_rcd = Get_Node_Vol_String(current_node, "rcd");
        String node_dcd = Get_Node_Vol_String(current_node, "dcd");
        String vol = Get_Node_Vol_String(current_node, "vol");
        double[] vols = Get_Node_Vol_Values(vol);
        if (node_dcd.equalsIgnoreCase("empty") || vols[2] == 0) {
            return false;
        }
        double node_dcd_PH = Get_PH_From_rcd(node_dcd, "H+");
        double node_rcd_PH = Get_PH_From_rcd(node_rcd, "H+");
        if (node_rcd_PH > GlobalVariables.m_end_point && node_dcd_PH < GlobalVariables.m_end_point) {
            {
                return true;
            }
        }
        return false;
    }

    public static double Get_PH_From_rcd(String str, String comp) {
        double retAns = 0;

        int lastIndexOfH = str.indexOf("of " + comp);
        if (lastIndexOfH >= 0) {
            int indexOfH = lastIndexOfH;
            while (str.charAt(indexOfH) != ',' && str.charAt(indexOfH) != '"') {
                indexOfH--;
            }
            String hComp = str.substring(indexOfH + 2, lastIndexOfH - 1);

            //compute the amount
            double numAmount = Double.parseDouble(hComp.substring(0, hComp.indexOf("E")));
            double numOfZeros = Double.parseDouble(hComp.substring(hComp.indexOf("E") + 1, hComp.indexOf("M")));
            if (-numOfZeros <= 15) {
                retAns = numAmount / (Math.pow(10, -numOfZeros));
            }
            if (comp.equalsIgnoreCase("H+")) {
                retAns = -Math.log10(retAns);
                DecimalFormat df = new DecimalFormat("####0.###");
                retAns = Double.parseDouble(df.format(retAns));
            }
        }
        return retAns;

    }

//    public static void Update_m_vis(VisualItem item, int num, String sComp) {
//        item.set("newName", "{" + num + "} " + sComp);
//        item.set("nameForNodes", sComp);
//    }

    public static String Update_Node(Node node) {
        int[] source_destination_IDs = Get_Source_And_Destination_IDs_Nums(node);
        String res = Get_ID_Content_String(source_destination_IDs[1]);
        ((Element) node).setAttribute(m_attribute_equation, res);
        return res;
    }

    public static String Update_Leaf(Node node) {
        int[] source_destination_IDs = Get_Source_And_Destination_IDs_Nums(node);
        String[] type_and_vols = Get_Pouring_Material_Types_And_Vols(node);
        update_IDs(source_destination_IDs, type_and_vols);
        String res = Get_ID_Content_String(source_destination_IDs[1]); //Get Destination ID content in string
        ((Element) node).setAttribute(m_attribute_equation, res);
        return res;
    }

    public static String Get_ID_Content_String(int id) {
        String res = "";
        Iterator it = IDsStatus.m_IDs[id].m_materials.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            String material_type = pairs.getKey().toString();
            Object material_vol = pairs.getValue();

            double vol_double = (Double) Double.valueOf(new DecimalFormat("#.###").format(material_vol));
            int vol_int = (int) vol_double;
            if (vol_double == 0) {
                continue;
            }

            String vol_string = (vol_double - Math.floor(vol_double) == 0) ? vol_int + "" : vol_double + "";
            if (material_type.trim().compareTo("") != 0) {
                res += vol_string + "ml of " + material_type + " , ";
            }
        }
        try {
            res = res.substring(0, res.length() - 3);
        } catch (Exception e) {
            res = res.toString();
        }
        return res;
    }

    public static void update_IDs(int[] source_destination_IDs, String[] types_and_vols) {
        String source_materials_type = types_and_vols[0];
        String destination_materials_type = types_and_vols[4];
        double source_id_vol = Double.parseDouble(types_and_vols[1]);
        double dest_id_vol = Double.parseDouble(types_and_vols[2]);
        double dest_before_vol = Double.parseDouble(types_and_vols[5]);
        double passed_vol = Double.parseDouble(types_and_vols[3]);

        String[] source_material_type = source_materials_type.split("::");
        String[] destination_material_type = destination_materials_type.split("::");

        int num_of_materials_from_scd = 0;
        int total_source_material_vol = 0;
        int num_of_materials_source_m_IDs = IDsStatus.m_IDs[source_destination_IDs[0]].m_materials.size();

        //When the m_IDs source data structure is not empty - usually at the middle of work 
        if (num_of_materials_source_m_IDs > 0)//Has materials
        {
            passed_vol = passed_vol / num_of_materials_source_m_IDs;
            for (Map.Entry<String, Double> entry : IDsStatus.m_IDs[source_destination_IDs[0]].m_materials.entrySet()) {
                Update_ID(source_destination_IDs[0], source_id_vol, entry.getKey(), false);
                Update_ID(source_destination_IDs[1], passed_vol, entry.getKey(), true);
            }
        } else { //When the m_IDs source data structure is empty - need to update m_IDs source flask from scd data.
            if (num_of_materials_from_scd == 2) {
                passed_vol = passed_vol / 2;
            }
            for (int i = 0; i < source_material_type.length; i++) {
                if (source_material_type[i].compareTo("") != 0) {
                    Update_ID(source_destination_IDs[0], source_id_vol, source_material_type[i], false);
                    Update_ID(source_destination_IDs[1], passed_vol, source_material_type[i], true);
                }
            }
        }

        //Updates destination ID at the first leaf of solution
        if (GlobalVariables.num_of_leafs_analyzed == 0) {
            for (int i = 0; i < destination_material_type.length; i++) {
                if (destination_material_type[i].compareTo("") != 0) {
                    if (!IDsStatus.m_IDs[source_destination_IDs[1]].m_materials.containsKey(destination_material_type[i])) {
                        Update_ID(source_destination_IDs[1], dest_before_vol, destination_material_type[i], false);
                    }
                }
            }
        }
        GlobalVariables.num_of_leafs_analyzed++;
    }

    public static void Update_ID(int ID_num, double ID_vol, String material_type, boolean destination) {
        double res;
        int dup = (destination) ? 1 : 0;
        if (!IDsStatus.m_IDs[ID_num].m_materials.containsKey(material_type)) {
        	IDsStatus.m_IDs[ID_num].m_materials.put(material_type, ID_vol);
        } else {
            res = ID_vol + dup * IDsStatus.m_IDs[ID_num].m_materials.get(material_type); //what_pass + what the ID has already
            IDsStatus.m_IDs[ID_num].m_materials.put(material_type, res);
        }
    }

    public static String Get_Decimal_Number(String number_expression) {
        Pattern r = Pattern.compile("(\\d{1,3})(\\.|)(\\d{1,3}|)E(\\-|)(\\d{1,3})");
        Matcher m = r.matcher(number_expression);
        String res = "";
        if (m.find()) {
            int e_num = Integer.parseInt(m.group(5));
            for (int i = 0; i < e_num; i++) {
                if (i == 0) {
                    res += "0.";
                } else {
                    res += "0";
                }
            }
            res += m.group(1);
        }
        return res;
    }

    public static String[] Get_Pouring_Material_Types_And_Vols(Node node) {
        String[] res = new String[6];
        String scd = Get_Node_Vol_String(node, "scd");
        String dcd = Get_Node_Vol_String(node, "dcd");
        String vol = Get_Node_Vol_String(node, "vol");

        res[0] = Get_Material_Types_From_ID(scd);
        res[4] = Get_Material_Types_From_ID(dcd);


        double[] vols = Get_Node_Vol_Values(vol);

        //vols[0] = amount that was removed from Source ID 
        //vols[1] = Source ID Full amound
        //vols[2] = Destination ID original amound
        //vols[3] = Destination ID amound after adding the amount from source ID

        res[1] = vols[1] - vols[0] + ""; //Source vol after passing metrial - ASK ORIEL
        res[2] = vols[3] + ""; //Destination vol after passing metrial
        res[3] = vols[3] - vols[2] + ""; //How match material we passed to the destination ID
        res[5] = vols[2] + "";
        return res;
    }

    public static String Get_Material_Types_From_ID(String toAnalysis) {
        //FIX REGEX - regex avoid point sign "."
        String pattern = "((\\d{1,3}(\\.|)(\\d{1,3}|)E(\\-|)\\d{1,3}M of )(Na|Unknown))"; //returns Molar ratio and Acid Type
        // Create a Pattern object
        Pattern r = Pattern.compile(pattern); //complies the pattern
        // Now create matcher object.
        Matcher m = r.matcher(toAnalysis);

        List<String> allMatches = new ArrayList<String>();

        while (m.find()) {
            if (m.group(6).compareTo("Na") == 0) {
                String material_ratio = m.group(2);
                allMatches.add(Get_Decimal_Number(material_ratio) + "M" + " NaOH");
            } else {
                allMatches.add(m.group(1));
            }
            //res[0] += m.group(1) + " "; // group(1) = all the pattern
        }
        String res = "";
        boolean has_unknown = false;
        for (int i = 0; i < allMatches.size(); i++) {
            if (!has_unknown && allMatches.get(i).contains("Unknown")) {
                has_unknown = true;
                res += "Unknown acid::";
            } else if (allMatches.get(i).contains("Na")) {
                res += allMatches.get(i) + "::";
            }

        }
        //if (res.contains("Unknown")) {
        // res.replaceAll("((\\d{1,3}(\\.|)(\\d{1,3}|)E(\\-|)\\d{1,3}M of )(Unknown))",GlobalVariables.m_material_Unknown_Acid_Display);
        //res = GlobalVariables.m_material_Unknown_Acid_Display; //Source material type
        //}
        return res;
    }

    public static void Update_IDs_States(Node node) {
        double[] vols = Get_Node_Vol_Values(Get_Node_Vol_String(node, "vol"));
        int[] source_destination_IDs = Get_Source_And_Destination_IDs_Nums(node);
        //GlobalVariables.IDs_states[ids[0]].m_NaoH_vol = GlobalVariables.IDs_states[0].m_NaoH_vol - values[0]; // values[0] = amound that was removed from Source ID 
        double source_id_value_2 = Math.abs(vols[0] - vols[1]);
        //GlobalVariables.IDs_states[ids[1]].m_NaoH_vol = GlobalVariables.IDs_states[ids[1]].m_NaoH_vol + values[0];
        //JOptionPane.showMessageDialog(null, GlobalVariables.IDs_states[ids[1]].m_NaoH_vol);
        double destination_id_value_2 = vols[3];
        //((Element) node).setAttribute("NaoH_Vol", GlobalVariables.IDs_states[ids[1]].m_NaoH_vol+"");
        ((Element) node).setAttribute("NaoH_Vol", destination_id_value_2 + "");
    }

    public static String[] Get_Vols(Node node, String node_data) {
        String[] Vols = new String[2];
        String[] parsing_data = new String[2];
        parsing_data = node_data.split(",");
        Vols[0] = (parsing_data[0].split(":"))[1] + Get_Node_Vol(node, "Unknown_Acid_Vol");
        Vols[1] = (parsing_data[1].split(":"))[1] + Get_Node_Vol(node, "NaoH_Vol");
        return Vols;
    }

    public static int Get_Node_Vol(Node node, String attributeName) {
        NamedNodeMap att_leaf = node.getAttributes();
        Node c_node = att_leaf.getNamedItem(attributeName);
        return Integer.parseInt(c_node.getNodeValue());
    }

    public static String Get_Node_Vol_String(Node node, String attributeName) {
        NamedNodeMap att_leaf = node.getAttributes();
        Node c_node = att_leaf.getNamedItem(attributeName);
        return c_node.getNodeValue();
    }

    public static double[] Get_Node_Vol_Values(String node_vol) {
        double[] values = new double[4];
        String[] split_by_plus = node_vol.split("\\+");
        String[] split_by_slash = split_by_plus[0].split("/");
        String[] split_by_space = split_by_plus[1].split(" ");

        values[0] = Double.parseDouble(split_by_slash[0].trim()); // amount that was removed from Source ID 
        values[1] = Double.parseDouble(split_by_slash[1].trim()); // Source ID Full amound
        values[2] = Double.parseDouble(split_by_space[1].trim()); // Destination ID original amound
        values[3] = Double.parseDouble(split_by_space[3].trim()); // Destination ID amound after adding the amount from source ID
        return values;
    }

    public static String[] Sum_Vols(String[] Vols_right, String[] Vols_left) {
        String[] sums_Vols = new String[2];
        for (int i = 0; i < sums_Vols.length; i++) {
            //double sum = Double.parseDouble(Vols_left[i]) + Double.parseDouble(Vols_right[i]);
            int sum = Integer.parseInt(Vols_left[i]) + Integer.parseInt(Vols_left[i]);
            sums_Vols[i] = sum + "";
        }
        return sums_Vols;
    }

    public static int[] Get_Source_And_Destination_IDs_Nums(Node node) {
        int[] source_destination_IDs = new int[2];
        String ids = Get_Node_Vol_String(node, "IDs");
        String[] split_by_to = ids.split("to");
        String[] split_by_space = split_by_to[0].split(" ");
        source_destination_IDs[0] = Integer.parseInt(split_by_space[1].replace("ID", "").trim());
        source_destination_IDs[1] = Integer.parseInt(split_by_to[1].replace("ID", "").trim()); // 
        return source_destination_IDs;
    }

 
 
    public static Document Get_XML_Document_From_Disc(String xml_file_path) {
        File file;
        Document xml_doc = null;
        // TODO code application logic here 
        try {
            file = new File(xml_file_path);
            if (file.exists()) {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                xml_doc = db.parse(file);
            } else {
                System.out.println("Error: XML File not found!");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return xml_doc;
    }

    public static Document Get_XML_Document_From_Jar(String xml_file_path) {
        Document xml_doc = null;

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream is = classLoader.getResourceAsStream(xml_file_path);
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            xml_doc = db.parse(is);
            xml_doc.getDocumentElement().normalize();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return xml_doc;
    }

    public static String Get_XML_DOC_Node_TextContent(Document doc, String node_name) {
        return doc.getElementsByTagName(node_name).item(0).getTextContent().trim();
    }

    //for oracle problem
//    public static String Update_Node_Oracle(Node node) {
//        int[] source_destination_IDs = Get_Source_And_Destination_IDs_Nums(node);
//        String res = Get_ID_Content_String(source_destination_IDs[1]);
//        ((Element) node).setAttribute(m_attribute_equation, res);
//        return res;
//    }
//
//    public static String Update_Leaf_Oracle(Node node) {
//        int[] source_destination_IDs = Get_Source_And_Destination_IDs_Nums(node);
//        String[] type_and_vols = Get_Pouring_Material_Types_And_Vols(node);
//        update_IDs(source_destination_IDs, type_and_vols);
//        String res = Get_ID_Content_String(source_destination_IDs[1]); //Get Destination ID content in string
//        ((Element) node).setAttribute(m_attribute_equation, res);
//        return res;
//    }

    //values in array: (-1): not seen yet. 0 - not poured. 1 - poured.
//    public static int[] Get_ID_Substances(int id) {
//        int[] retVal = new int[4]; //for A, B, C and D.
//        retVal[0] = -1;
//        retVal[1] = -1;
//        retVal[2] = -1;
//        retVal[3] = -1;
//        Iterator it = Form_StudentPresentation.JPanel_CurrentTab.m_IDs[id].m_materials.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry pairs = (Map.Entry) it.next();
//            String material_type = pairs.getKey().toString();
//            Object material_exist = pairs.getValue();
//
//            double tmpExist = (Double) Double.valueOf(new DecimalFormat("#.###").format(material_exist));
//            int exist = (int) tmpExist;
//
//            if (material_type.trim().compareTo("A") == 0) {
//                retVal[0] = exist;
//            }
//            if (material_type.trim().compareTo("B") == 0) {
//                retVal[1] = exist;
//            }
//            if (material_type.trim().compareTo("C") == 0) {
//                retVal[2] = exist;
//            }
//            if (material_type.trim().compareTo("D") == 0) {
//                retVal[3] = exist;
//            }
//        }
//        return retVal;
//    }
//
//    public static void Update_IDs_Substances(int ID_num, int[] substances) {
//        Form_StudentPresentation.JPanel_CurrentTab.m_IDs[ID_num].m_materials.put("A", (double) substances[0]);
//        Form_StudentPresentation.JPanel_CurrentTab.m_IDs[ID_num].m_materials.put("B", (double) substances[1]);
//        Form_StudentPresentation.JPanel_CurrentTab.m_IDs[ID_num].m_materials.put("C", (double) substances[2]);
//        Form_StudentPresentation.JPanel_CurrentTab.m_IDs[ID_num].m_materials.put("D", (double) substances[3]);
//    }
//
//    public static int[] Get_Pouring_Substances(Node node, String fromFlask) { //fromFlask is "scd" or "dcd"
//        int[] retVal = new int[4];
//        String contents = Get_Node_Vol_String(node, fromFlask);
//        if (getAmount(contents, "A") == 0) {
//            retVal[0] = 0;
//        } else {
//            retVal[0] = 1;
//        }
//        if (getAmount(contents, "B") == 0) {
//            retVal[1] = 0;
//        } else {
//            retVal[1] = 1;
//        }
//        if (getAmount(contents, "C") == 0) {
//            retVal[2] = 0;
//        } else {
//            retVal[2] = 1;
//        }
//        if (getAmount(contents, "D") == 0) {
//            retVal[3] = 0;
//        } else {
//            retVal[3] = 1;
//        }
//
//        return retVal;
//    } 
}

	


