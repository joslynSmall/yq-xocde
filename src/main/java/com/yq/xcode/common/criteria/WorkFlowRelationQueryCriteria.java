package com.yq.xcode.common.criteria;

@SuppressWarnings("serial")
public class WorkFlowRelationQueryCriteria extends NativeCriteria {

	/**
	 * 键值
	 */

	private String methodKey;

	/**
	 * 名称
	 */

	private String methodName;

	/**
	 * 表达式类别
	 */
	private String methodCategory;
	
	/**
	 * 代码
	 */
	private String methodSource;
	
	
	public String getMethodSource() {
		return methodSource;
	}

	public void setMethodSource(String methodSource) {
		this.methodSource = methodSource;
	}

	public String getMethodCategory() {
		return methodCategory;
	}

	public void setMethodCategory(String methodCategory) {
		this.methodCategory = methodCategory;
	}

	public String getMethodKey() {
		return methodKey;
	}

	public void setMethodKey(String methodKey) {
		this.methodKey = methodKey;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	
}