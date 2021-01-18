package com.yq.xcode.common.bean;

import java.util.ArrayList;
import java.util.List;

import com.yq.xcode.common.base.XBaseView;
import com.yq.xcode.common.utils.CommonUtil;




public class CsvSheet  extends XBaseView{
	
	private List<CsvRow> rows;

	public List<CsvRow> getRows() {
		return rows;
	}

	public void setRows(List<CsvRow> rows) {
		this.rows = rows;
	}
	
	public void addRow( CsvRow row ) {
		if (rows == null) {
			rows = new ArrayList<CsvRow>();
		}
		rows.add(row);
	}
	
	public Integer size() {
		if (CommonUtil.isNotNull(this.rows)) {
			return this.rows.size();
		}
		return 0;
	}
	 
	

}
