package org.gl.ceir.graph.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.gl.ceir.CeirPannelCode.Feignclient.AnalyticsFeign;
import org.gl.ceir.CeirPannelCode.Service.GraphService;
import org.gl.ceir.graph.model.ActiveDeviceGraphContent;
import org.gl.ceir.graph.model.ActiveDeviceGraphResponseModel;
import org.gl.ceir.graph.model.ActiveDeviceGraphRowData;
import org.gl.ceir.graph.model.BlockIMEIContent;
import org.gl.ceir.graph.model.BlockIMEIGraph;
import org.gl.ceir.graph.model.BlockIMEIRowData;
import org.gl.ceir.graph.model.BrandContent;
import org.gl.ceir.graph.model.BrandModelGrapContent;
import org.gl.ceir.graph.model.BrandModelRowData;
import org.gl.ceir.graph.model.ConsignmentContent;
import org.gl.ceir.graph.model.ConsignmentModelGrapContent;
import org.gl.ceir.graph.model.ConsignmentModelRowData;
import org.gl.ceir.graph.model.GraphRequest;
import org.gl.ceir.graph.model.GrievanceContent;
import org.gl.ceir.graph.model.GrievanceGrapContent;
import org.gl.ceir.graph.model.GrievanceModelRowData;
import org.gl.ceir.graph.model.GrievanceUserTpeModelRowData;
import org.gl.ceir.graph.model.GrievanceUserType;
import org.gl.ceir.graph.model.GrievanceUserTypeModelGrapContent;
import org.gl.ceir.graph.model.ImeiUsageGraphContent;
import org.gl.ceir.graph.model.ImeiUsageGraphResponseModel;
import org.gl.ceir.graph.model.MostStolenBrandContent;
import org.gl.ceir.graph.model.MostStolenRowData;
import org.gl.ceir.graph.model.OperatorTableGraph;
import org.gl.ceir.graph.model.OperatorTableGraphContent;
import org.gl.ceir.graph.model.OperatorWiseGraphContant;
import org.gl.ceir.graph.model.OperatorWiseGraphRowData;
import org.gl.ceir.graph.model.OperatorWiseImei;
import org.gl.ceir.graph.model.RegisterDeviceGraphContent;
import org.gl.ceir.graph.model.RegisteredDeviceGraph;
import org.gl.ceir.graph.model.RegisteredDeviceRawList;
import org.gl.ceir.graph.model.StockContentReport;
import org.gl.ceir.graph.model.StockModelGrapContent;
import org.gl.ceir.graph.model.StockModelRowData;
import org.gl.ceir.graph.model.StolenGraphContent;
import org.gl.ceir.graph.model.UserDashboardGraphContent;
import org.gl.ceir.graph.model.UserDashboardGraphResponseModel;
import org.gl.ceir.graph.model.UserDashboardMap;
import org.gl.ceir.graph.model.UserDashboardResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

@RestController
public class RenderGraphData {
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	GraphService graphService;

	@Autowired
	AnalyticsFeign analyticsFeign;

	@Autowired
	UserDashboardGraphResponseModel userDashboardGraphResponseModel;
	@Autowired
	UserDashboardResponseModel userDashboardResponseModel;
	@Autowired
	ActiveDeviceGraphResponseModel activeDeviceGraphResponseModel;
	@Autowired
	BrandContent brandModelGrapContent;
	@Autowired
	StockModelRowData stockModelRowData;
	@Autowired
	ConsignmentModelRowData consignmentModelRowData;
	@Autowired
	GrievanceModelRowData grievanceModelRowData;
	@Autowired
	GrievanceUserTpeModelRowData grievanceUserTpeModelRowData;
	@Autowired
	StockModelGrapContent stockModelGrapContent;
	@Autowired
	GrievanceGrapContent grievanceGrapContent;
	@Autowired
	GrievanceUserTypeModelGrapContent grievanceUserTypeModelGrapContent;
	@Autowired
	ConsignmentModelGrapContent consignmentModelGrapContent;
	@Autowired
	StockContentReport stockContentReport;
	@Autowired
	ConsignmentContent consignmentContent;
	@Autowired
	GrievanceContent grievanceContent;
	@Autowired
	GrievanceUserType grievanceUserTypeContent;

	@Autowired
	BlockIMEIContent blockIMEIContent;

	@Autowired
	BlockIMEIGraph blockIMEIGraph;

	@Autowired
	ImeiUsageGraphResponseModel imeiUsageGraphResponseModel;

	@Autowired
	OperatorWiseImei operatorWiseImei;
	@Autowired
	OperatorTableGraph operatorTableGraph;
	@Autowired
	RegisteredDeviceGraph registeredDeviceGraph;

	@Autowired
	MostStolenBrandContent mostStolenBrandContent;

	@ResponseBody
	@RequestMapping(value = "/userLoginGraph", method = { RequestMethod.POST })
	public ResponseEntity<?> userLoginGraph(@RequestBody GraphRequest graphRequest,HttpSession session) {
		Object response = null;
		graphRequest.setPublicIp(session.getAttribute("publicIP").toString());
		graphRequest.setBrowser(session.getAttribute("browser").toString());
		response = analyticsFeign.graph(graphRequest, graphRequest.getPageNo(), graphRequest.getPageSize(),
				graphRequest.getFile());

		log.info(
				"userLoginGraph:::::::::countRequest::::::::" + graphRequest + "::::::countResponse:::::::" + response);
		try {
			Gson gson = new Gson();
			String apiResponse = gson.toJson(response);
			log.info("userLoginGraph::::::apiResponse:::::::" + apiResponse);

			userDashboardGraphResponseModel = gson.fromJson(apiResponse, UserDashboardGraphResponseModel.class);
			UserDashboardGraphContent paginationContentList = userDashboardGraphResponseModel.getContent();

			List<UserDashboardMap> reverseList = paginationContentList.getRowData();
			Collections.sort(reverseList, Comparator.comparing(UserDashboardMap::getDate));
			paginationContentList.setRowData(reverseList);
			return new ResponseEntity<>(paginationContentList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(Collections.EMPTY_LIST, HttpStatus.SERVICE_UNAVAILABLE);
		}

	}

	@PostMapping("/report/data")
	public ResponseEntity<?> activeDeviceGraph(@RequestBody GraphRequest graphRequest,
			@RequestParam(name = "Type", required = false) String Type,HttpSession session) {
		Object response = null;
		graphRequest.setPublicIp(session.getAttribute("publicIP").toString());
		graphRequest.setBrowser(session.getAttribute("browser").toString());
		response = analyticsFeign.graph(graphRequest, graphRequest.getPageNo(), graphRequest.getPageSize(),
				graphRequest.getFile());

		log.info(":::::::::graphRequest::::::::" + graphRequest + "::::::response:::::::" + response);

		try {
			Gson gson = new Gson();
			String apiResponse = gson.toJson(response);
			log.info("::::::apiResponse:::::::" + apiResponse);
			if ("OperatorDashboard".equals(Type)) {
				operatorWiseImei = gson.fromJson(apiResponse, OperatorWiseImei.class);
				log.info("::::::graphResponseModel:::::::" + operatorWiseImei);
				OperatorWiseGraphContant paginationContentList = operatorWiseImei.getContent();
				List<OperatorWiseGraphRowData> reverseList = paginationContentList.getRowData();
				Collections.sort(reverseList, Comparator.comparing(OperatorWiseGraphRowData::getDate));
				paginationContentList.setRowData(reverseList);
				log.info("::::::paginationContentList:::::::" + paginationContentList);
				return new ResponseEntity<>(paginationContentList, HttpStatus.OK);
			} else if ("OperatorDatatable".equals(Type)) {
				operatorTableGraph = gson.fromJson(apiResponse, OperatorTableGraph.class);
				log.info("::::::graphResponseModel:::::::" + operatorTableGraph);
				OperatorTableGraphContent paginationContentList = operatorTableGraph.getContent();
				log.info("::::::paginationContentList:::::::" + paginationContentList);
				return new ResponseEntity<>(paginationContentList, HttpStatus.OK);
			} else if ("registeredDeviceGraph".equals(Type)) {
				registeredDeviceGraph = gson.fromJson(apiResponse, RegisteredDeviceGraph.class);
				log.info("::::::graphResponseModel:::::::" + registeredDeviceGraph);
				RegisterDeviceGraphContent paginationContentList = registeredDeviceGraph.getContent();
				
				List<RegisteredDeviceRawList> reverseList = paginationContentList.getRowData();
				Collections.sort(reverseList, Comparator.comparing(RegisteredDeviceRawList::getDate));
				paginationContentList.setRowData(reverseList);
				log.info("::::::paginationContentList:::::::" + paginationContentList);
				return new ResponseEntity<>(paginationContentList, HttpStatus.OK);
			} else {
				activeDeviceGraphResponseModel = gson.fromJson(apiResponse, ActiveDeviceGraphResponseModel.class);
				log.info("::::::graphResponseModel:::::::" + activeDeviceGraphResponseModel);
				ActiveDeviceGraphContent paginationContentList = activeDeviceGraphResponseModel.getContent();
				List<ActiveDeviceGraphRowData> reverseList = paginationContentList.getRowData();
				Collections.sort(reverseList, Comparator.comparing(ActiveDeviceGraphRowData::getDate));
				paginationContentList.setRowData(reverseList);
				log.info("::::::paginationContentList:::::::" + paginationContentList);
				return new ResponseEntity<>(paginationContentList, HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(Collections.EMPTY_LIST, HttpStatus.SERVICE_UNAVAILABLE);
		}
	}

	@PostMapping("/brandModel/data/{featureFlag}")
	public ResponseEntity<?> topBrandModel(@RequestBody GraphRequest graphRequest,
			@PathVariable("featureFlag") String featureFlag,HttpSession session) {
		Object response = null;
		graphRequest.setPublicIp(session.getAttribute("publicIP").toString());
		graphRequest.setBrowser(session.getAttribute("browser").toString());
		response = analyticsFeign.graph(graphRequest, graphRequest.getPageNo(), graphRequest.getPageSize(),
				graphRequest.getFile());
		log.info("featureFlag== " + featureFlag);
		log.info("::::::::: brand model graphRequest::::::::" + graphRequest + "::::::response:::::::" + response);

		try {
			Gson gson = new Gson();
			String apiResponse = gson.toJson(response);
			log.info("::::::apiResponse:::::::" + apiResponse);
			if (featureFlag.equals("Brand") || featureFlag.equals("Model")) {
				brandModelGrapContent = gson.fromJson(apiResponse, BrandContent.class);
				log.info(":::::: brand model graphResponseModel:::::::" + brandModelGrapContent);
				BrandModelGrapContent paginationContentList = brandModelGrapContent.getContent();
				List<BrandModelRowData> reverseList = paginationContentList.getRowData();
				Collections.sort(reverseList, Comparator.comparing(BrandModelRowData::getDate));
				paginationContentList.setRowData(reverseList);
				log.info(":::::: brand model paginationContentList:::::::" + paginationContentList);

				return new ResponseEntity<>(paginationContentList, HttpStatus.OK);
			} else if (featureFlag.equals("Stock")) {
				stockContentReport = gson.fromJson(apiResponse, StockContentReport.class);
				log.info(":::::: Stock model graphResponseModel:::::::" + brandModelGrapContent);
				StockModelGrapContent paginationContentList = stockContentReport.getContent();
				/*
				 * List<StockModelRowData> reverseList = paginationContentList.getRowData();
				 * Collections.sort(reverseList,
				 * Comparator.comparing(StockModelRowData::getDate));
				 * paginationContentList.setRowData(reverseList);
				 */
				log.info(":::::: Stock model paginationContentList:::::::" + paginationContentList);
				return new ResponseEntity<>(paginationContentList, HttpStatus.OK);
			} else if (featureFlag.equals("Consignment")) {
				consignmentContent = gson.fromJson(apiResponse, ConsignmentContent.class);
				log.info(":::::: Consignment model graphResponseModel:::::::" + brandModelGrapContent);
				ConsignmentModelGrapContent paginationContentList = consignmentContent.getContent();
				/*
				 * List<ConsignmentModelRowData> reverseList =
				 * paginationContentList.getRowData(); Collections.sort(reverseList,
				 * Comparator.comparing(ConsignmentModelRowData::getDate));
				 * paginationContentList.setRowData(reverseList);
				 */
				log.info(":::::: Consignment model paginationContentList:::::::" + paginationContentList);
				return new ResponseEntity<>(paginationContentList, HttpStatus.OK);
			}

			else if (featureFlag.equals("Grievance")) {
				grievanceContent = gson.fromJson(apiResponse, GrievanceContent.class);
				log.info(":::::: Grievance model graphResponseModel:::::::" + brandModelGrapContent);
				GrievanceGrapContent paginationContentList = grievanceContent.getContent();
				/*
				 * List<GrievanceModelRowData> reverseList = paginationContentList.getRowData();
				 * Collections.sort(reverseList,
				 * Comparator.comparing(GrievanceModelRowData::getDate));
				 * paginationContentList.setRowData(reverseList);
				 */
				log.info(":::::: Grievance model paginationContentList:::::::" + paginationContentList);
				return new ResponseEntity<>(paginationContentList, HttpStatus.OK);
			} else if (featureFlag.equals("UserType")) {
				grievanceUserTypeContent = gson.fromJson(apiResponse, GrievanceUserType.class);
				log.info(":::::: Grievance user model graphResponseModel:::::::" + grievanceUserTypeContent);
				GrievanceUserTypeModelGrapContent paginationContentList = grievanceUserTypeContent.getContent();
				/*
				 * List<GrievanceUserTpeModelRowData> reverseList =
				 * paginationContentList.getRowData(); Collections.sort(reverseList,
				 * Comparator.comparing(GrievanceUserTpeModelRowData::getDate));
				 * paginationContentList.setRowData(reverseList);
				 */
				log.info(":::::: Grievance user model paginationContentList:::::::" + paginationContentList);
				return new ResponseEntity<>(paginationContentList, HttpStatus.OK);
			} else if (featureFlag.equals("BlocedkIMEI")) {
				blockIMEIContent = gson.fromJson(apiResponse, BlockIMEIContent.class);
				log.info(":::::: blockIMEIContent user model graphResponseModel:::::::" + blockIMEIContent);
				BlockIMEIGraph paginationContentList = blockIMEIContent.getContent();
				
				/*
				 * List<BlockIMEIRowData> reverseList = paginationContentList.getRowData();
				 * Collections.sort(reverseList,
				 * Comparator.comparing(BlockIMEIRowData::getDate));
				 * paginationContentList.setRowData(reverseList);
				 */		
				
				log.info(":::::: blockIMEIContent user model paginationContentList:::::::" + paginationContentList);
				return new ResponseEntity<>(paginationContentList, HttpStatus.OK);
			} else if (featureFlag.equals("MostStolen")) {
				mostStolenBrandContent = gson.fromJson(apiResponse, MostStolenBrandContent.class);
				log.info(":::::: mostStolenBrandContent user model graphResponseModel:::::::" + blockIMEIContent);
				StolenGraphContent paginationContentList = mostStolenBrandContent.getContent();
				List<MostStolenRowData> reverseList = paginationContentList.getRowData();
				Collections.sort(reverseList, Comparator.comparing(MostStolenRowData::getDate));
				paginationContentList.setRowData(reverseList);
				log.info(":::::: mostStolenBrandContent user model paginationContentList:::::::"
						+ paginationContentList);
				return new ResponseEntity<>(paginationContentList, HttpStatus.OK);

			}
		}

		catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(Collections.EMPTY_LIST, HttpStatus.SERVICE_UNAVAILABLE);
		}
		return null;
	}

	@PostMapping("/report/imeiUsageDashBoard")
	public ResponseEntity<?> imeiUsageDashBoard(@RequestBody GraphRequest graphRequest,HttpSession session) {
		Object response = null;
		graphRequest.setPublicIp(session.getAttribute("publicIP").toString());
		graphRequest.setBrowser(session.getAttribute("browser").toString());
		response = analyticsFeign.graph(graphRequest, graphRequest.getPageNo(), graphRequest.getPageSize(),
				graphRequest.getFile());

		log.info(":::::::::graphRequest::::::::" + graphRequest + "::::::response:::::::" + response);

		try {
			Gson gson = new Gson();
			String apiResponse = gson.toJson(response);
			log.info("::::::apiResponse:::::::" + apiResponse);

			imeiUsageGraphResponseModel = gson.fromJson(apiResponse, ImeiUsageGraphResponseModel.class);
			log.info("::::::imeiUsageGraphResponseModel:::::::" + imeiUsageGraphResponseModel);
			ImeiUsageGraphContent paginationContentList = imeiUsageGraphResponseModel.getContent();
			log.info("::::::paginationContentList:::::::" + paginationContentList);
			return new ResponseEntity<>(paginationContentList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(Collections.EMPTY_LIST, HttpStatus.SERVICE_UNAVAILABLE);
		}
	}
}
