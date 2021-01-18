package com.yq.xcode.common.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.yq.xcode.common.criteria.CriteriaParameter;
import com.yq.xcode.common.criteria.WorkFlowEntityCriteria;

public class WorkFlowUtil {

	/**
	 * 合并角色和userCode,合并方式为[role:userCode]
	 * @param role
	 * @param userCode
	 * @return
	 */
	public static String roleUserstoString(String role,String userCode) {
		return "["+role+":"+userCode+"]";
	}
	public static boolean roleUsersContainsRole(String roleUsers, String role) {
		if (CommonUtil.isNull(roleUsers)) {
			return false;
		}
		return roleUsers.contains("["+role+":");
	}
	public static boolean roleUsersContainsRoleUser(String roleUsers, String role,String userCode) {
		if (CommonUtil.isNull(roleUsers)) {
			return false;
		}
		return roleUsers.contains("["+role+":"+userCode+"]");
	}
	
	public static String getUserByRole(String roleUsers, String role) {
		if (CommonUtil.isNull(roleUsers)) {
			return null;
		}
		String splitStr = "["+role+"-";
		int  index = roleUsers.indexOf(splitStr); 
		if (index >=0) {
			 String s = roleUsers.substring(index+splitStr.length()); 
			 index = s.indexOf("]");
			 if (index > 1) {
				 return s.substring(0,index);
			 } else {
				 return null;
			 }
		} else {
			return null;
		} 
	}
	
	/**
	 * 重新设置补齐参数
	 * @param criter
	 * @param defineList
	 * @throws Exception
	 */
	public static void resetWorkFlowEntityCriter(WorkFlowEntityCriteria criter, List<CriteriaParameter> defineList )  {
 		if (defineList == null) {
			defineList = new ArrayList<CriteriaParameter>();
		}
		List<CriteriaParameter> criteriaParameterListQuery=criter.getParameters();
		List<CriteriaParameter> retCrit = new ArrayList<CriteriaParameter>();
		for(int iTemp=0;iTemp<defineList.size();++iTemp){
			CriteriaParameter p = null;
			try {
				p = (CriteriaParameter) BeanUtils.cloneBean(defineList.get(iTemp));
			} catch ( Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
			p.setParameterValue(criteriaParameterListQuery.get(iTemp).getParameterValue());
			retCrit.add(p);
		}
		criter.setParameters(retCrit);
	}

}
