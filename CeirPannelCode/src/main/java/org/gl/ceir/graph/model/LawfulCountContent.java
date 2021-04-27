package org.gl.ceir.graph.model;

import java.util.List;

import org.springframework.stereotype.Component;
@Component
public class LawfulCountContent {
	private Object dbName;
	private String tableName;
	private List<String> columns = null;
	private List<LawfulDeviceCountData> rowData = null;
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
	public List<LawfulDeviceCountData> getRowData() {
		return rowData;
	}
	public void setRowData(List<LawfulDeviceCountData> rowData) {
		this.rowData = rowData;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LawfulCountContent [dbName=");
		builder.append(dbName);
		builder.append(", tableName=");
		builder.append(tableName);
		builder.append(", columns=");
		builder.append(columns);
		builder.append(", rowData=");
		builder.append(rowData);
		builder.append("]");
		return builder.toString();
	}
	
	
}
