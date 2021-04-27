package org.gl.ceir.CeirPannelCode.Model;

public class StockUploadModel {
	
	
	private String fileName;
	private int id;
	private String invoiceNumber;
	private String roleType;
	private String suplierName;
	private String txnId;
	private int quantity;
	private int stockStatus;
	private String supplierId;
	private Integer deviceQuantity,featureId;
	public String publicIp , browser;
	public Integer getDeviceQuantity() {
		return deviceQuantity;
	}
	public void setDeviceQuantity(Integer deviceQuantity) {
		this.deviceQuantity = deviceQuantity;
	}
	private String userType,remarks,createdOn,modifiedOn,stateInterp;
	private Long assignerId;
	private StockUserModel user;
	
	
	private int userId,roleTypeId;
	
	public int getRoleTypeId() {
		return roleTypeId;
	}
	public void setRoleTypeId(int roleTypeId) {
		this.roleTypeId = roleTypeId;
	}
	
	public Long getAssignerId() {
		return assignerId;
	}
	public void setAssignerId(Long assignerId) {
		this.assignerId = assignerId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public String getRoleType() {
		return roleType;
	}
	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}
	public String getSuplierName() {
		return suplierName;
	}
	public void setSuplierName(String suplierName) {
		this.suplierName = suplierName;
	}
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getStockStatus() {
		return stockStatus;
	}
	public void setStockStatus(int stockStatus) {
		this.stockStatus = stockStatus;
	}
	public String getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
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
	public String getStateInterp() {
		return stateInterp;
	}
	public void setStateInterp(String stateInterp) {
		this.stateInterp = stateInterp;
	}
	public StockUserModel getUser() {
		return user;
	}
	public void setUser(StockUserModel user) {
		this.user = user;
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
		builder.append("StockUploadModel [fileName=");
		builder.append(fileName);
		builder.append(", id=");
		builder.append(id);
		builder.append(", invoiceNumber=");
		builder.append(invoiceNumber);
		builder.append(", roleType=");
		builder.append(roleType);
		builder.append(", suplierName=");
		builder.append(suplierName);
		builder.append(", txnId=");
		builder.append(txnId);
		builder.append(", quantity=");
		builder.append(quantity);
		builder.append(", stockStatus=");
		builder.append(stockStatus);
		builder.append(", supplierId=");
		builder.append(supplierId);
		builder.append(", deviceQuantity=");
		builder.append(deviceQuantity);
		builder.append(", featureId=");
		builder.append(featureId);
		builder.append(", publicIp=");
		builder.append(publicIp);
		builder.append(", browser=");
		builder.append(browser);
		builder.append(", userType=");
		builder.append(userType);
		builder.append(", remarks=");
		builder.append(remarks);
		builder.append(", createdOn=");
		builder.append(createdOn);
		builder.append(", modifiedOn=");
		builder.append(modifiedOn);
		builder.append(", stateInterp=");
		builder.append(stateInterp);
		builder.append(", assignerId=");
		builder.append(assignerId);
		builder.append(", user=");
		builder.append(user);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", roleTypeId=");
		builder.append(roleTypeId);
		builder.append("]");
		return builder.toString();
	}
	
	
	
}
