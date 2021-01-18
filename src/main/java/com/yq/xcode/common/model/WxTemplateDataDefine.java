package com.yq.xcode.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yq.xcode.web.ui.annotation.ColumnLable;
import com.yq.xcode.web.ui.annotation.EditCol;
import com.yq.xcode.web.ui.annotation.EntityDesc;
import com.yq.xcode.web.ui.annotation.GridCol;

@SuppressWarnings("serial")
@Entity
@Table(name = "wx_template_data_define")

@JsonIgnoreProperties(ignoreUnknown = true)
@EntityDesc(name = "基础资料 >> 微信消息模板详情", autoGenPage = true, editCols = 2, genService = true, genAction = true, genCriteria = true)
public class WxTemplateDataDefine extends YqJpaBaseModel  {

	@TableGenerator(name = "idGen", table = "ID_GENERATOR", pkColumnName = "ID_KEY", valueColumnName = "ID_VALUE", pkColumnValue = "wx_template_data_define_ID", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idGen")
	@Column(name = "ID")
	private Long id;
	/**
	 * 主表ID
	 */
	@ColumnLable(name = "主表ID")
	@Column(name = "template_id")
	private Long templateId;
	/**
	 *数据字段，对应的消息类型
	 */
	@ColumnLable(name = "数据字段")
	@GridCol(lineNum = 1)
	@EditCol(lineNum = 1,mandatory=true)
	@Column(name = "key_code")
	private String keyCode;
	/**
	 *  系统模板， 来自微信的定义，模板申请后，有开发数据库录入
	 */
	@ColumnLable(name = "系统模板")
	@GridCol(lineNum = 2)
	@EditCol(lineNum = 2,mandatory=true)
	@Column(name = "is_sys")
	private Long isSys;
	/**
	 * 熟悉名称
	 */
	@ColumnLable(name = "熟悉名称")
	@GridCol(lineNum = 3)
	@EditCol(lineNum = 3,mandatory=true)
	@Column(name = "property_name") 
	private String propertyName;
	/**
	 * 内容
	 */
	@ColumnLable(name = "内容")
	@GridCol(lineNum = 4)
	@EditCol(lineNum = 4,mandatory=true)
	@Column(name = "context")
	private String context;
	/**
	 * 颜色
	 */
	@ColumnLable(name = "颜色")
	@GridCol(lineNum = 5)
	@EditCol(lineNum = 5,mandatory=true)
	@Column(name = "color")
	private String color;
	
	@Column(name = "source_wxtemplatedatadefine_id")
	private Long sourceWxTemplateDataDefineId;
	
	@Transient
	private WxTemplateDataDefine sourceWxTemplateDataDefine;
	
	@Override
	public Long getId() {
		
		return id;
	}
	@Override
	public void setId(Long id) {
		this.id=id;		
	}
	public Long getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}
	public String getKeyCode() {
		return keyCode;
	}
	public void setKeyCode(String keyCode) {
		this.keyCode = keyCode;
	}
	public Long getIsSys() {
		return isSys;
	}
	public void setIsSys(Long isSys) {
		this.isSys = isSys;
	}	
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Long getSourceWxTemplateDataDefineId() {
		return sourceWxTemplateDataDefineId;
	}
	public void setSourceWxTemplateDataDefineId(Long sourceWxTemplateDataDefineId) {
		this.sourceWxTemplateDataDefineId = sourceWxTemplateDataDefineId;
	}
	public WxTemplateDataDefine getSourceWxTemplateDataDefine() {
		return sourceWxTemplateDataDefine;
	}
	public void setSourceWxTemplateDataDefine(WxTemplateDataDefine sourceWxTemplateDataDefine) {
		this.sourceWxTemplateDataDefine = sourceWxTemplateDataDefine;
	} 
	
	
}