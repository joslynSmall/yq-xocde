package com.yq.xcode.common.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.yq.xcode.common.exception.DllException;
import com.yq.xcode.common.model.SystemParameter;
import com.yq.xcode.common.service.CacheService;
import com.yq.xcode.common.service.SqlToModelService;
import com.yq.xcode.common.service.SystemParameterService;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.DateUtil;
import com.yq.xcode.common.utils.JPAUtils;

@Service("SystemParameterService")
public class SystemParameterServiceImpl extends YqJpaDataAccessObject implements SystemParameterService{
	@Autowired
	private CacheService cacheService;
	@Autowired
	private SqlToModelService sqlToModelService;
	
	public SystemParameter getSystemParameterByKeyCode(String keyCode) {	
		return cacheService.getSystemParameterByKeyCode(keyCode);
	}

	@Override
	public List<SystemParameter> findSystemParameters(String keyCode, String category) {
		StringBuffer query = new StringBuffer();
		query.append(" select * from YQ_SYSTEM_PARAMETER a where 1=1");
		if(!CommonUtil.isNull(keyCode)){
			query.append(" and concat(a.KEY_CODE,a.parameter_name,a.description) like '%").append(keyCode).append("%'");
		}
		if(!CommonUtil.isNull(category)){
			query.append(" and  a.category like '%").append(category).append("%'");
		}
		query.append(" order by a.LINE_NUMBER");
		List<SystemParameter> list = sqlToModelService.executeNativeQuery(query.toString(), null, SystemParameter.class);
		return list;
	}
	
	@Override
	@CacheEvict(value="APP_MRP_SYSTEM_PARAMETER_BY_KEY",allEntries=true)
	public SystemParameter updateSystemParameter(SystemParameter systemParameter) {

		SystemParameter oldSystemparameter = em.find(SystemParameter.class, systemParameter.getKeyCode());
		oldSystemparameter.setParameterValue(systemParameter.getParameterValue());
		oldSystemparameter.setDescription(systemParameter.getDescription());
        if(checkValueByFormat(oldSystemparameter.getDataType(),systemParameter.getParameterValue())==null)
        {
        	throw new DllException("参数值不符合类型格式");
        }	
		return em.merge(oldSystemparameter);
	}

	private Object checkValueByFormat(String dataType,String value) {
		Object objValue=null;
	   if ("N".equals(dataType)) {
			try {
				objValue = new BigDecimal(value);
			} catch (Exception ex) {
				//ex.printStackTrace();
			}
		} else if ("D".equals(dataType)) {
			objValue = DateUtil.convertString2Date(value, "yyyy-MM-dd");
		} else if ("T".equals(dataType)) {
			objValue = DateUtil.convertString2Date(value, "HH:mm");
		} else if ("DT".equals(dataType)) {
			objValue = DateUtil.convertString2Date(value, "yyyy-MM-dd HH:mm");
		}else if("C".equals(dataType)){
			objValue = new String(value);
		}
		return objValue;
	}
	/**
	 * 数据类型
	 * C-字符
       N-数字
       D-日期(YYYY-MM-DD)
       T-时分(HH24:MM)
       DT-日期时间(YYYY-MM-DD HH24:MM)
	 */
	@Override
	public Object getValueByKey(String keyCode) {
		SystemParameter sp=getSystemParameterByKeyCode(keyCode);
		if("C".equals(sp.getDataType()))
		{
			return sp.getParameterValue();
		}else if("N".equals(sp.getDataType()))
		{
			return new BigDecimal(sp.getParameterValue());
		}else if("D".equals(sp.getDataType()))
		{
			return DateUtil.convertString2Date(sp.getParameterValue(),"yyyy-MM-dd");
		}
		else if("T".equals(sp.getDataType()))
		{
			return DateUtil.convertString2Date(sp.getParameterValue(),"HH:mm");
		}
		else if("DT".equals(sp.getDataType()))
		{
			return DateUtil.convertString2Date(sp.getParameterValue(),"yyyy-MM-dd HH:mm");
	    }
		return sp.getParameterValue();
	}
	@Override
	public String getStringValueByKey(String keyCode) {
		SystemParameter sp=getSystemParameterByKeyCode(keyCode);
		if (sp != null) {
			return sp.getParameterValue();
		} else {
			return null;
		}

	}
	
	@Override
	public List<SystemParameter> getParaListByCategory(String categoryCode) {
		StringBuffer query = new StringBuffer();
		query.append(" SELECT * FROM yq_system_parameter a WHERE  category='" + JPAUtils.toPar(categoryCode)+"' ");
		query.append(" ORDER BY a.key_code");
		List<SystemParameter> list = sqlToModelService.executeNativeQuery(query.toString(), null, SystemParameter.class);
		return list;
	}
	
	@Override
	public String getStringValueByKey(String keyCode, List<SystemParameter> paraList) {
		for (SystemParameter sp : paraList) {
			if (sp.getKeyCode().equals(keyCode)) {
				return sp.getParameterValue();
			}
		}
		return getStringValueByKey(keyCode);
	}

}
