package org.gl.ceir.CeirPannelCode.Controller;

import javax.servlet.http.HttpSession;

import org.gl.ceir.CeirPannelCode.Feignclient.FeignCleintImplementation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DeviceActivation {

	@Autowired
	FeignCleintImplementation feignCleintImplementation;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@RequestMapping(value=
		{"/deviceActivation"},method={org.springframework.web.bind.annotation.
				RequestMethod.GET,org.springframework.web.bind.annotation.RequestMethod.POST}
			)
	    public ModelAndView viewMessageManagement(HttpSession session, @RequestParam(name="passportNo",required = false)String passportNo) {
		log.info("passport No.--->" +passportNo);
		ModelAndView mv = new ModelAndView();
		 log.info(" view deviceActivation entry point."); 
		 mv.setViewName("deviceActivation");
		log.info(" view deviceActivation exit point."); 
		mv.addObject("passportNo", passportNo);
		return mv; 
	}	
}
