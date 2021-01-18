package com.yq.xcode.common.service.impl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.sql.DataSource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.yq.xcode.common.bean.AggregateCol;
import com.yq.xcode.common.bean.PropertyDefine;
import com.yq.xcode.common.bean.QueryModel;
import com.yq.xcode.common.criteria.NativeCriteria;
import com.yq.xcode.common.service.SqlToModelService;
import com.yq.xcode.common.service.WorkFlowEntityQueryService;
import com.yq.xcode.common.springdata.AggregatePageImpl;
import com.yq.xcode.common.springdata.HExportRequest;
import com.yq.xcode.common.springdata.HPageCriteria;
import com.yq.xcode.common.springdata.HPageRequest;
import com.yq.xcode.common.springdata.HWorkFlowEntityPageCriteria;
import com.yq.xcode.common.springdata.PageQuery;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.JPAUtils;
import com.yq.xcode.common.utils.JsUtil;
import com.yq.xcode.common.utils.ReportUtil;
import com.yq.xcode.common.utils.YqBeanUtil;

@Service("SqlToModelService")
public class SqlToModelServiceImpl  implements SqlToModelService {

	@Qualifier("dataSource")
	@Autowired
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate = new JdbcTemplate();
	private static Log log = LogFactory.getLog(SqlToModelServiceImpl.class);
	@Autowired
	private WorkFlowEntityQueryService workFlowEntityQueryService;
	//返回
/*	
 * @Autowired
	private SystemParameterService systemParameterService;
	*/

	private Object getObjectFromRset(ResultSet rset, String classType,
			String colName, Field field) throws SQLException {
		Object fieldValue = rset.getObject(colName);
		if (fieldValue == null) {
			return getNvlValue(classType);
		}
		PropertyDescriptor property = null;
		if (field != null && field.getType().isEnum()) {
			Enumerated em = field.getAnnotation(Enumerated.class);
			if (em != null && EnumType.STRING.compareTo(em.value()) == 0 ) {
				return Enum.valueOf(field.getType().asSubclass(Enum.class), fieldValue.toString());
			} else {
				Object[] emSa = field.getType().getEnumConstants();
				if (CommonUtil.isNotNull(emSa)) {
					Integer ind = Integer.parseInt(fieldValue.toString());
					return emSa[ind];
				}
			}
		} else	if ("java.lang.String".equals(classType)) {
			fieldValue = rset.getString(colName);
		} else if ("java.util.Date".equals(classType)) {
			fieldValue = rset.getTimestamp(colName);
		} else if ("java.lang.Integer".equals(classType)) {
			fieldValue = rset.getInt(colName);
		} else if ("int".equals(classType)) {
			fieldValue = rset.getInt(colName);
		} else if ("java.lang.Double".equals(classType)) {
			fieldValue = rset.getDouble(colName);
		} else if ("double".equals(classType)) {
			fieldValue = rset.getDouble(colName);
		} else if ("java.lang.Float".equals(classType)) {
			fieldValue = rset.getFloat(colName);
		} else if ("float".equals(classType)) {
			fieldValue = rset.getFloat(colName);
		} else if ("java.lang.Short".equals(classType)) {
			fieldValue = rset.getShort(colName);
		} else if ("short".equals(classType)) {
			fieldValue = rset.getShort(colName);
		} else if ("java.lang.Long".equals(classType)) {
			fieldValue = rset.getLong(colName);
		} else if ("long".equals(classType)) {
			fieldValue =  CommonUtil.nvl(rset.getLong(colName),Long.valueOf(0));
		} else if ("java.math.BigDecimal".equals(classType)) {
			fieldValue = rset.getBigDecimal(colName);
		} else if ("boolean".equals(classType) || "java.lang.Boolean".equals(classType)) {
			fieldValue = rset.getBoolean(colName);
		} else {
			fieldValue = rset.getObject(colName);
		}
		return fieldValue;
	}
	
	private Object getNvlValue(String classType) {
       if ("integer".equals(classType)) {
			return Integer.valueOf(0);
		} if ("double".equals(classType)) {
			return Double.valueOf(0);
		} else  if ("float".equals(classType)) {
			return Float.valueOf(0);
		} else if ("short".equals(classType)) {
			return Short.valueOf("0");
		}  else if ("long".equals(classType)) {
			return 0l;
		}  else if ("boolean".equals(classType)) {
			return false;
		} 
		return null;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
 
	@Override
	public <E> AggregatePageImpl<E> executeNativeQueryForPage(PageQuery pageQuery,Class<E> clazz)  {
		jdbcTemplate.setDataSource(this.dataSource);
		jdbcTemplate.afterPropertiesSet();
		AggregatePageImpl pageImpl = null;
		HPageRequest pageRequest = pageQuery.getPageRequest(); // 为能返回total records, 所以每次都要执行count, 可考虑将其返回
		if (pageRequest instanceof HExportRequest) {
			QueryModel fullQM = pageQuery.getFullQueryModelForMysql();
			List<E> list = this.executeNativeQuery(fullQM.getQuery(), fullQM.getParameters(), clazz);
			pageImpl = new AggregatePageImpl(list  ,pageRequest,list.size());
			return pageImpl;
		}
		if (pageQuery.getCriteria()!=null && !CommonUtil.isNull(pageRequest.getAggregateJsonStr())) {
			pageQuery.getCriteria().getAggregateCols().addAll(ReportUtil.mapToAggregates(pageRequest.getAggregateJsonStr()) );			
		}
		QueryModel currentQM = pageQuery.getCurrPageQueryModelForMysql();
		int firstResult = pageRequest.getPageSize() * (pageRequest.getPageNumber() - 1);
		if(firstResult < 0) {
			firstResult = 0;
		}
		int ttlRecords = 100; // 只用默认100
		//第一页执行，后面不执行合计
		List<E> tlist = this.findCurrentRecord(currentQM.getQuery(), currentQM.getParameters(), clazz,pageQuery) ;
		if(firstResult == 0 || pageRequest.getTotal() == 0 ) {
			QueryModel countQM = pageQuery.getCountPageQueryModel();
			Map<String, Object> mapGroup = mapGroup=jdbcTemplate.queryForMap(countQM.getQuery(),countQM.getParameters());
			ttlRecords = Integer.valueOf(mapGroup.get(PageQuery.COUNT_COL_NAME).toString());
 			pageImpl = new AggregatePageImpl(tlist ,pageRequest,ttlRecords);
			
			if (pageQuery.getCriteria()!=null && !CommonUtil.isNull(pageQuery.getCriteria().getAggregateCols())) {
				Iterator<AggregateCol> it = pageQuery.getCriteria().getAggregateCols().iterator();
				while (it.hasNext()) {
					AggregateCol col = it.next();
					if (JPAUtils.isAggregateCols(col)) { 
						pageImpl.addAggregate(col.getProperty(), col.getCategory(), new Double(CommonUtil.nvl(mapGroup.get(col.getPropertyAndCategory()),"0").toString()));
					}
				}
			}
		} else { 
			ttlRecords = pageRequest.getTotal();
			pageImpl = new AggregatePageImpl(tlist  ,pageRequest,ttlRecords);
			pageImpl.setAggregates(pageRequest.getAggregates());
		} 
		this.addPageAggregate(tlist,pageImpl,  pageQuery);
		return pageImpl;
	}
 
	private <E> List<E> findCurrentRecord(String query ,  Object parameters , final Class<E> clazz,PageQuery pageQuery ) {
		List<E> data = this.executeNativeQuery(query, parameters, clazz);
		//this.addGroupBy(data, pageQuery, clazz);		
		return data;
	}
	
	private <E> void addGroupBy(List<E> pageData ,PageQuery pageQuery,Class<E> clazz) {
		if (pageQuery.getCriteria() == null || CommonUtil.isNull(pageQuery.getCriteria().getGroupPropertyBy())
				|| CommonUtil.isNull(pageData)) {
    		return;
    	}
		String groupProperty = pageQuery.getCriteria().getGroupPropertyBy();
		QueryModel qm = pageQuery.getGroupPageQueryModel();
		Set gSet = new HashSet();
		for (E e : pageData) {
			Object value = YqBeanUtil.getPropertyValue(e, groupProperty);
 			gSet.add(CommonUtil.nvl(value, "")); 
		}
		String query = qm.getQuery()+" where "+CommonUtil.genInStrBySet("ifnull(a."+groupProperty+",'')", gSet)+" group by  a."+groupProperty;
	    List<E> list = this.executeNativeQuery(query, qm.getParameters(), clazz);
	    Map<Object,E> groupMap = new HashMap<Object,E>();
	    for (E e : list) {
	    	Object obj = YqBeanUtil.getPropertyValue(e, "pageMap");
	    	String cntStr = "";
	    	if (obj != null) {
	    		Map pageMap = (Map)obj ;
	    		cntStr = " <br> 共  "+pageMap.get(PageQuery.COUNT_COL_NAME)+" 条记录" ;
	    	}
	    	Object key = CommonUtil.nvl(YqBeanUtil.getPropertyValue(e, groupProperty),"");
	    	//YqBeanUtil.setProperty(e, "<font color='black'>"+ groupProperty, YqBeanUtil.getPropertyValue(e, groupProperty) +cntStr +"</font>" );
	    	YqBeanUtil.setProperty(e, groupProperty, "<font color='black' style=\"font-weight:bold;\">"+ YqBeanUtil.getPropertyValue(e, groupProperty) +"</font>"+cntStr  );
	    	groupMap.put(key, e);
	    }
	    Object pre = "";
	    for (int i=0; i<pageData.size(); i++) {
	    	E data = pageData.get(i);
	    	Object key = CommonUtil.nvl(YqBeanUtil.getPropertyValue(data, groupProperty),"");
	    	if (!key.equals(pre)) {
	    		pageData.add(i, groupMap.get(key));
	    	}
	    	pre = key;
	    }
	}
	
    private <E> void addPageAggregate(List<E> pageData,AggregatePageImpl pageImpl, PageQuery pageQuery) {
    	if (pageQuery.getCriteria() == null || CommonUtil.isNull(pageQuery.getCriteria().getAggregateCols())) {
    		return;
    	}
    	Iterator<AggregateCol> it = pageQuery.getCriteria().getAggregateCols().iterator();
    	if (CommonUtil.isNull(it) )  {
    		return;
    	}
    	while (it.hasNext()) {
			AggregateCol col = it.next();
			if (JPAUtils.isAggregateCols(col)) {
				BigDecimal  max = new BigDecimal("0");
				BigDecimal  min = new BigDecimal("0");
				if (CommonUtil.isNotNull(pageData)) {
					max = new BigDecimal(CommonUtil.nvl( YqBeanUtil.getPropertyValue(pageData.get(0), col.getProperty()),"0").toString());
					min = new BigDecimal(CommonUtil.nvl( YqBeanUtil.getPropertyValue(pageData.get(0), col.getProperty()),"0").toString());
				}			
				BigDecimal sum = new BigDecimal("0"); 
				for (E data : pageData ) {
		    		BigDecimal value = new BigDecimal(CommonUtil.nvl( YqBeanUtil.getPropertyValue(data, col.getProperty()),"0").toString());
		    		sum = JsUtil.calculate(sum, "+", value,4);
		    		if (CommonUtil.compare(value, max) > 0) {
		    			max = value;
		    		}
		    		if (CommonUtil.compare(value, min) < 0) {
		    			min = value;
		    		}
		    	}
				BigDecimal avg = JsUtil.calculate(sum, "/", new BigDecimal(pageData.size()),4);
				BigDecimal pValue = new BigDecimal("0");
				if (AggregateCol.CATEGORY_SUM.equals(col.getCategory())) {
					pValue = sum;
				} else if (AggregateCol.CATEGORY_MAX.equals(col.getCategory())) {
					pValue = max;
				} if (AggregateCol.CATEGORY_MIN.equals(col.getCategory())) {
					pValue = min;
				} if (AggregateCol.CATEGORY_AVERAGE.equals(col.getCategory())) {
					pValue = avg;
				}
				pageImpl.addAggregate(col.getProperty(), "p"+col.getCategory(), new Double(pValue.toString()));
			}
		}
    	
    }

	@Override
	public <E> List<E> executeNativeQuery(String query,String orderBy, Object parmObj,
			final Class<E> clazz) {
		jdbcTemplate.setDataSource(this.dataSource);
		jdbcTemplate.afterPropertiesSet();
		log.info("Query["+query+"]");
		if (CommonUtil.isNull(parmObj)) {
			return executeNativeQueryToModel(jdbcTemplate,query,null,clazz);
		} else	if (parmObj instanceof Object[])  {
			return executeNativeQueryToModel(jdbcTemplate,query,(Object[])parmObj,clazz);
		} else {
			
			if (  parmObj instanceof NativeCriteria ) {
				NativeCriteria criteria = (NativeCriteria) parmObj;
				String cause = criteria.genMainQueryCause();
				if (CommonUtil.isNotNull(cause)) {
					query = query+" "+cause+" ";
				}
			}
			QueryModel qm = JPAUtils.getSqlAndParmsMapBySql(query, parmObj);
			if (CommonUtil.isNotNull(orderBy)) {
				qm.setQuery(qm.getQuery()+" order by "+orderBy);
			}
			return executeNativeQueryToModel(jdbcTemplate,qm.getQuery(),qm.getParameters(),clazz);
		}
		
	}
	
	@Override
	public <E> List<E> executeNativeQuery(String query, Object parmObj,
			final Class<E> clazz) {
		return this.executeNativeQuery(query, null, parmObj, clazz);
	}
	
	private <E> List<E> executeNativeQueryToModel(JdbcTemplate currJdbcTemplate,String querySql , Object[] parmArray, final Class<E> clazz){
	     if (clazz.isInstance(new HashMap())) {
	    	 return this.executeNativeQueryToModelForMap( currJdbcTemplate,  querySql , parmArray, clazz);  
	     } else {
	    	 return this.executeNativeQueryToModelForEntity( currJdbcTemplate,  querySql , parmArray, clazz);
	     }
	}
	
	private <E> List<E> executeNativeQueryToModelForEntity(JdbcTemplate currJdbcTemplate,String querySql , Object[] parmArray, final Class<E> clazz){
		List<PropertyDefine> pdlist = YqBeanUtil.genEntityDefine(clazz); 
		final Map<String,String> colPropertyMap = new HashMap<String,String>();
		for (PropertyDefine pd : pdlist) {
			colPropertyMap.put(pd.getColumn().toUpperCase(), pd.getProperty());
		}
		final List<Object[]> propertiesAndColName = new LinkedList<Object[]>();
		final Set<String> tempColSet = new HashSet<String>();
	
		RowMapper<E> rowMapper = new RowMapper<E>() {
			public E mapRow(ResultSet rset, int row)
					throws SQLException {
				E modelOrView=null;
				Map<String,Object> pageMap = null;
				try {
					 modelOrView =  clazz.newInstance();
					 
				} catch (Exception e) {
					log.info(e.getStackTrace());
					e.printStackTrace();
				}
				try {
					//只有定义此特定的属性，将会将所有没有mapping modelOrView 属性的放入此map, key='列名'，值为sql 的返回值
					pageMap = (Map<String,Object>)PropertyUtils.getProperty(modelOrView, "pageMap");
					// tmpMap = (Map<String,Object>)YqBeanUtil.getPropertyValue(modelOrView, "tmpMap");
				} catch (Exception e) {
					
				}
			
				if (row == 0) {
					for (int i=0; i<rset.getMetaData().getColumnCount(); i++) {
						String colName = rset.getMetaData().getColumnLabel(i+1);
						String propertyName = colPropertyMap.get(colName.toUpperCase());
						//列名和属性名任意匹配
						if (CommonUtil.isNull(propertyName)) {
							propertyName = colName;
						}
						PropertyDescriptor property = CommonUtil.getBeanPropertyDescriptorByColumn(modelOrView, propertyName);
						Field field = CommonUtil.getFieldByColumn(modelOrView, propertyName);				
						if (property == null) {							
							if (pageMap != null) {
								log.info("col name " + colName+" not mapping to SqlToModelService properties,but it will be inputted pageMap .");
								pageMap.put(colName, rset.getObject(colName));
								tempColSet.add(colName);
							}	else {
								log.info("col name " + colName+" not mapping to SqlToModelService properties .");
							}
						} else {
							try {
							 CommonUtil.setBeanProperty(modelOrView, propertyName, getObjectFromRset(rset,property.getPropertyType().getName(),colName,field));
								propertiesAndColName.add(new Object[]{property,colName,propertyName,field});
							} catch (Exception e) {
								log.info(e.getStackTrace());
							}
						}
					}
				} else {
					for (Object[] pc : propertiesAndColName ) {
						try {
							PropertyDescriptor property = (PropertyDescriptor)pc[0];
							String colName =(String) pc[1];
							String propertyName = (String) pc[2];
							Field field = (Field) pc[3];
							if (colName != null) {
								CommonUtil.setBeanProperty(modelOrView, propertyName, getObjectFromRset(rset,property.getPropertyType().getName(),colName,field));
							}
						} catch (Exception e) {
							log.info(e.getStackTrace());
						}
					}
					if (!CommonUtil.isNull(tempColSet)) {
						Iterator<String> it = tempColSet.iterator();
						while (it.hasNext()) {
							String colName = it.next();
							pageMap.put(colName, rset.getObject(colName));
						}
					}					
				}
				return modelOrView;
			}
		};	
		List<E> list = currJdbcTemplate.query(this.replaceBankuhao(querySql), parmArray, rowMapper);
		return list;
	}
	private <E> List<E> executeNativeQueryToModelForMap(JdbcTemplate currJdbcTemplate,String querySql , Object[] parmArray, final Class<E> clazz){
 
		RowMapper<E> rowMapper = new RowMapper<E>() {
			public E mapRow(ResultSet rset, int row)
					throws SQLException {
				Map data = new HashMap();
				E modelOrView=(E)data;
				
					for (int i=0; i<rset.getMetaData().getColumnCount(); i++) {
						String colName = rset.getMetaData().getColumnLabel(i+1);
						String propertyName = colName;
						data.put(colName, rset.getObject(colName));
					}
				return modelOrView;
			}
		};	
		List<E> list = currJdbcTemplate.query(this.replaceBankuhao(querySql), parmArray, rowMapper);
		return list;
	}
 
	public <E> AggregatePageImpl<E> executeNativeQueryForPage(String query, String defaultSortBy, String groupBy, NativeCriteria criteria,HPageRequest pageRequest,Class<E> clazz) {
		return this.executeNativeQueryForPage( new PageQuery(query, defaultSortBy, groupBy, criteria,pageRequest), clazz);
	}
	
	@Override
	public <E> AggregatePageImpl<E> executeNativeQueryForPage(String query, String defaultSortBy, String groupBy, HPageCriteria criteria,Class<E> clazz) {
		HPageRequest pageRequest = null;
		if (criteria.isExportData()) { // 是导出， 不分页
			pageRequest = new HExportRequest(criteria);
		} else {
			pageRequest = new HPageRequest(criteria);
		} 
		
		if ( criteria instanceof HWorkFlowEntityPageCriteria ) {
			query = query + this.workFlowEntityQueryService.genAndIdIn((HWorkFlowEntityPageCriteria)criteria);
			HWorkFlowEntityPageCriteria wCriteria = (HWorkFlowEntityPageCriteria)criteria;
			if ( wCriteria.isOnlyCount() ) {
				PageQuery pq = new PageQuery(query, defaultSortBy, groupBy, criteria,pageRequest);
				int count = this.getCountByPageQuery(pq);
				List<E> tlist = new ArrayList<E>();
				AggregatePageImpl	pageImpl = new AggregatePageImpl(tlist ,pageRequest,count);
				return pageImpl;
			}
		}
		return this.executeNativeQueryForPage( new PageQuery(query, defaultSortBy, groupBy, criteria,pageRequest), clazz);
	}

	@Override
	public <E> List<E> executeNativeQuery(String query, String sortBy,String groupBy,
			NativeCriteria criteria, Class<E> clazz) {
		String sql = query;
		if (criteria != null) {
			sql = sql+criteria.genMainQueryCause(); // 如果要用属性定义，必须要存在where语句， 如果没有条件要写， 可以写where 1=1 
		} 
		if (CommonUtil.isNotNull(groupBy)) {
			sql = sql +" group by "+groupBy;
			if (CommonUtil.isNotNull(criteria)) {
				String havingCause = criteria.genHavingCause();
				if (CommonUtil.isNotNull(havingCause)) {
					sql = sql +" "+havingCause; //如果要用having条件，groupBy 必须包括having 1=1 ... 或其它自定义的条件
				}
			}
		}

		if (CommonUtil.isNotNull(sortBy)) {
			sql = sql+" order by  "+sortBy;
		}
		return this.executeNativeQuery(sql, null, clazz); // 参数已加入， 不用再传criteria
	}
	
	//mysql数据库表结构导出为excel
	@Override
	public HSSFWorkbook dbStructureToExcel(String dbName){
		jdbcTemplate.setDataSource(this.dataSource);
		jdbcTemplate.afterPropertiesSet();
		final List<Map<String, Object>> list = this.jdbcTemplate.queryForList("SHOW FULL TABLES FROM `" + dbName + "` WHERE table_type = 'BASE TABLE'");
		HSSFWorkbook workbook=new HSSFWorkbook();
		//填充表列表
		final HSSFSheet tableListSheet = workbook.createSheet("表列表");
		tableListSheet.setColumnWidth(0, 50 * 256);
		tableListSheet.setColumnWidth(1, 50 * 256);
		tableListSheet.setColumnWidth(2, 100 * 256);
		String[] tmp = {"表名","中文名称", "备注"};
		for (int i = 0; i < tmp.length; i++) {
			HSSFCellStyle style = tableListSheet.getWorkbook().createCellStyle();
			style.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
			//style.setFillPattern(FillPatternType.SOLID_FOREGROUND.);
			HSSFCell cell = getCell(tableListSheet, 0, i);
			cell.setCellStyle(style);
			cell.setCellValue(tmp[i]);
		}
		for(int i = 0; i < list.size(); i++){
			HSSFCell cell = getCell(tableListSheet, i+1, 0);
			cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
			cell.setCellFormula("HYPERLINK(\"#" + list.get(i).get("Tables_in_" + dbName).toString().toUpperCase() + "!A1\",\""+list.get(i).get("Tables_in_greentre").toString().toUpperCase()+"\")");
			HSSFCellStyle linkStyle = workbook.createCellStyle();
			HSSFFont cellFont = workbook.createFont();
			cellFont.setUnderline((byte) 1);
			cellFont.setColor(HSSFColor.BLUE.index);
			linkStyle.setFont(cellFont);
			cell.setCellStyle(linkStyle);
		}
		//填充表结构
		for(int i = 0; i < list.size(); i++){
			final HSSFSheet sheet = workbook.createSheet(list.get(i).get("Tables_in_" + dbName).toString().toUpperCase());
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
			sheet.setColumnWidth(0, 30 * 256);
			sheet.setColumnWidth(1, 15 * 256);
			sheet.setColumnWidth(2, 25 * 256);
			sheet.setColumnWidth(3, 30 * 256);
			RowMapper rowMapper = new RowMapper() {
				public Object mapRow(ResultSet rset, int row)
						throws SQLException {
					if (row == 0) {
						HSSFCellStyle style = sheet.getWorkbook().createCellStyle();
						HSSFFont font = sheet.getWorkbook().createFont();
						font.setFontName("Arial");
						font.setColor(HSSFColor.BLACK.index);
						font.setFontHeightInPoints((short) 16);
//						font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						style.setFont(font);
						//style.setVerticalAlignment(VerticalAlignment.CENTER);
						HSSFCell cell = getCell(sheet, row, 0);
						cell.setCellStyle(style);
						cell.setCellValue(sheet.getSheetName().toUpperCase());
						sheet.getRow(0).setHeightInPoints(50);
					}
					String[] tmp = {"Field","Type", "Comment"};
					if (row == 1) {
						HSSFCellStyle style = sheet.getWorkbook().createCellStyle();
						style.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
						//style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
						for (int i = 0; i < tmp.length; i++) {
							HSSFCell cell = getCell(sheet,row,i);
							cell.setCellStyle(style);
							cell.setCellValue(tmp[i]);
						}
						HSSFCell cell = getCell(sheet,row,3);
						cell.setCellStyle(style);
						cell.setCellValue("Remark");
					}
					for (int i = 0; i < tmp.length; i++) {
						Object value = rset.getObject(tmp[i]);
						if (value != null) {
							HSSFCell cell = getCell(sheet,row+1,i);
							cell.setCellValue(value.toString());
						}
					}
					return null;
				}
			};	
			this.jdbcTemplate.query("SHOW FULL FIELDS FROM `" + dbName + "`.`"+ list.get(i).get("Tables_in_" + dbName).toString() +"`", (Object[])null, rowMapper);
		}
		return workbook;
	}
	private HSSFCell getCell(HSSFSheet sheet, int row, int col) {
		HSSFRow sheetRow = sheet.getRow(row);
		if (sheetRow == null) {
			sheetRow = sheet.createRow(row);
		}
		HSSFCell cell = sheetRow.getCell(col);
		if (cell == null) {
			cell = sheetRow.createCell(col);
		}
		return cell;
	}
	
	@Override
	public List<Map<String, Object>> executeSqlNativeQuery(String query) {
		
		jdbcTemplate.setDataSource(this.dataSource);
		jdbcTemplate.afterPropertiesSet();
		
		return jdbcTemplate.queryForList(query);
	}
	
	@Override
	public int getCountByPageQuery(PageQuery pageQuery) {
		jdbcTemplate.setDataSource(this.dataSource);
		jdbcTemplate.afterPropertiesSet();
 		int ttlRecords = 0;
		QueryModel countQM = pageQuery.getCountPageQueryModel();
		Map<String, Object> mapGroup = jdbcTemplate.queryForMap(countQM.getQuery(),countQM.getParameters());
		ttlRecords = Integer.valueOf(mapGroup.get(PageQuery.COUNT_COL_NAME).toString());
		return ttlRecords;
	}
	
	  
	/**
	 * 替换 【】
	 * @param query
	 * @return
	 */ 
	private String replaceBankuhao(String query) {
		if (CommonUtil.isNull(query)) {
			return query;
		}
		String s = query.replace("[", "\"").replace("]", "\"");
		return s;
		
	}

	@Override
	public <E> E getSingleRecord(String query, Object parmObj, Class<E> clazz) {
		List<E> list = this.executeNativeQuery(query, parmObj, clazz);
		if (CommonUtil.isNotNull(list)) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public <E> List<E> executeNativeQuery(String query, String sortBy,
			String groupBy, String havingSql, NativeCriteria criteria, Class<E> clazz) {
		return executeNativeQuery(query, sortBy,
				groupBy, havingSql, criteria, clazz);
	}

}

