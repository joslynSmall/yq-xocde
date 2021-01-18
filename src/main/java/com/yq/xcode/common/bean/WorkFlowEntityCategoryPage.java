package com.yq.xcode.common.bean;



public class WorkFlowEntityCategoryPage  {

	/**
	 * 代码
	 */
	private String categoryCode;

	/**
	 * 名称
	 */
	private String categoryName;
	
  	
	/**
	 * 打开审批单的URL连接 固定后面的参数是 ?id=
	 */
	private String openUrl;
	
	private int recordCnt = 0;

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getOpenUrl() {
		return openUrl;
	}

	public void setOpenUrl(String openUrl) {
		this.openUrl = openUrl;
	}

	public int getRecordCnt() {
		return recordCnt;
	}

	public void setRecordCnt(int recordCnt) {
		this.recordCnt = recordCnt;
	}
	 
	
}
