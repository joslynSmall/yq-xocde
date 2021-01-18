


 package com.yq.xcode.common.service;

import java.io.File;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.yq.xcode.common.bean.CellProperty;
import com.yq.xcode.common.bean.IdAndVersion;
import com.yq.xcode.common.model.WorkFlowDetailSendMsg;


public interface WorkFlowDetailSendMsgService {
	

	public WorkFlowDetailSendMsg getWorkFlowDetailSendMsgById(Long id); 
	
	/**
	 * 新建初始化模型数据， 页面ID打开和新建就是一样的了， 如果ID为空就初始化一个
	 */
	public WorkFlowDetailSendMsg initWorkFlowDetailSendMsg();
	
	public WorkFlowDetailSendMsg saveWorkFlowDetailSendMsg(WorkFlowDetailSendMsg workFlowDetailSendMsg);

	public void deleteWorkFlowDetailSendMsg(List<IdAndVersion> idvs);
	
	/**
	 * 
	 */
	public void deleteWorkFlowDetailSendMsgById(Long id, Integer version);
	
 
	/**
	 * 导出字段定义
	 */
	public CellProperty[] genWorkFlowDetailSendMsgExportTemplate();
	
	/**
	 * 导入主数据
	 */
	public void importWorkFlowDetailSendMsg(File file) throws Exception  ;
	
	/**
	 * 批量操作， 更近 action 名称
	 * @param idv
	 * @param action
	 */
	public void batchAction(List<IdAndVersion> idv, String action);

	public List<WorkFlowDetailSendMsg> findWorkFlowDetailSendMsgByDtlId(Long workFlowDetailId);

	public List<WorkFlowDetailSendMsg> saveWorkFlowDetailSendMsgs(String workFlowDetailId,
			String resourceName, JSONObject permissionsJa);
	  
}
