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
import org.gl.ceir.configuration.Translator;
import org.gl.ceir.pageElement.model.Button;
import org.gl.ceir.pageElement.model.PageElement;
import org.gl.ceir.pagination.model.RegistrationContentModel;
import org.gl.ceir.pagination.model.RegistrationPaginationModel;
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
public class AssigneeStockDatatableController {

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

	@PostMapping("AssigneeDetailsData")
	public ResponseEntity<?> viewAssigneeRecord(
			@RequestParam(name = "type", defaultValue = "Stock", required = false) String role,
			HttpServletRequest request, HttpSession session) {
		String userType = (String) session.getAttribute("usertype");
		int userId = (int) session.getAttribute("userid");
		log.info("session value user Type viewAssigneeRecordController==" + session.getAttribute("usertype"));
		int file = 0;
		List<List<Object>> finalList = new ArrayList<List<Object>>();
		String filter = request.getParameter("filter");
		Gson gsonObject = new Gson();
		FilterRequest filterrequest = gsonObject.fromJson(filter, FilterRequest.class);
		Integer pageSize = Integer.parseInt(request.getParameter("length"));
		Integer pageNo = Integer.parseInt(request.getParameter("start")) / pageSize;
		filterrequest.setSearchString(request.getParameter("search[value]"));
		log.info("pageSize" + pageSize + "-----------pageNo---" + pageNo);
		try {
			log.info("request send to the filter api =" + filterrequest);
			Object response = userProfileFeignImpl.asigneeDetailsFeign(filterrequest, pageNo, pageSize, file);
			log.info("response in datatable" + response);
			Gson gson = new Gson();

			String apiResponse = gson.toJson(response);

			registrationpaginationmodel = gson.fromJson(apiResponse, RegistrationPaginationModel.class);

			List<RegistrationContentModel> paginationContentList = registrationpaginationmodel.getContent();

			if (paginationContentList.isEmpty()) {
				datatableResponseModel.setData(Collections.emptyList());
			} else {
				for (RegistrationContentModel dataInsideList : paginationContentList) {
					//String sNO = "";
					String username = dataInsideList.getUser().getUsername();
					String firstName = dataInsideList.getDisplayName();
					String phoneNo = dataInsideList.getPhoneNo();
					String email = dataInsideList.getEmail();
					log.info("username="+username+ "firstName="+firstName+" phoneNo=="+phoneNo+" email =="+email);
					
					String selectAction="saveAssigneDetails('"+username+"','"+firstName.replaceAll( "\\s", "+20")+"')";
					// log.info("Id-->"+Id+"--userStatus--->"+userStatus+"--StatusName---->"+StatusName+"--createdOn---->"+createdOn+"--id--->"+id+"--userName-->"+username);
					String action = "<a onclick="+selectAction+">SELECT</a>";
					Object[] finalData = { username, firstName, phoneNo, email, action };
					List<Object> finalDataList = new ArrayList<Object>(Arrays.asList(finalData));
					finalList.add(finalDataList);
					datatableResponseModel.setData(finalList);

				}
			}

			datatableResponseModel.setRecordsTotal(registrationpaginationmodel.getNumberOfElements());
			datatableResponseModel.setRecordsFiltered(registrationpaginationmodel.getTotalElements());
			return new ResponseEntity<>(datatableResponseModel, HttpStatus.OK);

		} catch (Exception e) {
			datatableResponseModel.setRecordsTotal(null);
			datatableResponseModel.setRecordsFiltered(null);
			datatableResponseModel.setData(Collections.emptyList());
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(datatableResponseModel, HttpStatus.OK);
		}
	}
}
