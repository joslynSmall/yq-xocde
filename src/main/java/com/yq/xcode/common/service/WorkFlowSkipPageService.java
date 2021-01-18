package com.yq.xcode.common.service;

import com.yq.xcode.common.bean.WorkFlowEntityIntf;
import com.yq.xcode.common.bean.WorkFlowEntitySkipPageView;
import com.yq.xcode.common.model.WorkFlowSkipPageDefine;



public interface WorkFlowSkipPageService {
 
	
	public WorkFlowSkipPageDefine getWorkFlowSkipPageDefineById(Long id);

	public WorkFlowEntitySkipPageView getWorkFlowEntitySkipPageView(Long wfEntityId, Long skipPageDefineId);
	
	public WorkFlowEntitySkipPageView getWorkFlowEntitySkipPageView(Long entityId, String entityCategory, Long skipPageDefineId);
	
	public WorkFlowEntitySkipPageView getWorkFlowEntitySkipPageView(WorkFlowEntityIntf entityIntf, Long skipPageDefineId);
	 
}
	
