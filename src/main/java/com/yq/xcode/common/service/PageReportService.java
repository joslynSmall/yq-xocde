package com.yq.xcode.common.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.yq.xcode.common.bean.SelectItem;
import com.yq.xcode.common.criteria.ReportCriteria;
import com.yq.xcode.common.model.ReportColumnDefine;
import com.yq.xcode.common.springdata.HPageRequest;





public interface PageReportService {
	 

	public Page<Map> findReportDatas( ReportCriteria criteria, String reportDefineCode);

	public List<ReportColumnDefine> getReportColumnsByCode(String code);

	public ReportCriteria genReportCriteriaByReportCode(String reportCode);

	public List<SelectItem> findReportGraphData(ReportCriteria rc,
			String reportCode); 

}
