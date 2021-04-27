package org.gl.ceir.datatable.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.gl.ceir.CeirPannelCode.Feignclient.GrievanceFeignClient;
import org.gl.ceir.CeirPannelCode.Model.AttachedFile;
import org.gl.ceir.CeirPannelCode.Model.ConsignmentUpdateRequest;
import org.gl.ceir.CeirPannelCode.Model.GenricResponse;
import org.gl.ceir.CeirPannelCode.Model.TRCRequest;
import org.gl.ceir.Class.HeadersTitle.DatatableResponseModel;
import org.gl.ceir.Class.HeadersTitle.IconsState;
import org.gl.ceir.configuration.ConfigParameters;
import org.gl.ceir.configuration.Translator;
import org.gl.ceir.interfacepackage.CRUD;
import org.gl.ceir.pageElement.model.Button;
import org.gl.ceir.pageElement.model.InputFields;
import org.gl.ceir.pageElement.model.PageElement;
import org.gl.ceir.pagination.model.TrcContentModel;
import org.gl.ceir.pagination.model.TrcPaginationModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

@RestController
@CrossOrigin
public class TRC implements CRUD{
	@Autowired
	Translator Translator;
	@Autowired
	GrievanceFeignClient grievanceFeignClient;	
	@Autowired
	DatatableResponseModel datatableResponseModel;
	@Autowired
	TrcPaginationModel trcPaginationModel;
	@Autowired
	IconsState iconState;
	@Autowired
	PageElement pageElement;
	@Autowired
	Button button;
	
	private final Logger log = LoggerFactory.getLogger(getClass());

	@ResponseBody
	@PostMapping("trc")
	@Override
	public ResponseEntity<?> view(@RequestParam(name="type",defaultValue = "consignment",required = false) String role ,
			 @RequestParam(name="sourceType",required = false) String sourceType,
			 @RequestParam(name = "file", defaultValue = "0", required = false) Integer file,
			 HttpServletRequest request,HttpSession session,
			 @RequestParam(name="sessionFlag",
			 required = false) Integer sessionFlag) {
		// TODO Auto-generated method stub
		String userType = (String) session.getAttribute("usertype");
		log.info("userType in TRC----"+userType);
		String userStatus = (String) session.getAttribute("userStatus");
		List<List<Object>> finalList = new ArrayList<List<Object>>();
		String filter = request.getParameter("filter");
		
		Integer pageSize = Integer.parseInt(request.getParameter("length"));
		Integer pageNo = Integer.parseInt(request.getParameter("start")) / pageSize;
		
		Object response = null;
		Gson gsonObject=new Gson();
		Gson gson=new Gson();
		TRCRequest filterrequest = gsonObject.fromJson(filter, TRCRequest.class);
		filterrequest.setSearchString(request.getParameter("search[value]"));
		log.info("--pageSize-"+pageSize+"----pageNo"+pageNo+"----file"+file+"-filterrequest-------"+filterrequest);
		try {
			response =grievanceFeignClient.viewTRC(filterrequest, pageNo, pageSize, file);
			log.info("response after Feign----->" +response);
			String apiResponse = gson.toJson(response);
			trcPaginationModel = gson.fromJson(apiResponse, TrcPaginationModel.class);
			log.info("apiResponse after Feign----->" +apiResponse);
			if(trcPaginationModel.getContent().isEmpty()) {
				datatableResponseModel.setData(Collections.emptyList());
			}
			else {
				if("CEIRAdmin".equals(userType)){
					log.info("--------in Admin Controller");
					for(TrcContentModel trcContentModelList :trcPaginationModel.getContent()) {
						log.info("inside Trc File Name" +trcContentModelList.getAttachedFiles());
						String fileName1="";
						List<AttachedFile> list = trcContentModelList.getAttachedFiles();
						for(AttachedFile fileList :list)
						{
							fileName1= fileList.getFileName();
							
						}
						
						String createdOn = trcContentModelList.getCreatedOn();
						String requestedDate = trcContentModelList.getRequestDate();
						String manufacturerName = trcContentModelList.getManufacturerName();
						String country = trcContentModelList.getCountry();
						String tac = trcContentModelList.getTac();
						String status = trcContentModelList.getStateInterp();
						String statusInterp = trcContentModelList.getStateInterp();
						String approveRejectionDate = trcContentModelList.getApproveDisapproveDate();
						String adminState = trcContentModelList.getAdminStateInterp();
						String txnId= trcContentModelList.getTxnId();
						String adminApproveStatus = String.valueOf(trcContentModelList.getAdminApproveStatus());
						String approveState = String.valueOf(trcContentModelList.getApproveStatus());	
						
						log.info("status----->" +status+"--Id--------->"+trcContentModelList.getId()+"--fileName1------->"+fileName1+"--txnId------>"+txnId);
						String action = iconState.trcAdminIcons(approveState,trcContentModelList.getId(),fileName1,txnId,adminApproveStatus);
						Object[] data = {createdOn,txnId,requestedDate,manufacturerName,country,tac,statusInterp,approveRejectionDate,adminState,action};
						List<Object> datatableList = Arrays.asList(data);
						finalList.add(datatableList);
						datatableResponseModel.setData(finalList);
					}
				}else if("TRC".equals(userType)) {
					log.info("--------in TRC Controller");
					for(TrcContentModel trcContentModelList :trcPaginationModel.getContent()) {
						String FileName="";
						List<AttachedFile> list = trcContentModelList.getAttachedFiles();
						for(AttachedFile fileList :list)
						{
							FileName= fileList.getFileName();
						}
						String createdOn = trcContentModelList.getCreatedOn();
						String requestedDate = trcContentModelList.getRequestDate();
						String manufacturerName = trcContentModelList.getManufacturerName();
						String country = trcContentModelList.getCountry();
						String tac = trcContentModelList.getTac();
						String status = trcContentModelList.getStateInterp();
						String statusInterp = trcContentModelList.getStateInterp();
						String approveRejectionDate = trcContentModelList.getApproveDisapproveDate();
						String txnId= trcContentModelList.getTxnId();
						String approveState = String.valueOf(trcContentModelList.getApproveStatus());
						log.info("status----->" +status+"--Id--------->"+trcContentModelList.getId()+"--FileName------->"+FileName+"--txnId------>"+txnId);
						String action = iconState.trcManageIcons(approveState,trcContentModelList.getId(),FileName,txnId,userStatus);
						Object[] data = {createdOn,txnId,requestedDate,manufacturerName,country,tac,statusInterp,approveRejectionDate,action};
						List<Object> datatableList = Arrays.asList(data);
						finalList.add(datatableList);
						datatableResponseModel.setData(finalList);
					}
				}

				
		}
			datatableResponseModel.setRecordsTotal(trcPaginationModel.getNumberOfElements());
			datatableResponseModel.setRecordsFiltered(trcPaginationModel.getTotalElements());
			log.info(":::::datatableResponseModel:::::"+datatableResponseModel);
			return new ResponseEntity<>(datatableResponseModel,HttpStatus.OK);	
		}
		catch(Exception e) {
			datatableResponseModel.setRecordsTotal(null);
			datatableResponseModel.setRecordsFiltered(null);
			datatableResponseModel.setData(Collections.emptyList());
			return new ResponseEntity<>(datatableResponseModel, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	
	
	
	@PostMapping("TRC/pageRendering")
	public ResponseEntity<?> directives(@RequestParam(name="type",defaultValue = "TRC",required = false) String role,HttpSession session){

		String userType = (String) session.getAttribute("usertype");
		String userStatus = (String) session.getAttribute("userStatus");
		
		InputFields inputFields = new InputFields();
		InputFields dateRelatedFields;
		
		
		pageElement.setPageTitle(Translator.toLocale("sidebar.Manage_Type-Approved"));
		
		List<Button> buttonList = new ArrayList<>();
		List<InputFields> dropdownList = new ArrayList<>();
		List<InputFields> inputTypeDateList = new ArrayList<>();
			
			log.info("USER STATUS:::::::::"+userStatus);
			log.info("session value user Type=="+session.getAttribute("usertype"));
			
			
			
			if("Importer".equals(userType)) {
				String[] names= {"HeaderButton",Translator.toLocale("table.ReportTypeApprovedDevices"),"./register-form-importer","btnLink","FilterButton", Translator.toLocale("button.filter"),"typeApprovedDataTable()","submitFilter"};
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
			}else {
				String[] names= {"HeaderButton",Translator.toLocale("table.ReportTypeApprovedDevices"),"./register-form","btnLink","FilterButton",Translator.toLocale("button.filter"),"typeApprovedDataTable("+ConfigParameters.languageParam+")","submitFilter"};
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
			}
			
	
			
			
			if("CEIRAdmin".equals(userType)) {
				//Dropdown items
				String[] selectParam= {"select",Translator.toLocale("table.ceirAdminStatus"),"Status",""};
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
			}else if("TRC".equals(userType)){
				//Dropdown items
				String[] selectParam= {"select",Translator.toLocale("table.status"),"Status",""};
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
			}
						
			
			

		
				//input type date list		
				String[] dateParam= {"date",Translator.toLocale("input.startDate"),"startDate","","date",Translator.toLocale("input.endDate"),"endDate","","text",Translator.toLocale("input.transactionID"),"transactionID","","text",Translator.toLocale("table.TAC"),"tac",""};
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
	

	//*********************************************** update consignment Status *******************************************************************************/

	@RequestMapping(value= {"/typeApprove/Approved"},method={org.springframework.web.bind.annotation.RequestMethod.GET,org.springframework.web.bind.annotation.RequestMethod.POST}) 
	public @ResponseBody GenricResponse updateApprovedStatus(@RequestBody ConsignmentUpdateRequest consignmentUpdateRequest,HttpSession session) {
	ConsignmentUpdateRequest request= new ConsignmentUpdateRequest ();
	log.info("enter in update consignment status ."+consignmentUpdateRequest);


	request.setAction(consignmentUpdateRequest.getAction());
	request.setTxnId(consignmentUpdateRequest.getTxnId());
	request.setRoleType((String) session.getAttribute("usertype"));
	request.setRoleTypeUserId((int) session.getAttribute("usertypeId"));
	request.setUserId((int) session.getAttribute("userid"));
	request.setRemarks(consignmentUpdateRequest.getRemarks());
	request.setTxnId(consignmentUpdateRequest.getTxnId());
	request.setFeatureId(consignmentUpdateRequest.getFeatureId());
	log.info(" request passed to the update consignment status="+request);
	//GenricResponse response=feignCleintImplementation.updateConsignmentStatus(request);
	//log.info("response after update consignment status="+response);
	return null;

	}


}