package com.yq.xcode.common.bean;

import java.util.List;

public class ContractFunctionView {
	/**
	 * 第几个函数
	 */
	private Integer ind;
	/**
	 * functionCode = eleNumber
	 */
	private Long functionId;
	private String functionCode;
	/**
	 * functionName = eleName
	 */
	private String functionName;
	private String description;
	
	private String parameters;
	private String paramsCn;
	
	private String termsTitle;
	
	private List<ContractParamsView> paramList;
	
	public ContractFunctionView() {
	}
	
	public ContractFunctionView(String functionCode, String functionName) {
		this.functionCode = functionCode;
		this.functionName = functionName;
		this.description = "当前尚未选择函数";
	}

	
	
	public String getParamsCn() {
		return paramsCn;
	}

	public void setParamsCn(String paramsCn) {
		this.paramsCn = paramsCn;
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
	public String getFunctionCode() {
		return functionCode;
	}
	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}
	public String getParameters() {
		return parameters;
	}
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
	public List<ContractParamsView> getParamList() {
		return paramList;
	}
	public void setParamList(List<ContractParamsView> paramList) {
		this.paramList = paramList;
	}

	public Integer getInd() {
		return ind;
	}

	public void setInd(Integer ind) {
		this.ind = ind;
	}

	public Long getFunctionId() {
		return functionId;
	}

	public void setFunctionId(Long functionId) {
		this.functionId = functionId;
	}

	public String getTermsTitle() {
		return termsTitle;
	}

	public void setTermsTitle(String termsTitle) {
		this.termsTitle = termsTitle;
	}
 
}
