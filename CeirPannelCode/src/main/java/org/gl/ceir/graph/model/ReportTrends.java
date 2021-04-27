package org.gl.ceir.graph.model;

import org.springframework.stereotype.Component;

@Component
public class ReportTrends {
	private int  typeFlag;
	private String  typeFlagInterp;
	public int getTypeFlag() {
		return typeFlag;
	}
	public void setTypeFlag(int typeFlag) {
		this.typeFlag = typeFlag;
	}
	public String getTypeFlagInterp() {
		return typeFlagInterp;
	}
	public void setTypeFlagInterp(String typeFlagInterp) {
		this.typeFlagInterp = typeFlagInterp;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ReportTrends [typeFlag=");
		builder.append(typeFlag);
		builder.append(", typeFlagInterp=");
		builder.append(typeFlagInterp);
		builder.append("]");
		return builder.toString();
	}
	
	
}
