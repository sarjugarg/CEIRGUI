package org.gl.ceir.graph.model;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

@Component
public class operatorDashboardContent {
	@JsonProperty("Date")
	@SerializedName("Date")
	private String date;
	
	@JsonProperty("Operator Name")
	@SerializedName("Operator Name")
	private String operatorName;
	
	@JsonProperty("Total IMEI")
	@SerializedName("Total IMEI")
	private String totalImei;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getTotalImei() {
		return totalImei;
	}

	public void setTotalImei(String totalImei) {
		this.totalImei = totalImei;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("operatorDashboardContent [date=");
		builder.append(date);
		builder.append(", operatorName=");
		builder.append(operatorName);
		builder.append(", totalImei=");
		builder.append(totalImei);
		builder.append("]");
		return builder.toString();
	}
	
	
	
}
