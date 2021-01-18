package com.yq.xcode.common.bean;

import java.util.List;

import com.yq.xcode.common.base.XBaseView;

/**
 * 类型为系统hardcode，新的不同类型的解析要用不同的定义，开发人员
 *
 */
public class ParseElementUse <E>  extends XBaseView { 
	private String code;
	private String name;
 	
	//描述
	private String description;
	/**
	 * 为函数定义的属性，在页面上可选的函数列表用
	 * itemKey: "properyName"
	 * itemName: "propertyLable"
       itemValue: "DataType"}
	 */
	private List<SelectItem> properties;
	
	/**
	 * 测试对象
	 */
	private Object testEntity;
	
	public ParseElementUse() {
		
	}
    public ParseElementUse(String code, String name, List<SelectItem> properties,Object testEntity) {
		this.code = code;
		this.name = name;  
		this.properties = properties;
		this.testEntity = testEntity;
	} 

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
 
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<SelectItem> getProperties() {
		return properties;
	}
	public void setProperties(List<SelectItem> properties) {
		this.properties = properties;
	}
	public Object getTestEntity() {
		return testEntity;
	}
	public void setTestEntity(Object testEntity) {
		this.testEntity = testEntity;
	}
	 
 
}
