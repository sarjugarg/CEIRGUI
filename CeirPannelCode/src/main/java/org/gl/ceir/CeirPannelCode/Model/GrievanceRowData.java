package org.gl.ceir.CeirPannelCode.Model;

import com.google.gson.annotations.SerializedName;

public class GrievanceRowData {
	@SerializedName("modified_on")
	private String modifiedOn;
	
	@SerializedName("txn_id")
	private String txnId;
	
	@SerializedName("grievance_status")
	private String grievanceStatus;
	
	private String grievance_id;
	
	@SerializedName("user_type")
	private String userType;
	
	@SerializedName("category_id")
	private String categoryId;
	
	@SerializedName("created_on")
	private String createdOn;
	
	@SerializedName("user_id")
	private String userId;
	
	@SerializedName("file_name")
	private Object fileName;
	
	private String remarks;
	
	public String getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public String getGrievanceStatus() {
		return grievanceStatus;
	}
	public void setGrievanceStatus(String grievanceStatus) {
		this.grievanceStatus = grievanceStatus;
	}
	public String getGrievance_id() {
		return grievance_id;
	}
	public void setGrievance_id(String grievance_id) {
		this.grievance_id = grievance_id;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Object getFileName() {
		return fileName;
	}
	public void setFileName(Object fileName) {
		this.fileName = fileName;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GrievanceRowData [modifiedOn=");
		builder.append(modifiedOn);
		builder.append(", txnId=");
		builder.append(txnId);
		builder.append(", grievanceStatus=");
		builder.append(grievanceStatus);
		builder.append(", grievance_id=");
		builder.append(grievance_id);
		builder.append(", userType=");
		builder.append(userType);
		builder.append(", categoryId=");
		builder.append(categoryId);
		builder.append(", createdOn=");
		builder.append(createdOn);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", fileName=");
		builder.append(fileName);
		builder.append(", remarks=");
		builder.append(remarks);
		builder.append("]");
		return builder.toString();
	}


	
}
