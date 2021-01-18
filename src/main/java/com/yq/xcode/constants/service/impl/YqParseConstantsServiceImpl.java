package com.yq.xcode.constants.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yq.xcode.common.bean.ParseElementUse;
import com.yq.xcode.constants.YqParseConstants;
import com.yq.xcode.constants.service.ParseConstantsService;

@Service("YqParseConstantsService")
public class YqParseConstantsServiceImpl   implements
ParseConstantsService {

	@Override
	public List<ParseElementUse> getParseElementUseList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class[] getParseCodeConstantsArray() {
		 
		return new Class[] {
				YqParseConstants.class
		};
	}

	 
	
 
}
