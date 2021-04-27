package org.gl.ceir.graph.model;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

@Component
public class OperatorBlackGreyCountContentModel {
	
	@JsonProperty("Date")
	@SerializedName("Date")
	private String date;
	
	
	@JsonProperty("Total Tax Paid Device Count")
  	@SerializedName("Total Tax Paid Device Count")
  	private String totalTaxPaidDeviceCount;
	
	
	@JsonProperty("Total Grey IMEI Count")
	@SerializedName("Total Grey IMEI Count")
	private String totalGreyIMEI;
	
	@JsonProperty("Total BlackList IMEI Count")
	@SerializedName("Total BlackList IMEI Count")
	private String totalBlackListIMEI;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTotalTaxPaidDeviceCount() {
		return totalTaxPaidDeviceCount;
	}

	public void setTotalTaxPaidDeviceCount(String totalTaxPaidDeviceCount) {
		this.totalTaxPaidDeviceCount = totalTaxPaidDeviceCount;
	}

	public String getTotalGreyIMEI() {
		return totalGreyIMEI;
	}

	public void setTotalGreyIMEI(String totalGreyIMEI) {
		this.totalGreyIMEI = totalGreyIMEI;
	}

	public String getTotalBlackListIMEI() {
		return totalBlackListIMEI;
	}

	public void setTotalBlackListIMEI(String totalBlackListIMEI) {
		this.totalBlackListIMEI = totalBlackListIMEI;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OperatorBlackGreyCountContentModel [date=");
		builder.append(date);
		builder.append(", totalTaxPaidDeviceCount=");
		builder.append(totalTaxPaidDeviceCount);
		builder.append(", totalGreyIMEI=");
		builder.append(totalGreyIMEI);
		builder.append(", totalBlackListIMEI=");
		builder.append(totalBlackListIMEI);
		builder.append("]");
		return builder.toString();
	}
	
	
}
