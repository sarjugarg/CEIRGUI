package org.gl.ceir.CeirPannelCode.Model;

import org.springframework.stereotype.Component;

@Component
public class AddressModel {
	private String province;
	private Long districtID;
	private Long communeID;
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public Long getDistrictID() {
		return districtID;
	}
	public void setDistrictID(Long districtID) {
		this.districtID = districtID;
	}
	public Long getCommuneID() {
		return communeID;
	}
	public void setCommuneID(Long communeID) {
		this.communeID = communeID;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AddressManagement [province=");
		builder.append(province);
		builder.append(", districtID=");
		builder.append(districtID);
		builder.append(", communeID=");
		builder.append(communeID);
		builder.append("]");
		return builder.toString();
	}
	
	
	
}
