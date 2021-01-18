package com.yq.xcode.common.criteria;

import com.yq.xcode.annotation.ParameterLogic;
import com.yq.xcode.common.springdata.HPageCriteria;

public class EntityTemplateCriteria extends HPageCriteria{
	
	private static final long serialVersionUID = -5305959941206392523L;

	@ParameterLogic(colName="CONCAT(a.CODE,'-',a.NAME)", operation="like")
    private String queryString;
	
	@ParameterLogic(colName="a.category", operation="=")
	private String category;
	
	@ParameterLogic(colName="a.TEMPLATE_STATUS", operation="=")
	private String templateStatus;
		
	private String letterTemplate;
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTemplateStatus() {
		return templateStatus;
	}

	public void setTemplateStatus(String templateStatus) {
		this.templateStatus = templateStatus;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public String getLetterTemplate() {
		return letterTemplate;
	}

	public void setLetterTemplate(String letterTemplate) {
		this.letterTemplate = letterTemplate;
	}
 
	
	
}
