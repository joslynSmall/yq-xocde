package com.yq.xcode.common.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.yq.xcode.common.bean.ListPageDefine;
import com.yq.xcode.common.bean.WorkFlowEntityCategory;
import com.yq.xcode.common.bean.WorkFlowEntityCategoryPage;
import com.yq.xcode.common.bean.WorkFlowEntityIntf;
import com.yq.xcode.common.bean.WorkFlowEntityView;
import com.yq.xcode.common.criteria.WorkFlowEntityMobileCriteria;
import com.yq.xcode.common.springdata.HWorkFlowEntityPageCriteria;


public interface WorkFlowEntityQueryService {
 
 

	/**
	 * 审批类型列表 包括了数量
	 * @param action
	 * @return
	 */
	public List<WorkFlowEntityCategoryPage> findWorkFlowCategoryPageList(String stage);
	
 

	/**
	 * entityId query table 
	 * @return
	 */
	public String getEntityIdQueryTable(String stage, String categoryCode );


	/**
	 * 单据明细
	 * @return
	 */
	public Page<WorkFlowEntityView> findWorkFlowEntityPageForMobile(WorkFlowEntityMobileCriteria criteria);

 
 
	

	public WorkFlowEntityCategory getCategoryByCategoryCode(String entityCategory);



	
	public String genAndIdIn(HWorkFlowEntityPageCriteria criteria);
	
	
	public ListPageDefine getWorkFlowEntityListDefineByCategoryCode(String entityCategory);


	/**
	 * 参数转换为criteria 串
	 * @param stage
	 * @param entityCategory
	 * @param criteriaStr
	 * @return
	 */

	public Page<WorkFlowEntityIntf> findWorkFlowEntityPage(String stage, String entityCategory, String criteriaStr)  throws  Exception;

 


 
	 
}
