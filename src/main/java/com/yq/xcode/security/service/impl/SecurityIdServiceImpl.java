package com.yq.xcode.security.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yq.xcode.common.exception.ValidateException;
import com.yq.xcode.common.exception.XException;
import com.yq.xcode.common.service.impl.YqJpaDataAccessObject;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.security.entity.SecurityId;
import com.yq.xcode.security.service.SecurityIdService;

@Service
@Transactional
public class SecurityIdServiceImpl extends YqJpaDataAccessObject implements SecurityIdService {
	@Override
	public SecurityId getSecurityIdById(Long id) {
		SecurityId securityId = this.getById(SecurityId.class, id);
		return securityId;
	}
	@Override
	public SecurityId getSecurityIdBySId(Long sid, SecurityId.Type type) {
		@SuppressWarnings("unchecked")
		List<SecurityId> list = this.find(" from SecurityId a where a.sid=? and type = ? ", sid,type);
		if(CommonUtil.isNull(list)){
			return null;
		}
		return list.get(0);
	}
	@Override
	public void saveSecurityId(SecurityId securityId) {
		if (null == securityId.getId()) {
			if (null != this.getSecurityIdBySId(securityId.getSid(),securityId.getType())) {
				throw new XException(String.format("编码%s已存在", securityId.getSid()));
			}
		}
		this.save(securityId);
	}

	@Override
	public void deleteSecurityId(Long id) {
		this.deleteById(SecurityId.class,id);
	}



}
