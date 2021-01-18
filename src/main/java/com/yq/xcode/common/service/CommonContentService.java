package com.yq.xcode.common.service;

import com.yq.xcode.common.model.CommonContent;

public interface CommonContentService {
	
	public CommonContent saveEntityContent(CommonContent entityContent);
	public CommonContent saveEntityContent(String sourceCategory, Long sourceId, CommonContent entityContent);
	public CommonContent getEntityContent(String sourceCategory, Long sourceId);
	public void deleteEntityContent(Long id);
	public void deleteEntityContent(String sourceCategory, Long sourceId);
	/**
	 * 替换宏后保存数据
	 * @param sourceCategory
	 * @param sourceId
	 * @param entityTemplateId
	 * @param bean
	 * @return
	 */
	//public CommonContent saveAndConvertEntityContent(String sourceCategory, Long sourceId, Long entityTemplateId, Object bean);
 
}
