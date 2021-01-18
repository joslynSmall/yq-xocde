package com.yq.xcode.common.model;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.yq.xcode.security.entity.JpaAuditableModel;

@MappedSuperclass
public class YqJpaAuditableModel extends JpaAuditableModel {
	@Transient 
	private Map<String,Object> pageMap = new HashMap<String,Object>();
	/**
	 * 保存查询结果的临时值(colName:value)
	 * @return
	 */
	public Map<String, Object> getPageMap() {
		return pageMap;
	}
	public void setPageMap(Map<String, Object> pageMap) {
		this.pageMap = pageMap;
	}

}
