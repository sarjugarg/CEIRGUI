package org.gl.ceir.CeirPannelCode.Controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ErrorPageController {
	    @ExceptionHandler(NoHandlerFoundException.class)
	    public ModelAndView handle404() {
	    	ModelAndView modelAndView= new ModelAndView("notFound");
	        return modelAndView;
	    }
	
}
