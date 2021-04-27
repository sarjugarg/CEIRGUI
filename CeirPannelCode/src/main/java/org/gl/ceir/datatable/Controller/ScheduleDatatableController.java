package org.gl.ceir.datatable.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.gl.ceir.CeirPannelCode.Feignclient.GsmaFeignClient;
import org.gl.ceir.CeirPannelCode.Feignclient.UserLoginFeignImpl;
import org.gl.ceir.CeirPannelCode.Model.FilterRequest;
import org.gl.ceir.Class.HeadersTitle.DatatableResponseModel;
import org.gl.ceir.Class.HeadersTitle.IconsState;
import org.gl.ceir.configuration.ConfigParameters;
import org.gl.ceir.configuration.Translator;
import org.gl.ceir.pageElement.model.Button;
import org.gl.ceir.pageElement.model.InputFields;
import org.gl.ceir.pageElement.model.PageElement;
import org.gl.ceir.pagination.model.AddressContentModel;
import org.gl.ceir.pagination.model.AddressPaginitionModel;
import org.gl.ceir.pagination.model.ScheduleContentModel;
import org.gl.ceir.pagination.model.SchedulePaginationModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

@RestController
public class ScheduleDatatableController {
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
	ScheduleContentModel scheduleContentModel;
	@Autowired
	SchedulePaginationModel schedulePaginationModel;
	@Autowired
	GsmaFeignClient gsmaFeignClient;
	
	
	@PostMapping("scheduleReportData")
	public ResponseEntity<?> viewCurrencyRecord(@RequestParam(name="type",defaultValue = "scheduleManagement",required = false) String role, HttpServletRequest request,HttpSession session) {
		//String userType = (String) session.getAttribute("usertype");
		//int userId=	(int) session.getAttribute("userid");
		int file=0;
		// Data set on this List
		List<List<Object>> finalList=new ArrayList<List<Object>>();
		String filter = request.getParameter("filter");
		Gson gsonObject=new Gson();
		FilterRequest filterrequest = gsonObject.fromJson(filter, FilterRequest.class);
		Integer pageSize = Integer.parseInt(request.getParameter("length"));
		Integer pageNo = Integer.parseInt(request.getParameter("start")) / pageSize ;
		filterrequest.setSearchString(request.getParameter("search[value]"));
		log.info("pageSize"+pageSize+"-----------pageNo---"+pageNo);
		try {
			log.info("request send to the filter api ="+filterrequest);
			Object response = gsmaFeignClient.viewAllScheduleReport(filterrequest,pageNo,pageSize,file);
			log.info("response in datatable"+response);
			Gson gson= new Gson(); 
			String apiResponse = gson.toJson(response);
			schedulePaginationModel = gson.fromJson(apiResponse, SchedulePaginationModel.class);
			List<ScheduleContentModel> paginationContentList = schedulePaginationModel.getContent();
			if(paginationContentList.isEmpty()) {
				datatableResponseModel.setData(Collections.emptyList());
			}
			else {
			for(ScheduleContentModel dataInsideList : paginationContentList) 
				{
				   String id= String.valueOf(dataInsideList.getId());	
				   String createdOn = dataInsideList.getCreatedOn();
				   String modifiedOn = dataInsideList.getModifiedOn();	
				   String category = dataInsideList.getCategory();
				   String reportName= dataInsideList.getReportName();
				   String emailId = dataInsideList.getEmailId();
				   String userStatus = (String) session.getAttribute("userStatus");	  
				   String action=iconState.scheduleManagementIcons(id,userStatus);
				   Object[] finalData={createdOn,modifiedOn,category,reportName,emailId,action}; 
				   List<Object> finalDataList=new ArrayList<Object>(Arrays.asList(finalData));
					finalList.add(finalDataList);
					datatableResponseModel.setData(finalList);	
					
			}
		}
			//data set on ModelClass
			datatableResponseModel.setRecordsTotal(schedulePaginationModel.getNumberOfElements());
			datatableResponseModel.setRecordsFiltered(schedulePaginationModel.getTotalElements());
			return new ResponseEntity<>(datatableResponseModel, HttpStatus.OK); 
	}catch(Exception e) {
		datatableResponseModel.setRecordsTotal(null);
		datatableResponseModel.setRecordsFiltered(null);
		datatableResponseModel.setData(Collections.emptyList());
		log.error(e.getMessage(),e);
		return new ResponseEntity<>(datatableResponseModel, HttpStatus.OK); 
		}
	}
	
	@PostMapping("scheduleManagement/pageRendering")
	public ResponseEntity<?> pageRendering(@RequestParam(name="type",defaultValue = "scheduleManagement",required = false) String role,HttpSession session){

		String userType = (String) session.getAttribute("usertype");
		String userStatus = (String) session.getAttribute("userStatus");
		
		InputFields inputFields = new InputFields();
		InputFields dateRelatedFields;
		
		pageElement.setPageTitle(Translator.toLocale("sidebar.Schedule_Report"));
		
		List<Button> buttonList = new ArrayList<>();
		List<InputFields> dropdownList = new ArrayList<>();
		List<InputFields> inputTypeDateList = new ArrayList<>();
			
			log.info("USER STATUS:::::::::"+userStatus);
			log.info("session value user Type=="+session.getAttribute("usertype"));
			
			String[] names = { "HeaderButton",Translator.toLocale("button.addSchedule"), "AddShedule()", "btnLink",
					"FilterButton", Translator.toLocale("button.filter"),"scheduleTable(" + ConfigParameters.languageParam + ")", "submitFilter" };
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
		  String[] selectParam={"select",Translator.toLocale("sidebar.ReportCatagory"),"reportCatagory","","select",Translator.toLocale("table.ReportName"),"reportName",""};  
		  for(int i=0; i<selectParam.length; i++) { 
				inputFields= new InputFields();
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
		 
			
			//input type date list		
			String[] dateParam= {"date",Translator.toLocale("input.startDate"),"startDate","", "date", Translator.toLocale("input.endDate"), "endDate", ""};
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
