package com.yq.xcode.common.criteria;

public class ParseElementQueryCriteria extends NativeCriteria{
	//元素用途
	private String categoryCode;
	
	//元素类型
	private String eleCategory;
	
	//函数工具窗用
	private String functionCode;
    
	private String keyString;
	
	
	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getEleCategory() {
		return eleCategory;
	}

	public void setEleCategory(String eleCategory) {
		this.eleCategory = eleCategory;
	}

	public String getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}

	public String getKeyString() {
		return keyString;
	}

	public void setKeyString(String keyString) {
		this.keyString = keyString;
	}



	

}
