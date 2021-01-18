package com.yq.xcode.common.model;

import com.yq.xcode.common.bean.ParseElementUse;
import com.yq.xcode.common.bean.WfConstants;

import java.util.HashMap;


public final class ParseConstants {
    /**
     * T - Time 事件 yyyy-MM-dd -24hh:mi:ms
     * D - Date 日期类型
     * N - Number 数据类型
     * C - Char 字符类型
     * B - Boolean
     * PN - Percentage Number - 百分比
     */
    public final static String DATA_TYPE_TIME = "T";
    public final static String DATA_TYPE_DATE = "D";
    public final static String DATA_TYPE_NUMBER = "N";
    // 数字表达式， 返回的是字符， 但字符本身是一个数据类型的表达式, 表达式的结果作为数字用
    public final static String DATA_TYPE_NUMBER_EXPR = "NEXP";
    public final static String DATA_TYPE_CHAR = "C";
    public final static String DATA_TYPE_BOOLEAN = "B";
    public final static String DATA_TYPE_OBJECT = "O";
    public final static String DATA_TYPE_PNUMBER = "PN";
    public final static String DATA_TYPE_ENUM = "E";

    public final static String ELE_CATEGORY_UNIT = "U"; // 因子
    public final static String ELE_CATEGORY_PARAMETER = "P"; //参数
    public final static String ELE_CATEGORY_FUNCTION = "F"; //函数

    public final static String SYS_TYPE = "SYS";
    public final static String CUST_TYPE = "CUST";

    public final static String WORKFLOWENTITY_ELEMENT = "WFE";
    
    
    public final static String[][] DATA_TYPES = new String[][]{
            {DATA_TYPE_TIME,"时间"},
            {DATA_TYPE_DATE,"日期"},
            {DATA_TYPE_NUMBER,"数字"},
            {DATA_TYPE_NUMBER_EXPR,"数字表达式"},
            {DATA_TYPE_CHAR,"字符"},
            {DATA_TYPE_BOOLEAN,"Boolean"},
            {DATA_TYPE_OBJECT,"对象"},
            {DATA_TYPE_PNUMBER,"百分比"},
            {DATA_TYPE_ENUM,"枚举类型"}
    } ;

    /**
     * HARDCODE 用途，表达式用的用途
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public final static ParseElementUse[] ALL_PARSE_ELEMENT_USE_CATEGORY = new ParseElementUse[]{
//		new ParseElementUse(ELEMENT_CHAIN_MONTH_SUMMARY, "合同收费款项函数", ChainMonthSummary.class, getInitChainMonthSummary(), "",
//			new String[][]{
//				{WfConstants.TEST_WORKFLOW_RELATION_CONDITION,"审批条件",DATA_TYPE_BOOLEAN},
//				{WfConstants.TEST_WORKFLOW_DETAIL_RELATION_CONDITION,"DET审批条件",DATA_TYPE_BOOLEAN},
//				{WfConstants.TEST_WORKFLOW_DETAIL_RELATION_FALSE_MSG,"提示信息",DATA_TYPE_CHAR}
//			}
//		),
//            new ParseElementUse(WORKFLOWENTITY_ELEMENT, "流程元素", HashMap.class, null, "", null),

    };



    public final static ParseElementUse<?> getParseElementUseByCode(String code) {
//        for (ParseElementUse<?> parseElementUse : WfConstants.wfCategoryToPe()) {
//            if (parseElementUse.getCode().equals(code)) {
//                return parseElementUse;
//            }
//        }
//        for (ParseElementUse<?> parseElementUse : ALL_PARSE_ELEMENT_USE_CATEGORY) {
//            if (parseElementUse.getCode().equals(code)) {
//                return parseElementUse;
//            }
//        }
        return null;
    }

}
