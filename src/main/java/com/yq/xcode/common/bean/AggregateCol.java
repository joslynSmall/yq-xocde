package com.yq.xcode.common.bean;

import com.yq.xcode.common.base.XBaseView;
import com.yq.xcode.common.utils.CommonUtil;



public class AggregateCol  extends XBaseView{
	
	
	/**
	 * 属性名称
	 */
	private String property;
	/**
	 * 数据库列
	 */
	private String col;
	/**
	 * 统计类型
	 */
	private String category; 
	/**
	 * 平均
	 */
	public static final String CATEGORY_AVERAGE = "average";
	/**
	 * 最大值
	 */
	public static final String CATEGORY_MAX = "max";
	/**
	 * 最小值
	 */
	public static final String CATEGORY_MIN = "min";
	/**
	 * 合计
	 */
	public static final String CATEGORY_SUM = "sum";
	
	/**
	 * 合计
	 */
	public static final String CATEGORY_COUNT = "count";
	
	private AggregateCol() {
		
	}
	public AggregateCol(String property, String col, String category ) {
		this.property = property;
		this.col = col;
		this.category = category;
	}
	
	
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getCol() {
		return col;
	}
	public void setCol(String col) {
		this.col = col;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	/**
	 * 个数 count, 现在默认有，现在不要
	 */
	//public static final String CATEGORY_COUNT = "count";
	
	public String genSelectCol() {
		if (CommonUtil.isNull(this.property)) {
			return "";
		}
		return " ," +this.category+"("+this.getCol()+") \""+this.getPropertyAndCategory()+"\"";
	}
	
	public String genGroupCol() {
		if (CommonUtil.isNull(this.property)) {
			return "";
		}
		return " ," +this.category+"("+this.getCol()+") \""+this.property+"\"";
	}
	
	public String getPropertyAndCategory() {
		return this.property+"."+this.category;
	}
	
}
