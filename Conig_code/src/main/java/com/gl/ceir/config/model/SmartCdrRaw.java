package com.gl.ceir.config.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;

@ApiModel
@Entity
@Table(name = "smart_raw")
public class SmartCdrRaw extends BaseCdrRawEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	
}
