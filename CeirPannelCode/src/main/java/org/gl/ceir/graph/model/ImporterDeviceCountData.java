package org.gl.ceir.graph.model;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
@Component
public class ImporterDeviceCountData {
	  @JsonProperty("Date")
	  @SerializedName("Date")
	  private String date;
	  
	  @JsonProperty("Month")
	  @SerializedName("Month")
	  private String month;
	  
	  @JsonProperty("Importer")
	  @SerializedName("Importer")
	  private String importer;

	  @JsonProperty("Importer & Distributor")
	  @SerializedName("Importer & Distributor")
	  private String importerDistributor;

	  @JsonProperty("Importer & Retailer")
	  @SerializedName("Importer & Retailer")
	  private String importerRetailer;
	  
	  @JsonProperty("Importer & Distributor & Retailer")
	  @SerializedName("Importer & Distributor & Retailer")
	  private String importerDistributorRetailer;
	  
	  @JsonProperty("Import Grievance")
	  @SerializedName("Import Grievance")
	  private String importGrievance;

	  @JsonProperty("Total Importer Device Count")
	  @SerializedName("Total Importer Device Count")
	  private String importerDevice;
	  
	  @JsonProperty("Total Importer User Count")
	  @SerializedName("Total Importer User Count")
	  private String importerUserFound;
	  
	  @JsonProperty("Total Stock Uploaded Importer")
	  @SerializedName("Total Stock Uploaded Importer")
	  private String importerStockFound;

	  @JsonProperty("Count")
	  @SerializedName("Count")
	  private String count;

	  
	  @JsonProperty("Withdrawn By CEIR Admin")
	  @SerializedName("Withdrawn By CEIR Admin")
	  private String withdrawnByCEIRAdmin;

	
	  @JsonProperty("Rejected by CEIR Admin")
	  @SerializedName("Rejected by CEIR Admin")
	  private String rejectedbyCEIRAdmin;
	  
	  @JsonProperty("Rejected By System")
	  @SerializedName("Rejected By System")
	  private String rejectedBySystem;
	  
	  @JsonProperty("Processing")
	  @SerializedName("Processing")
	  private String processing;

	  @JsonProperty("Pending Approval From CEIR Admin")
	  @SerializedName("Pending Approval From CEIR Admin")
	  private String pendingApprovalFromCEIRAdmin;
	  
	  @JsonProperty("Approved by CEIR Admin")
	  @SerializedName("Approved by CEIR Admin")
	  private String approvedbyCEIRAdmin;
	  
	  @JsonProperty("Withdrawn By User")
	  @SerializedName("Withdrawn By User")
	  private String withdrawnByUser;
	  
	  @JsonProperty("Approved")
	  @SerializedName("Approved")
	  private String approved;
	  
	  
	  @JsonProperty("Pending Clearance From Customs")
	  @SerializedName("Pending Clearance From Customs")
	  private String pendingClearanceFromCustoms;
	  
	  @JsonProperty("Pending Clearance from DRT")
	  @SerializedName("Pending Clearance from DRT")
	  private String pendingClearancefromDRT;
	  
	  
	  @JsonProperty("Withdrawn By Importer")
	  @SerializedName("Withdrawn By Importer")
	  private String withdrawnByImporter;
	  
	  
	  @JsonProperty("Rejected By Customs")
	  @SerializedName("Rejected By Customs")
	  private String rejectedByCustoms;
	  
	  
	  @JsonProperty("Rejected by DRT")
	  @SerializedName("Rejected by DRT")
	  private String rejectedbyDRT;
	  
	  
	  @JsonProperty("Withdrawn By CEIR")
	  @SerializedName("Withdrawn By CEIR")
	  private String withdrawnByCEIR;
	  
	  
	  
	  @JsonProperty("New")
	  @SerializedName("New")
	  private String newState;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getImporter() {
		return importer;
	}

	public void setImporter(String importer) {
		this.importer = importer;
	}

	public String getImporterDistributor() {
		return importerDistributor;
	}

	public void setImporterDistributor(String importerDistributor) {
		this.importerDistributor = importerDistributor;
	}

	public String getImporterRetailer() {
		return importerRetailer;
	}

	public void setImporterRetailer(String importerRetailer) {
		this.importerRetailer = importerRetailer;
	}

	public String getImporterDistributorRetailer() {
		return importerDistributorRetailer;
	}

	public void setImporterDistributorRetailer(String importerDistributorRetailer) {
		this.importerDistributorRetailer = importerDistributorRetailer;
	}

	public String getImportGrievance() {
		return importGrievance;
	}

	public void setImportGrievance(String importGrievance) {
		this.importGrievance = importGrievance;
	}

	public String getImporterDevice() {
		return importerDevice;
	}

	public void setImporterDevice(String importerDevice) {
		this.importerDevice = importerDevice;
	}

	public String getImporterUserFound() {
		return importerUserFound;
	}

	public void setImporterUserFound(String importerUserFound) {
		this.importerUserFound = importerUserFound;
	}

	public String getImporterStockFound() {
		return importerStockFound;
	}

	public void setImporterStockFound(String importerStockFound) {
		this.importerStockFound = importerStockFound;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getWithdrawnByCEIRAdmin() {
		return withdrawnByCEIRAdmin;
	}

	public void setWithdrawnByCEIRAdmin(String withdrawnByCEIRAdmin) {
		this.withdrawnByCEIRAdmin = withdrawnByCEIRAdmin;
	}

	public String getRejectedbyCEIRAdmin() {
		return rejectedbyCEIRAdmin;
	}

	public void setRejectedbyCEIRAdmin(String rejectedbyCEIRAdmin) {
		this.rejectedbyCEIRAdmin = rejectedbyCEIRAdmin;
	}

	public String getRejectedBySystem() {
		return rejectedBySystem;
	}

	public void setRejectedBySystem(String rejectedBySystem) {
		this.rejectedBySystem = rejectedBySystem;
	}

	public String getProcessing() {
		return processing;
	}

	public void setProcessing(String processing) {
		this.processing = processing;
	}

	public String getPendingApprovalFromCEIRAdmin() {
		return pendingApprovalFromCEIRAdmin;
	}

	public void setPendingApprovalFromCEIRAdmin(String pendingApprovalFromCEIRAdmin) {
		this.pendingApprovalFromCEIRAdmin = pendingApprovalFromCEIRAdmin;
	}

	public String getApprovedbyCEIRAdmin() {
		return approvedbyCEIRAdmin;
	}

	public void setApprovedbyCEIRAdmin(String approvedbyCEIRAdmin) {
		this.approvedbyCEIRAdmin = approvedbyCEIRAdmin;
	}

	public String getWithdrawnByUser() {
		return withdrawnByUser;
	}

	public void setWithdrawnByUser(String withdrawnByUser) {
		this.withdrawnByUser = withdrawnByUser;
	}

	public String getNewState() {
		return newState;
	}

	public void setNewState(String newState) {
		this.newState = newState;
	}

	public String getApproved() {
		return approved;
	}

	public void setApproved(String approved) {
		this.approved = approved;
	}

	public String getPendingClearanceFromCustoms() {
		return pendingClearanceFromCustoms;
	}

	public void setPendingClearanceFromCustoms(String pendingClearanceFromCustoms) {
		this.pendingClearanceFromCustoms = pendingClearanceFromCustoms;
	}

	public String getPendingClearancefromDRT() {
		return pendingClearancefromDRT;
	}

	public void setPendingClearancefromDRT(String pendingClearancefromDRT) {
		this.pendingClearancefromDRT = pendingClearancefromDRT;
	}

	public String getWithdrawnByImporter() {
		return withdrawnByImporter;
	}

	public void setWithdrawnByImporter(String withdrawnByImporter) {
		this.withdrawnByImporter = withdrawnByImporter;
	}

	public String getRejectedByCustoms() {
		return rejectedByCustoms;
	}

	public void setRejectedByCustoms(String rejectedByCustoms) {
		this.rejectedByCustoms = rejectedByCustoms;
	}

	public String getRejectedbyDRT() {
		return rejectedbyDRT;
	}

	public void setRejectedbyDRT(String rejectedbyDRT) {
		this.rejectedbyDRT = rejectedbyDRT;
	}

	public String getWithdrawnByCEIR() {
		return withdrawnByCEIR;
	}

	public void setWithdrawnByCEIR(String withdrawnByCEIR) {
		this.withdrawnByCEIR = withdrawnByCEIR;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ImporterDeviceCountData [date=");
		builder.append(date);
		builder.append(", month=");
		builder.append(month);
		builder.append(", importer=");
		builder.append(importer);
		builder.append(", importerDistributor=");
		builder.append(importerDistributor);
		builder.append(", importerRetailer=");
		builder.append(importerRetailer);
		builder.append(", importerDistributorRetailer=");
		builder.append(importerDistributorRetailer);
		builder.append(", importGrievance=");
		builder.append(importGrievance);
		builder.append(", importerDevice=");
		builder.append(importerDevice);
		builder.append(", importerUserFound=");
		builder.append(importerUserFound);
		builder.append(", importerStockFound=");
		builder.append(importerStockFound);
		builder.append(", count=");
		builder.append(count);
		builder.append(", withdrawnByCEIRAdmin=");
		builder.append(withdrawnByCEIRAdmin);
		builder.append(", rejectedbyCEIRAdmin=");
		builder.append(rejectedbyCEIRAdmin);
		builder.append(", rejectedBySystem=");
		builder.append(rejectedBySystem);
		builder.append(", processing=");
		builder.append(processing);
		builder.append(", pendingApprovalFromCEIRAdmin=");
		builder.append(pendingApprovalFromCEIRAdmin);
		builder.append(", approvedbyCEIRAdmin=");
		builder.append(approvedbyCEIRAdmin);
		builder.append(", withdrawnByUser=");
		builder.append(withdrawnByUser);
		builder.append(", approved=");
		builder.append(approved);
		builder.append(", pendingClearanceFromCustoms=");
		builder.append(pendingClearanceFromCustoms);
		builder.append(", pendingClearancefromDRT=");
		builder.append(pendingClearancefromDRT);
		builder.append(", withdrawnByImporter=");
		builder.append(withdrawnByImporter);
		builder.append(", rejectedByCustoms=");
		builder.append(rejectedByCustoms);
		builder.append(", rejectedbyDRT=");
		builder.append(rejectedbyDRT);
		builder.append(", withdrawnByCEIR=");
		builder.append(withdrawnByCEIR);
		builder.append(", newState=");
		builder.append(newState);
		builder.append("]");
		return builder.toString();
	}


}
