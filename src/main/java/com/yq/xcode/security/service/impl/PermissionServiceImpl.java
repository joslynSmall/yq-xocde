package com.yq.xcode.security.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.yq.xcode.common.exception.ValidateException;
import com.yq.xcode.common.exception.XException;
import com.yq.xcode.common.service.SqlToModelService;
import com.yq.xcode.common.service.impl.YqJpaDataAccessObject;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.JPAUtils;
import com.yq.xcode.security.bean.ActionMask;
import com.yq.xcode.security.bean.PermissionAction;
import com.yq.xcode.security.bean.PermissionBean;
import com.yq.xcode.security.criteria.PermissionCriteria;
import com.yq.xcode.security.entity.Permission;
import com.yq.xcode.security.entity.PermissionAssignment;
import com.yq.xcode.security.service.PermissionService;

@Service
@Transactional
public class PermissionServiceImpl  extends YqJpaDataAccessObject implements PermissionService {

    @Autowired
    private SqlToModelService sqlToModelService;
	@Override
	public List<Permission> findAll() {
        StringBuffer query = new StringBuffer();
        query.append("SELECT ")
                .append(JPAUtils.genEntityCols(Permission.class, "a",null))
                .append(" FROM sec_permission a")
                .append(" WHERE 1=1  ");
        List<Permission> list = sqlToModelService.executeNativeQuery(query.toString(), null, Permission.class);
        return list;

	}

	@Override
	public Permission getPermission(Long id) {
		return this.getById(Permission.class, id);
	}
	private Permission getPermissionByCode(String code) {
		@SuppressWarnings("unchecked")
		List<Permission> list = this.find(" from Permission a where a.code=?0", code);
		if(CommonUtil.isNull(list)){
			return null;
		}
		return list.get(0);
	}
	@Override
	public void savePermission(Permission permission) {
		if (null == permission.getId()) {
			if (null != getPermissionByCode(permission.getCode())) {
				throw new XException(String.format("编码%s已存在", permission.getCode()));
			}
		}
		this.save(permission);
	}

	@Override
	public void deletePermission(Long id) {
		this.deleteById(Permission.class, id);
	}
	@Override
	public List<PermissionAssignment> getRolePermissionBySId(Long sid) {
        StringBuffer query = new StringBuffer();
        query.append("SELECT ")
                .append(JPAUtils.genEntityCols(PermissionAssignment.class, "a",null))
                .append(" FROM sec_permission_assignment a")
                .append(" WHERE a.SID_ID = " + sid);
        List<PermissionAssignment> list = sqlToModelService.executeNativeQuery(query.toString(),
        		null, PermissionAssignment.class);
        return list;
	}
	public List<PermissionAssignment> getRolePermissionByRoleId(Long roleId) {
        StringBuffer query = new StringBuffer();
        query.append("SELECT ")
                .append(JPAUtils.genEntityCols(PermissionAssignment.class, "a",null))
                .append(" FROM sec_permission_assignment a,sec_sid b")
                .append(" WHERE a.SID_ID =b.id and b.type=1 and b.sid = " + roleId);
        List<PermissionAssignment> list = sqlToModelService.executeNativeQuery(query.toString(),
        		null, PermissionAssignment.class);
        return list;
	}
	public List<PermissionBean> findPermissionBean(Long roleId) {
		List<PermissionAssignment> rolePermissions = this.getRolePermissionByRoleId(roleId);
		if (!ObjectUtils.isEmpty(rolePermissions)) {
			List<PermissionBean> permissionBeans = new ArrayList<PermissionBean>();
			for (PermissionAssignment rolePermission : rolePermissions) {
				Permission permission = this.getPermission(rolePermission.getPermissionId());			
				// 权限
				PermissionBean permissionBean = new PermissionBean();
				permissionBean.setPermissionId(permission.getId());
				permissionBean.setCode(permission.getCode());
				permissionBean.setName(permission.getName());				
				// 动作
				List<PermissionAction> permissionActions = new ArrayList<PermissionAction>();
				for(ActionMask actionMask : permission.getActionMask())  {
					PermissionAction permissionAction = new PermissionAction();
					boolean selected  = (actionMask.getMask()&rolePermission.getActionMask()) > 0;
					permissionAction.setSelected(selected);
					permissionAction.setMask(actionMask.getMask());
					permissionAction.setName(actionMask.getName());
					permissionActions.add(permissionAction);
				}
				
				permissionBean.setActions(permissionActions);
				permissionBeans.add(permissionBean);
			}
			return permissionBeans;
		}
		return null;
	}

	@Override
	public List<PermissionBean> findAllPermissionBean(Long roleId) {
		List<Permission> permissions = this.findAll();
		List<PermissionBean> permissionBeans = new ArrayList<PermissionBean>();
		
		List<PermissionBean> beans = findPermissionBean(roleId);
		HashMap<String, PermissionBean> map = new HashMap<String, PermissionBean>();
		if(!ObjectUtils.isEmpty(beans)){
			for(PermissionBean bean : beans){
				map.put(bean.getCode(), bean);
			}
		}
		
		if (!ObjectUtils.isEmpty(permissions)) {
			for(Permission permission:permissions){
				PermissionBean permissionBean = this.conversion(permission);
				if(map.containsKey(permissionBean.getCode())){
					permissionBeans.add(map.get(permissionBean.getCode()));
				}else{
					permissionBeans.add(permissionBean);
				}
				
			}
		}
		return permissionBeans;
	}
	
	private PermissionBean conversion(Permission permission){
		PermissionBean permissionBean = new PermissionBean();
		permissionBean.setPermissionId(permission.getId());
		permissionBean.setCode(permission.getCode());
		permissionBean.setName(permission.getName());
		List<PermissionAction> permissionActions = new ArrayList<PermissionAction>();
		if(!ObjectUtils.isEmpty(permission.getActionMask())){
			for(ActionMask actionMask : permission.getActionMask())  {
				PermissionAction permissionAction = new PermissionAction();
				permissionAction.setSelected(false);
				permissionAction.setMask(actionMask.getMask());
				permissionAction.setName(actionMask.getName());
				permissionActions.add(permissionAction);
			}
		}
		
		permissionBean.setActions(permissionActions);
		return permissionBean;
	}
	@Override
	public Page<Permission> findPermissionPage(PermissionCriteria criteria) {
        StringBuffer query = new StringBuffer();
        query.append("SELECT ")
                .append(JPAUtils.genEntityCols(Permission.class, "a",null))
                .append(" FROM sec_permission a")
                .append(" WHERE 1=1  ");
        Page<Permission> page = sqlToModelService.executeNativeQueryForPage(query.toString(), "a.code", "a.ID", criteria, Permission.class);
        return page;
	}
}
