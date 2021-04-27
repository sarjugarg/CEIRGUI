package com.gl.ceir.config.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gl.ceir.config.exceptions.ResourceNotFoundException;
import com.gl.ceir.config.model.ImeiMsisdnIdentity;
import com.gl.ceir.config.model.VipList;
import com.gl.ceir.config.service.VipListService;

import io.swagger.annotations.ApiOperation;

@RestController
public class VipListController {

	private static final Logger logger = LogManager.getLogger(VipListController.class);

	@Autowired
	private VipListService vipListService;

	@ApiOperation(value = "View available VIP List Device ", response = VipList.class)
	@RequestMapping(path = "/VipList/", method = RequestMethod.GET)
	public MappingJacksonValue getByMsisdnAndImei(@RequestParam(required = false) String msisdn,
			@RequestParam(required = false) String imei) {
		ImeiMsisdnIdentity imeiMsisdnIdentity = new ImeiMsisdnIdentity();
		imeiMsisdnIdentity.setMsisdn(msisdn);
		imeiMsisdnIdentity.setImei(imei);
		return getByMsisdnAndImei(imeiMsisdnIdentity);
	}

	public MappingJacksonValue getByMsisdnAndImei(@RequestBody ImeiMsisdnIdentity imeiMsisdnIdentity) {

		if (imeiMsisdnIdentity.getMsisdn() == null && imeiMsisdnIdentity.getImei() == null) {
			List<VipList> allWhiteList = vipListService.getAll();
			MappingJacksonValue mapping = new MappingJacksonValue(allWhiteList);
			return mapping;
		}

		VipList vipList = vipListService.getByMsisdnAndImei(imeiMsisdnIdentity);
		if (vipList == null)
			throw new ResourceNotFoundException("VIP List", "msisdn and imei",
					imeiMsisdnIdentity.getMsisdn() + " and " + imeiMsisdnIdentity.getImei());
		else
			return new MappingJacksonValue(vipList);
	}

	@ApiOperation(value = "Save new Device in VIP List ", response = VipList.class)
	@RequestMapping(path = "/VipList/", method = RequestMethod.POST)
	public MappingJacksonValue save(@RequestBody VipList vipList) {
		VipList savedVipList = vipListService.save(vipList);
		MappingJacksonValue mapping = new MappingJacksonValue(savedVipList);
		return mapping;
	}

	@ApiOperation(value = "Delete a Device from VIP List ")
	@RequestMapping(path = "/VipList/", method = RequestMethod.DELETE)
	public void deleteByMsisdnAndImei(@RequestBody ImeiMsisdnIdentity imeiMsisdnIdentity) {
		vipListService.deleteByMsisdnAndImei(imeiMsisdnIdentity);
	}

}
