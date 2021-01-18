package com.yq.xcode.security.entity.query;

import java.util.ArrayList;
import java.util.List;

public class ParsedQueryCriteria extends QueryCriteria {
	
	private List<Criterion> crietrions = new ArrayList<Criterion>();
	
	public ParsedQueryCriteria add(Criterion criterion) {
		if(criterion!= null) {
			crietrions.add(criterion);
		}
		return this;
	}

	public List<Criterion> getCrietrions() {
		return crietrions;
	}
	
	
}
