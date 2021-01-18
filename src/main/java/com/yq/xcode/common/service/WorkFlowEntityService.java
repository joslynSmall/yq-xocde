package com.yq.xcode.common.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.yq.xcode.common.bean.HColumn;
import com.yq.xcode.common.bean.ResourceView;
import com.yq.xcode.common.bean.WorkFlowEntityIntf;
import com.yq.xcode.common.model.WorkFlowDetail;
import com.yq.xcode.common.springdata.HWorkFlowEntityPageCriteria;


public interface WorkFlowEntityService<F> {

 
	public WorkFlowEntityIntf saveEntity(WorkFlowEntityIntf workFlowEntity, WorkFlowDetail workFlowDetail);

 
	/**
	 * 审批完成调用的方法, 正常审批完成， 也就是没有下一个操作了
	 * 
	 * @param workFlowEntity
	 * @return
	 */
	public WorkFlowEntityIntf closedEntity(WorkFlowEntityIntf workFlowEntity, WorkFlowDetail workFlowDetail);
	
	public <E extends HWorkFlowEntityPageCriteria>  Page<F> findEntityPage(E criteria);
	
	public Long getWorkFlowIdByEntity(WorkFlowEntityIntf workFlowEntity);

	public List<WorkFlowEntityIntf> findWorkFlowEntityByEntityIds(Long[] entityIds);
	
	public WorkFlowEntityIntf getWorkFlowEntityByEntityId(Long entityId);
	
//  	/**
//	 * 取资源值， 如果有需要资源， 即使没有对应的值， 资源名称也要有
//	 * @param workFlowEntity
//	 * @return
//	 */
//	public List<ResourceView> getResourceViewList(
//			WorkFlowEntityIntf workFlowEntity);
	
	/**
	 * 页面 DataGrid 字段定义
	 * @return
	 */
	public List<HColumn> getPageColList();
    
}
