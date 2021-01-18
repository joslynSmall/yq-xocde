package com.yq.xcode.common.springdata;

import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
 
public class HPageRequest extends PageRequest{
	
	private static final long serialVersionUID = 6829837718288682587L;
	private int total = 0;
	private Map<String, Map<String,Double>> aggregates;
	
	private String aggregateJsonStr;
 
	public HPageRequest(int page, int size) {
		super(page, size, null);
	}
	
	public HPageRequest(int page, int size, Direction direction, String... properties) {

		super(page, size, new Sort(direction, properties));
	}
	
	public HPageRequest(int page, int size, Sort sort) {
		super(page, size, sort);
	}
	
	public HPageRequest(int page, int size, Sort sort,int total) {
		super(page, size, sort);
		
		this.total = total;
	}

	public int getTotal() {
		return total;
	}
	
	public Map<String, Map<String,Double>> getAggregates() {
		return aggregates;
	}

	public void setAggregates(Map<String, Map<String, Double>> aggregates) {
		this.aggregates = aggregates;
	}

 
	public String getAggregateJsonStr() {
		return aggregateJsonStr;
	}

	public void setAggregateJsonStr(String aggregateJsonStr) {
		this.aggregateJsonStr = aggregateJsonStr;
	}

	public HPageRequest(HPageCriteria pageCriteria) {
        this(pageCriteria.getPageNumber(), pageCriteria.getPageSize() ,pageCriteria.genSort());
  		this.total = pageCriteria.getTotal();
		this.aggregateJsonStr = pageCriteria.getAggregateJsonStr(); 
		this.aggregates = pageCriteria.getAggregates();
		
	}
	
	
}
