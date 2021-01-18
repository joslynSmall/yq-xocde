package com.yq.xcode.common.model;

import java.util.List;

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
import com.yq.xcode.web.ui.annotation.CriteriaCol;
import com.yq.xcode.web.ui.annotation.Detail;
import com.yq.xcode.web.ui.annotation.EditCol;
import com.yq.xcode.web.ui.annotation.EntityDesc;
import com.yq.xcode.web.ui.annotation.GridCol;

@SuppressWarnings("serial")
@Entity
@Table(name = "YQ_REPORT_DEFINE")

@EntityDesc(name="基础资料 >> 报表定义", autoGenPage=true, editCols = 3, genService=false, genAction=false, genCriteria=false) 
public class ReportDefine extends YqJpaBaseModel {

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
	@Column(name = "CODE")
	@CriteriaCol 	@EditCol(lineNum=1,mandatory=true ) @GridCol(isHyperlink=true) 
	private String code;
	/**
	 * REPORT_NAME VARCHAR(400) comment '报表名称'
	 * 
	 */
	@CriteriaCol 	@EditCol(lineNum=3,mandatory=true ) @GridCol 
	@ColumnLable(name = "报表名称")
	@Column(name = "NAME")
	private String name;
	
	/**
	 * 要是 from xxxx a where 1=1 , 一定包括了条件
	 */ 
	@ColumnLable(name = "表")
	@Column(name = "FROM_TABLE")
	private String fromTable;

	 @EditCol(lineNum=7  )  
	@ColumnLable(name = "报表排序")
	@Column(name = "SORT_BY")
	private String sortBy ;

	@EditCol(lineNum=9  )  
	@ColumnLable(name = "分组字段")
	@Column(name = "GROUP_BY")
	private String groupBy ;
	
	
	/**
	 * DESCRIPTION VARCHAR(400) comment '描述'
	 * 
	 */ 
	@ColumnLable(name = "描述")
	@Column(name = "DESCRIPTION")
	private String description;
	
	@EditCol(lineNum=9  )  
	@ColumnLable(name = "图表类型")
	@Column(name = "GRAPH_TYPE")
	private String graphType;
	
	@Detail(masterProperty="reportDefineId")
	@Transient
	private List<ReportColumnDefine> columns ;
	
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
 
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFromTable() {
		return fromTable;
	}
	public void setFromTable(String fromTable) {
		this.fromTable = fromTable;
	}
	public String getSortBy() {
		return sortBy;
	}
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
	public String getGroupBy() {
		return groupBy;
	}
	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}
	public List<ReportColumnDefine> getColumns() {
		return columns;
	}
	public void setColumns(List<ReportColumnDefine> columns) {
		this.columns = columns;
	}
	public String getGraphType() {
		return graphType;
	}
	public void setGraphType(String graphType) {
		this.graphType = graphType;
	}
 
}