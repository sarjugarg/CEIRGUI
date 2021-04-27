package org.gl.ceir.CeirPannelCode.Model;

public class FileCopyToOtherServer {


private String copyEndOn;
private String copyStartOn;
private String copyStatus;
private String createdOn;
private String fileName;
private String filePath;
private Integer id;
private Integer retryCount;
private Integer serverId;
private String txnId;


public String getCopyEndOn() {
return copyEndOn;
}

public void setCopyEndOn(String copyEndOn) {
this.copyEndOn = copyEndOn;
}

public String getCopyStartOn() {
return copyStartOn;
}

public void setCopyStartOn(String copyStartOn) {
this.copyStartOn = copyStartOn;
}

public String getCopyStatus() {
return copyStatus;
}

public void setCopyStatus(String copyStatus) {
this.copyStatus = copyStatus;
}

public String getCreatedOn() {
return createdOn;
}

public void setCreatedOn(String createdOn) {
this.createdOn = createdOn;
}

public String getFileName() {
return fileName;
}

public void setFileName(String fileName) {
this.fileName = fileName;
}

public String getFilePath() {
return filePath;
}

public void setFilePath(String filePath) {
this.filePath = filePath;
}

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public Integer getRetryCount() {
return retryCount;
}

public void setRetryCount(Integer retryCount) {
this.retryCount = retryCount;
}

public Integer getServerId() {
return serverId;
}

public void setServerId(Integer serverId) {
this.serverId = serverId;
}

public String getTxnId() {
return txnId;
}

public void setTxnId(String txnId) {
this.txnId = txnId;
}

@Override
public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("FileCopyToOtherServer [copyEndOn=");
	builder.append(copyEndOn);
	builder.append(", copyStartOn=");
	builder.append(copyStartOn);
	builder.append(", copyStatus=");
	builder.append(copyStatus);
	builder.append(", createdOn=");
	builder.append(createdOn);
	builder.append(", fileName=");
	builder.append(fileName);
	builder.append(", filePath=");
	builder.append(filePath);
	builder.append(", id=");
	builder.append(id);
	builder.append(", retryCount=");
	builder.append(retryCount);
	builder.append(", serverId=");
	builder.append(serverId);
	builder.append(", txnId=");
	builder.append(txnId);
	builder.append("]");
	return builder.toString();
}


}
