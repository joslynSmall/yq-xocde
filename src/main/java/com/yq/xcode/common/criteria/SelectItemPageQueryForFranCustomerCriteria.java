package com.yq.xcode.common.criteria;

import com.yq.xcode.annotation.ParameterLogic;

public class SelectItemPageQueryForFranCustomerCriteria extends SelectItemPageQueryCriteria{
	

	@ParameterLogic(colName="concat(item.code,'-',item.name)", operation="like")
	private String keyWord;
	
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	
	

	
}
