package parsers.planParser;

/**
 * 
 * @author Aviel and Chen
 *	this class holds an array with the flasks IDs and their materials
 */
public class IDsStatus {

	public static ID[] m_IDs = new ID[100];
	
	
    // init IDs state for new tree analysis
    public static void init_IDs_states() {
        for (int i = 0; i < IDsStatus.m_IDs.length; i++) {
        	IDsStatus.m_IDs[i] = new ID();
        }
    }
}
