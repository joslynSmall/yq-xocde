package com.yq.xcode.common.criteria;

import com.yq.xcode.annotation.ParameterLogic;
import com.yq.xcode.common.springdata.HPageCriteria;

@SuppressWarnings("serial")
public class WorkFlowDetailQueryCriteria extends HPageCriteria {

	@ParameterLogic(colName = "wfd.current_status", operation = "=")
	private String currentStatus;

	@ParameterLogic(colName = "wfd.role", operation = "=")
	private String role;

	@ParameterLogic(colName = "wfd.action", operation = "=")
	private String action;

	@ParameterLogic(colName = "wfd.work_Flow_Id", operation = "=")
	private Long workFlowId;

	@ParameterLogic(colName = "wfd.next_status", operation = "=")
	private String nextStatus;
	@ParameterLogic(colName = "wfd.id", operation = "=")
	private Long wid;

	public Long getWid() {
		return wid;
	}

	public void setWid(Long wid) {
		this.wid = wid;
	}
	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Long getWorkFlowId() {
		return workFlowId;
	}

	public void setWorkFlowId(Long workFlowId) {
		this.workFlowId = workFlowId;
	}

	public String getNextStatus() {
		return nextStatus;
	}

	public void setNextStatus(String nextStatus) {
		this.nextStatus = nextStatus;
	}

}