package com.yq.xcode.common.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import com.yq.xcode.common.bean.CellProperty;
import com.yq.xcode.common.bean.TableDllView;


public class GenTableAndModelUtil { 
	
	private static final String defaultModel = " package com.dossen.franchisee.model.common;                             "
			+ "\r\n                                                         "
			+ "\r\n import javax.persistence.Column;                        "
			+ "\r\n import javax.persistence.Entity;                        "
			+ "\r\n import javax.persistence.GeneratedValue;                "
			+ "\r\n import javax.persistence.GenerationType;                "
			+ "\r\n import javax.persistence.Id;                            "
			+ "\r\n import javax.persistence.Table;                         "
			+ "\r\n import javax.persistence.TableGenerator;                "
			+ "\r\n import javax.persistence.Transient;                "			
			+ "\r\n import com.yq.core.jpa.JpaBaseModel;              "			
			+ "\r\n                                                         "
			+ "\r\n import com.yq.core.data.redis.ProtostuffSerializable;   "
			+ "\r\n import java.util.Date;                    "
			+ "\r\n import java.math.BigDecimal;                    "
			+ "\r\n import com.dossen.franchisee.model.system.YqJpaBaseModel;                    "
			+ "\r\n import com.yunqi.autogen.web.ui.annotation.ColumnLable; "
			+ "\r\n import com.yunqi.autogen.web.ui.annotation.CriteriaCol; "
			+ "\r\n import com.yunqi.autogen.web.ui.annotation.EditCol;     "
			+ "\r\n import com.yunqi.autogen.web.ui.annotation.EntityDesc;  "
			+ "\r\n import com.yunqi.autogen.web.ui.annotation.GridCol;     "
			+ "\r\n                                                         "
			+ "\r\n @SuppressWarnings(\"serial\")                           "
			+ "\r\n @Entity                                                 "
			+ "\r\n @Table(name=\"{tableName}\")                            "
			+ "\r\n @ProtostuffSerializable                                 "
			+ "\r\n @EntityDesc(name = \"\", autoGenPage = true, editCols = 3, genService = true, genAction = true, genCriteria = true) "
			+ "\r\n public class {modelName} extends YqJpaBaseModel{                 "
			+ "\r\n 	                                                    "
			+ "\r\n 	@TableGenerator(name = \"idGen\", table = \"ID_GENERATOR\", pkColumnName = \"ID_KEY\", valueColumnName = \"ID_VALUE\", pkColumnValue = \"{tableName}_ID\", allocationSize = 1) "
			+ "\r\n 	@Id "
			+ "\r\n 	@GeneratedValue(strategy = GenerationType.TABLE, generator = \"idGen\") "
			+ "\r\n 	@Column(name=\"ID\") " + "\r\n 	private Long id; ";



	private static List<TableDllView> genTableDllViewList(String fileName,
			String sheetName, int startRow) {
		/*
		 * FileInputStream fis; try { fis = new FileInputStream(fileName);
		 * Workbook workbook = new HSSFWorkbook(fis); } catch (Exception e) {
		 * e.printStackTrace(); }
		 */
 		
		CellProperty[] pros = new CellProperty[] {
				new CellProperty( "A", "colName", "java.lang.String" ),
				new CellProperty(  "B","colType", "java.lang.String" ),
				new CellProperty( "B","colLable", "java.lang.String"  ),
				new CellProperty( "B","description", "java.lang.String" ), 
				new CellProperty( "B","lookupCode", "java.lang.String"  ),
				new CellProperty( "B","gridCol", "java.lang.String"  ),
				new CellProperty( "B","editCol", "java.lang.String"  ),
				new CellProperty( "B","eleCategory", "java.lang.String" ),
				new CellProperty( "B","criteriaCol", "java.lang.String"  ),
				new CellProperty( "B","attachName", "java.lang.String" ),
				new CellProperty( "B","attachMsg", "java.lang.String" ),
				new CellProperty( "B","hyplink", "java.lang.String" )
				
 		};  
 
		List<TableDllView> list = null;
		try {
			list = ExcelUtil.loadExcelDataForJ(new File(fileName), TableDllView.class, pros, sheetName, startRow);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); 
		} 
		for (TableDllView tv : list) {
			System.out.println(tv.getAttachName());
		}
		return list; 
	}

	private static final String BR = "\r\n";

	public static String genTableDll(String fileName, String sheetName,
			int startRow) {
		// String fileName = "d:\\temp\\create.xls";
		// String sheetName = "SO_INVENTORY_QUANTITY"; //sheetName 也是table名
		String createTableDll = " create table " + sheetName + "( ";
		List<TableDllView> dvList = genTableDllViewList(fileName, sheetName,
				startRow);
		String split = "";
		for (TableDllView dv : dvList) {
			if (!CommonUtil.isNull(dv.toCreateTableCol())) {
				createTableDll = createTableDll + split + BR
				+ dv.toCreateTableCol();
				split = ",";
			}			
		}
		return createTableDll + BR + ")";
	}
                                        
	private static String modelPath = "D:/dossen/dossen-franchisee-service/src/main/java/com/dossen/franchisee/model/common/";
	//private static String modelPath = "D:/testmodel/";

	private static String sheetNameToModelName(String sheetName) {
		int startInd = sheetName.indexOf("_");
		String modelName = sheetName.substring(startInd, startInd+1).toUpperCase()
				+ sheetName.substring(startInd+1, sheetName.length()).toLowerCase();
		while (modelName.contains("_")) {
			int ind = modelName.indexOf("_");
			modelName = modelName.substring(0, ind)
					+ modelName.substring(ind + 1, ind + 2).toUpperCase()
					+ modelName.substring(ind + 2, modelName.length());
		}
		return modelName;
	}

	private static String readStr = "";

	/**
	 * 返回java文件的类容
	 * 
	 * @param fileName
	 * @param sheetName
	 * @param startRow
	 * @return
	 */
	public static String genModelFile(String fileName, String sheetName,
			int startRow) {
		String modelName = sheetNameToModelName(sheetName);
		File filename = new File(modelPath + modelName + ".java");
		String filein = defaultModel.replace("{modelName}", modelName).replace("{tableName}", sheetName);
		List<TableDllView> dvList = genTableDllViewList(fileName, sheetName,
				startRow);
		for (TableDllView dv : dvList) {
			filein = filein + dv.toCreateModelProperty(modelName);
		}
		filein = filein + BR + "}";
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename),"UTF-8"));
			writer.write(filein);
			writer.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}
		}
		return filein;
	}

	public static void main(String[] arg) {
		String sheetName = "SO_INVENTORY_QUANTITY";
		System.out.println("modelName -->" + sheetNameToModelName(sheetName));

		/*
		 * System.out.println("------------start-----------"); try {
		 * writeTxtFile("test java \r\n test java22"); } catch (IOException e) { //
		 * TODO Auto-generated catch block e.printStackTrace(); }
		 * //System.out.println(genTableDll());
		 * System.out.println("------------end-----------");
		 * 
		 */
	}

}
