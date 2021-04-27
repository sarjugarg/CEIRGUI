package com.gl.ceir.config.model;

public class CheckDeviceReponse {

	private String tacNumber;
	private String bandName;
	private String modelName;
	private long deviceId;
	private String status;
	private String equipmentType;
	private String operatingSystem;
	private String imei;
	private String msidn;
	private String identifierType;
	
	public String getTacNumber() {
		return tacNumber;
	}
	public void setTacNumber(String tacNumber) {
		this.tacNumber = tacNumber;
	}
	public String getBandName() {
		return bandName;
	}
	public void setBandName(String bandName) {
		this.bandName = bandName;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	
	
	public long getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(long deviceId) {
		this.deviceId = deviceId;
	}
	public String getEquipmentType() {
		return equipmentType;
	}
	public void setEquipmentType(String equipmentType) {
		this.equipmentType = equipmentType;
	}
	public String getOperatingSystem() {
		return operatingSystem;
	}
	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getMsidn() {
		return msidn;
	}
	public void setMsidn(String msidn) {
		this.msidn = msidn;
	}
	public String getIdentifierType() {
		return identifierType;
	}
	public void setIdentifierType(String identifierType) {
		this.identifierType = identifierType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CheckDeviceReponse [tacNumber=");
		builder.append(tacNumber);
		builder.append(", bandName=");
		builder.append(bandName);
		builder.append(", modelName=");
		builder.append(modelName);
		builder.append(", deviceId=");
		builder.append(deviceId);
		builder.append(", status=");
		builder.append(status);
		builder.append(", equipmentType=");
		builder.append(equipmentType);
		builder.append(", operatingSystem=");
		builder.append(operatingSystem);
		builder.append(", imei=");
		builder.append(imei);
		builder.append(", msidn=");
		builder.append(msidn);
		builder.append(", identifierType=");
		builder.append(identifierType);
		builder.append("]");
		return builder.toString();
	}
	
	
	
}
