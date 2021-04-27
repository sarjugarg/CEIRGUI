package com.gl.ceir.config.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gl.ceir.config.model.AuditTrail;
import com.gl.ceir.config.model.ConsignmentMgmt;
import com.gl.ceir.config.model.FileDetails;
import com.gl.ceir.config.model.FilterRequest;
import com.gl.ceir.config.model.SystemConfigurationDb;
import com.gl.ceir.config.service.impl.AuditTrailServiceImpl;

import io.swagger.annotations.ApiOperation;

@RestController
public class AuditTrailController {

	private static final Logger logger = LogManager.getLogger(AuditTrailController.class);

	@Autowired
	AuditTrailServiceImpl auditTrailServiceImpl;
	
	@ApiOperation(value = "pagination View filtered audit-trail", response = ConsignmentMgmt.class)
	@PostMapping("/filter/audit-trail")
	public MappingJacksonValue withPaginationConsignments(@RequestBody FilterRequest filterRequest,
			@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value = "file", defaultValue = "0") Integer file) {

		MappingJacksonValue mapping = null;
		if(file == 0) {
			logger.info("Request to view filtered audit trail = " + filterRequest);
			Page<AuditTrail> auditTrail =  auditTrailServiceImpl.filterAuditTrail(filterRequest, pageNo, pageSize);
			mapping = new MappingJacksonValue(auditTrail);
		}else {
			logger.info("Request to export filtered audit trail = " + filterRequest);
			FileDetails fileDetails = auditTrailServiceImpl.getFilteredAuditTrailInFile(filterRequest);
			mapping = new MappingJacksonValue(fileDetails);
		}

		logger.info("Response of view Request = " + mapping);

		return mapping;
	}
	
	@ApiOperation(value = "View By Id || Audit Trail", response = SystemConfigurationDb.class)
	@GetMapping("/audit-trail/{id}")
	public MappingJacksonValue findAuditTrailById(@PathVariable long id) {

		logger.info("Get audit trail by id [" + id + "]");

		AuditTrail auditTrail = auditTrailServiceImpl.findById(id);

		MappingJacksonValue mapping = new MappingJacksonValue(auditTrail);

		logger.info("Response to send= " + mapping);

		return mapping;
	}
	
}