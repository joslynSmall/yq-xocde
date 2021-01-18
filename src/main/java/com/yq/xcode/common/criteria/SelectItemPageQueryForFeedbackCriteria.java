package com.yq.xcode.common.criteria;

import com.yq.xcode.annotation.ParameterLogic;

public class SelectItemPageQueryForFeedbackCriteria extends SelectItemPageQueryCriteria{
	

	@ParameterLogic(colName="concat(item.feedback_content,'-',b.LOOKUP_NAME)", operation="inlike")
	private String keyWord;
	
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	
	

	
}
