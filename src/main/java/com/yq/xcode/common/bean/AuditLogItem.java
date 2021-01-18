package com.yq.xcode.common.bean;

import java.io.Serializable;
import java.util.List;

import com.yq.xcode.common.audit.ChangedProperty;

public class AuditLogItem implements Serializable{


	private String entityName;
	private String entityId;
	private String action;
	private String remark;
	private List<ChangedProperty> changedProperties;


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

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<ChangedProperty> getChangedProperties() {
		return changedProperties;
	}

	public void setChangedProperties(List<ChangedProperty> changedProperties) {
		this.changedProperties = changedProperties;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer =  new StringBuffer("AuditLogItem[");
//		buffer.append("id=").append(id)
		buffer.append("entityName=").append(entityName)
				.append(", entityId=").append(entityId)
				.append(", action=").append(action)
				.append(", remark=").append(remark)
				.append(", changedProperties=").append(changedProperties)
				.append("]");
			
		return buffer.toString();
	}
}
