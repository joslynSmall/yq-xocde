package com.yq.xcode.common.bean;

import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

import com.yq.xcode.common.model.LookupCode;
import com.yq.xcode.common.model.PageTag;


/**
 * 此model为 LookupCodeCategory的hardcode 定义，用户不可改
 * @author jt
 *
 */

public class LookupCodeCategory {
	private String categoryCode;
	private String categoryName; 
	private Integer maxLevel;
	
	@Transient 
	private List<LookupCode> lookupCodes;
	
 	public List<LookupCode> getLookupCodes() {
		return lookupCodes;
	}

	public void setLookupCodes(List<LookupCode> lookupCodes) {
		this.lookupCodes = lookupCodes;
	}
	/**
	 * 扩展定义字段
	 */
	private Map<Integer,PageTag[]> extDefine;
	
	public LookupCodeCategory() {
		
	}
	
	public LookupCodeCategory(String categoryCode, String categoryName,  Integer maxLevel, Map<Integer,PageTag[]> extDefine) {
		this.categoryCode = categoryCode;
		this.categoryName = categoryName; 
		this.maxLevel = maxLevel;
		this.extDefine = extDefine;
	}
	
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
 
	public Integer getMaxLevel() {
		return maxLevel;
	}
	public void setMaxLevel(Integer maxLevel) {
		this.maxLevel = maxLevel;
	}
	public Map<Integer, PageTag[]> getExtDefine() {
		return extDefine;
	}
	public void setExtDefine(Map<Integer, PageTag[]> extDefine) {
		this.extDefine = extDefine;
	}
 	
	 
}
