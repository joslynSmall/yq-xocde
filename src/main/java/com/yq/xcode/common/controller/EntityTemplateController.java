package com.yq.xcode.common.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yq.xcode.common.bean.Result;
import com.yq.xcode.common.bean.SelectItem;
import com.yq.xcode.common.criteria.EntityTemplateCriteria;
import com.yq.xcode.common.criteria.PageTagCriteria;
import com.yq.xcode.common.model.EntityTemplate;
import com.yq.xcode.common.model.PageTag;
import com.yq.xcode.common.service.EntityTemplateService;
import com.yq.xcode.common.service.PageTagService;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.ResultUtil;
import com.yq.xcode.common.view.AbsControllerRequest;
import com.yq.xcode.common.view.EntityTemplateView;
import com.yq.xcode.common.view.PageTagGroupView;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "扩展定义")
@RestController
@RequestMapping("/admin/entityTemplate")
public class EntityTemplateController {

	@Autowired
	private EntityTemplateService entityTemplateService;
	@Autowired
	private PageTagService pageTagService;
	
	@ApiOperation("扩展定义【查询】")
	@GetMapping("/data")
	public Result<?> findEntityTemplate(EntityTemplateCriteria criteria){
		  Page<EntityTemplate> page = entityTemplateService.findEntityTemplatePage(criteria);
		  return ResultUtil.ok(page);
	}
	
	@ApiOperation("扩展定义【编辑】")
	@GetMapping("/{id}")
	public Result<?> getEntityTemplateById(@PathVariable("id") Long id){
		EntityTemplateView view = entityTemplateService.builderEntityTemplateView(id);
		return ResultUtil.ok(view);
	}
	
	@ApiOperation("扩展定义【保存】")
	@PostMapping("/save")
	public Result<?> saveEntityTemplate(@RequestBody EntityTemplate entityTemplate){
		entityTemplate = entityTemplateService.saveEntityTemplate(entityTemplate);
		EntityTemplateView view = entityTemplateService.builderEntityTemplateView(entityTemplate.getId());
		return ResultUtil.saveSuccess(view);
	}
	
	
	@ApiOperation("扩展定义【删除】")
	@GetMapping("/delete/{id}")
	public Result<?> deleteEntityTemplate(@PathVariable("id") Long id){
		entityTemplateService.deleteEntityTemplate(id);
		return ResultUtil.deleteSuccess();
	}
	
	@ApiOperation("扩展定义【批量删除】")
	@PostMapping("/delete/list")
	public Result<?> deleteTemplateList(@RequestBody AbsControllerRequest request){
		   entityTemplateService.deleteTemplateList(request.getIdv());
		return ResultUtil.deleteSuccess();
	}
	
	@ApiOperation("模板页面标签分组【查询】")
	@GetMapping("/pageTag/data/groupName")
	public Result<?> findPageTagByEntityTemplateCode(@RequestParam(value="code" )String code,@RequestParam(value="groupName" ) String groupName ){
		List<PageTag> list = pageTagService.findPageTagByEntityTemplateCode(code,groupName);
		  return ResultUtil.ok(list);
	}
	
	/**************************************页面标签***************************************************/
	
	@ApiOperation("页面标签【查询】")
	@GetMapping("/pageTag/data")
	public Result<?> findPageTag(PageTagCriteria criteria){
		List<PageTag> list = pageTagService.findPageTagList(criteria);
		  return ResultUtil.ok(list);
	}
	 
	 
	@ApiOperation("页面标签【编辑】")
	@GetMapping("/pageTag/{id}")
	public Result<?> getPageTagById(@PathVariable("id") Long id,
			@RequestParam(value="sourceCategory",defaultValue="",required=false)String sourceCategory,
			@RequestParam(value="sourceKey",defaultValue="",required=false)String sourceKey){
		PageTag pageTag = null;
		if(CommonUtil.isNullId(id)) {
			pageTag = pageTagService.initPageTag(sourceCategory,sourceKey);
		}else {
			pageTag = pageTagService.getPageTagById(id);
		}
		return ResultUtil.ok(pageTag);
	}
	
	@ApiOperation("页面标签【保存】")
	@PostMapping("/pageTag/save")
	public Result<?> savePageTag(@RequestBody PageTag pageTag){
		  pageTag = pageTagService.savePageTag(pageTag);
		return ResultUtil.saveSuccess(pageTag);
	}
	
	
	@ApiOperation("页面标签【删除】")
	@GetMapping("/pageTag/delete/{id}")
	public Result<?> deletePageTag(@PathVariable("id") Long id){
		pageTagService.deletePageTagById(id);
		return ResultUtil.deleteSuccess();
	}
	
	@ApiOperation("页面标签【批量删除】")
	@PostMapping("/pageTag/delete/list")
	public Result<?> deletePageTagList(@RequestBody AbsControllerRequest request){
		pageTagService.deletePageTag(request.getIdv());
		return ResultUtil.deleteSuccess();
	}
	
	@ApiOperation("页面标签-批量设置分组名称&序号【 修改】")
	@PostMapping("/pageTag/batchUpdatePageTagGroup")
	public Result<?> batchUpdatePageTagGroup(@RequestBody PageTagGroupView pageTagGroupView){
		pageTagService.batchUpdatePageTagGroup(pageTagGroupView);
		return ResultUtil.commonSuccess();
	}
	
	
	
	@ApiOperation("取模板的className属性【下拉取值】")
	@GetMapping("/find/property/{id}")
	public Result<?> findClassPropertyList(@PathVariable("id") Long id){
		List<SelectItem> list = pageTagService.findEntityTemplateClassProperty(id);
		return ResultUtil.ok(list);
	}
	
	@ApiOperation("指定数据源【下拉取值】")
	@GetMapping("/readType/source")
	public Result<?> findReadTypeSource(){
		List<SelectItem> list = pageTagService.findReadTypeSource();
		return ResultUtil.ok(list);
	}
	
	@ApiOperation("分组【下拉取值】")
	@GetMapping("/findGroupNameList")
	public Result<?> findGroupNameList(@RequestParam(value="sourceCategory")String sourceCategory,@RequestParam(value="sourceKey")String sourceKey){
		List<SelectItem> list = pageTagService.findGroupNameList(sourceCategory,sourceKey);
		return ResultUtil.ok(list);
	}
	
	
}
