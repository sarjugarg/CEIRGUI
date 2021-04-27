package org.gl.ceir.CeirPannelCode.Feignclient;
import java.util.List;

import org.gl.ceir.CeirPannelCode.Model.ConsignmentModel;
import org.gl.ceir.CeirPannelCode.Model.FilterRequest;
import org.gl.ceir.CeirPannelCode.Model.GenricResponse;
import org.gl.ceir.CeirPannelCode.Model.Operator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Service
@FeignClient(url = "${feignClientPath}",value = "dsj" )
public interface FeignClientImplementation {
	 
	//View all Consignment  feign  controller
	@RequestMapping(value="/consignment/Record" ,method=RequestMethod.GET) 
	public List<ConsignmentModel> consignmentList(@RequestParam long userId) ;

	
	
	//View filter Consignment  feign  controller
		@RequestMapping(value="/filter/consignment" ,method=RequestMethod.GET) 
		public List<ConsignmentModel> consignmentFilter(FilterRequest filterrequest) ;

		
	
	
	//Add new  Consignment  feign  controller
	//@RequestMapping(value="/consignment/upload" ,method=RequestMethod.POST) 
	
	// @PostMapping(value="/consignment/upload")
	
	// @RequestLine("POST /consignment/upload")
	@PostMapping(value="/consignment/register")
    public GenricResponse addConsignment(ConsignmentModel consignment); 

	
	//***************************************************** update consignment feign ******************************************************************/ 
	@PostMapping(value="/consignment/update")
	public GenricResponse updateConsignment(ConsignmentModel consignment) ;

	

	
	
	//edit Consignment feign  controller
		@RequestMapping(value="/consignment/view" ,method=RequestMethod.GET) 
		public @ResponseBody ConsignmentModel fetchConsignmentByTxnId(@RequestParam("txnId") String txnId) ;
		
	
	//delete Consignment feign  controller
	@RequestMapping(value="/consigment/Delete" ,method=RequestMethod.DELETE) 
	public @ResponseBody ConsignmentModel deleteConsignment(@RequestParam("txnId") String txnId) ;
	
	
	
		
	
	//download file(Error or Uploaded file) feign  controller
	@RequestMapping(value="/Download/uploadFile" ,method=RequestMethod.GET) 
		public @ResponseBody String downloadFile(@RequestParam("txnId") String txnId,@RequestParam("fileType") String fileType,@RequestParam("fileName") String fileName);

	
	
	
	//download file(Error or Uploaded file) feign  controller
		@RequestMapping(value="/stoke/Download/SampleFile" ,method=RequestMethod.GET) 
			public @ResponseBody String downloadSampleFile(@RequestParam("samplFileType") String fileType);

		@GetMapping("/system-config-list/{tag}")    
		public List<Operator> operatorList(@PathVariable("tag")String tag);       
		
		
	
	    	     
	/*
	 * @PostMapping("/MobileOperators/") public void saveOperator(@RequestBody
	 * Operator operator);
	 */ 

	/*
	 * @PutMapping("/MobileOperators/{id}") void
	 * editOperatorById(@PathVariable("id") int id,@RequestBody Operator op);
	 * 
	 * @DeleteMapping("/MobileOperators/{id}") void
	 * deleteOperatorById(@PathVariable("id") int id);
	 * 
	 * @GetMapping("/MediationSource/") public List<Mediation> getMediationData();
	 * 
	 * @PostMapping("/MediationSource/") public void addMediationData(@RequestBody
	 * Mediation mediation);
	 * 
	 * @PutMapping("/MediationSource/{id}") public void editMediation(@PathVariable
	 * int id,Mediation mediation);
	 * 
	 * @DeleteMapping("/MediationSource/{id}") public void
	 * deleteMediation(@PathVariable("id") int id);
	 */
}

