package org.gl.ceir.graph.model;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
@Component
public class OperatorWiseGraphRowData {
	
	/*
	 * @JsonProperty("QB")
	 * 
	 * @SerializedName("QB") private String qb;
	 */
	
	@JsonProperty("SEATEL")
	@SerializedName("SEATEL")
	private String seatel;
	
	@JsonProperty("SMART")
	@SerializedName("SMART")
	private String smart;
	
	@JsonProperty("CELLCARD")
	@SerializedName("CELLCARD")
	private String cellcard;
	
	@JsonProperty("METFONE")
	@SerializedName("METFONE")
	private String metfone;
	
	@JsonProperty("Date")
	@SerializedName("Date")
	private String date;

	/*
	 * public String getQb() { return qb; }
	 * 
	 * public void setQb(String qb) { this.qb = qb; }
	 */

	public String getSeatel() {
		return seatel;
	}

	public void setSeatel(String seatel) {
		this.seatel = seatel;
	}

	public String getSmart() {
		return smart;
	}

	public void setSmart(String smart) {
		this.smart = smart;
	}

	public String getCellcard() {
		return cellcard;
	}

	public void setCellcard(String cellcard) {
		this.cellcard = cellcard;
	}

	public String getMetfone() {
		return metfone;
	}

	public void setMetfone(String metfone) {
		this.metfone = metfone;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OperatorWiseGraphRowData [seatel=");
		builder.append(seatel);
		builder.append(", smart=");
		builder.append(smart);
		builder.append(", cellcard=");
		builder.append(cellcard);
		builder.append(", metfone=");
		builder.append(metfone);
		builder.append(", date=");
		builder.append(date);
		builder.append("]");
		return builder.toString();
	}

	
}
