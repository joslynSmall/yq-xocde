package com.yq.xcode.common.bean;

import com.yq.xcode.common.base.XBaseView;

public class CsvRow  extends XBaseView{
	
	private Object[] cellValues;
	private int length;
	public CsvRow() {}
	
	public CsvRow(int length) {
		this.length = length;
		this.cellValues = new Object[this.length];
	}
	
	public int getLength() {
		return length;
	}
	
 	public Object[] getCellValues() {
		return cellValues;
	}

	public void setCellValues(Object[] cellValues) {
		this.cellValues = cellValues;
	}

	public void addCellValue(int i, Object value) {
 		cellValues[i] = value;
	}
	
	public Object getCellValue(int i) {
		return cellValues[i];
	}

	public void clear() {
		if (cellValues != null) {
			for (int i=0; i<this.cellValues.length; i++) {
				this.cellValues[i] = null;
			}
		}
		
	}
 

}
