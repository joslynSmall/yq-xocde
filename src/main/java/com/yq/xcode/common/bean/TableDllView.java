package com.yq.xcode.common.bean;

import java.io.Serializable;

import com.yq.xcode.common.exception.ValidateException;
import com.yq.xcode.common.utils.CommonUtil;

public class TableDllView implements Serializable {
	private String colName;
	private String colType;
	private String colLable;
	private String description;
	
	private String lookupCode;
	private String gridCol;
	private String editCol;
	private String criteriaCol;
	
	private String attachName;
	private String attachMsg;
	
	private String hyplink;
	
	
	public String getHyplink() {
		return hyplink;
	}
	public void setHyplink(String hyplink) {
		this.hyplink = hyplink;
	}
	public String getAttachName() {
		return attachName;
	}
	public void setAttachName(String attachName) {
		this.attachName = attachName;
	}
	public String getAttachMsg() {
		return attachMsg;
	}
	public void setAttachMsg(String attachMsg) {
		this.attachMsg = attachMsg;
	}
	/**
	 * 控件类型
	 */
	private String eleCategory;
	
	
	public String getColName() {
		return colName;
	}
	public void setColName(String colName) {
		this.colName = colName;
	}
	public String getColType() {
		return colType;
	}
	public void setColType(String colType) {
		this.colType = colType;
	}
	public String getColLable() {
		return colLable;
	}
	public void setColLable(String colLable) {
		this.colLable = colLable;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	public String getLookupCode() {
		return lookupCode;
	}
	public void setLookupCode(String lookupCode) {
		this.lookupCode = lookupCode;
	}
	public String getGridCol() {
		return gridCol;
	}
	public void setGridCol(String gridCol) {
		this.gridCol = gridCol;
	}
	public String getEditCol() {
		return editCol;
	}
	public void setEditCol(String editCol) {
		this.editCol = editCol;
	}
	public String toCreateTableCol() {
		if (CommonUtil.isNull(this.colName)) {
			return "";
		}
		String notNull = "";
		String primaryKey = "";
		String realName = colName.replace(" ", "").replace("#", "").replace("*", "");
		if (colName.contains("*") || colName.contains("#")) {
			notNull = " not null ";
		}
		String comment = "";
		if (!CommonUtil.isNull(this.colLable)) {
			comment = " comment '"+this.colLable+"' ";
		}
		if (colName.contains("#")) {
			primaryKey =" , PRIMARY KEY ("+ realName+") ";
		}
		return realName+" "+this.toDbType()+notNull+comment+primaryKey;
	}
    public String toCreateModelProperty(String className) {
    	if (CommonUtil.isNull(this.colName)) {
			return "";
		}
    	
    
    	
    	String realName = colName.replace(" ", "").replace("#", "").replace("*", "");
    	if (this.sysCol.contains(","+realName.toUpperCase()+",")) {
    		return "";
    	}
    	String pStr = "/** "+BR+this.toCreateTableCol()+BR
    	         +CommonUtil.nvl(this.description,"")+BR
    	         +"*/"+BR;
    	if (!CommonUtil.isNull(this.colLable)) {
    		pStr = pStr+" @ColumnLable(name=\""+this.colLable+"\") " +BR;
    	}
    	String dspProterty = "";
    	if (CommonUtil.isNotNull(this.lookupCode)) {
    		dspProterty = " @Transient"+BR
    				 + " private String "+this.colNameToPropertyName()+"Dsp;"+BR;
    	}
    	if (CommonUtil.isNotNull(this.getAttachName())) {
    		int atNum = 5;
    		if (CommonUtil.isNotNull(this.editCol)) {
    			atNum = Integer.parseInt(this.editCol)+1;
    		}
    		dspProterty = dspProterty  
    		    +" @ColumnLable(name = \""+this.getAttachName()+"\") " +BR
    		    +"	@EditCol(lineNum = "+atNum+", tagKey= InputAttachmentTag.TAG_KEY,attachMsg=\""+CommonUtil.nvl(this.getAttachMsg(), "")+"\") " +BR
    			+"	@Transient " +BR
    			+"	private String showAttachment; "+BR;
    	}
    	
    	String propertyName = this.colNameToPropertyName();
    	pStr = pStr+" @Column(name = \""+realName+"\")"+BR
    			+ this.genAnocation(className,propertyName)
    	    + " private " +this.toPropertyType() +" "+propertyName+";"+BR;
    	
    	pStr = pStr+dspProterty;
		return pStr;
	}
    
    private String genAnocation(String className, String pName) {
    	String mandatory = "";
    	if (colName.startsWith("*")) {
    		mandatory = " , mandatory = true " ;
    	}    
    	
    	String str = "";
    	if (CommonUtil.isNotNull(this.gridCol)) {
    		if (CommonUtil.isNotNull(this.hyplink)) {
    			str = "@GridCol(lineNum="+this.gridCol+", isHyperlink = true )";
    		} else {
    			str = "@GridCol(lineNum="+this.gridCol+")";
    		}
    		
    	}
    	
    	if (CommonUtil.isNotNull(this.criteriaCol)) {
   			str = str+ " @CriteriaCol(lineNum="+this.gridCol+") ";
    	}
    	
    	if (CommonUtil.isNotNull(this.editCol)) {
    		String tagKey = "";
    		String listCategory = "";
    		if (CommonUtil.isNotNull(this.eleCategory)) {
    			tagKey = " , tagKey=\""+eleCategory+"\" ";
    		}
    		if (CommonUtil.isNotNull(this.lookupCode)) {
    			listCategory = " , listCategory=\""+className.toLowerCase()+"_"+pName.toLowerCase()+"\" ";
    		}
    		str = str+"@EditCol(lineNum="+this.editCol+tagKey+mandatory+listCategory+" )";
    	}
    	if (CommonUtil.isNotNull(str)) {
    		return str+BR;
    	}
    	return str;
    }
    
    public String getCriteriaCol() {
		return criteriaCol;
	}
	public void setCriteriaCol(String criteriaCol) {
		this.criteriaCol = criteriaCol;
	}
	private String toPropertyType() {
    	String lowType = this.colType.trim().toLowerCase();
    	if (lowType.startsWith("varchar")) {
    		return "String";
    	} else if (lowType.startsWith("int")) {
    		return "Integer";
    	} else if (lowType.startsWith("boolean")) {
    		return "Boolean";
    	} else if (lowType.startsWith("long")) {
    		return "Long";
    	} else if (lowType.startsWith("number")) {
    		return "BigDecimal";
    	}else if (lowType.startsWith("decimal")) {
    		return "BigDecimal";
    	} if (lowType.startsWith("date")) {
    		return "Date";
    	} if (lowType.startsWith("text")) {
    		return "String";
    	}  else {
    		throw new ValidateException("无此数据类型："+this.colType);
    	}
    }
    
    public String getEleCategory() {
		return eleCategory;
	}
	public void setEleCategory(String eleCategory) {
		this.eleCategory = eleCategory;
	}
	
	private String toDbType() {
    	if (CommonUtil.isNull(this.colType)) {
    		throw new ValidateException("数据类型（data type) 不可为空");
    	}
    	String lowType = this.colType.trim().toLowerCase();
    	if (lowType.startsWith("varchar")) {
    		if (lowType.equals("varchar")) {
    			return "varchar(40)";
    		}
    		return this.colType;
    	} else if (lowType.startsWith("int")) {
    		return "int";
    	}else if (lowType.startsWith("text")) {
    		return "text";
    	} else if (lowType.startsWith("boolean")) {
    		return "int";
    	} else if (lowType.startsWith("long")) {
    		return "int";
    	} else if (lowType.startsWith("number") || lowType.startsWith("decimal")) {
    		if (lowType.contains("(")) {
    			return lowType.replace("number", "decimal");
    		} else {
    			return "decimal(19,2)";
    		}
    	} if (lowType.startsWith("date")) {
    		return "datetime";
    	} else {
    		throw new ValidateException("无此数据类型："+this.colType);
    	}
    }
	
    private static String BR = "\r\n";
    
    private String colNameToPropertyName() {
		String proName = colName.replace(" ", "").replace("#", "").replace("*", "").toLowerCase();
		while (proName.contains("_")) {
			int ind = proName.indexOf("_");
			proName = proName.substring(0,ind)+proName.substring(ind+1,ind+2).toUpperCase()+proName.substring(ind+2,proName.length());
		}
		return proName;
	}
    
    private String sysCol = ",ID,VERSION,CREATED_BY,CREATED_TIME,LAST_UPDATED_BY,LAST_UPDATED_TIME,ORG_ID,";

	
}
