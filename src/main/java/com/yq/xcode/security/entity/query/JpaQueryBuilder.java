package com.yq.xcode.security.entity.query;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class JpaQueryBuilder {

	private StringBuffer queryBuffer = new StringBuffer();
	
	private List<Object> parameters = new ArrayList<Object>();
	
	private boolean whereStarted = false;
	
	private boolean ignoreAnd;
	
	public JpaQueryBuilder(String oprSegment,String entityName) {
		if(oprSegment != null) {
			queryBuffer.append(oprSegment);
		}
		queryBuffer.append(" from ").append(entityName);
	}
	
	public JpaQueryBuilder(String rawQuerySegment) {
		if(rawQuerySegment != null) {
			queryBuffer.append(rawQuerySegment);
		}
	}
	
	public void appendLiteral(String sqlSegment) {
		queryBuffer.append(sqlSegment);
	}
	
	public void appendLiteral(String sqlSegment,Object param) {
		queryBuffer.append(sqlSegment);
		this.addParameter(param);
	}
	
	public void append(String sqlSegment) {
		queryBuffer.append(whereStarted?(ignoreAnd?" ":" and "):" where ").append(sqlSegment);
		whereStarted = true;
	}
	
	public void append(String sqlSegment,boolean ignoreAnd0) {
		queryBuffer.append(whereStarted?((ignoreAnd||ignoreAnd0)?" ":" and "):" where ").append(sqlSegment);
		whereStarted = true;
	}
	
	
	public void append(String sqlSegment,Object parameter) {
		 append(sqlSegment,parameter,false);
	}
	
	@SuppressWarnings("rawtypes")
	public void append(String sqlSegment,Object parameter,boolean ignoreAnd0) {
		queryBuffer.append(whereStarted?(ignoreAnd||ignoreAnd0?" ":" and "):" where ").append(sqlSegment);
		whereStarted = true;
		if(parameter == null) {
			parameters.add(null);
		}else if(parameter.getClass().isArray()) {
			for(int i = 0;i < Array.getLength(parameter);i++) {
				parameters.add(Array.get(parameter, i));
			}
		}else if(parameter instanceof Collection) {
			for(Object p : ((Collection)parameter)) {
				parameters.add(p);
			}
		}else {
			parameters.add(parameter);
		}
	}
	
	protected void addParameter(Object parameter) {
		if(parameter == null) {
			parameters.add(null);
		}else if(parameter.getClass().isArray()) {
			for(int i = 0;i < Array.getLength(parameter);i++) {
				parameters.add(Array.get(parameter, i));
			}
		}else if(parameter instanceof Collection) {
			for(Object p : ((Collection)parameter)) {
				parameters.add(p);
			}
		}else {
			parameters.add(parameter);
		}
	}
	
	public int getNextParameterIndex() {
		return parameters.size() + 1;
	}
	
	public Query createQuery(EntityManager entityManager) {
		Query query = entityManager.createQuery(queryBuffer.toString());
		for(int i = 0;i < parameters.size();i++) {
			query.setParameter(i+1, parameters.get(i));
		}
		return query;
	}
	
	public String getQueryText() {
		return queryBuffer.toString();
	}

	public boolean isIgnoreAnd() {
		return ignoreAnd;
	}

	public void setIgnoreAnd(boolean ignoreAnd) {
		this.ignoreAnd = ignoreAnd;
	}

	public List<Object> getParameters() {
		return parameters;
	}

	public boolean isWhereStarted() {
		return whereStarted;
	}

	public void setWhereStarted(boolean whereStarted) {
		this.whereStarted = whereStarted;
	}
	
	
	
	
}
