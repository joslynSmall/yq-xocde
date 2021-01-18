package com.yq.xcode.common.service;

import java.util.List;

import com.yq.xcode.common.bean.IdAndVersion;
import com.yq.xcode.common.bean.SelectItem;
import com.yq.xcode.common.criteria.PageTagCriteria;
import com.yq.xcode.common.model.PageTag;
import com.yq.xcode.common.view.PageTagGroupView;

public interface PageTagService {
	
	public List<PageTag> getPageTagList(String sourceCategory);
	
	public List<PageTag> getPageTagList(String sourceCategory,String sourceHeaderKey);
	
	public List<PageTag> getPageTagList(String sourceCategory,String sourceHeaderKey, String sourceLineKey);

	public PageTag initPageTag(String sourceCategory,String sourceHeaderKey);

	public PageTag getPageTagById(Long id);

	public PageTag savePageTag(PageTag pageTag);

	public void deletePageTag(List<IdAndVersion> idvs);

	public void deletePageTagById(Long id);

	public List<PageTag> findPageTagList(String sourceCategory, String sourceHeaderKey);

	public List<SelectItem> findEntityTemplateClassProperty(Long entityTemplateId);

	public List<SelectItem> findReadTypeSource();

	public void batchUpdatePageTagGroup(PageTagGroupView pageTagGroupView);

	public List<PageTag> findPageTagByEntityTemplateCode(String entityTemplateCode, String groupName);

	public List<SelectItem> findGroupNameList(String sourceCategory, String sourceKey);

	public List<PageTag> findPageTagList(PageTagCriteria criteria);
 

}
