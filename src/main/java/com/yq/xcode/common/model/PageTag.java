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
import com.yq.xcode.constants.YqSelectHardcodeConstants;
import com.yq.xcode.web.ui.annotation.ColumnLable;

@SuppressWarnings("serial")
@Entity
@Table(name = "YQ_PAGE_TAG")

@JsonIgnoreProperties(ignoreUnknown = true)
public class PageTag extends YqJpaBaseModel {
	
	@TableGenerator(name = "idGen", table = "ID_GENERATOR", pkColumnName = "ID_KEY", valueColumnName = "ID_VALUE", pkColumnValue = "YQ_PAGE_TAG_ID", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idGen")
	@Column(name = "ID")
	private Long id;	 
	 
 	@ColumnLable(name = "来源")
	@Column(name = "SOURCE_CATEGORY")
	private String sourceCategory;
 	
  
	@ColumnLable(name = "来源主key")
	@Column(name = "source_key")
	private String sourceKey;
  
	
	@ColumnLable(name = "序号")
	@Column(name = "line_number")
	private Integer lineNumber; 
	
	@ColumnLable(name = "分组顺序")
	@Column(name = "group_line_number")
	private Integer groupLineNumber; 
	/**
	 * 分组名称， 序号相同去一个名称， 注意设置
	 */
	@ColumnLable(name = "分组名称")
	@Column(name = "group_name")
	private String groupName;
	
	@ColumnLable(name = "分组备注")
	@Column(name = "GROUP_REMARK")
	private String groupRemark;
	
	@ColumnLable(name = "属性")
	@Column(name = "PROPERTY")
	private String property;
	
	/**
	 * 日期期间选择用
	 */
	@ColumnLable(name = "结束属性")
	@Column(name = "END_PROPERTY")
	private String endProperty;
 
	@ColumnLable(name = "显示控件")
	@Column(name = "TAG_KEY")
	private String tagKey;
 
	@ColumnLable(name = "显示的属性名称")
	@Column(name = "LABLE")
	private String lable;
	
	@ColumnLable(name = "后缀显示说明")
	@Column(name = "POST_LABLE")
	private String postLable;
	
	@ColumnLable(name = "长度")
	@Column(name = "LENGTH")
	private  Integer length;
	
	@ColumnLable(name = "行数")
	@Column(name = "TEXT_AREA_ROWS")
	private Integer textAreaRows = 3;
	
	@ColumnLable(name = "数量类型")
	@Column(name = "LIST_CATEGORY")
	private String listCategory;
	
	@ColumnLable(name = "读数链接")
	@Column(name = "read_url")
	private String readUrl;
	
	@ColumnLable(name = "值")
	@Column(name = "VALUE")
	private String value;
	
	/**
	 * 遮罩提示
	 */
	@ColumnLable(name = "遮罩提示")
	@Column(name = "PLACE_HOLDER") 
	private String placeHolder; //
	

	
	@ColumnLable(name = "宽度")
	@Column(name = "width") 
	private String width;
 
	@ColumnLable(name = "只读")
	@Column(name = "disabled") 
	private Boolean disabled = false;
	
 
	@ColumnLable(name = "附件显示方式 ")
	@Column(name = "showpic") 
	private Boolean showpic = false;
	
	@ColumnLable(name = "非空 ")
	@Column(name = "MANDATORY") 
	private Boolean mandatory;
 	
	/**
	 * 提示信息, 附件
	 * @return
	 */
	@ColumnLable(name = "提示信息, 附件")
	@Column(name = "attach_msg") 
	private String attachMsg;
	
	/**
	 * SCALE int comment '小数位' 小数位数，默认是2，
	 */
	@ColumnLable(name = "小数位")
	@Column(name = "SCALE")
	private Integer scale;
	
	@ColumnLable(name = "指定数据源")
	@Column(name = "READ_TYPE")
	private String readType;
	
	@ColumnLable(name = "Js计算")
	@Column(name = "js_Fun")
	private String jsFun;
	
	@ColumnLable(name = "默认值")
	@Column(name = "DEFAULT_VALUE")
	private String defaultValue;
	
	@ColumnLable(name = "指定数据源")
	@Column(name = "REMARK")
	private String remark;
	
   /**
    * 显示属性， 如果没有输入就是属性加 Dsp
    */
	@ColumnLable(name = "显示属性")
	@Column(name = "DISP_PROPERTY")
	private String dispProperty;
	
	/**
	 * popup 使用
	 */
	@ColumnLable(name = "actionName")
	@Column(name = "ACTION_NAME")
	private String actionName;
	
	/**
	 * 操作符， 定义 特殊criteria 用
	 */
	@Transient
	private String opeartion;
	
 

	public PageTag() {
		
	}
    public PageTag(String property, String lable, String tagKey) {
		this.property = property;
		this.lable = lable;
		this.tagKey = tagKey;
	}
    
    public PageTag(String property, String lable, String tagKey, String listCategory ) {
 		this(property,lable,tagKey); 
		this.listCategory = listCategory;
	}
    
    public PageTag(String property, String lable, String tagKey, String listCategory, String operation ) {
    	this(property,lable,tagKey,listCategory); 
		this.opeartion = operation;
	}
    
    
    public PageTag(String prp) {
		
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getTagKey() {
		return tagKey;
	}

	public void setTagKey(String tagKey) {
		this.tagKey = tagKey;
	}

	public String getLable() {
		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Integer getTextAreaRows() {
		return textAreaRows;
	}

	public void setTextAreaRows(Integer textAreaRows) {
		this.textAreaRows = textAreaRows;
	}

	public String getListCategory() {
		return listCategory;
	}

	public void setListCategory(String listCategory) {
		this.listCategory = listCategory;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getPlaceHolder() {
		return placeHolder;
	}

	public void setPlaceHolder(String placeHolder) {
		this.placeHolder = placeHolder;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public Boolean getShowpic() {
		return showpic;
	}

	public void setShowpic(Boolean showpic) {
		this.showpic = showpic;
	}

	public String getAttachMsg() {
		return attachMsg;
	}

	public void setAttachMsg(String attachMsg) {
		this.attachMsg = attachMsg;
	}

	public String getPostLable() {
		return postLable;
	}

	public void setPostLable(String postLable) {
		this.postLable = postLable;
	}

	public Boolean getMandatory() {
		return mandatory;
	}

	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}

	public String getSourceCategory() {
		return sourceCategory;
	}

	public void setSourceCategory(String sourceCategory) {
		this.sourceCategory = sourceCategory;
	}

	public String getReadUrl() {
		return readUrl;
	}

	public void setReadUrl(String readUrl) {
		this.readUrl = readUrl;
	}

	public Integer getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}
 
	public Integer getGroupLineNumber() {
		return groupLineNumber;
	}
	public void setGroupLineNumber(Integer groupLineNumber) {
		this.groupLineNumber = groupLineNumber;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public Integer getScale() {
		return scale;
	}
	public void setScale(Integer scale) {
		this.scale = scale;
	}
	
	@Transient
	public String getTagType() {
		return YqSelectHardcodeConstants.PageTag_tag.getTagType(this.tagKey); 
	}
	
	@Transient
	public String getTagName() {
		return CommonUtil.getSelectItemNameByKey(YqSelectHardcodeConstants.PageTag_tag.list, tagKey); 
	} 
	public String getEndProperty() {
		return endProperty;
	}
	public void setEndProperty(String endProperty) {
		this.endProperty = endProperty;
	}
	public String getOpeartion() {
		return opeartion;
	}
	public void setOpeartion(String opeartion) {
		this.opeartion = opeartion;
	}
	public String getSourceKey() {
		return sourceKey;
	}
	public void setSourceKey(String sourceKey) {
		this.sourceKey = sourceKey;
	}
	public String getReadType() {
		return readType;
	}
	public void setReadType(String readType) {
		this.readType = readType;
	}
	public String getJsFun() {
		return jsFun;
	}
	public void setJsFun(String jsFun) {
		this.jsFun = jsFun;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getMergeLable() {
		return this.lineNumber+"."+this.lable;
	}
	public String getDispProperty() {
		return dispProperty;
	}
	public void setDispProperty(String dispProperty) {
		this.dispProperty = dispProperty;
	} 
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	public String getGroupRemark() {
		return groupRemark;
	}
	public void setGroupRemark(String groupRemark) {
		this.groupRemark = groupRemark;
	}
}
