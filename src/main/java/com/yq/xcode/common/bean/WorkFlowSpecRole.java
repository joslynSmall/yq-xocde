package com.yq.xcode.common.bean;

import com.yq.xcode.common.base.XBaseView;

public class WorkFlowSpecRole  extends XBaseView { 
	/**
	 * 特别的角色，特定的人审批，可能有多个
	 */
    private String role;
    private String userProperty;
    
    public WorkFlowSpecRole() {
    	
    }
    public WorkFlowSpecRole(String role,String userProperty) {
    	this.role = role;
    	this.userProperty = userProperty;
    }
    
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getUserProperty() {
		return userProperty;
	}
	public void setUserProperty(String userProperty) {
		this.userProperty = userProperty;
	}
  

}
