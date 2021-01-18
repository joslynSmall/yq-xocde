package com.yq.xcode.common.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.yq.xcode.annotation.EntityTableAlias;
import com.yq.xcode.common.bean.ListPageDefine;
import com.yq.xcode.common.bean.WfConstants;
import com.yq.xcode.common.bean.WorkFlowEntityCategory;
import com.yq.xcode.common.bean.WorkFlowEntityCategoryPage;
import com.yq.xcode.common.bean.WorkFlowEntityIntf;
import com.yq.xcode.common.bean.WorkFlowEntityView;
import com.yq.xcode.common.criteria.WorkFlowEntityMobileCriteria;
import com.yq.xcode.common.exception.ValidateException;
import com.yq.xcode.common.model.PageTag;
import com.yq.xcode.common.service.InitConstantsService;
import com.yq.xcode.common.service.SqlToModelService;
import com.yq.xcode.common.service.WorkFlowEntityQueryService;
import com.yq.xcode.common.service.WorkFlowEntityService;
import com.yq.xcode.common.service.WorkFlowService;
import com.yq.xcode.common.springdata.HWorkFlowEntityPageCriteria;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.YqBeanUtil;
import com.yq.xcode.common.utils.YqJsonUtil;
import com.yq.xcode.security.utils.YqSecurityUtils; 

@Service("WorkFlowEntityQueryService") 
public class WorkFlowEntityQueryServiceImpl extends YqJpaDataAccessObject implements WorkFlowEntityQueryService,ApplicationContextAware {
 
	@Autowired
	private SqlToModelService sqlToModelService;
	@Autowired
	private WorkFlowService workFlowService;
	@Autowired
	private InitConstantsService initConstantsService;
	
	
	private ApplicationContext applicationContext;
	
	private static Log Log = LogFactory.getLog(WorkFlowEntityQueryServiceImpl.class);
 
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;

	}


	@Override
	public List<WorkFlowEntityCategoryPage> findWorkFlowCategoryPageList(String stage) {
		List<WorkFlowEntityCategoryPage> list = new ArrayList<WorkFlowEntityCategoryPage>();
		if (CommonUtil.isNull(stage)) {
			throw new ValidateException("流程阶段必须输入 stage ");
		}
		
		
 
		for (WorkFlowEntityCategory cate : this.initConstantsService.getEntityCategoryList()) {
 
			WorkFlowEntityService service =   this.applicationContext.getBean(
					cate.getServiceName(), WorkFlowEntityService.class);
			WorkFlowEntityCategoryPage catePage = new WorkFlowEntityCategoryPage();
			catePage.setCategoryCode(cate.getCategoryCode());
			catePage.setCategoryName( cate.getCategoryName());
			catePage.setOpenUrl(cate.getOpenUrl());
 

			if (WfConstants.WorkFlow_stage.myprocess.equals(stage)) { 
				// 待我审批（我的任务）
				HWorkFlowEntityPageCriteria criteria = null;
				try {
					criteria = (HWorkFlowEntityPageCriteria)cate.getCriteriaClass().newInstance();
				} catch ( Exception e) { 
					e.printStackTrace();
					throw new ValidateException(cate.getCriteriaClass()+"CriteriaName 不能实例化");
				}  
				criteria.setStage(WfConstants.WorkFlow_stage.myprocess);
				criteria.setOnlyCount(true);
				Page page = service.findEntityPage(criteria);
				if ( page != null ) {
					long total = page.getTotalElements();
					catePage.setRecordCnt(Integer.parseInt(String.valueOf(total)));
				} else {
					catePage.setRecordCnt(0);
				}
				
			} // 其他不提供记录数 
			list.add(catePage);
 		}
		return list;
	}


	@Override
	public String getEntityIdQueryTable(String stage, String categoryCode ) {
		// 待我审批的操作
		if ( WfConstants.WorkFlow_stage.myprocess.equals(stage)) {
			return this.getMyProcessQueryTable(categoryCode);
		}else if ( WfConstants.WorkFlow_stage.confirmed.equals(stage)) {
			return this.getConfirmedQueryTable(categoryCode);
		}else if ( WfConstants.WorkFlow_stage.history.equals(stage)) {
			return this.getHistoryQueryTable(categoryCode);
		} else {
			throw new ValidateException("不存在的审批实体类型 category :  "+ categoryCode);
		} 
	}
	private String getConfirmedQueryTable(String categoryCode ) {  
			String query = " SELECT wfl.entity_id "
 				+" FROM  yq_work_flow_entity wfe "
				+ " inner join yq_work_flow_entity_action_log wfl "
				+ " on wfe.id = wfl.work_flow_entity_id  "
				+ " and wfl.action_by_code = '" + YqSecurityUtils.getLoginUserKey() + "' " 
				+"  WHERE wfe.entity_category = '" + categoryCode + "' ";
 		return "(" +query+" ) ";
	}
	
	private String getHistoryQueryTable(String categoryCode ) {  
		String query = " SELECT wfl.entity_id "
				+" FROM  yq_work_flow_entity_history wfe "
			+ " inner join yq_work_flow_entity_action_log wfl "
			+ " on wfe.id = wfl.work_flow_entity_id  "
			+ " and wfl.action_by_code = '" + YqSecurityUtils.getLoginUserKey() + "' " 
			+"  WHERE wfe.entity_category = '" + categoryCode + "' ";
		return "(" +query+" ) ";
    }


	// 待我审批的操作
	private String getMyProcessQueryTable(String categoryCode) {
		Set<String> res = YqSecurityUtils.getWorkFlowRoleResource();
		String query =  
				  " SELECT  wfe.ENTITY_ID "
				+ " FROM  yq_work_flow_detail wfd " 
				+ " inner join yq_lookup_code act "
				+ "   on act.key_code = wfd.action "
				+ "   and act.segment3 = 'Y' " 
				+ " inner join yq_lookup_code role "
				+ "   on role.key_code = wfd.role"
				+ " inner join yq_work_flow_entity wfe"
				+ "   on wfe.entity_status = wfd.current_status  " 
				+ "  and wfe.work_flow_id = wfd.work_flow_id  "
				+ "  and wfe.entity_category = '" + categoryCode + "' "
 				+ " WHERE  " 
				+  CommonUtil.getInCodeSql(" wfd.role ", res)
 				+ " and ifnull(wfd.NEXT_IS_SYS_ACTION,0) = 0" 
				+ " and wfd.CURRENT_Graph_NODE <> 'WNODE-START' "  
 				
				+ "   and ("
				//特殊角色，
				 +"         (      "
				 + "         role.segment1 = 'M'  "
				 + "         and INSTR(wfe.SPEC_ROLE_USER,concat(role.KEY_CODE,'-','"+YqSecurityUtils.getLoginUserKey()+"')) >0"
				 + "         ) " //
				 + "       or ifnull(role.segment1,'') != 'M' " 
				 + "    ) " ;
		return " ("+query+") ";
	}


	@Override
	public Page<WorkFlowEntityView> findWorkFlowEntityPageForMobile(WorkFlowEntityMobileCriteria criteria) {
		 
		return null;
	}


	@Override
	public WorkFlowEntityCategory getCategoryByCategoryCode(String entityCategory) {
		return this.initConstantsService.getEntityCategory(entityCategory);  
	}


	@Override
	public String genAndIdIn(HWorkFlowEntityPageCriteria criteria) {
		if (CommonUtil.isNull(criteria.getStage())) {
			return "";
		}
		WorkFlowEntityCategory cate = this.getCategoryByCriteriaName(criteria.getClass().getName());
		EntityTableAlias alias = criteria.getClass().getAnnotation(EntityTableAlias.class);
		if ( alias == null || CommonUtil.isNull(alias.alias())) {
			throw new ValidateException(criteria.getClass()+" 未定义别名注解 EntityTableAlias ");
		}
		String cause =  " and "+ alias.alias()+".id in " +this.getEntityIdQueryTable(criteria.getStage(), cate.getCategoryCode());
		if (!CommonUtil.isNullId(criteria.getWfEntityId())) {
			cause = cause + " and "+alias.alias()+".id = "+criteria.getWfEntityId();
		}
		return cause;
	}
	
	private WorkFlowEntityCategory getCategoryByCriteriaName(String criteriaName ) {
		for (WorkFlowEntityCategory cate : this.initConstantsService.getEntityCategoryList()) {
			if ( criteriaName.equals(cate.getCriteriaClass().getName()) ) {
				return cate;
			}
		}
		throw new ValidateException("未定义 criteriaName " + criteriaName);
	}

 

	@Override
	public ListPageDefine getWorkFlowEntityListDefineByCategoryCode(String entityCategory) {
		WorkFlowEntityCategory category = this.initConstantsService.getEntityCategory(entityCategory);
		WorkFlowEntityService service =   this.applicationContext.getBean(
				category.getServiceName(), WorkFlowEntityService.class);
        Class<HWorkFlowEntityPageCriteria> criteriaClass = null;
 	 
		criteriaClass = (Class<HWorkFlowEntityPageCriteria>) category.getCriteriaClass();
 
 		List<PageTag> ptList = YqBeanUtil.genPageTagList(criteriaClass);
 		ListPageDefine wfeListDef = new ListPageDefine();
 		wfeListDef.setPageTagList(ptList);
 		wfeListDef.setColumnList(service.getPageColList());
 		return wfeListDef;
	}


	@Override
	public Page<WorkFlowEntityIntf> findWorkFlowEntityPage(String stage, String entityCategory, String criteriaStr) throws  Exception {
		WorkFlowEntityCategory category = this.initConstantsService.getEntityCategory(entityCategory);
		WorkFlowEntityService service =   this.applicationContext.getBean(
				category.getServiceName(), WorkFlowEntityService.class);
		// 待我审批（我的任务）
		Class<HWorkFlowEntityPageCriteria> criteriaClass = null;
 		criteriaClass = (Class<HWorkFlowEntityPageCriteria>)  category.getCriteriaClass() ;
		HWorkFlowEntityPageCriteria criteria = YqJsonUtil.readJson2Entity(criteriaStr, criteriaClass);
		criteria.setStage(stage);
		service.findEntityPage(criteria);
		Page<WorkFlowEntityIntf> page = service.findEntityPage(criteria);
		return page;
	}
	
 
 
 
}
