package com.yq.xcode.common.bean;

import java.util.Date;

public class WorkFlowWeappView {

	/**
	 * 第二行浅灰文本
	 * 审批人
	 * value1
	 */
	private String actionByName;
	
	/**
	 * 第一行粗体文本
	 * 	节点名称
	 * Value2
	 */
	private String nodeName;
	
	/**
	 * 操作名称
	 * value3
	 */
	private String actionName;
	
	/**
	 * 右侧时间
	 * 审批小孩时间
	 * Value4
	 */
	private String actionTime;
	
	/**
	 * 节点是否完成</br>
	 * 影响小程序端圆点颜色
	 */
	private boolean done;
	
	
	private Date actionDate;
	
	private String actionReason;
	
	private String color;
	
 
	public String getActionReason() {
		return actionReason;
	}

	public void setActionReason(String actionReason) {
		this.actionReason = actionReason;
	}
 

	public String getActionByName() {
		return actionByName;
	}

	public void setActionByName(String actionByName) {
		this.actionByName = actionByName;
	}

 
 
	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

 

	public String getActionTime() {
		return actionTime;
	}

	public void setActionTime(String actionTime) {
		this.actionTime = actionTime;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public Date getActionDate() {
		return actionDate;
	}

	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
 	
}
