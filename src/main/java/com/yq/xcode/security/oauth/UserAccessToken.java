package com.yq.xcode.security.oauth;

import java.io.Serializable;

public class UserAccessToken implements Serializable {
	
	/**
	 * serialVersionUID.
	 */	
	private static final long serialVersionUID = 5057567449920607076L;
	/**
	 * 用户id.
	 */
	private String id;
	/**
	 * token.
	 */
	private String token; 
	/**
	 * expireTimestamp.
	 */
	private long expireTimestamp; 
	/**
	 * nonce.
	 */
	private String nonce;

	/**
	 * 获取id 的值.
	 *
	 * @return 返回 id 的值
	 */
	public String getId() {
		return id;
	}
	/**
	 * 设置 id.
	 * @param pid
	 *            pid
	 */
	public void setId(final String pid) {
		this.id = pid;
	}
	/**
	 * 获取token 的值.
	 *
	 * @return 返回 token 的值
	 */
	public String getToken() {
		return token;
	}
	/**
	 * 设置 token .
	 * @param ptoken
	 *            ptoken
	 */
	public void setToken(final String ptoken) {
		this.token = ptoken;
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
	 * @param pexpireTimestamp
	 *            pexpireTimestamp
	 */
	public void setExpireTimestamp(final long pexpireTimestamp) {
		this.expireTimestamp = pexpireTimestamp;
	}
	/**
	 * 获取nonce 的值.
	 *
	 * @return 返回 nonce 的值
	 */
	public String getNonce() {
		return nonce;
	}
	/**
	 * 设置 nonce.
	 * @param pnonce  
	 *          pnonce
	 */
	public void setNonce(final String pnonce) {
		this.nonce = pnonce;
	} 

}
