package com.gl.ceir.config.model.file;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

public class EndUserFileModel {

	@CsvBindByName(column = "Txn Id")
	@CsvBindByPosition(position = 0)
	private String txnId;
	
	@CsvBindByName(column = "Register date")
	@CsvBindByPosition(position = 1)
	private String registerDate;
	
	@CsvBindByName(column = "Passport No.")
	@CsvBindByPosition(position = 2)
	private String passportNumber;
	
	@CsvBindByName(column = "Name")
	@CsvBindByPosition(position = 3)
	private String name;
	
	@CsvBindByName(column = "Nationality")
	@CsvBindByPosition(position = 4)
	private String nationality;
	
	@CsvBindByName(column = "Visa Expiry date")
	@CsvBindByPosition(position = 5)
	private String visaExpirydate;
	
	@CsvBindByName(column = "Local Contact Number")
	@CsvBindByPosition(position = 6)
	private String localContactNumber;

	public EndUserFileModel() {
		// TODO Auto-generated constructor stub
	}

	public EndUserFileModel(String txnId, String registerDate, String passportNumber, String name,
			String nationality, String visaExpirydate, String localContactNumber) {
		this.txnId = txnId;
		this.registerDate = registerDate;
		this.passportNumber = passportNumber;
		this.name = name;
		this.nationality = nationality;
		this.visaExpirydate = visaExpirydate;
		this.localContactNumber = localContactNumber;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public String getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}

	public String getPassportNumber() {
		return passportNumber;
	}

	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getVisaExpirydate() {
		return visaExpirydate;
	}

	public void setVisaExpirydate(String visaExpirydate) {
		this.visaExpirydate = visaExpirydate;
	}

	public String getLocalContactNumber() {
		return localContactNumber;
	}

	public void setLocalContactNumber(String localContactNumber) {
		this.localContactNumber = localContactNumber;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EndUserFileModel [txnId=");
		builder.append(txnId);
		builder.append(", registerDate=");
		builder.append(registerDate);
		builder.append(", passportNumber=");
		builder.append(passportNumber);
		builder.append(", name=");
		builder.append(name);
		builder.append(", nationality=");
		builder.append(nationality);
		builder.append(", visaExpirydate=");
		builder.append(visaExpirydate);
		builder.append(", localContactNumber=");
		builder.append(localContactNumber);
		builder.append("]");
		return builder.toString();
	}

}
