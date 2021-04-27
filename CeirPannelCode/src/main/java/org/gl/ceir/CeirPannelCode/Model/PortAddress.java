package org.gl.ceir.CeirPannelCode.Model;


public class PortAddress {

	private long id;
	
	private String createdOn;
	
	private String modifiedOn;
	
	private String address;
	
	private Integer port;

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	@Override
	public String toString() {
		return "PortAddress [id=" + id + ", createdOn=" + createdOn + ", modifiedOn=" + modifiedOn + ", address="
				+ address + ", port=" + port + "]";
	}
	
	
	
}
