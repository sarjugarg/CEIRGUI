package org.gl.ceir.graph.model;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
@Component
public class UserDashboardUnblockCountData {

	@JsonProperty("Date")
	@SerializedName("Date")
	private String date;
	
	@JsonProperty("Total Unblock Device Count")
	@SerializedName("Total Unblock Device Count")
	private String totalUnblockDeviceCount;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTotalUnblockDeviceCount() {
		return totalUnblockDeviceCount;
	}

	public void setTotalUnblockDeviceCount(String totalUnblockDeviceCount) {
		this.totalUnblockDeviceCount = totalUnblockDeviceCount;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserDashboardUnblockCountData [date=");
		builder.append(date);
		builder.append(", totalUnblockDeviceCount=");
		builder.append(totalUnblockDeviceCount);
		builder.append("]");
		return builder.toString();
	}
	
	
}
