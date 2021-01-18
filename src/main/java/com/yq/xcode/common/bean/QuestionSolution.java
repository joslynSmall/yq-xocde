package com.yq.xcode.common.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.yq.xcode.common.model.YqJpaBaseModel;
import com.yq.xcode.web.ui.annotation.ColumnLable;

@SuppressWarnings("serial")
@Entity
@Table(name = "yq_question_solution")

public class QuestionSolution extends YqJpaBaseModel {

	@TableGenerator(name = "idGen", table = "ID_GENERATOR", pkColumnName = "ID_KEY", valueColumnName = "ID_VALUE", pkColumnValue = "yq_question_solution_ID", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idGen")
	@Column(name = "ID")
	private Long id;
	/**
	 * QUESTION_CATEGORY VARCHAR(40) comment '类型'
	 * 
	 */
	@ColumnLable(name = "类型")
	@Column(name = "QUESTION_CATEGORY")
	private String questionCategory;
	/**
	 * QUESTION_DESCRIPTION VARCHAR(800) comment '问题描述'
	 * 
	 */
	@ColumnLable(name = "问题描述")
	@Column(name = "QUESTION_DESCRIPTION")
	private String questionDescription;
	/**
	 * SOLUTION VARCHAR(8000) comment '解决办法'
	 * 
	 */
	@ColumnLable(name = "解决办法")
	@Column(name = "SOLUTION")
	private String solution;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getQuestionCategory() {
		return questionCategory;
	}
	public void setQuestionCategory(String questionCategory) {
		this.questionCategory = questionCategory;
	}
	public String getQuestionDescription() {
		return questionDescription;
	}
	public void setQuestionDescription(String questionDescription) {
		this.questionDescription = questionDescription;
	}
	public String getSolution() {
		return solution;
	}
	public void setSolution(String solution) {
		this.solution = solution;
	}

}