


 package com.yq.xcode.common.criteria;

import com.yq.xcode.annotation.ParameterLogic;
import com.yq.xcode.common.springdata.HPageCriteria;

public class WorkFlowMutiOperationCriteria extends HPageCriteria{

	@ParameterLogic(colName=" a.lookupCode_Key", operation="like" , placeHolder="" )
	private String lookupCodeKey;

	public String getLookupCodeKey() {
		return lookupCodeKey;
	}
	public void setLookupCodeKey(String lookupCodeKey) {
		this.lookupCodeKey=lookupCodeKey;
	}
	
}
