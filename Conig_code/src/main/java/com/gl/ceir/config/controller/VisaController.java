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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gl.ceir.config.model.CeirActionRequest;
import com.gl.ceir.config.model.EndUserDB;
import com.gl.ceir.config.model.FileDetails;
import com.gl.ceir.config.model.FilterRequest;
import com.gl.ceir.config.model.GenricResponse;
import com.gl.ceir.config.model.RegularizeDeviceDb;
import com.gl.ceir.config.model.VisaUpdateDb;
import com.gl.ceir.config.service.impl.EnduserServiceImpl;

import io.swagger.annotations.ApiOperation;

@RestController
public class VisaController {

	private static final Logger logger = LogManager.getLogger(VisaController.class);

	@Autowired
	EnduserServiceImpl enduserServiceImpl;

	@ApiOperation(value = "Update visa of end User", response = RegularizeDeviceDb.class)
	@PutMapping("/visa/end-user")
	public MappingJacksonValue getEnduserByNid(@RequestBody EndUserDB endUserDB) {
		logger.info("A request to to update user visa : " + endUserDB);
		MappingJacksonValue mapping = null;
		GenricResponse genricResponse = enduserServiceImpl.updateVisaEndUser(endUserDB);
		logger.info("A request to to update user visa : " + endUserDB);
		
		mapping = new MappingJacksonValue(genricResponse);

		return mapping;
	}
	
	@ApiOperation(value = "View End User data by Id", response = GenricResponse.class)
	@PostMapping("/visa/viewById")
	public MappingJacksonValue getEnduserByNid(@RequestBody FilterRequest filterRequest) {

		MappingJacksonValue mapping = null;

		GenricResponse genricResponse = enduserServiceImpl.endUserById(filterRequest);
		mapping = new MappingJacksonValue(genricResponse);

		return mapping;
	}

	@ApiOperation(value = "View visa update data", response = VisaUpdateDb.class)
	@RequestMapping(path = "/visa/view", method = RequestMethod.POST)
	public MappingJacksonValue getDeviceByNid( @RequestBody FilterRequest filterRequest,
			@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value = "file", defaultValue = "0") Integer file,
			@RequestParam(value = "source", defaultValue = "menu") String source) {

		MappingJacksonValue mapping = null;
		logger.info("source value is : "+source);
		if(file == 0) {
			logger.info("Visa update view request " + filterRequest);
			Page<VisaUpdateDb> customInfo = enduserServiceImpl.viewAllUpdateVisaRecord(filterRequest, pageNo, pageSize,source);
			mapping = new MappingJacksonValue(customInfo);
		}else {
			logger.info("visa update Export request " + filterRequest);
			FileDetails fileDetails = enduserServiceImpl.getFilterDataInFile(filterRequest,pageNo,pageSize,source);
			mapping = new MappingJacksonValue(fileDetails);
		}	
		return mapping;
	}
	
	@ApiOperation(value = "Accept/Reject Update Visa", response = GenricResponse.class)
	@RequestMapping(path = "accept-reject/end-user-visa", method = RequestMethod.PUT)
	public GenricResponse updateEndUSerVisa(@RequestBody CeirActionRequest ceirActionRequest) {

		logger.info("Request to update the regularized devices = " + ceirActionRequest);

		GenricResponse genricResponse = enduserServiceImpl.acceptReject(ceirActionRequest);

		return genricResponse ;
	}
}
