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
import org.gl.ceir.configuration.ConfigParameters;
import org.gl.ceir.configuration.Translator;
import org.gl.ceir.pageElement.model.Button;
import org.gl.ceir.pageElement.model.InputFields;
import org.gl.ceir.pageElement.model.PageElement;
import org.gl.ceir.pagination.model.StockContent;
import org.gl.ceir.pagination.model.StockPaginationModel;
import org.gl.ceir.pagination.model.UserModel;
import org.gl.ceir.pagination.model.UserProfileModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

@RestController
public class StockDatatableController {
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
	FeignCleintImplementation feignCleintImplementation;
	@Autowired
	StockPaginationModel stockPaginationModel;
	@Autowired
	IconsState iconState;

	@PostMapping("stockData")
	public ResponseEntity<?> viewStockList(
			@RequestParam(name = "type", defaultValue = "stock", required = false) String role,
			@RequestParam(name = "sourceType", required = false) String sourceType, HttpServletRequest request,
			HttpSession session, @RequestParam(name = "source", required = false) String sourceParam) {
		// Data set on this List
		List<List<Object>> finalList = new ArrayList<List<Object>>();

		log.info("session value user Type==" + session.getAttribute("usertype") + "==sourceParam==" + sourceParam);
		String userType = (String) session.getAttribute("usertype");
		// FilterRequest filterrequest = request.getParameter("FilterRequest");
		String filter = request.getParameter("filter");
		Gson gsonObject = new Gson();
		FilterRequest filterrequest = gsonObject.fromJson(filter, FilterRequest.class);
		Integer exportFile = 0;
		Integer pageSize = Integer.parseInt(request.getParameter("length"));
		Integer pageNo = Integer.parseInt(request.getParameter("start")) / pageSize;
		// TODO Convert header to an ENUM.
		// list provided via Back-end process
		/*
		 * if("CEIRAdmin".equals(userType)) {
		 * filterrequest.setColumnName(request.getParameter("order[0][column]") == null
		 * ? "modifiedOn" : request.getParameter("order[0][column]"));
		 * filterrequest.setSort(request.getParameter("order[0][dir]") == null ? "desc"
		 * : request.getParameter("order[0][dir]")); }
		 * 
		 * else { filterrequest.setColumnName("modifiedOn");
		 * filterrequest.setSort(request.getParameter("desc")); }
		 */
		
		filterrequest.setColumnName(request.getParameter("order[0][column]") == null ? "modifiedOn" : request.getParameter("order[0][column]"));
		filterrequest.setSort(request.getParameter("order[0][dir]") == null ? "desc" : request.getParameter("order[0][dir]"));
		filterrequest.setSearchString(request.getParameter("search[value]"));
		
		filterrequest.setPublicIp(session.getAttribute("publicIP").toString());
		filterrequest.setBrowser(session.getAttribute("browser").toString());
		
		Object response = feignCleintImplementation.stockFilter(filterrequest, pageNo, pageSize, exportFile,
				sourceParam);
		log.info("request passed to the filter api  =" + filterrequest);
		log.info("response::::::::::::::::" + response);
		try {
			Gson gson = new Gson();
			String apiResponse = gson.toJson(response);
			stockPaginationModel = gson.fromJson(apiResponse, StockPaginationModel.class);
			List<StockContent> paginationContentList = stockPaginationModel.getContent();
			// log.info("-----after response- paginationContentList------" +
			// paginationContentList);
			if (paginationContentList.isEmpty()) {
				datatableResponseModel.setData(Collections.emptyList());
			} else {

				if (("Importer".equals(userType) || "Retailer".equals(userType) || "Distributor".equals(userType)
						|| "Custom".equals(userType)) && "viaStock".equals(sourceType)) {
					log.info("userType in stock controller--------" + userType + "-----sourceType--->" + sourceType);
					for (StockContent dataInsideList : paginationContentList) {
						String checboxes = "<input type=checkbox class=filled-in>";
						String date = dataInsideList.getCreatedOn();
						String txnId = dataInsideList.getTxnId();
						String file = dataInsideList.getFileName();
						// if API provide me consignmentStatusName
						String statusOfStock = String.valueOf(dataInsideList.getStockStatus());
						String stockStatusName = dataInsideList.getStateInterp();
						String quantity = String.valueOf(dataInsideList.getQuantity());
						String deviceQuantity = String.valueOf(dataInsideList.getDeviceQuantity());
						Object[] finalData = { checboxes, date, txnId, file, stockStatusName, quantity,
								deviceQuantity };
						List<Object> finalDataList = new ArrayList<Object>(Arrays.asList(finalData));
						finalList.add(finalDataList);
						datatableResponseModel.setData(finalList);
					}
				} else if ("Importer".equals(userType)) {
					for (StockContent dataInsideList : paginationContentList) {
						String date = dataInsideList.getCreatedOn();
						String txnId = dataInsideList.getTxnId();
						String file = dataInsideList.getFileName();
						// if API provide me consignmentStatusName
						String statusOfStock = String.valueOf(dataInsideList.getStockStatus());
						String stockStatusName = dataInsideList.getStateInterp();
						String userStatus = (String) session.getAttribute("userStatus");
						String quantity = String.valueOf(dataInsideList.getQuantity());
						String deviceQuantity = String.valueOf(dataInsideList.getDeviceQuantity());
						String assignerId = String.valueOf(dataInsideList.getAssignerId());
						String userId = String.valueOf(dataInsideList.getUserId());
						String action = iconState.ImporterStockState(file, txnId, statusOfStock, userStatus, assignerId,
								userId);
						Object[] finalData = { date, txnId, file, stockStatusName, quantity, deviceQuantity, action };
						List<Object> finalDataList = new ArrayList<Object>(Arrays.asList(finalData));
						finalList.add(finalDataList);
						datatableResponseModel.setData(finalList);
					}
				} else if ("Custom".equals(userType)) {
					for (StockContent dataInsideList : paginationContentList) {
						String date = dataInsideList.getCreatedOn();
						String assignedTo = dataInsideList.getUser().getUserProfile().getDisplayName();
						if (assignedTo == null || assignedTo.equals("")) {
							assignedTo = "";
						} else {
							assignedTo = dataInsideList.getUser().getUserProfile().getDisplayName();
						}
						String txnId = dataInsideList.getTxnId();
						String file = dataInsideList.getFileName();
						// if API provide me consignmentStatusName
						String statusOfStock = String.valueOf(dataInsideList.getStockStatus());
						String stockStatusName = dataInsideList.getStateInterp();
						String userStatus = (String) session.getAttribute("userStatus");
						String quantity = String.valueOf(dataInsideList.getQuantity());
						String deviceQuantity = String.valueOf(dataInsideList.getDeviceQuantity());
						String action = iconState.customStockState(file, txnId, statusOfStock, userStatus);
						Object[] finalData = { date, assignedTo, txnId, file, stockStatusName, quantity, deviceQuantity,
								action };
						List<Object> finalDataList = new ArrayList<Object>(Arrays.asList(finalData));
						finalList.add(finalDataList);
						datatableResponseModel.setData(finalList);
					}
				} else if ("CEIRAdmin".equals(userType)) {
					// log.info("inside CEIRAdmin---------------------------");
					for (StockContent dataInsideList : paginationContentList) {
						String date = dataInsideList.getCreatedOn();
						String txnId = dataInsideList.getTxnId();
						UserModel userModel = dataInsideList.getUser();
						UserProfileModel userprofileModel = userModel.getUserProfile();
						String displayName = userprofileModel.getDisplayName();
						String roll = dataInsideList.getRoleType();
						String file = dataInsideList.getFileName();
						// if API provide me consignmentStatusName
						String statusOfStock = String.valueOf(dataInsideList.getStockStatus());
						String stockStatusName = dataInsideList.getStateInterp();
						String userStatus = (String) session.getAttribute("userStatus");
						String quantity = String.valueOf(dataInsideList.getQuantity());
						String deviceQuantity = String.valueOf(dataInsideList.getDeviceQuantity());
						String action = iconState.adminStockState(file, txnId, statusOfStock, userStatus);
						Object[] finalData = { date, txnId, displayName, roll, file, stockStatusName, quantity,
								deviceQuantity, action };
						List<Object> finalDataList = new ArrayList<Object>(Arrays.asList(finalData));
						finalList.add(finalDataList);
						datatableResponseModel.setData(finalList);
					}
				} else if ("Distributor".equals(userType)) {
					for (StockContent dataInsideList : paginationContentList) {
						String date = dataInsideList.getCreatedOn();
						String txnId = dataInsideList.getTxnId();
						String file = dataInsideList.getFileName();
						// if API provide me consignmentStatusName
						String statusOfStock = String.valueOf(dataInsideList.getStockStatus());
						String stockStatusName = dataInsideList.getStateInterp();
						String userStatus = (String) session.getAttribute("userStatus");
						String quantity = String.valueOf(dataInsideList.getQuantity());
						String deviceQuantity = String.valueOf(dataInsideList.getDeviceQuantity());
						String assignerId = String.valueOf(dataInsideList.getAssignerId());
						String userId = String.valueOf(dataInsideList.getUserId());
						String action = iconState.DistributerRetailerIcons(file, txnId, statusOfStock, userStatus,
								assignerId, userId);
						Object[] finalData = { date, txnId, file, stockStatusName, quantity, deviceQuantity, action };
						List<Object> finalDataList = new ArrayList<Object>(Arrays.asList(finalData));
						finalList.add(finalDataList);
						datatableResponseModel.setData(finalList);
					}
				} else if ("Retailer".equals(userType)) {
					log.info("<><><><>userType in retailer<><><>" + userType);
					for (StockContent dataInsideList : paginationContentList) {
						String date = dataInsideList.getCreatedOn();
						String txnId = dataInsideList.getTxnId();
						String file = dataInsideList.getFileName();
						// if API provide me consignmentStatusName
						String statusOfStock = String.valueOf(dataInsideList.getStockStatus());
						String stockStatusName = dataInsideList.getStateInterp();
						String userStatus = (String) session.getAttribute("userStatus");
						String quantity = String.valueOf(dataInsideList.getQuantity());
						String deviceQuantity = String.valueOf(dataInsideList.getDeviceQuantity());
						String assignerId = String.valueOf(dataInsideList.getAssignerId());
						String userId = String.valueOf(dataInsideList.getUserId());
						String action = iconState.DistributerRetailerIcons(file, txnId, statusOfStock, userStatus,
								assignerId, userId);
						Object[] finalData = { date, txnId, file, stockStatusName, quantity, deviceQuantity, action };
						List<Object> finalDataList = new ArrayList<Object>(Arrays.asList(finalData));
						finalList.add(finalDataList);
						datatableResponseModel.setData(finalList);
					}
				} else if ("Manufacturer".equals(userType)) {
					log.info("<><><><>userType in Manufacturer<><><>" + userType);
					for (StockContent dataInsideList : paginationContentList) {
						String date = dataInsideList.getCreatedOn();
						String txnId = dataInsideList.getTxnId();
						String file = dataInsideList.getFileName();
						// if API provide me consignmentStatusName
						String statusOfStock = String.valueOf(dataInsideList.getStockStatus());
						String stockStatusName = dataInsideList.getStateInterp();
						String userStatus = (String) session.getAttribute("userStatus");
						String quantity = String.valueOf(dataInsideList.getQuantity());
						String deviceQuantity = String.valueOf(dataInsideList.getDeviceQuantity());
						String action = iconState.stockState(file, txnId, statusOfStock, userStatus);
						Object[] finalData = { date, txnId, file, stockStatusName, quantity, deviceQuantity, action };
						List<Object> finalDataList = new ArrayList<Object>(Arrays.asList(finalData));
						finalList.add(finalDataList);
						datatableResponseModel.setData(finalList);
					}
				}

			}
			// data set on ModelClass
			datatableResponseModel.setRecordsTotal(stockPaginationModel.getNumberOfElements());
			datatableResponseModel.setRecordsFiltered(stockPaginationModel.getTotalElements());
			return new ResponseEntity<>(datatableResponseModel, HttpStatus.OK);

		} catch (Exception e) {
			datatableResponseModel.setRecordsTotal(null);
			datatableResponseModel.setRecordsFiltered(null);
			datatableResponseModel.setData(Collections.emptyList());
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(datatableResponseModel, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping
	@RequestMapping("stock/pageRendering")
	public ResponseEntity<?> pageRendering(
			@RequestParam(name = "type", defaultValue = "stock", required = false) String role,
			@RequestParam(name = "sourceType", required = false) String sourceType, HttpSession session) {
		String userType = (String) session.getAttribute("usertype");
		String userStatus = (String) session.getAttribute("userStatus");
		InputFields inputFields = new InputFields();
		InputFields dateRelatedFields;

		List<Button> buttonList = new ArrayList<>();
		List<InputFields> dropdownList = new ArrayList<>();
		List<InputFields> inputTypeDateList = new ArrayList<>();

		log.info("sourceType render---2------" + sourceType);
		if ("Custom".equals(userType)) {
			String[] names = { "HeaderButton", Translator.toLocale("button.assignStock"),
					"./openUploadStock?reqType=formPage", "btnLink", "FilterButton",
					Translator.toLocale("button.filter"), "filter(" + ConfigParameters.languageParam + ",'filter')",
					"submitFilter" };

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
		} else {
			String[] names = { "HeaderButton", Translator.toLocale("button.uploadStock"),
					"./openUploadStock?reqType=formPage", "btnLink", "FilterButton",
					Translator.toLocale("button.filter"), "filter(" + ConfigParameters.languageParam + ",'filter')",
					"submitFilter" };
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
		}

		if ("CEIRAdmin".equals(userType)) {
			// Dropdown items
			/*
			 * String[] selectParam=
			 * {"select",Translator.toLocale("select.stockStatus"),"StockStatus","","select"
			 * ,Translator.toLocale("table.userType"),"userType",""}; for(int i=0; i<
			 * selectParam.length; i++) { inputFields= new InputFields();
			 * inputFields.setType(selectParam[i]); i++;
			 * inputFields.setTitle(selectParam[i]); i++; inputFields.setId(selectParam[i]);
			 * i++; inputFields.setClassName(selectParam[i]); dropdownList.add(inputFields);
			 * }
			 */

			// input type date list
			String[] dateParam = { "date", Translator.toLocale("input.startDate"), "startDate", "", "date",
					Translator.toLocale("input.endDate"), "endDate", "", "text",
					Translator.toLocale("input.transactionID"), "transactionID", "18", "text",
					Translator.toLocale("table.importerCompanyName"), "name", "30", "select",
					Translator.toLocale("registration.roletype"), "userType", "", "text",
					Translator.toLocale("table.fileName"), "fileNameFilter", "30", "select",
					Translator.toLocale("select.stockStatus"), "StockStatus", "", "text",
					Translator.toLocale("input.quantity"), "IMEIQuantityFilter", "10" ,"text",
					Translator.toLocale("input.devicequantity"), "deviceQuantityFilter", "10"};
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
		else if ("Manufacturer".equals(userType)) {
			// Dropdown items
			/*
			 * String[] selectParam=
			 * {"select",Translator.toLocale("select.stockStatus"),"StockStatus","","select"
			 * ,Translator.toLocale("table.userType"),"userType",""}; for(int i=0; i<
			 * selectParam.length; i++) { inputFields= new InputFields();
			 * inputFields.setType(selectParam[i]); i++;
			 * inputFields.setTitle(selectParam[i]); i++; inputFields.setId(selectParam[i]);
			 * i++; inputFields.setClassName(selectParam[i]); dropdownList.add(inputFields);
			 * }
			 */

			// input type date list
			String[] dateParam = { "date", Translator.toLocale("input.startDate"), "startDate", "", "date",
					Translator.toLocale("input.endDate"), "endDate", "", "text",
					Translator.toLocale("input.transactionID"), "transactionID", "18", "text",
					Translator.toLocale("table.fileName"), "fileNameFilter", "30", "select",
					Translator.toLocale("select.stockStatus"), "StockStatus", "", "text",
					Translator.toLocale("input.quantity"), "IMEIQuantityFilter", "10" ,"text",
					Translator.toLocale("input.devicequantity"), "deviceQuantityFilter", "10"};
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
		else if ("Custom".equals(userType)) {
			// input type date list
						String[] dateParam = { "date", Translator.toLocale("input.startDate"), "startDate", "", "date",
								Translator.toLocale("input.endDate"), "endDate", "", "text",
								Translator.toLocale("table.assignto"), "name","30","text",
								Translator.toLocale("input.transactionID"), "transactionID", "18","text",
								Translator.toLocale("table.fileName"), "fileNameFilter", "30", "select",
								Translator.toLocale("select.stockStatus"), "StockStatus", "", "text",
								Translator.toLocale("input.quantity"), "IMEIQuantityFilter", "10" ,"text",
								Translator.toLocale("input.devicequantity"), "deviceQuantityFilter", "10" };
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
		else {
			// Dropdown items
			/*
			 * String[] selectParam = { "select", Translator.toLocale("select.stockStatus"),
			 * "StockStatus", "" }; for (int i = 0; i < selectParam.length; i++) {
			 * inputFields = new InputFields(); inputFields.setType(selectParam[i]); i++;
			 * inputFields.setTitle(selectParam[i]); i++; inputFields.setId(selectParam[i]);
			 * i++; inputFields.setClassName(selectParam[i]); dropdownList.add(inputFields);
			 * }
			 */

			// input type date list
			String[] dateParam = { "date", Translator.toLocale("input.startDate"), "startDate", "", "date",
					Translator.toLocale("input.endDate"), "endDate", "", "text",
					Translator.toLocale("input.transactionID"), "transactionID", "18","text",
					Translator.toLocale("table.fileName"), "fileNameFilter", "30", "select",
					Translator.toLocale("select.stockStatus"), "StockStatus", "", "text",
					Translator.toLocale("input.quantity"), "IMEIQuantityFilter", "10" ,"text",
					Translator.toLocale("input.devicequantity"), "deviceQuantityFilter", "10" };
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

		if ("CEIRAdmin".equals(userType)) {
			pageElement.setPageTitle(
					Translator.toLocale("view.stockMgt") + " " + Translator.toLocale("roletype." + userType));
			log.info("if userType-->" + userType);
		} else {
			String userTypeRole = (String) session.getAttribute("selectedUserTypeId");
			pageElement.setPageTitle(
					Translator.toLocale("view.stockMgt") + " " + Translator.toLocale("roletype." + userTypeRole));
			log.info("Else userType-->" + userType + " userTypeRole-->" + userTypeRole);
		}
		// String userTypeRole=(String)session.getAttribute("selectedUserTypeId");
		// pageElement.setPageTitle(Translator.toLocale("view.stockMgt")+"
		// "+Translator.toLocale("roletype."+userTypeRole));
		pageElement.setButtonList(buttonList);
		pageElement.setUserStatus(userStatus);
		pageElement.setDropdownList(dropdownList);
		pageElement.setInputTypeDateList(inputTypeDateList);
		return new ResponseEntity<>(pageElement, HttpStatus.OK);
	}

}
