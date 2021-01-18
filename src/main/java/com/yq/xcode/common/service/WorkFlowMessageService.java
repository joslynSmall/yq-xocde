package com.yq.xcode.common.service;

import com.yq.xcode.common.bean.WorkFlowEntityIntf;
import com.yq.xcode.common.model.WorkFlowDetail;
import com.yq.xcode.common.model.WorkFlowEntity;
import com.yq.xcode.common.model.WorkFlowEntityActionLog;


public interface WorkFlowMessageService {
	/**
	 * 发消息
	 */
	public void sendWorkFlowMessage(WorkFlowDetail wfd, WorkFlowEntity workFlowEntity,WorkFlowEntityIntf workFlowEntityIntf,WorkFlowEntityService entityService,WorkFlowEntityActionLog  actionLog );
	  
}
