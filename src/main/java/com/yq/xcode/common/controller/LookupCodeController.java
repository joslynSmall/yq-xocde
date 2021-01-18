package com.yq.xcode.common.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.yq.xcode.common.bean.LookupCodeCategory;
import com.yq.xcode.common.bean.Result;
import com.yq.xcode.common.criteria.LookupCodeQueryCriteria;
import com.yq.xcode.common.model.LookupCode;
import com.yq.xcode.common.model.PageTag;
import com.yq.xcode.common.service.InitConstantsService;
import com.yq.xcode.common.service.LookupCodeService;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.ResultUtil;

/**
 * @Auther 
 * @Date 2020/4/03
 */
@RestController
@RequestMapping("/admin/lookupcode")
public class LookupCodeController {

	@Autowired
    private InitConstantsService initConstantsService;
	@Autowired
    private LookupCodeService lookupCodeService;

	/**
	 * 获取数据字典分类
	 * @return
	 */
	@PostMapping( "/getLookupCodeCategoryList"  )
	@ResponseBody
	public Result<List<LookupCodeCategory>> getLookupCodeCategoryList() {
		List<LookupCodeCategory> cateList = this.initConstantsService.getLookupCodeCategoryList();
		for(LookupCodeCategory lookupCodeCategory:cateList) {
			if(lookupCodeCategory.getMaxLevel() > 1) {
//				LookupCodeQueryCriteria criteria = new LookupCodeQueryCriteria();
//				criteria.setKeyCode(lookupCodeCategory.getCategoryCode());
				List<LookupCode> lookupCodeByCriteria = lookupCodeService.findLookupCodeByCategoryLevel(lookupCodeCategory.getCategoryCode(),1);
				
				lookupCodeCategory.setLookupCodes(lookupCodeByCriteria);
			}
		}
 		return ResultUtil.ok(cateList);
	}
	
	@RequestMapping(value = { "/lookupCodes/read" }, method = RequestMethod.GET)
	@ResponseBody
	public Result<List<LookupCode>> getLookupCodes(@ModelAttribute LookupCodeQueryCriteria criteria) {
		List<LookupCode> LookupCodeList = lookupCodeService.getLookupCodeByCriteria(criteria);
		return ResultUtil.ok(LookupCodeList);
	}
	
	/**
	 * create和update时的编辑页面
	 * @param keyCode
	 * @param categoryCode
	 * @return
	 */
	@RequestMapping(value = "/lookupCodes/edit/{keyCode}/{categoryCode}/{parentKeyCode}", method = { RequestMethod.GET })
	@ResponseBody
	public Result lookupCodeEditView(@PathVariable("keyCode") String keyCode,
			@PathVariable("categoryCode") String categoryCode,
			@PathVariable("parentKeyCode") String parentKeyCode) {
		LookupCodeCategory cate = initConstantsService.getLookupCodeCategory(categoryCode);
 		List<PageTag> dtls = new ArrayList<PageTag>();
 		if (cate.getExtDefine() != null) {
 			Integer level = 1;
 			if (!CommonUtil.isNull(parentKeyCode) && !"null".equals(parentKeyCode)) {
 				LookupCode lc = this.lookupCodeService.getLookupCodeByKeyCode(parentKeyCode);
 				level = (Integer)CommonUtil.nvl(lc.getLookupLevel(),1)+1;
 			}
 			PageTag[] sa = cate.getExtDefine().get(level);
 			if (CommonUtil.isNotNull(sa)) {
 				for (PageTag s : sa) {
 					dtls.add(s);
 				}
 			}
 		} 
		return ResultUtil.ok(dtls);
	}
	
	@RequestMapping(value = { "/lookupCodes/window" }, method = RequestMethod.GET)
	@ResponseBody
	public Result<LookupCode> getEditLookupCode(String keyCode, String categoryCode, String parentKeyCode){
		if("new".equals(keyCode)){
			LookupCode newlookupCode = new LookupCode();
			newlookupCode.setCategoryCode(categoryCode);	
			newlookupCode.setLookupLevel(1);
			if (CommonUtil.isNotNull(parentKeyCode) && !"null".equals(parentKeyCode)) {
				LookupCode plc = this.lookupCodeService.getLookupCodeByKeyCode(parentKeyCode);
				newlookupCode.setParentKeyCode(parentKeyCode);
				newlookupCode.setLookupLevel(plc.getLookupLevel()+1-0);
			}
			
			return ResultUtil.ok(newlookupCode);
		}
		LookupCode lookupCode = lookupCodeService.getLookupCodeByKeyCode(keyCode);
		return ResultUtil.ok(lookupCode);
	}
	
	@RequestMapping(value = "/lookupCodes/deleteItems", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteLookupCodes(@RequestBody String[] codes) {
		lookupCodeService.deleteLookupCode(codes);
		return ResultUtil.ok();
	}
	
	@RequestMapping(value = "/lookupCodes/update", method = { RequestMethod.POST })
	@ResponseBody
	public Result saveLookupCode(@RequestBody LookupCode lookupCode) {
		if (CommonUtil.isNull(lookupCode.getKeyCode())) {
			lookupCode.setKeyCode(lookupCode.getCategoryCode() + "-" + lookupCode.getLookupCode());
			lookupCode = lookupCodeService.createLookupCode(lookupCode);//创建
		} else {
			lookupCode = lookupCodeService.updateLookupCode(lookupCode);//更新 
		}
		return ResultUtil.saveSuccess(lookupCode);
	}	

	/**
	 * 保存数据字典
	 * @return
	 */
//	@PostMapping("/save")
//	public Result saveLookupCode(@RequestBody LookupCode lookupCode) {
//		lookupCode = this.lookupCodeService.saveLookupCode(lookupCode);
// 		return ResultUtil.saveSuccess(lookupCode);
//	}

	/**
	 * 删除数据字典
	 * @return
	 */
	@DeleteMapping("/delete")
	public Result delete(@RequestParam String keyCode) {
//		this.lookupCodeService.deleteLookupCode(keyCode);
 		return ResultUtil.deleteSuccess();
	}

	/**
	 * 获取数据字典分页列表
	 * @param criteria
	 * @return
	 */
	@PostMapping("/findLookupCodePage")
	public Result findLookupCodePage(@RequestBody LookupCodeQueryCriteria criteria) {
		Page<LookupCode> page = this.lookupCodeService.findLookupCode(criteria);
		return ResultUtil.ok(page) ;
	}

	/**
	 * 获取数据字典列表
	 * @param criteria
	 * @return
	 */
	@PostMapping("/getLookupCodeList")
	public Result getLookupCodeList(@RequestBody LookupCodeQueryCriteria criteria) {
		List<LookupCode> cList = null;
		if (CommonUtil.isNull(criteria.getParentKeyCode())) {
			cList = this.lookupCodeService.findLookupCodeByCategory(criteria.getCategoryCode(), 0);
		} else {
			cList = this.lookupCodeService.findKeyCodeByParentKeyCode(criteria.getParentKeyCode());
		}
		return ResultUtil.ok(cList);
	}

	/**
	 * 根据keyCode获取数据字典
	 * @param keyCode
	 * @return
	 */
	@PostMapping("/getLookupCodeByKeyCode")
	public Result getLookupCodeByKeyCode(@RequestParam String keyCode) {
		LookupCode lc = this.lookupCodeService.getLookupCodeByKeyCode(keyCode);
		return ResultUtil.ok(lc);
	}
		
 
}
