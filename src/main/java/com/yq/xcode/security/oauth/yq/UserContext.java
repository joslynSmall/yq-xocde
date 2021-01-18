package com.yq.xcode.security.oauth.yq;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;

import com.yq.xcode.common.base.XBaseView;

public class UserContext extends XBaseView{

	private static final long serialVersionUID = 164385199647633549L;

	private Map<String, Object> attributes = new HashMap<String, Object>();

	private boolean updated;

	private Authentication authentication;

	private transient boolean isNew;
	//空的构造
	public UserContext() {
		super();
	}
	public Object getAttribute(String key) {
		return attributes.get(key);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> T getAttribute(String key, Class T) {
		return (T) getAttribute(key);
	}

	public void setAttribute(String key, Object value) {
		attributes.put(key, value);
	}

	public void removeAttribute(String key) {
		attributes.remove(key);
	}

	public boolean isUpdated() {
		return updated;
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
	}

	public void clear() {
		attributes.clear();
		this.updated = true;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	public Authentication getAuthentication() {
		return authentication;
	}

	public void setAuthentication(Authentication authentication) {
		this.authentication = authentication;
	}
}
