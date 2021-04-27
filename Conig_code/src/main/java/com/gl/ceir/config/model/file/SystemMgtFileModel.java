package com.gl.ceir.config.model.file;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

public class SystemMgtFileModel {
	
	@CsvBindByName(column = "Create On")
	@CsvBindByPosition(position = 0)
	private String createdOn;
	
	@CsvBindByName(column = "Modified On")
	@CsvBindByPosition(position = 1)
	private String modifiedOn;
	
	@CsvBindByName(column = "Description")
	@CsvBindByPosition(position = 2)
	private String description;
	
	@CsvBindByName(column = "Value")
	@CsvBindByPosition(position = 3)
	private String value;
	
	@CsvBindByName(column = "Type")
	@CsvBindByPosition(position = 4)
	private String userType;

	public SystemMgtFileModel() {
		
	}
	
	
	public SystemMgtFileModel(String createdOn, String modifiedOn, String description, String value, String userType) {
		super();
		this.createdOn = createdOn;
		this.modifiedOn = modifiedOn;
		this.description = description;
		this.value = value;
		this.userType = userType;
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


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}


	public String getUserType() {
		return userType;
	}


	public void setUserType(String userType) {
		this.userType = userType;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SystemMgtFileModel [createdOn=");
		builder.append(createdOn);
		builder.append(", modifiedOn=");
		builder.append(modifiedOn);
		builder.append(", description=");
		builder.append(description);
		builder.append(", value=");
		builder.append(value);
		builder.append(", userType=");
		builder.append(userType);
		builder.append("]");
		return builder.toString();
	}


}
