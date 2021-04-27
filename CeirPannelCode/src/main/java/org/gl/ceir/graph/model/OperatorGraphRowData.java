package org.gl.ceir.graph.model;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

@Component
public class OperatorGraphRowData {
	@JsonProperty("Total IMEI Count")
	@SerializedName("Total IMEI Count")
	private String totalImei;
	
	@JsonProperty("Operator Name")
	@SerializedName("Operator Name")
	private String operatorName;

	public String getTotalImei() {
		return totalImei;
	}

	public void setTotalImei(String totalImei) {
		this.totalImei = totalImei;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OperatorGraphRowData [totalImei=");
		builder.append(totalImei);
		builder.append(", operatorName=");
		builder.append(operatorName);
		builder.append("]");
		return builder.toString();
	}
	
	
	
}
