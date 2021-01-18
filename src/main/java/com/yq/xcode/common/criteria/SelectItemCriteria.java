package com.yq.xcode.common.criteria;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yq.xcode.common.springdata.HPageCriteria;

@JsonIgnoreProperties(ignoreUnknown=true)
public class SelectItemCriteria extends HPageCriteria{
	
	private static final long serialVersionUID = -5776119969248509138L;
 
	private String category;
	 
	private String keyword;
	
	private String itemName;
	private String itemValue;
	private String remark;
	private String column1;
	private String column2;
	private String column3;
	private String column4;
	
	
	/**
	 * 预留参数， 占位用
	 */
	private String param1;
	private String param2;
	
	/**
	 * 查询类型
	 */
	private String queryCategory;
	

 	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getQueryCategory() {
		return queryCategory;
	}

	public void setQueryCategory(String queryCategory) {
		this.queryCategory = queryCategory;
	}

	public String getParam1() {
		return param1;
	}

	public void setParam1(String param1) {
		this.param1 = param1;
	}

	public String getParam2() {
		return param2;
	}

	public void setParam2(String param2) {
		this.param2 = param2;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemValue() {
		return itemValue;
	}

	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getColumn1() {
		return column1;
	}

	public void setColumn1(String column1) {
		this.column1 = column1;
	}

	public String getColumn2() {
		return column2;
	}

	public void setColumn2(String column2) {
		this.column2 = column2;
	}

	public String getColumn3() {
		return column3;
	}

	public void setColumn3(String column3) {
		this.column3 = column3;
	}

	public String getColumn4() {
		return column4;
	}

	public void setColumn4(String column4) {
		this.column4 = column4;
	}
	
	
 
  
}
