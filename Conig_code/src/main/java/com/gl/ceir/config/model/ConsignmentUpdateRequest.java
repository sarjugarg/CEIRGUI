package com.gl.ceir.config.model;

public class ConsignmentUpdateRequest {

	private int action;
	private String userType;
	private String roleType;
	private Long userId;
	private Long roleTypeUserId;
	private String txnId;
	private String remarks;
	private Integer featureId;
	private Integer requestType;
	private String nid;
	private String userName;
	private Integer userTypeId;
	private String publicIp;
	private String browser;
	public Integer getUserTypeId() {
		return userTypeId;
	}
	
	public void setUserTypeId(Integer userTypeId) {
		this.userTypeId = userTypeId;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
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
	public String getRoleType() {
		return roleType;
	}
	public void setRoleType(String roleType) {
		this.roleType = roleType;
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
	public Integer getRequestType() {
		return requestType;
	}
	public void setRequestType(Integer requestType) {
		this.requestType = requestType;
	}
	public String getNid() {
		return nid;
	}
	public void setNid(String nid) {
		this.nid = nid;
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
		builder.append("ConsignmentUpdateRequest [action=");
		builder.append(action);
		builder.append(", userType=");
		builder.append(userType);
		builder.append(", roleType=");
		builder.append(roleType);
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
		builder.append(", requestType=");
		builder.append(requestType);
		builder.append(", nid=");
		builder.append(nid);
		builder.append(", userName=");
		builder.append(userName);
		builder.append(", userTypeId=");
		builder.append(userTypeId);
		builder.append(", publicIp=");
		builder.append(publicIp);
		builder.append(", browser=");
		builder.append(browser);
		builder.append("]");
		return builder.toString();
	}
	
 
}
