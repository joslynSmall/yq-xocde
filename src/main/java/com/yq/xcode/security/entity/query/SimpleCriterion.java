package com.yq.xcode.security.entity.query;



public class SimpleCriterion implements Criterion{
	
	
	private String name;
	
	private String operator;
	
	private Object value;
	
	private boolean ignoreWhenNull = true;
	
	public SimpleCriterion(String name,String operator,Object value) {
		this.name = name;
		this.operator = operator;
		this.value = value;
	}
	
	public SimpleCriterion(String name,String operator,Object value,boolean ignoreWhenNull) {
		this.name = name;
		this.operator = operator;
		this.value = value;
		this.ignoreWhenNull = ignoreWhenNull;
	}
	
	public String getName() {
		return name;
	}

	public String getOperator() {
		return operator;
	}

	public Object getValue() {
		return value;
	}
	
	public void appendToJpaQuery(JpaQueryBuilder builder) {
		if(ignoreWhenNull && CriterionUtils.isNullOrEmpty(value)) {
			return;
		}
		builder.append(new StringBuffer(name).append(' ').append(operator).append(' ').append("?").append(builder.getNextParameterIndex()).toString(), value);
	}

	@Override
	public void appendToSqlQuery(JpaQueryBuilder builder) {
		if(ignoreWhenNull && CriterionUtils.isNullOrEmpty(value)) {
			return;
		}
		builder.append(new StringBuffer(name).append(' ').append(operator).append(' ').append("?").toString(), value);
	}
	
	
}
