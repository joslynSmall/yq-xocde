package com.yq.xcode.constants;

import java.util.ArrayList;
import java.util.List;

import com.yq.xcode.annotation.SelectCategory;
import com.yq.xcode.annotation.SelectList;
import com.yq.xcode.common.bean.SelectItem;
import com.yq.xcode.common.model.PageTag;
import com.yq.xcode.common.utils.YqBeanUtil;

public final class YqSelectHardcodeConstants {

 
	@SelectCategory(code = "PAGETAG", name = "控件")
	public static class PageTag_tag { 
		public final static String TextTag = "text"; // 文本输入
		public final static String DateTag = "date"; // 日期输入
		public final static String DateRangeTag = "daterange"; // 开始结束日期
		public final static String SelectTag = "select"; // 单选
		public final static String YesOrNoTag = "yorn"; // 是否选择
		public final static String MultSelectTag = "multselect"; // 多选
		public final static String PercentTag = "percent"; // 百分比
		public final static String PopupTag = "popup"; // 弹窗选择
		public final static String NumberTag = "number"; // 数字输入
		public final static String AmountTag = "amount"; // 金额输入
		// itemValue ： 控件类型, remark : 属性默认控件
		@SelectList
		public final static List<SelectItem> list = new ArrayList<SelectItem>() {
			{
				this.add(new SelectItem(TextTag, "文本输入", "text", PropertyType.STRING));
				this.add(new SelectItem(NumberTag, "数字输入", "number", PropertyType.INTEGER));
				this.add(new SelectItem(AmountTag, "金额输入", "amount", PropertyType.NUMBER));
				this.add(new SelectItem(DateTag, "日期输入", "date", PropertyType.DATE));
				this.add(new SelectItem(DateRangeTag, "日期区间输入", "daterange", ""));
				this.add(new SelectItem(SelectTag, "单选输入", "select", ""));
				this.add(new SelectItem(MultSelectTag, "多选", "select", ""));
				this.add(new SelectItem(PopupTag, "弹窗选择", "popup", ""));
				this.add(new SelectItem(YesOrNoTag, "是否选择", "select", ""));
				this.add(new SelectItem(PercentTag, "百分比", "percent", ""));
			

			}
		};

		public static PageTag getDefaultPageTagByType(String propertyType) {
			for (SelectItem si : list) {
				if (YqBeanUtil.endsWith(propertyType, si.getRemark())) {
					PageTag pt = new PageTag();
					pt.setTagKey(si.getItemKey());
					return pt;
				}
			}
			return null;
		}

		public static String getTagType(String key) {
			for (SelectItem si : list) {
				if (si.getItemKey().equals(key)) {
					return si.getItemValue();
				}
			}
			return null;
		}

	};

	/**
	 * 对象的基本属性
	 * 
	 * @author jettie
	 *
	 */
	@SelectCategory(code = "PROPERTYTYPE", name = "属性类型")
	public static class PropertyType {  
		public final static String STRING = "string";
		public final static String DATE = "date";
		public final static String NUMBER = "bigdecimal";
		public final static String INTEGER = "integer";
		public final static String BOOLEAN = "boolean";
		public final static String OBJECT = "object";

		// itemValue ： 控件类型, remark : 属性默认控件
		@SelectList
		public final static List<SelectItem> list = new ArrayList<SelectItem>() {
			{
				this.add(new SelectItem(STRING, "字符"));
				this.add(new SelectItem(DATE, "日期"));
				this.add(new SelectItem(NUMBER, "数字"));
				this.add(new SelectItem(INTEGER, "整数"));
				this.add(new SelectItem(BOOLEAN, "Boolean"));
				this.add(new SelectItem(OBJECT, "对象"));

			}
		};

	};

	/**
	 * 
	 * @author jettie
	 *
	 */
	@SelectCategory(code = "PASEELECATEGORY", name = "元素类型")
	public static class ParseElement_eleCategory { 
		public final static String UNIT = "U"; // 因子
		public final static String PARAMETER = "P"; // 参数
		public final static String FUNCTION = "F"; // 函数

		// itemValue ： 控件类型, remark : 属性默认控件
		@SelectList
		public final static List<SelectItem> list = new ArrayList<SelectItem>() {
			{
				this.add(new SelectItem(UNIT, "因子"));
				this.add(new SelectItem(PARAMETER, "参数"));
				this.add(new SelectItem(FUNCTION, "函数"));
			}
		};

	};
	
	/**
	 * 
	 * @author jettie
	 *
	 */
	@SelectCategory(code = "ROLECATEGORY", name = "角色类型")
	public static class Role_category {  
		public final static String SYS = "Y"; // 因子
		public final static String ENTITY_ROLE = "M"; // 参数
		public final static String NORM = "N"; // 函数

		// itemValue ： 控件类型, remark : 属性默认控件
		@SelectList
		public final static List<SelectItem> list = new ArrayList<SelectItem>() {
			{
				this.add(new SelectItem(SYS, "系统角色"));
				this.add(new SelectItem(ENTITY_ROLE, "对象特定角色"));
				this.add(new SelectItem(NORM, "普通角色"));
			}
		};

	};
	
 

}
