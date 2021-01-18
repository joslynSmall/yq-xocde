package com.yq.xcode.security.entity.query;

public class LiteralCriterion implements Criterion{

	private String sqlSegment;
	
	private Object parameters;
	
	
	private boolean ignoreAnd = false;
	
	public LiteralCriterion(String sqlSegment) {
		this.sqlSegment = sqlSegment;
	}
	
	public LiteralCriterion(String sqlSegment,boolean ignoreAnd) {
		this.sqlSegment = sqlSegment;
		this.ignoreAnd = ignoreAnd;
	}
	public LiteralCriterion(String sqlSegment,Object parameters,boolean ignoreAnd) {
		this.sqlSegment = sqlSegment;
		this.parameters = parameters;
		this.ignoreAnd = ignoreAnd;
	}
	public void appendToJpaQuery(JpaQueryBuilder builder) {
		
		if(parameters != null) {
			builder.append(sqlSegment, parameters,ignoreAnd);
		}else {
			builder.append(sqlSegment,ignoreAnd);
		}
	}
	
	
	@Override
	public void appendToSqlQuery(JpaQueryBuilder builder) {
		if(parameters != null) {
			builder.append(sqlSegment, parameters,ignoreAnd);
		}else {
			builder.append(sqlSegment,ignoreAnd);
		}
	}

	public boolean isIgnoreAnd() {
		return ignoreAnd;
	}
	public void setIgnoreAnd(boolean ignoreAnd) {
		this.ignoreAnd = ignoreAnd;
	}
	
	

}
