package com.yq.xcode.aop.event;

import com.yq.xcode.common.bean.WorkFlowEntityIntf;
import com.yq.xcode.common.model.WorkFlowDetail;
import com.yq.xcode.common.model.WorkFlowEntity;
import com.yq.xcode.common.model.WorkFlowEntityActionLog;
import com.yq.xcode.common.service.WorkFlowEntityService;
import com.yq.xcode.common.service.WorkFlowMessageService;

public class WorkFlowMessageEvent extends BaseEvent {

	private static final long serialVersionUID = 7213246619064569795L;
	
	private WorkFlowMessageService workFlowMessageService;
	private WorkFlowDetail wfd;
	private WorkFlowEntity workFlowEntity;
	private WorkFlowEntityIntf workFlowEntityIntf;
	private WorkFlowEntityService entityService;
	private WorkFlowEntityActionLog  actionLog ;
 
	public WorkFlowMessageEvent(Object source) {
		super(source);
	}
	public WorkFlowMessageEvent(WorkFlowMessageService workFlowMessageService,WorkFlowDetail wfd, WorkFlowEntityIntf workFlowEntityIntf,WorkFlowEntity workFlowEntity ,WorkFlowEntityService entityService,WorkFlowEntityActionLog  actionLog ) {
		super(workFlowMessageService);
		this.workFlowMessageService = workFlowMessageService;
		this.wfd = wfd;
		this.workFlowEntity = workFlowEntity;
		this.workFlowEntityIntf = workFlowEntityIntf;
		this.entityService = entityService;
		this.actionLog = actionLog;
	}

	@Override
	public void run() {
		this.workFlowMessageService.sendWorkFlowMessage(wfd, workFlowEntity, workFlowEntityIntf, entityService, actionLog);
	} 
	
}
