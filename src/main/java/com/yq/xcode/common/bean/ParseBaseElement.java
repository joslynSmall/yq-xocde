package com.yq.xcode.common.bean;

import java.util.Date;


public interface   ParseBaseElement {

	public String getEleNumber() ;

	public void setEleNumber(String eleNumber) ;

    public String getEleName() ;
    
	public void setEleName(String eleName);
        
	public String getEleCategory() ;
        
	public void setEleCategory(String eleCategory) ;
	
	public String getUseCategory() ;
        
	public void setUseCategory(String useCategory) ;
	/**
	  T - Time 事件 yyyy-MM-dd 24hh:mi:ms
		D - Date 日期类型
		N - Number 数据类型
		C - Char 字符类型
		B - Boolean 
		O - Object 对象，一般是用在系统hardcode的方法中用
		NV - 无返回值，是方法
	 */
	public String getDataType() ;
	
	/**
	 * 数字类型是的小数位数
	 * @param scale
	 * @return
	 */
	public Integer getScale();
	
	public void setScale(Integer scale);
	
        
	public void setDataType(String dataType) ;
        
	public String getCheckedBy();
        
	public void setCheckedBy(String checkedBy) ;
        
	public Date getCheckedDate() ;
        
	public void setCheckedDate(Date checkedDate) ;
        
	public String getDescription() ;
        
	public void setDescription(String description) ;
	 
	
	public String getExpress() ;


	public void setExpress(String propertyName) ;

}
