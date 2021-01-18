package com.yq.xcode.common.service;

import java.util.List;
import java.util.Map;

import com.yq.xcode.common.bean.ParseRootAndVariable;
import com.yq.xcode.common.bean.WorkFlowActionButton;
import com.yq.xcode.common.bean.WorkFlowActionReason;
import com.yq.xcode.common.bean.WorkFlowEntityIntf;
import com.yq.xcode.common.bean.WorkFlowEntitySkipPageView;
import com.yq.xcode.common.bean.WorkFlowOpenPageView;
import com.yq.xcode.common.bean.WorkFlowWeappView;
import com.yq.xcode.common.exception.WorkFlowException;
import com.yq.xcode.common.model.LookupCode;
import com.yq.xcode.common.model.WorkFlowDetail;
import com.yq.xcode.common.model.WorkFlowEntity;
import com.yq.xcode.common.model.WorkFlowEntityActionLog;



public interface WorkFlowActionService  {
	/**
	 * 单笔审批
	 * @param batch
	 * @param action
	 * @return
	 * @throws WorkFlowException
	 */
	public WorkFlowEntityIntf executeSingleAction(Long id, Map variables, String action, String actionReason) throws WorkFlowException;

	/**
	 * 系统运行， 没有上下文
	 * @param id
	 * @param variables
	 * @param action
	 * @param actionReason
	 * @return
	 * @throws WorkFlowException
	 */
	public WorkFlowEntityIntf executeSingleActionBySys(String jobName, Long entityId,String entityCategory, Map variables, String action, String actionReason) throws WorkFlowException;

	/**
	 * resetWorkFlow 是否重取流程， 是会将流程重新去过来， 替代之前的流程， 当前状态不变

	 */
	public WorkFlowEntityIntf executeSingleAction(WorkFlowEntityIntf workFlowEntity,Map variables, String action, String actionReason,	boolean resetWorkFLow) throws WorkFlowException;
	
	public WorkFlowEntityIntf executeSingleAction(WorkFlowEntityIntf workFlowEntity, Map variables, String action, WorkFlowActionReason actionReason) throws WorkFlowException;
	public WorkFlowEntityIntf executeSingleAction(WorkFlowEntityIntf workFlowEntity, Map variables, String action, String actionReason) throws WorkFlowException;
	
	public WorkFlowEntityIntf executeSingleAction(Long entityId,String entityCategory, Map variables, String action, String actionReason) throws WorkFlowException;
	
	/**
	 * 批量审批
	 * @return
	 * @throws WorkFlowException
	 */
	public void executeBatchAction(List<WorkFlowEntity> workFlowEntities, Map variables, String action, WorkFlowActionReason actionReason) throws WorkFlowException;
	public void executeBatchAction(List<WorkFlowEntity> workFlowEntities, Map variables, String action, String actionReason) throws WorkFlowException;
	public void executeBatchByWfEntityIdsAction(List<Long> wfEntityIds, Map variables, String action, String actionReason, List<WorkFlowEntityIntf> wfIntfList) throws WorkFlowException;

	
	/**
	 * 实体类的批量审批
	 */
	public void executeBatchActionByIntf(List<WorkFlowEntityIntf> entities, Map variables, String action, WorkFlowActionReason actionReason) throws WorkFlowException;

	
	/**
	 * 打开业务单据时页面显示相关的流程信息
	 * @param entityCategory 
	 * @return
	 * @throws WorkFlowException
	 */
	public WorkFlowOpenPageView genOpenPageView(Long workFlowEntityId, Long entityId, String entityCategory, Map variables) throws WorkFlowException;
	public WorkFlowOpenPageView genOpenPageView(WorkFlowEntityIntf workFlowEntityIntf, Map variables ) throws WorkFlowException;
	
	/**
	 * 小程序端流流程图信息
	 * @param entityId
	 * @param entityCategory
	 * @return
	 */
	public List<WorkFlowWeappView> getWorkFlowWeappView(Long entityId, String entityCategory);
	
	/**
	 * 发送给审批 
	 * @return
	 * @throws WorkFlowException
	 */
	public void sendToWorkFlow(WorkFlowEntityIntf workFlowEntityIntf ) throws WorkFlowException;
	
	/**
	 * 当前有效操作
	 * @param workFlowEntity
	 * @param variables
	 * @return
	 */
	//public List<LookupCode> getEnableActions(WorkFlowEntityIntf workFlowEntity, Map variables, boolean hiddenFalseAction);

	/**
	 * 审批日志
	 * @param EntityId
	 * @param EntityCategory
	 * @return
	 */
	public List<WorkFlowEntityActionLog> findActionLog(Long entityId, String entityCategory,String currentStatus);

	public WorkFlowEntity getWorkFlowEntity(Long EntityId, String EntityCategory);

	void executeBatchAction(List<WorkFlowEntity> workFlowEntities,
			Map variables, String action, String actionReason,
			List<WorkFlowEntityIntf> wfIntfList) throws WorkFlowException;

	void executeBatchAction(List<WorkFlowEntity> workFlowEntities,
			Map variables, String action, WorkFlowActionReason actionReason,
			List<WorkFlowEntityIntf> wfIntfList) throws WorkFlowException;

	/**
	 * 包括冲历史表去数
	 * @param entityId
	 * @param entityCategory
	 * @return
	 */
	public WorkFlowEntity getWorkFlowEntityIncHistory(Long entityId, String entityCategory);

	/**
	 * 
	 * @param skipPageView
	 * @param action
	 * @return
	 * @throws WorkFlowException
	 */
	public WorkFlowEntityIntf executeSingleAction(WorkFlowEntitySkipPageView skipPageView, String action)
			throws WorkFlowException;
	
	public WorkFlowEntityIntf  executeSingleAction(WorkFlowEntitySkipPageView skipPageView , String action, String dateFormat) throws WorkFlowException;
	
	public WorkFlowEntityIntf saveWorkFlowEntityIntfByPageView(WorkFlowEntitySkipPageView skipPageView );
	
	public WorkFlowEntityIntf saveWorkFlowEntityIntfByPageView(WorkFlowEntitySkipPageView skipPageView, String dateFormat);

	public WorkFlowEntity getWorkFlowEntityIncHistory(Long id);
	
	public WorkFlowEntityIntf getWorkFlowEntityIntf(Long entityId, String entityCategory);

	public List<WorkFlowActionButton> getEnableActions(String entityCategory, Long entityId, Map variables);

	public List<WorkFlowEntityActionLog> findActionLog(Long entityId, String entityCategory);
 
}
	
