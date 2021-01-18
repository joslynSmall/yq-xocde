package com.yq.xcode.common.springdata;

import java.util.Iterator;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import com.yq.xcode.common.bean.AggregateCol;
import com.yq.xcode.common.bean.QueryModel;
import com.yq.xcode.common.bean.SumCol;
import com.yq.xcode.common.criteria.NativeCriteria;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.JPAUtils;


public class PageQuery {
	private String query;
	private String defaultSortBy;
	private NativeCriteria criteria;
	private HPageRequest pageRequest;
	public static final String COUNT_COL_NAME = "aacnt";
	private String groupBy;
	
	public String getGroupBy() {
		return groupBy;
	}
	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}
	
	private PageQuery() {		
	}
	public PageQuery(String query, String defaultSortBy,String groupBy, NativeCriteria criteria,HPageRequest pageRequest) {
		if (criteria != null) {
			this.query = query+criteria.genMainQueryCause(); // 如果要用属性定义，必须要存在where语句， 如果没有条件要写， 可以写where 1=1 
		} else {
			this.query = query;
		}	
		if (CommonUtil.isNotNull(groupBy)) {
			this.query = this.query +" group by "+groupBy;
			String havingCause = criteria.genHavingCause();
			if (CommonUtil.isNotNull(havingCause)) {
				this.query = this.query+" having "+havingCause; //如果要用having条件，groupBy 必须包括having 1=1 ... 或其它自定义的条件
			}
		}
		
		this.defaultSortBy = defaultSortBy;
		this.criteria = criteria;	
		this.pageRequest = pageRequest; 
	}
	
	
	public QueryModel getCurrPageQueryModelForMysql() {
		int firstResult = pageRequest.getPageSize() * (pageRequest.getPageNumber() - 1);
		if(firstResult < 0) {
			firstResult = 0;
		}
		int displayLength = pageRequest.getPageSize();
		//String sortBy = this.pageRequest.getSort() == null?"":(this.pageRequest.getSort().toString()).replace(":", " ");
		String sortBy = this.getSortBy(pageRequest);
		if (CommonUtil.isNull(sortBy) && !CommonUtil.isNull(this.defaultSortBy)) {
			sortBy = this.defaultSortBy;
		}
//		if (CommonUtil.isNotNull(this.criteria.getGroupPropertyBy())) {
//			sortBy = this.criteria.getGroupPropertyBy();
//		}
		if (CommonUtil.isNull(sortBy)) {
			sortBy = " ";
		} else {
			sortBy = " order by "+sortBy;
		}
		String query = this.query+" "+sortBy+" " +" limit  "+ firstResult+","+displayLength;
		return JPAUtils.getSqlAndParmsMapBySql(query, this.criteria);
	}
	
	private String getSortBy(HPageRequest pageRequest) {
		Sort sort = pageRequest.getSort();
		if (sort != null) {
			String str = "";
			Iterator <Order> iterator = sort.iterator();
			while (iterator.hasNext()) {
				Order od = iterator.next();
				str =", "+ od.toString()+" "+str;
			}
			if ( str.contains("pageMap.") ) {
				str = str.replace("pageMap.","");
			}
			if (CommonUtil.isNotNull(str)) {
				return str.substring(1).replace(":", " ");
			} 
		}
		return null;
	}
	
	public QueryModel getFullQueryModelForMysql() {

		//String sortBy = this.pageRequest.getSort() == null?"":(this.pageRequest.getSort().toString()).replace(":", " ");
		String sortBy = this.getSortBy(this.pageRequest);
		if (CommonUtil.isNull(sortBy) && !CommonUtil.isNull(this.defaultSortBy)) {
			sortBy = this.defaultSortBy;
		}
		if (CommonUtil.isNull(sortBy)) {
			sortBy = " ";
		} else {
			sortBy = " order by "+sortBy;
		}
		String query = this.query+" "+sortBy;
		return JPAUtils.getSqlAndParmsMapBySql(query, this.criteria);
	}
	
	public QueryModel getCurrPageQueryModelForSqlServer() {
		int firstResult = pageRequest.getPageSize() * (pageRequest.getPageNumber() - 1);
		if(firstResult < 0) {
			firstResult = 0;
		}
		int displayLength = pageRequest.getPageSize();
		//String sortBy = (this.pageRequest.getSort() == null || this.pageRequest.getSort().isUnsorted()) ?"":(this.pageRequest.getSort().toString()).replace(":", " ");
		String sortBy = this.getSortBy(this.pageRequest);
		if (CommonUtil.isNull(sortBy) && !CommonUtil.isNull(this.defaultSortBy)) {
			sortBy = this.defaultSortBy;
		}
		if (CommonUtil.isNull(sortBy)) {
			sortBy = " ";
		} else {
			sortBy = " order by "+sortBy;
		}
		int maxTop = firstResult+displayLength;
		//String query =  +" "+sortBy+" " +" limit  "+ firstResult+","+displayLength;
		
		String query = " select * from ( select row_number()over(order by tempcolumn)temprownumber,*  from ( select top "+maxTop+" tempcolumn=0,* from ( "
				+ this.query+" "+sortBy+" " 
				+ " ) b ) a	) c where c.temprownumber > "+firstResult;
				
		return JPAUtils.getSqlAndParmsMapBySql(query, this.criteria);
	}
	
	public QueryModel getCountPageQueryModel() {
		SumCol[] sumColArr = null; // 可能要在pageRequest中设置需要合计的列， 根据需要， 可能需要改SumCol的模型结构
		String sumCols = " ";
		if (this.criteria != null && !CommonUtil.isNull(this.criteria.getAggregateCols())) {
			Iterator<AggregateCol> it = this.criteria.getAggregateCols().iterator();
			while (it.hasNext()) {
				AggregateCol col = it.next();
				sumCols = sumCols + col.genSelectCol();
			}
		}
		String query = " select count(1) "+COUNT_COL_NAME+" " +sumCols+" from ("+this.query+" ) a" ;
		return JPAUtils.getSqlAndParmsMapBySql(query, this.criteria);
	}
	
	public QueryModel getGroupPageQueryModel() {
		SumCol[] sumColArr = null; // 可能要在pageRequest中设置需要合计的列， 根据需要， 可能需要改SumCol的模型结构
		String sumCols = " ";
		if (this.criteria == null || CommonUtil.isNull(this.criteria.getGroupPropertyBy())) {
			return null;
		}
		if (this.criteria != null && !CommonUtil.isNull(this.criteria.getAggregateCols())) {
			Iterator<AggregateCol> it = this.criteria.getAggregateCols().iterator();
			while (it.hasNext()) {
				AggregateCol col = it.next();
				sumCols = sumCols + col.genGroupCol();
			}
		}
		String query = " select "+this.criteria.getGroupPropertyBy()+", count(1) "+COUNT_COL_NAME+" " +sumCols+" from ("+this.query+" ) a "	;
		return JPAUtils.getSqlAndParmsMapBySql(query, this.criteria);
	}

	public NativeCriteria getCriteria() {
		return criteria;
	}
	public HPageRequest getPageRequest() {
		return pageRequest;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	
	
	
}
