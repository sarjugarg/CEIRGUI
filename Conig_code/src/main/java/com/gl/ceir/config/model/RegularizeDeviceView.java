package com.gl.ceir.config.model;

public class RegularizeDeviceView extends AllRequest{

	private String txnId;

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RegularizeDeviceView [txnId=");
		builder.append(txnId);
		builder.append(", toString()=");
		builder.append(super.toString());
		builder.append("]");
		return builder.toString();
	}
	
}
