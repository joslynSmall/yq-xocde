package com.yq.xcode.security.resourceproviders;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yq.xcode.common.bean.ResourceInstance;
import com.yq.xcode.common.model.LookupCode;
import com.yq.xcode.common.service.LookupCodeService;
import com.yq.xcode.common.service.ResourceInstanceProvider;
import com.yq.xcode.security.resourceproviders.ResourceConstants;

@Service
public class WorkFlowRoleResourceInstanceProvider implements ResourceInstanceProvider{
	
	@Autowired 
	private LookupCodeService lookupCodeService;

	@Override
	public String getResourceName() {
		return ResourceConstants.WORK_FLOW_ROLE;
	}

	@Override
	public List<ResourceInstance> findAllResourceInstances() {
		List<LookupCode> lcList = lookupCodeService.findLookupCodeByCategory(ResourceConstants.LOOKUPCODE_WORK_FLOW_ROLE);
		List<ResourceInstance> list = new ArrayList<ResourceInstance>();
		for(LookupCode lc : lcList) {
			if (!"Y".equals(lc.getSegment1())) {
				ResourceInstance ri = new ResourceInstance();
				ri.setId(lc.getKeyCode());
				ri.setName(lc.getLookupName());
				list.add(ri);
			} 
		}
		return list;
	}



}
