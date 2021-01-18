package com.yq.xcode.security.bean;

public class ActionMask {

	private String name;

	private int mask;

	public ActionMask() {
	}

	public ActionMask(String name, int mask) {
		setName(name);
		setMask(mask);
	}

	public int getMask() {
		return mask;
	}

	public void setMask(int mask) {
		if (mask <= 0) {
			throw new java.lang.IllegalArgumentException();
		}
		this.mask = mask;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
