package org.gl.ceir.CeirPannelCode.Model;

public class AnonymousUser {
	private Integer id;
	private String usertypeName,createdOn,modifiedOn;
	
	
	
	
	@Override
	public String toString() {
		return "AnonymousUser [id=" + id + ", usertypeName=" + usertypeName + ", createdOn=" + createdOn
				+ ", modifiedOn=" + modifiedOn + "]";
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsertypeName() {
		return usertypeName;
	}
	public void setUsertypeName(String usertypeName) {
		this.usertypeName = usertypeName;
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
	
	

}
