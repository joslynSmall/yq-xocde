package com.yq.xcode.security.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class MiniprogramUser implements Serializable {

	private static final long serialVersionUID = 1L;

	private String mobile;

	private String nickname;

}
