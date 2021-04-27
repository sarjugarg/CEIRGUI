package org.gl.ceir.CeirPannelCode.Model;

public class ConsignmentFilterPojo {
	
	private String endDate;
	private String startDate;
	private String fileStatus;
	private String taxPaidStatus;
	private int importerId;
	private String  consignmentStatus;
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getFileStatus() {
		return fileStatus;
	}
	public void setFileStatus(String fileStatus) {
		this.fileStatus = fileStatus;
	}
	public String getTaxPaidStatus() {
		return taxPaidStatus;
	}
	public void setTaxPaidStatus(String taxPaidStatus) {
		this.taxPaidStatus = taxPaidStatus;
	}
	public int getImporterId() {
		return importerId;
	}
	public void setImporterId(int importerId) {
		this.importerId = importerId;
	}
	public String getConsignmentStatus() {
		return consignmentStatus;
	}
	public void setConsignmentStatus(String consignmentStatus) {
		this.consignmentStatus = consignmentStatus;
	}
	@Override
	public String toString() {
		return "ConsignmentFilterPojo [endDate=" + endDate + ", startDate=" + startDate + ", fileStatus=" + fileStatus
				+ ", taxPaidStatus=" + taxPaidStatus + ", importerId=" + importerId + ", consignmentStatus="
				+ consignmentStatus + "]";
	}
	
	
	

}
