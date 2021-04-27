package com.gl.ceir.config.model.file;



import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

public class RegularizeDeviceFileModel {
	
	/*
	 * @CsvBindByName(column = "device_Id_Type")
	 * 
	 * @CsvBindByPosition(position = 6) private String deviceIdTypeInterp;
	 */
	
	/*
	 * @CsvBindByName(column = "Device Id Type")
	 * 
	 * @CsvBindByPosition(position = 5) private String deviceStatus;
	 */
	
	/*
	 * @CsvBindByName(column = "Device Type")
	 * 
	 * @CsvBindByPosition(position = 9)
	 * 
	 * private String deviceTypeInterp;
	 */
	
	/*
	 * @CsvBindByName(column = "Price")
	 * 
	 * @CsvBindByPosition(position = 8) private Double price;
	 */
	/*
	 * @CsvBindByName(column = "Currency")
	 * 
	 * @CsvBindByPosition(position = 10) private String currency;
	 */
	/*
	 * @CsvBindByName(column = "Country")
	 * 
	 * @CsvBindByPosition(position = 11) private String country;
	 */
	@CsvBindByName(column = "Tax Paid Status")
	@CsvBindByPosition(position = 5)
	private String TaxPaidStatus;
	
	@CsvBindByName(column = "Date")
	@CsvBindByPosition(position = 1)
	private String createdOn;
	
	@CsvBindByName(column = "ModifiedOn")
	@CsvBindByPosition(position = 0)
	private String modifiedOn;
	
	@CsvBindByName(column = "Transaction ID")
	@CsvBindByPosition(position = 3)
	private String txnId;
	
	@CsvBindByName(column = "First IMEI")
	@CsvBindByPosition(position = 8)
	private String firstImei;
	
	@CsvBindByName(column = "Second IMEI")
	@CsvBindByPosition(position = 9)
	private String secondImei;
	
	@CsvBindByName(column = "Third IMEI")
	@CsvBindByPosition(position = 10)
	private String thirdImei;
	
	@CsvBindByName(column = "Fourth IMEI")
	@CsvBindByPosition(position = 11)
	private String fourthImei;
	
	@CsvBindByName(column = "Status")
	@CsvBindByPosition(position = 7)
	private String status;
	
	@CsvBindByName(column = "Origin")
	@CsvBindByPosition(position = 6)
	private String origin;
	
	@CsvBindByName(column = "NID/Passport No.")
	@CsvBindByPosition(position = 2)
	private String nid;
	
	@CsvBindByName(column = "Nationality")
	@CsvBindByPosition(position = 4)
	private String nationality;
	
	
	public String getTaxPaidStatus() {
		return TaxPaidStatus;
	}
	public void setTaxPaidStatus(String taxPaidStatus) {
		TaxPaidStatus = taxPaidStatus;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public String getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public String getFirstImei() {
		return firstImei;
	}
	public void setFirstImei(String firstImei) {
		this.firstImei = firstImei;
	}
	public String getSecondImei() {
		return secondImei;
	}
	public void setSecondImei(String secondImei) {
		this.secondImei = secondImei;
	}
	public String getThirdImei() {
		return thirdImei;
	}
	public void setThirdImei(String thirdImei) {
		this.thirdImei = thirdImei;
	}
	public String getFourthImei() {
		return fourthImei;
	}
	public void setFourthImei(String fourthImei) {
		this.fourthImei = fourthImei;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getNid() {
		return nid;
	}
	public void setNid(String nid) {
		this.nid = nid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RegularizeDeviceFileModel [TaxPaidStatus=");
		builder.append(TaxPaidStatus);
		builder.append(", createdOn=");
		builder.append(createdOn);
		builder.append(", modifiedOn=");
		builder.append(modifiedOn);
		builder.append(", txnId=");
		builder.append(txnId);
		builder.append(", firstImei=");
		builder.append(firstImei);
		builder.append(", secondImei=");
		builder.append(secondImei);
		builder.append(", thirdImei=");
		builder.append(thirdImei);
		builder.append(", fourthImei=");
		builder.append(fourthImei);
		builder.append(", status=");
		builder.append(status);
		builder.append(", origin=");
		builder.append(origin);
		builder.append(", nid=");
		builder.append(nid);
		builder.append(", nationality=");
		builder.append(nationality);
		builder.append("]");
		return builder.toString();
	}
		
}
