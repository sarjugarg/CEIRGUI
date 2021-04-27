package com.gl.ceir.config.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gl.ceir.config.model.FilterRequest;
import com.gl.ceir.config.model.StateMgmtDb;
import com.gl.ceir.config.model.SystemConfigListDb;
import com.gl.ceir.config.service.impl.TagsMappingServiceImpl;

import io.swagger.annotations.ApiOperation;

@RestController
public class TagsMappingController {

	private static final Logger logger = LogManager.getLogger(TagsMappingController.class);

	@Autowired
	TagsMappingServiceImpl tagsMappingServiceImpl;
	
	@ApiOperation(value = "View list of state by feature_id and user_type_id", response = StateMgmtDb.class)
	@PostMapping("/get/tags-mapping")
	public MappingJacksonValue getByFeatureIdAndUserTypeId(@RequestBody FilterRequest filterRequest) {

		logger.info("REQUEST :  to get tags mapping = " + filterRequest );

		List<SystemConfigListDb> systemConfigListDbs = tagsMappingServiceImpl.getByFilter(filterRequest);

		MappingJacksonValue mapping = new MappingJacksonValue(systemConfigListDbs);

		logger.info("RESPONSE : tags-mapping on filter of mapping = " + systemConfigListDbs);

		return mapping;
	}

}
