package org.gl.ceir.CeirPannelCode.Model;

import java.util.List;

public class GrievanceModel {
private int id;
private int categoryId;
private int grievanceStatus ;
private int userId,featureId;
private String fileName;
private String grievanceId;
private String modifiedOn;
private String createdOn;
private String remarks;
private String txnId;
private String userType;
private String reply;
private String userDisplayName,email,firstName,lastName,middleName,phoneNo,raisedBy,username,raisedByUserType;
private Integer raisedByUserId,userTypeId;
private GrievanceMessageModal grievance;
private List<MultipleFileModel> attachedFiles;
private String publicIp;
private String browser;


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
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public int getCategoryId() {
	return categoryId;
}
public void setCategoryId(int categoryId) {
	this.categoryId = categoryId;
}
public int getGrievanceStatus() {
	return grievanceStatus;
}
public void setGrievanceStatus(int grievanceStatus) {
	this.grievanceStatus = grievanceStatus;
}
public int getUserId() {
	return userId;
}
public void setUserId(int userId) {
	this.userId = userId;
}
public int getFeatureId() {
	return featureId;
}
public void setFeatureId(int featureId) {
	this.featureId = featureId;
}
public String getFileName() {
	return fileName;
}
public void setFileName(String fileName) {
	this.fileName = fileName;
}
public String getGrievanceId() {
	return grievanceId;
}
public void setGrievanceId(String grievanceId) {
	this.grievanceId = grievanceId;
}
public String getModifiedOn() {
	return modifiedOn;
}
public void setModifiedOn(String modifiedOn) {
	this.modifiedOn = modifiedOn;
}
public String getCreatedOn() {
	return createdOn;
}
public void setCreatedOn(String createdOn) {
	this.createdOn = createdOn;
}
public String getRemarks() {
	return remarks;
}
public void setRemarks(String remarks) {
	this.remarks = remarks;
}
public String getTxnId() {
	return txnId;
}
public void setTxnId(String txnId) {
	this.txnId = txnId;
}
public String getUserType() {
	return userType;
}
public void setUserType(String userType) {
	this.userType = userType;
}
public String getReply() {
	return reply;
}
public void setReply(String reply) {
	this.reply = reply;
}
public String getUserDisplayName() {
	return userDisplayName;
}
public void setUserDisplayName(String userDisplayName) {
	this.userDisplayName = userDisplayName;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getFirstName() {
	return firstName;
}
public void setFirstName(String firstName) {
	this.firstName = firstName;
}
public String getLastName() {
	return lastName;
}
public void setLastName(String lastName) {
	this.lastName = lastName;
}
public String getMiddleName() {
	return middleName;
}
public void setMiddleName(String middleName) {
	this.middleName = middleName;
}
public String getPhoneNo() {
	return phoneNo;
}
public void setPhoneNo(String phoneNo) {
	this.phoneNo = phoneNo;
}
public String getRaisedBy() {
	return raisedBy;
}
public void setRaisedBy(String raisedBy) {
	this.raisedBy = raisedBy;
}
public String getUsername() {
	return username;
}
public void setUsername(String username) {
	this.username = username;
}
public String getRaisedByUserType() {
	return raisedByUserType;
}
public void setRaisedByUserType(String raisedByUserType) {
	this.raisedByUserType = raisedByUserType;
}
public Integer getRaisedByUserId() {
	return raisedByUserId;
}
public void setRaisedByUserId(Integer raisedByUserId) {
	this.raisedByUserId = raisedByUserId;
}
public Integer getUserTypeId() {
	return userTypeId;
}
public void setUserTypeId(Integer userTypeId) {
	this.userTypeId = userTypeId;
}
public GrievanceMessageModal getGrievance() {
	return grievance;
}
public void setGrievance(GrievanceMessageModal grievance) {
	this.grievance = grievance;
}
public List<MultipleFileModel> getAttachedFiles() {
	return attachedFiles;
}
public void setAttachedFiles(List<MultipleFileModel> attachedFiles) {
	this.attachedFiles = attachedFiles;
}
@Override
public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("GrievanceModel [id=");
	builder.append(id);
	builder.append(", categoryId=");
	builder.append(categoryId);
	builder.append(", grievanceStatus=");
	builder.append(grievanceStatus);
	builder.append(", userId=");
	builder.append(userId);
	builder.append(", featureId=");
	builder.append(featureId);
	builder.append(", fileName=");
	builder.append(fileName);
	builder.append(", grievanceId=");
	builder.append(grievanceId);
	builder.append(", modifiedOn=");
	builder.append(modifiedOn);
	builder.append(", createdOn=");
	builder.append(createdOn);
	builder.append(", remarks=");
	builder.append(remarks);
	builder.append(", txnId=");
	builder.append(txnId);
	builder.append(", userType=");
	builder.append(userType);
	builder.append(", reply=");
	builder.append(reply);
	builder.append(", userDisplayName=");
	builder.append(userDisplayName);
	builder.append(", email=");
	builder.append(email);
	builder.append(", firstName=");
	builder.append(firstName);
	builder.append(", lastName=");
	builder.append(lastName);
	builder.append(", middleName=");
	builder.append(middleName);
	builder.append(", phoneNo=");
	builder.append(phoneNo);
	builder.append(", raisedBy=");
	builder.append(raisedBy);
	builder.append(", username=");
	builder.append(username);
	builder.append(", raisedByUserType=");
	builder.append(raisedByUserType);
	builder.append(", raisedByUserId=");
	builder.append(raisedByUserId);
	builder.append(", userTypeId=");
	builder.append(userTypeId);
	builder.append(", grievance=");
	builder.append(grievance);
	builder.append(", attachedFiles=");
	builder.append(attachedFiles);
	builder.append(", publicIp=");
	builder.append(publicIp);
	builder.append(", browser=");
	builder.append(browser);
	builder.append("]");
	return builder.toString();
}


}