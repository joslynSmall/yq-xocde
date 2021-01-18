package com.yq.xcode.common.bean;



public interface ParseParameter extends ParseBaseElement {
	

	
    public  void setNewValue(String newValue);
	/**
	 * 替代原值的新的参数值, 不保存数据库
	 * @return
	 */
	public  String getNewValue();

}
