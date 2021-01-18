package com.yq.xcode.common.criteria;

import com.yq.xcode.annotation.ParameterLogic;

public class SelectItemCommonCriteria extends NativeCriteria {
 	@ParameterLogic(colName="concat(a.itemName,'-',ifnull(a.remark,''))", operation="inlike")
	private String keyWord;
	private String valueSetCode;
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public String getValueSetCode() {
		return valueSetCode;
	}
	public void setValueSetCode(String valueSetCode) {
		this.valueSetCode = valueSetCode;
	}

	

	
}
