package com.yq.xcode.security.bean;

import lombok.Data;

@Data
public class PrincipalPwd {

	private String oldpwd;

	private String newpwd;

	private String eqpwd;
}
