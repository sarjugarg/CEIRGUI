package com.gl.ceir.config.model;

public class CheckDevice{
	private static long serialVersionUID = 1L;
	private Integer deviceIdType;
	private String deviceId;
	public Integer getDeviceIdType() {
		return deviceIdType;
	}
	public void setDeviceIdType(Integer deviceIdType) {
		this.deviceIdType = deviceIdType;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CheckDevice [deviceIdType=");
		builder.append(deviceIdType);
		builder.append(", deviceId=");
		builder.append(deviceId);
		builder.append(", toString()=");
		builder.append(super.toString());
		builder.append("]");
		return builder.toString();
	}
}
