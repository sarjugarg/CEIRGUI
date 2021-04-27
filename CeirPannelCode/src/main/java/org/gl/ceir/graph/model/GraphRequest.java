package org.gl.ceir.graph.model;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class GraphRequest {

	private List<String> columns;
	private String groupBy;
	private String dbName;
	private String endDate ;
	private int reportnameId;
	private String searchString;
	private String startDate ;
	private String tableName;
	private String txnId ;
	private Boolean lastDate;
	private int file;
	private int pageSize;
	private int pageNo;
	private Integer typeFlag,dayDataLimit,featureId,userId;
	private String userType;
	private String publicIp,browser;
	public List<String> getColumns() {
		return columns;
	}
	public void setColumns(List<String> columns) {
		this.columns = columns;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getEndDate() {
		return endDate;
	}
	
	
	public String getGroupBy() {
		return groupBy;
	}
	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public int getReportnameId() {
		return reportnameId;
	}
	public void setReportnameId(int reportnameId) {
		this.reportnameId = reportnameId;
	}
	public String getSearchString() {
		return searchString;
	}
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	
	public int getFile() {
		return file;
	}
	public void setFile(int file) {
		this.file = file;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	
	public Boolean getLastDate() {
		return lastDate;
	}
	public void setLastDate(Boolean lastDate) {
		this.lastDate = lastDate;
	}
	public Integer getTypeFlag() {
		return typeFlag;
	}
	public void setTypeFlag(Integer typeFlag) {
		this.typeFlag = typeFlag;
	}
	public Integer getDayDataLimit() {
		return dayDataLimit;
	}
	public void setDayDataLimit(Integer dayDataLimit) {
		this.dayDataLimit = dayDataLimit;
	}
	public Integer getFeatureId() {
		return featureId;
	}
	public void setFeatureId(Integer featureId) {
		this.featureId = featureId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	 
	 
	public String getPublicIp() {
		return publicIp;
	}
	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}
	public String getBrowser() {
		return browser;
	}
	public void setBrowser(String browser) {
		this.browser = browser;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GraphRequest [columns=");
		builder.append(columns);
		builder.append(", groupBy=");
		builder.append(groupBy);
		builder.append(", dbName=");
		builder.append(dbName);
		builder.append(", endDate=");
		builder.append(endDate);
		builder.append(", reportnameId=");
		builder.append(reportnameId);
		builder.append(", searchString=");
		builder.append(searchString);
		builder.append(", startDate=");
		builder.append(startDate);
		builder.append(", tableName=");
		builder.append(tableName);
		builder.append(", txnId=");
		builder.append(txnId);
		builder.append(", lastDate=");
		builder.append(lastDate);
		builder.append(", file=");
		builder.append(file);
		builder.append(", pageSize=");
		builder.append(pageSize);
		builder.append(", pageNo=");
		builder.append(pageNo);
		builder.append(", typeFlag=");
		builder.append(typeFlag);
		builder.append(", dayDataLimit=");
		builder.append(dayDataLimit);
		builder.append(", featureId=");
		builder.append(featureId);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", userType=");
		builder.append(userType);
		builder.append(", publicIp=");
		builder.append(publicIp);
		builder.append(", browser=");
		builder.append(browser);
		builder.append("]");
		return builder.toString();
	}
	 
	
	
	 
	
	
}
