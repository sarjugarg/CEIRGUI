package org.gl.ceir.pagination.model;

import org.springframework.stereotype.Component;

@Component
public class GrievanceContentModel {
	private String grievanceId;
	private Integer userId;
	private String userType;
	private Integer grievanceStatus;
	private String txnId;
	private Integer categoryId;
	private String fileName;
	private String createdOn;
	private String modifiedOn;
	private String remarks;
	private String stateInterp;
	private String userDisplayName;
	private String raisedBy;
	public String getGrievanceId() {
		return grievanceId;
	}
	public void setGrievanceId(String grievanceId) {
		this.grievanceId = grievanceId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public Integer getGrievanceStatus() {
		return grievanceStatus;
	}
	public void setGrievanceStatus(Integer grievanceStatus) {
		this.grievanceStatus = grievanceStatus;
	}
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public String getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getStateInterp() {
		return stateInterp;
	}
	public void setStateInterp(String stateInterp) {
		this.stateInterp = stateInterp;
	}
	public String getUserDisplayName() {
		return userDisplayName;
	}
	public void setUserDisplayName(String userDisplayName) {
		this.userDisplayName = userDisplayName;
	}
	public String getRaisedBy() {
		return raisedBy;
	}
	public void setRaisedBy(String raisedBy) {
		this.raisedBy = raisedBy;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GrievanceContentModel [grievanceId=");
		builder.append(grievanceId);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", userType=");
		builder.append(userType);
		builder.append(", grievanceStatus=");
		builder.append(grievanceStatus);
		builder.append(", txnId=");
		builder.append(txnId);
		builder.append(", categoryId=");
		builder.append(categoryId);
		builder.append(", fileName=");
		builder.append(fileName);
		builder.append(", createdOn=");
		builder.append(createdOn);
		builder.append(", modifiedOn=");
		builder.append(modifiedOn);
		builder.append(", remarks=");
		builder.append(remarks);
		builder.append(", stateInterp=");
		builder.append(stateInterp);
		builder.append(", userDisplayName=");
		builder.append(userDisplayName);
		builder.append(", raisedBy=");
		builder.append(raisedBy);
		builder.append("]");
		return builder.toString();
	}

}