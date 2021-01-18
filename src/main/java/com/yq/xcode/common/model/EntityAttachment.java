package com.yq.xcode.common.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.security.entity.JpaBaseModel;
import com.yq.xcode.web.ui.annotation.ColumnLable;

@SuppressWarnings("serial")
@Entity
@Table(name="ATTACHMENT")
@JsonIgnoreProperties(ignoreUnknown=true)
public class EntityAttachment extends JpaBaseModel {

	@TableGenerator(
			name="idGen", 
			table="ID_GENERATOR", 
			pkColumnName="ID_KEY", 
			valueColumnName="ID_VALUE", 
			pkColumnValue="ATTACHMENT_ID", 
			allocationSize=1)
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="idGen")
	@Column(name="ID")
	private Long id;
	
	//文件名
	@Column(name="NAME",length=100,nullable=false)
	@ColumnLable(name="文件名" )
	private String name;
	
	//对应 主表的实体类简写名称,如Claim类,则是 com.sevendaysinn.claim.model.Claim
	@Column(name="MODEL_NAME",length=100,nullable=false)
	@ColumnLable(name="实体类简写名称" )
	public String modelName;
	
	//对应关联主表的ID
	@Column(name="MODEL_ID",nullable=false)
	@ColumnLable(name="对应关联主表的ID" )
	public Long modelId;
	
	//远程文件夹相对路径
	@Column(name="PATH",length=200,nullable=true)
	@ColumnLable(name="远程文件夹相对路径" )
	private String path;
	
	//文件大小,单位 B
	@Column(name="SIZE",nullable=true)
	@ColumnLable(name="文件大小,单位 B" )
	private long size;
	
	/**
	 * 附件类型
	 */
	@Column(name="TYPE")
	@ColumnLable(name="附件类型 " )
	private String type;
	
	
	@Column(name="THUMBNAIL_PATH",length=200)
	@ColumnLable(name="缩略图路径")
	private String thumbnailPath;
	
	@Column(name="LABEL")
	@ColumnLable(name="备注")
	private String label;
	
	@Column(name="DISPLAY_NAME")
	@ColumnLable(name="显示名")
	private String displayName;
	
	@Column(name="IMAGE_GROUP",length=20)
	@ColumnLable(name="分类")
	private String imageGroup;

	public EntityAttachment(){
		super();
	}
	
	public EntityAttachment(String name, String modelName, Long modelId) {
		super();
		this.name = name;
		this.modelName = modelName;
		this.modelId = modelId;
	}
	
	public EntityAttachment(String name, String path, String modelName, Long modelId) {
		super();
		this.name = name;
		this.modelName = modelName;
		this.modelId = modelId;
		this.path = path;
	}
    
	
	public EntityAttachment(String name, String modelName, Long modelId, long size) {
		super();
		this.name = name;
		this.modelName = modelName;
		this.modelId = modelId;
		this.size = size;
	}




	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getModelName() {
		return modelName;
	}


	public void setModelName(String modelName) {
		this.modelName = modelName;
	}


	public Long getModelId() {
		return modelId;
	}


	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}


	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getThumbnailPath() {
		return thumbnailPath;
	}

	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getImageGroup() {
		return imageGroup;
	}

	public void setImageGroup(String imageGroup) {
		this.imageGroup = imageGroup;
	}

	@PrePersist
	public void prePersist() {
		String username = "";
		if (CommonUtil.isNull(SecurityContextHolder.getContext().getAuthentication())) {
			username = "Sysjob";
		} else {
			username = SecurityContextHolder.getContext().getAuthentication().getName();
		}
		Date currentTime = new Date();
		this.setCreateTime(currentTime);
		this.setCreateUser(username);
		this.setLastUpdateTime(currentTime);
		this.setLastUpdateUser(username);
	}

	@PreUpdate
	public void preUpdate() {
		String username = "";
		if (CommonUtil.isNull(SecurityContextHolder.getContext().getAuthentication())) {
			username = "Sysjob";
		} else {
			username = SecurityContextHolder.getContext().getAuthentication().getName();
		}
		this.setLastUpdateTime(new Date());
		this.setLastUpdateUser(username);
	}
	
}
