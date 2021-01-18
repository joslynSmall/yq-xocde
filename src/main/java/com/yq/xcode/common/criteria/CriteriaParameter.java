package com.yq.xcode.common.criteria;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yq.xcode.common.bean.DataTypeConstants;
import com.yq.xcode.common.bean.PropertyDefine;
import com.yq.xcode.common.exception.ValidateException;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.JPAUtils;


@JsonIgnoreProperties(ignoreUnknown=true)
public class CriteriaParameter {
	//公用属性
	/**
	 * 参数分组， 按类型产生where 条件
	 */
	private String category;
	private String propertyName;
	private String colName; //数据库列，
	private String colLable; //显示标题 

	private String parameterValue; //参数值
	private String operator; //逻辑运算符，LIKE - 包含，  = - 等于 ... 直接写逻辑运算符
	/**
	 * 比较类型
	 */
	private String compareType; // C - 字符，D - 日期 , N - 数字 B - boolean 
	/**
	 * 参数值的预留处理,例如前匹配可以写concat(DataTypeConstants.PLACE_HOLDER,'%')
	 */
	private String placeHolder; //
    /**
     * 是否是期间标胶, 如果是, 有2条记录, propertyName 一样，
	   第一个的预算符是 >=, 第二个的运算符是 <= 
     */
	private boolean pair = false; 
	
	private boolean havingCause = false;
	
 

	/**
	 * 参数必须输入
	 */
	private Boolean mandatory;
 	
	/**
	 * 参数校验
	 */
	public void validateParameter() {
//		if (CommonUtil.isNull(this.colName)) {
//			throw new ValidateException(" 参数colName 列名不可为空 ！");
//		}
		if (CommonUtil.isNull(this.operator )) {
			throw new ValidateException("逻辑操作符不可为空！");
		}
	}
	
	public boolean isHavingCause() {
		return havingCause;
	}

	public void setHavingCause(boolean havingCause) {
		this.havingCause = havingCause;
	}

	/**
	 * 
	 * @return
	 */
	public String genCause() {
		
		this.validateParameter();
		if (CommonUtil.isNull(this.colName)) {
			return "";
		}
		if (CommonUtil.isNull(this.parameterValue)) {
			return "";
		}
		String placeStr = null;
		if (CommonUtil.isNotNull(this.placeHolder)) {
			return this.placeHolder.replace(DataTypeConstants.PLACE_HOLDER, JPAUtils.toPar(this.parameterValue));
		}
		
		if (this.operator.toUpperCase().contains("INLIKE")) { 
			return JPAUtils.genSuperLike(this.colName, this.parameterValue);
		}
		if (this.operator.toUpperCase().contains("NIN")) {//  数字IN ID in 
			return this.colName+" in "+CommonUtil.nvl(placeStr, JPAUtils.toInNumberSql(this.parameterValue.trim()));
		}
		
		if (DataTypeConstants.COMPARE_TYPE_CHAR.equals(this.compareType)) {
			
			if (this.operator.toUpperCase().contains("LIKE")) {
				return this.colName+" "+this.operator+CommonUtil.nvl(placeStr, " concat('%','"+JPAUtils.toPar(this.parameterValue.trim())+"','%' ) ");
			} else if (this.operator.toUpperCase().contains("IN")) {
				return this.colName+" "+this.operator+" "+CommonUtil.nvl(placeStr, JPAUtils.toInCharSql(this.parameterValue.trim()));
	
			} else {
				return this.colName+" "+this.operator+" "+CommonUtil.nvl(placeStr, " '"+JPAUtils.toPar(this.parameterValue.trim())+"' ");
			}
		} else if (DataTypeConstants.COMPARE_TYPE_DATE.equals(this.compareType)) {
			if ("<=".equals(this.operator.trim())) { //日期比较的结束日期,要处理时分秒的问题
				return this.colName+this.operator+CommonUtil.nvl(placeStr, JPAUtils.genEndDate(this.parameterValue));
			} else if ("=".equals(this.operator.trim())) { 
				if (CommonUtil.isNotNull(placeStr)) {
					return this.colName+" "+this.operator+" "+CommonUtil.nvl(placeStr, " '"+JPAUtils.toPar(this.parameterValue)+"' ");
				} else {
					return this.colName+" between '"+this.parameterValue+"' and "+JPAUtils.genEndDate(this.parameterValue);
				}								
			} else {
				return this.colName+" "+this.operator+" "+CommonUtil.nvl(placeStr, " '"+JPAUtils.toPar(this.parameterValue)+"' ");
			}
		//} else if (ReportConstants.COMPARE_TYPE_NUMBER.equals(this.compareType) || ReportConstants.COMPARE_TYPE_BOOLEAN.equals(this.compareType)) {
		// 做一个特殊处理， N开头的收拾数字， 包括金额， 单价， 数量， 百分比等	
		} else if (this.compareType.startsWith(DataTypeConstants.COMPARE_TYPE_NUMBER) || DataTypeConstants.COMPARE_TYPE_BOOLEAN.equals(this.compareType)) {
			if (this.operator.toUpperCase().contains("IN")) {
				return this.colName+" "+this.operator+" "+" "+CommonUtil.nvl(placeStr, JPAUtils.toInNumberSql(this.parameterValue));
			} else {
				return this.colName+" "+this.operator+" "+CommonUtil.nvl(placeStr, JPAUtils.toPar(this.parameterValue));
			}
			
		} else {
			throw new ValidateException("不存在的比较类型！");
		}
	}
	
	/**
	 * 如果value为空， 返回空字符串, 否则返回 and + 相关条件
	 * @return
	 */	
    public String genAndCause() {
    	if (this.havingCause) {
    		return "";
    	}
		String cause = this.genCause();
		if (CommonUtil.isNull(cause)) {
			return "";
		} else {
			return " and "+cause+" \r\n";
		}
	}
    
    /**
	 * 如果value为空， 返回空字符串, 否则返回 and + 相关条件
	 * @return
	 */	
    public String genAndHavingCause() {
    	if (!this.havingCause) {
    		return "";
    	}
		String cause = this.genCause();
		if (CommonUtil.isNull(cause)) {
			return "";
		} else {
			return " and  "+cause+" \r\n";
		}
	}
	
	
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public String getColName() {
		return colName;
	}
	public void setColName(String colName) {
		this.colName = colName;
	}


	public String getColLable() {
		return colLable;
	}

	public void setColLable(String colLable) {
		this.colLable = colLable;
	}

 
	public String getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getCompareType() {
		return compareType;
	}

	public void setCompareType(String compareType) {
		this.compareType = compareType;
	}

	public String getPlaceHolder() {
		return placeHolder;
	}

	public void setPlaceHolder(String placeHolder) {
		this.placeHolder = placeHolder;
	}

	public boolean isPair() {
		return pair;
	}

	public void setPair(boolean pair) {
		this.pair = pair;
	}

	public String toString() {
		return this.propertyName+" "+this.colLable+" "+this.colName+" "+this.operator;
	}

 
	
	public boolean isHigh() {
		return this.propertyName.contains(DataTypeConstants.BETWEEN_TO_SIGN);
	}
	public void setHigh(boolean high){} 
	public boolean isLow() { 
		return this.propertyName.contains(DataTypeConstants.BETWEEN_FROM_SIGN);
	}
	public void setLow(boolean low){}
	
	public CriteriaParameter() {
		
	}
  

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Boolean getMandatory() {
		return mandatory;
	}

	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}
	
 

	public CriteriaParameter(String colLable, String colName, String propertyName,   String operator, String compareType) {
		this.setColLable(colLable);
		this.setColName(colName);
		this.setPropertyName(propertyName); 
		this.setOperator(operator);
		this.setCompareType(compareType);
	} 
	
  	public CriteriaParameter(String colLable, String colName, String propertyName  ) {
  		PropertyDefine proDefine = new PropertyDefine();
  		proDefine.setDataType(DataTypeConstants.COMPARE_TYPE_CHAR);
  		proDefine.setColumn(colName);
  		proDefine.setLable(colLable);
  		proDefine.setProperty(propertyName);
		this.setColLable(colLable);
		this.setColName(colName);
		this.setPropertyName(propertyName);    
		this.compareType = DataTypeConstants.COMPARE_TYPE_CHAR;
		this.setOperator("like"); 
	} 
	
	
}
