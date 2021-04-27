package org.gl.ceir.CeirPannelCode.Controller;

import javax.servlet.http.HttpSession;

import org.gl.ceir.CeirPannelCode.Feignclient.UserProfileFeignImpl;
import org.gl.ceir.CeirPannelCode.Model.AlertRequest;
import org.gl.ceir.CeirPannelCode.Model.FileExportResponse;
import org.gl.ceir.CeirPannelCode.Model.FilterRequest;
import org.gl.ceir.CeirPannelCode.Model.GenricResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

@Controller
public class AlertController {
	@Autowired
	UserProfileFeignImpl userProfileFeignImpl;
		
private final Logger log = LoggerFactory.getLogger(getClass());
	
	@RequestMapping(value=
		{"/alertManagment"},method={org.springframework.web.bind.annotation.
				RequestMethod.GET,org.springframework.web.bind.annotation.RequestMethod.POST}
			)
	    public ModelAndView viewMessageManagement(HttpSession session) {
		ModelAndView mv = new ModelAndView();
		 log.info(" view Alert Management entry point."); 
		 mv.setViewName("alertManagement");
		log.info(" view Alert Management exit point."); 
		return mv; 
	}
	
	
	
	//------------------------------------- view Alert Address ----------------------------------------							
	
			@PostMapping("alertViewByID") 
			public @ResponseBody GenricResponse viewAlertAddress (@RequestBody FilterRequest filterRequest,HttpSession session)  {
				log.info("request send to the View Alert api="+filterRequest);
				filterRequest.setPublicIp(session.getAttribute("publicIP").toString());
				filterRequest.setBrowser(session.getAttribute("browser").toString());
				GenricResponse response= userProfileFeignImpl.viewAlertFeign(filterRequest);
				log.info("response from View api "+response);
				return response;
		}
			
			
	//------------------------------------- update alert ----------------------------------------							
			
			@PostMapping("updateAlert") 
			public @ResponseBody GenricResponse updateAlerts (@RequestBody FilterRequest filterRequest,HttpSession session)  {
				log.info("request send to the Update Alert api="+filterRequest);
				filterRequest.setPublicIp(session.getAttribute("publicIP").toString());
				filterRequest.setBrowser(session.getAttribute("browser").toString());
				GenricResponse response= userProfileFeignImpl.updateAlertFeign(filterRequest);
				log.info("response from update api "+response);
				return response;
		}	
				
			
	
	//***************************************** Export Alert  controller *********************************
	@PostMapping("exportAlertData")
	@ResponseBody
	public FileExportResponse exportToExcel(@RequestBody AlertRequest filterRequest,HttpSession session,
			@RequestParam(name = "source", defaultValue = "menu", required = false) String source)
	{
		Gson gsonObject=new Gson();
		Object response;
		Integer file = 1;	
		log.info("filterRequest:::::::::"+filterRequest);
		filterRequest.setPublicIp(session.getAttribute("publicIP").toString());
		filterRequest.setBrowser(session.getAttribute("browser").toString());
	response= userProfileFeignImpl.viewAlertRequest(filterRequest, filterRequest.getPageNo(), filterRequest.getPageSize(), file,source);
	FileExportResponse fileExportResponse;
	   Gson gson= new Gson(); 
	   String apiResponse = gson.toJson(response);
	   fileExportResponse = gson.fromJson(apiResponse, FileExportResponse.class);
	   log.info("response  from  Alert Export  api="+fileExportResponse);
		
		return fileExportResponse;
	}
	
	
}
