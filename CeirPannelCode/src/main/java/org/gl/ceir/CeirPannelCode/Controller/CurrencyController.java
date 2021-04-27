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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

@Controller
public class CurrencyController {

	@Autowired
	UserProfileFeignImpl userProfileFeignImpl;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@RequestMapping(value=
		{"/currencyManagement"},method={org.springframework.web.bind.annotation.
				RequestMethod.GET,org.springframework.web.bind.annotation.RequestMethod.POST}
			)
	    public ModelAndView viewMessageManagement(HttpSession session) {
		ModelAndView mv = new ModelAndView();
		 log.info(" view Currency Management entry point."); 
		 mv.setViewName("viewCurrencyManagement");
		log.info(" view Currency Management exit point."); 
		return mv; 
	}
	
	

	/*------------------------------------- Add Currency ------------------------------------------ */

	    @PostMapping("add-currency") 
	    public @ResponseBody GenricResponse AddCurrency (@RequestBody FilterRequest filterRequest,HttpSession session)  {
	       filterRequest.setPublicIp(session.getAttribute("publicIP").toString());
	       filterRequest.setBrowser(session.getAttribute("browser").toString());
		   log.info("request send to the add Currency api="+filterRequest);
	 	   GenricResponse response= userProfileFeignImpl.AddCurrencyFeign(filterRequest);
		   log.info("response from add Port api "+response);
		   return response;
	}
	
	
	//------------------------------------- view Currency ----------------------------------------							
	
		@PostMapping("currencyViewByID") 
		public @ResponseBody GenricResponse viewCurrency (@RequestBody FilterRequest filterRequest,HttpSession session )  {
			filterRequest.setPublicIp(session.getAttribute("publicIP").toString());
		    filterRequest.setBrowser(session.getAttribute("browser").toString());
			log.info("request send to the View currency api="+filterRequest);
			GenricResponse response= userProfileFeignImpl.viewCurrencyFeign(filterRequest);
			log.info("response from Currency api "+response);
			return response;
	}
		
		
	//------------------------------------- update Currency ----------------------------------------							
		
		@PostMapping("updateCurrency") 
		public @ResponseBody GenricResponse updatePortAddress (@RequestBody FilterRequest filterRequest,HttpSession session)  {
			filterRequest.setPublicIp(session.getAttribute("publicIP").toString());
		    filterRequest.setBrowser(session.getAttribute("browser").toString());
			log.info("request send to the Update Currency api="+filterRequest);
			GenricResponse response= userProfileFeignImpl.updateCurrencyFeign(filterRequest);
			log.info("response from update api "+response);
			return response;
	}	
	
		//------------------------------------ Export Currency controller ------------------------------------

		@PostMapping("exportCurrencyData")
		@ResponseBody
		public FileExportResponse exportToExcel(@RequestBody FilterRequest filterRequest,HttpSession session,@RequestParam(name = "source", defaultValue = "menu", required = false) String source)
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
			response= userProfileFeignImpl.viewCurrencyRequest(filterRequest, filterRequest.getPageNo(), filterRequest.getPageSize(), file,source);
			FileExportResponse fileExportResponse;
			Gson gson= new Gson(); 
			String apiResponse = gson.toJson(response);
			fileExportResponse = gson.fromJson(apiResponse, FileExportResponse.class);
			log.info("response  from  Export Currency api="+fileExportResponse);

			return fileExportResponse;
		}	
}
