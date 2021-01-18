


 package com.yq.xcode.common.criteria;

import com.yq.xcode.annotation.EntityTableAlias;
import com.yq.xcode.annotation.ParameterLogic;
import com.yq.xcode.common.springdata.HWorkFlowEntityPageCriteria;
@EntityTableAlias(alias = "a")
public class OvertimeNoteCriteria extends HWorkFlowEntityPageCriteria{

	@ParameterLogic(colName=" concat(a.vouch_number,ifnull(c.chain_Name,'') ,ifnull(c.chain_code,''),'-',ifnull(a.work_leader_name,''),ifnull(a.work_leader_mobile,'')) ", operation="inlike" , placeHolder="" )
	private String vouchNumber;
	@ParameterLogic(colName=" a.CREATED_TIME", operation=">=" , placeHolder="" )
	private QueryDate createdFrom;
	@ParameterLogic(colName=" a.CREATED_TIME", operation="<=" , placeHolder="" )
	private QueryDate createdTo;
	
	@ParameterLogic(colName=" a.status", operation="=" , placeHolder="" )
	private String status;
	
	@ParameterLogic(colName=" c.chain_code", operation="like" , placeHolder="" )
	private String chainCode;
 	
	@ParameterLogic(colName=" c.chain_name", operation="like" , placeHolder="" )
	private String chainName;
	 
	
 	public String getStatus() {
		return status;   
	}
	public void setStatus(String status) {
		this.status = status; 
	}
	public String getVouchNumber() {
		return vouchNumber;
	}
	public void setVouchNumber(String vouchNumber) {
		this.vouchNumber = vouchNumber;
	}
	public QueryDate getCreatedFrom() {
		return createdFrom;
	}
	public void setCreatedFrom(QueryDate createdFrom) {
		this.createdFrom = createdFrom;
	}
	public QueryDate getCreatedTo() {
		return createdTo;
	}
	public void setCreatedTo(QueryDate createdTo) {
		this.createdTo = createdTo;
	}
	
	public String getChainCode() {
		return chainCode;
	}
	public void setChainCode(String chainCode) {
		this.chainCode = chainCode;
	}
	public String getChainName() {
		return chainName;
	}
	public void setChainName(String chainName) {
		this.chainName = chainName;
	}
    
	
}
