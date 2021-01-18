package com.yq.xcode.common.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.yq.xcode.common.bean.ToolFunctionView;
import com.yq.xcode.common.exception.ParseExprException;


public class ParseUtil {

	public final static int DEFAULT_SCALE = 2;
	public static ParseUtil instance = new ParseUtil();
	private static Log log = LogFactory.getLog(ParseUtil.class);
	
	public static boolean isNull(Object obj) {
		if ( obj == null) {
			return true;
		}
		if (obj instanceof Long) {
			Long str = (Long) obj;
			if (str.equals(0L)) {
				return true;
			}
		} else if (obj instanceof String) {
			String str = (String) obj;
			if ("".equals(str.trim())) {
				return true;
			}
		} else if (obj instanceof Collection) {
			Collection c = (Collection) obj;
			if (c.isEmpty()) {
				return true;
			}
		} else if (obj instanceof Map) {
			Map map = (Map) obj;
			if (map.isEmpty()) {
				return true;
			}
		} else if (obj instanceof Iterator) {
			Iterator iter = (Iterator) obj;
			if (!iter.hasNext()) {
				return true;
			}
		} else if (obj instanceof Enumeration) {
			Enumeration em = (Enumeration) obj;
			if (!em.hasMoreElements()) {
				return true;
			}
		} else if (obj.getClass().isArray()) {
			Object[] objArr = (Object[]) obj;
			if (objArr.length <= 0) {
				return true;
			}
		} else {
			if ("".equals(obj.toString().trim())) {
				return true;
			}
		}
		return false;
	}


	public static Object nvl(Object o1, Object o2) {
		if (o1 == null) {
			return o2;
		}
		return o1;
	}


	public static int compare(BigDecimal v1, BigDecimal v2) {
		BigDecimal tmV1 = v1;
		BigDecimal tmV2 = v2;
		if (v1 == null) {
			tmV1 = new BigDecimal("0");
		}
		if (v2 == null) {
			tmV2 = new BigDecimal("0");
		}
		return tmV1.compareTo(tmV2);
	}

	
	public static boolean isZero(BigDecimal value) {
		if (value == null || value.compareTo(new BigDecimal("0")) == 0 ) {
			return true;
		}
		return false;
	}
	
	
	public static Object decode(Object ... oA) {
		return "11111111";
	}
	/**
	 * if (a) return b elase if (c) return d else return e
	 * @param oA
	 * @return
	 */
	public static Object caseWhen(Object ... oA) {
		if (CommonUtil.isNull(oA)) {
			return null;
		}
		for (int i=0; i<oA.length; i++) {
			Object obj = oA[i];
			if (i == oA.length - 1) {
				if(i%2==0) { //奇数,少了一个， 所以是奇数个
					 return obj;
				 } else {
					 return null;
				 }
			}
			if(i%2==0) { //
			     if (obj != null ) {
			    	 if (obj instanceof Boolean) {
			    		 if ((Boolean)obj) {
			    			 return oA[i+1];
			    		 } 
			    	 } else {
			    		 throw new ParseExprException("case when 参数["+obj+"]类型出错， 判断的参数必须是Boolean");
			    	 }
			     }
			 }
		}
		return null;
	}
	
	/**
	 * str1 是否包含 str2
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean contains(String str1, Object str2) {
		if (str1 == null || str2 == null ) {
			return false;
		}
		return str1.contains(str2.toString());
	}
	
	
	/**
	 * str1 是否开始字符串 str2
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean startWith(String str1, Object str2) {
		if (str1 == null || str2 == null ) {
			return false;
		}
		return str1.startsWith(str2.toString());
	}
	
//	public static Object rangeValue(String betweenCode, Object inputValue) {
//		log.debug("range calulate "+betweenCode+" input "+inputValue);
//		if (CommonUtil.isNull(betweenCode)) {
//			throw new ValidateException("期间函数无效!");
//		}
//		if (CommonUtil.isNull(inputValue)) {
//			throw new ValidateException("期间输入值无效!");
//		}
//		BigDecimal value =  BigDecimal.ZERO; 
//		if (inputValue instanceof String) {
//			try {
//				value = new BigDecimal((String) inputValue);
//			} catch (NumberFormatException e) {
//				throw new ValidateException("期间输入值" + inputValue.toString() + "无法转换");
//			}
//		} else if (inputValue instanceof BigDecimal) {
//			value = (BigDecimal) inputValue;
//		} else if (inputValue instanceof BigInteger) {
//			value = new BigDecimal((BigInteger) inputValue);
//		} else if (inputValue instanceof Number) {
//			value = new BigDecimal(((Number) inputValue).doubleValue());
//		}
//		
//		BetweenReturnValueService betweenReturnValueService = 
//				SpringUtil.getBean("BetweenReturnValueService", BetweenReturnValueService.class);
//		String str = betweenReturnValueService.getReturnAsStringValueByCodeAndRefValue(betweenCode, value);
//		if (CommonUtil.isNull(str)) {
//			throw new ValidateException("无法找到对应的期间返回值,请检查期间函数设置!");
//		}
//		return str;
//	}
//	
	/**
	 * 计算2个日期之间的天数
	 * @param fromDate
	 * @param toDate
	 * @return
	 */

	public static Integer jsBetweenDays(Date fromDate, Date toDate) {
		if(fromDate==null || toDate == null) {
			return 0;
		}
		String dateString1=DateUtil.convertDate2String(fromDate);
		String dateString2=DateUtil.convertDate2String(toDate);
	    return DateUtil.getDaysBetween(dateString1,dateString2)+1;
	}
	
	public static Integer jsBetweenDays(String fromDate, String toDate) {
		if(fromDate==null || toDate == null) {
			return 0;
		}
		
	    return DateUtil.getDaysBetween(fromDate,toDate)+1;
	}
	
	
	public static Integer jsBetweenMonths(Date fromDate, Date toDate) {
		if(fromDate==null || toDate == null) {
			return 0;
		}
		String dateString1=DateUtil.convertDate2String(fromDate,"yyyy-MM");
		String dateString2=DateUtil.convertDate2String(toDate,"yyyy-MM");
	    return DateUtil.getMonthsBetween(dateString1,dateString2);
	}
	

	public static Date getCurrDate() {
		return DateUtil.getCurrentDate();
	}
	
 
	/**
	 * 指定月份的年度最后一天
	 * @return
	 */
	public static Date getYearMonthLastDay(String yearMonth) {
		if (CommonUtil.isNotNull(yearMonth)) {
			return DateUtil.convertString2Date(yearMonth.substring(0,4)+"-12-31");
		} else {
			return DateUtil.convertString2Date((DateUtil.convertDate2String(DateUtil.getCurrentDate(),"yyyy")+"-12-31"));
		}
		
	}
	/**
	 * 指定月份最后一天
	 * @return
	 */
	public static Date getMonthLastDay(String yearMonth) {
		return DateUtil.getMonthLastDayByDate(DateUtil.convertString2Date(yearMonth.substring(0,7)+"-01"));
	}
	/**
	 * 指定月份的年度di一天
	 * @return
	 */
	public static Date getYearMonthFirstDay(String yearMonth) {
		if (CommonUtil.isNotNull(yearMonth)) {
			return DateUtil.convertString2Date(yearMonth.substring(0,4)+"-01-01");
		} else {
			return DateUtil.convertString2Date((DateUtil.convertDate2String(DateUtil.getCurrentDate(),"yyyy")+"-01-01"));
		}
		
	}
	/**
	 * 指定月份di一天
	 * @return
	 */
	public static Date getMonthFirstDay(String yearMonth) {
		return DateUtil.getMonthFirstDayByDate(DateUtil.convertString2Date(yearMonth.substring(0,7)+"-01"));
	}
	/**
	 * 指定月份的天数
	 * @return
	 */
	public static Integer getYearMonthDays(String yearMonth) {
		Date firstDate = null;
		if (CommonUtil.isNotNull(yearMonth)) {
			firstDate = DateUtil.convertString2Date(yearMonth+"-01");
		} else {
			firstDate = DateUtil.convertString2Date(DateUtil.convertDate2String(DateUtil.getCurrentDate(),"yyyy-MM")+"-01");
		}
		return jsBetweenDays(firstDate, DateUtil.getDateAfterMonths(firstDate,1))-1;
	}
	
	/**
	 * 将字符转换为日期, 格式一定为'yyyy-MM-dd'
	 * @return
	 */
	public static Date convertString2Date(String stringDate) {
		return DateUtil.convertString2Date(stringDate);
	}
	
	
 
	
//	public static boolean exists(String nativeQuery, Object ... pars) {
//		UtilService utilService = 
//				SpringUtil.getBean("UtilService", UtilService.class);
//		
//		return utilService.existsByNativeQuery(nativeQuery, pars);
//	}
	
	public static Date addMonths(Date date, Integer months) {
		if (date == null) {
			return DateUtil.getCurrentDate();
		}
		return DateUtil.getDateAfterMonths(date, months);
	}
	
	public static String  dateFormat(Date date, String format) {
		String fm = format;
		if (CommonUtil.isNull(format)) {
			fm = "yyyy-MM-dd";
		}
		Date d = date;
		if (date == null) {
			d = DateUtil.getCurrentDate();
		}
		return DateUtil.convertDate2String(d,fm);
	}
	
	public String substr(String str, int from, int to) {
		if (CommonUtil.isNull(str)) {
			return str;
		}
		if (str.length() < to) {
			return str;
		}
		return str.substring(from, to);
	}
	
	public static Object roundedData(Object inputValue,int scale){
		BigDecimal mData = new BigDecimal(inputValue.toString()).setScale(scale, BigDecimal.ROUND_HALF_UP);
		return mData;
	}
	public static Object roundedDownData(Object inputValue,int scale){
		BigDecimal mData = new BigDecimal(inputValue.toString()).setScale(scale, BigDecimal.ROUND_DOWN);
		return mData;
	}
	/**
	 * 返回最大的日期， 如果有任何一个为空， 返回null
	 * @param dates
	 * @return
	 */
	public static Date maxDate(Date ... dates){
		if (CommonUtil.isNull(dates)) {
			return null;
		}
		Date maxDate = dates[0];
		if (maxDate == null) {
			return null;
		}
		for (int i=1; i<dates.length; i++) {
			Date date = dates[i];
			if (date == null) {
				return null;
			}
			if(maxDate.before(date)) {
				maxDate = date;
			}
		}
		return maxDate;
	}
	
	/**
	 * 返回计算的金额类型
	 * @return
	 */
	public static Double toDouble(Object obj){
		
		if (CommonUtil.isNull(obj)) {
			return 0d;
		}
 		return Double.valueOf(obj.toString());
	}
	

	public static BigDecimal compoundingCalc(Date startDate, String periodMonth, int offsetMonth, int loopMonth, BigDecimal ratio){
		Date monthLastDate = DateUtil.getMonthLastDayByDate(DateUtil.convertString2Date(periodMonth, "yyyy-MM"));
		ratio = JsUtil.calculate(ratio, "+", BigDecimal.ONE,-1);
		int monthdiff = jsBetweenMonths(startDate, monthLastDate); //月份差
		if (monthdiff <= offsetMonth) {
			return BigDecimal.ONE;
		}
		int calc1 = monthdiff - offsetMonth-1;
		if (calc1 % loopMonth != 0) {
			//直接返回倍率
			int c1 =  calc1 / loopMonth;
			Double d1 = Math.pow(ratio.doubleValue(), c1+1);
			return new BigDecimal(d1).setScale(8, BigDecimal.ROUND_HALF_UP);
		} else {
			//如果是 和 startDate,同一个月份，同时有满足loopMonth 的倍数， 按日期拆分 日期前的按上一期的数据， 日期后的按新的系数
			int i = DateUtil.getDayByDate(startDate);
			int j = DateUtil.getDayByDate(monthLastDate);
			if (i>j) {
				//2月29日的问题
				j = i;
			}
			int c1 =  calc1 / loopMonth;
			Double d1 = Math.pow(ratio.doubleValue(), c1);
			Double d2 = Math.pow(ratio.doubleValue(), c1+1);
//			(1*i*d1+1*(j-i+1)*d2)/j;
			BigDecimal db1 =  new BigDecimal(d1);
			BigDecimal db2 =  new BigDecimal(d2);
			BigDecimal p1Value =  JsUtil.calculate(new BigDecimal(i), "*", db1,8);
			int ddiff = j-i;
			BigDecimal p2Value =  JsUtil.calculate(new BigDecimal(ddiff), "*", db2,8);
			BigDecimal r = JsUtil.calculate(p1Value, "+", p2Value, 8) ;
			BigDecimal result =  JsUtil.calculate(r, "/", new BigDecimal(j), 4);			
			return result;
		}
	}	
	
	public static Date addDays(Date date,Integer days) {
		if (date == null) {
			return null;
		}
		return DateUtil.getDateAfterDays(date, days);
	}
	
	/**
	 * 分段收费， 段内单价不同，
	 * @param value 整数
	 * @param from 整数
	 * @param to 整数
	 * @param unitPrice
	 * @return
	 */
	public static BigDecimal rangeJsAmt(Object value, Object from, Object to, Object unitPrice){
		int bdValue = new Integer(CommonUtil.nvl(value, 0).toString()) ;
		int bdFrom = new Integer(CommonUtil.nvl(from, 0).toString()) ;
		int bdTo = new Integer(CommonUtil.nvl(to, 0).toString()) ;
		BigDecimal bdUnitPrice = new BigDecimal(CommonUtil.nvl(unitPrice, 0).toString()) ;
		if ( bdValue < bdFrom) {
			return BigDecimal.ZERO;
		}
		int qty = 0;
		if (CommonUtil.isNull(to) || bdValue <= bdTo) { // to 是空， 代表超过开始所有 + 1
			qty =  bdValue - bdFrom + 1; 
		} else if ( bdValue > bdTo) {
			qty =  bdTo - bdFrom + 1; 
		}
 		return JsUtil.calculate(new BigDecimal(qty), "*", bdUnitPrice,2);
 	}
	
	/**
	 * 最大值
	 * @param dates
	 * @return
	 */
	public static BigDecimal maxAmount(Object ... amts){
		if (CommonUtil.isNull(amts)) {
			return BigDecimal.ZERO;
		}
		BigDecimal maxValue = new BigDecimal(CommonUtil.nvl(amts[0],0).toString());
 
		for (int i=1; i<amts.length; i++) {
			BigDecimal value = new BigDecimal(CommonUtil.nvl(amts[i],0).toString()); 
 
			if(CommonUtil.compare(value, maxValue) > 0) {
				maxValue = value;
			}
		}
		return maxValue;
	}
	
	/**
	 * 最小值
	 * @param dates
	 * @return
	 */
	public static BigDecimal minAmount(Object ... amts){
		if (CommonUtil.isNull(amts)) {
			return BigDecimal.ZERO;
		}
		BigDecimal minValue = new BigDecimal(CommonUtil.nvl(amts[0],0).toString());
 
		for (int i=1; i<amts.length; i++) {
			BigDecimal value = new BigDecimal(CommonUtil.nvl(amts[i],0).toString()); 
 
			if(CommonUtil.compare(value, minValue) < 0) {
				minValue = value;
			}
		}
		return minValue;
	}
	
	
	
	public static final List<ToolFunctionView>  getParseToolFunction() {
		List<ToolFunctionView> list = new ArrayList<ToolFunctionView>();
		list.add(new ToolFunctionView("#pt.isNull(obj)","判断是否为空","任意类型参数，返回boolean！"));
		list.add(new ToolFunctionView("#pt.caseWhen(true?1, value1,  true?2,  value2,..., valueN)","多条件返回","当满足条件1时，返回值value1；<br/>否则满足条件2时，返回值value2；<br/>否则返回指定值valueN；<br/>可有多个条件"));
		list.add(new ToolFunctionView("#pt.rangeValue(期间代码, 比较值)","期间返回值","根据期间返回值和比较值 得到期间返回比"));
		list.add(new ToolFunctionView("#pt.contains(str1, str2)","是否包含值","str1 是否包含 str2"));
		list.add(new ToolFunctionView("#pt.startWith(str1, str2)","是否匹配开始字符串","str1 是否开始字符串 str2"));
		list.add(new ToolFunctionView("#pt.getCurrDate()","当前日期","返回当前系统的日期"));
		list.add(new ToolFunctionView("#pt.getYearMonthDays(yearMonth)","指定年月天数","参数为年月， 格式为 yyyy-MM,为空返回当月份的天数"));
		list.add(new ToolFunctionView("#pt.getYearMonthLastDay(yearMonth)","指定年月的最后一天","参数为年月， 格式为 yyyy-MM,为空返回当前年份的最后一天"));
		list.add(new ToolFunctionView("#pt.getMonthLastDay(yearMonth)","指定月的最后一天","参数为年月， 格式为 yyyy-MM,为空返回当前月份的最后一天"));
		list.add(new ToolFunctionView("#pt.getYearMonthFirstDay(yearMonth)","指定年月的第一天","参数为年月， 格式为 yyyy-MM,为空返回当前份的第一天"));
		list.add(new ToolFunctionView("#pt.getMonthFirstDay(yearMonth)","指定月的第一天","参数为年月， 格式为 yyyy-MM,为空返回当前月份的第一天"));
		list.add(new ToolFunctionView("#pt.jsBetweenDays(fromDate, toDate)","计算2个日期之间的天数","fromDate 为开始日期，toDate 为结束日期, 一定是日期类型"));
		list.add(new ToolFunctionView("#pt.jsBetweenMonths(fromDate, toDate)","计算2个日期之间的月数","fromDate 为开始日期，toDate 为结束日期, 一定是日期类型"));
		list.add(new ToolFunctionView("#pt.convertString2Date(stringDate)","将字符转换为日期","stringDate 字符串参数，格式一定是 yyyy-MM-dd "));
		list.add(new ToolFunctionView("#pt.addMonths(date,months)","日期加月份","返回 多少个月后的日期"));
		list.add(new ToolFunctionView("#pt.addDays(date,days)","日期加天数","返回 多少个日后的日期"));
		list.add(new ToolFunctionView("#pt.dateFormat(date,format)","将日期格式化","format 为空格式为yyyy-MM-dd"));
		list.add(new ToolFunctionView("#pt.substr(str,from, to )","截取字符串从from到to","截取字符串从from到to, 不足to返回str"));
		list.add(new ToolFunctionView("#pt.roundedData(inputValue,scale)","将inputValue按scale的位数进行四舍五入","将inputValue对scale的位数进行四舍五入"));
		list.add(new ToolFunctionView("#pt.roundedDownData(inputValue,scale)","将inputValue按scale的位数进行去除小数位","将inputValue按scale的位数进行去除小数位"));
		list.add(new ToolFunctionView("#pt.maxDate(Date ... )","多个日期比较取最大的","多个日期比较取最大的，如果任何一个值为空， 返回空 ！"));
		list.add(new ToolFunctionView("#pt.toDouble(inputValue)","转换为double类型","把输入的变量变为double类型！"));
		list.add(new ToolFunctionView("#pt.compoundingCalc(startDate, periodMonth, offsetMonth, loopMonth, ratio)","按时间周期计算复利系数","当前日期与起始月数小于offsetMonth，返回1, 大于offsetMonth, 每超过一个loopMonth, 多乘以1次 ratio （ratio为 0.1 表示10%）"));
		list.add(new ToolFunctionView("#eService.totalChargeItem(chargeCodes,#self,#chargeMap)","合计多个收费项的值","用英文“,”号分割的费用项目代码,#self,#chargeMap 两个是固定参数， 照抄就可以了"));
		list.add(new ToolFunctionView("#eService.calcAverageRepar(#self, year)","计算PMS安装后指定年的年平均Revpra","参数为 年数"));
		list.add(new ToolFunctionView("#eService.getCmsDtlTotal(#self, 'CHAINPD', 'col9', 'total_amount',categorys)","统计类型明细， 根据 summaryDtl","参数为指定类型字段"));
		list.add(new ToolFunctionView("#pt.rangeJsAmt(value, from, to, unitPrice )","分段收费， 段内单价不同 ","value,from,to 为整数， to 为空代表超过 from 所有"
				+ " 例如：value = 102,from = 101, to = 200 ， unitPrice = 0.5，计算结果为 1.00 元， 也就是两张单 "));
		list.add(new ToolFunctionView("#pt.maxAmount(amount1 ... )","取金额数值最大值， ","多个金额数值比较， 返回最大的一个值 ！"));
		list.add(new ToolFunctionView("#pt.minAmount(amount1 ... )","取金额数值最小值， ","多个金额数值比较， 返回最小的一个值 ！"));

		return list;
	}
	
 

	public static void main(String[] arg) {
		 //System.out.println(ParseUtil.dateFormat(ParseUtil.getCurrDate(), "MM"));
		
	//	 System.out.println(ParseUtil.getMonthFirstDay("2018-02"));
	//	 System.out.println(ParseUtil.getMonthLastDay("2018-02"));
		 
		Date d = DateUtil.convertString2Date("2004-02-29");
		Date d1 = ParseUtil.getMonthLastDay("2018-02");
		Date d3= ParseUtil.addMonths(ParseUtil.getMonthFirstDay("2018-02"),1);
		Date d4= ParseUtil.addMonths(ParseUtil.getMonthFirstDay("2018-02"),2);
		
		int n2=ParseUtil.jsBetweenDays( ParseUtil.addMonths(ParseUtil.getMonthFirstDay("2018-03"),1),  ParseUtil.addMonths(ParseUtil.getMonthFirstDay("2018-03"),2)) -1;
		
		int n1=ParseUtil.jsBetweenDays(d3, d4)-1;
		
	//	d1=d1+1;
		String d2 = ParseUtil.dateFormat(d1,"yyyy-MM-dd");	
		System.out.println("d1="+d1);
		System.out.println("d2="+d2);
		System.out.println("d3="+d3);
		System.out.println("d4="+d4);		
		System.out.println("n1="+n1);	
		System.out.println("n2="+n2);
		
		
		int i = 2004;
		int j = 3;
		int c = 1;
		for (; i < 2032; i++) {
			for (; j < 13; j++) {
				String s = "";
				if (j < 10) {
					s = "" + i + "-0" + j;
				} else {
					s = "" + i + "-" + j;
				}
		//		System.out.print(s);
		//		System.out.print("  第");
		//		System.out.print(c++);
		//		System.out.print("月   ");

		//		System.out.println(compoundingCalc(d, s, 24, 36, new BigDecimal(1.1)));
				if (j == 12) {
					j = 1;
					break;
				}
			}
		}
		// 2006-03 第61月 1.1390
		// System.out.println(compoundingCalc(d, "2006-02", 24, 36, new
		// BigDecimal(1.1)));
		// System.out.println(compoundingCalc(d, "2003-04", 24, 36, new
		// BigDecimal(1.1)));
		 
	}
}
