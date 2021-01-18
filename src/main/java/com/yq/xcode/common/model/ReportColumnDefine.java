package com.yq.xcode.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yq.xcode.common.bean.DataTypeConstants;
import com.yq.xcode.common.bean.SelectItem;
import com.yq.xcode.common.utils.ReportUtil;
import com.yq.xcode.web.ui.annotation.ColumnLable;
import com.yq.xcode.web.ui.annotation.EditCol;
import com.yq.xcode.web.ui.annotation.EntityDesc;
import com.yq.xcode.web.ui.annotation.GridCol;

@SuppressWarnings("serial")
@Entity
@Table(name = "YQ_REPORT_COLUMN_DEFINE")

@JsonIgnoreProperties(ignoreUnknown=true)
@EntityDesc(name="基础资料 >> 报表定义", autoGenPage=true, editCols = 3, genService=false, genAction=false, genCriteria=false) 
public class ReportColumnDefine extends YqJpaBaseModel {

	@TableGenerator(name = "idGen", table = "ID_GENERATOR", pkColumnName = "ID_KEY", valueColumnName = "ID_VALUE", pkColumnValue = "REPORT_ID", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idGen")
	@Column(name = "ID")
	private Long id;

	@ColumnLable(name = "主表ID")
	@Column(name = "REPORT_DEFINE_ID")
	private Long reportDefineId;
	
	@EditCol(lineNum=2,mandatory=true ) @GridCol 
	@ColumnLable(name = "显示顺序")
	@Column(name = "DISPLAY_ORDER")
	private int displayOrder = 1;
	
	@EditCol(lineNum=2,mandatory=true ) @GridCol (isHyperlink=true)
	@ColumnLable(name = "属性名称")
	@Column(name = "PROPERTY_NAME")
	private String propertyName;
	
	@ColumnLable(name = "参数类型")
	@Column(name = "CATEGORY")
	private String category;

	@EditCol(lineNum=3  ) @GridCol
	@ColumnLable(name = "数据库列")
	@Column(name = "COL_NAME")
	private String colName;

	@EditCol(lineNum=5  ) @GridCol
	@ColumnLable(name = "报表列")
	@Column(name = "SHOW_COL")
	private boolean showCol = true;
	
	@EditCol(lineNum=7 ) @GridCol
	@ColumnLable(name = "报表标题")
	@Column(name = "COL_LABLE")
	private String colLable;
	
	@EditCol(lineNum=8 ) @GridCol
	@ColumnLable(name = "列宽")
	@Column(name = "width")
	private String width;
	
	@EditCol(lineNum=9  ) @GridCol
	@ColumnLable(name = "列合计")
	@Column(name = "aggregate_Col")
	private boolean aggregateCol = false; //
	
	/**
	 * 分组字段， 可选择分组, 选择列表用
	 */
	@EditCol(lineNum=10  ) @GridCol
	@ColumnLable(name = "可分组统计")
	@Column(name = "CAN_GROUP")
	private boolean canGroup = false;
	
 	@EditCol(lineNum=11  ) @GridCol
	@ColumnLable(name = "参数列")
	@Column(name = "PARAMETER_COL")
	private boolean parameterCol = true;
 	
 	@EditCol(lineNum=11  ) @GridCol
	@ColumnLable(name = "参数列非空")
	@Column(name = "PARAMETER_MANDATORY")
	private Boolean parameterMandatory = false;
	/**
	 * 如果参数标题为空， 就用列标题
	 */
 	@EditCol(lineNum=12  ) @GridCol
	@ColumnLable(name = "参数标题")
	@Column(name = "PARAMETER_LABLE")
	private String parameterLable;
 	
	/**
	 * 比较类型// C - 字符，D - 日期 , N - 数字 B - boolean
	 */ 
	@ColumnLable(name = "数据类型")
	@Column(name = "compare_Type")
	private String compareType; 
 	 
	@ColumnLable(name = "逻辑运算符")
	@Column(name = "operator")
	private String operator; // 逻辑运算符，LIKE - 包含， = - 等于 ... 直接写逻辑运算符
 
 	@EditCol(lineNum=17 ) @GridCol
	@ColumnLable(name = "控件")
	@Column(name = "TAG_KEY")
	private String tagKey;

 	 
	@ColumnLable(name = "控件数据")
	@Column(name = "LIST_CATEGORY")
	private String listCategory;

	@Transient
	private String parameterValue; // 参数值

	@EditCol(lineNum=21  ) @GridCol
	@ColumnLable(name = "默认值")
	@Column(name = "default_Value")
	private String defaultValue; //default value


	/**
	 * 参数值的预留处理,例如前匹配可以写 DataTypeConstants.PLACE_HOLDER,'%') 参数用
	 */ 
	@ColumnLable(name = "特殊查询条件")
	@Column(name = "place_Holder")
	private String placeHolder; //


	@EditCol(lineNum=24 ) @GridCol
	@ColumnLable(name = "聚组条件")
	@Column(name = "having_Cause")
	private boolean havingCause = false;
	
	@ColumnLable(name = "分组字段中需要忽略的列")
	@Column(name = "IGNORE_GROUPCOL")
	private String ignoreGroupCol;

	@Transient
	private String listCategoryDsp;
	
 
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getReportDefineId() {
		return reportDefineId;
	}

	public void setReportDefineId(Long reportDefineId) {
		this.reportDefineId = reportDefineId;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public boolean isShowCol() {
		return showCol;
	}

	public void setShowCol(boolean showCol) {
		this.showCol = showCol;
	}

	public String getColLable() {
		return colLable;
	}

	public void setColLable(String colLable) {
		this.colLable = colLable;
	}

	public boolean isParameterCol() {
		return parameterCol;
	}

	public void setParameterCol(boolean parameterCol) {
		this.parameterCol = parameterCol;
	}

	public String getParameterLable() {
		return parameterLable;
	}

	public void setParameterLable(String parameterLable) {
		this.parameterLable = parameterLable;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getTagKey() {
		return tagKey;
	}

	public void setTagKey(String tagKey) {
		this.tagKey = tagKey;
	}

	public String getListCategory() {
		return listCategory;
	}

	public void setListCategory(String listCategory) {
		this.listCategory = listCategory;
	}

	public String getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getCompareType() {
		return compareType;
	}

	public void setCompareType(String compareType) {
		this.compareType = compareType;
	}

	public String getPlaceHolder() {
		return placeHolder;
	}

	public void setPlaceHolder(String placeHolder) {
		this.placeHolder = placeHolder;
	}

	public boolean isAggregateCol() {
		return aggregateCol;
	}

	public void setAggregateCol(boolean aggregateCol) {
		this.aggregateCol = aggregateCol;
	}

	public boolean isHavingCause() {
		return havingCause;
	}

	public void setHavingCause(boolean havingCause) {
		this.havingCause = havingCause;
	}

	public boolean isCanGroup() {
		return canGroup;
	}

	public void setCanGroup(boolean canGroup) {
		this.canGroup = canGroup;
	}
 	
	public Boolean getParameterMandatory() {
		return parameterMandatory;
	}

	public void setParameterMandatory(Boolean parameterMandatory) {
		this.parameterMandatory = parameterMandatory;
	}

 
	public String getIgnoreGroupCol() {
		return ignoreGroupCol;
	}

	public void setIgnoreGroupCol(String ignoreGroupCol) {
		this.ignoreGroupCol = ignoreGroupCol;
	}

 
	@Transient  
	public String getOperatorDsp() {
		for (SelectItem si : DataTypeConstants.OPERATION_RELATION) {
			if (si.getItemKey().equals(this.operator)) {
				return si.getItemName();
			}
		}
		return null;		
	}
	@Transient
	public String getTagKeyDsp() {
		for (SelectItem si : ReportUtil.getInputTags()) {
			if (si.getItemKey().equals(this.tagKey)) {
				return si.getItemName();
			}
		}
		return null;		
	}
//	@Transient
//	public String getListCategoryDsp() {
//		SelectItemDefine sid = SelectItemDefineConstants.SELECT_ITEM_DEFINES.get(this.listCategory);
//		if (sid != null) {
//			return sid.getName();
//		}
//		LookupCodeCategory cate = LookupCodeConstants.getCategoryByCategoryCode(this.listCategory);
//		if (cate != null) {
//			return cate.getCategoryName();
//		}
//		return null;		
//	}

	
	
	public String getCategory() {
		return category;
	}

	public String getListCategoryDsp() {
		return listCategoryDsp;
	}

	public void setListCategoryDsp(String listCategoryDsp) {
		this.listCategoryDsp = listCategoryDsp;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	
	
	

	
	
 
}