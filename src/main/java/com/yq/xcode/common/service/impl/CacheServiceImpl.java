package com.yq.xcode.common.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.yq.xcode.common.bean.SelectItem;
import com.yq.xcode.common.exception.ValidateException;
import com.yq.xcode.common.model.LookupCode;
import com.yq.xcode.common.model.SystemParameter;
import com.yq.xcode.common.service.CacheService;
import com.yq.xcode.common.service.SqlToModelService;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.JPAUtils;


@Service("CacheService")
public class CacheServiceImpl extends YqJpaDataAccessObject implements CacheService {
	@Autowired
	private CacheManager cacheManager;
	@Autowired private SqlToModelService sqlToModelService;
	//所有的定义的Cache
	private static final String[] allCacheName = new String[] {
		"APP_SYSTEM_PARAMETER_BY_KEY",
		"APP_LOOKUP_CODE_BY_CATEGORY",
		"APP_LOOKUP_CODE_BY_ID",
		"APP_LOOKUP_CODE_BY_PRO",
		"REDIS_Area"
	};
	
	public void cleanAllCache(){
		for(String cacheName:allCacheName){
			Cache cache = cacheManager.getCache(cacheName);
			if (cache != null) {
				cache.clear();
			}
			cache = cacheManager.getCache(cacheName);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Cacheable(value="APP_LOOKUP_CODE_BY_CATEGORY",key="'APP_LOOKUP_CODE_BY_CATEGORY_KEY_' + #categoryCode")
	public List<LookupCode> findLookupCodeByCategory(String categoryCode) {
		return this.find("from LookupCode where categoryCode=?0 order by lineNumber ", categoryCode);
	}
	
	@SuppressWarnings("unchecked")
	@Cacheable(value="APP_LOOKUP_CODE_BY_PRO")
	public List<LookupCode> findLookupCodeByPro(String categoryCode,String queryString) {
		
		String sql = " from LookupCode a where a.categoryCode=?";
		if(CommonUtil.isNotNull(queryString)){
			sql += " and concat(a.lookupCode,a.lookupName,a.description) like '%" + queryString + "%'";
		}
		sql+=" order by a.lineNumber";
		return this.find(sql, categoryCode);
	}
	
	@Cacheable(value="APP_LOOKUP_CODE_BY_ID",key="'APP_LOOKUP_CODE_BY_ID_KEY_' + #keyCode")
	public LookupCode getLookupCodeByKeyCode(String keyCode) {
		
		List<LookupCode> list = this.find(" from LookupCode a where a.keyCode=?", keyCode);
		if(CommonUtil.isNull(list)){
			throw new ValidateException("系统无法找到系统代码" + keyCode);
		}
		return list.get(0);
	}
	


	@Override
	@Cacheable(value="REDIS_LookupCode_4" , key="'REDIS_findSelectItemByParentKey_key' +#parentKey ") 
	public List<SelectItem> findSelectItemByParentKey(String parentKey) {
		StringBuilder sb = new StringBuilder("  SELECT lc.KEY_CODE itemKey, lc.LOOKUP_NAME itemName, lc.DESCRIPTION remark ");
		sb.append(" FROM yq_lookup_code lc ");
		//sb.append(" where  lc.PARENT_KEY_CODE = '"+ JPAUtils.toPar(parentKey) +"'");
		sb.append(" where  lc.PARENT_KEY_CODE in "+ JPAUtils.toInCharSql(parentKey) +"");
		return this.sqlToModelService.executeNativeQuery(sb.toString(), null, SelectItem.class);
	}

	@Cacheable(value="APP_SIYUSCP_SYSTEM_PARAMETER_BY_KEY",key="'APP_SIYUSCP_SYSTEM_PARAMETER_BY_KEY_' + #keyCode")
	public SystemParameter getSystemParameterByKeyCode(String keyCode) {	
		return this.getById(SystemParameter.class, keyCode);
	}



	 

	 
	
}
