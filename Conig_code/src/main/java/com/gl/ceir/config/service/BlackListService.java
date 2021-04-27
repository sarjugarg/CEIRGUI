package com.gl.ceir.config.service;

import com.gl.ceir.config.model.BlackList;
import com.gl.ceir.config.model.ImeiMsisdnIdentity;

public interface BlackListService extends RestServices<BlackList> {
	public BlackList getByMsisdn(Long msisdn);

	public BlackList getByImei(String imei);

	public BlackList getByMsisdnAndImei(ImeiMsisdnIdentity imeiMsisdnIdentity);

	public void deleteByMsisdnAndImei(ImeiMsisdnIdentity imeiMsisdnIdentity);
}
