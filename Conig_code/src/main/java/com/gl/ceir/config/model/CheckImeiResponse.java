package com.gl.ceir.config.model;

public class CheckImeiResponse {

	private String status;
	private String errorMessage;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CheckDeviceResponse [status=");
		builder.append(status);
		builder.append(", errorMessage=");
		builder.append(errorMessage);
		builder.append("]");
		return builder.toString();
	}
}
