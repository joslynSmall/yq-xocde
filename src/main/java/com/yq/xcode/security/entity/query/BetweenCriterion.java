package com.yq.xcode.security.entity.query;



public class BetweenCriterion implements Criterion{
	
	
	private String name;
	
	
	private Object value1;
	
	private Object value2;
	
	private boolean includeStartValue = true;
	
	private boolean includeEndValue = false;
	
	public BetweenCriterion(String name,Object value1,Object value2) {
		this.name = name;
		this.value1 = value1;
		this.value2 = value2;
	}
	
	public BetweenCriterion(String name,Object value1,Object value2,boolean includeStartValue,boolean includeEndValue) {
		this.name = name;
		this.value1 = value1;
		this.value2 = value2;
		this.includeStartValue = includeStartValue;
		this.includeEndValue = includeEndValue;
	}
	
	public String getName() {
		return name;
	}

	
	
	@Override
	public void appendToSqlQuery(JpaQueryBuilder builder) {
		appendToQuery(builder,false);
	}
	
	@Override
	public void appendToJpaQuery(JpaQueryBuilder builder) {
		appendToQuery(builder,true);
	}
	
	private void appendToQuery(JpaQueryBuilder builder,boolean appendParamIndex) {
		StringBuffer buffer = new StringBuffer("(");
		int nextId = builder.getNextParameterIndex();
		if(!CriterionUtils.isNullOrEmpty(value1)) {
			buffer.append(name).append(includeStartValue?">=":">").append("?");
			if(appendParamIndex){
				buffer.append(nextId);
			}
		}
		if(!CriterionUtils.isNullOrEmpty(value2)) {
			if(!CriterionUtils.isNullOrEmpty(value1)) {
				nextId++;
				buffer.append(" and ");
			}
			buffer.append(name).append(includeEndValue?"<=":"<").append("?");
			if(appendParamIndex){
				buffer.append(nextId);
			}
		}
		buffer.append(")");
		builder.append(buffer.toString(), CriterionUtils.isNullOrEmpty(value1)?value2:(CriterionUtils.isNullOrEmpty(value2)?value1:new Object[]{value1,value2}));
	}
}
