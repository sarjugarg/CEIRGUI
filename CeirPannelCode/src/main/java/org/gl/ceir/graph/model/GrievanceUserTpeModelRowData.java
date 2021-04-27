package org.gl.ceir.graph.model;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
@Component
public class GrievanceUserTpeModelRowData {


	@SerializedName("DRT")
	@JsonProperty("DRT")
	private int drt;
	
	@SerializedName("Custom")
	@JsonProperty("Custom")
	private int custom;
	
	@SerializedName("Distributor")
	@JsonProperty("Distributor")
	private int distributor;
	
	@SerializedName("Immigration")
	@JsonProperty("Immigration")
	private int immigration;
	
	
	
	@SerializedName("Importer")
	@JsonProperty("Importer")
	private int importer;
	
	@SerializedName("Lawful Agency")
	@JsonProperty("Lawful Agency")
	private int lawfulAgency;
	
	@SerializedName("Manufacturer")
	@JsonProperty("Manufacturer")
	private String manufacturer;
	
	@SerializedName("Operator")
	@JsonProperty("Operator")
	private int operator;
	
	@SerializedName("Retailer")
	@JsonProperty("Retailer")
	private int retailer;
	
	@SerializedName("TRC")
	@JsonProperty("TRC")
	private int trc;
	
	
	@SerializedName("Date")
	@JsonProperty("Date")
	private String date;

	@SerializedName("End User")
	@JsonProperty("End User")
	private int endUser;
	
	public int getDrt() {
		return drt;
	}


	public void setDrt(int drt) {
		this.drt = drt;
	}


	public int getCustom() {
		return custom;
	}


	public void setCustom(int custom) {
		this.custom = custom;
	}


	public int getDistributor() {
		return distributor;
	}


	public void setDistributor(int distributor) {
		this.distributor = distributor;
	}


	public int getImmigration() {
		return immigration;
	}


	public void setImmigration(int immigration) {
		this.immigration = immigration;
	}


	public int getImporter() {
		return importer;
	}


	public void setImporter(int importer) {
		this.importer = importer;
	}


	public int getLawfulAgency() {
		return lawfulAgency;
	}


	public void setLawfulAgency(int lawfulAgency) {
		this.lawfulAgency = lawfulAgency;
	}


	public String getManufacturer() {
		return manufacturer;
	}


	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}


	public int getOperator() {
		return operator;
	}


	public void setOperator(int operator) {
		this.operator = operator;
	}


	public int getRetailer() {
		return retailer;
	}


	public void setRetailer(int retailer) {
		this.retailer = retailer;
	}


	public int getTrc() {
		return trc;
	}


	public void setTrc(int trc) {
		this.trc = trc;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public int getEndUser() {
		return endUser;
	}


	public void setEndUser(int endUser) {
		this.endUser = endUser;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GrievanceUserTpeModelRowData [drt=");
		builder.append(drt);
		builder.append(", custom=");
		builder.append(custom);
		builder.append(", distributor=");
		builder.append(distributor);
		builder.append(", immigration=");
		builder.append(immigration);
		builder.append(", importer=");
		builder.append(importer);
		builder.append(", lawfulAgency=");
		builder.append(lawfulAgency);
		builder.append(", manufacturer=");
		builder.append(manufacturer);
		builder.append(", operator=");
		builder.append(operator);
		builder.append(", retailer=");
		builder.append(retailer);
		builder.append(", trc=");
		builder.append(trc);
		builder.append(", date=");
		builder.append(date);
		builder.append(", endUser=");
		builder.append(endUser);
		builder.append("]");
		return builder.toString();
	}



	

}
