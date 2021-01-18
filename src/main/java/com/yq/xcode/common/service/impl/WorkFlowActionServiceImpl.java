package com.yq.xcode.common.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.yq.xcode.aop.event.ApplicationEventPublishers;
import com.yq.xcode.aop.event.WorkFlowMessageEvent;
import com.yq.xcode.common.bean.ParseRootAndVariable;
import com.yq.xcode.common.bean.WfConstants;
import com.yq.xcode.common.bean.WorkFlowActionButton;
import com.yq.xcode.common.bean.WorkFlowActionReason;
import com.yq.xcode.common.bean.WorkFlowEntityCategory;
import com.yq.xcode.common.bean.WorkFlowEntityIntf;
import com.yq.xcode.common.bean.WorkFlowEntitySkipPageView;
import com.yq.xcode.common.bean.WorkFlowOpenPageView;
import com.yq.xcode.common.bean.WorkFlowSpecRole;
import com.yq.xcode.common.bean.WorkFlowWeappView;
import com.yq.xcode.common.exception.ValidateException;
import com.yq.xcode.common.exception.WorkFlowException;
import com.yq.xcode.common.model.LookupCode;
import com.yq.xcode.common.model.ParseElement;
import com.yq.xcode.common.model.WorkFlow;
import com.yq.xcode.common.model.WorkFlowDetail;
import com.yq.xcode.common.model.WorkFlowEntity;
import com.yq.xcode.common.model.WorkFlowEntityActionLog;
import com.yq.xcode.common.model.WorkFlowGraphDetail;
import com.yq.xcode.common.service.EntityTemplateService;
import com.yq.xcode.common.service.LookupCodeService;
import com.yq.xcode.common.service.ParseService;
import com.yq.xcode.common.service.SqlToModelService;
import com.yq.xcode.common.service.WorkFlowActionService;
import com.yq.xcode.common.service.WorkFlowEntityService;
import com.yq.xcode.common.service.WorkFlowMessageService;
import com.yq.xcode.common.service.WorkFlowService;
import com.yq.xcode.common.service.YqEmailService;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.DateUtil;
import com.yq.xcode.common.utils.JPAUtils;
import com.yq.xcode.common.utils.WorkFlowUtil;
import com.yq.xcode.common.utils.YqBeanUtil;
import com.yq.xcode.constants.YqLCConstants;
import com.yq.xcode.security.utils.YqSecurityUtils; 

@Service("WorkFlowActionService") 
@Transactional
public class WorkFlowActionServiceImpl extends YqJpaDataAccessObject implements WorkFlowActionService,ApplicationContextAware {
	
	
	@Autowired
	private LookupCodeService lookupCodeService;
	@Autowired
	private SqlToModelService sqlToModelService;
	@Autowired
	private WorkFlowService workFlowService;
	@Autowired
	private ParseService parseService;
	@Autowired
	private YqEmailService emailService; 
	@Autowired
	private WorkFlowMessageService workFlowMessageService;
	private ApplicationContext applicationContext;
	
	@Autowired
	private EntityTemplateService entityTemplateService;
	
	
	 

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
		
	}

	private WorkFlowEntityIntf getWorkFlowEntityIntfByWorkFlowEntityId(Long workFlowEntityId, Long entityId, String entityCategory){
        if (!CommonUtil.isNullId(entityId) && CommonUtil.isNotNull(entityCategory)) {  
    		WorkFlowEntityService entityService = this.getEntityServiceByCategoryCode(entityCategory);
    		WorkFlowEntityIntf workFlowEntityIntf = entityService.getWorkFlowEntityByEntityId(entityId);
    		return workFlowEntityIntf;
        }
		WorkFlowEntity workFlowEntity = this.getById(WorkFlowEntity.class, workFlowEntityId);
		if (workFlowEntity == null) {
			//看是否存在历史表中
			workFlowEntity = this.getHistoryWorkFlowEntity(entityId, entityCategory);
		}
		if (workFlowEntity == null) {
			return null;
		}
		WorkFlowEntityService entityService = this.getEntityServiceByCategoryCode(workFlowEntity.getEntityCategory());
		WorkFlowEntityIntf workFlowEntityIntf = entityService.getWorkFlowEntityByEntityId(workFlowEntity.getEntityId());
		return workFlowEntityIntf;
	}
	
	private WorkFlowEntityIntf getWorkFlowEntityIntfByWorkFlowEntityId(Long workFlowEntityId){
		WorkFlowEntity workFlowEntity = this.getById(WorkFlowEntity.class, workFlowEntityId);
		WorkFlowEntityService entityService = this.getEntityServiceByCategoryCode(workFlowEntity.getEntityCategory());
		WorkFlowEntityIntf workFlowEntityIntf = entityService.getWorkFlowEntityByEntityId(workFlowEntity.getEntityId());
		return workFlowEntityIntf;
	}
	
	@Override
	public WorkFlowEntityIntf  executeSingleAction(Long workFlowEntityId, Map variables, String action, String actionReason) 
			throws WorkFlowException {
		WorkFlowEntityIntf workFlowEntityIntf = getWorkFlowEntityIntfByWorkFlowEntityId(workFlowEntityId);
		this.executeSingleAction(workFlowEntityIntf, variables, action, actionReason);
		return workFlowEntityIntf;
	}
	
	@Override
	public  WorkFlowEntityIntf executeSingleAction(WorkFlowEntityIntf workFlowEntity, Map variables, String action, String actionReason) 
			throws WorkFlowException {
		this.executeSingleAction( workFlowEntity,  variables,  action,  actionReason,false); 
		return workFlowEntity;
	}
	
	@Override
	public WorkFlowEntityIntf executeSingleAction(WorkFlowEntityIntf workFlowEntityIntf, Map variables, String action, String actionReason, boolean resetWorkFLow) 
			throws WorkFlowException {
		WorkFlowActionReason ar = null;
		if (CommonUtil.isNotNull(actionReason)) {
			ar = new WorkFlowActionReason();
			ar.setActionReason(actionReason);
		}
	   this.executeSingleAction(workFlowEntityIntf, variables, action, ar,resetWorkFLow);
	   return workFlowEntityIntf;
	}
	
	@Override
	public WorkFlowEntityIntf executeSingleAction(WorkFlowEntityIntf workFlowEntityIntf, Map variables, String action,WorkFlowActionReason actionReason)
			throws WorkFlowException {
	    this.executeSingleAction( workFlowEntityIntf,  variables,  action, actionReason,false);
	    return workFlowEntityIntf;
	}
	@Override
	public WorkFlowEntityIntf executeSingleAction(Long entityId,String entityCategory, Map variables, String action, String actionReason) throws WorkFlowException
	{
		WorkFlowEntityService entityService = this.getEntityServiceByCategoryCode(entityCategory);
		WorkFlowEntityIntf workFlowEntityIntf=entityService.getWorkFlowEntityByEntityId(entityId);
		workFlowEntityIntf= this.executeSingleAction(workFlowEntityIntf, variables,  action, actionReason,false);
	    return workFlowEntityIntf;
	}
	private void executeSingleAction(WorkFlowEntityIntf workFlowEntityIntf, Map variables, String action,WorkFlowActionReason actionReason, boolean resetWorkFLow)
			throws WorkFlowException {
		WorkFlowEntityService entityService = this.getEntityServiceByCategoryCode(workFlowEntityIntf.getEntityCategory());
		WorkFlowEntity wfEntity = this.getWorkFlowEntity(workFlowEntityIntf.getEntityId(), workFlowEntityIntf.getEntityCategory());
		//业务数据的状态如果是空，自动将设为 WST-NE
		if (CommonUtil.isNull(workFlowEntityIntf.getEntityStatus())) {
			workFlowEntityIntf.reSetEntityStatus(WfConstants.NULL_ENTITY_STATUS);
		}
		WorkFlowEntityIntf newEntity = null;
		if (wfEntity == null) {
			wfEntity = this.initEntityWorkFlow(workFlowEntityIntf,null);
		} else {
			//重设流程
			if (resetWorkFLow || WfConstants.NULL_ENTITY_STATUS.equals(workFlowEntityIntf.getEntityStatus() ) 
					|| WfConstants.WORKFLOW_ACTION_SUMBIT.equals(action) ) {
				Long workFlowId = entityService.getWorkFlowIdByEntity(workFlowEntityIntf);
				if (workFlowId == null || resetWorkFLow) {
					workFlowId = this.getWorkFlowIdByCategory(workFlowEntityIntf);
					workFlowEntityIntf.reSetWorkFlowId(workFlowId);
				}
				wfEntity = this.initEntityWorkFlow(workFlowEntityIntf,wfEntity);
				wfEntity.setWorkFlowId(workFlowId);
			}
		}		
 
 		ParseRootAndVariable prv = this.genParseRootAndVariable(workFlowEntityIntf, entityService,null);
		//WorkFlowDetail wfd = this.workFlowService.getNextWorkFlow(wfEntity.getWorkFlowId(), this.getRoles(), workFlowEntityIntf, variables, wfEntity.getEntityStatus(), action);
 		WorkFlowDetail wfd = this.workFlowService.getNextWorkFlow(wfEntity.getWorkFlowId(), this.getRoles(), prv, variables, wfEntity.getEntityStatus(), action);
		WorkFlowEntityActionLog actionLog = this.processAction(workFlowEntityIntf, variables,  wfd, wfEntity, action,actionReason);
 		this.createActionLogAndSendMsg(wfd, wfEntity, workFlowEntityIntf,entityService,actionLog);
	}
	
	


	private void createActionLogAndSendMsg(WorkFlowDetail wfd, WorkFlowEntity wfEntity,
			WorkFlowEntityIntf workFlowEntityIntf, WorkFlowEntityService entityService,
			WorkFlowEntityActionLog actionLog) { 
		    this.reSetTakeUpTime(actionLog);
			this.create(actionLog);// 创建工作流日志
			this.sendMessage(wfd, wfEntity, workFlowEntityIntf,entityService,actionLog); 
	}
	
	private void reSetTakeUpTime(WorkFlowEntityActionLog actionLog) {
		WorkFlowEntityActionLog lastActionLog = this.getLastWorkFlowEntityActionLog(actionLog.getWorkFlowEntityId());
		if (lastActionLog != null ) {
			lastActionLog.setTakeUpTime(DateUtil.getSecondBetween(lastActionLog.getCreateTime(), DateUtil.getCurrentDate()));
		    this.save(lastActionLog);
		}
	}

	@Override
	public WorkFlowOpenPageView genOpenPageView(Long workFlowEntityId, Long entityId, String entityCategory, Map variables)
			throws WorkFlowException {
		WorkFlowEntityIntf workFlowEntityIntf = getWorkFlowEntityIntfByWorkFlowEntityId(workFlowEntityId, entityId, entityCategory);
		if(CommonUtil.isNull(workFlowEntityIntf)){
			throw new ValidateException("单据还没有提交审核， 没有审批日志！");
		}
		WorkFlowOpenPageView pageView = this.genOpenPageView(workFlowEntityIntf, variables);
		return pageView;
	}
	
	@Override
	public List<WorkFlowActionButton> getEnableActions(String entityCategory, Long entityId, Map variables ){
		WorkFlowEntityIntf  intf = this.getWorkFlowEntityIntf(entityId, entityCategory);
		WorkFlowEntity wfsc = this.getWorkFlowEntity(entityId, entityCategory);
		if (wfsc == null) {
			wfsc = this.initEntityWorkFlow(intf,null);
		}
		List<WorkFlowDetail> list = this.findWorkFlowDetailsForView(wfsc.getWorkFlowId(), wfsc.getEntityStatus(), intf, wfsc);
		// actionCode锛屾彁绀轰俊鎭�
		Map<String, String> actionCodeMsg = new HashMap<String, String>();
		Map<String, WorkFlowDetail> actionCodeWfDetail = new HashMap<String, WorkFlowDetail>();
		List<WorkFlowActionButton> actionList = new ArrayList<WorkFlowActionButton>();
		for (WorkFlowDetail wf : list) {
//			if (hiddenFalseAction) {
//				if (this.workFlowService.isTrueWorkFlowDetail(workFlowEntity,variables, wf) && this.isCurrentRole(wf.getRole(), wfsc, YqSecurityUtils.getLoginUserKey())) {
//					actionCodeMsg.put(wf.getAction(), wf.getActionPreMsg());
//					actionCodeWfDetail.put(wf.getAction(), wf);
//				}
//			} else {
				if (CommonUtil.isNull(wf.getRole())) {
					if (this.workFlowService.isTrueWorkFlowDetail(intf,variables, wf)) {
						actionCodeMsg.put(wf.getAction(), wf.getActionPreMsg());
						actionCodeWfDetail.put(wf.getAction(), wf);
					}
				} else if (this.isCurrentRole(wf.getRole(), wfsc, YqSecurityUtils.getLoginUserKey())) {
					actionCodeMsg.put(wf.getAction(), wf.getActionPreMsg());
					actionCodeWfDetail.put(wf.getAction(), wf);
				}
//			}
			
		}
		List<LookupCode> lookupCodeList = lookupCodeService.findLookupCodeByCategory(WfConstants.WORK_FLOW_ACTION_CATEGORY);
		for (LookupCode lc : lookupCodeList) {
			if (actionCodeMsg.keySet().contains(lc.getKeyCode())) {
				WorkFlowActionButton button = new WorkFlowActionButton();
				button.setActionCode(lc.getKeyCode());
				button.setActionName(lc.getLookupName());
				button.setActionPreMsg(actionCodeMsg.get(lc.getKeyCode())); 
				WorkFlowDetail wf = actionCodeWfDetail.get(lc.getKeyCode());
				button.setReasonMandatory(wf.getReasonMandatory());
				//lc.setSkipPageDefineId(wf.getWorkFlowSkipPageDefineId());
				actionList.add(button);
			}
		}
		return actionList;
	}
	
	@Override
	public WorkFlowOpenPageView genOpenPageView(WorkFlowEntityIntf workFlowEntity, Map variables)
			throws WorkFlowException {
		WorkFlowOpenPageView openView = new WorkFlowOpenPageView();
		WorkFlowEntity wfsc = this.getWorkFlowEntity(workFlowEntity.getEntityId(), workFlowEntity.getEntityCategory());
		if (wfsc == null) {
			wfsc = this.initEntityWorkFlow(workFlowEntity,null);
		}
		WorkFlowEntityService entityService = this.getEntityServiceByCategoryCode(workFlowEntity.getEntityCategory());
		ParseRootAndVariable pr = this.genParseRootAndVariable(workFlowEntity, entityService, null);
		if (CommonUtil.isNotNull(variables)) {
			pr.getVariable().putAll(variables);
		}
		WorkFlow workFlow = this.workFlowService.getWorkFlowById(wfsc.getWorkFlowId());
		openView.setWorkFlow(workFlow); // 设流程
		List<WorkFlowDetail> list = this.findWorkFlowDetailsForView(wfsc.getWorkFlowId(), wfsc.getEntityStatus(), workFlowEntity, wfsc);
		// actionCode，提示信息
		Map<String, String> actionCodeMsg = new HashMap<String, String>();
		Map<String, WorkFlowDetail> actionCodeWfDetail = new HashMap<String, WorkFlowDetail>();
		boolean readOnly = false;
		List<LookupCode> actionList = new ArrayList<LookupCode>();
		for (WorkFlowDetail wf : list) {
			if (wf.isReadOnly()) {
				readOnly = true;
			}
			if (CommonUtil.isNull(wf.getRole())) {
				if (this.workFlowService.isTrueWorkFlowDetail( pr, wf)) {
					actionCodeMsg.put(wf.getAction(), wf.getActionPreMsg());
					actionCodeWfDetail.put(wf.getAction(), wf);
				}
			} else if (this.isCurrentRole(wf.getRole(), wfsc, YqSecurityUtils.getLoginUserKey()))  {
				if (this.workFlowService.isTrueWorkFlowDetail( pr, wf)) {
					actionCodeMsg.put(wf.getAction(), wf.getActionPreMsg());
					actionCodeWfDetail.put(wf.getAction(), wf);
				} 
			}
		}
		if (list.size() == 0) {
			readOnly = true;
		}

		// 有加签，要自动加上同意不同的操作 //不处理
		// if ( !CommonUtil.isNull(wfv.getClaim().getAddedApprovers()) &&
		// wfv.getClaim().getAddedApprovers().indexOf(CASecurityUtil.getCurrUserCode())
		// >-1) { //如果是加签，hardcode action
		// actionCodeSet.add("WAT-FINAP");
		// actionCodeSet.add("WAT-NOTAP");
		// view.setReadOnly(true);
		// }
		List<LookupCode> lookupCodeList = lookupCodeService.findLookupCodeByCategory(YqLCConstants.WORK_FLOW_ACTION_CATEGORY);
		for (LookupCode lc : lookupCodeList) {
			if (actionCodeMsg.keySet().contains(lc.getKeyCode())) {
				lc.setActionPreMsg(actionCodeMsg.get(lc.getKeyCode()));
				WorkFlowDetail wf = actionCodeWfDetail.get(lc.getKeyCode());
				lc.setReasonMandatory(wf.getReasonMandatory());
				lc.setSkipPageDefineId(wf.getWorkFlowSkipPageDefineId());
				actionList.add(lc);
			}
		}
		openView.setReadOnly(readOnly);
		//当前操作是系统， 不显示操作按钮
		//if (wfsc.getIsSysAction() == null || !wfsc.getIsSysAction()) {
			openView.setEnableActions(actionList);
		//}
		
		// List<WorkFlowEntityActionLog> actionLogList = this.find(" from
		// WorkFlowEntityActionLog where EntityId = ? and EntityCategory = ?
		// order by id
		// ",workFlowEntity.getEntityId(),workFlowEntity.getEntityCategory() );
		List<WorkFlowEntityActionLog> actionLogList = this.findActionLog(workFlowEntity.getEntityId(), workFlowEntity.getEntityCategory(),null);
		openView.setActionLogList(actionLogList);
		List<WorkFlowGraphDetail> graphDetail = this.workFlowService.findGraphDetailByWorkFlowId(wfsc.getWorkFlowId());
		openView.setGraphDetail(graphDetail);
		
		//this.resetGraphByStatus(actionLogList, graphDetail); // 重设graphNode, 不用保持的， 可能错了
		
		//生成openUrl
		WorkFlowEntityCategory category = this.workFlowService.getCategoryByCategoryCode(workFlowEntity.getEntityCategory());
		String openUrl = category.getOpenUrl();
		openUrl = openUrl.replace("{id}", workFlowEntity.getEntityId().toString());
	
		openView.setOpenUrl(openUrl);
		this.resetGraphDetailActionByLog(actionLogList, graphDetail);
		return openView;
	}

	// 为了改变经常设错的问题，改为不用图节点， 只用状态， 然后根据状态取节点
		private void resetGraphByStatus(
				List<WorkFlowEntityActionLog> actionLogList,
				List<WorkFlowGraphDetail> graphDetail) {
			Map<String, WorkFlowGraphDetail> statusMap = new HashMap<String, WorkFlowGraphDetail>();
			for (WorkFlowGraphDetail d : graphDetail) {
				List<String> staList = d.getSourceStatusesList();
				if (CommonUtil.isNotNull(staList)) {
					for (String s : staList) {
						statusMap.put(s, d);
					}
				}
			}
			if (!CommonUtil.isNull(actionLogList)) {
				for (int i = actionLogList.size() - 1; i >= 0; i--) {
					WorkFlowEntityActionLog aLog = actionLogList.get(i);
					String currNodeCode = null;
					String preNodeCode = null;
					WorkFlowGraphDetail currNodeDetail = statusMap.get(aLog
							.getCurrentStatus());
					WorkFlowGraphDetail preNodeDetail = statusMap.get(aLog.getPreStatus());
					if (currNodeDetail != null) {
						currNodeCode = currNodeDetail.getGraphNode();
					} else {
						throw new ValidateException("流程图设置有误，状态 : "
								+ aLog.getCurrentStatus() + "-"
								+ aLog.getCurrentStatusName() + " 包含在任何节点!");
					}
					if (preNodeDetail != null) {
						preNodeCode = preNodeDetail.getGraphNode();
					} else {
						throw new ValidateException("流程图设置有误，状态 : "
								+ aLog.getPreStatus() + "-"
								+ aLog.getPreStatusName() + " 包含在任何节点! ");
					}
					aLog.setPreGraphNode(preNodeCode);
					aLog.setCurrentGraphNode(currNodeCode);

				}
			}
		}
		
	@Override
	public List<WorkFlowEntityActionLog> findActionLog(Long entityId, String entityCategory,String currentStatus) {

		StringBuffer otherCols = new StringBuffer();
		otherCols.append(" actlc.lookup_name actionName,")
				.append(" (SELECT lc.lookup_name FROM yq_lookup_code lc WHERE lc.key_code = alog.role ) roleName,")
				.append(" (SELECT lc.lookup_name FROM yq_lookup_code lc WHERE lc.key_code = alog.pre_status ) preStatusName,")
				.append(" (SELECT lc.lookup_name FROM yq_lookup_code lc WHERE lc.key_code = alog.current_status ) currentStatusName ")
				.append(" , actlc.segment4 color ");
		StringBuffer mixSql = new StringBuffer();
		mixSql.append("SELECT alog.*,")
				.append( otherCols)
				.append(" FROM yq_work_flow_Entity_action_log alog LEFT JOIN yq_lookup_code actlc ON alog.action = actlc.key_code")
				.append(" WHERE alog.Entity_category = '" + entityCategory + "' ")
				.append(" AND alog.Entity_id = " + entityId );
		if (CommonUtil.isNotNull(currentStatus)) {
			mixSql.append(" and alog.current_status = '" + currentStatus + "' ");
		}
		
//		if(WfConstants.CONSIGNMENT_WORKFLOW_ENTITY_CATEGORY.equals(entityCategory)){
//			mixSql.append(" AND alog.pre_status <> '" + WfConstants.NULL_ENTITY_STATUS + "' ");
//		}
		mixSql.append(" ORDER BY alog.id ");
		return this.sqlToModelService.executeNativeQuery(mixSql.toString(), null, WorkFlowEntityActionLog.class);
	}

	@Override
	public WorkFlowEntity getWorkFlowEntity(Long entityId, String entityCategory) {
		String query = " SELECT "
				+ JPAUtils.genEntityColsForMysql(WorkFlowEntity.class,
						"wfe", null)
				+ ", slc.lookup_name entityStatusDsp"
				+ ", wf.work_flow_name workFlowName  "
		        + " FROM yq_work_flow_entity wfe" 
		        + " LEFT JOIN yq_lookup_code slc ON slc.key_code = wfe.entity_status" 
		        + " LEFT JOIN yq_work_flow wf ON wf.id = wfe.work_flow_id " 
		        +" where entity_Id = "+entityId
		        +"   and Entity_Category = '"+entityCategory+"' ";
		return this.sqlToModelService.getSingleRecord(query, null, WorkFlowEntity.class);		
				//return (WorkFlowEntity) this.getSingleRecord(" from WorkFlowEntity where entityId = ? and entityCategory = ? ", EntityId, EntityCategory);
	}
	private WorkFlowEntity initEntityWorkFlow(WorkFlowEntityIntf workFlowEntityInft, WorkFlowEntity oldWfEntity) {
		WorkFlowEntityService entityService = this.getEntityServiceByCategoryCode(workFlowEntityInft.getEntityCategory());
		WorkFlowEntity wfEntity = null;
		if (oldWfEntity == null) {
			wfEntity = new WorkFlowEntity();
		} else {
			wfEntity = oldWfEntity;
		}
		
		// 如果流程ID为空，可以考虑用EntityCategory取
		Long workFlowId = entityService.getWorkFlowIdByEntity(workFlowEntityInft);
		if (workFlowId == null) {
			workFlowId = this.getWorkFlowIdByCategory(workFlowEntityInft);
			workFlowEntityInft.reSetWorkFlowId(workFlowId);
		}
		wfEntity.setWorkFlowId(workFlowId);
		wfEntity.setEntityNumber(workFlowEntityInft.getEntityNumber());
		wfEntity.setEntityDescription(CommonUtil.subTextString(workFlowEntityInft.getEntityDescription(), 400) );
		wfEntity.setEntityCategory(workFlowEntityInft.getEntityCategory());
		wfEntity.setEntityId(workFlowEntityInft.getEntityId());
		wfEntity.setEntityStatus(workFlowEntityInft.getEntityStatus());
		wfEntity.setGraphNode(this.getGraphByStatus(workFlowId, workFlowEntityInft.getEntityStatus()));// 稍后处理
		wfEntity.setIsClosed(false);
		WorkFlow wf = this.workFlowService.getWorkFlowById(workFlowId);
		if (wf == null) {
			throw new ValidateException("工作流ID【" + workFlowId + "】不存在，请联系管理员！");
		}
		this.resetAttribute(wfEntity, workFlowEntityInft);
		this.resetSpecRoleUsers(wfEntity,workFlowEntityInft,wfEntity.getSpecRoleUser(),workFlowEntityInft.getSpecRoles());
		return wfEntity;
	}
	
	private void resetAttribute(WorkFlowEntity wfEntity, WorkFlowEntityIntf workFlowEntityInft) {
		WorkFlowEntityCategory cate = this.workFlowService.getCategoryByCategoryCode(workFlowEntityInft.getEntityCategory());
		String[][] attArr = cate.getAttributeDef();
		if (CommonUtil.isNotNull(attArr)) {
			for (String[] attDef: attArr) {
				String attributeName = "attribute"+attDef[0];
				String entityPropertyName = attDef[1];
				Object value = YqBeanUtil.getPropertyValue(workFlowEntityInft, entityPropertyName);
				String attValue = null;
				if (value != null) {
					attValue = value.toString();
				}
				YqBeanUtil.setProperty(wfEntity, attributeName, attValue);
			}
		}
	}
	
	private String getGraphByStatus(Long workFlowId, String status) {
		return (String)this.getSingleValueByNativeQuery("SELECT graph_node  FROM " +
				" yq_work_flow_graph_detail WHERE work_flow_id = ? AND source_statuses LIKE CONCAT('%','"+status+"','%')", workFlowId);
	}

	/**
	 * 根据时间，条件决定
	 * @param EntityCategory
	 * @return
	 */
	private Long getWorkFlowIdByCategory(WorkFlowEntityIntf workFlowEntityInft ) {
		String EntityCategory = workFlowEntityInft.getEntityCategory(); 
		String currDateStr = "'"+DateUtil.getCurrentStringDate()+"'";
		String query = " select " + JPAUtils.genEntityColsForMysql(WorkFlow.class, "wf", "")+" from YQ_WORK_FLOW wf "
				+" where wf.category_code = '"+EntityCategory+"' "
				+ " and ifNull(DATE_FORMAT(wf.start_date,'%Y-%m-%d'),"+currDateStr+") <="+currDateStr
				+ " and ifNull(DATE_FORMAT(wf.end_date,'%Y-%m-%d'),"+currDateStr+") >="+currDateStr;
		List<WorkFlow> list = this.sqlToModelService.executeNativeQuery(query, null, WorkFlow.class);
		if (CommonUtil.isNull(list)) {
			throw new ValidateException("未设置默认工作流，请联系管理员！");
		}
		List<WorkFlow> selectedWF = new ArrayList<WorkFlow>();
		Map<String, ParseElement> elements = this.parseService.getElementsByUseCategory(workFlowEntityInft.getEntityCategory());
		WorkFlowEntityService entityService = this.getEntityServiceByCategoryCode(workFlowEntityInft.getEntityCategory());
		for (WorkFlow wf : list) {
			ParseRootAndVariable prv = this.genParseRootAndVariable(workFlowEntityInft, entityService,null);
			if( CommonUtil.isNull(wf.getUseRelationFunction()) || this.parseService.isTrueByEleNumberWithPars(prv, wf.getUseRelationFunction(), elements) ) {
            	selectedWF.add(wf);
            }
		}
		if (CommonUtil.isNull(selectedWF)) {
			throw new ValidateException("未找到符合条件的工作流！");
		} else if (selectedWF.size() > 1 ){
			String wfs = "";
			for (WorkFlow wf : selectedWF) {
				wfs = wfs+wf.getWorkFlowNumber()+"-"+wf.getWorkFlowName()+",\r\n";
			}
			throw new ValidateException("设置有误， 找到多过1条的工作流， "+wfs);
			
		}
		return selectedWF.get(0).getId();
	}

	private WorkFlowEntityActionLog processAction(WorkFlowEntityIntf workFlowEntityIntf, Map variables,
			WorkFlowDetail wfd, WorkFlowEntity workFlowEntity, String action, WorkFlowActionReason actionReason) {
		WorkFlowEntityService entityService = this.getEntityServiceByCategoryCode(workFlowEntityIntf.getEntityCategory());
		//String username = SecurityContextHolder.getContext().getAuthentication().getName();
		String username = "开发者";
		if (WorkFlowUtil.roleUsersContainsRole(workFlowEntity.getSpecRoleUser(), wfd.getRole())
				&& !WorkFlowUtil.roleUsersContainsRoleUser(workFlowEntity.getSpecRoleUser(), wfd.getRole(), username)) {
			throw new ValidateException("您无权审核此单!");
		}

		if (wfd.getReasonMandatory() && this.isNullActionReason(actionReason)) {
			WorkFlowException wfe = new WorkFlowException(WorkFlowException.NO_ACTION_REASON);
			wfe.setActionReasonTitle(wfd.getReasonTitle());
			throw wfe;
		}
 		String preStatus = workFlowEntity.getEntityStatus();
		String preGraphStatus = workFlowEntity.getGraphNode();
		if (!CommonUtil.isNull(wfd.getNextStatus())) {
			workFlowEntity.setEntityStatus(wfd.getNextStatus());
			workFlowEntityIntf.reSetEntityStatus(wfd.getNextStatus());
			workFlowEntity.setGraphNode(wfd.getNextGraphNode());
			workFlowEntity.setIsSysAction(wfd.getNextIsSysAction());
		}
		
 		if (CommonUtil.isNotNull(wfd.getExecuteFunction())) { 
 			this.executeFunction(this.genParseRootAndVariable(workFlowEntityIntf, entityService,wfd), wfd.getExecuteFunction());
 
		}  
 
 		if ( this.getEndWorkFlow(wfd.getWorkFlowId(), wfd.getNextStatus()) )  { // 移动到历史库
 			if (wfd.getNotExeCloseEntity() != null && wfd.getNotExeCloseEntity() ) { // 不执行， 默认一般都执行
 				workFlowEntityIntf = entityService.saveEntity(workFlowEntityIntf,wfd);
 			} else {
 				workFlowEntityIntf = entityService.closedEntity(workFlowEntityIntf,wfd);
 			} 
			workFlowEntity.setIsClosed(true);
		}   else {
			
   			workFlowEntityIntf = entityService. saveEntity(workFlowEntityIntf,wfd);
 		}
 
		if (workFlowEntityIntf.getEntityId() == null
				|| workFlowEntityIntf.getEntityId() == 0l) {
			throw new ValidateException("无返回数据源ID,不可审批!");
		}
		if (workFlowEntity.getEntityId() == null || workFlowEntity.getEntityId() == 0l) {
			workFlowEntity.setEntityId(workFlowEntityIntf.getEntityId());
		}
		workFlowEntity.setLastActionCode(action);
		workFlowEntity.setEntityNumber(workFlowEntityIntf.getEntityNumber()); 
		if ( actionReason != null) {
			workFlowEntity.setActionReason(actionReason.getActionReason());
			workFlowEntity.setReasonType(actionReason.getReasonType());
		}
		if (WfConstants.WORKFLOW_ACTION_SUMBIT.equals(action)) {
			workFlowEntity.setSubmitDate(DateUtil.getCurrentDate());
			workFlowEntity.setSubmitUser(YqSecurityUtils.getLoginUserKey());
			this.resetSpecRoleUsers(workFlowEntity,workFlowEntityIntf, workFlowEntity.getSpecRoleUser(),workFlowEntityIntf.getSpecRoles());
			String name = YqSecurityUtils.getLoginUserDisplay();
  			workFlowEntity.setSubmitDisplayName(name);
		}
		
		if (workFlowEntity.getIsClosed() != null && workFlowEntity.getIsClosed() ) {
			if (workFlowEntity.getId() == null || workFlowEntity.getId() == 0l ) {
				workFlowEntity = this.save(workFlowEntity);
			}
			this.insertToHistory(workFlowEntity);
			this.delete(workFlowEntity);
		} else {
			workFlowEntity = this.save(workFlowEntity);
		}		
		// 保存action log
		WorkFlowEntityActionLog  actionLog = this.genActionLog(workFlowEntityIntf, workFlowEntity, wfd, preStatus, preGraphStatus, action, actionReason);
		return actionLog;
	}

	
	private void sendMessage(WorkFlowDetail wfd,WorkFlowEntity workFlowEntity, WorkFlowEntityIntf workFlowEntityIntf,WorkFlowEntityService entityService,WorkFlowEntityActionLog  actionLog) {
		WorkFlowMessageEvent event = new WorkFlowMessageEvent(workFlowMessageService, wfd, workFlowEntityIntf, workFlowEntity, entityService,actionLog);
	     //暂时注释	MethodInvocationContextHolder.getContext()空指针异常
		//ApplicationEventPublishers.afterTransaction().publishEvent(event);
	}
	

	private WorkFlowEntityActionLog genActionLog(WorkFlowEntityIntf workFlowEntity, WorkFlowEntity wfsc, WorkFlowDetail wfd, 
			String preStatus, String preGraphStatus, String action, WorkFlowActionReason actionReason) {
		WorkFlowEntityActionLog wfsa =  new WorkFlowEntityActionLog();
		//原因的特殊输入，可在WorkFlowEntity中加原因对象
		wfsa.setWorkFlowEntityId(wfsc.getId());
		wfsa.setEntityId(workFlowEntity.getEntityId());
		wfsa.setEntityCategory(workFlowEntity.getEntityCategory());
		wfsa.setRole(wfd.getRole());
		wfsa.setActionByCode(YqSecurityUtils.getLoginUserKey());
	    wfsa.setActionByName(YqSecurityUtils.getLoginUserDisplay());
		wfsa.setPreStatus(preStatus);
		wfsa.setPreGraphNode(preGraphStatus);
		wfsa.setCurrentStatus(wfsc.getEntityStatus());
		wfsa.setCurrentGraphNode(wfsc.getGraphNode());
		wfsa.setAction(action);
		wfsa.setActionDate(DateUtil.getCurrentDate());
		if (actionReason != null) {
			wfsa.setActionReason(actionReason.getActionReason());
			wfsa.setReasonType(actionReason.getReasonType());
		}
 
		return wfsa;
	}
	
	private WorkFlowEntityActionLog getLastWorkFlowEntityActionLog(Long workFlowEntityId) {
		String query = " select " + JPAUtils.genEntityCols(WorkFlowEntityActionLog.class, "a", null)
		+" from YQ_Work_Flow_ENTITY_Action_Log a "
		+" where a.id = "
		+ " (select max(b.id) from YQ_Work_Flow_ENTITY_Action_Log b"
		+ " where b.WORK_FLOW_ENTITY_ID = "+workFlowEntityId+" ) ";
		return this.sqlToModelService.getSingleRecord(query, null, WorkFlowEntityActionLog.class);
	}

	private List<WorkFlowDetail> findWorkFlowDetailsForView(Long workFlowId, String EntityStatus, WorkFlowEntityIntf workFlowEntity, WorkFlowEntity wfsc) {
		String[] roles = this.getRoles(); 
		List<WorkFlowDetail> list = this.workFlowService.findWorkFlowDetails(workFlowId, roles, EntityStatus);
		String currentUserCode =YqSecurityUtils.getLoginUserKey();
		// 特殊角色处理,只支持特定角色特定人
		if (!CommonUtil.isNull(workFlowEntity.getSpecRoles())) {
			if (!CommonUtil.isNull(list)) {
				for (int i = list.size() - 1; i >= 0; i--) {
					String role = list.get(i).getRole();
					if (WorkFlowUtil.roleUsersContainsRole(wfsc.getSpecRoleUser(), role)
							&& !WorkFlowUtil.roleUsersContainsRoleUser(wfsc .getSpecRoleUser(), role, currentUserCode)) {
						list.remove(i);
					}  
				}
			}
		} 
		if (!CommonUtil.isNull(list)) {
			for (int i = list.size() - 1; i >= 0; i--) {
				if (list.get(i).getNextIsSysAction() != null && list.get(i).getNextIsSysAction()) {
					list.remove(i);
				}
			}
		} 
 		return list;
	}

 

	private void resetGraphDetailActionByLog(List<WorkFlowEntityActionLog> actionLogList, List<WorkFlowGraphDetail> graphDetail) {
		if ( CommonUtil.isNull(graphDetail) ) {
			return;
		}
		if (!CommonUtil.isNull(actionLogList)) {
			Map<String, WorkFlowEntityActionLog> acMap = new HashMap<String, WorkFlowEntityActionLog>();
			String currNode = actionLogList.get(actionLogList.size() - 1).getCurrentGraphNode();
			for (int i = actionLogList.size() - 1; i >= 0; i--) {
				WorkFlowEntityActionLog aLog = actionLogList.get(i);
				if (WfConstants.WORK_FLOW_GRAPH_START.equals(aLog.getCurrentGraphNode())) {
					break;
				} else {
					acMap.put(aLog.getPreGraphNode(), aLog); 
					 //acMap.put(aLog.getCurrentGraphNode(), aLog);
				}
			}
			for (WorkFlowGraphDetail gDetail : graphDetail) {
				if (currNode.equals(gDetail.getGraphNode())) {
					gDetail.setLastNode(true);
					break;
				} else {
					WorkFlowEntityActionLog aLog = acMap.get(gDetail.getGraphNode());
					gDetail.setActionLog(aLog);
				}
			}
		} else {
			graphDetail.get(0).setLastNode(true);
		}
	}

	@Override
	public void executeBatchAction(List<WorkFlowEntity> workFlowEntities, Map variables , String action, WorkFlowActionReason actionReason) 
			throws WorkFlowException {
		if (CommonUtil.isNull(workFlowEntities)) {
			return;
		}
		Map<String,List<Long>> categoryIds = new HashMap<String,List<Long>>();
		for (WorkFlowEntity ec : workFlowEntities) {
			List<Long> ids = categoryIds.get(ec.getEntityCategory());
			if (ids == null) {
				ids = new ArrayList<Long>();
				categoryIds.put(ec.getEntityCategory(), ids);
			}
			ids.add(ec.getEntityId());
		}
		Iterator it = categoryIds.keySet().iterator();
		while (it.hasNext()) {
			String categoryCode = (String)it.next();
			List<Long> ids = categoryIds.get(categoryCode);
			WorkFlowEntityService wfs = this.getEntityServiceByCategoryCode(categoryCode);
			List<WorkFlowEntityIntf> wfes = wfs.findWorkFlowEntityByEntityIds((Long[])ids.toArray());
			for (WorkFlowEntityIntf wfe : wfes) {
				this.executeSingleAction(wfe, variables, action,actionReason);
			}
		}
	}

	private void resetSpecRoleUsers(WorkFlowEntity workFlowEntity, WorkFlowEntityIntf workFlowEntityInft, String oldSpecRoleUser,WorkFlowSpecRole[] specRoles) {
		if (CommonUtil.isNull(specRoles)) {
			return ;
		}
		String roleUsers = "";
		for (WorkFlowSpecRole wfr : specRoles) {
			String userCodes = (String) YqBeanUtil.getPropertyValue(workFlowEntityInft, wfr.getUserProperty());
			if (CommonUtil.isNotNull(userCodes)) {
			   for(String str:userCodes.split(",")) {
				   roleUsers +=","+ WorkFlowUtil.roleUserstoString(wfr.getRole(), str);
			   }
			   
			} 
		}
		if (CommonUtil.isNotNull(roleUsers)) {
			workFlowEntity.setSpecRoleUser(roleUsers.substring(1));
		} 
	}
	
	/**
	 * 待定
	 * @return
	 */
	private String[] getRoles() {
		Set<String> roleSet = YqSecurityUtils.getWorkFlowRoleResource();
		if (CommonUtil.isNotNull(roleSet)) {
			String[] roleArr = new String[roleSet.size()];
			int i=0;
			for (String r : roleSet) {
				roleArr[i] = r;
				i++;
			}
			return roleArr;
		} else if ("WFSYS".equals(YqSecurityUtils.getLoginUserKey())) { // 系统用户,
			return new String[]{WfConstants.WORK_FLOW_SYS_ROLE.JOB_ROLE };
		}
		return new String[]{};
	}
	
	private WorkFlowEntityService getEntityServiceByCategoryCode(String categoryCode) {
		WorkFlowEntityCategory cat = this.getEntityCategoryByCode(categoryCode);
		//WorkFlowEntityService service = SpringUtil.getBean(cat.getServiceName(), WorkFlowEntityService.class);
		WorkFlowEntityService service = this.applicationContext.getBean(cat.getServiceName(), WorkFlowEntityService.class);
		return service;
	}
	
	private WorkFlowEntityCategory getEntityCategoryByCode(String categoryCode) {
 
		WorkFlowEntityCategory c = this.workFlowService.getCategoryByCategoryCode(categoryCode);
		if ( c == null) {
			throw new ValidateException("业务类型代码【"+categoryCode+"】不存在 !");
		}
		return c;
	}
	
	private boolean isNullActionReason(WorkFlowActionReason actionReason) {
		if (actionReason == null || CommonUtil.isNull(actionReason.getActionReason())) {
			return true;
		}
		return false;
	}

	@Override
	public void executeBatchAction(List<WorkFlowEntity> workFlowEntities, Map variables, String action, String actionReason)
			throws WorkFlowException {
		WorkFlowActionReason ar = null;
		if (CommonUtil.isNotNull(actionReason)) {
			ar = new WorkFlowActionReason();
			ar.setActionReason(actionReason);
		}
		this.executeBatchAction(workFlowEntities, variables, action, ar);		
	}
	
	/**
	 * 可能后面再redis中的有标记
	 */
	@Override
	public void sendToWorkFlow(WorkFlowEntityIntf workFlowEntityIntf)
			throws WorkFlowException {
		String action =  WfConstants.WORKFLOW_ACTION_SUMBIT ;
		WorkFlowEntityService entityService = this.getEntityServiceByCategoryCode(workFlowEntityIntf.getEntityCategory());
		WorkFlowEntity oldEntity = this.getWorkFlowEntity(workFlowEntityIntf.getEntityId(), workFlowEntityIntf.getEntityCategory());
		boolean inHistory = false;
		if (oldEntity == null) {
			//看是否存在历史表中
			oldEntity = this.getHistoryWorkFlowEntity(workFlowEntityIntf.getEntityId(), workFlowEntityIntf.getEntityCategory());
			if (oldEntity != null) {
				inHistory = true;
				this.deleteHistoryWorkFlowEntityById(oldEntity.getId());
			}
		}
 		this.executeSingleAction(workFlowEntityIntf, null, action,""); 
	}
	
	
	
	/**
	 * 取历史数据
	 * @param EntityId
	 * @param EntityCategory
	 * @return
	 */
	private WorkFlowEntity getHistoryWorkFlowEntity(Long EntityId, String EntityCategory) {
		String historyTable = "YQ_Work_FLOW_ENTITY_HISTORY";
		String query = "Select " + JPAUtils.genEntityColsForMysql(WorkFlowEntity.class, "a", "") 
				+" from YQ_Work_FLOW_ENTITY_HISTORY a "
				+ " where a.entity_id = "+EntityId
				+ "  and a.entity_category = '"+EntityCategory+"'";
		List<WorkFlowEntity> list = this.sqlToModelService.executeNativeQuery(query, null, WorkFlowEntity.class);
		if (CommonUtil.isNull(list)) {
			return null;
		}
		return list.get(0);
	}
	private void deleteHistoryWorkFlowEntityById(Long id) {
		this.executeNativeQuery(" delete from YQ_Work_FLOW_ENTITY_HISTORY where id = ?",id);
	}

	/**
	 * entities 对象必须是同一类对象
	 */
	@Override
	public void executeBatchActionByIntf(List<WorkFlowEntityIntf> entities,
			Map variables, String action,  WorkFlowActionReason actionReason)
			throws WorkFlowException {
		if (CommonUtil.isNull(entities)) {
			return;
		}
		Map<Long,WorkFlowEntityIntf> entityIntfMap = new HashMap<Long,WorkFlowEntityIntf>();
		String category = entities.get(0).getEntityCategory();
		for (WorkFlowEntityIntf f : entities) {
			if (!category.equals(f.getEntityCategory())) {
				throw new ValidateException("批量实体审批必须是同一种类型的单！");
			}
			entityIntfMap.put(f.getEntityId(), f);
			
		}
		List<WorkFlowEntity> wfEntities = this.getWorkFlowEntityByIntf(entities);
		if (wfEntities == null || wfEntities.size() != entities.size()) {
			throw new ValidateException("批量操作的数据必须都已提交！");
		}
		Map<String, List<WorkFlowDetail>> wfdsMap = new HashMap<String, List<WorkFlowDetail>>();
		String[] roles =  this.getRoles();
		for (WorkFlowEntity wfEntity : wfEntities) {
			WorkFlowEntityIntf entityIntf = entityIntfMap.get(wfEntity.getEntityId());
			WorkFlowEntityService entityService = this.getEntityServiceByCategoryCode(entityIntf.getEntityCategory());
			Long workFlowId = wfEntity.getWorkFlowId();
			String key = workFlowId +"=="+wfEntity.getEntityStatus();
			List<WorkFlowDetail> wfds = wfdsMap.get(key);
			if (wfds == null) {
				wfds = this.workFlowService.findWorkFlowDetails(workFlowId, roles,wfEntity.getEntityStatus(), action);
				if ( CommonUtil.isNull(wfds)) {
					String msg = this.workFlowService.genErrorMsg(workFlowId, roles, wfEntity.getEntityStatus(), action, "未设置状态，操作和角色的工作流！");
				    throw new ValidateException(msg);
				}
				wfdsMap.put(key, wfds);
			}
			ParseRootAndVariable prv = this.genParseRootAndVariable(entityIntf, entityService,null);
			WorkFlowDetail wfd = this.workFlowService.getNextWorkFlow(workFlowId, roles, prv, variables, wfEntity.getEntityStatus(), action, wfds); // getNextWorkFlow(workFlowId, roles, entityIntf, variables, wfEntity.getEntityStatus(), actionReason, wfds);
			WorkFlowEntityActionLog actionLog = this.processAction(entityIntf, variables, wfd, wfEntity, action, actionReason);
			this.createActionLogAndSendMsg(wfd, wfEntity, entityIntf, entityService, actionLog);
		}

	}
	/**
	 * 同事校验， 如果不同类抛exception 
	 * @param entities
	 * @return
	 * @throws WorkFlowException
	 */
	private List<WorkFlowEntity> getWorkFlowEntityByIntf(List<WorkFlowEntityIntf> entities)
			throws WorkFlowException {
		String ids = "";
		int len = 0; // 做多1500 笔查一次
		 List<WorkFlowEntity> listAll = new ArrayList<WorkFlowEntity>();
		for (WorkFlowEntityIntf f : entities ) {
			ids = ids+","+f.getEntityId();
			if (len >1500) {
				List<WorkFlowEntity> list = this.find(" from WorkFlowEntity e where e.entityCategory = ? and e.entityId in ("+ids.subSequence(0, ids.length()-1)+")",entities.get(0).getEntityCategory());
				listAll.addAll(listAll);
				len = 0;
				ids="";
			} else {
				len = len +1;
			}
		}
		if (len >0) {
			List<WorkFlowEntity> list = this.find(" from WorkFlowEntity e where e.entityCategory = ? and e.entityId in ("+ids.subSequence(0, ids.length()-1)+")",entities.get(0).getEntityCategory());
			listAll.addAll(listAll);
		}
		
		return listAll;
	}

	@Override
	public void executeBatchByWfEntityIdsAction(
			List<Long> wfEntityIds, Map variables, String action,
			String actionReason, List<WorkFlowEntityIntf> wfIntfList)
			throws WorkFlowException {
		if (CommonUtil.isNull(wfEntityIds)) {
			return;
		}
		String s = "";
		for (Long id : wfEntityIds) {
			s=s+","+id;
		}
		s = s.substring(1);
		String query ="select "+JPAUtils.genEntityCols(WorkFlowEntity.class, "a", null)+" from YQ_Work_FLOW_ENTITY a where a.id in ("+s+") ";
		List<WorkFlowEntity> list = this.sqlToModelService.executeNativeQuery(query, null, WorkFlowEntity.class);
		this.executeBatchAction(list, variables, action, actionReason, wfIntfList);
	}
	@Override
	public void executeBatchAction(List<WorkFlowEntity> workFlowEntities, Map variables, String action, String actionReason, List<WorkFlowEntityIntf> wfIntfList)
			throws WorkFlowException {
		WorkFlowActionReason ar = null;
		if (CommonUtil.isNotNull(actionReason)) {
			ar = new WorkFlowActionReason();
			ar.setActionReason(actionReason);
		}
		this.executeBatchAction(workFlowEntities, variables, action, ar, wfIntfList);		
	}
	@Override
	public void executeBatchAction(List<WorkFlowEntity> workFlowEntities, Map variables , String action, 
			WorkFlowActionReason actionReason, List<WorkFlowEntityIntf> wfIntfList) 
			throws WorkFlowException {
		if (CommonUtil.isNull(workFlowEntities)) {
			return;
		}
		Map<String,List<Long>> categoryIds = new HashMap<String,List<Long>>();
		for (WorkFlowEntity ec : workFlowEntities) {
			List<Long> ids = categoryIds.get(ec.getEntityCategory());
			if (ids == null) {
				ids = new ArrayList<Long>();
				categoryIds.put(ec.getEntityCategory(), ids);
			}
			ids.add(ec.getEntityId());
		}
		Iterator it = categoryIds.keySet().iterator();
		while (it.hasNext()) {
			String categoryCode = (String)it.next();
			List<Long> ids = categoryIds.get(categoryCode);
			WorkFlowEntityService wfs = this.getEntityServiceByCategoryCode(categoryCode);
			if (CommonUtil.isNotNull(wfIntfList)) {
				for (WorkFlowEntityIntf wfe : wfIntfList) {
					this.executeSingleAction(wfe, variables, action,actionReason);
				}
			} else {
				List<WorkFlowEntityIntf> wfes = wfs.findWorkFlowEntityByEntityIds((Long[]) ids.toArray(new Long[0]));
				if (wfes == null) { //没有实现 findWorkFlowEntityByEntityIds 接口， 就用单个的接口， 为了性能的考量， 
					for ( Long entityId : ids) {
						WorkFlowEntityIntf wfe = wfs.getWorkFlowEntityByEntityId(entityId);
						this.executeSingleAction(wfe, variables, action,actionReason);
					}
				} else {
					for (WorkFlowEntityIntf wfe : wfes) {
						this.executeSingleAction(wfe, variables, action,actionReason);
					}
				}
				
			}
		}
	}
	
	/**
	 * 员工是当前角色
	 */
	private boolean isCurrentRole(String role, WorkFlowEntity workFlowEntity, String userCode ) {
		//其他就为角色
		if (WfConstants.WORK_FLOW_SYS_ROLE. CREATED_BY.equals(role)) {
			return userCode.equals(workFlowEntity.getSubmitUser()); 
		}
		//最后审批人（系统）
		if (WfConstants.WORK_FLOW_SYS_ROLE. LAST_APPROVER.equals(role)) {
			String query = "select a.action_by_code from yq_Work_Flow_Entity_Action_Log alog where alog.id in "
					+ "( select max(id) from  yq_Work_Flow_Entity_Action_Log a "
					+ " where a.entity_id = "+workFlowEntity.getEntityId()
					+ " and a.entity_category = '"+workFlowEntity.getEntityCategory()+"' ";
			String actionByCode = (String)this.getSingleValueByNativeQuery(query);
		 	return userCode.equals( actionByCode );
	    }
		
		// 下一审批人
		if (WfConstants.WORK_FLOW_SYS_ROLE.NEXT_APPROVER.equals(role)) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * 如果流程状态不能做任何操作了， 流程就结束了
	 * @param workFlowId
	 * @param currentStatus
	 * @return
	 */
	private boolean getEndWorkFlow(Long workFlowId, String currentStatus) {
		String query = " select * from yq_work_flow_detail "
		+" where work_flow_id = "+workFlowId
		+"   and CURRENT_STATUS = '"+currentStatus+"'";
		return !this.existsNativeQuery(query);
	}
	
	/**
	 * 包括从history 查找
	 * @param entityId
	 * @param entityCategory
	 * @return
	 */
	@Override
	public WorkFlowEntity getWorkFlowEntityIncHistory(Long entityId, String entityCategory) {
 
		WorkFlowEntity entity =  this.sqlToModelService.getSingleRecord(this.getWFEnfityQuery("yq_work_flow_entity", entityId, entityCategory), null, WorkFlowEntity.class);		
        if (entity != null) {
        	return entity;
        } else {
        	return this.sqlToModelService.getSingleRecord(this.getWFEnfityQuery("yq_work_flow_entity_history", entityId, entityCategory), null, WorkFlowEntity.class);
        }
        
	}
	
	/**
	 * 包括从history 查找
	 * @param entityId
	 * @param entityCategory
	 * @return
	 */
	@Override
	public WorkFlowEntity getWorkFlowEntityIncHistory(Long id ) {
 
		WorkFlowEntity entity =  this.sqlToModelService.getSingleRecord(this.getWFEnfityQuery("yq_work_flow_entity", id), null, WorkFlowEntity.class);		
        if (entity != null) {
        	return entity;
        } else {
        	return this.sqlToModelService.getSingleRecord(this.getWFEnfityQuery("yq_work_flow_entity_history", id), null, WorkFlowEntity.class);
        }
        
	}
	
 
	private String getWFEnfityQuery(String tableName,Long entityId, String entityCategory) {
		String query = " SELECT "
				+ JPAUtils.genEntityColsForMysql(WorkFlowEntity.class,
						"wfe", null)
				+ ", slc.lookup_name entityStatusDsp"
				+ ", wf.work_flow_name workFlowName  "
		        + " FROM "+tableName+" wfe" 
		        + " LEFT JOIN yq_lookup_code slc ON slc.key_code = wfe.entity_status" 
		        + " LEFT JOIN yq_work_flow wf ON wf.id = wfe.work_flow_id " 
		        +" where entity_Id = "+entityId
		        +"   and Entity_Category = '"+entityCategory+"' ";
		return query; 
	}
	private String getWFEnfityQuery(String tableName,Long id) {
		String query = " SELECT "
				+ JPAUtils.genEntityColsForMysql(WorkFlowEntity.class,
						"wfe", null)
				+ ", slc.lookup_name entityStatusDsp"
				+ ", wf.work_flow_name workFlowName  "
		        + " FROM "+tableName+" wfe" 
		        + " LEFT JOIN yq_lookup_code slc ON slc.key_code = wfe.entity_status" 
		        + " LEFT JOIN yq_work_flow wf ON wf.id = wfe.work_flow_id " 
		        +" where  wfe.Id = "+id;
		return query; 
	}

	@Override
	public List<WorkFlowWeappView> getWorkFlowWeappView(Long entityId, String entityCategory) {
		List<WorkFlowWeappView> result = new ArrayList<WorkFlowWeappView>();
 		String lastNodeTime = "99"; 
		WorkFlowOpenPageView pageView = this.genOpenPageView(0L, entityId, entityCategory, null);
		if (pageView == null || CommonUtil.isNull(pageView.getGraphDetail()) || CommonUtil.isNull(pageView.getActionLogList())) {
			return null;
		}
		String currentStatus = pageView.getActionLogList().get(pageView.getActionLogList().size() - 1).getCurrentStatus();
		String preStatus = pageView.getActionLogList().get(pageView.getActionLogList().size() - 1).getPreStatus();
        int currNoteInd = this.getCurrNoteInd(pageView.getGraphDetail(), currentStatus);
        int lastActionNoteInd = this.getCurrNoteInd(pageView.getGraphDetail(), preStatus);
        if ( currNoteInd > lastActionNoteInd ) {
        	lastActionNoteInd = currNoteInd;
        }
        WorkFlowEntityActionLog preActionLog = null; 
        for (int i = 0; i < pageView.getGraphDetail().size(); i++) {
			// 输出对象 
			WorkFlowGraphDetail detail = pageView.getGraphDetail().get(i);
			List<WorkFlowEntityActionLog> alogList = this.getALog(detail, pageView.getActionLogList());
			WorkFlowEntityActionLog  alog = null; 
			String value1 = "";
			String value4 = "";
			Date actionDate = null;
			if (CommonUtil.isNotNull(alogList)) {
				alog = alogList.get(0);
 			} 
 			if (i == 0 ) { // 开始
 				WorkFlowWeappView view = this.genWeappViewByLog(detail.getGraphNodeName(), alog);
 				view.setActionName("发起");
				if (alog != null) { 
	 				view.setDone(true);
				} 
				result.add(view);
			} else if ( i ==  pageView.getGraphDetail().size() - 1) { //结束
				WorkFlowWeappView view = this.genWeappViewByLog(detail.getGraphNodeName(), alog);
				view.setActionName("完成");
				if (detail.getSourceStatuses() != null && detail.getSourceStatuses().contains(currentStatus)) {
					view.setDone(true);
					//WorkFlowEntityActionLog firstActionLog = pageView.getActionLogList().get(0);
					WorkFlowEntityActionLog firstActionLog = this.getFirstActionLog(pageView.getActionLogList());
					WorkFlowEntityActionLog lastActionLog = pageView.getActionLogList().get(pageView.getActionLogList().size() - 1 );
					String allNodeTime = getDisTime(firstActionLog.getCreateTime().getTime(),
							lastActionLog.getCreateTime().getTime());
					view.setActionTime("总耗时:" + allNodeTime);
				} 
				result.add(view);
			} else if ( i <= lastActionNoteInd && !detail.getSourceStatuses().contains(currentStatus) ) { //已审
				if (CommonUtil.isNotNull(alogList)) {
					for (int j = alogList.size()-1; j >= 0; j-- ) {
						WorkFlowWeappView view = this.genWeappViewByLog(detail.getGraphNodeName(), alogList.get(j));
						view.setDone(true);
						result.add(view);
					} 
				} else {
					WorkFlowWeappView view = this.genWeappViewByLog(detail.getGraphNodeName(), alog);
 	 				view.setActionName("跳过"); 
					view.setDone(true);
					result.add(view);
				}
			} else if ( i == currNoteInd ) { //当前待审， 算日期
				if (CommonUtil.isNotNull(alogList) ) {
					for (int j = alogList.size()-1; j >= 0; j-- ) {
						WorkFlowWeappView view = this.genWeappViewByLog(detail.getGraphNodeName(), alogList.get(j));
						view.setDone(true);
						result.add(view);
					} 
				}  
				WorkFlowWeappView view = this.genWeappViewByLog(detail.getGraphNodeName(), null);
				view.setDone(false);
				view.setActionName("待审");
				if (preActionLog != null) {
					lastNodeTime = getDisTime(preActionLog.getCreateTime().getTime(), new Date().getTime());	
				} 
				view.setActionTime("等待:" + lastNodeTime);
				result.add(view);
			} else { //待审
				WorkFlowWeappView view = this.genWeappViewByLog(detail.getGraphNodeName(), null);
				view.setActionName("待审");
				result.add(view);
			}
  			if (alog != null) {
 				preActionLog = alog;
 			} 
		} 
			
		return result;
	}

	private List<WorkFlowEntityActionLog> getALog(WorkFlowGraphDetail detail, List<WorkFlowEntityActionLog> actionLogList) {
		if (CommonUtil.isNull(detail) || CommonUtil.isNull(actionLogList)) {
			return null;
		}
		List<WorkFlowEntityActionLog> list = new  ArrayList<WorkFlowEntityActionLog>();
		for (int i=actionLogList.size()-1; i>=0; i--) {
			WorkFlowEntityActionLog alog = actionLogList.get(i);
			if (detail.getSourceStatuses().contains(alog.getPreStatus())) {
				list.add(alog);
			}
			if (WfConstants.WORK_FLOW_GRAPH_START.equals( alog.getPreGraphNode()) ) { // 到开始的节点了， 
				break;
			}
		}
		return list;
	}
	
	private WorkFlowEntityActionLog getFirstActionLog( List<WorkFlowEntityActionLog> actionLogList) { 
		for (int i=actionLogList.size()-1; i>=0; i--) {
			WorkFlowEntityActionLog alog = actionLogList.get(i);
 
			if (WfConstants.WORK_FLOW_GRAPH_START.equals( alog.getPreGraphNode()) ) { // 到开始的节点了， 
				return alog;
			}
		}
		return null;
	}
	
	private WorkFlowWeappView  genWeappViewByLog( String nodeName , WorkFlowEntityActionLog  alog) { 
		WorkFlowWeappView view = new WorkFlowWeappView();
		view.setNodeName( nodeName);
		if (alog != null) {
			view.setActionDate(alog.getActionDate());
			view.setActionReason(alog.getActionReason());
			view.setActionName(alog.getActionName());
			view.setActionByName(alog.getActionByName());
			view.setActionTime(DateUtil.convertDate2String(alog.getActionDate(), "yy-MM-dd HH:mm"));
			view.setColor(alog.getColor());
		}
		return view;
	}

	private int getCurrNoteInd(List<WorkFlowGraphDetail> graphDetail, String currentStatus) {
        if (CommonUtil.isNull(graphDetail)) {
            return -1;
        }
        for (int i=0; i<graphDetail.size(); i++) {
        	WorkFlowGraphDetail d = graphDetail.get(i);
        	if (d.getSourceStatuses() != null && d.getSourceStatuses().contains(currentStatus)) {
        		return i;
        	}
        }
		return -1;
	}
	/**
	 * 不提供原因
	 * @param skipPageView
	 * @param action
	 * @return
	 * @throws WorkFlowException
	 */
	@Override
	public WorkFlowEntityIntf  executeSingleAction(WorkFlowEntitySkipPageView skipPageView , String action ) 
			throws WorkFlowException {
 		return executeSingleAction(skipPageView, action, null);
	}
	
	@Override
	public WorkFlowEntityIntf  executeSingleAction(WorkFlowEntitySkipPageView skipPageView , String action, String dateFormat) 
			throws WorkFlowException {
 		WorkFlowEntityService entityService = this.getEntityServiceByCategoryCode(skipPageView.getEntityCategory());
		WorkFlowEntityIntf workFlowEntityIntf = entityService.getWorkFlowEntityByEntityId(skipPageView.getId());
		//Map<String,String> map = this.getMergeMap(skipPageView.getEntityTemplateId());
		//YqBeanUtil.mergeValue(skipPageView, workFlowEntityIntf, map, dateFormat);
		this.executeSingleAction(workFlowEntityIntf, null, action, skipPageView.getActionReason()); // 没有原因， 已经是新的页面了， 不可以输入原因 
		return workFlowEntityIntf;
	}
	
	/**
	 * 取mapping 关系
	 * @param entityTemplateId
	 * @return
	 */
//	private Map<String,String> getMergeMap(Long entityTemplateId) {
//		return this.entityTemplateService.getMappingToRefer(entityTemplateId);
//	}
	
	
	
	

	private String getDisTime(long startDate, long endDate) {
		long timesDis = Math.abs(startDate - endDate);
		long day = timesDis / (1000 * 60 * 60 * 24);
		long hour = timesDis / (1000 * 60 * 60) - day * 24;
		long min = timesDis / (1000 * 60) - day * 24 * 60 - hour * 60;
		long sec = timesDis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60;

		StringBuffer str = new StringBuffer();
		if (day > 0) {
			str.append(day + "天");
		}
		if (hour > 0) {
			str.append(hour + "小时");
		}
		if (min > 0) {
			str.append(min + "分钟");
		}
		if (sec > 0) {
			str.append(sec + "秒");
		}

		return str.toString();
	}

	@Override
	public WorkFlowEntityIntf getWorkFlowEntityIntf(Long entityId, String entityCategory) {
		WorkFlowEntityService entityService = this.getEntityServiceByCategoryCode(entityCategory);
		WorkFlowEntityIntf workFlowEntityIntf=entityService.getWorkFlowEntityByEntityId(entityId);
		return workFlowEntityIntf;
	}

	@Override
	public WorkFlowEntityIntf saveWorkFlowEntityIntfByPageView(WorkFlowEntitySkipPageView skipPageView) {
		WorkFlowEntityService entityService = this.getEntityServiceByCategoryCode(skipPageView.getEntityCategory());
		WorkFlowEntityIntf workFlowEntityIntf=entityService.getWorkFlowEntityByEntityId(skipPageView.getId());
 		//YqBeanUtil.mergeValue(skipPageView, workFlowEntityIntf, this.entityTemplateService.getMappingToRefer(skipPageView.getEntityTemplateId()));
 		entityService.saveEntity(workFlowEntityIntf, null);
 		return workFlowEntityIntf;
	}
	
	@Override
	public WorkFlowEntityIntf saveWorkFlowEntityIntfByPageView(WorkFlowEntitySkipPageView skipPageView, String dateFormat) {
		WorkFlowEntityService entityService = this.getEntityServiceByCategoryCode(skipPageView.getEntityCategory());
		WorkFlowEntityIntf workFlowEntityIntf=entityService.getWorkFlowEntityByEntityId(skipPageView.getId());
 		//YqBeanUtil.mergeValue(skipPageView, workFlowEntityIntf, this.entityTemplateService.getMappingToRefer(skipPageView.getEntityTemplateId()), dateFormat);
 		entityService.saveEntity(workFlowEntityIntf, null);
 		return workFlowEntityIntf;
	}
	
	private ParseRootAndVariable genParseRootAndVariable(WorkFlowEntityIntf workFlowEntityIntf, WorkFlowEntityService entityService,WorkFlowDetail workFlowDetail) {
		Map  eServiceMap = new HashMap ();
		eServiceMap.put("eService",entityService);
		eServiceMap.put("self", workFlowEntityIntf);
		eServiceMap.put("wfd", workFlowDetail);
		ParseRootAndVariable pv = new ParseRootAndVariable(workFlowEntityIntf, eServiceMap);
		return pv;
	}
	
	private void executeFunction(ParseRootAndVariable rootAndVariable, String functionStr) {
		if (CommonUtil.isNull(functionStr)) {
			return;
		}
		if (functionStr.contains(";")) {
			String[] sa = functionStr.split(";"); 
			for (String fs : sa) {
				if (CommonUtil.isNotNull(fs)) {  
					this.parseService.genValueByEleNumberWithPars(rootAndVariable,fs,null);
				}
			}
		} else {
			this.parseService.genValueByEleNumberWithPars(rootAndVariable,functionStr,null);
		}
		
	}

	 
	
 
	private Object genlogInfo(WorkFlowEntityActionLog actLog) {
		return "操作时间："+DateUtil.convertDate2String(actLog.getCreateTime(), DateUtil.DEFAULT_DATE_TIME_FORMAT)
		      +" 操作人："+actLog.getActionByName()
		      +" 操作："+ actLog.getActionName()
		      +" 耗时："+ DateUtil.convertSecent(actLog.getTakeUpTime());
	}

	private List<WorkFlowEntityActionLog> getActionLogList( List data ) {
		String category = ((WorkFlowEntityIntf)data.get(0)).getEntityCategory();
		Set<Long> idSet = new HashSet<Long>();
		for (Object bean : data ) {
			idSet.add(((WorkFlowEntityIntf)bean).getEntityId());
		}
		String query = "select "+JPAUtils.genEntityCols(WorkFlowEntityActionLog.class, "alog",null)
		+" ,actlc.LOOKUP_NAME  actionName "
		+"	from yq_work_flow_entity_action_log  alog "  
	    +" left join yq_lookup_code actlc on actlc.KEY_CODE = alog.ACTION "
	    +" where alog.ENTITY_CATEGORY = '"+category+"' " 
	    +" and "+CommonUtil.genInLongBySet("alog.ENTITY_ID", idSet); 
		return this.sqlToModelService.executeNativeQuery(query, null, WorkFlowEntityActionLog.class);
	}
	
	@Override
	public List<WorkFlowEntityActionLog> findActionLog(Long entityId, String entityCategory) {

		StringBuffer otherCols = new StringBuffer();
		otherCols.append(" actlc.lookup_name actionName,")
				.append(" (SELECT lc.lookup_name FROM yq_lookup_code lc WHERE lc.key_code = alog.role ) roleName,")
				.append(" (SELECT lc.lookup_name FROM yq_lookup_code lc WHERE lc.key_code = alog.pre_status ) preStatusName,")
				.append(" (SELECT lc.lookup_name FROM yq_lookup_code lc WHERE lc.key_code = alog.current_status ) currentStatusName ");
		StringBuffer mixSql = new StringBuffer();
		mixSql.append("SELECT alog.*,")
				.append( otherCols)
				.append(" FROM yq_work_flow_entity_action_log alog LEFT JOIN yq_lookup_code actlc ON alog.action = actlc.key_code")
				.append(" WHERE alog.Entity_category = '" + entityCategory + "' ")
				.append(" AND alog.Entity_id = " + entityId );
//		if(WfConstants.CONSIGNMENT_WORKFLOW_ENTITY_CATEGORY.equals(entityCategory)){
//			mixSql.append(" AND alog.pre_status <> '" + WfConstants.NULL_ENTITY_STATUS + "' ");
//		}
		mixSql.append(" ORDER BY alog.id ");
		return this.sqlToModelService.executeNativeQuery(mixSql.toString(), null, WorkFlowEntityActionLog.class);
	}

	@Override
	public WorkFlowEntityIntf executeSingleActionBySys(String jobName, Long entityId,String entityCategory, Map variables, String action, String actionReason)
			throws WorkFlowException { 
		YqSecurityUtils.runAs(new UsernamePasswordAuthenticationToken("WFSYS", jobName), false);
		return this.executeSingleAction( entityId, entityCategory,  variables,  action, actionReason);
	}

 
	
}
