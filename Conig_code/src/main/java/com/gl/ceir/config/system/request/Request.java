package com.gl.ceir.config.system.request;

import java.time.LocalDateTime;

import com.gl.ceir.config.model.Rules;
import com.gl.ceir.config.model.constants.ActionNames;
import com.gl.ceir.config.model.constants.ImeiStatus;

public class Request {

	private Long imei;
	private Long msisdn;
	private String filename;
	private LocalDateTime systemEntry;
	private Long imsi;
	private Rules failRule;
	private String ticketId;
	private ImeiStatus status;
	private String state;
	private LocalDateTime nextRetryTime;
	private ActionNames actionNames;
	private String mobileOperator;
	private Long scriptId;

	public Long getImei() {
		return imei;
	}

	public void setImei(Long imei) {
		this.imei = imei;
	}

	public Long getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(Long msisdn) {
		this.msisdn = msisdn;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public LocalDateTime getSystemEntry() {
		return systemEntry;
	}

	public void setSystemEntry(LocalDateTime systemEntry) {
		this.systemEntry = systemEntry;
	}

	public Long getImsi() {
		return imsi;
	}

	public void setImsi(Long imsi) {
		this.imsi = imsi;
	}

	public String getTicketId() {
		return ticketId;
	}

	public Rules getFailRule() {
		return failRule;
	}

	public void setFailRule(Rules failRule) {
		this.failRule = failRule;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

	public ImeiStatus getStatus() {
		return status;
	}

	public void setStatus(ImeiStatus status) {
		this.status = status;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public LocalDateTime getNextRetryTime() {
		return nextRetryTime;
	}

	public void setNextRetryTime(LocalDateTime nextRetryTime) {
		this.nextRetryTime = nextRetryTime;
	}

	public ActionNames getActionNames() {
		return actionNames;
	}

	public void setActionNames(ActionNames actionNames) {
		this.actionNames = actionNames;
	}

	public String getMobileOperator() {
		return mobileOperator;
	}

	public void setMobileOperator(String mobileOperator) {
		this.mobileOperator = mobileOperator;
	}

	public Long getScriptId() {
		return scriptId;
	}

	public void setScriptId(Long scriptId) {
		this.scriptId = scriptId;
	}

	@Override
	public String toString() {
		return "Request [imei=" + imei + ", msisdn=" + msisdn + ", filename=" + filename + ", systemEntry="
				+ systemEntry + ", imsi=" + imsi + ", failRule=" + failRule + ", ticketId=" + ticketId + ", status="
				+ status + ", state=" + state + ", nextRetryTime=" + nextRetryTime + ", actionNames=" + actionNames
				+ ", mobileOperator=" + mobileOperator + "]";
	}

}
