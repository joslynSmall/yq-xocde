package com.yq.xcode.common.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import com.yq.xcode.web.ui.annotation.ColumnLable;
import com.yq.xcode.web.ui.annotation.EditCol;
import com.yq.xcode.web.ui.annotation.EntityDesc;
import com.yq.xcode.web.ui.annotation.GridCol;

@SuppressWarnings("serial")
@Entity
@Table(name = "yq_job_log")

@EntityDesc(name = "", autoGenPage = true, editCols = 3, genService = true, genAction = true, genCriteria = true)
public class JobLog extends YqJpaBaseModel {

	@TableGenerator(name = "idGen", table = "ID_GENERATOR", pkColumnName = "ID_KEY", valueColumnName = "ID_VALUE", pkColumnValue = "yq_job_log_ID", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idGen")
	@GridCol(lineNum = 1) @Column(name = "ID")
	private Long id;

 
	@ColumnLable(name = "job 名称")
	@GridCol(lineNum = 1) 
	@Column(name = "job_name")
	private String jobName;
	
	
	@ColumnLable(name = "显示名称")
	@GridCol(lineNum = 1) 
	@Column(name = "job_display")
	private String jobDisplay;
 
	@ColumnLable(name = "提交时间")
	@GridCol(lineNum = 1) 
	@Column(name = "submit_time")
	@EditCol(lineNum = 2)
	private Date submitTime;
	
	@ColumnLable(name = "开始时间")
	@GridCol(lineNum = 1) 
	@Column(name = "run_start_time")
	@EditCol(lineNum = 2)
	private Date runStartTime;
	
	@ColumnLable(name = "结束时间")
	@GridCol(lineNum = 1) 
	@Column(name = "run_end_time")
	@EditCol(lineNum = 2)
	private Date runEndTime;
	
	/**
	 * 1 - 成功
	 * 2 - 失败
	 * 3 - 执行中
	 */
	@ColumnLable(name = "状态")
	@GridCol(lineNum = 1) 
	@Column(name = "run_status")
	@EditCol(lineNum = 2)
	private String runStatus;
	
	@ColumnLable(name = "描述")
	@GridCol(lineNum = 1) 
	@Column(name = "description")
	@EditCol(lineNum = 2)
	private String description;
	
 
	
	@Transient
	private String runStatusDsp;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

 
	public Date getRunStartTime() {
		return runStartTime;
	}

	public void setRunStartTime(Date runStartTime) {
		this.runStartTime = runStartTime;
	}

	public Date getRunEndTime() {
		return runEndTime;
	}

	public void setRunEndTime(Date runEndTime) {
		this.runEndTime = runEndTime;
	}

	public String getRunStatus() {
		return runStatus;
	}

	public void setRunStatus(String runStatus) {
		this.runStatus = runStatus;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobDisplay() {
		return jobDisplay;
	}

	public void setJobDisplay(String jobDisplay) {
		this.jobDisplay = jobDisplay;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	

	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}

	public String getRunStatusDsp() {
		return runStatusDsp;
	}

	public void setRunStatusDsp(String runStatusDsp) {
		this.runStatusDsp = runStatusDsp;
	}

  
	 
}