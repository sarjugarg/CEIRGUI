package org.gl.ceir.graph.model;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
@Component
public class BlockIMEIRowData {

	
	@JsonProperty("Date")
	@SerializedName("Date")
	private String date;
	
	
	@JsonProperty("SMART")
	@SerializedName("SMART")
	private int smart;
	
	@JsonProperty("SEATEL")
	@SerializedName("SEATEL")
	private int seatel;
	
	@JsonProperty("CELLCARD")
	@SerializedName("CELLCARD")
	private int cellcard;

	
	@JsonProperty("METFONE")
	@SerializedName("METFONE")
	private int metfone;
	
	@JsonProperty("QB")
	@SerializedName("QB")
	private int qb;


	
	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public int getSmart() {
		return smart;
	}

	public void setSmart(int smart) {
		this.smart = smart;
	}

	public int getSeatel() {
		return seatel;
	}

	public void setSeatel(int seatel) {
		this.seatel = seatel;
	}

	public int getCellcard() {
		return cellcard;
	}

	public void setCellcard(int cellcard) {
		this.cellcard = cellcard;
	}

	public int getMetfone() {
		return metfone;
	}

	public void setMetfone(int metfone) {
		this.metfone = metfone;
	}

	public int getQb() {
		return qb;
	}

	public void setQb(int qb) {
		this.qb = qb;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BlockIMEIRowData [date=");
		builder.append(date);
		builder.append(", smart=");
		builder.append(smart);
		builder.append(", seatel=");
		builder.append(seatel);
		builder.append(", cellcard=");
		builder.append(cellcard);
		builder.append(", metfone=");
		builder.append(metfone);
		builder.append(", qb=");
		builder.append(qb);
		builder.append("]");
		return builder.toString();
	}
	
	
	
}
