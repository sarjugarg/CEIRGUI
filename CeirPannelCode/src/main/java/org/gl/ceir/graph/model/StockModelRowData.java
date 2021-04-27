package org.gl.ceir.graph.model;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
@Component
public class StockModelRowData {
	
	
	@SerializedName("Date")
	@JsonProperty("Date")
	private String date;
	
	@SerializedName("Withdrawn By User")
	@JsonProperty("Withdrawn By User")
	private int withdrawnBySystem;
	
	@SerializedName("Approved by CEIR Admin")
	@JsonProperty("Approved by CEIR Admin")
	private int approvedByAdmin;
	
	@SerializedName("Withdrawn By CEIR Admin")
	@JsonProperty("Withdrawn By CEIR Admin")
	private int withdrawnByAdmin;

	@SerializedName("New")
	@JsonProperty("New")
	private int newState;
	
	@SerializedName("Processing")
	@JsonProperty("Processing")
	private int processing;
	
	@SerializedName("Rejected By System")
	@JsonProperty("Rejected By System")
	private int rejectedBySystem;
	
	@SerializedName("Pending Approval From CEIR Admin")
	@JsonProperty("Pending Approval From CEIR Admin")
	private int pendingByAdmin;
	
	@SerializedName("Rejected by CEIR Admin")
	@JsonProperty("Rejected by CEIR Admin")
	private int rejectedByAdmin;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getWithdrawnBySystem() {
		return withdrawnBySystem;
	}

	public void setWithdrawnBySystem(int withdrawnBySystem) {
		this.withdrawnBySystem = withdrawnBySystem;
	}

	public int getApprovedByAdmin() {
		return approvedByAdmin;
	}

	public void setApprovedByAdmin(int approvedByAdmin) {
		this.approvedByAdmin = approvedByAdmin;
	}

	public int getWithdrawnByAdmin() {
		return withdrawnByAdmin;
	}

	public void setWithdrawnByAdmin(int withdrawnByAdmin) {
		this.withdrawnByAdmin = withdrawnByAdmin;
	}

	public int getNewState() {
		return newState;
	}

	public void setNewState(int newState) {
		this.newState = newState;
	}

	public int getProcessing() {
		return processing;
	}

	public void setProcessing(int processing) {
		this.processing = processing;
	}

	public int getRejectedBySystem() {
		return rejectedBySystem;
	}

	public void setRejectedBySystem(int rejectedBySystem) {
		this.rejectedBySystem = rejectedBySystem;
	}

	public int getPendingByAdmin() {
		return pendingByAdmin;
	}

	public void setPendingByAdmin(int pendingByAdmin) {
		this.pendingByAdmin = pendingByAdmin;
	}

	public int getRejectedByAdmin() {
		return rejectedByAdmin;
	}

	public void setRejectedByAdmin(int rejectedByAdmin) {
		this.rejectedByAdmin = rejectedByAdmin;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("StockModelRowData [date=");
		builder.append(date);
		builder.append(", withdrawnBySystem=");
		builder.append(withdrawnBySystem);
		builder.append(", approvedByAdmin=");
		builder.append(approvedByAdmin);
		builder.append(", withdrawnByAdmin=");
		builder.append(withdrawnByAdmin);
		builder.append(", newState=");
		builder.append(newState);
		builder.append(", processing=");
		builder.append(processing);
		builder.append(", rejectedBySystem=");
		builder.append(rejectedBySystem);
		builder.append(", pendingByAdmin=");
		builder.append(pendingByAdmin);
		builder.append(", rejectedByAdmin=");
		builder.append(rejectedByAdmin);
		builder.append("]");
		return builder.toString();
	}

	
	
	
}
