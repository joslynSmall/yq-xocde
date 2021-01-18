package com.yq.xcode.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.yq.xcode.web.ui.annotation.ColumnLable;
import com.yq.xcode.web.ui.annotation.EditCol;
import com.yq.xcode.web.ui.annotation.EntityDesc;
import com.yq.xcode.web.ui.annotation.GridCol;

/**
 * job 中间的日志信息
 * @author jettie
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "yq_job_log_dtl")

@EntityDesc(name = "", autoGenPage = true, editCols = 3, genService = true, genAction = true, genCriteria = true)
public class JobLogDtl extends YqJpaBaseModel {

	@TableGenerator(name = "idGen", table = "ID_GENERATOR", pkColumnName = "ID_KEY", valueColumnName = "ID_VALUE", pkColumnValue = "yq_job_log_dtl_id", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idGen")
	@GridCol(lineNum = 1) @Column(name = "ID")
	private Long id;

 
	@ColumnLable(name = "主表ID")
	@GridCol(lineNum = 1) 
	@Column(name = "job_log_id")
	private Long jobLogId;
	 
	/**
	 * 如果是失败， 保存exception信息
	 */
	@ColumnLable(name = "是失败")
	@GridCol(lineNum = 1) 
	@Column(name = "is_exception_info")
	@EditCol(lineNum = 2)
	private Boolean isExceptionInfo;
	
	@ColumnLable(name = "描述")
	@GridCol(lineNum = 1) 
	@Column(name = "description")
	@EditCol(lineNum = 2)
	private String description;
	
	


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


  	public Long getJobLogId() {
		return jobLogId;
	}

 	public void setJobLogId(Long jobLogId) {
		this.jobLogId = jobLogId;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Boolean getIsExceptionInfo() {
		return isExceptionInfo;
	}


	public void setIsExceptionInfo(Boolean isExceptionInfo) {
		this.isExceptionInfo = isExceptionInfo;
	}
	
	 
}