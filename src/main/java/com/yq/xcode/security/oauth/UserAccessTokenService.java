package com.yq.xcode.security.oauth;

public interface UserAccessTokenService {

	/***
	 * 通過用戶名和密碼生成token.
	 * @param ticket 
	 * @return UserAccessTokenVO
    * @throws Exception 异常
	 */
	public UserAccessToken createToken(UserAccessTicket ticket) throws Exception;

	/***
	 * 通過用戶名和密碼生成token.
	 * @param ticket 
	 * @return UserAccessTokenVO
    * @throws Exception 异常
	 */
	public UserAccessToken reNewToken(UserAccessTicket ticket) throws Exception;
	/***
	 * 通过token获取登录信息.
	 * 主要用于验证token时间
	 * @param token 保存单个对象
	 * @return TravelpoolServiceUser
	 */
	public UserAccessTicket decodeTicket(String token);
	
	/***
	 * removeToken.
	 * @param token 移除token对象
	 */
	public void removeToken(String token);
}
