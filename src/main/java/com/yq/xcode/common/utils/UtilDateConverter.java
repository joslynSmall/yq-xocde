package com.yq.xcode.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.beanutils.Converter;

public class UtilDateConverter implements Converter {

    public Object convert(Class arg0, Object arg1) {
    	if (CommonUtil.isNull(arg1)) {
    		return null;
    	}
    	if (arg1 instanceof String) {
    		String p = (String)arg1;
            if(p== null || p.trim().length()==0){
                return null;
            }   
            try{
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                return df.parse(p.trim());
            }
            catch(Exception e){
                try {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    return df.parse(p.trim());
                } catch (ParseException ex) {
                	try {
                        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                        return df.parse(p.trim());
                    } catch (ParseException exx) {
                        return null;
                    }
                }
            }
    	} else {
    		return arg1;
    	}
        
        
    }
    
    

}