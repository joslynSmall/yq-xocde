package com.yq.xcode.security.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sun.jndi.url.iiopname.iiopnameURLContextFactory;
import com.sun.scenario.effect.light.Light;
import com.yq.xcode.common.bean.Result;
import com.yq.xcode.common.bean.SelectItem;
import com.yq.xcode.common.bean.SelectItemDefine;
import com.yq.xcode.common.service.InitConstantsService;
import com.yq.xcode.common.utils.ResultUtil;
import com.yq.xcode.security.criteria.RoleCriteria;
import com.yq.xcode.security.entity.Role;
import com.yq.xcode.security.service.RoleService;

@RestController
@RequestMapping("/admin/role")

//@PreAuthorize("hasPermission('SEC_ROLE', 1)")
public class RoleController {

	@Autowired
	private RoleService roleService;
	@Autowired
	private InitConstantsService initConstantsService;

	/**
	 * 获取角色列表
	 */
	@PostMapping("/page")
	public Result<Page<Role>> findRole(@RequestBody RoleCriteria criteria) {
		Page<Role> page = roleService.findRolePage(criteria);
		return ResultUtil.ok(page);
	}

	@GetMapping("/roleType")
	public Result<List<SelectItem>> findRoleType() {
		SelectItemDefine selectItemDefine = initConstantsService.getSelectItemDefine("ROLETYPE");
		return ResultUtil.ok(selectItemDefine.getSelectItemList());
	}

	@GetMapping("/{id}")
	public Result<?> getRoleById(@PathVariable Long id) {
		Role role = roleService.getRoleById(id);
		return ResultUtil.ok(role);
	}

	/**
	 * 获取角色列表
	 */
	@PostMapping("/savePermission")
	public Result<?> saveRolePermission(@RequestBody Role role) {
		roleService.saveRolePermission(role);
		return ResultUtil.ok();
	}

	/**
	 * 获取角色列表
	 */
	@PostMapping("/save")
	public Result<?> saveRole(@RequestBody Role role) {
		roleService.saveRole(role);
		return ResultUtil.ok();
	}

	@PostMapping("/delete")
	public Result<?> delRole(@RequestBody Role role) {
		roleService.deleteRole(role.getId());
		return ResultUtil.ok();
	}
}
