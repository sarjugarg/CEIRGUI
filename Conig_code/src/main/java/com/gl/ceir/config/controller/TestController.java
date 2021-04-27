package com.gl.ceir.config.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gl.ceir.config.model.GenricResponse;
import com.gl.ceir.config.service.impl.TestServiceImpl;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("test")
public class TestController {

	private static final Logger logger = LogManager.getLogger(TestController.class);

	@Autowired
	TestServiceImpl testServiceImpl;

	@ApiOperation(value = "Update Consignment Status.", response = GenricResponse.class)
	@GetMapping("update/status/{featureId}/{txnId}/{Status}")
	public GenricResponse updateStatus(@PathVariable("featureId") int featureId,
			@PathVariable("txnId") String txnId,
			@PathVariable("Status") int Status) {

		logger.info("TEST : Request to update the feature [ " + featureId + "] of txnId [" + txnId +" ] with status [" + Status + "]");

		GenricResponse genricResponse = testServiceImpl.updateStatus(featureId, txnId, Status);

		return genricResponse ;

	}

}
