package com.yq.xcode.common.model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yq.xcode.common.bean.ParseFunction;
import com.yq.xcode.common.bean.ParseParameter;
import com.yq.xcode.common.bean.ParseUnit;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.constants.YqSelectHardcodeConstants;
import com.yq.xcode.web.ui.annotation.ColumnLable;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown=true)
@Entity
@Table(name = "YQ_PARSE_ELEMENT")

public class ParseElement extends YqJpaBaseModel implements ParseParameter,
		ParseUnit, ParseFunction {
	
	@Id
	@Column(name = "ID")
	private Long id;

	/**
	 * ELE_NUMBER VARCHAR(20) not null comment '编码' , PRIMARY KEY (ELE_NUMBER)
	 * 以元素类型为首字母,也是KEY,有系统录入，所以不用ID
	 */
	
	@ColumnLable(name = "编码")
	@Column(name = "ELE_NUMBER")
	private String eleNumber;
	
 	
	/**
	 * ELE_NAME VARCHAR(200) not null comment '名称'
	 * 
	 */
	@ColumnLable(name = "名称")
	@Column(name = "ELE_NAME")
	private String eleName;
	
	
	
	@ColumnLable(name = "数据提供者名称, 接口名称")
	@Column(name = "PROVIDER_NAME")
	private String providerName;
 	/**
	 * 显示顺序
	 */
	@ColumnLable(name = "显示顺序")
	@Column(name = "LINE_NUMBER")
	private Integer lineNumber;
	
	/**
	 * DESCRIPTION VARCHAR(800) comment '描述'
	 * 
	 */
	@ColumnLable(name = "描述")
	@Column(name = "DESCRIPTION")
	private String description;
	/**
	 * ELE_CATEGORY VARCHAR(20) comment '类型' HARDCODE 元素类型， F - 函数 U -
	 * 因子,取数的最小单位，一般是系统hardcode P - 参数
	 */
	@ColumnLable(name = "类型")
	@Column(name = "ELE_CATEGORY")
	private String eleCategory;
	/**
	 * USE_CATEGORY VARCHAR(20) comment '用途类型' HARDCODE 用途，表达式用途,
	 * 例如返利、方案、订单赠送等,代表数据来源，同一数据来源的用途类型是一样的
	 */
	@ColumnLable(name = "用途类型")
	@Column(name = "USE_CATEGORY")
	private String useCategory;
	/**
	 * USE_PROPERTY_NAME VARCHAR(40) comment '使用的property name'
	 * HARDCODE,为哪一个属性设置的函数， 只有函数有此属性，这样每个property 可选的函数就不多了
	 */
	@ColumnLable(name = "使用的property name")
	@Column(name = "USE_PROPERTY_NAME")
	private String usePropertyName;
	
	@Transient
	private String usePropertyDspName;
	
	/**
	 * SYSTEM int comment '是否系统'
	 * 
	 */
	@ColumnLable(name = "是否系统")
	@Column(name = "SYSTEM")
	private Boolean system;
	/**
	 * 返回数据列表，表达元素的数据来源,我们提供sql 方法，#pt.getData('select * from .. where {U01}') from dual');
	 * 现在不提供间接数据来源的体现
	 */
	/*
	@ColumnLable(name = "数据源表达式")
	@Column(name = "DATA_SOURCE_EXPRESS")
	private String dataSourceExpress;
	*/
	/**
	 * PROPERTY_NAME VARCHAR(80) comment '属性名称' U(因子)是因子用,P(参数) 返回字符值，value F(函数) 返回表达式
	 */
	@ColumnLable(name = "表达式")
	@Column(name = "express")
	private String express;
	/**
	 * DATA_TYPE VARCHAR(20) comment '数据类型' T - Time 事件 yyyy-MM-dd -24hh:mi:ms D -
	 * Date 日期类型 N - Number 数据类型 C - Char 字符类型 B - Boolean O - Object
	 * 对象，一般是用在系统hardcode的方法中用 NV - 无返回值，是方法
	 * PN Percentage Number - 百分比
	 * 
	 */
	@ColumnLable(name = "数据类型")
	@Column(name = "DATA_TYPE")
	private String dataType;
	
	@ColumnLable(name = "单位名称")
	@Column(name = "UNIT_NAME")
	private String unitName;
	
	
	/**
	 * 对元素， 和 参数有用， 
	 */
	@ColumnLable(name = "页面控件ID")
	@Column(name = "PAGE_TAG_ID")
	private Long pageTagId;
	
	/**
	 * SCALE int comment '小数位' 小数位数，默认是2，
	 */
	@ColumnLable(name = "小数位")
	@Column(name = "SCALE")
	private Integer scale;
	/**
	 * DELETED int comment '禁用'
	 * 
	 */
	@ColumnLable(name = "禁用")
	@Column(name = "DELETED")
	private Boolean deleted;
	/**
	 * PARAMETERS VARCHAR(400) comment '参数' 有些函数或因子是系统代码级别的，可能会用到参数，但分析不出来，
	 * 此时可以在此指定，多个用“，”分隔， 一般只有开发人员可以知道
	 */
	@ColumnLable(name = "参数")
	@Column(name = "PARAMETERS")
	private String parameters;
	@Transient
	private String parametersDsp;
	/**
	 * CHECKED_BY VARCHAR(40) comment '审核人'
	 * 
	 */
	@ColumnLable(name = "审核人")
	@Column(name = "CHECKED_BY")
	private String checkedBy;
	/**
	 * CHECKED_DATE datetime comment '审核日期'
	 * 
	 */
	@ColumnLable(name = "审核日期")
	@Column(name = "CHECKED_DATE")
	private Date checkedDate;
	
 
	/**
	 * 替代原值的新的参数值, 不保存数据库
	 * @return
	 */
	@Transient
	private String newValue;
	
	/**
	 * 表达式 句译
	 * **/
	@Transient
	private String translateExpress;
	
	@Transient
	private List<String> sourceStatusesList;
	
	@Transient
	private String codeName;
	
	@Transient
	private String eleCodeName;
	
	public String getEleCodeName() {
		return this.eleNumber+"-"+eleName;
	}
	
	public String getUsePropertyDspName() {
		return usePropertyDspName;
	}
	public void setUsePropertyDspName(String usePropertyDspName) {
		this.usePropertyDspName = usePropertyDspName;
	}
	
	public List<String> getSourceStatusesList() {
		if(CommonUtil.isNull(sourceStatusesList)&&CommonUtil.isNotNull(parameters)){
			String[]  a = parameters.split(",");
			sourceStatusesList = Arrays.asList(a);
		}
		return sourceStatusesList;
	}
	public void setSourceStatusesList(List<String> sourceStatusesList) {
		this.sourceStatusesList = sourceStatusesList;
	}
	
	public String getTranslateExpress() {
		return translateExpress;
	}

	public void setTranslateExpress(String translateExpress) {
		this.translateExpress = translateExpress;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public String getEleNumber() {
		return eleNumber;
	}

	public void setEleNumber(String eleNumber) {
		this.eleNumber = eleNumber;
	}

	public String getEleName() {
		return eleName;
	}

	public void setEleName(String eleName) {
		this.eleName = eleName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEleCategory() {
		return eleCategory;
	}

	public void setEleCategory(String eleCategory) {
		this.eleCategory = eleCategory;
	}

	public String getUseCategory() {
		return useCategory;
	}

	public void setUseCategory(String useCategory) {
		this.useCategory = useCategory;
	}

	public String getUsePropertyName() {
		return usePropertyName;
	}

	public void setUsePropertyName(String usePropertyName) {
		this.usePropertyName = usePropertyName;
	}

	public Boolean getSystem() {
		return system;
	}

	public void setSystem(Boolean system) {
		this.system = system;
	}

	public String getExpress() {
		return express;
	}

	public void setExpress(String express) {
		this.express = express;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Integer getScale() {
		return scale;
	}

	public void setScale(Integer scale) {
		this.scale = scale;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public String getCheckedBy() {
		return checkedBy;
	}

	public void setCheckedBy(String checkedBy) {
		this.checkedBy = checkedBy;
	}

	public Date getCheckedDate() {
		return checkedDate;
	}

	public void setCheckedDate(Date checkedDate) {
		this.checkedDate = checkedDate;
	}

 	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDataTypeDescription() {
		return CommonUtil.getSelectItemNameByKey(YqSelectHardcodeConstants.PropertyType.list, dataType);
		  
	}
	
	public String getSystemDescription() {
		if(CommonUtil.isNotNull(system)){
			if(system==true){
				return "是";
			}else if(system==false){
				return "否";
			}
		}
		return null;
	}
	
	public String getDeletedDescription() {
		if(CommonUtil.isNotNull(deleted)){
			if(deleted==true){
				return "是";
			}else if(deleted==false){
				return "否";
			}
		}
		return null;
	}

	public String getParametersDsp() {
		return parametersDsp;
	}

	public void setParametersDsp(String parametersDsp) {
		this.parametersDsp = parametersDsp;
	}
	
 
	public String getProviderName() {
		return providerName;
	}
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
	public Integer getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}
	public String getDiscountCategory() {
		/*
		if (ParseConstants.ELE_CATEGORY_FULL_DISCOUNT_CONDITION.equals(useCategory) ||
				ParseConstants.ELE_CATEGORY_SINGLE_DISCOUNT_CONDITION.equals(useCategory)) {
			if (ParseConstants.ELEMENT_PROGRAM_FULL_ITEM_DISCOUNT.equals(usePropertyName) || 
					ParseConstants.ELEMENT_PROGRAM_SINGLE_ITEM_DISCOUNT.equals(usePropertyName) ) {
				return SyConstants.PROGRAM_DISCOUNT_TYPE_ITEM;
			}
			if (ParseConstants.ELEMENT_PROGRAM_FULL_PRICE_DISCOUNT.equals(usePropertyName) || 
					ParseConstants.ELEMENT_PROGRAM_SINGLE_PRICE_DISCOUNT.equals(usePropertyName) ) {
				return SyConstants.PROGRAM_DISCOUNT_TYPE_PRICE;
			}
		} 
		*/
		return null;
	}
	public String getCodeName() {
		return "("+this.getEleNumber()+") " + this.getEleName();
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public Long getPageTagId() {
		return pageTagId;
	}
	public void setPageTagId(Long pageTagId) {
		this.pageTagId = pageTagId;
	} 
	
	
	

}