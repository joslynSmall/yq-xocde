package com.yq.xcode.security.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yq.xcode.common.bean.ResourceInstance;
import com.yq.xcode.common.service.SqlToModelService;
import com.yq.xcode.common.service.impl.YqJpaDataAccessObject;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.JPAUtils;
import com.yq.xcode.security.entity.ResourceAssignment;
import com.yq.xcode.security.entity.ResourceDefination;
import com.yq.xcode.security.entity.SecPrincipal;
import com.yq.xcode.security.entity.SecurityId;
import com.yq.xcode.security.oauth.LoginUser;
import com.yq.xcode.security.service.PrincipalService;
import com.yq.xcode.security.service.YqSecurityService;
import com.yq.xcode.security.utils.YqSecurityUtils;

@Service("YqSecurityService")
@Transactional
public class YqSecurityServiceImpl extends YqJpaDataAccessObject implements YqSecurityService {

	@Autowired
    private SqlToModelService sqlToModelService;
	
	@Autowired
    private PrincipalService principalService;
	
	
	@Override
	public Set<String> getResource(String asUserName, String sourceName) {
		if (CommonUtil.isNull(asUserName)) {
			return YqSecurityUtils.getResource(sourceName);
		}
		Set<String> userNames = new HashSet<String>();
		userNames.add(asUserName);
		SecPrincipal p = this.principalService.getPrincipalByUsername(asUserName);
		List<ResourceInstance> instList = this.principalService.findResourceInstanceByPrincipal(sourceName, p.getId());
		Set<String> resSet = new HashSet<String>();
		if (CommonUtil.isNull(instList)) {
			for (ResourceInstance inst : instList ) {
				resSet.add(inst.getId());
			}
 		}
		return resSet;
	}

	@Override
	public LoginUser getLoginUser(String asUserName) {
		if (CommonUtil.isNull(asUserName)) {
			return YqSecurityUtils.getLoginUser();
		}
		SecPrincipal p = this.principalService.getPrincipalByUsername(asUserName);
		if ( p == null ) {
			return null;
		}
		return new LoginUser(p);
	}
	
 


}
