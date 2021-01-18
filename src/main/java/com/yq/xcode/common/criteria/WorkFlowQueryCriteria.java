package com.yq.xcode.common.criteria;

import java.util.List;

import com.yq.xcode.common.springdata.HPageCriteria;

@SuppressWarnings("serial")
public  class   WorkFlowQueryCriteria   extends HPageCriteria{  
	
	 private String workFlowName;
	 private String workFlowNumber;
	 private String currentStatus;
	 private String role;
	 private String action;
	 private Long workFlowId; 
	 private String nextStatus;
	 private String description;
	 private String modifyStatus;
	 private String methodCategory;
	 private String executeMethodCode;
	 private String addedRelationCode;
	 private String sysWorkKeyCode;
	 private Boolean using;//是否正在使用
	 private String status;
	 private String keyword;
	 private String categoryCode;
	 
	 private String resource;
	 
	 private List<Long> ids;
	 
	 public List<Long> getIds() {
		return ids;
	}
	public void setIds(List<Long> ids) {
		this.ids = ids;
	}
	public String getExecuteMethodCode() {
		return executeMethodCode;
	}
	public void setExecuteMethodCode(String executeMethodCode) {
		this.executeMethodCode = executeMethodCode;
	}
	public String getAddedRelationCode() {
		return addedRelationCode;
	}
	public void setAddedRelationCode(String addedRelationCode) {
		this.addedRelationCode = addedRelationCode;
	}
	public String getMethodCategory() {
		return methodCategory;
	}
	public void setMethodCategory(String methodCategory) {
		this.methodCategory = methodCategory;
	}
	public String getWorkFlowNumber() {
		return workFlowNumber;
	}
	public void setWorkFlowNumber(String workFlowNumber) {
		this.workFlowNumber = workFlowNumber;
	}
	public String getWorkFlowName() {
		return workFlowName;
	}
	public void setWorkFlowName(String workFlowName) {
		this.workFlowName = workFlowName;
	}
	public WorkFlowQueryCriteria(){
		 super();
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getModifyStatus() {
		return modifyStatus;
	}
	public void setModifyStatus(String modifyStatus) {
		this.modifyStatus = modifyStatus;
	}
	public String getSysWorkKeyCode() {
		return sysWorkKeyCode;
	}
	public void setSysWorkKeyCode(String sysWorkKeyCode) {
		this.sysWorkKeyCode = sysWorkKeyCode;
	}
	public Boolean getUsing() {
		return using;
	}
	public void setUsing(Boolean using) {
		this.using = using;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getKeyword() {
		return keyword.trim();
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	
	 
}