package com.yq.xcode.common.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yq.xcode.common.base.XBaseView;
import com.yq.xcode.common.model.LookupCode;
import com.yq.xcode.common.model.WorkFlow;
import com.yq.xcode.common.model.WorkFlowEntityActionLog;
import com.yq.xcode.common.model.WorkFlowGraphDetail;
import com.yq.xcode.common.utils.CommonUtil;

@JsonIgnoreProperties(ignoreUnknown=true)
public class WorkFlowOpenPageView  extends XBaseView { 
	
	private boolean readOnly = false;
	private List<LookupCode> enableActions;
	private List<WorkFlowEntityActionLog> actionLogList;
	private List<WorkFlowGraphDetail> graphDetail;
	private WorkFlow workFlow;
	
	private String openUrl;
	@SuppressWarnings("unused")
	private String workFlowGraphHtml;
	@SuppressWarnings("unused")
	private String actionHtml;
	@JsonIgnore
    public WorkFlowEntityActionLog getReason() {
    	if (!CommonUtil.isNull(actionLogList)) {
    		return actionLogList.get(actionLogList.size()-1);
    	}
    	return null;
    }

    /**
     * 最后操作的名称
     * @return
     */
    @JsonIgnore
	public String getLastActionName() {
		if (!CommonUtil.isNull(actionLogList)) {
    		return actionLogList.get(actionLogList.size()-1).getActionName();
    	}
		return null;
	}

	public WorkFlow getWorkFlow() {
		return workFlow;
	}
	public void setWorkFlow(WorkFlow workFlow) {
		this.workFlow = workFlow;
	}
	public List<WorkFlowEntityActionLog> getActionLogList() {
		return actionLogList;
	}
	public void setActionLogList(List<WorkFlowEntityActionLog> actionLogList) {
		this.actionLogList = actionLogList;
	}
	public List<WorkFlowGraphDetail> getGraphDetail() {
		return graphDetail;
	}
	public void setGraphDetail(List<WorkFlowGraphDetail> graphDetail) {
		this.graphDetail = graphDetail;
	}
	public boolean isReadOnly() {
		return readOnly;
	}
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	public List<LookupCode> getEnableActions() {
		return enableActions;
	}
	public void setEnableActions(List<LookupCode> enableActions) {
		this.enableActions = enableActions;
	}
	
//	public String getActionHtml() {
//		String buttons=""; 
//		if(CommonUtil.isNotNull(enableActions)){
//			for(LookupCode slc : enableActions){
//				HtmlInputButton button=new HtmlInputButton();
//				//System.out.println(button.getClass().getClassLoader().getResource("com/siyu/scp/html/HtmlInputButton.class"));
//				button.setValue(slc.getLookupName());
//				button.setKeyCode(slc.getKeyCode());
//				button.setReadonly(this.isReadOnly());
//				button.setReasonMandatory(slc.getReasonMandatory());
//				if(!CommonUtil.isNull(slc.getActionPreMsg())){
//					button.setActionPreMsg(slc.getActionPreMsg());
//				}
//				buttons+=button.genHtml();
//			}
//		}	
//		return buttons;
//	}
	
	@JsonIgnore
	public String getShowReasonHtml() {
		return null;
	}
	
	/**
	 * 业务状态流程图
	 * @return
	 */
	
	public String getWorkFlowGraphHtml() {
 		return null;
	}
	
	@SuppressWarnings("unused")
	private String workFlowGraphDetailHtml;
	/**
	 * 流程图
	 * @return
	 */
	public String getWorkFlowGraphDetailHtml() {
		 
		return null;
	}
	
	@JsonIgnore
	public String getActionLogHtml() {
		return null; 
	}

	public String getOpenUrl() {
		return openUrl;
	}

	public void setOpenUrl(String openUrl) {
		this.openUrl = openUrl;
	}
	
}
