package com.yq.xcode.common.service;


import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.yq.xcode.common.criteria.NativeCriteria;
import com.yq.xcode.common.springdata.AggregatePageImpl;
import com.yq.xcode.common.springdata.HPageCriteria;
import com.yq.xcode.common.springdata.HPageRequest;
import com.yq.xcode.common.springdata.PageQuery;

public interface SqlToModelService {

	

	/**
	 *  支持table.* 的写法，先根据clazz的数据库列mapping到 模型，如果mapping不到
	 * 在根据列名属性mapping模型，如果还是mapping不到，如果有pageMap属性，则put到此属性，否则或略
	 * @param <E>
	 * @param query
	 * @param queryCriteria
	 * @param clazz
	 * @return
	 */
	//public <E> AggregatePageImpl<E> executeNativeQueryForPage(PageQuery pageQuery,Class<E> clazz) ;
	/**
	 * 和用PageQuery的参数相同，便于串参数
	 * @param <E>
	 * @param selectCols
	 * @param fromTable
	 * @param defaultSortBy
	 * @param criteria
	 * @param clazz
	 * @return
	 */
	//public <E> AggregatePageImpl<E> executeNativeQueryForPage(String query, String defaultSortBy, String groupBy, NativeCriteria criteria,HPageRequest pageRequest,Class<E> clazz) ;
	
	public <E> AggregatePageImpl<E> executeNativeQueryForPage(String selectCols , String fromTable, String defaultSortBy, NativeCriteria criteria,HPageRequest pageRequest,Class<E> clazz) ;

	/**
	 * 
	 *  先根据clazz的数据库列mapping到 模型，如果mapping不到
	 * 在根据列名属性mapping模型，如果还是mapping不到，如果有pageMap属性，则put到此属性，否则或略
	 * @param sql
	 * @param parmObj
	 * @param clazz
	 * @return
	 */
	public <E> List<E> executeNativeQuery(String query, Object parmObj,
			final Class<E> clazz) ;
	
	/**
	 * 主要是为了支持自定义查询列，
	 * 要注意，
	 * 1. query 不可以包含order by 语句， 如果有order by 用order 参数
	 * 2. query 必须要存在where语句， 如果没有条件要写， 可以写where 1=1
	 */
	public <E> List<E> executeNativeQuery(String query, String sortBy ,String groupBy, NativeCriteria criteria,
			final Class<E> clazz) ;
	public HSSFWorkbook dbStructureToExcel(String dbName);
	
	public List<Map<String, Object>> executeSqlNativeQuery(String query);
	
	
	public int getCountByPageQuery(PageQuery pageQuery);
	
	public <E> E getSingleRecord(String query, Object parmObj,
			final Class<E> clazz);
	
	public <E> AggregatePageImpl<E> executeNativeQueryForPage(String query, String defaultSortBy, String groupBy,
			HPageCriteria criteria, Class<E> clazz);
	
	public <E> List<E> executeNativeQuery(String query, String orderBy, Object parmObj, Class<E> clazz);

	public <E> AggregatePageImpl<E> executeNativeQueryForPage(PageQuery pageQuery, Class<E> clazz);

	public <E> List<E> executeNativeQuery(String query, String sortBy,String groupBy,String having, 
			NativeCriteria criteria, Class<E> clazz);

}
