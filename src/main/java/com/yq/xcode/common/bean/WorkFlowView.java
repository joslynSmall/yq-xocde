package com.yq.xcode.common.bean;

import java.util.List;

import com.yq.xcode.common.base.XBaseView;
import com.yq.xcode.common.model.WorkFlow;
import com.yq.xcode.common.model.WorkFlowDetail;
import com.yq.xcode.common.model.WorkFlowGraphDetail;



public class WorkFlowView  extends XBaseView { 
	
	private WorkFlow workFlow;
	private Boolean using;
	private List<WorkFlowDetail> workFlowDetails;
	
	private List<WorkFlowGraphDetail> graphDetails;
	private WorkFlowOpenPageView view;
	
	public WorkFlowOpenPageView getView() {
		return view;
	}
	public void setView(WorkFlowOpenPageView view) {
		this.view = view;
	}
	public List<WorkFlowGraphDetail> getGraphDetails() {
		return graphDetails;
	}
	public void setGraphDetails(List<WorkFlowGraphDetail> graphDetails) {
		this.graphDetails = graphDetails;
	}
	public WorkFlow getWorkFlow() {
		return workFlow;
	}
	public void setWorkFlow(WorkFlow workFlow) {
		this.workFlow = workFlow;
	}
	public Boolean getUsing() {
		if(using == null){
			return true;
		}
		return using;
	}
	public void setUsing(Boolean using) {
		this.using = using;
	}
	public List<WorkFlowDetail> getWorkFlowDetails() {
		return workFlowDetails;
	}
	public void setWorkFlowDetails(List<WorkFlowDetail> workFlowDetails) {
		this.workFlowDetails = workFlowDetails;
	}
	
	
}
