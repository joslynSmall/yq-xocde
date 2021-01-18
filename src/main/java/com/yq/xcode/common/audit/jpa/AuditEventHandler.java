package com.yq.xcode.common.audit.jpa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.context.ApplicationListener;

import com.yq.xcode.common.audit.AuditEvent;
import com.yq.xcode.common.audit.AuditableEntityMetaData;
import com.yq.xcode.common.audit.AuditableEntityMetaDataFactory;
import com.yq.xcode.common.audit.AuditablePropertyMetaData;
import com.yq.xcode.common.audit.ChangedProperty;
import com.yq.xcode.common.audit.jpa.service.AuditLogService;


public class AuditEventHandler implements ApplicationListener<AuditEvent>{

	private static Log LOG = LogFactory.getLog(AuditEventHandler.class);
	
	
	
	private AuditLogService auditLogService;
	private AuditEventSerializer auditEventSerializer = new JsonAuditEventSerializer();
	@Override
	public void onApplicationEvent(AuditEvent event) {
		if(LOG.isDebugEnabled()) {
			LOG.debug(Thread.currentThread().getName()+" "+event);
		}
		auditLogService.addAuditLog(toAuditLog(event));
	}
	
	public AuditLogPo toAuditLog(AuditEvent event) {
		AuditLogPo auditLog = new AuditLogPo();
		auditLog.setAction(event.getAction());
		auditLog.setActionTime(new Date(event.getTimestamp()));
		auditLog.setEntityId(String.valueOf(event.getEntityId()));
		auditLog.setEntityName(event.getEntityName());
		auditLog.setRemark(event.getRemark());
		auditLog.setUsername(event.getUsername());
		if(AuditEvent.ACTION_READ.equals(event.getAction())) {
			
		}else {
			auditLog.setChangedData(auditEventSerializer.toString(event));
		}
		return auditLog;
	}
	
	@SuppressWarnings("rawtypes")
	public static List<ChangedProperty> getChangedProperties(Object oldValue,Object newValue) {
		if(oldValue == null && newValue == null) {
			return null;
		}
		List<ChangedProperty> propList = new ArrayList<ChangedProperty>();
		AuditableEntityMetaData metaData = AuditableEntityMetaDataFactory.getDefault().get((oldValue==null?newValue:oldValue).getClass());
		for(Object o : metaData.getAuditableProperties()) {
			AuditablePropertyMetaData p = (AuditablePropertyMetaData)o;
			Object oldPropValue = null;
			Object newPropValue = null;
			if(oldValue != null) {
				oldPropValue = p.read(oldValue);
			}
			if(newValue != null) {
				newPropValue = p.read(newValue);
			}
			if((oldPropValue == null && newPropValue != null) || (oldPropValue != null && !oldPropValue.equals(newPropValue))) {
				propList.add(new ChangedProperty(p.getColumnName(),p.isHidden()?(oldPropValue==null?null:"*"):convertValue(oldPropValue),newPropValue==null?null:(p.isHidden()?(newPropValue==null?null:"*"):convertValue(newPropValue))));
			}
		}
		return propList;
	}
	
	@SuppressWarnings("rawtypes")
	public static List<ChangedProperty> getChangedProperties(Object oldValue,Object newValue,String[] propNames) {
		if(oldValue == null && newValue == null) {
			return null;
		}
		Set<String> nameSet = new HashSet<String>();
		if(propNames != null && propNames.length > 0) {
			for(String pname : propNames) {
				nameSet.add(pname);
			}
		}
		List<ChangedProperty> propList = new ArrayList<ChangedProperty>();
		AuditableEntityMetaData metaData = AuditableEntityMetaDataFactory.getDefault().get((oldValue==null?newValue:oldValue).getClass());
		for(Object o : metaData.getAuditableProperties()) {
			AuditablePropertyMetaData p = (AuditablePropertyMetaData)o;
			if(!nameSet.contains(p.getName())) {
				continue;
			}
			Object oldPropValue = null;
			Object newPropValue = null;
			if(oldValue != null) {
				oldPropValue = p.read(oldValue);
			}
			if(newValue != null) {
				newPropValue = p.read(newValue);
			}
			if((oldPropValue == null && newPropValue != null) || (oldPropValue != null && !oldPropValue.equals(newPropValue))) {
				propList.add(new ChangedProperty(p.getColumnName(),p.isHidden()?(oldPropValue==null?null:"*"):convertValue(oldPropValue),newPropValue==null?null:(p.isHidden()?(newPropValue==null?null:"*"):convertValue(newPropValue))));
			}
		}
		return propList;
	}
	
	
	protected static Object convertValue(Object value) {
		if(value == null) {
			return null;
		}
		if(value.getClass().isArray() || value instanceof Collection || value instanceof Map) {
			return value.toString();
		}
		return value;
	}
	
	@SuppressWarnings("rawtypes")
	protected List<ChangedProperty> getChangedProperties0(AuditableEntityMetaData entityMetaData,String prefix,Object oldValue,Object newValue) {
		if(oldValue == null && newValue == null) {
			return null;
		}
		List<ChangedProperty> propList = new ArrayList<ChangedProperty>();
		if(entityMetaData == null) {
			entityMetaData = AuditableEntityMetaDataFactory.getDefault().get((oldValue==null?newValue:oldValue).getClass());
		}
		BeanWrapper oldValueWrapper = null;
		BeanWrapper newValueWrapper = null;
		if(oldValue != null) {
			oldValueWrapper = new BeanWrapperImpl(oldValue);
		}
		if(newValue != null) {
			newValueWrapper = new BeanWrapperImpl(newValue);
		}
		
		for(Object o : entityMetaData.getAuditableProperties()) {
			
			AuditablePropertyMetaData p = (AuditablePropertyMetaData)o;
			Object oldPropValue = null;
			Object newPropValue = null;
			if(oldValueWrapper != null) {
				oldPropValue = empty2Null(oldValueWrapper.getPropertyValue(p.getName()));
			}
			if(newValueWrapper != null) {
				newPropValue = empty2Null(newValueWrapper.getPropertyValue(p.getName()));
			}
			if((oldPropValue == null && newPropValue != null) || (oldPropValue != null && !oldPropValue.equals(newPropValue))) {
				propList.add(new ChangedProperty(prefix+p.getName(),p.isHidden()?(oldPropValue==null?null:"*"):convertValue(oldPropValue),newPropValue==null?null:(p.isHidden()?(newPropValue==null?null:"*"):convertValue(newPropValue))));
			}
		}
		return propList;
	}
	
	private Object empty2Null(Object object) {
		if(object == null) {
			return null;
		}
		if(object instanceof String && ((String)object).trim().equals("")) {
			return null;
		}
		return object;
	}
	
	public AuditLogService getAuditLogService() {
		return auditLogService;
	}
	public void setAuditLogService(AuditLogService auditLogService) {
		this.auditLogService = auditLogService;
	}

	public AuditEventSerializer getAuditEventSerializer() {
		return auditEventSerializer;
	}

	public void setAuditEventSerializer(AuditEventSerializer auditEventSerializer) {
		this.auditEventSerializer = auditEventSerializer;
	}
	
	
	
}
