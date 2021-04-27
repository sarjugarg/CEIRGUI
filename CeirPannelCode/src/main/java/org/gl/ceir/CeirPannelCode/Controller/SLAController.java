package org.gl.ceir.CeirPannelCode.Controller;

import javax.servlet.http.HttpSession;

import org.gl.ceir.CeirPannelCode.Feignclient.UserProfileFeignImpl;
import org.gl.ceir.CeirPannelCode.Model.FileExportResponse;
import org.gl.ceir.CeirPannelCode.Model.FilterRequest;
import org.gl.ceir.CeirPannelCode.Model.SLAfilterRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

@Controller
public class SLAController {
	
	@Autowired
	UserProfileFeignImpl userProfileFeignImpl;
		
private final Logger log = LoggerFactory.getLogger(getClass());
	
	@RequestMapping(value=
		{"/slaManagement"},method={org.springframework.web.bind.annotation.
				RequestMethod.GET,org.springframework.web.bind.annotation.RequestMethod.POST}
			)
	    public ModelAndView viewMessageManagement(HttpSession session) {
		ModelAndView mv = new ModelAndView();
		 log.info(" view sla Management entry point."); 
		 mv.setViewName("slaManagement");
		log.info(" view sla Management exit point."); 
		return mv; 
	}
	
	
	//***************************************** Export SLA  controller *********************************
	@PostMapping("exportSlaData")
	@ResponseBody
	public FileExportResponse exportToExcel(@RequestBody SLAfilterRequest filterRequest,HttpSession session)
	{
		Gson gsonObject=new Gson();
		Object response;
		Integer file = 1;	
		filterRequest.setPublicIp(session.getAttribute("publicIP").toString());
		filterRequest.setBrowser(session.getAttribute("browser").toString());
		log.info("filterRequest:::::::::"+filterRequest);
	response= userProfileFeignImpl.viewSLARequest(filterRequest, filterRequest.getPageNo(), filterRequest.getPageSize(), file);
	FileExportResponse fileExportResponse;
	   Gson gson= new Gson(); 
	   String apiResponse = gson.toJson(response);
	   fileExportResponse = gson.fromJson(apiResponse, FileExportResponse.class);
	   log.info("response  from Running Alert Export  api="+fileExportResponse);
		
		return fileExportResponse;
	}
}
