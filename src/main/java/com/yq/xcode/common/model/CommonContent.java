package com.yq.xcode.common.model;
	
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yq.xcode.web.ui.annotation.ColumnLable;

@SuppressWarnings("serial")
@Entity
@Table(name = "YQ_COMMON_CONTENT")
@JsonIgnoreProperties(ignoreUnknown=true)
/**
 * 附件表
 * @author jettie 
 */
public class CommonContent  extends YqJpaBaseModel{

	@TableGenerator(name = "idGen", table = "ID_GENERATOR", pkColumnName = "ID_KEY", valueColumnName = "ID_VALUE", pkColumnValue = "GC_COMMON_LETTERS_CONTENT_ID", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idGen")
	@Column(name = "ID")
	private Long id;

	/**
	 *  
	 */
	@ColumnLable(name = "来源ID")
	@Column(name = "SOURCE_ID")
	private Long sourceId;
	
	/**
	 *  
	 */
	@ColumnLable(name = "ID")
	@Column(name = "SOURCE_CATEGORY")
	private String sourceCategory;
	
	/**
	 *  
	 */
	@ColumnLable(name = "函件内容")
	@Column(name = "CONTENT")
	private String content;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	public String getSourceCategory() {
		return sourceCategory;
	}

	public void setSourceCategory(String sourceCategory) {
		this.sourceCategory = sourceCategory;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
 
}
