package org.gl.ceir.pagination.model;

import org.springframework.stereotype.Component;

@Component
public class CurrencyContantModel {

	private Integer id;
	private String createdOn;
	private String modifiedOn;
	private String date;
	private Integer currency;
	private Double riel;
	private Double dollar;
	private String currencyInterp;
	private String month;
	private String year;
	private String monthInterp;
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
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Integer getCurrency() {
		return currency;
	}
	public void setCurrency(Integer currency) {
		this.currency = currency;
	}
	public Double getRiel() {
		return riel;
	}
	public void setRiel(Double riel) {
		this.riel = riel;
	}
	public Double getDollar() {
		return dollar;
	}
	public void setDollar(Double dollar) {
		this.dollar = dollar;
	}
	public String getCurrencyInterp() {
		return currencyInterp;
	}
	public void setCurrencyInterp(String currencyInterp) {
		this.currencyInterp = currencyInterp;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonthInterp() {
		return monthInterp;
	}
	public void setMonthInterp(String monthInterp) {
		this.monthInterp = monthInterp;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CurrencyContantModel [id=");
		builder.append(id);
		builder.append(", createdOn=");
		builder.append(createdOn);
		builder.append(", modifiedOn=");
		builder.append(modifiedOn);
		builder.append(", date=");
		builder.append(date);
		builder.append(", currency=");
		builder.append(currency);
		builder.append(", riel=");
		builder.append(riel);
		builder.append(", dollar=");
		builder.append(dollar);
		builder.append(", currencyInterp=");
		builder.append(currencyInterp);
		builder.append(", month=");
		builder.append(month);
		builder.append(", year=");
		builder.append(year);
		builder.append(", monthInterp=");
		builder.append(monthInterp);
		builder.append("]");
		return builder.toString();
	}

	

	
		
}
