package com.yq.xcode.common.service.impl;

 
import org.springframework.stereotype.Service;

@Service("YqPrincipalService")
public class YqPrincipalServiceImpl extends YqJpaDataAccessObject   {

//	@Autowired	private PrincipalMaintenanceService principalMaintenanceService;
//	@Autowired	private SqlToModelService sqlToModelService;
//	@SuppressWarnings("unchecked")
//	@Override
//	public Page<Principal> findPrincipals(Pageable pr, PrincipalQueryCriteria2 qc) {
//		JpaCriteria jpaCriteria = new JpaCriteria("DefaultPrincipal a");
//		if(qc.getOrganizationId() > 0) {
//			jpaCriteria.add(CriterionUtils.equals("a.organizationId", qc.getOrganizationId(), true));
//		}
//		if(StringUtils.hasText(qc.getKeyword())) {
//			jpaCriteria.add(new OrCriterion(
//				CriterionUtils.contains("a.name", qc.getKeyword(), true),
//				CriterionUtils.contains("a.alias", qc.getKeyword(), true),
//				CriterionUtils.contains("a.displayName", qc.getKeyword(), true),
//				CriterionUtils.contains("a.mobilePhone", qc.getKeyword(), true),
//				CriterionUtils.contains("a.email", qc.getKeyword(), true)));
//		}
// 		
//		jpaCriteria.add(CriterionUtils.notEquals("ifnull(a.text2,'0')", "1", false)); // 删除的不显示
////		jpaCriteria.add(CriterionUtils.notEquals("ifnull(a.text1,'0')", YqConstants.USER_CATEGORY_IS_VENDOR, false)); // 供应商不显示
//		
//		if(StringUtils.hasText(qc.getRolekeyword())) {
//			jpaCriteria.add(new LiteralCriterion("a.name in (select sid.sid from SecurityId sid,RoleAssignment ra where sid.id=ra.securityId.id"));
//			OrCriterion orCriterion = new OrCriterion(
//					CriterionUtils.contains("ra.role.name", qc.getRolekeyword(), true),
//					CriterionUtils.contains("ra.role.displayName", qc.getRolekeyword(), true));
//			jpaCriteria.add(orCriterion);
//			jpaCriteria.add(new LiteralCriterion(")",true));
//		}
//		return JpaQueryUtils.query(em, jpaCriteria, pr);
//	}
//	@Override
//	public Principal createOrUpdateStaff(DefaultPrincipal dp) {
//		String mobilePhone = dp.getMobilePhone();
////		if(mobilePhone!=null&&!"".equals(mobilePhone)){
//////			dp.setPassword(mobilePhone.substring(mobilePhone.length()-6,mobilePhone.length()));
////			dp.setPassword("123456");
////		}else{
////			dp.setPassword("123456");
////		}
////		dp.setText1(YqConstants.USER_CATEGORY_IS_STAFF);
//		return principalMaintenanceService.createOrUpdatePrincipal(dp);
//	}
// 
//	
//	public DefaultPrincipal getPrincipalByCode(String code) {
//		List<DefaultPrincipal> list = this.find("from DefaultPrincipal where name=?1",code);
//		if (CommonUtil.isNotNull(list)) {
//			return list.get(0);
//		}
//		return null;
//	}
//	
//	public DefaultPrincipal getPrincipalByAliasName(String aliasName) {
//		String query = "select name 'name', alias_name 'alias', display_name 'displayName' from sec_principal where alias_name = '"+JPAUtils.toPar(aliasName)+"' " ;
//		List<DefaultPrincipal> list = this.sqlToModelService.executeNativeQuery(query, null, DefaultPrincipal.class);
//		if (CommonUtil.isNotNull(list)) {
//			return list.get(0);
//		}
//		return null;
//	}
//	
//	public DefaultPrincipal getPrincipalByName(String name) {
//		String query = "select sp.* from sec_principal sp where name = '"+JPAUtils.toPar(name)+"' " ;
//		List<DefaultPrincipal> list = this.sqlToModelService.executeNativeQuery(query, null, DefaultPrincipal.class);
//		if (CommonUtil.isNotNull(list)) {
//			return list.get(0);
//		}
//		return null;
//	}
//	
//	@Override
//	public Map<String, List<PermissionAssignment>> findRolePermission(
//			List<Role> roles) {
//		Map<String, List<PermissionAssignment>> map = new HashMap<String, List<PermissionAssignment>>();
//		if (CommonUtil.isNull(roles)) {
//			return map;
//		}
//		String ids = "";
//		for (Role r : roles) {
//			ids = ids+","+r.getId();
//		}
//		ids = ids.substring(1);
//		String query = " select pms.id id,                                                                                   "
//				+"        pms.ACTION_MASK  mask,                                                                       "
//				+"        pms.VERSION   version,                                                                       "
//				+"        sec.ID    \"securityId.id\",                                                             "
//				+"        sec.SID    \"securityId.sid\",                                                             "
//				+"        pm.ID  \"permissionDef.id\",                                                                 "
//				+"        pm.code  \"permissionDef.code\",                                                             "
//				+"        pm.name  \"permissionDef.name\",                                                             "
//				+"        pm.DESCRIPTION \"permissionDef.description\",                                                "
//				+"        pm.ACTIONS \"permissionDef.actionText\",                                                     "
//				+"        pm.REALM_ID \"permissionDef.realm.id\"                                                       "
//				+"   from   sec_permission_assignment pms ,                                                            "
//				+"          sec_sid sec,                                                                               "
//				+"          sec_role r,                                                                                "
//				+"          sec_permission pm                                                                          "
//				+" where pms.SID_ID = sec.id                                                                           "
//				+"   and pms.PERMISSION_ID = pm.ID                                                                     "
//				+"   and  sec.sid = r.id                                                                               "
//				+"   and sec.type = 1                                                                                  "
//				+"   and sec.realm_id = r.realm_id                                                                     "
//				+"   and  r.id in  ("+ids+")   ";
//		List<PermissionAssignment> list = this.sqlToModelService.executeNativeQuery(query, null, PermissionAssignment.class);
//		if (CommonUtil.isNotNull(list)) {
//			for (PermissionAssignment p : list) {
//				List<PermissionAssignment> tmp = map.get(p.getSecurityId().getSid());
//				if (tmp == null) {
//					tmp = new ArrayList<PermissionAssignment>();
//					map.put(p.getSecurityId().getSid(), tmp);
//				}
//				tmp.add(p);
//			}
//		}
//		return map;
//	}
//	@Override
//	public Map<String, List<Role>> findRolesByUsers(List<Principal> users) {
//		Map<String, List<Role>> map = new HashMap<String, List<Role>>();
//		if (CommonUtil.isNull(users)) {
//			return map;
//		}
//		String ids = "";
//		for (Principal r : users) {
//			ids = ids+",'"+r.getName()+"'";
//		}
//		ids = ids.substring(1);
//		String query = " select pms.id       id,                      "
//				+"        pms.VERSION  version,                 "
//				+"        sec.id       \"securityId.id\",       "
//				+"        sec.SID      \"securityId.sid\",      "
//				+"        r.ID           \"role.id\",           "
//				+"        r.VERSION      \"role.version\",      "
//				+"        r.IS_ACTIVE    \"role.active\",       "
//				+"        r.`NAME`       \"role.name\" ,        "
//				+"        r.DISPLAY_NAME \"role.displayName\",  "
//				+"        r.DESCRIPTION  \"role.description\",  "
//				+"        r.TYPE         \"role.type\"          "
//				+"   from   sec_role_assignment pms ,           "
//				+"          sec_sid sec,                        "
//				+"          sec_role r                          "
//				+" where pms.SID_ID = sec.id                    "
//				+"   and pms.ROLE_ID = r.id                     "
//				+"   and sec.type = 0                           "
//				+"   and sec.realm_id = 1                       "
//				+"   and  sec.SID in  ("+ids+")   ";
//		List<RoleAssignment> list = this.sqlToModelService.executeNativeQuery(query, null, RoleAssignment.class);
//		if (CommonUtil.isNotNull(list)) {
//			for (RoleAssignment p : list) {
//				List<Role> tmp = map.get(p.getSecurityId().getSid());
//				if (tmp == null) {
//					tmp = new ArrayList<Role>();
//					map.put(p.getSecurityId().getSid(), tmp);
//				}
//				tmp.add(p.getRole());
//			}
//		}
//		return map;
//	}
//	@Override
//	public Map<String, List<ResourceAssignment>> findResourcesByUsers(
//			List<Principal> users) {
//		Map<String, List<ResourceAssignment>> map = new HashMap<String, List<ResourceAssignment>>();
//		if (CommonUtil.isNull(users)) {
//			return map;
//		}
//		String ids = "";
//		for (Principal r : users) {
//			ids = ids+",'"+r.getName()+"' ";
//		}
//		ids = ids.substring(1);
//		String query = " select pms.id id,                                  "
//				+"        pms.ACTION_MASK  mask,                      "
//				+"        pms.VERSION   version,                      "
//				+"        sec.id       \"securityId.id\",       "
//				+"        sec.SID      \"securityId.sid\",      "
//				+"        pms.RESOURCE_ID resourceId,                 "
//				+"        pm.ID  \"resourceDef.id\",                  "
//				+"        pm.name  \"resourceDef.name\",              "
//				+"        pm.DESCRIPTION \"resourceDef.description\", " 
//				+"        pm.ACTIONS \"resourceDef.actionText\",      "
//				+"        pm.REALM_ID \"resourceDef.realm.id\"        "
//				+"   from   sec_resource_assignment pms ,             "
//				+"          sec_sid sec,                              "
//				+"          sec_resource pm                           "
//				+" where pms.SID_ID = sec.id                          "
//				+"   and pms.RESOURCE_DEF_ID = pm.ID                  "
//				+"   and sec.type = 0                                 "
//				+"   and sec.realm_id = 1                             "
//				+"   and  sec.SID in  ("+ids+")   ";
//		List<ResourceAssignment> list = this.sqlToModelService.executeNativeQuery(query, null, ResourceAssignment.class);
//		if (CommonUtil.isNotNull(list)) {
//			for (ResourceAssignment p : list) {
//				List<ResourceAssignment> tmp = map.get(p.getSecurityId().getSid());
//				if (tmp == null) {
//					tmp = new ArrayList<ResourceAssignment>();
//					map.put(p.getSecurityId().getSid(), tmp);
//				}
//				tmp.add(p);
//			}
//		}
//		return map;
//	}
}
