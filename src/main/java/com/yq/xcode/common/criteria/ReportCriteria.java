package com.yq.xcode.common.criteria;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yq.xcode.common.springdata.HPageCriteria;


/**
 * report view 中定义SQL, 包括查询条件
 * 
 * @author jt
 * 
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ReportCriteria extends HPageCriteria {
	private String groupCol;
	private String groupValue;
	private String sumCol;
	 
	
	private Long reportId;


	public Long getReportId() {
		return reportId;
	}


	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}


	public String getGroupCol() {
		return groupCol;
	}


	public void setGroupCol(String groupCol) {
		this.groupCol = groupCol;
	}


	public String getGroupValue() {
		return groupValue;
	}


	public void setGroupValue(String groupValue) {
		this.groupValue = groupValue;
	}


	public String getSumCol() {
		return sumCol;
	}


	public void setSumCol(String sumCol) {
		this.sumCol = sumCol;
	}
	
	
	
	


}
