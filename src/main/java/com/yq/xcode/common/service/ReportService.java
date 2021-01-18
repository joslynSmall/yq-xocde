package com.yq.xcode.common.service;

import com.yq.xcode.common.criteria.ReportCriteria;
import com.yq.xcode.common.model.ReportExecuteLog;





public interface ReportService {
	
	/**
	 * 根据 ReportCriteria 创建report的运行日志, 初始状体为PENDING等待
	 * @param criteria
	 * @return
	 */

	public ReportExecuteLog createReportExecuteLogByCriteria(ReportCriteria criteria );
	
	/**
	 * 产生report , 将reort的名称保存在日志表
	 * @param reportExecuteLogId
	 * @return
	 */
	public void genReportByReportExecuteLogId(Long reportExecuteLogId);
	
	/**
	 * 运行出错时,写出错状态和日志
	 * @param reportExecuteLogId
	 * @param e
	 */

	public void runReportError(Long reportExecuteLogId, Exception e);
    /**
     * 开始运行,写开始运行的时间和状态
     * @param reportExecuteLogId
     */
	public void startRunReport(Long reportExecuteLogId);

	/**
	 * 产生ReportCriteria, 主要包含report的参数列表
	 * @param reportId
	 * @return
	 */
	public ReportCriteria genReportCriteriaByReportId(Long reportId); 

	public ReportCriteria genReportCriteriaByReportCode(String reportCategoryCode);
	
 
 
 
	 
}
