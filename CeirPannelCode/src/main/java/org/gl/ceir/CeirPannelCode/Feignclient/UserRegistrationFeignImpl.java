package org.gl.ceir.CeirPannelCode.Feignclient;
import java.util.List;

import org.gl.ceir.CeirPannelCode.Model.FeatureDropdown;
import org.gl.ceir.CeirPannelCode.Model.FilterRequest;
import org.gl.ceir.CeirPannelCode.Model.GenricResponse;
import org.gl.ceir.CeirPannelCode.Model.Otp;
import org.gl.ceir.CeirPannelCode.Model.OtpResponse;
import org.gl.ceir.CeirPannelCode.Model.PeriodValidate;
import org.gl.ceir.CeirPannelCode.Model.Registration;
import org.gl.ceir.CeirPannelCode.Model.ResendOtp;
import org.gl.ceir.CeirPannelCode.Model.SecurityQuestion;
import org.gl.ceir.CeirPannelCode.Model.Usertype;
import org.gl.ceir.CeirPannelCode.Util.HttpResponse;
import org.gl.ceir.pagination.model.AlertContentModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Service
@FeignClient(url="${apiUrl1}",value = "registrationUrls")
public interface UserRegistrationFeignImpl {
  
	@PostMapping("/userRegistration/getUsertypes")
	public List<Usertype> userypeList();
	
	@PostMapping("/userRegistration/usertypeIdByName/{usertype}")
	public Usertype userypeDataByName(@PathVariable("usertype")String usertype);
	                                  
	@PostMapping("/userRegistration/getSecurityQuestion/{username}")
	public GenricResponse securityQuestionList(@PathVariable("username")String username);
	
	@PostMapping("/userRegistration/getAllSecurityQuestion")
	public List<SecurityQuestion> getAllSecurityQuestion();

	
	@PostMapping("/userRegistration/registration")
	public OtpResponse registration(@RequestBody Registration registration); 
	                                          
	@PostMapping("/userRegistration/validate")
	public HttpResponse  otpValidate(@RequestBody Otp otp);
	
	@PostMapping("/userRegistration/resendOtp")                                                                                         
	public HttpResponse otpResend(@RequestBody ResendOtp otp); 
	
	@PostMapping("/userRegistration/profileResendOtp")                                                                                         
	public HttpResponse profileResendOtp(@RequestBody ResendOtp otp); 

	
	@PostMapping("/userRegistration/getUsertypes")                                                                                         
	public List<Usertype> userRegistrationDropdown(@RequestParam(name="type",required = false) Integer type); 
	
	                              
	@PostMapping("/userRegistration/checkAvailability/{usertypeId}")                                                                                         
	public HttpResponse checkRegistration(@PathVariable("usertypeId")Integer usertypeId); 
     
	@PostMapping("/getAllFeatures")                                                                                         
	public List<FeatureDropdown> userAllFeatureDropdown(); 
	
	@PostMapping("/alertDb/view")                                                                                         
	public List<AlertContentModel> userAllAlertDropdown();
	
	@PostMapping("/subFeature/view")                                                                                         
	public List<FeatureDropdown> userAllSubFeatureDropdown();
	
	@PostMapping("/userProfile/getAddDeleteRoles")	
	public @ResponseBody GenricResponse getAddDeleteRoleFeign(FilterRequest filterRequest);	
	
	@PostMapping("/periodValidate")
	public HttpResponse periodValidate(@RequestBody PeriodValidate periodValidate);
}
