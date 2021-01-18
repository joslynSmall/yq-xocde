package com.yq.xcode.common.springdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yq.xcode.common.criteria.NativeCriteria;
import com.yq.xcode.common.exception.ValidateException;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.YqJsonUtil;

@JsonIgnoreProperties(ignoreUnknown=true)
public class HTestPageCriteria extends NativeCriteria{
	
	private static final long serialVersionUID = 6829837718288682587L;
	private int total = 0; 
	private Map<String, Map<String,Double>> aggregates;
	
	private   Map<String, String> aggregate;
	/**
	 * json str
	 */
	private   String aggregateStr;
	private   int pageNumber = 0;
	private   int pageSize = 10;
	/**
	 * key property
	 * value asc, desc
	 * sortMap{property:desc, property2: asc}
	 */
	private   Map<String, String> sortMap ;
	
 

	public int getTotal() {
		return total;
	}
  
	public Map<String, String> getAggregate() {
		return aggregate;
	}

	public void setAggregate(Map<String, String> aggregate) {
		this.aggregate = aggregate;
	}

	 

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public Map<String, String> getSortMap() {
		return sortMap;
	}

	public void setSortMap(Map<String, String> sortMap) {
		this.sortMap = sortMap;
	}
	
	public Sort genSort() {
		List<Order> orderList = new ArrayList<Order>();
		if (CommonUtil.isNotNull(this.sortMap)) {
			for (String key : this.sortMap.keySet()) {
				if ("ASC".equals(this.sortMap.get(key).toUpperCase())) {
					orderList.add(Order.asc(key));
				} else if ("DESC".equals(this.sortMap.get(key).toUpperCase())) {
					orderList.add(Order.desc(key));
				} else {
					throw new ValidateException("不存在的排序方式!");
				} 
			}
		}
		return Sort.by(orderList);
	}

	public Map<String, Map<String, Double>> getAggregates() {
		return aggregates;
	}

	public void setAggregates(Map<String, Map<String, Double>> aggregates) {
		this.aggregates = aggregates;
	}

	public String getAggregateStr() {
		return aggregateStr;
	}

	public void setAggregateStr(String aggregateStr) {
		
		this.aggregateStr = aggregateStr;
		if (CommonUtil.isNull(aggregateStr)) {
			aggregate = new HashMap<String,String>();
		} else {
			try {
				Map obj = YqJsonUtil.readJson2Entity(aggregateStr, HashMap.class);
				this.aggregate = obj;
			} catch ( Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new ValidateException("json 格式错误 "+aggregateStr) ;
			}  
		}
	}
	
	
	
	

 
}
