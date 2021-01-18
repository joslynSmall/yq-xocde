


 package com.yq.xcode.common.service.impl;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.yq.xcode.common.bean.CellProperty;
import com.yq.xcode.common.bean.IdAndVersion;
import com.yq.xcode.common.criteria.JobLogCriteria;
import com.yq.xcode.common.model.JobLog;
import com.yq.xcode.common.model.JobLogDtl;
import com.yq.xcode.common.service.JobLogService;
import com.yq.xcode.common.service.SqlToModelService;
import com.yq.xcode.common.springdata.HPageRequest;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.ExcelUtil;
import com.yq.xcode.common.utils.JPAUtils;
@Service("JobLogService")
public class JobLogServiceImpl   extends YqJpaDataAccessObject  implements JobLogService {
	@Autowired private SqlToModelService sqlToModelService;
 
	@Override
	public JobLog getJobLogById(Long id) {
		return this.getById(JobLog.class, id);
	}
	
	@Override
	public JobLog initJobLog(){
		 JobLog v = new JobLog();
		 v.setId(0l);
		 return v;
	}

	@Override
	public JobLog saveJobLog(JobLog jobLog) {
		this.validateJobLog (jobLog);
  
		return this.save(jobLog);
	}
	
	private void validateJobLog (JobLog jobLog ) {
		
	}
	
	@Override
	public void deleteJobLog(List<IdAndVersion> idvs) {	
		for (IdAndVersion idv : idvs) {
			
			this.deleteJobLogById(idv.getId(), idv.getVersion());
		}
	}
	
	@Override
	public void deleteJobLogById(Long id, Integer version) {	
				this.delete(JobLog.class, id, version)	;
				
	}
	
	@Override
	public Page<JobLog> findJobLogs(JobLogCriteria criteria) {
		StringBuffer query =new StringBuffer();
		query.append("SELECT ")
		    .append(JPAUtils.genEntityCols(JobLog.class, "a", null))
			.append(" FROM yq_job_log  a ")
			.append(" WHERE 1=1");	 
		return sqlToModelService.executeNativeQueryForPage(query.toString(),
				" a.id desc ", null, criteria, JobLog.class);		
	} 
	/**
	 * 导出字段定义
	 */
	@Override
	public CellProperty[] genJobLogExportTemplate() {
		CellProperty[] cellPropertis = new CellProperty[]{				

				 new CellProperty("开始时间","A","runStartTime",false,null),
				 new CellProperty("结束时间","B","runEndTime",false,null),
				 new CellProperty("状态","C","runStatus",false,null),
				 new CellProperty("描述","D","description",false,null),
				 new CellProperty("exceptionMsg","E","exceptionMsg",false,null)				
		};
		return cellPropertis;
	}
	
	/**
	 * 导入主数据
	 */
	@Override
	public void importJobLog(File file) throws Exception  {
 		CellProperty[] cellPropertis = this.genJobLogExportTemplate();
 		List<JobLog> impList = ExcelUtil.loadExcelDataForJ(file, JobLog.class, cellPropertis,null, 1);
 		if (impList != null) {
 			for (JobLog v : impList ) {
 				this.saveJobLog(v);
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

	@Override
	public void addDtlMsg(Long id, List<String> msgList) {
		if (CommonUtil.isNotNull(msgList)) {
			for (String msg : msgList) {
				JobLogDtl dtl = new  JobLogDtl();
				dtl.setJobLogId(id);
				dtl.setDescription(msg);
				this.save(dtl);
			}
		} 
	}
	
	@Override
	public List<JobLogDtl> findJobLogDtls(Long jobLogId) {
		StringBuffer query =new StringBuffer();
		query.append("SELECT ")
		    .append(JPAUtils.genEntityCols(JobLogDtl.class, "a", null))
			.append(" FROM yq_job_log_Dtl  a ")
			.append(" left join yq_job_log jl on jl.id=a.job_log_Id")
			.append(" WHERE 1=1 and a.job_log_Id="+jobLogId);	
		return this.sqlToModelService.executeNativeQuery(query.toString(), null, JobLogDtl.class);
	} 
}

