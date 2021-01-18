package com.yq.xcode.common.utils;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.internet.MimeUtility;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.yq.xcode.annotation.SelectCategory;
import com.yq.xcode.annotation.SelectList;
import com.yq.xcode.common.bean.IdAndVersion;
import com.yq.xcode.common.bean.LookupCodeCategory;
import com.yq.xcode.common.bean.ParseStrModel;
import com.yq.xcode.common.bean.QueryModel;
import com.yq.xcode.common.bean.SelectItem;
import com.yq.xcode.common.bean.SelectItemDefine;
import com.yq.xcode.common.exception.ValidateException;
import com.yq.xcode.constants.YqLCConstants;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class CommonUtil {

	public final static int DEFAULT_SCALE = 2;
	public final static int DISCOUNT_RATE_SCALE = 8;
	public static CommonUtil instance = new CommonUtil();
	private static Log log = LogFactory.getLog(CommonUtil.class);

    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

	public static boolean isNotNull(Object obj) {
		return !isNull(obj);
	}

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
	public static PropertyDescriptor getBeanPropertyDescriptorByColumn(Object model, String colName) {
		String[] pros = colName.split("\\.");
		try {
			Object parObj = model;
			for (int i=0; i<pros.length - 1; i++) {
				String proName = pros[i];
				Object pro = PropertyUtils.getProperty(parObj, proName);
				if (pro == null) {
					Class id = PropertyUtils.getPropertyDescriptor(parObj, pros[i]).getPropertyType();
					if (id == null) {
						return null;
					}
					pro = id.newInstance();
				}
				parObj = pro;
			}
			return PropertyUtils.getPropertyDescriptor(parObj, pros[pros.length - 1]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("",e);
		}

		return null;
	}

	public static void setBeanProperty(Object model, String propertyName, Object value) {
		String[] pros = propertyName.split("\\.");
		try {
			Object parObj = model;
			for (int i=0; i<pros.length - 1; i++) {
				String proName = pros[i];
				Object pro = PropertyUtils.getProperty(parObj, proName);
				if (pro == null) {
					Class id = PropertyUtils.getPropertyDescriptor(parObj, pros[i]).getPropertyType();
					pro = id.newInstance();
					PropertyUtils.setProperty(parObj, proName, pro);
				}
				parObj = pro;
			}
			PropertyUtils.setProperty(parObj, pros[pros.length - 1], value);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("",e);
		}
	}
	public static void insertDataByPropertis(String tableName, String[][] colAndProperty, Object model,JdbcTemplate jdbcTemplate) {
		String cols = "";
		String values = "";
		String splitCh = "";
		List<Object> pars = new LinkedList<Object>();
		for (String[] cv : colAndProperty) {
			cols = cols+splitCh+cv[0];
			values = values+splitCh+"?";
			try {
				pars.add(PropertyUtils.getProperty(model, cv[1]));
			}catch (Exception e) {
				e.printStackTrace();
			}
			splitCh = ",";
		}
		String insertSql = " insert into "+tableName+" ( " +cols+" ) values ("+values+")";
		String prams="";
		for(Object str:pars)
		{
			prams=prams+"'"+str+"',";
		}
		log.info(insertSql);
		log.info(prams);
		jdbcTemplate.update(insertSql,pars.toArray());
	}

	public static String genInStrByList(String colname,String[] parameters) {
		String inStr = null;
		for (String par : parameters) {
			if (inStr == null) {
				inStr = "'"+par.replace("'", "''")+"'";
			} else {
				inStr = inStr + ",'"+par.replace("'", "''")+"'";
			}
		}
		return colname + " in ("+inStr+")";
	}

	public static String genInStrByList(String colname,List<String> parameters) {
		String inStr = null;
		for (String par : parameters) {
			if (inStr == null) {
				inStr = "'"+par.replace("'", "''")+"'";
			} else {
				inStr = inStr + ",'"+par.replace("'", "''")+"'";
			}
		}
		return colname + " in ("+inStr+")";
	}

	public static String genInStrBySet(String colname,Set set) {
		String inStr = null;
		if (CommonUtil.isNull(set)) {
			return " 1=2 ";
		}
		for (Object o : set) {
			String par = o.toString();
			if (inStr == null) {
				inStr = "'"+par.replace("'", "''")+"'";
			} else {
				inStr = inStr + ",'"+par.replace("'", "''")+"'";
			}
		}
		return colname + " in ("+inStr+")";
	}
	public static String genInLongBySet(String colname,Set<Long> set) {
		String inStr = null;
		if (CommonUtil.isNull(set)) {
			return " 1=2 ";
		}
		for (Object o : set) {
			String par = o.toString();
			if (inStr == null) {
				inStr =  par ;
			} else {
				inStr = inStr + ", "+par  ;
			}
		}
		return colname + " in ("+inStr+")";
	}

	public static String genIdInCause(String colName, long[] ids) {
		String inids = "";
		if (ids != null && ids.length != 0) {
			for (Long id : ids) {
				inids += "," + id + "";
			}
			inids = inids.substring(1);
			return colName + " in (" + inids + ")";
		}
		else
			return colName + " = 0";
	}
	public static String genIdInCause(String colName, List<Long> ids) {
		String inids = "";
		if (ids != null && ids.size() != 0) {
			for (Long id : ids) {
				inids += "," + id + "";
			}
			inids = inids.substring(1);
			return colName + " in (" + inids + ")";
		}
		else
			return colName + " = 0";
	}


	public static void main(String[] arg) {
		Class clazz = YqLCConstants.class;
		List<LookupCodeCategory> list = genStaticDefineList(clazz,LookupCodeCategory.class); 
		
	}

	public static QueryModel parseQueryParameters(Object criteria, String query) {
		QueryModel qm = new QueryModel();
		String allPars = "abcdefghijklmnopqrstuvwxyz0123456789_.";
		String tmQuery = query+" ";
		String parSign = ":";
		List pars = new LinkedList();
		while (true ) {
			int ind = tmQuery.indexOf(parSign);
			if (ind == -1) {
				break;
			}
			String preStr = tmQuery.substring(0, ind);
			String par = "";
			for (int i=ind+1; i<tmQuery.length(); i++) {
				String ch = tmQuery.substring(i,i+1);
				if (allPars.contains(ch.toLowerCase())) {
					par = par + ch;
				} else {
					tmQuery = preStr +"?"+tmQuery.substring(i,tmQuery.length());
					break;
				}
			}

			try {
				pars.add(PropertyUtils.getProperty(criteria, par));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		qm.setQuery(tmQuery);
		qm.setParameters(pars.toArray());
		return qm;
	}

	public static List mapToList(Map map) {
		if (isNull(map)) {
			return null;
		}
		List list = new LinkedList();
		Iterator it = map.keySet().iterator();
		while (it.hasNext()) {
			Object key = it.next();
			list.add(map.get(key));
		}
		return list;
	}

	public static BigDecimal stringToBigDecimal(String numStr) {
		if (CommonUtil.isNull(numStr)) {
			return BigDecimal.ZERO;
		}
		return new BigDecimal(numStr);
	}

	public static BigDecimal nvlZero(BigDecimal value) {
		if (value == null) {
			return new BigDecimal(0);
		}
		return value;
	}

	public static Object nvl(Object o1, Object o2) {
		if (o1 == null) {
			return o2;
		}
		return o1;
	}

	/**
	 * colName = a.sob_Code; colValues = "001,002,005-100,2001" ( a.sob_code in
	 * ('001',002','2001') or (a.sob_code b>='005 and a.sob_code b<='100'))
	 *
	 * @param colName
	 * @param relation
	 * @return
	 */
	public static String generateWhereCause(String colName, String colValues) {

		if (colValues == null || "".equals(colValues)) {
			return " (1=1) ";
		}
		StringBuffer whereCause = new StringBuffer("(");
		String colArray[] = colValues.split(",");
		List<String> colArrayList = new ArrayList<String>();
		for (int i = 0; i < colArray.length; i++) {
			if (colArray[i].indexOf("-") == -1) {
				colArrayList.add(colArray[i]);
			}
		}
		if (colArrayList.size() > 0) {
			whereCause.append(colName + " in ");
			int i = 0;
			String ix = "";
			for (String str : colArrayList) {
				if (i == 0) {
					ix += "'" + str.replace("'", "''") + "'";
				} else {
					ix += ",'" + str.replace("'", "''") + "'";
				}
				i++;
			}
			whereCause.append("(").append(ix).append(")");
		}
		List<String[]> colList = new ArrayList<String[]>();
		if (colArray != null) {
			for (String str : colArray) {
				if (str.indexOf("-") > 0) {
					colList.add(str.split("-"));
				}
			}
		}
		if (colList.size() > 0) {
			for (String[] str : colList) {
				if (colArrayList.size() > 0) {
					whereCause.append(" or ");
				}
				whereCause.append("(");
				String strTemp = "";
				if (str.length == 1) {
					strTemp += colName + ">='" + str[0].replace("'", "''")
							+ "'";
				} else {
					strTemp += colName + ">='" + str[0].replace("'", "''")
							+ "'";
					strTemp += " and ";
					strTemp += colName + "<='" + str[1].replace("'", "''")
							+ "'";
				}
				whereCause.append(strTemp).append(")");
			}
		}
		whereCause.append(")");
		return whereCause.toString().equals("()") ? "(1!=1)" : whereCause
				.toString();
	}
	public static String getInIDSql(String colName, Set<Long> IdSet) {
		String inids = "";
		if (IdSet != null && IdSet.size() != 0) {
			for (Long id : IdSet) {
				inids += "," + id + "";
			}
			inids = inids.substring(1);
			return colName + " in (" + inids + ")";
		}
		else
			return colName + " = 0";
	}

	public static String getInCodeSql(String colName, Set codeSet) {
		String incodes = "";
		if (codeSet != null && codeSet.size() != 0) {
			for (Object codeObj : codeSet) {
				String code = codeObj.toString();
				incodes += ",'" + code.replace("'", "''") + "'";
			}
			incodes = incodes.substring(1);
			return colName + " in (" + incodes + ")";
		}
		else
			return colName + " = ''";
	}

	public static String getInCodeSql(String colName, List<String> codeList) {
		String incodes = "";
		if (CommonUtil.isNotNull(codeList) && codeList.size() != 0) {
			for (String codeObj : codeList) {
				String code = codeObj.toString();
				incodes += ",'" + code.replace("'", "''") + "'";
			}
			incodes = incodes.substring(1);
			return colName + " in (" + incodes + ")";
		}
		else
			return colName + " = ''";
	}

	public static boolean isEquals(Object obj1, Object obj2) {
		if (obj1 == null && obj2 == null) {
			return true;
		}
		if (obj1 != null) {
			return obj1.equals(obj2);
		}
		return false;
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
	public static  Set<String> commaSeparatedString2Set(String commaSeparatedString)
	{
		Set<String> set = new HashSet<String>();
		String[] strs=commaSeparatedString.split(",");
		for(String str:strs)
		{
			set.add(str);
		}
		return set;
	}


	public static boolean isZero(BigDecimal value) {
		if (value == null || value.compareTo(new BigDecimal("0")) == 0 ) {
			return true;
		}
		return false;
	}

	public static Map ListToMap(List list,String propertyName) {
		Map map = new HashMap();
		if (!CommonUtil.isNull(list)) {
			for (Object obj : list) {
				Object key = YqBeanUtil.getPropertyValue(obj, propertyName);
				map.put(key, obj);
			}
		}
		return map;
	}
	public static Map<String,String> selectItemToNameKeyMap(List<SelectItem> list) {
		Map map = new HashMap();
		if (!CommonUtil.isNull(list)) {
			for (SelectItem si : list) {
				map.put(si.getItemName(), si.getItemKey());
			}
		}
		return map;
	}


	/**
	 *
	 * 处理中文文件名 (导出excel文件名使用)
	 * @param filename
	 * @param request
	 * @return
	 */
	public static String encodeFilename(String filename, HttpServletRequest request) {
		/**
		 * 获取客户端浏览器和操作系统信息
		 * 在IE浏览器中得到的是：User-Agent=Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; Maxthon; Alexa Toolbar)
		 * 在Firefox中得到的是：User-Agent=Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.7.10) Gecko/20050717 Firefox/1.0.6
		 */
		String agent = request.getHeader("USER-AGENT");
		try {
			if ((agent != null) && (-1 != agent.indexOf("MSIE"))) {
				String newFileName = URLEncoder.encode(filename, "UTF-8");
				newFileName = StringUtils.replace(newFileName, "+", "%20");
				if (newFileName.length() > 150) {
					newFileName = new String(filename.getBytes("GB2312"), "ISO8859-1");
					newFileName = StringUtils.replace(newFileName, " ", "%20");
				}
				return newFileName;
			}
			if ((agent != null) && (-1 != agent.indexOf("Mozilla")))
				return MimeUtility.encodeText(filename, "UTF-8", "B");

			return filename;
		} catch (Exception ex) {
			return filename;
		}
	}
	public static String trim(String value) {
		if (value != null) {
			return value.trim();
		}
		return value;
	}

	public static String buildRandomKey() {
		Date nowTime=new Date();
		SimpleDateFormat time=new SimpleDateFormat("MMddHHmmss");
		String str = time.format(nowTime) + Math.round(Math.random()*10000);
		return str;
	}

	private static final String[][] colMap = new String[][]{{"A","0"},{"B","1"},{"C","2"},{"D","3"},{"E","4"},
			{"F","5"},{"G","6"},{"H","7"},{"I","8"},{"J","9"},{"K","10"},{"L","11"},{"M","12"},{"N","13"},{"O","14"},
			{"P","15"},{"Q","16"},{"R","17"},{"S","18"},{"T","19"},{"U","20"},{"V","21"},{"W","22"},{"X","23"},{"Y","24"},{"Z","25"}
	};
	public static int colToInt(String colCh) {
		for (String[] sAr : colMap) {
			if (colCh.equals(sAr[0])) {
				return Integer.valueOf(sAr[1]);
			}
		}
		throw new ValidateException("列溢出");
	}
	public static String colToChar(int colInt ) {
		for (String[] sAr : colMap) {
			if (colInt == Integer.valueOf(sAr[1])) {
				return sAr[0];
			}
		}
		throw new ValidateException("列溢出");
	}

	public static String stringToJson(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {

			char c = s.charAt(i);
			switch (c) {
				case '\\':
					sb.append("、");
					break;
				default:
					sb.append(c);
			}
		}
		return sb.toString();
	}

	public static String rejectLastStr(String str) {
		if (null == str) {
			return "";
		}
		return str.substring(0, str.length() - 1);
	}

	public static String genLookupCodeName(String keyCodeCol, String propertyName ) {
		return " (select lc.lookup_name from yq_lookup_code lc where lc.key_code ="+keyCodeCol+") "+propertyName;
	}

	public static ParseStrModel parseStrByStardEndCh(String str, String start, String end) {
		ParseStrModel psm = new ParseStrModel();
		if (CommonUtil.isNull(str)) {
			psm.setPreStr(str);
			return psm;
		}
		String tmpFormal = str.toUpperCase();
		String tmpStart = start.toUpperCase();
		String tmpEnd = end.toUpperCase();
		int fromIndex = tmpFormal.indexOf(tmpStart);
		int endIndex = tmpFormal.indexOf(tmpEnd, fromIndex);
		if (fromIndex >= endIndex) {
			psm.setPreStr(str);
			return psm;
		}
		psm.setPreStr(str.substring(0, fromIndex));
		psm.setCurrStr(str.substring(fromIndex + start.length(), endIndex));
		psm.setPostStr(str.substring(endIndex + end.length()));
		psm.setSplitted(true);
		return psm;
	}



	/**
	 * 把＇[＇,＇]＇转成＇＂＇
	 * @param str
	 * @return
	 */
	public static String replaceHtmlValue(String str){
		return str=str.replace("[", "\"").replace("]", "\"");
	}

	public static Object decode(Object ... oA) {
		return "11111111";
	}
	public static Object caseWhen(Object ... oA) {
		return null;
	}

	public static String firstCharsOfPinyin(String chinese) {
		StringBuffer pybf = new StringBuffer();
		char[] arr = chinese.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > 128) {
				try {
					String[] _t = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
					if (_t != null) {
						pybf.append(_t[0].charAt(0));
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pybf.append(arr[i]);
			}
		}
		return pybf.toString().replaceAll("\\W", "").trim();
	}

	public static String idListToString(List<Long> list, char separator) {
		return org.apache.commons.lang.StringUtils.join(list.toArray(), separator);
	}

	public static <E, K> void groupView(Map<K, List<E>> viewMap, E value, K key) {
		List<E> views = viewMap.get(key);
		if (views == null) {
			views = new ArrayList<E>();
		}
		views.add((E)value);
		viewMap.put(key, views);
	}

	public static <K> void addUniqueIdToList(List<K> list, K key) {
		if (!list.contains(key)) {
			if (isNotNull(key)) {
				list.add(key);
			}
		}
	}

	public static  <K, E extends Number> void groupSummaryView(Map<K, E> viewMap, E value, K key) {
		if (viewMap.containsKey(key)) {
			if (value instanceof BigDecimal) {
				viewMap.put(key, (E)((BigDecimal)viewMap.get(key)).add((BigDecimal) value));
			}
			if (value instanceof Integer) {
				viewMap.put(key,  (E) new Integer(((Integer)viewMap.get(key))+(Integer)value));
			}
		} else {
			viewMap.put(key, value);
		}
	}

	public static <E, T>  List<E> getDataListByProperty(List<T> list, String property, Class<E> calss) {
		List<E> resultList = new ArrayList<E>();
		for (T t : list) {
			resultList.add((E) YqBeanUtil.getPropertyValue(t, property));
		}
		return resultList;
	}

	public static <K, E extends Number> void putSumDataToMap(Map<K, E> viewMap, E value, K key) {
		E valueData = viewMap.get(key);
		if (value instanceof BigDecimal) {
			if (CommonUtil.isNull(valueData)) {
				viewMap.put(key, value);
			} else {
				viewMap.put(key, (E) JsUtil.calculate((BigDecimal) valueData, "+", (BigDecimal) value,-1) );
			}
		}
		if (value instanceof Integer) {
			if (CommonUtil.isNull(valueData)) {
				viewMap.put(key, value);
			} else {
				viewMap.put(key, (E) new Integer((Integer) valueData + (Integer) value));
			}
		}
	}

	public static String buildRandomKey(int pwd_len){
		final int maxNum = 36;
		int i; //生成的随机数
		int count = 0; //生成的密码的长度
		char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
				'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
				'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

		StringBuffer pwd = new StringBuffer("");
		Random r = new Random();

		while(count < pwd_len){
			//生成随机数，取绝对�?�，防止生成负数�?

			i = Math.abs(r.nextInt(maxNum)); //生成的数�?大为36-1

			if (i >= 0 && i < str.length) {
				pwd.append(str[i]);
				count ++;
			}
		}
		return pwd.toString().toUpperCase();

	}

	/**
	 * 使用java正则表达式去掉多余的.与0
	 * @param s
	 * @return
	 */
	public static String subZeroAndDot(String s){
		if (CommonUtil.isNull(s)) {
			return s;
		}
		if(s.indexOf(".") > 0){
			s = s.replaceAll("0+?$", "");//去掉多余的0
			s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
		}
		return s;
	}

	public static String lpad(String s1, String ch, int len) {
		String ss = (String)nvl(s1,"");
		if (CommonUtil.isNull(ch)) {
			return s1;
		}
		while (ss.length() < len) {
			ss = ch+ss;
		}
		return ss;
	}
	public static boolean isNullId(Long id) {
		if (id == null || id == 0l || id.equals(0l)) {
			return true;
		}
		return false;
	}

	// 从指定的字符串截取非数字
	// content = 5f45Ad 8C7$@  return = fAd C$@
	public static String getCharFromString(String content) {
		Pattern pattern = Pattern.compile("\\D+");
		Matcher matcher = pattern.matcher(content);
		String result = "";
		while (matcher.find()) {
			result = result+matcher.group(0);
		}
		return result;
	}
	// 从指定的字符串中截取数字
	// content = 5f45Ad 8C7$@  return = 54587
	public static int getNumberFromString(String content) {
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(content);
		String result = "";
		while (matcher.find()) {
			result = result+matcher.group(0);
		}

		return Integer.parseInt(result);
	}
	public static Object trim(Object value) {
		if (value instanceof String && isNotNull(value)) {
			String str = (String) value;
			return trim(str);
		}
		return value;
	}


	public static Field getFieldByColumn(Object model, String colName) {
		String[] pros = colName.split("\\.");
		try {
			Object parObj = model;
			for (int i=0; i<pros.length - 1; i++) {
				String proName = pros[i];
				Object pro = PropertyUtils.getProperty(parObj, proName);
				if (pro == null) {
					Class id = PropertyUtils.getPropertyDescriptor(parObj, pros[i]).getPropertyType();
					if (id == null) {
						return null;
					}
					pro = id.newInstance();
				}
				parObj = pro;
			}
			return YqBeanUtil.getFieldIncSuperClazz(parObj.getClass(), pros[pros.length - 1]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("",e);
		}

		return null;
	}
	public static String toStr(Object obj) {
		if (obj == null) {
			return null;
		}
		return obj.toString();
	}

	public static String md5Encrypt(String str) throws Exception{
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes("UTF-8"));
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer();
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();


		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean checkFolder(String dirName) {
		File dir = new File(dirName);
		return checkFolder(dir);
	}

	public static boolean checkFolder(File dir) {
		if (!dir.exists()) {
			if (dir.mkdirs()) {
				System.out.println("创建目录" + dir.getName() + "成功！");
				return true;
			} else {
				System.out.println("创建目录" + dir.getName() + "失败！");
				return false;
			}
		}
		return false;
	}

	public static String subTextString(String str, int len) {
		if(null == str){
			return null;
		}
		if (str.length() < len / 2) {
			return str;
		}
		int count = 0;
		StringBuffer sb = new StringBuffer();
		String[] ss = str.split("");
		for (int i = 1; i < ss.length; i++) {
			count += ss[i].getBytes().length > 1 ? 2 : 1;
			sb.append(ss[i]);
			if (count >= len) {
				break;
			}
		}
		return (sb.toString().length() < str.length()) ? sb.append("...").toString() : str;
	}
	public static <E> Map<Object,List<E>> ListToMapList(List<E> list,String propertyName) {
		Map<Object,List<E>> map = new HashMap<Object,List<E>>();
		if (!CommonUtil.isNull(list)) {
			for (E obj : list) {
				Object key = YqBeanUtil.getPropertyValue(obj, propertyName);
				List<E> eList = map.get(key);
				if (eList == null) {
					eList = new ArrayList<E>();
					map.put(key, eList);
				}
				eList.add(obj);
			}
		}
		return map;
	}

	/**
	 * 一定是 用, 好分隔多条记录， 用 ^分隔id 和version  id^version,id^version
	 * @param idVersionStr
	 * @return
	 */
	public static List<IdAndVersion> toIdAndView(String idVersionStr) {
		if (CommonUtil.isNull(idVersionStr)) {
			return null;
		}
		List<IdAndVersion> list = new ArrayList<IdAndVersion>();
		String[] sa = idVersionStr.split(",");
		for (String s : sa) {
			String[] iv = s.split("\\^");
			IdAndVersion v = new IdAndVersion();
			v.setId(new Long(iv[0]));
			v.setVersion(new Integer(iv[1]));
			list.add(v);
		}
		return list;
	}

	public static String escape(String value) {
		if (CommonUtil.isNull(value)) {
			return value;
		}
		ScriptEngineManager sem = new ScriptEngineManager();
		ScriptEngine engine = sem.getEngineByExtension("js");
		try{
			//直接解析
			Object res = engine.eval(" escape('"+value+"')");
			return res.toString();
		}catch(Exception ex){
			ex.printStackTrace();
			return value;
		}
	}

	public static String unEscape(String value) {
		if (CommonUtil.isNull(value)) {
			return value;
		}
		ScriptEngineManager sem = new ScriptEngineManager();
		ScriptEngine engine = sem.getEngineByExtension("js");
		try{
			//直接解析
			Object res = engine.eval(" unescape('"+value+"')");
			return res.toString();
		}catch(Exception ex){
			ex.printStackTrace();
			return value;
		}
	}

	static Map<Integer, Character> index2titleMap = new HashMap<Integer, Character>();
	static {
		index2titleMap.put(1, 'A');
		index2titleMap.put(2, 'B');
		index2titleMap.put(3, 'C');
		index2titleMap.put(4, 'D');
		index2titleMap.put(5, 'E');
		index2titleMap.put(6, 'F');
		index2titleMap.put(7, 'G');
		index2titleMap.put(8, 'H');
		index2titleMap.put(9, 'I');
		index2titleMap.put(10, 'J');
		index2titleMap.put(11, 'K');
		index2titleMap.put(12, 'L');
		index2titleMap.put(13, 'M');
		index2titleMap.put(14, 'N');
		index2titleMap.put(15, 'O');
		index2titleMap.put(16, 'P');
		index2titleMap.put(17, 'Q');
		index2titleMap.put(18, 'R');
		index2titleMap.put(19, 'S');
		index2titleMap.put(20, 'T');
		index2titleMap.put(21, 'U');
		index2titleMap.put(22, 'V');
		index2titleMap.put(23, 'W');
		index2titleMap.put(24, 'X');
		index2titleMap.put(25, 'Y');
		index2titleMap.put(26, 'Z');
	}


	public static String valueToTitle(int n) {
		int row = n / 26;
		int column = n % 26;
		if (column == 0) {
			row -= 1;
			column = 26;
		}

		if (row == 0) {
			return String.valueOf(index2titleMap.get(column));
		} else if (row >= 1 && row <= 26) {
			return new StringBuilder().append(index2titleMap.get(row)).append(index2titleMap.get(column)).toString();
		} else {
			return null;
		}
	}
	/**
	 * 获得列表对应long型属性的数组 并去重
	 * @author eggsLee
	 * @date 2020/5/22
	 * @param list
	 * @param property long型属性名
	 * @param <E>
	 * @return long型property的数组
	 */
	public static <E> long[] listToIds(List<E> list, String property){

		Set<Long> idSet = new HashSet<Long>();
		for(E e : list){
			Object obj = YqBeanUtil.getPropertyValue(e, property);
			if (CommonUtil.isNotNull(obj)) {
				idSet.add(Long.parseLong(obj.toString()));
			}
		}
		long[] ids = idSet.stream().mapToLong(t -> t.longValue()).toArray();
		return ids;
	}

	/**
	 * 获得列表对应long型属性的字符串，并去重
	 * @author eggsLee
	 * @date 2020/5/22
	 * @param list
	 * @param property long型属性名
	 * @param <E>
	 * @return long型property的字符串
	 */
	public static <E> String listToIdString(List<E> list, String property){

		long[] ids = listToIds(list, property);
		String id = ArrayUtils.toString(ids, ",")
				.replace("{", "")
				.replace("}", "");
		return id;
	}

	public static boolean isEques(Object obj1, Object obj2) {
		if (obj1 == null && obj2 == null) {
			return true;
		}
		if (obj1 != null) {
			return obj1.equals(obj2);
		}
		return false;
	}
	
	public static List<List<?>> splitList(List<?> list, int len) {
		if (list == null || list.size() == 0 || len < 1) {
			return null;
		}
		List<List<?>> result = new ArrayList<List<?>>();
		int size = list.size();
		int count = (size + len - 1) / len;
		for (int i = 0; i < count; i++) {
			List<?> subList = list.subList(i * len,	((i + 1) * len > size ? size : len * (i + 1)));
			result.add(subList);
		}
		return result;
	}
	
	public static boolean isNotZero(BigDecimal value) {
		return !isZero(value);
	}
	
	public static String getInIDSql(String colName, List<Long> IdSet) {
		String inids = "";
		if (IdSet != null && IdSet.size() != 0) {
			for (Long id : IdSet) {
				inids += "," + id + "";
			}
			inids = inids.substring(1);
		return colName + " in (" + inids + ")";
		}
		else
		return colName + " = 0";
	}
	
	public static SelectItem getSelectItemByKey (List<SelectItem> siList , String key) {
		if (siList == null ) {
			return null;
		}
		for (SelectItem si : siList) {
			if (si.getItemKey().equals(key)) {
				return si ;
			}
		}
		return null;
	}
	
	public static String getSelectItemNameByKey(List<SelectItem> siList, String key) {
		if (siList == null ) {
			return null;
		}
		for (SelectItem si : siList) {
			if (si.getItemKey().equals(key)) {
				return si.getItemName();
			}
		}
		return null ;
	}
	
	public static void getSelectItemNameByKey(List<?> eList, List<SelectItem> siList, String propertyName) {
		if (eList == null || siList == null ) {
			return ;
		}
		for (Object e : eList) {
			YqBeanUtil.setBeanProperty(e, propertyName, getSelectItemNameByKey(siList,propertyName));
		} 
	}
	
	public static List<SelectItemDefine> genSelectItemDefineList(Class constansClazz ) {
		List<SelectItemDefine> list = new ArrayList<SelectItemDefine>();
		Class[] fs = constansClazz.getDeclaredClasses(); // 得到所有的fields  
		for (Class clazz : fs)   {
			SelectCategory sCate = (SelectCategory) clazz.getAnnotation(SelectCategory.class);
			if ( sCate != null ) {
				List<SelectItem> sList = null;
				List<Field> declaredFields = YqBeanUtil.getFieldsByAnnotation(clazz, SelectList.class);
				if (CommonUtil.isNotNull(declaredFields)) {
					Field field = declaredFields.get(0);
					field.setAccessible(true);
			        if( Modifier.isStatic(field.getModifiers())){
			        	try {
							sList = (List<SelectItem>)field.get(clazz);
 						} catch ( Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}   
			        }
					
				}
				if (sList != null ) {
					list.add(new SelectItemDefine(sCate.code(), sCate.name(),  sList,null));
				}
			     
			}
			
    	}  
		return list;
	}
	
	/**
	 * 静态属性值
	 * @param constansClazz
	 * @param retClass
	 * @return
	 */
	public static <E> List<E> genStaticDefineList(Class constansClazz, Class<E> retClass ) {
		List<E> list = new ArrayList<E>(); 
		Field[] declaredFields = constansClazz.getDeclaredFields();
		if (CommonUtil.isNotNull(declaredFields)) {
			for (Field field : declaredFields ) { 
				field.setAccessible(true);
		        if( Modifier.isStatic(field.getModifiers())){
		        	try {
						Object obj  =  field.get(constansClazz);
						if ( obj != null && obj.getClass().equals(retClass) ) {
							list.add((E)obj);
						}
					} catch ( Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}   
		        }
			} 
		}
 		return list;
	}
	
	public static String dec2str_multiply100(BigDecimal number) {
		BigDecimal result = number;
		result = result.multiply(new BigDecimal(100)).setScale(0);
		return result.toString();
	}	
		
}

