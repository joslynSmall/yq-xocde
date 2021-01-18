package com.yq.xcode.common.criteria;

import com.yq.xcode.annotation.ParameterLogic;

public class ReceivableCriteria extends NativeCriteria{
	
	private static final long serialVersionUID = 8038166433290219710L;
	
	@ParameterLogic(colName="gr.PERIOD_MONTH ", operation=">=")
	private String startMonth;
	@ParameterLogic(colName=" gr.PERIOD_MONTH ", operation="<=" )
	private String endMonth;
	@ParameterLogic(colName="STR_TO_DATE(gcp.PAYMENT_DATE ,'%Y-%m-%d')", operation=">=" )
	private String payStartMonth;
	@ParameterLogic(colName="STR_TO_DATE(gcp.PAYMENT_DATE ,'%Y-%m-%d')", operation="<=" )
	private String payEndMonth;
	@ParameterLogic(colName="concat(gcc.CHAIN_NAME,'-',gcc.CHAIN_CODE ,'-',gcp.payment_number,'-',IFNULL(ao.account_name,''),'-',cmp.band_code_cn)", operation="like")
	private String chainStr;
	@ParameterLogic(colName="gcp.payment_number", operation="like")
	private String paymentNumber;
	@ParameterLogic(colName="gr.id", operation="=")
	private Long id;
	@ParameterLogic(colName="gr.receivable_status", operation="=")
	private String receivableStatus;
	@ParameterLogic(colName =" case gcp.PAYMENT_STATUS when 'PAY' then '待客户付款' when 'PAID' then '客户已付款' when 'CM' then '总部已结账' else '收款失败' end ", operation="=" )
	private String paymentStatus;
	@ParameterLogic(colName="gr.receivable_status", operation="in")
	private String status;
	@ParameterLogic(colName="gcp.PAYMENT_STATUS", operation="in")
	private String paymentStatusAndPa;
	@ParameterLogic(colName="gr.CHARGE_CATEGORY", operation="in")
	private String orderType;
	@ParameterLogic(colName="gcp.id", operation="in")
	private String chainIds;
	//查询字段注入
	@ParameterLogic(colName="gr.AMOUNT",operation=">=")
	private String smallAmount;
	@ParameterLogic(colName="gr.AMOUNT",operation="<=")
	private String bigAmount;
	@ParameterLogic(colName="gr.CHARGE_CATEGORY", operation="in")
	private String from;//CHARGE_CATEGORY
	
	@ParameterLogic(colName="gcp.RECEIVE_BILL_STATUS", operation="in")
	private String receiveBillStatus; 
	
	
	public String getChainIds() {
		return chainIds;
	}
	public void setChainIds(String chainIds) {
		this.chainIds = chainIds;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getPaymentStatusAndPa() {
		return paymentStatusAndPa;
	}
	public void setPaymentStatusAndPa(String paymentStatusAndPa) {
		this.paymentStatusAndPa = paymentStatusAndPa;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getReceivableStatus() {
		return receivableStatus;
	}
	public void setReceivableStatus(String receivableStatus) {
		this.receivableStatus = receivableStatus;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getStartMonth() {
		return startMonth;
	}
	public void setStartMonth(String startMonth) {
		this.startMonth = startMonth;
	}
	public String getEndMonth() {
		return endMonth;
	}
	public void setEndMonth(String endMonth) {
		this.endMonth = endMonth;
	}
	public String getChainStr() {
		return chainStr;
	}
	public void setChainStr(String chainStr) {
		this.chainStr = chainStr;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public String getPayStartMonth() {
		return payStartMonth;
	}
	public void setPayStartMonth(String payStartMonth) {
		this.payStartMonth = payStartMonth;
	}
	public String getPayEndMonth() {
		return payEndMonth;
	}
	public void setPayEndMonth(String payEndMonth) {
		this.payEndMonth = payEndMonth;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getPaymentNumber() {
		return paymentNumber;
	}
	public void setPaymentNumber(String paymentNumber) {
		this.paymentNumber = paymentNumber;
	}
	public String getSmallAmount() {
		return smallAmount;
	}
	public void setSmallAmount(String smallAmount) {
		this.smallAmount = smallAmount;
	}
	public String getBigAmount() {
		return bigAmount;
	}
	public void setBigAmount(String bigAmount) {
		this.bigAmount = bigAmount;
	}
	public String getReceiveBillStatus() {
		return receiveBillStatus;
	}
	public void setReceiveBillStatus(String receiveBillStatus) {
		this.receiveBillStatus = receiveBillStatus;
	}
	
	
	
}
