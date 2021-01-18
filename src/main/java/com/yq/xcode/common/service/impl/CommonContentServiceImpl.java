package com.yq.xcode.common.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yq.xcode.common.model.CommonContent;
import com.yq.xcode.common.service.CommonContentService;
import com.yq.xcode.common.service.EntityTemplateService;
import com.yq.xcode.common.service.SqlToModelService;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.JPAUtils;

@Service("EntityContentService")
public class CommonContentServiceImpl extends YqJpaDataAccessObject  implements CommonContentService {
	@Autowired
	private SqlToModelService sqlToModelService;
	
	@Autowired
	private EntityTemplateService entityTemplateService;

	@Override
	public CommonContent saveEntityContent(CommonContent entityContent) {
		 
		return this.save(entityContent);
	}

	@Override
	public CommonContent saveEntityContent(String sourceCategory, Long sourceId, CommonContent entityContent) {
		entityContent.setSourceCategory(sourceCategory);
		entityContent.setSourceId(sourceId);
		return this.saveEntityContent(entityContent);
	}
	
//	@Override
//	public CommonContent saveAndConvertEntityContent(String sourceCategory, Long sourceId, Long entityTemplateId, Object bean) {
//		CommonContent entityContent = this.getEntityContent(sourceCategory, sourceId);
//		if (entityContent == null) {
//			entityContent = new CommonContent();
//			entityContent.setSourceCategory(sourceCategory);
//			entityContent.setSourceId(sourceId);
//		}
//		CommonContent tmpContent = entityTemplateService.getTemplateEntityContent(entityTemplateId);
//		if (tmpContent != null) { // 为空, 就不替换, 原样保存
//			entityContent.setContent(this.entityTemplateService.replaceEntityContent(entityTemplateId, tmpContent.getContent(), bean));
//		} else {
//			//throw new ValdiateException("模板不存在， 不能替换!");
//		} 
//		return this.saveEntityContent(entityContent);
//	}

	@Override
	public CommonContent getEntityContent(String sourceCategory, Long sourceId) {
		String query = "select "+JPAUtils.genEntityCols(CommonContent.class, "a", null)
		+" from YQ_COMMON_CONTENT a "
		+" where a.source_category = '"+sourceCategory+"' "
		+"   and a.source_id = "+sourceId;
		List<CommonContent> list = this.sqlToModelService.executeNativeQuery(query, null, CommonContent.class);
		if (CommonUtil.isNotNull(list)) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public void deleteEntityContent(Long id) {
		this.deleteById(CommonContent.class, id);
	}

	@Override
	public void deleteEntityContent(String sourceCategory, Long sourceId) {
		CommonContent entityContent = this.getEntityContent(sourceCategory, sourceId);
		if (entityContent != null) {
			this.delete(entityContent);
		} 
	}
	
	
}
