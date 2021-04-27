package com.gl.ceir.config.factory.impl;

import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gl.ceir.config.controller.CustomerCareController;
import com.gl.ceir.config.factory.CustomerCareTarget;
import com.gl.ceir.config.model.CustomerCareDeviceState;
import com.gl.ceir.config.model.RegularizeDeviceDb;
import com.gl.ceir.config.model.constants.Constants;
import com.gl.ceir.config.model.constants.Tags;
import com.gl.ceir.config.repository.RegularizedDeviceDbRepository;
import com.gl.ceir.config.repository.SystemConfigListRepository;

@Component
public class CustomerCareRegularizeDevice implements CustomerCareTarget{

	private static final Logger logger = LogManager.getLogger(CustomerCareController.class);
	
	@Autowired
	RegularizedDeviceDbRepository regularizedDeviceDbRepository;
	
	@Autowired
	SystemConfigListRepository systemConfigListRepository;

	@Override
	public CustomerCareDeviceState fetchDetailsByImei(String imei, CustomerCareDeviceState customerCareDeviceState) {

		RegularizeDeviceDb deviceDb = regularizedDeviceDbRepository.getByImei(imei);

		if(Objects.nonNull(deviceDb)) {
			customerCareDeviceState.setTxnId(deviceDb.getTxnId());
			customerCareDeviceState.setDate(deviceDb.getCreatedOn().toString());
			customerCareDeviceState.setStatus(Constants.available);
			customerCareDeviceState.setFeatureId(12);
		}
		else {
			customerCareDeviceState.setDate("");
			customerCareDeviceState.setStatus(Constants.non_available); 
			customerCareDeviceState.setFeatureId(12);
		}
		
		customerCareDeviceState.setImei(imei);
		setName(customerCareDeviceState);

		return customerCareDeviceState;
	}
	
	@Override
	public CustomerCareDeviceState fetchDetailsByImei(String imei, CustomerCareDeviceState customerCareDeviceState, String deviceIdTypeInterp ) {
		logger.info("TAG:["+Tags.DEVICE_ID_TYPE+"] and Interp:["+deviceIdTypeInterp+"]");
		Integer deviceIdType = Integer.valueOf(systemConfigListRepository.findByTagAndInterpIgnoreCase(Tags.DEVICE_ID_TYPE, deviceIdTypeInterp).getValue());
		
		RegularizeDeviceDb deviceDb = regularizedDeviceDbRepository.getByImeiAndDeviceIdType(imei, deviceIdType);

		if(Objects.nonNull(deviceDb)) {
			customerCareDeviceState.setTxnId(deviceDb.getTxnId());
			customerCareDeviceState.setDate(deviceDb.getCreatedOn().toString());
			customerCareDeviceState.setStatus(Constants.available);
			customerCareDeviceState.setFeatureId(12);
		}
		else {
			customerCareDeviceState.setDate("");
			customerCareDeviceState.setStatus(Constants.non_available); 
			customerCareDeviceState.setFeatureId(12);
		}
		
		customerCareDeviceState.setImei(imei);
		setName(customerCareDeviceState);

		return customerCareDeviceState;
	}

	@Override
	public void setName(CustomerCareDeviceState customerCareDeviceState) {
		customerCareDeviceState.setName("Regularize device");
	}

}
