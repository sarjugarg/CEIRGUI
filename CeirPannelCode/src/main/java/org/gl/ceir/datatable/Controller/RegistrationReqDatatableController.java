package org.gl.ceir.datatable.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.gl.ceir.CeirPannelCode.Feignclient.FeignCleintImplementation;
import org.gl.ceir.CeirPannelCode.Feignclient.UserProfileFeignImpl;
import org.gl.ceir.CeirPannelCode.Model.FilterRequest;
import org.gl.ceir.CeirPannelCode.Model.constants.UserStatus;
import org.gl.ceir.Class.HeadersTitle.DatatableResponseModel;
import org.gl.ceir.Class.HeadersTitle.IconsState;
import org.gl.ceir.configuration.ConfigParameters;
import org.gl.ceir.configuration.Translator;
import org.gl.ceir.pageElement.model.Button;
import org.gl.ceir.pageElement.model.InputFields;
import org.gl.ceir.pageElement.model.PageElement;
import org.gl.ceir.pagination.model.RegistrationContentModel;
import org.gl.ceir.pagination.model.RegistrationPaginationModel;
import org.gl.ceir.pagination.model.RegistrationUser;
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
public class RegistrationReqDatatableController {
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
	IconsState iconState;
	@Autowired
	RegistrationContentModel registrationcontentmodel;
	@Autowired
	RegistrationPaginationModel registrationpaginationmodel;
	@Autowired
	UserProfileFeignImpl userProfileFeignImpl;
	@Autowired
	RegistrationUser registrationUser;

	@PostMapping("registrationData")
	public ResponseEntity<?> viewUserProfileRecord(
			@RequestParam(name = "type", defaultValue = "registration", required = false) String role,
			HttpServletRequest request, HttpSession session,
			@RequestParam(name = "source", defaultValue = "menu", required = false) String source) {

		// String userType = (String) session.getAttribute("usertype");
		// int userId= (int) session.getAttribute("userid");
		String sessionUserName = (String) session.getAttribute("username");

		log.info("session value user Type admin registration Controller==" + session.getAttribute("usertype"));
		int file = 0;
		// Data set on this List
		List<List<Object>> finalList = new ArrayList<List<Object>>();
		String filter = request.getParameter("filter");
		Gson gsonObject = new Gson();
		FilterRequest filterrequest = gsonObject.fromJson(filter, FilterRequest.class);

		Integer pageSize = Integer.parseInt(request.getParameter("length"));
		Integer pageNo = Integer.parseInt(request.getParameter("start")) / pageSize;

		String column = "0".equalsIgnoreCase(request.getParameter("order[0][column]")) ? "Created On"
				: "1".equalsIgnoreCase(request.getParameter("order[0][column]")) ? "Modified On"
						: "2".equalsIgnoreCase(request.getParameter("order[0][column]")) ? "User ID"
								: "3".equalsIgnoreCase(request.getParameter("order[0][column]")) ? "Email"
										: "4".equalsIgnoreCase(request.getParameter("order[0][column]")) ? "Phone No."
												: "5".equalsIgnoreCase(request.getParameter("order[0][column]"))
														? "Type"
														: "6".equalsIgnoreCase(request.getParameter("order[0][column]"))
																? "User Type"
																: "7".equalsIgnoreCase(
																		request.getParameter("order[0][column]"))
																				? "Status"
																				: "Modified On";
		
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
		filterrequest.setColumnName(column);
		filterrequest.setSort(order);

		filterrequest.setSearchString(request.getParameter("search[value]"));
		log.info("pageSize" + pageSize + "-----------pageNo---" + pageNo + "------------source--->" + source);

		try {
			filterrequest.setPublicIp(session.getAttribute("publicIP").toString());
			filterrequest.setBrowser(session.getAttribute("browser").toString());
			log.info("request send to the filter api =" + filterrequest);
			Object response = userProfileFeignImpl.registrationRequest(filterrequest, pageNo, pageSize, file, source);
			log.info("response in datatable" + response);
			Gson gson = new Gson();

			String apiResponse = gson.toJson(response);

			registrationpaginationmodel = gson.fromJson(apiResponse, RegistrationPaginationModel.class);

			List<RegistrationContentModel> paginationContentList = registrationpaginationmodel.getContent();

			if (paginationContentList.isEmpty()) {

				datatableResponseModel.setData(Collections.emptyList());
			} else {
				for (RegistrationContentModel dataInsideList : paginationContentList) {
					String createdOn = (String) dataInsideList.getUser().getCreatedOn();
					String modifiedOn = (String) dataInsideList.getUser().getModifiedOn();
					String Id = String.valueOf(dataInsideList.getUser().getId());
					String username = dataInsideList.getUser().getUsername();
					String id = String.valueOf(dataInsideList.getId());
					String userTypeId = String.valueOf(dataInsideList.getUser().getUsertype().getId());
					String type = dataInsideList.getAsTypeName();
					String roles = (String) dataInsideList.getUser().getUsertype().getUsertypeName();
					String StatusName = dataInsideList.getUser().getStateInterp();
					String status = String.valueOf(dataInsideList.getUser().getCurrentStatus());
					String email = dataInsideList.getEmail();
					String phoneNo = dataInsideList.getPhoneNo();

					String userStatus = (String) session.getAttribute("userStatus");
					// log.info("Id-->"+Id+"--userStatus--->"+userStatus+"--StatusName---->"+StatusName+"--createdOn---->"+createdOn+"--id--->"+id+"--userName-->"+username);
					String action = iconState.adminRegistrationRequest(Id, userStatus, StatusName, createdOn, roles,
							type, id, username, status, sessionUserName, userTypeId, source);
					Object[] finalData = { createdOn, modifiedOn, username, email, phoneNo, type, roles, StatusName,
							action };

					List<Object> finalDataList = new ArrayList<Object>(Arrays.asList(finalData));
					finalList.add(finalDataList);
					datatableResponseModel.setData(finalList);

				}
			}
			// data set on ModelClass
			datatableResponseModel.setRecordsTotal(registrationpaginationmodel.getNumberOfElements());
			datatableResponseModel.setRecordsFiltered(registrationpaginationmodel.getTotalElements());
			return new ResponseEntity<>(datatableResponseModel, HttpStatus.OK);
		} catch (Exception e) {
			datatableResponseModel.setRecordsTotal(null);
			datatableResponseModel.setRecordsFiltered(null);
			datatableResponseModel.setData(Collections.emptyList());
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(datatableResponseModel, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("registration/pageRendering")
	public ResponseEntity<?> pageRendering(
			@RequestParam(name = "type", defaultValue = "consignment", required = false) String role,
			HttpSession session) {

		String userType = (String) session.getAttribute("usertype");
		String userStatus = (String) session.getAttribute("userStatus");

		InputFields inputFields = new InputFields();
		InputFields dateRelatedFields;

		pageElement.setPageTitle(Translator.toLocale("table.RegistrationRequest"));

		List<Button> buttonList = new ArrayList<>();
		List<InputFields> dropdownList = new ArrayList<>();
		List<InputFields> inputTypeDateList = new ArrayList<>();

		log.info("USER STATUS:::::::::" + userStatus);
		log.info("session value user Type==" + session.getAttribute("usertype"));

		String[] names = { "FilterButton", Translator.toLocale("button.filter"),
				"registrationDatatable(" + ConfigParameters.languageParam + ",'filter')", "submitFilter" };
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

		// Dropdown items
		/*
		 * String[] selectParam=
		 * {"select",Translator.toLocale("table.AsType"),"asType","","select",Translator
		 * .toLocale("select.userType"),"role","","select",Translator.toLocale(
		 * "table.status"),"recentStatus",""}; for(int i=0; i< selectParam.length; i++)
		 * { inputFields= new InputFields(); inputFields.setType(selectParam[i]); i++;
		 * inputFields.setTitle(selectParam[i]); i++; inputFields.setId(selectParam[i]);
		 * i++; inputFields.setClassName(selectParam[i]); dropdownList.add(inputFields);
		 * } pageElement.setDropdownList(dropdownList);
		 */
		// input type date list
		String[] dateParam = { "date", Translator.toLocale("input.startDate"), "startDate", "", "date",
				Translator.toLocale("input.endDate"), "endDate", "", "text", Translator.toLocale("table.UserName"),
				"userName", "", "text", Translator.toLocale("table.email"), "emailID", "", "text",
				Translator.toLocale("table.phone"), "phone", "", "select", Translator.toLocale("table.AsType"),
				"asType", "", "select", Translator.toLocale("select.userType"), "role", "", "select",
				Translator.toLocale("table.status"), "recentStatus", "" };
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

		pageElement.setInputTypeDateList(inputTypeDateList);
		pageElement.setUserStatus(userStatus);
		return new ResponseEntity<>(pageElement, HttpStatus.OK);

	}
}
