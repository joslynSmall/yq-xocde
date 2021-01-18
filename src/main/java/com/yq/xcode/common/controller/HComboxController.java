package com.yq.xcode.common.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yq.xcode.common.bean.ListPageDefine;
import com.yq.xcode.common.bean.Result;
import com.yq.xcode.common.bean.SelectItem;
import com.yq.xcode.common.criteria.SelectItemCriteria;
import com.yq.xcode.common.service.SelectItemService;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.ResultUtil;

/**
 * 数据字典模块.
 * 
 * @author 
 * @date 2020/7/8
 */
@RestController
@RequestMapping("/admin/HCombox")
public class HComboxController {

	@Autowired
	private SelectItemService selectItemService;

	@RequestMapping(value = { "/selectItemList/{category}" }, method = RequestMethod.GET)
	public Result<?> findSelectItemListByCategory(@PathVariable("category") String category, SelectItemCriteria criteria) {
		criteria.setQueryCategory(category);
		List<SelectItem> list = this.selectItemService.findSelectItemBycriteria(criteria);
		return ResultUtil.ok(list);
	}

	@RequestMapping(value = { "/getSelectItemByCates/{cates}" }, method = RequestMethod.GET)
	public Result<?> getSelectItemByCates(@PathVariable("cates") String cates) {
		Map<String, List<SelectItem>> map = new HashMap<String, List<SelectItem>>();
		if (CommonUtil.isNotNull(cates)) {
			SelectItemCriteria criteria = new SelectItemCriteria();
			String[] sArr = cates.split(",");
			for (String category : sArr) {
				criteria.setQueryCategory(category);
				List<SelectItem> list = this.selectItemService.findSelectItemBycriteria(criteria);
				map.put(category, list);
			}
		}
		return ResultUtil.ok(map);
	}

	@RequestMapping(value = { "/selectItemPage" }, method = RequestMethod.GET)
	public Result<?> findSelectItemPageByCategory(SelectItemCriteria criteria) {
		criteria.setQueryCategory(criteria.getCategory());
		Page<SelectItem> page = this.selectItemService.findSelectItemPageBycriteria(criteria);
		return ResultUtil.ok(page);
	}

	@RequestMapping(value = { "/selectItemPageDefine/{category}" }, method = RequestMethod.GET)
	public Result<?> getSelectItemPageDefine(@PathVariable("category") String category) {
		ListPageDefine pgDefine = this.selectItemService.getSelectItemPageDefine(category);
		return ResultUtil.ok(pgDefine);
	}
 

}
