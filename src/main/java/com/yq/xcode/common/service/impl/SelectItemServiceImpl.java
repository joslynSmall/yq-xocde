package com.yq.xcode.common.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yq.xcode.common.utils.DateUtil;
import com.yq.xcode.common.utils.JPAUtils;
import com.yq.xcode.common.utils.YqBeanUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.yq.xcode.common.bean.HColumn;
import com.yq.xcode.common.bean.ListPageDefine;
import com.yq.xcode.common.bean.LookupCodeCategory;
import com.yq.xcode.common.bean.SelectItem;
import com.yq.xcode.common.bean.SelectItemDefine;
import com.yq.xcode.common.bean.SelectItemDefine.SourceCategory;
import com.yq.xcode.common.criteria.SelectItemCriteria;
import com.yq.xcode.common.exception.ValidateException;
import com.yq.xcode.common.model.PageTag;
import com.yq.xcode.common.service.InitConstantsService;
import com.yq.xcode.common.service.LookupCodeService;
import com.yq.xcode.common.service.SelectItemService;
import com.yq.xcode.common.service.SqlToModelService;
import com.yq.xcode.common.springdata.AggregatePageImpl;
import com.yq.xcode.common.springdata.HPageRequest;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.YqJsonUtil;
import com.yq.xcode.constants.YqConstants;
import com.yq.xcode.constants.YqSelectHardcodeConstants;



@Service("SelectItem")
public class SelectItemServiceImpl extends YqJpaDataAccessObject implements SelectItemService {
	@Autowired
	private SqlToModelService sqlToModelService;
	@Autowired
	private LookupCodeService lookupCodeService;
	@Autowired
	private InitConstantsService initConstantsService;
	@Override
	public List<SelectItem> findSelectItemBycriteria(SelectItemCriteria criteria) {
		String category = criteria.getQueryCategory();
		if (CommonUtil.isNull(category)) {
			throw new ValidateException("SelectItemCriteria.queryCategory 不能为空 !");
		}
		if (category.startsWith(YqConstants.LIST_CATEGORY_HARDCODE_PREFIX.HARDCODE.getItemKey())) {
			String s = category.substring(YqConstants.LIST_CATEGORY_HARDCODE_PREFIX.HARDCODE.getItemKey().length());
			return this.genSelectItemByStr(s);
		}
		if (category.startsWith(YqConstants.LIST_CATEGORY_HARDCODE_PREFIX.WFSTATUS.getItemKey())) {
			String s = category.substring(YqConstants.LIST_CATEGORY_HARDCODE_PREFIX.WFSTATUS.getItemKey().length());
			return findWorkFlowStatusByCategoryCode(s);
		}
		 
		String query = null;
		SelectItemDefine  siDefine = this.initConstantsService.getSelectItemDefine(category);
		List<SelectItem> sits = new ArrayList<SelectItem>();
		if (siDefine != null ) {
			if (siDefine.getSourceCategory().equals(SourceCategory.QUERY)) {
				query = siDefine.getQuery();
			} else if (siDefine.getSourceCategory().equals(SourceCategory.HARDCODE)) {

				if (CommonUtil.isNotNull(siDefine.getSelectItemList())) { // 系统hardcode在静态文件中的， 通过静态方法
					return siDefine.getSelectItemList();
				} else if (CommonUtil.isNotNull(siDefine.getQuery())) {
					return YqJsonUtil.parseArray(siDefine.getQuery(), SelectItem.class);
				}
			}
		}

		if ( query == null ) {
			LookupCodeCategory lcCategory = this.initConstantsService.getLookupCodeCategory(category);

			if ( lcCategory == null ) {
				throw new ValidateException("查询类型 : " + category+" 不存在 ");
			} else {
				query = " select lc.key_code itemKey, lc.lookup_code itemValue, lc.lookup_name itemName "
						+" from yq_lookup_code lc "
						+" where lc.category_code = '"+category+"' and lc.lookup_level = 1";
			}
		}

		query = "select * from ("+query+") a where 1=1 ";
		return  this.sqlToModelService.executeNativeQuery(query, criteria, SelectItem.class);
	}



	private List<SelectItem> genSelectItemByStr(String s) {
		List<SelectItem> siList = new ArrayList<SelectItem>();
		if (CommonUtil.isNull(s)) {
			return siList;
		}
		String[] sa = s.split(",");
		for (String v : sa) {
			siList.add(new SelectItem(v,v));
		}
		return siList;
	}
    
	/**
	 * 流程状态
	 * @param categoryCode
	 * @return
	 */
	@Override
	public List<SelectItem> findWorkFlowStatusByCategoryCode(String categoryCode) {
 		String query = " select distinct lcc.key_code itemKey, lcc.lookup_name itemName from yq_work_flow wf ,yq_work_flow_graph_detail wfgd, yq_lookup_code lcc "
				+"  where wf.category_code ='"+categoryCode+"'                                                         "
				+"    and wfgd.work_flow_id = wf.id                                                               "
				+"    and INSTR(wfgd.source_statuses,lcc.KEY_CODE) > 0                                            "
				+"    and lcc.CATEGORY_CODE = 'WST'                                                               "
				+" order by lcc.LINE_NUMBER                                                                       ";
		return this.sqlToModelService.executeNativeQuery(query, null, SelectItem.class);
	}


	@Override
	public Page<SelectItem> findSelectItemPageBycriteria( SelectItemCriteria criteria) {
		String category = criteria.getQueryCategory();
		if (CommonUtil.isNull(category)) {
			throw new ValidateException("SelectItemCriteria.queryCategory 不能为空 !");
		}

		String query = null;
		SelectItemDefine  siDefine = this.initConstantsService.getSelectItemDefine(category);
		String keywordCause = "";
		if (siDefine != null ) {
			if (siDefine.getSourceCategory().equals(SourceCategory.QUERY)) {
				query =  siDefine.getQuery();
				ListPageDefine listPageDefine = this.getSelectItemPageDefine(category);
				if (CommonUtil.isNotBlank(criteria.getKeyword())) { 
					for (HColumn c : listPageDefine.getColumnList() ) {
						keywordCause = keywordCause+",ifnull("+c.getField()+",'')";
					}
					keywordCause = " and concat("+keywordCause.substring(1)+") like '%"+JPAUtils.toPar(criteria.getKeyword())+"%' ";
				}
				for (PageTag pt : listPageDefine.getPageTagList())  {
					if (!"keyword".equals(pt.getProperty())  ) {
						Object value = YqBeanUtil.getPropertyValue(criteria, pt.getProperty());
						if (CommonUtil.isNotNull(value)) {
							String operation = pt.getOpeartion();
							if (CommonUtil.isNull(operation)) {
								operation = "like";
							}
							keywordCause = keywordCause+" and "+pt.getProperty()+" "+operation+" '"+JPAUtils.toPar(value)+"' ";
						}
						
					}
				}
				
			} else if (siDefine.getSourceCategory().equals(SourceCategory.HARDCODE)) {
				List<SelectItem> sits = YqJsonUtil.parseArray(siDefine.getQuery(), SelectItem.class);
				Page<SelectItem> pageImpl = new AggregatePageImpl(sits  ,new HPageRequest(criteria),sits.size());
				return pageImpl;
			}
		}
		if ( query == null ) {
			LookupCodeCategory lcCategory = this.initConstantsService.getLookupCodeCategory(category);
			if ( lcCategory == null ) {
				throw new ValidateException("查询类型 : " + category+" 不存在 ");
			} else {
				query = " select lc.key_code itemKey, lc.lookup_code itemValue, lc.lookup_name itemName "
						+" from yq_lookup_code lc "
						+" where lc.category_code = '"+category+"' ";
				if (CommonUtil.isNotBlank(criteria.getKeyword())) {
					 
					keywordCause = " and concat(itemValue,itemName) like '%"+JPAUtils.toPar(criteria.getKeyword())+"%' ";
				}
			}
		}

		query = "select * from ("+query+") a where 1=1 "+keywordCause;
		AggregatePageImpl<SelectItem> pageImpl = this.sqlToModelService.executeNativeQueryForPage(query, null, null, criteria,  SelectItem.class) ;

		return pageImpl;
	}

	@Override
	public ListPageDefine getSelectItemPageDefine( String category ) {
		ListPageDefine listPageDefine = new ListPageDefine();
		SelectItemDefine  siDefine = this.initConstantsService.getSelectItemDefine(category);
		if ( siDefine == null ) {
			throw new ValidateException("取数类型 "+category+" 不存在 ， 请查证 ！");
		}
		if (CommonUtil.isNotNull(siDefine.getColumns())) {
			listPageDefine.setColumnList( Arrays.asList(siDefine.getColumns()) );
		} else {
			listPageDefine.setColumnList(DEFAULT_COLUMN);
		}
 
		listPageDefine.getPageTagList().add(new PageTag("keyword","匹配查询",YqSelectHardcodeConstants.PageTag_tag.TextTag));
		if ( CommonUtil.isNotNull(siDefine.getPageTags())) {
			for (PageTag tag : siDefine.getPageTags()) {
				listPageDefine.getPageTagList().add(tag);
			} 
		} 
		return listPageDefine;
	}

	@Override
	public Map<String, String> nameToKeyMap(String category) {
		SelectItemCriteria criteria = new SelectItemCriteria();
		criteria.setQueryCategory(category);
		Map<String, String> map = new HashMap<String, String>();
		List<SelectItem> siList = this.findSelectItemBycriteria(criteria);
		for (SelectItem si : siList) {
			map.put(si.getItemName(), si.getItemName());
		}
		return map;
	}

	private static List<HColumn> DEFAULT_COLUMN = new ArrayList<HColumn>() {
		{ 
			this.add(new HColumn( "itemName", "名称", "")); 
		}

	};
	/**
	 * DELETED = false 查找不被禁用的系统代码
	 */
	@Override
	public List<SelectItem> findLookupCodeSelect(String category) {
		String query = "select key_code itemKey, lookup_name itemName from yq_lookup_code where CATEGORY_CODE = '"+category+"' and ifnull(DELETED,0) = 0 order by LINE_NUMBER";
		return this.sqlToModelService.executeNativeQuery(query, null, SelectItem.class);
	}

	@Override
	public List<SelectItem> findVendorByVendorType(String vendorType) {
		String query ="select ID itemKey, VENDOR_NAME itemName from SO_VENDOR v where 1=1";
		if("CHAING".equals(vendorType)){
			query += " and v.VENDOR_TYPE in ('CHAIN','CHAINGRP')";
		}else{
			query += " and v.VENDOR_TYPE = '" + vendorType + "'";
		}
		return this.sqlToModelService.executeNativeQuery(query, null, SelectItem.class);
	}

	@Override
	public List<SelectItem> findAdvertisingType(String advertisingType) {
		StringBuffer query = new StringBuffer();
		query.append(" SELECT key_code itemKey, lookup_name itemName ")
				.append(" FROM yq_lookup_code ")
				.append(" WHERE CATEGORY_CODE = '")
				.append(advertisingType)
				.append("'");
		return this.sqlToModelService.executeNativeQuery(query.toString(), null, SelectItem.class);
	}

	@Override
	public List<SelectItem> findAllMonthSetUp() {
		String currentMonth = DateUtil.convertDate2String(DateUtil.getCurrentDate(), "yyyy-MM");
		String query = " select distinct mst.month itemName, mst.month itemKey "
				//+"		 if( nvl(runbatch.execute_status,'NE') = 'NE',runbatch.batch_number,null) remark "
				+"		 from so_month_setup mst "
				//	" left join so_run_rebates_batch runbatch on mst.month = runbatch.rebates_month "
				+"		where mst.month <= '"+currentMonth+"' "
				//+"		  and ( nvl(runbatch.execute_status,'NE') = 'NE' or  mst.month = '"+currentMonth+"') "
				+"		order by mst.month desc ";
		List<SelectItem> list = this.sqlToModelService.executeNativeQuery(query, null, SelectItem.class);
		if (list != null && list.size() >=2 ) {  //去掉第一个月, 第一个月的返利第二个月算，
			list = list.subList(0, list.size() - 1);
		}
		return list;
	}
	
 

}
