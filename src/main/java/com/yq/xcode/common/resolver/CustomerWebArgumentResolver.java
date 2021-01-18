package com.yq.xcode.common.resolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

import com.yq.xcode.common.springdata.HExportColumn;
import com.yq.xcode.common.springdata.HExportRequest;
import com.yq.xcode.common.springdata.HPageRequest;
	
public class CustomerWebArgumentResolver implements WebArgumentResolver {
	@Override
	public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest webRequest) throws Exception {
		Map<String,String[]> params = webRequest.getParameterMap();
		if(HExportRequest.class.isAssignableFrom(methodParameter.getParameterType())){
			HExportRequest her = new HExportRequest(getIntParameter(params,"page",1),getIntParameter(params,"pageSize",20),getOrders(params),getIntParameter(params,"total",0));
			her.setAggregates(getAggregates(params));
			//her.setAggregate(getAggregate(webRequest));
			her.setAggregateJsonStr( webRequest.getParameter("aggregateJsonStr")) ;
			Boolean exportFlag= this.getBooleanParameter(params, "exportFlag");
			if(exportFlag.equals(Boolean.TRUE))
				her.setColumns(getExportColumns(params));
			
			return her;
		}else if(HPageRequest.class.isAssignableFrom(methodParameter.getParameterType())){
			int page = getIntParameter(params,"page",1);
			SpringDataRequest sdr = methodParameter.getParameterAnnotation(SpringDataRequest.class);
			if(sdr != null){
				page -= 1;
			}
			HPageRequest hpr = new HPageRequest(page,getIntParameter(params,"pageSize",20),getOrders(params),getIntParameter(params,"total",0));
			hpr.setAggregates(getAggregates(params));
			//hpr.setAggregate(getAggregate(webRequest));
			hpr.setAggregateJsonStr( webRequest.getParameter("aggregateJsonStr")) ;
			return hpr;
		}else{
//			int page = getIntParameter(params,"page",1);
//			SpringDataRequest sdr = methodParameter.getParameterAnnotation(SpringDataRequest.class);
//			if(sdr != null){
//				page -= 1;
//			}
//			PageRequest pr = new PageRequest(page,getIntParameter(params,"pageSize",20),getOrders(params));
			return WebArgumentResolver.UNRESOLVED;
		}
	}
	

	private List<HExportColumn> getExportColumns(Map<String,String[]> params){
		List<HExportColumn> columns = new ArrayList<HExportColumn>();
		Map<String,String> maps = new HashMap<String, String>();
		for(String key:params.keySet()){
			if(key.startsWith("columns")){
				String colKey = key.substring(0, key.lastIndexOf("["));
				if(maps.containsKey(colKey))continue;
				maps.put(colKey, colKey);
				String field = getStringParameter(params,colKey + "[field]");
				String title = getStringParameter(params,colKey + "[title]");
				String width = getStringParameter(params,colKey + "[width]");
				Boolean hidden = getBooleanParameter(params, colKey + "[hidden]");
				int seq = getIntParameter(params, colKey + "[seq]",0);
				HExportColumn col = new HExportColumn();
				col.setField(field);
				col.setTitle(title);
				col.setWidth(width);
				col.setHidden(hidden);
				col.setSequence(seq);
				columns.add(col);
			}
		}
		
		if(columns.size() > 0){
			Collections.sort(columns, new Comparator<HExportColumn>(){
				@Override
				public int compare(HExportColumn a, HExportColumn b) {
					return a.getSequence() - b.getSequence();
				}
				
			});
			return columns;
		}
		
		
		return null;
	}
	private Map<String,Map<String,Double>> getAggregates(Map<String,String[]> params){
		Map<String,Map<String,Double>> aggregates = new HashMap<String, Map<String,Double>>();
		for(String key:params.keySet()){
			if(key.startsWith("aggregates")){
				String fieldKey = key.substring(key.indexOf("[") + 1,  key.lastIndexOf("]["));
				Map<String,Double> fieldAggregates;
				if(!aggregates.containsKey(fieldKey)){
					fieldAggregates = new HashMap<String, Double>();
					aggregates.put(fieldKey, fieldAggregates);
				}else{
					fieldAggregates = aggregates.get(fieldKey);
				}
				
				String aggregateKey = key.substring(key.lastIndexOf("][") + 2,key.lastIndexOf("]"));
				fieldAggregates.put(aggregateKey, getDoubleParameter(params, key));
			}
		}
		if(aggregates.size() > 0)
			return aggregates;
		
		return null;
	}
	
	private Map<String,String> getAggregate(NativeWebRequest webRequest){
		Map<String,String> aggregates = new HashMap<String, String>();
		boolean found = true;
		int ind = 0;
		while (found) {
			String keyField = "aggregate["+ind+"][field]";
			String keyAggregate = "aggregate["+ind+"][aggregate]";
			String field = webRequest.getParameter(keyField);
			if (field == null || "".equals(field)) {
				found = false;
			} else {
				String aggregate = webRequest.getParameter(keyAggregate);
				aggregates.put(field, aggregate);
				ind++;
			}
		}
		
		if(aggregates.size() > 0)
			return aggregates;
		
		return null;
	}

	private Sort getOrders(Map<String,String[]> params) {
		Map<String,String> maps = new HashMap<String,String>();
		
		List<Order> orders = new ArrayList<Sort.Order>();
		for(String key :params.keySet()){
			if(key.startsWith("sort[")){
				String sortKey = key.substring(0, key.lastIndexOf("["));
				if(maps.containsKey(sortKey))continue;
				maps.put(sortKey, sortKey);
				String field = getStringParameter(params,sortKey + "[field]");
				String desc = getStringParameter(params,sortKey + "[dir]");
				Order order = new Order(Direction.fromString(desc),field);
				orders.add(order);
			}
		}
		
		if(orders.size() > 0){
			Sort sort = new Sort(orders);
			return sort;
		}
		
		return new Sort(orders);
	}
	
	private String getStringParameter(Map<String,String[]> params,String key) {
		String[] values = params.get(key);
		if(values == null) {
			return null;
		}
		return values[0];
	}
	
	private int getIntParameter(Map<String,String[]> params,String key,int defalutValue ) {
		String[] values = params.get(key);
		if(values == null) {
			return defalutValue;
		}
		return Integer.parseInt(values[0]);
	}
	
	private Double getDoubleParameter(Map<String,String[]> params,String key){
		String[] values = params.get(key);
		if(values == null) {
			return 0d;
		}
		
		return Double.parseDouble(values[0]);
	}
	
	private Boolean getBooleanParameter(Map<String,String[]> params, String key){
		String[] values = params.get(key);
		if(values == null){
			return false;
		}
		
		return Boolean.parseBoolean(values[0]);
	}	
}
