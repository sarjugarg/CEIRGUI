package org.gl.ceir.CeirPannelCode.Feignclient;

import java.util.List;

import org.gl.ceir.CeirPannelCode.PropertyReader;
import org.gl.ceir.CeirPannelCode.Model.FilterRequest;
import org.gl.ceir.CeirPannelCode.Model.NumberOfBox;
import org.gl.ceir.CeirPannelCode.Model.RequestCountAndQuantity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@FeignClient(url = "${dashBoardfeignClientPath}", value = "dashoard")
public interface DashboardFeignClient {

	// ********************************************* Notification fiegn
	// implementation
	// ************************************************************************

	// countAndQuantity feign controller
	@RequestMapping(value = "/consignment/countAndQuantity", method = RequestMethod.GET)
	public RequestCountAndQuantity consignmentNotification(@RequestParam(value = "userId") Integer userId,
			@RequestParam(value = "featureId") Integer featureId,
			@RequestParam(value = "userTypeId") Integer userTypeId, @RequestParam(value = "userType") String userType,
			@RequestParam(name = "source", defaultValue = "dashboard", required = false) String source);

	// stock/countAndQuantity feign controller
	@RequestMapping(value = "/stock/countAndQuantity", method = RequestMethod.GET)
	public RequestCountAndQuantity stockNotification(@RequestParam(value = "userId") Integer userId,
			@RequestParam(value = "featureId") Integer featureId,
			@RequestParam(value = "userTypeId") Integer userTypeId, @RequestParam(value = "userType") String userType,
			@RequestParam(name = "source", defaultValue = "dashboard", required = false) String source);

	// stolen/recovery countAndQuantity feign controller
	@RequestMapping(value = "/stakeholder/count", method = RequestMethod.GET)
	public RequestCountAndQuantity stolenRecoveryNotification(@RequestParam(value = "requestType") String requestType,
			@RequestParam(value = "userId") Integer userId, @RequestParam(value = "featureId") Integer featureId,
			@RequestParam(value = "userTypeId") Integer userTypeId, @RequestParam(value = "userType") String userType);

	// stock/countAndQuantity feign controller
	@RequestMapping(value = "/grievance/count", method = RequestMethod.GET)
	public RequestCountAndQuantity grievanceNotification(@RequestParam(value = "requestType") String requestType,
			@RequestParam(value = "userId") Integer userId, @RequestParam(value = "featureId") Integer featureId,
			@RequestParam(value = "userTypeId") Integer userTypeId, @RequestParam(value = "userType") String userType);

	// dashboard/dbConf controller
	@RequestMapping(value = "/dashboard/dbConf", method = RequestMethod.GET)
	public List<NumberOfBox> dashBoardDBConf(@RequestParam(value = "userTypeId") Integer userTypeId);

	// stock/countAndQuantity feign controller
	@RequestMapping(value = "/users/pendingCount", method = RequestMethod.GET)
	public RequestCountAndQuantity userPendingCount(@RequestParam(value = "requestType") String requestType,
			@RequestParam(value = "userId") Integer userId, @RequestParam(value = "featureId") Integer featureId,
			@RequestParam(value = "userTypeId") Integer userTypeId, @RequestParam(value = "userType") String userType);

	/// stakeholder/blockUnblockCount
		@RequestMapping(value = "/stakeholder/blockUnblockCount", method = RequestMethod.GET)
		public RequestCountAndQuantity stakeholderBlockUnblockCount(@RequestParam(value = "requestType") String requestType,
				@RequestParam(value = "userId") Integer userId, @RequestParam(value = "featureId") Integer featureId,
				@RequestParam(value = "userTypeId") Integer userTypeId, @RequestParam(value = "userType") String userType,
				@RequestParam(value = "operatorId",required = false) Integer operatorId);

	/// TypeApproved/count
	@RequestMapping(value = "/TypeApproved/count", method = RequestMethod.GET)
	public RequestCountAndQuantity typeApprovedCount(@RequestParam(value = "requestType") String requestType,
			@RequestParam(value = "userId") Integer userId, @RequestParam(value = "featureId") Integer featureId,
			@RequestParam(value = "userTypeId") Integer userTypeId, @RequestParam(value = "userType") String userType);

	@RequestMapping(value = "/device/countAndQuantity", method = RequestMethod.GET)
	public RequestCountAndQuantity deviceCountAndQuantity(@RequestParam(value = "requestType") String requestType,
			@RequestParam(value = "userId") Integer userId, @RequestParam(value = "featureId") Integer featureId,
			@RequestParam(value = "userTypeId") Integer userTypeId, @RequestParam(value = "userType") String userType);

	@RequestMapping(value = "/updateVisa/countAndQuantity", method = RequestMethod.GET)
	public RequestCountAndQuantity updateVisaCountAndQuantity(@RequestParam(value = "requestType") String requestType,
			@RequestParam(value = "userId") Integer userId, @RequestParam(value = "featureId") Integer featureId,
			@RequestParam(value = "userTypeId") Integer userTypeId, @RequestParam(value = "userType") String userType);

}
