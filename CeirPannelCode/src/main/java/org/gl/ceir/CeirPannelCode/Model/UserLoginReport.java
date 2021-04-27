package org.gl.ceir.CeirPannelCode.Model;

import java.util.Arrays;

public class UserLoginReport {

	private String createdOn;
	private Integer noUserLogged;  
	private Integer uniqueUserLogged;
	private String[] labels;
	public String[] getLabels() {
		return labels;
	}
	public void setLabels(String[] labels) {
		this.labels = labels;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public Integer getNoUserLogged() {
		return noUserLogged;
	}
	public void setNoUserLogged(Integer noUserLogged) {
		this.noUserLogged = noUserLogged;
	}
	public Integer getUniqueUserLogged() {
		return uniqueUserLogged;
	}
	public void setUniqueUserLogged(Integer uniqueUserLogged) {
		this.uniqueUserLogged = uniqueUserLogged;
	}
	@Override
	public String toString() {
		return "UserLoginReport [createdOn=" + createdOn + ", noUserLogged=" + noUserLogged + ", uniqueUserLogged="
				+ uniqueUserLogged + ", labels=" + Arrays.toString(labels) + "]";
	}  
}
