package com.yq.xcode.security.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.yq.xcode.common.bean.ResourceInstance;
import com.yq.xcode.common.bean.ResourceView;
import com.yq.xcode.security.bean.PrincipalBean;
import com.yq.xcode.security.bean.ResourceAssignmentBean;
import com.yq.xcode.security.bean.RoleAssignmentBean;
import com.yq.xcode.security.criteria.PrincipalCriteria;
import com.yq.xcode.security.entity.Role;
import com.yq.xcode.security.entity.SecPrincipal;

public interface PrincipalService {

	public Page<SecPrincipal> findPrincipalPage(PrincipalCriteria criteria);
	
	public SecPrincipal getPrincipalById(Long id);
	
	public void savePrincipal(SecPrincipal principal);
	
	public void savePrincipal(PrincipalBean bean);
	
	public void deletePrincipal(Long id);
	
	public SecPrincipal getPrincipalByUsername(String username);

	/**
	 * 根据资源取用户
	 * @param allResList
	 * @return
	 */
	public List<SecPrincipal> findUserListByResource(List<ResourceView> allResList);
	/**
	 * 根据用户获取所属角色
	 * @param principalId  先单个查询，后续优化
	 * @return List<Role>
	 */
	public List<Role> findRoleByPrincipal(Long principalId);
	/**
	 * 根据用户获取角色
	 * @param principalId  先单个查询，后续优化
	 * @return List<RoleAssignmentBean>
	 */
	public List<RoleAssignmentBean> findRoleAssignmentBean(Long principalId);
	/**
	 * 保存角色信息
	 * @param beanList  
	 */
	public void saveRoleAssignmentBean(List<RoleAssignmentBean> beanList, Long principalId);
	/**
	 * 根据用户获取资源
	 * @param resourceName  
	 * @param principalId  先单个查询，后续优化
	 * @return List<ResourceInstance>
	 */
	public List<ResourceInstance> findResourceInstanceByPrincipal(String resourceName,Long principalId);
	/**
	 * 根据用户获取资源信息
     * @param resourceName  
	 * @param principalId 	
	 * @return List<ResourceAssignmentBean>
	 */
	public List<ResourceAssignmentBean> findResourceAssignmentBean(String resourceName,Long principalId);
	/**
	 * 保存资源信息
	 * @param resourceName
	 * @param beanList  目前暂时只支持流程角色
	 */
	public void saveResourceAssignmentBean(String resourceName,List<ResourceAssignmentBean> beanList, Long principalId);

	public void savePrincipal(SecPrincipal principal, String password);

}
