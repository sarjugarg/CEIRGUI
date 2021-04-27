package org.gl.ceir.CeirPannelCode.Model;

import java.util.HashMap;
import java.util.Map;

public class AttachedFile {

	private String docType;
	private String docTypeInterp;
	private String fileName;
	private Long id;
	private String TxnId;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	public String getDocType() {
		return docType;
	}
	public void setDocType(String docType) {
		this.docType = docType;
	}
	public String getDocTypeInterp() {
		return docTypeInterp;
	}
	public void setDocTypeInterp(String docTypeInterp) {
		this.docTypeInterp = docTypeInterp;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTxnId() {
		return TxnId;
	}
	public void setTxnId(String txnId) {
		TxnId = txnId;
	}
	public Map<String, Object> getAdditionalProperties() {
		return additionalProperties;
	}
	public void setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}
	@Override
	public String toString() {
		return "AttachedFile [docType=" + docType + ", docTypeInterp=" + docTypeInterp + ", fileName=" + fileName
				+ ", id=" + id + ", TxnId=" + TxnId + ", additionalProperties=" + additionalProperties + "]";
	}
	
	
}
