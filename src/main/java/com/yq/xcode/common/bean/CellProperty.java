package com.yq.xcode.common.bean;

import java.io.Serializable;
import java.text.Format;
import java.util.List;
import java.util.Map;

import com.yq.xcode.common.utils.ExcelUtil;

public class CellProperty implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String lable;
	/**
	 * 用字符
	 */
	private String col;	
	/**
	 * 数字的列
	 */
	private int intCol;
	private String property;
	private String propertyType;
	private Boolean mandatory;
	/**
	 * cell 空的默认值
	 */
	private String defaultValue; 
	
	/**
	 * 批注， 导出模板用
	 */
	private String comment;
	/**
	 * 对于属性是字符， 单要存储为日期格式的， 要这个字段
	 */
	private Boolean tryConvertSpecDate;
	private Format parseFormat;
	
	private List<String> duplicatCheckList;
	
	private String cellType;
	
	/**
	 * 代码名称的可以直接定义好对应关系， 导入是可以直接转换为key值
	 */
	private Map<String,String> nameKeys;
	
	/**
	 * 通过函数转换
	 */
	private String convertFun;
	
	
	public CellProperty() {
		
	}
	
    public CellProperty(String col,String property, String cellType) {
		this.col = col;
		this.property = property;
		this.cellType = cellType;
	}
    
	/**
	 * @param lable
	 * @param col
	 * @param property
	 * @param mandatory
	 * @param nameKeys
	 */
    public CellProperty(String lable,String col,String property,Boolean mandatory,Map<String,String> nameKeys) {
		this.lable = lable;
		this.col = col;
		this.property = property;
		this.mandatory = mandatory;
		this.nameKeys = nameKeys;
	}
    public CellProperty(String lable,String col,String property,Boolean mandatory,Map<String,String> nameKeys,Format parseFormat) {
		this.lable = lable;
		this.col = col;
		this.property = property;
		this.mandatory = mandatory;
		this.nameKeys = nameKeys;
		this.parseFormat = parseFormat;
	}
    
    /**
	 * @param lable
	 * @param col
	 * @param property
	 * @param mandatory
	 * @param nameKeys
	 * @param defaultValue
	 */
    public CellProperty(String lable,String col,String property,Boolean mandatory,Map<String,String> nameKeys,String defaultValue) {
		this.lable = lable;
		this.col = col;
		this.property = property;
		this.mandatory = mandatory;
		this.nameKeys = nameKeys;
		this.defaultValue = defaultValue;
	}
    
    public CellProperty(String lable,String col,String property,Boolean mandatory,Map<String,String> nameKeys,List<String> duplicatCheckList) {
    	this.lable = lable;
    	this.col = col;
    	this.property = property;
    	this.mandatory = mandatory;
    	this.nameKeys = nameKeys;
    	this.setDuplicatCheckList(duplicatCheckList);
    }
    
    public CellProperty(String lable,String col,String property,Boolean mandatory,Map<String,String> nameKeys, Boolean tryConvertSpecDate) {
    	this.lable = lable;
    	this.col = col;
    	this.property = property;
    	this.mandatory = mandatory;
    	this.nameKeys = nameKeys;
    	this.tryConvertSpecDate = tryConvertSpecDate;
    }
	public CellProperty(ImportSetupDtl importSetupDtl) {
		this.lable = importSetupDtl.getPropertyName();
    	this.col = importSetupDtl.getExcelCol();
    	this.intCol = ExcelUtil.getColByCh(col);
    	this.property = importSetupDtl.getProperty();
    	this.mandatory = importSetupDtl.getMandatory();
    	this.nameKeys = importSetupDtl.getNameKeys();
    	this.convertFun = importSetupDtl.getFuntion();
	}

	public String getLable() {
		return lable;
	}
	public void setLable(String lable) {
		this.lable = lable;
	}
	public String getCol() {
		return col;
	}
	public void setCol(String col) {
		this.col = col;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public Boolean getMandatory() {
		return mandatory;
	}
	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}
	public Map<String, String> getNameKeys() {
		return nameKeys;
	}
	public void setNameKeys(Map<String, String> nameKeys) {
		this.nameKeys = nameKeys;
	}
	public Boolean getTryConvertSpecDate() {
		return tryConvertSpecDate;
	}
	public void setTryConvertSpecDate(Boolean tryConvertSpecDate) {
		this.tryConvertSpecDate = tryConvertSpecDate;
	}
	public List<String> getDuplicatCheckList() {
		return duplicatCheckList;
	}
	public void setDuplicatCheckList(List<String> duplicatCheckList) {
		this.duplicatCheckList = duplicatCheckList;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Format getParseFormat() {
		return parseFormat;
	}
	public void setParseFormat(Format parseFormat) {
		this.parseFormat = parseFormat;
	}

	public String getCellType() {
		return cellType;
	}

	public void setCellType(String cellType) {
		this.cellType = cellType;
	}

	public String getConvertFun() {
		return convertFun;
	}

	public void setConvertFun(String convertFun) {
		this.convertFun = convertFun;
	}

	public int getIntCol() {
		return intCol;
	}

	public void setIntCol(int intCol) {
		this.intCol = intCol;
	}

	public String getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}
	
	
	
	 
 
}
