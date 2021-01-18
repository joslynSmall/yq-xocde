package com.yq.xcode.common.bean;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

import com.yq.xcode.common.model.PageTag;
 
public class SelectItemDefine implements Serializable { 
	 private String code; 
	 private String name;
	 /**
		 * 付款状态，草稿，已提交
		 * */
	public enum SourceCategory{
			HARDCODE, //固定的， 要直接定义一个列表
			QUERY   //来自一段Query， 返回对象是itemKey, itemValue 
	}
	@Enumerated(EnumType.STRING) 
	private SourceCategory sourceCategory;
	
	private String selectUnitCode; 
	 
	private String query; 
	 
	private HColumn[] columns;
	/**
	 * 查询字段
	 */
	private PageTag[] pageTags;
	
	/**
	 * harcode 
	 * 定义的 select item 用
	 */
	@Transient
	private List<SelectItem> selectItemList;
	
	
	public SelectItemDefine() {
		
	}
   public SelectItemDefine(String code, String name, List<SelectItem> selectItemList,HColumn[] columns) {
	   this.sourceCategory = SourceCategory.HARDCODE;
	   this.code = code;
	   this.name = name;
	   this.selectItemList = selectItemList;
	   this.columns = columns;
	}
    public SelectItemDefine(String code, String name, String query,HColumn[] columns) {
	   this.sourceCategory = SourceCategory.QUERY;
	   this.code = code;
	   this.name = name;
	   this.query = query;
	   this.columns = columns; 
	}
    
    public SelectItemDefine(String code, String name, String query,HColumn[] columns,PageTag[] pageTags) {
       this(  code,   name,   query, columns); 
	   this.pageTags = pageTags;
	}
    
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public SourceCategory getSourceCategory() {
		return sourceCategory;
	}
	public void setSourceCategory(SourceCategory sourceCategory) {
		this.sourceCategory = sourceCategory;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
 
	 
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
 
	public String getSelectUnitCode() {
		return selectUnitCode;
	}
	public void setSelectUnitCode(String selectUnitCode) {
		this.selectUnitCode = selectUnitCode;
	}
	public List<SelectItem> getSelectItemList() {
		return selectItemList;
	}
	public void setSelectItemList(List<SelectItem> selectItemList) {
		this.selectItemList = selectItemList;
	}
	public HColumn[] getColumns() {
		return columns;
	}
	public void setColumns(HColumn[] columns) {
		this.columns = columns;
	}
	public PageTag[] getPageTags() {
		return pageTags;
	}
	public void setPageTags(PageTag[] pageTags) {
		this.pageTags = pageTags;
	} 
	
}
