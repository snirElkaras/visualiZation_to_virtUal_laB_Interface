package parsers.planParser;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Aviel and Chen
 *	class that holds the materials types and the quantity
 */
public class ID {
    private Map<String, Double> m_materials;

    public ID() {
        this.m_materials = new HashMap<String, Double>(); //<material,material_vol>
    }

	public Map<String, Double> getM_materials() {
		return m_materials;
	}

	public void setM_materials(Map<String, Double> m_materials) {
		this.m_materials = m_materials;
	}
}
