package com.yq.xcode.common.service;

import java.util.List;

import com.yq.xcode.common.bean.SelectItem;
import com.yq.xcode.common.model.LookupCode;
import com.yq.xcode.common.model.SystemParameter;



public interface CacheService {
	
	/**
	 * 清除定义的cache
	 */
	public void cleanAllCache(); 
	
	public List<LookupCode> findLookupCodeByCategory(String categoryCode); 
	public LookupCode getLookupCodeByKeyCode(String keyCode);
	public List<LookupCode> findLookupCodeByPro(String categoryCode,String queryString);

	
	
	public List<SelectItem> findSelectItemByParentKey(String parentKey);

	public SystemParameter getSystemParameterByKeyCode(String keyCode);
	

}
