package org.gl.ceir.pagination.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
@Component
public class StockContent {
	private Integer id;
	private String createdOn;
	private String modifiedOn;
	private String suplierName;
	private String supplierId;
	private String invoiceNumber;
	private String txnId;
	private String fileName;
	private Integer userId;
	private UserModel user;
	private String roleType;
	private Integer quantity;
	private Integer stockStatus;
	private Integer previousStockStatus;
	private Integer currency;
	private String userType;
	private Object totalPrice;
	private Object remarks;
	private Integer assignerId;
	private String stateInterp,deviceQuantity;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public String getSuplierName() {
		return suplierName;
	}
	public void setSuplierName(String suplierName) {
		this.suplierName = suplierName;
	}
	public String getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public UserModel getUser() {
		return user;
	}
	public void setUser(UserModel user) {
		this.user = user;
	}
	public String getRoleType() {
		return roleType;
	}
	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Integer getStockStatus() {
		return stockStatus;
	}
	public void setStockStatus(Integer stockStatus) {
		this.stockStatus = stockStatus;
	}
	public Integer getPreviousStockStatus() {
		return previousStockStatus;
	}
	public void setPreviousStockStatus(Integer previousStockStatus) {
		this.previousStockStatus = previousStockStatus;
	}
	public Integer getCurrency() {
		return currency;
	}
	public void setCurrency(Integer currency) {
		this.currency = currency;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public Object getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Object totalPrice) {
		this.totalPrice = totalPrice;
	}
	public Object getRemarks() {
		return remarks;
	}
	public void setRemarks(Object remarks) {
		this.remarks = remarks;
	}
	public Integer getAssignerId() {
		return assignerId;
	}
	public void setAssignerId(Integer assignerId) {
		this.assignerId = assignerId;
	}
	public String getStateInterp() {
		return stateInterp;
	}
	public void setStateInterp(String stateInterp) {
		this.stateInterp = stateInterp;
	}
	public String getDeviceQuantity() {
		return deviceQuantity;
	}
	public void setDeviceQuantity(String deviceQuantity) {
		this.deviceQuantity = deviceQuantity;
	}
	public Map<String, Object> getAdditionalProperties() {
		return additionalProperties;
	}
	public void setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("StockContent [id=");
		builder.append(id);
		builder.append(", createdOn=");
		builder.append(createdOn);
		builder.append(", modifiedOn=");
		builder.append(modifiedOn);
		builder.append(", suplierName=");
		builder.append(suplierName);
		builder.append(", supplierId=");
		builder.append(supplierId);
		builder.append(", invoiceNumber=");
		builder.append(invoiceNumber);
		builder.append(", txnId=");
		builder.append(txnId);
		builder.append(", fileName=");
		builder.append(fileName);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", user=");
		builder.append(user);
		builder.append(", roleType=");
		builder.append(roleType);
		builder.append(", quantity=");
		builder.append(quantity);
		builder.append(", stockStatus=");
		builder.append(stockStatus);
		builder.append(", previousStockStatus=");
		builder.append(previousStockStatus);
		builder.append(", currency=");
		builder.append(currency);
		builder.append(", userType=");
		builder.append(userType);
		builder.append(", totalPrice=");
		builder.append(totalPrice);
		builder.append(", remarks=");
		builder.append(remarks);
		builder.append(", assignerId=");
		builder.append(assignerId);
		builder.append(", stateInterp=");
		builder.append(stateInterp);
		builder.append(", deviceQuantity=");
		builder.append(deviceQuantity);
		builder.append(", additionalProperties=");
		builder.append(additionalProperties);
		builder.append("]");
		return builder.toString();
	}
	
	
}
