package com.gl.ceir.config.factory.impl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gl.ceir.config.factory.CustomerCareTarget;
import com.gl.ceir.config.model.CustomerCareDeviceState;
import com.gl.ceir.config.model.TypeApprovedDb;
import com.gl.ceir.config.model.constants.Constants;
import com.gl.ceir.config.repository.TypeApproveRepository;

@Component
public class CustomerCareTypeApprove implements CustomerCareTarget{
	
	@Autowired
	TypeApproveRepository typeApproveRepository;
	
	@Override
	public CustomerCareDeviceState fetchDetailsByImei(String imei, CustomerCareDeviceState customerCareDeviceState) {
		
		TypeApprovedDb typeApprovedDb = typeApproveRepository.getByTac(imei.substring(0, 8));
		
		if(Objects.nonNull(typeApprovedDb)) {
			customerCareDeviceState.setTxnId("");
			customerCareDeviceState.setDate(typeApprovedDb.getCreatedOn().toString());
			customerCareDeviceState.setStatus(Constants.available);
			customerCareDeviceState.setFeatureId(21);
		}else {
			customerCareDeviceState.setDate("");
			customerCareDeviceState.setStatus(Constants.non_available);
			customerCareDeviceState.setFeatureId(21);
		}
		customerCareDeviceState.setImei(imei);
		setName(customerCareDeviceState);
		return customerCareDeviceState;
	}
	
	@Override
	public CustomerCareDeviceState fetchDetailsByImei(String imei, CustomerCareDeviceState customerCareDeviceState, String deviceIdType) {
		
		return fetchDetailsByImei(imei, customerCareDeviceState);
	}

	@Override
	public void setName(CustomerCareDeviceState customerCareDeviceState) {
		customerCareDeviceState.setName("Type Approve");
	}
}