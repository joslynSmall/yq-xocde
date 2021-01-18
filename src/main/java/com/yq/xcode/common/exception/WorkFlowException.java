package com.yq.xcode.common.exception;

public class WorkFlowException extends BaseDefineException {

	private static final long serialVersionUID = -7067506919151673199L;
	public static final String NO_ACTION_REASON = "noActionReason";
	public static final String CONFIRM = "confirm";
	public static final String NOT_FOUND_WORK_FLOW = "工作流设定有误，不能执行此操作";
	public static final String MORE_THEN_ONE_WORK_FLOW = "设定有误，返回多个状态";
	private String actionReasonTitle;
	private String actionInfo;

	public String getActionInfo() {
		return actionInfo;
	}

	public void setActionInfo(String actionInfo) {
		this.actionInfo = actionInfo;
	}

	public String getActionReasonTitle() {
		return actionReasonTitle;
	}

	public void setActionReasonTitle(String actionReasonTitle) {
		this.actionReasonTitle = actionReasonTitle;
	}

	public WorkFlowException() {
		super();
	}
	
	public WorkFlowException(String msg) {
		super(msg);
	}
	
	public WorkFlowException(Throwable cause) {
		super(cause);
	}
	
	public WorkFlowException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
