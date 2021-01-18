package com.yq.xcode.common.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yq.xcode.common.service.YqEmailService;
import com.yq.xcode.security.entity.SecPrincipal;




@Service("YqEmailService")
public class YqEmailServiceImpl extends YqJpaDataAccessObject implements YqEmailService{
 
	@Override
	public void sendExceptionEmail(Exception ex) {
		// TODO Auto-generated method stub
		
	}
//
//	@Override
//	public void sendMail(String templateName, List<YqUser> toUsers,
//			List<YqUser> ccUsers, String subject, Map parameters) {
//		if (CommonUtil.isNull(toUsers)) {
//			throw new ValidateException("toUser 不可为空!");
//		}
//		List<String> toList = new ArrayList<String>();
//		for (YqUser u : toUsers) {
//			if (CommonUtil.isNotNull(u.getEmail())) {
//				toList.add(u.getEmail());
//			}
//		}
//		String[] to = (String[])toList.toArray(new String[toList.size()]);
//		String[] cc = null;
//		List<String> ccList = new ArrayList<String>();
//		if (CommonUtil.isNotNull(ccUsers)) {
//			for (YqUser u : ccUsers) {
//				if (CommonUtil.isNotNull(u.getEmail())) {
//					ccList.add(u.getEmail());
//				}
//			}
//		}
//		if (CommonUtil.isNotNull(ccList)) {
//			cc = (String[])ccList.toArray(new String[ccList.size()]);
//		}
//		this.mailSender.sendMail(templateName, to, cc, subject, parameters);
//		
//	}

	@Override
	public void sendMail(String templateName, List<SecPrincipal> toUsers, List<SecPrincipal> ccUsers, String subject,
			Map parameters) {
		// TODO Auto-generated method stub
		
	}

}
