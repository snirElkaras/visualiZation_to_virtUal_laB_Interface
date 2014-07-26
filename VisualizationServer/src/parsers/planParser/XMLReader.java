package parsers.planParser;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



/**
 * 
 * @author Aviel and Chen
 * This class is responsible for reading the XML and analyze the root node
 */
public class XMLReader {

	public static String m_attribute_equation = "equation";
	public static String m_attribute_end_point = "end_point";
	private static boolean firstReactionOccured = false;
    public static Map<String, String> m_nodes_details = new HashMap<String, String>(); //<node_num,node_equation>


	public static CheckReasonableResult checkReasonable(Node root) {
		try {

			XMLTranslate.TranslateTree(root, GlobalVariables.oracle_name);
			NodeList nodeLst = root.getChildNodes();

			ArrayList<State> listOfStates = new ArrayList<State>();
			boolean firstReaction = false;
			for (int i = 0; i < nodeLst.getLength(); i++) {
				if (i % 2 != 0) {
					Node node = nodeLst.item(i);

					State s = buildDataStructure(node);
					s.information.setHigherCompAct(true);

					//let's check which of the states are reasonable
					firstReaction = checkReasonableAndReaction(s, firstReaction);
					s.information.setDirectChildOfRoot(true);
					
					if (s.information.isHasReaction() == true) {
						if (firstReaction == false) {
							firstReaction = true;
						}
						s.information.setReasonable(true);
					} else {
						if (firstReaction == false) {
							s.information.setReasonable(true);
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


	// paint the tree according to the reaction and the node's place
	// if didn't paint it then the node will be black
	private static void paintTreeNodes(State s) {
		if (s.information.isHasReaction() == true){
			s.information.setColor("Green");
		} else
			if (s.information.isHasChildrenWithReaction()){
				s.information.setColor("Orange");
			} else
				if (firstReactionOccured) {
					s.information.setColor("Red");
				}
	}


	// from a given node, build the State data structure
	public static State buildDataStructure(Node node) {
		State ans = new State();
		NodeList nodeLst = node.getChildNodes();

		//check if there are some children
		if (nodeLst.getLength() > 1) {
			//there are children
			if (nodeLst.getLength() <= 3) {
				//there is one child
				ans.firstChild = buildDataStructure(nodeLst.item(1));

				setFirstChildInformation(ans);

				setVolume(node, ans);

				setReactionEquation(ans);

			} else {
				//there are two children
				ans.firstChild = buildDataStructure(nodeLst.item(1));
				ans.secondChild = buildDataStructure(nodeLst.item(3));

				if (ans.firstChild.information.getPos() > ans.secondChild.information.getPos()) {
					setFirstChildInformation(ans);
				} else {
					setSecondChildInformation(ans);
				}



				setVolume(node, ans);

				setReactionEquation(ans);
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
						ans.information.setTotalVol(Double.parseDouble(attValue));
						volFromSource = Double.parseDouble(attValue);
					} else {
						ans.information.setTotalVol(Double.parseDouble(attValue.substring(indexOfCreate + 7)));
						volFromSource = Double.parseDouble(attValue.substring(0, attValue.indexOf(" /")));
					}
					ans.information.setVolume(attValue);
				}


				if (attName == "rcd") {
					ans.information.setActualAmount_A(getAmount(attValue, "A"));
					ans.information.setActualAmount_B(getAmount(attValue, "B"));
					ans.information.setActualAmount_C(getAmount(attValue, "C"));
					ans.information.setActualAmount_D(getAmount(attValue, "D"));
					ans.information.setRcdDesc(attValue);
				}

				if (attName == "scd") {
					ans.information.setAmount_A(getAmount(attValue, "A"));
					ans.information.setAmount_B(getAmount(attValue, "B"));
					ans.information.setAmount_C(getAmount(attValue, "C"));
					ans.information.setAmount_D(getAmount(attValue, "D"));

					ans.information.setSrcAmount_A(ans.information.getAmount_A());
					ans.information.setSrcAmount_B(ans.information.getAmount_B());
					ans.information.setSrcAmount_C(ans.information.getAmount_C());
					ans.information.setSrcAmount_D(ans.information.getAmount_D());
					ans.information.setScdDesc(attValue);
				}

				if (attName == "dcd") {
					destVolA = getAmount(attValue, "A");
					destVolB = getAmount(attValue, "B");
					destVolC = getAmount(attValue, "C");
					destVolD = getAmount(attValue, "D");
					ans.information.setDcdDesc(attValue);
				}


				if (attName == "pos") {
					if (attValue.equals("")){
						ans.information.setPos(999);
					} else {
						ans.information.setPos(Integer.parseInt(attValue));
					}
				}

				if (attName == "IDs") {
					ans.information.setIds(attValue);
				}
			}


			double totalDestVol = destVolA + destVolB + destVolC + destVolD;
			if (totalDestVol != 0) {
				ans.information.setAmount_A(ans.information.getAmount_A() * volFromSource + (destVolA) * (ans.information.getTotalVol() - volFromSource));
				ans.information.setAmount_B(ans.information.getAmount_B() * volFromSource + (destVolB) * (ans.information.getTotalVol() - volFromSource));
				ans.information.setAmount_C(ans.information.getAmount_C() * volFromSource + (destVolC) * (ans.information.getTotalVol() - volFromSource));
				ans.information.setAmount_D(ans.information.getAmount_D() * volFromSource + (destVolD) * (ans.information.getTotalVol() - volFromSource));
			} else {
				ans.information.setAmount_A(ans.information.getAmount_A() * volFromSource);
				ans.information.setAmount_B(ans.information.getAmount_B() * volFromSource);
				ans.information.setAmount_C(ans.information.getAmount_C() * volFromSource);
				ans.information.setAmount_D(ans.information.getAmount_D() * volFromSource);
			}

			setReactionEquation(ans);
		}

		return ans;
	}


	private static void setReactionEquation(State ans) {
		ans.information.setHasReaction(checkReacion(ans));
		if (ans.information.isHasReaction()){
			ans.information.setReactionEquation(ans.get_reaction_equation());
		}
	}


	private static void setVolume(Node node, State ans) {
		NamedNodeMap attributeMap = node.getAttributes();
		for (int i = 0; i < attributeMap.getLength(); i++) {
			Node att = attributeMap.item(i);
			String attName = att.getNodeName();
			String attValue = att.getNodeValue();

			if (attName == "vol"){
				ans.information.setVolume(attValue);
			}
		}
	}


	private static void setSecondChildInformation(State ans) {
		ans.information.setTotalVol(ans.secondChild.information.getTotalVol());

		ans.information.setAmount_A(ans.secondChild.information.getAmount_A());
		ans.information.setAmount_B(ans.secondChild.information.getAmount_B());
		ans.information.setAmount_C(ans.secondChild.information.getAmount_C());
		ans.information.setAmount_D(ans.secondChild.information.getAmount_D());

		ans.information.setActualAmount_A(ans.secondChild.information.getActualAmount_A());
		ans.information.setActualAmount_B(ans.secondChild.information.getActualAmount_B());
		ans.information.setActualAmount_C(ans.secondChild.information.getActualAmount_C());
		ans.information.setActualAmount_D(ans.secondChild.information.getActualAmount_D());

		ans.information.setSrcAmount_A(ans.secondChild.information.getSrcAmount_A());
		ans.information.setSrcAmount_B(ans.secondChild.information.getSrcAmount_B());
		ans.information.setSrcAmount_C(ans.secondChild.information.getSrcAmount_C());
		ans.information.setSrcAmount_D(ans.secondChild.information.getSrcAmount_D());

		ans.information.setPos(ans.secondChild.information.getPos());

		ans.information.setIds(ans.secondChild.information.getIds());

		ans.information.setScdDesc(ans.secondChild.information.getScdDesc());
		ans.information.setDcdDesc(ans.secondChild.information.getDcdDesc());
		ans.information.setRcdDesc(ans.secondChild.information.getRcdDesc());
	}


	private static void setFirstChildInformation(State ans) {
		ans.information.setTotalVol(ans.firstChild.information.getTotalVol());

		ans.information.setAmount_A(ans.firstChild.information.getAmount_A());
		ans.information.setAmount_B(ans.firstChild.information.getAmount_B());
		ans.information.setAmount_C(ans.firstChild.information.getAmount_C());
		ans.information.setAmount_D(ans.firstChild.information.getAmount_D());

		ans.information.setActualAmount_A(ans.firstChild.information.getActualAmount_A());
		ans.information.setActualAmount_B(ans.firstChild.information.getActualAmount_B());
		ans.information.setActualAmount_C(ans.firstChild.information.getActualAmount_C());
		ans.information.setActualAmount_D(ans.firstChild.information.getActualAmount_D());

		ans.information.setSrcAmount_A(ans.firstChild.information.getSrcAmount_A());
		ans.information.setSrcAmount_B(ans.firstChild.information.getSrcAmount_B());
		ans.information.setSrcAmount_C(ans.firstChild.information.getSrcAmount_C());
		ans.information.setSrcAmount_D(ans.firstChild.information.getSrcAmount_D());

		ans.information.setPos(ans.firstChild.information.getPos());

		ans.information.setIds(ans.firstChild.information.getIds());

		ans.information.setScdDesc(ans.firstChild.information.getScdDesc());
		ans.information.setDcdDesc(ans.firstChild.information.getDcdDesc());
		ans.information.setRcdDesc(ans.firstChild.information.getRcdDesc());
	}

	private static double getAmount(String strVol, String solType) {
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


	private static boolean checkReasonableAndReaction(State s, boolean firstReaction) {
		
		if (!s.hasFirstChild() && !s.hasSecondChild()) {
			if (s.information.isHasReaction()) {
				if (firstReactionOccured == false) {
					firstReactionOccured = true;
				}
				s.information.setReasonable(true);
				s.information.setHasChildrenWithReaction(true);
			} else {
				if (firstReactionOccured == false) {
					s.information.setReasonable(true);
				}
				s.information.setHasChildrenWithReaction(false);
			}
		} else {
			boolean firstReasonable = false;
			boolean secondReasonable = false;
			boolean firsthasChildrenWithReaction = false;
			boolean secondhasChildrenWithReaction = false;
			if (s.hasFirstChild()) {
				firstReaction = checkReasonableAndReaction(s.firstChild, firstReaction);
				firstReasonable = s.firstChild.information.isReasonable();
				firsthasChildrenWithReaction = s.firstChild.information.isHasChildrenWithReaction();
			}
			if (s.hasSecondChild()) {
				firstReaction = checkReasonableAndReaction(s.secondChild, firstReaction);
				secondReasonable = s.secondChild.information.isReasonable();
				secondhasChildrenWithReaction = s.secondChild.information.isHasChildrenWithReaction();
			}
			s.information.setReasonable(firstReasonable || secondReasonable);
			s.information.setHasChildrenWithReaction(firsthasChildrenWithReaction || secondhasChildrenWithReaction);
		}
		paintTreeNodes(s);
		return firstReactionOccured;
	}

	private static boolean checkReacion(State s) {
		boolean ans = false;

		//check if there was a reaction
		double aVol = s.information.getAmount_A();
		double bVol = s.information.getAmount_B();
		double cVol = s.information.getAmount_C();
		double dVol = s.information.getAmount_D();
		double allVol = aVol + bVol + cVol + dVol;

		double aActVol = s.information.getActualAmount_A();
		double bActVol = s.information.getActualAmount_B();
		double cActVol = s.information.getActualAmount_C();
		double dActVol = s.information.getActualAmount_D();
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

    public static void InOrder_Accumulate_Tree_rec(Node node, int num, String problem_type) {
        String node_equation = "";

        ((Element) node).setAttribute("node_number", num + "");

        if (node.hasChildNodes() && node.getChildNodes().getLength() > 0) { //has two childs
            Node right_node = node.getChildNodes().item(1);
            Node left_node = node.getChildNodes().item(3);
            InOrder_Accumulate_Tree_rec(right_node, num, problem_type);
            num++;
            InOrder_Accumulate_Tree_rec(left_node, num + 100, problem_type);
            if (problem_type.contains(GlobalVariables.m_material_Unknown_Acid)) { // unknown acid
                node_equation = Update_Node(node);
                m_nodes_details.put(((Element) node).getAttribute("node_number"), node_equation);
                if (is_End_Point(left_node, right_node)) {
                    ((Element) node).setAttribute(m_attribute_end_point, "YES");
                    m_nodes_details.put(((Element) node).getAttribute("node_number") + "_end_point", "1");
                } else {
                    ((Element) node).setAttribute(m_attribute_end_point, "NO");
                    m_nodes_details.put(((Element) node).getAttribute("node_number") + "_end_point", "0");
                }
            } 
        } else if (node.hasChildNodes() && node.getChildNodes().getLength() < 2) { //has one child
            Node one_node = node.getChildNodes().item(1);
            InOrder_Accumulate_Tree_rec(one_node, num, problem_type);
            if (problem_type.contains(GlobalVariables.m_material_Unknown_Acid)) {
                node_equation = Update_Node(node);
                m_nodes_details.put(((Element) node).getAttribute("node_number"), node_equation);
                m_nodes_details.put(((Element) node).getAttribute("node_number") + "_end_point", "0");
            } 
        } else if (!node.hasChildNodes()) { //has no child
            if (problem_type.contains(GlobalVariables.m_material_Unknown_Acid)) {
                node_equation = Update_Leaf(node);
                m_nodes_details.put(((Element) node).getAttribute("node_number"), node_equation);
                m_nodes_details.put(((Element) node).getAttribute("node_number") + "_end_point", "0");
                if (is_End_Point(node)) {
                    ((Element) node).setAttribute(m_attribute_end_point, "YES");
                    m_nodes_details.put(((Element) node).getAttribute("node_number") + "_end_point", "1");
                } else {
                    ((Element) node).setAttribute(m_attribute_end_point, "NO");
                    m_nodes_details.put(((Element) node).getAttribute("node_number") + "_end_point", "0");
                }
            } 
        }
    }

    private static boolean is_End_Point(Node left_node, Node right_node) {
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

    private static boolean is_End_Point(Node current_node) {
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

    private static String Update_Node(Node node) {
        int[] source_destination_IDs = Get_Source_And_Destination_IDs_Nums(node);
        String res = Get_ID_Content_String(source_destination_IDs[1]);
        ((Element) node).setAttribute(m_attribute_equation, res);
        return res;
    }

    private static String Update_Leaf(Node node) {
        int[] source_destination_IDs = Get_Source_And_Destination_IDs_Nums(node);
        String[] type_and_vols = Get_Pouring_Material_Types_And_Vols(node);
        update_IDs(source_destination_IDs, type_and_vols);
        String res = Get_ID_Content_String(source_destination_IDs[1]); //Get Destination ID content in string
        ((Element) node).setAttribute(m_attribute_equation, res);
        return res;
    }

    private static String Get_ID_Content_String(int id) {
        String res = "";
        Iterator it = IDsStatus.m_IDs[id].getM_materials().entrySet().iterator();
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

    private static void update_IDs(int[] source_destination_IDs, String[] types_and_vols) {
        String source_materials_type = types_and_vols[0];
        String destination_materials_type = types_and_vols[4];
        double source_id_vol = Double.parseDouble(types_and_vols[1]);
        double dest_before_vol = Double.parseDouble(types_and_vols[5]);
        double passed_vol = Double.parseDouble(types_and_vols[3]);

        String[] source_material_type = source_materials_type.split("::");
        String[] destination_material_type = destination_materials_type.split("::");

        int num_of_materials_from_scd = 0;
        int num_of_materials_source_m_IDs = IDsStatus.m_IDs[source_destination_IDs[0]].getM_materials().size();

        //When the m_IDs source data structure is not empty - usually at the middle of work 
        if (num_of_materials_source_m_IDs > 0)//Has materials
        {
            passed_vol = passed_vol / num_of_materials_source_m_IDs;
            for (Map.Entry<String, Double> entry : IDsStatus.m_IDs[source_destination_IDs[0]].getM_materials().entrySet()) {
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
                    if (!IDsStatus.m_IDs[source_destination_IDs[1]].getM_materials().containsKey(destination_material_type[i])) {
                        Update_ID(source_destination_IDs[1], dest_before_vol, destination_material_type[i], false);
                    }
                }
            }
        }
        GlobalVariables.num_of_leafs_analyzed++;
    }

    private static void Update_ID(int ID_num, double ID_vol, String material_type, boolean destination) {
        double res;
        int dup = (destination) ? 1 : 0;
        if (!IDsStatus.m_IDs[ID_num].getM_materials().containsKey(material_type)) {
        	IDsStatus.m_IDs[ID_num].getM_materials().put(material_type, ID_vol);
        } else {
            res = ID_vol + dup * IDsStatus.m_IDs[ID_num].getM_materials().get(material_type); //what_pass + what the ID has already
            IDsStatus.m_IDs[ID_num].getM_materials().put(material_type, res);
        }
    }

    private static String Get_Decimal_Number(String number_expression) {
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

    private static String[] Get_Pouring_Material_Types_And_Vols(Node node) {
        String[] res = new String[6];
        String scd = Get_Node_Vol_String(node, "scd");
        String dcd = Get_Node_Vol_String(node, "dcd");
        String vol = Get_Node_Vol_String(node, "vol");

        res[0] = Get_Material_Types_From_ID(scd);
        res[4] = Get_Material_Types_From_ID(dcd);


        double[] vols = Get_Node_Vol_Values(vol);

        res[1] = vols[1] - vols[0] + ""; //Source vol after passing metrial - ASK ORIEL
        res[2] = vols[3] + ""; //Destination vol after passing metrial
        res[3] = vols[3] - vols[2] + ""; //How match material we passed to the destination ID
        res[5] = vols[2] + "";
        return res;
    }

    private static String Get_Material_Types_From_ID(String toAnalysis) {
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

    private static String Get_Node_Vol_String(Node node, String attributeName) {
        NamedNodeMap att_leaf = node.getAttributes();
        Node c_node = att_leaf.getNamedItem(attributeName);
        return c_node.getNodeValue();
    }

    private static double[] Get_Node_Vol_Values(String node_vol) {
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

    private static int[] Get_Source_And_Destination_IDs_Nums(Node node) {
        int[] source_destination_IDs = new int[2];
        String ids = Get_Node_Vol_String(node, "IDs");
        String[] split_by_to = ids.split("to");
        String[] split_by_space = split_by_to[0].split(" ");
        source_destination_IDs[0] = Integer.parseInt(split_by_space[1].replace("ID", "").trim());
        source_destination_IDs[1] = Integer.parseInt(split_by_to[1].replace("ID", "").trim()); // 
        return source_destination_IDs;
    }
}

	


