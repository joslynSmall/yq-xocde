package com.yq.xcode.common.criteria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yq.xcode.common.bean.AggregateCol;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.ReportUtil; 

@JsonIgnoreProperties(ignoreUnknown = true)
public class NativeCriteria implements Serializable  {

	/**
	 * 导出数据， 这样不会要分页， 查询是不加分页数据， 查全部的数据
	 */
	private boolean exportData = false;
	private static final long serialVersionUID = 3063503064840227866L;
	
	/**
	 * 分组字段
	 */
	private String groupPropertyBy;
	/**
	 * 查询列的定义，自定义查询列
	 */
	private List<CriteriaParameter> parameters = new ArrayList<CriteriaParameter>();
	
	private Map<String,String> pMap = new HashMap<String,String>();
	
	private Set<AggregateCol> aggregateCols = new HashSet<AggregateCol>();

	public String genMainQueryCause() {
//		String cause = "";
//		if (!CommonUtil.isNull(this.parameters)) {
//			for (CriteriaParameter pd : this.parameters) {
//				cause = cause +pd.genAndCause();
//			}
//		}
//		List<CriteriaParameter> selfParameters = ReportUtil.genParametersByCriteria(this);
//		if (!CommonUtil.isNull(selfParameters)) {
//			for (CriteriaParameter pd : selfParameters) {
//				cause = cause +pd.genAndCause();
//			}
//		}
//		return cause;
		return this.genCauseByCategory(null);
	}
	
	/**
	 * 剧组函数条件
	 * @return
	 */
	public String genHavingCause() {
 
		String cause = "";
		if (!CommonUtil.isNull(this.parameters)) {
			for (CriteriaParameter pd : this.parameters) {
				cause = cause +pd.genAndHavingCause();
			}
		}
		List<CriteriaParameter> selfParameters = ReportUtil.genParametersByCriteria(this);
		if (!CommonUtil.isNull(selfParameters)) {
			for (CriteriaParameter pd : selfParameters) {
				cause = cause +pd.genAndHavingCause();
			}
		}
		if (CommonUtil.isNotNull(cause)) {
			return cause.substring(5); // 去掉第一个and 
		}
		return cause;
 
	}
	

	public void addAggregateCol(AggregateCol aggregateCol) {
		this.aggregateCols.add(aggregateCol);
	}
	public Set<AggregateCol> getAggregateCols() {
		return aggregateCols;
	}
	public void setAggregateCols(Set<AggregateCol> aggregateCols) {
		this.aggregateCols = aggregateCols;
	}
	public List<CriteriaParameter> getParameters() {
		return parameters;
	}
	public void setParameters(List<CriteriaParameter> parameters) {
		this.parameters = parameters;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	// 有between 表的问题，已后处理
	public void setParameterValueByPropertyName(String properyName, String parameterValue) {
		if (CommonUtil.isNotNull(this.getParameters())) {
			for (CriteriaParameter p : this.getParameters()) {
				if (properyName.equals(p.getPropertyName())) {
					p.setParameterValue(parameterValue);
					return;
				}
			}
		}
	}

	public String getGroupPropertyBy() {
		return groupPropertyBy;
	}

	public void setGroupPropertyBy(String groupPropertyBy) {
		this.groupPropertyBy = groupPropertyBy;
	}
 
	public String getParaValue(String paraName) {
		if (CommonUtil.isNull(this.parameters)) {
			return null;
		}
		for (CriteriaParameter p : this.parameters) {
			if (paraName.equals(p.getPropertyName())) {
				return p.getParameterValue();
			}
		}
		return null;
	}
	
	public String genAndCause(String colName, String paraName) {
		if (CommonUtil.isNull(this.parameters)) {
			return " ";
		}
		for (CriteriaParameter p : this.parameters) {
			if (paraName.equals(p.getPropertyName())) {
				try {
					CriteriaParameter clonePar = (CriteriaParameter) BeanUtils.cloneBean(p);
					clonePar.setColName(colName);
					return clonePar.genAndCause();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return " ";
	}
	
	public String genCauseByCategory(String category) {
		String cause = "";
		if (!CommonUtil.isNull(this.parameters)) {
			for (CriteriaParameter pd : this.parameters) {
				if (this.objEquals(category, pd.getCategory())) {
					cause = cause +pd.genAndCause();
				}
				
			}
		}
		List<CriteriaParameter> selfParameters = ReportUtil.genParametersByCriteria(this);
		if (!CommonUtil.isNull(selfParameters)) {
			for (CriteriaParameter pd : selfParameters) {
				if (this.objEquals(category, pd.getCategory())) {
					cause = cause +pd.genAndCause();
				}
			}
		}
		return cause;
	}
	
	private boolean objEquals(String o1, String o2  ) {
		if (CommonUtil.isNull(o1) && CommonUtil.isNull(o2) ) {
			return true;
		}
		if (CommonUtil.isNull(o1) ) {
			return false;
		}
		if (o1.equals(o2)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isExportData() {
		return exportData;
	}

	public void setExportData(boolean exportData) {
		this.exportData = exportData;
	}

	public Map<String, String> getpMap() {
		return pMap;
	}

	public void setpMap(Map<String, String> pMap) {
		this.pMap = pMap;
	}
	
	
	
	
 
}
