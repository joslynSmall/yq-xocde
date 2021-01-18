package com.yq.xcode.common.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.yq.xcode.common.bean.IdAndVersion;
import com.yq.xcode.common.bean.SelectItem;
import com.yq.xcode.common.criteria.EntityTemplateCriteria;
import com.yq.xcode.common.model.EntityTemplate;
import com.yq.xcode.common.view.EntityTemplateView;

public interface EntityTemplateService {
	
	public EntityTemplate saveEntityTemplate(EntityTemplate entityTemplate);
	public Page<EntityTemplate> findEntityTemplatePage( EntityTemplateCriteria criteria);
	public List<EntityTemplate> findEntityTemplateList(EntityTemplateCriteria criteria);
	public void deleteEntityTemplate(Long id); 
	public EntityTemplate getEntityTemplateById(Long id);
	public EntityTemplate getEntityTemplateByCategory(String category); 
	   
    /**
     * 替换宏， 支持PS工具
     * @param entityTemplateId
     * @param entityContent
     * @param bean
     * @return
     */
	public String replaceEntityContent(Long entityTemplateId, String entityContent, Object bean);  
	
	public void deleteTemplateList(List<IdAndVersion> idvs);
	public EntityTemplate initEntityTemplate(Long id);
	public EntityTemplateView builderEntityTemplateView(Long id);
	public EntityTemplate getEntityTemplateByCode(String code);

}
