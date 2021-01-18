package com.yq.xcode.security.entity.query;

import java.lang.reflect.Array;
import java.util.Collection;

public class InCriterion  implements Criterion{
	
	
	private String name;
	
	private String operator;
	
	private Object value;
	
	private boolean ignoreWhenNull = true;
	
	public InCriterion(String name,Object value) {
		this(name,"in",value,true);
	}
	
	
	public InCriterion(String name,String operator,Object value) {
		this.name = name;
		this.operator = operator;
		this.value = value;
	}
	
	public InCriterion(String name,String operator,Object value,boolean ignoreWhenNull) {
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
		appendToQuery(builder,true);
	}
	
	
		
	@Override
	public void appendToSqlQuery(JpaQueryBuilder builder) {
		appendToQuery(builder,false);
	}


	private void appendToQuery(JpaQueryBuilder builder,boolean appendParamIndex) {
		int parameterSize = 0;
		if(value != null) {
			if(value.getClass().isArray()) {
				parameterSize = Array.getLength(value);
			}else if(value instanceof Collection) {
				parameterSize = ((Collection)value).size();
			}
		}
		if(parameterSize == 0  && ignoreWhenNull) {
				return;
		}
		StringBuffer buffer = new StringBuffer(name).append(' ').append(operator).append(" (?");
		int nextIndex = builder.getNextParameterIndex();
		if(appendParamIndex) {
			buffer.append(nextIndex);
		}
		nextIndex++;
		if(parameterSize == 0) {
			builder.append(buffer.append(")").toString(), null);
		}else {
			for(int i = 1;i < parameterSize;i++) {
				buffer.append(",?");
				if(appendParamIndex) {
					buffer.append(nextIndex);
				}
				nextIndex++;
			}
			buffer.append(")");
			builder.append(buffer.toString(), value);
		}
	}
}

