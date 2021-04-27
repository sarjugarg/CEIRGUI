package org.gl.ceir.pagination.model;
import java.util.HashMap;
import java.util.Map;

public class ConsignmentContent {
	private Integer id;
	private String supplierId;
	private String supplierName;
	private String consignmentNumber;
	private Integer taxPaidStatus;
	private String createdOn;
	private String modifiedOn;
	private Integer userId;
	private String txnId;
	private String fileName;
	private Integer consignmentStatus;
	private Integer previousConsignmentStatus;
	private String organisationCountry;
	private String expectedDispatcheDate;
	private String expectedArrivaldate;
	private String expectedArrivalPort;
	private Integer quantity;
	private String remarks;
	private Integer currency;
	private Object totalPrice;
	private String stateInterp;
	private String taxInterp;
	private UserModel user;
	private String supplierld;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	private int deviceQuantity;
	private String portAddressInterp;
	public String getPortAddressInterp() {
		return portAddressInterp;
	}
	public void setPortAddressInterp(String portAddressInterp) {
		this.portAddressInterp = portAddressInterp;
	}
	public int getDeviceQuantity() {
		return deviceQuantity;
	}
	public void setDeviceQuantity(int deviceQuantity) {
		this.deviceQuantity = deviceQuantity;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getConsignmentNumber() {
		return consignmentNumber;
	}
	public void setConsignmentNumber(String consignmentNumber) {
		this.consignmentNumber = consignmentNumber;
	}
	public Integer getTaxPaidStatus() {
		return taxPaidStatus;
	}
	public void setTaxPaidStatus(Integer taxPaidStatus) {
		this.taxPaidStatus = taxPaidStatus;
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
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
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
	public Integer getConsignmentStatus() {
		return consignmentStatus;
	}
	public void setConsignmentStatus(Integer consignmentStatus) {
		this.consignmentStatus = consignmentStatus;
	}
	public Integer getPreviousConsignmentStatus() {
		return previousConsignmentStatus;
	}
	public void setPreviousConsignmentStatus(Integer previousConsignmentStatus) {
		this.previousConsignmentStatus = previousConsignmentStatus;
	}
	public String getOrganisationCountry() {
		return organisationCountry;
	}
	public void setOrganisationCountry(String organisationCountry) {
		this.organisationCountry = organisationCountry;
	}
	public String getExpectedDispatcheDate() {
		return expectedDispatcheDate;
	}
	public void setExpectedDispatcheDate(String expectedDispatcheDate) {
		this.expectedDispatcheDate = expectedDispatcheDate;
	}
	public String getExpectedArrivaldate() {
		return expectedArrivaldate;
	}
	public void setExpectedArrivaldate(String expectedArrivaldate) {
		this.expectedArrivaldate = expectedArrivaldate;
	}
	public String getExpectedArrivalPort() {
		return expectedArrivalPort;
	}
	public void setExpectedArrivalPort(String expectedArrivalPort) {
		this.expectedArrivalPort = expectedArrivalPort;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Integer getCurrency() {
		return currency;
	}
	public void setCurrency(Integer currency) {
		this.currency = currency;
	}
	public Object getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Object totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getStateInterp() {
		return stateInterp;
	}
	public void setStateInterp(String stateInterp) {
		this.stateInterp = stateInterp;
	}
	public String getTaxInterp() {
		return taxInterp;
	}
	public void setTaxInterp(String taxInterp) {
		this.taxInterp = taxInterp;
	}
	public UserModel getUser() {
		return user;
	}
	public void setUser(UserModel user) {
		this.user = user;
	}
	public String getSupplierld() {
		return supplierld;
	}
	public void setSupplierld(String supplierld) {
		this.supplierld = supplierld;
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
		builder.append("ConsignmentContent [id=");
		builder.append(id);
		builder.append(", supplierId=");
		builder.append(supplierId);
		builder.append(", supplierName=");
		builder.append(supplierName);
		builder.append(", consignmentNumber=");
		builder.append(consignmentNumber);
		builder.append(", taxPaidStatus=");
		builder.append(taxPaidStatus);
		builder.append(", createdOn=");
		builder.append(createdOn);
		builder.append(", modifiedOn=");
		builder.append(modifiedOn);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", txnId=");
		builder.append(txnId);
		builder.append(", fileName=");
		builder.append(fileName);
		builder.append(", consignmentStatus=");
		builder.append(consignmentStatus);
		builder.append(", previousConsignmentStatus=");
		builder.append(previousConsignmentStatus);
		builder.append(", organisationCountry=");
		builder.append(organisationCountry);
		builder.append(", expectedDispatcheDate=");
		builder.append(expectedDispatcheDate);
		builder.append(", expectedArrivaldate=");
		builder.append(expectedArrivaldate);
		builder.append(", expectedArrivalPort=");
		builder.append(expectedArrivalPort);
		builder.append(", quantity=");
		builder.append(quantity);
		builder.append(", remarks=");
		builder.append(remarks);
		builder.append(", currency=");
		builder.append(currency);
		builder.append(", totalPrice=");
		builder.append(totalPrice);
		builder.append(", stateInterp=");
		builder.append(stateInterp);
		builder.append(", taxInterp=");
		builder.append(taxInterp);
		builder.append(", user=");
		builder.append(user);
		builder.append(", supplierld=");
		builder.append(supplierld);
		builder.append(", additionalProperties=");
		builder.append(additionalProperties);
		builder.append(", deviceQuantity=");
		builder.append(deviceQuantity);
		builder.append(", portAddressInterp=");
		builder.append(portAddressInterp);
		builder.append("]");
		return builder.toString();
	}
	
}
