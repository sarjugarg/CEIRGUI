package org.gl.ceir.CeirPannelCode.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ImmigrationController {

	private final Logger log = LoggerFactory.getLogger(getClass());
	@GetMapping(value="immigration_register")
	public ModelAndView register()
	{
		ModelAndView mv= new ModelAndView();
		mv.setViewName("immigration_foreign");
		return mv;
	}

	@GetMapping(value="edit_immigration")
	public ModelAndView update()
	{
		ModelAndView mv= new ModelAndView();
		mv.setViewName("editImmigration");
		return mv;
	}
}
