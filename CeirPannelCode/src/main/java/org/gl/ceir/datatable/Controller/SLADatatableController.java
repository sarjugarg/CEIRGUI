package org.gl.ceir.datatable.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.gl.ceir.CeirPannelCode.Feignclient.UserProfileFeignImpl;
import org.gl.ceir.CeirPannelCode.Model.FilterRequest;
import org.gl.ceir.CeirPannelCode.Model.SLAContentModel;
import org.gl.ceir.CeirPannelCode.Model.SLAfilterRequest;
import org.gl.ceir.Class.HeadersTitle.DatatableResponseModel;
import org.gl.ceir.Class.HeadersTitle.IconsState;
import org.gl.ceir.configuration.ConfigParameters;
import org.gl.ceir.configuration.Translator;
import org.gl.ceir.pageElement.model.Button;
import org.gl.ceir.pageElement.model.InputFields;
import org.gl.ceir.pageElement.model.PageElement;
import org.gl.ceir.pagination.model.SLAPaginationModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

@RestController
public class SLADatatableController {
		
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
	SLAContentModel slaContentModel;
	@Autowired
	SLAPaginationModel slaPaginationModel;
	
	@PostMapping("slaData") 
	public ResponseEntity<?> viewPendingTacList(HttpServletRequest request,HttpSession session) {
		//String userType = (String) session.getAttribute("usertype");
		//int userId=	(int) session.getAttribute("userid");
		int file=0;
		// Data set on this List
		List<List<Object>> finalList=new ArrayList<List<Object>>();
		String filter = request.getParameter("filter");
		Gson gsonObject=new Gson();
		SLAfilterRequest filterRequest = gsonObject.fromJson(filter, SLAfilterRequest.class);
		Integer pageSize = Integer.parseInt(request.getParameter("length"));
		Integer pageNo = Integer.parseInt(request.getParameter("start")) / pageSize ;
		filterRequest.setSearchString(request.getParameter("search[value]"));
		

		String column="0".equalsIgnoreCase(request.getParameter("order[0][column]")) ? "Created On":
			"1".equalsIgnoreCase(request.getParameter("order[0][column]")) ? "User ID":
				"2".equalsIgnoreCase(request.getParameter("order[0][column]")) ? "Transaction ID":
					"3".equalsIgnoreCase(request.getParameter("order[0][column]")) ? "User Type" :
							"4".equalsIgnoreCase(request.getParameter("order[0][column]")) ? "Feature Name" :
									"5".equalsIgnoreCase(request.getParameter("order[0][column]")) ? "Status"
						:"Modified On";
		
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
		filterRequest.setOrderColumnName(column);
		filterRequest.setOrder(order);
		
		try {
			filterRequest.setPublicIp(session.getAttribute("publicIP").toString());
			filterRequest.setBrowser(session.getAttribute("browser").toString());
			log.info("request send to the filter api ="+filterRequest);
			Object response = userProfileFeignImpl.viewSLARequest(filterRequest, pageNo, pageSize, file);
			log.info("response in datatable"+response);
			Gson gson= new Gson(); 
			String apiResponse = gson.toJson(response);
			slaPaginationModel = gson.fromJson(apiResponse, SLAPaginationModel.class);
			List<SLAContentModel> paginationContentList = slaPaginationModel.getContent();
			
			if(paginationContentList.isEmpty()){
				datatableResponseModel.setData(Collections.emptyList());
				
			}else {
				for(SLAContentModel dataInsideList : paginationContentList) 
				{
				   String id= String.valueOf(dataInsideList.getId());	
				   String createdOn= dataInsideList.getCreatedOn();
				   String userName = dataInsideList.getUsername();
				   String txnId = dataInsideList.getTxnId();
				   String userTypeName = dataInsideList.getUsertype();
				   String featureName = dataInsideList.getFeatureName();
				   String Status = dataInsideList.getStateInterp();
				  //String userStatus = (String) session.getAttribute("userStatus");	  
				   Object[] finalData={createdOn,userName,txnId,userTypeName,featureName,Status}; 
				   List<Object> finalDataList=new ArrayList<Object>(Arrays.asList(finalData));
					finalList.add(finalDataList);
					datatableResponseModel.setData(finalList);	
			}
				
			}
			datatableResponseModel.setRecordsTotal(slaPaginationModel.getNumberOfElements());
			datatableResponseModel.setRecordsFiltered(slaPaginationModel.getTotalElements());
			return new ResponseEntity<>(datatableResponseModel, HttpStatus.OK); 
			
		}catch (Exception e) {
			datatableResponseModel.setRecordsTotal(null);
			datatableResponseModel.setRecordsFiltered(null);
			datatableResponseModel.setData(Collections.emptyList());
			log.error(e.getMessage(),e);
			return new ResponseEntity<>(datatableResponseModel, HttpStatus.INTERNAL_SERVER_ERROR); 
			
		}
	}
	
	
	
	@PostMapping("sla/pageRendering")
	public ResponseEntity<?> pageRendering(HttpSession session){

		//String userType = (String) session.getAttribute("usertype");
		String userStatus = (String) session.getAttribute("userStatus");
		
		InputFields inputFields = new InputFields();
		InputFields dateRelatedFields;
		
		pageElement.setPageTitle(Translator.toLocale("sidebar.SLA_Management"));
		
		List<Button> buttonList = new ArrayList<>();
		List<InputFields> dropdownList = new ArrayList<>();
		List<InputFields> inputTypeDateList = new ArrayList<>();
			
			log.info("USER STATUS:::::::::"+userStatus);
			log.info("session value user Type=="+session.getAttribute("usertype"));
			
			String[] names = { "HeaderButton", Translator.toLocale("button.addCurrency"), "AddCurrencyAddress()", "btnLink",
					"FilterButton", Translator.toLocale("button.filter"),"DataTable(" + ConfigParameters.languageParam + ")", "submitFilter" };
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
			
		
			
		
			
			String[] dateParam= {"date",Translator.toLocale("input.startDate"),"startDate","","date",Translator.toLocale("input.endDate"),"endDate","",
					"text",Translator.toLocale("table.UserName"),"username","","text",Translator.toLocale("table.transactionID"),"transactionid","",
					"select",Translator.toLocale("table.userType"),"userType","","select",Translator.toLocale("table.featureName"),"feature",""};
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
