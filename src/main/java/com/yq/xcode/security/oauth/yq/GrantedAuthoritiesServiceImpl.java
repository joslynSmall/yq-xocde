package com.yq.xcode.security.oauth.yq;

import java.security.AllPermission;
import java.security.Permissions;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.yq.xcode.security.bean.GrantedPermissions;

public class GrantedAuthoritiesServiceImpl implements GrantedAuthoritiesService {

	@Override
	public List<GrantedAuthority> findGrantedAuthorities(String username, String realmCode) {
		// TODO Auto-generated method stub
		List<GrantedAuthority> gas = new ArrayList<GrantedAuthority>();
		List<String> roles = new ArrayList<String>();
		roles.add("ADMIN");
		roles.add("MANAGER");
		for (String roleName : roles) {
			gas.add(new SimpleGrantedAuthority("ROLE_"+roleName));
		}
		//init from role list
//		gas.add(new GrantedPermissions(findPermissionsOfUser(username,roles,realmCode)));
		AllPermission pc = new AllPermission();
		Permissions ps = new Permissions();
		ps.add(pc);
		gas.add(new GrantedPermissions(ps));
		return gas;
	}
	
	@Override
	public GrantedPermissions findPermissionsOfRole(String roleCode, String realmCode) {
		return null;
	}

//	@PersistenceContext protected EntityManager em;
//
//	@Override
//	public List<GrantedAuthority> findGrantedAuthorities(String username,String realmCode,RoleFilter roleFilter) {
//		List<GrantedAuthority> gas = new ArrayList<GrantedAuthority>();
//		List<Role> roles = findAssignedRolesOfSid(username,SecurityId.Type.PRINCIPAL,realmCode,roleFilter);
//		for(Role role : roles) {
//			gas.add(new SimpleGrantedAuthority("ROLE_"+role.getName()));
//		}
//		gas.add(new GrantedPermissions(findPermissionsOfUser(username,roles,realmCode)));
//		gas.addAll(this.findGrantedResourcesOfUser(username,roles, realmCode));
//		return gas;
//	}
//	
//	
//	
//	@Override
//	public GrantedPermissions findPermissionsOfRole(String roleCode,
//			String realmCode) {
//		List<Role> roles= em.createQuery("from Role  where name=?1 and realm.code=?2 ")
//			    .setParameter(1, roleCode)
//				.setParameter(2, realmCode).getResultList();
//	
//		List<PermissionAssignment> pas=new ArrayList<PermissionAssignment>();
//		for(Role role : roles) {
//			List<PermissionAssignment> rolePermissions = em.createQuery("from PermissionAssignment pa left join fetch pa.permissionDef where pa.securityId.sid=?1 and pa.securityId.type=?2 and pa.permissionDef.realm.code=?3")
//			.setParameter(1, role.getId()+"")
//			.setParameter(2, SecurityId.Type.ROLE)
//			.setParameter(3, realmCode).getResultList();
//			pas.addAll(rolePermissions);
//		}
//		Map<String,MaskPermission> permMap = new HashMap<String,MaskPermission>();
//		for(PermissionAssignment pa : pas) {
//			MaskPermission p = permMap.get(pa.getPermissionCode());
//			if(p == null) {
//				permMap.put(pa.getPermissionCode(), new MaskPermission(pa.getPermissionCode(),pa.getMask()));
//			}else {
//				p.merge(pa.getMask());
//			}
//		}
//		Permissions ps = new Permissions();
//		for(MaskPermission p : permMap.values()) {
//			ps.add(p);
//		}
//		return new GrantedPermissions(ps);
//	}
//
//
//
//	protected List<Role> findAssignedRolesOfSid(String sid,SecurityId.Type type,String realmCode,RoleFilter roleFilter) {
//		List<RoleAssignment> ras = em.createQuery("from RoleAssignment ra left join fetch ra.role where ra.securityId.sid=?1 and ra.securityId.type=?2 and ra.role.active=true and ra.role.realm.code=?3")
//		.setParameter(1, sid)
//		.setParameter(2, type)
//		.setParameter(3, realmCode).getResultList();
//		List<Role> roles = new ArrayList<Role>();
//		for(RoleAssignment ra : ras) {
//			if(roleFilter == null || roleFilter.filter(ra.getRole())) {
//				roles.add(ra.getRole());
//			}
//		}
//		for(Role role : roles.toArray(new Role[roles.size()])) {
//			roles.addAll(findAssignedRolesOfSid(role.getId()+"",SecurityId.Type.ROLE,realmCode,roleFilter));
//		}
//		return roles;
//	}
//	
//	
//	protected PermissionCollection findPermissionsOfUser(String username,List<Role> roles,String realmCode) {
//		List<PermissionAssignment> pas = em.createQuery("from PermissionAssignment pa left join fetch pa.permissionDef where pa.securityId.sid=?1 and pa.securityId.type=?2 and pa.permissionDef.realm.code=?3")
//				.setParameter(1, username)
//				.setParameter(2, SecurityId.Type.PRINCIPAL)
//				.setParameter(3, realmCode).getResultList();
//		for(Role role : roles) {
//			List<PermissionAssignment> rolePermissions = em.createQuery("from PermissionAssignment pa left join fetch pa.permissionDef where pa.securityId.sid=?1 and pa.securityId.type=?2 and pa.permissionDef.realm.code=?3")
//			.setParameter(1, role.getId()+"")
//			.setParameter(2, SecurityId.Type.ROLE)
//			.setParameter(3, realmCode).getResultList();
//			pas.addAll(rolePermissions);
//		}
//		Map<String,MaskPermission> permMap = new HashMap<String,MaskPermission>();
//		boolean hasAllPermission = false;
//		for(PermissionAssignment pa : pas) {
//			if("all".equalsIgnoreCase(pa.getPermissionCode())) {
//				hasAllPermission = true;
//				continue;
//			}
//			MaskPermission p = permMap.get(pa.getPermissionCode());
//			if(p == null) {
//				permMap.put(pa.getPermissionCode(), new MaskPermission(pa.getPermissionCode(),pa.getMask()));
//			}else {
//				p.merge(pa.getMask());
//			}
//		}
//		Permissions ps = new Permissions();
//		if(hasAllPermission) {
//			ps.add(new AllPermission());
//		}
//		for(MaskPermission p : permMap.values()) {
//			ps.add(p);
//		}
//		return ps;
//	}
//	
//	protected Collection<GrantedResource> findGrantedResourcesOfUser(String username,List<Role> roles,String realmCode) {
//		//fetch granted resources assigned to current user directly.
//		List<ResourceAssignment> resources = em.createQuery("from ResourceAssignment ra left join fetch ra.resourceDef where ra.securityId.sid=?1 and ra.securityId.type=?2 and ra.resourceDef.realm.code=?3")
//				.setParameter(1, username)
//				.setParameter(2, SecurityId.Type.PRINCIPAL)
//				.setParameter(3, realmCode).getResultList();
//		
//		//fetch granted resources assigned to roles belongs to current user. role tree is taken into consideration.
//		for(Role role : roles) {
//			List<ResourceAssignment> list = em.createQuery("from ResourceAssignment ra left join fetch ra.resourceDef where ra.securityId.sid=?1 and ra.securityId.type=?2 and ra.resourceDef.realm.code=?3")
//					.setParameter(1, role.getId()+"")
//					.setParameter(2, SecurityId.Type.ROLE)
//					.setParameter(3, realmCode).getResultList();
//			resources.addAll(list);
//		}
//		Map<ResourceAssignmentId,GrantedResource> resourceMap = new HashMap<ResourceAssignmentId,GrantedResource>();
//		for(ResourceAssignment ra : resources) {
//			ResourceAssignmentId id = new ResourceAssignmentId(ra.getResourceDef().getName(),ra.getMask());
//			GrantedResource gr = resourceMap.get(id);
//			if(gr == null) {
//				gr = new GrantedResource(ra.getResourceDef().getName(),new HashSet<String>(),ra.getMask());
//				resourceMap.put(id, gr);
//			}
//			gr.getIdSet().add(ra.getResourceId());
//		}
//		return resourceMap.values();
//	}
//	
//	static class ResourceAssignmentId {
//		String resourceName;
//		int mask;
//		
//		public ResourceAssignmentId() {}
//		
//		public ResourceAssignmentId(String resourceName,int mask) {
//			this.resourceName = resourceName;
//			this.mask = mask;
//		}
//		
//		@Override
//		public boolean equals(Object obj) {
//			if(obj != null && obj instanceof ResourceAssignmentId) {
//				ResourceAssignmentId id = (ResourceAssignmentId)obj;
//				if(resourceName.equals(id.resourceName) && mask == id.mask) {
//					return true;
//				}
//			}
//			return false;
//		}
//		@Override
//		public int hashCode() {
//			return (resourceName+mask).hashCode();
//		}
//		
//		
//	}

}
