package com.yq.xcode.common.criteria;

import com.yq.xcode.annotation.ParameterLogic;

public class SelectItemPageQueryCriteria extends NativeCriteria {

	private static final long serialVersionUID = 7104068027176738779L;

	@ParameterLogic(colName = " item.itemName  ", operation = "inlike")
	private String keyWord;
	@ParameterLogic(colName = "item.itemKey", operation = "inlike")
	private String keyWordContract;
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

	public String getKeyWordContract() {
		return keyWordContract;
	}

	public void setKeyWordContract(String keyWordContract) {
		this.keyWordContract = keyWordContract;
	}

}
