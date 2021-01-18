package com.yq.xcode.common.utils;

import com.yq.xcode.common.bean.*;
import com.yq.xcode.common.exception.ValidateException;
import com.yq.xcode.security.entity.JpaBaseModel;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.Column;
import javax.persistence.Query;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.*;

public final class JPAUtils {

    public static boolean isSingleQuery(Query query) {
        List list = query.getResultList();
        if (list == null || list.size() == 0) {
            return false;
        }
        return list.size() == 1 ? true : false;
    }

    public static boolean isExists(Query query) {
        List list = query.getResultList();
        if (list == null || list.size() == 0) {
            return false;
        }
        return list.size() > 0 ? true : false;
    }

    public static String toInSql(Number[] numbers) {
        StringBuffer buffer = new StringBuffer(" ( ");
        for (int i = 0; i < numbers.length; i++) {
            buffer.append(numbers[i]);
            if (i + 1 != numbers.length) {
                buffer.append(",");
            }
        }
        buffer.append(" ) ");
        return buffer.toString();
    }


    public static String toInSql(String[] strs) {
        StringBuffer buffer = new StringBuffer(" ( ");
        for (int i = 0; i < strs.length; i++) {
            buffer.append("'" + strs[i] + "'");
            if (i + 1 != strs.length) {
                buffer.append(",");
            }
        }
        buffer.append(" ) ");
        return buffer.toString();
    }

    /**
     * 鍙栧弬鏁�
     *
     * @param sql
     * @return
     */
    public static QueryModel getSqlAndParmsMapBySql(String sql, Object parmObj) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<Object> pars = new LinkedList<Object>();
        String hasparmSql = sql;
        String querySql = "";
        int fromIndex = -1;
        while (true) {
            fromIndex = hasparmSql.indexOf(":", 1);
            if (fromIndex == -1) {
                querySql = querySql + hasparmSql;
                break;
            } else {
                if (fromIndex != 0) {
                    querySql = querySql + hasparmSql.substring(0, fromIndex);
                    hasparmSql = hasparmSql.substring(fromIndex);
                }
            }
            String par = "";
            for (int i = 1; i < hasparmSql.length(); i++) {
                String ch = hasparmSql.substring(i, i + 1);
                if (isRightParCh(ch)) {
                    par = par + ch;
                } else {
                    try {
                        pars.add(PropertyUtils.getProperty(parmObj, par));
                        hasparmSql = " ? " + hasparmSql.substring(i);
                    } catch (Exception e) {
                        hasparmSql = ":" + par + hasparmSql.substring(i);
                        //e.printStackTrace();
                    }

                    break;
                }
            }
        }
        QueryModel qm = new QueryModel();
        qm.setParameters(pars.toArray());
        //qm.setQuery(replaceQuerySplit(querySql));

        qm.setQuery(querySql);
        return qm;
    }

    /*
    public static String replaceQuerySplit(String query) {
        if (CommonUtil.isNull(query)) {
            return query;
        }
        return query.replace("[", "\"").replace("]", "\"");
    }
    */
    private static String allPars = "abcdefghijklmnopqrstuvwxyz0123456789_";

    private static boolean isRightParCh(String ch) {
        return allPars.contains(ch.toLowerCase());
    }

    /**
     * 濡傚疄鏄┖锛� 杩斿洖鈥溾�濈┖瀛楃涓诧紝鍚﹀垯杩斿洖鎸囧畾鐨勫瓧绗︿覆锛� 涓昏涓烘煡璇㈡潯浠剁殑绌哄�煎垽鏂敤锛�
     *
     * @param value
     * @param String
     * @return
     */
    public static String notNullReturn(Object parameter, String cause) {
        if (CommonUtil.isNull(parameter)) {
            return "";
        } else {
            return cause;
        }
    }


    public static String toPar(Object par) {
        if (par == null) {
            return "";
        } else {
            if (par instanceof String) {
                return (par.toString()).replace("'", "''").replace("\\", "\\\\");
            } else {
                return par.toString();
            }
        }
    }

    public static String genCauseByBetweenDate(String colName, Date fromDate, Date toDate) {
        String cause = "";
        if (fromDate != null) {
            cause = cause + colName + " >= '" + DateUtil.convertDate2String(fromDate) + "'";
        }
        if (toDate != null) {
            if (fromDate == null) {
                cause = cause + colName + " < '" + DateUtil.convertDate2String(DateUtil.getDateAfterDays(toDate, 1)) + "'";
            } else {
                cause = cause + " and " + colName + " < '" + DateUtil.convertDate2String(DateUtil.getDateAfterDays(toDate, 1)) + "'";
            }
        }
        if (CommonUtil.isNull(cause)) {
            return "";
        } else {
            return "(" + cause + ")";
        }

    }

    public static String genEntityCols(Class clazz, String tableAlias) {
        return genEntityCols(clazz, tableAlias, null);
    }

    public static String genEntityCols(Class clazz, String tableAlias, String entityProName) {
        return genEntityColsForMysql(clazz, tableAlias, entityProName);
    }

    /**
     * 鏍规嵁EntityClass 浜х敓Sql 鐨刢ols 瀹氫箟锛� 杩斿洖鍊煎叆 v.id [vendor.id],v.vendor_name [vendor.vendorName]
     * 琛ㄧ殑鍒悕
     *
     * @param clazz
     * @param entityProName
     * @return
     */
    public static String genEntityColsForMysql(Class clazz, String tableAlias, String entityProName) {
        List<PropertyDefine> list = YqBeanUtil.genEntityDefine(clazz);
        if (CommonUtil.isNull(list)) {
            return "";
        } else {
            String cols = "";
            String preProperty = "";
            if (CommonUtil.isNotNull(entityProName)) {
                preProperty = entityProName + ".";
            }
            String ta = "";
            for (PropertyDefine pd : list) {
                if (pd.getColumn().contains(".") && "".equals(ta) && CommonUtil.isNull(tableAlias)) {
                    ta = pd.getColumn().split("\\.")[0];
                }
                String alias = "";
                if (!pd.getColumn().contains(".")) {
                    if (CommonUtil.isNull(alias)) {
                        alias = ta;
                    }
                    alias = tableAlias == null ? (CommonUtil.isNull(ta) ? ta : ta + ".") : tableAlias + ".";
                }
                cols = cols + alias + pd.getColumn() + "  \"" + preProperty + pd.getProperty() + "\",\r\n";
            }
            cols = cols.substring(0, cols.length() - 3);
            return cols;
        }

    }

    /**
     * 鏍规嵁EntityClass 浜х敓Sql 鐨刢ols 瀹氫箟锛� 杩斿洖鍊煎叆 v.id [vendorid],v.vendor_name [vendorvendorName]
     * 琛ㄧ殑鍒悕
     *
     * @param clazz
     * @param entityProName
     * @return
     */
    public static String genEntityColsWithPrefix(Class clazz, String tableAlias, String prefix) {
        List<PropertyDefine> list = YqBeanUtil.genEntityDefine(clazz);
        if (CommonUtil.isNull(list)) {
            return "";
        } else {
            String cols = "";
            String preProperty = "";
            String split = " ";
            if (CommonUtil.isNotNull(prefix)) {
                preProperty = prefix;
            }

            for (PropertyDefine pd : list) {
                cols = cols + tableAlias + "." + pd.getColumn() + "  \"" + preProperty + pd.getProperty() + "\",\r\n";
            }
            cols = cols.substring(0, cols.length() - 3);
            return cols;
        }

    }


    /**
     * 鏍规嵁EntityClass 浜х敓Sql 鐨刢ols 瀹氫箟锛� 杩斿洖鍊煎叆 v.id [vendor.id],v.vendor_name [vendor.vendorName]
     * 琛ㄧ殑鍒悕
     *
     * @param clazz
     * @param entityProName
     * @return
     */
    public static String genEntityColsForSqlServer(Class clazz, String tableAlias, String entityProName) {
        List<PropertyDefine> list = YqBeanUtil.genEntityDefine(clazz);
        if (CommonUtil.isNull(list)) {
            return "";
        } else {
            String cols = "";
            String preProperty = "";
            String split = " ";
            if (CommonUtil.isNotNull(entityProName)) {
                preProperty = entityProName + ".";
            }

            for (PropertyDefine pd : list) {
                cols = cols + tableAlias + "." + pd.getColumn() + "  [" + preProperty + pd.getProperty() + "],\r\n";
            }
            cols = cols.substring(0, cols.length() - 3);
            return cols;
        }

    }

    /**
     * 鏃堕棿姣旇緝鏃�,缁撴潫鏃ユ湡濡傛棤鏃跺垎绉掞紝鍗曟暟鎹簱鍙堟湁鏃跺垎绉掞紝 瑕佺敤姝QL
     *
     * @param dateStr
     * @return
     */
    public static String genEndDate(String dateStr) {
        return "DATE_ADD('" + dateStr + "',INTERVAL 24*60*60-1 SECOND)";
    }

    /**
     * ","鍒嗛殧鐨勫瓧绗︿覆锛� 杞垚瀛楃鐨刬n
     *
     * @param splitStr
     * @return
     */
    public static String toInCharSql(String splitStr) {
        String[] strs = splitStr.split(",");
        StringBuffer buffer = new StringBuffer(" ( ");
        for (int i = 0; i < strs.length; i++) {
            buffer.append("'" + toPar(strs[i]) + "'");
            if (i + 1 != strs.length) {
                buffer.append(",");
            }
        }
        buffer.append(" ) ");
        return buffer.toString();
    }

    /**
     * ","鍒嗛殧鐨勫瓧绗︿覆锛� 杞垚鏁板瓧鐨刬n
     *
     * @param splitStr
     * @return
     */
    public static String toInNumberSql(String splitStr) {
        String[] strs = splitStr.split(",");
        StringBuffer buffer = new StringBuffer(" ( ");
        for (int i = 0; i < strs.length; i++) {
            buffer.append(toPar(strs[i]));
            if (i + 1 != strs.length) {
                buffer.append(",");
            }
        }
        buffer.append(" ) ");
        return buffer.toString();
    }

    public static String genVendorNameQuery(String vendorAlias) {
        if (CommonUtil.isNull(vendorAlias)) {
            return " concat(vendor_name,'(',vendor_code,')') ";
        } else {
            return " concat(" + vendorAlias + ".vendor_name,'('," + vendorAlias + ".vendor_code,')') ";
        }
    }

    /**
     * 姣旇緝闄や簡鏍囧噯淇敼鍒涘缓淇℃伅浠ュ鐨勬墍鏈塮ield鐨勫��,濡傛灉閮界浉绛夎繑鍥瀟rue
     *
     * @param newModel
     * @param oldModel
     * @return
     */
    public static boolean modelDataIsChanged(JpaBaseModel newModel, JpaBaseModel oldModel) {
        List<PropertyDefine> pdList = new ArrayList<PropertyDefine>();
        Class tmpClass = newModel.getClass();
        while (tmpClass != null && !tmpClass.getSimpleName().equals(JpaBaseModel.class.getSimpleName())) {
            Field[] fs = tmpClass.getDeclaredFields(); // 寰楀埌鎵�鏈夌殑fields
            for (Field f : fs) {
                Class fieldClazz = f.getType();
                Column column = f.getAnnotation(Column.class);
                if (column != null) {
                    Object newObjValue = YqBeanUtil.getPropertyValue(newModel, f.getName());
                    Object oldObjValue = YqBeanUtil.getPropertyValue(oldModel, f.getName());
                    if (!valueEquals(newObjValue, oldObjValue)) {
                        return true;
                    }

                }
            }
            tmpClass = tmpClass.getSuperclass();
        }
        return false;
    }

    public static boolean valueEquals(Object newObjValue, Object oldObjValue) {

        if (CommonUtil.isNull(newObjValue) && CommonUtil.isNull(oldObjValue)) {
            return true;
        }
        if (CommonUtil.isNotNull(newObjValue) && newObjValue.equals(oldObjValue)) {
            return true;
        }
        if (CommonUtil.isNotNull(oldObjValue) && oldObjValue.equals(newObjValue)) {
            return true;
        }
        return false;

    }

    public static boolean isAggregateCols(AggregateCol col) {
        if (AggregateCol.CATEGORY_SUM.equals(col.getCategory())
                || AggregateCol.CATEGORY_MAX.equals(col.getCategory())
                || AggregateCol.CATEGORY_MIN.equals(col.getCategory())
                || AggregateCol.CATEGORY_AVERAGE.equals(col.getCategory())) {
            return true;
        }
        return false;
    }

    /**
     * 濡傛灉鏄▼搴忔寚瀹氾紝idKey = 琛ㄥ悕+鈥淿id"
     *
     * @param entityClass
     * @return
     */
    public static String genManualIdKey(Class<? extends JpaBaseModel> entityClass) {
        Table table = entityClass.getAnnotation(Table.class);
        String idKey = table.name().toUpperCase() + "_id";//id鐢ㄨ〃鍚�+_id
        return idKey;
    }

    /**
     * 濡傛灉鏄▼搴忔寚瀹氾紝idKey = 琛ㄥ悕+鈥淿id"
     *
     * @param entityClass
     * @return
     */
    public static String getTableNameByEntity(Class<? extends JpaBaseModel> entityClass) {
        Table table = entityClass.getAnnotation(Table.class);
        return table.name();
    }


    /**
     * 鏍规嵁EntityClass 浜х敓Sql 鐨刢ols 瀹氫箟锛� 杩斿洖鍊煎叆 v.id [vendor.id],v.vendor_name [vendor.vendorName]
     * 琛ㄧ殑鍒悕
     * @param clazz
     * @param entityProName
     * @return
     */
	/*
	public static String genEntityCols(Class clazz, String tableAlias, String entityProName) {
		List<PropertyDefine> list = YqBeanUtil.genEntityDefine(clazz);
		if (CommonUtil.isNull(list)) {
			return "";
		} else {
			String cols = "";
			String preProperty = "";
			String split = " ";
			String preSplit = " ";
			String postSplit = " ";
			if (CommonUtil.isNotNull(entityProName)) {
				preProperty = entityProName+".";
				preSplit = " [";
				postSplit = "] ";
			}
			
			for (PropertyDefine pd : list) {
				cols = cols + tableAlias+".\""+pd.getColumn().toUpperCase()+"\"" +preSplit+preProperty+pd.getProperty()+postSplit+",\r\n";
			}
			cols = cols.substring(0,cols.length()-3);
			return cols;
		}
		
	}
	*/

    /**
     * 鏀寔" " 鍒嗛殧鐨勫or鏉′欢
     * 鏀寔"," 鍒嗛殧鐨勫and鏉′欢
     *
     * @param colName
     * @param par
     * @return
     */
    public static String genSuperLike(String colName, String par) {
        if (CommonUtil.isNull(par)) {
            return " 1=1 ";
        }
        String relation = " or ";
        String likeWord = par.trim();
        String[] sa = null;
        if (par.contains(",") || par.contains("锛�")) {
            likeWord = likeWord.replace("锛�", ",").replace(" ", "");
            relation = " and ";
            sa = likeWord.split(",");
        } else {
            sa = likeWord.split(" ");
        }
        String tmpCause = null;
        for (String s : sa) {
            if (CommonUtil.isNotNull(s)) {
                if (tmpCause == null) {
                    tmpCause = colName + " like " + " concat('%','" + JPAUtils.toPar(s) + "','%' ) ";
                } else {
                    tmpCause = tmpCause + relation + colName + " like " + " concat('%','" + JPAUtils.toPar(s) + "','%' ) ";
                }
            }
        }
        return "(" + tmpCause + ")";
    }


    /**
     * 鏀寔"," 鍒嗛殧鐨勫鎴栨潯浠�
     *
     * @param colName
     * @param par
     * @return
     */
    public static String genInEquals(String colName, String par) {
        if (CommonUtil.isNull(par)) {
            return " 1=1 ";
        }
        String likeWord = par.trim();
        String[] sa = likeWord.split(",");
        String tmpCause = null;
        for (String s : sa) {
            if (tmpCause == null) {
                tmpCause = " '" + JPAUtils.toPar(s) + "' ";
            } else {
                tmpCause = tmpCause + ", '" + JPAUtils.toPar(s) + "' ";
            }
        }
        return colName + " in (" + tmpCause + ")";
    }

    public static String genTableColsForMysql(Class clazz, String tableAlias) {
        List<PropertyDefine> list = YqBeanUtil.genEntityDefine(clazz);
        if (CommonUtil.isNull(list)) {
            return "";
        } else {
            String cols = "";
            String preProperty = "";
            String split = " ";
            String alias = "";
            if (CommonUtil.isNotNull(tableAlias)) {
                alias = tableAlias + ".";
            }

            for (PropertyDefine pd : list) {
                cols = cols + alias + pd.getColumn() + ",\r\n";
            }
            cols = cols.substring(0, cols.length() - 3);
            return cols;
        }

    }

    public static String genBaseCol() {
        return "VERSION,CREATED_BY,CREATED_TIME,LAST_UPDATED_BY,LAST_UPDATED_TIME";
    }

    public static String genBaseValues() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        if (auth != null) {
            username = auth.getName();
        }
        Date currentTime = new Date();
        return "0," + JPAUtils.toSqlValue(username) + "," + JPAUtils.toSqlValue(currentTime) + "," + JPAUtils.toSqlValue(username)
                + "," + JPAUtils.toSqlValue(currentTime);
    }

    public static String genInsertSql(String tableName, JColumn[] colArr) {
        String cols = "";
        String values = "";
        for (JColumn jc : colArr) {
            cols = cols + "," + jc.getInsertColName();
            values = values + ",?";
        }
        cols = cols.substring(1);
        values = values.substring(1);
        String insertQuery = "insert into " + tableName + " (" + cols + ") values (" + values + ")";
        return insertQuery;
    }

    public static String genKeyCols(String tableName, JColumn[] colArr) {
        String cols = "";
        String values = "";
        for (JColumn jc : colArr) {
            if (jc.isKey()) {
                cols = cols + "," + jc.getInsertColName();
            }
        }
        cols = cols.substring(1);
        return cols;
    }

    public static String genNotKeyCols(JColumn[] colArr) {
        String cols = "";
        for (JColumn jc : colArr) {
            if (!jc.isKey()) {
                cols = cols + "," + jc.getInsertColName();
            }
        }
        cols = cols.substring(1);
        return cols;
    }

    public static String genCols(JColumn[] colArr) {
        String cols = "";
        for (JColumn jc : colArr) {
            cols = cols + "," + jc.getSelectColName();
        }
        cols = cols.substring(1);
        return cols;
    }

    /**
     * 非KEY字段
     *
     * @param colArr
     * @return
     */
    public static String genDuplicateUpdateCols(JColumn[] colArr) {
        String cols = "";
        boolean foundPk = false;
        for (JColumn jc : colArr) {
            if (!jc.isKey()) {
                cols = cols + "," + jc.getInsertColName() + "=VALUES(" + jc.getInsertColName() + ")";
            } else {
                foundPk = true;
            }
        }
        if (foundPk && CommonUtil.isNotNull(cols)) {
            cols = cols.substring(1);
            return " on DUPLICATE KEY UPDATE  " + cols;
        }
        return "";
    }

    public static String genPageMapProperty(String preFix, String property) {
        return "pageMap." + CommonUtil.nvl(preFix, "") + property;
    }

    /**
     * 转为SQL 字符值
     *
     * @param par
     * @return
     */
    public static String toSqlValue(Object par) {
        if (par == null) {
            return "null";
        } else {
            if (par instanceof String) {
                return "'" + (par.toString()).replace("'", "''").replace("\\", "\\\\") + "'";
            } else if (par instanceof Date) {
                return "'" + DateUtil.convertDate2String((Date) par, DateUtil.DEFAULT_DATE_TIME_FORMAT) + "'";
            } else {
                return par.toString();
            }
        }
    }

    public static String genSqlConcat(String databaseType, String... sA) {
//		String databaseType = getDatabaseType();
        if ("ORACLE".equals(databaseType)) {
            String cs = null;
            for (String s : sA) {
                if (cs == null) {
                    cs = s;
                } else {
                    cs = cs + "||" + s;
                }
            }
            return cs;
        } else if ("MYSQL".equals(databaseType)) {
            String cs = null;
            for (String s : sA) {
                if (cs == null) {
                    cs = s;
                } else {
                    cs = cs + "," + s;
                }
            }
            return "concat(" + cs + ")";
        } else {
            throw new ValidateException("SqlToModel 不支持 " + databaseType + "数据库类型!");
        }
    }

    /**
     * 根据EntityClass 产生Sql 的cols 定义， 返回值入 v.id [vendor.id],v.vendor_name [vendor.vendorName]
     * 表的别名
     *
     * @param clazz
     * @param entityProName
     * @return
     */
    public static EntifySqlDef genEntityColsForTooLong(Class clazz, String tableAlias, String entityProName) {
        List<PropertyDefine> list = YqBeanUtil.genEntityDefine(clazz);
        if (CommonUtil.isNull(list)) {
            return null;
        } else {
            String[][] colToProperty = new String[list.size()][2];
            String cols = "";
            String dbCols = "";
            String preProperty = "";
            if (CommonUtil.isNotNull(entityProName)) {
                preProperty = entityProName + ".";
            }
            int i = 0;
            for (PropertyDefine pd : list) {
                String aliasColName = tableAlias + "_" + pd.getColumn().toUpperCase();
                if (aliasColName.length() > 25) {
                    aliasColName = aliasColName.substring(0, 25);
                }
                aliasColName = aliasColName + "_" + i + "_";
                String colName = tableAlias + "." + pd.getColumn().toUpperCase();
                String propertyName = preProperty + pd.getProperty();
                cols = cols + colName + " " + aliasColName + ",\r\n";
                colToProperty[i][0] = aliasColName;
                colToProperty[i][1] = propertyName;
                dbCols = dbCols + colName + ",";
                i++;
            }
            cols = cols.substring(0, cols.length() - 3);
            dbCols = dbCols.substring(0, dbCols.length() - 1);
            EntifySqlDef def = new EntifySqlDef();
            def.setColQuery(cols);
            def.setColToProperty(colToProperty);
            def.setDbCols(dbCols);
            return def;
        }

    }

    /**
     * 替换 ？ ， 如果没有数字， 就按默认从 0， 到参数个数计算
     *
     * @param query
     * @return
     */
    public static String replaceParWithSeq(String query) {
        if (query == null || !query.contains("?")) {
            return query;
        }
        int ind = query.indexOf("?");
        if (ind == query.length() - 1) {// 最后一个是问号
            return query + "0";
        }
        String leftCha = query.substring(ind + 1, ind + 2); // 后面的字符
        if (leftCha.matches("[0-9]{1,}")) { // 如果是数字 ， 已用占位符数据， 退出，
            return query;
        }
        String result = "";
        String left = query;
        int i = 0;
        while (ind != -1) {
            result = result + left.substring(0, ind + 1) + i;
            left = left.substring(ind + 1);
            ind = left.indexOf("?");
            i++;
        }
        return result + left;
    }

    public static void main(String[] args) {
        String query = " and (v.currentStatus = ? or ifnull(v.currentStatus,'') = '')  and action=?  ";

        System.out.println(JPAUtils.replaceParWithSeq(query));
    }

}
