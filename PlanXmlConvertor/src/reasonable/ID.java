/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package reasonable;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author gyosef2
 */
public class ID {
    Map<String, Double> m_materials;
    //double m_Unknown_acid_vol;
    //double m_NaoH_vol;
    //double m_Unknown_acid_ratio;
    //double m_NaoH_ratio;

    public ID() {
        m_materials = new HashMap<String, Double>(); //<material,material_vol>
        //m_Unknown_acid_ratio = 0;
        //m_NaoH_ratio = 0;
        //m_NaoH_vol = 0;
        //m_Unknown_acid_vol = 0;
    }
}
