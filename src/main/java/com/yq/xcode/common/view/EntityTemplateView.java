package com.yq.xcode.common.view;

import java.util.List;

import com.yq.xcode.common.model.EntityTemplate;
import com.yq.xcode.common.model.PageTag;

public class EntityTemplateView {

	private EntityTemplate entityTemplate;
	
	private List<PageTag> pageTagList;

	public EntityTemplate getEntityTemplate() {
		return entityTemplate;
	}

	public void setEntityTemplate(EntityTemplate entityTemplate) {
		this.entityTemplate = entityTemplate;
	}

	public List<PageTag> getPageTagList() {
		return pageTagList;
	}

	public void setPageTagList(List<PageTag> pageTagList) {
		this.pageTagList = pageTagList;
	}
	
	
	
	
}
