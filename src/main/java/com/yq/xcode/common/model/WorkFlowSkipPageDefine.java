package com.yq.xcode.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yq.xcode.web.ui.annotation.ColumnLable;
import com.yq.xcode.web.ui.annotation.EntityDesc;

@SuppressWarnings("serial")
@Entity
@Table(name = "yq_work_flow_SKIP_PAGE_DEFINE")

@EntityDesc(name = "流程跳转页面", autoGenPage = true, editCols = 3, genService = true, genAction = true, genCriteria = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkFlowSkipPageDefine extends YqJpaBaseModel {

	@Id
	@Column(name = "ID")
	private Long id;
	
 	@ColumnLable(name = "审批类型")
	@Column(name = "category_code")
	private String categoryCode;
	
	@ColumnLable(name = "名称")
	@Column(name = "name")
	private String name;
	

	/**
	 * 如果是固定跳转页面，webUrl, mobileUrl 费控， 
	 * 如果否， 曾 reportDefine ID非空
	 */
	@ColumnLable(name = "固定页面")
	@Column(name = "is_page_url")
	private Boolean isPageUrl;
	/**
	 * WEBURL,pcpage 的页面链接
	 */
	@ColumnLable(name = "WEB_URL")
	@Column(name = "WEB_URL")
	private String webUrl;
	
	/**
	 * 页面链接
	 */
	@ColumnLable(name = "移动页面")
	@Column(name = "MOBILE_URL")
	private String mobileUrl;
	
	/**
	 * 定义字段, 放在报表定义中
	 */
	@Column(name = "ENTITY_TEMPLATE_ID")
	private Long entityTemplateId;
	
	@Transient
	private String entityNumber;
	@Transient
	private String statusDsp;
	
	@Override
	public Long getId() {
		return this.id;
	}
	
	@Override
	public void setId(Long id) {
		this.id = id;
	}

 
	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getIsPageUrl() {
		return isPageUrl;
	}

	public void setIsPageUrl(Boolean isPageUrl) {
		this.isPageUrl = isPageUrl;
	}

	public String getWebUrl() {
		return webUrl;
	}

	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

	public String getMobileUrl() {
		return mobileUrl;
	}

	public void setMobileUrl(String mobileUrl) {
		this.mobileUrl = mobileUrl;
	}

	public Long getEntityTemplateId() {
		return entityTemplateId;
	}

	public void setEntityTemplateId(Long entityTemplateId) {
		this.entityTemplateId = entityTemplateId;
	}

	public String getEntityNumber() {
		return entityNumber;
	}

	public void setEntityNumber(String entityNumber) {
		this.entityNumber = entityNumber;
	}

	public String getStatusDsp() {
		return statusDsp;
	}

	public void setStatusDsp(String statusDsp) {
		this.statusDsp = statusDsp;
	}
	
	

 
}