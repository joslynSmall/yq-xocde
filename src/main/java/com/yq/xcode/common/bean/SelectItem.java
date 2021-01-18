package com.yq.xcode.common.bean;

import java.util.HashMap;
import java.util.Map;

import com.yq.xcode.common.base.XBaseView;
import com.yq.xcode.common.utils.JsUtil;


public class SelectItem extends XBaseView  implements Comparable {
	private static final long serialVersionUID = -5288719823113782238L;
	private String itemKey;
	private String itemName;
	private String itemValue;
	private String remark;
	private Map pageMap = new HashMap();
	
	/**
	 * 备用字段
	 */
	private String column1;
	private String column2;
	private String column3;
	private String column4;
	
	

	public SelectItem() {
		
	}
    public SelectItem(String itemKey,String itemName) {
		this.itemKey = itemKey;
		this.itemName = itemName;
	}
    public SelectItem(String itemKey,String itemName,String itemValue,String remark) {
		this.itemKey = itemKey;
		this.itemName = itemName;
		this.itemValue = itemValue;
		this.remark = remark;
	}
    
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getItemKey() {
		return itemKey;
	}
	public void setItemKey(String itemKey) {
		this.itemKey = itemKey;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getShortPinyin() {
		return JsUtil.getPinYinHeadChar(this.itemName);
	}
	public void setShortPinyin(String shortPinyin) {

	}
	public String getColumn1() {
		return column1;
	}
	public void setColumn1(String column1) {
		this.column1 = column1;
	}
	public String getColumn2() {
		return column2;
	}
	public void setColumn2(String column2) {
		this.column2 = column2;
	}
	public String getItemValue() {
		return itemValue;
	}
	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}
	@Override
	public int compareTo(Object o) {
		SelectItem si = (SelectItem)o;
		return this.getShortPinyin().compareTo(si.getShortPinyin());
	}
	public Map getPageMap() {
		return pageMap;
	}
	public void setPageMap(Map pageMap) {
		this.pageMap = pageMap;
	}
	public String getColumn3() {
		return column3;
	}
	public void setColumn3(String column3) {
		this.column3 = column3;
	}
	public String getColumn4() {
		return column4;
	}
	public void setColumn4(String column4) {
		this.column4 = column4;
	}
	
	 

}
