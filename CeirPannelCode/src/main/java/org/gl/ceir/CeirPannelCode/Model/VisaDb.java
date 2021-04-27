package org.gl.ceir.CeirPannelCode.Model;

import java.util.HashMap;
import java.util.Map;

public class VisaDb {
	

private String createdOn;

private String modifiedOn;
private String visaExpiryDate;
private String visaNumber,visaFileName,visaTypeInterp;
private Integer visaType;
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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
public String getVisaExpiryDate() {
	return visaExpiryDate;
}
public void setVisaExpiryDate(String visaExpiryDate) {
	this.visaExpiryDate = visaExpiryDate;
}
public String getVisaNumber() {
	return visaNumber;
}
public void setVisaNumber(String visaNumber) {
	this.visaNumber = visaNumber;
}
public String getVisaFileName() {
	return visaFileName;
}
public void setVisaFileName(String visaFileName) {
	this.visaFileName = visaFileName;
}
public Integer getVisaType() {
	return visaType;
}
public void setVisaType(Integer visaType) {
	this.visaType = visaType;
}
public Map<String, Object> getAdditionalProperties() {
	return additionalProperties;
}
public void setAdditionalProperties(Map<String, Object> additionalProperties) {
	this.additionalProperties = additionalProperties;
}


public String getVisaTypeInterp() {
	return visaTypeInterp;
}
public void setVisaTypeInterp(String visaTypeInterp) {
	this.visaTypeInterp = visaTypeInterp;
}
@Override
public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("VisaDb [createdOn=");
	builder.append(createdOn);
	builder.append(", modifiedOn=");
	builder.append(modifiedOn);
	builder.append(", visaExpiryDate=");
	builder.append(visaExpiryDate);
	builder.append(", visaNumber=");
	builder.append(visaNumber);
	builder.append(", visaFileName=");
	builder.append(visaFileName);
	builder.append(", visaTypeInterp=");
	builder.append(visaTypeInterp);
	builder.append(", visaType=");
	builder.append(visaType);
	builder.append(", additionalProperties=");
	builder.append(additionalProperties);
	builder.append("]");
	return builder.toString();
}


}
