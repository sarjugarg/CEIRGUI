package org.gl.ceir.graph.model;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
@Component
public class DistributorDeviceCountData {

	  @JsonProperty("Date")
	  @SerializedName("Date")
	  private String date;
	  
	  @JsonProperty("Month")
	  @SerializedName("Month")
	  private String month;
	  
	  @JsonProperty("Distributor")
	  @SerializedName("Distributor")
	  private String distributor;
	  
	  @JsonProperty("Distributor & Importer")
	  @SerializedName("Distributor & Importer")
	  private String distributorImporter;
	  
	  @JsonProperty("Distributor & Retailer")
	  @SerializedName("Distributor & Retailer")
	  private String distributorRetailer;
	  
	  @JsonProperty("Distributor & Importer & Retailer")
	  @SerializedName("Distributor & Importer & Retailer")
	  private String distributorImporterRetailer;
	  
	  @JsonProperty("Distributor Grievance")
	  @SerializedName("Distributor Grievance")
	  private String distributorGrievance;
	  
	  @JsonProperty("Total Stock Uploaded Distributor")
	  @SerializedName("Total Stock Uploaded Distributor")
	  private String distributorStock;
	  
	  @JsonProperty("Total Distributor Device Count")
	  @SerializedName("Total Distributor Device Count")
	  private String distributorDeviceCount;
	  
	  @JsonProperty("Total Distributor User Count")
	  @SerializedName("Total Distributor User Count")
	  private String totalDistributor;

	  
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
	  
	  @JsonProperty("New")
	  @SerializedName("New")
	  private String newState;
	  
	  
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDistributor() {
		return distributor;
	}

	public void setDistributor(String distributor) {
		this.distributor = distributor;
	}

	public String getDistributorImporter() {
		return distributorImporter;
	}

	public void setDistributorImporter(String distributorImporter) {
		this.distributorImporter = distributorImporter;
	}

	public String getDistributorRetailer() {
		return distributorRetailer;
	}

	public void setDistributorRetailer(String distributorRetailer) {
		this.distributorRetailer = distributorRetailer;
	}

	public String getDistributorImporterRetailer() {
		return distributorImporterRetailer;
	}

	public void setDistributorImporterRetailer(String distributorImporterRetailer) {
		this.distributorImporterRetailer = distributorImporterRetailer;
	}

	public String getDistributorGrievance() {
		return distributorGrievance;
	}

	public void setDistributorGrievance(String distributorGrievance) {
		this.distributorGrievance = distributorGrievance;
	}

	public String getDistributorStock() {
		return distributorStock;
	}

	public void setDistributorStock(String distributorStock) {
		this.distributorStock = distributorStock;
	}

	public String getDistributorDeviceCount() {
		return distributorDeviceCount;
	}

	public void setDistributorDeviceCount(String distributorDeviceCount) {
		this.distributorDeviceCount = distributorDeviceCount;
	}

	public String getTotalDistributor() {
		return totalDistributor;
	}

	public void setTotalDistributor(String totalDistributor) {
		this.totalDistributor = totalDistributor;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DistributorDeviceCountData [date=");
		builder.append(date);
		builder.append(", month=");
		builder.append(month);
		builder.append(", distributor=");
		builder.append(distributor);
		builder.append(", distributorImporter=");
		builder.append(distributorImporter);
		builder.append(", distributorRetailer=");
		builder.append(distributorRetailer);
		builder.append(", distributorImporterRetailer=");
		builder.append(distributorImporterRetailer);
		builder.append(", distributorGrievance=");
		builder.append(distributorGrievance);
		builder.append(", distributorStock=");
		builder.append(distributorStock);
		builder.append(", distributorDeviceCount=");
		builder.append(distributorDeviceCount);
		builder.append(", totalDistributor=");
		builder.append(totalDistributor);
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
		builder.append(", newState=");
		builder.append(newState);
		builder.append("]");
		return builder.toString();
	}

	
	  
}
