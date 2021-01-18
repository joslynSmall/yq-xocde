package com.yq.xcode.common.bean;

import java.util.Date;

import com.yq.xcode.common.utils.DateUtil;

/**
 * 用来处理审批查询的扩展字段， 包括关联的表和条件
 * @author jettie
 *
 */
public class WorkFlowEntityView {
	
	private Integer id;
	private String entityNumber;
	private String chainId;
	private String chainName;
	private Date createdDate;
	private String createdByName;
	private String entityId;
	private String entityCategory; 
	private String entityStatus;
	private String entityStatusName; 
	private String dataSource;
	/**
	 * 记录数
	 */
	private Integer recordCount;
	
	/**
	 * 
	 */
	private String entityDescription;
	private String workFlowName;
	private String entityCategoryName;
	 
	
	
	
 	public Integer getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(Integer recordCount) {
		this.recordCount = recordCount;
	}
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	public String getDataSourceName() {
		if ("current".equals(dataSource)) {
			return "待我审批";
		} else if ("history".equals(dataSource)) {
			return "审批历史";
		} else if ("myprocess".equals(dataSource)) {
			return "处理中工单";
		} else if ("myall".equals(dataSource)) {
			return "我的工单";
		}  else if ("conformed".equals(dataSource))  {
			return "审批中";
		} else {
			return dataSource; // 没有定义的类型
		}
			
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getEntityNumber() {
		return entityNumber;
	}
	public void setEntityNumber(String entityNumber) {
		this.entityNumber = entityNumber;
	}
	public String getChainId() {
		return chainId;
	}
	public void setChainId(String chainId) {
		this.chainId = chainId;
	}
	public String getChainName() {
		return chainName;
	}
	public void setChainName(String chainName) {
		this.chainName = chainName;
	}
 
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getEntityId() {
		return entityId;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	public String getEntityCategory() {
		return entityCategory;
	}
	public void setEntityCategory(String entityCategory) {
		this.entityCategory = entityCategory;
	}
 
 
	public String getCreatedByName() {
		return createdByName;
	}
	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}
	public String getEntityStatus() {
		return entityStatus;
	}
	public void setEntityStatus(String entityStatus) {
		this.entityStatus = entityStatus;
	}
	public String getEntityStatusName() {
		return entityStatusName;
	}
	public void setEntityStatusName(String entityStatusName) {
		this.entityStatusName = entityStatusName;
	}
	
	/**
	 * 特殊日期格式
	 * @return
	 */
	public String getCreatedDateDsp() {
		return DateUtil.convertDate2StringRefNow(this.createdDate);
	}
	
	/**
	 * 列表描述
	 * @return
	 */
	public String getDescription() {
		return this.chainName;
 	}
	public String getEntityDescription() {
		return entityDescription;
	}
	public void setEntityDescription(String entityDescription) {
		this.entityDescription = entityDescription;
	}
	public String getWorkFlowName() {
		return workFlowName;
	}
	public void setWorkFlowName(String workFlowName) {
		this.workFlowName = workFlowName;
	}
	public void setEntityCategoryName(String entityCategoryName) {
		this.entityCategoryName = entityCategoryName;
	}
	public String getEntityCategoryName() {
		return entityCategoryName;
	}
	
	
	 
 
}

