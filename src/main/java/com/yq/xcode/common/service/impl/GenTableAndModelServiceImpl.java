package com.yq.xcode.common.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Query;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yq.xcode.common.bean.PropertyDefine;
import com.yq.xcode.common.service.GenTableAndModelService;
import com.yq.xcode.common.service.SqlToModelService;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.GenTableAndModelUtil;
import com.yq.xcode.common.utils.YqBeanUtil;
import com.yq.xcode.common.utils.YqBeanUtilsEx;



@Service("GenTableAndModelService")
public class GenTableAndModelServiceImpl extends YqJpaDataAccessObject implements GenTableAndModelService{
    private static final String fileName = "D:/workspace/candao-settle-web/doc/dcjms_数据库表结构.xlsx";
    
    @Autowired
    private SqlToModelService sqlToModelService;
    @Override
	public void run() {
		String[]  sheetNameArr = new String[] {
				//"SO_SALE_ORDER_HEADER",
				//"SO_SALE_ORDER_LINE"
				//"SO_RETURN_ORDER",
				//"SO_RETURN_ORDER_DETAIL"
				//"SO_TRANSFER_ORDER_DETAIL"
				//"SO_TRANSFER_ORDER"
				//"SO_RECEIPT_DETAIL"
				//"SO_RECEIPT_APP"
				//"SO_USER"
				//"SO_VENDOR_BY_INVENTORY",
				//"SO_ITEM",
				//"SO_INVENTORY_QUANTITY"	,			
				//"SO_INVENTORY_TRANSACTION"				
		};
		for (String sheetName : sheetNameArr) {
			this.genNewModel(sheetName);
		}		
		
				
	}
	public String genNewModel(String sheetName) {
		
		String resultStr = "Gen All models ";
		//resultStr = this.genSingleModel(sheetName);
		/*
		if (CommonUtil.isNull(sheetName)) {
			for (String mName : this.allModels()) {
				this.genSingleModel(mName);
			}
		} else {
			resultStr = this.genSingleModel(sheetName);
		}*/
		return resultStr;
	}
	
	public String genTable(String sheetName) {
		System.out.println("------------>"+sheetName);
		String resultStr = "";
		resultStr = resultStr +"\r\n"+ sheetName;
		String createTable = GenTableAndModelUtil.genTableDll(fileName, sheetName,2);
		resultStr = resultStr +"\r\n"+ "-----------"+sheetName+" start----------------------------";
		resultStr = resultStr +"\r\n"+ createTable;
		resultStr = resultStr +"\r\n"+ "--------------------------------------------";	

	//	Query dropTableQ = this.em.createNamedQuery(" drop table  IF EXISTS "+sheetName);
	//	dropTableQ.executeUpdate();
		Query createQ = this.em.createNativeQuery(createTable);
		createQ.executeUpdate();
		return resultStr;
		
//		return genModelFile(sheetName, resultStr);
	}
	
	public String genSingleModel(String sheetName ) {
		String resultStr = "";
		String createJava = GenTableAndModelUtil.genModelFile(fileName, sheetName,2);
		resultStr = resultStr +"\r\n"+ createJava;
		resultStr = resultStr +"\r\n"+ "-----------"+sheetName+" end----------------------------";
		return resultStr;
	}
	
	@Override
	public String dropNoUseColumns(Class clazz, String dbName) {
		List<PropertyDefine> pList = YqBeanUtil.genEntityDefine(clazz );
		Table table = (Table)clazz.getAnnotation(Table.class);
		if (CommonUtil.isNull(pList) || table == null) {
			return clazz.getName()+"不是数据库的模型\r\n";
		}
		String tableName = table.name();
		String query = "select table_name lable, upper(COLUMN_NAME) property "
				+ " from information_schema.`COLUMNS` "
				+ " where UPPER(table_name) = UPPER('"+tableName+"') "
				+"   and TABLE_SCHEMA ='"+dbName+"' ";
		List<PropertyDefine> colList = this.sqlToModelService.executeNativeQuery(query, null, PropertyDefine.class);
		Map<String,PropertyDefine> pMap = new HashMap<String,PropertyDefine>();
		for (PropertyDefine pd : pList ) {
			pMap.put(pd.getColumn().toUpperCase(), pd);
		}
		
		for (PropertyDefine pCol : colList ) {
			String column = pCol.getProperty();
			if (!pMap.keySet().contains(column)) {
				System.out.println("不存在的列"+column+" 已删除 ！\r\n");
				Query createQ = this.em.createNativeQuery("alter table "+tableName+" DROP COLUMN  `"+column+"` ");
				createQ.executeUpdate();
			}
		}
		return clazz.getSimpleName()+" 已清理 ！\r\n";
 	}
 
	@Override
	public String dropNoUseColumnsByPackage(String packageName, String dbName) {
		Set<Class<?>> clazzSet = YqBeanUtilsEx.getClasses(packageName);
		Iterator it = clazzSet.iterator();
		String msg = "";
		while (it.hasNext()) {
			Class clazz = (Class)it.next();
			msg = msg+"\r\n"+this.dropNoUseColumns(clazz,dbName);
		}
		return msg;
	}
	@Override
	public String genSingleModelAndTable(String sheetName) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
