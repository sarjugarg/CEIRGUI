package com.gl.ceir.config.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Audited
//@JsonIdentityInfo(
//		  generator = ObjectIdGenerators.PropertyGenerator.class, 
//		  property = "id"
//)
public class EndUserDB   {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private LocalDateTime createdOn;
	
	@UpdateTimestamp
	private LocalDateTime modifiedOn;

	@Column(unique = true)
	private String nid;
	
	@NotNull
	private String txnId;
	private String firstName;  
	private String middleName;
	private String lastName;
	private String propertyLocation;
	private String street;
	private String locality;
	
	@NotNull
	@Column(length = 50)
	private String district;
	
	@NotNull
	@Column(length = 50)
	private String commune;
	
//	@NotNull
	@Column(length = 50)
	private String village;
	
	@NotNull
	private Integer postalCode;
	
	private String province;
	private String country;
	private String email;
	private String phoneNo;
	
	private Integer docType;
	@Transient
	private String docTypeInterp;
	
	@Transient
	private String documentInterp;
	

	@NotAudited
	@OneToMany(mappedBy = "endUserDB",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
//	@JsonManagedReference("device-info")
	private List<RegularizeDeviceDb> regularizeDeviceDbs ;
	
	@Column(length = 50)
	@NotNull
	private String nationality;
	
	@Column(length = 1)
	@ColumnDefault("N")
	private String onVisa;
	
	@NotAudited
	@OneToMany(mappedBy = "endUserDB", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<VisaDb> visaDb;

	@NotAudited
	@JsonIgnore
	@OneToMany(mappedBy = "endUserDBData", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<VisaUpdateDb> visaUpdateDb;
	
	@Column(length = 1)
	@ColumnDefault("N")
	private String isVip;
	
	@NotAudited
	@OneToOne(mappedBy = "endUserDB", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
	private UserDepartment userDepartment;
	
	@Column(length = 50)
	private String passportFileName;
	
	private long creatorUserId;
	
	private Integer status;
	
	private String remarks;
	
	@NotNull
	@Column(length = 20)
	private String origin;
	
	private String entryDateInCountry;

	@Transient
	private String rejectedRemark;
	
	@Transient
	private AllRequest auditParameters;
	
	@Transient
	private String publicIp;
	
	@Transient
	private String browser;
	
	public String getRejectedRemark() {
		return rejectedRemark;
	}
	public void setRejectedRemark(String rejectedRemark) {
		this.rejectedRemark = rejectedRemark;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public LocalDateTime getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}
	public LocalDateTime getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(LocalDateTime modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	public String getNid() {
		return nid;
	}
	public void setNid(String nid) {
		this.nid = nid;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPropertyLocation() {
		return propertyLocation;
	}
	public void setPropertyLocation(String propertyLocation) {
		this.propertyLocation = propertyLocation;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getLocality() {
		return locality;
	}
	public void setLocality(String locality) {
		this.locality = locality;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	
	public String getDocumentInterp() {
		return documentInterp;
	}
	public void setDocumentInterp(String documentInterp) {
		this.documentInterp = documentInterp;
	}
	public AllRequest getAuditParameters() {
		return auditParameters;
	}
	public void setAuditParameters(AllRequest auditParameters) {
		this.auditParameters = auditParameters;
	}
	public List<RegularizeDeviceDb> getRegularizeDeviceDbs() {
		return regularizeDeviceDbs;
	}
	public void setRegularizeDeviceDbs(List<RegularizeDeviceDb> regularizeDeviceDbs) {
		this.regularizeDeviceDbs = regularizeDeviceDbs;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getCommune() {
		return commune;
	}
	public void setCommune(String commune) {
		this.commune = commune;
	}
	public String getVillage() {
		return village;
	}
	public void setVillage(String village) {
		this.village = village;
	}
	public Integer getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(Integer postalCode) {
		this.postalCode = postalCode;
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
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getOnVisa() {
		return onVisa;
	}
	public void setOnVisa(String onVisa) {
		this.onVisa = onVisa;
	}
	
	public List<VisaDb> getVisaDb() {
		return visaDb;
	}
	public void setVisaDb(List<VisaDb> visaDb) {
		this.visaDb = visaDb;
	}
	public String getIsVip() {
		return isVip;
	}
	public void setIsVip(String isVip) {
		this.isVip = isVip;
	}
	public UserDepartment getUserDepartment() {
		return userDepartment;
	}
	public void setUserDepartment(UserDepartment userDepartment) {
		this.userDepartment = userDepartment;
	}
	
	public String getPassportFileName() {
		return passportFileName;
	}
	public void setPassportFileName(String passportFileName) {
		this.passportFileName = passportFileName;
	}
	
	public long getCreatorUserId() {
		return creatorUserId;
	}
	public void setCreatorUserId(long creatorUserId) {
		this.creatorUserId = creatorUserId;
	}

	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
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
	public List<VisaUpdateDb> getVisaUpdateDb() {
		return visaUpdateDb;
	}
	public void setVisaUpdateDb(List<VisaUpdateDb> visaUpdateDb) {
		this.visaUpdateDb = visaUpdateDb;
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
	

}
