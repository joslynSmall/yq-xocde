package com.yq.xcode.common.springdata;

import java.util.Iterator;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import com.yq.xcode.common.bean.AggregateCol;
import com.yq.xcode.common.bean.QueryModel;
import com.yq.xcode.common.bean.SumCol;
import com.yq.xcode.common.criteria.NativeCriteria;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.JPAUtils;


public class AggregateView {
	private String field;
	private String aggregate;
	
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getAggregate() {
		return aggregate;
	}
	public void setAggregate(String aggregate) {
		this.aggregate = aggregate;
	} 
	
	
}
