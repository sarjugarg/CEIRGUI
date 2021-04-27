package com.gl.ceir.config.factory.impl;

import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gl.ceir.config.factory.CustomerCareTarget;
import com.gl.ceir.config.model.CustomerCareDeviceState;
import com.gl.ceir.config.model.DeviceManufacturerDb;
import com.gl.ceir.config.model.constants.Constants;
import com.gl.ceir.config.repository.DeviceManufacturerDbRepository;
import com.gl.ceir.config.util.CommonFunction;

@Component
public class CustomerCareManufacturer implements CustomerCareTarget{
	
	private static final Logger logger = LogManager.getLogger(CustomerCareManufacturer.class);
	
	@Autowired
	DeviceManufacturerDbRepository deviceManufacturerDbRepository;
	@Autowired
	CommonFunction commonFunction;
	
	@Override
	public CustomerCareDeviceState fetchDetailsByImei(String imei, CustomerCareDeviceState customerCareDeviceState) {
		
		DeviceManufacturerDb deviceDb = deviceManufacturerDbRepository.findByImeiEsnMeid(imei);
		
		if(Objects.nonNull(deviceDb)) {
			customerCareDeviceState.setTxnId(deviceDb.getTxnId());
			customerCareDeviceState.setDate(deviceDb.getCreatedOn().toString());
			customerCareDeviceState.setStatus(Constants.available);
			customerCareDeviceState.setFeatureId(0);
			customerCareDeviceState.setFeatureId(commonFunction.getFeatureIdByTxnId(deviceDb.getTxnId()));
		}else {
			//customerCareDeviceState.setDate("");
			customerCareDeviceState.setStatus(Constants.non_available);
			//customerCareDeviceState.setFeatureId(commonFunction.getFeatureIdByTxnId(deviceDb.getTxnId()));
		}
		customerCareDeviceState.setImei(imei);
		setName(customerCareDeviceState);
//		logger.info("CustomerCareDeviceState:"+customerCareDeviceState.toString());
		return customerCareDeviceState;
	}

	@Override
	public CustomerCareDeviceState fetchDetailsByImei(String imei, CustomerCareDeviceState customerCareDeviceState, String deviceIdType) {
		
		DeviceManufacturerDb deviceDb = deviceManufacturerDbRepository.findByImeiEsnMeidAndDeviceIdType(imei, deviceIdType);
		
		if(Objects.nonNull(deviceDb)) {
			customerCareDeviceState.setTxnId(deviceDb.getTxnId());
			customerCareDeviceState.setDate(deviceDb.getCreatedOn().toString());
			customerCareDeviceState.setStatus(Constants.available);
			customerCareDeviceState.setFeatureId(0);
			customerCareDeviceState.setFeatureId(commonFunction.getFeatureIdByTxnId(deviceDb.getTxnId()));
		}else {
			//customerCareDeviceState.setDate("");
			customerCareDeviceState.setStatus(Constants.non_available);
			//customerCareDeviceState.setFeatureId(commonFunction.getFeatureIdByTxnId(deviceDb.getTxnId()));
		}
		customerCareDeviceState.setImei(imei);
		setName(customerCareDeviceState);
//		logger.info("CustomerCareDeviceState:"+customerCareDeviceState.toString());
		return customerCareDeviceState;
	}
	
	@Override
	public void setName(CustomerCareDeviceState customerCareDeviceState) {
		customerCareDeviceState.setName("Manufacturer");
	}

}
