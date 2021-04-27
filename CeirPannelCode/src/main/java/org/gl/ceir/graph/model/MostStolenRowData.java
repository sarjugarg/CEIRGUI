package org.gl.ceir.graph.model;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
@Component
public class MostStolenRowData {
	@SerializedName("Date")
	@JsonProperty("Date")
	private String date;
	
	@SerializedName("Stolen IMEI Count")
	@JsonProperty("Stolen IMEI Count")
	private int stolenCount;

	@SerializedName("Recovered IMEI Count")
	@JsonProperty("Recovered IMEI Count")
	private int recoverdCount;
	
	@SerializedName("Blocked IMEI Count")
	@JsonProperty("Blocked IMEI Count")
	private int blockedCount ;
	
	@SerializedName("Pending IMEI Count")
	@JsonProperty("Pending IMEI Count")
	private int pendingCount ;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getStolenCount() {
		return stolenCount;
	}

	public void setStolenCount(int stolenCount) {
		this.stolenCount = stolenCount;
	}

	public int getRecoverdCount() {
		return recoverdCount;
	}

	public void setRecoverdCount(int recoverdCount) {
		this.recoverdCount = recoverdCount;
	}

	public int getBlockedCount() {
		return blockedCount;
	}

	public void setBlockedCount(int blockedCount) {
		this.blockedCount = blockedCount;
	}

	public int getPendingCount() {
		return pendingCount;
	}

	public void setPendingCount(int pendingCount) {
		this.pendingCount = pendingCount;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MostStolenRowData [date=");
		builder.append(date);
		builder.append(", stolenCount=");
		builder.append(stolenCount);
		builder.append(", recoverdCount=");
		builder.append(recoverdCount);
		builder.append(", blockedCount=");
		builder.append(blockedCount);
		builder.append(", pendingCount=");
		builder.append(pendingCount);
		builder.append("]");
		return builder.toString();
	}

}
