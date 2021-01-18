package com.yq.xcode.common.service;

import com.yq.xcode.security.entity.JpaBaseModel;




public interface UtilService {

	public void createByNativeQuery(JpaBaseModel entity);
	
	public boolean existsByNativeQuery(String query, Object ... pars);

}
