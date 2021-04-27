package org.gl.ceir.CeirPannelCode.Feignclient;
import java.util.List;

import org.gl.ceir.CeirPannelCode.Model.ActionModel;
import org.gl.ceir.CeirPannelCode.Model.AddMoreFileModel;
import org.gl.ceir.CeirPannelCode.Model.AddressModel;
import org.gl.ceir.CeirPannelCode.Model.AddressResponse;
import org.gl.ceir.CeirPannelCode.Model.AllRequest;
import org.gl.ceir.CeirPannelCode.Model.CCPolicyBreachRequest;
import org.gl.ceir.CeirPannelCode.Model.ConsignmentModel;
import org.gl.ceir.CeirPannelCode.Model.ConsignmentUpdateRequest;
import org.gl.ceir.CeirPannelCode.Model.CustomerCareByTxnId;
import org.gl.ceir.CeirPannelCode.Model.CustomerCareRequest;
import org.gl.ceir.CeirPannelCode.Model.Dropdown;
import org.gl.ceir.CeirPannelCode.Model.FileExportResponse;
import org.gl.ceir.CeirPannelCode.Model.FilterRequest;
import org.gl.ceir.CeirPannelCode.Model.GenricResponse;
import org.gl.ceir.CeirPannelCode.Model.GrievanceDropdown;
import org.gl.ceir.CeirPannelCode.Model.NewRule;
import org.gl.ceir.CeirPannelCode.Model.RuleListContent;
import org.gl.ceir.CeirPannelCode.Model.RuleNameModel;
import org.gl.ceir.CeirPannelCode.Model.StockUploadModel;
import org.gl.ceir.CeirPannelCode.Model.StolenRecoveryModel;
import org.gl.ceir.CeirPannelCode.Model.Tag;
import org.gl.ceir.pagination.model.AuditContentModel;
import org.gl.ceir.pagination.model.ConfigContentModel;
import org.gl.ceir.pagination.model.MessageContentModel;
import org.gl.ceir.pagination.model.PolicyConfigContent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Service
@FeignClient(url = "${feignClientPath}",value = "dsj" )
public interface FeignCleintImplementation {


	//View all Consignment  feign  controller
	@RequestMapping(value="/consignment/Record" ,method=RequestMethod.GET) 
	public List<ConsignmentModel> consignmentList(@RequestParam long userId) ;



	//View filter Consignment  feign  controller
	@RequestMapping(value="/v2/filter/consignment" ,method=RequestMethod.GET) 
	public Object consignmentFilter(@RequestBody FilterRequest filterRequest,
			@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value = "file", defaultValue = "0") Integer file,
			@RequestParam(name="source",defaultValue = "menu",required = false) String source) ;




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
	public @ResponseBody ConsignmentModel fetchConsignmentByTxnId(FilterRequest request) ;


	//delete Consignment feign  controller
	@RequestMapping(value="/consigment/delete" ,method=RequestMethod.DELETE) 
	public @ResponseBody GenricResponse deleteConsignment(ConsignmentModel consignmentModel,@RequestParam("userType") String userType) ;

	//accept reject Consignment feign  controller
	@RequestMapping(value="/update/consigmentStatus" ,method=RequestMethod.PUT) 
	public @ResponseBody GenricResponse updateConsignmentStatus(ConsignmentUpdateRequest consignmentUpdateRequest) ;





	//download file(Error or Uploaded file) feign  controller
	@RequestMapping(value="/Download/uploadFile" ,method=RequestMethod.POST) 
	public @ResponseBody FileExportResponse downloadFile(@RequestParam("txnId") String txnId,@RequestParam("fileType") String fileType,@RequestParam("fileName") String fileName,@RequestParam(name="tag",required = false) String tag,@RequestBody AllRequest allrequest);




	//download file(Error or Uploaded file) feign  controller
	@RequestMapping(value="/Download/SampleFile" ,method=RequestMethod.POST) 
	public @ResponseBody FileExportResponse downloadSampleFile(@RequestParam("featureId") int featureId,@RequestBody AllRequest allrequest);


	/// **************************************** Stock Api integration ******************************************************************************************


	// @RequestLine("POST /stock/upload")
	@PostMapping(value="/Stock/upload")
	public GenricResponse uploadStock(StockUploadModel stockUploadModel); 

	/// **************************************** Stock Api integration ******************************************************************************************


	//delete stock feign  controller
	@RequestMapping(value="/stock/delete" ,method=RequestMethod.POST) 
	public @ResponseBody GenricResponse deleteStock(StockUploadModel stockUploadModel,@RequestParam("userType") String userType) ;


	//edit stock feign  controller
	@RequestMapping(value="/stock/view" ,method=RequestMethod.POST) 
	public @ResponseBody StockUploadModel fetchUploadedStockByTxnId(FilterRequest filterRequest) ;




	//***************************************************** update uploaded Stock feign ******************************************************************/ 
	@PostMapping(value="/Stock/update")
	public GenricResponse updateStock(StockUploadModel stockUploadModel) ;



	@RequestMapping(value="filter/stakeholder/record" ,method=RequestMethod.POST) 
	public Object stolenFilter(@RequestBody FilterRequest filterRequest,
			@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value = "file", defaultValue = "0") Integer file,
			@RequestParam(name="source",defaultValue = "menu",required = false) String source);
	


	
	//accept reject stock feign  controller
		@RequestMapping(value="/accept-reject/stock" ,method=RequestMethod.PUT) 
		public @ResponseBody GenricResponse acceptRejectStock(ConsignmentUpdateRequest consignmentUpdateRequest) ;

	//****************************************************                              ***************************************************************************		
	//**************************************************** Stolen Recovery integration  **************************************************************************
	//****************************************************                              **************************************************************************			


	@RequestMapping(value="/stock/record" ,method=RequestMethod.POST) 
	public Object stockFilter(@RequestBody FilterRequest filterRequest,
			@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value = "file", defaultValue = "0") Integer file, 
	        @RequestParam(value = "source",defaultValue = "menu") String  source);



	//********************************************  upload multiple Stolen and Recovery ***************************************************************88
	@RequestMapping(value="/stakeholder/uploadMultiple/StolenAndRecovery" ,method=RequestMethod.POST) 
	public GenricResponse multipleStolen(@RequestBody List<StolenRecoveryModel> request) ;






	//**************************************************************** file Stolen type ***************************************************************************************************		


	@RequestMapping(value="/stakeholder/Stolen" ,method=RequestMethod.POST) 
	public GenricResponse fileStolen(@RequestBody StolenRecoveryModel request) ;




	//**************************************************************** file Recovery type ***************************************************************************************************		


	@RequestMapping(value="/stakeholder/Recovery" ,method=RequestMethod.POST) 
	public GenricResponse fileRecovery(@RequestBody StolenRecoveryModel request) ;


	//delete stolen recovery feign  controller
	@RequestMapping(value="/stakeholder/Delete" ,method=RequestMethod.DELETE) 
	public @ResponseBody GenricResponse deleteStolenRecovery(FilterRequest stolenRecoveryModel) ;
	/************* DROPDOWN *****************/

	@RequestMapping(value="/state-mgmt/{featureId}/{userTypeId}" ,method=RequestMethod.GET) 
	public List<Dropdown> consignmentStatusList(@PathVariable("featureId") Integer featureId,@PathVariable("userTypeId") Integer userTypeId);


	@RequestMapping(value="system-config-list/{tag}" ,method=RequestMethod.GET) 
	public List<Dropdown> taxPaidStatusList(@PathVariable("tag") String tag);

	//**************************************************************** file Stolen type ***************************************************************************************************		


	@RequestMapping(value="/stakeholder/update" ,method=RequestMethod.PUT) 
	public GenricResponse updateFileStolen(@RequestBody StolenRecoveryModel request) ;




	
	
	
	
	
	//Dashboard/Datatable Feign
		@RequestMapping(value="/v2/history/Notification" ,method=RequestMethod.GET) 
		public Object dashBoardNotification(@RequestBody FilterRequest filterRequest,
		@RequestParam Integer pageNo,
		@RequestParam Integer pageSize) ;	
		
		
		
		
		

		//***************************************************Admin Registration as Type Dropdown********************************


		@RequestMapping(value="/system-config-list/by-tag-and-usertype/{tagId}/{userTypeId}" ,method=RequestMethod.GET) 
		public List<Dropdown> asTypeList(@PathVariable("tagId") String tag, @PathVariable("userTypeId") Integer userTypeId);


		@PostMapping("/system/viewTag")    
		public Dropdown dataByTag(Tag tag);       
		

		// fetch block/Unblock(bulk) devices.
		
				//edit stock feign  controller
				@RequestMapping(value="/stolen-and-recovery/by-txnId" ,method=RequestMethod.POST) 
				public @ResponseBody Object fetchBulkDeviceByTxnId(StolenRecoveryModel stolenRecoveryModel) ;

				
				//***************************************************Admin System message Management Feign********************************
				
				@RequestMapping(value="/filter/message-configuration" ,method=RequestMethod.POST) 
				public Object adminMessageFeign(@RequestBody FilterRequest filterRequest,
						@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
						@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
						@RequestParam(value = "file", defaultValue = "0") Integer file) ;

				
				//***************************************************Admin System Config Management Feign********************************
				
				@RequestMapping(value="/filter/system-configuration" ,method=RequestMethod.POST) 
				public Object adminConfigFeign(@RequestBody FilterRequest filterRequest,
						@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
						@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
						@RequestParam(value = "file", defaultValue = "0") Integer file) ;
				
		

			
//***************************************************Admin System Config Management Feign********************************

@RequestMapping(value="/filter/policy-configuration" ,method=RequestMethod.POST) 
public Object policyManagementFeign(@RequestBody FilterRequest filterRequest,
		@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
		@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
		@RequestParam(value = "file", defaultValue = "0") Integer file) ;

	

//************************************************ view Policy Config Feign *****************************************

@PostMapping("/policy/viewTag")
public @ResponseBody PolicyConfigContent viewPolicyConfigFeign(FilterRequest filterRequest);



//************************************************ view icon Message Management Feign *****************************************

@PostMapping("/message/viewTag")
public @ResponseBody MessageContentModel viewMessageFeign(FilterRequest filterRequest);


@PostMapping("/system/viewTag")
public @ResponseBody ConfigContentModel viewAdminFeign(FilterRequest filterRequest);


		@RequestMapping(value="/system-config-list/by-tag-and-featureid/{tagId}/{featureId}" ,method=RequestMethod.GET) 
		public List<Dropdown> modeType(@PathVariable("tagId") String tagId, @PathVariable("featureId") Integer featureId);
		
		//******************************* Block Unblock Approve/Reject Devices Feign ********************************
		
				@PutMapping("/accept-reject/stolen-recovery-block-unblock")
				public @ResponseBody GenricResponse approveRejectFeign(FilterRequest FilterRequest);
				
				
				//************************************ Table Actions Feign  *************************************************
				
				@RequestMapping(value="/table-actions/{featureId}/{userTypeId}" ,method=RequestMethod.GET) 
				public List<ActionModel> tableActionFeign(@PathVariable("featureId") Integer featureId,@PathVariable("userTypeId") Integer userTypeId);
				
		//************************************ Policy update Feign  *************************************************
				
				@PutMapping(value="/policy/update")
				public @ResponseBody PolicyConfigContent updatePolicy(PolicyConfigContent policyConfigContent);
				
				
				//************************************ Message update Feign  *************************************************
				
				@PutMapping(value="/message/update")
				public @ResponseBody MessageContentModel updateMessages(MessageContentModel messageContentModel);
				
				//************************************ System update Feign  *************************************************
				
				@PutMapping(value="/system/update")
				public @ResponseBody GenricResponse updateSystem(@RequestBody ConfigContentModel configContentModel);
				//***************************************************Audit Management Feign********************************

				@RequestMapping(value="/filter/audit-trail" ,method=RequestMethod.POST) 
				public Object auditManagementFeign(@RequestBody FilterRequest filterRequest,
						@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
						@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
						@RequestParam(value = "file", defaultValue = "0") Integer file) ;

			//************************************************ view Audit Management Feign *************************************

				@RequestMapping(value="/audit-trail/{id}" ,method=RequestMethod.GET) 
				public AuditContentModel viewAuditManagementFeign(@PathVariable("id") Integer id);

//************************************ Message update Feign  *************************************************
				
				@PostMapping(value="/checkDevice")
				public @ResponseBody GenricResponse viewDetails(FilterRequest filterRequest);
					
				//************************************ manage User Feign  *************************************************

				@RequestMapping(value="/filter/end-users" ,method=RequestMethod.POST) 
				public Object manageUserFeign(@RequestBody FilterRequest filterRequest,
						@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
						@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
						@RequestParam(value = "file", defaultValue = "0") Integer file);
		
				@PostMapping("/get/tags-mapping")
				public @ResponseBody List<GrievanceDropdown> catagoryDropdownListFeign(FilterRequest filterRequest);	
				
		
				//download file(Error or Uploaded file) feign  controller
				@RequestMapping(value="/Download/manuals" ,method=RequestMethod.POST) 
				public @ResponseBody FileExportResponse manualDownloadSampleFile(@RequestBody AllRequest auditRequest);
				
//******************************* Tag Updated DropDown in Field ****************************************
				
				@PostMapping("/projection/tags/system-config-list")
				public @ResponseBody GenricResponse getAllTagsDropdowntFeign(FilterRequest filterRequest);	
				
				
				//***************************************************Field Management Feign**********************************

				@RequestMapping(value= "/filter/system-config-list" , method=RequestMethod.POST) 
				public Object fieldManagementFeign(@RequestBody FilterRequest filterRequest,
						@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
						@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
						@RequestParam(value = "file", defaultValue = "0") Integer file);

				
				
				//***************************************************Add Field Management Feign********************************

				@RequestMapping(value= "/save/system-config-list" , method=RequestMethod.POST) 
				public GenricResponse AddfieldManagementFeign(@RequestBody FilterRequest filterRequest);
						

				//***************************************************View Field Management Feign********************************

				@RequestMapping(value= "/get/system-config-list" , method=RequestMethod.POST) 
				public GenricResponse viewfieldManagementFeign(@RequestBody FilterRequest filterRequest);
				
				
				//***************************************************update Field Management Feign********************************

				@RequestMapping(value= "/system-config-list" , method=RequestMethod.PUT) 
				public GenricResponse updatefieldManagementFeign(@RequestBody FilterRequest filterRequest);
				
				
				//***************************************************Delete Field Management Feign********************************
				
				@RequestMapping(value="/tags/system-config-list" ,method=RequestMethod.DELETE) 
				public @ResponseBody GenricResponse deleteFieldFeign(@RequestBody FilterRequest filterRequest);
				
				@PostMapping("/system/viewTag")
				public @ResponseBody AddMoreFileModel addMoreBuutonCount(AddMoreFileModel addMoreCount);	
				
				
				//************************************************ view customer Care Feign *****************************************

				@PostMapping("/customer-care/record")
				public @ResponseBody GenricResponse viewcustomerDetialsfeign(
						@RequestParam(name = "listType", required = false) String listType,
						@RequestBody CustomerCareRequest customerCareRequest);

				

				//***************************************************CC Policy Notification Feign********************************

				@RequestMapping(value="/policy-breach-notification" ,method=RequestMethod.POST) 
				public Object ccdashBoardNotification(@RequestBody CCPolicyBreachRequest filterRequest,
						@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
						@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize);

				
				@PostMapping("/customer-care/by-txn-id")
				public @ResponseBody GenricResponse customerCareViaTxnId( @RequestBody CustomerCareByTxnId customerCareDeviceState);
				/* Rule List Feign */
				@RequestMapping(value="/filter/rule-engine" ,method=RequestMethod.POST) 
				public Object ruleListFeign(@RequestBody FilterRequest filterRequest,
						@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
						@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
						@RequestParam(name = "file", defaultValue = "0" ,required = false) Integer file);
				
				
				
				@RequestMapping(value="rule-engine/{id}" ,method=RequestMethod.GET) 
				public RuleListContent fetchData(@PathVariable("id") Integer id);
				
				@RequestMapping(value="rule-engine" ,method=RequestMethod.PUT) 
				public GenricResponse update(@RequestBody RuleListContent ruleListContent);
		
				/* Rule Feature Mapping  Feign */
				@RequestMapping(value="/filter/rule-engine-mapping" ,method=RequestMethod.POST) 
				public Object ruleFeatureMappingListFeign(@RequestBody FilterRequest filterRequest,
						@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
						@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
						@RequestParam(name = "file", defaultValue = "0" ,required = false) Integer file);
				
					@PostMapping("/rule-engine-mapping")
					public GenricResponse save(@RequestBody NewRule newRule);
					
					@RequestMapping(value="/rule-engine-mapping" ,method=RequestMethod.PUT) 
					public GenricResponse updateRuleFeatureMapping(@RequestBody NewRule newRule);
					
					@GetMapping("/all/rule-engine")
					public List<RuleNameModel> getList();
					
					@GetMapping("/rule-engine-mapping/{id}")
					public NewRule getObjectByID(@PathVariable("id") Integer id);
					
					@DeleteMapping(value="rule-engine-mapping") 
					public @ResponseBody GenricResponse delete(NewRule newRule) ;
					
					
					//***************************************************Admin Pending TAC List Feign********************************

					@RequestMapping(value="/filter/pending-tac-approveddb" ,method=RequestMethod.POST) 
					public Object pendingTACFeign(@RequestBody FilterRequest filterRequest,
							@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
							@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
							@RequestParam(value = "file", defaultValue = "0") Integer file,
							@RequestParam(value = "source",defaultValue = "menu") String source) ;	
					
					
					//****************************************Pending TAC List Delete Feign********************************
					
					@RequestMapping(value="/pending-tac-approved" ,method=RequestMethod.DELETE) 
					public @ResponseBody GenricResponse deletePendingTac(@RequestBody FilterRequest filterRequest);
					
					@RequestMapping(value="/visa/view" ,method=RequestMethod.POST) 
					public Object viewVisaRequest(@RequestBody FilterRequest filterRequest,
					@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
					@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
					@RequestParam(value = "file", defaultValue = "0") Integer file,
					@RequestParam(value = "source", defaultValue = "menu") String source);
					

							
}					





