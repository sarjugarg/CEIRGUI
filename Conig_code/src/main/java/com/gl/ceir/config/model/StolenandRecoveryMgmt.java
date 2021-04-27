package com.gl.ceir.config.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Audited
public class StolenandRecoveryMgmt implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long userId;

	@Column(length = 50)
	private String fileName;

	@Column(length = 50)
	private String firFileName;

	private Integer fileStatus;
	
	@NotNull
	private String txnId;

	@CreationTimestamp
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private LocalDateTime createdOn;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	@UpdateTimestamp
	private LocalDateTime modifiedOn;

	private Integer requestType;
	@Transient
	private String requestTypeInterp;

	private String roleType;
	@Column(name = "blockingType")
	private String blockingType;
	private String blockingTimePeriod;
	private Integer sourceType;
	
	@Column(name = "quantity")
	private Integer qty;

	
	private String remark;
	
	private String rejectedRemark;

	@Transient
	private String sourceTypeInterp;

	@Transient
	private String stateInterp;

	private Integer operatorTypeId;
	@Transient
	private String operatorTypeIdInterp;

	private Integer blockCategory;
	@Transient
	private String blockCategoryInterp;

	@Column(length = 25)
	private String dateOfStolen;

	@Column(length = 25)
	private String dateOfRecovery;

	private Integer complaintType;

	@NotAudited
	@OneToOne(mappedBy = "sARm", cascade = {CascadeType.PERSIST, CascadeType.REMOVE},fetch = FetchType.LAZY)
	SingleImeiDetails singleImeiDetails; 

	@NotAudited
	@OneToOne(mappedBy = "stolenandRecoveryMgmt", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	StolenIndividualUserDB stolenIndividualUserDB; 

	@NotAudited
	@OneToOne(mappedBy = "stolenandRecoveryMgmt", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	StolenOrganizationUserDB stolenOrganizationUserDB; 

	@Transient
	private String imei;

	private Integer deleteFlag;

	@Transient
	private String deleteFlagInterp;
	
	private Long ceirAdminId;
	@Column(name = "deviceQuantity")
	private Integer deviceQuantity;

	@Transient
	private String publicIp;
	@Transient
	private String browser;
	
	@OneToMany(
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
	private List<StolenAttachedFileInfo> attachedFiles = new ArrayList<>();
	
	public List<StolenAttachedFileInfo> getAttachedFiles() {
		return attachedFiles;
	}

	public void setAttachedFiles(List<StolenAttachedFileInfo> attachedFiles) {
		this.attachedFiles = attachedFiles;
	}

	
	public Long getCeirAdminId() {
		return ceirAdminId;
	}

	public void setCeirAdminId(Long ceirAdminId) {
		this.ceirAdminId = ceirAdminId;
	}

	public Integer getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getDeleteFlagInterp() {
		return deleteFlagInterp;
	}

	public void setDeleteFlagInterp(String deleteFlagInterp) {
		this.deleteFlagInterp = deleteFlagInterp;
	}

	public Integer getComplaintType() {
		return complaintType;
	}
	public void setComplaintType(Integer complaintType) {
		this.complaintType = complaintType;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
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
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public LocalDateTime getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(LocalDateTime modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	public Integer getRequestType() {
		return requestType;
	}
	public void setRequestType(Integer requestType) {
		this.requestType = requestType;
	}
	public String getRoleType() {
		return roleType;
	}
	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}
	public String getBlockingType() {
		return blockingType;
	}
	public void setBlockingType(String blockingType) {
		this.blockingType = blockingType;
	}
	public String getBlockingTimePeriod() {
		return blockingTimePeriod;
	}
	public void setBlockingTimePeriod(String blockingTimePeriod) {
		this.blockingTimePeriod = blockingTimePeriod;
	}
	public LocalDateTime getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}
	public Integer getSourceType() {
		return sourceType;
	}
	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}
	public SingleImeiDetails getSingleImeiDetails() {
		return singleImeiDetails;
	}
	public void setSingleImeiDetails(SingleImeiDetails singleImeiDetails) {
		this.singleImeiDetails = singleImeiDetails;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getRequestTypeInterp() {
		return requestTypeInterp;
	}
	public void setRequestTypeInterp(String requestTypeInterp) {
		this.requestTypeInterp = requestTypeInterp;
	}
	public String getSourceTypeInterp() {
		return sourceTypeInterp;
	}
	public void setSourceTypeInterp(String sourceTypeInterp) {
		this.sourceTypeInterp = sourceTypeInterp;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getStateInterp() {
		return stateInterp;
	}
	public void setStateInterp(String stateInterp) {
		this.stateInterp = stateInterp;
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
		builder.append("StolenandRecoveryMgmt [id=");
		builder.append(id);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", fileName=");
		builder.append(fileName);
		builder.append(", firFileName=");
		builder.append(firFileName);
		builder.append(", fileStatus=");
		builder.append(fileStatus);
		builder.append(", txnId=");
		builder.append(txnId);
		builder.append(", createdOn=");
		builder.append(createdOn);
		builder.append(", modifiedOn=");
		builder.append(modifiedOn);
		builder.append(", requestType=");
		builder.append(requestType);
		builder.append(", requestTypeInterp=");
		builder.append(requestTypeInterp);
		builder.append(", roleType=");
		builder.append(roleType);
		builder.append(", blockingType=");
		builder.append(blockingType);
		builder.append(", blockingTimePeriod=");
		builder.append(blockingTimePeriod);
		builder.append(", sourceType=");
		builder.append(sourceType);
		builder.append(", qty=");
		builder.append(qty);
		builder.append(", remark=");
		builder.append(remark);
		builder.append(", rejectedRemark=");
		builder.append(rejectedRemark);
		builder.append(", sourceTypeInterp=");
		builder.append(sourceTypeInterp);
		builder.append(", stateInterp=");
		builder.append(stateInterp);
		builder.append(", operatorTypeId=");
		builder.append(operatorTypeId);
		builder.append(", operatorTypeIdInterp=");
		builder.append(operatorTypeIdInterp);
		builder.append(", blockCategory=");
		builder.append(blockCategory);
		builder.append(", blockCategoryInterp=");
		builder.append(blockCategoryInterp);
		builder.append(", dateOfStolen=");
		builder.append(dateOfStolen);
		builder.append(", dateOfRecovery=");
		builder.append(dateOfRecovery);
		builder.append(", complaintType=");
		builder.append(complaintType);
		builder.append(", singleImeiDetails=");
		builder.append(singleImeiDetails);
		builder.append(", stolenIndividualUserDB=");
		builder.append(stolenIndividualUserDB);
		builder.append(", stolenOrganizationUserDB=");
		builder.append(stolenOrganizationUserDB);
		builder.append(", imei=");
		builder.append(imei);
		builder.append(", deleteFlag=");
		builder.append(deleteFlag);
		builder.append(", deleteFlagInterp=");
		builder.append(deleteFlagInterp);
		builder.append(", ceirAdminId=");
		builder.append(ceirAdminId);
		builder.append(", deviceQuantity=");
		builder.append(deviceQuantity);
		builder.append(", publicIp=");
		builder.append(publicIp);
		builder.append(", browser=");
		builder.append(browser);
		builder.append(", attachedFiles=");
		builder.append(attachedFiles);
		builder.append("]");
		return builder.toString();
	}

 


}
