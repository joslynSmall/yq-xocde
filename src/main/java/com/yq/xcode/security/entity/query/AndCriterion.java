package com.yq.xcode.security.entity.query;


public class AndCriterion implements Criterion{

	private Criterion[] criterionArray;
	
	public AndCriterion(Criterion...criterions) {
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
					builder.appendLiteral(" and ");
					criterionArray[i].appendToJpaQuery(builder);
				}else {
					found = true;
					builder.append("(");
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
	public static void main(String[] args) {
		SimpleCriterion sc = new SimpleCriterion("a","=","123");
		OrCriterion oc = new OrCriterion(new SimpleCriterion("a","=","123"));
		AndCriterion ac = new AndCriterion(new SimpleCriterion("a","=","123"),
				new SimpleCriterion("b","=","123"));
		JpaQueryBuilder builder  = new JpaQueryBuilder(null,"A a");
		sc.appendToJpaQuery(builder);
		oc.appendToJpaQuery(builder);
		ac.appendToJpaQuery(builder);
		System.out.println(builder.getQueryText());
	}

}
