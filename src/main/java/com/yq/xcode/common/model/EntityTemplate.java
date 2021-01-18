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
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.web.ui.annotation.ColumnLable;
import com.yq.xcode.web.ui.annotation.EditCol;
import com.yq.xcode.web.ui.annotation.GridCol;
/**
 * 模板页面定义， 包括扩展字段和富文本
 * @author jettie
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "YQ_ENTITY_TEMPLATE")

@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityTemplate extends YqJpaBaseModel {
	
	public static final String CATEGORY = "entitytemplate";

	@TableGenerator(name = "idGen", table = "ID_GENERATOR", pkColumnName = "ID_KEY", valueColumnName = "ID_VALUE", pkColumnValue = "GC_COMMON_LETTERS_EXT_DEFINE_ID", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idGen")
	@Column(name = "ID")
	private Long id;

	/**
	 * 来源数据字段
	 */
	@ColumnLable(name = "类型")
	@Column(name = "CATEGORY")
	private String category;

	@EditCol(lineNum = 21)
	@ColumnLable(name = "编码")
	@Column(name = "CODE")
	private String code; 
	
	
	@EditCol(lineNum = 21)
	@ColumnLable(name = "名称")
	@Column(name = "NAME")
	private String name;
	
	/**
	 * 用来取属性定义
	 */
	
	@ColumnLable(name = "className")
	@Column(name = "class_name")
	private String className;
	

	@ColumnLable(name = "生效状态")
	@Column(name = "TEMPLATE_STATUS")
	private Boolean templateStatus;
	
	/**
	 * 只能选择到扩展字段
	 */
	@ColumnLable(name = "扩展属性页面定义")
	@Column(name = "ext_property_define")
	private Boolean extPropertyDefine;
	
	/**
	 * 字段后面的说明
	 */
	@EditCol(lineNum = 21)
	@GridCol
	@ColumnLable(name = "描述")
	@Column(name = "remark")
	private String remark;
	
 
	
	@Transient
	private CommonContent entityContent;
	@Transient
    private String templateStatusDsp;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	 

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public CommonContent getEntityContent() {
		return entityContent;
	}

	public void setEntityContent(CommonContent entityContent) {
		this.entityContent = entityContent;
	}

	public Boolean getTemplateStatus() {
		return templateStatus;
	}

	public void setTemplateStatus(Boolean templateStatus) {
		this.templateStatus = templateStatus;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Boolean getExtPropertyDefine() {
		if(CommonUtil.isNull(extPropertyDefine)) {
			return false;
		}
		return extPropertyDefine;
	}

	public void setExtPropertyDefine(Boolean extPropertyDefine) {
		this.extPropertyDefine = extPropertyDefine;
	}

	public String getTemplateStatusDsp() {
		if(CommonUtil.isNull(templateStatus)) {return "无效";}
		if(templateStatus) {
			return "有效";
		}
		return  "无效";
	}

	public void setTemplateStatusDsp(String templateStatusDsp) {
		this.templateStatusDsp = templateStatusDsp;
	}
 

}