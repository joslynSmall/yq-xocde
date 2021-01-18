package com.yq.xcode.constants;
 
import java.util.HashMap;

import com.yq.xcode.common.bean.LookupCodeCategory;
import com.yq.xcode.common.model.PageTag;

public final class YqLCConstants { 

	 
	
	public final static LookupCodeCategory WORK_FLOW_STATUS_CATEGORY = new LookupCodeCategory(
			"WST", "工作流状态",  1 ,
			new HashMap<Integer,PageTag[]>() {
				 {
					 put(1, new PageTag[]{
							 new PageTag("segment1", "颜色", YqSelectHardcodeConstants.PageTag_tag.TextTag)   
						});
				 }
			}
		);
	
	public final static LookupCodeCategory WORK_FLOW_ACTION_CATEGORY = new LookupCodeCategory(
			"WAT", "工作流操作",  1 ,
			new HashMap<Integer,PageTag[]>() {
				 {
					 put(1, new PageTag[]{
							 new PageTag("segment1", "提醒提单人", YqSelectHardcodeConstants.PageTag_tag.SelectTag, "MSGTMP"), // 模板选择 
							 new PageTag("segment2", "通知下一审批人", YqSelectHardcodeConstants.PageTag_tag.SelectTag, "MSGTMP"),
							 new PageTag("segment3", "是审批操作", YqSelectHardcodeConstants.PageTag_tag.YesOrNoTag)
					   });
				 }
			}
	);
	
	public final static LookupCodeCategory WORK_FLOW_ROLE_CATEGORY = new LookupCodeCategory(
			"WFR", "工作流审批角色",  1 ,
			new HashMap<Integer,PageTag[]>() {
				 {
					 put(1, new PageTag[]{
							 new PageTag("segment1", "角色类型", YqSelectHardcodeConstants.PageTag_tag.SelectTag, "ROLECATEGORY"),  
							 new PageTag("segment2", "移动端待处理名称", YqSelectHardcodeConstants.PageTag_tag.TextTag ) 
					   });
				 }
			}
	);
	
	public final static LookupCodeCategory WORK_FLOW_GRAPH_NODE = new LookupCodeCategory(
			"WNODE", "工作流节点", 1 , null); 
	 
}
