package org.gl.ceir.CeirPannelCode.Controller;

import javax.servlet.http.HttpSession;

import org.gl.ceir.CeirPannelCode.Feignclient.UserProfileFeignImpl;
import org.gl.ceir.CeirPannelCode.Model.FileExportResponse;
import org.gl.ceir.CeirPannelCode.Model.FilterRequest;
import org.gl.ceir.CeirPannelCode.Model.GenricResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

@Controller
public class PortController {
	
	@Autowired
	UserProfileFeignImpl userProfileFeignImpl;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@RequestMapping(value=
		{"/portManagement"},method={org.springframework.web.bind.annotation.
				RequestMethod.GET,org.springframework.web.bind.annotation.RequestMethod.POST}
			)
	    public ModelAndView viewMessageManagement(HttpSession session) {
		ModelAndView mv = new ModelAndView();
		 log.info(" view port Management entry point."); 
		 mv.setViewName("viewPortManagement");
		log.info(" view port Management exit point."); 
		return mv; 
	}
	
	
	/*------------------------------------- Add Port ------------------------------------------ */

	    @PostMapping("add-Port") 
	    public @ResponseBody GenricResponse AddPortAddress (@RequestBody FilterRequest filterRequest,HttpSession session)  {
	       filterRequest.setPublicIp(session.getAttribute("publicIP").toString());
	       filterRequest.setBrowser(session.getAttribute("browser").toString());
		   log.info("request send to the add Port api="+filterRequest);
	 	   GenricResponse response= userProfileFeignImpl.AddPortAddressFeign(filterRequest);
		   log.info("response from add Port api "+response);
		   return response;
	}
	
	
	//------------------------------------- view Port Address ----------------------------------------							
	
		@PostMapping("portViewByID") 
		public @ResponseBody GenricResponse viewPortAddress (@RequestBody FilterRequest filterRequest,HttpSession session)  {
			filterRequest.setPublicIp(session.getAttribute("publicIP").toString());
		    filterRequest.setBrowser(session.getAttribute("browser").toString());
			log.info("request send to the View Port api="+filterRequest);
			GenricResponse response= userProfileFeignImpl.viewPortFeign(filterRequest);
			log.info("response from add View api "+response);
			return response;
	}
		
		
	//------------------------------------- update Port Address ----------------------------------------							
		
		@PostMapping("updatePortAddress") 
		public @ResponseBody GenricResponse updatePortAddress (@RequestBody FilterRequest filterRequest,HttpSession session)  {
			filterRequest.setPublicIp(session.getAttribute("publicIP").toString());
		    filterRequest.setBrowser(session.getAttribute("browser").toString());
			log.info("request send to the Update Port api="+filterRequest);
			GenricResponse response= userProfileFeignImpl.updatePortAddressFeign(filterRequest);
			log.info("response from update api "+response);
			return response;
	}	
	
	
	//------------------------------------- delete Port Address ----------------------------------------	
	
		@PostMapping ("deletePort")
		public @ResponseBody GenricResponse deletePortAddress(@RequestBody FilterRequest filterRequest,HttpSession session) {
			filterRequest.setPublicIp(session.getAttribute("publicIP").toString());
		    filterRequest.setBrowser(session.getAttribute("browser").toString());
			log.info("request send to the Delete PORT api="+filterRequest);
			GenricResponse response= userProfileFeignImpl.deletePortFeign(filterRequest);
			log.info("response after delete PORT."+response);
			return response;

	}
		
		//***************************************** Export Custom controller *********************************
		@PostMapping("exportCustom")
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
			response= userProfileFeignImpl.viewPortRequest(filterRequest,filterRequest.getPageNo(), filterRequest.getPageSize(), file);
			FileExportResponse fileExportResponse;
			Gson gson= new Gson(); 
			String apiResponse = gson.toJson(response);
			fileExportResponse = gson.fromJson(apiResponse, FileExportResponse.class);
			log.info("response  from  Custom Export  api="+fileExportResponse);

			return fileExportResponse;
		}		
}



