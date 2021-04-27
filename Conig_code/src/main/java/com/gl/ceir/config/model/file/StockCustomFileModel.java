package com.gl.ceir.config.model.file;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

public class StockCustomFileModel {
		
	@CsvBindByName(column = "Modified On")
	@CsvBindByPosition(position = 0)
	private String modifiedOn;
	
	@CsvBindByName(column = "Created On")
	@CsvBindByPosition(position = 1)
	private String createdOn;
	
	@CsvBindByName(column = "Assigned To")
	@CsvBindByPosition(position = 2)
	private String assigneName;
	
	
	@CsvBindByName(column = "Transaction ID")
	@CsvBindByPosition(position = 3)
	private String txnId;

	@CsvBindByName(column = "File Name")
	@CsvBindByPosition(position = 4)
	private String fileName;
	
	@CsvBindByName(column = "Status")
	@CsvBindByPosition(position = 5)
	private String stockStatus;
	
	
	@CsvBindByName(column = "IMEI Quantity")
	@CsvBindByPosition(position = 6)
	private Integer quantity;


	

	@CsvBindByName(column = "Device Quantity")
	@CsvBindByPosition(position = 7)
	private Integer deviceQuantity;

	
	@CsvBindByName(column = "Invoice Number ")
	@CsvBindByPosition(position = 8)
	private String invoiceNumber;

	public Integer getDeviceQuantity() {
		return deviceQuantity;
	}

	public void setDeviceQuantity(Integer deviceQuantity) {
		this.deviceQuantity = deviceQuantity;
	}

	public String getStockStatus() {
		return stockStatus;
	}

	public void setStockStatus(String stockStatus) {
		this.stockStatus = stockStatus;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
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

	
	public String getAssigneName() {
		return assigneName;
	}

	public void setAssigneName(String assigneName) {
		this.assigneName = assigneName;
	}

	

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("StockFileModel [createdOn=");
		builder.append(createdOn);
		builder.append(", txnId=");
		builder.append(txnId);
		builder.append(", fileName=");
		builder.append(fileName);
		builder.append(", quantity=");
		builder.append(quantity);
		builder.append(", modifiedOn=");
		builder.append(modifiedOn);
		builder.append(", stockStatus=");
		builder.append(stockStatus);
		builder.append(", deviceQuantity=");
		builder.append(deviceQuantity);
		builder.append(", assigneName=");
		builder.append(assigneName);
		builder.append(", invoiceNumber=");
		builder.append(invoiceNumber);
		builder.append("]");
		return builder.toString();
	}
}
