package org.gl.ceir.CeirPannelCode.Model;

import java.util.List;

public class MultipleFileRequest {
	private List<MultipleFileModel> multifile;
	private String remarks;
	private String txnId;
	private int categoryId;
	@Override
	public String toString() {
		return "MultipleFileRequest [multifile=" + multifile + ", remarks=" + remarks + ", txnId=" + txnId
				+ ", categoryId=" + categoryId + "]";
	}
	public List<MultipleFileModel> getMultifile() {
		return multifile;
	}
	public void setMultifile(List<MultipleFileModel> multifile) {
		this.multifile = multifile;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}


}
