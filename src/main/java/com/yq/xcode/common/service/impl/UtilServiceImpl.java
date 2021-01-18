package com.yq.xcode.common.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Table;

import org.springframework.stereotype.Service;

import com.yq.xcode.common.bean.PropertyDefine;
import com.yq.xcode.common.service.UtilService;
import com.yq.xcode.common.utils.YqBeanUtil;
import com.yq.xcode.security.entity.JpaBaseModel;

@Service("UtilService")
public class UtilServiceImpl extends YqJpaDataAccessObject implements
		UtilService {

	/**
	 * 
	 */
	public void createByNativeQuery(JpaBaseModel entity) {
		Table table = entity.getClass().getAnnotation(Table.class);
		List<PropertyDefine> pdList = YqBeanUtil.genEntityDefine(entity.getClass());
		List pars = new ArrayList();
		String insertStr = "insert into "+table.name()+" ( ";
		String values = "";
		boolean first = true;
		for (PropertyDefine pd : pdList) {
			if (first) {
				insertStr = insertStr + pd.getColumn();
				values = "?";
				first = false;
			} else {
				values = values+", ?";
				insertStr = insertStr +","+ pd.getColumn();
			}
			pars.add(YqBeanUtil.getPropertyValue(entity, pd.getProperty()));
			
		}
		insertStr=insertStr+") values ( "+values+" )";
		this.executeNativeQuery(insertStr, pars.toArray());		
	}

	@Override
	public boolean existsByNativeQuery(String query, Object... pars) {
		return this.existsNativeQuery(query, pars);
	}

}
