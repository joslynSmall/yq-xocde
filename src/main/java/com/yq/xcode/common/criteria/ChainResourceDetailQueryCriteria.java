package com.yq.xcode.common.criteria;

import com.yq.xcode.annotation.ParameterLogic;
import com.yq.xcode.common.springdata.HPageCriteria;

public class ChainResourceDetailQueryCriteria extends HPageCriteria {
 
	@ParameterLogic(colName ="CONCAT(c.chain_code, '-', c.chain_name,'-',r.resource_name)", operation="like" )
	private String chainKeyWord;
 
	@ParameterLogic(colName ="r.mutex", operation="="  )
	private String mutexCategory;
	
	private String status;

	public String getChainKeyWord() {
		return chainKeyWord;
	}

	public void setChainKeyWord(String chainKeyWord) {
		this.chainKeyWord = chainKeyWord;
	}

	public String getMutexCategory() {
		return mutexCategory;
	}

	public void setMutexCategory(String mutexCategory) {
		this.mutexCategory = mutexCategory;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	 
}
