package org.gl.ceir.datatable.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.gl.ceir.CeirPannelCode.Feignclient.FeignCleintImplementation;
import org.gl.ceir.CeirPannelCode.Model.AttachedFile;
import org.gl.ceir.CeirPannelCode.Model.CCPolicyBreachRequest;
import org.gl.ceir.CeirPannelCode.Model.CustomerCareRequest;
import org.gl.ceir.Class.HeadersTitle.DatatableResponseModel;
import org.gl.ceir.Class.HeadersTitle.IconsState;
import org.gl.ceir.pageElement.model.Button;
import org.gl.ceir.pageElement.model.PageElement;
import org.gl.ceir.pagination.model.CCNotificationContent;
import org.gl.ceir.pagination.model.CCNotificationPagination;
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
public class CCnotificationDatatable {
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
	CCNotificationContent ccnotificationContent;
	@Autowired
	CCNotificationPagination ccnotificationPagination;
	
	
	
	@PostMapping("CCNotificationData")
	public ResponseEntity<?> viewNotificationRecord(@RequestParam(name="type",defaultValue = "ccnotification",required = false) String role, HttpServletRequest request,HttpSession session) {
		
		//String userType = (String) session.getAttribute("usertype");
		//int userId=	(int) session.getAttribute("userid");
		
		
				// Data set on this List
				List<List<Object>> finalList=new ArrayList<List<Object>>();
		
				String filter = request.getParameter("filter");
				Gson gsonObject=new Gson();
				CCPolicyBreachRequest filterRequest = gsonObject.fromJson(filter, CCPolicyBreachRequest.class);
				Integer pageSize = Integer.parseInt(request.getParameter("length"));
				Integer pageNo = Integer.parseInt(request.getParameter("start")) / pageSize ;
				log.info("pageSize"+pageSize+"-----------pageNo---"+pageNo);
				filterRequest.setSearchString(request.getParameter("search[value]"));
		try {
			log.info("request send to the filter api ="+filterRequest);
			Object response = feignCleintImplementation.ccdashBoardNotification(filterRequest, pageNo, pageSize);
			log.info("response in datatable"+response);
			Gson gson= new Gson(); 
			String apiResponse = gson.toJson(response);
			ccnotificationPagination = gson.fromJson(apiResponse,CCNotificationPagination.class);
			List<CCNotificationContent> paginationContentList = ccnotificationPagination.getContent();
			if(paginationContentList.isEmpty()) {
				datatableResponseModel.setData(Collections.emptyList());
			}else {
				for(CCNotificationContent dataInsideList : paginationContentList) 
				{
					String createdOn =dataInsideList.getCreatedOn();
					String txnID = null;
					txnID = dataInsideList.getFeatureTxnId();
					if(txnID != null  && txnID.equals("")) {
						txnID = dataInsideList.getFeatureTxnId();
					}else {
						txnID = "NA";
					}
					//String feature = dataInsideList.getFeatureName();
					String feature = null;
					feature = dataInsideList.getFeatureName();
					if(feature != null  && feature.equals("")) {
						feature = dataInsideList.getFeatureTxnId();
					}else {
						feature = "NA";
					}
					String message =dataInsideList.getMessage();
					String action=iconState.ccNotificationIcon();			   
					Object[] finalData={createdOn,txnID,feature,message,action}; 
					List<Object> finalDataList=new ArrayList<Object>(Arrays.asList(finalData));
					finalList.add(finalDataList);
					datatableResponseModel.setData(finalList);	
			}
		}
			//data set on ModelClass
			datatableResponseModel.setRecordsTotal(ccnotificationPagination.getNumberOfElements());
			datatableResponseModel.setRecordsFiltered(ccnotificationPagination.getTotalElements());
			return new ResponseEntity<>(datatableResponseModel, HttpStatus.OK); 
			
		}catch(Exception e) {
			datatableResponseModel.setRecordsTotal(null);
			datatableResponseModel.setRecordsFiltered(null);
			datatableResponseModel.setData(Collections.emptyList());
			return new ResponseEntity<>(datatableResponseModel, HttpStatus.OK); 
			
			
		}
		
	}
}
