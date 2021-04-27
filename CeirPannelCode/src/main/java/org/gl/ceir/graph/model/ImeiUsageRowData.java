package org.gl.ceir.graph.model;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

@Component
public class ImeiUsageRowData {
	@SerializedName("Total Paired IMEI")
	@JsonProperty("Total Paired IMEI")
	private String totalPairedIMEI;
	
	@JsonProperty("Total Duplicate IMEI")
	@SerializedName("Total Duplicate IMEI")
	private String totalDuplicateIMEI;

	public String getTotalPairedIMEI() {
		return totalPairedIMEI;
	}

	public void setTotalPairedIMEI(String totalPairedIMEI) {
		this.totalPairedIMEI = totalPairedIMEI;
	}

	public String getTotalDuplicateIMEI() {
		return totalDuplicateIMEI;
	}

	public void setTotalDuplicateIMEI(String totalDuplicateIMEI) {
		this.totalDuplicateIMEI = totalDuplicateIMEI;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ImeiUsageRowData [totalPairedIMEI=");
		builder.append(totalPairedIMEI);
		builder.append(", totalDuplicateIMEI=");
		builder.append(totalDuplicateIMEI);
		builder.append("]");
		return builder.toString();
	}
}
