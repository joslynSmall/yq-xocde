package com.yq.xcode.common.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yq.xcode.common.bean.WorkFlowActionButton;
import com.yq.xcode.common.bean.WorkFlowEntityIntf;
import com.yq.xcode.common.bean.WorkFlowWeappView;
import com.yq.xcode.common.exception.WorkFlowException;
import com.yq.xcode.common.model.WorkFlowEntityActionLog;
import com.yq.xcode.common.service.WorkFlowActionService;
import com.yq.xcode.common.service.WorkFlowSimpleActionService; 

@Service("WorkFlowSimpleActionService") 
@Transactional
public class WorkFlowSimpleActionServiceImpl extends YqJpaDataAccessObject implements WorkFlowSimpleActionService {
	@Autowired private WorkFlowActionService workFlowActionService; 
	@Override
	public void sendToWorkFlow(WorkFlowEntityIntf workFlowEntityIntf) throws WorkFlowException {
		this.workFlowActionService.sendToWorkFlow(workFlowEntityIntf);
	}

	@Override
	public WorkFlowEntityIntf executeSingleAction(WorkFlowEntityIntf workFlowEntity, String action, String actionReason)
			throws WorkFlowException { 
		return this.workFlowActionService.executeSingleAction(workFlowEntity,null, action, actionReason);
	}

	@Override
	public WorkFlowEntityIntf executeSingleAction(Long entityId, String entityCategory, String action,
			String actionReason) throws WorkFlowException {
		 
		return this.workFlowActionService.executeSingleAction(entityId, entityCategory, null, action, actionReason);
	}

	@Override
	public List<WorkFlowWeappView> getWorkFlowGraphList(Long entityId, String entityCategory) {
		 
		return this.workFlowActionService.getWorkFlowWeappView(entityId, entityCategory);
	}

	@Override
	public List<WorkFlowActionButton> getEnableActions(Long entityId, String entityCategory) {
		 
		return this.workFlowActionService.getEnableActions( entityCategory,  entityId, null);
	}

	@Override
	public List<WorkFlowEntityActionLog> findActionLog(Long entityId, String entityCategory) {
		return this.workFlowActionService.findActionLog(entityId, entityCategory);
	}
	 
}
