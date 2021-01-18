package com.yq.xcode.constants.service;

import java.util.List;

import com.yq.xcode.common.bean.SelectItemDefine;

/**
 * 返回元素测试
 * @author jettie
 *
 */
public interface SelectConstantsService {
	
	/**
	 * 一般定义查询的列表， 也可以定义 harcode 选择在里面， 
	 * @return
	 */
	public List<SelectItemDefine> getSelectItemDefineList();
	
	/**
	 * harcode 的列表定义
	 * @return
	 */
	public Class<?>[] getSelectHarcodeConstansClassArr();
	
	/**
	 * query 列表定义
	 * @return
	 */
	public Class<?>[] getSelectQueryConstansClassArr();
  
}
