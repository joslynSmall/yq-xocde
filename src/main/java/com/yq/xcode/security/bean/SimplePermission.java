package com.yq.xcode.security.bean;

import java.security.Permission;
import java.util.HashSet;
import java.util.Set;

public class SimplePermission extends Permission {

	private static final long serialVersionUID = 7154945754487987661L;

	private String actions;

	private Set<String> actionSet = new HashSet<String>();

	public SimplePermission(String name) {
		super(name);
	}

	public SimplePermission(String name, String actions) {
		super(name);
		setActions(actions);

	}

	public SimplePermission(String name, String[] actionsTokens) {
		super(name);
		setActions(actionsTokens);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof SimplePermission)) {
			return false;
		}
		SimplePermission perm = (SimplePermission) obj;
		return perm.getName().equals(getName())
				&& (perm.getActions() == null ? (actions == null) : (perm.getActions().equals(actions)));
	}

	private void setActions(String actions) {
		this.actions = actions;
		if (actions != null) {
			String[] array = parseArray(actions);
			for (String act : array) {
				actionSet.add(act);
			}
		}
	}

	private void setActions(String[] actions) {
		if (actions != null) {
			StringBuffer buffer = new StringBuffer();
			for (String act : actions) {
				actionSet.add(act);
				buffer.append(act).append(",");
			}
			buffer.delete(buffer.length(), buffer.length());
		}
	}

	@Override
	public String getActions() {
		return actions;
	}

	public Set<String> getActionSet() {
		return actionSet;
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
			if (actionSet == null) {
				return false;
			}
			if (permission instanceof SimplePermission) {
				Set<String> actions = ((SimplePermission) permission).getActionSet();
				for (String action : actions) {
					if (!actionSet.contains(action)) {
						return false;
					}
				}
			} else {
				String[] actions = parseArray(permission.getActions());
				for (String action : actions) {
					if (!actionSet.contains(action)) {
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}

	public static String[] parseArray(String text) {
		if (text == null || text.trim().equals("")) {
			return null;
		}
		if (text.startsWith("[") && text.endsWith("]")) {
			text = text.substring(1, text.length() - 1);
			return text.split(",");
		} else {
			return text.split(",");
		}
	}
}
