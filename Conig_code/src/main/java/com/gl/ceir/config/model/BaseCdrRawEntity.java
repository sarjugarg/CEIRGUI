package com.gl.ceir.config.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseCdrRawEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SNO")
	private Integer sNo;
	
	@Column(name = "TIME")
	private LocalDateTime time;
	
	@Column(name = "RECORDTYPE")
	private String recordType;
	
	@Column(name = "SERVEDIMEI")
	private String servedImei;
	
	@Column(name = "SERVEDIMSI")
	private String servedImsi;
	
	@Column(name = "SERVEDMSISDN")
	private String servedMsisdn;
	
	@Column(name = "SYSTEMTYPE")
	private String systemType;
	
	@Column(name = "OPERATOR")
	private String operator;
	
	@Column(name = "FILE_NAME")
	private String fileName;
	
	@Column(name = "RECORD_TIME")
	private LocalDateTime recordTime;
	
	@Column(name = "STATUS")
	private String status;

	public Integer getsNo() {
		return sNo;
	}

	public void setsNo(Integer sNo) {
		this.sNo = sNo;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public String getServedImei() {
		return servedImei;
	}

	public void setServedImei(String servedImei) {
		this.servedImei = servedImei;
	}

	public String getServedImsi() {
		return servedImsi;
	}

	public void setServedImsi(String servedImsi) {
		this.servedImsi = servedImsi;
	}

	public String getServedMsisdn() {
		return servedMsisdn;
	}

	public void setServedMsisdn(String servedMsisdn) {
		this.servedMsisdn = servedMsisdn;
	}

	public String getSystemType() {
		return systemType;
	}

	public void setSystemType(String systemType) {
		this.systemType = systemType;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public LocalDateTime getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(LocalDateTime recordTime) {
		this.recordTime = recordTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BaseCdrRawEntity [sNo=");
		builder.append(sNo);
		builder.append(", time=");
		builder.append(time);
		builder.append(", recordType=");
		builder.append(recordType);
		builder.append(", servedImei=");
		builder.append(servedImei);
		builder.append(", servedImsi=");
		builder.append(servedImsi);
		builder.append(", servedMsisdn=");
		builder.append(servedMsisdn);
		builder.append(", systemType=");
		builder.append(systemType);
		builder.append(", operator=");
		builder.append(operator);
		builder.append(", fileName=");
		builder.append(fileName);
		builder.append(", recordTime=");
		builder.append(recordTime);
		builder.append(", status=");
		builder.append(status);
		builder.append("]");
		return builder.toString();
	}
	
}
