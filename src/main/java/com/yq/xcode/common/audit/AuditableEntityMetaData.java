package com.yq.xcode.common.audit;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import org.springframework.util.StringUtils;

import com.yq.xcode.common.audit.annotation.Auditable;
import com.yq.xcode.common.audit.annotation.IgnoreAudit;

public class AuditableEntityMetaData<T> {

	private Class<T> entityClass;
	
	private Set<AuditablePropertyMetaData> auditableProperties = new HashSet<AuditablePropertyMetaData>();
	
	public AuditableEntityMetaData(Class<T> entityClass) {
		this.entityClass = entityClass;
		parseEntity();
	}
	
	private void parseEntity() {
		for(Field field : entityClass.getDeclaredFields()) {
			if(Modifier.isStatic(field.getModifiers())) {
				continue;
			}
			IgnoreAudit ignore =  field.getAnnotation(IgnoreAudit.class);
			Auditable auditable =  field.getAnnotation(Auditable.class);
			if(ignore != null) {
				continue;
			}
			String name = field.getName();
			Method readMethod = null;
			Method writeMethod = null;
			try{
				if("boolean".equals(field.getType().getName()) || "java.lang.Boolean".equals(field.getType().getName())) {
					try{
						readMethod = entityClass.getMethod("is"+StringUtils.capitalize(name), null);
					}catch(NoSuchMethodException e) {}
				}
				if(readMethod == null) {
					readMethod = entityClass.getMethod("get"+StringUtils.capitalize(name), null);
				}
			}catch(NoSuchMethodException e) {
				throw new RuntimeException("Field["+name+"] of entity["+entityClass.getName()+"] has no read method.",e);
			}
			try{
				writeMethod = entityClass.getMethod("set"+StringUtils.capitalize(name), field.getType());
			}catch(NoSuchMethodException e) {
				throw new RuntimeException("Field["+name+"] of entity["+entityClass.getName()+"] has no write method.",e);
			}
			AuditablePropertyMetaData ef = new AuditablePropertyMetaData();
			ef.setType(field.getType());
			ef.setName(name);
			ef.setReadMethod(readMethod);
			ef.setWriteMethod(writeMethod);
			String columnName = name;
			if(auditable != null) {
				if(!auditable.name().equals("")) {
					columnName = auditable.name();
				}
				ef.setHidden(auditable.hidden());
			}
			ef.setColumnName(columnName);
			auditableProperties.add(ef);
		}
	}
	
	public Class getEntityClass() {
		return entityClass;
	}
	

	public Set<AuditablePropertyMetaData> getAuditableProperties() {
		return auditableProperties;
	}
	
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("MongoEntityMetaData[");
		buffer.append("entityClass=").append(entityClass.getName())
		.append(", auditableProperties=").append(auditableProperties)
		.append("]");
		return buffer.toString();
	}
}
