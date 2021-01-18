package com.yq.xcode.security.entity.query;


public class SimpleQueryCriteria<T>  extends QueryCriteria {

	private T parameter;
	
	public SimpleQueryCriteria() {}
	
	public SimpleQueryCriteria(T parameter) {
		this.parameter = parameter;
	}

	public T getParameter() {
		return parameter;
	}

	public void setParameter(T parameter) {
		this.parameter = parameter;
	}
	
	
	

}
