package com.yq.xcode.common.service;

import java.util.List;

import com.yq.xcode.common.bean.ResourceInstance;

public interface ResourceInstanceProvider {

	public String getResourceName();
	
	public List<ResourceInstance> findAllResourceInstances();

}

