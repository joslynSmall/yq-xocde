


 package com.yq.xcode.common.service;

import java.io.File;
import java.util.List;

import org.springframework.data.domain.Page;

import com.yq.xcode.common.bean.CellProperty;
import com.yq.xcode.common.bean.IdAndVersion;
import com.yq.xcode.common.criteria.JobLogCriteria;
import com.yq.xcode.common.model.JobLog;
import com.yq.xcode.common.model.JobLogDtl;
import com.yq.xcode.common.springdata.HPageRequest;


public interface JobLogService {
	

	public JobLog getJobLogById(Long id); 
	
	/**
	 * 新建初始化模型数据， 页面ID打开和新建就是一样的了， 如果ID为空就初始化一个
	 */
	public JobLog initJobLog();
	
	public JobLog saveJobLog(JobLog jobLog);

	public void deleteJobLog(List<IdAndVersion> idvs);
	
	/**
	 * 
	 */
	public void deleteJobLogById(Long id, Integer version);
	
	public Page<JobLog> findJobLogs( JobLogCriteria criteria);	
	
	/**
	 * 导出字段定义
	 */
	public CellProperty[] genJobLogExportTemplate();
	
	/**
	 * 导入主数据
	 */
	public void importJobLog(File file) throws Exception  ;
	
	/**
	 * 批量操作， 更近 action 名称
	 * @param idv
	 * @param action
	 */
	public void batchAction(List<IdAndVersion> idv, String action);

	public List<JobLogDtl> findJobLogDtls(Long jobLogId);

	public void addDtlMsg(Long id, List<String> msgList);
	
	  
}
