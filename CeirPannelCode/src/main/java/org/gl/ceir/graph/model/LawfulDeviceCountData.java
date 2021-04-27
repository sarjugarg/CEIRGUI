package org.gl.ceir.graph.model;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
@Component
public class LawfulDeviceCountData {
	  @JsonProperty("Date")
	  @SerializedName("Date")
	  private String date;
	
      @JsonProperty("Total Stolen IMEI Count")
  	  @SerializedName("Total Stolen IMEI Count")
      private String stolenIMEI;
      
	/*
	 * @JsonProperty("Total Lost IMEI Count")
	 * 
	 * @SerializedName("Total Lost IMEI Count") private String lostIMEI;
	 * 
	 * @JsonProperty("Total Recoverd IMEI Count")
	 * 
	 * @SerializedName("Total Recoverd IMEI Count") private String recoveredIMEI;
	 */

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getStolenIMEI() {
		return stolenIMEI;
	}

	public void setStolenIMEI(String stolenIMEI) {
		this.stolenIMEI = stolenIMEI;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LawfulDeviceCountData [date=");
		builder.append(date);
		builder.append(", stolenIMEI=");
		builder.append(stolenIMEI);
		builder.append("]");
		return builder.toString();
	}

      
}
