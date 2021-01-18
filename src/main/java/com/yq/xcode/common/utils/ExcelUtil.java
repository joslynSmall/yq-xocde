package com.yq.xcode.common.utils;

import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.yq.xcode.common.bean.CellPosition;
import com.yq.xcode.common.bean.CellProperty;
import com.yq.xcode.common.bean.CsvRow;
import com.yq.xcode.common.bean.CsvSheet;
import com.yq.xcode.common.bean.InsertSql;
import com.yq.xcode.common.bean.SelectItem;
import com.yq.xcode.common.exception.ValidateException;

public class ExcelUtil {
	private static Log Log = LogFactory.getLog(ExcelUtil.class);
	public final static String XLS = "xls";

	public final static String XLSX = "xlsx";
	
	public final static String CSV = "csv";
	
 	
	public static String getCharByCol(int i) {
		if (i > colMap.length -1) {
			throw new ValidateException("Excel 超最大列数！");	
		}
		return colMap[i];
		
	}
 
	public static final String[] colMap = new String[]{  "A", "B", "C", "D", "E","F", "G", "H", "I", "J", "K", "L","M", "N", "O","P",  "Q","R",  "S","T",  "U","V", "W","X","Y","Z",
		"AA", "AB", "AC", "AD", "AE","AF", "AG", "AH", "AI", "AJ", "AK", "AL","AM", "AN", "AO","AP",  "AQ","AR",  "AS","AT",  "AU","AV", "AW","AX","AY","AZ",
		"BA", "BB", "BC", "BD", "BE","BF", "BG", "BH", "BI", "BJ", "BK", "BL","BM", "BN", "BO","BP",  "BQ","BR",  "BS","BT",  "BU","BV", "BW","BX","BY","BZ",
		"CA", "CB", "CC", "CD", "CE","CF", "CG", "CH", "CI", "CJ", "CK", "CL","CM", "CN", "CO","CP",  "CQ","CR",  "CS","CT",  "CU","CV", "CW","CX","CY","CZ"};

	public static int getColByCh(String ch) {
		int i=0;
		for (String sAr : colMap) {
			if (ch.equals(sAr)) {
				return i;
			}
			i++;
			
		}
		throw new ValidateException("Excel 超最大列数！");
	}
	public static <E> List<E> loadExcelDataForJ(File excelFile, Class<E>  clazz,
			CellProperty[] cellPropertis,  int startRow) throws Exception {
		HSSFWorkbook workbook;
		FileInputStream fis = null;
		String fileType = getFileType(excelFile);
		try {
			fis = new FileInputStream(excelFile);
			workbook = new HSSFWorkbook(fis);
			HSSFSheet sheet = workbook.getSheetAt(0);
			return loadExcelDataForJ( sheet,  clazz,
					  cellPropertis, startRow);
			/*
			if (fileType == XLS) {
				workbook = new HSSFWorkbook(fis);
			} else if ( fileType == XLSX) {
				workbook = new XSSFWorkbook(fis);
			}
			*/
			
		} catch (IOException e) {
			e.printStackTrace();
		 	throw new ValidateException("excel文件格式错误，建议用excel转存为另一文件再做导入");
		} finally {
			if( fis != null ) {
				try {
				 fis.close();
				} catch(IOException ioe){
	    			 throw new ValidateException("excel文件格式错误，建议用excel转存为另一文件再做导入");
				}
			}
		}
	}
	
	
	//导入类型为： excel中的每一行是一个数据对象
	public static <E> List<E> loadExcelDataForJ(File excelFile, Class<E>  clazz,
			CellProperty[] cellPropertis, String sheetName, int startRow) throws Exception {
//		HSSFWorkbook workbook;
		FileInputStream fis = null;
		String fileType = getFileType(excelFile);
		try {
			fis = new FileInputStream(excelFile);
//			workbook = new HSSFWorkbook(fis);
			
		    Workbook workbook = null;
	        try {
	        	workbook = new XSSFWorkbook(fis);
	        } catch (Exception ex) {
	        	workbook = new HSSFWorkbook(new FileInputStream(excelFile));
	        }
//	        XSSFSheet 
			Sheet sheet = workbook.getSheetAt(0);
			if (CommonUtil.isNotNull(sheetName)) {
				sheet = workbook.getSheet(sheetName);
			}
			return loadExcelDataForJ( sheet,  clazz, cellPropertis, startRow);
 
			
		} catch (IOException e) {
		 	throw new ValidateException("excel文件格式错误，建议用excel转存为另一文件再做导入");
		} finally {
			if( fis != null ) {
				try {
				 fis.close();
				} catch(IOException ioe){
	    			 throw new ValidateException("excel文件格式错误，建议用excel转存为另一文件再做导入");
				}
			}
		}
	}
	
	//导入类型为： excel中的每一个cell都是一个数据对象
	public static <E> List<E> loadExcelDataFromCell(File excelFile, Class<E>  clazz, CellPosition cellPosition) throws Exception {
		HSSFWorkbook workbook;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(excelFile);
			workbook = new HSSFWorkbook(fis);
			return loadExcelDataFromCell(workbook,  clazz, cellPosition);
			
		} catch (IOException e) {
			throw new ValidateException("excel文件格式错误，建议用excel转存为另一文件再做导入");
		} finally {
			if( fis != null ) {
				try {
					fis.close();
				} catch(IOException ioe){
					throw new ValidateException("excel文件格式错误，建议用excel转存为另一文件再做导入");
				}
			}
		}
	}
	
	//导入类型为： excel中的每一行是一个数据对象, 所有sheet 
	public static <E> List<E> loadExcelDataForJAnySheet(File excelFile, Class<E>  clazz,
			CellProperty[] cellPropertis,  int startRow) throws Exception {
//		HSSFWorkbook workbook;
		FileInputStream fis = null;
		String fileType = getFileType(excelFile);
		try {
			fis = new FileInputStream(excelFile);
//			workbook = new HSSFWorkbook(fis);
			
		    Workbook workbook = null;
	        try {
	        	workbook = new XSSFWorkbook(fis);
	        } catch (Exception ex) {
	        	workbook = new HSSFWorkbook(new FileInputStream(excelFile));
	        }
//	        XSSFSheet 
	        int sheets = workbook.getNumberOfSheets();
	        List<E> list = new ArrayList<E>();
	        for (int i=0; i<sheets; i++) {
	        	Sheet sheet = workbook.getSheetAt(i);
	        	List<E> tmpList = loadExcelDataForJ( sheet,  clazz, cellPropertis, startRow);
	        	if (tmpList != null ) {
	        		list.addAll(tmpList);
	        	}
	        }
			return list;
 
			
		} catch (IOException e) {
		 	throw new ValidateException("excel文件格式错误，建议用excel转存为另一文件再做导入");
		} finally {
			if( fis != null ) {
				try {
				 fis.close();
				} catch(IOException ioe){
	    			 throw new ValidateException("excel文件格式错误，建议用excel转存为另一文件再做导入");
				}
			}
		}
	}
	
	
	public static <E> List<E> loadExcelDataFromCell(HSSFWorkbook workbook, Class<E>  clazz, CellPosition cellPosition) throws Exception {
		Map<Integer, String> colMap = new HashMap<Integer, String>();   //存储 列数 和 属性的对应关系
		List<E> loadList=new ArrayList<E>();
		HSSFSheet sheet = null;
		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {//获取每个Sheet表
			sheet = workbook.getSheetAt(i);
			colMap.clear();
			int physicalRow = sheet.getPhysicalNumberOfRows();
			String sheetNameValue = sheet.getSheetName();
			if (cellPosition.isSheetValueCheck() && CommonUtil.isNotNull(cellPosition.getSheetValueMap())) {
				if (CommonUtil.isNull(cellPosition.getSheetValueMap().get(sheetNameValue.trim()))) {
					throw new ValidateException("</br>第 "+(i+1)+" 页名"+"【"+sheetNameValue+"】: 无法在系统表中找到，请核对！");
				}
			}
			//B2-> 1
			int headrRowIdx = columnToIndex(CommonUtil.getCharFromString(cellPosition.getHeaderStartCell()))-1;
			int colRowIdx = CommonUtil.getNumberFromString(cellPosition.getHeaderStartCell())-1;
			
			HSSFRow headerRow=sheet.getRow(headrRowIdx);
			for (int k = colRowIdx; k < headerRow.getPhysicalNumberOfCells(); k++) {//获取每个单元格
				String colPropertyValue = headerRow.getCell(k).getStringCellValue().trim();
				if (CommonUtil.isNotNull(colPropertyValue)) {
					colMap.put(k, colPropertyValue);
				}
			}
			
			
			//B3-> 2
			int startRow = CommonUtil.getNumberFromString(cellPosition.getCellValueStart())-1;
			
			int propertyColIdx = columnToIndex(CommonUtil.getCharFromString(cellPosition.getRowStartCell()))-1;
			for (int j = startRow; j < physicalRow; j++) {//获取每行
				HSSFRow row=sheet.getRow(j);
//					System.out.println(">>> "+j+"    .. |"+row.getCell(propertyColIdx)+"|"); 
				if (CommonUtil.isNull(row.getCell(propertyColIdx))) {
					continue;
				}

				int startCol = colRowIdx;
				for (int k = startCol; k < row.getPhysicalNumberOfCells(); k++) {//获取每个单元格
					if (CommonUtil.isNull(row.getCell(k))) {
						continue;
					}
					if (cellPosition.isColValueCheck() && CommonUtil.isNotNull(cellPosition.getColValueMap())) {
						if (!cellPosition.getColValueMap().containsKey(colMap.get(k).trim())) {
							throw new ValidateException("</br>第 "+(i+1)+" 页 "
									+" 第 "+(headrRowIdx+1) +" 行"
									+"  "+indexToColumn(k+1)+" 列【"+colMap.get(k)+"】: 无法在系统表中找到，请核对！");
						}
					}
					
					String rowPropertyValue = row.getCell(propertyColIdx).toString();
					if (cellPosition.isRowValueCheck() && CommonUtil.isNotNull(cellPosition.getRowValueMap())) {
						if (!cellPosition.getRowValueMap().containsKey(rowPropertyValue.trim())) {
							throw new ValidateException("</br>第"+(i+1)+"页 "
						      +" 第 "+(j+1) +" 行"
							  +"  "+indexToColumn(propertyColIdx+1)+" 列【"+rowPropertyValue+"】: 无法在系统表中找到，请核对！");
						}
					}
					
					E model = clazz.newInstance();
					Cell cell = row.getCell(k);
					Object value = getObjectFromCell(cell, cellPosition.getValueType());
					if (CommonUtil.isNotNull(rowPropertyValue)) {
						rowPropertyValue = rowPropertyValue.trim();
						YqBeanUtil.setBeanProperty(model, cellPosition.getRowProperty(), rowPropertyValue);
					}
					if (CommonUtil.isNotNull(colMap.get(k))) {
						String colProperty = colMap.get(k).trim();
						YqBeanUtil.setBeanProperty(model, cellPosition.getColProperty(), colProperty);
					}
					YqBeanUtil.setBeanProperty(model, cellPosition.getSheetProperty(), sheetNameValue.trim());
					
					if (CommonUtil.isNull(value)) { 
						continue;
					} else if  ("java.lang.Integer".equals(cellPosition.getValueType()) 
							|| "integer".equals(cellPosition.getValueType()) 
							|| "int".equals(cellPosition.getValueType()) ) {
						
						double dValue = Double.valueOf((Integer)value);
						if (cellPosition.isSkipCellValueIsZero() && dValue == 0d)  {
							continue;
						} else {
							YqBeanUtil.setBeanProperty(model, cellPosition.getCellProperty(),  value);
						}
					}
					HSSFComment hComment = row.getCell(k).getCellComment();
					if (cellPosition.isSaveComment() && CommonUtil.isNotNull(hComment)) {
						YqBeanUtil.setBeanProperty(model, cellPosition.getCellCommentProperty(), hComment.getString().getString());
					}
					loadList.add(model);
				}
			}
		}
		return loadList;
	}
	
	public static String getFileType(File file) {
		String fileLower = file.getName().toLowerCase();
		if (fileLower.endsWith(XLS)) {
			return XLS;
		} else if (fileLower.endsWith(XLSX)) {
			return XLSX;
		} else if (fileLower.endsWith(CSV)) {
			return CSV;
		} else {
			throw new ValidateException("系统无法解析的文件格式:" + fileLower);
		}
	}

	
	public static int getMaxRowNumber(Sheet sheet) {
		int physicalRow = sheet.getPhysicalNumberOfRows();
		int lastNumber = sheet.getLastRowNum();
		int rowNumber = physicalRow > lastNumber ? physicalRow : lastNumber;
		return rowNumber;
	}
	
	public static <E> List<E> loadExcelDataForJ(Sheet sheet, Class<E>  clazz,
			CellProperty[] cellPropertis,  int startRow) throws Exception {
		int physicalRow = sheet.getPhysicalNumberOfRows();
		int lastNumber = sheet.getLastRowNum();
		int rowNumber = physicalRow > lastNumber ? physicalRow : lastNumber; 
        List<Row> rowList = new ArrayList<Row>();
		for (int i = startRow; i <= rowNumber; i++) {
			rowList.add(sheet.getRow(i));
        }
		if(clazz == List.class || List.class.isAssignableFrom(clazz)){
			return loadExcelDataForList(rowList,clazz,cellPropertis,startRow);
		}
 		return loadExcelDataForJ(rowList,clazz,cellPropertis,startRow);
	}
	
	
	public static Object getBeanValueByCell(Object bean, String property, Cell cell) {
		Object value = null;
		if ( cell != null ) {
			CellProperty cp = new CellProperty();
			cp.setProperty(property);
			PropertyDescriptor propertyDesc = CommonUtil.getBeanPropertyDescriptorByColumn(bean, property);
			if (propertyDesc == null) {
				throw new ValidateException(cp.getLable()+"的属性"+cp.getProperty()+"定义错误， 请仔细检查!");
			}
			String classType = propertyDesc.getPropertyType().getName() ;
			value = getObjectFromCell(cell,classType, cp);
		} 
		return value;
	}
	/**
	 * 
	 * @param rowList
	 * @param clazz
	 * @param cellPropertis
	 * @param startRow
	 * @param variables  
	 * @return
	 * @throws Exception
	 */
	
	public static <E> List<E> loadExcelDataForList(List<Row> rowList, Class<E>  clazz,
			CellProperty[] cellPropertis,int startRow ) throws Exception {
		if (CommonUtil.isNull(rowList)) {
			return null;
		}
		List<String> errMsg = new ArrayList<String>();
		List<E> loadList=new ArrayList<E>();
		for (Row row : rowList) { 
			if (row == null) {
				continue;
			}
			if( isSpaceRow(row)) {
				continue; 
			}
			List<String> rowdata = new ArrayList<String>();
			for(short i = (short)0;i<row.getLastCellNum();i++){
				Cell cell= row.getCell(i);
				String value = getCellStringValue(cell);
	 			if (CommonUtil.isNull(value)) {
	 				value = null;
				} else {
					value = value.trim();
				}
	 			rowdata.add(i, value);
			}
			loadList.add((E) rowdata);
		}
		if (CommonUtil.isNotNull(errMsg)) {
			String errMsgInfo = "";
			for (String s : errMsg) {
				errMsgInfo = errMsgInfo +s+"<br>";
			}
			throw new ValidateException(errMsgInfo);
		}
		return loadList;
	}
	/**
	 * 
	 * @param rowList
	 * @param clazz
	 * @param cellPropertis
	 * @param startRow
	 * @param variables  
	 * @return
	 * @throws Exception
	 */
	
	public static <E> List<E> loadExcelDataForJ(List<Row> rowList, Class<E>  clazz,
			CellProperty[] cellPropertis,int startRow ) throws Exception {
		if (CommonUtil.isNull(rowList)) {
			return null;
		}
		Map variables = new HashMap();
		variables.put("pt", new ExcelImportUtil());
 		List<String> errMsg = new ArrayList<String>();
		List<E> loadList=new ArrayList<E>();
		PropertyDescriptor propertyDesc = null;
		Map<String,String> pTypeMap = new HashMap<String,String>();
		for (CellProperty cp : cellPropertis) {
			propertyDesc = CommonUtil.getBeanPropertyDescriptorByColumn(clazz.newInstance(), cp.getProperty());
			if (propertyDesc == null) {
				throw new ValidateException(cp.getLable()+"的属性"+cp.getProperty()+"定义错误， 请仔细检查!");
			}
			pTypeMap.put(cp.getProperty(), propertyDesc.getPropertyType().getName());
		}
		int i = startRow;
		for (Row row : rowList) { 
			if (row == null) {
				continue;
			}
			if( isSpaceRow(row)) {
        		continue; 
        	}
			E model = clazz.newInstance();
			for (CellProperty cp : cellPropertis) {
				Cell cell = row.getCell(getColByCh(cp.getCol()));
				try {
					Object value = null;
					if (CommonUtil.isNotNull(cp.getConvertFun())) {
						value = runCellFun(cell,cp.getConvertFun(),variables);
					} else {
						value = getObjectFromCell(cell,pTypeMap.get(cp.getProperty()), cp);
					}
					if (cp.getMandatory()!= null && cp.getMandatory() && CommonUtil.isNull(value)) { 
						errMsg.add("第"+(row.getRowNum())+"行 "+cp.getCol()+"列 "+ cp.getLable()+": 不可以为空，请查阅！");
					}
					if (CommonUtil.isNotNull(cp.getDuplicatCheckList())) {
						if (cp.getDuplicatCheckList().indexOf(value.toString().trim()) > -1) { 
							errMsg.add("第"+(row.getRowNum())+"行 "+cp.getCol()+"列 "+ cp.getLable()+"已存在，不能添加");
						}
					}
					YqBeanUtil.setBeanProperty(model, cp.getProperty(), CommonUtil.trim(value));
				} catch (ValidateException e) {
					errMsg.add(e.getMessage());
				}
			}
			loadList.add(model);
        }
		if (CommonUtil.isNotNull(errMsg)) {
			String errMsgInfo = "";
			for (String s : errMsg) {
				errMsgInfo = errMsgInfo +s+"<br>";
			}
			throw new ValidateException(errMsgInfo);
		}
		return loadList;
	}
	
	public static <E> List<E> loadExcelDataForJByCsvRow(List<CsvRow> rowList, Class<E>  clazz,
			CellProperty[] cellPropertis,int startRow ) throws Exception {
		if (CommonUtil.isNull(rowList)) {
			return null;
		}
		Map variables = new HashMap();
		variables.put("pt", new ExcelImportUtil());
 		List<String> errMsg = new ArrayList<String>();
		List<E> loadList=new ArrayList<E>();
		PropertyDescriptor propertyDesc = null;
		Map<String,String> pTypeMap = new HashMap<String,String>();
		for (CellProperty cp : cellPropertis) {
			propertyDesc = CommonUtil.getBeanPropertyDescriptorByColumn(clazz.newInstance(), cp.getProperty());
			if (propertyDesc == null) {
				throw new ValidateException(cp.getLable()+"的属性"+cp.getProperty()+"定义错误， 请仔细检查!");
			}
			pTypeMap.put(cp.getProperty(), propertyDesc.getPropertyType().getName());
		}
		int y = startRow;
		for (CsvRow row : rowList) { 
			y++;
			if (row == null) {
				continue;
			}
			if( isSpaceRow(row)) {
        		continue; 
        	}
			E model = clazz.newInstance();
			for (CellProperty cp : cellPropertis) {
				Object value = row.getCellValue(getColByCh(cp.getCol()));
				try { 
					if (CommonUtil.isNotNull(cp.getConvertFun())) {
						value = runCellFun(value,cp.getConvertFun(),variables);
					}  else {
						value = getObjectFromCsvCell(value,pTypeMap.get(cp.getProperty()));
					}
					if (cp.getMandatory()!= null && cp.getMandatory() && CommonUtil.isNull(value)) { 
						errMsg.add("第"+y+"行 "+cp.getCol()+"列 "+ cp.getLable()+": 不可以为空，请查阅！");
					}
					if (CommonUtil.isNotNull(cp.getDuplicatCheckList())) {
						if (cp.getDuplicatCheckList().indexOf(value.toString().trim()) > -1) { 
							errMsg.add("第"+y+"行 "+cp.getCol()+"列 "+ cp.getLable()+"已存在，不能添加");
						}
					}
					YqBeanUtil.setBeanProperty(model, cp.getProperty(), CommonUtil.trim(value));
				} catch (ValidateException e) {
					errMsg.add(e.getMessage());
					if (errMsg.size() > 20) { // 超过直接返回, 解决错误数据量过大的问题
						throwErrMsg(errMsg);
					}
				}
			}
			loadList.add(model);
        }
		if (CommonUtil.isNotNull(errMsg)) {
			throwErrMsg(errMsg);
		}
		return loadList;
	}
	
 
	
  
 
	
	private static void throwErrMsg(List<String> errMsg) {
		if (CommonUtil.isNotNull(errMsg)) {
			String errMsgInfo = "";
			for (String s : errMsg) {
				errMsgInfo = errMsgInfo +s+"<br>";
			}
			throw new ValidateException(errMsgInfo);
		}
	}
	
	private static  Object runCellFun(Object cell, String funStr,Map variables) {
 		variables.put("cell", cell);
		return YqBeanUtil.executeExpression(variables, funStr);
	}
 
 
	
 	
	private static Object getObjectFromCell(Cell cell, String classType,CellProperty cp)   {
		String value = null;
		try {
			if (cell == null ) {
				if (CommonUtil.isNotNull(cp.getDefaultValue())) {
					return cp.getDefaultValue();
				} else {
					return null;
				}
			}
			
			 
		
			value = getCellStringValue(cell);
 			if (CommonUtil.isNull(value)) {
				if (CommonUtil.isNotNull(cp.getDefaultValue())) {
					value = cp.getDefaultValue();
				} else {
					return null;
				}
				
			} else {
				value = value.trim();
			}
			if ( "java.util.Date".equals(classType)) {
				try {
					return cell.getDateCellValue();
				} catch (Exception ex) {
					if (cp.getParseFormat() != null) {
						SimpleDateFormat simpleDateFormat = (SimpleDateFormat) cp.getParseFormat();
						return simpleDateFormat.parse(value);
					} else {
						return DateUtil.convertStringToDateForAll(value);
					}
					
				}
			}
			if (CommonUtil.isNotNull(cp.getParseFormat()) && cell.getCellType() != HSSFCell.CELL_TYPE_NUMERIC) {
				try {
					cp.getParseFormat().parseObject(value);
				} catch (Exception e ) {
					throw new ValidateException("第"+(cell.getRowIndex()+1-0)+"行"+cp.getCol()+"列 "+ cp.getLable()+": [" +value+"] ,格式有误，请查阅！");
				}					
			}
			
			if (cp.getNameKeys() != null) {
				String key = cp.getNameKeys().get(value);
				if (key == null) {
					throw new ValidateException("第"+(cell.getRowIndex()+1-0)+"行"+cp.getCol()+"列 "+ cp.getLable()+": [" +value+"] ,不存在，请查阅！");
				} else {
					value = key;
				}
			}
			if ("java.lang.String".equals(classType)) {
				if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) { 
					if (HSSFDateUtil.isCellDateFormatted(cell)) {
						Date d = cell.getDateCellValue();
						if (cp.getParseFormat() != null) {
							return cp.getParseFormat().format(d);
						} 
					} 
				 
					if (CommonUtil.isNotNull(cp.getTryConvertSpecDate()) && cp.getTryConvertSpecDate()) {
						if (HSSFDateUtil.isCellDateFormatted(cell)){
							return DateUtil.convertDate2String(cell.getDateCellValue()).toString();
						} else {
							Date tDate = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
							if (tDate.before(DateUtil.convertString2Date("2010-01-01"))) {
								return value;
							}
							return DateUtil.convertDate2String(HSSFDateUtil.getJavaDate(cell.getNumericCellValue())).toString();
						}
					} else {
						double cellValue = cell.getNumericCellValue(); 	
						if (cellValue - new BigDecimal(cellValue).longValue() < Double.MIN_VALUE) {
							value = new DecimalFormat("#").format(cellValue);
						} else {
							value = cellValue+"";
						}
					}
				}
				if (CommonUtil.isNotNull(value)) {
					value = value.trim();
				}
				return value;
			}  else if ("java.lang.Integer".equals(classType) || "integer".equals(classType) || "int".equals(classType)) {
				value=value.replace(",", "");
				double dValue = Double.valueOf(value);
				if (isInt(dValue)) {
					return (int) dValue;
				}
				return Integer.parseInt(value);
			}   else if ("java.lang.Double".equals(classType) || "double".equals(classType)) {
				value=value.replace(",", "");
				return Double.parseDouble(value);
			}  else if ("java.lang.Float".equals(classType) || "float".equals(classType)) {
				value=value.replace(",", "");
				return Float.parseFloat(value);
			}   else if ("java.lang.Short".equals(classType) || "short".equals(classType)) {
				return Short.parseShort(value);
			}  else if ("java.lang.Long".equals(classType) || "long".equals(classType)) {
				return Long.parseLong(value);
			}  else if ("java.math.BigDecimal".equals(classType)) {
					value=value.replace(",", "").replace("¥", "");
					return new BigDecimal(value);				
			} else if ("boolean".equals(classType) || "java.lang.Boolean".equals(classType)) {
				if ("y".equals(value.toLowerCase()) ||"yes".equals(value.toLowerCase()) || "是".equals(value) ) {
						return true;
				} else {
					return false;
				}
			} 
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof ValidateException ) {
				throw new ValidateException(e.getMessage());
			} else {
				e.printStackTrace();
				throw new ValidateException( "第"+(cell.getRowIndex()+1-0)+"行"+cp.getCol()+"列 "+ cp.getLable()+" ("+value+") ： 数据类型有误， 请查阅！");

			}
		}
		
	}
	
	private static Object getObjectFromCell(Cell cell, String classType)   {
		try {
			if (cell == null ) {
				return null;
			}
			if ( "java.util.Date".equals(classType)) {
				return cell.getDateCellValue();
			}
			String value = getCellStringValue(cell);
			if (CommonUtil.isNull(value)) {
				return null;
			}
			if ("java.lang.String".equals(classType)) {
				return value;
			}  else if ("java.lang.Integer".equals(classType) || "integer".equals(classType) || "int".equals(classType)) {
				double dValue = Double.valueOf(value);
				if (isInt(dValue)) {
					return (int) dValue;
				}
				return Integer.parseInt(value);
			}   else if ("java.lang.Double".equals(classType) || "double".equals(classType)) {
				return Double.parseDouble(value);
			}  else if ("java.lang.Float".equals(classType) || "float".equals(classType)) {
				return Float.parseFloat(value);
			}   else if ("java.lang.Short".equals(classType) || "short".equals(classType)) {
				return Short.parseShort(value);
			}  else if ("java.lang.Long".equals(classType) || "long".equals(classType)) {
				return Long.parseLong(value);
			}  else if ("java.math.BigDecimal".equals(classType)) {
				return new BigDecimal(value);
			} else if ("boolean".equals(classType) || "java.lang.Boolean".equals(classType)) {
				if ("y".equals(value.toLowerCase()) ||"yes".equals(value.toLowerCase()) || "是".equals(value) ) {
					return true;
				} else {
					return false;
				}
			} 
			return value;
		} catch (Exception e) {
			if (e instanceof ValidateException ) {
				throw new ValidateException(e.getMessage());
			} else {
				e.printStackTrace();
				throw new ValidateException( "第"+(cell.getRowIndex()+1-0)+"行"+cell.getColumnIndex()+"列 ： 数据类型有误， 请查阅！");
				
			}
		}
	}
	
	private static boolean isInt(double value){
	    return (value % 1d) == 0d;
	}
	
	public static String getCellStringValue(Cell cell) {
		if (cell == null || Cell.CELL_TYPE_BLANK == cell.getCellType() || Cell.CELL_TYPE_ERROR==cell.getCellType()) {
			return null;
		}
		Object o = null;
		int cellType = cell.getCellType();
		 if (Cell.CELL_TYPE_FORMULA == cellType) {
				cellType = cell.getCachedFormulaResultType();
		 }
		if (Cell.CELL_TYPE_BOOLEAN == cellType) {
			Boolean b = cell.getBooleanCellValue();
			if (b != null && b) {
				return "Y";
			} else {
				return "N";
			}
		} else if (Cell.CELL_TYPE_NUMERIC == cellType) {
			//o = cell.getNumericCellValue();
			DecimalFormat df = new DecimalFormat("#.######");    
			o = df.format(cell.getNumericCellValue()); 
		}  else {
			o = cell.getStringCellValue();
		}
		if (o!= null) {
			return o.toString();
		} else {
			return null;
		}
	}
	
	public static boolean isSpaceRow(Row row){
		if (row == null) {
			return true;
		}
		int lastCell = row.getLastCellNum() ;
		   int phLastCell = row.getPhysicalNumberOfCells();
		   int cellNumber = (lastCell >phLastCell)?lastCell:phLastCell;
		   boolean  is = false; 
		   for(int i = 0 ; i < cellNumber; i++ ) {
			  Cell cell = row.getCell(i);
			  if( cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK ) {
				  is = true; 
			  } else if( cell.getCellType() == Cell.CELL_TYPE_STRING ){
				String value = cell.getStringCellValue();
				if( CommonUtil.isNull(value) ) {
				    is = true;	
				} else {
					return false;
				}
			  }else {
				  return false;
			  }
		   }
		   return is;	
	}
	
	public static boolean isNullCell(Cell cell ) {
		if( cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK ) {
			  return true; 
		  } else if( cell.getCellType() == Cell.CELL_TYPE_STRING ){
			String value = cell.getStringCellValue();
			if( CommonUtil.isNull(value) ) {
			    return true;	
			} else {
				return false;
			}
		  }else {
			  return false;
		  }
	}
	/**
	 * **2 维数组， 
	 * @param sa
	 * @return
	 */
	public static Map<String,String> arr2Map(String[][] sa) {
		Map<String,String> map = new HashMap<String,String>();
		for (String[] s : sa) {
			map.put(s[0], s[1]);
		}
		return map;
	}
	/**
	 * selectItem name作为KEY, keyCode作为value
	 * @param list
	 * @return
	 */
	public static Map<String,String> selectItem2Map(List<SelectItem> list) {
		Map<String,String> map = new HashMap<String,String>();
		for (SelectItem si : list) {
			map.put(si.getItemName(), si.getItemKey());
		}
		return map;
	}
	public static Map<String, String> list2Map(
			List  list, String keyProperty,
			String valueProperty) {
		Map<String,String> map = new HashMap<String,String>();
		if (!CommonUtil.isNull(list)) {
			for (Object obj : list) {
				Object key = YqBeanUtil.getPropertyValue(obj, keyProperty);
				Object value = YqBeanUtil.getPropertyValue(obj, valueProperty);
				map.put(key.toString(), value.toString());
			}
		}
		return map;
	}
	
	/**
	 * 用于将Excel表格中列号字母转成列索引，从1对应A开始   
	 * column ="B",  result = 2
	 * column ="BR", result= 70
	 */
	public static int columnToIndex(String column) {
		if (!column.matches("[A-Z]+")) {
			try {
				throw new Exception("Invalid parameter");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		int index = 0;
		char[] chars = column.toUpperCase().toCharArray();
		for (int i = 0; i < chars.length; i++) {
			index += ((int) chars[i] - (int) 'A' + 1) * (int) Math.pow(26, chars.length - i - 1);
		}
		return index;
	}

	/**
	 * 用于将excel表格中列索引转成列号字母，从A对应1开始
	 * index 1 return B
	 */
	public static String indexToColumn(int index) {
		if (index <= 0) {
			try {
				throw new Exception("Invalid parameter");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		index--;
		String column = "";
		do {
			if (column.length() > 0) {
				index--;
			}
			column = ((char) (index % 26 + (int) 'A')) + column;
			index = (int) ((index - index % 26) / 26);
		} while (index > 0);
		return column;
	}
	
	public static CsvSheet readCsv(File file, int length,Integer[] endSingCols,String brackets,String charset) throws IOException{
		// 读取csv文件
		System.out.println("readCsv start "+DateUtil.getCurrentStringDate(DateUtil.DEFAULT_DATE_TIME_FORMAT));
		CsvSheet sheet = new CsvSheet();
		BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream( file ), CommonUtil.nvl(charset, "GBK").toString()));
		String line;
		// 创建结果集,泛型可以根据实际情况定 
		while ((line = br.readLine()) != null) {
			line = line.replace(brackets, "");
			String[] split = line.split(",");
			CsvRow row = new CsvRow(length);
			for (int i = 0; i < split.length; i++) {
				if ( i < length) { // 超过不读， 或略
					row.addCellValue(i,split[i]); 
				}
			}
			if (!endRow(row,endSingCols)) {
				sheet.addRow(row);
			} 
		}
		br.close();
		System.out.println("readCsv end "+DateUtil.getCurrentStringDate(DateUtil.DEFAULT_DATE_TIME_FORMAT));
		return sheet; 
	}
	
	private static String cutBrackets(String value, String breackets) {
		if (CommonUtil.isNull(value)) {
			return value;
		}
		if (CommonUtil.isNull(breackets)) {
			return value;
		} else if (value.startsWith(breackets)) {
			return value.substring(1,value.length()-1);
		} else {
			return value;
		}
	}
	
	public static boolean endRow(CsvRow record2, Integer[] endSignCol) {
		if (record2 != null && CommonUtil.isNotNull(endSignCol)) {
			for (int i : endSignCol) {
				if (CommonUtil.isNotNull(record2.getCellValue(i))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	public static boolean endRow(String[] data , Integer[] endSignCol) {
		if (data != null && CommonUtil.isNotNull(endSignCol)) {
			for (int i : endSignCol) {
				if (i<data.length && CommonUtil.isNotNull(data[i])) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	public static boolean endRow(Row record2, Integer[] endSignCol) {
		if (record2 != null && CommonUtil.isNotNull(endSignCol)) {
			for (int i : endSignCol) {
				Cell cell = record2.getCell(i);
				if (!isNullCell(cell) ) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	private static Object getObjectFromCsvCell(Object csvCellValue, String classType)   {
		try {
			if (CommonUtil.isNull(csvCellValue))   {
				return null;
			}
			if ( "java.util.Date".equals(classType)) {
				if (csvCellValue instanceof Date) {
					return csvCellValue;
				} else {
					return DateUtil.convertString2Date(csvCellValue.toString());
				} 
			}
			String value = csvCellValue.toString().trim();
			if (CommonUtil.isNull(value)) {
				return null;
			}
			if ("java.lang.String".equals(classType)) {
				return value;
			}  else if ("java.lang.Integer".equals(classType) || "integer".equals(classType) || "int".equals(classType)) {
				double dValue = Double.valueOf(value);
				if (isInt(dValue)) {
					return (int) dValue;
				}
				return Integer.parseInt(value);
			}   else if ("java.lang.Double".equals(classType) || "double".equals(classType)) {
				return Double.parseDouble(value);
			}  else if ("java.lang.Float".equals(classType) || "float".equals(classType)) {
				return Float.parseFloat(value);
			}   else if ("java.lang.Short".equals(classType) || "short".equals(classType)) {
				return Short.parseShort(value);
			}  else if ("java.lang.Long".equals(classType) || "long".equals(classType)) {
				return Long.parseLong(value);
			}  else if ("java.math.BigDecimal".equals(classType)) {
				if(value.endsWith("%")){
					value=value.replace("%", "");
				}
				return new BigDecimal(value);
			} else if ("boolean".equals(classType) || "java.lang.Boolean".equals(classType)) {
				if ("y".equals(value.toLowerCase()) ||"yes".equals(value.toLowerCase()) || "是".equals(value) ) {
					return true;
				} else {
					return false;
				}
			} 
			return value;
		} catch (Exception e) {
			if (e instanceof ValidateException ) {
				throw new ValidateException(e.getMessage());
			} else {
				e.printStackTrace();
				throw new ValidateException("数据转换错误!");
			}
		}
	}
	
	public static boolean isSpaceRow(CsvRow row){
		if (row == null || CommonUtil.isNull(row.getCellValues())) {
			return true;
		}
		for (Object v : row.getCellValues()) {
			if (CommonUtil.isNotNull(v)) {
				return false;
			}
		}
		return true;
		 	
	}
	public static CsvSheet readCsv(String file,Integer length,Integer[] endSignCols,String breackets,String charset) throws IOException{
		return readCsv(new File(file),length,endSignCols,breackets,charset);
	}
	
	/**
	 * Converts an Excel column name like "C" to a zero-based index.
	 * 
	 * @param name
	 * @return Index corresponding to the specified name
	 */
	public static int nameToColumn(String name) {
		int column = -1;
		for (int i = 0; i < name.length(); ++i) {
			int c = name.charAt(i);
			column = (column + 1) * 26 + c - 'A';
		}
		return column;
	}
	
	public static void main(String[] args) throws IOException {
		System.out.println(ExcelUtil.cutBrackets("aaa和", ""));
		double cellValue = 5.02197E5; 	
	     System.out.println(cellValue);
	     System.out.println(new BigDecimal(cellValue).longValue());
	     System.out.println(cellValue-(long)cellValue);
		//5.0219791730116E13  50219791730116
		
	}
	
	
}
