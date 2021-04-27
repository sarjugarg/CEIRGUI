package org.gl.ceir.pagination.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class PortContentModal {
	
	private Integer id;
	private String createdOn;
	private String modifiedOn;
	private String address;
	private Integer port;
	private String portInterp;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getPortInterp() {
		return portInterp;
	}
	public void setPortInterp(String portInterp) {
		this.portInterp = portInterp;
	}
	public Map<String, Object> getAdditionalProperties() {
		return additionalProperties;
	}
	public void setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}
	@Override
	public String toString() {
		return "PortContentModal [id=" + id + ", createdOn=" + createdOn + ", modifiedOn=" + modifiedOn + ", address="
				+ address + ", port=" + port + ", portInterp=" + portInterp + ", additionalProperties="
				+ additionalProperties + "]";
	}
	
	
	
	
	
}
