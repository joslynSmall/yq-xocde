


 package com.yq.xcode.common.criteria;

import com.yq.xcode.annotation.ParameterLogic;
import com.yq.xcode.common.springdata.HPageCriteria;

public class ReportDefineCriteria extends HPageCriteria{

	@ParameterLogic(colName=" a.CODE", operation="like" , placeHolder="" )
	private String code;
	@ParameterLogic(colName=" a.NAME", operation="like" , placeHolder="" )
	private String name;

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code=code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name=name;
	}
	
}
