package org.gl.ceir.CeirPannelCode.Feignclient;

import java.util.List;

import org.gl.ceir.CeirPannelCode.Model.Dropdown;
import org.gl.ceir.CeirPannelCode.Model.FileCopyToOtherServer;
import org.gl.ceir.CeirPannelCode.Model.FilterRequest;
import org.gl.ceir.CeirPannelCode.Model.GenricResponse;
import org.gl.ceir.CeirPannelCode.Model.GrievanceModel;
import org.gl.ceir.CeirPannelCode.Model.SingleImeiDetailsModel;
import org.gl.ceir.CeirPannelCode.Model.TRCRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
@Component
@Service
@FeignClient(url = "${dashBoardfeignClientPath}",value = "grievance" )


public interface GrievanceFeignClient {


	//****************************************************************grievance api starts from here ***************************************************************************************************		
	//View filter grievance  feign  controller
	@RequestMapping(value="/v2/filter/grievance" ,method=RequestMethod.GET) 
	public Object grievanceFilter(@RequestBody FilterRequest filterRequest,
			@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value = "file", defaultValue = "0") Integer file,
			@RequestParam(name="source",defaultValue = "menu",required = false) String source) ;


	// ******************************************** save 	grievance api ********************************************************************************
	@RequestMapping(value="/grievance/save" ,method=RequestMethod.POST) 
	public GenricResponse saveGrievance(@RequestBody GrievanceModel greGrievanceModel) ;



	// ******************************************** view 	grievance api ********************************************************************************
	@RequestMapping(value="/grievance/msg" ,method=RequestMethod.GET) 
	public List<GrievanceModel> viewGrievance(@RequestParam("grievanceId") String  grievanceId,@RequestParam("userId") Integer userId,@RequestParam("recordLimit") Integer recordLimit,
			@RequestParam(name="publicIp",required = false ) String publicIp,
			@RequestParam(name="browser",required = false ) String browser,
			@RequestParam(name="userType",required = false ) String userType,
			@RequestParam(name="featureId",required = false ) Integer featureId) ;

	// ******************************************** save 	grievance api ********************************************************************************
	@RequestMapping(value="/grievance/saveMessage" ,method=RequestMethod.POST) 
	public GenricResponse saveGrievanceMessage(@RequestBody GrievanceModel greGrievanceModel) ;



	//***************************************************TRC********************************

	@PostMapping("TypeApproved/view")
	public Object viewTRC(@RequestBody TRCRequest filterRequest,
			@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,@RequestParam(value = "file", defaultValue = "0") Integer file);	
	
	
	//***************************************************View Operator********************************
	
	@RequestMapping(value="/filedump/filter" ,method=RequestMethod.GET) 
	public Object viewOperatorFeign(@RequestBody FilterRequest filterRequest,
			@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value = "file", defaultValue = "0") Integer file) ;
	
	
	
	
	@RequestMapping(value="/filedump/downloadFile" ,method=RequestMethod.GET) 
	public @ResponseBody Object downloadOperatorFeign(@RequestParam("fileName") String fileName);
	



	// ******************************************** single imei block device ********************************************************************************
	@RequestMapping(value="/stakeholder/uploadSingle/block" ,method=RequestMethod.POST) 
	public GenricResponse singleImeiBlockDevices(@RequestBody SingleImeiDetailsModel singleImeiDetailsModel) ;

	// ******************************************** single imei block device ********************************************************************************
		@RequestMapping(value="/stakeholder/updateSingle/blockUnblock" ,method=RequestMethod.POST) 
		public GenricResponse updateSingleImeiBlockDevices(@RequestBody SingleImeiDetailsModel singleImeiDetailsModel) ;

	

	//***************************************************Admin Registration as Type Dropdown********************************


	@RequestMapping(value="/stakeholder/view/singleImei" ,method=RequestMethod.POST) 
	public List<SingleImeiDetailsModel> fetchSingleDevicebyTxnId(@RequestParam("txnId") String txnId );


	// ******************************************** save 	grievance api ********************************************************************************
		@RequestMapping(value="/grievance/endUserSave" ,method=RequestMethod.POST) 
		public GenricResponse saveEndUserGrievance(@RequestBody GrievanceModel greGrievanceModel) ;

		@RequestMapping(value="/uploadedFile/save" ,method=RequestMethod.POST) 
		public GenricResponse saveUploadedFileOnANotherServer(@RequestBody FileCopyToOtherServer fileRequest) ;
		
		
}
