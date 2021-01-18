package com.yq.xcode.common.bean;

import org.springframework.web.bind.annotation.RequestParam;

public class WorkFlowActionParam {

	private Long entityId;
	private String entityCategory;
	private String action;
	private String actionReason;

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public String getEntityCategory() {
		return entityCategory;
	}

	public void setEntityCategory(String entityCategory) {
		this.entityCategory = entityCategory;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getActionReason() {
		return actionReason;
	}

	public void setActionReason(String actionReason) {
		this.actionReason = actionReason;
	}

}
