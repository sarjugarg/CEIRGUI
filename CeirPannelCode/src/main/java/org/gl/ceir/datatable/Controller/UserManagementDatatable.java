package org.gl.ceir.datatable.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.gl.ceir.CeirPannelCode.Feignclient.UserProfileFeignImpl;
import org.gl.ceir.CeirPannelCode.Model.FilterRequest;
import org.gl.ceir.Class.HeadersTitle.DatatableResponseModel;
import org.gl.ceir.Class.HeadersTitle.IconsState;
import org.gl.ceir.configuration.ConfigParameters;
import org.gl.ceir.configuration.Translator;
import org.gl.ceir.pageElement.model.Button;
import org.gl.ceir.pageElement.model.InputFields;
import org.gl.ceir.pageElement.model.PageElement;
import org.gl.ceir.pagination.model.UserManagementContent;
import org.gl.ceir.pagination.model.UserManagementPagination;
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
public class UserManagementDatatable {
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
	IconsState iconState;
	@Autowired
	UserProfileFeignImpl userProfileFeignImpl;
	@Autowired
	UserManagementContent userManagementContent;
	@Autowired
	UserManagementPagination userManagementPagination;
	
	@PostMapping("systemUserManagementData")
	public ResponseEntity<?> viewuserManagement(@RequestParam(name="type",defaultValue = "userManagement",required = false) String role, HttpServletRequest request,HttpSession session) {
		//String userType = (String) session.getAttribute("usertype");
		//int userId=	(int) session.getAttribute("userid");
		int file=0;
		// Data set on this List
		List<List<Object>> finalList=new ArrayList<List<Object>>();
		String filter = request.getParameter("filter");
		Gson gsonObject=new Gson();
		FilterRequest filterrequest = gsonObject.fromJson(filter, FilterRequest.class);

		String column=null;
		 column="0".equalsIgnoreCase(request.getParameter("order[0][column]")) ? "Created On":
				"1".equalsIgnoreCase(request.getParameter("order[0][column]")) ? "Modified On":
					"2".equalsIgnoreCase(request.getParameter("order[0][column]")) ? "User Type":
						"3".equalsIgnoreCase(request.getParameter("order[0][column]")) ? "Status":
							"Modified On";
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
		
		Integer pageSize = Integer.parseInt(request.getParameter("length"));
		Integer pageNo = Integer.parseInt(request.getParameter("start")) / pageSize ;
		filterrequest.setSearchString(request.getParameter("search[value]"));
		log.info("pageSize"+pageSize+"-----------pageNo---"+pageNo);
		try{
			filterrequest.setPublicIp(session.getAttribute("publicIP").toString());
			filterrequest.setBrowser(session.getAttribute("browser").toString());
			log.info("request send to the filter api ="+filterrequest);
			Object response = userProfileFeignImpl.viewUserTypeRequest(filterrequest,pageNo,pageSize,file);
			log.info("response in datatable"+response);
			Gson gson= new Gson(); 
			String apiResponse = gson.toJson(response);
			userManagementPagination = gson.fromJson(apiResponse, UserManagementPagination.class);
			List<UserManagementContent> paginationContentList = userManagementPagination.getContent();
			if(paginationContentList.isEmpty()){
				datatableResponseModel.setData(Collections.emptyList());
			}else {
				for(UserManagementContent dataInsideList : paginationContentList) 
				{
				   String id= String.valueOf(dataInsideList.getId());	
				   String createdOn= dataInsideList.getCreatedOn();
				   String modifiedOn = dataInsideList.getModifiedOn();
				   String usertypeName = dataInsideList.getUsertypeName();
				   String status = String.valueOf(dataInsideList.getStatus());
				   String statusInterp = dataInsideList.getStatusInterp();
				   String userStatus = (String) session.getAttribute("userStatus");	  
				   String action=iconState.userManagementIcons(id,userStatus);			   
				   Object[] finalData={createdOn,modifiedOn,usertypeName,statusInterp,action}; 
				   List<Object> finalDataList=new ArrayList<Object>(Arrays.asList(finalData));
					finalList.add(finalDataList);
					datatableResponseModel.setData(finalList);	
					
			}
				
			}
			datatableResponseModel.setRecordsTotal(userManagementPagination.getNumberOfElements());
			datatableResponseModel.setRecordsFiltered(userManagementPagination.getTotalElements());
			return new ResponseEntity<>(datatableResponseModel, HttpStatus.OK); 
			
			
			
		}catch(Exception e) {
			datatableResponseModel.setRecordsTotal(null);
			datatableResponseModel.setRecordsFiltered(null);
			datatableResponseModel.setData(Collections.emptyList());
			log.error(e.getMessage(),e);
			return new ResponseEntity<>(datatableResponseModel, HttpStatus.INTERNAL_SERVER_ERROR); 
			}
			
		}
		
		
	@PostMapping("systemUserManagement/pageRendering")
	public ResponseEntity<?> pageRendering(@RequestParam(name="type",defaultValue = "userManagement",required = false) String role,HttpSession session){

		String userType = (String) session.getAttribute("usertype");
		String userStatus = (String) session.getAttribute("userStatus");
		
		InputFields inputFields = new InputFields();
		InputFields dateRelatedFields;
		
		pageElement.setPageTitle(Translator.toLocale("User Type Management"));
		
		List<Button> buttonList = new ArrayList<>();
		List<InputFields> dropdownList = new ArrayList<>();
		List<InputFields> inputTypeDateList = new ArrayList<>();
			
			log.info("USER STATUS:::::::::"+userStatus);
			log.info("session value user Type=="+session.getAttribute("usertype"));
			
			String[] names = { "HeaderButton", Translator.toLocale("button.addCurrency"), "AddCurrencyAddress()", "btnLink",
					"FilterButton", Translator.toLocale("button.filter"),"userManangementTable(" + ConfigParameters.languageParam + ")", "submitFilter" };
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
			
		
		  //Dropdown items 
		/*
		 * String[]
		 * selectParam={"select",Translator.toLocale("select.userType"),"userType",""};
		 * for(int i=0; i<selectParam.length; i++) { inputFields= new InputFields();
		 * inputFields.setType(selectParam[i]); i++;
		 * inputFields.setTitle(selectParam[i]); i++; inputFields.setId(selectParam[i]);
		 * i++; inputFields.setClassName(selectParam[i]); dropdownList.add(inputFields);
		 * } pageElement.setDropdownList(dropdownList);
		 */
		 
			
			//input type date list		
			String[] dateParam= {"date",Translator.toLocale("input.startDate"),"startDate","","date",Translator.toLocale("input.endDate"),"endDate","","select",Translator.toLocale("select.userType"),"userType","","select",Translator.toLocale("table.status"),"statusID",""};
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
