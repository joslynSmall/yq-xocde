package com.yq.xcode.security.entity.query;


public class OrCriterion implements Criterion{

	private Criterion[] criterionArray;
	
	private boolean ignoreAnd = false;
	
	public OrCriterion(Criterion...criterions) {
		criterionArray = criterions;
	}
	public void appendToJpaQuery(JpaQueryBuilder builder) {
		if(criterionArray == null || criterionArray.length == 0) {
			return;
		}
		boolean found = false;
		boolean oldIgnoreAnd = builder.isIgnoreAnd();
		for(int i = 0; i < criterionArray.length;i++) {
			if(criterionArray[i] != null) {
				if(found) {
					builder.appendLiteral(" or ");
					criterionArray[i].appendToJpaQuery(builder);
				}else {
					found = true;
					builder.append("(",ignoreAnd);
					builder.setIgnoreAnd(true);
					criterionArray[i].appendToJpaQuery(builder);
					
				}
			}
		}
		if(found) {
			builder.appendLiteral(")");
		}
		builder.setIgnoreAnd(oldIgnoreAnd);
	}
	
	
	@Override
	public void appendToSqlQuery(JpaQueryBuilder builder) {
		appendToJpaQuery(builder);
	}
	public boolean isIgnoreAnd() {
		return ignoreAnd;
	}
	public void setIgnoreAnd(boolean ignoreAnd) {
		this.ignoreAnd = ignoreAnd;
	}

	
}
