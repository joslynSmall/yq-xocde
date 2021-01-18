


 package com.yq.xcode.common.service.impl;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.yq.xcode.common.bean.CellProperty;
import com.yq.xcode.common.bean.IdAndVersion;
import com.yq.xcode.common.criteria.WorkFlowSkipPageDefineCriteria;
import com.yq.xcode.common.model.WorkFlowSkipPageDefine;
import com.yq.xcode.common.service.SqlToModelService;
import com.yq.xcode.common.service.WorkFlowSkipPageDefineService;
import com.yq.xcode.common.service.YqSequenceService;
import com.yq.xcode.common.springdata.HPageRequest;
import com.yq.xcode.common.springdata.PageQuery;
import com.yq.xcode.common.utils.ExcelUtil;
import com.yq.xcode.common.utils.JPAUtils;
@Service("WorkFlowSkipPageDefineService")
public class WorkFlowSkipPageDefineServiceImpl   extends YqJpaDataAccessObject  implements WorkFlowSkipPageDefineService {
	@Autowired private SqlToModelService sqlToModelService; 
	@Autowired private YqSequenceService yqSequenceService; 
	@Override
	public WorkFlowSkipPageDefine getWorkFlowSkipPageDefineById(Long id) {
		return this.getById(WorkFlowSkipPageDefine.class, id);
	}
	
	@Override
	public WorkFlowSkipPageDefine initWorkFlowSkipPageDefine(){
		 WorkFlowSkipPageDefine v = new WorkFlowSkipPageDefine();
		 v.setId(0l);
		 return v;
	}

	@Override
	public WorkFlowSkipPageDefine saveWorkFlowSkipPageDefine(WorkFlowSkipPageDefine workFlowSkipPageDefine) {
		this.validateWorkFlowSkipPageDefine (workFlowSkipPageDefine);
  
		return this.save(workFlowSkipPageDefine);
	}
	
	private void validateWorkFlowSkipPageDefine (WorkFlowSkipPageDefine workFlowSkipPageDefine ) {
		
	}
	
	@Override
	public void deleteWorkFlowSkipPageDefine(List<IdAndVersion> idvs) {	
		for (IdAndVersion idv : idvs) {
			
			this.deleteWorkFlowSkipPageDefineById(idv.getId(), idv.getVersion());
		}
	}
	
	@Override
	public void deleteWorkFlowSkipPageDefineById(Long id, Integer version) {	
				this.delete(WorkFlowSkipPageDefine.class, id, version)	;
				
	}
	
	@Override
	public Page<WorkFlowSkipPageDefine> findWorkFlowSkipPageDefines( WorkFlowSkipPageDefineCriteria criteria) {
		StringBuffer query =new StringBuffer();
		query.append("SELECT ")
		    .append(JPAUtils.genEntityCols(WorkFlowSkipPageDefine.class, "a", null))
			.append(" FROM yq_work_flow_SKIP_PAGE_DEFINE  a ")
			.append(" WHERE 1=1");	 
		return sqlToModelService.executeNativeQueryForPage(query.toString(),
				null, null, criteria, WorkFlowSkipPageDefine.class);		
	} 
	
	@Override
	public List<WorkFlowSkipPageDefine> getWorkFlowSkipPageDefinesList(WorkFlowSkipPageDefineCriteria criteria) {
		StringBuffer query =new StringBuffer();
		query.append("SELECT ")
		.append(JPAUtils.genEntityCols(WorkFlowSkipPageDefine.class, "a", null))
		.append(" FROM yq_work_flow_SKIP_PAGE_DEFINE  a ")
		.append(" WHERE 1=1");	
		return sqlToModelService.executeNativeQuery(query.toString(), " a.id desc ", "", criteria,  WorkFlowSkipPageDefine.class);
	} 
	/**
	 * 导出字段定义
	 */
	@Override
	public CellProperty[] genWorkFlowSkipPageDefineExportTemplate() {
		CellProperty[] cellPropertis = new CellProperty[]{				
				
		};
		return cellPropertis;
	}
	
	/**
	 * 导入主数据
	 */
	@Override
	public void importWorkFlowSkipPageDefine(File file) throws Exception  {
 		CellProperty[] cellPropertis = this.genWorkFlowSkipPageDefineExportTemplate();
 		List<WorkFlowSkipPageDefine> impList = ExcelUtil.loadExcelDataForJ(file, WorkFlowSkipPageDefine.class, cellPropertis,null, 1);
 		if (impList != null) {
 			for (WorkFlowSkipPageDefine v : impList ) {
 				this.saveWorkFlowSkipPageDefine(v);
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

