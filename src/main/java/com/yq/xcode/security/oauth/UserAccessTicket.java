package com.yq.xcode.security.oauth;

import com.yq.xcode.common.base.XBaseView;
import com.yq.xcode.common.utils.CommonUtil;

public class UserAccessTicket extends XBaseView {
	
	public final static String SOURCE_PC = "pc";
	
	public final static String SOURCE_APP = "app";
	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 5739179816880149619L;

	/**
	 * 类型 .
	 */
	private String source;
	/**
	 * 用户名.
	 */
	private String username;
	/**
	 * 密码.
	 */
	private String password;

	/**
	 * 随机字符传.
	 */
	private String nonce;
	/**
	 * expireTimestamp.
	 */
	private long expireTimestamp; 
	/**
	 * 获取username 的值.
	 *
	 * @return 返回 username 的值
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * 设置 username .
	 * @param username
	 *            username
	 */
	public void setUsername(final String username) {
		this.username = username;
	}
	/**
	 * 获取password 的值.
	 *
	 * @return 返回 password 的值
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * 设置 password .
	 * @param password
	 *            password
	 */
	public void setPassword(final String password) {
		this.password = password;
	}
	/**
	 * 获取nonce 的值.
	 *
	 * @return 返回 nonce 的值
	 */
	public String getNonce() {
		if(CommonUtil.isNull(nonce)) {
			nonce = "lkzad";
		}
		return nonce;
	}
	/**
	 * 设置 nonce .
	 * @param nonce
	 *            nonce
	 */
	public void setNonce(final String nonce) {
		this.nonce = nonce;
	}
	
	/**
	 * 获取expireTimestamp 的值.
	 *
	 * @return 返回 expireTimestamp 的值
	 */
	public long getExpireTimestamp() {
		return expireTimestamp;
	}
	/**
	 * 设置 expireTimestamp .
	 * @param expireTimestamp
	 *            expireTimestamp
	 */
	public void setExpireTimestamp(final long expireTimestamp) {
		this.expireTimestamp = expireTimestamp;
	}
	public String getSource() {
		if(CommonUtil.isNull(source)) {
			source = SOURCE_PC;
		}
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}

}
