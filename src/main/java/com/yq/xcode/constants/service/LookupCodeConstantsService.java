package com.yq.xcode.constants.service;

import java.util.List;

import com.yq.xcode.common.bean.LookupCodeCategory;

/**
 * 返回元素测试
 * @author jettie
 *
 */
public interface LookupCodeConstantsService {
	
 
	/**
	 * 可以定义列表
	 * @return
	 */
	public List<LookupCodeCategory> getLookupCodeCategoryList();
	/**
	 * 可以 lookupcodeCategory 定义的静态问题
	 * @return
	 */
	public Class[] getLookupCodeConstantsArray();
 
  
}
