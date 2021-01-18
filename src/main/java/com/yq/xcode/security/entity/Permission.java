package com.yq.xcode.security.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.yq.xcode.security.bean.ActionMask;

@Entity
@Table(name = "sec_permission")
public class Permission extends JpaBaseModel {

	private static final long serialVersionUID = -8956178046705823035L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
    private Long id;
    
	@Column(name = "ACTIONS")
	private String actions;
	
	@Column(name = "CODE")
	private String code;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "name")
	private String name;
	
	@Column(name = "REALM_ID")
	private Long realmId;

	@Transient
	private ActionMask[] actionMask;

	public ActionMask[] getActionMask() {
		return toActionMasks(actions);
	}

	public static ActionMask[] toActionMasks(String text) {
		if (text == null || text.trim().equals("")) {
			return null;
		}
		if (text.startsWith("{") && text.endsWith("}")) {
			List<ActionMask> masks = new ArrayList<ActionMask>();
			text = text.substring(1, text.length() - 1);
			for (String elem : text.split(",")) {
				int colonIndex = elem.indexOf('=');
				if (colonIndex <= 0) {
					continue;
				}
				masks.add(new ActionMask(elem.substring(0, colonIndex),
						Integer.parseInt(elem.substring(colonIndex + 1))));
			}
			return masks.toArray(new ActionMask[masks.size()]);
		} else {
			return null;
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getActions() {
		return actions;
	}

	public void setActions(String actions) {
		this.actions = actions;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public Long getRealmId() {
		return realmId;
	}

	public void setRealmId(Long realmId) {
		this.realmId = realmId;
	}

	public void setActionMask(ActionMask[] actionMask) {
		this.actionMask = actionMask;
	}
}
