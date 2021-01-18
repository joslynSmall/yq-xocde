package com.yq.xcode.security.bean;

import java.security.Permission;

public class MaskPermission extends Permission {

	private static final long serialVersionUID = 6628035374055304499L;

	public final static String ALL_ACTIONS = "all";

	private int mask;
	
	private String permissonActions;

	public MaskPermission(String name) {
		super(name);
	}

	public MaskPermission(String name, int mask) {
		super(name);
		this.mask = mask;
	}

	public MaskPermission(String name, int mask, String permissonActions) {
		super(name);
		this.mask = mask;
		this.permissonActions =  permissonActions;
	}


	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof MaskPermission)) {
			return false;
		}
		MaskPermission perm = (MaskPermission) obj;
		return perm.getName().equals(getName()) && getMask() == perm.getMask();

	}

	@Override
	public String getActions() {
		return mask + "";
	}

	public int getMask() {
		return mask;
	}

	@Override
	public int hashCode() {
		return this.getName().hashCode();
	}

	@Override
	public boolean implies(Permission permission) {
		if (getName().equals(permission.getName())) {
			if (permission.getActions() == null || permission.getActions().trim().equals("")) {
				return true;
			}
			if (permission instanceof MaskPermission) {
				int inputMask = ((MaskPermission) permission).getMask();
				return hasAction(inputMask);
			} else {
				String action = permission.getActions();
				try {
					int inputMask = Integer.parseInt(action);
					return hasAction(inputMask);
				} catch (Exception e) {
				}
			}
		}
		return false;
	}

	public void merge(MaskPermission permission) {
		if (getName().equals(permission.getName())) {
			this.mask = mask | permission.getMask();
		}
	}

	public void merge(int newmask) {
		this.mask = mask | newmask;
	}

	protected boolean hasAction(int inputMask) {
		return ((mask & inputMask) == inputMask);
	}

	public String getPermissonActions() {
		return permissonActions;
	}

	public void setPermissonActions(String permissonActions) {
		this.permissonActions = permissonActions;
	}
	
}