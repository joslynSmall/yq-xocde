


 package com.yq.xcode.common.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yq.xcode.common.bean.CellProperty;
import com.yq.xcode.common.bean.IdAndVersion;
import com.yq.xcode.common.model.WorkFlowDetailSendMsg;
import com.yq.xcode.common.service.SqlToModelService;
import com.yq.xcode.common.service.WorkFlowDetailSendMsgService;
import com.yq.xcode.common.service.YqSequenceService;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.ExcelUtil;
import com.yq.xcode.common.utils.JPAUtils;
@Service("WorkFlowDetailSendMsgService")
public class WorkFlowDetailSendMsgServiceImpl   extends YqJpaDataAccessObject  implements WorkFlowDetailSendMsgService {
	@Autowired private SqlToModelService sqlToModelService; 
	@Autowired private YqSequenceService yqSequenceService; 
	@Override
	public WorkFlowDetailSendMsg getWorkFlowDetailSendMsgById(Long id) {
		return this.getById(WorkFlowDetailSendMsg.class, id);
	}
	
	@Override
	public WorkFlowDetailSendMsg initWorkFlowDetailSendMsg(){
		 WorkFlowDetailSendMsg v = new WorkFlowDetailSendMsg();
		 v.setId(0l);
		 return v;
	}

	@Override
	public WorkFlowDetailSendMsg saveWorkFlowDetailSendMsg(WorkFlowDetailSendMsg workFlowDetailSendMsg) {
		this.validateWorkFlowDetailSendMsg (workFlowDetailSendMsg);
  
		return this.save(workFlowDetailSendMsg);
	}
	@Override
	public List<WorkFlowDetailSendMsg> saveWorkFlowDetailSendMsgs(String workFlowDetailId,String resourceName, JSONObject permissionsJa) {
//		this.validateWorkFlowDetailSendMsg (workFlowDetailSendMsg);
		JSONArray r= permissionsJa.getJSONArray("R");
		JSONArray p= permissionsJa.getJSONArray("P");
		List<WorkFlowDetailSendMsg>  saveList = new ArrayList<WorkFlowDetailSendMsg>();
		;
		List<WorkFlowDetailSendMsg> wfdsm = findWorkFlowDetailSendMsgByDtlId(new Long(workFlowDetailId));
		Map<String,WorkFlowDetailSendMsg> wfdsmMap = CommonUtil.ListToMap(wfdsm, "targetCode");
		for(JSONArray current:new JSONArray[]{r,p}){
			for(int j = 0 ; j<current.size() ; j++ ){
				JSONObject permissionsJb = current.getJSONObject(j);
				String appRoleName = permissionsJb.getString("resourceName");
				String appRoleId = permissionsJb.getString("resourceId");
				String msgCode = permissionsJb.getString("msgCode");
				boolean selected = permissionsJb.getBoolean("selected");
				if(CommonUtil.isNotNull(appRoleId) && selected ){
					WorkFlowDetailSendMsg w= wfdsmMap.get(appRoleId);
					if(CommonUtil.isNotNull(w)){
						w.setMsgCode(msgCode);
						saveList.add(w);
						wfdsm.remove(w);
					}else{
						WorkFlowDetailSendMsg newWorkFlowDetailSendMsg = new WorkFlowDetailSendMsg();
						newWorkFlowDetailSendMsg.setWorkFlowDetailId(new Long(workFlowDetailId));
						newWorkFlowDetailSendMsg.setMsgCode(msgCode);
						newWorkFlowDetailSendMsg.setTargetCode(appRoleId);
						newWorkFlowDetailSendMsg.setTargetCategory(appRoleId.startsWith("WFR-")?"R":"P");
						newWorkFlowDetailSendMsg.setVersion(0l);
						saveList.add(newWorkFlowDetailSendMsg);
					}
				}
			}
		}
		
		List<WorkFlowDetailSendMsg> savedList = this.save(saveList);
		this.delete(wfdsm);
		
		return savedList;
		
	}

	
	private void validateWorkFlowDetailSendMsgs (WorkFlowDetailSendMsg workFlowDetailSendMsg ) {
		
	}
	private void validateWorkFlowDetailSendMsg (WorkFlowDetailSendMsg workFlowDetailSendMsg ) {
		
	}
	
	@Override
	public void deleteWorkFlowDetailSendMsg(List<IdAndVersion> idvs) {	
		for (IdAndVersion idv : idvs) {
			
			this.deleteWorkFlowDetailSendMsgById(idv.getId(), idv.getVersion());
		}
	}
	
	@Override
	public void deleteWorkFlowDetailSendMsgById(Long id, Integer version) {	
				this.delete(WorkFlowDetailSendMsg.class, id, version)	;
				
	}
	
 
	/**
	 * 导出字段定义
	 */
	@Override
	public CellProperty[] genWorkFlowDetailSendMsgExportTemplate() {
		CellProperty[] cellPropertis = new CellProperty[]{				

				 new CellProperty("目标代码","A","targetCode",false,null),
				 new CellProperty("描述","B","description",false,null)				
		};
		return cellPropertis;
	}
	
	/**
	 * 导入主数据
	 */
	@Override
	public void importWorkFlowDetailSendMsg(File file) throws Exception  {
 		CellProperty[] cellPropertis = this.genWorkFlowDetailSendMsgExportTemplate();
 		List<WorkFlowDetailSendMsg> impList = ExcelUtil.loadExcelDataForJ(file, WorkFlowDetailSendMsg.class, cellPropertis, null,1);
 		if (impList != null) {
 			for (WorkFlowDetailSendMsg v : impList ) {
 				this.saveWorkFlowDetailSendMsg(v);
 			}
 		}
	}
 
	@Override
	public void batchAction(List<IdAndVersion> idv, String action) {
//		if ("forbidden".equals(action)) {
//			this.forbidden(idv,true);
//		} else if ("unforbidden".equals(action)) {
//			this.forbidden(idv,false);
//		}else {
//			throw new ValidateException("不存在的action: "+action);
//		}
	}

	@Override
	public List<WorkFlowDetailSendMsg> findWorkFlowDetailSendMsgByDtlId(
			Long workFlowDetailId) {
		String query = "select "+JPAUtils.genEntityCols(WorkFlowDetailSendMsg.class, "a", null)
		        + " , ifnull(lc.lookup_name,u.display_name) targetName "
		        + " , m.LOOKUP_NAME msgCodeDsp "
				+ "  from yq_work_flow_DETAIL_SEND_MSG a "
				+ " left join yq_lookup_code lc on a.target_code = lc.key_code and a.TARGET_CATEGORY ='R' "
				+ " left join yq_lookup_code m on a.msg_code = m.key_code  "
				+ " left join sec_principal u on a.target_code = u.name and a.TARGET_CATEGORY ='P' "
//				+ " left join wx_template wt on a.msg_code = wt.code  "
				+ " where a.work_flow_detail_id = "+workFlowDetailId;
		return this.sqlToModelService.executeNativeQuery(query, null, WorkFlowDetailSendMsg.class);
	}
	
	
			
}

