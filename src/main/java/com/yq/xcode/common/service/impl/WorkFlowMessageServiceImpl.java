package com.yq.xcode.common.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.yq.xcode.common.bean.ResourceView;
import com.yq.xcode.common.bean.SelectItem;
import com.yq.xcode.common.bean.WfConstants;
import com.yq.xcode.common.bean.WorkFlowEntityCategory;
import com.yq.xcode.common.bean.WorkFlowEntityIntf;
import com.yq.xcode.common.exception.ValidateException;
import com.yq.xcode.common.model.LookupCode;
import com.yq.xcode.common.model.WorkFlowDetail;
import com.yq.xcode.common.model.WorkFlowDetailSendMsg;
import com.yq.xcode.common.model.WorkFlowEntity;
import com.yq.xcode.common.model.WorkFlowEntityActionLog;
import com.yq.xcode.common.service.LookupCodeService;
import com.yq.xcode.common.service.SqlToModelService;
import com.yq.xcode.common.service.WorkFlowActionService;
import com.yq.xcode.common.service.WorkFlowDetailSendMsgService;
import com.yq.xcode.common.service.WorkFlowEntityQueryService;
import com.yq.xcode.common.service.WorkFlowEntityService;
import com.yq.xcode.common.service.WorkFlowMessageService;
import com.yq.xcode.common.service.WorkFlowService;
import com.yq.xcode.common.service.WxSendMsgService;
import com.yq.xcode.common.springdata.HWorkFlowEntityPageCriteria;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.ParseUtil;
import com.yq.xcode.common.utils.WorkFlowUtil;
import com.yq.xcode.common.utils.YqBeanUtil;
import com.yq.xcode.security.entity.SecPrincipal;
import com.yq.xcode.security.resourceproviders.ResourceConstants;
import com.yq.xcode.security.service.PrincipalService; 

@Service("WorkFlowMessageService")
public class WorkFlowMessageServiceImpl extends YqJpaDataAccessObject implements
		WorkFlowMessageService {
	@Autowired
	private SqlToModelService sqlToModelService;
	@Autowired
	private WorkFlowDetailSendMsgService workFlowDetailSendMsgService;
	@Autowired
	private LookupCodeService lookupCodeService;
	@Autowired
	private WorkFlowActionService workFlowActionService;
	@Autowired
	private WorkFlowService workFlowService; 
	@Autowired
	private WxSendMsgService wxSendMsgService;
	@Autowired
	private PrincipalService principalService;
	@Autowired
	private WorkFlowEntityQueryService  workFlowEntityQueryService;
	
	
	@Override
	public void sendWorkFlowMessage(WorkFlowDetail wfd,
			WorkFlowEntity workFlowEntity,WorkFlowEntityIntf workFlowEntityIntf,
			WorkFlowEntityService entityServicey,WorkFlowEntityActionLog  actionLog) { 
		List<WorkFlowDetailSendMsg> sendList = this.workFlowDetailSendMsgService.findWorkFlowDetailSendMsgByDtlId(wfd.getId());
		/**
		 * 对象丢掉了显示字段， 补上
		 */
// 		workFlowEntity.getPageMap().put("actionDate", actionLog.getActionDate());
//		workFlowEntity.getPageMap().put("actionBy", actionLog.getActionByName());  
		LookupCode lc = this.lookupCodeService.getLookupCodeByKeyCode(actionLog.getAction());
		String commentUrl = YqBeanUtil.replaceExpression(workFlowEntity, WfConstants.COMMENT_URL);
//		workFlowEntity.getPageMap().put("commentUrl", commentUrl);
//		if (lc != null) {
//			workFlowEntity.getPageMap().put("actionName", lc.getLookupName());
//		}
		if (sendList == null) {
			sendList = new ArrayList<WorkFlowDetailSendMsg>();
		}
		lc = this.lookupCodeService.getLookupCodeByKeyCode(wfd.getAction()); 
		if (lc != null) {
			if (CommonUtil.isNotNull(lc.getSegment1())) { //提醒提单人
				WorkFlowDetailSendMsg msg = new WorkFlowDetailSendMsg();
				msg.setMsgCode(lc.getSegment1());
				msg.setTargetCode(WfConstants.WORK_FLOW_SYS_ROLE. CREATED_BY);
				sendList.add(msg);
			}
			if (CommonUtil.isNotNull(lc.getSegment2())) { //通知下一审批人
				WorkFlowDetailSendMsg msg = new WorkFlowDetailSendMsg();
				msg.setMsgCode(lc.getSegment2());
				msg.setTargetCode(WfConstants.WORK_FLOW_SYS_ROLE.NEXT_APPROVER);
				sendList.add(msg);
			}
		}
		if (CommonUtil.isNotNull(sendList)) {
			Map<String,List<SecPrincipal>> msgToUserListMap = new HashMap<String,List<SecPrincipal>>();
			Set<String> userCodeSet = new HashSet<String>();
			for (WorkFlowDetailSendMsg sendMsg : sendList) {
				List<SecPrincipal> msgUserList = this.getUserList(sendMsg,  workFlowEntity,workFlowEntityIntf,
			  entityServicey, actionLog.getPreStatus());
				if (CommonUtil.isNotNull(msgUserList)) { // 顾虑掉重复的用户， 一个用户不发两个消息
					for (int i=msgUserList.size()-1; i>=0; i--) {
						SecPrincipal u = msgUserList.get(i);
						if (userCodeSet.contains(u.getName())) {
							msgUserList.remove(i);
						} else {
							userCodeSet.add(u.getName());
						}
					}
				}
 				if (CommonUtil.isNotNull(msgUserList)) { 
					List<SecPrincipal> userList = msgToUserListMap.get(sendMsg.getMsgCode());
					if (userList == null) {
						msgToUserListMap.put(sendMsg.getMsgCode(), msgUserList);
					} else {
						userList.addAll(msgUserList);
					}
				}
			}
			if (CommonUtil.isNotNull(msgToUserListMap)) {
				this.executeSendMsg(msgToUserListMap,  workFlowEntity,
			  entityServicey);
			}
		}
 	}
	/**
	 * 已取到对应的人， 发送保存信息
	 * @param msgToUserListMap
	 * @param workFlowEntity
	 * @param entityServicey
	 */
	private void executeSendMsg(Map<String, List<SecPrincipal>> msgToUserListMap,
			WorkFlowEntity workFlowEntity,
			WorkFlowEntityService entityServicey) {
		WorkFlowEntityCategory c = this.workFlowEntityQueryService.getCategoryByCategoryCode(workFlowEntity.getEntityCategory());
		if(CommonUtil.isNotNull(c)){
			workFlowEntity.setWxUrl(c.getWxUrl()+"?id="+workFlowEntity.getEntityId()); 
		} 
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("e",workFlowEntity);
		data.put("pt", new ParseUtil());
		if (CommonUtil.isNotNull(msgToUserListMap)) {
			for (String key : msgToUserListMap.keySet() ) {
				List<SecPrincipal> uList = msgToUserListMap.get(key);
				if (CommonUtil.isNotNull(uList)) {
					List<String> openIdList = new ArrayList<String>();
					for (SecPrincipal u : uList ) {
						if (CommonUtil.isNull(u.getWxOpenId())) {
							System.out.println(u.getDisplayName()+" 没有绑定微信号， 发不了消息！");
						} else {
							System.out.println(u.getDisplayName()+" 有绑定微信公众号， 会发送消息！");
							openIdList.add(u.getWxOpenId());
						}
					}
					if (CommonUtil.isNotNull(openIdList)) {
						this.wxSendMsgService.sendMsg(openIdList, key, data);
					} 
				}
			}
		} 
	}
	
	
	
	
	/**
	 * 取角色对应人的列表
	 * @param sendMsg
	 * @param workFlowEntity
	 * @param entityServicey
	 * @return
	 */
	private List<SecPrincipal> getUserList(WorkFlowDetailSendMsg sendMsg,
			WorkFlowEntity workFlowEntity,WorkFlowEntityIntf workFlowEntityIntf,
			WorkFlowEntityService entityServicey,String preStatus) {
		Set<String> entityRoleSet = this.getEntityRole();
		List<SecPrincipal> userList = new ArrayList<SecPrincipal>();
		if ("P".equals(sendMsg.getTargetCategory())) { // 指定人的
			userList.add(this.principalService.getPrincipalByUsername( sendMsg.getTargetCode()));
			return userList;
		}
		//其他就为角色
		if (WfConstants.WORK_FLOW_SYS_ROLE.CREATED_BY.equals(sendMsg.getTargetCode())) {
			userList.add(this.principalService.getPrincipalByUsername(workFlowEntity.getCreateUser()));
			return userList;
		}
		//最后审批人（系统）
		if (WfConstants.WORK_FLOW_SYS_ROLE.LAST_APPROVER.equals(sendMsg.getTargetCode())) {
			List<WorkFlowEntityActionLog> list = this.workFlowActionService.findActionLog(workFlowEntity.getEntityId(), workFlowEntity.getEntityCategory(), preStatus);
			if (!CommonUtil.isNull(list)) {
				userList.add(this.principalService.getPrincipalByUsername(list.get(list.size()-1).getActionByCode()));
			} 
			return userList;
		}
		
		// 模型角色
		if (entityRoleSet.contains(sendMsg.getTargetCode())) {
			String userCode = WorkFlowUtil.getUserByRole(workFlowEntity.getSpecRoleUser(), sendMsg.getTargetCode());
			if (CommonUtil.isNotNull(userCode)) {
				userList.add(this.principalService.getPrincipalByUsername(userCode));
			}
		}
		

				
		List<ResourceView> allResList = new ArrayList<ResourceView>();
		ResourceView roleRes = new ResourceView();
		roleRes.setResourceName(ResourceConstants.WORK_FLOW_ROLE);
		List<String> vList = new ArrayList<String>();
		roleRes.setResourceValueList(vList);
		allResList.add(roleRes);
		//下一审批人, 取角色
		if (WfConstants.WORK_FLOW_SYS_ROLE.NEXT_APPROVER.equals(sendMsg.getTargetCode())) {
			List<WorkFlowDetail> wfDtlList = this.workFlowService.findNextWorkFlowList(workFlowEntity.getWorkFlowId(), workFlowEntityIntf, null, workFlowEntity.getEntityStatus());
			if (CommonUtil.isNull(wfDtlList)) {
				return null;
			}
 			for (WorkFlowDetail wfd : wfDtlList) {
 				if (entityRoleSet.contains(wfd.getRole())) {
 					String userCode = WorkFlowUtil.getUserByRole(workFlowEntity.getSpecRoleUser(), wfd.getRole());
 					if (CommonUtil.isNotNull(userCode)) {
 						userList.add(this.principalService.getPrincipalByUsername(userCode));
 					}
 				} else { 
 					vList.add(wfd.getRole());
 				}
				
			}
 		} else { // 普通角色， 加进去就可以了
 			vList.add(sendMsg.getTargetCode());
 		}
		
		/**
		 * 其他资源
		 */
//		List<ResourceView> otherResList = entityServicey.getResourceViewList(workFlowEntityIntf);
//		if (CommonUtil.isNotNull(otherResList)) {
//			allResList.addAll(otherResList);
//		}
		
		List<SecPrincipal> list = this.principalService.findUserListByResource(allResList);
		List<SecPrincipal> retList = new ArrayList<SecPrincipal>();
		if (CommonUtil.isNotNull(list)) {
			WorkFlowEntityCategory cate = this.workFlowService.getCategoryByCategoryCode(workFlowEntity.getEntityCategory());
			HWorkFlowEntityPageCriteria criteria = null;
			try {
				criteria = (HWorkFlowEntityPageCriteria) cate.getCriteriaClass().newInstance();
			} catch (Exception e) { 
				e.printStackTrace();
				throw new ValidateException("");
			}  
			for (SecPrincipal p : list ) {
				criteria.setFindDataAs(p.getName());
				criteria.setOnlyCount(true);
				Page page = entityServicey.findEntityPage(criteria);
				if (page.getContent().size() > 0 ) {
					retList.add(p);
				}
			}
		}
		return retList;
	}
	
	
	/**
	 * entity role 
	 * @return
	 */
	private Set<String> getEntityRole() {
		Set<String> mSet = new HashSet<String> ();
		StringBuilder sb = new StringBuilder("  SELECT lc.KEY_CODE itemKey, lc.LOOKUP_NAME itemName, lc.DESCRIPTION remark ");
		sb.append(" FROM yq_lookup_code lc ");
		//sb.append(" where  lc.PARENT_KEY_CODE = '"+ JPAUtils.toPar(parentKey) +"'");
		sb.append(" where  lc.category_Code ='WFR' and lc.segment1 = 'M'");
		List<SelectItem> list = this.sqlToModelService.executeNativeQuery(sb.toString(), null, SelectItem.class);
		if (list != null) {
			for (SelectItem si : list) {
				mSet.add(si.getItemKey());
			}
		}
		return mSet;
	}

 
}
