package com.yq.xcode.common.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.transaction.Transactional;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.yq.xcode.common.bean.IdAndVersion;
import com.yq.xcode.common.bean.LookupCodeCategory;
import com.yq.xcode.common.bean.SelectItem;
import com.yq.xcode.common.criteria.LookupCodeQueryCriteria;
import com.yq.xcode.common.exception.DllException;
import com.yq.xcode.common.exception.ValidateException;
import com.yq.xcode.common.model.LookupCode;
import com.yq.xcode.common.model.PageTag;
import com.yq.xcode.common.service.CacheService;
import com.yq.xcode.common.service.InitConstantsService;
import com.yq.xcode.common.service.LookupCodeService;
import com.yq.xcode.common.service.PageTagService;
import com.yq.xcode.common.service.SqlToModelService;
import com.yq.xcode.common.service.YqSequenceService;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.JPAUtils;
import com.yq.xcode.common.utils.YqBeanUtil;

@Service("LookupCodeService")
@Transactional
public class LookupCodeServiceImpl extends YqJpaDataAccessObject implements LookupCodeService {


	@Autowired
	private CacheService cacheService;
	@Autowired
	private YqSequenceService yqSequenceService;
	@Autowired
	private SqlToModelService sqlToModelService;

	@Autowired
	private PageTagService pageTagService;
	@Autowired
	private InitConstantsService initConstantsService;

	@Override
	public List<LookupCode> findLookupCodeByCategory(String categoryCode) {
		return cacheService.findLookupCodeByCategory(categoryCode);
	}

	@Override
	public List<LookupCode> findLookupCodeByPro(String categoryCode,String queryString) {
		return cacheService.findLookupCodeByPro(categoryCode, queryString);
	}

	@SuppressWarnings("unchecked")
	@Override
	public LookupCode getLookupCodeByKeyCode(String keyCode) {
		List<LookupCode> list = this.find(" from LookupCode a where a.keyCode=?0", keyCode);
		if(CommonUtil.isNull(list)){
			throw new ValidateException("系统无法找到系统代码" + keyCode);
		}
		return list.get(0);
	}

	@Override
	@CacheEvict(value={"APP_LOOKUP_CODE_BY_CATEGORY","APP_LOOKUP_CODE_BY_ID","APP_LOOKUP_CODE_BY_PRO"},allEntries=true)
	public LookupCode saveLookupCode(LookupCode lookupCode) {
		if (CommonUtil.isNullId(lookupCode.getId())) {
			lookupCode.setKeyCode(lookupCode.getCategoryCode()+"-"+lookupCode.getLookupCode());
		} else if (!lookupCode.getKeyCode().equals(lookupCode.getCategoryCode()+"-"+lookupCode.getLookupCode())) {
			throw new ValidateException("数据字典代码不可以更改 ！");
		}
		return this.save(lookupCode);
	}

	@Override
	@CacheEvict(value={"APP_LOOKUP_CODE_BY_CATEGORY","APP_LOOKUP_CODE_BY_ID"},allEntries=true)
	public void deleteLookupCode(String keyCode) {
		LookupCode lookupCode = getLookupCodeByKeyCode(keyCode);
		this.delete(lookupCode);
	}

	@Override
	@CacheEvict(value={"APP_LOOKUP_CODE_BY_CATEGORY","APP_LOOKUP_CODE_BY_ID","APP_LOOKUP_CODE_BY_PRO"},allEntries=true)
	public void deleteLookupCode(String[] keyCodes) {
		for (int i=0;i<keyCodes.length;i++) {
			deleteLookupCode(keyCodes[i]);
		}
	}

	@Override
	public List<LookupCode> findLookupCodeByCategory(LookupCodeCategory lookupCodeCategory) {
		List<LookupCode> lookupCodeList = findLookupCodeByCategory(lookupCodeCategory.getCategoryCode());
		return lookupCodeList;
	}

	@Override
	public Map<String,LookupCode> list2MapLookupCode(List<LookupCode> lookupCodeList) {
		Map<String,LookupCode> map=new HashMap<String,LookupCode>();
		for(LookupCode lookupCode:lookupCodeList)
		{
			map.put(lookupCode.getKeyCode(), lookupCode);
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String,LookupCode> getAllLookupCodeByCodeMap() {
		List<LookupCode> list = this.find(" from LookupCode ");
		Map<String,LookupCode> vMap = new HashMap<String,LookupCode>();
		if (!CommonUtil.isNull(list)) {
			for (LookupCode code : list) {
				vMap.put(code.getKeyCode(), code);
			}
		}
		return vMap;
	}

	@Override
	public Page<LookupCode> findLookupCode(LookupCodeQueryCriteria criteria ){

		String query = "select " +JPAUtils.genEntityCols(LookupCode.class, "lc", null)
				+" from yq_lookup_code lc "
				+" where 1=1 ";
		Page<LookupCode> page =
				sqlToModelService.executeNativeQueryForPage(query, " lc.line_number ",
						null, criteria,   LookupCode.class);
		return page;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <E> void resetLookupCodeName(List<E> list,String[][] keyAndNames) {
		Map<String,LookupCode> map = getAllLookupCodeByCodeMap();
		for (E bean : list) {
			for (String[] keyAndName : keyAndNames) {
				String key = keyAndName[0];
				String name = keyAndName[1];
				LookupCode lc = map.get(YqBeanUtil.getPropertyValue(bean, key));
				if (lc != null) {
					YqBeanUtil.setProperty(bean, name, lc.getLookupName());
					Map pageMap = (Map)YqBeanUtil.getPropertyValue(bean, "pageMap");
					if (pageMap == null) {
						pageMap = new HashMap();
						YqBeanUtil.setProperty(bean, "pageMap", pageMap);
					}
					pageMap.put(name, lc.getLookupName());
				}
			}
		}
	}

	@Override
	public String getNameByKey(String key) {
		LookupCode lc = this.getLookupCodeByKeyCode(key);
		if (lc == null) {
			return "";
		} else {
			return lc.getLookupName();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public LookupCode  findLookupCodeByKeyCode(String keyCode) {
		List<LookupCode> list = this.find(" from LookupCode a where a.keyCode=?", keyCode);
		if(CommonUtil.isNull(list)){
			return null;
		}
		return list.get(0);
	}
 
	@Override
	public List<LookupCode> findLookupCodeByCategory(String category,int level){
		String query = "select * from yq_lookup_code lc where lc.CATEGORY_CODE ='"+JPAUtils.toPar(category)+"' order by lc.LINE_NUMBER ";
		return sqlToModelService.executeNativeQuery(query, null, LookupCode.class);
	} 
	
	@Override
	public List<LookupCode> findLookupCodeByCategoryLevel(String category,int level){
		String query = "select * from yq_lookup_code lc where lc.CATEGORY_CODE ='"+JPAUtils.toPar(category)+"' and lc.LOOKUP_LEVEL='"+level+"' order by lc.LINE_NUMBER ";
		return sqlToModelService.executeNativeQuery(query, null, LookupCode.class);
	} 

	@Override
	public List<LookupCode> findKeyCodeByParentKeyCode(String keyCode) {
		String query = "select * from yq_lookup_code where parent_key_Code ='"+JPAUtils.toPar(keyCode)+"' ";
		return sqlToModelService.executeNativeQuery(query, null, LookupCode.class);
	}

	@Override
	public List<LookupCode> findLookupCodeByCriteria(LookupCodeQueryCriteria  criteria) {
		String sql = "select "+JPAUtils.genEntityCols(LookupCode.class, "a",null)
				+" from yq_lookup_code a "
				+" where 1=1 ";
		sql+=" order by a.line_Number";
		List<LookupCode> lists=this.sqlToModelService.executeNativeQuery(sql, null, LookupCode.class);
		return  lists;


	}
	
	@Override
	public List<LookupCode> getLookupCodeByCriteria(LookupCodeQueryCriteria  criteria) {
		String sql = " select * from yq_lookup_code a where a.category_code= " ;
		sql+="'"+criteria.getCategoryCode()+"'";
		if(CommonUtil.isNotNull(criteria.getQueryString())){
			sql += " and concat(ifnull(a.lookup_code,''),ifnull(a.lookup_name,'')) like '%";
			sql+=criteria.getQueryString()+"%' ";
		}

		if(CommonUtil.isNotNull(criteria.getParentKeyCode())){
			sql+=" and parent_key_code= ";
			sql+="'"+criteria.getParentKeyCode()+"'";
		}
		if(CommonUtil.isNotNull(criteria.getLookuplevel())){
			sql+=" and a.lookup_level= ";
			sql+=criteria.getLookuplevel();
		}
		if(CommonUtil.isNotNull(criteria.getKeyCode())) {
			sql+=" and key_code= ";
			sql+="'"+criteria.getKeyCode()+"'";
		}

		sql+=" order by a.line_number";
//		List<LookupCode> lists=this.findByCriteria(sql, criteria);
		List<LookupCode> lookupCodes = sqlToModelService.executeNativeQuery(sql.toString(), null, LookupCode.class);
		for(LookupCode lookupCode : lookupCodes) {
			String segmentDesc = null;
			try {
				segmentDesc = this.getLookupCodeCategory(lookupCode,lookupCode.getCategoryCode());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lookupCode.setSegmentDesc(segmentDesc);
		}
		return  lookupCodes;


	}

	@Override
	public List<LookupCode> findSecondLevelByParentKeyCode(String keyCode) {
		String query = "select * from yq_lookup_code where parent_key_Code ='"+JPAUtils.toPar(keyCode)+"' "
				+ " and lookup_level = 2 ";
		return sqlToModelService.executeNativeQuery(query, null, LookupCode.class);
	}

	@Override
	public void deleteLookupCodeByIdvs(List<IdAndVersion> idvs) {
		this.delete(LookupCode.class, idvs);


	}
 

	private Map<String,String> queryToNameKey(String query) {
		if (CommonUtil.isNull(query)) {
			return null;
		}
		List<SelectItem> list = this.sqlToModelService.executeNativeQuery(query, null, SelectItem.class);
		Map<String,String> map = new HashMap<String,String>();
		for (SelectItem si : list) {
			map.put(si.getItemName(), si.getItemKey());
		}
		return map;
	}

 
	public static void main(String[] arg) {
		char c = (char) (71);
		String s = String.valueOf(c);
		System.out.print(s);
	}
 

	@Override
	public List<SelectItem> findKeyCodeByParentKeyCodeList(String keyCode) {
		String query = "select segment1 itemKey, lookup_name itemName from yq_lookup_code where parent_key_Code ='"+JPAUtils.toPar(keyCode)+"' ";
		return sqlToModelService.executeNativeQuery(query, null, SelectItem.class);
	}
	
	public String getLookupCodeCategory(LookupCode lookupCode,String categoryCode) throws Exception {
		LookupCodeCategory cat = initConstantsService.getLookupCodeCategory(categoryCode);
		if (cat != null && cat.getExtDefine() != null) {
			PageTag[] mds = cat.getExtDefine().get(1);
			if (CommonUtil.isNotNull(mds)) {
				String rtStr = "";
				for (PageTag extKey : mds) {
					 
						rtStr = rtStr+
								"<strong style=\"color:black\">"+ extKey.getLable()+"</strong>"
								+": "+CommonUtil.nvl(PropertyUtils.getProperty(lookupCode, extKey.getProperty())," ")+"<br> ";
				}
				return rtStr;
			}
		}
		return null;
	}
	
	@CacheEvict(value={"APP_MRP_LOOKUP_CODE_BY_CATEGORY","APP_MRP_LOOKUP_CODE_BY_ID","APP_MRP_LOOKUP_CODE_BY_PRO"},allEntries=true)
	public LookupCode createLookupCode(LookupCode lookupCode) {
		Query query = em.createQuery("from LookupCode where keyCode=?0").setParameter(0, lookupCode.getKeyCode());
		if (JPAUtils.isSingleQuery(query)) {
			throw new DllException("该类型代码已存在，请输入其他代码");
		}

		return this.create(lookupCode);
	}
	
	/**
	 * 注意，lookupCode 的keyCode 是指定的， 所以修改时如果是keyCODE 修改，需要删除旧的，再新增新的一笔
	 * @param lookupCode
	 * @return
	 */
	@CacheEvict(value={"APP_MRP_LOOKUP_CODE_BY_CATEGORY","APP_MRP_LOOKUP_CODE_BY_ID","APP_MRP_LOOKUP_CODE_BY_PRO"},allEntries=true)
	public LookupCode updateLookupCode(LookupCode lookupCode) {
		LookupCode oldLookupCode = em.find(LookupCode.class, lookupCode.getId());
		//LookupCode oldLookupCode = getLookupCodeByKeyCode(lookupCode.getKeyCode());
		if (oldLookupCode != null) {
			lookupCode.setCreateTime(oldLookupCode.getCreateTime());
			lookupCode.setCreateUser(oldLookupCode.getCreateUser());
			lookupCode.setSysType(oldLookupCode.getSysType());
		}
		
		return em.merge(lookupCode);
	}

}