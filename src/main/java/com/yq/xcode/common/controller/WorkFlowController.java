package com.yq.xcode.common.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.yq.xcode.common.bean.Result;
import com.yq.xcode.common.bean.SelectItem;
import com.yq.xcode.common.bean.WorkFlowEntityCategory;
import com.yq.xcode.common.bean.WorkFlowView;
import com.yq.xcode.common.criteria.WorkFlowDetailQueryCriteria;
import com.yq.xcode.common.criteria.WorkFlowQueryCriteria;
import com.yq.xcode.common.model.WorkFlow;
import com.yq.xcode.common.model.WorkFlowDetail;
import com.yq.xcode.common.model.WorkFlowDetailSendMsg;
import com.yq.xcode.common.model.WorkFlowGraphDetail;
import com.yq.xcode.common.service.InitConstantsService;
import com.yq.xcode.common.service.WorkFlowDetailSendMsgService;
import com.yq.xcode.common.service.WorkFlowService;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.ResultUtil;

/**
 * @author zjZhang
 * @date 2020/8/3 0003
 */
@RestController
@RequestMapping("/admin/workFlow")
public class WorkFlowController {

    @Autowired
    private WorkFlowService workFlowService;

    @Autowired
    private InitConstantsService initConstantsService;

    @Autowired
    private WorkFlowDetailSendMsgService workFlowDetailSendMsgService;

    /**
     * 主表list页面
     */
    @RequestMapping(value = {"/workFlows/tree"}, method = RequestMethod.GET)
    public Result<?> workFlowsIndex() {

        List<WorkFlowEntityCategory> entityCategoryList = initConstantsService.getEntityCategoryList();
        return ResultUtil.ok(entityCategoryList);
    }

    @RequestMapping(value = {"/workFlows/{categoryCode}"}, method = RequestMethod.GET)
    @ResponseBody
    public Result<Page<WorkFlow>> findWorkFlows( WorkFlowQueryCriteria criteria, @PathVariable("categoryCode") String categoryCode) {
        if (CommonUtil.isNotNull(categoryCode))
            criteria.setCategoryCode(categoryCode);
        Page<WorkFlow> page = workFlowService.findWorkFlows(criteria );
        return ResultUtil.ok(page);
    }

    @RequestMapping(value = {"/workFlow/{id}"}, method = RequestMethod.GET)
    @ResponseBody
    public Result<?> getWorkFlowView(@PathVariable("id") Long workFlowId) {
        WorkFlowView view = workFlowService.getWorkFlowViewByWorkFlowId(workFlowId);
        return ResultUtil.ok(view);
    }

    @RequestMapping(value = {"/workFlowDetail/data"}, method = RequestMethod.GET)
    @ResponseBody
    public Result<WorkFlowDetail> getWorkFlowDetail(@RequestParam("id") Long id, @RequestParam("workFlowId") Long workFlowId) {
        WorkFlowDetail detail = new WorkFlowDetail();
        if (CommonUtil.isNotNull(id)) {
            WorkFlowDetailQueryCriteria criteria = new WorkFlowDetailQueryCriteria();
            criteria.setWid(id);
            criteria.setWorkFlowId(workFlowId);
            List<WorkFlowDetail> list = workFlowService.findWorkFlowDetailsByCriteria(criteria);
            if (CommonUtil.isNotNull(list) && !list.isEmpty()) {
                detail = list.get(0);
                List<WorkFlowDetailSendMsg> msgList = this.workFlowDetailSendMsgService.findWorkFlowDetailSendMsgByDtlId(detail.getId());
                if (CommonUtil.isNotNull(msgList)) {
                    String msg = "";
                    String split = "";
                    for (WorkFlowDetailSendMsg dtlMsg : msgList) {
                        msg = msg + split + dtlMsg.getTargetName() + " : " + dtlMsg.getMsgCodeDsp();
                        split = "<br>";
                    }
                    detail.setRemaindRoles(msg);
                }
            }
        }
        return ResultUtil.ok(detail);
    }

    /**
     * 审批流程图状态列表所有状态
     */
    @RequestMapping(value = {"/workFlowStatuseBygIds/{graphDetailId}"}, method = RequestMethod.GET)
    @ResponseBody
    public Result<List<SelectItem>> findWorkFlowStatuseBygraphDetailIds(@PathVariable("graphDetailId") Long graphDetailId) {
        List<SelectItem> list = workFlowService.findWorkFlowStatuseBygraphDetailIds(graphDetailId);
        return ResultUtil.ok(list);
    }

    /**
     * 审批流程图1状态列表所有状态
     */
    @RequestMapping(value = {"/workFlowStatuses/{id}"}, method = RequestMethod.GET)
    @ResponseBody
    public Result<List<SelectItem>> findWorkFlowStatuses(@PathVariable("id") Long workFlowId) {
        List<SelectItem> list = workFlowService.findWorkFlowStatuses(workFlowId);
        return ResultUtil.ok(list);
    }

    @RequestMapping(value = {"/getWorkFlowSkipPageDefineList/{workFlowId}"}, method = RequestMethod.GET)
    @ResponseBody
    public Result<List<SelectItem>> getWorkFlowSkipPageDefineList(@PathVariable("workFlowId") Long workFlowId) {
        List<SelectItem> list = this.workFlowService.getWorkFlowSkipPageDefineList(workFlowId);
        return ResultUtil.ok(list);
    }

    @RequestMapping(value = {"/workFlow/{id}"}, method = RequestMethod.POST)
    @ResponseBody
    public Result<?> updateWorkFlow(@PathVariable("id") long id, @RequestBody WorkFlow view) {
        WorkFlowView workFlowView = new WorkFlowView();
        workFlowView.setWorkFlow(view);
        WorkFlow workFlow = workFlowService.saveWorkFlowView(workFlowView);
        return ResultUtil.ok(workFlow, "保存成功!");
//        view = workFlowService.getWorkFlowViewByWorkFlowId(view.getWorkFlow().getId());
    }

    @RequestMapping(value = {"/workFlow"}, method = RequestMethod.GET)
    @ResponseBody
    public Result<?> getNewWorkFlowView(@RequestParam("categoryCode") String categoryCode) {
    	WorkFlowView view = new WorkFlowView();
		WorkFlow workFlow = new WorkFlow();
		workFlow.setCategoryCode(categoryCode);
//		workFlow.setStartDate(new Date());
//		workFlow.setEndDate(DateUtil.getDateAfterMonths(new Date(), 240));
		//不知道为什么这样做
		workFlow.setUseRelationFunction("");
		view.setWorkFlow(workFlow);
		view.setUsing(true);
		return ResultUtil.ok(view);
    }

    @RequestMapping(value = {"/workFlow/{id}"}, params = {"action=delete"}, method = RequestMethod.POST)
    @ResponseBody
    public Result<?> deleteWorkFlow(@PathVariable("id") long id, @RequestBody WorkFlowView view) {
        workFlowService.deleteWorkFlowView(view);
        return ResultUtil.ok("删除成功!");
    }
    
    @RequestMapping(value={"/workFlow/{id}"},params={"action=copy"},method=RequestMethod.POST)
	@ResponseBody
	public Result<?> copyWorkFlow(@PathVariable("id") long id) {
		WorkFlow workflow = workFlowService.copyWorkFlowById(id);
		return ResultUtil.ok("复制成功!");
	}

    @RequestMapping(value = {"/workFlowDetail/{id}"}, params = {"action=copy"}, method = RequestMethod.POST)
    @ResponseBody
    public Result<?> copyWorkFlowDetail(@PathVariable("id") long workFlowDetailId) {
        WorkFlowDetail workFlowDetail = workFlowService.copyWorkFlowDetail(workFlowDetailId);
        return ResultUtil.ok("复制成功!");
    }

    @RequestMapping(value = {"/workFlowGraphDetail/save/{id}"}, method = RequestMethod.POST)
    @ResponseBody
    public Result<?> saveWorkFlowGraphDetail(@PathVariable("id") Long workFlowId, @RequestBody WorkFlowGraphDetail workFlowGraphDetail) {
        workFlowService.saveWorkFlowGraphDetail(workFlowId, workFlowGraphDetail);
        return ResultUtil.ok("保存成功!");
    }

    @RequestMapping(value = {"/workFlowGraphDetail"}, method = RequestMethod.GET)
    @ResponseBody
    public Result<?> getNewWorkFlowGraphDetail() {
        WorkFlowGraphDetail detail = new WorkFlowGraphDetail();
        return ResultUtil.ok(detail);
    }

    @RequestMapping(value = {"/workFlowGraphDetails/delete/{id}"}, method = RequestMethod.POST)
    @ResponseBody
    public Result<?> deleteWorkFlowGraphDetail(@PathVariable("id") Long workFlowId) {
        workFlowService.deleteWorkFlowGraphDetailByOne(workFlowService.getWorkFlowGraphDetail(workFlowId));
        return ResultUtil.ok("删除成功!");
    }

    @RequestMapping(value = {"/workFlowDetail"}, method = RequestMethod.POST)
    @ResponseBody
    public Result<?> saveWorkFlowDetail(@RequestBody WorkFlowDetail workFlowDetail) {
        workFlowService.saveWorkFlowDetail(workFlowDetail);
        workFlowDetail = workFlowService.getWorkFlowDetailById(workFlowDetail.getId());
        return ResultUtil.ok();

    }

    @RequestMapping(value = {"/workFlowDetail/delete/{id}"}, params = {"action=delete"}, method = RequestMethod.POST)
    @ResponseBody
    public Result<?> deleteWorkFlowDetailById(@PathVariable("id") long id) {
        workFlowService.deleteWorkFlowDetail(workFlowService.getWorkFlowDetailById(id));
        return ResultUtil.ok();
    }
    
    /**
	 * 工作流程导出
	 * @param pr
	 * @param categoryCode
	 * @return
	 */
    @RequestMapping(value = { "/workFlows/export" }, method = RequestMethod.POST)
	@ResponseBody
	public Result<?> exportchainFeeLists(WorkFlowQueryCriteria criteria){
    	criteria.setExportData(true);
//    	workFlowService.findWorkFlowExportView(categoryCode,idvs);
//	    Page<WorkFlow> pageResult = workFlowService.findWorkFlows(criteria);
//	    return SiyuExcelUtil.exportExcel(criteria, pageResult.getContent());
    	return ResultUtil.ok();
	}
    
//    @RequestMapping(value = "/worlFlowView/{categoryCode}",params= {"action=export"},method=RequestMethod.POST)
//	@ResponseBody
//	public Result<?> exportWorkFlowView(@PathVariable("categoryCode")String categoryCode,
//			@RequestParam("idvs") Long idvs){
//		  pr.setColumns(workFlowService.genWorkFlowHExportColumn());
//		List<WorkFlowExportView> data = workFlowService.findWorkFlowExportView(categoryCode,idvs);
//		return excelService.exportExcel(pr, data, null,null);
//	}

}
