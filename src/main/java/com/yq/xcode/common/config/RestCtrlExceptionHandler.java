package com.yq.xcode.common.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.yq.xcode.common.bean.Result;
import com.yq.xcode.common.exception.ValidateException;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.ResultUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class RestCtrlExceptionHandler {

	@ExceptionHandler(ValidateException.class)
	@ResponseStatus(value = HttpStatus.OK)
	public Result<Object> handleXCloudException(ValidateException e) {
		String errorMsg = "自定义异常";
		if (e != null) {
			if(CommonUtil.isNotNull(e.getMessage())) {
				  errorMsg = e.getMessage();
		    }
		}
 		return ResultUtil.error(500, errorMsg);
	}
	

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(value = HttpStatus.OK)
	public Result<?> invalidInput(MethodArgumentNotValidException ex) {
		return ResultUtil.error( ex);
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.OK)
	public Result<Object> handleException(Exception e) {
		 
		return ResultUtil.error( e);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(value = HttpStatus.OK)
	public Result<Object> handleAccessDeniedException(AccessDeniedException e) {
		String errorMsg = "权限不足，不允许访问";
		if (e != null) {
			if(CommonUtil.isNotNull(e.getMessage())) {
				  errorMsg = e.getMessage();
			}
		}
		return ResultUtil.error(HttpServletResponse.SC_FORBIDDEN, errorMsg);
	}
	
}
