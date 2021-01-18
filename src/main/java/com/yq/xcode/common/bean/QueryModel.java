package com.yq.xcode.common.bean;

import java.io.Serializable;

public class QueryModel  implements Serializable {
	String query;
	Object[] parameters;
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public Object[] getParameters() {
		return parameters;
	}
	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}
}
