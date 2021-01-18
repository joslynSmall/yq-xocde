


 package com.yq.xcode.common.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yq.xcode.common.bean.CellProperty;
import com.yq.xcode.common.bean.IdAndVersion;
import com.yq.xcode.common.criteria.ParseElementQueryCriteria;
import com.yq.xcode.common.criteria.WxTemplateCriteria;
import com.yq.xcode.common.model.ParseElement;
import com.yq.xcode.common.model.WxTemplate;
import com.yq.xcode.common.model.WxTemplateDataDefine;
import com.yq.xcode.common.service.ParseElementService;
import com.yq.xcode.common.service.SqlToModelService;
import com.yq.xcode.common.service.WxTemplateService;
import com.yq.xcode.common.service.YqSequenceService;
import com.yq.xcode.common.springdata.HPageRequest;
import com.yq.xcode.common.springdata.PageQuery;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.ExcelUtil;
import com.yq.xcode.common.utils.JPAUtils;
import com.yq.xcode.common.utils.YqBeanUtil;
@Service("WxTemplateService")
public class WxTemplateServiceImpl   extends YqJpaDataAccessObject  implements WxTemplateService {
	@Autowired private SqlToModelService sqlToModelService; 
	@Autowired private YqSequenceService yqSequenceService; 
	@Autowired private ParseElementService parseElementMainService;
	@Override
	public WxTemplate getWxTemplateById(Long id) {
		return this.getById(WxTemplate.class, id);
	}
	@Override
	public WxTemplate getWxMsgTemplateById(Long id) {
		WxTemplate msgWxTemplate = this.getById(WxTemplate.class, id);
		msgWxTemplate.getSourceTemplateId();
		WxTemplate sourceWxTemplate = this.getById(WxTemplate.class, msgWxTemplate.getSourceTemplateId());
		msgWxTemplate.setSourceWxTemplate(sourceWxTemplate); 
		return msgWxTemplate;
	}
	
	@Override
	public WxTemplate initWxTemplate(Long isSys){
		 WxTemplate v = new WxTemplate();
		 v.setIsSys(isSys);
		 v.setId(0l);
		 return v;
	}

	@Override
	public WxTemplate saveWxTemplate(WxTemplate wxTemplate) {
		this.validateWxTemplate (wxTemplate);
  
		return this.save(wxTemplate);
	}
	
	private void validateWxTemplate (WxTemplate wxTemplate ) {
		
	}
	@Override
	public WxTemplate saveWxMsgTemplate(WxTemplate wxTemplate) {
		this.validateWxMsgTemplate (wxTemplate);
		List<WxTemplateDataDefine> sourceDetail = null; 
		if(CommonUtil.isNull(wxTemplate.getId())){
			if(CommonUtil.isNotNull(wxTemplate.getSourceTemplateId())){
				sourceDetail = findWxTemplateDataDefinesByPId(wxTemplate.getSourceTemplateId());
			}
		}
		WxTemplate savedWxTemplate= this.save(wxTemplate);
		if(CommonUtil.isNotNull(sourceDetail)){
			initAndSaveWxTemplateDataDefineWithNewWxTemplate(savedWxTemplate , sourceDetail);
		}
		return savedWxTemplate;
	}
	
	private boolean initAndSaveWxTemplateDataDefineWithNewWxTemplate (WxTemplate wxTemplate,List<WxTemplateDataDefine> sourceDetail ) {
		if(CommonUtil.isNotNull(sourceDetail) && CommonUtil.isNotNull(wxTemplate)){
			for(WxTemplateDataDefine wtdd:sourceDetail){
				Long sourceDetailId = wtdd.getId();
				wtdd.setId(null);
				wtdd.setTemplateId(wxTemplate.getId());
				wtdd.setSourceWxTemplateDataDefineId(sourceDetailId);
				wtdd.setIsSys(0l);
				wtdd.setContext("");
			}
			this.save(sourceDetail);
		}else{
			System.out.println("sourceDetail or NewwxTemplate is null ");
			return false;
		}
		return true;
	}
	
	private void validateWxMsgTemplate (WxTemplate wxTemplate ) {
		
	}
	
	@Override
	public void deleteWxTemplate(List<IdAndVersion> idvs) {	
		for (IdAndVersion idv : idvs) {
			
			this.deleteWxTemplateById(idv.getId(), idv.getVersion());
		}
	}
	
	@Override
	public void deleteWxTemplateById(Long id, Integer version) {	
				this.deleteMasterDetailEntity(WxTemplate.class,id,version);
				
	}
	
	@Override
	public Page<WxTemplate> findWxTemplates(HPageRequest hpr,WxTemplateCriteria criteria,String isSys) {
		StringBuffer query =new StringBuffer();
		query.append("SELECT ")
		    .append(JPAUtils.genEntityCols(WxTemplate.class, "a", null))
			.append(" FROM wx_template  a ")
			.append(" WHERE 1=1 ")
			.append(" and a.is_sys = "+isSys);	
		PageQuery pageQuery = new PageQuery(query.toString(), null, null, criteria, hpr);
		return sqlToModelService.executeNativeQueryForPage(query.toString(),
				null, null, criteria, WxTemplate.class);		
	} 
	/**
	 * 导出字段定义
	 */
	@Override
	public CellProperty[] genWxTemplateExportTemplate() {
		CellProperty[] cellPropertis = new CellProperty[]{				

				 new CellProperty("编码","A","code",true,null),
				 new CellProperty("名称","B","name",true,null),
				 new CellProperty("系统模板","C","isSys",true,null),
				 new CellProperty("微信模板ID","D","templateId",true,null),
				 new CellProperty("系统模板ID","E","sourceTemplateId",true,null),
				 new CellProperty("业务实体类型","F","entityCategory",true,null)				
		};
		return cellPropertis;
	}
	
	/**
	 * 导入主数据
	 */
	@Override
	public void importWxTemplate(File file) throws Exception  {
 		CellProperty[] cellPropertis = this.genWxTemplateExportTemplate();
 		List<WxTemplate> impList = ExcelUtil.loadExcelDataForJ(file, WxTemplate.class, cellPropertis, null,1);
 		if (impList != null) {
 			for (WxTemplate v : impList ) {
 				this.saveWxTemplate(v);
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
	public List<WxTemplateDataDefine> findWxTemplateDataDefinesByPId(Long wxTemplateId ) {
		StringBuffer query =new StringBuffer();
		query.append("SELECT b.context sourceContext ,")
	    .append(JPAUtils.genEntityCols(WxTemplateDataDefine.class, "a", null))
		.append(" FROM wx_template_data_define  a ")
		.append(" left join wx_template_data_define  b on a.source_wxtemplatedatadefine_id = b.id ")
		.append(" WHERE a.template_id = "+ wxTemplateId);	
		return this.sqlToModelService.executeNativeQuery(query.toString(), null, WxTemplateDataDefine.class);
	}
	
	@Override
	public WxTemplateDataDefine getWxTemplateDataDefineById(Long id) {
		return this.getById(WxTemplateDataDefine.class, id) ;
	}
	@Override
	public WxTemplateDataDefine getWxMsgTemplateDataDefineById(Long id) {
		WxTemplateDataDefine msgWxTemplateDataDefine =this.getById(WxTemplateDataDefine.class, id);
		getSourceWxTemplateDataDefineForMsgWxTemplateDataDefine(msgWxTemplateDataDefine);
		Map<String,String> map = this.getMapOfFUElementKeyIsEleName();
		if (msgWxTemplateDataDefine != null) {
			msgWxTemplateDataDefine.setContext(YqBeanUtil.replaceToKey(msgWxTemplateDataDefine.getContext(), map));
		}
		return msgWxTemplateDataDefine ;
	}
	
	@Override
	public WxTemplateDataDefine getSourceWxTemplateDataDefineForMsgWxTemplateDataDefine(WxTemplateDataDefine msgWxTemplateDataDefine) {
		if(CommonUtil.isNotNull(msgWxTemplateDataDefine) && CommonUtil.isNotNull(msgWxTemplateDataDefine.getSourceWxTemplateDataDefineId())){
			WxTemplateDataDefine sourceWxTemplateDataDefine = this.getById(WxTemplateDataDefine.class, msgWxTemplateDataDefine.getSourceWxTemplateDataDefineId());
			msgWxTemplateDataDefine.setSourceWxTemplateDataDefine(sourceWxTemplateDataDefine); 
		}
		return msgWxTemplateDataDefine ;
	}
	
	@Override
	public WxTemplateDataDefine initWxTemplateDataDefine(Long wxTemplateId){
		WxTemplateDataDefine wxTemplateDataDefine = new WxTemplateDataDefine();
		wxTemplateDataDefine.setTemplateId(wxTemplateId);
		return wxTemplateDataDefine;
	}

	@Override
	public WxTemplateDataDefine saveWxTemplateDataDefine(WxTemplateDataDefine wxTemplateDataDefine) {
		this.validateWxTemplateDataDefine (wxTemplateDataDefine);
		return this.save(wxTemplateDataDefine);
	}
	
	private void validateWxTemplateDataDefine (WxTemplateDataDefine wxTemplateDataDefine ) {
		
	}
	
	@Override
	public WxTemplateDataDefine saveWxMsgTemplateDataDefine(WxTemplateDataDefine wxTemplateDataDefine) {
		this.validateWxTemplateDataDefine (wxTemplateDataDefine);
		WxTemplateDataDefine savedWxTemplateDataDefine = this.save(wxTemplateDataDefine);
		getSourceWxTemplateDataDefineForMsgWxTemplateDataDefine(savedWxTemplateDataDefine);
		return savedWxTemplateDataDefine;
	}
	
	private void validateWxMsgTemplateDataDefine (WxTemplateDataDefine wxTemplateDataDefine ) {
		
	}
	
	@Override
	public void deleteWxTemplateDataDefine(List<IdAndVersion> idvs) {	
		for (IdAndVersion idv : idvs) {
			this.deleteWxTemplateDataDefineById(idv.getId(), idv.getVersion());
		}
	}
	
	@Override
	public void deleteWxTemplateDataDefineById(Long id, Integer version) {
		this.delete(WxTemplateDataDefine.class, id, version)	;
	}
	 
	/**
	 * 导出字段定义
	 */
	@Override
	public CellProperty[] genWxTemplateDataDefineExportTemplate() {
		CellProperty[] cellPropertis = new CellProperty[]{				

				 new CellProperty("数据字段","A","keyCode",true,null),
				 new CellProperty("系统模板","B","isSys",true,null),
				 new CellProperty("熟悉名称","C","propertyName",true,null),
				 new CellProperty("内容","D","context",true,null),
				 new CellProperty("颜色","E","color",true,null)				
		};
		return cellPropertis;
	}
	
	/**
	 * 导入主数据
	 */
	@Override
	public void importWxTemplateDataDefine(File file, Long wxTemplateId ) throws Exception  {
 		CellProperty[] cellPropertis = this.genWxTemplateDataDefineExportTemplate();
 		List<WxTemplateDataDefine> impList = ExcelUtil.loadExcelDataForJ(file, WxTemplateDataDefine.class, cellPropertis, null,1);
 		if (impList != null) {
 			for (WxTemplateDataDefine v : impList ) {
 				v.setTemplateId(wxTemplateId);
 				this.saveWxTemplateDataDefine(v);
 			}
 		}
	}
 
	/**
	 * 批量操作， 更近 action 名称
	 * @param idv
	 * @param action
	 */
	public void wxTemplateDataDefinebatchAction(List<IdAndVersion> idv, String action) {
//		if ("forbidden".equals(action)) {
//			this.forbidden(idv,true);
//		} else if ("unforbidden".equals(action)) {
//			this.forbidden(idv,false);
//		}else {
//			throw new ValidateException("不存在的action: "+action);
//		}
	}
//	public String genBaseAndColsButtonHtml(Long commonContractId) {
//		String buttonsHtml = "" ;
//		String baseButtonsHtml = this.genBaseButtons();
//		
//		List<CommonContractExtDefine> commonContractExtDefineList = this.findCommonContractExtDefineByCommonContractId(commonContractId);
//		Map<Object,List<CommonContractExtDefine>> mlist= CommonUtil.ListToMapList(commonContractExtDefineList, "groupName");
//		
//		StringBuffer colsButton= new StringBuffer();
//		for(Entry<Object, List<CommonContractExtDefine>> entry:mlist.entrySet()){
//			if (entry.getValue() != null) {
//				int i=0;
//				colsButton.append("<tr><th>"+entry.getKey()+"</th><td>");
//				for (CommonContractExtDefine p : entry.getValue()) {
//					colsButton.append("<a class='k-button k-button-icontext' data-name='commonContractExt."+p.getColName()+"' data-keycode='"+p.getPropertyName()+"'href='javascript:;' >"+p.getPropertyName()+"</a>");
//				}
//				colsButton.append("</td></tr>");
//			}
//		}
// 		buttonsHtml +="<table class='win_form content_table' >"+baseButtonsHtml +colsButton+"</table>";
//		return buttonsHtml ;
//	}
	@Override
	public String genElementButtonHtml() {
		
		List<ParseElement> uElement= findWfeElements("U");
		
		List<ParseElement> fElement= findWfeElements("F");
		
		StringBuffer buttonHtml = new StringBuffer("");
		for(ParseElement p:uElement){
			buttonHtml.append("<a class='k-button k-button-icontext' name='ebutton' source='"+JSONObject.toJSONString(p).toString()+"' title='"+p.getDescription()+"' id='"+p.getEleNumber()+"' href='javascript:;' >"+p.getEleName()+"</a>");
		}
		for(ParseElement p:fElement){
			buttonHtml.append("<a class='k-button k-button-icontext' name='ebutton' source='"+JSONObject.toJSONString(p).toString()+"' title='"+p.getDescription()+"' id='"+p.getEleNumber()+"' href='javascript:;' >"+p.getEleName()+"</a>");
		}
		
		
		return buttonHtml.toString();
	}
	public List<ParseElement> findWfeElements(String eleCategory) {	
		ParseElementQueryCriteria criteria = new ParseElementQueryCriteria();
		criteria.setCategoryCode("WFE");
		criteria.setEleCategory(eleCategory);
		List<ParseElement> uElement= parseElementMainService.findAllParseElements(criteria);
		return uElement ;
	}
	@Override
	public Map<String,String> getMapOfFUElementKeyIsElementNumber() {	
		
		List<ParseElement> uElements = this.findWfeElements("U");
		List<ParseElement> fElements = this.findWfeElements("F");
		Map<String,String> m = new HashMap<String,String>();
		for(ParseElement p:uElements){
			m.put(p.getEleNumber(),p.getEleName());
		}
		for(ParseElement p:fElements){
			m.put(p.getEleNumber(),p.getEleName());
		}
		
		return m;
		
	}
	@Override
	public Map<String,String> getMapOfFUElementKeyIsEleName() {	
		
		List<ParseElement> uElements = this.findWfeElements("U");
		List<ParseElement> fElements = this.findWfeElements("F");
		Map<String,String> m = new HashMap<String,String>();
		for(ParseElement p:uElements){
			m.put(p.getEleName(),p.getEleNumber());
		}
		for(ParseElement p:fElements){
			m.put(p.getEleName(),p.getEleNumber());
		}
		
		return m;
		
	}
  		
}

