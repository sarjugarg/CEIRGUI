package org.gl.ceir.CeirPannelCode.Controller;

import javax.servlet.http.HttpSession;

import org.gl.ceir.CeirPannelCode.Feignclient.FeignCleintImplementation;
import org.gl.ceir.CeirPannelCode.Model.FilterRequest;
import org.gl.ceir.pagination.model.PolicyConfigContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PolicyConfigController {
		
	@Autowired
	FeignCleintImplementation feignCleintImplementation;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	@RequestMapping(value=
		{"/policyManagement"},method={org.springframework.web.bind.annotation.
				RequestMethod.GET,org.springframework.web.bind.annotation.RequestMethod.POST}
			)
	    public ModelAndView viewPolicyManagement(HttpSession session) {
		ModelAndView mv = new ModelAndView();
		 log.info(" view PolicyManagement entry point."); 
		 mv.setViewName("policyConfig");
		log.info(" view PolicyManagement exit point."); 
		return mv; 
	}

	
	
	
	@PostMapping("/policy/viewTag") 
	public @ResponseBody PolicyConfigContent policyViewTag (@RequestBody FilterRequest filterRequest)  {
		log.info("request send to the policyViewTag api="+filterRequest);
		PolicyConfigContent response= feignCleintImplementation.viewPolicyConfigFeign(filterRequest);

		log.info("response from currency api "+response);
		return response;

		}
	

	
	@PutMapping("/policy/update")
	public @ResponseBody PolicyConfigContent updatePolicy (@RequestBody PolicyConfigContent policyConfigContent) {
		log.info("request send to Edit policy api="+policyConfigContent);
		policyConfigContent = feignCleintImplementation.updatePolicy(policyConfigContent);
		log.info("response from Edit api "+policyConfigContent);
		return policyConfigContent;
		
	}
}
