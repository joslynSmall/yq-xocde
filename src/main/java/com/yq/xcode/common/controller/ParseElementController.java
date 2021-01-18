package com.yq.xcode.common.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.yq.xcode.common.bean.ParseElementDisplayView;
import com.yq.xcode.common.bean.ParseElementUse;
import com.yq.xcode.common.bean.Result;
import com.yq.xcode.common.bean.SelectItem;
import com.yq.xcode.common.bean.SelectItemDefine;
import com.yq.xcode.common.criteria.ParseElementQueryCriteria;
import com.yq.xcode.common.model.ParseElement;
import com.yq.xcode.common.service.InitConstantsService;
import com.yq.xcode.common.service.ParseElementService;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.ParseUtil;
import com.yq.xcode.common.utils.ResultUtil;

/**
 * @author zjZhang
 * @date 2020/8/5 0005
 */
@RestController
@RequestMapping("/admin/parseElement")
public class ParseElementController {

    @Autowired
    private ParseElementService parseElementMainService;

    @Autowired
    private InitConstantsService initConstantsService;

    @RequestMapping(value="/elementSetup/tree",method=RequestMethod.GET)
    @ResponseBody
    public Result elementSetupForm(){
        List<ParseElementUse> parseElementUseList = initConstantsService.getParseElementUseList();
        return ResultUtil.ok(parseElementUseList);
    }

    @RequestMapping(value ={"/readelement"}, method = RequestMethod.POST)
    @ResponseBody
    public Result<List<ParseElement>> programPtable(String eleCategory, String categoryCode){
        ParseElementQueryCriteria criteria = new ParseElementQueryCriteria();
        criteria.setCategoryCode(categoryCode);
        criteria.setEleCategory(eleCategory);
        List<ParseElement> page = parseElementMainService.findAllParseElements(criteria);
        return ResultUtil.ok(page);
    }

    @RequestMapping(value="/getElementByid",method=RequestMethod.GET)
    @ResponseBody
    public Result<ParseElement> elementByid(Long id,String eleCategory,String useCategory){

        ParseElement parseElementByIdAndNew = parseElementMainService.getParseElementByIdAndNew(id, eleCategory, useCategory);
        return ResultUtil.ok(parseElementByIdAndNew);
    }

    /**
     * 元素设置 数据类型
     * **/
    @RequestMapping(value = { "/element/dataType" }, method = RequestMethod.GET)
    @ResponseBody
    public Result elementDataTypes(){
//        List<SelectItem> types = new ArrayList<SelectItem>();
        SelectItemDefine propertytype = initConstantsService.getSelectItemDefine("PROPERTYTYPE");
        return ResultUtil.ok(propertytype.getSelectItemList());
    }

    @RequestMapping(value = { "/toolFunctiones" }, method = RequestMethod.GET)
    @ResponseBody
    public Result selectToolFunction(){
        return ResultUtil.ok(ParseUtil.getParseToolFunction());
    }

    @RequestMapping(value="/readselectelement",method= RequestMethod.GET)
    @ResponseBody
    public Result<ParseElement> selectelementtable(String elementNumber){
        ParseElement pe = new ParseElement();
        if(CommonUtil.isNotNull(elementNumber)){
            ParseElement pet =parseElementMainService.getParseElementByEleNumber(elementNumber.substring(0,4));
            pe =parseElementMainService.translateParseElement(pet);
        }

        return ResultUtil.ok(pe);
    }

    /**
     * 元素设置 函数设置 元素取值
     * **/
    @RequestMapping(value = { "/elementGetResult/{eleNumber}" }, method = RequestMethod.GET)
    @ResponseBody
    public Result<List<SelectItem>> elementGetResult(@PathVariable("eleNumber") String eleNumber){
        List<SelectItem> types = parseElementMainService.findelementGetResult(eleNumber);
        return ResultUtil.ok(types);
    }
    
    /**
	 * 元素设置 函数设置 用途类型
	 * **/
	@RequestMapping(value = { "/elementUseProperty/{useCategory}" }, method = RequestMethod.GET)
	@ResponseBody
	public Result elementUseProperty(@PathVariable("useCategory") String useCategory){
		ParseElementUse parseElementUse = initConstantsService.getParseElementUse(useCategory);
		return ResultUtil.ok(parseElementUse);
	}

    //翻译
    @RequestMapping(value = { "/editElement" }, params={"action=translate"}, method = RequestMethod.POST)
    @ResponseBody
    public Result translateElement(@RequestBody ParseElement parseElement){

        ParseElement translatePe = parseElementMainService.translateParseElement(parseElement);

        return ResultUtil.ok(translatePe);
    }

    /**
     * 元素设置 函数设置 选择参数
     * **/
    @RequestMapping(value = { "/functionByParameter/{useCategory}" }, method = RequestMethod.GET)
    @ResponseBody
    public Result<List<SelectItem>> functionByParameters(@PathVariable("useCategory") String useCategory){
        List<SelectItem> types = parseElementMainService.findfunctionByParameter(useCategory);
        return ResultUtil.ok(types);
    }

    //保存
    @RequestMapping(value = { "/editElement" }, params={"action=edit"}, method = RequestMethod.POST)
    @ResponseBody
    public Result editVendorPO(@RequestBody ParseElement parseElement){

        ParseElement pe = parseElementMainService.editParseElement(parseElement);

        return ResultUtil.ok(pe);
    }

    //测试
    @RequestMapping(value = { "/editElement" }, params={"action=check"}, method = RequestMethod.POST)
    @ResponseBody
    public Result checkElement(@RequestBody ParseElement parseElement, HttpSession httpSession){
        httpSession.setAttribute("parseElement", parseElement);
        ParseElementDisplayView pv  = parseElementMainService.checkParseElementUse(parseElement);
         //throw new ValidateException("恭喜, 通过测试, 结果值为:"+pv.getValue()+"<br/>计算过程："+pv.getDisplayExpress());
        return ResultUtil.ok ("恭喜, 通过测试, 结果值为:"+pv.getValue()+"<br/>计算过程："+pv.getDisplayExpress(), null);
    }

}
