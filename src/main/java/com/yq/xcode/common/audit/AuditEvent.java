package com.yq.xcode.common.audit;

import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditEvent extends ApplicationEvent{
	public final static String ACTION_CREATE = "Create";
	public final static String ACTION_UPDATE = "Update";
	public final static String ACTION_READ = "Read";
	public final static String ACTION_DELETE = "Delete";

	private String username;
	private String action;
	private String remark;
	
	private String entityName;
	private String entityId;
	private Object oldEntity;
	private Object newEntity;
	
	private long transactionNo;
	
	private List<AuditItem> items;
	
	private List<ChangedProperty> changedProperties;
	
	public static AuditEvent userActionRemark(String username,String action,String remark) {
		AuditEvent event = new AuditEvent(username);
		event.setUsername(username);
		event.setAction(action);
		event.setRemark(remark);
		return event;
	}
	
	AuditEvent(Object source) {
		super(source);
	}
	
	public AuditEvent(String entityId,Object oldEntity,Object newEntity) {
		this(entityId,null,oldEntity,newEntity);
	}
	
	public AuditEvent(String entityId,String action,Object oldEntity,Object newEntity) {
		this(oldEntity==null?newEntity.getClass().getSimpleName():oldEntity.getClass().getSimpleName(),entityId,action,null,oldEntity,newEntity);
		
	}
	
	public AuditEvent(String entityName,String entityId,String action,List<ChangedProperty> changedProperties) {
		this(entityName, entityId, action, changedProperties, null,null);
	}
	
	public AuditEvent(String entityName,String entityId,String action,List<ChangedProperty> changedProperties,String remark) {
		this(entityName, entityId, action, changedProperties, remark,null);
	}
	
	public AuditEvent(String entityName,String entityId,String action,List<ChangedProperty> changedProperties,String remark,String username) {
		super(entityName);
		this.entityName = entityName;
		this.entityId = entityId;
		this.action = action;
		this.changedProperties = changedProperties;
		this.remark = remark;
		this.username = username==null?AuditEvent.currentUserName():username;
	}
	
	public AuditEvent(String entityName,String entityId,String action,String remark) {
		this(entityName,entityId,action,remark,null,null);
	}
	
	public AuditEvent(String entityName,String entityId,String action,String remark,List<AuditItem> items) {
		this(entityName,entityId,action,remark,null,null,items);
	}
	public AuditEvent(String entityName,String entityId,String action,String remark,Object oldEntity,Object newEntity) {
		super(entityName);
		this.entityName = entityName;
		this.entityId = entityId;
		this.action = action;
		if(action == null) {
			if(oldEntity == null && newEntity != null) {
				this.action = ACTION_CREATE;
			}else if(oldEntity != null && newEntity == null) {
				this.action = ACTION_DELETE;
			}else {
				this.action = ACTION_UPDATE;
			}
		}
		this.oldEntity = oldEntity;
		this.newEntity = newEntity;
		this.remark = remark;
		this.username = AuditEvent.currentUserName();
	}
	
	public AuditEvent(String entityName,String entityId,String action,String remark,Object oldEntity,Object newEntity,List<AuditItem> items) {
		super(entityName);
		this.entityName = entityName;
		this.entityId = entityId;
		this.action = action;
		this.oldEntity = oldEntity;
		this.newEntity = newEntity;
		this.remark = remark;
		this.username = AuditEvent.currentUserName();
		this.items = items;
	}
	

	public String getEntityId() {
		return entityId;
	}

	public String getEntityName() {
		return entityName;
	}

	public String getAction() {
		return action;
	}

	public String getUsername() {
		return username;
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
	
	public long getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(long transactionNo) {
		this.transactionNo = transactionNo;
	}

	public List<AuditItem> getItems() {
		return items;
	}
	
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public void setItems(List<AuditItem> items) {
		this.items = items;
	}
	
	

	public List<ChangedProperty> getChangedProperties() {
		return changedProperties;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("AuditEvent[");
		buffer.append("entityName=").append(entityName)
			.append(", action=").append(action)
			.append(", username=").append(username)
			.append(", actionTime=").append(new Date(getTimestamp()))
			.append(", oldEntity=").append(oldEntity)
			.append(", newEntity=").append(newEntity)
			.append(", items=").append(items)
			.append("]");
		return buffer.toString();
	}
	
	public static void addChangedProperty(List<ChangedProperty> props,String propName,Object oldValue,Object newValue) {
		if(oldValue == null && newValue == null) {
			return;
		}
		if(oldValue != null && oldValue.equals(newValue)) {
			return;
		}
		props.add(new ChangedProperty(propName,oldValue,newValue));
	}
	
	private static String currentUserName() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth==null?null:auth.getName();
	}
}
