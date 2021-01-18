package com.yq.xcode.common.criteria;

import com.yq.xcode.common.springdata.HPageCriteria;

@SuppressWarnings("serial")
public abstract  class   WorkFlowEntityCriteria   extends HPageCriteria{
	public abstract String getStage(); 
	 
}