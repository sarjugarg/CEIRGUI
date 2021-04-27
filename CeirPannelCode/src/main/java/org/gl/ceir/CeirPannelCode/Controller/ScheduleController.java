package org.gl.ceir.CeirPannelCode.Controller;

import javax.servlet.http.HttpSession;

import org.gl.ceir.CeirPannelCode.Feignclient.GsmaFeignClient;
import org.gl.ceir.CeirPannelCode.Model.FileExportResponse;
import org.gl.ceir.CeirPannelCode.Model.FilterRequest;
import org.gl.ceir.CeirPannelCode.Model.GenricResponse;
import org.gl.ceir.CeirPannelCode.Model.NewRule;
import org.gl.ceir.CeirPannelCode.Model.NewSystemUser;
import org.gl.ceir.graph.model.ScheduleRequest;
import org.gl.ceir.pagination.model.AuditContentModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

@Controller
public class ScheduleController {
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	GsmaFeignClient gsmaFeignClient;

	@RequestMapping(value={"/scheduleManagement"},method={org.springframework.web.bind.annotation.
			RequestMethod.GET,org.springframework.web.bind.annotation.RequestMethod.POST}
			)
	public ModelAndView viewManageType(HttpSession session,@RequestParam(name="txnID",required = false) String txnID) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("viewScheduleManagement");
		return mv; 
	}
	
	/*------------------------------------- Add Schedule ------------------------------------------ */

    @PostMapping("addSchedule") 
    public @ResponseBody ScheduleRequest saveSchedule (@RequestBody ScheduleRequest scheduleRequest)  {
	   log.info("request send to the add Schedule api="+scheduleRequest);
	   ScheduleRequest response= gsmaFeignClient.AddScheduleFeign(scheduleRequest);
	   log.info("response from add Schedule api "+response);
	   return response;
    }
    
    //------------------------------------- view Schedule ----------------------------------------							
	
  	
  	@GetMapping("viewBy/{id}")
	public ResponseEntity<?> viewSchedule(@PathVariable("id") Integer id) {
  		ScheduleRequest scheduleRequest = gsmaFeignClient.getScheduleByID(id);
		return new ResponseEntity<>(scheduleRequest, HttpStatus.OK);
	}
  	
  	
  	//------------------------------------- Update Schedule ----------------------------------------							
	
  	
	
  	@PutMapping("updateSchedule") 
  	public @ResponseBody ScheduleRequest updateRecord(@RequestBody ScheduleRequest scheduleRequest) {
  		log.info("request::::::" + scheduleRequest); 
  		ScheduleRequest response = gsmaFeignClient.updateScheduleByID(scheduleRequest);
  		log.info(" response from update Schedule api=" + response); 
  		return response;
  	}
	 
  	
  //------------------------------------- Delete Address ----------------------------------------	
	
  		@DeleteMapping ("deleteSchedule/{id}")
  		public @ResponseBody GenricResponse deleteReportSchedule(@PathVariable("id") Integer id) {
  			log.info("request send to the deleteSchedule api="+id);
  			GenricResponse response=gsmaFeignClient.deleteScheduleFeign(id);
  			log.info("response after Delete Schedule api."+response);
  			return response;

  		}
  		
  //------------------------------------- Export Schedule controller -----------------------------
  		
  		@PostMapping("exportSchedule")
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
  			log.info("filterRequest:::::::::"+filterRequest);
  			response= gsmaFeignClient.viewAllScheduleReport(filterRequest, filterRequest.getPageNo(), filterRequest.getPageSize(), file);
  			FileExportResponse fileExportResponse;
  			Gson gson= new Gson(); 
  			String apiResponse = gson.toJson(response);
  			fileExportResponse = gson.fromJson(apiResponse, FileExportResponse.class);
  			log.info("response  from  Schedule Export  api="+fileExportResponse);

  			return fileExportResponse;
  		}	
}
