package org.gl.ceir.pagination.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gl.ceir.CeirPannelCode.Model.EndUserVisaInfo;
import org.gl.ceir.CeirPannelCode.Model.UplodPaidStatusModel;
import org.gl.ceir.CeirPannelCode.Model.UserDepartment;
import org.gl.ceir.CeirPannelCode.Model.VisaDb;
import org.springframework.stereotype.Component;
@Component
public class UserPaidStatusContent {
	private Integer id;
	private Integer status;
	private String stateInterp;
	private String createdOn;
	private String modifiedOn;
	private String nid;
	private Integer deviceStatus;
	private Integer taxPaidStatus;
	private String taxPaidStatusInterp;
	private Integer deviceType;
	private String deviceTypeInterp;
	private Integer deviceIdType;
	private String deviceIdTypeInterp;
	private String multiSimStatus,multiSimStatusInterp;
	private String country;
	private String deviceSerialNumber;
	private String txnId;
	private String deviceStatusInterp;
	private Double price;
	private Integer currency;
	private String currencyInterp;
	private String firstImei;
	private String secondImei;
	private String thirdImei;
	private String fourthImei,filePreviewLink;
	private UserDepartment userDepartment;
	private List<UplodPaidStatusModel> regularizeDeviceDbs;
	private List<VisaDb> visaDb;
	private EndUserVisaInfo endUserDB;
	private String origin,nationality,deviceRemark;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getStateInterp() {
		return stateInterp;
	}
	public void setStateInterp(String stateInterp) {
		this.stateInterp = stateInterp;
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
	public String getNid() {
		return nid;
	}
	public void setNid(String nid) {
		this.nid = nid;
	}
	public Integer getDeviceStatus() {
		return deviceStatus;
	}
	public void setDeviceStatus(Integer deviceStatus) {
		this.deviceStatus = deviceStatus;
	}
	public Integer getTaxPaidStatus() {
		return taxPaidStatus;
	}
	public void setTaxPaidStatus(Integer taxPaidStatus) {
		this.taxPaidStatus = taxPaidStatus;
	}
	public String getTaxPaidStatusInterp() {
		return taxPaidStatusInterp;
	}
	public void setTaxPaidStatusInterp(String taxPaidStatusInterp) {
		this.taxPaidStatusInterp = taxPaidStatusInterp;
	}
	public Integer getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(Integer deviceType) {
		this.deviceType = deviceType;
	}
	public String getDeviceTypeInterp() {
		return deviceTypeInterp;
	}
	public void setDeviceTypeInterp(String deviceTypeInterp) {
		this.deviceTypeInterp = deviceTypeInterp;
	}
	public Integer getDeviceIdType() {
		return deviceIdType;
	}
	public void setDeviceIdType(Integer deviceIdType) {
		this.deviceIdType = deviceIdType;
	}
	public String getDeviceIdTypeInterp() {
		return deviceIdTypeInterp;
	}
	public void setDeviceIdTypeInterp(String deviceIdTypeInterp) {
		this.deviceIdTypeInterp = deviceIdTypeInterp;
	}
	public String getMultiSimStatus() {
		return multiSimStatus;
	}
	public void setMultiSimStatus(String multiSimStatus) {
		this.multiSimStatus = multiSimStatus;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getDeviceSerialNumber() {
		return deviceSerialNumber;
	}
	public void setDeviceSerialNumber(String deviceSerialNumber) {
		this.deviceSerialNumber = deviceSerialNumber;
	}
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public String getDeviceStatusInterp() {
		return deviceStatusInterp;
	}
	public void setDeviceStatusInterp(String deviceStatusInterp) {
		this.deviceStatusInterp = deviceStatusInterp;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Integer getCurrency() {
		return currency;
	}
	public void setCurrency(Integer currency) {
		this.currency = currency;
	}
	public String getCurrencyInterp() {
		return currencyInterp;
	}
	public void setCurrencyInterp(String currencyInterp) {
		this.currencyInterp = currencyInterp;
	}
	public String getFirstImei() {
		return firstImei;
	}
	public void setFirstImei(String firstImei) {
		this.firstImei = firstImei;
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
	public String getFourthImei() {
		return fourthImei;
	}
	public void setFourthImei(String fourthImei) {
		this.fourthImei = fourthImei;
	}
	public String getFilePreviewLink() {
		return filePreviewLink;
	}
	public void setFilePreviewLink(String filePreviewLink) {
		this.filePreviewLink = filePreviewLink;
	}
	public UserDepartment getUserDepartment() {
		return userDepartment;
	}
	public void setUserDepartment(UserDepartment userDepartment) {
		this.userDepartment = userDepartment;
	}
	public List<UplodPaidStatusModel> getRegularizeDeviceDbs() {
		return regularizeDeviceDbs;
	}
	public void setRegularizeDeviceDbs(List<UplodPaidStatusModel> regularizeDeviceDbs) {
		this.regularizeDeviceDbs = regularizeDeviceDbs;
	}
	public List<VisaDb> getVisaDb() {
		return visaDb;
	}
	public void setVisaDb(List<VisaDb> visaDb) {
		this.visaDb = visaDb;
	}
	public EndUserVisaInfo getEndUserDB() {
		return endUserDB;
	}
	public void setEndUserDB(EndUserVisaInfo endUserDB) {
		this.endUserDB = endUserDB;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getMultiSimStatusInterp() {
		return multiSimStatusInterp;
	}
	public void setMultiSimStatusInterp(String multiSimStatusInterp) {
		this.multiSimStatusInterp = multiSimStatusInterp;
	}
	public String getDeviceRemark() {
		return deviceRemark;
	}
	public void setDeviceRemark(String deviceRemark) {
		this.deviceRemark = deviceRemark;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserPaidStatusContent [id=");
		builder.append(id);
		builder.append(", status=");
		builder.append(status);
		builder.append(", stateInterp=");
		builder.append(stateInterp);
		builder.append(", createdOn=");
		builder.append(createdOn);
		builder.append(", modifiedOn=");
		builder.append(modifiedOn);
		builder.append(", nid=");
		builder.append(nid);
		builder.append(", deviceStatus=");
		builder.append(deviceStatus);
		builder.append(", taxPaidStatus=");
		builder.append(taxPaidStatus);
		builder.append(", taxPaidStatusInterp=");
		builder.append(taxPaidStatusInterp);
		builder.append(", deviceType=");
		builder.append(deviceType);
		builder.append(", deviceTypeInterp=");
		builder.append(deviceTypeInterp);
		builder.append(", deviceIdType=");
		builder.append(deviceIdType);
		builder.append(", deviceIdTypeInterp=");
		builder.append(deviceIdTypeInterp);
		builder.append(", multiSimStatus=");
		builder.append(multiSimStatus);
		builder.append(", multiSimStatusInterp=");
		builder.append(multiSimStatusInterp);
		builder.append(", country=");
		builder.append(country);
		builder.append(", deviceSerialNumber=");
		builder.append(deviceSerialNumber);
		builder.append(", txnId=");
		builder.append(txnId);
		builder.append(", deviceStatusInterp=");
		builder.append(deviceStatusInterp);
		builder.append(", price=");
		builder.append(price);
		builder.append(", currency=");
		builder.append(currency);
		builder.append(", currencyInterp=");
		builder.append(currencyInterp);
		builder.append(", firstImei=");
		builder.append(firstImei);
		builder.append(", secondImei=");
		builder.append(secondImei);
		builder.append(", thirdImei=");
		builder.append(thirdImei);
		builder.append(", fourthImei=");
		builder.append(fourthImei);
		builder.append(", filePreviewLink=");
		builder.append(filePreviewLink);
		builder.append(", userDepartment=");
		builder.append(userDepartment);
		builder.append(", regularizeDeviceDbs=");
		builder.append(regularizeDeviceDbs);
		builder.append(", visaDb=");
		builder.append(visaDb);
		builder.append(", endUserDB=");
		builder.append(endUserDB);
		builder.append(", origin=");
		builder.append(origin);
		builder.append(", nationality=");
		builder.append(nationality);
		builder.append(", deviceRemark=");
		builder.append(deviceRemark);
		builder.append("]");
		return builder.toString();
	}
	
	
	
}
