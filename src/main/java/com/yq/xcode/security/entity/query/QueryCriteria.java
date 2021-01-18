package com.yq.xcode.security.entity.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class QueryCriteria  implements Serializable {

	private boolean paginate = true;
	
	private int totalRecords;
	
	private int displayStart = 0;
	
	private int displayLength = 50;
	
	private List<SortBy> sortByColumns = new ArrayList<SortBy>();
	
	private SumCol[] sumCols = null;
	
	private String defaultSort;
	
	private String columnNames;

	public SumCol[] getSumCols() {
		return sumCols;
	}

	public void setSumCols(SumCol[] sumCols) {
		this.sumCols = sumCols;
	}

	public boolean isPaginate() {
		return paginate;
	}

	public void setPaginate(boolean paginate) {
		this.paginate = paginate;
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public int getDisplayStart() {
		return displayStart;
	}

	public void setDisplayStart(int displayStart) {
		this.displayStart = displayStart;
	}

	public int getDisplayLength() {
		return displayLength;
	}

	public void setDisplayLength(int displayLength) {
		this.displayLength = displayLength;
	}

	public int getPageCount() {
		if(displayLength == 0 || !paginate) {
			return 0;
		}
		return totalRecords/displayLength+((totalRecords%displayLength==0)?0:1);
	}
	
	public int getCurrentPage() {
		if(displayLength == 0 || !paginate) {
			return 0;
		}
		return displayStart/displayLength + 1;
	}
	
	public List<SortBy> getSortByColumns() {
		return sortByColumns;
	}

	public void setSortByColumns(List<SortBy> sortByColumns) {
		this.sortByColumns = sortByColumns;
	}
	
	public SortBy getSortByColumn(String name) {
		for(SortBy s : sortByColumns) {
			if(s.getName().equals(name)) {
				return s;
			}
		}
		return null;
	}
	
	public void addOrUpdateSortByColumn(SortBy s) {
		for(int i = 0;i < sortByColumns.size();i++) {
			SortBy elem = sortByColumns.get(i);
			if(elem.getName().equals(s.getName())) {
				sortByColumns.set(i, s);
				return;
			}
		}
		sortByColumns.add(s);
	}
	
	public String getSortByExpression() {
		if(sortByColumns.isEmpty()) {
			return null;
		}
		StringBuffer buffer = new StringBuffer();
		for(SortBy s : sortByColumns) {
			buffer.append(s.getName()).append(" ").append(s.getDirection().toString()).append(",");
		}
		buffer.delete(buffer.length() -1, buffer.length());
		return buffer.toString();
	}

	public int getTotalPages() {
		return totalRecords/displayLength +((totalRecords%displayLength==0?0:1));
	}

	public String getDefaultSort() {
		return defaultSort;
	}

	public void setDefaultSort(String defaultSort) {
		this.defaultSort = defaultSort;
	}

	public String getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(String columnNames) {
		this.columnNames = columnNames;
	}
	
	
	
}
