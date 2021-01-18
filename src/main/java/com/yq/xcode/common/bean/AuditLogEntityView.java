package com.yq.xcode.common.bean;

import com.yq.xcode.common.base.XBaseView;

@SuppressWarnings("serial")
public class AuditLogEntityView  extends XBaseView{
	private String entityCode;
	private String entityName;
	private Class clazz;
	public AuditLogEntityView() {
		
	}
	public AuditLogEntityView(String entityCode, String entityName, Class clazz) {
		this.entityCode = entityCode;
		this.entityName = entityName;
		this.clazz = clazz;
	}
	public String getEntityCode() {
		return entityCode;
	}
	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public Class getClazz() {
		return clazz;
	}
	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}

}
