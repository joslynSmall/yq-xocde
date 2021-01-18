package com.yq.xcode.common.criteria;

import com.yq.xcode.annotation.ParameterLogic;

public class PageTagCriteria extends NativeCriteria {

	@ParameterLogic(colName="a.group_Name", operation="=")
	private String groupName;
	@ParameterLogic(colName="a.TAG_KEY", operation="=")
	private String tagKey;
	@ParameterLogic(colName="a.SOURCE_CATEGORY", operation="=")
	private String sourceCategory;
	@ParameterLogic(colName="a.source_key", operation="=")
	private String sourceKey;
	
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getTagKey() {
		return tagKey;
	}
	public void setTagKey(String tagKey) {
		this.tagKey = tagKey;
	}
	public String getSourceCategory() {
		return sourceCategory;
	}
	public void setSourceCategory(String sourceCategory) {
		this.sourceCategory = sourceCategory;
	}
	public String getSourceKey() {
		return sourceKey;
	}
	public void setSourceKey(String sourceKey) {
		this.sourceKey = sourceKey;
	}

}
