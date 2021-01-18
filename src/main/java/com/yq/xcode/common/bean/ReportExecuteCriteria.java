package com.yq.xcode.common.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.yq.xcode.security.entity.JpaBaseModel;
import com.yq.xcode.web.ui.annotation.ColumnLable;

@SuppressWarnings("serial")
@Entity
@Table(name = "YQ_REPORT_EXECUTE_CRITERIA")

public class ReportExecuteCriteria extends JpaBaseModel {

	@TableGenerator(name = "idGen", table = "ID_GENERATOR", pkColumnName = "ID_KEY", valueColumnName = "ID_VALUE", pkColumnValue = "REPORT_EXECUTE_CRITERIA_ID", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idGen")
	@Column(name = "ID")
	private Long id;
	/**
	 * REPORT_EXECUTE_LOG_ID int comment '执行的报表日志ID'
	 * 
	 */
	@ColumnLable(name = "执行的报表日志ID")
	@Column(name = "REPORT_EXECUTE_LOG_ID")
	private Long reportExecuteLogId;
	/**
	 * CRITERIA_OBJECT VARCHAR(8000) comment '参数对象' 保存查询序列化的对象
	 */
	@ColumnLable(name = "参数对象")
	@Column(name = "CRITERIA_OBJECT")
	private String criteriaObject;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getReportExecuteLogId() {
		return reportExecuteLogId;
	}

	public void setReportExecuteLogId(Long reportExecuteLogId) {
		this.reportExecuteLogId = reportExecuteLogId;
	}

	public String getCriteriaObject() {
		return criteriaObject;
	}

	public void setCriteriaObject(String criteriaObject) {
		this.criteriaObject = criteriaObject;
	}

}