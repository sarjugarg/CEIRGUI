package com.gl.ceir.config.factory.impl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gl.ceir.config.factory.CustomerCareTarget;
import com.gl.ceir.config.model.CustomerCareDeviceState;
import com.gl.ceir.config.model.GreylistDb;
import com.gl.ceir.config.model.constants.Constants;
import com.gl.ceir.config.repository.GreyListRepository;

@Component
public class CustomerCareGreylist implements CustomerCareTarget{
	
	@Autowired
	GreyListRepository greyListRepository;
	
	@Override
	public CustomerCareDeviceState fetchDetailsByImei(String imei, CustomerCareDeviceState customerCareDeviceState) {
		
		GreylistDb greylistDb = greyListRepository.findByImei(imei);
		
		if(Objects.nonNull(greylistDb)) {
			customerCareDeviceState.setTxnId("");
			customerCareDeviceState.setDate(greylistDb.getCreatedOn().toString());
			customerCareDeviceState.setStatus(Constants.available);
			customerCareDeviceState.setFeatureId(9);
		}else {
			customerCareDeviceState.setDate("");
			customerCareDeviceState.setStatus(Constants.non_available);
			customerCareDeviceState.setFeatureId(9);
		}
		customerCareDeviceState.setImei(imei);
		setName(customerCareDeviceState);
		return customerCareDeviceState;
	}
	
	@Override
	public CustomerCareDeviceState fetchDetailsByImei(String imei, CustomerCareDeviceState customerCareDeviceState, String deviceIdType) {
		
		GreylistDb greylistDb = greyListRepository.findByImeiAndDeviceIdType(imei, deviceIdType);
		
		if(Objects.nonNull(greylistDb)) {
			customerCareDeviceState.setTxnId("");
			customerCareDeviceState.setDate(greylistDb.getCreatedOn().toString());
			customerCareDeviceState.setStatus(Constants.available);
			customerCareDeviceState.setFeatureId(9);
		}else {
			customerCareDeviceState.setDate("");
			customerCareDeviceState.setStatus(Constants.non_available);
			customerCareDeviceState.setFeatureId(9);
		}
		customerCareDeviceState.setImei(imei);
		setName(customerCareDeviceState);
		return customerCareDeviceState;
	}

	@Override
	public void setName(CustomerCareDeviceState customerCareDeviceState) {
		customerCareDeviceState.setName("Greylist");
	}

}
