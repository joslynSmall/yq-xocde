


 package com.yq.xcode.common.service;

import java.io.File;
import java.util.List;

import org.springframework.data.domain.Page;

import com.yq.xcode.common.bean.CellProperty;
import com.yq.xcode.common.bean.IdAndVersion;
import com.yq.xcode.common.criteria.WorkFlowSkipPageDefineCriteria;
import com.yq.xcode.common.model.WorkFlowSkipPageDefine;
import com.yq.xcode.common.springdata.HPageRequest;


public interface WorkFlowSkipPageDefineService {
	

	public WorkFlowSkipPageDefine getWorkFlowSkipPageDefineById(Long id); 
	
	/**
	 * 新建初始化模型数据， 页面ID打开和新建就是一样的了， 如果ID为空就初始化一个
	 */
	public WorkFlowSkipPageDefine initWorkFlowSkipPageDefine();
	
	public WorkFlowSkipPageDefine saveWorkFlowSkipPageDefine(WorkFlowSkipPageDefine workFlowSkipPageDefine);

	public void deleteWorkFlowSkipPageDefine(List<IdAndVersion> idvs);
	
	/**
	 * 
	 */
	public void deleteWorkFlowSkipPageDefineById(Long id, Integer version);
	
	public Page<WorkFlowSkipPageDefine> findWorkFlowSkipPageDefines(  WorkFlowSkipPageDefineCriteria criteria);	
	
	/**
	 * 导出字段定义
	 */
	public CellProperty[] genWorkFlowSkipPageDefineExportTemplate();
	
	/**
	 * 导入主数据
	 */
	public void importWorkFlowSkipPageDefine(File file) throws Exception  ;
	
	/**
	 * 批量操作， 更近 action 名称
	 * @param idv
	 * @param action
	 */
	public void batchAction(List<IdAndVersion> idv, String action);

	public List<WorkFlowSkipPageDefine> getWorkFlowSkipPageDefinesList(WorkFlowSkipPageDefineCriteria criteria);
	
	  
}
