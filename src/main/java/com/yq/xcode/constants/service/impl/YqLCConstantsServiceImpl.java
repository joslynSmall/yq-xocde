package com.yq.xcode.constants.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.yq.xcode.common.bean.LookupCodeCategory;
import com.yq.xcode.constants.YqLCConstants;
import com.yq.xcode.constants.service.LookupCodeConstantsService;

@Service("YqLCConstantsService")
@Transactional
public class YqLCConstantsServiceImpl   implements LookupCodeConstantsService {

	@Override
	public List<LookupCodeCategory> getLookupCodeCategoryList() {
		 
		return null;
	}

	@Override
	public Class[] getLookupCodeConstantsArray() {
		 
		return new Class[] {
				YqLCConstants.class
		};
	}

	 
	

 
}