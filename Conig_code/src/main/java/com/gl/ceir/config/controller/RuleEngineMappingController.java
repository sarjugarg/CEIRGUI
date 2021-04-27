package com.gl.ceir.config.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gl.ceir.config.model.AuditTrail;
import com.gl.ceir.config.model.FileDetails;
import com.gl.ceir.config.model.FilterRequest;
import com.gl.ceir.config.model.GenricResponse;
import com.gl.ceir.config.model.RuleEngine;
import com.gl.ceir.config.model.RuleEngineMapping;
import com.gl.ceir.config.model.constants.Features;
import com.gl.ceir.config.model.constants.SubFeatures;
import com.gl.ceir.config.repository.AuditTrailRepository;
import com.gl.ceir.config.service.impl.RuleEngineMappingServiceImpl;

import io.swagger.annotations.ApiOperation;

@RestController
public class RuleEngineMappingController {

	private static final Logger logger = LogManager.getLogger(RuleEngineMappingController.class);

	@Autowired
	RuleEngineMappingServiceImpl ruleEngineMappingServiceImpl;

	@Autowired
	AuditTrailRepository auditTrailRepository;
	
	@ApiOperation(value = "pagination View filtered rule-engine-mapping", response = RuleEngine.class)
	@PostMapping("/filter/rule-engine-mapping")
	public MappingJacksonValue getFilteredData(@RequestBody FilterRequest filterRequest,
			@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value = "file", defaultValue = "0") Integer file) {

		MappingJacksonValue mapping = null;
	if(file==0) {
		logger.info("Request to view filtered rule engine mapping = " + filterRequest);
		Page<RuleEngineMapping> ruleEngineMapping =  ruleEngineMappingServiceImpl.filterRuleEngineMapping(filterRequest, pageNo, pageSize,"view");
		mapping = new MappingJacksonValue(ruleEngineMapping);

		logger.info("Response of view Request = " + mapping);
	}
		else {
			FileDetails fileDetails = ruleEngineMappingServiceImpl.getFile(filterRequest);
			mapping = new MappingJacksonValue(fileDetails);

			
		}
		
		return mapping;
	}

	@ApiOperation(value = "View By Id || rule-engine-mapping", response = MappingJacksonValue.class)
	@GetMapping("/rule-engine-mapping/{id}")
	public MappingJacksonValue findAuditTrailById(@PathVariable long id) {

		logger.info("Get rule engine mapping by id [" + id + "]");

		RuleEngineMapping ruleEngineMapping = ruleEngineMappingServiceImpl.findById(id);

		MappingJacksonValue mapping = new MappingJacksonValue(ruleEngineMapping);

		logger.info("Response to send= " + mapping);

		return mapping;
	}
	
	@ApiOperation(value = "Save || rule-engine-mapping", response = MappingJacksonValue.class)
	@PostMapping("/rule-engine-mapping")
	public GenricResponse save(@RequestBody RuleEngineMapping ruleEngineMapping) {


		logger.info("Save rule engine mapping [" + ruleEngineMapping + "]");

		GenricResponse genricResponse = ruleEngineMappingServiceImpl.save(ruleEngineMapping);
//		MappingJacksonValue mapping = new MappingJacksonValue(ruleEngineMapping);
		
		if(genricResponse.getErrorCode() == 409) {
			return genricResponse;
		}
		else if(genricResponse.getErrorCode() == 0) {
			auditTrailRepository.save( new AuditTrail( ruleEngineMapping.getId(),
					ruleEngineMapping.getUserName(), Long.valueOf(ruleEngineMapping.getUserTypeId()),
			  "CEIRSystem", Long.valueOf(ruleEngineMapping.getFeatureId()),
			  Features.RULE_FEATURE_MAPPING, SubFeatures.REGISTER, "","NA",
			  ruleEngineMapping.getRoleType(),ruleEngineMapping.getPublicIp(),ruleEngineMapping.getBrowser()));
			
		}
		return genricResponse;
		
	
	}
	
	@ApiOperation(value = "Update By Id || rule-engine-mapping", response = MappingJacksonValue.class)
	@PutMapping("/rule-engine-mapping")
	public MappingJacksonValue updateById(@RequestBody RuleEngineMapping ruleEngineMapping) {

		logger.info("Update rule engine mapping [" + ruleEngineMapping + "]");

		GenricResponse genricResponse = ruleEngineMappingServiceImpl.updateById(ruleEngineMapping);
		
		if(genricResponse.getErrorCode() == 0) {
			auditTrailRepository.save( new AuditTrail(ruleEngineMapping.getId(),
					ruleEngineMapping.getUserName(), Long.valueOf(ruleEngineMapping.getUserTypeId()),
			  "CEIRSystem", Long.valueOf(ruleEngineMapping.getFeatureId()),
			  Features.RULE_FEATURE_MAPPING, SubFeatures.UPDATE, "","NA",
			  ruleEngineMapping.getRoleType(),ruleEngineMapping.getPublicIp(),ruleEngineMapping.getBrowser()));
		}
		
		MappingJacksonValue mapping = new MappingJacksonValue(ruleEngineMapping);

		logger.info("Response to send= " + mapping);

		return mapping;
	}
	
	@ApiOperation(value = "Delete By Id || rule-engine-mapping", response = MappingJacksonValue.class)
	@DeleteMapping("/rule-engine-mapping")
	public MappingJacksonValue deleteById(@RequestBody RuleEngineMapping ruleEngineMapping) {

		logger.info("Delete rule engine mapping [" + ruleEngineMapping + "]");

		GenricResponse genricResponse = ruleEngineMappingServiceImpl.deleteById(ruleEngineMapping);

		MappingJacksonValue mapping = new MappingJacksonValue(ruleEngineMapping);
		
	if(genricResponse.getErrorCode() == 0) {
			auditTrailRepository.save( new AuditTrail( ruleEngineMapping.getId(),
					ruleEngineMapping.getUserName(), Long.valueOf(ruleEngineMapping.getUserTypeId()),
			  "CEIRSystem", Long.valueOf(ruleEngineMapping.getFeatureId()),
			  Features.RULE_FEATURE_MAPPING, SubFeatures.DELETE, "","NA",
			  ruleEngineMapping.getRoleType(),ruleEngineMapping.getPublicIp(),ruleEngineMapping.getBrowser()));
		}
		
		logger.info("Response to send = " + mapping);

		return mapping;
	}
}