package com.yq.xcode.common.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yq.xcode.common.bean.WorkFlowEntityIntf;
import com.yq.xcode.common.bean.WorkFlowSpecRole;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.DateUtil;
import com.yq.xcode.web.ui.annotation.ColumnLable;
import com.yq.xcode.web.ui.annotation.CriteriaCol;
import com.yq.xcode.web.ui.annotation.EditCol;
import com.yq.xcode.web.ui.annotation.EntityDesc;
import com.yq.xcode.web.ui.annotation.GridCol;
/**
 * 
 * @author jettie
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "yq_overtime_note") 
@JsonIgnoreProperties(ignoreUnknown = true)
@EntityDesc(name = "加班单", autoGenPage = true, editCols = 3, genService = true, genAction = true, genCriteria = true)
public class OvertimeNote extends YqJpaBaseModel implements WorkFlowEntityIntf  {

	@TableGenerator(name = "idGen", table = "ID_GENERATOR", pkColumnName = "ID_KEY", valueColumnName = "ID_VALUE", pkColumnValue = "YQ_OVERTIME_NOTE_ID", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idGen")
	@Column(name = "ID")
	private Long id;
	/**
	 * overtime_number VARCHAR(80) comment '加班单号'
	 */
	@ColumnLable(name = "加班单号")
	@Column(name = "vouch_number")
	@GridCol(lineNum = 1, isHyperlink = true)
	@CriteriaCol(lineNum = 1)
	@EditCol(lineNum = 1)
	private String vouchNumber; 
	
  
	/**
	 * status VARCHAR(40) comment '状态'
	 */
	@ColumnLable(name = "状态")
	@Column(name = "status")
	@GridCol(lineNum = 2)
	@EditCol(lineNum = 2, listCategory = "WST")
	private String status;
 
	/**
	 * work_flow_id int comment '流程ID'
	 */
	@ColumnLable(name = "流程ID")
	@Column(name = "work_flow_id")
	private Long workFlowId;
	/**
	 * chain_id int comment '客户ID'
	 */
	@ColumnLable(name = "客户ID")
	@Column(name = "chain_id")
	@GridCol(lineNum = 3)
	@EditCol(lineNum = 3)
	private Long chainId;
	
  	
	/**
	 * overtime_from datetime comment '加班开始时间'
	 */
	@ColumnLable(name = "加班开始日期")
	@Column(name = "overtime_from")
	@GridCol(lineNum = 4)
	@CriteriaCol(lineNum = 4)
	@EditCol(lineNum = 4)
	private Date overtimeFrom;
	/**
	 * overtime_to datetime comment '加班结束时间'
	 */
	@ColumnLable(name = "加班结束日期")
	@Column(name = "overtime_to")
	@GridCol(lineNum = 5)
	@EditCol(lineNum = 5)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date overtimeTo;
	
	/**
	 * 每天加班开始时间
	 */
	@ColumnLable(name = "加班开始时间")
	@Column(name = "day_time_from")
	@GridCol(lineNum = 4)
	@CriteriaCol(lineNum = 4)
	@EditCol(lineNum = 4)
	private String dayTimeFrom;
	/**
	 * 每天加班结束时间
	 */
	@ColumnLable(name = "加班结束日期")
	@Column(name = "day_time_to")
	@GridCol(lineNum = 5)
	@EditCol(lineNum = 5)
	private String dayTimeTo;
	/**
	 * work_note VARCHAR(800) comment '作业内容'
	 */
	@ColumnLable(name = "作业内容")
	@Column(name = "work_note")
	@GridCol(lineNum = 6) 
	private String workNote;
	/**
	 * work_persons int comment '作业人数'
	 */
	@ColumnLable(name = "作业人数")
	@Column(name = "work_persons")
	@GridCol(lineNum = 7)
	@EditCol(lineNum = 7)
	private Integer workPersons;
	/**
	 * work_leader_name VARCHAR(80) comment '负责人'
	 */
	@ColumnLable(name = "员工出入证姓名")
	@Column(name = "TEMPORARYPASS_NAME")
	@GridCol(lineNum = 8)
	@EditCol(lineNum = 8)
	private String temporaryPassName;
	/**
	 * work_leader_mobile VARCHAR(40) comment '负责人电话'
	 */
	@ColumnLable(name = "员工出入证编号")
	@Column(name = "TEMPORARYPASS_CODE")
	@GridCol(lineNum = 9)
	@EditCol(lineNum = 9)
	private String temporaryPassCode;
	/**
	 * work_leader_name VARCHAR(80) comment '负责人'
	 */
	@ColumnLable(name = "负责人", mandatory = true, colLength = 4)
	@Column(name = "work_leader_name")
	@GridCol(lineNum = 8)
	@EditCol(lineNum = 8)
	private String workLeaderName;
	/**
	 * work_leader_mobile VARCHAR(40) comment '负责人电话'
	 */
	@ColumnLable(name = "负责人电话")
	@Column(name = "work_leader_mobile")
	@GridCol(lineNum = 9)
	@EditCol(lineNum = 9)
	private String workLeaderMobile;
	
	@ColumnLable(name = "二维码图片路径")
	@Column(name = "qrcode")
	private String qrcode;
	
	
	@Transient  
	private String chainCode;
	@Transient
	private String chainName;
	
	@Transient
	private String chainManager;
	
	@Transient
	private String chainManagerMobile;
	@Transient
	private String documentNumber;
	@Transient
	private List<LookupCode> enableActions;

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

 
	public Long getWorkFlowId() {
		return workFlowId;
	}

	public void setWorkFlowId(Long workFlowId) {
		this.workFlowId = workFlowId;
	}

	public Long getChainId() {
		return chainId;
	}

	public void setChainId(Long chainId) {
		this.chainId = chainId;
	}

	public Date getOvertimeFrom() {
		return overtimeFrom;
	}
	public String getOvertimeFromFormat() {
		return DateUtil.convertDate2String(overtimeFrom);
	}
	public String getLastUpdateTimeFormat() {
		return DateUtil.convertDate2String(this.getLastUpdateTime());
	}
	public void setOvertimeFrom(Date overtimeFrom) {
		this.overtimeFrom = overtimeFrom;
	}

	public Date getOvertimeTo() {
		return overtimeTo;
	}
	public String getOvertimeToFormat() {
		return DateUtil.convertDate2String(overtimeTo);
	}
	public void setOvertimeTo(Date overtimeTo) {
		this.overtimeTo = overtimeTo;
	}

	public String getWorkNote() {
		return workNote;
	}

	public void setWorkNote(String workNote) {
		this.workNote = workNote;
	}

	public Integer getWorkPersons() {
		return workPersons;
	}

	public void setWorkPersons(Integer workPersons) {
		this.workPersons = workPersons;
	}

	public String getWorkLeaderName() {
		return workLeaderName;
	}

	public void setWorkLeaderName(String workLeaderName) {
		this.workLeaderName = workLeaderName;
	}

	public String getWorkLeaderMobile() {
		return workLeaderMobile;
	}

	public void setWorkLeaderMobile(String workLeaderMobile) {
		this.workLeaderMobile = workLeaderMobile;
	}

	@Override
	public Long getEntityId() { 
		return this.getId();
	}

	@Override
	public void reSetEntityStatus(String entityStatus) { 
		this.setStatus(entityStatus);
	}

	@Override
	public String getEntityStatus() { 
		return this.status;
	}
 
 
	public String getDayTimeFrom() {
		return dayTimeFrom;
	}

	public void setDayTimeFrom(String dayTimeFrom) {
		this.dayTimeFrom = dayTimeFrom;
	}

	public String getDayTimeTo() {
		return dayTimeTo;
	}

	public void setDayTimeTo(String dayTimeTo) {
		this.dayTimeTo = dayTimeTo;
	}

	@Override
	public String getEntityCategory() { 
		return "WF-OVERTIME";
	}

	public List<LookupCode> getEnableActions() {
		return enableActions;
	}

	public void setEnableActions(List<LookupCode> enableActions) {
		this.enableActions = enableActions;
	}

	@Override
	public String getEntityNumber() { 
		return this.vouchNumber;
	}

	@Override
	public String getEntityDescription() {
		return "申请加班从"+DateUtil.convertDate2String(this.getOvertimeFrom())+ " 到 " + DateUtil.convertDate2String(this.getOvertimeTo())+" 每天"+this.dayTimeFrom+"到"+this.dayTimeTo;
	}

	@Override
	public WorkFlowSpecRole[] getSpecRoles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reSetWorkFlowId(Long workFlowId) {
		this.workFlowId = workFlowId;
		
	}

	public String getChainName() {
		return chainName;
	}

	public void setChainName(String chainName) {
		this.chainName = chainName;
	}

	public String getTemporaryPassName() {
		return temporaryPassName;
	}

	public void setTemporaryPassName(String temporaryPassName) {
		this.temporaryPassName = temporaryPassName;
	}

	public String getTemporaryPassCode() {
		return temporaryPassCode;
	}

	public void setTemporaryPassCode(String temporaryPassCode) {
		this.temporaryPassCode = temporaryPassCode;
	}

	public String getChainCode() {
		return chainCode;
	}

	public void setChainCode(String chainCode) {
		this.chainCode = chainCode;
	}



	public String getChainManager() {
		return chainManager;
	}

	public void setChainManager(String chainManager) {
		this.chainManager = chainManager;
	}

	public String getChainManagerMobile() {
		return chainManagerMobile;
	}

	public void setChainManagerMobile(String chainManagerMobile) {
		this.chainManagerMobile = chainManagerMobile;
	}
 
	@Override
	public Long getItWorkFlowId() {
		// TODO Auto-generated method stub
		return this.getWorkFlowId();
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	} 
	
	/**
	 * 过期 
	 * @return
	 */
	public boolean isExpire() {
		if (CommonUtil.isNull(this.overtimeTo) || CommonUtil.isNull(this.dayTimeTo)) {
			return false;
		}
		Date date = DateUtil.convertString2Date(DateUtil.convertDate2String(this.overtimeTo)+" "+this.dayTimeTo ,"yyyy-MM-dd HH:mm");
		return date.before(DateUtil.getCurrentDate());
	}
	
	/**
	 * 最后日期
	 * @return
	 */
	public boolean isLastDate() {
		if (CommonUtil.isNull(this.overtimeTo) ) {
			return false;
		}
		if (DateUtil.convertDate2String(DateUtil.getCurrentDate()).equals(DateUtil.convertDate2String(this.overtimeTo))) {
			return true;
		}
		return false; 
	}

	public String getQrcode() {
		return qrcode;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}

	@Override
	public String getEntityStatusDsp() { 
		return null;
	}

	@Override
	public String getCreateByDsp() { 
		return null;
	}
 
}

