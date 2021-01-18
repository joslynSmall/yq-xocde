package com.yq.xcode.security.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.yq.xcode.common.bean.ResourceInstance;
import com.yq.xcode.security.criteria.ResourceCriteria;
import com.yq.xcode.security.entity.ResourceAssignment;
import com.yq.xcode.security.entity.ResourceDefination;

public interface ResourceService {

	public List<ResourceDefination> findAll(ResourceCriteria criteria);
	
	public Page<ResourceDefination> findResourcePage(ResourceCriteria criteria);
	
	public ResourceDefination getResourceDefination(String resourceName);
	
	public void saveResourceDefination(ResourceDefination resource);
	
	public void deleteResourceDefination(Long id);
	
	public ResourceDefination getResourceDefinationById(Long id); 
	
	public List<ResourceInstance> findResourceInstanceList(String resourceName);
	
	public List<ResourceAssignment> getResourceAssignmentBySId(Long resourceDefinationId,Long sid);
}
