package com.yq.xcode.security.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yq.xcode.common.bean.ResourceInstance;
import com.yq.xcode.common.bean.Result;
import com.yq.xcode.common.springdata.AggregatePageImpl;
import com.yq.xcode.common.springdata.HPageRequest;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.ResultUtil;
import com.yq.xcode.security.bean.PrincipalBean;
import com.yq.xcode.security.bean.PrincipalPwd;
import com.yq.xcode.security.bean.ResourceAssignmentBean;
import com.yq.xcode.security.bean.RoleAssignmentBean;
import com.yq.xcode.security.criteria.PrincipalCriteria;
import com.yq.xcode.security.criteria.ResourceCriteria;
import com.yq.xcode.security.entity.ResourceDefination;
import com.yq.xcode.security.entity.SecPrincipal;
import com.yq.xcode.security.resourceproviders.ResourceConstants;
import com.yq.xcode.security.service.AuthenticationService;
import com.yq.xcode.security.service.PrincipalService;
import com.yq.xcode.security.service.ResourceService;
import com.yq.xcode.security.utils.YqSecurityUtils;

@RestController
@RequestMapping("/admin/principal")
// @PreAuthorize("hasPermission('SEC_PRINCIPAL', 1)")
public class PrincipalController {

    @Autowired
    private PrincipalService principalService;

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private ResourceService resourceService;

    /**
     * 获取用户列表
     */
    @PostMapping("/page")
    public Result<Page<PrincipalBean>> findPrincipal(@RequestBody PrincipalCriteria criteria) {
        Page<SecPrincipal> page = principalService.findPrincipalPage(criteria);
        Page<PrincipalBean> newPage = new AggregatePageImpl<PrincipalBean>(toBeans(page.getContent()),
                new HPageRequest(criteria), page.getTotalElements());
        return ResultUtil.ok(newPage);
    }

    @GetMapping("/resourceList")
    public Result<List<ResourceDefination>> findResource() {
        ResourceCriteria resourceCriteria = new ResourceCriteria();
        List<ResourceDefination> findAll = resourceService.findAll(resourceCriteria);
        return ResultUtil.ok(findAll);
    }

    private List<PrincipalBean> toBeans(List<SecPrincipal> users) {
        List<PrincipalBean> beans = new ArrayList<PrincipalBean>();
        if (users == null) {
            return beans;
        }
        ResourceCriteria resourceCriteria = new ResourceCriteria();
        List<ResourceDefination> findAll = resourceService.findAll(resourceCriteria);
        Map<String, Map<String, ResourceInstance>> resourceData = new HashMap<String, Map<String, ResourceInstance>>();
        for (ResourceDefination resourceDefination : findAll) {
            List<ResourceInstance> resourceList = resourceService
                    .findResourceInstanceList(resourceDefination.getCode());
            resourceData.put(resourceDefination.getCode(), CommonUtil.ListToMap(resourceList, "id"));
        }
        for (SecPrincipal user : users) {
            PrincipalBean bean = new PrincipalBean(user);
            bean.setRoles(principalService.findRoleByPrincipal(user.getId()));

            Map<String, String> resourceNameMap = new HashMap<String, String>();
            for (ResourceDefination resourceDefination : findAll) {
                List<ResourceInstance> instanceList = principalService
                        .findResourceInstanceByPrincipal(resourceDefination.getCode(), user.getId());
                if (CommonUtil.isNotNull(instanceList)) {
                    for (ResourceInstance resourceInstance : instanceList) {
                        Map<String, ResourceInstance> map = resourceData.get(resourceDefination.getCode());
                        ResourceInstance nameString = map.get(resourceInstance.getId());
                        this.putToresourceNameMap(resourceDefination.getCode(), nameString.getName(), resourceNameMap);
                    }
                }
            }
            bean.setResourceNameMap(resourceNameMap);
            beans.add(bean);
        }
        return beans;
    }

    private void putToresourceNameMap(String resourceName, String name, Map<String, String> resourceNameMap) {
        if (CommonUtil.isNull(name)) {
            return;
        }
        String names = resourceNameMap.get(resourceName);
        if (names == null) {
            resourceNameMap.put(resourceName, " 【" + name + "】");
        } else {
            names = names + "【" + name + "】";
            resourceNameMap.put(resourceName, names);
        }
    }

    /**
     * 保存用户
     */
    @PostMapping("/save")
    public Result<?> savePrincipal(@RequestBody SecPrincipal principal) {
        principalService.savePrincipal(principal);
        return ResultUtil.ok();
    }

    @GetMapping("/{id}")
    public Result<SecPrincipal> getPrincipal(@PathVariable Long id) {
        SecPrincipal secPrincipal = principalService.getPrincipalById(id);
        return ResultUtil.ok(secPrincipal);
    }

    @GetMapping("/roleAssignment/{id}")
    public Result<List<RoleAssignmentBean>> getRoleAssignmentListById(@PathVariable Long id) {
        List<RoleAssignmentBean> beanList = principalService.findRoleAssignmentBean(id);
        return ResultUtil.ok(beanList);
    }

    @GetMapping("/resourceAssignment/{id}/{resourceName}")
    public Result<List<ResourceAssignmentBean>> getResourceAssignmentListById(@PathVariable Long id,
                                                                              @PathVariable(value = "resourceName", required = false) String resourceName) {
        List<ResourceAssignmentBean> beanList = principalService.findResourceAssignmentBean(resourceName, id);
        return ResultUtil.ok(beanList);
    }

    @PostMapping("/roleAssignment/save")
    public Result<String> saveRoleAssignmentList(@RequestBody List<RoleAssignmentBean> beanList,
                                                 @RequestParam(required = false) Long principalId) {
        principalService.saveRoleAssignmentBean(beanList, principalId);
        return ResultUtil.ok("保存成功");
    }

    @PostMapping("/resourceAssignment/save/{resourceName}")
    public Result<String> saveResourceAssignmentList(@PathVariable String resourceName,
                                                     @RequestBody List<ResourceAssignmentBean> beanList,
                                                     @RequestParam(required = false) Long principalId) {
        principalService.saveResourceAssignmentBean(resourceName, beanList, principalId);
        return ResultUtil.ok("保存成功");
    }

    @PostMapping("/pwd")
    public Result<?> savePrincipalPwd(@RequestBody PrincipalPwd pwd) {
        try {
            if (!pwd.getNewpwd().equals(pwd.getEqpwd())) {
                return ResultUtil.error("新密码不一致，请重试输入");
            }
            authenticationService.checkPassword(YqSecurityUtils.getUser().getUsername(), pwd.getOldpwd());
            SecPrincipal principal = principalService.getPrincipalByUsername(YqSecurityUtils.getUser().getUsername());
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            final String rawPassword = pwd.getNewpwd();
            principal.setPassword(encoder.encode(rawPassword));
            principalService.savePrincipal(principal);
            return ResultUtil.ok();
        } catch (BadCredentialsException e) {
            return ResultUtil.error("账号密码有误");
        }
    }

    /**
     * 删除用户
     */
    @PostMapping("/delete")
    public Result<?> delPrincipal(Long id) {
        principalService.deletePrincipal(id);
        return ResultUtil.ok();
    }
}
