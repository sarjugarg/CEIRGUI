package org.gl.ceir.graph.model;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

@Component
public class MobileDeviceCountData {
	@JsonProperty("Date")
	@SerializedName("Date")
	private String date;
	
	
	@JsonProperty("Total Smartphone IMEI Count")
	@SerializedName("Total Smartphone IMEI Count")
	private String totalSmartPhone;
	
	@JsonProperty("Total Featurephone IMEI Count")
	@SerializedName("Total Featurephone IMEI Count")
	private String totalFeaturePhone;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTotalSmartPhone() {
		return totalSmartPhone;
	}

	public void setTotalSmartPhone(String totalSmartPhone) {
		this.totalSmartPhone = totalSmartPhone;
	}

	public String getTotalFeaturePhone() {
		return totalFeaturePhone;
	}

	public void setTotalFeaturePhone(String totalFeaturePhone) {
		this.totalFeaturePhone = totalFeaturePhone;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MobileDeviceCountData [date=");
		builder.append(date);
		builder.append(", totalSmartPhone=");
		builder.append(totalSmartPhone);
		builder.append(", totalFeaturePhone=");
		builder.append(totalFeaturePhone);
		builder.append("]");
		return builder.toString();
	}
}
