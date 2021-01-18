package com.yq.xcode.security.oauth;

import com.yq.xcode.common.base.XBaseView;
import com.yq.xcode.security.entity.SecPrincipal;
import com.yq.xcode.web.ui.annotation.ColumnLable;

public class LoginUser extends XBaseView {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -8304923301273614599L;

    private Long id;

    private String displayName;

    private String mobilePhone;

    private String name;

    private String password;

    private String organizationId;

    private String organizationName;

    private String wxOpenId;

    @ColumnLable(name = "用户类型")
    private String userCategory;

    private Long vendorId;

    private String text1;

    private String text2;

    private String text3;

    private String text4;

    private String text5;

    public LoginUser() {

    }

    public LoginUser(SecPrincipal principal) {
        this.id = principal.getId();
        this.name = principal.getName();
        this.displayName = principal.getDisplayName();
        this.mobilePhone = principal.getMobilePhone();
        this.password = principal.getPassword();
        this.organizationId = principal.getOrganizationId();
        this.userCategory = principal.getUserCategory();
        this.vendorId = principal.getVendorId();
        this.text1 = principal.getText1();
        this.text2 = principal.getText2();
        this.text3 = principal.getText3();
        this.text4 = principal.getText4();
        this.text5 = principal.getText5();

        this.wxOpenId = principal.getWxOpenId();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
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


}
