


 package com.yq.xcode.common.service;

import java.io.File;
import java.util.List;

import org.springframework.data.domain.Page;

import com.yq.xcode.common.bean.CellProperty;
import com.yq.xcode.common.bean.IdAndVersion;
import com.yq.xcode.common.bean.SelectItem;
import com.yq.xcode.common.criteria.CriteriaParameter;
import com.yq.xcode.common.criteria.ReportDefineCriteria;
import com.yq.xcode.common.model.ReportColumnDefine;
import com.yq.xcode.common.model.ReportDefine;
import com.yq.xcode.common.springdata.HPageRequest;


public interface ReportDefineService {
	

	public ReportDefine getReportDefineById(Long id);
	
	/**
	 * 新建初始化模型数据， 页面ID打开和新建就是一样的了， 如果ID为空就初始化一个
	 */
	public ReportDefine initReportDefine();
	
	public ReportDefine saveReportDefine(ReportDefine reportDefine);

	public void deleteReportDefine(List<IdAndVersion> idvs);
	
	/**
	 * 
	 */
	public void deleteReportDefineById(Long id, Integer version);
	
	public Page<ReportDefine> findReportDefines( ReportDefineCriteria criteria);	
	
	/**
	 * 导出字段定义
	 */
	public CellProperty[] genReportDefineExportTemplate();
	
	/**
	 * 导入主数据
	 */
	public void importReportDefine(File file) throws Exception  ;
	
	/**
	 * 批量操作， 更近 action 名称
	 * @param idv
	 * @param action
	 */
	public void batchAction(List<IdAndVersion> idv, String action);
	
	    /**
     * 主表取明细数据
     */
	public List<ReportColumnDefine> findReportColumnDefinesByPId(Long reportDefineId );
	
    public ReportColumnDefine getReportColumnDefineById(Long id);
	
	/**
	 * 新建初始化模型数据， 页面ID打开和新建就是一样的了， 如果ID为空就初始化一个
	 */
	public ReportColumnDefine initReportColumnDefine(Long reportDefineid);  
	
	public ReportColumnDefine saveReportColumnDefine(ReportColumnDefine reportColumnDefine);

	public void deleteReportColumnDefine(List<IdAndVersion> idvs);
	
	public void deleteReportColumnDefineById(Long id, Integer version);
 
	
	/**
	 * 导出字段定义
	 */
	public CellProperty[] genReportColumnDefineExportTemplate();
	
	/**
	 * 导入主数据
	 */
	public void importReportColumnDefine(File file, Long reportDefineId ) throws Exception  ;
	
	/**
	 * 批量操作， 更近 action 名称
	 * @param idv
	 * @param action
	 */
	public void reportColumnDefinebatchAction(List<IdAndVersion> idv, String action);
	public ReportDefine getReportDefineByCode(String defineCode );
	 
	public List<ReportColumnDefine> findReportShowColsByCode(String defineCode);
	
	public List<CriteriaParameter> findReportParametersByCode(String defineCode);
	
	public String genCriteriaHtml(String defineCode);

	/**
	 * 查找分组列表
	 * @param reportCode
	 * @return
	 */
	public List<SelectItem> findReportGroup(String reportCode);

	public List<SelectItem> findReportSumCol(String reportCode);
	  
}
