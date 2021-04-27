package org.gl.ceir.CeirPannelCode.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EndUserVisaInfo {
	

private String commune;
private String country;
private String createdOn;
private String district;
private Integer docType;
private String docTypeInterp,documentInterp;
private String email;
private String firstName;
private Integer id;
private String isVip;
private String lastName;
private String locality;
private String middleName;
private String modifiedOn;
private String nationality;
private String nid;
private String onVisa;
private String phoneNo;
private Integer postalCode;
private String propertyLocation;
private String province,entryDateInCountry;;
private AllRequest auditParameters;
private List<UplodPaidStatusModel> regularizeDeviceDbs;
private String street;
private String txnId;
private String passportFileName;
private UserDepartment userDepartment;
private String village,rejectedRemark;
private List<VisaDb> visaDb;
private Map<String, Object> additionalProperties = new HashMap<String, Object>();
private String origin;
private String publicIp;
private String browser;
public String getCommune() {
	return commune;
}
public void setCommune(String commune) {
	this.commune = commune;
}
public String getCountry() {
	return country;
}
public void setCountry(String country) {
	this.country = country;
}
public String getCreatedOn() {
	return createdOn;
}
public void setCreatedOn(String createdOn) {
	this.createdOn = createdOn;
}
public String getDistrict() {
	return district;
}
public void setDistrict(String district) {
	this.district = district;
}
public Integer getDocType() {
	return docType;
}
public void setDocType(Integer docType) {
	this.docType = docType;
}


public String getDocTypeInterp() {
	return docTypeInterp;
}
public void setDocTypeInterp(String docTypeInterp) {
	this.docTypeInterp = docTypeInterp;
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
public Integer getId() {
	return id;
}
public void setId(Integer id) {
	this.id = id;
}
public String getIsVip() {
	return isVip;
}
public void setIsVip(String isVip) {
	this.isVip = isVip;
}
public String getLastName() {
	return lastName;
}
public void setLastName(String lastName) {
	this.lastName = lastName;
}
public String getLocality() {
	return locality;
}
public void setLocality(String locality) {
	this.locality = locality;
}
public String getMiddleName() {
	return middleName;
}
public void setMiddleName(String middleName) {
	this.middleName = middleName;
}
public String getModifiedOn() {
	return modifiedOn;
}
public void setModifiedOn(String modifiedOn) {
	this.modifiedOn = modifiedOn;
}
public String getNationality() {
	return nationality;
}
public void setNationality(String nationality) {
	this.nationality = nationality;
}
public String getNid() {
	return nid;
}
public void setNid(String nid) {
	this.nid = nid;
}
public String getOnVisa() {
	return onVisa;
}
public void setOnVisa(String onVisa) {
	this.onVisa = onVisa;
}
public String getPhoneNo() {
	return phoneNo;
}
public void setPhoneNo(String phoneNo) {
	this.phoneNo = phoneNo;
}
public Integer getPostalCode() {
	return postalCode;
}
public void setPostalCode(Integer postalCode) {
	this.postalCode = postalCode;
}
public String getPropertyLocation() {
	return propertyLocation;
}
public void setPropertyLocation(String propertyLocation) {
	this.propertyLocation = propertyLocation;
}
public String getProvince() {
	return province;
}
public void setProvince(String province) {
	this.province = province;
}
public List<UplodPaidStatusModel> getRegularizeDeviceDbs() {
	return regularizeDeviceDbs;
}
public void setRegularizeDeviceDbs(List<UplodPaidStatusModel> regularizeDeviceDbs) {
	this.regularizeDeviceDbs = regularizeDeviceDbs;
}
public String getStreet() {
	return street;
}
public void setStreet(String street) {
	this.street = street;
}
public String getTxnId() {
	return txnId;
}
public void setTxnId(String txnId) {
	this.txnId = txnId;
}
public String getPassportFileName() {
	return passportFileName;
}
public void setPassportFileName(String passportFileName) {
	this.passportFileName = passportFileName;
}
public UserDepartment getUserDepartment() {
	return userDepartment;
}
public void setUserDepartment(UserDepartment userDepartment) {
	this.userDepartment = userDepartment;
}
public String getVillage() {
	return village;
}
public void setVillage(String village) {
	this.village = village;
}
public List<VisaDb> getVisaDb() {
	return visaDb;
}
public void setVisaDb(List<VisaDb> visaDb) {
	this.visaDb = visaDb;
}
public Map<String, Object> getAdditionalProperties() {
	return additionalProperties;
}
public void setAdditionalProperties(Map<String, Object> additionalProperties) {
	this.additionalProperties = additionalProperties;
}
public String getOrigin() {
	return origin;
}
public void setOrigin(String origin) {
	this.origin = origin;
}
public String getEntryDateInCountry() {
	return entryDateInCountry;
}
public void setEntryDateInCountry(String entryDateInCountry) {
	this.entryDateInCountry = entryDateInCountry;
}
public String getRejectedRemark() {
	return rejectedRemark;
}
public void setRejectedRemark(String rejectedRemark) {
	this.rejectedRemark = rejectedRemark;
}
public AllRequest getAuditParameters() {
	return auditParameters;
}
public void setAuditParameters(AllRequest auditParameters) {
	this.auditParameters = auditParameters;
}
public String getDocumentInterp() {
	return documentInterp;
}
public void setDocumentInterp(String documentInterp) {
	this.documentInterp = documentInterp;
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
	builder.append("EndUserVisaInfo [commune=");
	builder.append(commune);
	builder.append(", country=");
	builder.append(country);
	builder.append(", createdOn=");
	builder.append(createdOn);
	builder.append(", district=");
	builder.append(district);
	builder.append(", docType=");
	builder.append(docType);
	builder.append(", docTypeInterp=");
	builder.append(docTypeInterp);
	builder.append(", documentInterp=");
	builder.append(documentInterp);
	builder.append(", email=");
	builder.append(email);
	builder.append(", firstName=");
	builder.append(firstName);
	builder.append(", id=");
	builder.append(id);
	builder.append(", isVip=");
	builder.append(isVip);
	builder.append(", lastName=");
	builder.append(lastName);
	builder.append(", locality=");
	builder.append(locality);
	builder.append(", middleName=");
	builder.append(middleName);
	builder.append(", modifiedOn=");
	builder.append(modifiedOn);
	builder.append(", nationality=");
	builder.append(nationality);
	builder.append(", nid=");
	builder.append(nid);
	builder.append(", onVisa=");
	builder.append(onVisa);
	builder.append(", phoneNo=");
	builder.append(phoneNo);
	builder.append(", postalCode=");
	builder.append(postalCode);
	builder.append(", propertyLocation=");
	builder.append(propertyLocation);
	builder.append(", province=");
	builder.append(province);
	builder.append(", entryDateInCountry=");
	builder.append(entryDateInCountry);
	builder.append(", auditParameters=");
	builder.append(auditParameters);
	builder.append(", regularizeDeviceDbs=");
	builder.append(regularizeDeviceDbs);
	builder.append(", street=");
	builder.append(street);
	builder.append(", txnId=");
	builder.append(txnId);
	builder.append(", passportFileName=");
	builder.append(passportFileName);
	builder.append(", userDepartment=");
	builder.append(userDepartment);
	builder.append(", village=");
	builder.append(village);
	builder.append(", rejectedRemark=");
	builder.append(rejectedRemark);
	builder.append(", visaDb=");
	builder.append(visaDb);
	builder.append(", additionalProperties=");
	builder.append(additionalProperties);
	builder.append(", origin=");
	builder.append(origin);
	builder.append(", publicIp=");
	builder.append(publicIp);
	builder.append(", browser=");
	builder.append(browser);
	builder.append("]");
	return builder.toString();
}




}
