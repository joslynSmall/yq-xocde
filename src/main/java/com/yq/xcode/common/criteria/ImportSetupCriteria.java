


 package com.yq.xcode.common.criteria;

import com.yq.xcode.annotation.ParameterLogic;

public class ImportSetupCriteria extends NativeCriteria{

	@ParameterLogic(colName=" a.vouch_number", operation="like" , placeHolder="" )
	private String vouchNumber;
	@ParameterLogic(colName=" a.channel_category", operation="like" , placeHolder="" )
	private String channelCategory;

	public String getVouchNumber() {
		return vouchNumber;
	}
	public void setVouchNumber(String vouchNumber) {
		this.vouchNumber=vouchNumber;
	}
	public String getChannelCategory() {
		return channelCategory;
	}
	public void setChannelCategory(String channelCategory) {
		this.channelCategory=channelCategory;
	}
	
}
