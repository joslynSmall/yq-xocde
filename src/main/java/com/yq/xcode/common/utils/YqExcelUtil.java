package com.yq.xcode.common.utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.yq.xcode.common.bean.Result;
import com.yq.xcode.common.springdata.HPageCriteria;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.ExcelExportUtil;
import com.yq.xcode.common.utils.ResultUtil;

@Component
public class YqExcelUtil {

	/**
	 * WebMvcConfig addResourceHandlers
	 */
	private final static String PATH_PATTERN = "/uploadFiles/";
	
	private static String pattern;

	public static <T> Result<String> exportExcel(HPageCriteria criteria, List<T> data) {
		String fileName = CommonUtil.buildRandomKey();
		ExcelExportUtil.exportExcel(pattern + "/" + fileName + ".xls", null, criteria, data);
		return ResultUtil.ok(PATH_PATTERN + fileName + ".xls", "导出成功");
	}

	@Value("${static.path}")
	public void setPattern(String p) {
		pattern = p;
	}
	
}
