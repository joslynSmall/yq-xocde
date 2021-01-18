package com.yq.xcode.security.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.yq.xcode.common.exception.XException;
import com.yq.xcode.common.service.SqlToModelService;
import com.yq.xcode.common.service.impl.YqJpaDataAccessObject;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.JPAUtils;
import com.yq.xcode.security.bean.PermissionAction;
import com.yq.xcode.security.bean.PermissionBean;
import com.yq.xcode.security.criteria.RoleCriteria;
import com.yq.xcode.security.entity.PermissionAssignment;
import com.yq.xcode.security.entity.Role;
import com.yq.xcode.security.entity.RoleAssignment;
import com.yq.xcode.security.entity.SecurityId;
import com.yq.xcode.security.service.PermissionService;
import com.yq.xcode.security.service.RoleService;
import com.yq.xcode.security.service.SecurityIdService;

@Service
@Transactional
public class RoleServiceImpl extends YqJpaDataAccessObject implements RoleService {

	@Autowired
	PermissionService permissionService;
	@Autowired
	SecurityIdService securityIdService;
	@Autowired
	private SqlToModelService sqlToModelService;

	@Override
	public List<Role> findRoleList() {
		StringBuffer query = new StringBuffer();
		query.append("SELECT ").append(JPAUtils.genEntityCols(Role.class, "a", null)).append(" FROM sec_role a")
				.append(" WHERE 1=1  ");
		List<Role> roles = sqlToModelService.executeNativeQuery(query.toString(), null, Role.class);
		return roles;
	}

	@Override
	public Role getRoleById(Long id) {
		Role role = this.getById(Role.class, id);
		List<PermissionBean> permissionBeans = permissionService.findAllPermissionBean(role.getId());
		role.setPermissions(permissionBeans);
		return role;
	}

	private Role getRoleByCode(String code) {
		@SuppressWarnings("unchecked")
		List<Role> list = this.find(" from Role a where a.code=?0", code);
		if (CommonUtil.isNull(list)) {
			return null;
		}
		return list.get(0);
	}

	private SecurityId handleRoleSecurityId(Role role) {
		SecurityId securityId = securityIdService.getSecurityIdBySId(role.getId(), SecurityId.Type.ROLE);
		if (securityId == null) {
			securityId = new SecurityId();
			securityId.setSid(role.getId());
			securityId.setType(SecurityId.Type.ROLE);
			securityId.setRealmId(role.getRealmId());
			securityIdService.saveSecurityId(securityId);
		}
		return securityId;
	}

	@Override
	public void saveRolePermission(Role role) {
		List<PermissionBean> permissionBeanList = role.getPermissions();
		SecurityId securityId = handleRoleSecurityId(role);
		assignmentPermission2Role(permissionBeanList, securityId);
	}

	public void saveRole(Role role) {
		if (null == role.getId()) {
			if (null != this.getRoleByCode(role.getCode())) {
				throw new XException(String.format("编码%s已存在", role.getCode()));
			}
		}
		this.save(role);
		handleRoleSecurityId(role);
	}

	private void assignmentPermission2Role(List<PermissionBean> permissionBeanList, SecurityId securityId) {
		if (CommonUtil.isNull(permissionBeanList)) {
			return;
		}
		List<PermissionAssignment> updateList = new ArrayList<PermissionAssignment>();
		List<PermissionAssignment> deletedList = new ArrayList<PermissionAssignment>();
		Map<Long, PermissionAssignment> map = new HashMap<Long, PermissionAssignment>();
		List<PermissionAssignment> rolePermissions = permissionService.getRolePermissionBySId(securityId.getId());
		if (CommonUtil.isNotNull(rolePermissions)) {
//			this.delete(rolePermissions);
			for (PermissionAssignment permissionAssignment : rolePermissions) {
				map.put(permissionAssignment.getPermissionId(), permissionAssignment);
			}
		}
		for (PermissionBean permissionBean : permissionBeanList) {
			if (!ObjectUtils.isEmpty(permissionBean.getActions())) {
				int mask = 0;
				for (PermissionAction actions : permissionBean.getActions()) {
					if (actions.isSelected()) {
						mask = mask | actions.getMask();
					}
				}
				PermissionAssignment rolePermission = new PermissionAssignment();
				if (mask == 0) {
					if (map.containsKey(permissionBean.getPermissionId())) {
						rolePermission = map.get(permissionBean.getPermissionId());
						deletedList.add(rolePermission);
					}
				} else {
					if (map.containsKey(permissionBean.getPermissionId())) {
						rolePermission = map.get(permissionBean.getPermissionId());
						updateList.add(rolePermission);
					}
					rolePermission.setPermissionId(permissionBean.getPermissionId());
					rolePermission.setSidId(securityId.getId());
					rolePermission.setActionMask(mask);
					updateList.add(rolePermission);
				}

			}
		}
		this.save(updateList);
		this.delete(deletedList);
	}

	@Override
	public void deleteRole(Long id) {
		SecurityId securityId = securityIdService.getSecurityIdBySId(id, SecurityId.Type.ROLE);
		if (securityId != null) {
			List<PermissionAssignment> rolePermissions = permissionService.getRolePermissionBySId(securityId.getId());
			if (CommonUtil.isNotNull(rolePermissions)) {
				this.delete(rolePermissions);
			}
			this.delete(securityId);
		}
		this.deleteById(Role.class, id);
	}

	@Override
	public Page<Role> findRolePage(RoleCriteria criteria) {
		StringBuffer query = new StringBuffer();
		query.append("SELECT ").append(JPAUtils.genEntityCols(Role.class, "a", null)).append(" FROM sec_role a")
				.append(" WHERE 1=1  ");
		Page<Role> page = sqlToModelService.executeNativeQueryForPage(query.toString(), "a.code", null, criteria,
				Role.class);
		for (Role role : page.getContent()) {
			List<PermissionBean> permissionBeans = permissionService.findPermissionBean(role.getId());
			role.setPermissions(permissionBeans);
		}
		return page;
	}

	@Override
	public List<RoleAssignment> getRoleAssignmentBySId(Long sid) {
		StringBuffer query = new StringBuffer();
		query.append("SELECT ").append(JPAUtils.genEntityCols(RoleAssignment.class, "a", null))
				.append(" FROM sec_role_assignment a ").append(" WHERE a.SID_ID =  " + sid);
		List<RoleAssignment> list = sqlToModelService.executeNativeQuery(query.toString(), null, RoleAssignment.class);
		return list;
	}

}
