package com.yq.xcode.common.service;

import java.util.List;

import com.yq.xcode.common.bean.WorkFlowActionButton;
import com.yq.xcode.common.bean.WorkFlowEntityIntf;
import com.yq.xcode.common.bean.WorkFlowWeappView;
import com.yq.xcode.common.exception.WorkFlowException;
import com.yq.xcode.common.model.WorkFlowEntityActionLog;



public interface WorkFlowSimpleActionService  {
	 	
	/**
	 * 发送给审批 
	 * @return
	 * @throws WorkFlowException
	 */
	public void sendToWorkFlow(WorkFlowEntityIntf workFlowEntityIntf ) throws WorkFlowException;
	
	/**
	 * resetWorkFlow 是否重取流程， 是会将流程重新去过来， 替代之前的流程， 当前状态不变
 	 */
	public WorkFlowEntityIntf executeSingleAction(WorkFlowEntityIntf workFlowEntity , String action, String actionReason ) throws WorkFlowException;
	
 
	/**
	 * 
	 * @param entityId 业务单据ID
	 * @param entityCategory 业务单据类型
	 * @param action 
	 * @param actionReason
	 * @return
	 * @throws WorkFlowException
	 */
	public WorkFlowEntityIntf executeSingleAction(Long entityId,String entityCategory,  String action, String actionReason) throws WorkFlowException;
	
	   
	/**
	 * 小程序端流流程图信息
	 * @param entityId
	 * @param entityCategory
	 * @return
	 */
	public List<WorkFlowWeappView> getWorkFlowGraphList(Long entityId, String entityCategory);
	
 
	
	/**
	 * 当前有效操作
	 * @param workFlowEntity
	 * @param variables
	 * @return
	 */
	public List<WorkFlowActionButton> getEnableActions(Long entityId, String entityCategory);

 
	/**
	 * 审批日志
	 * @param EntityId
	 * @param EntityCategory
	 * @return
	 */
	public List<WorkFlowEntityActionLog> findActionLog(Long entityId, String entityCategory);

}
	
