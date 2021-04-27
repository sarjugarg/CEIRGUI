package org.gl.ceir.graph.model;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
@Component
public class UserDashboardCountData {
	@JsonProperty("Date")
	@SerializedName("Date")
	private String date;


	@JsonProperty("Total User Count")
	@SerializedName("Total User Count")
	private String totalUserCount;


	@JsonProperty("Total Importer User Count")
	@SerializedName("Total Importer User Count")
	private String totalImporterUserCount;



	@JsonProperty("Total Distributor User Count")
	@SerializedName("Total Distributor User Count")
	private String totalDistributorUserCount;


	@JsonProperty("Total Retailer User Count")
	@SerializedName("Total Retailer User Count")
	private String totalRetailerUserCount;


	@JsonProperty("Total Manufacturer User Count")
	@SerializedName("Total Manufacturer User Count")
	private String totalManufacturerUserCount;



	@JsonProperty("Total End User Count")
	@SerializedName("Total End User Count")
	private String totalEndUserCount;


	@JsonProperty("Total Lawful Agency User Count")
	@SerializedName("Total Lawful Agency User Count")
	private String totalLawfulAgencyUserCount;


	@JsonProperty("Total Operator User Count")
	@SerializedName("Total Operator User Count")
	private String totalOperatorUserCount;




	@JsonProperty("Total CEIR Admin User Count")
	@SerializedName("Total CEIR Admin User Count")
	private String totalCEIRAdminUserCount;


	@JsonProperty("Total Customer Care User Count")
	@SerializedName("Total Customer Care User Count")
	private String totalCustomerCareUserCount;


	@JsonProperty("Total Operation User Count")
	@SerializedName("Total Operation User Count")
	private String totalOperationUserCount;




	@JsonProperty("Total TRC User Count")
	@SerializedName("Total TRC User Count")
	private String totalTRCUserCount;


	@JsonProperty("Total DRT User Count")
	@SerializedName("Total DRT User Count")
	private String totalDRTUserCount;


	@JsonProperty("Total Custom User Count")
	@SerializedName("Total Custom User Count")
	private String totalCustomUserCount;


	@JsonProperty("Total Immigration User Count")
	@SerializedName("Total Immigration User Count")
	private String totalImmigrationUserCount;


	@JsonProperty("Total Anonymous User Count")
	@SerializedName("Total Anonymous User Count")
	private String totalAnonymousUserCount;

	
	
	@JsonProperty("Total System User Count ")
	@SerializedName("Total System User Count ")
	private String totalsystemUserCount;

	
	
	@JsonProperty("Total Reporting User Count ")
	@SerializedName("Total Reporting User Count")
	private String totalReportingUserCount;
	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public String getTotalUserCount() {
		return totalUserCount;
	}


	public void setTotalUserCount(String totalUserCount) {
		this.totalUserCount = totalUserCount;
	}


	public String getTotalImporterUserCount() {
		return totalImporterUserCount;
	}


	public void setTotalImporterUserCount(String totalImporterUserCount) {
		this.totalImporterUserCount = totalImporterUserCount;
	}


	public String getTotalDistributorUserCount() {
		return totalDistributorUserCount;
	}


	public void setTotalDistributorUserCount(String totalDistributorUserCount) {
		this.totalDistributorUserCount = totalDistributorUserCount;
	}


	public String getTotalRetailerUserCount() {
		return totalRetailerUserCount;
	}


	public void setTotalRetailerUserCount(String totalRetailerUserCount) {
		this.totalRetailerUserCount = totalRetailerUserCount;
	}


	public String getTotalManufacturerUserCount() {
		return totalManufacturerUserCount;
	}


	public void setTotalManufacturerUserCount(String totalManufacturerUserCount) {
		this.totalManufacturerUserCount = totalManufacturerUserCount;
	}


	public String getTotalEndUserCount() {
		return totalEndUserCount;
	}


	public void setTotalEndUserCount(String totalEndUserCount) {
		this.totalEndUserCount = totalEndUserCount;
	}


	public String getTotalLawfulAgencyUserCount() {
		return totalLawfulAgencyUserCount;
	}


	public void setTotalLawfulAgencyUserCount(String totalLawfulAgencyUserCount) {
		this.totalLawfulAgencyUserCount = totalLawfulAgencyUserCount;
	}


	public String getTotalOperatorUserCount() {
		return totalOperatorUserCount;
	}


	public void setTotalOperatorUserCount(String totalOperatorUserCount) {
		this.totalOperatorUserCount = totalOperatorUserCount;
	}


	public String getTotalCEIRAdminUserCount() {
		return totalCEIRAdminUserCount;
	}


	public void setTotalCEIRAdminUserCount(String totalCEIRAdminUserCount) {
		this.totalCEIRAdminUserCount = totalCEIRAdminUserCount;
	}


	public String getTotalCustomerCareUserCount() {
		return totalCustomerCareUserCount;
	}


	public void setTotalCustomerCareUserCount(String totalCustomerCareUserCount) {
		this.totalCustomerCareUserCount = totalCustomerCareUserCount;
	}


	public String getTotalOperationUserCount() {
		return totalOperationUserCount;
	}


	public void setTotalOperationUserCount(String totalOperationUserCount) {
		this.totalOperationUserCount = totalOperationUserCount;
	}


	public String getTotalTRCUserCount() {
		return totalTRCUserCount;
	}


	public void setTotalTRCUserCount(String totalTRCUserCount) {
		this.totalTRCUserCount = totalTRCUserCount;
	}


	public String getTotalDRTUserCount() {
		return totalDRTUserCount;
	}


	public void setTotalDRTUserCount(String totalDRTUserCount) {
		this.totalDRTUserCount = totalDRTUserCount;
	}


	public String getTotalCustomUserCount() {
		return totalCustomUserCount;
	}


	public void setTotalCustomUserCount(String totalCustomUserCount) {
		this.totalCustomUserCount = totalCustomUserCount;
	}


	public String getTotalImmigrationUserCount() {
		return totalImmigrationUserCount;
	}


	public void setTotalImmigrationUserCount(String totalImmigrationUserCount) {
		this.totalImmigrationUserCount = totalImmigrationUserCount;
	}


	public String getTotalAnonymousUserCount() {
		return totalAnonymousUserCount;
	}


	public void setTotalAnonymousUserCount(String totalAnonymousUserCount) {
		this.totalAnonymousUserCount = totalAnonymousUserCount;
	}


	public String getTotalsystemUserCount() {
		return totalsystemUserCount;
	}


	public void setTotalsystemUserCount(String totalsystemUserCount) {
		this.totalsystemUserCount = totalsystemUserCount;
	}


	public String getTotalReportingUserCount() {
		return totalReportingUserCount;
	}


	public void setTotalReportingUserCount(String totalReportingUserCount) {
		this.totalReportingUserCount = totalReportingUserCount;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserDashboardCountData [date=");
		builder.append(date);
		builder.append(", totalUserCount=");
		builder.append(totalUserCount);
		builder.append(", totalImporterUserCount=");
		builder.append(totalImporterUserCount);
		builder.append(", totalDistributorUserCount=");
		builder.append(totalDistributorUserCount);
		builder.append(", totalRetailerUserCount=");
		builder.append(totalRetailerUserCount);
		builder.append(", totalManufacturerUserCount=");
		builder.append(totalManufacturerUserCount);
		builder.append(", totalEndUserCount=");
		builder.append(totalEndUserCount);
		builder.append(", totalLawfulAgencyUserCount=");
		builder.append(totalLawfulAgencyUserCount);
		builder.append(", totalOperatorUserCount=");
		builder.append(totalOperatorUserCount);
		builder.append(", totalCEIRAdminUserCount=");
		builder.append(totalCEIRAdminUserCount);
		builder.append(", totalCustomerCareUserCount=");
		builder.append(totalCustomerCareUserCount);
		builder.append(", totalOperationUserCount=");
		builder.append(totalOperationUserCount);
		builder.append(", totalTRCUserCount=");
		builder.append(totalTRCUserCount);
		builder.append(", totalDRTUserCount=");
		builder.append(totalDRTUserCount);
		builder.append(", totalCustomUserCount=");
		builder.append(totalCustomUserCount);
		builder.append(", totalImmigrationUserCount=");
		builder.append(totalImmigrationUserCount);
		builder.append(", totalAnonymousUserCount=");
		builder.append(totalAnonymousUserCount);
		builder.append(", totalsystemUserCount=");
		builder.append(totalsystemUserCount);
		builder.append(", totalReportingUserCount=");
		builder.append(totalReportingUserCount);
		builder.append("]");
		return builder.toString();
	}

 

}
