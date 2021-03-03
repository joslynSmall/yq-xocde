package com.yq.xcode.security.oauth;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.RedisUtil;
import com.yq.xcode.security.oauth.yq.YqTokenConstant;
import com.yq.xcode.security.utils.AesEncryptUtils;

/**
 * 用户token<br>
 * .
 *
 * @author wangyancheng <br>
 * @version 1.0.0 2020年4月25日<br>
 *
 * @since JDK 1.8.0
 */
@Service
public class UserAccessTokenServiceImpl implements UserAccessTokenService {

	/**
	 * token 过期时间.
	 */
    private static final long EXPIRE_SECONDS_PC = 1296000L;
	/**
	 * app token 过期时间.设置15天
	 */
    private static final long EXPIRE_SECONDS_APP = 1296000L;

    @Autowired
    private RedisUtil redisUtil;
	/***
	 * 通過用戶名和密碼生成token.
	 * @param ticket
	 * @return UserAccessToken
    * @throws Exception 异常
	 */
	@Override
	public UserAccessToken createToken(final UserAccessTicket ticket) throws Exception {
		if (CommonUtil.isNull(ticket.getUsername())) {
			throw new Exception("用户名不能为空");
		}
		if (CommonUtil.isNull(ticket.getPassword())) {
			throw new Exception("密码不能为空");
		}
		final UserAccessToken token = genAndSaveToken(ticket);
		return token;

	}
	/***
	 * 产生随机字符串.
	 * @param length
	 * @return String
	 */
	private  String getRandomString(final int length) {
	     final String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	     final Random random = new Random();
	     final StringBuffer sb = new StringBuffer();
	     for (int i = 0; i < length; i++) {
	      final int number = random.nextInt(62);
	       sb.append(str.charAt(number));
	     }
	     return sb.toString();
	 }
	/***
	 * 生成和保存token.
	 * @param ticket
	 * @return UserAccessToken
	 * @throws Exception
	 */
    private UserAccessToken genAndSaveToken(final UserAccessTicket ticket) throws Exception {
    	final UserAccessToken userToken  = new UserAccessToken();
    	userToken.setId(ticket.getUsername());
    	userToken.setNonce(ticket.getNonce());
    	final int secondStep = 1000;
    	long expireSeconds = EXPIRE_SECONDS_PC;
    	//设置过期时间
    	if(UserAccessTicket.SOURCE_APP.equals(ticket.getSource())) {
    		expireSeconds = EXPIRE_SECONDS_APP;
    	}
    	final long expireTimestamp = System.currentTimeMillis() + expireSeconds * secondStep;
    	long userTimestamp = 0L;
    	if (CommonUtil.isNotNull(ticket.getExpireTimestamp())) {
    		userTimestamp = System.currentTimeMillis() + ticket.getExpireTimestamp();
    	}
    	if (CommonUtil.isNull(ticket.getExpireTimestamp()) || userTimestamp < expireTimestamp) {
    		userToken.setExpireTimestamp(expireTimestamp);
    	} else {
    		userToken.setExpireTimestamp(userTimestamp);
    	}
    	//目前使用 用户名+内置随机key + 加传过来的随机数AesEncryptUtils加密返回
    	final String token = AesEncryptUtils.aesEncrypt(ticket.getUsername() + getRandomString(10) + ticket.getNonce());
    	userToken.setToken(token);
    	if (CommonUtil.isNull(ticket.getExpireTimestamp()) || userTimestamp < expireTimestamp) {
    		ticket.setExpireTimestamp(expireTimestamp);
    	} else {
    		ticket.setExpireTimestamp(userTimestamp);
    	}
    	redisUtil.put(YqTokenConstant.USER_ACCESS_TOKEN, token, ticket);
    	return userToken;

    }
	/***
	 * 通過用戶名和密碼生成token.
	 * @param ticket
	 * @return UserAccessToken
	 * @throws Exception 异常
	 */
	@Override
	public UserAccessToken reNewToken(final UserAccessTicket ticket) throws Exception {
		return createToken(ticket);
	}
	/***
	 * 通过token获取登录信息.
	 * 主要用于验证token时间
	 * @param token 保存单个对象
	 * @return TravelpoolServiceUser
	 */
	@Override
	public UserAccessTicket decodeTicket(final String token) {
		//目前直接从redis获取，如果涉及到加密解密，在此方法实现
		return 	redisUtil.get(YqTokenConstant.USER_ACCESS_TOKEN, token);
	}

	/***
	 * removeToken.
	 * @param token 移除token对象
	 */
	@Override
	public void removeToken(final String token) {
		redisUtil.remove(YqTokenConstant.USER_ACCESS_TOKEN, token);
	}

}
