package parsers.planParser;

/**
 * 
 * @author Aviel and Chen
 *
 */
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
            return amount / this.information.getTotalVol();
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
        String before_reaction = "( ";
        String after_reaction = "( ";
        double basic_unit;
        double ratio_A, ratio_B, ratio_C, ratio_D;
        double diff_A, diff_B, diff_C, diff_D;

        ratio_A = get_ratio_amout(this.information.getAmount_A());
        ratio_B = get_ratio_amout(this.information.getAmount_B());
        ratio_C = get_ratio_amout(this.information.getAmount_C());
        ratio_D = get_ratio_amout(this.information.getAmount_D());

        diff_A = get_diff_amout(ratio_A, this.information.getActualAmount_A());
        diff_B = get_diff_amout(ratio_B, this.information.getActualAmount_B());
        diff_C = get_diff_amout(ratio_C, this.information.getActualAmount_C());
        diff_D = get_diff_amout(ratio_D, this.information.getActualAmount_D());

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
        before_reaction = before_reaction.substring(0, before_reaction.length() - 2) + ")";
        after_reaction = after_reaction.substring(0, after_reaction.length() - 2) + ")";
        return before_reaction + " --> " + after_reaction;
    }
}
