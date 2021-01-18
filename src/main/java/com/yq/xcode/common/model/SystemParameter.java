package com.yq.xcode.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.security.entity.JpaAuditableModel;

@SuppressWarnings("serial")
@Entity
@Table(name = "YQ_SYSTEM_PARAMETER")
public class SystemParameter extends JpaAuditableModel {

	@Id
	@Column(name = "KEY_CODE", length = 40)
	private String keyCode;

	/**
	 * 参数名称
	 */
	@Column(name = "PARAMETER_NAME", length = 100, nullable = false)
	private String parameterName;

	/**
	 * 参数值
	 */
	@Column(name = "PARAMETER_VALUE", length = 200, nullable = false)
	private String parameterValue;

	/**
	 * 序号
	 */
	@Column(name = "LINE_NUMBER", nullable = false)
	private Integer lineNumber;

	/**
	 * 描述
	 */
	@Column(name = "DESCRIPTION")
	private String description;

	/**
	 * 类型
	 */
	@Column(name = "CATEGORY")
	private String category;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 数据类型 C-字符 N-数字 D-日期(YYYY-MM-DD) T-时分(HH24:MM) DT-日期时间(YYYY-MM-DD HH24:MM)
	 */
	@Column(name = "DATA_TYPE", nullable = false)
	private String dataType;

	public String getKeyCode() {
		return keyCode;
	}

	public void setKeyCode(String keyCode) {
		this.keyCode = keyCode;
	}

	public Integer getLineNumber() {
		if (CommonUtil.isNull(lineNumber)) {
			return 0;
		}
		return lineNumber;
	}

	public void setLineNumber(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public String getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}

	public String getDataType() {
		return dataType;
	}

	public String getDataType2String() {
		if ("C".equals(dataType)) {
			return "字符串";
		} else if ("N".equals(dataType)) {
			return "数值";
		} else if ("D".equals(dataType)) {
			return "yyyy-MM-dd";
		} else if ("T".equals(dataType)) {
			return "HH:mm";
		} else if ("DT".equals(dataType)) {
			return "yyyy-MM-dd HH:mm";
		}
		return "";
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
