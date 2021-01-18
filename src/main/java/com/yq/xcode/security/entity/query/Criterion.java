package com.yq.xcode.security.entity.query;


public interface Criterion {
	
	public void appendToJpaQuery(JpaQueryBuilder builder);
	
	public void appendToSqlQuery(JpaQueryBuilder builder);
}
