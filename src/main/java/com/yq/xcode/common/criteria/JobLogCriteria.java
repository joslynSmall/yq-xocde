


 package com.yq.xcode.common.criteria;

import com.yq.xcode.annotation.ParameterLogic;
import com.yq.xcode.common.springdata.HPageCriteria;

public class JobLogCriteria extends HPageCriteria{

	@ParameterLogic(colName="CONCAT(a.job_name, '-', a.job_display) ", operation="like" , placeHolder="" )
	private String keyString;
	
	@ParameterLogic(colName=" a.run_start_time", operation=">=" , placeHolder="" )
	private String startTime;
	
	@ParameterLogic(colName=" a.run_end_time", operation="<=" , placeHolder="" )
	private String endTime;
	
	@ParameterLogic(colName=" a.run_status", operation="=" , placeHolder="" )
	private String runStatus;

	

	public String getKeyString() {
		return keyString;
	}

	public void setKeyString(String keyString) {
		this.keyString = keyString;
	}

	
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getRunStatus() {
		return runStatus;
	}

	public void setRunStatus(String runStatus) {
		this.runStatus = runStatus;
	}

	
	
}
