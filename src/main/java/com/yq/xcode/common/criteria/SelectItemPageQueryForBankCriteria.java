package com.yq.xcode.common.criteria;

import com.yq.xcode.annotation.ParameterLogic;

public class SelectItemPageQueryForBankCriteria extends SelectItemPageQueryCriteria{
	

	@ParameterLogic(colName="concat(item.names,'-',item.number)", operation="inlike")
	private String keyWord;
	
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	
	

	
}
