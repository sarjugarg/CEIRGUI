package org.gl.ceir.CeirPannelCode.Controller;

import javax.servlet.http.HttpSession;

import org.gl.ceir.CeirPannelCode.Feignclient.FeignCleintImplementation;
import org.gl.ceir.CeirPannelCode.Model.FileExportResponse;
import org.gl.ceir.CeirPannelCode.Model.FilterRequest;
import org.gl.ceir.CeirPannelCode.Model.GenricResponse;
import org.gl.ceir.pagination.model.ConfigContentModel;
import org.gl.ceir.pagination.model.MessageContentModel;
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

import com.google.gson.Gson;

@Controller
public class SystemConfigController {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	FeignCleintImplementation feignCleintImplementation;
	
	@RequestMapping(value=
		{"/systempConfigManagement"},method={org.springframework.web.bind.annotation.
				RequestMethod.GET,org.springframework.web.bind.annotation.RequestMethod.POST}
			)
	    public ModelAndView viewConfigManagement(HttpSession session) {
		ModelAndView mv = new ModelAndView();
		 log.info(" view ConfigManagement entry point."); 
		 mv.setViewName("viewConfigManagement");
		log.info(" view ConfigManagement exit point."); 
		return mv; 
	}
	
	
	@PostMapping("/system/viewTag") 
	public @ResponseBody ConfigContentModel SystemConfigViewTag (@RequestBody FilterRequest filterRequest,HttpSession session)  {
		filterRequest.setPublicIp(session.getAttribute("publicIP").toString());
		filterRequest.setBrowser(session.getAttribute("browser").toString());
		log.info("request send to the SystemConfigViewTag api="+filterRequest);
		ConfigContentModel response= feignCleintImplementation.viewAdminFeign(filterRequest);
		log.info("response from currency api "+response);
		return response;
	}
	
	
	@PutMapping("/system/update")
	public @ResponseBody GenricResponse updateSystem (@RequestBody ConfigContentModel configContentModel,HttpSession session) {
		configContentModel.setPublicIp(session.getAttribute("publicIP").toString());
		configContentModel.setBrowser(session.getAttribute("browser").toString());
		log.info("request send update Messsage api="+configContentModel);
		GenricResponse Response = feignCleintImplementation.updateSystem(configContentModel);
		log.info("response from update Message api "+Response);
		return Response;
		
	}
	
	
	
	
	@PostMapping("exportSystemConfigData")
	@ResponseBody
	public FileExportResponse exportToExcel(@RequestBody FilterRequest filterRequest,HttpSession session)
	{
		Gson gsonObject=new Gson();
		Object response;
		Integer file = 1;	
		filterRequest.setPublicIp(session.getAttribute("publicIP").toString());
		filterRequest.setBrowser(session.getAttribute("browser").toString());
		log.info("filterRequest:::::::::"+filterRequest);
		response= feignCleintImplementation.adminConfigFeign(filterRequest, filterRequest.getPageNo(), filterRequest.getPageSize(), file);
		FileExportResponse fileExportResponse;
		Gson gson= new Gson(); 
		String apiResponse = gson.toJson(response);
		fileExportResponse = gson.fromJson(apiResponse, FileExportResponse.class);
		log.info("response from system Management Export  api="+fileExportResponse);

		return fileExportResponse;
	}		
	
}
