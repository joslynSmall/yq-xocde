package com.yq.xcode.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yq.xcode.common.bean.Result;
import com.yq.xcode.common.utils.ResultUtil;
import com.yq.xcode.security.criteria.ResourceCriteria;
import com.yq.xcode.security.entity.Permission;
import com.yq.xcode.security.entity.ResourceDefination;
import com.yq.xcode.security.service.ResourceService;

@RestController
@RequestMapping("/admin/resource")
//@PreAuthorize("hasPermission('SEC_PERMISSION', 1)")
public class ResourceDefinationController {

	@Autowired
	private ResourceService resourceService;

	/**
	 * 获取所有权限列表
	 */
	@PostMapping("/page")
	public Result<Page<ResourceDefination>> findPermissionPage(@RequestBody ResourceCriteria criteria) {
			Page<ResourceDefination> page = resourceService.findResourcePage(criteria);
			return ResultUtil.ok(page);
	}
	/**
	 * 获取所有权限列表
	 */
	@PostMapping("/save")
	public Result<ResourceDefination> savePermission(@RequestBody ResourceDefination resource) {
			resourceService.saveResourceDefination(resource);
        	return ResultUtil.ok(resource);
	}

	/**
	 * 获取所有权限列表
	 */
	@PostMapping("/delete")
	public Result<?> delResourceDefination(@RequestBody Permission resource) {
			resourceService.deleteResourceDefination(resource.getId());
			return ResultUtil.ok();
	}
}
