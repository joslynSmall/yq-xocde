package com.yq.xcode.common.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yq.xcode.common.bean.SelectItem;

public class YqJsonUtil {
	
    private static ObjectMapper mapper = new ObjectMapper() {
    	{
    		this.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    	}
    };  
    
    public static String writeEntity2JSON(Object obj) throws Exception {
        StringWriter sw = new StringWriter();
        JsonGenerator gen = new JsonFactory().createGenerator(sw);
        mapper.writeValue(gen, obj);
        gen.close();
        return sw.toString();
    }  
  
    public static <T> T readJson2Entity(String jsonStr, Class<T> objClass)  
            throws JsonParseException, JsonMappingException, IOException {  
        return mapper.readValue(jsonStr, objClass);  
    }
    
	public static <T> List<T> parseArray(String jsonStr, Class<T> clazz) {
		return (List<T>) JSONObject.parseArray(jsonStr, clazz);
	}
	 
	public static void main(String[] arg) {
		ObjectMapper om = new ObjectMapper(); 
		String jsStr = " [ "
		               +" {\"itemKey\":\"key1\",\"itemName\":\"名称1\"}, "
		               +" 	   {\"itemKey\":\"key2\",\"itemName\":\"名称2\"} "
		               +" 	] ";
		//Object obj = om. convertValue(jsStr, new ArrayList<SelectItem>().getClass());
		Object obj = parseArray(jsStr, SelectItem.class);
		System.out.println(obj);
	}
}
