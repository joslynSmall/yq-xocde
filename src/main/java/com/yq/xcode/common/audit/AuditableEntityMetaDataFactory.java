package com.yq.xcode.common.audit;

import java.util.HashMap;
import java.util.Map;

public class AuditableEntityMetaDataFactory {

	private Map<Class,AuditableEntityMetaData> cache = new HashMap<Class,AuditableEntityMetaData>();
	
	public static AuditableEntityMetaDataFactory instance = new AuditableEntityMetaDataFactory();
	
	public static AuditableEntityMetaDataFactory getDefault() {
		return instance;
		
	}
	public <T> AuditableEntityMetaData<T> get(Class<T> entityClass) {
		AuditableEntityMetaData<T> metaData = cache.get(entityClass);
		if(metaData == null) {
			metaData = new AuditableEntityMetaData(entityClass);
		}
		cache.put(entityClass, metaData);
		return metaData;
	}
	
	
	
}
