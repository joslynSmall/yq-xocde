package com.yq.xcode.security.entity.query;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class JpaCriteria {
	
	public String entityName;
	
	private String alias;
	
	private List<Criterion> crietrionList = new ArrayList<Criterion>();
	
	private String sortBy;
	
	private boolean whereStarted = false;
	
	public JpaCriteria(String entityName) {
		this.entityName = entityName;
	}
	
	public JpaCriteria(Class clazz) {
		this.entityName = clazz.getName();
	}
	
	public JpaCriteria(Class clazz,String alias) {
		this.entityName = clazz.getName();
		this.alias = alias;
	}
	
	public JpaCriteria add(Criterion criterion) {
		if(criterion!= null) {
			crietrionList.add(criterion);
		}
		return this;
	}

	public String getEntityName() {
		return entityName;
	}

	public List<Criterion> getCrietrionList() {
		return crietrionList;
	}
	
	public Query createQueryByBuilder(JpaQueryBuilder builder, EntityManager entityManager,boolean appendOrderBy){
		builder.setWhereStarted(whereStarted);
		for(Criterion c : crietrionList) {
			c.appendToJpaQuery(builder);
		}
		if(appendOrderBy && sortBy != null && !sortBy.trim().equals("")) {
			builder.appendLiteral(" order by " + sortBy);
		}
		return builder.createQuery(entityManager);
	}
	
	public Query createQuery(String entityName, String oprSegment,EntityManager entityManager){
		return createQuery(entityName, oprSegment, entityManager,true);
	}
	
	public Query createQuery(String entityName, String selectSegment, EntityManager entityManager,boolean appendOrderBy){
		JpaQueryBuilder builder = new JpaQueryBuilder(selectSegment,entityName+(alias==null?"":(" "+alias)));
		builder.setWhereStarted(whereStarted);
		for(Criterion c : crietrionList) {
			c.appendToJpaQuery(builder);
		}
		if(appendOrderBy && sortBy != null && !sortBy.trim().equals("")) {
			builder.appendLiteral(" order by " + sortBy);
		}
		return builder.createQuery(entityManager);
	}
	
	public String createQueryString(String entityName, String selectSegment, boolean appendOrderBy){
		JpaQueryBuilder builder = new JpaQueryBuilder(selectSegment,entityName+(alias==null?"":(" "+alias)));
		builder.setWhereStarted(whereStarted);
		for(Criterion c : crietrionList) {
			c.appendToJpaQuery(builder);
		}
		if(appendOrderBy && sortBy != null && !sortBy.trim().equals("")) {
			builder.appendLiteral(" order by " + sortBy);
		}
		return builder.getQueryText();
	}
	
	public Query createQuery(String oprSegment,EntityManager entityManager) {
		return createQuery(this.entityName, oprSegment, entityManager,true);
	}
	
	public Query createQuery(String selectSegment,EntityManager entityManager,boolean appendOrderBy) {
		return createQuery(this.entityName, selectSegment, entityManager,appendOrderBy);
	}
	
	public String getQueryString(String oprSegment,boolean appendOrderBy) {
		JpaQueryBuilder builder = new JpaQueryBuilder(oprSegment,entityName+(alias==null?"":(" "+alias)));
		builder.setWhereStarted(whereStarted);
		for(Criterion c : crietrionList) {
			c.appendToJpaQuery(builder);
		}
		if(appendOrderBy && sortBy != null && !sortBy.trim().equals("")) {
			builder.appendLiteral(" order by " + sortBy);
		}
		return builder.getQueryText();
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public boolean isWhereStarted() {
		return whereStarted;
	}

	public void setWhereStarted(boolean whereStarted) {
		this.whereStarted = whereStarted;
	}
	
	
}
