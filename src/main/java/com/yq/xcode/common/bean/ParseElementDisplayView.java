package com.yq.xcode.common.bean;

import java.util.List;

import com.yq.xcode.common.model.ParseElement;



public class ParseElementDisplayView {
	private ParseElement parseElement;
	private Object value;
	/**
	 * 替换掉所有的ELEMENT{eleNumber} 为 ： {eleName:value}
	 */
	private String displayExpress;
	
	/**
	 * 替换掉所有的ELEMENT{eleNumber} 替换最明细的计算方式
	 */
	//private String displayFullExpress;
	
    private List<ParseElementDisplayView> funList;

	public ParseElement getParseElement() {
		return parseElement;
	}

	public void setParseElement(ParseElement parseElement) {
		this.parseElement = parseElement;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getDisplayExpress() {
		return displayExpress;
	}

	public void setDisplayExpress(String displayExpress) {
		this.displayExpress = displayExpress;
	}
	
	public String toString() {
		return "　　　　<span style='color:blue;'>函数 :</span> "+this.parseElement.getEleName()+"("+this.parseElement.getEleNumber()+")"+"<br/>"+
		       "　　　<span style='color:blue;'>结果值 :</span> "+this.value+"<br/>"+
		       "<span style='color:blue;'>表达式元素值 :</span> " +this.getDisplayExpress()+"<br/>";
	}

	public List<ParseElementDisplayView> getFunList() {
		return funList;
	}

	public void setFunList(List<ParseElementDisplayView> funList) {
		this.funList = funList;
	}
 
 
}
