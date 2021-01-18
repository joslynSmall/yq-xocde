package com.yq.xcode.common.bean;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yq.xcode.common.model.YqJpaBaseModel;
import com.yq.xcode.web.ui.annotation.ColumnLable;
import com.yq.xcode.web.ui.annotation.CriteriaCol;
import com.yq.xcode.web.ui.annotation.Detail;
import com.yq.xcode.web.ui.annotation.EditCol;
import com.yq.xcode.web.ui.annotation.EntityDesc;
import com.yq.xcode.web.ui.annotation.GridCol;

@SuppressWarnings("serial")
@Entity
@Table(name = "YQ_IMPORT_SETUP")

@JsonIgnoreProperties(ignoreUnknown = true)
@EntityDesc(name = "对账导入设置", autoGenPage = true, editCols = 3, genService = false, genAction = true, genCriteria = true)
public class ImportSetup extends YqJpaBaseModel {

	@TableGenerator(name = "idGen", table = "ID_GENERATOR", pkColumnName = "ID_KEY", valueColumnName = "ID_VALUE", pkColumnValue = "ACT_IMPORT_SETUP_ID", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idGen")
	@Column(name = "ID")
	private Long id;
	/**
	 * vouch_number VARCHAR(80) comment '设置单号' 业务办理卡编号
	 */
	@ColumnLable(name = "设置单号")
	@Column(name = "vouch_number")
	@GridCol(lineNum = 1, isHyperlink = true)
	@CriteriaCol(lineNum = 1)
	@EditCol(lineNum = 1,mandatory = true)
	private String vouchNumber;
	/**
	 * status VARCHAR(40) comment '状态' 后面审核用
	 *  启用(ENABLE)/过期(EXPIRE)
	 */
	@ColumnLable(name = "状态")
	@Column(name = "status") 
	private String status;
	
	@ColumnLable(name = "备注")
	@Column(name = "remark") 
	private String remark;
	
	/**
	 * sheet_data_format VARCHAR(40) comment 'sheet数据格式' GRP - 分组，空是不分组数据
	 * 分开显示， 如果分组 ， 数据库直接配
	 */
	@ColumnLable(name = "sheet数据格式")
	@Column(name = "sheet_data_format") 
	private String sheetDataFormat;
	/**
	 * channel_category VARCHAR(40) comment '渠道类型' 静态文件定义
	 */
	@ColumnLable(name = "渠道类型")
	@Column(name = "channel_category")
	@GridCol(lineNum = 15)
	@CriteriaCol(lineNum = 15)
	@EditCol(lineNum = 15,mandatory = true)
	private String channelCategory;
	
	@ColumnLable(name = "导入对象")
	@Column(name = "import_entity") 
	@GridCol(lineNum = 20) 
	@EditCol(lineNum = 20,mandatory = true)
	private String importEntity;
	
	/**
	 * sheet_index int comment 'sheet 位置' 空读所有sheet
	 */
	@ColumnLable(name = "sheet名称")
	@Column(name = "sheet_Name")
	@GridCol(lineNum = 25)
	@EditCol(lineNum = 25)
	private String sheetName;
	
	/**
	 * 忽略sheet 名称， 模糊匹配
	 */
	@ColumnLable(name = "忽略sheet名称")
	@Column(name = "ingore_sheet_Name")
	@GridCol(lineNum = 25)
	@EditCol(lineNum = 25)
	private String ingoreSheetName;
	
	
	/**
	 * CSV 括号字符
	 */
	@ColumnLable(name = "括号字符")
	@Column(name = "csv_brackets")
	@GridCol(lineNum = 25)
	@EditCol(lineNum = 25)
	private String csvBrackets;
	
	/**
	 * start_row int comment '开始行'
	 * 
	 */
	@ColumnLable(name = "开始行")
	@Column(name = "start_row")
	@GridCol(lineNum = 30)
	@EditCol(lineNum = 30)
	private Integer startRow;
	
	/**
	 * 同时计算开始行
	 * M - dtl 中定义的列明必须和COL 号一直， 读取按定义的COL 
	 * N - dtl 中定义已名称为准， 如果名称和COL对不上， 调换列
	 * null - 已列为准， 必须输入
	 */
	@ColumnLable(name = "列读取方式")
	@Column(name = "col_read_type")
	@GridCol(lineNum = 35)
	@EditCol(lineNum = 35,mandatory = true)
	private String colReadType;
	
	
	/**
	 * group_property VARCHAR(40) comment '分组对应的属性明细'
	 * 
	 */
	@ColumnLable(name = "分组对应的属性")
	@Column(name = "group_property") 
	private String groupProperty;
 
	/**
	 * 返回cell , 如果是分组，  就返回分组的cell, 否则返回空
	 */
	@ColumnLable(name = "分组cell函数")
	@Column(name = "group_cell_function") 
	private String groupCellFunction; 
	/**
	 * is_group_end_function int comment '分组数据结束' function ， ？ 代表当前cell
	 */
	@ColumnLable(name = "数据结束函数")
	@Column(name = "is_group_end_function") 
	private String isGroupEndFunction;
	
	/**
	 * 指定的列都是空， 为结束
	 */
	@ColumnLable(name = "空列结束标记")
	@Column(name = "end_sign_col")
	@GridCol(lineNum = 52)
	@EditCol(lineNum = 52)
	private String endSignCol;
	
	@ColumnLable(name = "列数")
	@Column(name = "col_length")
	@GridCol(lineNum = 55)
	@EditCol(lineNum = 55,mandatory = true)
	private Integer colLength;
	
	@ColumnLable(name = "文件编码")
	@Column(name = "charset")
	@GridCol(lineNum = 55)
	@EditCol(lineNum = 55)
	private String charset;
	
	@ColumnLable(name = "关联渠道")
	@Column(name = "connect_channel")
	private String connectChannel;
	
	
	
	/**
	 * 导入对象
	 */
	@Transient
	private String entityClazz;
	
	/**
	 * 对象保存
	 */
	@Transient
	private String entityService;
	
	/**
	 * 状态
	 */
	@Transient
	private String statusDsp;
	
	/**
	 * 渠道文件
	 */
	@Transient
	private String channelFolder; 
	
	@Detail(masterProperty = "importSetupId")
	@Transient
	private List<ImportSetupDtl> dtls;
	
	
 	public Integer getColLength() {
		return colLength;
	}

	public void setColLength(Integer colLength) {
		this.colLength = colLength;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVouchNumber() {
		return vouchNumber;
	}

	public void setVouchNumber(String vouchNumber) {
		this.vouchNumber = vouchNumber;
	}
	
	

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSheetDataFormat() {
		return sheetDataFormat;
	}

	public void setSheetDataFormat(String sheetDataFormat) {
		this.sheetDataFormat = sheetDataFormat;
	}

	public String getChannelCategory() {
		return channelCategory;
	}

	public void setChannelCategory(String channelCategory) {
		this.channelCategory = channelCategory;
	}

 

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public Integer getStartRow() {
		return startRow;
	}

	public void setStartRow(Integer startRow) {
		this.startRow = startRow;
	}

	public String getGroupProperty() {
		return groupProperty;
	}

	public void setGroupProperty(String groupProperty) {
		this.groupProperty = groupProperty;
	}

 
	public String getGroupCellFunction() {
		return groupCellFunction;
	}

	public void setGroupCellFunction(String groupCellFunction) {
		this.groupCellFunction = groupCellFunction;
	}

	public String getIsGroupEndFunction() {
		return isGroupEndFunction;
	}

	public void setIsGroupEndFunction(String isGroupEndFunction) {
		this.isGroupEndFunction = isGroupEndFunction;
	}

	public List<ImportSetupDtl> getDtls() {
		return dtls;
	}

	public void setDtls(List<ImportSetupDtl> dtls) {
		this.dtls = dtls;
	}

	public String getImportEntity() {
		return importEntity;
	}

	public void setImportEntity(String importEntity) {
		this.importEntity = importEntity;
	}

 

	public String getColReadType() {
		return colReadType;
	}

	public void setColReadType(String colReadType) {
		this.colReadType = colReadType;
	}

	public String getEntityClazz() {
		return entityClazz;
	}

	public void setEntityClazz(String entityClazz) {
		this.entityClazz = entityClazz;
	}

	public String getEntityService() {
		return entityService;
	}

	public void setEntityService(String entityService) {
		this.entityService = entityService;
	}

	public String getChannelFolder() {
		return channelFolder;
	}

	public void setChannelFolder(String channelFolder) {
		this.channelFolder = channelFolder;
	}

	public String getEndSignCol() {
		return endSignCol;
	}

	public void setEndSignCol(String endSignCol) {
		this.endSignCol = endSignCol;
	}

	public String getIngoreSheetName() {
		return ingoreSheetName;
	}

	public void setIngoreSheetName(String ingoreSheetName) {
		this.ingoreSheetName = ingoreSheetName;
	}

	public String getCsvBrackets() {
		return csvBrackets;
	}

	public void setCsvBrackets(String csvBrackets) {
		this.csvBrackets = csvBrackets;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStatusDsp() {
		if(this.status == null){
			return "启用";
		}
		if("ENABLE".equals(this.status)){
			return "启用";
		}
		if("EXPIRE".equals(this.status)){
			return "过期";
		}
		return status;
	}

	public void setStatusDsp(String statusDsp) {
		this.statusDsp = statusDsp;
	}

	public String getConnectChannel() {
		return connectChannel;
	}

	public void setConnectChannel(String connectChannel) {
		this.connectChannel = connectChannel;
	}  
	
}