package com.gl.ceir.config.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gl.ceir.config.model.ConsignmentMgmt;
import com.gl.ceir.config.model.ConsignmentUpdateRequest;
import com.gl.ceir.config.model.FileDetails;
import com.gl.ceir.config.model.FilterRequest;
import com.gl.ceir.config.model.GenricResponse;
import com.gl.ceir.config.service.impl.ConsignmentServiceImpl;

import io.swagger.annotations.ApiOperation;

@RestController
public class ConsignmentController {

	private static final Logger logger = LogManager.getLogger(ConsignmentController.class);

	@Autowired
	ConsignmentServiceImpl consignmentServiceImpl;

	@ApiOperation(value = "Add new consignment.", response = GenricResponse.class)
	@RequestMapping(path = "/consignment/register", method = RequestMethod.POST)
	public GenricResponse uploadFile(@RequestBody ConsignmentMgmt consignmentUploadRequest) {

		logger.info("Consignment Register Request = " + consignmentUploadRequest);

		GenricResponse genricResponse = consignmentServiceImpl.registerConsignment( consignmentUploadRequest);
		logger.info("Consignment Register Response = " + genricResponse);

		return genricResponse;
	}

	@Transactional
	@ApiOperation(value = "Update Consignment Info.", response = GenricResponse.class)
	@RequestMapping(path = "/consignment/update", method = RequestMethod.POST)
	public GenricResponse updateConsigmentInfo(@RequestBody ConsignmentMgmt consignmentUploadRequest) {

		logger.info("Update Consignment Request = " + consignmentUploadRequest);

		GenricResponse genricResponse =	consignmentServiceImpl.updateConsignment(consignmentUploadRequest);
		logger.info("Update Consignment Response ="+genricResponse);

		return genricResponse;

	}

	@ApiOperation(value = "View all the list of consignment", response = ConsignmentMgmt.class)
	@GetMapping("/consignment/Record")
	public MappingJacksonValue getByImporterId(@RequestParam("userId") Long userId) {

		logger.info("Request TO view TO all record of user="+userId);

		List<ConsignmentMgmt> consignment = consignmentServiceImpl.getAll(userId);
		MappingJacksonValue mapping = new MappingJacksonValue(consignment);
		logger.info("Response of view Request = " + mapping);

		return mapping;
	}

	@ApiOperation(value = "View filtered consignment", response = ConsignmentMgmt.class)
	@PostMapping("v1/filter/consignment")
	public MappingJacksonValue filterConsignments(@RequestBody FilterRequest filterRequest,
			@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

		logger.info("Request TO view filtered consignment = " + filterRequest);

		List<ConsignmentMgmt>  consignment = consignmentServiceImpl.getFilterConsignments(filterRequest, pageNo, pageSize);
		MappingJacksonValue mapping = new MappingJacksonValue(consignment);
		logger.info("Response of view Request ="+mapping);

		return mapping;
	}

	@ApiOperation(value = "pagination View filtered consignment", response = ConsignmentMgmt.class)
	@PostMapping("v2/filter/consignment")
	public MappingJacksonValue withPaginationConsignments(@RequestBody FilterRequest filterRequest,
			@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value = "file", defaultValue = "0") Integer file,
			@RequestParam(value = "source", defaultValue = "menu") String source) {

		logger.info("source " + source);
		MappingJacksonValue mapping = null;
		if(file == 0) {
			logger.info("Request to view filtered consignment = " + filterRequest);
			Page<ConsignmentMgmt> consignment =  consignmentServiceImpl.getFilterPaginationConsignments(filterRequest, pageNo, pageSize,source);
			mapping = new MappingJacksonValue(consignment);
		}else {
			logger.info("Request to export filtered consignment = " + filterRequest);
			FileDetails fileDetails = consignmentServiceImpl.getFilteredConsignmentInFileV2(filterRequest, source);
			mapping = new MappingJacksonValue(fileDetails);
		}

		logger.info("Response of view Request = " + mapping);

		return mapping;
	}

	@ApiOperation(value = "View the Particular consignment info.", response = ConsignmentMgmt.class)
	@PostMapping("/consignment/view")
	public MappingJacksonValue getByTxnId(@RequestBody FilterRequest filterRequest) {

		logger.info("View Request only Single Record = " + filterRequest);

		ConsignmentMgmt consignmentRecordInfo = consignmentServiceImpl.getRecordInfo(filterRequest);
		MappingJacksonValue mapping = new MappingJacksonValue(consignmentRecordInfo);
		logger.info("Response of View ="+mapping);

		return mapping;
	}

	@Transactional
	@ApiOperation(value = "Delete Consignment.", response = GenricResponse.class)
	@DeleteMapping("/consigment/delete")
	public GenricResponse deleteConsigment(@RequestBody ConsignmentUpdateRequest consignmentUpdateRequest) {

		logger.info("Consignment Withdraw Request = " + consignmentUpdateRequest);

		GenricResponse genricResponse = null;
		if(consignmentServiceImpl.updatePendingApproval(consignmentUpdateRequest)) {	
			genricResponse = consignmentServiceImpl.deleteConsigmentInfo(consignmentUpdateRequest);
			logger.info("Response of Delete Request = " + genricResponse);
		}else {
			new GenricResponse(1, "Error during update status before deleting", consignmentUpdateRequest.getTxnId());
		}
		return genricResponse;
	}

	// For Approve 
	@Transactional
	@ApiOperation(value = "Update Consignment Status.", response = GenricResponse.class)
	@PutMapping("update/consigmentStatus")
	public GenricResponse updateConsigmentStatus(@RequestBody ConsignmentUpdateRequest consignmentUpdateRequest) {

		logger.info("Request to update the consignmentStatus = " + consignmentUpdateRequest);

		GenricResponse genricResponse = consignmentServiceImpl.updateConsignmentStatus(consignmentUpdateRequest);

		return genricResponse ;
	}
}