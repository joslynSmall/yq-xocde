package com.yq.xcode.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.yq.xcode.security.entity.JpaBaseModel;
import com.yq.xcode.web.ui.annotation.ColumnLable;
import com.yq.xcode.web.ui.annotation.EntityDesc;

@SuppressWarnings("serial")
@Entity
@Table(name = "YQ_INTERFACE_SETTING")
@EntityDesc(name = "", autoGenPage = true, editCols = 3, genService = true, genAction = true, genCriteria = true)

public class InterfaceSetting extends JpaBaseModel {

	@Id
	@Column(name = "ID")
	private Long id;
	
	@ColumnLable(name = "数据来源类型")
	@Column(name = "SOURCE_TYPE")
	private String sourceType;
	
	@ColumnLable(name = "定义名")
	@Column(name = "SOURCE_NAME")
	private String sourceName;
	
	@ColumnLable(name = "编码")
	@Column(name = "SOURCE_CODE")
	private String sourceCode;
	
	@ColumnLable(name = "SQL语句")
	@Column(name = "SQLS_QUERY")
	private String sqlsQuery;
	
	@ColumnLable(name = "连接名")
	@Column(name = "RESOURCE_NAME")
	private String resourceName;
	
	@ColumnLable(name = "连接别名")
	@Column(name = "RESOURCE_ALIAS_NAME")
	private String resourceAliasName;
	
	@Transient
	private String resourceNameDsp;

	@ColumnLable(name = "webapi服务地址")
	@Column(name = "WEBAPI_URL")
	private String webapiUrl;
	
	@ColumnLable(name = "webapi是否需要登录")
	@Column(name = "WEBAPI_LOGIN_IND")
	private boolean webapiLoginInd;
	
	
	@ColumnLable(name = "登录token")
	@Column(name = "WEBAPI_LOGIN_TOKEN")
	private String webapiLoginToken;

	@ColumnLable(name = "登录密码")
	@Column(name = "WEBAPI_LOGIN_PASSWORD")
	private String webapiLoginPassword;
	
	@ColumnLable(name = "webapi Json 格式串")
	@Column(name = "WEBAPI_JSON_STR")
	private String webapiJsonStr;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getSqlsQuery() {
		return sqlsQuery;
	}

	public void setSqlsQuery(String sqlsQuery) {
		this.sqlsQuery = sqlsQuery;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getWebapiUrl() {
		return webapiUrl;
	}

	public void setWebapiUrl(String webapiUrl) {
		this.webapiUrl = webapiUrl;
	}

	public boolean isWebapiLoginInd() {
		return webapiLoginInd;
	}

	public void setWebapiLoginInd(boolean webapiLoginInd) {
		this.webapiLoginInd = webapiLoginInd;
	}

	public String getWebapiLoginToken() {
		return webapiLoginToken;
	}

	public void setWebapiLoginToken(String webapiLoginToken) {
		this.webapiLoginToken = webapiLoginToken;
	}

	public String getWebapiLoginPassword() {
		return webapiLoginPassword;
	}

	public void setWebapiLoginPassword(String webapiLoginPassword) {
		this.webapiLoginPassword = webapiLoginPassword;
	}

	public String getWebapiJsonStr() {
		return webapiJsonStr;
	}

	public void setWebapiJsonStr(String webapiJsonStr) {
		this.webapiJsonStr = webapiJsonStr;
	}

	public String getResourceAliasName() {
		return resourceAliasName;
	}

	public void setResourceAliasName(String resourceAliasName) {
		this.resourceAliasName = resourceAliasName;
	}

	public String getResourceNameDsp() {
		return resourceNameDsp;
	}

	public void setResourceNameDsp(String resourceNameDsp) {
		this.resourceNameDsp = resourceNameDsp;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}
	
}