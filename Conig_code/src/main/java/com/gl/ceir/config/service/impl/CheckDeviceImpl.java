package com.gl.ceir.config.service.impl;

import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gl.ceir.config.feign.CheckDeviceFeign;
import com.gl.ceir.config.model.AuditTrail;
import com.gl.ceir.config.model.CheckDevice;
import com.gl.ceir.config.model.CheckDeviceReponse;
import com.gl.ceir.config.model.CheckImeiResponse;
import com.gl.ceir.config.model.GenricResponse;
import com.gl.ceir.config.model.ImeiValidate;
import com.gl.ceir.config.model.constants.Features;
import com.gl.ceir.config.model.constants.GenericMessageTags;
import com.gl.ceir.config.model.constants.SubFeatures;
import com.gl.ceir.config.repository.AuditTrailRepository;

@Service
public class CheckDeviceImpl {

	@Autowired
	CheckDeviceFeign checkDeviceFeign;
	
	@Autowired
	AuditTrailRepository auditTrailRepository;
	
	private static final Logger logger = LogManager.getLogger(CheckDeviceImpl.class);
	
	public GenricResponse checkDevices( CheckDevice checkDevice) {
		logger.info("inside check device and daa given: "+checkDevice);
		    ImeiValidate imeiValidate=new ImeiValidate("CheckImei",checkDevice.getDeviceIdType(),
	        		"default", checkDevice.getDeviceId() );
	        try {
                logger.info("going to save audit trail: ");  
                AuditTrail auditTrail = new AuditTrail(0, 
						"NA", 
						17l, 
					    "End User", 
					    44, Features.Check_Device, 
						SubFeatures.Check, 
						"", "NA","End User");
				auditTrailRepository.save(auditTrail);
				
	        	logger.info("now going to call check imei api");
		        CheckImeiResponse resp=checkDeviceFeign.checkImei(imeiValidate);
		        if(Objects.nonNull(resp)) {
		        	logger.info("response got : "+resp);
		        	if("Pass".equalsIgnoreCase(resp.getStatus())){
		        		logger.info("imei found so calling imei data api");
		        		CheckDeviceReponse data=checkDeviceFeign.imeiValues(checkDevice.getDeviceId());
		        		logger.info("imei data api called");
		    	        GenricResponse response=new GenricResponse(200,resp.getStatus(),"",data);
		    	        return response;
		        	}
		        	else {
				        GenricResponse response=new GenricResponse(409,resp.getErrorMessage(),"","");
				        return response;
		        	}
		        }
		        else {
			        GenricResponse response = new GenricResponse(500, GenericMessageTags.COMMAN_FAIL_MSG.getTag(), "Oops Somthing wrong happened","");
			        return response;
		        }
	        }
	        catch(Exception e){
	        	logger.info(e.getMessage(), e);
		        GenricResponse response=new GenricResponse(500, GenericMessageTags.COMMAN_FAIL_MSG.getTag(), "Oops Somthing wrong happened","");
		        return response;

	        }
	}
}
