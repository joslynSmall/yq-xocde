package com.yq.xcode.constants;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Table;

import com.yq.xcode.annotation.EntityTableAlias;
import com.yq.xcode.common.bean.SelectItem;
import com.yq.xcode.common.model.PageTag;
import com.yq.xcode.common.utils.YqBeanUtil;

public final class YqConstants { 
 
	 
  	/**
	 * 公式属性
	 */
 	public static class Placeholder {
 		/**
 		 * 解析PLACEHOLDER用
 		 */
 		public final static String pDisplayId = "@OX5XWAXWptF@"; 
 		
 	} 
 	
 	/**
	 *  HD - hardcode ， 固定取
	 *  PARENT - 数据字典  - 下级
	 *  CATE   - 数据字典  - 所有
	 */
	public static class LIST_CATEGORY_HARDCODE_PREFIX {
		public final static SelectItem HARDCODE = new SelectItem("hardcode:","逗号“，”分隔 "); // 逗号“,”分隔 
		public final static SelectItem LOOKUP_PARENT = new SelectItem("parent:","数据字典， 通过父值"); ; // 来自数据字典， 通过父值
		public final static SelectItem LOOKUP_CATEGORY = new SelectItem("category:","数据字典， 通过类型"); ; //来自数据字典， 通过类型
		public final static SelectItem WFSTATUS = new SelectItem("wfstatus:","流程状态"); ; //来自流程状态
		public final static List<SelectItem> SELECT_LIST = new ArrayList<SelectItem>() {
			{
				add(HARDCODE);
				add(LOOKUP_PARENT);
				add(LOOKUP_CATEGORY);
				add(WFSTATUS);
			}
			
		};
	}
  
 
}
