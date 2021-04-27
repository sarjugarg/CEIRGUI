package org.gl.ceir.datatable.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.gl.ceir.CeirPannelCode.Feignclient.UploadPaidStatusFeignClient;
import org.gl.ceir.CeirPannelCode.Model.AllRequest;
import org.gl.ceir.CeirPannelCode.Model.ConsignmentModel;
import org.gl.ceir.CeirPannelCode.Model.FilterRequest_UserPaidStatus;
import org.gl.ceir.CeirPannelCode.Model.GenricResponse;
import org.gl.ceir.CeirPannelCode.Model.UserHeader;
import org.gl.ceir.CeirPannelCode.Service.RegistrationService;
import org.gl.ceir.Class.HeadersTitle.DatatableResponseModel;
import org.gl.ceir.Class.HeadersTitle.IconsState;
import org.gl.ceir.configuration.ConfigParameters;
import org.gl.ceir.configuration.Translator;
import org.gl.ceir.pageElement.model.Button;
import org.gl.ceir.pageElement.model.InputFields;
import org.gl.ceir.pageElement.model.PageElement;
import org.gl.ceir.pagination.model.UserPaidStatusContent;
import org.gl.ceir.pagination.model.UserPaidStatusPaginationModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

@CrossOrigin
@RestController
public class UploadPaidStatus {
	@Autowired
	Translator Translator;
	@Autowired
	UploadPaidStatusFeignClient uploadPaidStatusFeignClient;

	@Autowired
	DatatableResponseModel datatableResponseModel;

	@Autowired
	IconsState iconState;

	@Autowired
	PageElement pageElement;

	@Autowired
	Button button;
	@Autowired
	RegistrationService registerService;
	
	private final Logger log = LoggerFactory.getLogger(getClass());

	@GetMapping("/paid-status/{nid}")
	public ResponseEntity<?> respone(@PathVariable("nid") String nid, HttpSession session) {
		AllRequest request = new AllRequest();
		log.info("Nid/PassportNumber" + nid);
		String userType = (String) session.getAttribute("usertype");
		String userName = session.getAttribute("username").toString();
		int userId = (int) session.getAttribute("userid");
		int userTypeid = (int) session.getAttribute("usertypeId");

		request.setFeatureId(12);
		request.setUserId(userId);
		request.setNid(nid);
		request.setUsername(userName);
		request.setUserTypeId(userTypeid);
		request.setUserType(userType);
		request.setPublicIp(session.getAttribute("publicIP").toString());
		request.setBrowser(session.getAttribute("browser").toString());
		GenricResponse response = uploadPaidStatusFeignClient.respone(request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/endUserpaid-status/{nid}")
	public ResponseEntity<?> endUserpaid(@PathVariable("nid") String nid, HttpSession session,HttpServletRequest requestHeader) {
		AllRequest request = new AllRequest();
		log.info("Nid/PassportNumber" + nid);
		request.setFeatureId(12);
		request.setNid(nid);
		request.setUserTypeId(17);
		request.setUsername("NA");
		request.setUserType("End User");
		UserHeader header=registerService.getUserHeaders(requestHeader);
		request.setPublicIp(header.getPublicIp());
		request.setBrowser(header.getBrowser());
		GenricResponse response = uploadPaidStatusFeignClient.respone(request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ResponseBody
	@PostMapping("/user-paid-status-data")
	public ResponseEntity<?> view(@RequestParam(name = "file", defaultValue = "0", required = false) Integer file,
			HttpServletRequest request, HttpSession session,
			@RequestParam(name = "source", required = false) String source) {
		// TODO Auto-generated method stub
		String filter = request.getParameter("filter");
		Object response = null;
		Gson gsonObject = new Gson();
		Gson gson = new Gson();
		FilterRequest_UserPaidStatus filterrequest = gsonObject.fromJson(filter, FilterRequest_UserPaidStatus.class);
		String userType = filterrequest.getUserType();

		List<List<Object>> finalList = new ArrayList<List<Object>>();

		Integer pageSize = Integer.parseInt(request.getParameter("length"));
		Integer pageNo = Integer.parseInt(request.getParameter("start")) / pageSize;
		Integer userId = (Integer) session.getAttribute("userid");
		Integer userTypeId = (Integer) session.getAttribute("usertypeId");
		String userStatus = (String) session.getAttribute("userStatus");
		log.info("userId===" + userId);

		log.info("userType in uploadPaidStatus" + userType + "  source==" + source);

		/*
		 * filterrequest.setUserId(userId); filterrequest.setUserTypeId(userTypeId);
		 */

		String column = "0".equalsIgnoreCase(request.getParameter("order[0][column]")) ? "Date"
				: "1".equalsIgnoreCase(request.getParameter("order[0][column]")) ? "NID/Passport No."
						: "2".equalsIgnoreCase(request.getParameter("order[0][column]")) ? "Transaction ID"
								: "3".equalsIgnoreCase(request.getParameter("order[0][column]")) ? "Nationality"
										: "4".equalsIgnoreCase(request.getParameter("order[0][column]"))
												? "Tax Paid Status"
												: "5".equalsIgnoreCase(request.getParameter("order[0][column]"))
														? "Origin"
														: "6".equalsIgnoreCase(request.getParameter("order[0][column]"))
																? "Status"
																: "Modified On";
		String order;
		if ("Modified On".equalsIgnoreCase(column)) {
			order = "desc";
		} else {
			order = request.getParameter("order[0][dir]");
		} 
		filterrequest.setColumnName(column);
		filterrequest.setSort(order);

		filterrequest.setSearchString(request.getParameter("search[value]"));
		UserHeader header=registerService.getUserHeaders(request);
		filterrequest.setPublicIp(header.getPublicIp());
		filterrequest.setBrowser(header.getBrowser());
		log.info("filterrequest--->" + filterrequest);
		
		response = uploadPaidStatusFeignClient.view(filterrequest, pageNo, pageSize, file, source);
		log.info("request passed to the filter api  =" + filterrequest);
		String apiResponse = gson.toJson(response);
		log.info("response filter api  =" + apiResponse);
		// filterrequest.setSearchString(request.getParameter("search[value]"));
		try {
			UserPaidStatusPaginationModel upsPaginationModel = gson.fromJson(apiResponse,
					UserPaidStatusPaginationModel.class);
			List<UserPaidStatusContent> contentList = upsPaginationModel.getContent();
			if (contentList.isEmpty()) {
				datatableResponseModel.setData(Collections.emptyList());
			} else if ("Custom".equals(userType) || "DRT".equals(userType)) {
				log.info("in Custom Userpaid Status---" + userType);
				log.info("~~~~~~~~~~~~~~~ 1 ~~~~~~~~~~~~~~~~~~~");

				for (UserPaidStatusContent contentModelList : contentList) {
					String nid = contentModelList.getNid();
					String txnId = contentModelList.getTxnId();
					// Integer sno = contentModelList.getId();
					String createdOn = contentModelList.getCreatedOn();
					// String deviceIDInterp = contentModelList.getDeviceIdTypeInterp();
					// String deviceTypeInterp = contentModelList.getDeviceTypeInterp();
					// String currency = contentModelList.getCurrencyInterp() == null ? "" :
					// contentModelList.getCurrencyInterp();
					// String price = currency.concat(String.valueOf(contentModelList.getPrice()));
					// String country = contentModelList.getCountry();
					String taxStatus = String.valueOf(contentModelList.getTaxPaidStatus());
					String taxStatusInterp = contentModelList.getTaxPaidStatusInterp();
					String origin = contentModelList.getOrigin();
					String nationality = contentModelList.getNationality();
					String statusInterp = contentModelList.getStateInterp();
					String status = String.valueOf(contentModelList.getStatus());
					// params for action
					String imei1 = contentModelList.getFirstImei();
					String action = iconState.userPaidStatusIcon(imei1, taxStatus, status, userStatus, txnId, source);

					Object[] data = { createdOn, nid, txnId, nationality, taxStatusInterp, origin, statusInterp,
							action };

					List<Object> datatableList = Arrays.asList(data);
					finalList.add(datatableList);
					datatableResponseModel.setData(finalList);
				}
			} else if ("CEIRAdmin".equals(userType)) {
				log.info("~~~~~~~~~~~~~~~ 2 ~~~~~~~~~~~~~~~~~~~");
				for (UserPaidStatusContent contentModelList : contentList) {
					// Integer sno = contentModelList.getId();
					String createdOn = contentModelList.getCreatedOn();
					String nid = contentModelList.getNid();
					String txnId = contentModelList.getTxnId();
					String deviceIDInterp = contentModelList.getDeviceIdTypeInterp();
					// String deviceTypeInterp = contentModelList.getDeviceTypeInterp();
					String currency = contentModelList.getCurrencyInterp() == null ? ""
							: contentModelList.getCurrencyInterp();
					String price = currency.concat(String.valueOf(contentModelList.getPrice()));
					String country = contentModelList.getCountry();
					String taxStatus = contentModelList.getTaxPaidStatusInterp();
					String status = contentModelList.getStateInterp();
					String origin = contentModelList.getOrigin();
					String nationality = contentModelList.getNationality();
					// params for action
					String imei1 = contentModelList.getFirstImei();
					String deviceState = String.valueOf(contentModelList.getStatus());
					String action = iconState.adminUserPaidStatusIcon(imei1, createdOn, contentModelList.getTxnId(),
							deviceState, userStatus, source);

					Object[] data = { createdOn, nid, txnId, nationality, taxStatus, origin, status, action };

					List<Object> datatableList = Arrays.asList(data);
					finalList.add(datatableList);
					datatableResponseModel.setData(finalList);
				}
			} else if ("Immigration".equals(userType)) {
				log.info("~~~~~~~~~~~~~~~~~ 3 ~~~~~~~~~~~~~~~~~~~~~~");
				for (UserPaidStatusContent contentModelList : contentList) {
					log.info("in Immigration -----> " + userType);
					// Integer sno = contentModelList.getId();
					String createdOn = contentModelList.getCreatedOn();
					String nid = contentModelList.getNid();
					String txnId = contentModelList.getTxnId();
					String deviceIDInterp = contentModelList.getDeviceIdTypeInterp();
					// String deviceTypeInterp = contentModelList.getDeviceTypeInterp();
					String currency = contentModelList.getCurrencyInterp() == null ? ""
							: contentModelList.getCurrencyInterp();
					String price = currency.concat(String.valueOf(contentModelList.getPrice()));
					String country = contentModelList.getCountry();
					String taxStatus = contentModelList.getTaxPaidStatusInterp();
					String status = contentModelList.getStateInterp();
					String origin = contentModelList.getOrigin();
					String nationality = contentModelList.getNationality();
					String statusInterp = contentModelList.getStateInterp();
					// params for action
					String imei1 = contentModelList.getFirstImei();
					String deviceState = String.valueOf(contentModelList.getStatus());
					String action = iconState.deviceActivationIcon(imei1, createdOn, contentModelList.getTxnId(),
							deviceState, userStatus, source);

					Object[] data = { createdOn, nid, txnId, nationality, taxStatus, origin, status, action };

					List<Object> datatableList = Arrays.asList(data);
					finalList.add(datatableList);
					datatableResponseModel.setData(finalList);
				}
			} else if (userType.equals("End User")) {
				log.info("when user type is null");
				log.info("~~~~~~~~~~~~~ 4 ~~~~~~~~~~~~~~~~~~~");
				for (UserPaidStatusContent contentModelList : contentList) {
					String nid = contentModelList.getNid();
					String txnId = contentModelList.getTxnId();
					// Integer sno = contentModelList.getId();
					String createdOn = contentModelList.getCreatedOn();
					String deviceIDInterp = contentModelList.getDeviceIdTypeInterp();
					// String deviceTypeInterp = contentModelList.getDeviceTypeInterp();
					String currency = contentModelList.getCurrencyInterp() == null ? ""
							: contentModelList.getCurrencyInterp();
					String price = currency.concat(String.valueOf(contentModelList.getPrice()));
					String country = contentModelList.getCountry();
					String status = contentModelList.getTaxPaidStatusInterp();
					String origin = contentModelList.getOrigin();
					String nationality = contentModelList.getNationality();
					String statusInterp = contentModelList.getStateInterp();
					// params for action
					String imei1 = contentModelList.getFirstImei();
					String deviceState = String.valueOf(contentModelList.getStatus());
					String action = iconState.endUserPaidStatusIcon(imei1, userStatus, deviceState, txnId);
					log.info("in end user data table controller  Status---");

					Object[] data = { createdOn, nid, txnId, nationality, status, origin, statusInterp, action };

					List<Object> datatableList = Arrays.asList(data);
					finalList.add(datatableList);
					datatableResponseModel.setData(finalList);
				}
			}
			datatableResponseModel.setRecordsTotal(upsPaginationModel.getNumberOfElements());
			datatableResponseModel.setRecordsFiltered(upsPaginationModel.getTotalElements());
			log.info(":::::datatableResponseModel:::::" + datatableResponseModel);
			return new ResponseEntity<>(datatableResponseModel, HttpStatus.OK);
		} catch (Exception e) {
			datatableResponseModel.setRecordsTotal(null);
			datatableResponseModel.setRecordsFiltered(null);
			datatableResponseModel.setData(Collections.emptyList());
			log.info(e.getMessage());
			return new ResponseEntity<>(datatableResponseModel, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("upload-paid-status/pageRendering")
	public ResponseEntity<?> directives(
			@RequestParam(name = "type", defaultValue = "userPaidStatus", required = false) String role,
			HttpSession session) {

		String userType = (String) session.getAttribute("usertype");
		String userStatus = (String) session.getAttribute("userStatus");

		InputFields inputFields = new InputFields();
		InputFields dateRelatedFields;

		if ("Immigration".equals(userType)) {
			pageElement.setPageTitle(Translator.toLocale("customRegisterDevice"));
		} else {
			pageElement.setPageTitle(Translator.toLocale("customRegisterDevice"));
		}

		List<Button> buttonList = new ArrayList<>();
		List<InputFields> dropdownList = new ArrayList<>();
		List<InputFields> inputTypeDateList = new ArrayList<>();

		if ("Immigration".equals(userType)) {
			String[] names = { "HeaderButton", Translator.toLocale("button.register"), "JavaScript:void(0);", "btnLink",
					"FilterButton", Translator.toLocale("button.filter"),
					"filter(" + ConfigParameters.languageParam + ",'filter')", "submitFilter" };
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

		} else {
			String[] names = { "HeaderButton", Translator.toLocale("button.register"), "./add-device-information",
					"btnLink", "FilterButton", Translator.toLocale("button.filter"),
					"filter(" + ConfigParameters.languageParam + ",'filter')", "submitFilter" };
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

		// Dropdown items
		String[] selectParam = { "select", Translator.toLocale("input.Status"), "recordStatus", "", "select",
				Translator.toLocale("select.taxPaidStatus"), "taxPaidStatus", "" };
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

		if ("Custom".equals(userType)) {
			// input type date list
			/*
			 * String[] dateParam = { "date", Translator.toLocale("input.startDate"),
			 * "startDate", "", "date", Translator.toLocale("input.endDate"), "endDate", "",
			 * "text", Translator.toLocale("input.transactionID"), "transactionID", "18",
			 * "text", Translator.toLocale("input.passportNo"), "nId", "15" };
			 */
			String[] dateParam = { "date", Translator.toLocale("input.startDate"), "startDate", "", "date",
					Translator.toLocale("input.endDate"), "endDate", "", "text",
					Translator.toLocale("input.passportNo"), "nId", "15","text",
					Translator.toLocale("input.transactionID"), "transactionID", "18","select",
					Translator.toLocale("input.Nationality"), "filterNationality", "","select",
					Translator.toLocale("select.taxPaidStatus"), "taxPaidStatus", "", "select",
					Translator.toLocale("table.origin"), "originFilter", "", "select",
					Translator.toLocale("input.Status"), "recordStatus", "" };
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
		} else if ("Immigration".equals(userType)) {
			String[] dateParam = { "date", Translator.toLocale("input.startDate"), "startDate", "", "date",
					Translator.toLocale("input.endDate"), "endDate", "", "text",
					Translator.toLocale("input.passportNo"), "nId", "15","text",
					Translator.toLocale("input.transactionID"), "transactionID", "18","select",
					Translator.toLocale("input.Nationality"), "filterNationality", "","select",
					Translator.toLocale("select.taxPaidStatus"), "taxPaidStatus", "", "select",
					Translator.toLocale("table.origin"), "originFilter", "", "select",
					Translator.toLocale("input.Status"), "recordStatus", "" };
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
		} else if ("CEIRAdmin".equals(userType)) {
			// input type date list
			String[] dateParam = { "date", Translator.toLocale("input.startDate"), "startDate", "", "date",
					Translator.toLocale("input.endDate"), "endDate", "", "text", Translator.toLocale("input.nidInput"),
					"nId", "15", "text", Translator.toLocale("input.transactionID"), "transactionID", "18", "select",
					Translator.toLocale("input.Nationality"), "filterNationality", "", "select",
					Translator.toLocale("select.taxPaidStatus"), "taxPaidStatus", "", "select",
					Translator.toLocale("table.origin"), "originFilter", "", "select",
					Translator.toLocale("input.Status"), "recordStatus", "" };
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
		} else {
			// input type date list
			/*
			 * String[] dateParam = { "date", Translator.toLocale("input.startDate"),
			 * "startDate", "", "date", Translator.toLocale("input.endDate"), "endDate", "",
			 * "text", Translator.toLocale("input.nidInput"), "nId", "15", "text",
			 * Translator.toLocale("input.transactionID"), "transactionID", "18" };
			 */
			String[] dateParam = { "date", Translator.toLocale("input.startDate"), "startDate", "", "date",
					Translator.toLocale("input.endDate"), "endDate", "", "text", Translator.toLocale("input.nidInput"),
					"nId", "15", "text", Translator.toLocale("input.transactionID"), "transactionID", "18", "text",
					Translator.toLocale("input.Nationality"), "filterNationality", "15", "select",
					Translator.toLocale("select.taxPaidStatus"), "taxPaidStatus", "", "select",
					Translator.toLocale("table.origin"), "originFilter", "", "select",
					Translator.toLocale("input.Status"), "recordStatus", "" };
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

	// ************************************************ delete consignment record
	// page********************************************************************************/

	@DeleteMapping("/delete/{imei}/{txnId}")
	public @ResponseBody GenricResponse deleteConsignment(@PathVariable("imei") String imei,
			@PathVariable("txnId") String txnId, HttpSession session) {

		String userType = (String) session.getAttribute("usertype");
		String userName = session.getAttribute("username").toString();
		int userId = (int) session.getAttribute("userid");
		int userTypeid = (int) session.getAttribute("usertypeId");
		AllRequest request = new AllRequest();

		request.setFeatureId(12);
		request.setUsername(userName);
		request.setImei(imei);
		request.setUserTypeId(userTypeid);
		request.setUserType(userType);
		request.setUserId(userId);
		request.setTxnId(txnId);

		request.setPublicIp(session.getAttribute("publicIP").toString());
		request.setBrowser(session.getAttribute("browser").toString());
		log.info(" request==" + request);
		GenricResponse response = uploadPaidStatusFeignClient.delete(request);
		log.info("response after delete device." + response);
		return response;
	}

	@DeleteMapping("/endUserdelete/{imei}/{txnId}")
	public @ResponseBody GenricResponse endUserdeleteConsignment(@PathVariable("imei") String imei,
			@PathVariable("imei") String txnId , HttpServletRequest requestHeaders) {
		AllRequest request = new AllRequest();
		request.setFeatureId(12);

		request.setImei(imei);
		request.setUserTypeId(17);
		request.setUserType("End User");
		request.setTxnId(txnId);
		UserHeader header=registerService.getUserHeaders(requestHeaders);
		request.setPublicIp(header.getPublicIp());
		request.setBrowser(header.getBrowser());
		log.info(" request==" + request);
		GenricResponse response = uploadPaidStatusFeignClient.delete(request);
		log.info("response after delete consignment." + response);
		return response;
	}

	/*
	 * @GetMapping("/deviceInfo/{imei}") public @ResponseBody UserPaidStatusContent
	 * viewByImei(@PathVariable("imei") Long imei) { UserPaidStatusContent content=
	 * uploadPaidStatusFeignClient.viewByImei(imei); return content; }
	 */

}