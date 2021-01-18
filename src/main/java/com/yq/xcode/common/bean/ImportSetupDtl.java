package com.yq.xcode.common.bean;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yq.xcode.common.model.YqJpaBaseModel;
import com.yq.xcode.web.ui.annotation.ColumnLable;
import com.yq.xcode.web.ui.annotation.EditCol;
import com.yq.xcode.web.ui.annotation.EntityDesc;
import com.yq.xcode.web.ui.annotation.GridCol;

@SuppressWarnings("serial")
@Entity
@Table(name = "YQ_IMPORT_SETUP_DTL")

@JsonIgnoreProperties(ignoreUnknown = true)
@EntityDesc(name = "", autoGenPage = true, editCols = 3, genService = true, genAction = true, genCriteria = true)
public class ImportSetupDtl extends YqJpaBaseModel {

	@TableGenerator(name = "idGen", table = "ID_GENERATOR", pkColumnName = "ID_KEY", valueColumnName = "ID_VALUE", pkColumnValue = "ACT_IMPORT_SETUP_DTL_ID", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idGen")
	@Column(name = "ID")
	private Long id;
	/**
	 * IMPORT_SETUP_ID int comment '主表ID'
	 * 
	 */
	@ColumnLable(name = "主表ID")
	@Column(name = "IMPORT_SETUP_ID")
	private Long importSetupId;

	@ColumnLable(name = "属性")
	@Column(name = "property")
	@GridCol(lineNum = 1,isHyperlink=true)
	@EditCol(lineNum = 1)
	private String property;
	
	@ColumnLable(name = "属性名称")
	@Column(name = "property_name")
	@GridCol(lineNum = 5)
	@EditCol(lineNum = 5)
	private String propertyName;
	
	
	/**
	 * EXCEL_COL VARCHAR(8) comment 'EXCEL 列' 用字母， 比如 A, B, 下拉选择
	 * listCategory = "importsetupdtl_excelcol"
	 */
	@ColumnLable(name = "EXCEL列")
	@Column(name = "EXCEL_COL")
	@GridCol(lineNum = 10)
	@EditCol(lineNum = 10 )
	private String excelCol; 
	
	@ColumnLable(name = "EXCEL列标题")
	@Column(name = "EXCEL_COL_NAME")
	@GridCol(lineNum = 10)
	@EditCol(lineNum = 10 )
	private String excelColName; 

	@ColumnLable(name = "转换函数")
	@Column(name = "FUNTION")
	@GridCol(lineNum = 15)
	@EditCol(lineNum = 15 )
	private String funtion;
	@Transient
	private String funtionDsp;
 
 	@ColumnLable(name = "必须输入")
	@Column(name = "mandatory")
 	@GridCol(lineNum = 20)
	@EditCol(lineNum = 20 )
	private Boolean mandatory;
	
	@ColumnLable(name = "数据来源")
	@Column(name = "map_Category")
	private String mapCategory;
	
	@ColumnLable(name = "描述")
	@Column(name = "DESCRIPTION")
	@GridCol(lineNum = 25)
	@EditCol(lineNum = 25)
	private String description;
	
	/**
	 * 代码名称的可以直接定义好对应关系， 导入是可以直接转换为key值
	 */
	@Transient
	private Map<String,String> nameKeys;
	
 	 
 	public Map<String, String> getNameKeys() {
		return nameKeys;
	}

	public void setNameKeys(Map<String, String> nameKeys) {
		this.nameKeys = nameKeys;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getImportSetupId() {
		return importSetupId;
	}

	public void setImportSetupId(Long importSetupId) {
		this.importSetupId = importSetupId;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getExcelCol() {
		return excelCol;
	}

	public void setExcelCol(String excelCol) {
		this.excelCol = excelCol;
	}

 

	public String getFuntion() {
		return funtion;
	}

	public void setFuntion(String funtion) {
		this.funtion = funtion;
	}

	public String getFuntionDsp() {
		return funtionDsp;
	}

	public void setFuntionDsp(String funtionDsp) {
		this.funtionDsp = funtionDsp;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getMandatory() {
		return mandatory;
	}

	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}

	public String getMapCategory() {
		return mapCategory;
	}

	public void setMapCategory(String mapCategory) {
		this.mapCategory = mapCategory;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getExcelColName() {
		return excelColName;
	}

	public void setExcelColName(String excelColName) {
		this.excelColName = excelColName;
	}
	
	
	
	
	
	

}