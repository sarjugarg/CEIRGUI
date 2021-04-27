package org.gl.ceir.CeirPannelCode.Controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.gl.ceir.CeirPannelCode.Feignclient.DashboardFeignClient;
import org.gl.ceir.CeirPannelCode.Feignclient.FeignCleintImplementation;
import org.gl.ceir.CeirPannelCode.Model.NumberOfBox;
import org.gl.ceir.CeirPannelCode.Model.RequestCountAndQuantity;
import org.gl.ceir.CeirPannelCode.Service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class Dashboard {
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	FeignCleintImplementation feignCleintImplementation;

	@Autowired
	DashboardFeignClient dashboardFeignClient;
	@Autowired
	DashboardFeignClient dashBoardclient;

	@Autowired
	LoginService loginService;
	ModelAndView mv = new ModelAndView();

	@GetMapping("*")
	public ModelAndView openUserRegisterPage(HttpSession session,HttpServletRequest request) {
		//return loginService.dashBoard(session,request);
		return loginService.dashBoard(session);
	}

	@RequestMapping(value = { "/Home" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public ModelAndView Home(HttpSession session) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("Home");
		return mv;
	}

	@GetMapping("dashboard/box")
	@ResponseBody
	public ResponseEntity<?> initialDashBoard(@RequestParam(value = "userTypeId") Integer userTypeId) {
		List<NumberOfBox> response = dashboardFeignClient.dashBoardDBConf(userTypeId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/consignment/countAndQuantity")
	public ResponseEntity<?> getConsignmetnCountAndQuantity(@RequestParam(value = "userId") Integer userId,
			@RequestParam(value = "featureId") Integer featureId,
			@RequestParam(value = "userTypeId") Integer userTypeId, @RequestParam(value = "userType") String userType,
			@RequestParam(name = "source", defaultValue = "dashboard", required = false) String source) {
		RequestCountAndQuantity response = dashboardFeignClient.consignmentNotification(userId, featureId, userTypeId,
				userType, source);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/stock/countAndQuantity")
	public ResponseEntity<?> getStockCountAndQuantity(@RequestParam(value = "userId") Integer userId,
			@RequestParam(value = "featureId") Integer featureId,
			@RequestParam(value = "userTypeId") Integer userTypeId, @RequestParam(value = "userType") String userType,
			@RequestParam(name = "source", defaultValue = "dashboard", required = false) String source) {
		RequestCountAndQuantity response = dashboardFeignClient.stockNotification(userId, featureId, userTypeId,
				userType, source);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/stakeholder/count")
	public ResponseEntity<?> getStolen_RecoveryCountAndQuantity(@RequestParam(value = "requestType") String requestType,
			@RequestParam(value = "userId") Integer userId, @RequestParam(value = "featureId") Integer featureId,
			@RequestParam(value = "userTypeId") Integer userTypeId, @RequestParam(value = "userType") String userType) {
		RequestCountAndQuantity response = dashboardFeignClient.stolenRecoveryNotification(requestType, userId,
				featureId, userTypeId, userType);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/grievance/count")
	public ResponseEntity<?> getGrievanceNotificationCountAndQuantity(
			@RequestParam(value = "requestType") String requestType, @RequestParam(value = "userId") Integer userId,
			@RequestParam(value = "featureId") Integer featureId,
			@RequestParam(value = "userTypeId") Integer userTypeId, @RequestParam(value = "userType") String userType) {
		RequestCountAndQuantity response = dashboardFeignClient.grievanceNotification(requestType, userId, featureId,
				userTypeId, userType);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/stakeholder/blockUnblockCount")
	public ResponseEntity<?> getStakeholderBlockUnblockCount(@RequestParam(value = "requestType") String requestType,
			@RequestParam(value = "userId") Integer userId, @RequestParam(value = "featureId") Integer featureId,
			@RequestParam(value = "userTypeId") Integer userTypeId, @RequestParam(value = "userType") String userType,
			@RequestParam(value = "operatorId",required = false) Integer operatorId) {
		RequestCountAndQuantity response = dashboardFeignClient.stakeholderBlockUnblockCount(requestType, userId,
				featureId, userTypeId, userType,operatorId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/TypeApproved/count")
	public ResponseEntity<?> getTypeApprovedCount(@RequestParam(value = "requestType") String requestType,
			@RequestParam(value = "userId") Integer userId, @RequestParam(value = "featureId") Integer featureId,
			@RequestParam(value = "userTypeId") Integer userTypeId, @RequestParam(value = "userType") String userType) {
		RequestCountAndQuantity response = dashboardFeignClient.typeApprovedCount(requestType, userId, featureId,
				userTypeId, userType);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/users/pendingCount")
	public ResponseEntity<?> getUsersPendingCount(@RequestParam(value = "requestType") String requestType,
			@RequestParam(value = "userId") Integer userId, @RequestParam(value = "featureId") Integer featureId,
			@RequestParam(value = "userTypeId") Integer userTypeId, @RequestParam(value = "userType") String userType) {
		RequestCountAndQuantity response = dashboardFeignClient.userPendingCount(requestType, userId, featureId,
				userTypeId, userType);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/device/countAndQuantity")
	public ResponseEntity<?> getDeviceCountAndQuantityCount(@RequestParam(value = "requestType") String requestType,
			@RequestParam(value = "userId") Integer userId, @RequestParam(value = "featureId") Integer featureId,
			@RequestParam(value = "userTypeId") Integer userTypeId, @RequestParam(value = "userType") String userType) {
		RequestCountAndQuantity response = dashboardFeignClient.deviceCountAndQuantity(requestType, userId, featureId,
				userTypeId, userType);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/updateVisa/countAndQuantity")
	public ResponseEntity<?> getUpdateVisaCount(@RequestParam(value = "requestType") String requestType,
			@RequestParam(value = "userId") Integer userId, @RequestParam(value = "featureId") Integer featureId,
			@RequestParam(value = "userTypeId") Integer userTypeId, @RequestParam(value = "userType") String userType) {
		RequestCountAndQuantity response = dashboardFeignClient.updateVisaCountAndQuantity(requestType, userId, featureId,
				userTypeId, userType);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
