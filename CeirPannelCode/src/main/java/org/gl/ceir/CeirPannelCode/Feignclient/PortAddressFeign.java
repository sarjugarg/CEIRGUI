package org.gl.ceir.CeirPannelCode.Feignclient;

import java.util.List;

import org.gl.ceir.CeirPannelCode.Model.PortAddress;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Service
@FeignClient(url="${apiUrl1}/portAddress",value = "portUrls")
public interface PortAddressFeign {

	@PostMapping("/getByPort/{arrivalPort}")
	public List<PortAddress> getByPort(@PathVariable("arrivalPort") Integer arrivalPort);
}
