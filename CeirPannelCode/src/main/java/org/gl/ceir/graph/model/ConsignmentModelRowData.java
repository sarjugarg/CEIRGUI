package org.gl.ceir.graph.model;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
@Component
public class ConsignmentModelRowData {
	
	@SerializedName("Date")
	@JsonProperty("Date")
	private String date;
	
	@SerializedName("Approved")
	@JsonProperty("Approved")
	private int approved;

	@SerializedName("Pending Approval From CEIR Admin")
	@JsonProperty("Pending Approval From CEIR Admin")
	private int pendingByAdmin;
	
	@SerializedName("Rejected by CEIR Admin")
	@JsonProperty("Rejected by CEIR Admin")
	private int rejectedByAdmin;
	
	@SerializedName("Rejected By System")
	@JsonProperty("Rejected By System")
	private int rejectedBySystem;
	
	@SerializedName("Withdrawn By Importer")
	@JsonProperty("Withdrawn By Importer")
	private int withdrawnByImporter;
	
	@SerializedName("Withdrawn By CEIR")
	@JsonProperty("Withdrawn By CEIR")
	private int withdrawnByCeir;
	
	@SerializedName("Processing")
	@JsonProperty("Processing")
	private int processing;
	
	@SerializedName("Pending Clearance From Customs")
	@JsonProperty("Pending Clearance From Customs")
	private int pendingClearenceFromCustom;
	
	

	@SerializedName("New")
	@JsonProperty("New")
	private int newState;
	
	@SerializedName("Rejected By Customs")
	@JsonProperty("Rejected By Customs")
	private int rejectedByCustoms;
	
	@SerializedName("Rejected by DRT")
	@JsonProperty("Rejected by DRT")
	private int rejectedByDrt;
	
	@SerializedName("Pending Clearance from DRT")
	@JsonProperty("Pending Clearance from DRT")
	private int pndingByDrt;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getApproved() {
		return approved;
	}

	public void setApproved(int approved) {
		this.approved = approved;
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

	public int getRejectedBySystem() {
		return rejectedBySystem;
	}

	public void setRejectedBySystem(int rejectedBySystem) {
		this.rejectedBySystem = rejectedBySystem;
	}

	public int getWithdrawnByImporter() {
		return withdrawnByImporter;
	}

	public void setWithdrawnByImporter(int withdrawnByImporter) {
		this.withdrawnByImporter = withdrawnByImporter;
	}

	public int getWithdrawnByCeir() {
		return withdrawnByCeir;
	}

	public void setWithdrawnByCeir(int withdrawnByCeir) {
		this.withdrawnByCeir = withdrawnByCeir;
	}

	public int getProcessing() {
		return processing;
	}

	public void setProcessing(int processing) {
		this.processing = processing;
	}

	public int getPendingClearenceFromCustom() {
		return pendingClearenceFromCustom;
	}

	public void setPendingClearenceFromCustom(int pendingClearenceFromCustom) {
		this.pendingClearenceFromCustom = pendingClearenceFromCustom;
	}

	public int getNewState() {
		return newState;
	}

	public void setNewState(int newState) {
		this.newState = newState;
	}

	public int getRejectedByCustoms() {
		return rejectedByCustoms;
	}

	public void setRejectedByCustoms(int rejectedByCustoms) {
		this.rejectedByCustoms = rejectedByCustoms;
	}

	public int getRejectedByDrt() {
		return rejectedByDrt;
	}

	public void setRejectedByDrt(int rejectedByDrt) {
		this.rejectedByDrt = rejectedByDrt;
	}

	public int getPndingByDrt() {
		return pndingByDrt;
	}

	public void setPndingByDrt(int pndingByDrt) {
		this.pndingByDrt = pndingByDrt;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ConsignmentModelRowData [date=");
		builder.append(date);
		builder.append(", approved=");
		builder.append(approved);
		builder.append(", pendingByAdmin=");
		builder.append(pendingByAdmin);
		builder.append(", rejectedByAdmin=");
		builder.append(rejectedByAdmin);
		builder.append(", rejectedBySystem=");
		builder.append(rejectedBySystem);
		builder.append(", withdrawnByImporter=");
		builder.append(withdrawnByImporter);
		builder.append(", withdrawnByCeir=");
		builder.append(withdrawnByCeir);
		builder.append(", processing=");
		builder.append(processing);
		builder.append(", pendingClearenceFromCustom=");
		builder.append(pendingClearenceFromCustom);
		builder.append(", newState=");
		builder.append(newState);
		builder.append(", rejectedByCustoms=");
		builder.append(rejectedByCustoms);
		builder.append(", rejectedByDrt=");
		builder.append(rejectedByDrt);
		builder.append(", pndingByDrt=");
		builder.append(pndingByDrt);
		builder.append("]");
		return builder.toString();
	}

	
	
}
