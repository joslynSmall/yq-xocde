package com.yq.xcode.common.springdata;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class HExportRequest extends HPageRequest {

	private static final long serialVersionUID = 3439266824117626696L;
	
	private List<HExportColumn> columns;
	
	public HExportRequest(int page, int size, Direction direction,
			String[] properties) {
		super(page<=0?1:page, size<=0?10:size, direction, properties);
	}
	
	public HExportRequest(int page, int size, Sort sort,int total) {
		super(page<=0?1:page, size<=0?10:size, sort, total);
	}
	
 	public HExportRequest(HPageCriteria criteria) {
		super(criteria);
	}

	public List<HExportColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<HExportColumn> columns) {
		this.columns = columns;
	}
}
