package org.gl.ceir.CeirPannelCode.Model;

import java.util.List;

public class LawfulStolenRecovey {
	
	private Integer blockCategory;
	private String blockCategoryInterp;
	private String blockingTimePeriod;
	private String blockingType;
	private String createdOn;
	private String fileName;
	private Integer fileStatus;
	private Integer id;
	private Integer imei;
	private String modifiedOn;
	private Integer operatorTypeId;
	private String operatorTypeIdInterp;
	private Integer qty,quantity,deviceQuantity;
	private String remark,rejectedRemark;
	private Integer requestType;
	private String requestTypeInterp;
	private String roleType;
	private SingleImeiDetails singleImeiDetails;
	private Integer sourceType;
	private String sourceTypeInterp;
	private String stateInterp,dateOfStolen,dateOfRecovery,firFileName,complaintType,fileLink;
	private StolenIndividualUserDB stolenIndividualUserDB;
	private StolenOrganizationUserDB stolenOrganizationUserDB;
	private List<AttachedFile> attachedFiles;
	private String txnId;
	private Integer userId;
	private String publicIp;
	private String browser;
	
	public Integer getBlockCategory() {
	return blockCategory;
	}

	public void setBlockCategory(Integer blockCategory) {
	this.blockCategory = blockCategory;
	}

	public String getBlockCategoryInterp() {
	return blockCategoryInterp;
	}

	public void setBlockCategoryInterp(String blockCategoryInterp) {
	this.blockCategoryInterp = blockCategoryInterp;
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

	public String getCreatedOn() {
	return createdOn;
	}

	public void setCreatedOn(String createdOn) {
	this.createdOn = createdOn;
	}

	public String getFileName() {
	return fileName;
	}

	public void setFileName(String fileName) {
	this.fileName = fileName;
	}

	public Integer getFileStatus() {
	return fileStatus;
	}

	public void setFileStatus(Integer fileStatus) {
	this.fileStatus = fileStatus;
	}

	public Integer getId() {
	return id;
	}

	public void setId(Integer id) {
	this.id = id;
	}

	public Integer getImei() {
	return imei;
	}

	public void setImei(Integer imei) {
	this.imei = imei;
	}

	public String getModifiedOn() {
	return modifiedOn;
	}

	public void setModifiedOn(String modifiedOn) {
	this.modifiedOn = modifiedOn;
	}

	public Integer getOperatorTypeId() {
	return operatorTypeId;
	}

	public void setOperatorTypeId(Integer operatorTypeId) {
	this.operatorTypeId = operatorTypeId;
	}

	public String getOperatorTypeIdInterp() {
	return operatorTypeIdInterp;
	}

	public void setOperatorTypeIdInterp(String operatorTypeIdInterp) {
	this.operatorTypeIdInterp = operatorTypeIdInterp;
	}

	public Integer getQty() {
	return qty;
	}

	public void setQty(Integer qty) {
	this.qty = qty;
	}

	public String getRemark() {
	return remark;
	}

	public void setRemark(String remark) {
	this.remark = remark;
	}

	public Integer getRequestType() {
	return requestType;
	}

	public void setRequestType(Integer requestType) {
	this.requestType = requestType;
	}

	public String getRequestTypeInterp() {
	return requestTypeInterp;
	}

	public void setRequestTypeInterp(String requestTypeInterp) {
	this.requestTypeInterp = requestTypeInterp;
	}

	public String getRoleType() {
	return roleType;
	}

	public void setRoleType(String roleType) {
	this.roleType = roleType;
	}

	public SingleImeiDetails getSingleImeiDetails() {
	return singleImeiDetails;
	}

	public void setSingleImeiDetails(SingleImeiDetails singleImeiDetails) {
	this.singleImeiDetails = singleImeiDetails;
	}

	public Integer getSourceType() {
	return sourceType;
	}

	public void setSourceType(Integer sourceType) {
	this.sourceType = sourceType;
	}

	public String getSourceTypeInterp() {
	return sourceTypeInterp;
	}

	public void setSourceTypeInterp(String sourceTypeInterp) {
	this.sourceTypeInterp = sourceTypeInterp;
	}

	public String getStateInterp() {
	return stateInterp;
	}

	public void setStateInterp(String stateInterp) {
	this.stateInterp = stateInterp;
	}

	public StolenIndividualUserDB getStolenIndividualUserDB() {
	return stolenIndividualUserDB;
	}

	public void setStolenIndividualUserDB(StolenIndividualUserDB stolenIndividualUserDB) {
	this.stolenIndividualUserDB = stolenIndividualUserDB;
	}

	public StolenOrganizationUserDB getStolenOrganizationUserDB() {
	return stolenOrganizationUserDB;
	}

	public void setStolenOrganizationUserDB(StolenOrganizationUserDB stolenOrganizationUserDB) {
	this.stolenOrganizationUserDB = stolenOrganizationUserDB;
	}

	public String getTxnId() {
	return txnId;
	}

	public void setTxnId(String txnId) {
	this.txnId = txnId;
	}

	public Integer getUserId() {
	return userId;
	}

	public void setUserId(Integer userId) {
	this.userId = userId;
	}

	
	
	public String getDateOfStolen() {
		return dateOfStolen;
	}

	public void setDateOfStolen(String dateOfStolen) {
		this.dateOfStolen = dateOfStolen;
	}

	public String getDateOfRecovery() {
		return dateOfRecovery;
	}

	public void setDateOfRecovery(String dateOfRecovery) {
		this.dateOfRecovery = dateOfRecovery;
	}

	public String getFirFileName() {
		return firFileName;
	}

	public void setFirFileName(String firFileName) {
		this.firFileName = firFileName;
	}

	
	
	public String getComplaintType() {
		return complaintType;
	}

	public void setComplaintType(String complaintType) {
		this.complaintType = complaintType;
	}

	public String getFileLink() {
		return fileLink;
	}

	public void setFileLink(String fileLink) {
		this.fileLink = fileLink;
	}

	public String getRejectedRemark() {
		return rejectedRemark;
	}

	public void setRejectedRemark(String rejectedRemark) {
		this.rejectedRemark = rejectedRemark;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	
	
	public Integer getDeviceQuantity() {
		return deviceQuantity;
	}

	public void setDeviceQuantity(Integer deviceQuantity) {
		this.deviceQuantity = deviceQuantity;
	}

	
	 

	public List<AttachedFile> getAttachedFiles() {
		return attachedFiles;
	}

	public void setAttachedFiles(List<AttachedFile> attachedFiles) {
		this.attachedFiles = attachedFiles;
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
		builder.append("LawfulStolenRecovey [blockCategory=");
		builder.append(blockCategory);
		builder.append(", blockCategoryInterp=");
		builder.append(blockCategoryInterp);
		builder.append(", blockingTimePeriod=");
		builder.append(blockingTimePeriod);
		builder.append(", blockingType=");
		builder.append(blockingType);
		builder.append(", createdOn=");
		builder.append(createdOn);
		builder.append(", fileName=");
		builder.append(fileName);
		builder.append(", fileStatus=");
		builder.append(fileStatus);
		builder.append(", id=");
		builder.append(id);
		builder.append(", imei=");
		builder.append(imei);
		builder.append(", modifiedOn=");
		builder.append(modifiedOn);
		builder.append(", operatorTypeId=");
		builder.append(operatorTypeId);
		builder.append(", operatorTypeIdInterp=");
		builder.append(operatorTypeIdInterp);
		builder.append(", qty=");
		builder.append(qty);
		builder.append(", quantity=");
		builder.append(quantity);
		builder.append(", deviceQuantity=");
		builder.append(deviceQuantity);
		builder.append(", remark=");
		builder.append(remark);
		builder.append(", rejectedRemark=");
		builder.append(rejectedRemark);
		builder.append(", requestType=");
		builder.append(requestType);
		builder.append(", requestTypeInterp=");
		builder.append(requestTypeInterp);
		builder.append(", roleType=");
		builder.append(roleType);
		builder.append(", singleImeiDetails=");
		builder.append(singleImeiDetails);
		builder.append(", sourceType=");
		builder.append(sourceType);
		builder.append(", sourceTypeInterp=");
		builder.append(sourceTypeInterp);
		builder.append(", stateInterp=");
		builder.append(stateInterp);
		builder.append(", dateOfStolen=");
		builder.append(dateOfStolen);
		builder.append(", dateOfRecovery=");
		builder.append(dateOfRecovery);
		builder.append(", firFileName=");
		builder.append(firFileName);
		builder.append(", complaintType=");
		builder.append(complaintType);
		builder.append(", fileLink=");
		builder.append(fileLink);
		builder.append(", stolenIndividualUserDB=");
		builder.append(stolenIndividualUserDB);
		builder.append(", stolenOrganizationUserDB=");
		builder.append(stolenOrganizationUserDB);
		builder.append(", attachedFiles=");
		builder.append(attachedFiles);
		builder.append(", txnId=");
		builder.append(txnId);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", publicIp=");
		builder.append(publicIp);
		builder.append(", browser=");
		builder.append(browser);
		builder.append("]");
		return builder.toString();
	}

 

	 

}
