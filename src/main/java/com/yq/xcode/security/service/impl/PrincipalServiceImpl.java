package com.yq.xcode.security.service.impl;

import com.yq.xcode.common.bean.ResourceInstance;
import com.yq.xcode.common.bean.ResourceView;
import com.yq.xcode.common.service.SqlToModelService;
import com.yq.xcode.common.service.impl.YqJpaDataAccessObject;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.JPAUtils;
import com.yq.xcode.common.utils.UpdateUtil;
import com.yq.xcode.security.bean.PrincipalBean;
import com.yq.xcode.security.bean.ResourceAssignmentBean;
import com.yq.xcode.security.bean.RoleAssignmentBean;
import com.yq.xcode.security.criteria.PrincipalCriteria;
import com.yq.xcode.security.entity.*;
import com.yq.xcode.security.service.PrincipalService;
import com.yq.xcode.security.service.ResourceService;
import com.yq.xcode.security.service.RoleService;
import com.yq.xcode.security.service.SecurityIdService;
import com.yq.xcode.security.utils.YqSecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class PrincipalServiceImpl extends YqJpaDataAccessObject implements PrincipalService {


    @Autowired
    private RoleService roleService;
    @Autowired
    private SecurityIdService securityIdService;
    @Autowired
    private SqlToModelService sqlToModelService;

    @Autowired
    private ResourceService resourceService;

    @Override
    public Page<SecPrincipal> findPrincipalPage(PrincipalCriteria criteria) {

        String organizationId = YqSecurityUtils.getLoginUser().getOrganizationId();
        StringBuffer query = new StringBuffer();
        query.append("SELECT ")
                .append(JPAUtils.genEntityCols(SecPrincipal.class, "a", null))
                .append(" FROM sec_principal a")
                .append(" WHERE 1=1 ")
                .append(" AND a.is_active = 1 ");
        if (CommonUtil.isNull(criteria.getEnterpriseId())){
            query.append(" AND a.organization_id = ").append(organizationId);
        }
        Page<SecPrincipal> page =
                sqlToModelService.executeNativeQueryForPage(query.toString(),
                        "a.name", null, criteria, SecPrincipal.class);
        return page;
    }

    @Override
    public SecPrincipal getPrincipalById(Long id) {
        return this.getById(SecPrincipal.class, id);
    }

    @Override
    public void savePrincipal(SecPrincipal principal) {

        this.initPassword(principal);
        if (principal.getId() != null) {
            this.updateProperties(principal, UpdateUtil.getNonNullProperties(principal));
        } else {
            this.save(principal);
        }
    }

    /*
     * 用来适配旧系统，传入 手工加密的密码
     */
    @Override
    public void savePrincipal(SecPrincipal principal, String password) {
        if (principal.getId() != null) {
            this.updateProperties(principal, UpdateUtil.getNonNullProperties(principal));
        } else {
            this.save(principal);
        }
    }

    @Override
    public void deletePrincipal(Long id) {
        this.deleteById(SecPrincipal.class, id);
    }

    private SecPrincipal findByName(String name) {
        if (CommonUtil.isNotNull(name)) {
            name = name.trim();
        }
        @SuppressWarnings("unchecked")
        List<SecPrincipal> list = this.find(" from SecPrincipal a "
                        + " where (a.aliasName=? or name = ? or mobilePhone = ?) and a.active = 1 ",
                name, name, name);
        if (CommonUtil.isNull(list)) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public SecPrincipal getPrincipalByUsername(String username) {
        return this.findByName(username);
    }

    @Override
    public void savePrincipal(PrincipalBean bean) {
        SecPrincipal principal = bean.getPrincipal();
        this.initPassword(principal);
        this.save(principal);

    }

    /**
     * 初始化密码
     * 当前年月日 yyyymmdd
     *
     * @param principal
     */
    private void initPassword(SecPrincipal principal) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (CommonUtil.isNull(principal.getPassword())) {
            if (CommonUtil.isNotNull(principal.getMobilePhone())) {
                principal.setPassword(encoder.encode(principal.getMobilePhone().substring(5)));
            } else {
                principal.setPassword(encoder.encode("123456"));
            }
        } else {
            principal.setPassword(encoder.encode(principal.getPassword()));
        }

    }

    @Override
    public List<SecPrincipal> findUserListByResource(List<ResourceView> allResList) {
        List<Map<String, SecPrincipal>> mapList = new ArrayList<Map<String, SecPrincipal>>();
        Map<String, SecPrincipal> allUserMap = new HashMap<String, SecPrincipal>();
        List<SecPrincipal> retUserList = new ArrayList<SecPrincipal>();
        for (ResourceView res : allResList) {
            if (CommonUtil.isNull(res.getResourceValueList())) {
                return null;
            }
            Map<String, SecPrincipal> resUserMap = this.getUserMap(res);
            if (CommonUtil.isNull(resUserMap)) {
                return null;
            }
            mapList.add(resUserMap);
            allUserMap.putAll(resUserMap);
        }
        for (String userId : allUserMap.keySet()) {
            boolean allFound = true;

            for (Map<String, SecPrincipal> tmp : mapList) {
                if (!tmp.keySet().contains(userId)) {
                    allFound = false;
                    break;
                }
            }
            if (allFound) {
                retUserList.add(allUserMap.get(userId));
            }

        }
        return retUserList;
    }

    private Map<String, SecPrincipal> getUserMap(ResourceView rv) {
        Map<String, SecPrincipal> userMap = new HashMap<String, SecPrincipal>();
        if (CommonUtil.isNull(rv.getResourceValueList())) {
            return userMap;
        }
        String query = " select " + JPAUtils.genEntityCols(SecPrincipal.class, "u", null)
                + " from sec_resource r, sec_resource_assignment rs , sec_sid sid, sec_principal u  "
                + " where r.id = rs.RESOURCE_DEF_ID "
                + " and sid.id = rs.sid_id "
                + " and sid.type = 0 "
                //+ " and sid.sid = u.user_code "
                + " and sid.sid = u.id "
                + " and r.name = '" + rv.getResourceName() + "' "
                + " and " + CommonUtil.genInStrByList("rs.RESOURCE_ID", rv.getResourceValueList());
        List<SecPrincipal> list = this.sqlToModelService.executeNativeQuery(query, null, SecPrincipal.class);
        if (!CommonUtil.isNull(list)) {
            for (SecPrincipal u : list) {
                userMap.put(u.getName(), u);
            }
        }
        return userMap;
    }

    @Override
    public List<Role> findRoleByPrincipal(Long principalId) {
        StringBuffer query = new StringBuffer();
        query.append("SELECT ")
                .append(JPAUtils.genEntityCols(Role.class, "a", null))
                .append(" FROM sec_role a,sec_role_assignment b,sec_sid c ")
                .append(" WHERE a.id = b.role_id and b.sid_id = c.id ")
                .append(" and c.sid = " + principalId);
        List<Role> list = sqlToModelService.executeNativeQuery(query.toString(), null, Role.class);
        return list;

    }

    @Override
    public List<ResourceInstance> findResourceInstanceByPrincipal(String resourceName, Long principalId) {
        StringBuffer query = new StringBuffer();
        query.append("SELECT b.resource_id id ")
                .append(" FROM sec_resource a,sec_resource_assignment b,sec_sid c   ")
                .append(" WHERE a.id = b.resource_def_id and b.sid_id = c.id  ")
                .append(" and a.code =  '" + resourceName + "' and c.sid = " + principalId);
        List<ResourceInstance> list =
                sqlToModelService.executeNativeQuery(query.toString(), null, ResourceInstance.class);
        return list;

    }

    @Override
    public List<RoleAssignmentBean> findRoleAssignmentBean(Long principalId) {
        List<RoleAssignmentBean> beanList = new ArrayList<RoleAssignmentBean>();
        List<Role> principalRoleList = this.findRoleByPrincipal(principalId);
        Set<Long> roleSet = new HashSet<Long>();
        for (Role role : principalRoleList) {
            roleSet.add(role.getId());
        }
        List<Role> roleList = roleService.findRoleList();
        for (Role role : roleList) {
            RoleAssignmentBean bean = new RoleAssignmentBean();
            bean.setRole(role);
            bean.setPrincipalId(principalId);
            bean.setSelected(roleSet.contains(role.getId()));
            beanList.add(bean);
        }
        return beanList;
    }

    private SecurityId initGetSecurityId(Long principalId) {
        SecurityId securityId =
                securityIdService.getSecurityIdBySId(principalId, SecurityId.Type.PRINCIPAL);
        if (securityId == null) {
            securityId = new SecurityId();
            securityId.setSid(principalId);
            securityId.setType(SecurityId.Type.PRINCIPAL);
            securityId.setRealmId(0L);
            securityIdService.saveSecurityId(securityId);
        }
        return securityId;
    }

    @Override
    public void saveRoleAssignmentBean(List<RoleAssignmentBean> beanList) {
        if (CommonUtil.isNull(beanList)) {
            return;
        }
        SecurityId securityId = initGetSecurityId(beanList.get(0).getPrincipalId());
        List<RoleAssignment> roleAssignments = roleService.getRoleAssignmentBySId(securityId.getId());
        if (CommonUtil.isNotNull(roleAssignments)) {
            this.delete(roleAssignments);
        }
        for (RoleAssignmentBean bean : beanList) {
            RoleAssignment roleAssignment = new RoleAssignment();
            roleAssignment.setRoleId(bean.getRole().getId());
            roleAssignment.setSidId(securityId.getId());
            this.save(roleAssignment);
        }
    }

    @Override
    public List<ResourceAssignmentBean> findResourceAssignmentBean(String resourceName, Long principalId) {
        List<ResourceAssignmentBean> beanList = new ArrayList<ResourceAssignmentBean>();
        List<ResourceInstance> instanceList =
                this.findResourceInstanceByPrincipal(resourceName, principalId);
        Set<String> resourceSet = new HashSet<String>();
        for (ResourceInstance resourceInstance : instanceList) {
            resourceSet.add(resourceInstance.getId());
        }
        List<ResourceInstance> resourceList = resourceService.findResourceInstanceList(resourceName);
        for (ResourceInstance resourceInstance : resourceList) {
            ResourceAssignmentBean bean = new ResourceAssignmentBean();
            bean.setResourceInstance(resourceInstance);
            bean.setPrincipalId(principalId);
            bean.setSelected(resourceSet.contains(resourceInstance.getId()));
            beanList.add(bean);
        }
        return beanList;
    }

    @Override
    public void saveResourceAssignmentBean(String resourceName, List<ResourceAssignmentBean> beanList) {
        if (CommonUtil.isNull(beanList)) {
            return;
        }
        SecurityId securityId = initGetSecurityId(beanList.get(0).getPrincipalId());
        ResourceDefination resourceDefination = resourceService.getResourceDefination(resourceName);
        List<ResourceAssignment> resourceAssignments =
                resourceService.getResourceAssignmentBySId(resourceDefination.getId(), securityId.getId());
        if (CommonUtil.isNotNull(resourceAssignments)) {
            this.delete(resourceAssignments);
        }
        for (ResourceAssignmentBean bean : beanList) {
            ResourceAssignment resourceAssignment = new ResourceAssignment();
            resourceAssignment.setResourceDefId(resourceDefination.getId());
            resourceAssignment.setSidId(securityId.getId());
            resourceAssignment.setResourceId(bean.getResourceInstance().getId());
            this.save(resourceAssignment);
        }

    }

}
