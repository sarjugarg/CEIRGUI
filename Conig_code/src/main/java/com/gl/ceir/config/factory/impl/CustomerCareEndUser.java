package com.gl.ceir.config.factory.impl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gl.ceir.config.factory.CustomerCareTarget;
import com.gl.ceir.config.model.CustomerCareDeviceState;
import com.gl.ceir.config.model.DeviceEndUserDb;
import com.gl.ceir.config.model.constants.Constants;
import com.gl.ceir.config.repository.DeviceEndUserDbRepository;
import com.gl.ceir.config.util.CommonFunction;

@Component
public class CustomerCareEndUser implements CustomerCareTarget{

	@Autowired
	DeviceEndUserDbRepository deviceEndUserDbRepository;
	
	@Autowired
	CommonFunction commonFunction;
	
	@Override
	public CustomerCareDeviceState fetchDetailsByImei(String imei, CustomerCareDeviceState customerCareDeviceState) {
		
		DeviceEndUserDb deviceDb = deviceEndUserDbRepository.getByImeiEsnMeid(imei);
		
		if(Objects.nonNull(deviceDb)) {
			customerCareDeviceState.setTxnId(deviceDb.getTxnId());
			customerCareDeviceState.setDate(deviceDb.getCreatedOn().toString());
			customerCareDeviceState.setStatus(Constants.available);
			customerCareDeviceState.setFeatureId(commonFunction.getFeatureIdByTxnId(deviceDb.getTxnId()));
		}else {
			customerCareDeviceState.setStatus(Constants.non_available);
		}
		
		customerCareDeviceState.setImei(imei);
		setName(customerCareDeviceState);
		return customerCareDeviceState;
	}
	
	@Override
	public CustomerCareDeviceState fetchDetailsByImei(String imei, CustomerCareDeviceState customerCareDeviceState, String deviceIdType) {
		
		DeviceEndUserDb deviceDb = deviceEndUserDbRepository.getByImeiEsnMeidAndDeviceIdType(imei, deviceIdType);
		
		if(Objects.nonNull(deviceDb)) {
			customerCareDeviceState.setTxnId(deviceDb.getTxnId());
			customerCareDeviceState.setDate(deviceDb.getCreatedOn().toString());
			customerCareDeviceState.setStatus(Constants.available);
			customerCareDeviceState.setFeatureId(commonFunction.getFeatureIdByTxnId(deviceDb.getTxnId()));
		}else {
			customerCareDeviceState.setStatus(Constants.non_available);
		}
		
		customerCareDeviceState.setImei(imei);
		setName(customerCareDeviceState);
		return customerCareDeviceState;
	}
	
	@Override
	public void setName(CustomerCareDeviceState customerCareDeviceState) {
		customerCareDeviceState.setName("End User");
	}
}
