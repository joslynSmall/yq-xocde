package com.yq.xcode.common.model;

import java.util.List;

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
import com.yq.xcode.web.ui.annotation.Detail;
import com.yq.xcode.web.ui.annotation.EditCol;
import com.yq.xcode.web.ui.annotation.EntityDesc;
import com.yq.xcode.web.ui.annotation.GridCol;

@SuppressWarnings("serial")
@Entity
@Table(name = "wx_template")

@JsonIgnoreProperties(ignoreUnknown = true)
@EntityDesc(name = "基础资料 >> 微信消息模板", autoGenPage = true, editCols = 2, genService = true, genAction = true, genCriteria = true)
public class WxTemplate extends YqJpaBaseModel  {

	@TableGenerator(name = "idGen", table = "ID_GENERATOR", pkColumnName = "ID_KEY", valueColumnName = "ID_VALUE", pkColumnValue = "wx_template_ID", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idGen")
	@Column(name = "ID")	
	private Long id;
	/**
	 * 编码, 系统是， 是微信的模板代码
	 */
	@ColumnLable(name = "编码")
	@GridCol(lineNum = 1)
	@EditCol(lineNum = 1,mandatory=true)
	@Column(name = "code")
	private String code;
	/**
	 * 名称， 系统时， 是微信模板名称
	 */
	@ColumnLable(name = "名称")
	@GridCol(lineNum = 2)
	@EditCol(lineNum = 2,mandatory=true)
	@Column(name = "name")
	private String name;
	/**
	 *  系统模板， 来自微信的定义，模板申请后，有开发数据库录入
	 */
	@ColumnLable(name = "系统模板")
	@GridCol(lineNum = 3)
	@EditCol(lineNum = 3,mandatory=true)
	@Column(name = "is_sys")
	private Long isSys;
	/**
	 * 模版ID， 微信模板ID
	 */
	@ColumnLable(name = "微信模板ID")
	@GridCol(lineNum = 4)
	@EditCol(lineNum = 4,mandatory=true)
	@Column(name = "template_id") 
	private String templateId;
	/**
	 * 用户模板时， copy 的系统模板ID
	 */
	@ColumnLable(name = "系统模板ID")
	@GridCol(lineNum = 5)
	@EditCol(lineNum = 5,mandatory=true)
	@Column(name = "source_template_id")
	private Long sourceTemplateId;
	/**
	 * 使用业务实体类型， 对应取数对象
	 */
	@ColumnLable(name = "业务实体类型")
	@GridCol(lineNum = 6)
	@EditCol(lineNum = 6,mandatory=true)
	@Column(name = "entity_category")
	private String entityCategory;
	
	@Detail(masterProperty="templateId")
	@Transient
	private List<WxTemplateDataDefine> detail ;
	
	@Transient
	private WxTemplate sourceWxTemplate; 
	
	
	
	public WxTemplate getSourceWxTemplate() {
		return sourceWxTemplate;
	}
	public void setSourceWxTemplate(WxTemplate sourceWxTemplate) {
		this.sourceWxTemplate = sourceWxTemplate;
	}
	

	@Override
	public Long getId() {
		
		return id;
	}
	@Override
	public void setId(Long id) {
		this.id=id;		
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getIsSys() {
		return isSys;
	}
	public void setIsSys(Long isSys) {
		this.isSys = isSys;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	public Long getSourceTemplateId() {
		return sourceTemplateId;
	}
	public void setSourceTemplateId(Long sourceTemplateId) {
		this.sourceTemplateId = sourceTemplateId;
	}
	public String getEntityCategory() {
		return entityCategory;
	}
	public void setEntityCategory(String entityCategory) {
		this.entityCategory = entityCategory;
	}
	public String getIsSysDsp() {
		if(isSys != null){
			if(isSys == 1l){
				return "系统模板";
			}else if(isSys == 0l){
				return "消息模板";
			}
		}
		return "";
	}
	public List<WxTemplateDataDefine> getDetail() {
		return detail;
	}
	public void setDetail(List<WxTemplateDataDefine> detail) {
		this.detail = detail;
	}
	
}