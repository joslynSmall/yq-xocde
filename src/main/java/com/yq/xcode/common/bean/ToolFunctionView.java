package com.yq.xcode.common.bean;

public class ToolFunctionView {
	
	private String functionCode;
	private String functionName;
	private String description;
	
	
	public ToolFunctionView() {
		
	}
	public ToolFunctionView(String functionCode, String functionName, String description ) {
		this.functionCode = functionCode;
		this.functionName = functionName;
		this.description = description;
	}
	public String getFunctionCode() {
		return functionCode;
	}
	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}
	public String getFunctionName() {
		return functionName;
	}
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	

}
