package org.gl.ceir.CeirPannelCode.Model;

public class ConsignmentUpdateRequest {
	
	private int action;
	private String roleType;
	private int roleTypeUserId;
	private String txnId;
	private int userId;
	private String remarks;
	private Integer featureId;
	private String publicIp;
	private String browser;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public int getUserTypeId() {
		return userTypeId;
	}
	public void setUserTypeId(int userTypeId) {
		this.userTypeId = userTypeId;
	}
	private String userName,userType;
	private int userTypeId;

	
	public int getAction() {
		return action;
	}
	public void setAction(int action) {
		this.action = action;
	}
	public String getRoleType() {
		return roleType;
	}
	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}
	public int getRoleTypeUserId() {
		return roleTypeUserId;
	}
	public void setRoleTypeUserId(int roleTypeUserId) {
		this.roleTypeUserId = roleTypeUserId;
	}
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
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
		builder.append(", roleType=");
		builder.append(roleType);
		builder.append(", roleTypeUserId=");
		builder.append(roleTypeUserId);
		builder.append(", txnId=");
		builder.append(txnId);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", remarks=");
		builder.append(remarks);
		builder.append(", featureId=");
		builder.append(featureId);
		builder.append(", publicIp=");
		builder.append(publicIp);
		builder.append(", browser=");
		builder.append(browser);
		builder.append(", userName=");
		builder.append(userName);
		builder.append(", userType=");
		builder.append(userType);
		builder.append(", userTypeId=");
		builder.append(userTypeId);
		builder.append("]");
		return builder.toString();
	}
	
	
	
	
}
