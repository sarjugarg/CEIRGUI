package org.gl.ceir.CeirPannelCode.Model;

public class AssigneRequestType {

	private String field;
	private  Integer type;
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AssigneRequestType [field=");
		builder.append(field);
		builder.append(", type=");
		builder.append(type);
		builder.append("]");
		return builder.toString();
	}

	
	
	
}
