package com.yq.xcode.common.bean;

import java.util.ArrayList;
import java.util.List;

import com.yq.xcode.common.model.PageTag;
/**
 * 查询页面定义
 * @author jettie
 *
 */
public class ListPageDefine {
	private List<HColumn> columnList = new ArrayList<HColumn>();
	private List<PageTag> pageTagList = new ArrayList<PageTag>();
	
	public List<HColumn> getColumnList() {
		return columnList;
	}
	public void setColumnList(List<HColumn> columnList) {
		this.columnList = columnList;
	}
	public List<PageTag> getPageTagList() {
		return pageTagList;
	}
	public void setPageTagList(List<PageTag> pageTagList) {
		this.pageTagList = pageTagList;
	} 
}
