package com.gl.ceir.config.model;

public class GenricResponse {

	private int errorCode;
	private String tag;
	private String message;
	private String txnId;
	private Object data;

	public GenricResponse(int errorCode) {
		this.errorCode = errorCode;
	}
	
	public GenricResponse(int errorCode, String message, String txnId) {
		this.errorCode = errorCode;
		this.message = message;
		this.txnId = txnId;
	}
	
	public GenricResponse(int errorCode, String tag, String message, String txnId) {
		this.errorCode = errorCode;
		this.tag = tag;
		this.message = message;
		this.txnId = txnId;
	}
	
	public GenricResponse(int errorCode, String message, String txnId, Object data) {
		this.errorCode = errorCode;
		this.message = message;
		this.txnId = txnId;
		this.data = data;
	}

	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public Object getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GenricResponse [errorCode=");
		builder.append(errorCode);
		builder.append(", message=");
		builder.append(message);
		builder.append(", txnId=");
		builder.append(txnId);
		builder.append(", data=");
		builder.append(data);
		builder.append("]");
		return builder.toString();
	}

}
