package org.gl.ceir.CeirPannelCode.Feignclient;

import org.gl.ceir.CeirPannelCode.Model.FilterRequest_UserPaidStatus;
import org.gl.ceir.CeirPannelCode.Model.GenricResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import CeirPannelCode.Model.Register_UploadPaidStatus;

@Component
@Service
@FeignClient(url = "${feignClientPath}",value = "dsj" )
public interface UserPaidStatusFeignClient {

	@RequestMapping(value="/filter/end-user-device-info" ,method=RequestMethod.POST) 
	public Object consignmentFilter(@RequestBody FilterRequest_UserPaidStatus filterRequest,
			@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value = "file", defaultValue = "0") Integer file,
			@RequestParam(value="source",defaultValue ="menu") String source) ;



	@PostMapping("/end-user-device-info") 
	public GenricResponse uploadPaidUser(@RequestBody Register_UploadPaidStatus model) ;
	
	@PutMapping("/update-tax-paid-status/end-user-device-info")
	public GenricResponse tax(@RequestBody Register_UploadPaidStatus model);
	
}

