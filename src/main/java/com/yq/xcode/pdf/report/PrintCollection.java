package com.yq.xcode.pdf.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

//import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;


public class PrintCollection implements Serializable {

//	private static final long serialVersionUID = -3978741268499128416L;
//
//	private HashMap<String, Object> map = null;
//	private ArrayList<Map<String, Object>> list = null;
//
//	public PrintCollection() {
//	}
//
//	public void addData(String name, Object value) {
//		getMap().put(name, value);
//	}
//
//	public void addData(String name, String value) {
//		getMap().put(name, value);
//	}
//
//	public void addData(String name, PrintCollection col) {
//		getMap().put(name, col);
//	}
//
//	public Object getData(String name) {
//		return getMap().get(name);
//	}
//
//	public JRMapCollectionDataSource toCollectionDataSource() {
//		Collection<Map<String, ?>> list = new ArrayList<Map<String, ?>>();
//		if (getList().size() > 0) {
//			for (Map<String, Object> map : getList()) {
//				list.add(transform(map));
//			}
//		}
//		if (getMap().size() > 0) {
//			list.add(transform(getMap()));
//		}
//
//		return new JRMapCollectionDataSource(list);
//	}
//
//	private HashMap<String, Object> transform(Map<String, Object> source) {
//		HashMap<String, Object> dest = new HashMap<String, Object>();
//		Set<String> set = source.keySet();
//		Iterator<String> it = set.iterator();
//		while (it.hasNext()) {
//			String key = (String) it.next();
//			if (source.get(key) instanceof PrintCollection) {
//				PrintCollection col = (PrintCollection) source.get(key);
//				dest.put(key, col.toCollectionDataSource());
//			} else {
//				dest.put(key, source.get(key));
//			}
//		}
//		return dest;
//	}
//
//	public void addRow(ReportRow row) {
//		getList().add(row.getValues());
//	}
//
//	public void merge(PrintCollection pc) {
//		if (pc != null && pc.getMap() != null) {
//			getMap().putAll(pc.getMap());
//		}
//	}
//
//	public List<Map<String, Object>> getList() {
//		if (list == null) {
//			list = new ArrayList<Map<String, Object>>();
//		}
//		return list;
//	}
//
//	Map<String, Object> getMap() {
//		if (map == null) {
//			map = new HashMap<String, Object>();
//		}
//		return map;
//	}
}
