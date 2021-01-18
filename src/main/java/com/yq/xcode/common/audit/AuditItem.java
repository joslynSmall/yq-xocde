package com.yq.xcode.common.audit;


public class AuditItem {

	private String id;
	private String action;
	private String entityName;
	private String remark;
	private Object oldEntity;
	private Object newEntity;
	
	public AuditItem() {}
	
	public AuditItem(String id,String action,String remark,Object oldEntity,Object newEntity) {
		this.id = id;
		this.action = action;
		this.remark = remark;
		this.oldEntity = oldEntity;
		this.newEntity = newEntity;
		if(oldEntity != null) {
			entityName = oldEntity.getClass().getSimpleName();
		}
		if(entityName == null && newEntity != null) {
			entityName = newEntity.getClass().getSimpleName();
		}
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAction() {
		return action;
	}
	public String getRemark() {
		return remark;
	}
	public Object getOldEntity() {
		return oldEntity;
	}
	public Object getNewEntity() {
		return newEntity;
	}
	
	
	
	public void setAction(String action) {
		this.action = action;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setOldEntity(Object oldEntity) {
		this.oldEntity = oldEntity;
	}

	public void setNewEntity(Object newEntity) {
		this.newEntity = newEntity;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	@Override
	public String toString() {
		StringBuffer buffer =  new StringBuffer("AuditItem[");
		buffer.append("action=").append(action)
			.append(", entityName=").append(entityName)
			.append(", remark=").append(remark)
			.append(", oldEntity=").append(oldEntity)
			.append(", newEntity=").append(newEntity)
			.append("]");
		return buffer.toString();
	}
	
	
}
