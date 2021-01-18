package com.yq.xcode.common.criteria;

import com.yq.xcode.annotation.ParameterLogic;

public class YqUserQueryCriteria extends NativeCriteria{

	/**
	 * 
	 */
	private static long serialVersionUID = -6054588233365853055L;
	

	@ParameterLogic(colName ="CONCAT(u.user_code, ' || ', u.user_name)", operation="like" )
	private String keyWord;
	@ParameterLogic(colName ="u.USER_CATEGORY", operation="like" )
	private String userCategory;
	
	
	public String getUserCategory() {
		return userCategory;
	}
	public void setUserCategory(String userCategory) {
		this.userCategory = userCategory;
	}
	/**
	 * 客户，总部用户
	 */
	private String userType;
	
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}

}
