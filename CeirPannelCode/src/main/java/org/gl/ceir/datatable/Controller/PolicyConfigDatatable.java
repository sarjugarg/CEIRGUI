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
import org.gl.ceir.pageElement.model.Button;
import org.gl.ceir.pageElement.model.InputFields;
import org.gl.ceir.pageElement.model.PageElement;
import org.gl.ceir.pagination.model.PolicyConfigContent;
import org.gl.ceir.pagination.model.PolicyConfigPagination;
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
public class PolicyConfigDatatable {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
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
	PolicyConfigContent policyConfigContent;
	@Autowired
	PolicyConfigPagination policyConfigPagination;
	
	
	@PostMapping("policyConfigData")
	public ResponseEntity<?> viewPolicyConfig(@RequestParam(name="type",defaultValue = "PolicyConfig",required = false) String role, HttpServletRequest request,HttpSession session) {
		//String userType = (String) session.getAttribute("usertype");
		//int userId=	(int) session.getAttribute("userid");
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
				Object response = feignCleintImplementation.policyManagementFeign(filterrequest, pageNo, pageSize, file);
				log.info("response in datatable"+response);
				Gson gson= new Gson(); 
				String apiResponse = gson.toJson(response);
				
				policyConfigPagination = gson.fromJson(apiResponse, PolicyConfigPagination.class);
				List<PolicyConfigContent> paginationContentList = policyConfigPagination.getContent();
				if(paginationContentList.isEmpty()) {
					datatableResponseModel.setData(Collections.emptyList());
				}else {
					for(PolicyConfigContent dataInsideList : paginationContentList) 
					{
					   String createdOn = (String) dataInsideList.getCreatedOn();
					   String modifiedOn = (String) dataInsideList.getModifiedOn();
					   String description = dataInsideList.getDescription();
					   String value = dataInsideList.getValue();
					   String tag = dataInsideList.getTag();
					   Object period = dataInsideList.getPeriod();
					   Object statusInterp = dataInsideList.getStatusInterp();
					   String Status = String.valueOf(dataInsideList.getStatus());
					   String type = String.valueOf(dataInsideList.getType());
					   String typeInterp = dataInsideList.getTypeInterp();
					   String policyOrder =String.valueOf(dataInsideList.getPolicyOrder());
					   String userStatus = (String) session.getAttribute("userStatus");
					   String action=iconState.policyConfigIcons(userStatus,tag,Status,type);			   
					   Object[] finalData={createdOn,modifiedOn,description,value,tag ,period,statusInterp,action}; 
						List<Object> finalDataList=new ArrayList<Object>(Arrays.asList(finalData));
						finalList.add(finalDataList);
						datatableResponseModel.setData(finalList);	
				}
			}
				
				//data set on ModelClass
				datatableResponseModel.setRecordsTotal(policyConfigPagination.getNumberOfElements());
				datatableResponseModel.setRecordsFiltered(policyConfigPagination.getTotalElements());
				return new ResponseEntity<>(datatableResponseModel, HttpStatus.OK); 
			
			}catch(Exception e) {
				
				datatableResponseModel.setRecordsTotal(null);
				datatableResponseModel.setRecordsFiltered(null);
				datatableResponseModel.setData(Collections.emptyList());
				log.error(e.getMessage(),e);
				return new ResponseEntity<>(datatableResponseModel, HttpStatus.INTERNAL_SERVER_ERROR); 
			}		
			
		}
		
	
	@PostMapping("policyConfig/pageRendering")
	public ResponseEntity<?> pageRendering(@RequestParam(name="type",defaultValue = "Config",required = false) String role,HttpSession session){

		String userType = (String) session.getAttribute("usertype");
		String userStatus = (String) session.getAttribute("userStatus");
		
		InputFields inputFields = new InputFields();
		InputFields dateRelatedFields;
		
		pageElement.setPageTitle("Policy Management");
		
		List<Button> buttonList = new ArrayList<>();
		List<InputFields> dropdownList = new ArrayList<>();
		List<InputFields> inputTypeDateList = new ArrayList<>();
			
			log.info("USER STATUS:::::::::"+userStatus);
			log.info("session value user Type=="+session.getAttribute("usertype"));
			
			String[] names= {"FilterButton", "filter","configManagementDatatable("+ConfigParameters.languageParam+")","submitFilter"};
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
			String[] selectParam= {"select","Type","type","","select","Status","status",""};
			for(int i=0; i< selectParam.length; i++) {
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
			
		
			
			pageElement.setInputTypeDateList(inputTypeDateList);
			pageElement.setUserStatus(userStatus);
			return new ResponseEntity<>(pageElement, HttpStatus.OK); 
		
		
	}
	
}
