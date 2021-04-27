package org.gl.ceir.CeirPannelCode.Model;

public class SingleImeiDetailsModel {
	private String categoryInterp,createdOn,deviceIdTypeInterp,deviceTypeInterp,deviceSerialNumber,modifiedOn,multipleSimStatusInterp,remark,txnId,userType,blockingTimePeriod,blockingType,rejectedRemark,roleType;
	private Integer category,deviceIdType,deviceType,multipleSimStatus,processState,requestType,sourceType,operatorTypeId,deviceQuantity;
	private long userId,id;
	private String firstImei,fourthImei,secondImei,thirdImei;
	private String publicIp;
	private String browser;
	public String getCategoryInterp() {
		return categoryInterp;
	}
	public void setCategoryInterp(String categoryInterp) {
		this.categoryInterp = categoryInterp;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public String getDeviceIdTypeInterp() {
		return deviceIdTypeInterp;
	}
	public void setDeviceIdTypeInterp(String deviceIdTypeInterp) {
		this.deviceIdTypeInterp = deviceIdTypeInterp;
	}
	public String getDeviceTypeInterp() {
		return deviceTypeInterp;
	}
	public void setDeviceTypeInterp(String deviceTypeInterp) {
		this.deviceTypeInterp = deviceTypeInterp;
	}
	public String getDeviceSerialNumber() {
		return deviceSerialNumber;
	}
	public void setDeviceSerialNumber(String deviceSerialNumber) {
		this.deviceSerialNumber = deviceSerialNumber;
	}
	public String getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	public String getMultipleSimStatusInterp() {
		return multipleSimStatusInterp;
	}
	public void setMultipleSimStatusInterp(String multipleSimStatusInterp) {
		this.multipleSimStatusInterp = multipleSimStatusInterp;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public String getBlockingTimePeriod() {
		return blockingTimePeriod;
	}
	public void setBlockingTimePeriod(String blockingTimePeriod) {
		this.blockingTimePeriod = blockingTimePeriod;
	}
	public String getBlockingType() {
		return blockingType;
	}
	public void setBlockingType(String blockingType) {
		this.blockingType = blockingType;
	}
	public Integer getCategory() {
		return category;
	}
	public void setCategory(Integer category) {
		this.category = category;
	}
	public Integer getDeviceIdType() {
		return deviceIdType;
	}
	public void setDeviceIdType(Integer deviceIdType) {
		this.deviceIdType = deviceIdType;
	}
	public Integer getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(Integer deviceType) {
		this.deviceType = deviceType;
	}
	public Integer getMultipleSimStatus() {
		return multipleSimStatus;
	}
	public void setMultipleSimStatus(Integer multipleSimStatus) {
		this.multipleSimStatus = multipleSimStatus;
	}
	public Integer getProcessState() {
		return processState;
	}
	public void setProcessState(Integer processState) {
		this.processState = processState;
	}
	public Integer getRequestType() {
		return requestType;
	}
	public void setRequestType(Integer requestType) {
		this.requestType = requestType;
	}
	public Integer getSourceType() {
		return sourceType;
	}
	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}
	public Integer getOperatorTypeId() {
		return operatorTypeId;
	}
	public void setOperatorTypeId(Integer operatorTypeId) {
		this.operatorTypeId = operatorTypeId;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getFirstImei() {
		return firstImei;
	}
	public void setFirstImei(String firstImei) {
		this.firstImei = firstImei;
	}
	public String getFourthImei() {
		return fourthImei;
	}
	public void setFourthImei(String fourthImei) {
		this.fourthImei = fourthImei;
	}
	public String getSecondImei() {
		return secondImei;
	}
	public void setSecondImei(String secondImei) {
		this.secondImei = secondImei;
	}
	public String getThirdImei() {
		return thirdImei;
	}
	public void setThirdImei(String thirdImei) {
		this.thirdImei = thirdImei;
	}
	public String getRejectedRemark() {
		return rejectedRemark;
	}
	public void setRejectedRemark(String rejectedRemark) {
		this.rejectedRemark = rejectedRemark;
	}
	public String getRoleType() {
		return roleType;
	}
	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}
	public Integer getDeviceQuantity() {
		return deviceQuantity;
	}
	public void setDeviceQuantity(Integer deviceQuantity) {
		this.deviceQuantity = deviceQuantity;
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
		builder.append("SingleImeiDetailsModel [categoryInterp=");
		builder.append(categoryInterp);
		builder.append(", createdOn=");
		builder.append(createdOn);
		builder.append(", deviceIdTypeInterp=");
		builder.append(deviceIdTypeInterp);
		builder.append(", deviceTypeInterp=");
		builder.append(deviceTypeInterp);
		builder.append(", deviceSerialNumber=");
		builder.append(deviceSerialNumber);
		builder.append(", modifiedOn=");
		builder.append(modifiedOn);
		builder.append(", multipleSimStatusInterp=");
		builder.append(multipleSimStatusInterp);
		builder.append(", remark=");
		builder.append(remark);
		builder.append(", txnId=");
		builder.append(txnId);
		builder.append(", userType=");
		builder.append(userType);
		builder.append(", blockingTimePeriod=");
		builder.append(blockingTimePeriod);
		builder.append(", blockingType=");
		builder.append(blockingType);
		builder.append(", rejectedRemark=");
		builder.append(rejectedRemark);
		builder.append(", roleType=");
		builder.append(roleType);
		builder.append(", category=");
		builder.append(category);
		builder.append(", deviceIdType=");
		builder.append(deviceIdType);
		builder.append(", deviceType=");
		builder.append(deviceType);
		builder.append(", multipleSimStatus=");
		builder.append(multipleSimStatus);
		builder.append(", processState=");
		builder.append(processState);
		builder.append(", requestType=");
		builder.append(requestType);
		builder.append(", sourceType=");
		builder.append(sourceType);
		builder.append(", operatorTypeId=");
		builder.append(operatorTypeId);
		builder.append(", deviceQuantity=");
		builder.append(deviceQuantity);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", id=");
		builder.append(id);
		builder.append(", firstImei=");
		builder.append(firstImei);
		builder.append(", fourthImei=");
		builder.append(fourthImei);
		builder.append(", secondImei=");
		builder.append(secondImei);
		builder.append(", thirdImei=");
		builder.append(thirdImei);
		builder.append(", publicIp=");
		builder.append(publicIp);
		builder.append(", browser=");
		builder.append(browser);
		builder.append("]");
		return builder.toString();
	}
	
	
	
	}
