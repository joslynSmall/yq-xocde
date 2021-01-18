package com.yq.xcode.common.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.yq.xcode.common.bean.IdAndVersion;
import com.yq.xcode.common.bean.LookupCodeCategory;
import com.yq.xcode.common.bean.SelectItem;
import com.yq.xcode.common.criteria.LookupCodeQueryCriteria;
import com.yq.xcode.common.model.LookupCode;
import com.yq.xcode.common.springdata.HPageRequest;

public interface LookupCodeService {
	/**
	 * 通过category 查找到LookupCode List
	 * @param categoryCode
	 * @return
	 */

	public List<LookupCode> findLookupCodeByCategory(String categoryCode);

	public List<LookupCode> findLookupCodeByPro(String categoryCode,String queryString);
	
	public LookupCode getLookupCodeByKeyCode(String keyCode);
	
	public String getNameByKey(String key);
	 

	/**
	 * 注意，lookupCode 的keyCode 是指定的， 所以修改时如果是keyCODE 修改，需要删除旧的，再新增新的一笔
	 * @param lookupCode
	 * @return
	 */
	public LookupCode saveLookupCode(LookupCode lookupCode);

	public void deleteLookupCode(String keyCode);

	public void deleteLookupCode(String[] keyCodes);

	public List<LookupCode> findLookupCodeByCategory(LookupCodeCategory lookupCodeCategory);
	
	public Map<String,LookupCode> list2MapLookupCode(List<LookupCode> lookupCodeList);
	
	public Map<String,LookupCode> getAllLookupCodeByCodeMap();
	
	public Page<LookupCode> findLookupCode(LookupCodeQueryCriteria criteria );

	public <E> void resetLookupCodeName(List<E> list, String[][] keyAndNames);
	
	public LookupCode  findLookupCodeByKeyCode(String keyCode);

	public List<LookupCode> findLookupCodeByCategory(String category, int level);

	public List<LookupCode> findKeyCodeByParentKeyCode(String keyCode);

	public List<LookupCode> findLookupCodeByCriteria(LookupCodeQueryCriteria criteria);

	public List<LookupCode> findSecondLevelByParentKeyCode(String keyCode);
	
	public List<LookupCode> getLookupCodeByCriteria(LookupCodeQueryCriteria  criteria);

	public void deleteLookupCodeByIdvs(List<IdAndVersion> idvs);

	//public void importLookupCode(String categoryCode, File file) throws Exception;


	//public List<SelectItem> findModelExtPros(String extKey);

	public List<SelectItem> findKeyCodeByParentKeyCodeList(String keyCode);
	
	public LookupCode createLookupCode(LookupCode lookupCode);
	
	public LookupCode updateLookupCode(LookupCode lookupCode);
	
	public List<LookupCode> findLookupCodeByCategoryLevel(String category,int level);
  
}
