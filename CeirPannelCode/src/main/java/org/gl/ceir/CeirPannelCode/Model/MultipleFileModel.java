package org.gl.ceir.CeirPannelCode.Model;


public class MultipleFileModel {
private String docTypeInterp,docType,url;
private String fileName;
private String grievanceId;
public String getDocTypeInterp() {
return docTypeInterp;
}
public void setDocTypeInterp(String docTypeInterp) {
this.docTypeInterp = docTypeInterp;
}
public String getDocType() {
return docType;
}
public void setDocType(String docType) {
this.docType = docType;
}
public String getUrl() {
return url;
}
public void setUrl(String url) {
this.url = url;
}
public String getFileName() {
return fileName;
}
public void setFileName(String fileName) {
this.fileName = fileName;
}
public String getGrievanceId() {
return grievanceId;
}
public void setGrievanceId(String grievanceId) {
this.grievanceId = grievanceId;
}
@Override
public String toString() {
return "MultipleFileModel [docTypeInterp=" + docTypeInterp + ", docType=" + docType + ", url=" + url
+ ", fileName=" + fileName + ", grievanceId=" + grievanceId + "]";
}




}