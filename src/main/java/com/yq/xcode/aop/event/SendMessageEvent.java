package com.yq.xcode.aop.event;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.yq.xcode.common.bean.WxMpTemplateMessage;
import com.yq.xcode.common.service.WxSendMsgService;

public class SendMessageEvent extends BaseEvent {

	private static final long serialVersionUID = 7213246619064569795L;
	private static Log LOG = LogFactory.getLog(SendMessageEvent.class);
	private WxMpTemplateMessage templateMessage;
	private List<String> toUserOpenIdList;
	private String wxUrl;
	private String accessTokenUrl;
	private WxSendMsgService wxSendMsgService;
	
 
	public SendMessageEvent(Object source) {
		super(source);
	}
	public SendMessageEvent(WxMpTemplateMessage templateMessage,List<String> toUserOpenIdList,String wxUrl,String accessTokenUrl,WxSendMsgService wxSendMsgService) {
		super(templateMessage);
		this.templateMessage = templateMessage;
		this.toUserOpenIdList = toUserOpenIdList;
		this.wxUrl = wxUrl;
		this.accessTokenUrl = accessTokenUrl;
		this.wxSendMsgService = wxSendMsgService;
	 
	}

	@Override
	public void run() {
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
//				this.wxSendMsgService.createTemplateMessage(templateMessage); // 保存失败的
//			} 
//		}
	} 
	
}
