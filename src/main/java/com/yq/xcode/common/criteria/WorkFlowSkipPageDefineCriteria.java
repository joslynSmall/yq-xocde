


 package com.yq.xcode.common.criteria;

import com.yq.xcode.annotation.ParameterLogic;
import com.yq.xcode.common.springdata.HPageCriteria;

public class WorkFlowSkipPageDefineCriteria extends HPageCriteria{
	
	@ParameterLogic(colName ="a.category_code", operation="=" )
	private String categoryCode;

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	
	
	
}
