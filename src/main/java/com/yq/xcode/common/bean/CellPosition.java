package com.yq.xcode.common.bean;

import java.util.Map;

public class CellPosition {
	// 用于定义按cell导入object的excel中，起始行列的定义

	private String colProperty = "";
	private String rowProperty = "";
	private String sheetProperty = "";
	private String cellProperty = "";
	private String headerStartCell = ""; // 属性域起始cell, 同一row的都是列属性 colProperty
	private String rowStartCell = ""; // 属性域开始cell，同一col的都是行属性 rowProperty
	private String cellValueStart = ""; // 数据域起始cell
	private Class classz;
	private String valueType = "";
	private boolean skipCellValueIsZero = false;
	private boolean saveComment = false;
	private String cellCommentProperty;

	private boolean colValueCheck = false;
	private Map<String, String> colValueMap;
	private boolean rowValueCheck = false;
	private Map<String, String> rowValueMap;
	private boolean sheetValueCheck = false;
	private Map<String, String> sheetValueMap;

	public CellPosition(Class classz, String sheetProperty, String colProperty, String rowProperty, String cellProperty) {
		this.classz = classz;
		this.colProperty = colProperty;
		this.rowProperty = rowProperty;
		this.sheetProperty = sheetProperty;
		this.cellProperty = cellProperty;
	}

	public String getColProperty() {
		return colProperty;
	}

	public void setColProperty(String colProperty) {
		this.colProperty = colProperty;
	}

	public String getRowProperty() {
		return rowProperty;
	}

	public void setRowProperty(String rowProperty) {
		this.rowProperty = rowProperty;
	}

	public String getSheetProperty() {
		return sheetProperty;
	}

	public void setSheetProperty(String sheetProperty) {
		this.sheetProperty = sheetProperty;
	}

	public String getHeaderStartCell() {
		return headerStartCell;
	}

	public void setHeaderStartCell(String headerStartCell) {
		this.headerStartCell = headerStartCell;
	}

	public String getRowStartCell() {
		return rowStartCell;
	}

	public void setRowStartCell(String rowStartCell) {
		this.rowStartCell = rowStartCell;
	}

	public String getCellValueStart() {
		return cellValueStart;
	}

	public void setCellValueStart(String cellValueStart) {
		this.cellValueStart = cellValueStart;
	}

	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	public String getCellProperty() {
		return cellProperty;
	}

	public void setCellProperty(String cellProperty) {
		this.cellProperty = cellProperty;
	}

	public boolean isSkipCellValueIsZero() {
		return skipCellValueIsZero;
	}

	public void setSkipCellValueIsZero(boolean skipCellValueIsZero) {
		this.skipCellValueIsZero = skipCellValueIsZero;
	}

	public boolean isSaveComment() {
		return saveComment;
	}

	public void setSaveComment(boolean saveComment) {
		this.saveComment = saveComment;
	}

	public String getCellCommentProperty() {
		return cellCommentProperty;
	}

	public void setCellCommentProperty(String cellCommentProperty) {
		this.cellCommentProperty = cellCommentProperty;
	}

	public Class getClassz() {
		return classz;
	}

	public void setClassz(Class classz) {
		this.classz = classz;
	}

	public boolean isColValueCheck() {
		return colValueCheck;
	}

	public void setColValueCheck(boolean colValueCheck) {
		this.colValueCheck = colValueCheck;
	}

	public boolean isRowValueCheck() {
		return rowValueCheck;
	}

	public void setRowValueCheck(boolean rowValueCheck) {
		this.rowValueCheck = rowValueCheck;
	}

	public boolean isSheetValueCheck() {
		return sheetValueCheck;
	}

	public void setSheetValueCheck(boolean sheetValueCheck) {
		this.sheetValueCheck = sheetValueCheck;
	}

	public Map<String, String> getColValueMap() {
		return colValueMap;
	}

	public void setColValueMap(Map<String, String> colValueMap) {
		this.colValueMap = colValueMap;
	}

	public Map<String, String> getRowValueMap() {
		return rowValueMap;
	}

	public void setRowValueMap(Map<String, String> rowValueMap) {
		this.rowValueMap = rowValueMap;
	}

	public Map<String, String> getSheetValueMap() {
		return sheetValueMap;
	}

	public void setSheetValueMap(Map<String, String> sheetValueMap) {
		this.sheetValueMap = sheetValueMap;
	}
 

}
