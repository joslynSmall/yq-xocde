package com.yq.xcode.security.oauth;

import java.security.PermissionCollection;
import java.security.Permissions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.yq.xcode.common.service.SqlToModelService;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.JPAUtils;
import com.yq.xcode.security.bean.GrantedPermissions;
import com.yq.xcode.security.bean.GrantedResource;
import com.yq.xcode.security.bean.MaskPermission;
import com.yq.xcode.security.bean.PermissionVo;
import com.yq.xcode.security.entity.ResourceAssignment;
import com.yq.xcode.security.entity.ResourceDefination;
import com.yq.xcode.security.entity.Role;
import com.yq.xcode.security.entity.SecPrincipal;
import com.yq.xcode.security.service.PrincipalService;

@Service
public class XUserDetailsServiceImpl implements XUserDetailsService {

	@Autowired
	private PrincipalService principalService;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private SqlToModelService sqlToModelService;
	 

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		SecPrincipal secPrincipal = principalService.getPrincipalByUsername(username);
		
		if (secPrincipal == null) {
			throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
		} else {
			LoginUser user = new LoginUser(secPrincipal);
			return new XUserDetails(user, findGrantedAuthorities(user.getId()));
		}
	}

	@Override
	public List<GrantedAuthority> findGrantedAuthorities(Long principalId) {
		List<GrantedAuthority> gas = new ArrayList<GrantedAuthority>();
 		List<Role> roles = findRolesOfPrincipalId(principalId);
		for (Role role : roles) {
			gas.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
		}
		gas.add(new GrantedPermissions(findPermissionsOfUser(roles, principalId)));
 		gas.addAll(this.findGrantedResourcesOfUser(principalId, roles ));
		return gas;
	}
	
	protected Collection<GrantedResource> findGrantedResourcesOfUser(Long principalId,List<Role> roles ) {
		//fetch granted resources assigned to current user directly.
  		StringBuffer sb = new StringBuffer();
		sb.append("select "+JPAUtils.genEntityCols(ResourceAssignment.class, "b", null)
		         +","+JPAUtils.genEntityCols(ResourceDefination.class, "a", "resourceDef")
				+ " from sec_resource a, sec_resource_assignment b, sec_sid c, sec_principal d "
				+ " where a.id = b.RESOURCE_DEF_ID  "
				+ " and  b.SID_ID = c.id "
				+"  and c.type = 0 "
				+ " and c.SID = d.id "
				+ " and d.id =  "+principalId);
		List<ResourceAssignment> resources = this.sqlToModelService.executeNativeQuery(sb.toString(), null, ResourceAssignment.class);  
		
		//fetch granted resources assigned to roles belongs to current user. role tree is taken into consideration.
		
		if (CommonUtil.isNotNull(roles)) {
			List<Long> roleIdSet = new ArrayList<Long>();
			for(Role role : roles) {
				roleIdSet.add(role.getId());
			}
			sb = new StringBuffer();
			sb.append("select "
				        +   JPAUtils.genEntityCols(ResourceAssignment.class, "b", null)
			        +","+JPAUtils.genEntityCols(ResourceDefination.class, "a", "resourceDef")
					+ " from sec_resource a, sec_resource_assignment b, sec_sid c "
					+ " where a.id = b.RESOURCE_DEF_ID  "
					+ "   and  b.SID_ID = c.id "
					+"    and c.type = 1 "
					+ "   and "+CommonUtil.genIdInCause("c.SID", roleIdSet));
			List<ResourceAssignment> list =  this.sqlToModelService.executeNativeQuery(sb.toString(), null, ResourceAssignment.class);
			resources.addAll(list);
			
		}
		
		
		Map<ResourceAssignmentId,GrantedResource> resourceMap = new HashMap<ResourceAssignmentId,GrantedResource>();
		for(ResourceAssignment ra : resources) {
			ResourceAssignmentId id = new ResourceAssignmentId(ra.getResourceDef().getCode(),ra.getActionMask());
			GrantedResource gr = resourceMap.get(id);
			if(gr == null) {
				gr = new GrantedResource(ra.getResourceDef().getCode(),new HashSet<String>(),ra.getActionMask());
				resourceMap.put(id, gr);
			}
			gr.getIdSet().add(ra.getResourceId());
		}
		return resourceMap.values();
	}
	
	static class ResourceAssignmentId {
		String resourceName;
		int mask;
		
		public ResourceAssignmentId() {}
		
		public ResourceAssignmentId(String resourceName,int mask) {
			this.resourceName = resourceName;
			this.mask = mask;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj != null && obj instanceof ResourceAssignmentId) {
				ResourceAssignmentId id = (ResourceAssignmentId)obj;
				if(resourceName.equals(id.resourceName) && mask == id.mask) {
					return true;
				}
			}
			return false;
		}
		@Override
		public int hashCode() {
			return (resourceName+mask).hashCode();
		}
		
		
	}


	protected List<Role> findRolesOfPrincipalId(Long principalId) {
		StringBuffer sb = new StringBuffer();
		sb.append("select a.* from sec_role a, sec_role_assignment b, sec_sid c, sec_principal d "
				+ " where a.id = b.ROLE_ID  and  b.SID_ID = c.id and c.SID = d.id and d.id = ? ");
		return jdbcTemplate.query(sb.toString(), new Object[] { principalId },
				new BeanPropertyRowMapper<Role>(Role.class));
	}

	protected PermissionCollection findPermissionsOfUser(List<Role> roles, Long principalId) {
		List<PermissionVo> permissionVo = new ArrayList<PermissionVo>();
//		for (Role role : roles) {
//			StringBuffer sb = new StringBuffer();
////			sb.append(
////					" SELECT rp.mask, p.`code` FROM sec_role_permission rp, sec_role r, sec_permission p WHERE rp.role_id=r.id AND rp.permission_id=p.id AND r.id=? ");
//			List<PermissionVo> pas = jdbcTemplate.query(sb.toString(), new Object[] { role.getId() },
//					new BeanPropertyRowMapper<PermissionVo>(PermissionVo.class));
//			permissionVo.addAll(pas);
//		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("  select pms.id id,      "+ 
				"			        pms.ACTION_MASK  mask,  "+ 
				"			        pms.VERSION   version,  "+ 
				"			        sec.ID     ,            " + 
				"			        sec.SID     ,           " + 
				"			        pm.ID  ,                " + 
				"			        pm.code   ,             " + 
				"			        pm.name   ,             " + 
				"			        pm.DESCRIPTION  ,       " + 
				"			        pm.ACTIONS  ,           " + 
				"			        pm.REALM_ID             " + 
				"			   from   sec_permission_assignment pms ,   " + 
				"			          sec_sid sec,                      " + 
				"			          sec_role r,                       " + 
				"			          sec_permission pm                 " + 
				"			 where pms.SID_ID = sec.id                  " + 
				"			   and pms.PERMISSION_ID = pm.ID            " + 
				"			   and  sec.sid = r.id                      " + 
				"			   and sec.type = 1 " + 
				"         and sec.sid  in (" + 
				" select a.id from sec_role a, sec_role_assignment b, sec_sid c, sec_principal d "
				+ "       where a.id = b.ROLE_ID  and  b.SID_ID = c.id and c.SID = d.id and d.id =?   )" + 
				"                       ");
		
		List<PermissionVo> pas = jdbcTemplate.query(sb.toString(), new Object[] { principalId },
				new BeanPropertyRowMapper<PermissionVo>(PermissionVo.class));
		permissionVo.addAll(pas);
		
		Map<String, MaskPermission> permMap = new HashMap<String, MaskPermission>();
		for (PermissionVo pv : permissionVo) {
			MaskPermission p = permMap.get(pv.getCode());
			if (p == null) {
				permMap.put(pv.getCode(), new MaskPermission(pv.getCode(), pv.getMask(), pv.getActions()));
			} else {
				p.merge(pv.getMask());
				p.setPermissonActions(pv.getActions());
			}
		}
		Permissions ps = new Permissions();
		for (MaskPermission p : permMap.values()) {
			ps.add(p);
		}
		return ps;
	}

}
