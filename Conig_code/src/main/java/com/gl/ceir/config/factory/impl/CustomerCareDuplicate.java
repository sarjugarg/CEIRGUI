package com.gl.ceir.config.factory.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gl.ceir.config.factory.CustomerCareTarget;
import com.gl.ceir.config.model.CustomerCareDeviceState;
import com.gl.ceir.config.model.DeviceDuplicateDb;
import com.gl.ceir.config.model.constants.Constants;
import com.gl.ceir.config.repository.DeviceDuplicateDbRepository;
import com.gl.ceir.config.util.CommonFunction;

@Component
public class CustomerCareDuplicate implements CustomerCareTarget{
	
	@Autowired
	DeviceDuplicateDbRepository deviceDuplicateDbRepository;
	@Autowired
	CommonFunction commonFunction;
	
	@Override
	public CustomerCareDeviceState fetchDetailsByImei(String imei, CustomerCareDeviceState customerCareDeviceState) {
		
//		List<DeviceDuplicateDb> deviceDbList = deviceDuplicateDbRepository.findByImeiMsisdnIdentityImei(imei);
		List<DeviceDuplicateDb> deviceDbList = deviceDuplicateDbRepository.findByImei(imei);
		
		if(!deviceDbList.isEmpty()) {
			customerCareDeviceState.setTxnId("");
			customerCareDeviceState.setDate(deviceDbList.get(0).getCreatedOn().toString());
			customerCareDeviceState.setStatus(Constants.available);
			customerCareDeviceState.setFeatureId(34);
		}else {
			customerCareDeviceState.setDate("");
			customerCareDeviceState.setStatus(Constants.non_available);
			customerCareDeviceState.setFeatureId(34);
		}
		customerCareDeviceState.setImei(imei);
		setName(customerCareDeviceState);
		return customerCareDeviceState;
	}
	
	@Override
	public CustomerCareDeviceState fetchDetailsByImei(String imei, CustomerCareDeviceState customerCareDeviceState, String deviceIdType ) {
		
		return fetchDetailsByImei(imei, customerCareDeviceState);
	}

	@Override
	public void setName(CustomerCareDeviceState customerCareDeviceState) {
		customerCareDeviceState.setName("Duplicate");
	}

}
