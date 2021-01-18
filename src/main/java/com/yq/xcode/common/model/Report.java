package com.yq.xcode.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import com.yq.xcode.common.criteria.ReportCriteria;
import com.yq.xcode.common.utils.JsUtil;
import com.yq.xcode.web.ui.annotation.ColumnLable;

@SuppressWarnings("serial")
@Entity
@Table(name = "YQ_REPORT")

public class Report extends YqJpaBaseModel {

	@TableGenerator(name = "idGen", table = "ID_GENERATOR", pkColumnName = "ID_KEY", valueColumnName = "ID_VALUE", pkColumnValue = "REPORT_ID", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idGen")
	@Column(name = "ID")
	private Long id;
	/**
	 * REPORT_CODE VARCHAR(40) comment '报表代码'
	 * 
	 */
	@ColumnLable(name = "报表代码")
	@Column(name = "REPORT_CODE")
	private String reportCode;
	/**
	 * REPORT_NAME VARCHAR(400) comment '报表名称'
	 * 
	 */
	@ColumnLable(name = "报表名称")
	@Column(name = "REPORT_NAME")
	private String reportName;
	/**
	 * REPORT_CATEGORY_CODE VARCHAR(40) comment '母报表代码'
	 * 
	 */
	@ColumnLable(name = "母报表代码")
	@Column(name = "REPORT_CATEGORY_CODE")
	private String reportCategoryCode;
	/**
	 * HIDDEN_COLUMNS VARCHAR(800) comment '隐藏自动' 用“，”分隔的字段
	 */
	@ColumnLable(name = "隐藏自动")
	@Column(name = "HIDDEN_COLUMNS")
	private String hiddenColumns;
	/**
	 * DESCRIPTION VARCHAR(400) comment '描述'
	 * 
	 */
	@ColumnLable(name = "描述")
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Transient
	private ReportCriteria reportCriteria;
	
	public ReportCriteria getReportCriteria() {
		return reportCriteria;
	}
	public void setReportCriteria(ReportCriteria reportCriteria) {
		this.reportCriteria = reportCriteria;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getReportCode() {
		return reportCode;
	}
	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public String getReportCategoryCode() {
		return reportCategoryCode;
	}
	public void setReportCategoryCode(String reportCategoryCode) {
		this.reportCategoryCode = reportCategoryCode;
	}
	public String getHiddenColumns() {
		return hiddenColumns;
	}
	public void setHiddenColumns(String hiddenColumns) {
		this.hiddenColumns = hiddenColumns;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Transient
	public String getShortPinyin() {
		return JsUtil.getPinYinHeadChar(this.reportName);
	}
	public void setShortPinyin(String shortPinyin) {

	}

}