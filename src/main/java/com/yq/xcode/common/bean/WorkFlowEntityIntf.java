package com.yq.xcode.common.bean;



public  interface   WorkFlowEntityIntf     {
	/**
	 * Entity ID
	 */
	public Long getEntityId();
	
	/**
	 * 设务数据状态
	 */
	public void reSetEntityStatus( String entityStatus);
	
	/**
	 * 设置实体数据状态
	 */
	public String getEntityStatus();
	
	/**
	 * 流程显示名称
	 * @return
	 */
	public String getEntityStatusDsp();
	
	/**
	 * source table category
	 * 区分不同实体的审批数据源 ，一般hardcode
	 */
	public String getEntityCategory();
	
	public String getEntityNumber();
	
	public String getEntityDescription();
	
	/**
	 * 定义特殊角色
	 * @return
	 */
	public WorkFlowSpecRole[] getSpecRoles();
	
	public void reSetWorkFlowId(Long workFlowId);
	
	/**
	 * 提单人， 一般是createdBy, 但可能特殊情况，帮忙提单
	 * @return
	 */ 
	
	/**
	 * 取流程Id
	 * @return
	 */
	public Long getItWorkFlowId();
	
	/**
	 * 提单人显示名称
	 * @return
	 */
	public String getCreateByDsp();
	
  
}

	

