package com.gl.ceir.config.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gl.ceir.config.exceptions.ResourceNotFoundException;
import com.gl.ceir.config.model.BlackList;
import com.gl.ceir.config.model.ImeiMsisdnIdentity;
import com.gl.ceir.config.service.BlackListService;

import io.swagger.annotations.ApiOperation;

@RestController
public class BlackListController {

	private static final Logger logger = LogManager.getLogger(BlackListController.class);

	@Autowired
	private BlackListService blackListService;

	@ApiOperation(value = "View available Black List Device ", response = BlackList.class)
	@GetMapping("/BlackList/")
	public MappingJacksonValue getByMsisdnAndImei(@RequestParam(required = false) String msisdn,
			@RequestParam(required = false) String imei) {
		ImeiMsisdnIdentity imeiMsisdnIdentity = new ImeiMsisdnIdentity();
		imeiMsisdnIdentity.setMsisdn(msisdn);
		imeiMsisdnIdentity.setImei(imei);
		return getByMsisdnAndImei(imeiMsisdnIdentity);
	}

	public MappingJacksonValue getByMsisdnAndImei(@RequestBody ImeiMsisdnIdentity imeiMsisdnIdentity) {
		logger.info("get BlackList Method Calling " + imeiMsisdnIdentity);

		if (imeiMsisdnIdentity == null
				|| (imeiMsisdnIdentity.getMsisdn() == null && imeiMsisdnIdentity.getImei() == null)) {
			logger.info("get BlackList Method Calling for All Devices");
			List<BlackList> allWhiteList = blackListService.getAll();
			MappingJacksonValue mapping = new MappingJacksonValue(allWhiteList);
			return mapping;
		}

		BlackList blackList = blackListService.getByMsisdnAndImei(imeiMsisdnIdentity);
		if (blackList == null)
			throw new ResourceNotFoundException("BLACK List", "msisdn and imei",
					imeiMsisdnIdentity.getMsisdn() + " and " + imeiMsisdnIdentity.getImei());
		else
			return new MappingJacksonValue(blackList);
	}

	@ApiOperation(value = "Save new Device in Black List ", response = BlackList.class)
	@PostMapping("/BlackList/")
	public MappingJacksonValue save(@RequestBody BlackList blackList) {
		logger.info("Post Logger BlackList Method Calling ");
		BlackList savedWhiteList = blackListService.save(blackList);
		MappingJacksonValue mapping = new MappingJacksonValue(savedWhiteList);
		return mapping;
	}

	@ApiOperation(value = "Delete a Device from Black List ")
	@DeleteMapping(path = "/BlackList/")
	public void deleteByMsisdnAndImei(@RequestBody ImeiMsisdnIdentity imeiMsisdnIdentity) {
		blackListService.deleteByMsisdnAndImei(imeiMsisdnIdentity);
	}
}
