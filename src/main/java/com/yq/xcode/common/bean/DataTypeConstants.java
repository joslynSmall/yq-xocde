package com.yq.xcode.common.bean;

import java.util.HashMap;



public final class DataTypeConstants  {
	
	/**
	 * 字符比较类型
	 */
	public final static String COMPARE_TYPE_CHAR = "C";
	/**
	 * 日期比较
	 */
	public final static String COMPARE_TYPE_DATE = "D";
	
	/**
	 * BOOLEAN
	 */
	public final static String COMPARE_TYPE_BOOLEAN = "B";
	
	/**
	 * 数字
	 */
	public final static String COMPARE_TYPE_NUMBER = "N";
	
	/**
	 * 百分比
	 */
	public final static String COMPARE_TYPE_PERCENT = "PN";
	
	//期间值，为了propertyName唯一， 开始值的属性加'_LOW', 结束值加'_HIGH
	public final static String BETWEEN_FROM_SIGN="_LOW";
	public final static String BETWEEN_TO_SIGN="_HIGH";
	
	/**
	 * 参数占位符
	 */
	public final static String PLACE_HOLDER = "{parValue}";
 
	
 
	
 
	/**
	 * 逻辑运算符
	 */
	public final static SelectItem[] OPERATION_RELATION = new SelectItem[]{
		new SelectItem("LIKE","模糊匹配(like)"),
		new SelectItem("INLIKE","智能匹配"),
		new SelectItem("=","等于(=)"),
		new SelectItem(">","大于(>)"),
		new SelectItem(">=","大于等于(>=)"),
		new SelectItem("<","小于(<)"),
		new SelectItem("<=","小于等于(<=)"),
		new SelectItem("in","字符包含(in)"),
		new SelectItem("NIN","数字包含")
	};
	
	private final static HashMap<String,String> ITEMPAGE_PLACEHOLSER_MAP = new HashMap<String,String>() {
		{
		//	put("CHAIN","店铺号 店铺名称 ");
		}
	};
	public final static String getPlaceHolderByName(String name){
		return ITEMPAGE_PLACEHOLSER_MAP.get(name);
	}
 
}
