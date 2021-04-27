package org.gl.ceir.CeirPannelCode.Controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CustomerCareSearch {
			
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	
	@GetMapping("search")
	public ModelAndView ViewCustomerCare(@RequestParam(name="via", required = false) String via,
									 @RequestParam(name="msisdn", required = false) String msisdn,
									 @RequestParam(name="imei", required = false) String imei,
									 @RequestParam(name="deviceIdType", required = false) String deviceIdType,
									 @RequestParam(name="deviceIdvalue", required = false) Integer deviceIdvalue,
									 HttpSession session){
		log.info("via-->"+via+" msisdn-->"+msisdn+" imei-->"+imei+" deviceIdType-->"+deviceIdType);
		ModelAndView modelAndView = new ModelAndView();
		if("other".equals(via)) {
			modelAndView.addObject("msisdn",msisdn );
			modelAndView.addObject("imei",imei );
			modelAndView.addObject("deviceIdType",deviceIdType);
			modelAndView.addObject("deviceIdvalue",deviceIdvalue);
			modelAndView.setViewName("customerCare");
		} 
		else {
			modelAndView.setViewName("customerSearch");
		}
		
		return modelAndView;
	}
}
