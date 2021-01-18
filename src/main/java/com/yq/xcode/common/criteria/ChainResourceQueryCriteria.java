package com.yq.xcode.common.criteria;

import com.yq.xcode.annotation.ParameterLogic;
import com.yq.xcode.common.springdata.HPageCriteria;

public class ChainResourceQueryCriteria extends HPageCriteria {
	@ParameterLogic(colName ="CONCAT(cr.resource_code, '-', cr.resource_name)", operation="like" )
	private String keyWord;
	@ParameterLogic(colName ="CONCAT(c.chain_code, '-', c.chain_name)", operation="like" )
	private String chainKeyWord;
	@ParameterLogic(colName ="c.id", operation="not in" )
	private String chainIds; 
	
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public String getChainKeyWord() {
		return chainKeyWord;
	}
	public void setChainKeyWord(String chainKeyWord) {
		this.chainKeyWord = chainKeyWord;
	}
	public String getChainIds() {
		return chainIds;
	}
	public void setChainIds(String chainIds) {
		this.chainIds = chainIds;
	}
//	public String getMutexCategory() {
//		return mutexCategory;
//	}
//	public void setMutexCategory(String mutexCategory) {
//		this.mutexCategory = mutexCategory;
//	}
}
