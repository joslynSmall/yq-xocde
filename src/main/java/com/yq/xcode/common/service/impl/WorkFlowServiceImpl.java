package com.yq.xcode.common.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.NoResultException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.yq.xcode.common.bean.ParseRootAndVariable;
import com.yq.xcode.common.bean.SelectItem;
import com.yq.xcode.common.bean.WfConstants;
import com.yq.xcode.common.bean.WorkFlowEntityCategory;
import com.yq.xcode.common.bean.WorkFlowOpenPageView;
import com.yq.xcode.common.bean.WorkFlowView;
import com.yq.xcode.common.criteria.WorkFlowDetailQueryCriteria;
import com.yq.xcode.common.criteria.WorkFlowQueryCriteria;
import com.yq.xcode.common.exception.DllException;
import com.yq.xcode.common.exception.ValidateException;
import com.yq.xcode.common.exception.WorkFlowException;
import com.yq.xcode.common.model.LookupCode;
import com.yq.xcode.common.model.WorkFlow;
import com.yq.xcode.common.model.WorkFlowDetail;
import com.yq.xcode.common.model.WorkFlowEntity;
import com.yq.xcode.common.model.WorkFlowGraphDetail;
import com.yq.xcode.common.service.InitConstantsService;
import com.yq.xcode.common.service.LookupCodeService;
import com.yq.xcode.common.service.ParseService;
import com.yq.xcode.common.service.SqlToModelService;
import com.yq.xcode.common.service.WorkFlowService;
import com.yq.xcode.common.service.YqSequenceService;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.JPAUtils;

@Service("WorkFlowService")
public class WorkFlowServiceImpl extends YqJpaDataAccessObject implements
		WorkFlowService, ApplicationContextAware {

	@Autowired
	private SqlToModelService sqlToModelService;
	@Autowired
	private LookupCodeService lookupCodeService;
	@Autowired
	private ParseService parseService;
	@Autowired
	private YqSequenceService yqSequenceService;
	@Autowired
	private InitConstantsService initConstantsService;

	private ApplicationContext applicationContext;
	
	private static Log Log = LogFactory.getLog(WorkFlowServiceImpl.class);
	 

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;

	}

	private void validate() {
		// String
		// flag=systemParameterService.getStringValueByKey(SaConstants.ENABLE_CHANGE_WORK_FLOW_KEY_CODE);
		// if(Boolean.valueOf(flag)){
		// throw new ValidateException("系统已锁定审批流程设置");
		// }
	}

	private void validateWorkFlowInfo(WorkFlow workFlow) {
		if (null == workFlow.getId()) {
			String validateNumberSql = "select 1 from yq_work_flow where WORK_FLOW_NUMBER=? ";
			if (this.existsNativeQuery(validateNumberSql, workFlow
					.getWorkFlowNumber().trim())) {
				throw new ValidateException("工作流程代码【"
						+ workFlow.getWorkFlowNumber().trim() + "】已存在");
			}
			String validateNameSql = "select 1 from yq_work_flow where WORK_FLOW_NAME=? ";
			if (this.existsNativeQuery(validateNameSql, workFlow
					.getWorkFlowName().trim())) {
				throw new ValidateException("工作流程名称【"
						+ workFlow.getWorkFlowName().trim() + "】已存在");
			}
		} else if (workFlow.getId() != 0L) {
			String validateNumberSql = "select 1 from yq_work_flow where WORK_FLOW_NUMBER=? and ID!=? ";
			if (this.existsNativeQuery(validateNumberSql, workFlow
					.getWorkFlowNumber().trim(), workFlow.getId())) {
				throw new ValidateException("工作流程代码【"
						+ workFlow.getWorkFlowNumber().trim() + "】已存在");
			}
			String validateNameSql = "select 1 from yq_work_flow where WORK_FLOW_NAME=? and ID!=? ";
			if (this.existsNativeQuery(validateNameSql, workFlow
					.getWorkFlowName().trim(), workFlow.getId())) {
				throw new ValidateException("工作流程名称【"
						+ workFlow.getWorkFlowName().trim() + "】已存在");
			}
		}

	}

	@Override
	public WorkFlow createWorkFlow(WorkFlow workFlow) {
		String generateNumber = this.yqSequenceService.nextTextSequenceNumber("W", null,
				5, "WORKFLOW_NUMBER");
		workFlow.setWorkFlowNumber(generateNumber);
		validate();
		validateWorkFlowInfo(workFlow);
		return this.create(workFlow);
	}

	// @Override
	public WorkFlow updateWorkFlow(WorkFlow workFlow) {
		validate();
		validateWorkFlowInfo(workFlow);
		return this.update(workFlow);
	}

	@Override
	public WorkFlowDetail createWorkFlowDetail(WorkFlowDetail workFlowDetail) {
		validate();
		return this.create(workFlowDetail);
	}

	@Override
	public WorkFlowDetail updateWorkFlowDetail(WorkFlowDetail WorkFlowDetail) {
		validate();
		return this.update(WorkFlowDetail);
	}

	// @Override
	public WorkFlowGraphDetail createWorkFlowGraphDetail(
			WorkFlowGraphDetail workFlowGraphDetail) {
		validate();
		return this.create(workFlowGraphDetail);
	}

	// @Override
	public WorkFlowGraphDetail updateWorkFlowGraphDetail(
			WorkFlowGraphDetail workFlowGraphDetail) {
		validate();
		return em.merge(workFlowGraphDetail);
	}

	@Override
	public void deleteWorkFlow(Long id, Long version) {
		validate();
		this.delete(WorkFlow.class, id, version);
	}

	@Override
	public void deleteWorkFlowDetail(Long id, Long version) {
		validate();
		this.delete(WorkFlowDetail.class, id, version);
	}

	// @Override
	private void deleteWorkFlowGraphDetail(
			WorkFlowGraphDetail workFlowGraphDetail) {
		validate();
		em.remove(em.merge(workFlowGraphDetail));
	}

	@Override
	public void deleteWorkFlowDetails(Long[] ids, Long[] versions) {
		validate();
		if (ids != null && ids.length > 0) {
			for (int i = 0; i < ids.length; i++) {
				this.deleteWorkFlowDetail(ids[i], versions[i]);
			}
		}
	}

	@Override
	public void deleteWorkFlows(Long[] ids, Long[] versions) {
		validate();
		if (ids != null && ids.length > 0) {
			for (int i = 0; i < ids.length; i++) {
				List<WorkFlowDetail> details = findWorkFlowDetails(ids[i]);
				if (!CommonUtil.isNull(details)) {
					throw new DllException("存在明细信息，请先删除！");
				}
				this.deleteWorkFlow(ids[i], versions[i]);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WorkFlow> findAllWorkFlows() {
		return this.find(" from WorkFlow ");
	}

	@Override
	public Page<WorkFlow> findWorkFlows(WorkFlowQueryCriteria criteria ) {
		StringBuffer query = new StringBuffer();
		query.append(" SELECT wf.*")
				.append(" FROM yq_work_flow wf")
				.append(" WHERE 1=1 ")
				.append(JPAUtils.notNullReturn(
						criteria.getWorkFlowName(),
						" AND wf.work_flow_name LIKE '%"
								+ criteria.getWorkFlowName() + "%' "))
				.append(JPAUtils.notNullReturn(
						criteria.getWorkFlowNumber(),
						" AND wf.work_flow_number LIKE '%"
								+ criteria.getWorkFlowNumber() + "%' "))
				.append(JPAUtils.notNullReturn(
						criteria.getCategoryCode(),
						" AND wf.category_code = '"
								+ criteria.getCategoryCode() + "' "))
				.append(JPAUtils.notNullReturn(
						criteria.getDescription(),
						" AND wf.description LIKE '%"
								+ criteria.getDescription() + "%' "));
		String defaultSortBy = " wf.id desc ";
		return this.sqlToModelService.executeNativeQueryForPage(
				query.toString(), defaultSortBy, null, criteria, 
				WorkFlow.class);
	}

	@Override
	public WorkFlow getWorkFlowById(Long id) {
		WorkFlow workFlow = this.getById(WorkFlow.class, id);
		if(CommonUtil.isNotNull(workFlow.getUseRelationFunction())) {
			String useRelationFunctionStr = this.translationFunction(workFlow
					.getUseRelationFunction());
			workFlow.setUseRelationFunctionStr(useRelationFunctionStr);
		}
		
		return workFlow;
	}

	@Override
	public WorkFlowDetail getWorkFlowDetailById(Long id) {
		WorkFlowDetail workFlowDetail = this.getById(WorkFlowDetail.class, id);
//		String relationFunctionName = this.translationFunction(workFlowDetail
//				.getRelationFunction());
//		String falseMessageStr = this.translationFunction(workFlowDetail
//				.getFalseMessage());
//		workFlowDetail.setRelationFunctionName(relationFunctionName);
//		workFlowDetail.setFalseMessageStr(falseMessageStr);
		return workFlowDetail;
	}

	private String translationFunction(String functionCode) {
		if (CommonUtil.isNotNull(functionCode) && !"0".equals(functionCode)) {
			return parseService
					.translationEleCodeWithParsToChiness(functionCode,null);
		}
		return "";
	}

	@Override
	public List<WorkFlowDetail> findWorkFlowDetails(Long workFlowId) {

		StringBuffer query = new StringBuffer();
		query.append("SELECT")
				.append(" wfd.*,")
				.append(" ylc_c.lookup_name currentStatusName,")
				.append(" ylc_n.lookup_name nextStatusName,")
				.append(" ylc_a.lookup_name actionName,")
				.append(" pe.ele_name relationFunctionName,")
				.append(" ylc_r.lookup_name roleName")
				.append(" FROM yq_work_flow_detail wfd ")
				.append(" LEFT JOIN yq_lookup_code ylc_c ON ylc_c.key_code = wfd.current_status")
				.append(" LEFT JOIN yq_lookup_code ylc_n ON ylc_n.key_code = wfd.next_status")
				.append(" LEFT JOIN yq_lookup_code ylc_a ON ylc_a.key_code = wfd.action")
				.append(" LEFT JOIN yq_lookup_code ylc_r ON ylc_r.key_code = wfd.role")
				.append(" LEFT JOIN yq_parse_element pe ON pe.ele_number = wfd.relation_function")
				.append(" WHERE wfd.work_flow_id = " + workFlowId);

		String defaultSortBy = " ORDER BY wfd.line_number";
		List<WorkFlowDetail> list = sqlToModelService.executeNativeQuery(query
				.append(defaultSortBy).toString(), null, WorkFlowDetail.class);
		for (WorkFlowDetail workFlowDetail : list) {
			workFlowDetail.setRelationFunctionName(this
					.translationFunction(workFlowDetail.getRelationFunction()));
			//workFlowDetail.setFalseMessageStr(this
			//		.translationFunction(workFlowDetail.getFalseMessage()));
		}
		return list;
	}

	@Override
	public List<WorkFlowDetail> findWorkFlowDetailsByCriteria(
			WorkFlowDetailQueryCriteria criteria) {
		if (CommonUtil.isNull(criteria.getWorkFlowId())) {
			throw new ValidateException("流程ID为空,请刷新当前页面!");
		}
		StringBuffer query = new StringBuffer();
		query.append("SELECT")
				.append(" wfd.*,")
				.append(" ylc_c.lookup_name currentStatusName,")
				.append(" ylc_n.lookup_name nextStatusName,")
				.append(" ylc_a.lookup_name actionName,")
				.append(" pe.ele_name relationFunctionName,")
				.append(" ylc_r.lookup_name roleName")
				.append(" FROM yq_work_flow_detail wfd ")
				.append(" LEFT JOIN yq_lookup_code ylc_c ON ylc_c.key_code = wfd.current_status")
				.append(" LEFT JOIN yq_lookup_code ylc_n ON ylc_n.key_code = wfd.next_status")
				.append(" LEFT JOIN yq_lookup_code ylc_a ON ylc_a.key_code = wfd.action")
				.append(" LEFT JOIN yq_lookup_code ylc_r ON ylc_r.key_code = wfd.role")
				.append(" LEFT JOIN yq_parse_element pe ON pe.ele_number = wfd.relation_function")
				.append(" WHERE 1 = 1 AND wfd.work_flow_id = "
						+ criteria.getWorkFlowId())
				.append(JPAUtils.notNullReturn(
						criteria.getCurrentStatus(),
						" AND wfd.current_Status = '"
								+ criteria.getCurrentStatus() + "' "))
				.append(JPAUtils.notNullReturn(criteria.getRole(),
						" AND wfd.role = '" + criteria.getRole() + "' "))
				.append(JPAUtils.notNullReturn(criteria.getAction(),
						" AND wfd.action = '" + criteria.getAction() + "' "))
				.append(JPAUtils.notNullReturn(criteria.getNextStatus(),
						" AND wfd.next_Status = '" + criteria.getNextStatus()
								+ "' "));
		if(CommonUtil.isNotNull(criteria.getWid())){
			query.append(" and wfd.id="+criteria.getWid());
		}
		String defaultSortBy = " ORDER BY wfd.line_number";
		List<WorkFlowDetail> list = sqlToModelService.executeNativeQuery(query
				.append(defaultSortBy).toString(), null, WorkFlowDetail.class);
		for (WorkFlowDetail workFlowDetail : list) {
			workFlowDetail.setRelationFunctionName(this
					.translationFunction(workFlowDetail.getRelationFunction())); 
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WorkFlowDetail> findWorkFlowDetailStatus(Long workFlowId) {
		return this
				.find(" from WorkFlowDetail  where workFlowId = ? order by lineNumber ",
						workFlowId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WorkFlowDetail> findWorkFlowDetails(String[] roles,
			String... status) {
		if (CommonUtil.isNull(roles)) {
			return null;
		}
		List<WorkFlowDetail> list = this.find(" from WorkFlowDetail a where  "
				+ CommonUtil.genInStrByList(" role ", roles)
				+ " order by a.lineNumber");
		if (!CommonUtil.isNull(list)) {
			List<WorkFlowDetail> newList = new LinkedList();
			for (WorkFlowDetail wfd : list) {
				if (this.isIn(wfd.getCurrentStatus(), status)) {
					newList.add(wfd);
				}
			}
			return newList;
		}
		return null;
	}

	private boolean isIn(String st, String... statuses) {
		if (statuses == null) {
			return true;
		}
		if (CommonUtil.isNull(st)) {
			return true;
		}
		for (String s : statuses) {
			if (s.equals(st)) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WorkFlowDetail> findWorkFlowDetails(Long workFlowId,
			String[] roles, String status) {
		if (CommonUtil.isNull(roles)) {
			return new ArrayList<WorkFlowDetail>();
		}

		String query = " from WorkFlowDetail v  where v.workFlowId = ? "
				+ " and (v.currentStatus = ?  or ifNull(v.currentStatus,'') = '' )and  ( "
				+ CommonUtil.genInStrByList(" v.role ", roles)
				+ " or ifNull(v.role,'') = '' ) ";

		List<WorkFlowDetail> wfList = this.find(query, workFlowId, status);
		return wfList;
	}

	@Override
	public List<WorkFlowDetail> findWorkFlowDetails(Long workFlowId,
			String[] roles, String status, String action) {
		String inStr = " "; 
		Set<String> roleSet = new HashSet<String>();
		roleSet.add(WfConstants.WORK_FLOW_SYS_ROLE.ANYROLE);
		if ( roles != null ) {
			for ( String role : roles ) {
				roleSet.add(role);
			}
		}
		inStr = " and   "
				+ CommonUtil.genInStrBySet(" v.role ", roleSet) ;
		String query = " from WorkFlowDetail v  where v.workFlowId = ?  "
				+ inStr
				+ " and (v.currentStatus = ? or ifnull(v.currentStatus,'') = '')  and action=?  "
				+" order by lineNumber ";
		Log.info("workFlowid = " + workFlowId + " status = " + status
				+ " action=" + action);
		List<WorkFlowDetail> wfList = this.find(query, workFlowId, status,
				action);
		return wfList;
	}

	public String genErrorMsg(Long workFlowId, String[] roles,
			String currentStatus, String action, String errorCategory) {
		String msg = null;
		try {
			msg = "工作流程【"
					+ (String) this
							.getValueByNativeQuery(
									"select work_flow_name from yq_work_flow where id = ? ",
									workFlowId) + "】设置有误!</br>";
		} catch (NoResultException e) {
			return "工作流程ID:" + workFlowId + "不存在！";
		}

		msg = msg + "出错类型: " + errorCategory + "</br> ";
		msg = msg + "当前状态: " + currentStatus + "-"
				+ this.getNameByLookupKeyCode(currentStatus) + "</br> ";
		msg = msg + "当前操作: " + action + "-"
				+ this.getNameByLookupKeyCode(action) + "</br> ";
		msg = msg + "当前角色: ";
		if (CommonUtil.isNull(roles)) {
			msg = msg + " 角色为空，可能为系统定制流程！请于管理员联系！ ";
		} else {
			for (String r : roles) {
				msg = msg + r + "-" + this.getNameByLookupKeyCode(r) + ",";
			}
		}

		msg = msg + "</br>";
		return msg;

	}

	private String getNameByLookupKeyCode(String keyCode) {
		if (CommonUtil.isNull(keyCode)) {
			return null;
		}
		LookupCode lc = lookupCodeService.getLookupCodeByKeyCode(keyCode);
		if (lc == null) {
			return null;
		} else {
			return lc.getLookupName();
		}
	}

	@Override
	public WorkFlowDetail getNextWorkFlow(Long workFlowId, String[] roles,
			Object source, Map variables, String currentStatus, String action)
			throws WorkFlowException {
		return this.getNextWorkFlow(workFlowId, roles, source, variables,
				currentStatus, action, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public WorkFlowDetail getNextWorkFlow(Long workFlowId, String[] roles,
			Object source, Map variables, String currentStatus, String action,
			List<WorkFlowDetail> workFlowDetails) throws WorkFlowException {
		List<WorkFlowDetail> list = workFlowDetails;
		if (CommonUtil.isNull(workFlowDetails)) {
			list = this.findWorkFlowDetails(workFlowId, roles, currentStatus,
					action);
		}
		List<WorkFlowDetail> newList = new LinkedList();
		if (list == null || list.size() == 0) {
			String msg = this.genErrorMsg(workFlowId, roles, currentStatus,
					action, "未设置状态，操作和角色的工作流！");
			throw new WorkFlowException(msg);
		}

		String addedRelMsg = "";
		String relations = "";
		for (WorkFlowDetail fd : list) {
			if (action.equals(fd.getAction())
					&& this.isTrueFun(source, variables,
							fd.getRelationFunction())) {
				newList.add(fd);
			} else {
				if (!CommonUtil.isNull(fd.getFalseMessage())) {
					addedRelMsg = addedRelMsg + fd.getFalseMessage() + "\n";
				}
			}
		}
		if (newList.size() == 0) {
			if (CommonUtil.isNull(addedRelMsg)) {
//				String msg = this.genErrorMsg(workFlowId, roles, currentStatus,
//						action, "设置了状态，操作和角色的工作流，但没有符合条件的工作流！");
//				throw new WorkFlowException(msg);
				throw new WorkFlowException("单据已被其他人操作，请退出后重新打开!");
			} else {
				throw new WorkFlowException(addedRelMsg);
			}

		}
//		if (newList.size() > 1) {
//			String msg = this.genErrorMsg(workFlowId, roles, currentStatus,
//					action, "此条件产生了多个工作流设定，详查条件！");
//			throw new WorkFlowException(msg);
//		}
		return newList.get(0);
	}

	public boolean isTrueWorkFlowDetail(Object source, Map variables,
			WorkFlowDetail WorkFlowDetail) {
		boolean bool = true;
		if (!CommonUtil.isNull(WorkFlowDetail.getRelationFunction())) {
			return this.isTrueFun(source, variables,
					WorkFlowDetail.getRelationFunction());
		}
		return true;
	}

	public WorkFlow copyWorkFlowById(Long workFlowId) {
		validate();
		return copyWorkFlow(workFlowId, true);
	}

	public void copyWorkFlowByIds(Long[] workFlowId) {
		validate();
		for (Long id : workFlowId) {
			copyWorkFlow(id, false);
		}
	}

	private WorkFlow copyWorkFlow(Long workFlowId, Boolean setCopyFromId) {
		if (CommonUtil.isNull(workFlowId)) {
			return null;
		}
		WorkFlow oldWf = this.getById(WorkFlow.class, workFlowId);
		WorkFlow wf = null;
		try {
			wf = (WorkFlow) BeanUtils.cloneBean(oldWf);
		} catch (Exception e) {
		}

		// int randomNumber = (int) (Math.random() * (9999 - 1234) + 4567);
		String randomNumber = CommonUtil.buildRandomKey().substring(9);
		// 流程编码设置
		String[] numberArray = oldWf.getWorkFlowNumber().split("-");
		String number = numberArray[0] + "-" + randomNumber;

		// 流程名称设置
		String[] nameArray = oldWf.getWorkFlowName().split("-");
		String workFlowName = nameArray[0] + "-" + randomNumber;

		wf.setId(null);
		wf.setCreateTime(null);
		wf.setCreateUser(null);
		wf.setWorkFlowNumber(number);
		wf.setWorkFlowName(workFlowName);
		wf.setVersion(0l);
		// wf.setModifyStatus(SaConstants.WORK_FLOW_MODIFY);//修改中状态
		wf = this.createWorkFlow(wf);
		// 复制流程明细
		List<WorkFlowDetail> list = this.find(
				"from WorkFlowDetail wfd where wfd.workFlowId=?", workFlowId);
		try {
			List<WorkFlowDetail> copyList = new ArrayList<WorkFlowDetail>();

			for (WorkFlowDetail wfd : list) {
				WorkFlowDetail copyWfd = (WorkFlowDetail) BeanUtils
						.cloneBean(wfd);
				copyWfd.setWorkFlowId(wf.getId());
				copyWfd.setId(null);
				copyList.add(copyWfd);
			}
			this.save(copyList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 复制流程图
		List<WorkFlowGraphDetail> graphList = this.find(
				"from WorkFlowGraphDetail wfgd where wfgd.workFlowId=?",
				workFlowId);
		try {
			List<WorkFlowGraphDetail> copyGraphList = new ArrayList<WorkFlowGraphDetail>();

			for (WorkFlowGraphDetail wfgd : graphList) {
				WorkFlowGraphDetail copyWfgd = (WorkFlowGraphDetail) BeanUtils
						.cloneBean(wfgd);
				copyWfgd.setWorkFlowId(wf.getId());
				copyWfgd.setId(null);
				copyGraphList.add(copyWfgd);
			}
			this.save(copyGraphList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// String sql = " SELECT ADDED_RELATION addedRelation,     "
		// + "        BATCH_EMAIL            batchEmail,        "
		// + "        DESCRIPTION            description,        "
		// + "        EXECUTE_METHOD         executeMethod,     "
		// + "        LINE_NUMBER            lineNumber,        "
		// + "        READONLY               readOnly,           "
		// + "        REASON_MANDATORY       reasonMandatory,   "
		// + "        ROLE                   role,               "
		// + "        SINGLE_EMAIL           singleEmail,       "
		// + wf.getId() + "        workFlowId,       "
		// + "        CURRENT_STATUS        currentStatus,     "
		// + "        NEXT_STATUS           nextStatus,        "
		// + "        ACTION                action,             "
		// + "        ADDED_RELATION_MSG    addedRelationMsg, "
		// + "        REASON_TITLE          reasonTitle,       "
		// + "        STATUS_DISPLAY            statusDisplay          "
		// + "   FROM SY_work_flow_detail where WORK_FLOW_ID = "
		// + workFlowId;
		//
		// List wfdList = this.sqlToModelService.getListSqlToModel(sql, null,
		// WorkFlowDetail.class);
		// this.save(wfdList);

		return wf;
	}

	@Override
	public WorkFlowDetail copyWorkFlowDetail(Long workFlowDetailId) {
		validate();
		WorkFlowDetail oldDetail = getWorkFlowDetailById(workFlowDetailId);
		WorkFlowDetail detail = null;
		try {
			detail = (WorkFlowDetail) BeanUtils.cloneBean(oldDetail);
		} catch (Exception e) {
			e.printStackTrace();
		}
		detail.setId(null);
		detail.setCreateTime(null);
		detail.setCreateUser(null);
		detail.setVersion(0l);
		return this.save(detail);
	}

	@Override
	public Page<WorkFlowDetail> findWorkFlowDetailsPageByCriteria(
			WorkFlowDetailQueryCriteria criteria ) {
		if (CommonUtil.isNull(criteria.getWorkFlowId())) {
			throw new ValidateException("流程ID为空,请刷新当前页面!");
		}
		StringBuffer query = new StringBuffer();
		query.append("SELECT")
				.append(" wfd.*,")
				.append(" ylc_c.lookup_name currentStatusName,")
				.append(" ylc_n.lookup_name nextStatusName,")
				.append(" ylc_a.lookup_name actionName,")
				.append(" pe.ele_name relationFunctionName,")
				.append(" ylc_r.lookup_name roleName")
				.append(" FROM yq_work_flow_detail wfd ")
				.append(" LEFT JOIN yq_lookup_code ylc_c ON ylc_c.key_code = wfd.current_status")
				.append(" LEFT JOIN yq_lookup_code ylc_n ON ylc_n.key_code = wfd.next_status")
				.append(" LEFT JOIN yq_lookup_code ylc_a ON ylc_a.key_code = wfd.action")
				.append(" LEFT JOIN yq_lookup_code ylc_r ON ylc_r.key_code = wfd.role")
				.append(" LEFT JOIN yq_parse_element pe ON pe.ele_number = wfd.relation_function")
				.append(" WHERE 1 = 1 ");
 
		String defaultSortBy = "  wfd.line_number ";  
 
		Page<WorkFlowDetail> page = 
				this.sqlToModelService.executeNativeQueryForPage(query.toString(),
						defaultSortBy, null, criteria, WorkFlowDetail.class);
		List<WorkFlowDetail> list = page.getContent();
		for (WorkFlowDetail workFlowDetail : list) {
			workFlowDetail.setRelationFunctionName(this
					.translationFunction(workFlowDetail.getRelationFunction())); 
		}
		return page;
	}

	/*
	 * @Override public Page<WorkFlowView> findWorkFlowViewByCriteria(
	 * WorkFlowQueryCriteria criteria,HPageRequest pageRequest) { StringBuffer
	 * sql=new StringBuffer(); sql.append("SELECT")
	 * .append(" wf.ID [workFlow.id],")
	 * .append(" wf.VERSION [workFlow.version],")
	 * .append(" wf.WORK_FLOW_NUMBER [workFlow.workFlowNumber],")
	 * .append(" wf.WORK_FLOW_NAME [workFlow.workFlowName],")
	 * .append(" wf.DESCRIPTION [workFlow.description],")
	 * .append(" wfd.id [workFlowDetail.id],")
	 * .append(" wfd.version [workFlowDetail.version],")
	 * .append(" wfd.LINE_NUMBER [workFlowDetail.lineNumber],")
	 * .append(" wfd.CURRENT_STATUS [workFlowDetail.currentStatus],")
	 * .append(" wfd.ROLE [workFlowDetail.role],")
	 * .append(" wfd.ACTION [workFlowDetail.action],")
	 * .append(" wfd.ADDED_RELATION [workFlowDetail.addedRelation],")
	 * .append(" wfd.ADDED_RELATION_CODE [workFlowDetail.addedRelationCode],")
	 * .append(" wfd.NEXT_STATUS [workFlowDetail.nextStatus],")
	 * .append(" wfd.STATUS_DISPLAY [workFlowDetail.statusDisplay],")
	 * .append(" wfd.EXECUTE_METHOD [workFlowDetail.executeMethod],")
	 * .append(" wfd.REASON_MANDATORY [workFlowDetail.reasonMandatory],")
	 * .append(" wfd.READONLY [workFlowDetail.readOnly],")
	 * .append(" wfd.EXECUTE_METHOD_CODE [workFlowDetail.executeMethodCode]");
	 * String selectCols = sql.toString(); sql = new StringBuffer();
	 * sql.append(" FROM") .append(" work_flow wf,")
	 * .append(" work_flow_detail wfd") .append(
	 * " LEFT JOIN SY_work_flow_method md on wfd.EXECUTE_METHOD_CODE = md.METHOD_KEY "
	 * ) .append(
	 * " LEFT JOIN SY_work_flow_method rel on wfd.ADDED_RELATION_CODE = rel.METHOD_KEY "
	 * ) .append(" where ") .append(" wfd.WORK_FLOW_ID=wf.ID");
	 * if(!CommonUtil.isNull(criteria.getCurrentStatus())){
	 * sql.append(" and wfd.CURRENT_STATUS=:currentStatus "); }
	 * if(!CommonUtil.isNull(criteria.getRole())){
	 * sql.append(" and wfd.ROLE=:role "); }
	 * if(!CommonUtil.isNull(criteria.getAction())){
	 * sql.append(" and wfd.ACTION=:action "); }
	 * if(!CommonUtil.isNull(criteria.getNextStatus())){
	 * sql.append(" and wfd.NEXT_STATUS=:nextStatus "); }
	 * if(!CommonUtil.isNull(criteria.getWorkFlowNumber())){
	 * sql.append(" and wf.WORK_FLOW_NUMBER like '%"
	 * ).append(criteria.getWorkFlowNumber()).append("%'"); }
	 * if(!CommonUtil.isNull(criteria.getWorkFlowName())){
	 * sql.append(" and wf.WORK_FLOW_NAME like '%"
	 * ).append(criteria.getWorkFlowName()).append("%'"); }
	 * 
	 * if(!CommonUtil.isNull(criteria.getExecuteMethodCode()))
	 * sql.append(" and md.METHOD_KEY = '"
	 * ).append(criteria.getExecuteMethodCode()).append("'");
	 * if(!CommonUtil.isNull(criteria.getAddedRelationCode()))
	 * sql.append(" and rel.METHOD_KEY = '"
	 * ).append(criteria.getAddedRelationCode()).append("'");
	 * if(!CommonUtil.isNull(criteria.getModifyStatus())){
	 * sql.append(" and wf.MODIFY_STATUS in("+criteria.getModifyStatus()+") ");
	 * } String fromTable = sql.toString(); String defaultSortBy =
	 * " order by wf.WORK_FLOW_NUMBER,wfd.LINE_NUMBER "; Page<WorkFlowView> page
	 * = this.sqlToModelService.executeNativeQueryForPage(selectCols, fromTable,
	 * defaultSortBy, criteria, pageRequest, WorkFlowView.class);
	 * page.getContent(); return page; }
	 */

	@Override
	public boolean checkClaimExistsWorkFlowById(Long id) {
		// 2012-11-8逻辑修改为,修改完成的,提示是否复制一份
		WorkFlow wf = this.getById(WorkFlow.class, id);
		if (CommonUtil.isNull(wf)) {
			throw new ValidateException("此流程不存在。");
		}
		return false;
	}

	//
	//
	// private String setLog(Map<String,LookupCode> mapCode,WorkFlowDetail
	// oldwfd,WorkFlowDetail newWfd ){
	// StringBuffer log=new StringBuffer();
	// WorkFlow workFlow = this.getWorkFlowById(oldwfd.getWorkFlowId());
	// log.append("被修改过流程:【").append(workFlow.getWorkFlowName()).append("】");
	// log.append("被修改字段:【").append(SaConstants.WORKFLOW_DETAIL_PROPERTIE.get(newWfd.getUpdatePropertieName())).append("】\r\n");
	// log.append("修改前:【").append(YqBeanUtil.getPropertyValue(oldwfd,
	// newWfd.getUpdatePropertieName())).append("】\r\n");
	// log.append("修改后:【").append(YqBeanUtil.getPropertyValue(newWfd,
	// newWfd.getUpdatePropertieName())).append("】\r\n");
	// log.append("------------------------------------------------------\r\n");
	// return log.toString();
	// }

	private String getLookupName(Map<String, LookupCode> mapCode, String keyCode) {
		return mapCode.get(keyCode).getLookupName();
	}

	@Override
	public void batchUpdateColByIds(Long[] ids, String field, String fieldValue) {
		for (Long id : ids) {
			this.execute(
					"update WorkFlowDetail set " + field + "=? where id=?",
					fieldValue, id);
		}
	}

	/*
	 * @Override public Page<WorkFlowView> findWorkFlowView2ByCriteria(
	 * WorkFlowQueryCriteria criteria, HPageRequest pageRequest) { StringBuffer
	 * sb =new StringBuffer(); sb.append("SELECT ")
	 * .append("wf.ID [workFlow.id],") .append("wf.VERSION [workFlow.version],")
	 * .append("wf.WORK_FLOW_NUMBER [workFlow.workFlowNumber],")
	 * .append("wf.WORK_FLOW_NAME [workFlow.workFlowName],")
	 * .append("wf.DESCRIPTION [workFlow.description],")
	 * .append("wf.MODIFY_STATUS [workFlow.modifyStatus]"); String selectCols =
	 * sb.toString(); sb = new StringBuffer(); sb.append(" FROM")
	 * .append(" SY_work_flow wf where");
	 * if(CommonUtil.isNull(criteria.getUsing())){ sb.append(" 1=1 "); }
	 * if(!CommonUtil.isNull(criteria.getWorkFlowNumber())){
	 * sb.append(" and wf.WORK_FLOW_NUMBER like '%"
	 * ).append(criteria.getWorkFlowNumber()).append("%'"); }
	 * if(!CommonUtil.isNull(criteria.getWorkFlowName())){
	 * sb.append(" and wf.WORK_FLOW_NAME like '%"
	 * ).append(criteria.getWorkFlowName()).append("%'"); }
	 * if(!CommonUtil.isNull(criteria.getModifyStatus())){
	 * sb.append(" and wf.MODIFY_STATUS in("+criteria.getModifyStatus()+") "); }
	 * if(!CommonUtil.isNull(criteria.getSysWorkKeyCode())){
	 * sb.append(" and sw.key_code =:sysWorkKeyCode "); }
	 * if(!CommonUtil.isNull(criteria.getDescription())){
	 * sb.append(" and wf.DESCRIPTION like '%"
	 * ).append(criteria.getDescription()).append("%'"); } String fromTable =
	 * sb.toString(); return
	 * this.sqlToModelService.executeNativeQueryForPage(selectCols, fromTable,
	 * null, criteria, pageRequest, WorkFlowView.class); }
	 */

	@Override
	public List<WorkFlowDetail> findWorkFlowDetailStatusByIds(Long[] workFlowId)
			throws WorkFlowException {
		String ids = "";
		for (Long id : workFlowId) {
			ids += id + ",";
		}
		return this.find(" from WorkFlowDetail  where workFlowId in ("
				+ ids.substring(0, ids.length() - 1)
				+ ") and statusDisplay !='' order by workFlowId,lineNumber ");
	}

	// public WorkFlowDetail getNextWorkFlowForSkip(Long workFlowId, Object
	// source, String currentStatus ) {
	// String action = SaConstants.WAT_FINAP;
	// List<WorkFlowDetail> list =
	// this.find(" from WorkFlowDetail where workFlowId = ? and currentStatus = ? and action = ? ",workFlowId,
	// currentStatus,action) ;
	// if (CommonUtil.isNull(list)) {
	// return null;
	// }
	// list = this.addMethodObj(list);
	//
	// String addedRelMsg = "";
	// String relations = "";
	// List<WorkFlowDetail> newList = new ArrayList<WorkFlowDetail>();
	// for (WorkFlowDetail fd : list) {
	// if ( YqBeanUtil.isTrueExpression(source,
	// this.replaceExpressionName(fd.getUsedAddedRelation()))) {
	// newList.add(fd);
	// }
	// }
	// if (newList.size() == 0) {
	// return null;
	// }
	// if (newList.size() > 1) {
	// throw new WorkFlowException("状态为 "+currentStatus+"-"+
	// this.getNameByLookupKeyCode(currentStatus)
	// +", 执行跳过流程时，产生了多个工作流设定，详查条件！");
	// }
	// return newList.get(0);
	// }
	@Override
	public List<WorkFlowDetail> createWorkFlowDetail(
			List<WorkFlowDetail> workFlowDetails) {
		List<WorkFlowDetail> list = new ArrayList<WorkFlowDetail>();
		for (WorkFlowDetail detail : workFlowDetails) {
			WorkFlowDetail workFlowDetail = this.createWorkFlowDetail(detail);
			list.add(workFlowDetail);
		}
		return list;
	}

	/**
	 * 待定，权限管理做完后处理
	 * 
	 * @return
	 */
	// protected String[] getRoles() {
	/*
	 * if (true) { //测试用 //return new
	 * String[]{WfConstants.WORKFLOW_ROLE_CHAIN};//店长 //return new
	 * String[]{WfConstants.WORKFLOW_ROLE_CUST_MANGER}; return new
	 * String[]{"WFR-02"}; //初审 }
	 */
	/*
	 * if (SASecurityUtil.isChainManager()) { String[] wfrs = new String[1];
	 * wfrs[0] = WfConstants.WORKFLOW_ROLE_CHAIN; return wfrs; } else {
	 * Set<String> workFlowRoleSet = SASecurityUtil.getWorkFlowRoles(); if
	 * (!CommonUtil.isNull(WfConstants.specRoles)) { for (String role :
	 * WfConstants.specRoles) { workFlowRoleSet.add(role); } } String[] wfrs =
	 * new String[workFlowRoleSet.size()]; int i=0; for (Iterator<String> iter1
	 * = workFlowRoleSet.iterator(); iter1 .hasNext();) { wfrs[i] =
	 * iter1.next(); i++; } return wfrs; }
	 */
	// return null;
	// }

	// protected String getLoginUserCode() {
	/*
	 * if (true) { return "3078"; //客户经理 测试 }
	 */
	// return SASecurityUtil.getCurrUserCode();
	// return null;
	// }

	private Boolean isTrueFun(Object bean, Map variables, String funCode) {
		if (CommonUtil.isNull(funCode)) {
			return true;
		}
		return this.parseService.isTrueByEleNumberWithPars(bean, funCode,
				variables);
		// return this.parseService.isTrueByEleNumber(bean, funCode, variables,
		// null);
	}

	@Override
	public List<WorkFlowGraphDetail> findGraphDetailByWorkFlowId(Long workFlowId) {
		String otherCols = "  (SELECT lc.lookup_name FROM yq_lookup_code lc WHERE lc.key_code = a.graph_node ) graphNodeName  ";
		String mixSql = " select a.*," + otherCols
				+ " FROM yq_work_flow_graph_detail a WHERE a.work_flow_id = "
				+ workFlowId + " ORDER BY a.line_Number ";
		List<WorkFlowGraphDetail> list = this.sqlToModelService
				.executeNativeQuery(mixSql, null, WorkFlowGraphDetail.class);
		for (WorkFlowGraphDetail d : list) {
			String sourceStatuses2String = "";
			if (CommonUtil.isNotNull(d.getSourceStatusesList())) {
				List<String> statuses = d.getSourceStatusesList();
				for (String keyCode : statuses) {
					LookupCode c = lookupCodeService
							.getLookupCodeByKeyCode(keyCode);
					if (CommonUtil.isNotNull(c)) {
						sourceStatuses2String += c.getLookupName() + ",";
					}
				}
			}
			if (CommonUtil.isNotNull(sourceStatuses2String)) {
				d.getPageMap().put(
						"sourceStatuses2String",
						sourceStatuses2String.substring(0,
								sourceStatuses2String.length() - 1));
			}
			d.setWorkFlowDetails(this.findWorkFlowDetailsByGraphDetailId(d.getId()));
		}
		return list;
	}

	/**
	 * view
	 * 
	 * 
	 */

	@Override
	public WorkFlowView getWorkFlowViewByWorkFlowId(Long workFlowId) {
		WorkFlowView view = new WorkFlowView();
		WorkFlow workFlow = this.getWorkFlowById(workFlowId);
		List<WorkFlowDetail> workFlowDetail = this
				.findWorkFlowDetails(workFlowId);
		List<WorkFlowGraphDetail> gs = this.findGraphDetailByWorkFlowId(workFlowId);
		view.setWorkFlow(workFlow);
		view.setWorkFlowDetails(workFlowDetail);
		view.setGraphDetails(gs);
 
		return view;
	}

	@Override
	public WorkFlow saveWorkFlowView(WorkFlowView view) {
		WorkFlow workFlow = view.getWorkFlow();
		if (CommonUtil.isNull(workFlow.getCategoryCode())) {
			throw new ValidateException("审批类型不能为空。");
		}
		if (CommonUtil.isNull(workFlow.getId())) {
			workFlow = this.createWorkFlow(view.getWorkFlow());
		} else {
			workFlow = this.updateWorkFlow(view.getWorkFlow());
		}
		// findGraphDetailByWorkFlowId(workFlow.getId());
//		List<WorkFlowDetail> details = view.getWorkFlowDetails();
//
//		if (CommonUtil.isNotNull(details)) {
//			for (WorkFlowDetail detail : details) {
//				if (CommonUtil.isNull(detail.getId())) {
//					detail.setWorkFlowId(workFlow.getId());
//					detail = this.createWorkFlowDetail(detail);
//				} else {
//					detail = this.updateWorkFlowDetail(detail);
//				}
//			}
//			List<WorkFlowDetail> oldDetails = this.findWorkFlowDetails(workFlow
//					.getId());
//			List<Long> detailIds = CommonUtil.getDataListByProperty(details,
//					"id", Long.class);
//			for (WorkFlowDetail oldDetail : oldDetails) {
//				if (!detailIds.contains(oldDetail.getId())) {
//					this.deleteWorkFlowDetail(oldDetail.getId(),
//							oldDetail.getVersion());
//				}
//			}
//		}
		return workFlow;
	}

	@Override
	public void deleteWorkFlowView(WorkFlowView view) {
		WorkFlow workFlow = view.getWorkFlow();
		// 删除明细
		List<WorkFlowDetail> details = view.getWorkFlowDetails();
		if (CommonUtil.isNotNull(details)) {
			for (WorkFlowDetail detail : details) {
				this.deleteWorkFlowDetail(detail.getId(), detail.getVersion());
			}
		}
		// 删除流程图
		List<WorkFlowGraphDetail> graphDetails = this
				.findGraphDetailByWorkFlowId(workFlow.getId());
		this.delete(graphDetails);
		this.deleteWorkFlow(workFlow.getId(), workFlow.getVersion());
	}
	@Override
	public WorkFlowGraphDetail getWorkFlowGraphDetail(Long workFlowGraphDetailId){
		return this.getById(WorkFlowGraphDetail.class, workFlowGraphDetailId);
	}
	@Override
	public void deleteWorkFlowGraphDetailByOne(WorkFlowGraphDetail workFlowGraphDetail){
		List<WorkFlowDetail> wfdList = this.findWorkFlowDetailsByGraphDetailId(workFlowGraphDetail.getId());
		for(WorkFlowDetail w : wfdList){
			this.deleteWorkFlowDetail(w);
		}
		this.deleteWorkFlowGraphDetail(workFlowGraphDetail);
	}
	@Override
	public WorkFlow saveWorkFlowGraphDetail(Long workFlowId,WorkFlowGraphDetail detail){
		WorkFlow workFlow = this.getWorkFlowById(workFlowId);
		if (CommonUtil.isNull(workFlow)||CommonUtil.isNull(workFlow.getId())) {
			throw new ValidateException("流程为空时不能保存审批流程图");
		}
		
		List<String> validStatusList = new ArrayList<String>();
		List<WorkFlowGraphDetail> wgd = this.findGraphDetailByWorkFlowId(workFlow.getId());
		//若新增时，增加前端传来状态(此操作确保整个流程的状态唯一)
		if(CommonUtil.isNull(detail.getId())){
			validStatusList.addAll(detail.getSourceStatusesList());
		}
		for(WorkFlowGraphDetail w: wgd){
			List<String> ss = w.getSourceStatusesList();
			//若修改时，update前端传来状态(此操作确保整个流程的状态唯一)
			if(w.getId().equals(detail.getId())){
				ss = detail.getSourceStatusesList();
			}
			if (CommonUtil.isNotNull(ss)) {
				for (String s : ss) {
					if (validStatusList.contains(s)) {
						throw new ValidateException("流程不能包含2个相同的业务状态");
					}
					validStatusList.add(s);
				}
			}
		}
		
		if (CommonUtil.isNull(detail.getId())) {
			detail.setWorkFlowId(workFlow.getId());
		}else{
			List<WorkFlowDetail> wfdList = this.findWorkFlowDetailsByGraphDetailId(detail.getId());
			for(WorkFlowDetail w : wfdList){
				if (!detail.getSourceStatusesList().contains(w.getCurrentStatus())) {
					this.deleteWorkFlowDetail(w);
				}
			}
		}	
		detail = this.save(detail);
		return workFlow;
	}
	@Override
	public WorkFlow saveWorkFlowOpenPageView(WorkFlowOpenPageView pageView) {
		WorkFlow workFlow = pageView.getWorkFlow();
		if (CommonUtil.isNull(workFlow)) {
			throw new ValidateException("流程为空时不能保存审批流程图");
		}
		List<WorkFlowGraphDetail> graphDetails = pageView.getGraphDetail();
		// 验证业务状态是否重复
		List<String> validStatusList = new ArrayList<String>();
		for (WorkFlowGraphDetail graphDetail : graphDetails) {
			List<String> sourceStatuses = graphDetail.getSourceStatusesList();
			if (CommonUtil.isNotNull(sourceStatuses)) {
				for (String sourceStatuse : sourceStatuses) {
					if (validStatusList.contains(sourceStatuse)) {
						throw new ValidateException("流程不能包含2个相同的业务状态");
					}
					validStatusList.add(sourceStatuse);
				}
			}
		}
		// 同步流程明细中使用的业务状态
		List<WorkFlowQueryCriteria> detailStatusList = this
				.findStatusUsedByWorkFlowDetails(workFlow.getId());
		for (WorkFlowQueryCriteria o : detailStatusList) {
			if (!validStatusList.contains(o.getStatus())) {
				throw new ValidateException("业务状态【"
						+ lookupCodeService.getLookupCodeByKeyCode(
								o.getStatus()).getLookupName()
						+ "】已被流程明细使用，请先删除对应的流程明细!");
			}
		}
		for (WorkFlowGraphDetail graphDetail : graphDetails) {
			if (CommonUtil.isNull(graphDetail.getId())) {
				graphDetail.setWorkFlowId(workFlow.getId());
				// graphDetail.setSourceStatuses(detail.getCurrentStatus());
				this.createWorkFlowGraphDetail(graphDetail);
			} else {
				this.updateWorkFlowGraphDetail(graphDetail);
			}
		}
		List<WorkFlowGraphDetail> oldGraphDetails = this
				.findGraphDetailByWorkFlowId(workFlow.getId());
		List<Long> detailIds = CommonUtil.getDataListByProperty(graphDetails,
				"id", Long.class);
		for (WorkFlowGraphDetail oldGraphDetail : oldGraphDetails) {
			if (!detailIds.contains(oldGraphDetail.getId())) {
				// this.deleteWorkFlowGraphDetail(oldGraphDetail.getId(),oldGraphDetail.getVersion());
				this.deleteWorkFlowGraphDetail(oldGraphDetail);
			}
		}
		return workFlow;
	}

	@Override
	public boolean isExistWorkFlowGraphDetail(Long workFlowId) {
		String sql = " SELECT 1 FROM yq_work_flow_graph_detail g WHERE g.work_flow_id = ? ";
		// "AND g.graph_node NOT IN ('"+WfConstants.WORK_FLOW_GRAPH_START+"','"+WfConstants.WORK_FLOW_GRAPH_END+"')";
		return this.existsNativeQuery(sql, workFlowId);
	}

	/**
	 * 查找审批1的work_flow_detail使用中的业务状态
	 * 
	 * @return
	 */
	public List<WorkFlowQueryCriteria> findStatusUsedByWorkFlowDetails(
			Long workFlowId) {
		StringBuffer sql = new StringBuffer();
		sql.append(
				"select DISTINCT d.CURRENT_STATUS 'status' from yq_work_flow_detail d where  d.WORK_FLOW_ID = "
						+ workFlowId)
				.append(" UNION")
				.append(" select DISTINCT d.NEXT_STATUS 'status' from yq_work_flow_detail d where  d.WORK_FLOW_ID = "
						+ workFlowId);
		return sqlToModelService.executeNativeQuery(sql.toString(), null,
				WorkFlowQueryCriteria.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<WorkFlowDetail> findNextWorkFlowList(Long workFlowId, 
			Object source, Map variables, String currentStatus ) throws WorkFlowException {
		String query = "select "+JPAUtils.genEntityCols(WorkFlowDetail.class, "a", null)
				+" from yq_work_flow_DETAIL a "
				+ " inner join yq_lookup_code act on act.key_code = a.action"
				+ "   and act.segment3 = 'Y'"  // 是审批流程的才会发
				+ " where a.work_flow_id = " + workFlowId
				+ "   and a.CURRENT_STATUS = '"+currentStatus+"' ";
				
		List<WorkFlowDetail> workFlowDetails = this.sqlToModelService.executeNativeQuery(query, null, WorkFlowDetail.class);
		if (CommonUtil.isNull(workFlowDetails)) {
			return null;
		}
		List<WorkFlowDetail> retList = new ArrayList<WorkFlowDetail>();
		for (WorkFlowDetail fd : workFlowDetails) {
			if ( this.isTrueFun(source, variables,
							fd.getRelationFunction())) {
				retList.add(fd);
			}  
		}
 		return retList;
	}
	

	@Override
	public List<WorkFlowDetail> findWorkFlowDetailsByGraphDetailId(
			Long graphDetailId) {
		StringBuffer query = new StringBuffer();
		query.append("SELECT")
				.append(" wfd.*,")
				.append(" ylc_c.lookup_name currentStatusName,")
				.append(" ylc_n.lookup_name nextStatusName,")
				.append(" ylc_a.lookup_name actionName,")
				.append(" pe.ele_name relationFunctionName,")
				.append(" ylc_r.lookup_name roleName")
				.append(" FROM yq_work_flow_graph_detail wfgd, yq_work_flow_detail wfd ")
				.append(" LEFT JOIN yq_lookup_code ylc_c ON ylc_c.key_code = wfd.current_status")
				.append(" LEFT JOIN yq_lookup_code ylc_n ON ylc_n.key_code = wfd.next_status")
				.append(" LEFT JOIN yq_lookup_code ylc_a ON ylc_a.key_code = wfd.action")
				.append(" LEFT JOIN yq_lookup_code ylc_r ON ylc_r.key_code = wfd.role")
				.append(" LEFT JOIN yq_parse_element pe ON pe.ele_number = wfd.relation_function")
				.append(" WHERE wfgd.ID = " + graphDetailId)
				.append("   and wfd.work_flow_id = wfgd.work_flow_id ")
				.append("   and INSTR(wfgd.SOURCE_STATUSES,wfd.current_status) >0 ");
 		String defaultSortBy = " ORDER BY wfd.line_number";
		List<WorkFlowDetail> list = sqlToModelService.executeNativeQuery(query
				.append(defaultSortBy).toString(), null, WorkFlowDetail.class);
		for (WorkFlowDetail workFlowDetail : list) {
			resetElementName(workFlowDetail);
		}
		return list;
	}
	
	@Override
	public WorkFlowEntity getWorkFlowEntityById(Long id) {
		return this.getById(WorkFlowEntity.class, id);
	}

	@Override
	public void deleteWorkFlowDetail(WorkFlowDetail workFlowDetail) {
		this.delete(workFlowDetail);
	}

	@Override
	public void saveWorkFlowDetail(WorkFlowDetail workFlowDetail) {
		validate();
		this.save(workFlowDetail);
	}

	

	private void resetElementName(WorkFlowDetail workFlowDetail) {
		workFlowDetail.setExecuteFunctionName(this
				.translationFunctionCode(workFlowDetail.getExecuteFunction()));
		workFlowDetail.setRelationFunctionName(this
				.translationFunctionCode(workFlowDetail.getRelationFunction()));
		//workFlowDetail.setFalseMessageStr(this
		//		.translationFunctionCode(workFlowDetail.getFalseMessage()));

	}
	

	private String translationFunctionCode(String functionCode) {
		if (CommonUtil.isNotNull(functionCode) && !"0".equals(functionCode)) {
			return parseService
					.translationEleCodeWithParsToChiness(functionCode,null);
		}
		return "";
	}

	@Override
	public List<SelectItem> findWorkFlowStatuseBygraphDetailIds(Long graphDetailId){
		String query = " select  lcc.KEY_CODE itemKey , lcc.LOOKUP_NAME itemName, "
				+"  (select wfgd.GRAPH_NODE from yq_work_flow_graph_detail wfgd where wfgd.id = " + graphDetailId+" and LOCATE(lcc.KEY_CODE,wfgd.source_statuses) > 0 ) remark "
				+"  from yq_lookup_code lcc "
				+" where lcc.CATEGORY_CODE = 'WST' "
				+"  and exists (select 1 from yq_work_flow_graph_detail wfgd where wfgd.id = " + graphDetailId+" and LOCATE(lcc.KEY_CODE,wfgd.source_statuses) > 0 ) ";

		return this.sqlToModelService.executeNativeQuery(query.toString(), null, SelectItem.class);
	}

	/**
	 * WorkFlow
	 *
	 *
	 */

	@Override
	public List<SelectItem> findWorkFlowStatuses(Long workFlowId){

		/*StringBuffer childTable = new StringBuffer();
		childTable.append("SELECT a.id,SUBSTRING_INDEX(SUBSTRING_INDEX(a.source_statuses,',',b.help_topic_id+1),',',-1) sourcestatuses")
				  .append(" FROM yq_work_flow_graph_detail a")
				  .append(" JOIN mysql.help_topic b ON b.help_topic_id < (LENGTH(a.source_statuses) - LENGTH(REPLACE(a.source_statuses,',',''))+1)")
				  .append(" WHERE a.work_flow_id = " + workFlowId);

		StringBuffer query = new StringBuffer();
		query.append("SELECT a.sourcestatuses itemKey,l.lookup_name itemName")
			 .append(" FROM (").append(childTable).append(") a")
			 .append(" LEFT JOIN yq_lookup_code l ON l.key_code = a.sourcestatuses")
			 .append(" WHERE l.lookup_name IS NOT NULL")
			 .append(" ORDER BY a.id");*/

		String query = " select  lcc.KEY_CODE itemKey , lcc.LOOKUP_NAME itemName, "
				+"  (select wfgd.GRAPH_NODE from yq_work_flow_graph_detail wfgd where wfgd.WORK_FLOW_ID = " + workFlowId+" and LOCATE(lcc.KEY_CODE,wfgd.source_statuses) > 0 ) remark "
				+"  from yq_lookup_code lcc "
				+" where lcc.CATEGORY_CODE = 'WST' "
				+"  and exists (select 1 from yq_work_flow_graph_detail wfgd where wfgd.WORK_FLOW_ID = " + workFlowId+" and LOCATE(lcc.KEY_CODE,wfgd.source_statuses) > 0 ) ";

		return this.sqlToModelService.executeNativeQuery(query.toString(), null, SelectItem.class);
	}


	@Override
	public List<SelectItem> getWorkFlowSkipPageDefineList(Long workFlowId) {
		String query = "select a.id itemKey, a.name itemName from yq_Work_Flow_SKIP_PAGE_DEFINE a, yq_work_flow wf   " +
				"where a.category_code = wf.CATEGORY_CODE " +
				"  and wf.id = "+workFlowId;
		return this.sqlToModelService.executeNativeQuery(query , null, SelectItem.class);
	}
	
	@Override
	public boolean isTrueWorkFlowDetail(ParseRootAndVariable pr,
			WorkFlowDetail workFlowDetail) {
		boolean bool = true;
		if (!CommonUtil.isNull(workFlowDetail.getRelationFunction())) {
			return this.parseService.isTrueByEleNumberWithPars(pr, workFlowDetail.getRelationFunction(),
					null);
		}
		return true;
	}

	@Override
	public WorkFlowEntityCategory getCategoryByCategoryCode(String entityCategory) {
		 
		return this.initConstantsService.getEntityCategory(entityCategory);
	}


}
