package org.gl.ceir.datatable.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.gl.ceir.CeirPannelCode.Feignclient.FeignCleintImplementation;
import org.gl.ceir.CeirPannelCode.Model.FilterRequest;
import org.gl.ceir.Class.HeadersTitle.DatatableResponseModel;
import org.gl.ceir.Class.HeadersTitle.IconsState;
import org.gl.ceir.pageElement.model.Button;
import org.gl.ceir.pageElement.model.PageElement;
import org.gl.ceir.pagination.model.NotificationContent;
import org.gl.ceir.pagination.model.NotificationPaginationModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

@RestController
public class NotificationDatatableController {
	private final Logger log = LoggerFactory.getLogger(getClass());
	String className = "emptyClass";
	@Autowired
	DatatableResponseModel datatableResponseModel;
	@Autowired
	PageElement pageElement;
	@Autowired
	Button button;
	@Autowired
	FeignCleintImplementation feignCleintImplementation;
	@Autowired
	IconsState iconState;
	@Autowired
	NotificationContent notificationContent;
	@Autowired
	NotificationPaginationModel notificationPaginationModel;

	@PostMapping("NotificationData")
	public ResponseEntity<?> viewNotificationRecord(
			@RequestParam(name = "type", defaultValue = "notification", required = false) String role,
			HttpServletRequest request, HttpSession session) {

		String userType = (String) session.getAttribute("usertype");
		int userId = (int) session.getAttribute("userid");

		log.info("session value user Type admin registration Controller==" + session.getAttribute("usertype"));
		// Data set on this List
		List<List<Object>> finalList = new ArrayList<List<Object>>();

		String filter = request.getParameter("filter");
		Gson gsonObject = new Gson();
		FilterRequest filterrequest = gsonObject.fromJson(filter, FilterRequest.class);

		Integer pageSize = Integer.parseInt(request.getParameter("length"));
		Integer pageNo = Integer.parseInt(request.getParameter("start")) / pageSize;
		log.info("pageSize" + pageSize + "-----------pageNo---" + pageNo);
		filterrequest.setSearchString(request.getParameter("search[value]"));
		try {
			log.info("request send to the filter api =" + filterrequest);
			Object response = feignCleintImplementation.dashBoardNotification(filterrequest, pageNo, pageSize);
			log.info("response in datatable" + response);
			Gson gson = new Gson();
			String apiResponse = gson.toJson(response);
			notificationPaginationModel = gson.fromJson(apiResponse, NotificationPaginationModel.class);
			List<NotificationContent> paginationContentList = notificationPaginationModel.getContent();
			if (paginationContentList.isEmpty()) {
				datatableResponseModel.setData(Collections.emptyList());
			} else {
				for (NotificationContent dataInsideList : paginationContentList) {
					// Integer id = dataInsideList.getId();
					String createdOn = (String) dataInsideList.getCreatedOn();
					String txnID = dataInsideList.getFeatureTxnId();
					String featureName = dataInsideList.getFeatureName();
					String message = dataInsideList.getMessage();
					String userStatus = (String) session.getAttribute("userStatus");
					Integer userID = dataInsideList.getUserId() == null ? -1 : dataInsideList.getUserId();
					String roleType = dataInsideList.getRoleType() == null ? "blank" : dataInsideList.getRoleType();
					String reciverUserType=dataInsideList.getReceiverUserType() == null ? "NA" : dataInsideList.getReceiverUserType();
					String action = iconState.dashboardIcon(userStatus, dataInsideList.getFeatureId(), txnID, userID,
							roleType,reciverUserType);
					Object[] finalData = { createdOn, txnID, featureName, message, action };
					List<Object> finalDataList = new ArrayList<Object>(Arrays.asList(finalData));
					finalList.add(finalDataList);
					datatableResponseModel.setData(finalList);
				}
			}
			// data set on ModelClass
			datatableResponseModel.setRecordsTotal(notificationPaginationModel.getNumberOfElements());
			datatableResponseModel.setRecordsFiltered(notificationPaginationModel.getTotalElements());
			return new ResponseEntity<>(datatableResponseModel, HttpStatus.OK);

		} catch (Exception e) {

			datatableResponseModel.setRecordsTotal(null);
			datatableResponseModel.setRecordsFiltered(null);
			datatableResponseModel.setData(Collections.emptyList());
			return new ResponseEntity<>(datatableResponseModel, HttpStatus.OK);

		}

	}

}
