package com.yq.xcode.security.entity.query;

import java.io.Serializable;
import java.math.BigDecimal;

public class SumCol implements Serializable{
	
	private String name;
	private String title;
	private BigDecimal value;
	
	public SumCol(String name, String title) {
		this.name = name;
		this.title = title;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}

	
}
