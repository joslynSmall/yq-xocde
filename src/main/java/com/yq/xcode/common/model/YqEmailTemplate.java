package com.yq.xcode.common.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
@SuppressWarnings("serial")
@Entity
@Table(name = "YQ_EMAIL_TEMPLATE")
public class YqEmailTemplate  implements Serializable {


	@Id
	@Column(name="template_code",length=40)
	private String templateCode;
	
	/**
	 * 名称
	 */
	@Column(name = "template_name", nullable = false)
	private String templateName;
	
	/**
	 * 类型, 和审批类型相同
	 */
	@Column(name = "template_category", nullable = false)
	private String templateCategory;
	
	/**
	 * from
	 */
	@Column(name = "email_from", nullable = false)
	private String emailFrom;
	
	/**
	 * to
	 */
	@Column(name = "email_to", nullable = false)
	private String emailTo;
	
	/**
	 * cc
	 */
	@Column(name = "email_cc",  nullable = false)
	private String cc;
	
	/**
	 * 标题
	 */
	@Column(name = "`subject`", length = 800, nullable = true)
	private String subject;
	
	/**
	 * 类容 content
	 */
	@Column(name = "template_content", length = 40, nullable = true)
	public String templateContent;

	/**
	 * 描述
	 */
	@Column(name = "description",  nullable = true)
	public String description;
	@Transient
	private String templateCategoryName;
	
	public String getTemplateCategory() {
		return templateCategory;
	}

	public void setTemplateCategory(String templateCategory) {
		this.templateCategory = templateCategory;
	}

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}



	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getEmailFrom() {
		return emailFrom;
	}

	public void setEmailFrom(String emailFrom) {
		this.emailFrom = emailFrom;
	}

	public String getEmailTo() {
		return emailTo;
	}

	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTemplateContent() {
		return templateContent;
	}

	public void setTemplateContent(String templateContent) {
		this.templateContent = templateContent;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	public String getTemplateCategoryName() {
		return templateCategoryName;
	}

	public void setTemplateCategoryName(String templateCategoryName) {
		this.templateCategoryName = templateCategoryName;
	}

	public void print() {
		System.out.println(" --------Mail info -------------");
		System.out.println(" From : "+this.emailFrom);
		System.out.println(" To : "+this.emailTo);
		System.out.println(" Cc : "+cc);
		System.out.println(" Subject : "+subject);
		System.out.println(" Context : "+this.templateContent);
		System.out.println(" -------------------------------");		
		
	}
	
 

}
