


 package com.yq.xcode.common.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.yq.xcode.common.bean.CellProperty;
import com.yq.xcode.common.bean.IdAndVersion;
import com.yq.xcode.common.criteria.WxTemplateCriteria;
import com.yq.xcode.common.model.WxTemplate;
import com.yq.xcode.common.model.WxTemplateDataDefine;
import com.yq.xcode.common.springdata.HPageRequest;


public interface WxTemplateService {
	

	public WxTemplate getWxTemplateById(Long id);
	
	/**
	 * 新建初始化模型数据， 页面ID打开和新建就是一样的了， 如果ID为空就初始化一个
	 */
	public WxTemplate initWxTemplate(Long isSys);
	
	public WxTemplate saveWxTemplate(WxTemplate wxTemplate);

	public void deleteWxTemplate(List<IdAndVersion> idvs);
	
	/**
	 * 
	 */
	public void deleteWxTemplateById(Long id, Integer version);
	
	/**
	 * 导出字段定义
	 */
	public CellProperty[] genWxTemplateExportTemplate();
	
	/**
	 * 导入主数据
	 */
	public void importWxTemplate(File file) throws Exception  ;
	
	/**
	 * 批量操作， 更近 action 名称
	 * @param idv
	 * @param action
	 */
	public void batchAction(List<IdAndVersion> idv, String action);
	
	    /**
     * 主表取明细数据
     */
	public List<WxTemplateDataDefine> findWxTemplateDataDefinesByPId(Long wxTemplateId );
	
    public WxTemplateDataDefine getWxTemplateDataDefineById(Long id);
	
	/**
	 * 新建初始化模型数据， 页面ID打开和新建就是一样的了， 如果ID为空就初始化一个
	 */
	public WxTemplateDataDefine initWxTemplateDataDefine(Long wxTemplateid);  
	
	public WxTemplateDataDefine saveWxTemplateDataDefine(WxTemplateDataDefine wxTemplateDataDefine);

	public void deleteWxTemplateDataDefine(List<IdAndVersion> idvs);
	
	public void deleteWxTemplateDataDefineById(Long id, Integer version);
 
	
	/**
	 * 导出字段定义
	 */
	public CellProperty[] genWxTemplateDataDefineExportTemplate();
	
	/**
	 * 导入主数据
	 */
	public void importWxTemplateDataDefine(File file, Long wxTemplateId ) throws Exception  ;
	
	/**
	 * 批量操作， 更近 action 名称
	 * @param idv
	 * @param action
	 */
	public void wxTemplateDataDefinebatchAction(List<IdAndVersion> idv, String action);

	public Page<WxTemplate> findWxTemplates(HPageRequest hpr,WxTemplateCriteria criteria, String isSys);

	public WxTemplate getWxMsgTemplateById(Long id);

	public WxTemplate saveWxMsgTemplate(WxTemplate wxTemplate);

	public WxTemplateDataDefine getSourceWxTemplateDataDefineForMsgWxTemplateDataDefine(
			WxTemplateDataDefine msgWxTemplateDataDefine);

	public WxTemplateDataDefine getWxMsgTemplateDataDefineById(Long id);

	public WxTemplateDataDefine saveWxMsgTemplateDataDefine(
			WxTemplateDataDefine wxTemplateDataDefine);

	public String genElementButtonHtml();

	public Map<String, String> getMapOfFUElementKeyIsElementNumber();

	public Map<String, String> getMapOfFUElementKeyIsEleName();

	  
}
