package org.gl.ceir.graph.model;

public class ScheduleRequest {
	 
	public String createdOn,modifiedOn;
	private int id;
	private String action,category,emailId,flag,reportName;
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ScheduleRequest [createdOn=");
		builder.append(createdOn);
		builder.append(", modifiedOn=");
		builder.append(modifiedOn);
		builder.append(", id=");
		builder.append(id);
		builder.append(", action=");
		builder.append(action);
		builder.append(", category=");
		builder.append(category);
		builder.append(", emailId=");
		builder.append(emailId);
		builder.append(", flag=");
		builder.append(flag);
		builder.append(", reportName=");
		builder.append(reportName);
		builder.append("]");
		return builder.toString();
	}
	
	
}
