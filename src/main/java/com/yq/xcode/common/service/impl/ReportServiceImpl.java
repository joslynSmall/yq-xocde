package com.yq.xcode.common.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.yq.xcode.common.bean.CodeGenerator;
import com.yq.xcode.common.bean.ReportExecuteCriteria;
import com.yq.xcode.common.bean.SelectItem;
import com.yq.xcode.common.criteria.CriteriaParameter;
import com.yq.xcode.common.criteria.ReportCriteria;
import com.yq.xcode.common.exception.ValidateException;
import com.yq.xcode.common.model.Report;
import com.yq.xcode.common.model.ReportColumnDefine;
import com.yq.xcode.common.model.ReportExecuteLog;
import com.yq.xcode.common.service.InitConstantsService;
import com.yq.xcode.common.service.ReportDefineService;
import com.yq.xcode.common.service.ReportService;
import com.yq.xcode.common.service.SqlToModelService;
import com.yq.xcode.common.service.YqSequenceService;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.DateUtil;
import com.yq.xcode.common.utils.YqBeanUtil; 

@Service("ReportService")
public class ReportServiceImpl extends YqJpaDataAccessObject implements
		ReportService {
	@Value("${reportDirectory}")
	private String reportDirectory;

	@Autowired
	private SqlToModelService sqlToModelService;
	@Autowired
	private YqSequenceService yqSequenceService;
	@Autowired
	private ReportDefineService reportDefineService;
	
	@Autowired
	private InitConstantsService initConstantsService;

	@Override
	public ReportExecuteLog createReportExecuteLogByCriteria(
			ReportCriteria criteria) {
		ReportExecuteLog log = new ReportExecuteLog();
		log.setExecuteStatus(ReportExecuteLog.EXECUTE_STATUS_PENDING);
		// log.setReportId(criteria.getReportId());
		log = this.create(log);
		ReportExecuteCriteria logCrit = new ReportExecuteCriteria();
		logCrit.setReportExecuteLogId(log.getId());
		List<CriteriaParameter> ps = criteria.getParameters();
		if (ps != null) {
			// 值保持非空的属性
			List<CriteriaParameter> notNullPs = new ArrayList<CriteriaParameter>();
			for (CriteriaParameter p : ps) {
				if (CommonUtil.isNotNull(p.getParameterValue())) {
					notNullPs.add(p);
				}
			}
			criteria.setParameters(notNullPs);
		}
		logCrit.setCriteriaObject(YqBeanUtil.jsonToString(criteria));
		criteria.setParameters(ps);
		logCrit = this.create(logCrit);
		return log;
	}

	/**
	 * run报表， 返回文件名称
	 * 
	 * @param criteria
	 * @return
	 */
	private String genReportByCriteria(ReportCriteria criteria) {
		// if (criteria.getReportId() == null || criteria.getReportId() == 0l )
		// {
		// throw new ValidateException("必须选择运行的报表");
		// }
		// Report report = this.getReportById(criteria.getReportId());
		// ReportCategory rCategory =
		// this.getReportCategoryByCode(report.getReportCategoryCode());
		// ReportQuery reportQuery =
		// ReportUtil.genReportQuery(rCategory.getReportDataView());
		// String queryStr =
		// " select "+ReportUtil.genQueryCols(rCategory.getReportDataView())+" "+reportQuery.fromTable();
		// List<BaseReport> reportData =
		// this.sqlToModelService.executeNativeQuery(queryStr,
		// reportQuery.sortBy(), reportQuery.groupBy(), criteria,
		// rCategory.getReportDataView());
		// String fileName = this.genFileName();
		// String fullName = this.reportDirectory+"/"+fileName+".xls";
		// String sheetName = report.getReportName();
		// if (sheetName.length() > 20) {
		// sheetName = sheetName.substring(0,20);
		// }
		// List<CriteriaParameter> parameters =
		// ReportUtil.genParametersByCriteria(criteria);
		// if (parameters == null) {
		// parameters = new ArrayList<CriteriaParameter>();
		// }
		// List<CriteriaParameter> genPars = criteria.getParameters();
		// if (CommonUtil.isNotNull(genPars)) {
		// parameters.addAll(genPars);
		// }
		// ReportUtil.genReportFile(reportData,this.genShowColumn(report,
		// ReportUtil.genAllColumns(rCategory.getReportDataView())), fullName ,
		// sheetName, parameters);
		return "fileName+.xls";
	}

	private List<ReportColumnDefine> genShowColumn(Report report,
			List<ReportColumnDefine> allCols) {
		List<ReportColumnDefine> list = new ArrayList<ReportColumnDefine>();
		String hiddenCols = "," + report.getHiddenColumns() + ",";
		for (ReportColumnDefine define : allCols) {
			// if (define.isShow() &&
			// !hiddenCols.contains(","+define.getPropertyName()+",")) {
			// list.add(define);
			// }
		}
		return list;
	}

	private String genFileName() {
		CodeGenerator codeGenerator = new CodeGenerator();
		codeGenerator.setSequenceId("REPORTFILENAME"
				+ DateUtil.convertDate2String(DateUtil.getCurrentDate(),
						"yyyyMM"));
		codeGenerator.setPrefix("REP"
				+ DateUtil.convertDate2String(DateUtil.getCurrentDate(),
						"yyyyMM"));
		String fileName = this.yqSequenceService.nextTextSequenceNumber(codeGenerator.getPrefix(), null, 5, codeGenerator.getSequenceId());
		return fileName + CommonUtil.buildRandomKey(10);
	}

	@Override
	public void genReportByReportExecuteLogId(Long reportExecuteLogId) {
//		ReportExecuteLog reportExecuteLog = this
//				.getReportExecuteLogById(reportExecuteLogId);
//		ReportCriteria criteria = this
//				.genReportCriteriaByLogId(reportExecuteLogId);
//		String fileName = this.genReportByCriteria(criteria);
//		reportExecuteLog
//				.setExecuteStatus(reportExecuteLog.EXECUTE_STATUS_COMPLETED);
//		reportExecuteLog.setCompletedTime(DateUtil.getCurrentDate());
//		reportExecuteLog.setResultFile(fileName);
//		this.update(reportExecuteLog);
	}

	@Override
	public void startRunReport(Long reportExecuteLogId) {
		ReportExecuteLog reportExecuteLog = this
				.getReportExecuteLogById(reportExecuteLogId);
		if (!ReportExecuteLog.EXECUTE_STATUS_PENDING.equals(reportExecuteLog
				.getExecuteStatus())) {
			throw new ValidateException("已执行，无需重新运行!");
		}
		reportExecuteLog.setStartTime(DateUtil.getCurrentDate());
		reportExecuteLog
				.setExecuteStatus(reportExecuteLog.EXECUTE_STATUS_DOING);
		this.update(reportExecuteLog);
	}

	@Override
	public void runReportError(Long reportExecuteLogId, Exception e) {
		ReportExecuteLog reportExecuteLog = this
				.getReportExecuteLogById(reportExecuteLogId);
		String errorFile = this.genFileName() + "err.log";
		File filename = new File(this.reportDirectory + "/" + errorFile);
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(filename), "UTF-8"));
			writer.write(ExceptionUtils.getStackTrace(e));   
			writer.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}
		}
		reportExecuteLog.setResultFile(errorFile);
		reportExecuteLog
				.setExecuteStatus(reportExecuteLog.EXECUTE_STATUS_ERROR);
		this.update(reportExecuteLog);
	}

	public ReportExecuteLog getReportExecuteLogById(Long reportExecuteLogId) {
		return this.getById(ReportExecuteLog.class, reportExecuteLogId);
	}

 
	 

	@Override
	public ReportCriteria genReportCriteriaByReportId(Long reportId) {
		Report report = this.getReportById(reportId);
		ReportCriteria criteria = this
				.genReportCriteriaByReportCode(report
						.getReportCategoryCode());
		// criteria.setReportId(reportId);
		return criteria;
	}

	@Override
	public ReportCriteria genReportCriteriaByReportCode(
			String reportCode) {
		List<CriteriaParameter> pList = this.reportDefineService.findReportParametersByCode(reportCode);
		ReportCriteria reportCriteria = new ReportCriteria();
 		reportCriteria.setParameters(pList);
 		List<SelectItem> sumList = this.reportDefineService.findReportSumCol(reportCode);
 		if (CommonUtil.isNotNull(sumList)) {
 			reportCriteria.setSumCol(sumList.get(0).getItemKey());
 		}
		return reportCriteria; 
	}
 

	private Report getReportById(Long reportId) {
		return this.getById(Report.class, reportId);
	}

 

 
 
}
