package org.gl.ceir.CeirPannelCode.Model;

public class CountApprovedData {
	
	private Integer allowed,current,status;
	private String tacNumber,brandName,modelName;
	public Integer getAllowed() {
		return allowed;
	}
	public void setAllowed(Integer allowed) {
		this.allowed = allowed;
	}
	public Integer getCurrent() {
		return current;
	}
	public void setCurrent(Integer current) {
		this.current = current;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getTacNumber() {
		return tacNumber;
	}
	public void setTacNumber(String tacNumber) {
		this.tacNumber = tacNumber;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	@Override
	public String toString() {
		return "CountApprovedData [allowed=" + allowed + ", current=" + current + ", status=" + status + ", tacNumber="
				+ tacNumber + ", brandName=" + brandName + ", modelName=" + modelName + "]";
	}
	
	

	
}
