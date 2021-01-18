package com.yq.xcode.common.bean;

import com.yq.xcode.common.base.XBaseView;

public class WorkFlowActionReason  extends XBaseView { 
	/**
	 * ACTION_REASON 操作原因
	 */
	private String reasonType;


	/**
	 * ACTION_REASON 操作原因
	 */
	private String actionReason;


	public String getReasonType() {
		return reasonType;
	}


	public void setReasonType(String reasonType) {
		this.reasonType = reasonType;
	}


	public String getActionReason() {
		return actionReason;
	}


	public void setActionReason(String actionReason) {
		this.actionReason = actionReason;
	}
}
