package com.yq.xcode.common.audit.jpa;

import java.io.Serializable;
import java.util.Date;

public class AuditLogQuery implements Serializable{
	
	private static final long serialVersionUID = -5974321588189630754L;
	private String entityName;
	private String entityId;
	private String username;
	private String action;
	private Date fromTime;
	private Date toTime;
	
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
	public Date getFromTime() {
		return fromTime;
	}
	public void setFromTime(Date fromTime) {
		this.fromTime = fromTime;
	}
	public Date getToTime() {
		return toTime;
	}
	public void setToTime(Date toTime) {
		this.toTime = toTime;
	}
	
	
}
