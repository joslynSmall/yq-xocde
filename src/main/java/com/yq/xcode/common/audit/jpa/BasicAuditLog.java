package com.yq.xcode.common.audit.jpa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BasicAuditLog {

	private String entityId;
	@Column(name="ENTITY_NAME",length=50)
	private String entityName;
	@Column(name="ACTION",length=20)
	private String action;
	@Column(name="ACTION_TIME")
	private Date actionTime;
	@Column(name="USER_NAME",length=50)
	private String username;
	
	@Column(name="CHANGED_DATA",length=1000)
	private String changedData;
	@Column(name="REMARK",length=500)
	private String remark;

	public Date getActionTime() {
		return actionTime;
	}

	public void setActionTime(Date actionTime) {
		this.actionTime = actionTime;
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

	public String getChangedData() {
		return changedData;
	}

	public void setChangedData(String changedData) {
		this.changedData = changedData;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
}
