package com.yq.xcode.common.springdata;

public class HWorkFlowEntityPageCriteria extends HPageCriteria{
	
	private static final long serialVersionUID = 6829837718288682587L;
	
	/**
	 * 只有在流程类型列表里用， 查询记录数
	 */
	private boolean onlyCount = false;
	
	/**
	 * 指定用户执行， 也就是用指定用户的权限
	 */
	private String findDataAs;
	
	/**
	 * 被审批单的ID, 审批流程用
	 */
	private Long wfEntityId;
	
	private String stage;

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public boolean isOnlyCount() {
		return onlyCount;
	}

	public void setOnlyCount(boolean onlyCount) {
		this.onlyCount = onlyCount;
	}

	public String getFindDataAs() {
		return findDataAs;
	}

	public void setFindDataAs(String findDataAs) {
		this.findDataAs = findDataAs;
	}

	public Long getWfEntityId() {
		return wfEntityId;
	}

	public void setWfEntityId(Long wfEntityId) {
		this.wfEntityId = wfEntityId;
	} 
	
	
	 
}
