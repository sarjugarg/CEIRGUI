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
import org.gl.ceir.pagination.model.ManageUserContent;
import org.gl.ceir.pagination.model.ManageUserPagination;
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
public class ManageUserDatatable {
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	Translator Translator;
	@Autowired
	IconsState iconState;
	@Autowired
	PageElement pageElement;
	@Autowired
	Button button;
	@Autowired
	DatatableResponseModel datatableResponseModel;	
	@Autowired
	FeignCleintImplementation feignCleintImplementation;
	@Autowired
	ManageUserContent manageUserContent;
	@Autowired
	ManageUserPagination manageUserPagination;
	

	@PostMapping("manageUserData")
	public ResponseEntity<?> viewManageUser(@RequestParam(name="type",defaultValue = "manageUser",required = false) String role, HttpServletRequest request,HttpSession session) {
		String userType = (String) session.getAttribute("usertype");
		int userId=	(int) session.getAttribute("userid");
		// Data set on this List
				List<List<Object>> finalList=new ArrayList<List<Object>>();
				String filter = request.getParameter("filter");
				Gson gsonObject=new Gson();
				FilterRequest filterrequest = gsonObject.fromJson(filter, FilterRequest.class);
				Integer file = 0;
				Integer pageSize = Integer.parseInt(request.getParameter("length"));
				Integer pageNo = Integer.parseInt(request.getParameter("start")) / pageSize ;
				filterrequest.setSearchString(request.getParameter("search[value]"));
				log.info("pageSize"+pageSize+"-----------pageNo---"+pageNo);
				try {
					log.info("request send to the filter api ="+filterrequest);
					Object response = feignCleintImplementation.manageUserFeign(filterrequest, pageNo, pageSize, file);
					log.info("response in datatable"+response);
					Gson gson= new Gson(); 
					String apiResponse = gson.toJson(response);
					manageUserPagination = gson.fromJson(apiResponse, ManageUserPagination.class);
					List<ManageUserContent> paginationContentList = manageUserPagination.getContent();
				if(paginationContentList.isEmpty()) {
					datatableResponseModel.setData(Collections.emptyList());
				}else {
					for(ManageUserContent dataInsideList : paginationContentList) {
						String id = String.valueOf(dataInsideList.getId());
						String createdOn = dataInsideList.getCreatedOn();
						String txnId = dataInsideList.getTxnId();
						String passportNo = dataInsideList.getNid();
						String nationality =dataInsideList.getNationality();
						//String visaExpiryDate = "";
						String phoneNo = dataInsideList.getPhoneNo();
						String action=iconState.manageUserIcons(id,passportNo);			   
						Object[] finalData={createdOn,txnId,passportNo,nationality,phoneNo,action}; 
						List<Object> finalDataList=new ArrayList<Object>(Arrays.asList(finalData));
						finalList.add(finalDataList);
						datatableResponseModel.setData(finalList);	
					} 
				}	
				//data set on ModelClass
				datatableResponseModel.setRecordsTotal(manageUserPagination.getNumberOfElements());
				datatableResponseModel.setRecordsFiltered(manageUserPagination.getTotalElements());
				return new ResponseEntity<>(datatableResponseModel, HttpStatus.OK); 
					
				}catch(Exception e) {
					datatableResponseModel.setRecordsTotal(null);
					datatableResponseModel.setRecordsFiltered(null);
					datatableResponseModel.setData(Collections.emptyList());
					log.error(e.getMessage(),e);
					return new ResponseEntity<>(datatableResponseModel, HttpStatus.OK); 
				}
			}
		
	
		
	@PostMapping("manageUser/pageRendering")
	public ResponseEntity<?> directives(@RequestParam(name="type",defaultValue = "manageUser",required = false) String role,HttpSession session){

		String userType = (String) session.getAttribute("usertype");
		String userStatus = (String) session.getAttribute("userStatus");
		
		InputFields inputFields = new InputFields();
		InputFields dateRelatedFields;
		
		
		pageElement.setPageTitle(Translator.toLocale("view.manageUsers"));
		
		List<Button> buttonList = new ArrayList<>();
		List<InputFields> dropdownList = new ArrayList<>();
		List<InputFields> inputTypeDateList = new ArrayList<>();
			
			log.info("USER STATUS:::::::::"+userStatus);
			log.info("session value user Type=="+session.getAttribute("usertype"));
			
			
			String[] names= {"HeaderButton",Translator.toLocale("table.registerUser"),"./immigration_register","btnLink","FilterButton", Translator.toLocale("button.filter"),"filter("+ConfigParameters.languageParam+")","submitFilter"};
				for(int i=0; i< names.length ; i++) {
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
	
			//input type date list		
				String[] dateParam= {"date",Translator.toLocale("input.startDate"),"startDate","","date",Translator.toLocale("input.endDate"),"endDate","","text",Translator.toLocale("input.transactionID"),"transactionID","","text",Translator.toLocale("input.passportNo"),"nId",""};
				for(int i=0; i< dateParam.length; i++) {
				dateRelatedFields= new InputFields();
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
