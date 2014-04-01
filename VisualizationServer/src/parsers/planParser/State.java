package parsers.planParser;

import javax.swing.JOptionPane;

public class State {

    public InformationState information;
    public State firstChild;
    public State secondChild;
    @Override
	public String toString() {
		return "State [information=" + information + ", firstChild="
				+ firstChild + ", secondChild=" + secondChild + ", epsilon="
				+ epsilon + "]";
	}

	public double epsilon = 0.01;

    State() {
        information = new InformationState();
        firstChild = null;
        secondChild = null;
    }

    public InformationState getInformation() {
        return information;
    }

    public boolean hasFirstChild() {
        if (firstChild != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean hasSecondChild() {
        if (secondChild != null) {
            return true;
        } else {
            return false;
        }
    }

    public State getFirstChild() {
        return firstChild;
    }

    public State getSecondChild() {
        return secondChild;
    }

    public double get_ratio_amout(double amount) {
        if (amount > 0) {
            return amount / this.information.totalVol;
        }
        return 0;
    }

    public double get_diff_amout(double amout_source, double amount_result) {
        double diff = amount_result - amout_source;
        return (diff <= epsilon && diff >= (-1)*epsilon) ? 0 : diff;
    }

    public double get_minimun_not_zero(double x, double y) {
        y = Math.abs(y);
        x = Math.abs(x);
        if (y != 0) {
            return (x <= epsilon) ? y : Math.min(x, y);
        } else {
            return x;
        }
    }

    public double get_basic_unit(double unit_a, double unit_b, double unit_c, double unit_d) {
        return (get_minimun_not_zero(get_minimun_not_zero(get_minimun_not_zero(unit_a, unit_b), unit_c), unit_d));
    }

    public int get_metrial_ratio_in_reaction_equation(double material_amout, double basic_amout) {
        material_amout = Math.abs(material_amout);
        basic_amout = Math.abs(basic_amout);
        
        return (material_amout > basic_amout) ? (int)(Math.round(material_amout / basic_amout)) : (int)(Math.round(basic_amout / material_amout)); //To deal with devid with 0
    }

    public String get_reaction_equation() {
        String before_reaction = "{ ";
        String after_reaction = "{ ";
        double basic_unit;
        double ratio_A, ratio_B, ratio_C, ratio_D;
        double diff_A, diff_B, diff_C, diff_D;

        ratio_A = get_ratio_amout(this.information.amount_A);
        ratio_B = get_ratio_amout(this.information.amount_B);
        ratio_C = get_ratio_amout(this.information.amount_C);
        ratio_D = get_ratio_amout(this.information.amount_D);

        diff_A = get_diff_amout(ratio_A, this.information.actualAmount_A);
        diff_B = get_diff_amout(ratio_B, this.information.actualAmount_B);
        diff_C = get_diff_amout(ratio_C, this.information.actualAmount_C);
        diff_D = get_diff_amout(ratio_D, this.information.actualAmount_D);

        basic_unit = get_basic_unit(diff_A, diff_B, diff_C, diff_D);

        if (diff_A < 0) {
            before_reaction += get_metrial_ratio_in_reaction_equation(diff_A, basic_unit) + "A + ";
        } else if (diff_A > 0) {
            after_reaction += get_metrial_ratio_in_reaction_equation(diff_A, basic_unit) + "A + ";
        }
        
        if (diff_B < 0) {
            before_reaction += get_metrial_ratio_in_reaction_equation(diff_B, basic_unit) + "B + ";
        } else if (diff_B > 0){
            after_reaction += get_metrial_ratio_in_reaction_equation(diff_B, basic_unit) + "B + ";
        }
        
        if (diff_C < 0) {
            before_reaction += get_metrial_ratio_in_reaction_equation(diff_C, basic_unit) + "C + ";
        } else if (diff_C > 0) {
            after_reaction += get_metrial_ratio_in_reaction_equation(diff_C, basic_unit) + "C + ";
        }
        
        if (diff_D < 0) {
            before_reaction += get_metrial_ratio_in_reaction_equation(diff_D, basic_unit) + "D + ";
        } else if (diff_D > 0) {
            after_reaction += get_metrial_ratio_in_reaction_equation(diff_D, basic_unit) + "D + ";
        }
        before_reaction = before_reaction.substring(0, before_reaction.length() - 2) + "}";
        after_reaction = after_reaction.substring(0, after_reaction.length() - 2) + "}";
        return before_reaction + " -> " + after_reaction;
    }

    public String getComponent_Actually() {
        String ans = "";
        if (this.information.actualAmount_A > 0) {
            ans = "A + ";
        }
        if (this.information.actualAmount_B > 0) {
            ans = ans + "B + ";
        }
        if (this.information.actualAmount_C > 0) {
            ans = ans + "C + ";
        }
        if (this.information.actualAmount_D > 0) {
            ans = ans + "D + ";
        }
        ans = ans.substring(0, ans.length() - 3);
        return ans;
    }

    public String getComponent_Mixed() {
        String ans = "";
        if (this.information.amount_A > 0) {
            ans = "A + ";
        }
        if (this.information.amount_B > 0) {
            ans = ans + "B + ";
        }
        if (this.information.amount_C > 0) {
            ans = ans + "C + ";
        }
        if (this.information.amount_D > 0) {
            ans = ans + "D + ";
        }
        ans = ans.substring(0, ans.length() - 3);
        return ans;
    }

    public String getHigherDescription() {
        if (this.hasFirstChild() && this.hasSecondChild()) //there are two children
        {
            //make recursive call for two children
            this.updateSolutionsPouredChildren();
            //update this node
            this.information.AHasPoured = (this.getFirstChild().information.AHasPoured || this.getSecondChild().information.AHasPoured);
            this.information.BHasPoured = (this.getFirstChild().information.BHasPoured || this.getSecondChild().information.BHasPoured);
            this.information.CHasPoured = (this.getFirstChild().information.CHasPoured || this.getSecondChild().information.CHasPoured);
            this.information.DHasPoured = (this.getFirstChild().information.DHasPoured || this.getSecondChild().information.DHasPoured);
        }
        else //there are no 2 children
        {
            if (this.hasFirstChild() || this.hasSecondChild()) //there are one child
            {
                if (this.hasFirstChild())
                {
                    //update the child
                    
                    //update this node
                    this.information.AHasPoured = this.getFirstChild().information.AHasPoured;
                    this.information.BHasPoured = this.getFirstChild().information.BHasPoured; 
                    this.information.CHasPoured = this.getFirstChild().information.CHasPoured; 
                    this.information.DHasPoured = this.getFirstChild().information.DHasPoured; 
                }
                else
                {
                    //update the child
                    
                    //update this node
                    this.information.AHasPoured = this.getSecondChild().information.AHasPoured;
                    this.information.BHasPoured = this.getSecondChild().information.BHasPoured;
                    this.information.CHasPoured = this.getSecondChild().information.CHasPoured;
                    this.information.DHasPoured = this.getSecondChild().information.DHasPoured;
                }
            }
            else //there are no child
            {
                if (XMLReader.getAmount(this.information.scdDesc, "A") > 0)
                    this.information.AHasPoured = true;
                if (XMLReader.getAmount(this.information.scdDesc, "B") > 0)
                        this.information.BHasPoured = true;
                if (XMLReader.getAmount(this.information.scdDesc, "C") > 0)
                        this.information.CHasPoured = true;
                if (XMLReader.getAmount(this.information.scdDesc, "D") > 0)
                        this.information.DHasPoured = true;
                
                if (XMLReader.getAmount(this.information.dcdDesc, "A") > 0)
                    this.information.AHasPoured = true;
                if (XMLReader.getAmount(this.information.dcdDesc, "B") > 0)
                        this.information.BHasPoured = true;
                if (XMLReader.getAmount(this.information.dcdDesc, "C") > 0)
                        this.information.CHasPoured = true;
                if (XMLReader.getAmount(this.information.dcdDesc, "D") > 0)
                        this.information.DHasPoured = true;
            }
        }
        
        String ans = "";
        if (this.information.AHasPoured) {
            ans = "A + ";
        }
        if (this.information.BHasPoured) {
            ans = ans + "B + ";
        }
        if (this.information.CHasPoured) {
            ans = ans + "C + ";
        }
        if (this.information.DHasPoured) {
            ans = ans + "D + ";
        }
        ans = ans.substring(0, ans.length() - 3);
        String actComp = this.getComponent_Actually();
        if (!ans.equalsIgnoreCase(actComp)) {
            ans = "{" + ans + "} -> {" + actComp + "}";
        }
        return ans;
    }
    
    public void updateSolutionsPouredChildren ()
    {
        if (this.hasFirstChild() && this.hasSecondChild()) //2 children
        {
            //update the children
            this.getFirstChild().updateSolutionsPouredChildren();
            this.getSecondChild().updateSolutionsPouredChildren();
            
            //update the first child if it hasn't children
            if (!this.getFirstChild().hasFirstChild() && !this.getFirstChild().hasSecondChild())
            {
                if (XMLReader.getAmount(this.getFirstChild().information.dcdDesc, "A") > 0)
                    this.getFirstChild().information.AHasPoured = true;
                if (XMLReader.getAmount(this.getFirstChild().information.dcdDesc, "B") > 0)
                    this.getFirstChild().information.BHasPoured = true;
                if (XMLReader.getAmount(this.getFirstChild().information.dcdDesc, "C") > 0)
                    this.getFirstChild().information.CHasPoured = true;
                if (XMLReader.getAmount(this.getFirstChild().information.dcdDesc, "D") > 0)
                    this.getFirstChild().information.DHasPoured = true;
            }
            
            //update this node
            this.information.AHasPoured = (this.getFirstChild().information.AHasPoured || this.getSecondChild().information.AHasPoured);
            this.information.BHasPoured = (this.getFirstChild().information.BHasPoured || this.getSecondChild().information.BHasPoured);
            this.information.CHasPoured = (this.getFirstChild().information.CHasPoured || this.getSecondChild().information.CHasPoured);
            this.information.DHasPoured = (this.getFirstChild().information.DHasPoured || this.getSecondChild().information.DHasPoured);
            
            
            //check if it is solution mixing action or intermediate flask action
            String id1 = this.getFirstChild().information.ids;
            String id2 = this.getSecondChild().information.ids;
            String[] ids1 = id1.split(" ");
            String[] ids2 = id2.split(" "); //ids[1] == sid, ids[3] == did
            if (ids1[3].equalsIgnoreCase(ids2[3])) //solution mix action
            {
                
            }
            else // intermediate flask action
            {
                
            }
        }
        else // no child
        {
            if (XMLReader.getAmount(this.information.scdDesc, "A") > 0)
                    this.information.AHasPoured = true;
            if (XMLReader.getAmount(this.information.scdDesc, "B") > 0)
                    this.information.BHasPoured = true;
            if (XMLReader.getAmount(this.information.scdDesc, "C") > 0)
                    this.information.CHasPoured = true;
            if (XMLReader.getAmount(this.information.scdDesc, "D") > 0)
                    this.information.DHasPoured = true;
        }
        return;
    }
    
    /*
     public String getHigherDescription() {
        String ans = "";
        if (this.isCompPoured("A")) {
            ans = "A + ";
        }
        if (this.isCompPoured("B")) {
            ans = ans + "B + ";
        }
        if (this.isCompPoured("C")) {
            ans = ans + "C + ";
        }
        if (this.isCompPoured("D")) {
            ans = ans + "D + ";
        }
        ans = ans.substring(0, ans.length() - 3);
        String actComp = this.getComponent_Actually();
        if (!ans.equalsIgnoreCase(actComp)) {
            ans = "{" + ans + "} -> {" + actComp + "}";
        }
        return ans;
    }
     */

    private boolean isCompPoured(String comp) {
        boolean ans = false;
        if (!this.hasFirstChild() && !this.hasSecondChild()) {
            if (comp.equalsIgnoreCase("A")) {
                ans = this.information.amount_A > 0;
            }
            if (comp.equalsIgnoreCase("B")) {
                ans = this.information.amount_B > 0;
            }
            if (comp.equalsIgnoreCase("C")) {
                ans = this.information.amount_C > 0;
            }
            if (comp.equalsIgnoreCase("D")) {
                ans = this.information.amount_D > 0;
            }
        } else {
            if (this.hasFirstChild()) {
                ans = ans || this.firstChild.isCompPoured(comp);
            }
            if (this.hasSecondChild()) {
                ans = ans || this.secondChild.isCompPoured(comp);
            }
        }
        return ans;
    }
}
