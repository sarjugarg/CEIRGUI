package com.gl.ceir.config.model;

import javax.persistence.Transient;

public class CeirActionRequest {

	private int action;
	
	private String userType;
	
	private Long userId;
	
	private Long roleTypeUserId;
	
	private String txnId;
	
	private String remarks;
	
	private Integer featureId;
	
	private String username;
	
	private long id;
	
	
	private String imei1;
	
	@Transient
	private String publicIp;
	@Transient
	private String browser;
	
	public int getAction() {
		return action;
	}
	public void setAction(int action) {
		this.action = action;
	}
	
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getRoleTypeUserId() {
		return roleTypeUserId;
	}
	public void setRoleTypeUserId(Long roleTypeUserId) {
		this.roleTypeUserId = roleTypeUserId;
	}
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Integer getFeatureId() {
		return featureId;
	}
	public void setFeatureId(Integer featureId) {
		this.featureId = featureId;
	}
	public String getImei1() {
		return imei1;
	}
	public void setImei1(String imei1) {
		this.imei1 = imei1;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPublicIp() {
		return publicIp;
	}
	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}
	public String getBrowser() {
		return browser;
	}
	public void setBrowser(String browser) {
		this.browser = browser;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CeirActionRequest [action=");
		builder.append(action);
		builder.append(", userType=");
		builder.append(userType);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", roleTypeUserId=");
		builder.append(roleTypeUserId);
		builder.append(", txnId=");
		builder.append(txnId);
		builder.append(", remarks=");
		builder.append(remarks);
		builder.append(", featureId=");
		builder.append(featureId);
		builder.append(", username=");
		builder.append(username);
		builder.append(", id=");
		builder.append(id);
		builder.append(", imei1=");
		builder.append(imei1);
		builder.append(", publicIp=");
		builder.append(publicIp);
		builder.append(", browser=");
		builder.append(browser);
		builder.append("]");
		return builder.toString();
	}
	

}
