


 package com.yq.xcode.common.service;

import java.io.File;
import java.util.List;

import org.springframework.data.domain.Page;

import com.yq.xcode.common.bean.CellProperty;
import com.yq.xcode.common.bean.IdAndVersion;
import com.yq.xcode.common.criteria.WorkFlowMutiOperationCriteria;
import com.yq.xcode.common.model.LookupCode;
import com.yq.xcode.common.model.WorkFlowMutiOperation;
import com.yq.xcode.common.springdata.HPageRequest;


public interface WorkFlowMutiOperationService {
	

	public WorkFlowMutiOperation getWorkFlowMutiOperationById(Long id); 
	
	/**
	 * 新建初始化模型数据， 页面ID打开和新建就是一样的了， 如果ID为空就初始化一个
	 */
	public WorkFlowMutiOperation initWorkFlowMutiOperation();
	
	public WorkFlowMutiOperation saveWorkFlowMutiOperation(WorkFlowMutiOperation workFlowMutiOperation);

	public void deleteWorkFlowMutiOperation(List<IdAndVersion> idvs);
	
	/**
	 * 
	 */
	public void deleteWorkFlowMutiOperationById(Long id, Integer version);
	
	public Page<WorkFlowMutiOperation> findWorkFlowMutiOperations(HPageRequest hpr, WorkFlowMutiOperationCriteria criteria, String categoryCode);	
	
	/**
	 * 导出字段定义
	 */
	public CellProperty[] genWorkFlowMutiOperationExportTemplate();
	
	/**
	 * 导入主数据
	 */
	public void importWorkFlowMutiOperation(File file) throws Exception  ;
	
	/**
	 * 批量操作， 更近 action 名称
	 * @param idv
	 * @param action
	 */
	public void batchAction(List<IdAndVersion> idv, String action);

	public Page<WorkFlowMutiOperation> findWorkFlowDtl(HPageRequest hpr,
			WorkFlowMutiOperationCriteria criteria, String categoryCode);

	public List<WorkFlowMutiOperation> findWorkFlowMutiOperations(
			WorkFlowMutiOperationCriteria criteria, String categoryCode);

	List<LookupCode> findWorkFlowOperations(
			WorkFlowMutiOperationCriteria criteria, String categoryCode);
	
	  
}
