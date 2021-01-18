package com.yq.xcode.common.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.security.entity.JpaBaseModel;

@SuppressWarnings("serial")
@Entity
@Table(name="yq_work_flow_GRAPH_DETAIL")
public class WorkFlowGraphDetail extends JpaBaseModel{
	@TableGenerator(name = "idGen", table = "ID_GENERATOR", pkColumnName = "ID_KEY", valueColumnName = "ID_VALUE", pkColumnValue = "WORK_FLOW_GRAPH_DETAIL_ID", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idGen")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "WORK_FLOW_ID")
	private Long workFlowId;
	
	@Column(name = "LINE_NUMBER")
	private int lineNumber;
	
	@Column(name = "GRAPH_NODE")
	private String graphNode;
	@Transient
	private String graphNodeName;
	/**
	 * 业务状态，多个用“，”分隔
	 */
	@Column(name = "SOURCE_STATUSES")
	private String sourceStatuses;
	
	@Column(name = "READ_ONLY")
	private boolean readOnly;
	/**
	 * 和当前节点的只读属性相反的业务状态，必须包含在业务状态‘SOURCE_STATUSES’中，
	 */
	@Column(name = "NOT_READ_ONLY_SOURCE_STATUSES")
	private String notReadOnlySourceStatus;
	
	@Transient
	private WorkFlowEntityActionLog actionLog;
	
	@Transient
	private boolean lastNode = false;
	
	@Transient
	private List<String> sourceStatusesList;
	@Transient
	private List<WorkFlowDetail> workFlowDetails;
	
	public List<WorkFlowDetail> getWorkFlowDetails() {
		return workFlowDetails;
	}
	public void setWorkFlowDetails(List<WorkFlowDetail> workFlowDetails) {
		this.workFlowDetails = workFlowDetails;
	}
	public List<String> getSourceStatusesList() {
		if(CommonUtil.isNull(sourceStatusesList)&&CommonUtil.isNotNull(sourceStatuses)){
			String[]  a = sourceStatuses.split(",");
			sourceStatusesList = Arrays.asList(a);
		}
		return sourceStatusesList;
	}
	public void setSourceStatusesList(List<String> sourceStatusesList) {
		this.sourceStatusesList = sourceStatusesList;
	}

	@Transient
	private Map<String,Object> pageMap = new HashMap<String,Object>();
	public Map<String, Object> getPageMap() {
		return pageMap;
	}
	public void setPageMap(Map<String, Object> pageMap) {
		this.pageMap = pageMap;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getGraphNode() {
		return graphNode;
	}

	public void setGraphNode(String graphNode) {
		this.graphNode = graphNode;
	}

	public String getSourceStatuses() {
		return sourceStatuses;
	}

	public void setSourceStatuses(String sourceStatuses) {
		this.sourceStatuses = sourceStatuses;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public String getNotReadOnlySourceStatus() {
		return notReadOnlySourceStatus;
	}

	public void setNotReadOnlySourceStatus(String notReadOnlySourceStatus) {
		this.notReadOnlySourceStatus = notReadOnlySourceStatus;
	}

	public String getGraphNodeName() {
		return graphNodeName;
	}

	public void setGraphNodeName(String graphNodeName) {
		this.graphNodeName = graphNodeName;
	}

	public Long getWorkFlowId() {
		return workFlowId;
	}

	public void setWorkFlowId(Long workFlowId) {
		this.workFlowId = workFlowId;
	}

	public WorkFlowEntityActionLog getActionLog() {
		return actionLog;
	}

	public void setActionLog(WorkFlowEntityActionLog actionLog) {
		this.actionLog = actionLog;
	}

	public boolean isLastNode() {
		return lastNode;
	}

	public void setLastNode(boolean lastNode) {
		this.lastNode = lastNode;
	}
	
}
