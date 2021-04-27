package org.gl.ceir.datatable.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.gl.ceir.CeirPannelCode.Feignclient.GrievanceFeignClient;
import org.gl.ceir.CeirPannelCode.Model.FilterRequest;
import org.gl.ceir.CeirPannelCode.Model.UserHeader;
import org.gl.ceir.CeirPannelCode.Service.RegistrationService;
import org.gl.ceir.Class.HeadersTitle.DatatableResponseModel;
import org.gl.ceir.Class.HeadersTitle.IconsState;
import org.gl.ceir.configuration.ConfigParameters;
import org.gl.ceir.configuration.Translator;
import org.gl.ceir.pageElement.model.Button;
import org.gl.ceir.pageElement.model.InputFields;
import org.gl.ceir.pageElement.model.PageElement;
import org.gl.ceir.pagination.model.GrievanceContentModel;
import org.gl.ceir.pagination.model.GrievancePaginationModel;
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
public class GrievanceDatatableController {
	private final Logger log = LoggerFactory.getLogger(getClass());
	String className = "emptyClass";
	@Autowired
	Translator Translator;
	@Autowired
	DatatableResponseModel datatableResponseModel;
	@Autowired
	PageElement pageElement;
	@Autowired
	Button button;
	@Autowired
	GrievanceContentModel grievancecontentmodel;
	@Autowired
	IconsState iconState;
	@Autowired
	GrievancePaginationModel grievancepaginationmodel;
	@Autowired
	GrievanceFeignClient grievanceFeignClient;
	@Autowired
	RegistrationService registerService;

	@PostMapping("grievanceData")
	public ResponseEntity<?> viewStockList(
			@RequestParam(name = "type", defaultValue = "grievance", required = false) String role,
			HttpServletRequest request, HttpSession session,
			@RequestParam(name = "grievanceSessionUsesFlag", required = false) Integer grievanceSessionUsesFlag,
			@RequestParam(name="source",defaultValue = "menu",required = false) String source) {

		// Data set on this List
		List<List<Object>> finalList = new ArrayList<List<Object>>();
		String filter = request.getParameter("filter");
		Gson gsonObject = new Gson();
		FilterRequest filterrequest = gsonObject.fromJson(filter, FilterRequest.class);
		Integer pageSize = Integer.parseInt(request.getParameter("length"));
		Integer pageNo = Integer.parseInt(request.getParameter("start")) / pageSize;
		filterrequest.setSearchString(request.getParameter("search[value]"));
		
		//filterrequest.setOrderColumnName(request.getParameter("order[0][column]") == null ? "Modified On" : request.getParameter("order[0][column]"));
		//filterrequest.setOrder(request.getParameter("order[0][dir]") == null ? "desc" : request.getParameter("order[0][dir]"));
		String userType = (String) session.getAttribute("usertype");
		if(userType!=null){
			log.info("inside if----->" +userType);
			userType = (String) session.getAttribute("usertype");
		}else {
			log.info("inside else----->" +userType);
			userType = "End User";
		}
		log.info("user type is ______________" +userType);
		filterrequest.setUserType(userType);
		
		String column;
		if("CEIRAdmin".equals(userType)){
		   column="0".equalsIgnoreCase(request.getParameter("order[0][column]")) ? "Created On":
				"1".equalsIgnoreCase(request.getParameter("order[0][column]")) ? "Modified On":
					"2".equalsIgnoreCase(request.getParameter("order[0][column]")) ? "Transaction ID":
						"3".equalsIgnoreCase(request.getParameter("order[0][column]")) ? "Grievance ID":
							"4".equalsIgnoreCase(request.getParameter("order[0][column]")) ? "User ID":
								"5".equalsIgnoreCase(request.getParameter("order[0][column]")) ? "Raised By":
									"6".equalsIgnoreCase(request.getParameter("order[0][column]")) ? "User Type" :
										"7".equalsIgnoreCase(request.getParameter("order[0][column]")) ? "Status"
									:"Modified On";
		}else {
			column="0".equalsIgnoreCase(request.getParameter("order[0][column]")) ? "Created On":
				"1".equalsIgnoreCase(request.getParameter("order[0][column]")) ? "Modified On":
					"2".equalsIgnoreCase(request.getParameter("order[0][column]")) ? "Transaction ID":
						"3".equalsIgnoreCase(request.getParameter("order[0][column]")) ? "Grievance ID":
							"4".equalsIgnoreCase(request.getParameter("order[0][column]")) ? "Status"
									:"Modified On";
		}
		log.info("---->"+request.getParameter("order[0][column]")+"============>"+request.getParameter("order[0][dir]"));
		
		
		String order;
		if ("Modified On".equalsIgnoreCase(column) && request.getParameter("order[0][dir]")==null) {
			order = "desc";
		} 
		else if("Modified On".equalsIgnoreCase(column) && request.getParameter("order[0][dir]")=="asc"){
			order ="asc";
		}
		else {
			order = request.getParameter("order[0][dir]");
		}
		filterrequest.setOrderColumnName(column);
		filterrequest.setOrder(order);
		
		
		Integer file = 0;
		Object response;
		log.info("session value user Type==" + session.getAttribute("usertype") + " grievanceSessionUsesFlag=="
				+ grievanceSessionUsesFlag);
		
		Integer userId = (Integer) session.getAttribute("userid");
		
		UserHeader header=registerService.getUserHeaders(request);
		filterrequest.setPublicIp(header.getPublicIp());
		filterrequest.setBrowser(header.getBrowser());
		
		log.info("request parameters send to view grievance api=" + filterrequest);
		response = grievanceFeignClient.grievanceFilter(filterrequest,pageNo,pageSize,file,source);
		log.info("response::::::::::::::" + response);
		try {
			Gson gson = new Gson();
			String apiResponse = gson.toJson(response);
			grievancepaginationmodel = gson.fromJson(apiResponse, GrievancePaginationModel.class);
			List<GrievanceContentModel> paginationContentList = grievancepaginationmodel.getContent();
			if (paginationContentList.isEmpty()) {
				datatableResponseModel.setData(Collections.emptyList());
			} else {
				if ("Importer".equals(userType) || "TRC".equals(userType)) {
					log.info("<><><><> in Importer Controller AND TRC CONTROLLER");
					for (GrievanceContentModel dataInsideList : paginationContentList) {
						String createdOn = dataInsideList.getCreatedOn();
						String modifiedOn = dataInsideList.getModifiedOn();
						//String txnId = dataInsideList.getTxnId();
						String txnId = dataInsideList.getTxnId() == null ? "NA" : dataInsideList.getTxnId();
						String grievanceId = String.valueOf(dataInsideList.getGrievanceId());
						String StatusofGrievance = String.valueOf(dataInsideList.getGrievanceStatus());
						String grievanceStatus = dataInsideList.getStateInterp();
						String userStatus = (String) session.getAttribute("userStatus");
						String action = iconState.grievanceState(dataInsideList.getFileName(), txnId, grievanceId,
								StatusofGrievance, userStatus, userId);
						Object[] finalData = { createdOn, modifiedOn, txnId, grievanceId, grievanceStatus, action };
						List<Object> finalDataList = new ArrayList<Object>(Arrays.asList(finalData));
						finalList.add(finalDataList);
						datatableResponseModel.setData(finalList);
					}
				} else if ("Custom".equals(userType)) {
					log.info("<><><><> in Custom Controller");
					for (GrievanceContentModel dataInsideList : paginationContentList) {
						String createdOn = dataInsideList.getCreatedOn();
						String modifiedOn = dataInsideList.getModifiedOn();
						String txnId = dataInsideList.getTxnId() == null ? "NA" : dataInsideList.getTxnId();
						String grievanceId = String.valueOf(dataInsideList.getGrievanceId());
						String StatusofGrievance = String.valueOf(dataInsideList.getGrievanceStatus());
						String grievanceStatus = dataInsideList.getStateInterp();
						String userStatus = (String) session.getAttribute("userStatus");
						String action = iconState.customGrievanceState(dataInsideList.getFileName(), txnId, grievanceId,
								StatusofGrievance, userStatus, userId);
						Object[] finalData = { createdOn, modifiedOn, txnId, grievanceId, grievanceStatus, action };
						List<Object> finalDataList = new ArrayList<Object>(Arrays.asList(finalData));
						finalList.add(finalDataList);
						datatableResponseModel.setData(finalList);
					}

				} else if ("CEIRAdmin".equals(userType)) {
					log.info("<><><><> in CEIRAdmin Controller");
					for (GrievanceContentModel dataInsideList : paginationContentList) {
						String createdOn = dataInsideList.getCreatedOn();
						String modifiedOn = dataInsideList.getModifiedOn();
						String txnId = dataInsideList.getTxnId() == null ? "NA" : dataInsideList.getTxnId();
						//String userDisplayName = dataInsideList.getUserDisplayName();
						String userName = dataInsideList.getUserDisplayName();
						String userTypeName = dataInsideList.getUserType();
						String grievanceId = String.valueOf(dataInsideList.getGrievanceId());
						String StatusofGrievance = String.valueOf(dataInsideList.getGrievanceStatus());
						String grievanceStatus = dataInsideList.getStateInterp();
						String raisedBy = dataInsideList.getRaisedBy();
						String userStatus = (String) session.getAttribute("userStatus");
						String action = iconState.adminGrievanceState(dataInsideList.getFileName(), txnId, grievanceId,
						StatusofGrievance, userStatus, userId);
						Object[] finalData = { createdOn, modifiedOn, txnId, grievanceId, userName,raisedBy, userTypeName, grievanceStatus, action };
						List<Object> finalDataList = new ArrayList<Object>(Arrays.asList(finalData));
						finalList.add(finalDataList);
						datatableResponseModel.setData(finalList);
					}
				} else if ("Retailer".equals(userType)) {
					log.info("<><><><> in Retailer Greivance Controller");
					for (GrievanceContentModel dataInsideList : paginationContentList) {
						String createdOn = dataInsideList.getCreatedOn();
						String modifiedOn = dataInsideList.getModifiedOn();
						String txnId = dataInsideList.getTxnId() == null ? "NA" : dataInsideList.getTxnId();
						String grievanceId = String.valueOf(dataInsideList.getGrievanceId());
						String StatusofGrievance = String.valueOf(dataInsideList.getGrievanceStatus());
						String grievanceStatus = dataInsideList.getStateInterp();
						String userStatus = (String) session.getAttribute("userStatus");
						String action = iconState.grievanceState(dataInsideList.getFileName(), txnId, grievanceId,
								StatusofGrievance, userStatus, userId);
						Object[] finalData = { createdOn, modifiedOn, txnId, grievanceId, grievanceStatus, action };
						List<Object> finalDataList = new ArrayList<Object>(Arrays.asList(finalData));
						finalList.add(finalDataList);
						datatableResponseModel.setData(finalList);
					}
				}
				
				else if ("Distributor".equals(userType)) {
					log.info("<><>Distributor<><> in Greivance Controller");
					for (GrievanceContentModel dataInsideList : paginationContentList) {
						String createdOn = dataInsideList.getCreatedOn();
						String modifiedOn = dataInsideList.getModifiedOn();
						String txnId = dataInsideList.getTxnId() == null ? "NA" : dataInsideList.getTxnId();
						String grievanceId = String.valueOf(dataInsideList.getGrievanceId());
						String StatusofGrievance = String.valueOf(dataInsideList.getGrievanceStatus());
						String grievanceStatus = dataInsideList.getStateInterp();
						String userStatus = (String) session.getAttribute("userStatus");
						String action = iconState.grievanceState(dataInsideList.getFileName(), txnId, grievanceId,
								StatusofGrievance, userStatus, userId);
						Object[] finalData = { createdOn, modifiedOn, txnId, grievanceId, grievanceStatus, action };
						List<Object> finalDataList = new ArrayList<Object>(Arrays.asList(finalData));
						finalList.add(finalDataList);
						datatableResponseModel.setData(finalList);
					}
				}
				
				else if ("Operator".equals(userType)) {
					log.info("<><><><> in Operator Greivance Controller");
					for (GrievanceContentModel dataInsideList : paginationContentList) {
					String createdOn = dataInsideList.getCreatedOn();
					String modifiedOn = dataInsideList.getModifiedOn();
					String txnId = dataInsideList.getTxnId() == null ? "NA" : dataInsideList.getTxnId();
					String grievanceId = String.valueOf(dataInsideList.getGrievanceId());
					String StatusofGrievance = String.valueOf(dataInsideList.getGrievanceStatus());
					String grievanceStatus = dataInsideList.getStateInterp();
					String userStatus = (String) session.getAttribute("userStatus");
					String action = iconState.grievanceState(dataInsideList.getFileName(), txnId, grievanceId,
					StatusofGrievance, userStatus, userId);
					Object[] finalData = { createdOn, modifiedOn, txnId, grievanceId, grievanceStatus, action };
					List<Object> finalDataList = new ArrayList<Object>(Arrays.asList(finalData));
					finalList.add(finalDataList);
					datatableResponseModel.setData(finalList);
					}
					}
				else if ("Manufacturer".equals(userType)) { 
					log.info("<><><><> in Manufacturer CONTROLLER");
					for (GrievanceContentModel dataInsideList : paginationContentList) {
						String createdOn = dataInsideList.getCreatedOn();
						String modifiedOn = dataInsideList.getModifiedOn();
						String txnId = dataInsideList.getTxnId() == null ? "NA" : dataInsideList.getTxnId();
						String grievanceId = String.valueOf(dataInsideList.getGrievanceId());
						String StatusofGrievance = String.valueOf(dataInsideList.getGrievanceStatus());
						String grievanceStatus = dataInsideList.getStateInterp();
						String userStatus = (String) session.getAttribute("userStatus");
						String action = iconState.grievanceState(dataInsideList.getFileName(), txnId, grievanceId,
								StatusofGrievance, userStatus, userId);
						Object[] finalData = { createdOn, modifiedOn, txnId, grievanceId, grievanceStatus, action };
						List<Object> finalDataList = new ArrayList<Object>(Arrays.asList(finalData));
						finalList.add(finalDataList);
						datatableResponseModel.setData(finalList);
					}
				} else if (userType.equals("Lawful Agency")) { 
					log.info("<><><><> in Lawful Agency CONTROLLER");
					for (GrievanceContentModel dataInsideList : paginationContentList) {
						String createdOn = dataInsideList.getCreatedOn();
						String modifiedOn = dataInsideList.getModifiedOn();
						String txnId = dataInsideList.getTxnId() == null ? "NA" : dataInsideList.getTxnId();
						String grievanceId = String.valueOf(dataInsideList.getGrievanceId());
						String StatusofGrievance = String.valueOf(dataInsideList.getGrievanceStatus());
						String grievanceStatus = dataInsideList.getStateInterp();
						String userStatus = (String) session.getAttribute("userStatus");
						String action = iconState.grievanceState(dataInsideList.getFileName(), txnId, grievanceId,
								StatusofGrievance, userStatus, userId);
						Object[] finalData = { createdOn, modifiedOn, txnId, grievanceId, grievanceStatus, action };
						List<Object> finalDataList = new ArrayList<Object>(Arrays.asList(finalData));
						finalList.add(finalDataList);
						datatableResponseModel.setData(finalList);
					}
				}
				
				else if (userType.equals("End User")) { 
					log.info("End User request send to view api&&&&&&&&&&&&");
					for (GrievanceContentModel dataInsideList : paginationContentList) {
						String createdOn = dataInsideList.getCreatedOn();
						String modifiedOn = dataInsideList.getModifiedOn();
						String txnId = dataInsideList.getTxnId() == null ? "NA" : dataInsideList.getTxnId();
						String grievanceId = String.valueOf(dataInsideList.getGrievanceId());
						String StatusofGrievance = String.valueOf(dataInsideList.getGrievanceStatus());
						String grievanceStatus = dataInsideList.getStateInterp();
						Integer endUserId=dataInsideList.getUserId();
						log.info("**********createdOn=="+createdOn+"  modifiedOn=="+modifiedOn+" txnId=="+txnId+"grievanceId=="+grievanceId+"StatusofGrievance=="+StatusofGrievance);
						String action = iconState.endUserGrievanceState(dataInsideList.getFileName(), txnId, grievanceId,endUserId,StatusofGrievance);
						log.info("--------------response*******"+action);
						Object[] finalData = { createdOn, modifiedOn, txnId, grievanceId, grievanceStatus, action };
						List<Object> finalDataList = new ArrayList<Object>(Arrays.asList(finalData));
						finalList.add(finalDataList);
						datatableResponseModel.setData(finalList);
					}
				}else if (userType.equals("Customer Care")) {
					log.info("<><><><> in Customer Care Controller");
					for (GrievanceContentModel dataInsideList : paginationContentList) {
						String createdOn = dataInsideList.getCreatedOn();
						String modifiedOn = dataInsideList.getModifiedOn();
						String txnId = dataInsideList.getTxnId() == null ? "NA" : dataInsideList.getTxnId();
						//String userDisplayName = dataInsideList.getUserDisplayName();
						String userName = dataInsideList.getUserDisplayName();
						String userTypeName = dataInsideList.getUserType();
						String grievanceId = String.valueOf(dataInsideList.getGrievanceId());
						String raisedBy = dataInsideList.getRaisedBy();
						String StatusofGrievance = String.valueOf(dataInsideList.getGrievanceStatus());
						String grievanceStatus = dataInsideList.getStateInterp();
						String userStatus = (String) session.getAttribute("userStatus");
						String action = iconState.customerCareGrievanceState(dataInsideList.getFileName(), txnId, grievanceId,
						StatusofGrievance, userStatus, userId);
						Object[] finalData = { createdOn, modifiedOn, txnId, userName, userTypeName, grievanceId, raisedBy, grievanceStatus, action };
						List<Object> finalDataList = new ArrayList<Object>(Arrays.asList(finalData));
						finalList.add(finalDataList);
						datatableResponseModel.setData(finalList);
					}
				}else if("Immigration".equals(userType)) {
					log.info("<><><><> in Immigration Greivance Controller");
					for (GrievanceContentModel dataInsideList : paginationContentList) {
					String createdOn = dataInsideList.getCreatedOn();
					String modifiedOn = dataInsideList.getModifiedOn();
					String txnId = dataInsideList.getTxnId() == null ? "NA" : dataInsideList.getTxnId();
					String grievanceId = String.valueOf(dataInsideList.getGrievanceId());
					String StatusofGrievance = String.valueOf(dataInsideList.getGrievanceStatus());
					String grievanceStatus = dataInsideList.getStateInterp();
					String userStatus = (String) session.getAttribute("userStatus");	
					String action = iconState.grievanceState(dataInsideList.getFileName(), txnId, grievanceId,
					StatusofGrievance, userStatus, userId);
					Object[] finalData = { createdOn, modifiedOn, txnId, grievanceId, grievanceStatus, action };
					List<Object> finalDataList = new ArrayList<Object>(Arrays.asList(finalData));
					finalList.add(finalDataList);
					datatableResponseModel.setData(finalList);
					}
				}
			}
			// data set on ModelClass
			datatableResponseModel.setRecordsTotal(grievancepaginationmodel.getNumberOfElements());
			datatableResponseModel.setRecordsFiltered(grievancepaginationmodel.getTotalElements());
			return new ResponseEntity<>(datatableResponseModel, HttpStatus.OK);

		} catch (Exception e) {
			datatableResponseModel.setRecordsTotal(null);
			datatableResponseModel.setRecordsFiltered(null);
			datatableResponseModel.setData(Collections.emptyList());
			return new ResponseEntity<>(datatableResponseModel, HttpStatus.OK);
		}

	}

	@PostMapping("grievance/pageRendering")
	public ResponseEntity<?> pageRendering(
			@RequestParam(name = "type", defaultValue = "consignment", required = false) String role,
			HttpSession session) {

		String userType = (String) session.getAttribute("usertype");
		String userStatus = (String) session.getAttribute("userStatus");

		InputFields inputFields = new InputFields();
		InputFields dateRelatedFields;

		pageElement.setPageTitle(Translator.toLocale("view.griev"));

		List<Button> buttonList = new ArrayList<>();
		List<InputFields> dropdownList = new ArrayList<>();
		List<InputFields> inputTypeDateList = new ArrayList<>();

		log.info("USER STATUS:::::::::" + userStatus);
		log.info("session value user Type==" + session.getAttribute("usertype"));

		
		if(userType.equals("Customer Care")) {
			String[] names = { "HeaderButton", Translator.toLocale("button.reportGriev"), "./searchUserNameForm", "btnLink",
					"FilterButton", Translator.toLocale("button.filter"), "grievanceDataTable("+ConfigParameters.languageParam+",'filter')", "submitFilter" };
			for (int i = 0; i < names.length; i++) {
				button = new Button();
				button.setType(names[i]);
				i++;
				button.setButtonTitle(names[i]);
				i++;
				button.setButtonURL(names[i]);
				i++;
				button.setId(names[i]);
				buttonList.add(button);
			}
			pageElement.setButtonList(buttonList);
			
		}else {
			String[] names = { "HeaderButton", Translator.toLocale("button.reportGriev"), "./openGrievanceForm?reqType=formPage", "btnLink",
					"FilterButton", Translator.toLocale("button.filter"), "grievanceDataTable("+ConfigParameters.languageParam+")", "submitFilter" };
			for (int i = 0; i < names.length; i++) {
				button = new Button();
				button.setType(names[i]);
				i++;
				button.setButtonTitle(names[i]);
				i++;
				button.setButtonURL(names[i]);
				i++;
				button.setId(names[i]);
				buttonList.add(button);
			}
			pageElement.setButtonList(buttonList);
			
		} 
		
		if("CEIRAdmin".equals(userType) || userType.equals("Customer Care")) {
			// Dropdown items
			String[] selectParam = {"select", Translator.toLocale("table.userType"), "userType", "","select", Translator.toLocale("table.status"), "recentStatus", "" };
			for (int i = 0; i < selectParam.length; i++) {
				inputFields = new InputFields();
				inputFields.setType(selectParam[i]);
				i++;
				inputFields.setTitle(selectParam[i]);
				i++;
				inputFields.setId(selectParam[i]);
				i++;
				inputFields.setClassName(selectParam[i]);
				dropdownList.add(inputFields);
			}
			pageElement.setDropdownList(dropdownList);

			// input type date list
			String[] dateParam = { "date", Translator.toLocale("input.startDate"), "startDate", "", "date", Translator.toLocale("input.endDate"), "endDate", "", "text",
					Translator.toLocale("input.transactionID"), "transactionID", "", "text",Translator.toLocale("table.grievanceID"), "grievanceID", "","text",Translator.toLocale("table.UserName"), "userName","","text",Translator.toLocale("table.raisedBy"), "raisedby", "" };
			for (int i = 0; i < dateParam.length; i++) {
				dateRelatedFields = new InputFields();
				dateRelatedFields.setType(dateParam[i]);
				i++;
				dateRelatedFields.setTitle(dateParam[i]);
				i++;
				dateRelatedFields.setId(dateParam[i]);
				i++;
				dateRelatedFields.setClassName(dateParam[i]);
				inputTypeDateList.add(dateRelatedFields);
			}
			
		
		 
		}else {
			// Dropdown items
			String[] selectParam = { "select", Translator.toLocale("button.grievStatus"), "recentStatus", "" };
			for (int i = 0; i < selectParam.length; i++) {
				inputFields = new InputFields();
				inputFields.setType(selectParam[i]);
				i++;
				inputFields.setTitle(selectParam[i]);
				i++;
				inputFields.setId(selectParam[i]);
				i++;
				inputFields.setClassName(selectParam[i]);
				dropdownList.add(inputFields);
			}
			pageElement.setDropdownList(dropdownList);

			// input type date list
			String[] dateParam = { "date", Translator.toLocale("input.startDate"), "startDate", "", "date", Translator.toLocale("input.endDate"), "endDate", "", "text",
					Translator.toLocale("input.transactionID"), "transactionID", "", "text",Translator.toLocale("table.grievanceID"), "grievanceID", "" };
			for (int i = 0; i < dateParam.length; i++) {
				dateRelatedFields = new InputFields();
				dateRelatedFields.setType(dateParam[i]);
				i++;
				dateRelatedFields.setTitle(dateParam[i]);
				i++;
				dateRelatedFields.setId(dateParam[i]);
				i++;
				dateRelatedFields.setClassName(dateParam[i]);
				inputTypeDateList.add(dateRelatedFields);
			}
			
		}
		
		

		pageElement.setInputTypeDateList(inputTypeDateList);
		pageElement.setUserStatus(userStatus);
		return new ResponseEntity<>(pageElement, HttpStatus.OK);

	}

}
