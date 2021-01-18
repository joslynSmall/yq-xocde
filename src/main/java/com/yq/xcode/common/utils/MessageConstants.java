package com.yq.xcode.common.utils;

import java.util.HashMap;
import java.util.Map;

public class MessageConstants {
	
	public static final String SUCCESS = "success";
	public static final String MESSAGE = "message";
	public static final String RESULT = "result";
	public static final String DATA = "data";
	
	public static Map<String, ?> genSaveMessageTest(Object data){
		Map<String, Object> result = new HashMap<String, Object>();		
		result.put(RESULT,SUCCESS);
		result.put(MESSAGE, "保存成功!");
		result.put(DATA, data);
		return result;
	}


	public Map<String, Object> genSaveMessage(Object data){
		Map<String, Object> result = new HashMap<String, Object>();		
		result.put(RESULT,SUCCESS);
		result.put(MESSAGE, "保存成功!");
		result.put(DATA, data);
		return result;
	}
	
	public Map<String, ?> genUnCheckedMessage(Object data){
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put(RESULT,SUCCESS);
		result.put(MESSAGE, "取消审核完成!");
		result.put("data", data);
		return result;
	}
	
	public Map<String, ?> genCheckedMessage(Object data){
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put(RESULT,SUCCESS);
		result.put(MESSAGE, "审核完成!");
		result.put(DATA, data);
		return result;
	}
	
	public Map<String, ?> genUnSettleMessage(Object data){
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put(RESULT,SUCCESS);
		result.put(MESSAGE, "取消结算完成!");
		result.put("data", data);
		return result;
	}
	
	public Map<String, ?> genSettleMessage(Object data){
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put(RESULT,SUCCESS);
		result.put(MESSAGE, "结算完成!");
		result.put(DATA, data);
		return result;
	}
	
	public Map<String, ?> genDeleteMessage(Object data){
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put("result","success");
		result.put(MESSAGE, "删除成功!");
		result.put(DATA, data);
		return result;
	}
	
	public Map<String, ?> genShowMessage(String message,Object data){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(RESULT,SUCCESS);
		result.put(MESSAGE, message);
		result.put(DATA, data);
		return result;
	}
	
	public Map<String, ?> genErrorMessage(String message,Object data){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(RESULT,"fail");
		result.put(MESSAGE, message);
		result.put(DATA, data);
		return result;
	}
	
	/**
	 * 
	 * @param message
	 * @param data
	 * @param variables 变量
	 * @return
	 */
	public Map<String, ?> genShowMessage(String message,Object data,Map<String, ?> variables){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(RESULT,SUCCESS);
		result.put(MESSAGE, message);
		result.put(DATA, data);
		if (CommonUtil.isNotNull(variables)) {
			result.putAll(variables);
		}
		return result;
	}
	
	
	public Map<String, ?> genSendToApproveMessage(Object data){
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put("result","success");
		result.put(MESSAGE, "提交审批成功， 可在工作台查看!");
		result.put(DATA, data);
		return result;
	}

}
