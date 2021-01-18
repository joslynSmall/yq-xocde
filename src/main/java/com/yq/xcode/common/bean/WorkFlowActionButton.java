package com.yq.xcode.common.bean;

import com.yq.xcode.common.base.XBaseView;

/**
 * 工作量操作按钮 
 * @author jettie
 *
 */

public class WorkFlowActionButton  extends XBaseView { 
	private String actionName;
	private String actionCode;
	private boolean reasonMandatory = false;
	private String actionPreMsg;
	
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	public String getActionCode() {
		return actionCode;
	}
	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}
	public boolean isReasonMandatory() {
		return reasonMandatory;
	}
	public void setReasonMandatory(boolean reasonMandatory) {
		this.reasonMandatory = reasonMandatory;
	}
	public String getActionPreMsg() {
		return actionPreMsg;
	}
	public void setActionPreMsg(String actionPreMsg) {
		this.actionPreMsg = actionPreMsg;
	} 
 	
}
