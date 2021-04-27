package org.gl.ceir.graph.model;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

@Component
public class ActiveDeviceGraphRowData {
	@SerializedName("GSMA Approved TAC Count")
	private String approvedTAC;
	@SerializedName("GSMA Rejected TAC Count")
	private String rejectedTAC;
	
	
	@SerializedName("Created On")
	private String createdOn;
	@SerializedName("Modified On")
	private String modifiedOn;
	
	@JsonProperty("Date")
	@SerializedName("Date")
	private String date;
	
	@JsonProperty("Total IMEI")
	@SerializedName("Total IMEI")
	private String totalImei;
	
	@JsonProperty("Operator Name")
	@SerializedName("Operator Name")
	private String operatorName;

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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTotalImei() {
		return totalImei;
	}

	public void setTotalImei(String totalImei) {
		this.totalImei = totalImei;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ActiveDeviceGraphRowData [approvedTAC=");
		builder.append(approvedTAC);
		builder.append(", rejectedTAC=");
		builder.append(rejectedTAC);
		builder.append(", createdOn=");
		builder.append(createdOn);
		builder.append(", modifiedOn=");
		builder.append(modifiedOn);
		builder.append(", date=");
		builder.append(date);
		builder.append(", totalImei=");
		builder.append(totalImei);
		builder.append(", operatorName=");
		builder.append(operatorName);
		builder.append("]");
		return builder.toString();
	}

}
