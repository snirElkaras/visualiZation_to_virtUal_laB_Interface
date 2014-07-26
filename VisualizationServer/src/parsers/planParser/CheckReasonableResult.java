package parsers.planParser;

import java.util.ArrayList;

import org.w3c.dom.Node;

/**
 * 
 * @author Aviel and Chen
 * 			this class wraps list of states and the root node
 *
 */
public class CheckReasonableResult {
	private Node root;
	private ArrayList<State> listOfStates;
	
	public CheckReasonableResult(Node root, ArrayList<State> listOfStates){
		this.setRoot(root);
		this.setListOfStates(listOfStates);
	}

	public Node getRoot() {
		return root;
	}

	public void setRoot(Node root) {
		this.root = root;
	}

	public ArrayList<State> getListOfStates() {
		return listOfStates;
	}

	public void setListOfStates(ArrayList<State> listOfStates) {
		this.listOfStates = listOfStates;
	}
}
