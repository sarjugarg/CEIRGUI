package org.gl.ceir.graph.model;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
@Component
public class GrievanceModelRowData {
	
	
	@SerializedName("Date")
	@JsonProperty("Date")
	private String date;
	
		
	@SerializedName("Closed")
	@JsonProperty("Closed")
	private int closed;
	
	@SerializedName("Pending With Admin")
	@JsonProperty("Pending With Admin")
	private int pendigWithAdmin;
	
	@SerializedName("Pending With User")
	@JsonProperty("Pending With User")
	private int pendingWithUser;
	

	@SerializedName("New")
	@JsonProperty("New")
	private int newState;


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public int getClosed() {
		return closed;
	}


	public void setClosed(int closed) {
		this.closed = closed;
	}


	public int getPendigWithAdmin() {
		return pendigWithAdmin;
	}


	public void setPendigWithAdmin(int pendigWithAdmin) {
		this.pendigWithAdmin = pendigWithAdmin;
	}


	public int getPendingWithUser() {
		return pendingWithUser;
	}


	public void setPendingWithUser(int pendingWithUser) {
		this.pendingWithUser = pendingWithUser;
	}


	public int getNewState() {
		return newState;
	}


	public void setNewState(int newState) {
		this.newState = newState;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GrievanceModelRowData [date=");
		builder.append(date);
		builder.append(", closed=");
		builder.append(closed);
		builder.append(", pendigWithAdmin=");
		builder.append(pendigWithAdmin);
		builder.append(", pendingWithUser=");
		builder.append(pendingWithUser);
		builder.append(", newState=");
		builder.append(newState);
		builder.append("]");
		return builder.toString();
	}

	
}
