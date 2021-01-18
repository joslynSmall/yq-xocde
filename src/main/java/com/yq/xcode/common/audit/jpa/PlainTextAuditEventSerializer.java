package com.yq.xcode.common.audit.jpa;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.yq.xcode.common.audit.AuditEvent;
import com.yq.xcode.common.audit.ChangedProperty;


public class PlainTextAuditEventSerializer implements AuditEventSerializer {
	
	@Override
	public String toString(AuditEvent event) {
		List<ChangedProperty> props = null;
		if(event.getOldEntity() != null || event.getNewEntity() != null) {
			props = AuditEventHandler.getChangedProperties(event.getOldEntity(),event.getNewEntity());
		}else if(event.getChangedProperties() != null && !event.getChangedProperties().isEmpty()) {
			props = event.getChangedProperties();
		}
		if(props == null || props.isEmpty()) {
			return null;
		}
		StringBuffer buf = new StringBuffer();
		for(ChangedProperty p : props) {
			buf.append("[")
				.append(p.getName())
				.append(":")
				.append(valueToString(p.getOldValue()))
				.append("->")
				.append(valueToString(p.getNewValue()))
			.append("],");
		}
		buf.deleteCharAt(buf.length()-1);
		return buf.toString();
	}
	
	private static String valueToString(Object value) {
		if(value == null) {
			return null;
		}
		if(value instanceof String) {
			return "\""+value+"\"";
		}else if(value instanceof Number ) {
			if(value instanceof BigDecimal) {
				return String.valueOf(((BigDecimal)value).doubleValue());
			}
			return value.toString();
		}else if(value instanceof Date) {
			return "\""+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date)value)+"\"";
		}else  {
			return value.toString();
		}
		
	}
	
	

}
