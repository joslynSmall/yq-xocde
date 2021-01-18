package com.yq.xcode.common.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.yq.xcode.common.bean.LookupCodeCategory;
import com.yq.xcode.common.bean.Result;
import com.yq.xcode.common.bean.SelectItemDefine;
import com.yq.xcode.common.service.InitConstantsService;
import com.yq.xcode.common.springdata.HPageRequest;
import com.yq.xcode.common.utils.ResultUtil;

/**
 * @author zjZhang
 * @date 2020/8/7 0007
 */
@RestController
@RequestMapping("/admin/selectItemDefine")
public class SelectItemDefineController {

    @Autowired
    private InitConstantsService initConstantsService;

    @RequestMapping(value = {"/list"}, method = RequestMethod.GET)
    @ResponseBody
    public Result getselectItemDefineView(HPageRequest hPageRequest) {
        List<SelectItemDefine> selectItemDefineList = initConstantsService.getSelectItemDefineList();
        List<LookupCodeCategory> lookupCodeCategoryList = initConstantsService.getLookupCodeCategoryList();
        Map<String, Object> obj = new HashMap<>();
        obj.put("selectItemDefineList", selectItemDefineList);
        obj.put("lookupCodeCategoryList", lookupCodeCategoryList);
        return ResultUtil.ok(obj);
    }

}
