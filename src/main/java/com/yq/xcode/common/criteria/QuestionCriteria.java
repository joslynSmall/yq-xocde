package com.yq.xcode.common.criteria;

import com.yq.xcode.annotation.ParameterLogic;

public class QuestionCriteria extends NativeCriteria{
	

	@ParameterLogic(colName="qe.question_category", operation="=")
	private String questionCategory;

	@ParameterLogic(colName="qe.question_description", operation="like")
	private String queryString;


	
	public String getQuestionCategory() {
		return questionCategory;
	}

	public void setQuestionCategory(String questionCategory) {
		this.questionCategory = questionCategory;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
	
	
	
}
