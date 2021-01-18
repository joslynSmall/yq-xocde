


 package com.yq.xcode.common.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.yq.xcode.common.bean.CellProperty;
import com.yq.xcode.common.bean.IdAndVersion;
import com.yq.xcode.common.bean.PropertyDefine;
import com.yq.xcode.common.bean.SelectItem;
import com.yq.xcode.common.criteria.CriteriaParameter;
import com.yq.xcode.common.criteria.ReportDefineCriteria;
import com.yq.xcode.common.model.ReportColumnDefine;
import com.yq.xcode.common.model.ReportDefine;
import com.yq.xcode.common.service.ReportDefineService;
import com.yq.xcode.common.service.SelectItemService;
import com.yq.xcode.common.service.SqlToModelService;
import com.yq.xcode.common.service.YqSequenceService;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.ExcelUtil;
import com.yq.xcode.common.utils.JPAUtils;
import com.yq.xcode.common.utils.ReportUtil;
import com.yq.xcode.common.utils.YqBeanUtil;
@Service("ReportDefineService")
public class ReportDefineServiceImpl   extends YqJpaDataAccessObject  implements ReportDefineService {
	@Autowired private SqlToModelService sqlToModelService;
	@Autowired private SelectItemService selectItemService;
	@Autowired private YqSequenceService yqSequenceService; 
	@Override
	public ReportDefine getReportDefineById(Long id) {
		return this.getById(ReportDefine.class, id);
	}
	
	@Override
	public ReportDefine initReportDefine(){
		 ReportDefine v = new ReportDefine();
		 v.setId(0l);
		 return v;
	}

	@Override
	public ReportDefine saveReportDefine(ReportDefine reportDefine) {
		this.validateReportDefine (reportDefine);
  
		return this.save(reportDefine);
	}
	
	private void validateReportDefine (ReportDefine reportDefine ) {
		
	}
	
	@Override
	public void deleteReportDefine(List<IdAndVersion> idvs) {	
		for (IdAndVersion idv : idvs) {
			
			this.deleteReportDefineById(idv.getId(), idv.getVersion());
		}
	}
	
	@Override
	public void deleteReportDefineById(Long id, Integer version) {	
				this.deleteMasterDetailEntity(ReportDefine.class,id,version);
				
	}
	
	@Override
	public Page<ReportDefine> findReportDefines( ReportDefineCriteria criteria) {
		StringBuffer query =new StringBuffer();
		query.append("SELECT ")
		    .append(JPAUtils.genEntityCols(ReportDefine.class, "a", null))
			.append(" FROM YQ_REPORT_DEFINE  a ")
			.append(" WHERE 1=1");	 
		return sqlToModelService.executeNativeQueryForPage(query.toString(),
				null, null, criteria, ReportDefine.class);
	} 
	/**
	 * 导出字段定义
	 */
	@Override
	public CellProperty[] genReportDefineExportTemplate() {
		CellProperty[] cellPropertis = new CellProperty[]{				

				 new CellProperty("报表代码","A","code",true,null),
				 new CellProperty("报表名称","B","name",true,null),
				 new CellProperty("表","C","fromTable",true,null),
				 new CellProperty("报表排序","D","sortBy",false,null),
				 new CellProperty("分组字段","E","groupBy",false,null),
				 new CellProperty("描述","F","description",true,null)				
		};
		return cellPropertis;
	}
	
	/**
	 * 导入主数据
	 */
	@Override
	public void importReportDefine(File file) throws Exception  {
 		CellProperty[] cellPropertis = this.genReportDefineExportTemplate();
 		List<ReportDefine> impList = ExcelUtil.loadExcelDataForJ(file, ReportDefine.class, cellPropertis,null, 1);
 		if (impList != null) {
 			for (ReportDefine v : impList ) {
 				this.saveReportDefine(v);
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
	
		    /**
	     * 主表取明细数据
	     */
	public List<ReportColumnDefine> findReportColumnDefinesByPId(Long reportDefineId ) {
		StringBuffer query =new StringBuffer();
		query.append("SELECT ")
	    .append(JPAUtils.genEntityCols(ReportColumnDefine.class, "a", null))
		.append(" FROM YQ_REPORT_COLUMN_DEFINE  a ")
		.append(" WHERE a.REPORT_DEFINE_ID = "+ reportDefineId)
		.append(" order by a.display_order ");	
		return this.sqlToModelService.executeNativeQuery(query.toString(), null, ReportColumnDefine.class);
	}
	
	@Override
	public ReportColumnDefine getReportColumnDefineById(Long id) {
		return this.getById(ReportColumnDefine.class, id);
	}
	
	@Override
	public ReportColumnDefine initReportColumnDefine(Long reportDefineId){
		ReportColumnDefine reportColumnDefine = new ReportColumnDefine();
		reportColumnDefine.setReportDefineId(reportDefineId);
		return reportColumnDefine;
	}

	@Override
	public ReportColumnDefine saveReportColumnDefine(ReportColumnDefine reportColumnDefine) {
		this.validateReportColumnDefine (reportColumnDefine);
		return this.save(reportColumnDefine);
	}
	
	private void validateReportColumnDefine (ReportColumnDefine reportColumnDefine ) {
		
	}
	
	@Override
	public void deleteReportColumnDefine(List<IdAndVersion> idvs) {	
		for (IdAndVersion idv : idvs) {
			this.deleteReportColumnDefineById(idv.getId(), idv.getVersion());
		}
	}
	
	@Override
	public void deleteReportColumnDefineById(Long id, Integer version) {
		this.delete(ReportColumnDefine.class, id, version)	;
	}
	 
	/**
	 * 导出字段定义
	 */
	@Override
	public CellProperty[] genReportColumnDefineExportTemplate() {
		CellProperty[] cellPropertis = new CellProperty[]{				

				 new CellProperty("显示顺序","A","displayOrder",true,null),
				 new CellProperty("属性名称","B","propertyName",true,null),
				 new CellProperty("数据库列","C","colName",false,null),
				 new CellProperty("报表列","D","showCol",false,null),
				 new CellProperty("报表标题","E","colLable",false,null),
				 new CellProperty("列宽","F","width",false,null),
				 new CellProperty("列合计","G","aggregateCol",false,null),
				 new CellProperty("可分组统计","H","canGroup",false,null),
				 new CellProperty("参数列","I","parameterCol",false,null),
				 new CellProperty("参数标题","J","parameterLable",false,null),
				 new CellProperty("数据类型","K","compareType",false,selectItemService.nameToKeyMap("DATACATEGORY")),
				 new CellProperty("逻辑运算符","L","operator",false,selectItemService.nameToKeyMap("OPERATORTYPE")),
				 new CellProperty("控件","M","tagKey",false,null),
				 new CellProperty("控件数据","N","listCategory",false,selectItemService.nameToKeyMap("SELECTCATEGORY")),
				 new CellProperty("默认值","O","defaultValue",false,null),
				 new CellProperty("特殊查询条件","P","placeHolder",false,null),
				 new CellProperty("聚组条件","Q","havingCause",false,null)				
		};
		return cellPropertis;
	}
	
	/**
	 * 导入主数据
	 */
	@Override
	public void importReportColumnDefine(File file, Long reportDefineId ) throws Exception  {
 		CellProperty[] cellPropertis = this.genReportColumnDefineExportTemplate();
 		List<ReportColumnDefine> impList = ExcelUtil.loadExcelDataForJ(file, ReportColumnDefine.class, cellPropertis,null, 1);
 		if (impList != null) {
 			for (ReportColumnDefine v : impList ) {
 				v.setReportDefineId(reportDefineId);
 				this.saveReportColumnDefine(v);
 			}
 		}
	}
 
	/**
	 * 批量操作， 更近 action 名称
	 * @param idv
	 * @param action
	 */
	public void reportColumnDefinebatchAction(List<IdAndVersion> idv, String action) {
//		if ("forbidden".equals(action)) {
//			this.forbidden(idv,true);
//		} else if ("unforbidden".equals(action)) {
//			this.forbidden(idv,false);
//		}else {
//			throw new ValidateException("不存在的action: "+action);
//		}
	}
	
	@Override
	public ReportDefine getReportDefineByCode(String code) {
		List<ReportDefine> list = this.find(" from ReportDefine d where d.code = ? ",code);
		if (CommonUtil.isNotNull(list)) {
			return list.get(0);
		}
		return null;
	}
	@Override
	public List<ReportColumnDefine> findReportShowColsByCode(String code) {
        String query = "select "+JPAUtils.genEntityCols(ReportColumnDefine.class, "col", null)
        		+" from YQ_REPORT_COLUMN_DEFINE col, YQ_REPORT_DEFINE d "
        		+" where col.report_define_id = d.id "
        		+"   and col.SHOW_COL = 1 "
        		+"   and d.code ='"+code+"' "
        		+" order by col.display_Order ";
        
		return this.sqlToModelService.executeNativeQuery(query, null, ReportColumnDefine.class);
	}
	@Override
	public List<CriteriaParameter> findReportParametersByCode(String code) {

		List<CriteriaParameter> list = new ArrayList<CriteriaParameter>();
        
		List<ReportColumnDefine> colList = findReportParameterDefByCode(code);
		Map variables = this.getVariables();
	    if (CommonUtil.isNotNull(colList)) {
	    	for (ReportColumnDefine col : colList) {
	    		CriteriaParameter p = ReportUtil.toParameter(col); 
	    		p.setParameterValue(YqBeanUtil.replaceExpression(variables, p.getParameterValue()));
	    		list.add(p);
	    	}
	    }
	    return list;
	}
	
	private Map getVariables() { 
		Map map = new HashMap();
		map.put("rptUtl", new ReportUtil());
		return map;
	}

	private List<ReportColumnDefine> findReportParameterDefByCode(String code) {
		String query = "select "+JPAUtils.genEntityCols(ReportColumnDefine.class, "col", null)
        		+" from YQ_REPORT_COLUMN_DEFINE col, YQ_REPORT_DEFINE d "
        		+" where col.report_define_id = d.id "
        		+"   and col.PARAMETER_COL = 1 "
        		+"   and d.code ='"+code+"' "
        		+" order by col.display_Order ";
			List<ReportColumnDefine> colList = this.sqlToModelService.executeNativeQuery(query, null, ReportColumnDefine.class);
			return colList;
	}
	
	
	@Override
	public String genCriteriaHtml(String defineCode) {
		List<ReportColumnDefine> pList = this.findReportParameterDefByCode(defineCode);
		List<PropertyDefine> pdList = new ArrayList<PropertyDefine>();
		if (pList != null) {
			int i=0;
			for (ReportColumnDefine p : pList) {
				PropertyDefine pd = new PropertyDefine();
				pd.setColumn(p.getColName());
				pd.setDataType(p.getCompareType());
				if (CommonUtil.isNull(p.getParameterLable())) {
					pd.setLable(  p.getColLable() );
				} else {
					pd.setLable( p.getParameterLable() );
				}
				
				pd.setListCategory(p.getListCategory());
				pd.setOperator(p.getOperator());
				//pd.setPlaceHolder(p.getPlaceHolder());
				pd.setProperty("parameters["+i+"].parameterValue");
				pd.setTagKey(p.getTagKey());
				pd.setValue(p.getDefaultValue());
				pd.setWidth("200px"); 
				if (p.getParameterMandatory() != null) { 
					pd.setMandatory(p.getParameterMandatory());
				}
				pdList.add(pd);
				i++;
			}
		} 
		//String html = cTable.genTrHtml();
    	return null;
	}
	@Override
	public List<SelectItem> findReportGroup(String reportCode) {
		String query = "select col.PROPERTY_NAME itemKey,"
				+ " col.col_Lable itemName "
        		+" from YQ_REPORT_COLUMN_DEFINE col, YQ_REPORT_DEFINE d "
        		+" where col.report_define_id = d.id " 
        		+"   and d.code ='"+reportCode+"' "
        		+"   and col.can_group = 1 "
        		+" order by col.display_Order ";
		List<SelectItem> list = this.sqlToModelService.executeNativeQuery(query, null, SelectItem.class);
			return list;
	}
	
	@Override
	public List<SelectItem> findReportSumCol(String reportCode) {
		String query = "select col.PROPERTY_NAME itemKey,"
				+ " col.col_Lable itemName "
        		+" from YQ_REPORT_COLUMN_DEFINE col, YQ_REPORT_DEFINE d "
        		+" where col.report_define_id = d.id " 
        		+"   and d.code ='"+reportCode+"' "
        		+"   and col.aggregate_Col = 1 "
        		+" order by col.display_Order ";
		List<SelectItem> list = this.sqlToModelService.executeNativeQuery(query, null, SelectItem.class);
			return list;
	}
	
	

	
		
  		
}

