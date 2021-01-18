package com.yq.xcode.security.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.security.bean.PermissionAction;
import com.yq.xcode.security.bean.PermissionBean;

@Entity
@Table(name = "sec_role")
public class Role extends JpaBaseModel {

	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
    private Long id;
    
	@Column(name = "IS_ACTIVE")
	private boolean active;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "CODE")
	private String code;
	
	@Column(name = "TYPE")
	private String type;

	@Column(name = "REALM_ID")
	private Long realmId;
	
	@Transient
	private List<PermissionBean> permissions;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getRealmId() {
		return realmId;
	}

	public void setRealmId(Long realmId) {
		this.realmId = realmId;
	}

	public List<PermissionBean> getPermissions() {
		return permissions;
	}
	public String getPermissionsFormat() {
		String permissionsFormat = "";
		if(CommonUtil.isNotNull(permissions)) {
		for(PermissionBean bean : permissions) {
			boolean hasAction = false;
			for(PermissionAction action : bean.getActions()) {
				if(action.isSelected() ) {
					if(!hasAction) {
						hasAction = true;
						permissionsFormat = permissionsFormat + "{" 
								+ bean.getName() +":"+bean.getCode()
								+ "[";
						permissionsFormat = permissionsFormat + action.getName();
					} else {
						permissionsFormat = permissionsFormat + "," + action.getName() ;
					}					
			  }
			}
			if (hasAction) {
				permissionsFormat = permissionsFormat + "]} ";
			}
		}
		}
		return permissionsFormat ;
	}
	public void setPermissions(List<PermissionBean> permissions) {
		this.permissions = permissions;
	}
}
