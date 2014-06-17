package parsers.planParser;

public class InformationState {
	double amount_A;
	double amount_B;
	double amount_C;
	double amount_D;
	
	double actualAmount_A;
	double actualAmount_B;
	double actualAmount_C;
	double actualAmount_D;
	
	double srcAmount_A;
	double srcAmount_B;
	double srcAmount_C;
	double srcAmount_D;
	
	double totalVol;
	String volume;
	
	boolean hasReaction;
	boolean isReasonable;
	boolean hasChildrenWithReaction;
	boolean isHigherCompAct;
	String color;
	boolean isDirectChildOfRoot;
	int pos;
	String reactionEquation;
        
        @Override
	public String toString() {
		return "InformationState [amount_A=" + amount_A + ", amount_B="
				+ amount_B + ", amount_C=" + amount_C + ", amount_D="
				+ amount_D + ", actualAmount_A=" + actualAmount_A
				+ ", actualAmount_B=" + actualAmount_B + ", actualAmount_C="
				+ actualAmount_C + ", actualAmount_D=" + actualAmount_D
				+ ", totalVol=" + totalVol + ", hasReaction=" + hasReaction
				+ ", isReasonable=" + isReasonable
				+ ", hasChildrenWithReaction=" + hasChildrenWithReaction
				+ ", isHigherCompAct=" + isHigherCompAct + ", Color=" + color +  ", pos=" + pos
				+ ", isDirectChildOfRoot=" + isDirectChildOfRoot
				+ ", scdDesc=" + scdDesc + ", dcdDesc=" + dcdDesc
				+ ", rcdDesc=" + rcdDesc + ", ids=" + ids + ", AHasPoured="
				+ AHasPoured + ", BHasPoured=" + BHasPoured + ", CHasPoured="
				+ CHasPoured + ", DHasPoured=" + DHasPoured + "]";
	}

		String scdDesc;
        String dcdDesc;
        String rcdDesc;
        String ids;
        
        boolean AHasPoured;
        boolean BHasPoured;
        boolean CHasPoured;
        boolean DHasPoured;
	
	
	InformationState()
	{
		amount_A = 0;
		amount_B = 0;
		amount_C = 0;
		amount_D = 0;
		actualAmount_A = 0;
		actualAmount_B = 0;
		actualAmount_C = 0;
		actualAmount_D = 0;
		totalVol = 0;
		hasReaction = false;
		isReasonable = false;
		hasChildrenWithReaction = false;
		isHigherCompAct = false;
        scdDesc = "";
        dcdDesc = "";
        rcdDesc = "";
        ids = "";
        color = "Black";        
        isDirectChildOfRoot = false;   
        volume = "";
        reactionEquation = "";
		
	}
        
        
        public int getPos () { // Gil_Yosef 29/3/2013 for finding node function
            return pos;
        }
	
	public boolean isReasonable()
	{
		return isReasonable;
	}

	public boolean hasReaction()
	{
		return hasReaction;
	}
	
	public boolean hasChildrenWithReaction()
	{
		return hasChildrenWithReaction;
	}
	
	public boolean isHigherComplexAction()
	{
		return isHigherCompAct;
	}
	
	public String getColor(){
		return color;
	}


	public String getReactionEquation() {
		return reactionEquation;
	}


	public void setReactionEquation(String reactionEquation) {
		this.reactionEquation = reactionEquation;
	}
	
}
