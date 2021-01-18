package com.yq.xcode.common.audit.jpa;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yq.xcode.common.audit.AuditEvent;
import com.yq.xcode.common.audit.ChangedProperty;

public class JsonAuditEventSerializer implements AuditEventSerializer {
	
	private static Log LOG = LogFactory.getLog(JsonAuditEventSerializer.class);

	private ObjectMapper objectMapper = new ObjectMapper();
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
		try{
			return objectMapper.writeValueAsString( props);
		}catch(Exception e){
			LOG.error("",e);
			return null;
		}
	}

}
