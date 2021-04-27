package org.gl.ceir.graph.model;


import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
@Component
public class BrandModelRowData {
	@SerializedName("Date")
	@JsonProperty("Date")
	private String date;
	
	@SerializedName("Brand Name")
	@JsonProperty("Brand Name")
	private String brandName;
	
	@SerializedName("Model Name")
	@JsonProperty("Model Name")
	private String modelNumber;
	
	
	@SerializedName("Count")
	@JsonProperty("Count")
	private String count;


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BrandModelRowData [date=");
		builder.append(date);
		builder.append(", brandName=");
		builder.append(brandName);
		builder.append(", modelNumber=");
		builder.append(modelNumber);
		builder.append(", count=");
		builder.append(count);
		builder.append("]");
		return builder.toString();
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public String getBrandName() {
		return brandName;
	}


	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}


	public String getModelNumber() {
		return modelNumber;
	}


	public void setModelNumber(String modelNumber) {
		this.modelNumber = modelNumber;
	}


	public String getCount() {
		return count;
	}


	public void setCount(String count) {
		this.count = count;
	}

}
