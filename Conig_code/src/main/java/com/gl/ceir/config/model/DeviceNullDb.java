package com.gl.ceir.config.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Audited
@Table(name="device_null_db")
public class DeviceNullDb  implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "msisdn")
	private String msisdn;
	
	@Column(name = "imsi")
	private Long imsi;

	@JsonIgnore
	@CreationTimestamp
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	@Column(updatable = false)
	private Date createdOn;

	@JsonIgnore
	@UpdateTimestamp
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private Date updatedOn;

	@Column(name = "create_filename", length = 50)
	private String createFilename;
	
	@Column(name = "update_filename", length = 50)
	private String updateFilename;
	
	@Column(name = "record_type")
	private Integer recordType;
	
	@Column(name = "system_type", length = 100)
	private String systemType;

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public Long getImsi() {
		return imsi;
	}

	public void setImsi(Long imsi) {
		this.imsi = imsi;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getCreateFilename() {
		return createFilename;
	}

	public void setCreateFilename(String createFilename) {
		this.createFilename = createFilename;
	}

	public String getUpdateFilename() {
		return updateFilename;
	}

	public void setUpdateFilename(String updateFilename) {
		this.updateFilename = updateFilename;
	}

	public Integer getRecordType() {
		return recordType;
	}

	public void setRecordType(Integer recordType) {
		this.recordType = recordType;
	}

	public String getSystemType() {
		return systemType;
	}

	public void setSystemType(String systemType) {
		this.systemType = systemType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DeviceNullDb [msisdn=");
		builder.append(msisdn);
		builder.append(", imsi=");
		builder.append(imsi);
		builder.append(", createdOn=");
		builder.append(createdOn);
		builder.append(", updatedOn=");
		builder.append(updatedOn);
		builder.append(", createFilename=");
		builder.append(createFilename);
		builder.append(", updateFilename=");
		builder.append(updateFilename);
		builder.append(", recordType=");
		builder.append(recordType);
		builder.append(", systemType=");
		builder.append(systemType);
		builder.append("]");
		return builder.toString();
	}
	
}
