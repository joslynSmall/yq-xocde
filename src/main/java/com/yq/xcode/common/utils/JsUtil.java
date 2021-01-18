package com.yq.xcode.common.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sourceforge.pinyin4j.PinyinHelper;

public class JsUtil {
	public final static String ADD = "+";
	public final static String MINUS = "-";
	public final static String MULTIPLY = "*";
	public final static String DIV = "/";
	private static Log log = LogFactory.getLog(JsUtil.class);

	public static BigDecimal calculate(BigDecimal arg1, String operator,
			BigDecimal arg2, int scale) {
		BigDecimal value = null;
		if ( -1 == scale ) { // 没有指定保利位数， 按计算结果为准
			scale = CommonUtil.DISCOUNT_RATE_SCALE;
		}
		if (ADD.equals(operator.trim())) {
			value = nvlZero(arg1).add(nvlZero(arg2));
		}
		if (MINUS.equals(operator.trim())) {
			value = nvlZero(arg1).subtract(nvlZero(arg2));
		}
		if (MULTIPLY.equals(operator.trim())) {
			if (arg1 == null || arg2 == null) {
				return null;
			}
			value = arg1.multiply(arg2);
		}
		if (DIV.equals(operator.trim())) {
			if (arg1 == null || arg2 == null) {
				return null;
			}
			if (isZero(arg2)) {
				value = arg2;
			} else {
				//value = arg1.divide(arg2, BigDecimal.ROUND_HALF_UP);
//				value = arg1.divide(arg2);
				value = arg1.divide(arg2, scale, BigDecimal.ROUND_HALF_UP);
			}
		}
		if ( -1 == scale ) { // 没有指定保利位数， 按计算结果为准
			return value; 
		} else {
			value = value.setScale(scale, BigDecimal.ROUND_HALF_UP);
			return value;
		}
		
	}

	public static boolean isZero(BigDecimal value) {
		if (value == null || value.compareTo(new BigDecimal("0")) == 0) {
			return true;
		}
		return false;
	}

	public static BigDecimal nvlZero(BigDecimal value) {
		if (value == null) {
			return new BigDecimal(0);
		}
		return value;
	}

	public static BigDecimal calculateExpression(Object... arg) {
		BigDecimal value = (BigDecimal) arg[0];
		for (int i = 0; (2 * i + 2) < arg.length; i++) {
			value = calculate(value, (String) arg[2 * i + 1],
					(BigDecimal) arg[2 * i + 2], 10);
		}
		if (value != null) {
			value = value.setScale(CommonUtil.DEFAULT_SCALE,
					BigDecimal.ROUND_HALF_UP);
		}
		return value;
	}

	public static BigDecimal calculate(BigDecimal arg1, String operator,
			BigDecimal arg2) {
		return calculate(arg1, operator, arg2, CommonUtil.DEFAULT_SCALE);
	}
	
	public static BigDecimal add(BigDecimal arg1, BigDecimal arg2) {
		return calculate(arg1, ADD, arg2);
	}
	
	/**
	 * 
	 * @param arg1
	 * @param arg2
	 * @param scale  -1 为不设定精度
	 * @return
	 */
	public static BigDecimal add(BigDecimal arg1, BigDecimal arg2,int scale) {
		return calculate(arg1, ADD, arg2,scale);
	}

	public static BigDecimal multiply(BigDecimal arg1, BigDecimal arg2) {
		return calculate(arg1, MULTIPLY, arg2);
	}
	public static BigDecimal multiply(BigDecimal arg1, BigDecimal arg2,int scale) {
		return calculate(arg1, MULTIPLY, arg2,scale);
	}
	
	public static BigDecimal round(BigDecimal value, int scale) {
		return value.setScale(scale, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * * 鎻愬彇姣忎釜姹夊瓧鐨勯瀛楁瘝 *
	 * 
	 * @param str *
	 * @return String
	 */
	/**
	 * * 鎻愬彇姣忎釜姹夊瓧鐨勯瀛楁瘝 *
	 * 
	 * @param str *
	 * @return String
	 */
	public static String getPinYinHeadChar(String str) {
		if (CommonUtil.isNull(str)) {
			return str;
		}
		String convert = "";
		for (int j = 0; j < str.length(); j++) {
			char word = str.charAt(j);
			// 鎻愬彇姹夊瓧鐨勯瀛楁瘝
			String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
			if (pinyinArray != null) {
				convert += pinyinArray[0].charAt(0);
			} else {
				convert += word;
			}
		}
		return convert;
	}
	
	//璁＄畻瀛楄〃鍒颁富琛ㄧ殑鍚堣,鑷姩璁惧湪涓昏〃鐨勭浉鍏冲睘鎬т笂锛屼竴瀹氭槸鍚屼竴绉嶇被鍨嬶紝bigdecimal绫诲瀷
	public static void totalDetail(Object header, List lines, String headerProName, String lineProName ) {
		if (CommonUtil.isNull(lines)) {
			YqBeanUtil.setProperty(header, headerProName, new BigDecimal("0"));
		}
		BigDecimal total = new BigDecimal("0");
		for (Object line : lines) {
			total = JsUtil.calculate(total, "+", (BigDecimal)YqBeanUtil.getPropertyValue(line, lineProName));
		}
		YqBeanUtil.setProperty(header, headerProName, total);
	}
	
	// 鍔犲崈鍒嗕綅  279560.22 ->279,560.22
	public static String formaMicrometer(BigDecimal b) {
		DecimalFormat df = null;
		String text = b.toString();
		if (text.indexOf(".") > 0) {
			if (text.length() - text.indexOf(".") - 1 == 0) {
				df = new DecimalFormat("###,##0.");
			} else if (text.length() - text.indexOf(".") - 1 == 1) {
				df = new DecimalFormat("###,##0.0");
			} else {
				df = new DecimalFormat("###,##0.00");
			}
		} else {
			df = new DecimalFormat("###,##0");
		}
		return df.format(b);
	}
	
	public static BigDecimal toAmt(Object obj) {
		if (obj == null) {
			return new BigDecimal("0");
		}
		return new BigDecimal(obj.toString());
	}

	public static void main(String[] arg) { 
		BigDecimal a = new BigDecimal("28");
		BigDecimal b = new BigDecimal("40");
		System.out.println(a.divide(b, 2, BigDecimal.ROUND_HALF_UP));
		System.out.println(calculate(new BigDecimal("28"),"/",new BigDecimal("40")));
	}
}
