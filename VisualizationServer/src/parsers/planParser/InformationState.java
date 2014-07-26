package parsers.planParser;

/**
 * 
 * @author Aviel and Chen
 *	This class holds all the information of a node for the oracle problem
 */
public class InformationState {
	private double amount_A;
	private double amount_B;
	private double amount_C;
	private double amount_D;

	private double actualAmount_A;
	private double actualAmount_B;
	private double actualAmount_C;
	private double actualAmount_D;

	private double srcAmount_A;
	private double srcAmount_B;
	private double srcAmount_C;
	private double srcAmount_D;

	private double totalVol;
	private String volume;

	private boolean hasReaction;
	private boolean isReasonable;
	private boolean hasChildrenWithReaction;
	private boolean isHigherCompAct;
	private String color;
	private boolean isDirectChildOfRoot;
	private int pos;
	private String reactionEquation;

	private String scdDesc;
	private String dcdDesc;
	private String rcdDesc;
	private String ids;

	private boolean AHasPoured;
	private boolean BHasPoured;
	private boolean CHasPoured;
	private boolean DHasPoured;
	
	public double getAmount_A() {
		return amount_A;
	}
	public void setAmount_A(double amount_A) {
		this.amount_A = amount_A;
	}
	public double getAmount_B() {
		return amount_B;
	}
	public void setAmount_B(double amount_B) {
		this.amount_B = amount_B;
	}
	public double getAmount_C() {
		return amount_C;
	}
	public void setAmount_C(double amount_C) {
		this.amount_C = amount_C;
	}
	public double getAmount_D() {
		return amount_D;
	}
	public void setAmount_D(double amount_D) {
		this.amount_D = amount_D;
	}
	public double getActualAmount_A() {
		return actualAmount_A;
	}
	public void setActualAmount_A(double actualAmount_A) {
		this.actualAmount_A = actualAmount_A;
	}
	public double getActualAmount_B() {
		return actualAmount_B;
	}
	public void setActualAmount_B(double actualAmount_B) {
		this.actualAmount_B = actualAmount_B;
	}
	public double getActualAmount_C() {
		return actualAmount_C;
	}
	public void setActualAmount_C(double actualAmount_C) {
		this.actualAmount_C = actualAmount_C;
	}
	public double getActualAmount_D() {
		return actualAmount_D;
	}
	public void setActualAmount_D(double actualAmount_D) {
		this.actualAmount_D = actualAmount_D;
	}
	public double getSrcAmount_A() {
		return srcAmount_A;
	}
	public void setSrcAmount_A(double srcAmount_A) {
		this.srcAmount_A = srcAmount_A;
	}
	public double getSrcAmount_B() {
		return srcAmount_B;
	}
	public void setSrcAmount_B(double srcAmount_B) {
		this.srcAmount_B = srcAmount_B;
	}
	public double getSrcAmount_C() {
		return srcAmount_C;
	}
	public void setSrcAmount_C(double srcAmount_C) {
		this.srcAmount_C = srcAmount_C;
	}
	public double getSrcAmount_D() {
		return srcAmount_D;
	}
	public void setSrcAmount_D(double srcAmount_D) {
		this.srcAmount_D = srcAmount_D;
	}
	public double getTotalVol() {
		return totalVol;
	}
	public void setTotalVol(double totalVol) {
		this.totalVol = totalVol;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public boolean isHasReaction() {
		return hasReaction;
	}
	public void setHasReaction(boolean hasReaction) {
		this.hasReaction = hasReaction;
	}
	public boolean isReasonable() {
		return isReasonable;
	}
	public void setReasonable(boolean isReasonable) {
		this.isReasonable = isReasonable;
	}
	public boolean isHasChildrenWithReaction() {
		return hasChildrenWithReaction;
	}
	public void setHasChildrenWithReaction(boolean hasChildrenWithReaction) {
		this.hasChildrenWithReaction = hasChildrenWithReaction;
	}
	public boolean isHigherCompAct() {
		return isHigherCompAct;
	}
	public void setHigherCompAct(boolean isHigherCompAct) {
		this.isHigherCompAct = isHigherCompAct;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public boolean isDirectChildOfRoot() {
		return isDirectChildOfRoot;
	}
	public void setDirectChildOfRoot(boolean isDirectChildOfRoot) {
		this.isDirectChildOfRoot = isDirectChildOfRoot;
	}
	public int getPos() {
		return pos;
	}
	public void setPos(int pos) {
		this.pos = pos;
	}
	public String getReactionEquation() {
		return reactionEquation;
	}
	public void setReactionEquation(String reactionEquation) {
		this.reactionEquation = reactionEquation;
	}
	public String getScdDesc() {
		return scdDesc;
	}
	public void setScdDesc(String scdDesc) {
		this.scdDesc = scdDesc;
	}
	public String getDcdDesc() {
		return dcdDesc;
	}
	public void setDcdDesc(String dcdDesc) {
		this.dcdDesc = dcdDesc;
	}
	public String getRcdDesc() {
		return rcdDesc;
	}
	public void setRcdDesc(String rcdDesc) {
		this.rcdDesc = rcdDesc;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public boolean isAHasPoured() {
		return AHasPoured;
	}
	public void setAHasPoured(boolean aHasPoured) {
		AHasPoured = aHasPoured;
	}
	public boolean isBHasPoured() {
		return BHasPoured;
	}
	public void setBHasPoured(boolean bHasPoured) {
		BHasPoured = bHasPoured;
	}
	public boolean isCHasPoured() {
		return CHasPoured;
	}
	public void setCHasPoured(boolean cHasPoured) {
		CHasPoured = cHasPoured;
	}
	public boolean isDHasPoured() {
		return DHasPoured;
	}
	public void setDHasPoured(boolean dHasPoured) {
		DHasPoured = dHasPoured;
	}
	

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
