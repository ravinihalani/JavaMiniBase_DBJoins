package tests;

import global.AttrOperator;

class TwoPredicate {
	public String LHS1;
	public Integer LHS1Column;
	public Integer LHSOperator;
	public String LHS2;
	public Integer LHS2Column;

	//public String ANDorOR;
	
	public String RHS1;
	public Integer RHS1Column;
	public Integer RHSOperator;
	public String RHS2;
	public Integer RHS2Column;

	//public Integer Projection;

	public TwoPredicate() {
		
	}
	
	public String getLHS1() {
		return LHS1;
	}

	public void setLHS1(String lHS1) 
	{
	
			LHS1 = lHS1;
		
		
	}

	
	
	public Integer getLHS1Column() {
		return LHS1Column;
	}

	public void setLHS1Column(Integer lHS1Column) {
		LHS1Column = lHS1Column;
	}

	public Integer getLHSOperator() {
		return LHSOperator;
	}

	public void setLHSOperator(Integer lHSOperator) {
		LHSOperator = lHSOperator;
	}

	public String getLHS2() {
		return LHS2;
	}

	public void setLHS2(String lHS2) {
		LHS2 = lHS2;
	}

	public Integer getLHS2Column() {
		return LHS2Column;
	}

	public void setLHS2Column(Integer lHS2Column) {
		LHS2Column = lHS2Column;
	}
	
	/*

	public String getANDorOR() {
		return ANDorOR;
	}

	public void setANDorOR(String aNDorOR) {
		ANDorOR = aNDorOR;
	}
*/
	
	public String getRHS1() {
		return RHS1;
	}

	public void setRHS1(String rHS1) {
		
		RHS1 = rHS1;
	}

	public Integer getRHS1Column() {
		return RHS1Column;
	}

	public void setRHS1Column(Integer rHS1Column) {
		RHS1Column = rHS1Column;
	}

	public Integer getRHSOperator() {
		return RHSOperator;
	}

	public void setRHSOperator(Integer rHSOperator) {
		RHSOperator = rHSOperator;
	}

	public String getRHS2() {
		
		return RHS2;
	}

	public void setRHS2(String rHS2) {
		
		RHS2 = rHS2;
	}

	public Integer getRHS2Column() {
		return RHS2Column;
	}

	public void setRHS2Column(Integer rHS2Column) {
		RHS2Column = rHS2Column;
	}

	/*
	public Integer getProjection() {
		return Projection;
	}

	public void setProjection(Integer projection) {
		Projection = projection;
	}

	*/
	public void show(){
		System.out.println("t:"+LHS1+" "+LHS1Column+" "+LHSOperator+" "+" t:"+LHS2+" "+LHS2Column+" "+" t:"+RHS1+" "+RHS1Column+" "+RHSOperator+" "+" t:"+RHS2+" "+RHS2Column);
	}
}


