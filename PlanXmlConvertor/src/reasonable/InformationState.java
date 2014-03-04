package reasonable;

public class InformationState {
	double amount_A;
	double amount_B;
	double amount_C;
	double amount_D;
	
	double actualAmount_A;
	double actualAmount_B;
	double actualAmount_C;
	double actualAmount_D;
	
	double totalVol;
	
	boolean hasReaction;
	boolean isReasonable;
	boolean hasChildrenWithReaction;
	boolean isHigherCompAct;
	
	int pos;
        
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
	
}
