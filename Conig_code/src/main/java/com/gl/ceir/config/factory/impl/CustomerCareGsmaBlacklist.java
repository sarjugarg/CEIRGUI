package com.gl.ceir.config.factory.impl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gl.ceir.config.factory.CustomerCareTarget;
import com.gl.ceir.config.model.BlacklistImeiDb;
import com.gl.ceir.config.model.CustomerCareDeviceState;
import com.gl.ceir.config.model.GsmaBlackList;
import com.gl.ceir.config.model.constants.Constants;
import com.gl.ceir.config.repository.BlacklistImeiDbRepository;
import com.gl.ceir.config.repository.GsmaBlacklistRepository;

@Component
public class CustomerCareGsmaBlacklist implements CustomerCareTarget{
	
//	@Autowired
//	public GsmaBlacklistRepository gsmaBlackListRepository;
	@Autowired
	public BlacklistImeiDbRepository blacklistImeiDbRepository;
	
	@Override
	public CustomerCareDeviceState fetchDetailsByImei(String imei, CustomerCareDeviceState customerCareDeviceState) {
		
		BlacklistImeiDb blackList = blacklistImeiDbRepository.getByDeviceidAndBlacklistStatus(imei,"Yes");
		
		if(Objects.nonNull(blackList)) {
			customerCareDeviceState.setTxnId("");
			customerCareDeviceState.setDate(blackList.getCreatedOn().toString());
			customerCareDeviceState.setStatus(Constants.available);
			customerCareDeviceState.setFeatureId(36);
		}else {
			customerCareDeviceState.setDate("");
			customerCareDeviceState.setStatus(Constants.non_available);
			customerCareDeviceState.setFeatureId(36);
		}
		customerCareDeviceState.setImei(imei);
		setName(customerCareDeviceState);
		return customerCareDeviceState;
	}
	
	@Override
	public CustomerCareDeviceState fetchDetailsByImei(String imei, CustomerCareDeviceState customerCareDeviceState, String deviceIdType ) {
		//Because table doesn't contain any deviceIdType column
		return fetchDetailsByImei( imei, customerCareDeviceState );
	}

	@Override
	public void setName(CustomerCareDeviceState customerCareDeviceState) {
		customerCareDeviceState.setName("Global Black List");
	}

}
