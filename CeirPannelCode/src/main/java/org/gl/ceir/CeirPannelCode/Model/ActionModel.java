package org.gl.ceir.CeirPannelCode.Model;

import org.springframework.stereotype.Component;

@Component
public class ActionModel {
	private Integer state;
	private Long stateId;
	private String view;
	private String downloadErrorFile;
	private String edit;
	private String downloadFile;
	private String delete;
	private String approve;
	private String reject;
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Long getStateId() {
		return stateId;
	}
	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}
	public String getView() {
		return view;
	}
	public void setView(String view) {
		this.view = view;
	}
	public String getDownloadErrorFile() {
		return downloadErrorFile;
	}
	public void setDownloadErrorFile(String downloadErrorFile) {
		this.downloadErrorFile = downloadErrorFile;
	}
	public String getEdit() {
		return edit;
	}
	public void setEdit(String edit) {
		this.edit = edit;
	}
	public String getDownloadFile() {
		return downloadFile;
	}
	public void setDownloadFile(String downloadFile) {
		this.downloadFile = downloadFile;
	}
	public String getDelete() {
		return delete;
	}
	public void setDelete(String delete) {
		this.delete = delete;
	}
	public String getApprove() {
		return approve;
	}
	public void setApprove(String approve) {
		this.approve = approve;
	}
	public String getReject() {
		return reject;
	}
	public void setReject(String reject) {
		this.reject = reject;
	}
	@Override
	public String toString() {
		return "ActionModel [state=" + state + ", stateId=" + stateId + ", view=" + view + ", downloadErrorFile="
				+ downloadErrorFile + ", edit=" + edit + ", downloadFile=" + downloadFile + ", delete=" + delete
				+ ", approve=" + approve + ", reject=" + reject + "]";
	}
	
	
	
}
