package com.yq.xcode.common.service;

import java.util.List;

import com.yq.xcode.common.bean.WxMpTemplateMessage;

public interface WxSendMsgService {

	public void sendMsg(List<String> toUserOpenIdList, String messageCode, Object data);

	public void createTemplateMessage(WxMpTemplateMessage templateMessage);
 
}
