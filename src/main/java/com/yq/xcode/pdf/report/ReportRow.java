package com.yq.xcode.pdf.report;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ReportRow implements Serializable {

	private static final long serialVersionUID = -1051888921026444883L;

	private HashMap<String, Object> map = new HashMap<String, Object>();

	public ReportRow() {
	}

	public ReportRow(String name, String val) {
		map.put(name, val);
	}

	public void addData(String name, String val) {
		map.put(name, val);
	}

	public void addData(String name, PrintCollection val) {
		map.put(name, val);
	}

	Map<String, Object> getValues() {
		return map;
	}

	public void addData(String name, int val) {
		map.put(name, val);
	}

	public void addData(String name, BigDecimal bigDecimal) {
		map.put(name, bigDecimal);
	}
}
