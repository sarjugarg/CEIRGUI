package com.gl.ceir.config.factory.impl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gl.ceir.config.factory.CustomerCareTarget;
import com.gl.ceir.config.model.BlackList;
import com.gl.ceir.config.model.CustomerCareDeviceState;
import com.gl.ceir.config.model.constants.Constants;
import com.gl.ceir.config.repository.BlackListRepository;

@Component
public class CustomerCareBlacklist implements CustomerCareTarget{
	
	@Autowired
	BlackListRepository blackListRepository;
	
	@Override
	public CustomerCareDeviceState fetchDetailsByImei(String imei, CustomerCareDeviceState customerCareDeviceState) {
		
		BlackList blackList = blackListRepository.findByImei(imei);
		
		if(Objects.nonNull(blackList)) {
			customerCareDeviceState.setTxnId("");
			customerCareDeviceState.setDate(blackList.getCreatedOn().toString());
			customerCareDeviceState.setStatus(Constants.available);
			customerCareDeviceState.setFeatureId(10);
		}else {
			customerCareDeviceState.setDate("");
			customerCareDeviceState.setStatus(Constants.non_available);
			customerCareDeviceState.setFeatureId(10);
		}
		
		customerCareDeviceState.setImei(imei);
		setName(customerCareDeviceState);
		return customerCareDeviceState;
	}
	
	@Override
	public CustomerCareDeviceState fetchDetailsByImei(String imei, CustomerCareDeviceState customerCareDeviceState, String deviceIdType ) {
		
		BlackList blackList = blackListRepository.findByImeiAndDeviceIdType(imei, deviceIdType);
		
		if(Objects.nonNull(blackList)) {
			customerCareDeviceState.setTxnId("");
			customerCareDeviceState.setDate(blackList.getCreatedOn().toString());
			customerCareDeviceState.setStatus(Constants.available);
			customerCareDeviceState.setFeatureId(10);
		}else {
			customerCareDeviceState.setDate("");
			customerCareDeviceState.setStatus(Constants.non_available);
			customerCareDeviceState.setFeatureId(10);
		}
		
		customerCareDeviceState.setImei(imei);
		setName(customerCareDeviceState);
		return customerCareDeviceState;
	}

	@Override
	public void setName(CustomerCareDeviceState customerCareDeviceState) {
		customerCareDeviceState.setName("Blacklist");
	}

}
