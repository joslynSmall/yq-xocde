package com.yq.xcode.common.service;





public interface GenTableAndModelService {

	public void run();
	
	/**
	 * 
	 * @param sheetName, 表名， 对象名都取决于此名称
	 * @return
	 */
	
	public String genNewModel(String sheetName);
	
	/**
	 * gen table and model 
	 * @param sheetName
	 * @return
	 */
	
	public String genSingleModelAndTable(String sheetName) ;

	public String dropNoUseColumns(Class clazz, String dbName);

	public String dropNoUseColumnsByPackage(String packageName, String dbName);

}
