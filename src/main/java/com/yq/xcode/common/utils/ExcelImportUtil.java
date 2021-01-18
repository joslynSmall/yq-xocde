package com.yq.xcode.common.utils;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

//OA 系统excel模板导出
public class ExcelImportUtil {
	private static Log LOG = LogFactory.getLog(ExcelImportUtil.class);
	
	
	/**
	 * 指定开始行, 指定列为开始, 没找到 - 1 
	 * @param sheet
	 * @param functionStr
	 * @return
	 */
	public static int getSignStart(Sheet sheet, String col , String signStr) {
		int rowNumber = ExcelUtil.getMaxRowNumber(sheet);
		int colInt = ExcelUtil.getColByCh(col);
		for (int i=0; i<rowNumber; i++) {
			Row row = sheet.getRow(i);
			if ( row != null) {
				Cell cell = row.getCell(colInt);
				if (cell != null && signStr.equals(cell.getStringCellValue())) {
					return i;
				}
			} 
		}
		return -1;
	}
	
	/** 
	 * 字符截取， 且转换为日期
	 */
	public static Date substrToDate(Cell cell, Integer from, Integer to, String format) {
		if (cell == null || CommonUtil.isNull(cell.getStringCellValue())) {
			return null;
		}
		String value = cell.getStringCellValue();
		if (value.length() < to) { // 设定格式
			LOG.info("截取长度超过 cell 的值 ");
			return null; 
		}
		return DateUtil.convertString2Date(value.substring(from,to),format);
		
	}
	
	/** 
	 * 字符截取， 且转换为日期
	 */
	public static Date cellToDate(Object cellObj, String format) {
		if (cellObj == null   ) {
			return null;
		} 
		String value = null;
		if (cellObj instanceof Cell ) {
			Cell cell = (Cell)cellObj; 
			value = cell.getStringCellValue();
 		} else {
 			value = cellObj.toString().trim();
 		}
		if (CommonUtil.isNull(value)) {
			return null;
		}
		if("MM-dd".equals(format)){
			value = DateUtil.getYear()+"-"+value;
			format = "yyyy-MM-dd";
		}
 		return DateUtil.convertString2Date(value ,format);
		
	}
 
	public static Object caseWhen(Object ... oA) {
		return ParseUtil.caseWhen(oA);
	}
	
	public static Cell getCell(Row row,String col) {
		if (row == null) {
			return null;
		}
		return row.getCell(ExcelUtil.getColByCh(col));
	}
	
	public static boolean isNullRow(Row row) {
		return ExcelUtil.isSpaceRow(row);
	}
	
	public static boolean isNullCell(Row row,String col) {
		if (row == null) {
			return true;
		}
		return ExcelUtil.isNullCell(getCell(row,col));
	}
	
  
	/** 
	 * 毫秒数转日期
	 */
	public static Date millisecondToDate(Object cellObj) {
		if (cellObj == null   ) {
			return null;
		} 
		String value = null;
		if (cellObj instanceof Cell ) {
			Cell cell = (Cell)cellObj; 
			value = cell.getStringCellValue();
 		} else {
 			value = cellObj.toString().trim();
 		}
		if (CommonUtil.isNull(value)) {
			return null;
		}
		return new Date(Long.parseLong(value));
	}
	public static String filterEmoji(Object cellObj) {
		String source = null;
	    if(cellObj != null){
	    	source = cellObj.toString();
	        Pattern emoji = Pattern.compile ("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",Pattern.UNICODE_CASE | Pattern . CASE_INSENSITIVE ) ;
	        Matcher emojiMatcher = emoji.matcher(source);
	        if ( emojiMatcher.find()){
	            source = emojiMatcher.replaceAll("");
	            return source ;
	        }
	    return source;
	   }
	   return source;

	}

	
}
