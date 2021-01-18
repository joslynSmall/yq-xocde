package com.yq.xcode.common.springdata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.yq.xcode.common.base.YqPageImpl;

public class AggregatePageImpl<T> extends YqPageImpl<T> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4236162487118868685L;
	
	private Map<String, Map<String,Double>> aggregates;
	
	public AggregatePageImpl(List<T> content, Pageable pageable,
			long total) {
		super(content, pageable, total);
	}
	
	public void addAggregate(String field, String name, Double value){
		if(aggregates == null)
			aggregates = new HashMap<String, Map<String,Double>>();
		
		Map<String,Double> fieldAggregateMap = aggregates.get(field);
		
		if(fieldAggregateMap == null){
			fieldAggregateMap = new HashMap<String, Double>();
			aggregates.put(field, fieldAggregateMap);
		}
	
		fieldAggregateMap.put(name, value);
	}

	public Map<String, Map<String, Double>> getAggregates() {
		return aggregates;
	}

	public void setAggregates(Map<String, Map<String, Double>> aggregates) {
		this.aggregates = aggregates;
	}

 
	 
}
