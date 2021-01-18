package com.yq.xcode.common.model;

import java.math.BigDecimal;
import java.util.Date;

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
import com.yq.xcode.common.utils.JsUtil;
import com.yq.xcode.security.entity.JpaBaseModel;
import com.yq.xcode.web.ui.annotation.ColumnLable;

@SuppressWarnings("serial")
@Entity
@Table(name="YQ_LOOKUP_CODE") 
@JsonIgnoreProperties(ignoreUnknown = true)
public class LookupCode extends JpaBaseModel {

	@TableGenerator(name = "idGen", table = "ID_GENERATOR", pkColumnName = "ID_KEY", valueColumnName = "ID_VALUE", pkColumnValue = "LOOKUP_CODE_ID", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idGen")
	@Column(name = "ID")
	private Long id;
	
	@ColumnLable(name="key",canNotDefine = true)
	@Column(name="KEY_CODE",length=40)
	private String keyCode;
	
	@ColumnLable(name="parentKeyCode",canNotDefine = true)
	@Column(name="PARENT_KEY_CODE",length=40)
	private String parentKeyCode;
	
	/**
	 * 类型代码
	 */
	@Column(name = "CATEGORY_CODE", length = 20, nullable = false)
	private String categoryCode;
	
	/**
	 * 代码
	 */
	@ColumnLable(name="代码" )
	@Column(name = "LOOKUP_CODE", length = 20, nullable = false)
	private String lookupCode;
	
	/**
	 * 行号
	 */
	@Column(name = "LINE_NUMBER", length = 20, nullable = false)
	private Integer lineNumber;
	
	/**
	 * 名称
	 */
	@ColumnLable(name="名称" )
	@Column(name = "LOOKUP_NAME", length = 80, nullable = false)
	private String lookupName;
	
	
	
	
	/**
	 * 描述
	 */
	@Column(name = "DESCRIPTION", length = 800, nullable = true)
	private String description;
	
	/**
	 * 特殊备注
	 */
	@ColumnLable(name="扩展1",extProperty = true)
	@Column(name = "SEGMENT1", length = 800, nullable = true)
 	private String segment1;
	@ColumnLable(name="扩展2",extProperty = true)
	@Column(name = "SEGMENT2", length = 400, nullable = true)
	private String segment2;
	@ColumnLable(name="扩展3",extProperty = true)
	@Column(name = "SEGMENT3", length = 400, nullable = true)
 	private String segment3;
	@ColumnLable(name="扩展4",extProperty = true)
	@Column(name = "SEGMENT4", length = 400, nullable = true)
	private String segment4;
	@ColumnLable(name="扩展5",extProperty = true)
	@Column(name = "SEGMENT5", length = 400, nullable = true)
	private String segment5;
	
	@Column(name = "SEGMENT6", length = 400, nullable = true)
	private String segment6;
	@Column(name = "SEGMENT7", length = 400, nullable = true)
	private String segment7;
	@Column(name = "SEGMENT8", length = 400, nullable = true)
	private String segment8;
	@Column(name = "SEGMENT9", length = 400, nullable = true)
	private String segment9;
	
	@Column(name = "NUMBER_COL1", length = 400, nullable = true)
	private BigDecimal numberCol1;
	@Column(name = "NUMBER_COL2", length = 400, nullable = true)
	private BigDecimal numberCol2;
	@Column(name = "NUMBER_COL3", length = 400, nullable = true)
	private BigDecimal numberCol3;
	
	@Column(name = "DATE_COL1", length = 400, nullable = true)
	private Date dateCol1;
	@Column(name = "DATE_COL2", length = 400, nullable = true)
	private Date dateCol2;
	
	
	@Transient
	private String parentCode;
	@Transient
	private Long skipPageDefineId;
	
	@Transient
	private String parentName;
	
	@Transient
	public String getCategoryName(){
		return lookupName;
	}
	
	public String getSegment1() {
		return segment1;
	}

	public void setSegment1(String segment1) {
		this.segment1 = segment1;
	}

	/**
	 * SYS : 系统hardcode, 不可修改
	 * CUST: 用户定义， 可修改
	 */
	@Column(name = "SYS_TYPE", length = 40, nullable = true)
	public String sysType;

	/**
	 * 禁用
	 */
	@Column(name = "DELETED")
	private Boolean deleted;
	
	@Column(name="LOOKUP_LEVEL",length=40)
	private Integer lookupLevel;
	
	@Transient
	private String actionPreMsg;
	
	@Transient
	private Boolean reasonMandatory;

	@Transient
	private Long maxLevel;
	
	@Transient
	private String segmentDesc;
	
	public Boolean getDeleted() {
		if (deleted == null) {
			return false;
		}
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public String getSysType() {
		return sysType;
	}

	public void setSysType(String sysType) {
		this.sysType = sysType;
	}

	public String getKeyCode() {
		return keyCode;
	}

	public void setKeyCode(String keyCode) {
		this.keyCode = keyCode;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getLookupCode() {
		return lookupCode;
	}

	public void setLookupCode(String lookupCode) {
		this.lookupCode = lookupCode;
	}

	public String getLookupName() {
		return lookupName;
	}

	public void setLookupName(String lookupName) {
		this.lookupName = lookupName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getActionPreMsg() {
		return actionPreMsg;
	}

	public void setActionPreMsg(String actionPreMsg) {
		this.actionPreMsg = actionPreMsg;
	}
	@Transient
	public String getShortPinyin() {
		return JsUtil.getPinYinHeadChar(this.getLookupName());
	}
	public void setShortPinyin(String shortPinyin) {
		
	}

	@Override
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getReasonMandatory() {
		if(CommonUtil.isNull(reasonMandatory))
		{
			return false;
		}
		return reasonMandatory;
	}

	public void setReasonMandatory(Boolean reasonMandatory) {
		this.reasonMandatory = reasonMandatory;
	}

	public String getSegment2() {
		return segment2;
	}

	public void setSegment2(String segment2) {
		this.segment2 = segment2;
	}

	public String getSegment3() {
		return segment3;
	}

	public void setSegment3(String segment3) {
		this.segment3 = segment3;
	}

	public String getSegment4() {
		return segment4;
	}

	public void setSegment4(String segment4) {
		this.segment4 = segment4;
	}

	public String getSegment5() {
		return segment5;
	}

	public void setSegment5(String segment5) {
		this.segment5 = segment5;
	}

	public String getSegment6() {
		return segment6;
	}

	public void setSegment6(String segment6) {
		this.segment6 = segment6;
	}

	public BigDecimal getNumberCol1() {
		return numberCol1;
	}

	public void setNumberCol1(BigDecimal numberCol1) {
		this.numberCol1 = numberCol1;
	}

	public BigDecimal getNumberCol2() {
		return numberCol2;
	}

	public void setNumberCol2(BigDecimal numberCol2) {
		this.numberCol2 = numberCol2;
	}

	public BigDecimal getNumberCol3() {
		return numberCol3;
	}

	public void setNumberCol3(BigDecimal numberCol3) {
		this.numberCol3 = numberCol3;
	}

	public Date getDateCol1() {
		return dateCol1;
	}

	public void setDateCol1(Date dateCol1) {
		this.dateCol1 = dateCol1;
	}

	public Date getDateCol2() {
		return dateCol2;
	}

	public void setDateCol2(Date dateCol2) {
		this.dateCol2 = dateCol2;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}


	public void setSegmentDesc(String segmentDesc) {
		this.segmentDesc = segmentDesc;
	}

	public Long getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(Long maxLevel) {
		this.maxLevel = maxLevel;
	}
	
	public Integer getLookupLevel() {
		return lookupLevel;
	}

	public void setLookupLevel(Integer lookupLevel) {
		this.lookupLevel = lookupLevel;
	}

	public String getParentKeyCode() {
		return parentKeyCode;
	}

	public void setParentKeyCode(String parentKeyCode) {
		this.parentKeyCode = parentKeyCode;
	}

 

	public String getSegment7() {
		return segment7;
	}

	public void setSegment7(String segment7) {
		this.segment7 = segment7;
	}

	public String getSegment8() {
		return segment8;
	}

	public void setSegment8(String segment8) {
		this.segment8 = segment8;
	}

	public String getSegment9() {
		return segment9;
	}

	public void setSegment9(String segment9) {
		this.segment9 = segment9;
	}

	public Long getSkipPageDefineId() {
		return skipPageDefineId;
	}

	public void setSkipPageDefineId(Long skipPageDefineId) {
		this.skipPageDefineId = skipPageDefineId;
	}

	public String getSegmentDesc() {
		return segmentDesc;
	}
	
	
	
	
}

