package com.gl.ceir.config.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "model_name")
@Entity
public class ModelRepoPojo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private int id;
	private int brandNameId;
	private String modelName;
	private String brandName;

	private LocalDateTime	createdOn;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getModelName() {
		return modelName;
	}
	public LocalDateTime getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public int getBrandNameId() {
		return brandNameId;
	}
	public void setBrandNameId(int brandNameId) {
		this.brandNameId = brandNameId;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	
}
