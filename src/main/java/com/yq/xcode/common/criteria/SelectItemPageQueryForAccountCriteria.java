package com.yq.xcode.common.criteria;

import com.yq.xcode.annotation.ParameterLogic;

public class SelectItemPageQueryForAccountCriteria extends SelectItemPageQueryCriteria{
	
	private static final long serialVersionUID = 6725380870350015625L;
	
	@ParameterLogic(colName="concat(item.alias_name,'-',item.user_name)", operation="inlike")
	private String keyWord;
	
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	
	

	
}
