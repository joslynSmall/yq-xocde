package com.yq.xcode.common.service.impl;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.yq.xcode.common.bean.CellProperty;
import com.yq.xcode.common.bean.IdAndVersion;
import com.yq.xcode.common.criteria.WorkFlowMutiOperationCriteria;
import com.yq.xcode.common.model.LookupCode;
import com.yq.xcode.common.model.WorkFlow;
import com.yq.xcode.common.model.WorkFlowDetail;
import com.yq.xcode.common.model.WorkFlowMutiOperation;
import com.yq.xcode.common.service.LookupCodeService;
import com.yq.xcode.common.service.SqlToModelService;
import com.yq.xcode.common.service.WorkFlowMutiOperationService;
import com.yq.xcode.common.service.YqSequenceService;
import com.yq.xcode.common.springdata.HPageRequest;
import com.yq.xcode.common.springdata.PageQuery;
import com.yq.xcode.common.utils.ExcelUtil;
import com.yq.xcode.common.utils.JPAUtils;
@Service("WorkFlowMutiOperationService")
public class WorkFlowMutiOperationServiceImpl   extends YqJpaDataAccessObject  implements WorkFlowMutiOperationService {
	@Autowired private SqlToModelService sqlToModelService; 
	@Autowired private YqSequenceService yqSequenceService; 
	@Autowired private LookupCodeService lookupCodeService; 
	@Override
	public WorkFlowMutiOperation getWorkFlowMutiOperationById(Long id) {
		return this.getById(WorkFlowMutiOperation.class, id);
	}
	
	@Override
	public WorkFlowMutiOperation initWorkFlowMutiOperation(){
		 WorkFlowMutiOperation v = new WorkFlowMutiOperation();
		 v.setId(0l);
		 return v;
	}

	@Override
	public WorkFlowMutiOperation saveWorkFlowMutiOperation(WorkFlowMutiOperation workFlowMutiOperation) {
		this.validateWorkFlowMutiOperation (workFlowMutiOperation);
  
		return this.save(workFlowMutiOperation);
	}
	
	private void validateWorkFlowMutiOperation (WorkFlowMutiOperation workFlowMutiOperation ) {
		
	}
	
	@Override
	public void deleteWorkFlowMutiOperation(List<IdAndVersion> idvs) {	
		for (IdAndVersion idv : idvs) {
			
			this.deleteWorkFlowMutiOperationById(idv.getId(), idv.getVersion());
		}
	}
	
	@Override
	public void deleteWorkFlowMutiOperationById(Long id, Integer version) {	
				this.delete(WorkFlowMutiOperation.class, id, version)	;
				
	}
	
//	@Override
//	public Page<WorkFlowMutiOperation> findWorkFlowMutiOperations(HPageRequest hpr,WorkFlowMutiOperationCriteria criteria,String categoryCode) {
//		StringBuffer query =new StringBuffer();
//		query.append("SELECT ")
//		.append("  (SELECT lc.lookup_name FROM yq_lookup_code lc WHERE lc.key_code = wfd.CURRENT_Graph_NODE ) 'graphNodeName'  ").append(" , ")
//		.append("  (SELECT lc.lookup_name FROM yq_lookup_code lc WHERE lc.key_code = wfd.CURRENT_STATUS ) 'workFlowDetail.currentStatusName'  ").append(" , ")
//		.append("  (SELECT lc.lookup_name FROM yq_lookup_code lc WHERE lc.key_code = wfd.ROLE ) 'workFlowDetail.roleName'  ").append(" , ")
//		.append("  (SELECT lc.lookup_name FROM yq_lookup_code lc WHERE lc.key_code = wfd.ACTION ) 'workFlowDetail.actionName'  ").append(" , ")
//		    .append(JPAUtils.genEntityCols(WorkFlowMutiOperation.class, "a", null)).append(" , ")
//		    .append(JPAUtils.genEntityCols(WorkFlow.class, "wf", "workFlow")).append(" , ")
//		    .append(JPAUtils.genEntityCols(WorkFlowDetail.class, "wfd", "workFlowDetail"))
//			.append(" FROM pur_WorkFlow_MutiOperation  a ")
//			.append(" inner join  yq_work_flow_DETAIL  wfd on wfd.id = a.workFlowDetail_ID ")
//			.append(" inner join  yq_work_flow  wf on wf.id=wfd.WORK_FLOW_ID ")
//			.append(" WHERE 1=1 and a.categoryCode = '"+categoryCode+"'");	
//		PageQuery pageQuery = new PageQuery(query.toString(), null, null, criteria, hpr);
//		return sqlToModelService.executeNativeQueryForPage(pageQuery , WorkFlowMutiOperation.class);		
//	} 
	@Override
	public Page<WorkFlowMutiOperation> findWorkFlowMutiOperations(HPageRequest hpr,WorkFlowMutiOperationCriteria criteria,String categoryCode) {
		StringBuffer query =new StringBuffer();
		query.append("SELECT ")
		.append("  (SELECT lc.lookup_name FROM yq_lookup_code lc WHERE lc.key_code = wfd.CURRENT_Graph_NODE ) 'opName'  ").append(" , ")
		.append(JPAUtils.genEntityCols(WorkFlowMutiOperation.class, "a", null)).append(" , ")
		.append(" FROM pur_WorkFlow_MutiOperation  a ")
		.append(" WHERE 1=1 and a.categoryCode = '"+categoryCode+"'");	 
		return sqlToModelService.executeNativeQueryForPage(query.toString(),
				null, null, criteria, WorkFlowMutiOperation.class);		
	} 
	@Override
	public  List<LookupCode> findWorkFlowOperations(WorkFlowMutiOperationCriteria criteria,String categoryCode) {
		StringBuffer query =new StringBuffer();
		query.append("SELECT * from yq_lookup_code where category_code = 'WAT' and key_code not in (select lookupCode_Key from pur_WorkFlow_MutiOperation where categoryCode = '"+categoryCode+"' ) ");
		return sqlToModelService.executeNativeQuery(query.toString(), null, null, criteria, LookupCode.class);		
//		return lookupCodeService.findLookupCodeByCategory("WAT");		
	} 
	@Override
	public Page<WorkFlowMutiOperation> findWorkFlowDtl(HPageRequest hpr,WorkFlowMutiOperationCriteria criteria,String categoryCode) {
		StringBuffer query =new StringBuffer();
		query.append("SELECT ")
		.append("  (SELECT lc.lookup_name FROM yq_lookup_code lc WHERE lc.key_code = wfd.CURRENT_Graph_NODE ) 'graphNodeName'  ").append(" , ")
		.append("  (SELECT lc.lookup_name FROM yq_lookup_code lc WHERE lc.key_code = wfd.CURRENT_STATUS ) 'workFlowDetail.currentStatusName'  ").append(" , ")
		.append("  (SELECT lc.lookup_name FROM yq_lookup_code lc WHERE lc.key_code = wfd.ROLE ) 'workFlowDetail.roleName'  ").append(" , ")
		.append("  (SELECT lc.lookup_name FROM yq_lookup_code lc WHERE lc.key_code = wfd.ACTION ) 'workFlowDetail.actionName'  ").append(" , ")
		.append(JPAUtils.genEntityCols(WorkFlow.class, "wf", "workFlow")).append(" , ")
		.append(JPAUtils.genEntityCols(WorkFlowDetail.class, "wfd", "workFlowDetail"))
		.append(" FROM yq_work_flow_DETAIL  wfd   ")
		.append(" inner join  yq_work_flow  wf on wf.id=wfd.WORK_FLOW_ID ")
		.append(" WHERE 1=1 and wf.CATEGORY_CODE = '"+categoryCode+"' ")	
		.append(" and wfd.id not in (select wm.workFlowDetail_ID from pur_WorkFlow_MutiOperation wm ) ");	
		PageQuery pageQuery = new PageQuery(query.toString(), null, null, criteria, hpr);
		return sqlToModelService.executeNativeQueryForPage(query.toString(), null, null, criteria, WorkFlowMutiOperation.class);		
	} 
	@Override
	public List<WorkFlowMutiOperation> findWorkFlowMutiOperations(WorkFlowMutiOperationCriteria criteria,String categoryCode) {
		StringBuffer query =new StringBuffer();
		query.append("SELECT ")
		.append("  (SELECT lc.lookup_name FROM yq_lookup_code lc WHERE lc.key_code = a.lookupCode_Key ) 'operationName'  ").append(" , ")
//		.append("  (SELECT lc.lookup_name FROM yq_lookup_code lc WHERE lc.key_code = wfd.CURRENT_Graph_NODE ) 'graphNodeName'  ").append(" , ")
//		.append("  (SELECT lc.lookup_name FROM yq_lookup_code lc WHERE lc.key_code = wfd.CURRENT_STATUS ) 'workFlowDetail.currentStatusName'  ").append(" , ")
//		.append("  (SELECT lc.lookup_name FROM yq_lookup_code lc WHERE lc.key_code = wfd.ROLE ) 'workFlowDetail.roleName'  ").append(" , ")
//		.append("  (SELECT lc.lookup_name FROM yq_lookup_code lc WHERE lc.key_code = wfd.ACTION ) 'workFlowDetail.actionName'  ").append(" , ")
		.append(JPAUtils.genEntityCols(WorkFlowMutiOperation.class, "a", null))//.append(" , ")
//		.append(JPAUtils.genEntityCols(WorkFlow.class, "wf", "workFlow")).append(" , ")
//		.append(JPAUtils.genEntityCols(WorkFlowDetail.class, "wfd", "workFlowDetail"))
		.append(" FROM pur_WorkFlow_MutiOperation  a ")
//		.append(" inner join  yq_work_flow_DETAIL  wfd on wfd.id = a.workFlowDetail_ID ")
//		.append(" inner join  yq_work_flow  wf on wf.id=wfd.WORK_FLOW_ID ")
		.append(" WHERE 1=1 and a.categoryCode = '"+categoryCode+"'");	
//		PageQuery pageQuery = new PageQuery(query.toString(), null, null, criteria, hpr);
		return sqlToModelService.executeNativeQuery(query.toString(), null, null, criteria, WorkFlowMutiOperation.class);		
	} 
	/**
	 * 导出字段定义
	 */
	@Override
	public CellProperty[] genWorkFlowMutiOperationExportTemplate() {
		CellProperty[] cellPropertis = new CellProperty[]{				

				 new CellProperty("编码","A","lookupCodeKey",false,null),
		};
		return cellPropertis;
	}
	
	/**
	 * 导入主数据
	 */
	@Override
	public void importWorkFlowMutiOperation(File file) throws Exception  {
 		CellProperty[] cellPropertis = this.genWorkFlowMutiOperationExportTemplate();
 		List<WorkFlowMutiOperation> impList = ExcelUtil.loadExcelDataForJ(file, WorkFlowMutiOperation.class, cellPropertis, null,1);
 		if (impList != null) {
 			for (WorkFlowMutiOperation v : impList ) {
 				this.saveWorkFlowMutiOperation(v);
 			}
 		}
	}
 
	@Override
	public void batchAction(List<IdAndVersion> idv, String action) {
//		if ("forbidden".equals(action)) {
//			this.forbidden(idv,true);
//		} else if ("unforbidden".equals(action)) {
//			this.forbidden(idv,false);
//		}else {
//			throw new ValidateException("不存在的action: "+action);
//		}
	}
	
			
}

