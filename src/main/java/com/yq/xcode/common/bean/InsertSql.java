package com.yq.xcode.common.bean;

import java.util.ArrayList;
import java.util.List;

import com.yq.xcode.common.base.XBaseView;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.security.entity.JpaBaseModel;




public class InsertSql  extends XBaseView{
	
	private Class<? extends JpaBaseModel> clazz;
	private String cols;
	private List<String> valuesList;
	private int size = 0;
	private CellProperty[] cellPropertis;
	
	
	
	public CellProperty[] getCellPropertis() {
		return cellPropertis;
	}
	public void setCellPropertis(CellProperty[] cellPropertis) {
		this.cellPropertis = cellPropertis;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public Class<? extends JpaBaseModel> getClazz() {
		return clazz;
	}
	public void setClazz(Class<? extends JpaBaseModel> clazz) {
		this.clazz = clazz;
	}
	public String getCols() {
		return cols;
	}
	public void setCols(String cols) {
		this.cols = cols;
	}
	public List<String> getValuesList() {
		return valuesList;
	}
	public void setValuesList(List<String> valuesList) {
		this.valuesList = valuesList;
	}
	public void appendCol(String name) {
		if (CommonUtil.isNull(this.cols ) ) {
			this.cols = name; 
		} else {
			this.cols = this.cols+","+name; 
		}
		
	}
	public void appendValue(String values) {
		if (this.valuesList == null) {
			this.valuesList = new ArrayList<String>();
		}
		this.valuesList.add(values);
		
	}
	
	/**
	 * 会将原对象clear 掉
	 * @param cols
	 * @param values
	 */
	public void batchAddColAndValues(String cols, String values) {
		this.appendCol(cols);
		if (CommonUtil.isNotNull(this.valuesList)) {
			List<String> vList = new ArrayList<String>();
			for (String v : valuesList) {
				vList.add(v+","+values);
 			}
			this.valuesList.clear();
			this.valuesList = vList;
		}
		
	}
	public void clearValue() {
		if (valuesList != null) {
			valuesList.clear();
		} 
	}
	
 
 
}
