package com.yq.xcode.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yq.xcode.common.bean.Result;
import com.yq.xcode.common.utils.ResultUtil;
import com.yq.xcode.security.criteria.PermissionCriteria;
import com.yq.xcode.security.entity.Permission;
import com.yq.xcode.security.service.PermissionService;

@RestController
@RequestMapping("/admin/permission")
//@PreAuthorize("hasPermission('SEC_PERMISSION', 1)")

public class PermissionController {

	@Autowired
	private PermissionService permissionService;

	/**
	 * 获取所有权限列表
	 */

	@PostMapping("/page")
	public Result<Page<Permission>> findPermissionPage(@RequestBody PermissionCriteria criteria) {
		Page<Permission> page = permissionService.findPermissionPage(criteria);
		return ResultUtil.ok(page);

	}

	/**
	 * 获取所有权限列表
	 */
	@PostMapping("/save")
	public Result<Permission> savePermission(@RequestBody Permission permission) {
		permissionService.savePermission(permission);
		return ResultUtil.ok(permission);

	}

	/**
	 * 获取所有权限列表
	 */
	@PostMapping("/delete")
	public Result<?> delPermission(@RequestBody Permission permission) {
		permissionService.deletePermission(permission.getId());
		return ResultUtil.ok();

	}
}
