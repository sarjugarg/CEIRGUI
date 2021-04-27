package org.gl.ceir.graph.model;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

@Component
public class ActiveDeviceCountData {
	@JsonProperty("Date")
	@SerializedName("Date")
	private String date;
	
	@JsonProperty("Total IMEI")
	@SerializedName("Total IMEI")
	private String totalIMEI;
	
	@JsonProperty("Total Unique IMEI")
	@SerializedName("Total Unique IMEI")
	private String totalUniqueIMEI;
	
	@JsonProperty("Total Paired IMEI")
	@SerializedName("Total Paired IMEI")
	private String totalPairedIMEI;
	
	@JsonProperty("GSMA Approved TAC Count")
	@SerializedName("GSMA Approved TAC Count")
	private String approvedTAC;
	
	@JsonProperty("GSMA Rejected TAC Count")
	@SerializedName("GSMA Rejected TAC Count")
	private String rejectedTAC;
	
	@JsonProperty("Total GSMA BlackList Count")
	@SerializedName("Total GSMA BlackList Count")
	private String totalGSMABlackList;
	
	@JsonProperty("Total Importer IMEI Count")
	@SerializedName("Total Importer Device Count")
	private String totalImporterDevice;
	
	@JsonProperty("Total Distributor IMEI Count")
	@SerializedName("Total Distributor Device Count")
	private String totalDistributorDevice;
	
	@JsonProperty("Total Retailer IMEI Count")
	@SerializedName("Total Retailer Device Count")
	private String totalRetailerDevice;
	
	@JsonProperty("Total Manufacturer IMEI Count")
	@SerializedName("Total Manufacturer Device Count")
	private String totalManufacturerDevice;
	
	@JsonProperty("Total End User IMEI Count")
	@SerializedName("Total End User Device Count")
	private String totalEndUserDevice;

	@JsonProperty("Total Stolen IMEI Count")
  	@SerializedName("Total Stolen Device Count")
  	private String totalStolenDeviceCount;
  	
  	@JsonProperty("Total Blocked IMEI Count")
  	@SerializedName("Total Blocked Device Count")
  	private String totalBlockedDeviceCount;
  	
  	
     
  	@JsonProperty("Total Device Model Count")
  	@SerializedName("Total Device Model Count")
  	private String totalModelCount;
  	
  	@JsonProperty("Total Device Brand Count")
  	@SerializedName("Total Device Brand Count")
  	private String totalBrandCount;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTotalIMEI() {
		return totalIMEI;
	}

	public void setTotalIMEI(String totalIMEI) {
		this.totalIMEI = totalIMEI;
	}

	public String getTotalUniqueIMEI() {
		return totalUniqueIMEI;
	}

	public void setTotalUniqueIMEI(String totalUniqueIMEI) {
		this.totalUniqueIMEI = totalUniqueIMEI;
	}

	public String getTotalPairedIMEI() {
		return totalPairedIMEI;
	}

	public void setTotalPairedIMEI(String totalPairedIMEI) {
		this.totalPairedIMEI = totalPairedIMEI;
	}

	public String getApprovedTAC() {
		return approvedTAC;
	}

	public void setApprovedTAC(String approvedTAC) {
		this.approvedTAC = approvedTAC;
	}

	public String getRejectedTAC() {
		return rejectedTAC;
	}

	public void setRejectedTAC(String rejectedTAC) {
		this.rejectedTAC = rejectedTAC;
	}

	public String getTotalGSMABlackList() {
		return totalGSMABlackList;
	}

	public void setTotalGSMABlackList(String totalGSMABlackList) {
		this.totalGSMABlackList = totalGSMABlackList;
	}

	public String getTotalImporterDevice() {
		return totalImporterDevice;
	}

	public void setTotalImporterDevice(String totalImporterDevice) {
		this.totalImporterDevice = totalImporterDevice;
	}

	public String getTotalDistributorDevice() {
		return totalDistributorDevice;
	}

	public void setTotalDistributorDevice(String totalDistributorDevice) {
		this.totalDistributorDevice = totalDistributorDevice;
	}

	public String getTotalRetailerDevice() {
		return totalRetailerDevice;
	}

	public void setTotalRetailerDevice(String totalRetailerDevice) {
		this.totalRetailerDevice = totalRetailerDevice;
	}

	public String getTotalManufacturerDevice() {
		return totalManufacturerDevice;
	}

	public void setTotalManufacturerDevice(String totalManufacturerDevice) {
		this.totalManufacturerDevice = totalManufacturerDevice;
	}

	public String getTotalEndUserDevice() {
		return totalEndUserDevice;
	}

	public void setTotalEndUserDevice(String totalEndUserDevice) {
		this.totalEndUserDevice = totalEndUserDevice;
	}

	public String getTotalStolenDeviceCount() {
		return totalStolenDeviceCount;
	}

	public void setTotalStolenDeviceCount(String totalStolenDeviceCount) {
		this.totalStolenDeviceCount = totalStolenDeviceCount;
	}

	public String getTotalBlockedDeviceCount() {
		return totalBlockedDeviceCount;
	}

	public void setTotalBlockedDeviceCount(String totalBlockedDeviceCount) {
		this.totalBlockedDeviceCount = totalBlockedDeviceCount;
	}

	public String getTotalModelCount() {
		return totalModelCount;
	}

	public void setTotalModelCount(String totalModelCount) {
		this.totalModelCount = totalModelCount;
	}

	public String getTotalBrandCount() {
		return totalBrandCount;
	}

	public void setTotalBrandCount(String totalBrandCount) {
		this.totalBrandCount = totalBrandCount;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ActiveDeviceCountData [date=");
		builder.append(date);
		builder.append(", totalIMEI=");
		builder.append(totalIMEI);
		builder.append(", totalUniqueIMEI=");
		builder.append(totalUniqueIMEI);
		builder.append(", totalPairedIMEI=");
		builder.append(totalPairedIMEI);
		builder.append(", approvedTAC=");
		builder.append(approvedTAC);
		builder.append(", rejectedTAC=");
		builder.append(rejectedTAC);
		builder.append(", totalGSMABlackList=");
		builder.append(totalGSMABlackList);
		builder.append(", totalImporterDevice=");
		builder.append(totalImporterDevice);
		builder.append(", totalDistributorDevice=");
		builder.append(totalDistributorDevice);
		builder.append(", totalRetailerDevice=");
		builder.append(totalRetailerDevice);
		builder.append(", totalManufacturerDevice=");
		builder.append(totalManufacturerDevice);
		builder.append(", totalEndUserDevice=");
		builder.append(totalEndUserDevice);
		builder.append(", totalStolenDeviceCount=");
		builder.append(totalStolenDeviceCount);
		builder.append(", totalBlockedDeviceCount=");
		builder.append(totalBlockedDeviceCount);
		builder.append(", totalModelCount=");
		builder.append(totalModelCount);
		builder.append(", totalBrandCount=");
		builder.append(totalBrandCount);
		builder.append("]");
		return builder.toString();
	}


	
	
}
