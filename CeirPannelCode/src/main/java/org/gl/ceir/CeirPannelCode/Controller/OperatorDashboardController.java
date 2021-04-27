package org.gl.ceir.CeirPannelCode.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class OperatorDashboardController {
	
	@RequestMapping(value = "/operatorDashboard",method = {RequestMethod.GET})
	public ModelAndView userLoginReport(){
		ModelAndView mv=new ModelAndView();
		mv.setViewName("OperatorDashboard");
		return  mv;
	}
}
