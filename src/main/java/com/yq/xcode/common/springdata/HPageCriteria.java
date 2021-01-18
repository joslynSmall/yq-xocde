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
public class HPageCriteria extends NativeCriteria{
	
	private static final long serialVersionUID = 6829837718288682587L;
	private int total = 0; 
	private Map<String, Map<String,Double>> aggregates;
	
	private String columnsJsonStr;
	 
	
	private   String aggregateJsonStr;
	private   int pageNumber = 0;
	private   int pageSize = 10;
 
	/**
	 * key property
	 * value asc, desc
	 * sortMap{property:desc, property2: asc}
	 */
	private   String sortMapStr ;
	
 

	public int getTotal() {
		return total;
	}
  
 
	public String getAggregateJsonStr() {
		return aggregateJsonStr;
	}


	public void setAggregateJsonStr(String aggregateJsonStr) {
		this.aggregateJsonStr = aggregateJsonStr;
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

 
	
	public String getSortMapStr() {
		return sortMapStr;
	}


	public void setSortMapStr(String sortMapStr) {
		this.sortMapStr = sortMapStr;
	}


	public Sort genSort() {
		List<Order> orderList = new ArrayList<Order>();
		if (CommonUtil.isNotNull(sortMapStr)) {
			Map<String, String> sortMap = null ;
			try {
				sortMap = YqJsonUtil.readJson2Entity(sortMapStr, HashMap.class);
			} catch ( Exception e) { 
				e.printStackTrace();
				throw new ValidateException("排序参数格式转换错误！");
			} 
			for (String key :  sortMap.keySet()) {
				if ("ASC".equals( sortMap.get(key).toUpperCase())) {
					orderList.add(Order.asc(key));
				} else if ("DESC".equals( sortMap.get(key).toUpperCase())) {
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

 

	public String getColumnsJsonStr() {
		return columnsJsonStr;
	}


	public void setColumnsJsonStr(String columnsJsonStr) {
		this.columnsJsonStr = columnsJsonStr;
	}
	
	
 
 
}
