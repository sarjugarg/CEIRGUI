package org.gl.ceir.graph.controller;

import java.util.Collections;

import javax.servlet.http.HttpSession;

import org.gl.ceir.CeirPannelCode.Feignclient.AnalyticsFeign;
import org.gl.ceir.graph.model.ActiveDeviceCountContent;
import org.gl.ceir.graph.model.ActiveDeviceCountResponseModel;
import org.gl.ceir.graph.model.ActiveDeviceDeviceResponseModel;
import org.gl.ceir.graph.model.ActiveIMEICountContent;
import org.gl.ceir.graph.model.DistributorCountContent;
import org.gl.ceir.graph.model.DistributorCountResponseModel;
import org.gl.ceir.graph.model.GraphRequest;
import org.gl.ceir.graph.model.ImporterCountContent;
import org.gl.ceir.graph.model.ImporterCountResponseModel;
import org.gl.ceir.graph.model.LawfulCountContent;
import org.gl.ceir.graph.model.LawfulCountResponseModel;
import org.gl.ceir.graph.model.MobileDeviceCountContent;
import org.gl.ceir.graph.model.MobileDeviceCountResponseModel;
import org.gl.ceir.graph.model.OperatorBlackGreyCountContent;
import org.gl.ceir.graph.model.OperatorBlackGreyCountResponse;
import org.gl.ceir.graph.model.RetailerCountContent;
import org.gl.ceir.graph.model.RetailerCountResponseModel;
import org.gl.ceir.graph.model.UserDashboardCountContent;
import org.gl.ceir.graph.model.UserDashboardResponseModel;
import org.gl.ceir.graph.model.UserDashboardUnblockCountContent;
import org.gl.ceir.graph.model.UserDashboardUnblockResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

@RestController
public class GraphDashboardCount {

	private final Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	AnalyticsFeign analyticsFeign;
	@Autowired
	ActiveDeviceCountResponseModel countResponseModel;

	@Autowired
	UserDashboardResponseModel userDashboardResponseModel;
	
	@Autowired
	MobileDeviceCountResponseModel mobileDeviceCountResponseModel;
	@Autowired
	LawfulCountResponseModel lawfulCountrespnose;
	@Autowired
	RetailerCountResponseModel retailerCountResponseModel;
	@Autowired
	DistributorCountResponseModel distributorCountResponseModel;

	@Autowired
	ImporterCountResponseModel importerCountResponseModel;
	
	@Autowired
	UserDashboardUnblockResponseModel userDashboardUnblockResponseModel;
	
	@Autowired
	ActiveDeviceDeviceResponseModel activeIMEICountContent;
	
	@Autowired
	OperatorBlackGreyCountResponse operatorBlackGreyCountResponse;
	@PostMapping("/report/count/{featureTag}") 
	public ResponseEntity<?> activeDeviceCount(@RequestBody GraphRequest graphRequest, @PathVariable("featureTag") int featureTag,HttpSession session) {
		Object response= null;
		graphRequest.setPublicIp(session.getAttribute("publicIP").toString());
		graphRequest.setBrowser(session.getAttribute("browser").toString());
		response = analyticsFeign.graph(graphRequest, graphRequest.getPageNo(),  graphRequest.getPageSize(),  graphRequest.getFile());
		 
		 log.info(":::::::::countRequest::::::::"+graphRequest+"::::::countResponse:::::::"+response);
			try {
			Gson gson= new Gson(); 
			String apiResponse = gson.toJson(response);
			log.info("::::::apiResponse:::::::"+apiResponse);
			if(featureTag==31) {

				countResponseModel = gson.fromJson(apiResponse, ActiveDeviceCountResponseModel.class);
				ActiveDeviceCountContent paginationContentList = countResponseModel.getContent();
				log.info("::::::apiResponse ActiveDevice ="+paginationContentList);
				return new ResponseEntity<>(paginationContentList, HttpStatus.OK);
			}
			else if(featureTag==1) {
				userDashboardUnblockResponseModel = gson.fromJson(apiResponse, UserDashboardUnblockResponseModel.class);
				UserDashboardUnblockCountContent paginationContentList = userDashboardUnblockResponseModel.getContent();
				log.info("::::::apiResponse userDashboardUnblockResponseModel ="+userDashboardUnblockResponseModel);
				return new ResponseEntity<>(paginationContentList, HttpStatus.OK);
			}
			else if(featureTag==27) {
				activeIMEICountContent = gson.fromJson(apiResponse, ActiveDeviceDeviceResponseModel.class);
				ActiveIMEICountContent paginationContentList = activeIMEICountContent.getContent();
				log.info("::::::apiResponse activeIMEICountContent ="+activeIMEICountContent);
				return new ResponseEntity<>(paginationContentList, HttpStatus.OK);
			}
			}
			catch(Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(Collections.EMPTY_LIST, HttpStatus.SERVICE_UNAVAILABLE);
			}
			return null;
	}
	
	
	
	@PostMapping("/user/report/count") 
	public ResponseEntity<?> activeDeviceGraph(@RequestBody GraphRequest graphRequest,HttpSession session) {

		Object response= null;
		graphRequest.setPublicIp(session.getAttribute("publicIP").toString());
		graphRequest.setBrowser(session.getAttribute("browser").toString());
		response = analyticsFeign.graph(graphRequest, graphRequest.getPageNo(),  graphRequest.getPageSize(),  graphRequest.getFile());
		 
		 log.info("/user/report/count:::::::::countRequest::::::::"+graphRequest+"::::::countResponse:::::::"+response);
			try {
			Gson gson= new Gson(); 
			String apiResponse = gson.toJson(response);
			log.info("/user/report/count::::::apiResponse:::::::"+apiResponse);
			
			userDashboardResponseModel = gson.fromJson(apiResponse, UserDashboardResponseModel.class);
			UserDashboardCountContent paginationContentList = userDashboardResponseModel.getContent();
			return new ResponseEntity<>(paginationContentList, HttpStatus.OK);
			}
			catch(Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(Collections.EMPTY_LIST, HttpStatus.SERVICE_UNAVAILABLE);
			}
	
	}
	
	@PostMapping("/mobileDevice/report/count") 
	public ResponseEntity<?> mobileDeviceCount(@RequestBody GraphRequest graphRequest,HttpSession session) {

		Object response= null;
		graphRequest.setPublicIp(session.getAttribute("publicIP").toString());
		graphRequest.setBrowser(session.getAttribute("browser").toString());
		response = analyticsFeign.graph(graphRequest, graphRequest.getPageNo(),  graphRequest.getPageSize(),  graphRequest.getFile());
		 
		 log.info("/mobileDevice/report/count:::::::::countRequest::::::::"+graphRequest+"::::::countResponse:::::::"+response);
			try {
			Gson gson= new Gson(); 
			String apiResponse = gson.toJson(response);
			log.info("/mobileDevice/report/count::::::apiResponse:::::::"+apiResponse);
			
			mobileDeviceCountResponseModel = gson.fromJson(apiResponse, MobileDeviceCountResponseModel.class);
			MobileDeviceCountContent paginationContentList = mobileDeviceCountResponseModel.getContent();
			return new ResponseEntity<>(paginationContentList, HttpStatus.OK);
			}
			catch(Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(Collections.EMPTY_LIST, HttpStatus.SERVICE_UNAVAILABLE);
			}
	
	}
	
	@PostMapping("/lawful/report/count") 
	public ResponseEntity<?> lawfulCount(@RequestBody GraphRequest graphRequest,HttpSession session) {

		Object response= null;
		graphRequest.setPublicIp(session.getAttribute("publicIP").toString());
		graphRequest.setBrowser(session.getAttribute("browser").toString());
		response = analyticsFeign.graph(graphRequest, graphRequest.getPageNo(),  graphRequest.getPageSize(),  graphRequest.getFile());
		 
		 log.info("/lawful/report/count:::::::::countRequest::::::::"+graphRequest+"::::::countResponse:::::::"+response);
			try {
			Gson gson= new Gson(); 
			String apiResponse = gson.toJson(response);
			log.info("/lawful/report/count::::::apiResponse:::::::"+apiResponse);
			
			lawfulCountrespnose = gson.fromJson(apiResponse, LawfulCountResponseModel.class);
			LawfulCountContent paginationContentList = lawfulCountrespnose.getContent();
			return new ResponseEntity<>(paginationContentList, HttpStatus.OK);
			}
			catch(Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(Collections.EMPTY_LIST, HttpStatus.SERVICE_UNAVAILABLE);
			}
	
	}
	
	@PostMapping("/importer/count/{featureFlag}") 
	public ResponseEntity<?> importerCount(@RequestBody GraphRequest graphRequest , @PathVariable("featureFlag") String featureFlag,HttpSession session) {

		Object response= null;
		graphRequest.setPublicIp(session.getAttribute("publicIP").toString());
		graphRequest.setBrowser(session.getAttribute("browser").toString());
		response = analyticsFeign.graph(graphRequest, graphRequest.getPageNo(),  graphRequest.getPageSize(),  graphRequest.getFile());
		 
		 log.info("/importer/report/count:::::::::countRequest::::::::"+graphRequest+"::::::countResponse:::::::"+response);
			try {
			Gson gson= new Gson(); 
			String apiResponse = gson.toJson(response);
			log.info("/importer/report/count::::::apiResponse:::::::"+apiResponse);
			if(featureFlag.equals("importer")) {
				importerCountResponseModel = gson.fromJson(apiResponse, ImporterCountResponseModel.class);
				ImporterCountContent paginationContentList = importerCountResponseModel.getContent();
				return new ResponseEntity<>(paginationContentList, HttpStatus.OK);
			}
			else if(featureFlag.equals("retailer")) {
				retailerCountResponseModel = gson.fromJson(apiResponse, RetailerCountResponseModel.class);
				RetailerCountContent paginationContentList = retailerCountResponseModel.getContent();
				return new ResponseEntity<>(paginationContentList, HttpStatus.OK);
					
			}
			else if(featureFlag.equals("distributor")) {
				distributorCountResponseModel = gson.fromJson(apiResponse, DistributorCountResponseModel.class);
				DistributorCountContent paginationContentList = distributorCountResponseModel.getContent();
				return new ResponseEntity<>(paginationContentList, HttpStatus.OK);
					
			}
			
			}
			catch(Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(Collections.EMPTY_LIST, HttpStatus.SERVICE_UNAVAILABLE);
			}
			return null;
	
	}
	
	@PostMapping("/operatorReport/count/{featureTag}") 
	public ResponseEntity<?> operatorReport(@RequestBody GraphRequest graphRequest, @PathVariable("featureTag") int featureTag,HttpSession session) {
		Object response= null;
		graphRequest.setPublicIp(session.getAttribute("publicIP").toString());
		graphRequest.setBrowser(session.getAttribute("browser").toString());
		response = analyticsFeign.graph(graphRequest, graphRequest.getPageNo(),  graphRequest.getPageSize(),  graphRequest.getFile());
		 
		 log.info(":::::::::countRequest::::::::"+graphRequest+"::::::countResponse:::::::"+response);
			try {
			Gson gson= new Gson(); 
			String apiResponse = gson.toJson(response);
			log.info("::::::apiResponse:::::::"+apiResponse);
			if(featureTag==31) {

				operatorBlackGreyCountResponse = gson.fromJson(apiResponse, OperatorBlackGreyCountResponse.class);
				OperatorBlackGreyCountContent paginationContentList = operatorBlackGreyCountResponse.getContent();
				log.info("::::::apiResponse operatorBlackGreyCountResponse ="+paginationContentList);
				return new ResponseEntity<>(paginationContentList, HttpStatus.OK);
			}
			
			}
			catch(Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(Collections.EMPTY_LIST, HttpStatus.SERVICE_UNAVAILABLE);
			}
			return null;
	}
}
