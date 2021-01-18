package com.yq.xcode.security.entity.query;

import java.util.HashMap;
import java.util.Map;

public class MappedQueryCriteria extends QueryCriteria {

	private Map<String,String[]> parameters = new HashMap<String,String[]>();

	public Map<String, String[]> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String[]> parameters) {
		this.parameters = parameters;
	}
	
	public String getParameter(String key) {
		String[] values = parameters.get(key);
		if(values == null || values.length == 0) {
			return null;
		}
		return values[0];
	}
	
	public void setParameter(String key,String value) {
		if(value != null) {
			parameters.put(key, new String[]{value});
		}
	}
	
	public Integer getIntegerParameter(String key) {
		String value = getParameter(key);
		return value==null?null:Integer.parseInt(value);
	}
}
