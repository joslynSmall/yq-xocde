package com.yq.xcode.common.criteria;

import com.yq.xcode.annotation.ParameterLogic;
import com.yq.xcode.common.springdata.HPageCriteria;

@SuppressWarnings("serial")
public class LookupCodeQueryCriteria extends HPageCriteria {
	
	@ParameterLogic(colName="lc.category_Code", operation="=")
	private String categoryCode;
	
	@ParameterLogic(colName="lc.lookup_name", operation="like")
	private String lookupName;
	
	@ParameterLogic(colName="lc.key_code", operation="in")
	private String keyCode;

	@ParameterLogic(colName="lc.line_number", operation="<")
	private Integer lessThanLineNumber;
	
	@ParameterLogic(colName="lc.line_number", operation=">")
	private Integer largerThanLineNumber;
	 
	@ParameterLogic(colName=" CONCAT( a.lookup_Code,a.lookup_Name,a.description ) ", operation="like")
	private String queryString;
	
	@ParameterLogic(colName="lc.parent_key_code", operation="in")
	private String parentKeyCode;
	
	@ParameterLogic(colName="lc.LOOKUP_LEVEL", operation="=")
	private Integer lookuplevel;
	
	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getLookupName() {
		return lookupName;
	}

	public void setLookupName(String lookupName) {
		this.lookupName = lookupName;
	}

	public String getKeyCode() {
		return keyCode;
	}

	public void setKeyCode(String keyCode) {
		this.keyCode = keyCode;
	}

	public Integer getLessThanLineNumber() {
		return lessThanLineNumber;
	}

	public void setLessThanLineNumber(Integer lessThanLineNumber) {
		this.lessThanLineNumber = lessThanLineNumber;
	}

	public Integer getLargerThanLineNumber() {
		return largerThanLineNumber;
	}

	public void setLargerThanLineNumber(Integer largerThanLineNumber) {
		this.largerThanLineNumber = largerThanLineNumber;
	}

 
	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public String getParentKeyCode() {
		return parentKeyCode;
	}

	public void setParentKeyCode(String parentKeyCode) {
		this.parentKeyCode = parentKeyCode;
	}

	public Integer getLookuplevel() {
		return lookuplevel;
	}

	public void setLookuplevel(Integer lookuplevel) {
		this.lookuplevel = lookuplevel;
	} 
}
