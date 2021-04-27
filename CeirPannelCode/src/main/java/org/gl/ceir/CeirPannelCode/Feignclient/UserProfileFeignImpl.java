package org.gl.ceir.CeirPannelCode.Feignclient;

import java.util.List;

import org.gl.ceir.CeirPannelCode.Model.AlertRequest;
import org.gl.ceir.CeirPannelCode.Model.FilterRequest;
import org.gl.ceir.CeirPannelCode.Model.GenricResponse;
import org.gl.ceir.CeirPannelCode.Model.NewSystemUser;
import org.gl.ceir.CeirPannelCode.Model.Password;
import org.gl.ceir.CeirPannelCode.Model.PaymentRequest;
import org.gl.ceir.CeirPannelCode.Model.Registration;
import org.gl.ceir.CeirPannelCode.Model.SLAfilterRequest;
import org.gl.ceir.CeirPannelCode.Model.UserHeader;
import org.gl.ceir.CeirPannelCode.Model.UserStatus;
import org.gl.ceir.CeirPannelCode.Response.UpdateProfileResponse;
import org.gl.ceir.CeirPannelCode.Util.HttpResponse;
import org.gl.ceir.pagination.model.UserManagementContent;
import org.gl.ceir.pagination.model.UserfeatureContent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
@Component
@Service
@FeignClient(url="${apiUrl1}",value = "profileUrls")
public interface UserProfileFeignImpl {
	
	@PostMapping("/userProfile/changePassword")
	public HttpResponse changePassword(Password password);
	
	@PostMapping("/userProfile/updateExpirePassword")
	public HttpResponse updateExpirePassword(Password password);
	

	@PostMapping("/userProfile/updateUserStatus")
	public HttpResponse updateUserStatus(UserStatus userStatus);


	@PostMapping("/userProfile/editProfile/{userid}") 
    public Registration editUserProfile(@PathVariable("userid") Integer userid , @RequestBody UserHeader userHeader);
	
	@PostMapping("/userProfile/updateProfile") 
    public UpdateProfileResponse updateUserProfile(Registration registration);
	
	//****************************************************************Admin Registration api starts from here ***************************************************************************************************	
	//View admin registration feign controller
	@RequestMapping(value="/userProfile/record" ,method=RequestMethod.POST) 
	public Object registrationRequest(@RequestBody FilterRequest filterRequest,
	@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
	@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
	@RequestParam(value = "file", defaultValue = "0") Integer file,
	@RequestParam(name="source",defaultValue = "menu",required = false) String source
	) ;

	
	@PostMapping("/userProfile/adminApproval")
	public HttpResponse adminUserApproval(UserStatus userStatus);
	
	@PostMapping("/userProfile/viewProfile/{id}/{userId}")
	public Registration ViewAdminUser(@PathVariable("id") long id, @PathVariable("userId") Integer userId,
			@RequestParam(name="publicIp", required = false) String publicIp,
			@RequestParam(name="browser", required = false) String browser);
	
	
	@RequestMapping(value="/userProfile/searchAssignee" ,method=RequestMethod.POST)
	public Object asigneeDetailsFeign(@RequestBody FilterRequest filterRequest,
	@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
	@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
	@RequestParam(value = "file", defaultValue = "0") Integer file
	) ;
	
	@PostMapping("/userProfile/changeUserStatus")
	public HttpResponse changeUserStatusFeign(UserStatus userStatus);
	
	
	/*-------------------------- view Port Feign ------------------------------*/
	
	@RequestMapping(value="/portAddress/view" ,method=RequestMethod.POST) 
	public Object viewPortRequest(@RequestBody FilterRequest filterRequest,
	@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
	@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
	@RequestParam(value = "file", defaultValue = "0") Integer file);
	
	
	
	//***************************************************Add Port Address Feign********************************

	@RequestMapping(value= "/portAddress/save" , method=RequestMethod.POST) 
	public GenricResponse AddPortAddressFeign(@RequestBody FilterRequest filterRequest);
	
	
	
	//***************************************************View Port Management Feign********************************
	
	@RequestMapping(value="/portAddress/viewDataById" ,method=RequestMethod.POST) 
	public @ResponseBody GenricResponse viewPortFeign(@RequestBody FilterRequest filterRequest);
	
	
	
	//***************************************************Update Port Address Feign********************************

	@RequestMapping(value= "/portAddress/update" , method=RequestMethod.POST) 
	public GenricResponse updatePortAddressFeign(@RequestBody FilterRequest filterRequest);
	
	//***************************************************Delete Port Management Feign********************************
	
	@RequestMapping(value="/portAddress/delete" ,method=RequestMethod.POST) 
	public @ResponseBody GenricResponse deletePortFeign(@RequestBody FilterRequest filterRequest);
	
	
	/*-------------------------- view all Currency Feign ------------------------------*/
	
	@RequestMapping(value="/currency/view" ,method=RequestMethod.POST) 
	public Object viewCurrencyRequest(@RequestBody FilterRequest filterRequest,
	@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
	@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
	@RequestParam(value = "file", defaultValue = "0") Integer file,
	@RequestParam(name = "source", defaultValue = "menu", required = false) String source);
	
	
	
	//***************************************************Add Currency Feign********************************

		@RequestMapping(value= "/currency/save" , method=RequestMethod.POST) 
		public GenricResponse AddCurrencyFeign(@RequestBody FilterRequest filterRequest);
		
		
		
		//***************************************************View Currency Feign********************************
		
		@RequestMapping(value="/currency/viewById" ,method=RequestMethod.POST) 
		public @ResponseBody GenricResponse viewCurrencyFeign(@RequestBody FilterRequest filterRequest);
		
		
		
		//***************************************************Update Currency Feign********************************

		@RequestMapping(value= "/currency/update" , method=RequestMethod.POST) 
		public GenricResponse updateCurrencyFeign(@RequestBody FilterRequest filterRequest);
		
	/*-------------------------- view userManagement Feign ------------------------------*/
		
		@RequestMapping(value="/usertypeData" ,method=RequestMethod.POST) 
		public Object viewUserTypeRequest(@RequestBody FilterRequest filterRequest,
		@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
		@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
		@RequestParam(value = "file", defaultValue = "0") Integer file);
	
		
	/*-------------------------- Update System user Type Management Feign ------------------------------*/	
		
		@PostMapping("/updateUserTypeStatus")
		public HttpResponse changeSystemUserStatusFeign(UserManagementContent userManagementContent);

	
		
		/*-------------------------- view userManagement Feign ------------------------------*/
		
		@RequestMapping(value="/userTypeFeatureData" ,method=RequestMethod.POST) 
		public Object viewUserFeatureMappingRequest(@RequestBody FilterRequest filterRequest,
		@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
		@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
		@RequestParam(value = "file", defaultValue = "0") Integer file);	
		
	/*-------------------------- Update System user Period Feign ------------------------------*/	
		
		@PostMapping("/updatePeriod")
		public HttpResponse changeSystemUserPeriodFeign(UserManagementContent userManagementContent);
		
		/*-------------------------- view Alert Management Feign ------------------------------*/
		
		@RequestMapping(value="/alertDb/viewAll" ,method=RequestMethod.POST) 
		public Object viewAlertRequest(@RequestBody AlertRequest filterRequest,
		@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
		@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
		@RequestParam(value = "file", defaultValue = "0") Integer file,
		@RequestParam(name = "source", defaultValue = "menu", required = false) String source);	
		
/*-------------------------- view Running Alert Management Feign ------------------------------*/
		
		@RequestMapping(value="/runningAlert/viewAll" ,method=RequestMethod.POST) 
		public Object viewRunningAlertRequest(@RequestBody FilterRequest filterRequest,
		@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
		@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
		@RequestParam(value = "file", defaultValue = "0") Integer file,
		@RequestParam(name="source",defaultValue = "menu",required = false) String source );	
		
/*-------------------------- view IP LOG Management Feign ------------------------------*/
		
		@RequestMapping(value="/viewAll" ,method=RequestMethod.POST) 
		public Object viewIPLogRequest(@RequestBody FilterRequest filterRequest,
		@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
		@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
		@RequestParam(value = "file", defaultValue = "0") Integer file,
		@RequestParam(name = "source", defaultValue = "menu", required = false) String source);	

		//***************************************************View Alert by Id Management Feign********************************
		
		@RequestMapping(value="/alertDb/viewById" ,method=RequestMethod.POST) 
		public @ResponseBody GenricResponse viewAlertFeign(@RequestBody FilterRequest filterRequest);

		
		//***************************************************Update Alert Management  Feign******************************

		@RequestMapping(value= "/alertDb/update" , method=RequestMethod.POST) 
		public GenricResponse updateAlertFeign(@RequestBody FilterRequest filterRequest);
		
		//***************************************************consignment Tax Pay Feign********************************

		@RequestMapping(value= "/payment/url" , method=RequestMethod.POST) 
		public GenricResponse consignmentTaxFeign(@RequestBody PaymentRequest paymentRequest);

/*-------------------------- view SLA Management Feign ------------------------------*/
		
		@RequestMapping(value="/sla/viewAll" ,method=RequestMethod.POST) 
		public Object viewSLARequest(@RequestBody SLAfilterRequest filterRequest,
		@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
		@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
		@RequestParam(value = "file", defaultValue = "0") Integer file);	
		
		/*-------------------------- view user Management Feign ------------------------------*/
		
		@RequestMapping(value="/userMgmt/view" ,method=RequestMethod.POST) 
		public Object viewSystemUserManagementRequest(@RequestBody FilterRequest filterRequest,
		@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
		@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
		@RequestParam(value = "file", defaultValue = "0") Integer file);
		
/*-------------------------- view Visa Update Feign ------------------------------*/
		
		@RequestMapping(value="/visa/view" ,method=RequestMethod.POST) 
		public Object viewVisaRequest(@RequestBody FilterRequest filterRequest,
		@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
		@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
		@RequestParam(value = "file", defaultValue = "0") Integer file);
		
	/*----------------------- Add System user feign ---------------------------*/
		
		@PostMapping("/userMgmt/save")
		public GenricResponse saveSystemUser(@RequestBody NewSystemUser newSystemUser);		
		
		//***************************************************View user Feign********************************
		
		@RequestMapping(value="/userMgmt/getById" ,method=RequestMethod.POST) 
		public @ResponseBody GenricResponse viewUserFeign(@RequestBody NewSystemUser newSystemUser);	
		
		//***************************************************Update user Feign********************************

		@RequestMapping(value= "/userMgmt/update", method=RequestMethod.POST) 
		public GenricResponse updateUserFeign(@RequestBody NewSystemUser newSystemUser);
		
		//***************************************************Delete user Feign********************************
		
		@RequestMapping(value="/userMgmt/delete" ,method=RequestMethod.POST) 
		public @ResponseBody GenricResponse deleteUserFeign(@RequestBody NewSystemUser newSystemUser);
		
		//***************************************************userToFeatureDropdown user Feign********************************
		
		@GetMapping("/userToFeatureDropdown/{featureId}/{usertypeId}")
		public List <UserfeatureContent> userToFeatureDropdownFeign(@PathVariable("featureId") Integer featureId, @PathVariable("usertypeId") Integer usertypeId);
		
		//***************************************************View userType Feign********************************
		
		@RequestMapping(value="/usertype/viewById" ,method=RequestMethod.POST) 
		public @ResponseBody GenricResponse viewUserTypeFeign(@RequestBody FilterRequest filterRequest);
		
		//***************************************************View userType Feature Feign********************************
		
		@RequestMapping(value="/viewPeriodById" ,method=RequestMethod.POST) 
		public @ResponseBody GenricResponse viewUserTypeFeatureFeign(@RequestBody FilterRequest filterRequest);
} 

