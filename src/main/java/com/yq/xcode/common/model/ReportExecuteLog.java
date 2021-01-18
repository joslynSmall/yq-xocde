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

import com.yq.xcode.common.criteria.ReportCriteria;
import com.yq.xcode.web.ui.annotation.ColumnLable;

@SuppressWarnings("serial")
@Entity
@Table(name = "YQ_REPORT_EXECUTE_LOG")

public class ReportExecuteLog extends YqJpaBaseModel {

	@TableGenerator(name = "idGen", table = "ID_GENERATOR", pkColumnName = "ID_KEY", valueColumnName = "ID_VALUE", pkColumnValue = "REPORT_EXECUTE_LOG_ID", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idGen")
	@Column(name = "ID")
	private Long id;
	/**
	 * REPORT_ID int comment '执行的报表ID'
	 * 
	 */
	@ColumnLable(name = "执行的报表ID")
	@Column(name = "REPORT_ID")
	private Long reportId;
	/**
	 * EXECUTE_STATUS VARCHAR(40) comment '执行状态' PENDING - 等待 DOING - 执行中
	 * COMPLETED - 完成 ERROR - 出错
	 */
	@ColumnLable(name = "执行状态")
	@Column(name = "EXECUTE_STATUS")
	private String executeStatus;
	/**
	 * COMPLETED_TIME datetime comment '完成时间'
	 * 
	 */
	@ColumnLable(name = "完成时间")
	@Column(name = "COMPLETED_TIME")
	private Date completedTime;
	/**
	 * START_TIME datetime comment '开始运行时间'
	 * 
	 */
	@ColumnLable(name = "开始运行时间")
	@Column(name = "START_TIME")
	private Date startTime;

	/**
	 * RESULT_FILE VARCHAR(100) comment '结果文件' 文件名称，如果出错，保存出错的日志
	 */
	@ColumnLable(name = "结果文件")
	@Column(name = "RESULT_FILE")
	private String resultFile;
	
	@Transient
	private String criteriaStr;
	@Transient
	private String reportCategory;
	@Transient
	private ReportCriteria reportCriteria;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getReportId() {
		return reportId;
	}
	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}
	public String getExecuteStatus() {
		return executeStatus;
	}
	public void setExecuteStatus(String executeStatus) {
		this.executeStatus = executeStatus;
	}
	
	@Transient
	public String getExecuteStatusDsp() {
		if ("COMPLETED".equals(executeStatus)) {
			return "完成";
		}
		if ("PENDING".equals(executeStatus)) {
			return "等待执行";
		}
		if ("DOING".equals(executeStatus)) {
			return "执行中";
		}
		return "执行错误";
	}
	public void setExecuteStatusDsp(){}
	
	public Date getCompletedTime() {
		return completedTime;
	}
	public void setCompletedTime(Date completedTime) {
		this.completedTime = completedTime;
	}
	public String getResultFile() {
		return resultFile;
	}
	public void setResultFile(String resultFile) {
		this.resultFile = resultFile;
	}
	/**
	 * EXECUTE_STATUS VARCHAR(40) comment '执行状态' PENDING - 等待 DOING - 执行中
	 * COMPLETED - 完成 ERROR - 出错
	 */
	@Transient
	public static final String EXECUTE_STATUS_PENDING = "PENDING";
	@Transient
	public static final String EXECUTE_STATUS_DOING = "DOING";
	@Transient
	public static final String EXECUTE_STATUS_COMPLETED = "COMPLETED";
	@Transient
	public static final String EXECUTE_STATUS_ERROR = "ERROR";
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public String getCriteriaStr() {
		return criteriaStr;
	}
	public void setCriteriaStr(String criteriaStr) {
		this.criteriaStr = criteriaStr;
	}
	public String getReportCategory() {
		return reportCategory;
	}
	public void setReportCategory(String reportCategory) {
		this.reportCategory = reportCategory;
	}
	public ReportCriteria getReportCriteria() {
		return reportCriteria;
	}
	public void setReportCriteria(ReportCriteria reportCriteria) {
		this.reportCriteria = reportCriteria;
	}


}