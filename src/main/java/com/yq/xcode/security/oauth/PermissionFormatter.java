package com.yq.xcode.security.oauth;

import java.security.Permission;
import java.security.PermissionCollection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.security.bean.ActionMask;
import com.yq.xcode.security.bean.GrantedPermissions;
import com.yq.xcode.security.bean.MaskPermission;
import com.yq.xcode.security.bean.SimpleAuthentication;

 

public class PermissionFormatter {

	public static SimpleAuthentication format(String userName, Authentication auth) {
		List<String> roles = new ArrayList<String>();
		List<MaskPermission> maskPermissions = new ArrayList<MaskPermission>();
		if(CommonUtil.isNotNull(auth.getAuthorities())) {
		for (GrantedAuthority ca : auth.getAuthorities()) {
			if (ca instanceof SimpleGrantedAuthority) {
				roles.add(ca.getAuthority());
			} else if (ca instanceof GrantedPermissions) {
				PermissionCollection permissions = ((GrantedPermissions) ca).getPermissions();
				Enumeration<Permission> elements = permissions.elements();
				while (elements.hasMoreElements()) {
					Permission permission = elements.nextElement();
					if (permission instanceof MaskPermission) {
						MaskPermission maskPermission = (MaskPermission) permission;
						ActionMask[] a = toActionMasks(maskPermission.getPermissonActions());
						String ps = "";
						if (CommonUtil.isNotNull(a)) {
							for (int i = 0; i < a.length; i++) {
								if (hasAction(maskPermission.getMask(), a[i].getMask())) {
									ps = ps + a[i].getName()+"=1,";
								}
							}
							if (CommonUtil.isNotNull(ps) && ps.endsWith(",")) {
								ps = ps.substring(0, ps.length()-1);
								maskPermission.setPermissonActions(ps);
							} else {
								maskPermission.setPermissonActions("");
							}
						}
						maskPermissions.add(maskPermission);
					}
				}
			}
		}
		}
		SimpleAuthentication authentication = new SimpleAuthentication();
		authentication.setRoles(roles);
		authentication.setMasks(maskPermissions);
		return authentication;
	}
	
	 
    public static void main(String[] args) {
		String s = "{Create=1,Update=2,Delete=4,生成账单=8,取消账单=16,导入=32,导入1=64,导入2=128}";
		ActionMask[] a = toActionMasks(s);
		for (int i = 0; i < a.length; i++) {
			System.out.println(a[i].getMask()+" "+a[i].getName());
		}
		System.out.println(hasAction(659, 1024));
		System.out.println(hasAction(659, 2048));
		
	}
    
	protected static boolean hasAction(int mask,  int inputMask) {
		return ((mask & inputMask) == inputMask);
	}
	
	public static ActionMask[] toActionMasks(String text) {
		if (text == null || text.trim().equals("")) {
			return null;
		}
		if (text.startsWith("{") && text.endsWith("}")) {
			List<ActionMask> masks = new ArrayList<ActionMask>();
			text = text.substring(1, text.length() - 1);
			for (String elem : text.split(",")) {
				int colonIndex = elem.indexOf('=');
				if (colonIndex <= 0) {
					throw new java.lang.IllegalArgumentException(text + " can not parse to a map.");
				}
				masks.add(new ActionMask(elem.substring(0, colonIndex), Integer.parseInt(elem.substring(colonIndex + 1))));
			}
			return masks.toArray(new ActionMask[masks.size()]);
		} else {
			return null;
		}
	}
	
}
