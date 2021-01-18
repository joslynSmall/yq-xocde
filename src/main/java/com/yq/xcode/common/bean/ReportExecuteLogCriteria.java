package com.yq.xcode.common.bean;

import com.yq.xcode.annotation.ParameterLogic;
import com.yq.xcode.common.criteria.NativeCriteria;
import com.yq.xcode.common.springdata.HPageCriteria;



public class ReportExecuteLogCriteria extends HPageCriteria{
	
	@ParameterLogic(colName="r.REPORT_NAME", operation="like")
	private String queryString;

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
	
}
