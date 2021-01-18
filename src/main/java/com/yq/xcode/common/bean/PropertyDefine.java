package com.yq.xcode.common.bean;

import java.io.Serializable;


public class PropertyDefine implements Serializable, Comparable{

	private static final long serialVersionUID = -5469629620355457476L;
	/*
	 * 数据库表的Column
	 */
	private String column;
	
	private boolean loinColumn = false;
	/*
	 * 模型属性名
	 */
	private String property;
	/**
	 * 显示的属性数据类型
	 */
	private String dataType;
	
	/**
	 * 显示控件
	 */
	private String tagKey;
	/**
	 * 显示的属性名称
	 */
	private String lable;
	private  int length;
	private int textAreaRows = 3;
	
	/**
	 * 
	 * 取数类型, 如果输入这个默认就是jobdownlist
	 * @return
	 * 
	 */	
	private String listCategory;
	//可以用表达式
	private String value;
	
	/**
	 * 以下这部分为查询特有的属性
	 * @return
	 */
	private String operator; //逻辑运算符，LIKE - 包含，  = - 等于 ... 直接写逻辑运算符
 
	/**
	 * 参数值的预留处理,例如前匹配可以写concat(DataTypeConstants.PLACE_HOLDER,'%')
	 */
	private String placeHolder; //
	
	
	//可以用表达式
	private String toValue;	
    
	//可以用表达式
	private boolean hyperlink = false;
	
	private String width;
	/**
	 * 只读字段， 一般自动产生的字段用只读
	 */
	private boolean disabled = false;
	
	/**
	 * 附件显示方式, 是否显示图
	 * @return
	 */
	private boolean showpic = false;
	
	/**
	 * 提示信息, 附件
	 * @return
	 */
	private String attachMsg;
	
	
 	
	public boolean isShowpic() {
		return showpic;
	}
	public void setShowpic(boolean showpic) {
		this.showpic = showpic;
	}
	public String getAttachMsg() {
		return attachMsg;
	}
	public void setAttachMsg(String attachMsg) {
		this.attachMsg = attachMsg;
	}
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getListCategory() {
		return listCategory;
	}
	public void setListCategory(String listCategory) {
		this.listCategory = listCategory;
	}
	
	public boolean isMandatory() {
		return mandatory;
	}
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}
 
	public boolean isHyperlink() {
		return hyperlink;
	}
	public void setHyperlink(boolean hyperlink) {
		this.hyperlink = hyperlink;
	}

	/**
	 * 是否hidden字段
	 * @return
	 */
	private boolean hidden = false;
	
	 private boolean mandatory = false;
	 
 
	 
	 
	/**
	 * 显示顺序
	 */
	private Integer lineNum;
	
 
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getLable() {
		return lable;
	}
	public void setLable(String lable) {
		this.lable = lable;
	}
 
	public boolean isHidden() {
		return hidden;
	}
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	public Integer getLineNum() {
		return lineNum;
	}
	public void setLineNum(Integer lineNum) {
		this.lineNum = lineNum;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public boolean isLoinColumn() {
		return loinColumn;
	}
	public void setLoinColumn(boolean loinColumn) {
		this.loinColumn = loinColumn;
	}
 
	public String getValue() {
		return value;
	}
	public String getTagKey() {
		return tagKey;
	}
	public void setTagKey(String tagKey) {
		this.tagKey = tagKey;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getPlaceHolder() {
		return placeHolder;
	}
	public void setPlaceHolder(String placeHolder) {
		this.placeHolder = placeHolder;
	}
	public String getToValue() {
		return toValue;
	}
	public void setToValue(String toValue) {
		this.toValue = toValue;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public int getTextAreaRows() {
		return textAreaRows;
	}
	public void setTextAreaRows(int textAreaRows) {
		this.textAreaRows = textAreaRows;
	}
	public int compareTo(Object o) {
		PropertyDefine pd = (PropertyDefine)o;
		if (pd.getLineNum() == null || this.lineNum == null) {
			return 0;
		}
		return this.lineNum - pd.getLineNum();
	}
	
	
	

}
