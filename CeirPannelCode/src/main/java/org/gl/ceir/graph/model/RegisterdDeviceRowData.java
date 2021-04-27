package org.gl.ceir.graph.model;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

@Component
public class RegisterdDeviceRowData {
	@JsonProperty("Date")
	@SerializedName("Date")
	private String date;
	
	@JsonProperty("Equipment Type")
	@SerializedName("Equipment Type")
	private String equipmentType;
	
	@JsonProperty("Count")
	@SerializedName("Count")
	private String count;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getEquipmentType() {
		return equipmentType;
	}

	public void setEquipmentType(String equipmentType) {
		this.equipmentType = equipmentType;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RegisterdDeviceRowData [date=");
		builder.append(date);
		builder.append(", equipmentType=");
		builder.append(equipmentType);
		builder.append(", count=");
		builder.append(count);
		builder.append("]");
		return builder.toString();
	}
	
	
}
