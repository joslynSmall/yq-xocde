package com.yq.xcode.common.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.yq.xcode.common.audit.ChangedProperty;



public class AuditLog implements Serializable{
	
	private String id;
	private String username;
	private String action;
	private Date actionTime;
	private String remark;
	
	private String entityName;
	private String entityId;
	
	private long transactionNo;
	
	private List<ChangedProperty> changedProperties;
	private List<AuditLogItem> items;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Date getActionTime() {
		return actionTime;
	}

	public void setActionTime(Date actionTime) {
		this.actionTime = actionTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public long getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(long transactionNo) {
		this.transactionNo = transactionNo;
	}

	public List<ChangedProperty> getChangedProperties() {
		return changedProperties;
	}

	public void setChangedProperties(List<ChangedProperty> changedProperties) {
		this.changedProperties = changedProperties;
	}
	
	
	
	public List<AuditLogItem> getItems() {
		return items;
	}

	public void setItems(List<AuditLogItem> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		StringBuffer buffer =  new StringBuffer("AuditLog[");
		buffer.append("id=").append(id)
				.append(", entityName=").append(entityName)
				.append(", entityId=").append(entityId)
				.append(", username=").append(username)
				.append(", action=").append(action)
				.append(", actionTime=").append(actionTime)
				.append(", remark=").append(remark)
				.append(", transactionNo=").append(transactionNo)
				.append(", changedProperties=").append(changedProperties)
				.append(", items=").append(items)
				.append("]");
			
		return buffer.toString();
	}
	
	
}
