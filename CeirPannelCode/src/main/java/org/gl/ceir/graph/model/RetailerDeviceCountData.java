package org.gl.ceir.graph.model;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

@Component
public class RetailerDeviceCountData {
	  @JsonProperty("Date")
	  @SerializedName("Date")
	  private String date;
	  
	  @JsonProperty("Month")
	  @SerializedName("Month")
	  private String month;
	  
	  @JsonProperty("Retailer")
	  @SerializedName("Retailer")
	  private String retailer;
	  
	  @JsonProperty("Retailer & Importer")
	  @SerializedName("Retailer & Importer")
	  private String retailerImporter;
	  
	  @JsonProperty("Retailer & Distributor")
	  @SerializedName("Retailer & Distributor")
	  private String retailerDistributor;
	  
	  @JsonProperty("Retailer & Importer & Distributor")
	  @SerializedName("Retailer & Importer & Distributor")
	  private String retailerImporterDistributor;
	  
	  @JsonProperty("Retailer Grievance")
	  @SerializedName("Retailer Grievance")
	  private String retailerGrievance;

	  @JsonProperty("Total Retailer Device Count")
	  @SerializedName("Total Retailer Device Count")
	  private String retailerDevice;
	  
	  @JsonProperty("Total Stock Uploaded Retailer")
	  @SerializedName("Total Stock Uploaded Retailer")
	  private String retailerStock;
	  
	  @JsonProperty("Total Retailer User Count")
	  @SerializedName("Total Retailer User Count")
	  private String totalRetailer;
	
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

	public String getRetailer() {
		return retailer;
	}

	public void setRetailer(String retailer) {
		this.retailer = retailer;
	}

	public String getRetailerImporter() {
		return retailerImporter;
	}

	public void setRetailerImporter(String retailerImporter) {
		this.retailerImporter = retailerImporter;
	}

	public String getRetailerDistributor() {
		return retailerDistributor;
	}

	public void setRetailerDistributor(String retailerDistributor) {
		this.retailerDistributor = retailerDistributor;
	}

	public String getRetailerImporterDistributor() {
		return retailerImporterDistributor;
	}

	public void setRetailerImporterDistributor(String retailerImporterDistributor) {
		this.retailerImporterDistributor = retailerImporterDistributor;
	}

	public String getRetailerGrievance() {
		return retailerGrievance;
	}

	public void setRetailerGrievance(String retailerGrievance) {
		this.retailerGrievance = retailerGrievance;
	}

	public String getRetailerDevice() {
		return retailerDevice;
	}

	public void setRetailerDevice(String retailerDevice) {
		this.retailerDevice = retailerDevice;
	}

	public String getRetailerStock() {
		return retailerStock;
	}

	public void setRetailerStock(String retailerStock) {
		this.retailerStock = retailerStock;
	}

	public String getTotalRetailer() {
		return totalRetailer;
	}

	public void setTotalRetailer(String totalRetailer) {
		this.totalRetailer = totalRetailer;
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
		builder.append("RetailerDeviceCountData [date=");
		builder.append(date);
		builder.append(", month=");
		builder.append(month);
		builder.append(", retailer=");
		builder.append(retailer);
		builder.append(", retailerImporter=");
		builder.append(retailerImporter);
		builder.append(", retailerDistributor=");
		builder.append(retailerDistributor);
		builder.append(", retailerImporterDistributor=");
		builder.append(retailerImporterDistributor);
		builder.append(", retailerGrievance=");
		builder.append(retailerGrievance);
		builder.append(", retailerDevice=");
		builder.append(retailerDevice);
		builder.append(", retailerStock=");
		builder.append(retailerStock);
		builder.append(", totalRetailer=");
		builder.append(totalRetailer);
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
