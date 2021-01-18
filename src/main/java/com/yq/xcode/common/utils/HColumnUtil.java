package com.yq.xcode.common.utils;

import java.util.List;

import com.yq.xcode.common.bean.HColumn;
import com.yq.xcode.common.bean.SelectItem;

public class HColumnUtil {

	 
	/**
	 * 列定义
	 * @param field
	 * @param title
	 * @param attributes
	 * @param template
	 * @return
	 */
	public static HColumn constructor(String field, String title,String width, String attributes, String template){
		HColumn hColumn = new HColumn();
		hColumn.setField(field);
		hColumn.setTitle(title);
		hColumn.setAttributes(attributes);
		hColumn.setTemplate(template);
		hColumn.setWidth(width);
		return hColumn;
	}
	
	public static HColumn textColumn(String field, String title,String width, String template){
		HColumn hColumn = new HColumn();
		hColumn.setField(field);
		hColumn.setTitle(title); 
		hColumn.setTemplate(template);
		hColumn.setWidth(width);
		return hColumn;
	}
	
	public static HColumn dateColumn(String field, String title,String width ){
		HColumn hColumn = new HColumn();
		hColumn.setField(field);
		hColumn.setTitle(title); 
		hColumn.setAttributes("{style:\"text-align:center\"}");
		hColumn.setTemplate("#:hfive.yqformatDate("+field+")#");  
		hColumn.setWidth(width); 
		return hColumn;
	}
	
	public static HColumn dateTimeColumn(String field, String title,String width ){
		HColumn hColumn = new HColumn();
		hColumn.setField(field);
		hColumn.setTitle(title); 
		hColumn.setAttributes("{style:\"text-align:center\"}");
		hColumn.setTemplate("#:hfive.yqformatTime("+field+")#");  
		hColumn.setWidth(width); 
		return hColumn;
 	}
	
	public static HColumn priceColumn(String field, String title,String width ){
		HColumn hColumn = new HColumn();
		hColumn.setField(field);
		hColumn.setTitle(title); 
		hColumn.setAttributes("{style:\"text-align:right\"}");
		hColumn.setTemplate("#:hfive.formatPrice("+field+")#");  
		hColumn.setWidth(width); 
		return hColumn;
 	}
	
	public static HColumn percentColumn(String field, String title,String width ){
		HColumn hColumn = new HColumn();
		hColumn.setField(field);
		hColumn.setTitle(title); 
		hColumn.setAttributes("{style:\"text-align:center\"}");
		hColumn.setTemplate("#:hfive.formatPercent("+field+")#");  
		hColumn.setWidth(width); 
		return hColumn;
 	}
	
	public static HColumn booleanColumn(String field, String title,String width ){
		HColumn hColumn = new HColumn();
		hColumn.setField(field);
		hColumn.setTitle(title); 
		hColumn.setAttributes("{style:\"text-align:center\"}");
		hColumn.setTemplate("#:hfive.formatBoolean("+field+")#");  
		hColumn.setWidth(width); 
		return hColumn;
 	}
	   
	
	/**
	 * 金额合计
	 * @param field
	 * @param title
	 * @param attributes
	 * @param template
	 * @return
	 */
	public static HColumn amountColumn(String field, String title,String width){
		HColumn hColumn = new HColumn();
		hColumn.setField(field);
		hColumn.setTitle(title);
		hColumn.setWidth(width); 
		//hColumn.setAttributes("{style:\"text-align:right\"}");
		hColumn.setTemplate("#:hfive.formatAmount("+field+")#");
		//hColumn.setFooterTemplate("<div style=\"width:100%;text-align:right;\">#:hfive.formatAmount(psum)#<br> #:hfive.formatAmount(sum)#</div> ");
		hColumn.setAggregate("sum,psum");
		return hColumn;
	}
	
	/**
	 * 多列合并显示
	 * @param field
	 * @param title
	 * @param width
	 * @param template
	 * @return
	 */
	public static HColumn muiltColumn(String field, String title,String width, List<SelectItem> colList){
		HColumn hColumn = new HColumn();
		hColumn.setField(field);
		hColumn.setTitle(title); 
		
		hColumn.setWidth(width);
		if (CommonUtil.isNotNull(colList)) {
			String tmpStr = "";
			String br = "";
			for (SelectItem col : colList) {
				String s = "";
				if (CommonUtil.isNull(col.getItemName())) {
					s = " #:"+col.getItemKey()+"#";
				} else {
					s = "<span>"+col.getItemName()+" : </span>#:"+col.getItemKey()+"#";
				}
				tmpStr = tmpStr+br+s;
				br="<br>";
			}
 			hColumn.setTemplate(tmpStr);
		}
		
		return hColumn;
	}
	
	public static HColumn setSumFooterTitle(HColumn hColumn) {
		//hColumn.setFooterTemplate("本页合计：<br> 总&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;计：");
		return hColumn;
	} 
	 
	/**
	 * 金额格式化
	 * @return
	 */
	public static String formatAmount() {
		 return HColumn.Format.amount.toString();
	}
	
	/**
	 * 布尔值格式化
	 * @return
	 */
	public static String formatYarn() {
		return HColumn.Format.yorn.toString();
	}
	
	/**
	 * html格式化
	 * @return
	 */
	public static String formatHtml() {
		return HColumn.Format.html.toString();
	}
	
}

