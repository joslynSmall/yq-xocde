package com.yq.xcode.common.bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.yq.xcode.common.utils.HColumnUtil;

public final class WfConstants {
	
	public final static String WORK_FLOW_STATUS_CATEGORY = "WST";
	public final static String WORK_FLOW_ACTION_CATEGORY = "WAT";
	public final static String WORK_FLOW_ROLE_CATEGORY = "WFR";
 
	/**
	 * 业务空状态
	 */
	public final static String NULL_ENTITY_STATUS = "WST-NE";
	/**
	 * 业务空状态
	 */
	public final static String WORK_FLOW_STATUS_RETURN = "WST-RT";
	/**
	 * 审批完成状态
	 */
	public final static String WORK_FLOW_STATUS_END = "WST-CLOSED";

	/**
	 * 流程图开始
	 */
	public final static String WORK_FLOW_GRAPH_START = "WNODE-START";
	/**
	 * 流程图结束
	 */
	public final static String WORK_FLOW_GRAPH_END = "WNODE-END";

	/**
	 * 保存操作
	 */
	public static final String WORKFLOW_ACTION_SAVE = "WAT-SAVE";

	/**
	 * 退回操作，会call退回的方法
	 */
	public static final String WORKFLOW_ACTION_RETURN = "WAT-RT";

	/**
	 * 提交审批 初始动作
	 */
	public static final String WORKFLOW_ACTION_SUMBIT = "WAT-SM";
 
 
	/**
	 * 系统角色
	 */
	public final static String WORKFLOW_SYSROLE = "SYS";

 
	/**
	 * 评价URL
	 * pages/couchComment/couchComment参数id：单据ID;sourceCategory:单据类型
	 */
	public final static String COMMENT_URL = "pages/couchComment/couchComment?sourceCategory={entityCategory}&id={entityId}";
	
 	/**
	 * 元素设置在ParseConstants中设， 一定和次名称相同
	 */
	public final static String WORKFLOW_RELATION_CONDITION = "wfRelFun"; //选择流程的条件， workflow.userProperty
	public final static String WORKFLOW_DETAIL_RELATION_CONDITION = "wfdtlRelFun"; //操作条件 
	public final static String WORKFLOW_DETAIL_RELATION_FALSE_MSG = "falseMessage"; //失败提示信息
	public final static String WORKFLOW_DETAIL_EXECUTE_METHOD = "exeMethod"; //失败提示信息
	
	
 

//	public final static WorkFlowEntityCategory getCategoryByCategoryCode(
//			String categoryCode) {
//		for (WorkFlowEntityCategory workFlowEntityCategory : ENTITY_CATEGORY_ARR) {
//			if (workFlowEntityCategory.getCategoryCode().equals(categoryCode)) {
//				return workFlowEntityCategory;
//			}
//		}
//		return null;
//	}

 
 

	public final static List<HColumn> DEFAULT_GRIDCOLS = new ArrayList<HColumn>() {
		{
			add(HColumnUtil.textColumn("entityNumber", "单号", " ", "colEntityNumberRender"));
			add(HColumnUtil.textColumn("pageMap.entityStatusDsp", "状态", " ", ""));
			add(HColumnUtil.textColumn("submitDisplayName", "提单人", " ", ""));
			add(HColumnUtil.textColumn("submitDate", "提单日期", " ",
					"#:hfive.siyuformatTime(submitDate)#"));

		}
	};
 

	/**
	 * 提交审批固定用的 action 值
	 */
	public final static String SENT_TO_APPROVE = "toAp";

	/**
	 * 可以修改的状态
	 */
	public final static HashSet<String> EDIT_STATUS_SET = new HashSet<String>() {
		{
			add("WST-NE");
			add("WST-RT");
			add("WST-RECALL");
		}
	};
	 
	/**
	 * 单句创建方式
	 * 
	 * @author Administrator
	 *
	 */
	public static class WORK_FLOW_SYS_ROLE {
 
		public final static String ANYROLE = "WFR-ANYROLE"; //	任意角色 
		/**
		 * 提单人 （系统）
		 */
		public static final String CREATED_BY = "WFR-createdBy";
		/**
		 * 最后审批人（系统）
		 */
		public static final String LAST_APPROVER = "WFR-lastAp";

		/**
		 * 下个审批人（系统）
		 */
		public static final String NEXT_APPROVER = "WFR-nextAp";
		
		/**
		 *  job 任务
		 */
		public static final String JOB_ROLE = "WFR-job";

	}
	
	public static class WorkFlow_stage {
		 
		/**
		 * 待我审批
		 */
		public final static String myprocess = "myprocess";  
		/**
		 * 审批中
		 */
		public static final String confirmed = "confirmed";
		/**
		 * 审批历史
		 */
		public static final String history = "history";

 

	}
	
	
 

}
