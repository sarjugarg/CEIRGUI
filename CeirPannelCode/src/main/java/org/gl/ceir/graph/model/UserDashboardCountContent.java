package org.gl.ceir.graph.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDashboardCountContent {
	private Object dbName;
	private String tableName;
	private List<String> columns = null;
	private List<UserDashboardCountData> rowData = null;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	public Object getDbName() {
		return dbName;
	}
	public void setDbName(Object dbName) {
		this.dbName = dbName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public List<String> getColumns() {
		return columns;
	}
	public void setColumns(List<String> columns) {
		this.columns = columns;
	}
	public List<UserDashboardCountData> getRowData() {
		return rowData;
	}
	public void setRowData(List<UserDashboardCountData> rowData) {
		this.rowData = rowData;
	}
	public Map<String, Object> getAdditionalProperties() {
		return additionalProperties;
	}
	public void setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GraphContent [dbName=");
		builder.append(dbName);
		builder.append(", tableName=");
		builder.append(tableName);
		builder.append(", columns=");
		builder.append(columns);
		builder.append(", rowData=");
		builder.append(rowData);
		builder.append(", additionalProperties=");
		builder.append(additionalProperties);
		builder.append("]");
		return builder.toString();
	}
	
}
