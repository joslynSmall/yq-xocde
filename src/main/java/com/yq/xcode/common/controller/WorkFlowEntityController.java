package com.yq.xcode.common.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yq.xcode.common.bean.ListPageDefine;
import com.yq.xcode.common.bean.Result;
import com.yq.xcode.common.bean.WorkFlowActionButton;
import com.yq.xcode.common.bean.WorkFlowActionParam;
import com.yq.xcode.common.bean.WorkFlowEntityCategoryPage;
import com.yq.xcode.common.bean.WorkFlowEntityIntf;
import com.yq.xcode.common.bean.WorkFlowWeappView;
import com.yq.xcode.common.model.WorkFlowEntityActionLog;
import com.yq.xcode.common.service.SelectItemService;
import com.yq.xcode.common.service.WorkFlowEntityQueryService;
import com.yq.xcode.common.service.WorkFlowSimpleActionService;
import com.yq.xcode.common.utils.ResultUtil;

/**
 * @Auther caoyongqiang
 * @Date 2020/4/03
 */
@RestController
@RequestMapping("/admin/workFlowEntity")
public class WorkFlowEntityController {
	@Autowired
    private SelectItemService selectItemService; 
	@Autowired
    private WorkFlowEntityQueryService workFlowEntityQueryService;
	
	@Autowired
    private WorkFlowSimpleActionService workFlowSimpleActionService;
 
	/**
	 * stage 待审， 已审核， 历史 
	 * @param stage
	 * @return
	 */
	@RequestMapping(value = { "/categoryList/{stage}" }, method = RequestMethod.GET)
  	public Result findSelectItemListByCategory(@PathVariable("stage") String stage) {
		List<WorkFlowEntityCategoryPage> list = this.workFlowEntityQueryService.findWorkFlowCategoryPageList(stage) ;
		return ResultUtil.ok(list);
	}
	/**
	 * 执行审批
	 * @param param
	 * @return
	 */
	@RequestMapping(value = { "/executeAction" }, method = RequestMethod.POST)
  	public Result executeAction(@RequestBody WorkFlowActionParam param) {
		this.workFlowSimpleActionService.executeSingleAction(param.getEntityId() , param.getEntityCategory(), 
				param.getAction() , param.getActionReason());
		return ResultUtil.ok("审批 通过 ！");
	}
	
	/**
	 * 可以的操作
	 * @param stage
	 * @return
	 */
	@RequestMapping(value = { "/getEnableActions" }, method = RequestMethod.GET)
  	public Result getEnableActions(@RequestParam Long entityId, @RequestParam String entityCategory) {
		List<WorkFlowActionButton> list = this.workFlowSimpleActionService.getEnableActions(entityId, entityCategory);
		return ResultUtil.ok(list, "取数成功！");
	}
	
	/**
	 * 流程日志
	 * @param stage
	 * @return
	 */
	@RequestMapping(value = { "/findActionLog" }, method = RequestMethod.GET)
  	public Result findActionLogList(@RequestParam Long entityId, @RequestParam String entityCategory) {
		List<WorkFlowEntityActionLog> list = this.workFlowSimpleActionService.findActionLog(entityId, entityCategory); 
		return ResultUtil.ok(list, "取数成功！");
	}
	
	
	/**
	 * 流程图
	 * @param stage
	 * @return
	 */
	@RequestMapping(value = { "/getWorkFlowGraphList" }, method = RequestMethod.GET)
  	public Result getWorkFlowGraphList(@RequestParam Long entityId, @RequestParam String entityCategory) {
		List<WorkFlowWeappView> list = this.workFlowSimpleActionService.getWorkFlowGraphList(entityId, entityCategory) ;
		return ResultUtil.ok(list, "取数成功！");
	}
	
	@GetMapping("/findEntityPages/{stage}")
	public Result<?> findEntityPage( @PathVariable("stage") String stage , @RequestParam String entityCategory, @RequestParam String criteriaStr ) throws Exception {
		Page<WorkFlowEntityIntf> page = this.workFlowEntityQueryService.findWorkFlowEntityPage(stage,entityCategory,criteriaStr);
 		return ResultUtil.ok(page);
	}


	@GetMapping("/pagedef")
	public Result<?> getWorkFlowEntityListDefineByCategoryCode(   @RequestParam String entityCategory ) {
		ListPageDefine listDefine = this.workFlowEntityQueryService.getWorkFlowEntityListDefineByCategoryCode( entityCategory );
 		return ResultUtil.ok(listDefine);
	}

}
