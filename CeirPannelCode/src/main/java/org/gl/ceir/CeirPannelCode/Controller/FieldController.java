package org.gl.ceir.CeirPannelCode.Controller;

import javax.servlet.http.HttpSession;

import org.gl.ceir.CeirPannelCode.Feignclient.FeignCleintImplementation;
import org.gl.ceir.CeirPannelCode.Model.ConsignmentModel;
import org.gl.ceir.CeirPannelCode.Model.FileExportResponse;
import org.gl.ceir.CeirPannelCode.Model.FilterRequest;
import org.gl.ceir.CeirPannelCode.Model.GenricResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

@Controller
public class FieldController {

	private final Logger log = LoggerFactory.getLogger(getClass());

	
	@Autowired
	FeignCleintImplementation feignCleintImplementation;

	@GetMapping("fieldManagement")
	public ModelAndView pageView(@RequestParam(name="via", required = false) String via,
								@RequestParam(name="tagId", required = false) String tagId,
								@RequestParam(name="action", required = false) String action,HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		if("other".equals(via)) {
			modelAndView.setViewName("fieldManagement");
			modelAndView.addObject("action", action);
		}
		else {
			modelAndView.setViewName("tagForm");
		}
		
		
		return modelAndView;
	}
	
	
	
	/*------------------------------------- Add Fields ------------------------------------------ */
	
	@PostMapping("add-Field") 
	public @ResponseBody GenricResponse AddfieldManagement (@RequestBody FilterRequest filterRequest, HttpSession session)  {
		filterRequest.setPublicIp(session.getAttribute("publicIP").toString());
		filterRequest.setBrowser(session.getAttribute("browser").toString());
		log.info("request send to the add Field api="+filterRequest);
		GenricResponse response= feignCleintImplementation.AddfieldManagementFeign(filterRequest);
		log.info("response from add Field api "+response);
		return response;
	}
					
	
	//------------------------------------- viewfieldManagement ----------------------------------------							
	
	@PostMapping("fieldViewByID") 
	public @ResponseBody GenricResponse viewfieldManagement (@RequestBody FilterRequest filterRequest, HttpSession session)  {
		filterRequest.setPublicIp(session.getAttribute("publicIP").toString());
		filterRequest.setBrowser(session.getAttribute("browser").toString());
		log.info("request send to the View Field api="+filterRequest);
		GenricResponse response= feignCleintImplementation.viewfieldManagementFeign(filterRequest);
		log.info("response from add View api "+response);
		return response;
	}
	
	
	//------------------------------------- update fieldManagement ----------------------------------------							
	
	@PutMapping("updateSystemTags") 
	public @ResponseBody GenricResponse updatefieldManagement (@RequestBody FilterRequest filterRequest, HttpSession session)  {
		filterRequest.setPublicIp(session.getAttribute("publicIP").toString());
		filterRequest.setBrowser(session.getAttribute("browser").toString());
		log.info("request send to the update Field api="+filterRequest);
		GenricResponse response= feignCleintImplementation.updatefieldManagementFeign(filterRequest);
		log.info("response from update api "+response);
		return response;
	}
	
	
	//------------------------------------- delete fieldManagement ----------------------------------------	
	
	@DeleteMapping ("deleteField")
	public @ResponseBody GenricResponse deleteFieldManagement(@RequestBody FilterRequest filterRequest, HttpSession session) {
		filterRequest.setPublicIp(session.getAttribute("publicIP").toString());
		filterRequest.setBrowser(session.getAttribute("browser").toString());
		log.info("request send to the Delete Field api="+filterRequest);
		GenricResponse response=feignCleintImplementation.deleteFieldFeign(filterRequest);
		log.info("response after delete consignment."+response);
		return response;

	}
	

	//------------------------------------ Export Field controller ------------------------------------

	@PostMapping("exportFieldData")
	@ResponseBody
	public FileExportResponse exportToExcel(@RequestBody FilterRequest filterRequest,HttpSession session)
	{
		Gson gsonObject=new Gson();
		Object response;
		Integer file = 1;	
		String userType=(String) session.getAttribute("usertype");
		Integer usertypeId=(int) session.getAttribute("usertypeId");
		filterRequest.setUserType(userType);
		filterRequest.setUserTypeId(usertypeId);
		filterRequest.setPublicIp(session.getAttribute("publicIP").toString());
		filterRequest.setBrowser(session.getAttribute("browser").toString());
		log.info("filterRequest:::::::::"+filterRequest);
		response= feignCleintImplementation.fieldManagementFeign(filterRequest, filterRequest.getPageNo(), filterRequest.getPageSize(), file);
		FileExportResponse fileExportResponse;
		Gson gson= new Gson(); 
		String apiResponse = gson.toJson(response);
		fileExportResponse = gson.fromJson(apiResponse, FileExportResponse.class);
		log.info("response  from  Export Field api="+fileExportResponse);

		return fileExportResponse;
	}	
}
