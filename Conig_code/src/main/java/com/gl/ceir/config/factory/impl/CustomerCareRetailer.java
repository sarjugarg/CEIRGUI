package com.gl.ceir.config.factory.impl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gl.ceir.config.factory.CustomerCareTarget;
import com.gl.ceir.config.model.CustomerCareDeviceState;
import com.gl.ceir.config.model.DeviceRetailerDb;
import com.gl.ceir.config.model.constants.Constants;
import com.gl.ceir.config.repository.DeviceRetailerDbRepository;
import com.gl.ceir.config.util.CommonFunction;

@Component
public class CustomerCareRetailer implements CustomerCareTarget{
	
	@Autowired
	DeviceRetailerDbRepository deviceRetailerDbRepository;
	@Autowired
	CommonFunction commonFunction;
	
	@Override
	public CustomerCareDeviceState fetchDetailsByImei(String imei, CustomerCareDeviceState customerCareDeviceState) {
		
		DeviceRetailerDb deviceDb = deviceRetailerDbRepository.getByImeiEsnMeid(imei);
		
		if(Objects.nonNull(deviceDb)) {
			customerCareDeviceState.setTxnId(deviceDb.getTxnId());
			customerCareDeviceState.setDate(deviceDb.getCreatedOn().toString());
			customerCareDeviceState.setStatus(Constants.available);
			customerCareDeviceState.setFeatureId(commonFunction.getFeatureIdByTxnId(deviceDb.getTxnId()));
		}else {
			//customerCareDeviceState.setDate("");
			customerCareDeviceState.setStatus(Constants.non_available);
			//customerCareDeviceState.setFeatureId(commonFunction.getFeatureIdByTxnId(deviceDb.getTxnId()));
		}
		customerCareDeviceState.setImei(imei);
		setName(customerCareDeviceState);
		return customerCareDeviceState;
	}
	
	@Override
	public CustomerCareDeviceState fetchDetailsByImei(String imei, CustomerCareDeviceState customerCareDeviceState, String deviceIdType ) {
		
		DeviceRetailerDb deviceDb = deviceRetailerDbRepository.getByImeiEsnMeidAndDeviceIdType(imei, deviceIdType);
		
		if(Objects.nonNull(deviceDb)) {
			customerCareDeviceState.setTxnId(deviceDb.getTxnId());
			customerCareDeviceState.setDate(deviceDb.getCreatedOn().toString());
			customerCareDeviceState.setStatus(Constants.available);
			customerCareDeviceState.setFeatureId(commonFunction.getFeatureIdByTxnId(deviceDb.getTxnId()));
		}else {
			//customerCareDeviceState.setDate("");
			customerCareDeviceState.setStatus(Constants.non_available);
			//customerCareDeviceState.setFeatureId(commonFunction.getFeatureIdByTxnId(deviceDb.getTxnId()));
		}
		customerCareDeviceState.setImei(imei);
		setName(customerCareDeviceState);
		return customerCareDeviceState;
	}

	@Override
	public void setName(CustomerCareDeviceState customerCareDeviceState) {
		customerCareDeviceState.setName("Retailer");
	}

}
