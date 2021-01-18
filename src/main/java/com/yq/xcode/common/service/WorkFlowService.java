package com.yq.xcode.common.service;

import java.util.List;
import java.util.Map;

import com.yq.xcode.common.bean.SelectItem;
import com.yq.xcode.common.criteria.WorkFlowQueryCriteria;
import com.yq.xcode.common.model.WorkFlow;
import com.yq.xcode.common.bean.SelectItem;
import org.springframework.data.domain.Page;

import com.yq.xcode.common.bean.ParseRootAndVariable;
import com.yq.xcode.common.bean.WorkFlowEntityCategory;
import com.yq.xcode.common.bean.WorkFlowOpenPageView;
import com.yq.xcode.common.bean.WorkFlowView;
import com.yq.xcode.common.criteria.WorkFlowDetailQueryCriteria;
import com.yq.xcode.common.criteria.WorkFlowQueryCriteria;
import com.yq.xcode.common.exception.WorkFlowException;
import com.yq.xcode.common.model.WorkFlow;
import com.yq.xcode.common.model.WorkFlowDetail;
import com.yq.xcode.common.model.WorkFlowEntity;
import com.yq.xcode.common.model.WorkFlowGraphDetail;
import com.yq.xcode.common.springdata.HPageRequest;



public interface WorkFlowService {

	/**
	 * 根据
	 * @param id
	 * @return
	 */
	public WorkFlow getWorkFlowById(Long id);
	
	
	public List<WorkFlow> findAllWorkFlows();

	public Page<WorkFlow> findWorkFlows(WorkFlowQueryCriteria criteria );

	public WorkFlow createWorkFlow(WorkFlow workFlow);

	public void deleteWorkFlow(Long id, Long version);

	public void deleteWorkFlows(Long[] ids, Long[] versions);

	public List<WorkFlowDetail> findWorkFlowDetails(Long workFlowId);

	public WorkFlowDetail createWorkFlowDetail(WorkFlowDetail workFlowDetail);

	public List<WorkFlowDetail> createWorkFlowDetail(List<WorkFlowDetail> workFlowDetails);

	public WorkFlowDetail updateWorkFlowDetail(WorkFlowDetail WorkFlowDetail);

	public WorkFlowDetail getWorkFlowDetailById(Long id);


	public void deleteWorkFlowDetails(Long[] ids, Long[] versions);

	public void deleteWorkFlowDetail(Long id, Long version);
	public void deleteWorkFlowDetail(WorkFlowDetail workFlowDetail);
	
	public void saveWorkFlowDetail(WorkFlowDetail workFlowDetail);
	
	/**
	 * 根据action得到工作流,
	 * 没有记录 NOT_FOUND_WORK_FLOW = "没有工作流设定";
	 * 返回多行 MORE_THEN_ONE_WORK_FLOW = "设定有吴，返回多个状态";
	 * Map variables 变量，可在表达式中引用 
	 * @param claim
	 * @param action
	 * @param reason
	 */
	public WorkFlowDetail getNextWorkFlow(Long workFlowId, String[] roles,  Object entity, Map variables, String currentStatus ,String action) throws WorkFlowException ;

	/**
	 * 根据action得到工作流,
	 * 没有记录 NOT_FOUND_WORK_FLOW = "没有工作流设定";
	 * 返回多行 MORE_THEN_ONE_WORK_FLOW = "设定有吴，返回多个状态";
	 * List<WorkFlowDetail> workFlowDetails参数 为batch操作用
	 * @param claim
	 * @param action
	 * @param reason
	 */
	public WorkFlowDetail getNextWorkFlow(Long workFlowId, String[] roles,  Object entity, Map variables, String currentStatus ,String action, List<WorkFlowDetail> workFlowDetails) throws WorkFlowException ;
/**
	 * 得到工作流的状态变化图，画出对应流程变化图
	 * @param claim
	 * @param action
	 * @param reason
	 */
	public List<WorkFlowDetail> findWorkFlowDetailStatus(Long workFlowId) throws WorkFlowException ;
	
	/**
	 * 得到工作流的状态变化图，画出对应流程变化图
	 * @param claim
	 * @param action
	 * @param reason
	 */
	public List<WorkFlowDetail> findWorkFlowDetailStatusByIds(Long[] workFlowId) throws WorkFlowException ;

	/**
	 * 根据当前用户的审批角色，查找本用户这些状态下可做的操作，status 空，表示所有状态,要注意，如果有一个action操作必须输入原因，在页面提交时就弹出窗口输入原因
	 * 如果一个action有多条WFD,且有的要输入原因，有的不需输入原因， 此种情况在提交时根据action在确定是否需输入原因，用EXCEPTION信息处理,主要为所有申报单查询后批量审批用
	 */
	public List<WorkFlowDetail> findWorkFlowDetails(String[] roles, String... statuses);

	public List<WorkFlowDetail> findWorkFlowDetails(Long workFlowId, String[] roles, String status);

	public WorkFlow copyWorkFlowById(Long workFlowId);

	public void copyWorkFlowByIds(Long[] workFlowId);

	public WorkFlowDetail copyWorkFlowDetail(Long workFlowDetailId);

	public List<WorkFlowDetail> findWorkFlowDetails(Long workFlowId, String[] roles, String status, String action);

	public Page<WorkFlowDetail> findWorkFlowDetailsPageByCriteria(WorkFlowDetailQueryCriteria criteria );
	
	public List<WorkFlowDetail> findWorkFlowDetailsByCriteria(WorkFlowDetailQueryCriteria criteria);
	
	/**
	 * 审批流程设置查询(包括明细)
	 * 
	 */
	//public Page<WorkFlowView> findWorkFlowViewByCriteria(WorkFlowQueryCriteria criteria, HPageRequest pageRequest);
	/**
	 * 根据workFlow ID判断是否被申报单使用
	 */
	public boolean checkClaimExistsWorkFlowById(Long id);

	
	/**
	 * 批量修改单列
	 * @param ids ==workFlowDetailId
	 * @param version
	 * @param workFlowDetail
	 */
	public void batchUpdateColByIds(Long[] ids,String field,String fieldValue);
	
	/**
	 * 审批流程设置(显示系统事务,不包括明细)
	 * 
	 */
	//public Page<WorkFlowView> findWorkFlowView2ByCriteria(WorkFlowQueryCriteria criteria,HPageRequest pageRequest);

	public String genErrorMsg(Long workFlowId, String[] roles, String currentStatus, String action, String errorCategory);

	/**
	 * WorkFlowDetail 的表达式是否为真
	 * @param entity
	 * @param WorkFlowDetail
	 * @return
	 */
	public boolean isTrueWorkFlowDetail(Object entity, Map variables, WorkFlowDetail WorkFlowDetail);

	public List<WorkFlowGraphDetail> findGraphDetailByWorkFlowId(Long workFlowId);
	public WorkFlowGraphDetail getWorkFlowGraphDetail(Long workFlowGraphDetailId);
	//删除流程图
	public void deleteWorkFlowGraphDetailByOne(WorkFlowGraphDetail workFlowGraphDetail);

	public WorkFlowView getWorkFlowViewByWorkFlowId(Long workFlowId);

	public WorkFlow saveWorkFlowView(WorkFlowView view);

	public void deleteWorkFlowView(WorkFlowView view);

	public WorkFlow saveWorkFlowOpenPageView(WorkFlowOpenPageView pageView);

	public boolean isExistWorkFlowGraphDetail(Long workFlowId);
	
	public WorkFlowEntity getWorkFlowEntityById(Long id);

	
	
	public List<WorkFlowDetail> findWorkFlowDetailsByGraphDetailId(Long graphDetailId);
	public WorkFlow saveWorkFlowGraphDetail(Long workFlowId,WorkFlowGraphDetail detail);


	/**
	 * 查找下一审批人对应的流程
	 * @param workFlowId
	 * @param source
	 * @param variables
	 * @param currentStatus 
	 * @return
	 * @throws WorkFlowException
	 */
	public List<WorkFlowDetail> findNextWorkFlowList(Long workFlowId, Object source,
			Map variables, String currentStatus )
			throws WorkFlowException;

	public boolean isTrueWorkFlowDetail(ParseRootAndVariable pr,
			WorkFlowDetail workFlowDetail);
	
	public WorkFlowEntityCategory getCategoryByCategoryCode(String entityCategory);

	public List<SelectItem> findWorkFlowStatuseBygraphDetailIds(Long graphDetailId);

	public List<SelectItem> findWorkFlowStatuses(Long workFlowId);

	public List<SelectItem> getWorkFlowSkipPageDefineList(Long workFlowId);

}
	
