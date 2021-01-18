package com.yq.xcode.constants.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yq.xcode.common.bean.SelectItemDefine;
import com.yq.xcode.constants.YqSelectHardcodeConstants;
import com.yq.xcode.constants.YqSelectQueryConstants;
import com.yq.xcode.constants.service.SelectConstantsService;


@Service("YqSelectConstantsService")
public class YqSelectConstantsServiceImpl   implements SelectConstantsService {

	@Override
	public List<SelectItemDefine> getSelectItemDefineList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?>[] getSelectHarcodeConstansClassArr() {
		Class[] clazzArr = new Class[] {
				YqSelectHardcodeConstants.class
		};
		return clazzArr;
	}

	@Override
	public Class<?>[] getSelectQueryConstansClassArr() {
		Class[] clazzArr = new Class[] {
				YqSelectQueryConstants.class
		};
		return clazzArr;
	}
	 
	
}
