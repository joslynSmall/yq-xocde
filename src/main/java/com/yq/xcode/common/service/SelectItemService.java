package com.yq.xcode.common.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.yq.xcode.common.bean.ListPageDefine;
import com.yq.xcode.common.bean.SelectItem;
import com.yq.xcode.common.bean.SelectItemDefine;
import com.yq.xcode.common.criteria.SelectItemCriteria;
import com.yq.xcode.common.springdata.HPageRequest;


public interface SelectItemService {

	public List<SelectItem> findSelectItemBycriteria(SelectItemCriteria criteria) ;

	public Page<SelectItem> findSelectItemPageBycriteria( SelectItemCriteria criteria) ;

	public Map<String,String> nameToKeyMap(String category );

	/**
	 * 查询页面定义
	 * @param category
	 * @return
	 */
	public ListPageDefine getSelectItemPageDefine(String category);

	public List<SelectItem> findLookupCodeSelect(String category);

	public List<SelectItem> findVendorByVendorType(String vendorType);

	public List<SelectItem> findAdvertisingType(String advertisingType);

	public List<SelectItem> findAllMonthSetUp();

	public List<SelectItem> findWorkFlowStatusByCategoryCode(String categoryCode);

}
