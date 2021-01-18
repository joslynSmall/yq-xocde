package com.yq.xcode.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoRollbackException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String msg;
}
