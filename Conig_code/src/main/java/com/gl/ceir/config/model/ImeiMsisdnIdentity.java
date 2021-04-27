package com.gl.ceir.config.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class ImeiMsisdnIdentity implements Serializable {

	private static final long serialVersionUID = 1L;
	@NotNull
	private String imei;
	@NotNull
	private String msisdn;

	public ImeiMsisdnIdentity() {

	}

	public ImeiMsisdnIdentity(String imei, String msisdn) {
		this.imei = imei;
		this.msisdn = msisdn;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		ImeiMsisdnIdentity that = (ImeiMsisdnIdentity) o;

		if (imei == null || msisdn == null)
			return false;
		else if (imei == that.imei && msisdn == that.msisdn)
			return true;
		else
			return false;
	}

	@Override
	public int hashCode() {
		int result = imei.hashCode();
		result = 31 * result + msisdn.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return imei + "_" + msisdn;
	}
}
