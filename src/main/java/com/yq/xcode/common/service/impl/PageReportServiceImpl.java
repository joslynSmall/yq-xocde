package com.yq.xcode.common.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.yq.xcode.common.bean.SelectItem;
import com.yq.xcode.common.criteria.ReportCriteria;
import com.yq.xcode.common.exception.ValidateException;
import com.yq.xcode.common.model.ReportColumnDefine;
import com.yq.xcode.common.model.ReportDefine;
import com.yq.xcode.common.service.PageReportService;
import com.yq.xcode.common.service.ReportDefineService;
import com.yq.xcode.common.service.ReportService;
import com.yq.xcode.common.service.SqlToModelService;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.JPAUtils;
import com.yq.xcode.common.utils.YqBeanUtil;

@Service("PageReportService")
public class PageReportServiceImpl extends YqJpaDataAccessObject implements
		PageReportService {

	@Autowired
	private ReportService reportService;
	@Autowired
	private ReportDefineService reportDefineService;

	@Autowired
	private SqlToModelService sqlToModelService;

	
	@Override
	public Page<Map> findReportDatas( 
			ReportCriteria criteria, String reportCode) {
		ReportDefine repDef = this.reportDefineService.getReportDefineByCode(reportCode);
		List<ReportColumnDefine> cols = this.reportDefineService.findReportShowColsByCode(reportCode);
		String selectCols = this.genSelectCols(cols,reportCode,criteria.getGroupPropertyBy());
		String fromTable = repDef.getFromTable();
		if (CommonUtil.isNotNull(criteria.getGroupCol())) { // 是明细查询
			ReportColumnDefine df = this.getGroupBy(cols, criteria.getGroupCol());
			fromTable=fromTable+" and "+df.getColName()+" = '"+JPAUtils.toPar(criteria.getGroupValue())+"' ";
		}
		Map map = this.getVariables(criteria);
		String groupBy = repDef.getGroupBy();
		if (CommonUtil.isNotNull(criteria.getGroupPropertyBy())) {
			if (CommonUtil.isNotNull(groupBy)) {
				throw new ValidateException("已经是分组报表， 不可以再选择分组!");
			}
			ReportColumnDefine gpDef = this.getGroupBy(cols, criteria.getGroupPropertyBy());
			groupBy = gpDef.getColName();			
		}
	 
		Page<Map> pageData = this.sqlToModelService.executeNativeQueryForPage(YqBeanUtil.replaceExpression(map, selectCols)+" "+ YqBeanUtil.replaceExpression(map,fromTable), 
				YqBeanUtil.replaceExpression(map,repDef.getSortBy()), YqBeanUtil.replaceExpression(map,groupBy), criteria, Map.class);
		return pageData;
 
	}
	
	  private ReportColumnDefine getGroupBy(List<ReportColumnDefine> cols, String groupCol) {
     	   for (ReportColumnDefine col : cols) {
     		  if (groupCol.equals(col.getPropertyName())) {
  				  return col;
  			   } 
	     	}
	    	return null;
	    }
	
    private String genSelectCols(List<ReportColumnDefine> cols, String reportCode,String groupCol) {
    	

    	if (CommonUtil.isNull(cols)) {
    		throw new ValidateException("报表 :"+reportCode+", 没有定义报表列!");
    	}
    	String selectCols = "select ";
    	String dot = "  ";
    	String ignoreGroupCol = "";
    	for (ReportColumnDefine reportColumnDefine : cols) {
    		if (CommonUtil.isNotNull(groupCol)) {
    			if (groupCol.equals(reportColumnDefine.getPropertyName())) {
    				ignoreGroupCol = reportColumnDefine.getIgnoreGroupCol();
    			}
    		}
		}
    	if (CommonUtil.isNull(ignoreGroupCol)) {
    		ignoreGroupCol  = "";
    	}
    	List<String> ignoreColList = Arrays.asList(StringUtils.split(ignoreGroupCol,","));
    	
    	for (ReportColumnDefine col : cols) {
    		if (CommonUtil.isNotNull(groupCol)) {
    			if (groupCol.equals(col.getPropertyName())) {
    				selectCols = selectCols+dot+col.getColName()+" "+col.getPropertyName();
    			} else if (col.isAggregateCol() && !ignoreColList.contains(col.getColLable())) {
    				selectCols = selectCols+dot+"sum(ifnull("+col.getColName()+",0)) "+col.getPropertyName();
    			} else {
    				selectCols = selectCols+dot+ " null "+col.getPropertyName();
    			}
    		} else {
    			selectCols = selectCols+dot+col.getColName()+" "+col.getPropertyName();
    		}
    		dot = " , ";
     	}
    	return selectCols;
    }
	
	private Map getVariables(ReportCriteria criteria) {
		Map map = new HashMap();
		map.put("crt", criteria);
		map.put("prs", this);
		return  map;
	}
	
 
	@Override
	public List<ReportColumnDefine> getReportColumnsByCode(String categoryCode) {
		return null;
		//return ReportUtil.genShowColumns(ReportUtil.getReportCategoryByCode(categoryCode).getReportDataView());
	}
	
	@Override
	public ReportCriteria genReportCriteriaByReportCode(String reportCode) {
			ReportCriteria criteria = this.reportService.genReportCriteriaByReportCode(reportCode);
			return criteria;
	}

	@Override
	public List<SelectItem> findReportGraphData(ReportCriteria criteria,
			String reportCode) {
	 
		ReportDefine repDef = this.reportDefineService.getReportDefineByCode(reportCode);
		List<ReportColumnDefine> cols = this.reportDefineService.findReportShowColsByCode(reportCode);
		String selectCols = this.genGraphSelectCols(cols,reportCode,criteria.getGroupCol(),criteria.getSumCol());
		String fromTable = repDef.getFromTable();
		
		Map map = this.getVariables(criteria);
		String groupBy = repDef.getGroupBy();
		if (CommonUtil.isNotNull(criteria.getGroupPropertyBy())) {
			if (CommonUtil.isNotNull(groupBy)) {
				throw new ValidateException("已经是分组报表， 不可以再选择分组!");
			}
			ReportColumnDefine gpDef = this.getGroupBy(cols, criteria.getGroupPropertyBy());
			groupBy = gpDef.getColName();			
		}
		if (CommonUtil.isNotNull(criteria.getGroupCol())) { // 是明细查询
			ReportColumnDefine df = this.getGroupBy(cols, criteria.getGroupCol()); 
			groupBy = df.getColName();
		}
 		List<SelectItem> list = this.sqlToModelService.executeNativeQuery(YqBeanUtil.replaceExpression(map, selectCols)+" "+ YqBeanUtil.replaceExpression(map,fromTable), YqBeanUtil.replaceExpression(map,repDef.getSortBy()), YqBeanUtil.replaceExpression(map,groupBy), criteria, SelectItem.class);
		return list;
	}
	
    private String genGraphSelectCols(List<ReportColumnDefine> cols, String reportCode,String groupCol,String sumCol) {
    	

    	if (CommonUtil.isNull(cols)) {
    		throw new ValidateException("报表 :"+reportCode+", 没有定义报表列!");
    	}
    	String selectCols = "select ";
    	String dot = "  ";
    	int cnt = 0;
    	for (ReportColumnDefine col : cols) {
    		if (groupCol.equals(col.getPropertyName())) {
    			selectCols = selectCols+dot+"ifnull("+col.getColName()+",'无') itemName";
        		dot = " , ";
    		} else if (col.isAggregateCol()) {
    			if (col.getPropertyName().equals(sumCol)) {
    				selectCols = selectCols+dot+"sum(ifnull("+col.getColName()+",0)) itemValue" ;
            		dot = " , ";
            		cnt++;
    			}
    			
    		}    			
     	}
    	if (cnt > 1) {
    		throw new ValidateException("超过一个汇总列， 暂且不支持多汇总列图标!");
    	}
    	return selectCols;
    }
	
 
}
