package com.gl.ceir.config.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gl.ceir.config.model.AllRequest;
import com.gl.ceir.config.model.ConsignmentMgmt;
import com.gl.ceir.config.model.ConsignmentUpdateRequest;
import com.gl.ceir.config.model.EndUserDB;
import com.gl.ceir.config.model.FileDetails;
import com.gl.ceir.config.model.FilterRequest;
import com.gl.ceir.config.model.GenricResponse;
import com.gl.ceir.config.service.impl.EnduserServiceImpl;

import io.swagger.annotations.ApiOperation;

@RestController
public class EnduserController {

	private static final Logger logger = LogManager.getLogger(EnduserController.class);

	@Autowired
	EnduserServiceImpl enduserServiceImpl;

	@ApiOperation(value = "View Regularized DB of end User", response = GenricResponse.class)
	@PostMapping("/end-user/searchByNid")
	public MappingJacksonValue getEnduserByNid(@RequestBody AllRequest data) {

		MappingJacksonValue mapping = null;

		GenricResponse genricResponse = enduserServiceImpl.endUserByNid(data);
		mapping = new MappingJacksonValue(genricResponse);

		return mapping;
	}

	@ApiOperation(value = "Save End User", response = GenricResponse.class)
	@PostMapping("/end-user")
	public MappingJacksonValue saveEndUser(@RequestBody EndUserDB endUserDB) {

		MappingJacksonValue mapping = null;
		logger.info("Request to save end users = " + endUserDB);
		logger.info("regularze data: "+endUserDB.getRegularizeDeviceDbs().toString());
		GenricResponse genricResponse = enduserServiceImpl.saveEndUser(endUserDB);
		//logger.info("Response of save end users = " + endUserDB);
		mapping = new MappingJacksonValue(genricResponse);

		return mapping;
	}
	
	@ApiOperation(value = "Update End User.", response = GenricResponse.class)
	@PutMapping("/end-user") 
	public GenricResponse updateEndUser(@RequestBody EndUserDB endUserDB) {

		logger.info("Update End User Db = " + endUserDB);

		GenricResponse genricResponse = enduserServiceImpl.updateEndUser(endUserDB);

		return genricResponse ;

	}

//	@ApiOperation(value = "pagination View filtered consignment", response = ConsignmentMgmt.class)
//	@PostMapping("/filter/end-users")
//	public MappingJacksonValue withPaginationConsignments(@RequestBody FilterRequest filterRequest,
//			@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
//			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
//			@RequestParam(value = "file", defaultValue = "0") Integer file) {
//
//		MappingJacksonValue mapping = null;
//		if(file == 0) {
//			logger.info("Request to view filtered end users = " + filterRequest);
//			Page<EndUserDB> consignment = enduserServiceImpl.filter(filterRequest, pageNo, pageSize);
//			mapping = new MappingJacksonValue(consignment);
//		}else {
//			logger.info("Request to export filtered end users = " + filterRequest);
//			FileDetails fileDetails = enduserServiceImpl.getFilteredEndUserInFileV2(filterRequest);
//			mapping = new MappingJacksonValue(fileDetails);
//		}
//
//		logger.info("Response of view Request = " + mapping);
//
//		return mapping;
//	}

	@ApiOperation(value = "Accept Reject end-users.", response = GenricResponse.class)
	@PutMapping("/accept-reject/end-users") 
	public GenricResponse updateEndUserStatus(@RequestBody ConsignmentUpdateRequest acceptRejectRequest) {

		logger.info("Request to accept/reject the end-user = " + acceptRejectRequest);

		GenricResponse genricResponse = enduserServiceImpl.acceptReject(acceptRejectRequest);

		return genricResponse ;

	}
	
	


}
