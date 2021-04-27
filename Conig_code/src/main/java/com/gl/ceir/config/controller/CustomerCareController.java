package com.gl.ceir.config.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gl.ceir.config.model.CustomerCareDeviceState;
import com.gl.ceir.config.model.CustomerCareRequest;
import com.gl.ceir.config.model.FilterRequest;
import com.gl.ceir.config.model.GenricResponse;
import com.gl.ceir.config.model.Notification;
import com.gl.ceir.config.model.PolicyBreachNotification;
import com.gl.ceir.config.service.impl.CustomerCareServiceImpl;
import com.gl.ceir.config.util.Utility;

import io.swagger.annotations.ApiOperation;

@RestController
public class CustomerCareController {

	private static final Logger logger = LogManager.getLogger(CustomerCareController.class);

	@Autowired
	CustomerCareServiceImpl customerCareServiceImpl;
	
	@Autowired
	Utility utility;

	@ApiOperation(value = "View all DB's state for the imei or msisdn.", response = GenricResponse.class)
	@PostMapping("/customer-care/record")
	public MappingJacksonValue getByImporterId(@RequestBody CustomerCareRequest customerCareRequest,
			@RequestParam(name = "listType", defaultValue = "device") String listType) {

		logger.info(String.format("Request TO view TO all record of user = %s", customerCareRequest));

		GenricResponse response = customerCareServiceImpl.getAll(customerCareRequest, listType);
		MappingJacksonValue mapping = new MappingJacksonValue(response);
		logger.info("Response of view Request = " + mapping);

		return mapping;
	}
	
	@ApiOperation(value = "View Api", response = GenricResponse.class)
	@PostMapping("/customer-care/by-txn-id")
	public MappingJacksonValue getByTxnId(@RequestBody CustomerCareDeviceState customerCareDeviceState) {

		logger.info(String.format("Request TO view TO all record of user = %s", customerCareDeviceState));

		GenricResponse response = customerCareServiceImpl.getByTxnId(customerCareDeviceState);
		MappingJacksonValue mapping = new MappingJacksonValue(response);
		logger.info("Response of view Request = " + mapping);

		return mapping;
	}
	
	@ApiOperation(value = "View Record of policy-breach-notification by imei or msisdn.", response = Notification.class)
	@RequestMapping(path = "/policy-breach-notification", method = RequestMethod.POST)
	public MappingJacksonValue viewNotification(@RequestBody FilterRequest filterRequest,
			@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

		logger.info("Request to view Notification Page No = "+pageNo+" Pagesize = " + pageNo);

		Page<PolicyBreachNotification> policyBreachNotifications = customerCareServiceImpl.viewPolicyBreachNotification(filterRequest, pageNo, pageSize);

		logger.info("Notification history Response= " + policyBreachNotifications);
		MappingJacksonValue mapping = new MappingJacksonValue(policyBreachNotifications);
		return mapping;
	}
	
}