package org.gl.ceir.CeirPannelCode.Model;

import org.gl.ceir.pagination.model.UserPaidStatusContent;

public class UpdateVisaModel {
	private Integer errorCode;
	private String tag;
	private String message;
	private String txnId;
	private EndUserVisaInfo data;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UpdateVisaModel [errorCode=");
		builder.append(errorCode);
		builder.append(", tag=");
		builder.append(tag);
		builder.append(", message=");
		builder.append(message);
		builder.append(", txnId=");
		builder.append(txnId);
		builder.append(", data=");
		builder.append(data);
		builder.append("]");
		return builder.toString();
	}
	public Integer getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
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
	public EndUserVisaInfo getData() {
		return data;
	}
	public void setData(EndUserVisaInfo data) {
		this.data = data;
	}



}
