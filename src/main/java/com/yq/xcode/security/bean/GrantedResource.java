package com.yq.xcode.security.bean;

import java.util.Set;


import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class GrantedResource implements GrantedAuthority{

	private static final long serialVersionUID = 180356040228178195L;

	private String resourceName;
	
	private Set<String> idSet;
	
	private int mask;
	
	public GrantedResource() {

	}
	public GrantedResource(String resourceName,Set<String> idSet,int mask) {
		this.resourceName = resourceName;
		this.idSet = idSet;
		this.mask = mask;
	}
	
	@Override
	public String getAuthority() {
		return resourceName;
	}

	public Set<String> getIdSet() {
		return idSet;
	}
	
	public boolean implies(int mask) {
		return ((this.mask&mask) == mask);
	}

	public int getMask() {
		return mask;
	}

	@Override
	public String toString() {
		return new StringBuffer("GrantedResource[Name=").append(resourceName).append(", Mask=").append(mask).append(", ResourceID=").append(idSet).append("]").toString();
	}
	
	

	
	
}
