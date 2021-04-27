package org.gl.ceir.Class.HeadersTitle;

import java.util.List;

import org.springframework.stereotype.Component;
@Component
public class DatatableResponseModel {
	private Integer recordsTotal;
	//Integer recordsTotal = new Integer(10)     int recordsTotal;  int x= recordsTotal.intValue();
	private Integer recordsFiltered;
	private List<List<Object>> data = null;
	public Integer getRecordsTotal() {
		return recordsTotal;
	}
	public void setRecordsTotal(Integer recordsTotal) {
		this.recordsTotal = recordsTotal;
	}
	public Integer getRecordsFiltered() {
		return recordsFiltered;
	}
	public void setRecordsFiltered(Integer recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}
	public List<List<Object>> getData() {
		return data;
	}
	public void setData(List<List<Object>> data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "DatatableResponseModel [recordsTotal=" + recordsTotal + ", recordsFiltered=" + recordsFiltered
				+ ", data=" + data + "]";
	}
	}
