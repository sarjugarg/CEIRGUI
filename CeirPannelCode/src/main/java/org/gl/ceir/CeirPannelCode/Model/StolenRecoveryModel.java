package org.gl.ceir.CeirPannelCode.Model;



public class StolenRecoveryModel {
	
	private String blockingTimePeriod;
	private String blockingType;
	private String fileName;
	private int fileStatus;
	private int id;
	private int requestType;
	private String roleType;
	private String  txnId,remark,rejectedRemark;
	private int userId;
	private Integer operatorTypeId;
	private int sourceType,category;
	private Integer qty,deviceCaegory,blockCategory,deviceQuantity  ;
	private String categoryInterp,blockCategoryInterp;
	private String publicIp;
	private String browser;
	
	
	
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




	public String getFileName() {
		return fileName;
	}




	public void setFileName(String fileName) {
		this.fileName = fileName;
	}




	public int getFileStatus() {
		return fileStatus;
	}




	public void setFileStatus(int fileStatus) {
		this.fileStatus = fileStatus;
	}




	public int getId() {
		return id;
	}




	public void setId(int id) {
		this.id = id;
	}




	public int getRequestType() {
		return requestType;
	}




	public void setRequestType(int requestType) {
		this.requestType = requestType;
	}




	public String getRoleType() {
		return roleType;
	}




	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}




	public String getTxnId() {
		return txnId;
	}




	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}




	public String getRemark() {
		return remark;
	}




	public void setRemark(String remark) {
		this.remark = remark;
	}




	public int getUserId() {
		return userId;
	}




	public void setUserId(int userId) {
		this.userId = userId;
	}




	public Integer getOperatorTypeId() {
		return operatorTypeId;
	}




	public void setOperatorTypeId(Integer operatorTypeId) {
		this.operatorTypeId = operatorTypeId;
	}




	public int getSourceType() {
		return sourceType;
	}




	public void setSourceType(int sourceType) {
		this.sourceType = sourceType;
	}




	public int getCategory() {
		return category;
	}




	public void setCategory(int category) {
		this.category = category;
	}




	public Integer getQty() {
		return qty;
	}




	public void setQty(Integer qty) {
		this.qty = qty;
	}




	public Integer getDeviceCaegory() {
		return deviceCaegory;
	}




	public void setDeviceCaegory(Integer deviceCaegory) {
		this.deviceCaegory = deviceCaegory;
	}




	public Integer getBlockCategory() {
		return blockCategory;
	}




	public void setBlockCategory(Integer blockCategory) {
		this.blockCategory = blockCategory;
	}




	public String getCategoryInterp() {
		return categoryInterp;
	}




	public void setCategoryInterp(String categoryInterp) {
		this.categoryInterp = categoryInterp;
	}




	public String getBlockCategoryInterp() {
		return blockCategoryInterp;
	}




	public void setBlockCategoryInterp(String blockCategoryInterp) {
		this.blockCategoryInterp = blockCategoryInterp;
	}




	public String getRejectedRemark() {
		return rejectedRemark;
	}




	public void setRejectedRemark(String rejectedRemark) {
		this.rejectedRemark = rejectedRemark;
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
		builder.append("StolenRecoveryModel [blockingTimePeriod=");
		builder.append(blockingTimePeriod);
		builder.append(", blockingType=");
		builder.append(blockingType);
		builder.append(", fileName=");
		builder.append(fileName);
		builder.append(", fileStatus=");
		builder.append(fileStatus);
		builder.append(", id=");
		builder.append(id);
		builder.append(", requestType=");
		builder.append(requestType);
		builder.append(", roleType=");
		builder.append(roleType);
		builder.append(", txnId=");
		builder.append(txnId);
		builder.append(", remark=");
		builder.append(remark);
		builder.append(", rejectedRemark=");
		builder.append(rejectedRemark);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", operatorTypeId=");
		builder.append(operatorTypeId);
		builder.append(", sourceType=");
		builder.append(sourceType);
		builder.append(", category=");
		builder.append(category);
		builder.append(", qty=");
		builder.append(qty);
		builder.append(", deviceCaegory=");
		builder.append(deviceCaegory);
		builder.append(", blockCategory=");
		builder.append(blockCategory);
		builder.append(", deviceQuantity=");
		builder.append(deviceQuantity);
		builder.append(", categoryInterp=");
		builder.append(categoryInterp);
		builder.append(", blockCategoryInterp=");
		builder.append(blockCategoryInterp);
		builder.append(", publicIp=");
		builder.append(publicIp);
		builder.append(", browser=");
		builder.append(browser);
		builder.append("]");
		return builder.toString();
	}



 
}
