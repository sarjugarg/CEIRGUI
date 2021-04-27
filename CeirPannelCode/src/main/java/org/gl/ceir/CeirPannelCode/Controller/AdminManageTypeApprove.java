package org.gl.ceir.CeirPannelCode.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.gl.ceir.CeirPannelCode.Feignclient.TypeApprovedFeignImpl;
import org.gl.ceir.CeirPannelCode.Model.GenricResponse;
import org.gl.ceir.CeirPannelCode.Model.UserHeader;
import org.gl.ceir.CeirPannelCode.Service.RegistrationService;
import org.gl.ceir.CeirPannelCode.Util.UtilDownload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AdminManageTypeApprove {
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	TypeApprovedFeignImpl typeApprovedFeignImpl;
	@Autowired
	UtilDownload utildownload;
	
	@Autowired
	RegistrationService registerService;
	
	@RequestMapping(value=
		{"/manageTypeDevices2"},method={org.springframework.web.bind.annotation.
				RequestMethod.GET,org.springframework.web.bind.annotation.RequestMethod.POST}
			)
	public ModelAndView viewManageTypeAdmin(HttpSession session,@RequestParam(name="txnID",required = false) String txnID) {
		ModelAndView mv = new ModelAndView();
		log.info(" view ManageType entry point."); 
		mv.setViewName("viewManageTypeAdmin");
		log.info(" view ManageType  exit point."); 
		return mv; 
	}
	
	@GetMapping("register-form-importer")
	public ModelAndView regiserImporterForm() {
		ModelAndView modelAndView = new ModelAndView("importerTypeApproved");
		return modelAndView;

	}
	
	@PostMapping("importerTacDelete")
	public @ResponseBody GenricResponse deleteTac(@RequestParam(name="id",required = false ) Integer id,
			  									  @RequestParam(name="userType",required = false ) String userType, 
												  @RequestParam(name="userId",required = false ) Integer userId,
												  @RequestParam(name="remark",required = false ) String remark,
												  HttpSession session,HttpServletRequest request) {

		
		UserHeader header=registerService.getUserHeaders(request);
		String publicIp = header.getPublicIp();
		String browser = header.getBrowser();
		log.info("request passed to the deleteTAC Api="+id+" userType="+userType+" userId="+userId+" remark="+remark);
		GenricResponse response=typeApprovedFeignImpl.TypeApproveDelete(id, userType, userId, remark,publicIp,browser);
		log.info("response after delete Stock."+response);
		return response;
		

	}
	
	
}
