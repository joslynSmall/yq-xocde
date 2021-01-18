package com.yq.xcode.security.entity;

import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.web.ui.annotation.ColumnLable;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "sec_principal")
public class SecPrincipal extends JpaBaseModel {

    private static final long serialVersionUID = 8422809654873820155L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "IS_ACTIVE")
    private Boolean active;

    @Transient
    private String activeName;

    @Column(name = "ALIAS_NAME")
    private String aliasName;

    @Column(name = "DISPLAY_NAME")
    private String displayName;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "EXPIRE_TIME")
    private Date expireTime;

    @Column(name = "MOBILE_PHONE")
    private String mobilePhone;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ORGANIZATION_ID")
    private String organizationId;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "PASSWORD_EXPIRE_TIME")
    private String passwordExpireTime;

    @Column(name = "WX_OPEN_ID")
    private String wxOpenId;

    @ColumnLable(name = "用户类型")
    @Column(name = "USER_CATEGORY")
    private String userCategory;

    /**
     * 客户ID非空或!=0时为客户数据
     */
    @ColumnLable(name = "门店或分中心ID")
    @Column(name = "VENDOR_ID")
    private Long vendorId;

    /**
     * 一般来自系统代码表
     */
    @ColumnLable(name = "部门编码")
    @Column(name = "DEPT_CODE")
    private String deptNo;

    @Column(name = "TEXT1")
    private String text1;

    @Column(name = "TEXT2")
    private String text2;

    @Column(name = "TEXT3")
    private String text3;

    @Column(name = "TEXT4")
    private String text4;

    @Column(name = "TEXT5")
    private String text5;

    @Transient
    private String deptName;

    @Transient
    private String vendorName;

    @Transient
    private String userCategoryName;

    public String getUserCategoryName() {
        if ("Z".equals(userCategory)) {
            userCategoryName = "总部用户";
        } else if ("C".equals(userCategory)) {
            userCategoryName = "门店用户";
        } else if ("G".equals(userCategory)) {
            userCategoryName = "分中心用户";
        } else if ("S".equals(userCategory)) {
            userCategoryName = "供应商";
        }
        return userCategoryName;
    }

    public void setUserCategoryName(String userCategoryName) {
        this.userCategoryName = userCategoryName;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActive() {
        return active;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getActiveName() {
        if (CommonUtil.isNull(this.active)) {
            return null;
        }
        return this.active ? "是" : "否";
    }

    public void setActiveName(String activeName) {
        this.active = "是".equals(activeName);
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public long getDefaultOrganizationId() {
        if (CommonUtil.isNotNull(organizationId)) {
            String[] orgids = organizationId.split(",");
            return Long.parseLong(orgids[0]);
        }
        return 0L;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordExpireTime() {
        return passwordExpireTime;
    }

    public void setPasswordExpireTime(String passwordExpireTime) {
        this.passwordExpireTime = passwordExpireTime;
    }

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    public String getText3() {
        return text3;
    }

    public void setText3(String text3) {
        this.text3 = text3;
    }

    public String getText4() {
        return text4;
    }

    public void setText4(String text4) {
        this.text4 = text4;
    }

    public String getText5() {
        return text5;
    }

    public void setText5(String text5) {
        this.text5 = text5;
    }


    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    public String getUserCategory() {
        return userCategory;
    }

    public void setUserCategory(String userCategory) {
        this.userCategory = userCategory;
    }


    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public String getDeptNo() {
        return deptNo;
    }

    public void setDeptNo(String deptNo) {
        this.deptNo = deptNo;
    }


}
