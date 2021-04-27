package com.gl.ceir.config.controller;

import java.util.List;

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

import com.gl.ceir.config.model.ConsignmentUpdateRequest;
import com.gl.ceir.config.model.FileDetails;
import com.gl.ceir.config.model.FilterRequest;
import com.gl.ceir.config.model.GenricResponse;
import com.gl.ceir.config.model.StolenandRecoveryMgmt;
import com.gl.ceir.config.repository.AuditTrailRepository;
import com.gl.ceir.config.service.impl.StackholderPolicyMappingServiceImpl;
import com.gl.ceir.config.service.impl.StolenAndRecoveryServiceImpl;

import io.swagger.annotations.ApiOperation;

@RestController
public class StolenAndRecoveryController {

	private static final Logger logger = LogManager.getLogger(StolenAndRecoveryController.class);

	@Autowired
	StolenAndRecoveryServiceImpl stolenAndRecoveryServiceImpl;

	@Autowired
	StackholderPolicyMappingServiceImpl stackholderPolicyMappingServiceImpl;
	
	@Autowired
	AuditTrailRepository auditTrailRepository;

	@ApiOperation(value = "Upload Recovery Details.", response = GenricResponse.class)
	@RequestMapping(path = "/stakeholder/Recovery", method = RequestMethod.POST)
	public GenricResponse uploadFileAction(@RequestBody StolenandRecoveryMgmt stolenandRecoveryRequest) {

		logger.info("Upload Recovery Details = " + stolenandRecoveryRequest);

		GenricResponse genricResponse =	stolenAndRecoveryServiceImpl.uploadDetails(stolenandRecoveryRequest);
		logger.info("Upload recovery details response = " + genricResponse);

		return genricResponse;
	}

	@ApiOperation(value = "Upload Stolen Details.", response = GenricResponse.class)
	@RequestMapping(path = "/stakeholder/Stolen", method = RequestMethod.POST)
	public GenricResponse uploadStolenDetails(@RequestBody StolenandRecoveryMgmt stolenandRecoveryDetails){
		logger.info("Stolen upload Request = " + stolenandRecoveryDetails);

		GenricResponse genricResponse =	stolenAndRecoveryServiceImpl.uploadDetails(stolenandRecoveryDetails);
		logger.info("Stolen upload Response = " + genricResponse);

		return genricResponse;
	}

	@ApiOperation(value = "Upload Stolen Details.", response = GenricResponse.class)
	@RequestMapping(path = "v2/stakeholder/Stolen", method = RequestMethod.POST)
	public GenricResponse v2uploadStolenDetails(@RequestBody StolenandRecoveryMgmt stolenandRecoveryDetails){
		logger.info("Stolen upload Request = " + stolenandRecoveryDetails);
		GenricResponse genricResponse =	stolenAndRecoveryServiceImpl.v2uploadDetails(stolenandRecoveryDetails);
		logger.info("Stolen upload Response = " + genricResponse);

		return genricResponse;
	}

	@ApiOperation(value = "Upload Multiple Stolen Details.", response = GenricResponse.class)
	@RequestMapping(path = "/stakeholder/uploadMultiple/StolenAndRecovery", method = RequestMethod.POST)
	public GenricResponse uploadMultipleStolenDetails(@RequestBody List<StolenandRecoveryMgmt> stolenandRecoveryDetails)
	{
		logger.info("Multiple Stolen Upload Request="+stolenandRecoveryDetails);

		GenricResponse genricResponse =	stolenAndRecoveryServiceImpl.uploadMultipleStolen(stolenandRecoveryDetails);
		logger.info("Muliple Stolen Upload Response = " + genricResponse);

		return genricResponse;
	}

	@ApiOperation(value = "View Stolen and Recovery Details.", response = StolenandRecoveryMgmt.class)
	@RequestMapping(path = "filter/stakeholder/record", method = RequestMethod.POST)
	public MappingJacksonValue getAllActionDetails(@RequestBody FilterRequest stolenandRecoveryDetails,
			@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value = "file", defaultValue = "0") Integer file,
			@RequestParam(value = "source", defaultValue = "menu") String source) {

		MappingJacksonValue mapping = null;
		logger.info("source == "+source);
		if(file == 0) {
			logger.info("Record request to Stolen And Recovery Info = " +  stolenandRecoveryDetails);
			Page<StolenandRecoveryMgmt>	stolenandRecoveryDetailsResponse = stolenAndRecoveryServiceImpl.getAllInfo(stolenandRecoveryDetails,pageNo,pageSize,source);
			mapping = new MappingJacksonValue(stolenandRecoveryDetailsResponse);
		}else {
			logger.info("Request to export filtered Stolen And Recovery = " + stolenandRecoveryDetails);
			FileDetails fileDetails = stolenAndRecoveryServiceImpl.getFilteredStolenAndRecoveryInFile(stolenandRecoveryDetails, source);
			mapping = new MappingJacksonValue(fileDetails);
		}
		
		logger.info("Record Response of Stolen And Recovery Info = " + mapping);

		return mapping;
	}

	@ApiOperation(value = "Delete Stolen and  Recovery Details.", response = GenricResponse.class)
	@RequestMapping(path = "/stakeholder/Delete", method = RequestMethod.DELETE)
	public GenricResponse deleteRecord(@RequestBody FilterRequest filterRequest) {

		logger.info("Record Delete request = " + filterRequest);
		GenricResponse genricResponse = stolenAndRecoveryServiceImpl.deleteRecord(filterRequest);

		logger.info("Response send = " + genricResponse);

		return genricResponse;
	}

	@ApiOperation(value = "Update Stolen and  Recovery Details.", response = GenricResponse.class)
	@RequestMapping(path = "/stakeholder/update", method = RequestMethod.PUT)
	public GenricResponse updateRecord(@RequestBody StolenandRecoveryMgmt stolenandRecoveryRequest) {
		logger.info("Record update request = " + stolenandRecoveryRequest);

		GenricResponse genricResponse = stolenAndRecoveryServiceImpl.updateRecord(stolenandRecoveryRequest);
		logger.info("Response send= " + genricResponse);

		return genricResponse;
	}

	@ApiOperation(value = "View Stolen and Recovery Details.", response = StolenandRecoveryMgmt.class)
	@RequestMapping(path = "/stakeholder/view", method = RequestMethod.POST)
	public MappingJacksonValue viewRecord(@RequestBody StolenandRecoveryMgmt stolenandRecoveryRequest)
	{
		logger.info("view Stolen and recovery request="+stolenandRecoveryRequest);

		StolenandRecoveryMgmt mgmt = stolenAndRecoveryServiceImpl.viewRecord(stolenandRecoveryRequest);
		logger.info("View details Response send =" + mgmt);

		MappingJacksonValue mapping = new MappingJacksonValue(mgmt);
		return mapping;

	}
	
	@ApiOperation(value = "View Stolen and  Recovery Details By txn Id.", response = StolenandRecoveryMgmt.class)
	@PostMapping("/stolen-and-recovery/by-txnId")
	public MappingJacksonValue getByTxnId(@RequestBody StolenandRecoveryMgmt stolenandRecoveryRequest)
	{
		logger.info("view Stolen and recovery request by txn id = " + stolenandRecoveryRequest);

		StolenandRecoveryMgmt mgmt = stolenAndRecoveryServiceImpl.getByTxnId(stolenandRecoveryRequest);
		logger.info("View details Response send = " + mgmt);

		MappingJacksonValue mapping = new MappingJacksonValue(mgmt);
		return mapping;
	}
	
	@ApiOperation(value = "Accept Reject Stolen/Recovery/Block/Unblock.", response = GenricResponse.class)
	@RequestMapping(path = "accept-reject/stolen-recovery-block-unblock", method = RequestMethod.PUT)
	public GenricResponse updateConsigmentStatus(@RequestBody ConsignmentUpdateRequest acceptRejectRequest) {

		logger.info("Request to accept/reject the stolen-recovery-block-unblock = " + acceptRejectRequest);

		GenricResponse genricResponse = stolenAndRecoveryServiceImpl.acceptReject(acceptRejectRequest);

		return genricResponse ;
	}
}