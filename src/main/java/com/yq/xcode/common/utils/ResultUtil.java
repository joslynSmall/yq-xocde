package com.yq.xcode.common.utils;

import org.springframework.data.domain.Page;

import com.yq.xcode.common.bean.ExceptionView;
import com.yq.xcode.common.bean.Result;
import com.yq.xcode.common.bean.Result.Mode;
import com.yq.xcode.common.bean.Result.Status;
import com.yq.xcode.common.criteria.NativeCriteria;
import com.yq.xcode.common.springdata.HPageCriteria;

public class ResultUtil<T> {

	public static <T> Result<T> ok(T t) {
		Result<T> result = new Result<>();
		result.setStatus(Status.SUCCESS);
		result.setCode(200);
		result.setData(t);
		return result;
	}

	public static <T> Result<T> ok() {
		Result<T> result = new Result<>();
		result.setStatus(Status.SUCCESS);
		result.setCode(200);
		result.setData(null);
		return result;
	}

	public static <T> Result<T> page() {
		Result<T> result = new Result<>();
		result.setStatus(Status.SUCCESS);
		result.setCode(200);
		result.setData(null);
		return result;
	}

	public static <T> Result<T> ok(String msg) {
		Result<T> result = new Result<>();
		result.setStatus(Status.SUCCESS);
		result.setCode(200);
		result.setData(null);
		result.setMsg(msg);
		return result;
	}

	public static <T> Result<T> ok(T t, String msg) {
		Result<T> result = new Result<>();
		result.setStatus(Status.SUCCESS);
		result.setCode(200);
		result.setData(t);
		result.setMsg(msg);
		return result;
	}
	
	public static <T> Result<T> ok(Mode mode, String msg) {
		Result<T> result = new Result<>();
		result.setStatus(Status.SUCCESS);
		result.setCode(200);
		result.setData(null);
		result.setMsg(msg);
		result.setMode(mode);
		return result;
	}
	
	public static <T> Result<T> ok(Mode mode, T t, String msg) {
		Result<T> result = new Result<>();
		result.setStatus(Status.SUCCESS);
		result.setCode(200);
		result.setData(t);
		result.setMsg(msg);
		result.setMode(mode);
		return result;
	}

	public static <T> Result<T> error(Mode mode, String msg) {
		Result<T> result = new Result<>();
		result.setMode(mode);
		result.setStatus(Status.ERROR);
		result.setCode(500);
		result.setMsg(msg);
		return result;
	}
	
	public static <T> Result<T> error(String msg) {
		return error(msg,null);
	}
	
	public static <T> Result<T> error(String msg,String detail) {
		return error(500,msg,detail);
	}
	
	public static <T> Result<T> error(Integer code, String msg) {
		return error(500,msg,null);
	}

	public static <T> Result<T> error(Integer code, String msg,String detail) {
		Result<T> result = new Result<>();
		result.setStatus(Status.ERROR);
		result.setCode(code);
		result.setMsg(msg);
		result.setDtlMsg(detail);
		return result;
	}
	
	public static <T> Result<T> error(Exception e) {
		ExceptionView view;
		try {
			view = YqExceptionUtil.genExceptionMsg(e);
			return error(view.getTitle(),view.getDetail());
		} catch (Exception e1) {
 			e1.printStackTrace();
 			return error("异常识别错误！");
		}
		
	}
	
	

	public static <T> Result<T> okAlert(T data,String msg) {
		return ok(Mode.ALERT, data, msg);
	}
	
	public static <T> Result<T> okInfo(T data,String msg) {
		return ok(Mode.INFO, data, msg);
	}
	
	
	public static <T> Result<T> saveSuccess(T data) {
		return okInfo(data, "保存成功!");
	}

	public static <T> Result<T> deleteSuccess() {
		return okInfo(null, "删除成功!");
	}

	public static <T> Result<T> checkSuccess(T data) {
		return okInfo(data, "审核成功!");
	}

	public static <T> Result<T> unCheckSuccess(T data) {
		return okInfo(data, "取消审核成功!");
	}

	public static <T> Result<T> approveSuccess(T data) {
		return okInfo(data, "提交审批成功!");
	}

	public static <T> Result<T> commonSuccess() {
		return okInfo(null, "执行成功!");
	}
	
	public static Result pageOrExport(Page page,HPageCriteria criteria) {
		if (criteria.isExportData() ) {
			return YqExcelUtil.exportExcel(criteria, page.getContent());
		}
		return ok(page);
	} 

}
