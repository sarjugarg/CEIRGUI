package org.gl.ceir.CeirPannelCode.Model;

import java.util.List;

public class StockUserModel {

	private int   id ;
	private String createdOn;

	private String username;
	private String  password;
	private int   fwstatus;
	private String  modifiedBy;
	
	private String modifiedOn;
	private int subuser_status;
	private int  parent_id;
	private String email ;
	private String phone_no;
	private String organization;
	private int user_limit;
	private String organization_id;
	private String captcha;
    private String status;
    private Registration userProfile;
    private AnonymousUser usertype;
	@Override
	public String toString() {
		return "StockUserModel [id=" + id + ", createdOn=" + createdOn + ", username=" + username + ", password="
				+ password + ", fwstatus=" + fwstatus + ", modifiedBy=" + modifiedBy + ", modifiedOn=" + modifiedOn
				+ ", subuser_status=" + subuser_status + ", parent_id=" + parent_id + ", email=" + email + ", phone_no="
				+ phone_no + ", organization=" + organization + ", user_limit=" + user_limit + ", organization_id="
				+ organization_id + ", captcha=" + captcha + ", status=" + status + ", userProfile=" + userProfile
				+ ", usertype=" + usertype + "]";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getFwstatus() {
		return fwstatus;
	}
	public void setFwstatus(int fwstatus) {
		this.fwstatus = fwstatus;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	public int getSubuser_status() {
		return subuser_status;
	}
	public void setSubuser_status(int subuser_status) {
		this.subuser_status = subuser_status;
	}
	public int getParent_id() {
		return parent_id;
	}
	public void setParent_id(int parent_id) {
		this.parent_id = parent_id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone_no() {
		return phone_no;
	}
	public void setPhone_no(String phone_no) {
		this.phone_no = phone_no;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public int getUser_limit() {
		return user_limit;
	}
	public void setUser_limit(int user_limit) {
		this.user_limit = user_limit;
	}
	public String getOrganization_id() {
		return organization_id;
	}
	public void setOrganization_id(String organization_id) {
		this.organization_id = organization_id;
	}
	public String getCaptcha() {
		return captcha;
	}
	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Registration getUserProfile() {
		return userProfile;
	}
	public void setUserProfile(Registration userProfile) {
		this.userProfile = userProfile;
	}
	public AnonymousUser getUsertype() {
		return usertype;
	}
	public void setUsertype(AnonymousUser usertype) {
		this.usertype = usertype;
	}
	
	
    
    
}
