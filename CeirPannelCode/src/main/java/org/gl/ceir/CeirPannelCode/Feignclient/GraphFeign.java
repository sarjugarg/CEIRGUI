package org.gl.ceir.CeirPannelCode.Feignclient;

import java.util.List;

import org.gl.ceir.CeirPannelCode.Model.LoginGraphFilter;
import org.gl.ceir.CeirPannelCode.Model.UserLoginReport;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Service
@FeignClient(url="${apiUrl1}",value = "graphUrls")
public interface GraphFeign {

	@PostMapping("/report/userLogin") 
	public List<UserLoginReport> userLoginGraph(@RequestBody LoginGraphFilter filter);

}
