package org.gl.ceir.CeirPannelCode.Controller;

import javax.servlet.http.HttpSession;

import org.gl.ceir.CeirPannelCode.Feignclient.FeignCleintImplementation;
import org.gl.ceir.CeirPannelCode.Feignclient.UserProfileFeignImpl;
import org.gl.ceir.CeirPannelCode.Model.FileExportResponse;
import org.gl.ceir.CeirPannelCode.Model.FilterRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

@Controller
public class VisaController {
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	UserProfileFeignImpl userProfileFeignImpl;
	
	@Autowired
	FeignCleintImplementation feignCleintImplementation;
	
	@GetMapping("updateVisa")
	public ModelAndView view(@RequestParam(name="source",defaultValue ="menu" ,required = false) String source,HttpSession session) {
		
		log.info("source type--="+source);
		session.setAttribute("filterSource", source);
		return new ModelAndView("visaUpdate");
	}

	//------------------------------------- Export Pending TAC ----------------------------------------
	@PostMapping("exportVisaData")
	@ResponseBody
	public FileExportResponse exportToExcel(@RequestBody FilterRequest filterRequest,HttpSession session)
	{
		Gson gsonObject=new Gson();
		Object response;
		Integer file = 1;	
		filterRequest.setPublicIp(session.getAttribute("publicIP").toString());
		filterRequest.setBrowser(session.getAttribute("browser").toString());
		log.info("filterRequest:::::::::"+filterRequest);
		response= feignCleintImplementation.viewVisaRequest(filterRequest, filterRequest.getPageNo(), filterRequest.getPageSize(), file,filterRequest.getSource());
		FileExportResponse fileExportResponse;
		Gson gson= new Gson(); 
		String apiResponse = gson.toJson(response);
		fileExportResponse = gson.fromJson(apiResponse, FileExportResponse.class);
		log.info("response  from Pending Tac Export  api="+fileExportResponse);

		return fileExportResponse;
	}		



}
