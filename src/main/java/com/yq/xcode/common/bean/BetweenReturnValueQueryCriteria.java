package com.yq.xcode.common.bean;

import java.util.Date;

import com.yq.xcode.common.springdata.HPageCriteria;


public class BetweenReturnValueQueryCriteria extends HPageCriteria{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4988577560185804312L;
	
	private String keyWord;
	private Date startDate;
	private Date endDate;
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	
	
}
