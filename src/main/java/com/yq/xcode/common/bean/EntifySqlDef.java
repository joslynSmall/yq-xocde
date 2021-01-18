package com.yq.xcode.common.bean;

import java.io.Serializable;

public class EntifySqlDef implements Serializable {
	private static final long serialVersionUID = 6241774097691343998L;
	
	private String colQuery;
	private String[][] colToProperty;
	private String dbCols;
	
	public String getDbCols() {
		return dbCols;
	}
	public void setDbCols(String dbCols) {
		this.dbCols = dbCols;
	}
	public String getColQuery() {
		return colQuery;
	}
	public void setColQuery(String colQuery) {
		this.colQuery = colQuery;
	}
	public String[][] getColToProperty() {
		return colToProperty;
	}
	public void setColToProperty(String[][] colToProperty) {
		this.colToProperty = colToProperty;
	}
	
	
}
