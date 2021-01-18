package com.yq.xcode.common.utils;

import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate5.HibernateOptimisticLockingFailureException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.yq.xcode.common.bean.ExceptionView;
import com.yq.xcode.common.exception.BaseDefineException;
import com.yq.xcode.common.exception.DllException;
import com.yq.xcode.common.exception.ParseExprException;
import com.yq.xcode.common.exception.ValidateException;
import com.yq.xcode.common.exception.WorkFlowException; 

public class YqExceptionUtil {
    private static Log log = LogFactory.getLog(YqExceptionUtil.class);
 
    public static ExceptionView genExceptionMsg(Exception e) throws Exception  {
    	if (e == null) {
    		return new ExceptionView();
    	}
    	e.printStackTrace();
    	String eClassName = e.getClass().getName();
    	//System.out.println("eClassName -->"+eClassName);
    	String eMsg = "";
    	if (e instanceof ValidateException) {
    		e.printStackTrace();
    		return new ExceptionView(e.getMessage(),null);
    	} 
    	if (e instanceof DllException) {
    		return new ExceptionView(e.getMessage(),null);
    	}
    	if (e instanceof WorkFlowException) {
    		WorkFlowException wfe = (WorkFlowException)e;
    		ExceptionView ev =new ExceptionView(wfe.getMessage(),null);
    		ev.setMessage(wfe.getActionInfo());
    		return ev;
    	}
    	
    	if (e instanceof ParseExprException) {
    		return new ExceptionView(e.getMessage(),ExceptionUtils.getFullStackTrace(e));
    	}
    	
    	if (e instanceof BaseDefineException) {
    		return new ExceptionView(e.getMessage(),null);
    	}
    	if (e instanceof NullPointerException ) {
    		return new ExceptionView("空指针异常，请联系管理员!" ,ExceptionUtils.getFullStackTrace(e));
    	}
    	

    	if (e instanceof MethodArgumentNotValidException ) {
    		return new ExceptionView("参数无效异常！" ,ExceptionUtils.getFullStackTrace(e));
    	}
    	
    	if (e instanceof HibernateOptimisticLockingFailureException ) {
    		return new ExceptionView("多人操作同一数据，数据被锁！请稍后重试！" ,ExceptionUtils.getFullStackTrace(e));
    	}
    	
    	if (e instanceof DataIntegrityViolationException ) {  
    		if (e.getCause() instanceof ConstraintViolationException ) {
    			ConstraintViolationException cvExp = (ConstraintViolationException)e.getCause();
    			int errorCode = cvExp.getErrorCode(); //将来根据错误的代码的不同，提示不同的信息
    			if (errorCode == 1451) {
    				return new ExceptionView("数据已被外键引用，不可删除!" ,null);	
    			}  
    			if (errorCode == 1048) {
    				return new ExceptionView("数据操作异常,非空列未给值!" ,ExceptionUtils.getFullStackTrace(e));	
    			} 
    			if (errorCode == 1062) {
    				return new ExceptionView("违法数据重复校验!" ,ExceptionUtils.getFullStackTrace(e));	
    			} 
        	}  
//    		if (e.getCause().getCause() instanceof MysqlDataTruncation) {
//    			MysqlDataTruncation jdbcEx = (MysqlDataTruncation) e.getCause().getCause();
//    			if("22".equals(jdbcEx.getSQLState().substring(0,2))){
//    				if("001".equals(jdbcEx.getSQLState().substring(2,5))){
//    					return new ExceptionView("字符数据，发生右截断；例如，更新或插入值对于列来说太长（字符串），或者日期时间值由于太小而不能赋给主机变量。若确实需要，请通知数据库管理员！" ,ExceptionUtils.getFullStackTrace(jdbcEx));	
//    				}
//    			}
//    		}
    		return new ExceptionView("数据操作异常!请联系管理员!" ,ExceptionUtils.getFullStackTrace(e));
    	}
    	
    	
    	
    	if (e instanceof PersistenceException ) {
    		if (e.getCause() instanceof ConstraintViolationException ) {
    			ConstraintViolationException ve = (ConstraintViolationException)e.getCause();
        		return new ExceptionView("违反唯一性约束!" ,ExceptionUtils.getFullStackTrace(ve));
        	}
    		if (e instanceof OptimisticLockException ) {
        		return new ExceptionView("数据已被其他人修改或删除， 请重新打开处理!", null);
        	}
    		return new ExceptionView("查询语句错误，请联系管理员!" ,ExceptionUtils.getFullStackTrace(e));
    	}
    	if (e instanceof BindException ) {
    		return new ExceptionView("数据绑定异常，请联系管理员!" ,ExceptionUtils.getFullStackTrace(e));
    	}
    	if (e instanceof HttpMediaTypeNotSupportedException ) {
    		return new ExceptionView("方法匹配异常，请联系管理员!" ,ExceptionUtils.getFullStackTrace(e));
    	}  
    	
    	return new ExceptionView("系统错误，请联系管理员!" ,ExceptionUtils.getFullStackTrace(e));  		
    }   

}
