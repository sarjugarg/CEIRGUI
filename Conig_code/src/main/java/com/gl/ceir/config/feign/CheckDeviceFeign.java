package com.gl.ceir.config.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gl.ceir.config.model.CheckDeviceReponse;
import com.gl.ceir.config.model.CheckImeiResponse;
import com.gl.ceir.config.model.ImeiValidate;

@Service
@FeignClient(url = "${ApiPath2}", value = "ApiPath2" )
public interface CheckDeviceFeign {

	@PostMapping(value="/cc/CheckImeI")
	public CheckImeiResponse checkImei(ImeiValidate imeiValidate) ;

	
	@PostMapping(value="/gsma/GsmaValues")
	public CheckDeviceReponse imeiValues(@RequestParam String imei) ;

}
