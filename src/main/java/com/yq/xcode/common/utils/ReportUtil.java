package com.yq.xcode.common.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.yq.xcode.annotation.ParameterLogic;
import com.yq.xcode.common.bean.AggregateCol;
import com.yq.xcode.common.bean.BaseReport;
import com.yq.xcode.common.bean.DataTypeConstants;
import com.yq.xcode.common.bean.SelectItem;
import com.yq.xcode.common.criteria.CriteriaParameter;
import com.yq.xcode.common.criteria.NativeCriteria;
import com.yq.xcode.common.exception.ValidateException;
import com.yq.xcode.common.model.ReportColumnDefine;

/**
 * 字符默认是like, BigDecimal默认是isPair, 日期默认是 isPair， 其他数字为 = 
 * @author jt
 *
 */

public class ReportUtil  {
	 
  
  
	
	/**
	 * 数据库列
	 * 
	 * @return
	 */
	public static List<CriteriaParameter>  genParametersByCriteria( NativeCriteria  criteria) {
		if (criteria == null) {
			return null;
		}
		Class clazz = criteria.getClass();
		List<CriteriaParameter> pdList = new ArrayList<CriteriaParameter>();
		Field[] fs = clazz.getDeclaredFields(); // 得到所有的fields
		for (Field f : fs) {
			Class fieldClazz = f.getType();
			ParameterLogic column = f.getAnnotation(ParameterLogic.class);
			if (column != null) {
				CriteriaParameter pd = new CriteriaParameter();
				pd.setColName(column.colName());
				pd.setOperator(column.operation());
				pd.setPropertyName(f.getName());
				pd.setColLable(column.lable());
				pd.setCompareType(genCompareTypeByProType(f.getType().getName()));
				pd.setPlaceHolder(column.placeHolder());
				pd.validateParameter();
				String sValue = "";
				Object value = YqBeanUtil.getPropertyValue(criteria, pd.getPropertyName());
				if (CommonUtil.isNotNull(value)) {
					if (value instanceof Date) {
						sValue = DateUtil.convertDate2String((Date)value);
					} else {
						sValue = value.toString();
					}
				}
				pd.setParameterValue(sValue);
				pdList.add(pd);
			}
		}
		return pdList;
	}
	
	private static String genCompareTypeByProType(String proType) {
		String classType = proType;
		if ("java.lang.String".equals(proType)  ) {
			return DataTypeConstants.COMPARE_TYPE_CHAR;
		} else if ("java.util.Date".equals(classType)  ) {
			return DataTypeConstants.COMPARE_TYPE_DATE;
		} else if ("boolean".equals(classType) || "java.lang.Boolean".equals(classType)) {
			return DataTypeConstants.COMPARE_TYPE_BOOLEAN;
		}  else {
			return DataTypeConstants.COMPARE_TYPE_NUMBER;
		}
	}

	public static void genReportFile(List<BaseReport> data,
			List<ReportColumnDefine> cols, String fullName, String sheetName, List<CriteriaParameter> parameters) {
		int maxPageRow = 60000; //最大记录数
		HSSFWorkbook workbook=new HSSFWorkbook();
		if (cols.isEmpty()) {
			throw new ValidateException("无导出列定义!");
		}
		HSSFSheet sheet = null;
		if (CommonUtil.isNull(data) || data.size() <= maxPageRow) {
			if (CommonUtil.isNull(sheetName)) {
				sheet = workbook.createSheet("sheet1");
			}  else {
				sheet = workbook.createSheet(sheetName);
			}
			writeSheet(cols, sheet, data);
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
				writeSheet(cols, sheet, data.subList((seq-1)*maxPageRow, toSize));
				lostSize = lostSize - maxPageRow;
				seq++;
			}
		}
		addParameterSheet(workbook,parameters);

		
		File file=new File(fullName);
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
	    System.out.println("End ... ");
	}
	
	private static void addParameterSheet(HSSFWorkbook workbook,List<CriteriaParameter> parameters) {
		if (CommonUtil.isNotNull(parameters)) {
			HSSFSheet sheet = workbook.createSheet("报表参数");
			HSSFCell cell =getCell(sheet,0,0);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue("参数名称");
			cell =getCell(sheet,0,1);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue("参数值");
			for (int i=0; i<parameters.size(); i++) {
				CriteriaParameter p = parameters.get(i);
				//参数
				HSSFCell parCell = getCell(sheet,i+1,0);
				parCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				parCell.setCellValue((String)CommonUtil.nvl(p.getColLable(),p.getPropertyName()));
				//参数值
				HSSFCell valueCell = getCell(sheet,i+1,1);
				valueCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				valueCell.setCellValue(p.getParameterValue());
				
			}
		}
	}
	
	private static <T> void writeSheet(List<ReportColumnDefine> cols,HSSFSheet sheet,List<T> data) {
		for (int i=0; i<cols.size(); i++) {
			//ExportCol col = cols[i];
			ReportColumnDefine col = cols.get(i);
			HSSFCell cell =getCell(sheet,0,i);
			cell.setCellValue(col.getColLable());
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			CellStyle style = cell.getCellStyle();
			//style.setVerticalAlignment(VerticalAlignment.CENTER);
			cell.setCellStyle(style);

		}
		if (!CommonUtil.isNull(data)) {
			for (int i=0; i<data.size(); i++) {
				T bean = data.get(i);
				for (int j=0; j<cols.size(); j++) {
					//ExportCol col = cols[j];
					HSSFCell cell = getCell(sheet,i+1,j);
					//cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					//cell.setCellValue(valueToString(bean,cols.get(j).getPropertyName()));
					setCellValue(cell, bean,cols.get(j).getPropertyName());
				}
			}

			HSSFCell cell = getCell(sheet,data.size()+2,0);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue("生成报表时间:" + DateUtil.convertDate2String(DateUtil.getCurrentDate(),DateUtil.DEFAULT_DATE_TIME_FORMAT));
			
		}
		
	}
	
	private static void setCellValue(HSSFCell cell, Object bean, String field ) {
		try{
			String sName = PropertyUtils.getPropertyType(bean, field).getName();
			Object value = BeanUtils.getProperty(bean, field);
			if (value != null) {
				if ("java.math.BigDecimal".equals(sName)  ) {
					cell.setCellValue(new Double(value.toString()));
				} else {
					cell.setCellValue(value.toString());
				}
			}
		}catch(Exception ex){
			throw new ValidateException("Invalid property '" + field + "' of bean class [" + bean.getClass().getName() + "]: " + ex.getMessage() );
		}
	}
	
	private static HSSFCell getCell(HSSFSheet sheet, int row, int col) {
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
	private static <T> String valueToString(T bean,String field ) {
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

 
	
	
	public static List<AggregateCol> mapToAggregates(String aggregateJsonStr)  {
		if (CommonUtil.isNull(aggregateJsonStr)) {
			return null;
		}
		Map<String, String> aggregateMap;
		try {
			aggregateMap = YqJsonUtil.readJson2Entity(aggregateJsonStr, HashMap.class);
		} catch ( Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ValidateException(" json 转 map 格式错误 ！");
		}  
		List<AggregateCol> list = new ArrayList<AggregateCol>();
		if (CommonUtil.isNotNull(aggregateMap) ) {
			Iterator it = aggregateMap.keySet().iterator();
			Map<String,AggregateCol> colMap = new HashMap<String,AggregateCol>();
			while (it.hasNext()) {
				String key =(String) it.next();
				String aggregate = aggregateMap.get(key);
				String[] sA = aggregate.split(",");
				for (String s : sA) {
					String[] tsa = s.split("-");
					for (String ts : tsa) {
						String cate = toAggCategory(ts);
						String colMapKey = key+cate;
						colMap.put(colMapKey, new AggregateCol(key, key, cate ));
					}
 				}
			}
		   list = new ArrayList<AggregateCol>(colMap.values());
 		}
		return list;
	}
	
	private static String toAggCategory(String s ) {
		if (s.contains(AggregateCol.CATEGORY_SUM)) {
			return AggregateCol.CATEGORY_SUM;
		} else if (s.contains(AggregateCol.CATEGORY_AVERAGE)) {
			return AggregateCol.CATEGORY_AVERAGE;
		} else if (s.contains(AggregateCol.CATEGORY_MAX)) {
			return AggregateCol.CATEGORY_MAX;
		} else if (s.contains(AggregateCol.CATEGORY_MIN)) {
			return AggregateCol.CATEGORY_MIN;
		} else if (s.contains(AggregateCol.CATEGORY_COUNT)) {
			return AggregateCol.CATEGORY_COUNT;
		} 
		throw new ValidateException("不存在的统计类型: " + s);
	}
	
	public static CriteriaParameter toParameter(ReportColumnDefine colDef) {
		CriteriaParameter p = new CriteriaParameter();
		p.setColLable((String)CommonUtil.nvl(colDef.getParameterLable(), colDef.getColLable()));
		p.setColName(colDef.getColName());
		p.setCompareType(colDef.getCompareType());
		if (CommonUtil.isNull(colDef.getOperator())) {
			if (DataTypeConstants.COMPARE_TYPE_CHAR.equals(colDef.getCompareType())) {
				p.setOperator("like");
			} else if (DataTypeConstants.COMPARE_TYPE_BOOLEAN.equals(colDef.getCompareType())) {
				p.setOperator("=");
			} else {
				p.setOperator("between");
			}
		} else {
			p.setOperator(colDef.getOperator());
		}		
		p.setParameterValue(colDef.getDefaultValue());
		p.setPlaceHolder(colDef.getPlaceHolder());
		p.setPropertyName(colDef.getPropertyName());
		//p.setValueSetCode(colDef.getValueSetCode());
		p.setHavingCause(colDef.isHavingCause()); 
		p.setMandatory(colDef.getParameterMandatory());
		return p;
	}
 
	public static SelectItem[] getInputTags() { 
		SelectItem[] siArr = new SelectItem[]{
//	    	new SelectItem(InputTextTag.TAG_KEY,InputTextTag.TAG_NAME),
//			new SelectItem(InputDateTag.TAG_KEY,InputDateTag.TAG_NAME),
//			new SelectItem(InputYearMonthTag.TAG_KEY,InputYearMonthTag.TAG_NAME),
//			new SelectItem(InputYearTag.TAG_KEY,InputYearTag.TAG_NAME),
//			new SelectItem(InputAmountTag.TAG_KEY,InputAmountTag.TAG_NAME),
//			
//			new SelectItem(InputHcomboxTag.TAG_KEY,InputHcomboxTag.TAG_NAME),			 
//			new SelectItem(InputHmultiselectTag.TAG_KEY,InputHmultiselectTag.TAG_NAME), 
//				 
//			new SelectItem(InputIntegerTag.TAG_KEY,InputIntegerTag.TAG_NAME),
//			new SelectItem(InputNumberTag.TAG_KEY,InputNumberTag.TAG_NAME),
//			
//			new SelectItem(InputPriceTag.TAG_KEY,InputPriceTag.TAG_NAME),	
//			new SelectItem(InputTextAreaTag.TAG_KEY,InputTextAreaTag.TAG_NAME), 
//			new SelectItem(InputAttachmentTag.TAG_KEY,InputAttachmentTag.TAG_NAME),
//			new SelectItem(InputBooleanTag.TAG_KEY,InputBooleanTag.TAG_NAME) 	
  		};
		return siArr;
	}
	
	/**
	 * 报表默认值用
	 * @param months
	 * @return
	 */
	
	public static String getDateAfterMonths(Integer months) {
		Date date = DateUtil.getCurrentDate();
		return DateUtil.convertDate2String(DateUtil.getDateAfterMonths(date, months));
	}
	/**
	 * 报表默认值用
	 * 多少天
	 * @param months
	 * @return
	 */
	
	public static String getDateAfterDays(Integer months) {
		Date date = DateUtil.getCurrentDate();
		return DateUtil.convertDate2String(DateUtil.getDateAfterDays(date, months));
	}
	
	/**
	 * 报表默认值用
	 * @param 月份
	 * @return
	 */
	
	public static String getMonthAfterMonths(Integer months) {
		Date date = DateUtil.getCurrentDate();
		return DateUtil.convertDate2String(DateUtil.getDateAfterMonths(date, months),"yyyy-MM");
	}
	
	/**
	 * 报表默认值用
	 * @param 月份
	 * @return
	 */
	
	public static String getYearAfterYears(Integer year,Integer years) {
		return String.valueOf(year+years);
	}
 
}
