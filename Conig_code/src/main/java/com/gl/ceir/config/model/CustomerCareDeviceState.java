package com.gl.ceir.config.model;

import java.io.Serializable;
import java.util.Objects;

public class CustomerCareDeviceState implements Serializable{

	private static final long serialVersionUID = 1L;

	private String name;
	private String date;
	private String status;
	private String txnId;
	private Integer featureId;
	private String imei;
	private String msisdn;

	public CustomerCareDeviceState() {}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		if(Objects.isNull(date) || date.isEmpty()) {
			this.date = "";
		}else {
			this.date = date.substring(0, 10);
		}
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getFeatureId() {
		return featureId;
	}

	public void setFeatureId(Integer featureId) {
		this.featureId = featureId;
	}
	
	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}
	

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CustomerCareDeviceState [name=");
		builder.append(name);
		builder.append(", date=");
		builder.append(date);
		builder.append(", status=");
		builder.append(status);
		builder.append(", txnId=");
		builder.append(txnId);
		builder.append(", featureId=");
		builder.append(featureId);
		builder.append(", imei=");
		builder.append(imei);
		builder.append(", msisdn=");
		builder.append(msisdn);
		builder.append("]");
		return builder.toString();
	}

}
