package org.gl.ceir.CeirPannelCode.Feignclient;

import org.gl.ceir.CeirPannelCode.Model.EndUserVisaInfo;
import org.gl.ceir.CeirPannelCode.Model.GenricResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Service
@FeignClient(url = "${feignClientPath}",value = "dsj" )
public interface ImmigrationFeignImpl {
	@PutMapping("/end-user")
	public @ResponseBody GenricResponse RegisterEndUserDevice(EndUserVisaInfo visaInfo);
	
}
