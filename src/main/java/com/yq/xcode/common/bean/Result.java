package com.yq.xcode.common.bean;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> implements Serializable {
 
	private static final long serialVersionUID = 5662874108601857330L;

	public enum Status {
		SUCCESS, ERROR
	}
	
	public enum Mode {
		INFO, ALERT
	}

	/**
	 * 成功标志
	 */
	private Status status;

	/**
	 * 提示消息
	 */
	private String msg;
	
	/**
	 * 提示详情
	 */
	private String dtlMsg;

	/**
	 * 返回代码
	 */
	private Integer code;

	/**
	 * 时间戳
	 */
	private long timestamp = System.currentTimeMillis();

	/**
	 * 结果对象
	 */
	private T data;
	
	/**
	 * 消息展示模式
	 */
	private Mode mode;
}
