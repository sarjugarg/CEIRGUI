package com.gl.ceir.config.model.file;

import java.util.Date;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

public class UpdateVisaFileModel {

	

	 
	
	
	@CsvBindByName(column = "Modified On")
	@CsvBindByPosition(position = 1)
	private String modifiedOn;
	@CsvBindByName(column = "Created On")
	@CsvBindByPosition(position = 0)
	private String requestedOn;
	@CsvBindByName(column = "Transaction ID")
	@CsvBindByPosition(position = 2)
	private String txnId;
	@CsvBindByName(column = "Passport Number")
	@CsvBindByPosition(position = 3)
	private String nid;
	@CsvBindByName(column = "Visa Type")
	@CsvBindByPosition(position = 4)
	private String visaType;
	
	@CsvBindByName(column = "Visa Number")
	@CsvBindByPosition(position = 5)
	private String visaNumber;
	
	@CsvBindByName(column = "File Name")
	@CsvBindByPosition(position = 6)
	private String fileName;	
	@CsvBindByName(column = "Visa Expiry Date")
	@CsvBindByPosition(position = 7)
	private String visaExpiryDate;
	
	@CsvBindByName(column = "Status")
	@CsvBindByPosition(position = 8)
	private String status;

	public String getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public String getRequestedOn() {
		return requestedOn;
	}

	public void setRequestedOn(String requestedOn) {
		this.requestedOn = requestedOn;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	public String getVisaType() {
		return visaType;
	}

	public void setVisaType(String visaType) {
		this.visaType = visaType;
	}

	public String getVisaNumber() {
		return visaNumber;
	}

	public void setVisaNumber(String visaNumber) {
		this.visaNumber = visaNumber;
	}

	

	

	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getVisaExpiryDate() {
		return visaExpiryDate;
	}

	public void setVisaExpiryDate(String visaExpiryDate) {
		this.visaExpiryDate = visaExpiryDate;
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
		builder.append("UpdateVisaFileModel [modifiedOn=");
		builder.append(modifiedOn);
		builder.append(", requestedOn=");
		builder.append(requestedOn);
		builder.append(", txnId=");
		builder.append(txnId);
		builder.append(", nid=");
		builder.append(nid);
		builder.append(", visaType=");
		builder.append(visaType);
		builder.append(", visaNumber=");
		builder.append(visaNumber);
		builder.append(", fileName=");
		builder.append(fileName);
		builder.append(", visaExpiryDate=");
		builder.append(visaExpiryDate);
		builder.append(", status=");
		builder.append(status);
		builder.append("]");
		return builder.toString();
	}


	
}
