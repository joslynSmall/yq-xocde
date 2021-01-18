package com.yq.xcode.security.criteria;

import com.yq.xcode.annotation.ParameterLogic;
import com.yq.xcode.common.springdata.HPageCriteria;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrincipalCriteria extends HPageCriteria {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -8164533222216773931L;
	

    @ParameterLogic(colName = "CONCAT(a.ORGANIZATION_ID,',')", operation = " like ")
    private String enterpriseId;
	@ParameterLogic(colName = "CONCAT(a.name,a.display_name,ifnull(a.mobile_phone,''))", operation="like" )
	private String name;
}
