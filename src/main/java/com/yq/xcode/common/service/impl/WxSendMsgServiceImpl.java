package com.yq.xcode.common.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.yq.xcode.aop.event.ApplicationEventPublishers;
import com.yq.xcode.aop.event.SendMessageEvent;
import com.yq.xcode.common.bean.MiniProgram;
import com.yq.xcode.common.bean.SelectItem;
import com.yq.xcode.common.bean.WxMpTemplateData;
import com.yq.xcode.common.bean.WxMpTemplateMessage;
import com.yq.xcode.common.model.LookupCode;
import com.yq.xcode.common.service.LookupCodeService;
import com.yq.xcode.common.service.SqlToModelService;
import com.yq.xcode.common.service.WxSendMsgService;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.YqBeanUtil;

@Service("WxSendMsgService")
public class WxSendMsgServiceImpl extends YqJpaDataAccessObject implements WxSendMsgService {

	private static Log LOG = LogFactory.getLog(WxSendMsgServiceImpl.class);
	@Autowired
	private SqlToModelService sqlToModelService;
	@Autowired
	private LookupCodeService lookupCodeService;
	@Value("${accessTokenUrl}")
	private String accessTokenUrl;
	@Value("${wxUrl}")
	private String wxUrl;
	@Value("${wechat.miniapp.appid}")
	private String weappid;

	/**
	 * 
	 * @param toUserOpenId
	 * @param messageCode
	 *            数据字段， 可以取到对应的 消息设置和templateId
	 * @param data
	 * @return
	 */
	@Override
	public void sendMsg(List<String> toUserOpenIdList, String messageCode, Object data) {
		LookupCode lc = this.lookupCodeService.getLookupCodeByKeyCode(messageCode);
		if (lc == null || CommonUtil.isNull(lc.getSegment1())) {
			return;
		}
		if (CommonUtil.isNull(toUserOpenIdList)) {
			return;
		}
		WxMpTemplateMessage templateMessage = this.genWxMpTemplateMessage(messageCode, lc.getSegment1(), data);
		SendMessageEvent event = new SendMessageEvent( templateMessage, toUserOpenIdList,  wxUrl, accessTokenUrl, this);
		ApplicationEventPublishers.afterTransaction().publishEvent(event);
		
//		WorkFlowMessageEvent event = new WorkFlowMessageEvent(workFlowMessageService, wfd, workFlowEntityIntf, workFlowEntity, entityService,actionLog);
//				//ApplicationEventPublishers.afterTransaction().publishEvent(event);
//		
//		for (String toUserOpenId : toUserOpenIdList) {
//			templateMessage.setToUser(toUserOpenId);
// 			try {
//				RestTemplate restTemplate = RestTemplateFactory.createDefaultRestTemplate(true, null);
//				AccessToken accessToken = restTemplate.getForObject(accessTokenUrl, AccessToken.class);
//				String url1 = new StringBuffer(wxUrl + "/wxmsg/msg/send").toString();
//				HttpHeaders reqHeader = new HttpHeaders();
//				reqHeader.add("Authorization", "Admin " + accessToken.getData().getToken());
//				HttpEntity<WxMpTemplateMessage> requestEntity = new HttpEntity<WxMpTemplateMessage>(templateMessage,
//						reqHeader);
//				String res = restTemplate.postForObject(url1, requestEntity, String.class);
//			} catch (Exception e ) {
//				e.printStackTrace();
//				LOG.info(toUserOpenId+" 可能没有关注公众号！ ");
//				this.createTemplateMessage(templateMessage); // 保存失败的
//			} 
//		}
	}
	@Override
	public void createTemplateMessage(WxMpTemplateMessage templateMessage) {

	}

	private WxMpTemplateMessage genWxMpTemplateMessage(String messageCode, String templateCode, Object data) {
		WxMpTemplateMessage templateMessage = new WxMpTemplateMessage();
		templateMessage.setTemplateId(this.getTemplageId(templateCode));
		Map<String, SelectItem> pMap = this.genDefineMap(messageCode);
		List<WxMpTemplateData> datas = new ArrayList<WxMpTemplateData>();
		templateMessage.setData(datas);
		for (String key : pMap.keySet()) {
			SelectItem si = pMap.get(key);
			Object value = YqBeanUtil.replaceExpression(data, si.getItemName());
			if (CommonUtil.isNotNull(value)) {
				if ("url".equals(key)) { // 固定key
					weappTemplateMessage(templateMessage, value.toString());
				} else {
					WxMpTemplateData tData = new WxMpTemplateData();
					tData.setColor(si.getRemark());
					tData.setName(key);
					tData.setValue(value.toString());
					datas.add(tData);
				}
			}
		}
		return templateMessage;
	}

	/**
	 * 李毅奇
	 * @param templateMessage
	 * @param url
	 */
	private void weappTemplateMessage(WxMpTemplateMessage templateMessage, String url) {
		MiniProgram miniProgram = new MiniProgram();
		miniProgram.setAppid(weappid);
		miniProgram.setPagePath(url);
		miniProgram.setUsePath(false);
		templateMessage.setMiniProgram(miniProgram);
	}

	private Map<String, SelectItem> genDefineMap(String messageCode) {
		Map<String, SelectItem> map = new HashMap<String, SelectItem>();
		String query = "select PROPERTY_NAME itemKey, context itemName, color remark "
				+ " from wx_template_data_define where key_code = '" + messageCode + "' ";
		List<SelectItem> list = this.sqlToModelService.executeNativeQuery(query, null, SelectItem.class);
		if (CommonUtil.isNotNull(list)) {
			for (SelectItem si : list) {
				map.put(si.getItemKey(), si);
			}
		}
		return map;
	}

	private String getTemplageId(String msgCode) {
		return (String) this
				.getSingleValueByNativeQuery("select template_id from wx_template where code = '" + msgCode + "'");
	}

}
