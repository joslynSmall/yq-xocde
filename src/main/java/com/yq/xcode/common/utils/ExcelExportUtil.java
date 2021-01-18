package com.yq.xcode.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.yq.xcode.common.bean.AggregateCol;
import com.yq.xcode.common.bean.CellProperty;
import com.yq.xcode.common.bean.Result;
import com.yq.xcode.common.exception.ValidateException;
import com.yq.xcode.common.model.ReportColumnDefine;
import com.yq.xcode.common.springdata.HExportColumn;
import com.yq.xcode.common.springdata.HExportRequest;
import com.yq.xcode.common.springdata.HPageCriteria;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.DateUtil;
import com.yq.xcode.common.utils.ExcelUtil;
import com.yq.xcode.common.utils.ReportUtil;
import com.yq.xcode.common.utils.ResultUtil;
import com.yq.xcode.common.utils.YqBeanUtil;
import com.yq.xcode.constants.YqSelectHardcodeConstants;

public class ExcelExportUtil {  
	
	/**
	 * 
	 * @param er
	 * @param data
	 * @param renders
	 * @param sumColArr 可以指定合计列
	 * @return
	 */ 
	public  static <T>  Result<String> exportExcel(String fileName,
			String sheetName, HPageCriteria criteria , List<T> data ) {
		
		List<HExportColumn> cols = null; 
		if (CommonUtil.isNotBlank(criteria.getColumnsJsonStr())) {
			cols = YqJsonUtil.parseArray(criteria.getColumnsJsonStr(), HExportColumn.class);
		} 
		List<AggregateCol> sumColArr = ReportUtil.mapToAggregates(criteria.getAggregateJsonStr());
		return  exportExcel(fileName, sheetName, data, cols, sumColArr);
	}
 
	
 
	/**
	 * 临时用， 后改此方法
	 * @param seq
	 * @return
	 */
	private static String getChBySeq(int seq) {
 		String[] charArr = new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        StringBuffer col = new StringBuffer(charArr[(seq%26==0?26:seq%26) - 1].toString());
        int groupnum = seq;
        for(;groupnum>26;){
        	groupnum = groupnum/26-1;
        	col.append(charArr[groupnum%26].toString());
        }
        return col.reverse().toString();
	}
	
	private  void setCellValueByCol(HSSFCell cell, Object bean, ReportColumnDefine colDefine,HSSFCellStyle styleRight,
			StandardEvaluationContext simpleContext ,ExpressionParser parser) {
			simpleContext.setRootObject(bean);
			Expression exp=parser.parseExpression(colDefine.getPropertyName());
			Object value = exp.getValue(simpleContext);
			//Object value = BeanUtils.getProperty(bean, colDefine.getPropertyName());
			if (value != null) {
				if (YqSelectHardcodeConstants.PropertyType.NUMBER.equals(colDefine.getCompareType())  ) {
					cell.setCellValue(new Double(value.toString()));
					cell.setCellStyle(styleRight);
				} else {
					cell.setCellValue(value.toString());
				}
			}
	
	}
	
	public static  <T> Result<String> exportExcel(String fileName,
			String sheetName,  List<T> data ,List<HExportColumn> cols, List<AggregateCol> sumColArr) {
		int maxPageRow = 60000; //最大记录数 
 		HSSFWorkbook workbook=new HSSFWorkbook();
		if (CommonUtil.isNull(fileName)) {
			fileName =  CommonUtil.buildRandomKey();
		}		
 

		if (cols.isEmpty()) {
			throw new ValidateException("导出列异常!");
		}
 

		if (cols.isEmpty()) {
			throw new ValidateException("导出列异常!");
		}
		
		HSSFSheet sheet = null;
		if (CommonUtil.isNull(data) || data.size() <= maxPageRow) {
			if (CommonUtil.isNull(sheetName)) {
				sheet = workbook.createSheet("sheet1");
			}  else {
				sheet = workbook.createSheet(sheetName);
			}
			 writeSheet(cols, sheet, data,  sumColArr,true);
		} else {
			String newSheetName = sheetName;
			if (CommonUtil.isNull(sheetName)) {
				newSheetName = "Sheet";
			} 
			int lostSize = data.size();
			int seq = 1;
			while (lostSize >0) {
				sheet = workbook.createSheet(newSheetName+seq);
				int toSize = (seq)*maxPageRow;
				if (toSize > data.size()) {
					toSize = data.size();
				}
				writeSheet(cols, sheet, data.subList((seq-1)*maxPageRow, toSize),  sumColArr,true);
				lostSize = lostSize - maxPageRow;
				seq++;
			}
		}

		
		File file=new File(fileName);
		try {
 			FileOutputStream fos = new FileOutputStream(file);
			workbook.write(fos);
			 fos.flush();
			 fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new ValidateException("无法找到服务器上的文件!");
		} catch (IOException e) {
			e.printStackTrace();
			throw new ValidateException("导出文件错误!");
		}
		//result.setResult(ActionResult.RESULT_SUCCESS);
		//result.setMessage("");
		//ResultUtil.ok("/987temp/"+fileName+".xls");
	    //System.out.println("End ... ");
	    return ResultUtil.ok(fileName);
	}
	
//	public <T> Result<String> exportExcelForOtherFee(String fileName
//			) {
//		System.out.println("Start ... ");
// 		Result<String> result=new Result<String>();
//		//result.setResult(ActionResult.RESULT_SUCCESS);
//		//result.setMessage("");
//		ResultUtil.ok("/987temp/"+fileName+"template.xls");
//	    System.out.println("End ... ");
//	    return result;
//	}
	/**
	 * 
	 * @param cols
	 * @param sheet
	 * @param data
	 * @param er
	 * @param renders
	 * @param sumColArr
	 * @param incEnd  包括结尾， 模板就可以包含数据
	 */
	private static  <T> void writeSheet(List<HExportColumn> cols,HSSFSheet sheet,List<T> data ,  List<AggregateCol> sumColArr,boolean incEnd) {
		for (int i=0; i<cols.size(); i++) {
			//ExportCol col = cols[i];
			HExportColumn col = cols.get(i);
			HSSFCell cell =getCell(sheet,0,i);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(col.getTitle());
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			CellStyle style = cell.getCellStyle();
			style.setVerticalAlignment(VerticalAlignment.CENTER);
		
			cell.setCellStyle(style);
			
		}
		if (!CommonUtil.isNull(data)) {
			/// 加合计列
			Set<String> cSet = new HashSet<String>();
			if (CommonUtil.isNotNull(sumColArr)) {
				
				for (AggregateCol s : sumColArr) {
					cSet.add(s.getProperty());
				}
				HSSFCell cell = getCell(sheet,data.size()+2,0);
				cell.setCellValue("合计:");
				for (int i=0; i<cols.size(); i++) {
					String pName = cols.get(i).getField();
					if (cSet.contains(pName)) {
						cell = getCell(sheet,data.size()+2,i);
						String ch = getChBySeq(i);
						int to = data.size()+2;
						cell.setCellFormula("sum("+ch+"2:"+ch+to+")");
					}
					 
				}

			}
			
			
			// 合计结束
			for (int i=0; i<data.size(); i++) {
				T bean = data.get(i);
				for (int j=0; j<cols.size(); j++) {
					//ExportCol col = cols[j];
					HSSFCell cell = getCell(sheet,i+1,j);
					//cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					if (cSet.contains(cols.get(j).getField())) {
						String s = valueToString(bean,cols.get(j).getField() );
						if (CommonUtil.isNotNull(s)) {
							cell.setCellValue(new Double(s));
						}
						
					} else {
						cell.setCellValue(valueToString(bean,cols.get(j).getField() ));
					}
					
				}
			}
	        if (incEnd) {
				HSSFCell cell = getCell(sheet,data.size()+4,0);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("生成报表时间:" + DateUtil.convertDate2String(DateUtil.getCurrentDate(),DateUtil.DEFAULT_DATE_TIME_FORMAT));
 	        }

			
		} else {
			if (incEnd) {
				HSSFCell cell = getCell(sheet,2,0);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("报表没有数据！");
				cell = getCell(sheet,3,0);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("生成报表时间:" + DateUtil.convertDate2String(DateUtil.getCurrentDate(),DateUtil.DEFAULT_DATE_TIME_FORMAT));
			}
		}
		
	}
	
	protected static HSSFCell getCell(HSSFSheet sheet, int row, int col) {
		HSSFRow sheetRow = sheet.getRow(row);
		if (sheetRow == null) {
			sheetRow = sheet.createRow(row);
		}
		HSSFCell cell = sheetRow.getCell(col);
		if (cell == null) {
			cell = sheetRow.createCell(col);
		}
		return cell;
	}
	
 
	
	public Result<String> exportTemplate(CellProperty[] cpArr,String fName,String sheetName) {
		return this.exportTemplate(this.addNullCol(cpArr), fName, sheetName, null,null);
	}
	private CellProperty[] addNullCol(CellProperty[] cpArr) {
		Integer maxCol = 0;
		Map<Integer,CellProperty> colMap = new HashMap<Integer,CellProperty>();
		for (CellProperty cell : cpArr) {
			int col = ExcelUtil.getColByCh(cell.getCol());
			if (col > maxCol ) {
				maxCol = col;
			}
			colMap.put(col, cell);
		}
		
		CellProperty[] retCell = new CellProperty[maxCol+1];
		for (int i=0; i<=maxCol; i++ ) {
			CellProperty cell = colMap.get(i);
			if (cell == null ) {
				CellProperty nullCell = new CellProperty();
				nullCell.setCol(ExcelUtil.getCharByCol(i));
				retCell[i] = nullCell;
			} else {
				retCell[i] = cell;
			}
		}
		
		return retCell;
	}
	public Result<String> exportTemplateWithValidations(CellProperty[] cpArr,String fName,String sheetName,Map<String,Object> extraData) {
		return this.exportTemplate(this.addNullCol(cpArr), fName, sheetName, null,extraData);
	}

	public <T> Result<String> exportTemplate(CellProperty[] cpArr,String fName,String sheetName,List<T> data ,Map<String,Object> extraData) {
		HSSFWorkbook workbook=new HSSFWorkbook();
		if (CommonUtil.isNull(sheetName)) {
			sheetName = "sheet1";
		}
		HSSFSheet sheet = workbook.createSheet(sheetName);
		HSSFCellStyle lableStyle  = sheet.getWorkbook().createCellStyle();//创建样式1 
		lableStyle .setAlignment(HorizontalAlignment.CENTER ); 
		HSSFFont lableFont = sheet.getWorkbook().createFont();
 		lableFont.setBold(true);
		lableStyle.setFont(lableFont);

		List<HExportColumn> cols = new ArrayList<HExportColumn>();
		Set<String> colSet = new HashSet<String>();
		for (CellProperty cp : cpArr) {
			if (!colSet.contains(cp.getCol())) {
				HExportColumn col = new HExportColumn();
				col.setField(cp.getProperty());
				col.setTitle(cp.getLable());
				cols.add(col);
			}			
			colSet.add(cp.getCol());
		}
		this.writeSheet(cols, sheet, data, null,  false);
		
		if(CommonUtil.isNotNull(extraData)){
			Object hiddenSheents = extraData.get("hiddenSheets");
			if(CommonUtil.isNotNull(hiddenSheents) && (hiddenSheents instanceof List)){
				for(Object  hiddenSheent:(List)hiddenSheents){
					if(hiddenSheent instanceof Object[]){
						Object[] obArr= (Object[])hiddenSheent;
						String groupName = obArr[0].toString();
						String hiddenSheetName = obArr[1].toString();
						String formula = obArr[2].toString();
						String[] dropDownListData = obArr[3].toString().substring(1, obArr[3].toString().length()-1).split(",");
						initDropDownList(workbook,groupName ,hiddenSheetName ,formula ,dropDownListData );
					}
				}
			}
			Object dataValidationList = extraData.get("dataValidations");
			if(CommonUtil.isNotNull(dataValidationList) && (dataValidationList instanceof List)){
				for(Object  dv:(List)dataValidationList){
					if(dv instanceof DataValidation){
						sheet.addValidationData((DataValidation)dv);
					}
				}
			}
		}
		
		
		String fileName = fName;
        if (CommonUtil.isNull(fileName)) {
        	fileName =  CommonUtil.buildRandomKey();
        }		
		File file=new File( fileName );
		try {
 			FileOutputStream fos = new FileOutputStream(file);
			workbook.write(fos);
			 fos.flush();
			 fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new ValidateException("无法找到服务器上的文件!");
		} catch (IOException e) {
			e.printStackTrace();
			throw new ValidateException("导出文件错误!");
		}
		//Result<String> result=new Result<String>();
 
		//ResultUtil.ok("/987temp/"+fileName+".xls");
	    return ResultUtil.ok(fileName);
	}
	private HSSFComment genComment(HSSFPatriarch patr,String msg) {
		 HSSFComment comment =patr.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short)4, 2, (short) 6, 5));
		 comment.setString(new HSSFRichTextString(msg));
		 return comment;
	}
	public static DataValidation getDataValidationByFormula(int startRow ,int endRow, String formulaString, int columnStartIndex,int columnEndIndex) {
        // 加载下拉列表内容
        DVConstraint constraint = DVConstraint.createFormulaListConstraint(formulaString);
        // 设置数据有效性加载在哪个单元格上。
        // 四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(startRow, endRow, columnStartIndex, columnEndIndex);
        // 数据有效性对象
        DataValidation dataValidationList = new HSSFDataValidation(regions, constraint);
        dataValidationList.createErrorBox("Error", "请选择或输入有效的选项，或下载最新模版重试！");
        return dataValidationList;
    }
    private  void initDropDownList(HSSFWorkbook workbook, String dataName,String hiddenSheetName,String formula,String[] dataList ) {
    		
    	HSSFSheet hiddenSheet = workbook.getSheet(hiddenSheetName);
    	if(CommonUtil.isNull(hiddenSheet)){
    		hiddenSheet = workbook.createSheet(hiddenSheetName);
    		int index = workbook.getSheetIndex(hiddenSheet);
    		workbook.setSheetHidden(index,true);
    	}
    	for(int i = 0;i <dataList.length ;i++){
    		HSSFRow row = hiddenSheet.createRow(i);
    		HSSFCell cell1 = row.createCell(0);
    		cell1.setCellValue(dataList[i].trim());
    	}
//    	HSSFSheet mainSheet = workbook.getSheet("importSetupDtl");
//    	HSSFRow row = mainSheet.createRow(1);
//    	for(int i = 0;i <dataList.length ;i++){
//    		HSSFCell cell1 = row.createCell(i);
//    		cell1.setCellValue(dataList[i].trim());
//    	}
    	 Name name = workbook.createName();
         name.setNameName(dataName);
         name.setRefersToFormula(formula);
    	
    }
    
    
 
	private List<String> genAmountCols(Class clazz, List<String> amountStrings ) {
		if (amountStrings == null) {
			amountStrings = new ArrayList<String>();
		}
		Class tmpClass = clazz;
    	List<Field> fieldList = new ArrayList<Field>();
    	while (tmpClass != null) {
			Field[] fs = tmpClass.getDeclaredFields(); // 得到所有的fields  
			for (Field f : fs)   {   
	    	    Class fieldClazz = f.getType();     	    
	    	    if ("java.math.BigDecimal".equals(fieldClazz.getName())) {
	    	    	amountStrings.add(f.getName());
	    	    }
	    	}  
			tmpClass = tmpClass.getSuperclass();
		}  
    	return amountStrings;
	}
	
	private List<String> genDateCols(Class clazz  ) {
		List<String> dateStrings = new ArrayList<String>();
		Class tmpClass = clazz;
    	List<Field> fieldList = new ArrayList<Field>();
    	while (tmpClass != null) {
			Field[] fs = tmpClass.getDeclaredFields(); // 得到所有的fields  
			for (Field f : fs)   {   
	    	    Class fieldClazz = f.getType();     	    
	    	    if ("java.util.Date".equals(fieldClazz.getName())) {
	    	    	dateStrings.add(f.getName());
	    	    }
	    	}  
			tmpClass = tmpClass.getSuperclass();
		}  
    	return dateStrings;
	}
	
 
	public <T> Result<String> exportExcel(String fileName,
			String sheetName, HExportRequest er, List<T> data,  List<String> amountStrings, List<String> priceStrings,List<String> numStrings,boolean needCreateDate) {
		
		List<String> dateStrs = null;
		if (CommonUtil.isNotNull(data)) {
			dateStrs = this.genDateCols(data.get(0).getClass());
		}
		
		int maxPageRow = 60000; //最大记录数 
		HSSFWorkbook workbook=new HSSFWorkbook();
		if (CommonUtil.isNull(fileName)) {
			fileName =  CommonUtil.buildRandomKey();
		}		
		List<HExportColumn> cols= er.getColumns();
		if (cols.isEmpty()) {
			throw new ValidateException("导出列异常!");
		}
		HSSFSheet sheet = null;
		HSSFFont font = workbook.createFont();
		font.setFontName("宋体");
		
		HSSFCellStyle cellStyleAmount = workbook.createCellStyle();
		cellStyleAmount.setAlignment(HorizontalAlignment.RIGHT);
		HSSFDataFormat df = workbook.createDataFormat();
		cellStyleAmount.setDataFormat(df.getFormat("#,##0.00"));
		
		HSSFCellStyle cellStyleDate = workbook.createCellStyle();
		HSSFDataFormat dateFort = workbook.createDataFormat();
		cellStyleDate.setDataFormat(dateFort.getFormat("yyyy-MM-dd"));
		
		HSSFCellStyle cellStylePrice = workbook.createCellStyle();
		cellStylePrice.setAlignment(HorizontalAlignment.RIGHT);
//		HSSFDataFormat df = workbook.createDataFormat();
		cellStylePrice.setDataFormat(df.getFormat("#,##0.0000"));
		
		if (CommonUtil.isNull(data) || data.size() <= maxPageRow) {
			if (CommonUtil.isNull(sheetName)) {
				sheet = workbook.createSheet("sheet1");
			}  else {
				sheet = workbook.createSheet(sheetName);
			}
			this.writeSheet(cols, sheet, data, er,  font, amountStrings,numStrings,  priceStrings,dateStrs, cellStyleAmount, cellStylePrice,cellStyleDate,needCreateDate);
		} else {
			String newSheetName = sheetName;
			if (CommonUtil.isNull(sheetName)) {
				newSheetName = "Sheet";
			} 
			int lostSize = data.size();
			int seq = 1;
			while (lostSize >0) {
				sheet = workbook.createSheet(newSheetName+seq);
				int toSize = (seq)*maxPageRow;
				if (toSize > data.size()) {
					toSize = data.size();
				}
				this.writeSheet(cols, sheet, data.subList((seq-1)*maxPageRow, toSize), er,  font, amountStrings,numStrings, priceStrings, dateStrs,  cellStyleAmount, cellStylePrice,cellStyleDate,needCreateDate);
				lostSize = lostSize - maxPageRow;
				seq++;
			}
		}

		
		File file=new File( fileName );
		try { 
			FileOutputStream fos = new FileOutputStream(file);
			workbook.write(fos);
			 fos.flush();
			 fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new ValidateException("无法找到服务器上的文件!");
		} catch (IOException e) {
			e.printStackTrace();
			throw new ValidateException("导出文件错误!");
		}
 
		//ResultUtil.ok("/987temp/"+fileName+".xls");
	    System.out.println("End ... ");
	    return ResultUtil.ok(fileName);
	}
	
	private <T> void writeSheet(List<HExportColumn> cols,HSSFSheet sheet,List<T> data,HExportRequest er,  HSSFFont font,
			List<String> amountStrings,List<String> numStrings, List<String> priceStrings, List<String>  dateStrs , HSSFCellStyle cellStyleAmount, HSSFCellStyle cellStylePrice,HSSFCellStyle cellStyleDate,
			boolean needCreateDate) {
		
		for (int i=0; i<cols.size(); i++) {
			//ExportCol col = cols[i];
			HExportColumn col = cols.get(i);
			HSSFCell cell =getCell(sheet,0,i);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(col.getTitle());
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			CellStyle style = cell.getCellStyle();
			style.setVerticalAlignment(VerticalAlignment.CENTER);
			style.setFont(font);
			cell.setCellStyle(style);
			int width = 0;
			if(col.getWidth() != null){
				width = Integer.parseInt(col.getWidth()) * 35;
				sheet.setColumnWidth(i, width);
			}
			
		}
		if (!CommonUtil.isNull(data)) {
			if (CommonUtil.isNull(amountStrings)) {
				amountStrings = this.genAmountCols(data.get(0).getClass(), amountStrings);
			}
			
			List<AggregateCol> sumColArr = ReportUtil.mapToAggregates(er.getAggregateJsonStr());
			Set<String> cSet = new HashSet<String>();
			if (CommonUtil.isNotNull(sumColArr)) {
				
				for (AggregateCol s : sumColArr) {
					cSet.add(s.getProperty());
				}
				HSSFCell cell = getCell(sheet,data.size()+2,0);
				cell.setCellValue("合计:");
				for (int i=0; i<cols.size(); i++) {
					String pName = cols.get(i).getField();
					if (cSet.contains(pName)) {
						cell = getCell(sheet,data.size()+2,i);
						String ch = this.getChBySeq(i+1);
						int to = data.size()+2;
						cell.setCellFormula("sum("+ch+"2:"+ch+to+")");
					}
					 
				}

			}
			
			for (int i=0; i<data.size(); i++) {
				T bean = data.get(i);
				for (int j=0; j<cols.size(); j++) {
					//ExportCol col = cols[j];
					HSSFCell cell = getCell(sheet,i+1,j);
					if (CommonUtil.isNotNull(numStrings)) {
						if (numStrings.contains(cols.get(j).getField())) {
							Double value = null;
							Object tmp = null;
							try {
								tmp = BeanUtils.getProperty(bean, cols.get(j).getField());
							} catch (IllegalAccessException e1) {
								e1.printStackTrace();
							} catch (InvocationTargetException e1) {
								e1.printStackTrace();
							} catch (NoSuchMethodException e1) {
								e1.printStackTrace();
							}
							if (tmp != null) {
								cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
								value = new Double(tmp.toString());	
								cell.setCellValue(value);
							}
							continue;
						}
					}
					if (CommonUtil.isNotNull(priceStrings)) {
						if (priceStrings.contains(cols.get(j).getField())) {
							Double value = null;
							Object tmp = null;
							try {
								tmp = BeanUtils.getProperty(bean, cols.get(j).getField());
							} catch (IllegalAccessException e1) {
								e1.printStackTrace();
							} catch (InvocationTargetException e1) {
								e1.printStackTrace();
							} catch (NoSuchMethodException e1) {
								e1.printStackTrace();
							}
							if (tmp != null) {
								cell.setCellStyle(cellStylePrice);
								cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
								value = new Double(tmp.toString());	
								cell.setCellValue(value);
							}
							continue;
						}
					}
					if (CommonUtil.isNotNull(amountStrings)) {
						if (amountStrings.contains(cols.get(j).getField())) {
							Double value = null;
							Object tmp = null;
							try {
								tmp = BeanUtils.getProperty(bean, cols.get(j).getField());
							} catch (IllegalAccessException e1) {
								e1.printStackTrace();
							} catch (InvocationTargetException e1) {
								e1.printStackTrace();
							} catch (NoSuchMethodException e1) {
								e1.printStackTrace();
							}
							if (tmp != null) {
								cell.setCellStyle(cellStyleAmount);
								cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
								value = new Double(tmp.toString());	
								cell.setCellValue(value);
							}
							continue;
						}
					}
					
					if (CommonUtil.isNotNull(dateStrs)) {
						if (dateStrs.contains(cols.get(j).getField())) {
							Date value = null;
							Object tmp =   YqBeanUtil.getPropertyValue(bean, cols.get(j).getField());
							if (tmp != null) {
								cell.setCellStyle(cellStyleDate);
								cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
								value =(Date) tmp;	
								cell.setCellValue(value);
							}
							continue;
						}
					}
					
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);						
					cell.setCellValue(valueToString(bean,cols.get(j).getField() ));
					
				}
			}
           if(needCreateDate)
           {
			HSSFCell cell = getCell(sheet,data.size()+3,0);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue("生成报表时间:" + DateUtil.convertDate2String(DateUtil.getCurrentDate(),DateUtil.DEFAULT_DATE_TIME_FORMAT));
           }
			
		}else {
			if(needCreateDate)
	        {
			HSSFCell cell = getCell(sheet,2,0);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue("报表没有数据！");
			cell = getCell(sheet,3,0);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue("生成报表时间:" + DateUtil.convertDate2String(DateUtil.getCurrentDate(),DateUtil.DEFAULT_DATE_TIME_FORMAT));
	        }
		}
		
	}
	
	
	public static <T> String valueToString(T bean,String field ) {
		try{
	 
			Object value = BeanUtils.getProperty(bean, field);
			if (CommonUtil.isNull(value)) {
				return "";
			} 
			
			return value.toString();
		}catch(Exception ex){
			throw new ValidateException("Invalid property '" + field + "' of bean class [" + bean.getClass().getName() + "]: " + ex.getMessage() );
		}
	}
	 
    
    
}
