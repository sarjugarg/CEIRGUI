package org.gl.ceir.CeirPannelCode.Feignclient;

import java.util.List;

import org.gl.ceir.CeirPannelCode.Model.Feature;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Service
@FeignClient(url = "${apiUrl1}",value = "featureUrls" )
public interface FeatureFeignImpl { 
	@PostMapping("/featureList/{userid}")
	public List<Feature> featureList(@PathVariable("userid") Integer userid);
	
	/*
	 * @PostMapping("/featureList") public List<Feature> featureList(@RequestParam
	 * Integer userid,UserHeader user);
	 */
}
