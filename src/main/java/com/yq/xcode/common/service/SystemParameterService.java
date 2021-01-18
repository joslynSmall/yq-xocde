package com.yq.xcode.common.service;

import java.util.List;

import com.yq.xcode.common.model.SystemParameter;



public interface SystemParameterService {
	
	public List<SystemParameter> findSystemParameters(String keyCode, String category) ;

	public SystemParameter getSystemParameterByKeyCode(String keyCode);

	public SystemParameter updateSystemParameter(SystemParameter systemParameter);

	/**
	 * 根据参数keyCode,的到value,到要根据数据类型自动转换为相应的类型
	 * 
	 * C-字符  --> String
       N-数字  --> BigDecimal
       D-日期(YYYY-MM-DD) --> Date
       T-时分(HH24:MM)    --> Date 年月日可随便， 要设时 分
       DT-日期时间(YYYY-MM-DD HH24:MM) -->日期，包括时分秒

	 * @param keyCode
	 * @return
	 */
	public Object getValueByKey(String keyCode);
	
	public String getStringValueByKey(String keyCode) ;

	public List<SystemParameter> getParaListByCategory(String categoryCode);
	
	public String getStringValueByKey(String keyCode, List<SystemParameter> paraList);
	
}
