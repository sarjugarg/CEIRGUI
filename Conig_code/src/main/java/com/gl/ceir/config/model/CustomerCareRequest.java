package com.gl.ceir.config.model;

public class CustomerCareRequest {
	
	private String deviceIdType;
	private String imei;
	private String msisdn;
	private String userType;
	
	public CustomerCareRequest() {
		
	}
	
	public String getDeviceIdType() {
		return deviceIdType;
	}
	public void setDeviceIdType(String deviceIdType) {
		this.deviceIdType = deviceIdType;
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
	
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CustomerCareRequest [deviceIdType=");
		builder.append(deviceIdType);
		builder.append(", imei=");
		builder.append(imei);
		builder.append(", msisdn=");
		builder.append(msisdn);
		builder.append(", userType=");
		builder.append(userType);
		builder.append("]");
		return builder.toString();
	}
	
}
