package org.gl.ceir.CeirPannelCode.Controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.crypto.Data;

import org.gl.ceir.CeirPannelCode.PropertyReader;
import org.gl.ceir.CeirPannelCode.Feignclient.FeignCleintImplementation;
import org.gl.ceir.CeirPannelCode.Feignclient.GrievanceFeignClient;
import org.gl.ceir.CeirPannelCode.Feignclient.ImmigrationFeignImpl;
import org.gl.ceir.CeirPannelCode.Feignclient.UploadPaidStatusFeignClient;
import org.gl.ceir.CeirPannelCode.Feignclient.UserPaidStatusFeignClient;
import org.gl.ceir.CeirPannelCode.Feignclient.UserRegistrationFeignImpl;
import org.gl.ceir.CeirPannelCode.Model.AddMoreFileModel;
import org.gl.ceir.CeirPannelCode.Model.AllRequest;
import org.gl.ceir.CeirPannelCode.Model.EndUserVisaInfo;
import org.gl.ceir.CeirPannelCode.Model.FileCopyToOtherServer;
import org.gl.ceir.CeirPannelCode.Model.FileExportResponse;
import org.gl.ceir.CeirPannelCode.Model.FilterRequest;
import org.gl.ceir.CeirPannelCode.Model.FilterRequest_UserPaidStatus;
import org.gl.ceir.CeirPannelCode.Model.GenricResponse;
import org.gl.ceir.CeirPannelCode.Model.PeriodValidate;
import org.gl.ceir.CeirPannelCode.Model.UpdateVisaModel;
import org.gl.ceir.CeirPannelCode.Model.UserHeader;
import org.gl.ceir.CeirPannelCode.Model.VisaDb;
import org.gl.ceir.CeirPannelCode.Service.RegistrationService;
import org.gl.ceir.CeirPannelCode.Util.HttpResponse;
import org.gl.ceir.CeirPannelCode.Util.UtilDownload;
import org.gl.ceir.pagination.model.UserPaidStatusContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import CeirPannelCode.Model.Register_UploadPaidStatus;

@Controller
public class UploadPaidStatusView {


	private final Logger log = LoggerFactory.getLogger(getClass());

	
	
	
	@Autowired
	UtilDownload utildownload;

	@Autowired
	UserPaidStatusFeignClient userPaidStatusFeignClient;

	@Autowired
	UploadPaidStatusFeignClient uploadPaidStatusFeignClient;

	@Autowired
	ImmigrationFeignImpl immigrationFeignImpl;

	@Autowired
	UserRegistrationFeignImpl userRegistrationFeignImpl;
@Autowired
AddMoreFileModel addMoreFileModel,urlToUpload,urlToMove;

@Autowired

FeignCleintImplementation feignCleintImplementation;

@Autowired
PropertyReader propertyReader;

@Autowired
GrievanceFeignClient grievanceFeignClient;

@Autowired
RegistrationService registerService;

	@GetMapping("uploadPaidStatus")
	public ModelAndView pageView(@RequestParam(name="via", required = false) String via,@RequestParam(name="NID", required = false) String NID,HttpSession session
			,@RequestParam(name="txnID",required = false) String txnID,@RequestParam(name="source",defaultValue ="menu" ,required = false) String source) {
		ModelAndView modelAndView = new ModelAndView();
		String userStatus = (String) session.getAttribute("userStatus");
		log.info("The user Status is " +userStatus);
		try {
		if((session.getAttribute("usertype").equals("CEIRAdmin") || session.getAttribute("usertype").equals("DRT")) && !("other".equals(via))) {
			session.setAttribute("filterSource", source);
			session.setAttribute("userStatus", userStatus);
			modelAndView.setViewName("uploadPaidStatus");
			
		}
		else if("other".equals(via)) {
			session.setAttribute("filterSource", source);
			session.setAttribute("userStatus", userStatus);
			modelAndView.setViewName("uploadPaidStatus");
		}
		else if("dashboard".equals(source)) {
			session.setAttribute("filterSource", source);
			session.setAttribute("userStatus", userStatus);
			modelAndView.setViewName("uploadPaidStatus");
		}
		else{
			modelAndView.setViewName("nidForm");
			session.setAttribute("userStatus", userStatus);
		}}
		catch (Exception e) {
			// TODO: handle exception
			log.info("this is catch block session is blank or something went wrong.");
		}
		return modelAndView;
	}


	@GetMapping("add-device-information")
	public ModelAndView deviceInformationView(HttpSession session) {
		log.info("enter in add device page.");
		String userType=(String) session.getAttribute("usertype");
		ModelAndView modelAndView = new ModelAndView();
		log.info("userType==="+userType);
		if(userType!=null)
		{
			modelAndView.setViewName("addDeviceInformation");		
		}
		else 
		{
			modelAndView.setViewName("endUserAddDevice");
		}
		
		return modelAndView;
	}
	

	@GetMapping("view-device-information/{imei}/{txnId}")
	public ModelAndView viewDeviceInformationView(@PathVariable("imei") String imei,@PathVariable("txnId") String txnId,HttpSession session,
			@RequestParam( name="transactionID" , required = false) String transactionID,@RequestParam(name="source") String source) {
		log.info(" imei =="+imei+"  txnid=="+txnId+"  source=="+source);
		String userType=(String) session.getAttribute("usertype"); 
		  String  userName= (String) session.getAttribute("username").toString(); 
		  int userId= (int) session.getAttribute("userid"); 
		  int userTypeid=(int)  session.getAttribute("usertypeId");
		  AllRequest request= new AllRequest();
		  request.setFeatureId(12);
		  request.setUserId(userId);
		  request.setImei(imei);
		  request.setUsername(userName);
		  request.setUserTypeId(userTypeid);
		  request.setUserType(userType);
		  request.setTxnId(txnId);
		  request.setPublicIp(session.getAttribute("publicIP").toString());
		  request.setBrowser(session.getAttribute("browser").toString());
		ModelAndView modelAndView = new ModelAndView("viewAdddeviceInformation");
		log.info("request info send to view api device api= "+request);
		UserPaidStatusContent content= uploadPaidStatusFeignClient.viewByImei(request);
		log.info(" content =="+content);

		addMoreFileModel.setTag("upload_file_link");
        urlToUpload=feignCleintImplementation.addMoreBuutonCount(addMoreFileModel);
        log.info("file link =="+urlToUpload.getValue());
       // content.setFilePreviewLink(urlToUpload.getValue());
		String fileLink=urlToUpload.getValue();
		modelAndView.addObject("fileLink", fileLink);
        modelAndView.addObject("viewInformation", content);
        
        
        if(userType!=null) {
        	modelAndView.setViewName("viewAdddeviceInformation");	
        }
        else {
        	modelAndView.setViewName("endUserViewDeviceInformation");
        }
		return modelAndView;
	}


	@PostMapping("uploadPaidStatusForm")
	public @ResponseBody GenricResponse register(@RequestParam(name="file",required = false) MultipartFile file,HttpServletRequest request,HttpSession session,
			@RequestParam(name="sourceType") String source) {
		log.info("-inside controller register-approved-device-------request---------");
		// log.info(""+request.getParameter("file"));
		String userName=session.getAttribute("username").toString();
		String userId= session.getAttribute("userid").toString();
		String name=session.getAttribute("name").toString();
		String txnNumber="";
		if (source.equalsIgnoreCase("Custom"))
			
		{
			 txnNumber="R" + utildownload.getTxnId();	
		}
		else if(source.equalsIgnoreCase("Immigration"))
		{
			 txnNumber="I" + utildownload.getTxnId();
		}
		
		log.info("Random transaction id number="+txnNumber);
		//request.setAttribute("txnId", txnNumber);
		//request.setAttribute("request[regularizeDeviceDbs][txnId]",txnNumber);
		String filter = request.getParameter("request");
		//log.info("txnid+++++++++++"+request.getParameter("request[regularizeDeviceDbs][txnId]"));
		Gson gson= new Gson(); 

		log.info("*********"+filter);
		addMoreFileModel.setTag("system_upload_filepath");
		urlToUpload=feignCleintImplementation.addMoreBuutonCount(addMoreFileModel);
		
		/*
		 * FileCopyToOtherServer fileCopyRequest= new FileCopyToOtherServer();
		 * fileCopyRequest.setFilePath(urlToUpload.getValue());
		 * fileCopyRequest.setTxnId(txnNumber);
		 * fileCopyRequest.setFileName(file.getOriginalFilename());
		 * fileCopyRequest.setServerId(serverId);
		 * log.info("request passed to move file to other server=="+fileCopyRequest);
		 * GenricResponse
		 * fileRespnose=grievanceFeignClient.saveUploadedFileOnANotherServer(
		 * fileCopyRequest); log.info("file move api response==="+fileRespnose);
		 */

		Register_UploadPaidStatus regularizeDeviceDbs  = gson.fromJson(filter, Register_UploadPaidStatus.class);
		regularizeDeviceDbs.setNationality("Cambodian");
		for(int i =0; i<regularizeDeviceDbs.getRegularizeDeviceDbs().size();i++) {
			regularizeDeviceDbs.getRegularizeDeviceDbs().get(i).setTxnId(txnNumber);
		}

		log.info(""+regularizeDeviceDbs.toString());
		log.info(" upload status  entry point.");
		/*
		 * try { byte[] bytes = file.getBytes(); String rootPath
		 * =urlToUpload.getValue()+txnNumber+"/"; File dir = new File(rootPath +
		 * File.separator);
		 * 
		 * if (!dir.exists()) dir.mkdirs(); // Create the file on server File serverFile
		 * = new File(rootPath+file.getOriginalFilename());
		 * log.info("uploaded file path on server" + serverFile); BufferedOutputStream
		 * stream = new BufferedOutputStream(new FileOutputStream(serverFile));
		 * stream.write(bytes); stream.close(); }
		 * 
		 * catch (Exception e) { // TODO: handle exception e.printStackTrace(); }
		 */
		UserHeader header=registerService.getUserHeaders(request);
		regularizeDeviceDbs.setPublicIp(header.getPublicIp());
		regularizeDeviceDbs.setBrowser(header.getBrowser());
		log.info("request passed to the save regularizeDeviceDbs api"+regularizeDeviceDbs);
		GenricResponse response = null;
		try {
			response = userPaidStatusFeignClient.uploadPaidUser(regularizeDeviceDbs);
			//GenricResponse response = null;
			log.info("---------response--------"+response);
		}
		catch (Exception e) {
			// TODO: handle exception
			log.info("exception in upload paid stat error"+e);
			e.printStackTrace();

		}
		return response;
	}

	//***************************************** Export Registration controller *********************************

	@RequestMapping(value="/exportPaidStatus",method ={org.springframework.web.bind.annotation.RequestMethod.GET})
	public String exportToExcel(@RequestParam(name="startDate", required = false) String startDate,
			@RequestParam(name="endDate",required = false) String endDate,
			@RequestParam(name="taxPaidStatus",required = false) Integer taxPaidStatus,
			@RequestParam(name="deviceIdType", required = false) Integer deviceIdType,
			@RequestParam(name="deviceType",required = false) Integer deviceType,
			@RequestParam(name="nid",required = false) String nid,
			@RequestParam(name="origin",required = false) String origin,
			@RequestParam(name="txnId",required = false) String txnId,
			@RequestParam(name="pageSize") Integer pageSize,
			@RequestParam(name="pageNo") Integer pageNo,
			@RequestParam(name="status", required = false) Integer status,
			@RequestParam(name="nationality",required = false) String nationality,
			HttpServletRequest request,
			HttpSession session)
	{

		int userId= (int) session.getAttribute("userid");
		int file=1;
		
		String userName=(String) session.getAttribute("username").toString();
		log.info("username value=="+userName);
		int userTypeId =(int) session.getAttribute("usertypeId");
		String userType=(String) session.getAttribute("usertype"); 	
		
		FileExportResponse fileExportResponse;
		FilterRequest_UserPaidStatus filterRequestuserpaidStatus = new FilterRequest_UserPaidStatus();
		filterRequestuserpaidStatus.setCreatedOn(startDate);
		filterRequestuserpaidStatus.setModifiedOn(endDate);
		filterRequestuserpaidStatus.setStartDate(startDate);
		filterRequestuserpaidStatus.setEndDate(endDate);
		filterRequestuserpaidStatus.setTaxPaidStatus(taxPaidStatus);
		filterRequestuserpaidStatus.setDeviceIdType(deviceIdType);
		filterRequestuserpaidStatus.setDeviceType(deviceType);
		filterRequestuserpaidStatus.setNid(nid);
		filterRequestuserpaidStatus.setTxnId(txnId);
		filterRequestuserpaidStatus.setUserId(userId);
		filterRequestuserpaidStatus.setStatus(status); 
		filterRequestuserpaidStatus.setUsername(userName);
		filterRequestuserpaidStatus.setUserTypeId(userTypeId);
		filterRequestuserpaidStatus.setFeatureId(12);
		filterRequestuserpaidStatus.setUserType(userType);
		filterRequestuserpaidStatus.setNationality(nationality);
		filterRequestuserpaidStatus.setOrigin(origin);
		
		UserHeader header=registerService.getUserHeaders(request);
		filterRequestuserpaidStatus.setPublicIp(header.getPublicIp());
		filterRequestuserpaidStatus.setBrowser(header.getBrowser());
		log.info(" request passed to the exportTo Excel Api =="+filterRequestuserpaidStatus+" *********** pageSize"+pageSize+"  pageNo  "+pageNo);
		Object response = userPaidStatusFeignClient.consignmentFilter(filterRequestuserpaidStatus, pageNo, pageSize, file,"filter");
		Gson gson= new Gson(); 
		String apiResponse = gson.toJson(response);
		fileExportResponse = gson.fromJson(apiResponse, FileExportResponse.class);
		log.info("response  from   export grievance  api="+fileExportResponse);
		return "redirect:"+fileExportResponse.getUrl();
	}



	//***********************************************cuurency controller *************************************************
	@RequestMapping(value="/countByNid",method={org.springframework.web.bind.annotation.RequestMethod.GET}) 
	public @ResponseBody GenricResponse countByNid(@RequestParam(name="nid", required = false) String nId,@RequestParam(name="nationType", required = false) int nationType)  {
		log.info("request send to the currency  api="+nId+"  nationType   =="+nationType);
		GenricResponse response= uploadPaidStatusFeignClient.countByNid(nId,nationType);
		log.info("response from currency api "+response);
		return response;

	}

	
	//********************************************Admin Approve/Reject Controller******************************************
	
	@PostMapping("approveRejectDevice") 
	public @ResponseBody GenricResponse approveRejectDevice (@RequestBody FilterRequest_UserPaidStatus filterRequestuserpaidStatus ,HttpSession session)  {
		filterRequestuserpaidStatus.setPublicIp(session.getAttribute("publicIP").toString());
		filterRequestuserpaidStatus.setBrowser(session.getAttribute("browser").toString());
		log.info("request send to the approveRejectDevice api="+filterRequestuserpaidStatus);
		GenricResponse response= uploadPaidStatusFeignClient.approveRejectFeign(filterRequestuserpaidStatus);

		log.info("response from currency api "+response);
		return response;

		}
	
	
	
	
	


	@ResponseBody
	@PutMapping("tax-paid/status")
	public GenricResponse taxPaidStatusUpdate(@RequestBody Register_UploadPaidStatus model,HttpSession session) {
		
		try {
			log.info("11");
		String roleType=String.valueOf(session.getAttribute("usertype"));
		String userName=session.getAttribute("username").toString();
		int userId= (int) session.getAttribute("userid");  
		int userTypeId =(int) session.getAttribute("usertypeId");
		
		 AllRequest request= new AllRequest();
		 request.setFeatureId(12);
		 request.setUsername(userName);
		 request.setUserId(userId);
		 request.setUserType(roleType);
         request.setUserTypeId(userTypeId);
		 model.setAuditParameters(request);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		model.setPublicIp(session.getAttribute("publicIP").toString());
		model.setBrowser(session.getAttribute("browser").toString());
		log.info("request passed to the tax pay api="+model);
		GenricResponse response = userPaidStatusFeignClient.tax(model);
		//model.getAuditParameters().setUserId(userId);
		
		log.info("---------response--------"+response);
		return response;
	}
	
	
	@GetMapping("selfRegisterDevice")
	public ModelAndView selfRegisterDevice(HttpSession session) {
		HttpResponse httpResponse=userRegistrationFeignImpl.periodValidate(new PeriodValidate(17L, 12L));
		ModelAndView modelAndView = new ModelAndView();
		if(httpResponse.getStatusCode() == 200) {
		log.info("---entry point in enter nid page");
		modelAndView.setViewName("endUserNid");
		log.info("---exit  point in enter nid page");
		}
		else {
			modelAndView.setViewName("registrationPopup");
		}
		return modelAndView;
	}
	
	@PostMapping("selfRegisterDevicePage")
	public ModelAndView selfRegisterDevicePage(@RequestParam(name="Search",required = false) String Search,@RequestParam(name="sourceType",required = false) String sourceType,
			@RequestParam(name="lang",required = false) String lang) {
		ModelAndView modelAndView = new ModelAndView();
		log.info("---entry point in self register page=="+Search+"  sourceType   =="+sourceType+"  --lang--"+lang);
		modelAndView.addObject("nid", Search);
		if(sourceType!=null)
		{
			log.info("enter in  sourceType=."+sourceType);
			modelAndView.setViewName("selfRegisterDevice");	
		}
		else if(Search!=null )  {
			log.info("enter in self register source type is null   page.");
			modelAndView.setViewName("selfRegisterDevice");
		}
		else {
		modelAndView.setViewName("endUserNid");
		}
		log.info("---exit  point in self register page");
		return modelAndView;
	}
	
	
	@GetMapping("updateVisavalidity")
	public ModelAndView updateVisavalidity(HttpSession session) {
		HttpResponse httpResponse=userRegistrationFeignImpl.periodValidate(new PeriodValidate(17L, 43L));
		ModelAndView modelAndView = new ModelAndView();
		if(httpResponse.getStatusCode() == 200) {
		log.info("---entry point in update visa validity page");
		modelAndView.setViewName("endUserUpdateVisaValidity");
		log.info("---exit  point in update visa validity page");
		}
		else {
			modelAndView.setViewName("registrationPopup");
		}
		return modelAndView;
	}
	
	@PostMapping("findEndUserByNid")
	public @ResponseBody GenricResponse findEndUserByNid(@RequestParam(name="findEndUserByNid",required = false) String findEndUserByNid,HttpSession session,HttpServletRequest requestHeader) {
		log.info("---entry point in update visa validity page");
		GenricResponse endUserVisaInfo= new GenricResponse();
		  AllRequest request= new AllRequest();
		  request.setFeatureId(12);

		  request.setNid(findEndUserByNid);
	      request.setUserTypeId(17);
		  request.setUserType("End User");
		  UserHeader header=registerService.getUserHeaders(requestHeader);
		  request.setPublicIp(header.getPublicIp());
		  request.setBrowser(header.getBrowser());
		  request.setPublicIp(header.getPublicIp());
		  request.setBrowser(header.getBrowser());
		log.info("Request send to the fetch record by Passport="+request);
		addMoreFileModel.setTag("upload_file_link");
        urlToUpload=feignCleintImplementation.addMoreBuutonCount(addMoreFileModel);
        log.info("file link =="+urlToUpload.getValue());
       // content.setFilePreviewLink(urlToUpload.getValue());
		String fileLink=urlToUpload.getValue();
		endUserVisaInfo=	uploadPaidStatusFeignClient.fetchVisaDetailsbyPassport(request);
		log.info("Response from fetchVisaDetailsbyPassport api== "+endUserVisaInfo);
		endUserVisaInfo.setResponse(fileLink);
		log.info("---exit  point in update visa validity page");
		return endUserVisaInfo;
	}
	
	@PostMapping("updateEndUSerVisaValidity")
	public @ResponseBody GenricResponse updateEndUSerVisaValidity(@RequestParam(name="passportImage",required = false) MultipartFile passportImage,@RequestParam(name="visaImage",required = false) MultipartFile visaImage,
			  @RequestParam(name="existingTxnId",required = false) String existingTxnId,HttpServletRequest request,HttpSession session) {
		log.info("---entry point in update visa validity page existingTxnId =="+existingTxnId);
		String filter = request.getParameter("request");
		//log.info("txnid+++++++++++"+request.getParameter("request[regularizeDeviceDbs][txnId]"));
		Gson gson= new Gson(); 
		  String txnNumber="A" + utildownload.getTxnId();	
			log.info("Random transaction id number="+txnNumber);
		log.info("before casting request in to pojo classs"+filter);
		addMoreFileModel.setTag("system_upload_filepath");
		urlToUpload=feignCleintImplementation.addMoreBuutonCount(addMoreFileModel);

		addMoreFileModel.setTag("uploaded_file_move_path");
		urlToMove=feignCleintImplementation.addMoreBuutonCount(addMoreFileModel);
		 FileCopyToOtherServer fileCopyRequest= new FileCopyToOtherServer();
         EndUserVisaInfo endUservisaInfo  = gson.fromJson(filter, EndUserVisaInfo.class);
		
		log.info("after casting request in to pojo classs"+endUservisaInfo);
		log.info("device db size--"+endUservisaInfo.getVisaDb().size());
		  for(int i =0; i<endUservisaInfo.getVisaDb().size();i++) {
		  //regularizeDeviceDbs.getRegularizeDeviceDbs().get(i).setTxnId(txnNumber);
		  endUservisaInfo.setTxnId(txnNumber);
		 // endUservisaInfo.getRegularizeDeviceDbs().get(i).setTxnId(txnNumber);
		  if(visaImage==null)
			{
				log.info("visa image is null..");	
				endUservisaInfo.getVisaDb().get(i).setVisaFileName("NA");
			}
		  else {
			  log.info("visa image is not null..");	
			  endUservisaInfo.getVisaDb().get(i).setVisaFileName((visaImage.getOriginalFilename()));  
		  }
		  
		  
		  log.info("file name to be set in varivable="+endUservisaInfo.getVisaDb().get(i).getVisaFileName());
		  
		  }
		 
		//endUservisaInfo.getVisaDb().get(1).setVisaFileName((visaImage.getOriginalFilename()));

		log.info(""+endUservisaInfo);
		log.info(" upload status  entry point.");
		if(visaImage==null)
		{
			log.info("visa image is null");	
		}
		else {
			try {
				
				byte[] bytes = visaImage.getBytes();
			String rootPath =urlToUpload.getValue()+txnNumber+"/"; 
			//give file read and writ  permission to txnId Directory
			File dir1 = new File(urlToUpload.getValue()+txnNumber+"/" + File.separator);
			File dir = new File(rootPath + File.separator);

			if (!dir.exists()) { 
				dir.mkdirs();
				dir.setExecutable(true,false);
				dir.setReadable(true,false);
				dir.setWritable(true,false);
				dir1.setExecutable(true,false);
				dir1.setReadable(true,false);
				dir1.setWritable(true,false);
			}
			// Create the file on server} 
			File serverFile = new File(rootPath+visaImage.getOriginalFilename());
			log.info("uploaded file path on server 1" + serverFile); BufferedOutputStream
			stream = new BufferedOutputStream(new FileOutputStream(serverFile));
			serverFile.setExecutable(true,false);
			serverFile.setReadable(true,false);
			serverFile.setWritable(true,false);
			stream.write(bytes); 
			stream.close();
			endUservisaInfo.setPassportFileName(passportImage.getOriginalFilename());
			
			} 

			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}	
		}

		if(visaImage==null)
		{
			log.info("visa image is null for previous file");	
		}
		else {
			try {
				
				byte[] bytes = visaImage.getBytes();
			String rootPath =urlToUpload.getValue()+existingTxnId+"/"; 
			File tmpDir = new File(rootPath+visaImage.getOriginalFilename());
			
			tmpDir.setExecutable(true,false);
			tmpDir.setReadable(true,false);
			tmpDir.setWritable(true,false);
			boolean exists = tmpDir.exists();
			if(exists) {

			Path temp = Files.move 
			(Paths.get(urlToUpload.getValue()+"/"+existingTxnId+"/"+visaImage.getOriginalFilename()), 
			Paths.get(urlToMove.getValue()+visaImage.getOriginalFilename())); 
			String movedPath=urlToMove.getValue()+visaImage.getOriginalFilename();	

			log.info("previous file is already exist, moved to this "+movedPath+" path. ");
			tmpDir.delete();
			}
			
			File dir = new File(rootPath + File.separator);

			if (!dir.exists()) { 
				dir.mkdirs();
				dir.setExecutable(true,false);
				dir.setReadable(true,false);
				dir.setWritable(true,false);
			}
			// Create the file on server }
			File serverFile = new File(rootPath+visaImage.getOriginalFilename());
			log.info("uploaded file path on server for previous txn id" + serverFile); BufferedOutputStream
			stream = new BufferedOutputStream(new FileOutputStream(serverFile));
			serverFile.setExecutable(true,false);
			serverFile.setReadable(true,false);
			serverFile.setWritable(true,false);
			stream.write(bytes); 
			stream.close();
			endUservisaInfo.setPassportFileName(passportImage.getOriginalFilename());
			
			} 

			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}	
		}
		
		/*
		 * try {
		 * 
		 * 
		 * String rootPath = urlToUpload.getValue()+existingTxnId+"/passport/"; File
		 * tmpDir = new File(rootPath+visaImage.getOriginalFilename()); boolean exists =
		 * tmpDir.exists(); if(exists) {
		 * 
		 * Path temp = Files.move
		 * (Paths.get(urlToUpload.getValue()+"/"+existingTxnId+"/passport/"+visaImage.
		 * getOriginalFilename()),
		 * Paths.get(urlToMove.getValue()+visaImage.getOriginalFilename())); String
		 * movedPath=urlToMove.getValue()+visaImage.getOriginalFilename();
		 * 
		 * log.info("previous file is already exist, moved to this "+movedPath+" path. "
		 * ); tmpDir.delete(); } byte[] bytes = visaImage.getBytes(); File dir = new
		 * File(rootPath + File.separator); if (!dir.exists()) dir.mkdirs(); File
		 * serverFile = new File(rootPath+visaImage.getOriginalFilename());
		 * log.info("uploaded visa  path on server 2" + serverFile);
		 * BufferedOutputStream stream = new BufferedOutputStream(new
		 * FileOutputStream(serverFile)); stream.write(bytes); stream.close();
		 * 
		 * } catch (Exception e) { // TODO: handle exception e.printStackTrace(); }
		 */	
		fileCopyRequest.setFilePath(urlToUpload.getValue()+txnNumber+"/");
	  	fileCopyRequest.setTxnId(txnNumber);
	  
	    if(visaImage==null)
		{
			log.info("visa image is null");	
			fileCopyRequest.setFileName("NA");
		}
	    else {
	    	fileCopyRequest.setFileName(visaImage.getOriginalFilename());
	    }
	  	fileCopyRequest.setServerId(propertyReader.serverId);
	  	log.info("request passed to move file to other server=="+fileCopyRequest);
	  	GenricResponse fileRespnose=grievanceFeignClient.saveUploadedFileOnANotherServer(fileCopyRequest);
	  	log.info("file move api response==="+fileRespnose);
		
		GenricResponse endUserVisaInfo= new GenricResponse();
		UserHeader header=registerService.getUserHeaders(request);
		endUservisaInfo.setPublicIp(header.getPublicIp());
		endUservisaInfo.setBrowser(header.getBrowser());
		log.info("Request send to the update emd user visa details ="+endUservisaInfo);
		endUserVisaInfo=	uploadPaidStatusFeignClient.updateEndUSerVisaDetailsby(endUservisaInfo);
		log.info("Response from fetchVisaDetailsbyPassport api== "+endUserVisaInfo);
		log.info("---exit  point in update visa validity page");
		endUserVisaInfo.setTxnId(txnNumber);
		return endUserVisaInfo;
	}
	
	
	
	
	@PostMapping("registerEndUserDevice")
	public @ResponseBody GenricResponse registerEndUserDevice(@RequestParam(name="visaImage",required = false) MultipartFile visaImage,@RequestParam(name="sourceType",required = false) String sourceType,
			@RequestParam(name="endUserDepartmentFile",required = false) MultipartFile endUserDepartmentFile,@RequestParam(name="uploadnationalID",required = false) MultipartFile uploadnationalID,
			HttpServletRequest request,HttpSession session,@RequestParam(name="docType",required = false) String docType) {
		log.info("---entry point in update visa validity page");
		log.info("---request---"+request.getParameter("request"));
		/*
		 * String userType=(String) session.getAttribute("usertype"); String
		 * userName=session.getAttribute("username").toString(); int userId= (int)
		 * session.getAttribute("userid"); int userTypeid=(int)
		 * session.getAttribute("usertypeId");
		 */
		String txnNumber="";
		if (sourceType.equalsIgnoreCase("custom"))
		{
			txnNumber="R" + utildownload.getTxnId();
		}
		else if(sourceType.equalsIgnoreCase("Immigration")) {
			txnNumber="I" + utildownload.getTxnId();
		}
		else {
			txnNumber="A" + utildownload.getTxnId();
		}
		  
		  log.info("Random transaction id number="+txnNumber);
		   String filter = request.getParameter("request");
		   Gson gson= new Gson();
		  log.info("before casting request in to pojo classs"+filter);
		  
		  addMoreFileModel.setTag("system_upload_filepath");
			urlToUpload=feignCleintImplementation.addMoreBuutonCount(addMoreFileModel);
			
			
		  EndUserVisaInfo endUservisaInfo = gson.fromJson(filter,  EndUserVisaInfo.class);
          FileCopyToOtherServer fileCopyRequest= new FileCopyToOtherServer();
		
		  if(endUservisaInfo.getNationality().equals(""))
		  {
			  log.info("nationality......");
			  endUservisaInfo.setNationality("Cambodian");
			  log.info("nationality......"+endUservisaInfo.getNationality());
		  }
		  if("N".equals(endUservisaInfo.getOnVisa())) {
			  endUservisaInfo.setVisaDb(null);
		  }
		  
		  if("N".equals(endUservisaInfo.getIsVip())) {
			  endUservisaInfo.setUserDepartment(null);
		  }
		  
		  endUservisaInfo.getOnVisa();
		  
		  log.info("after casting request in to pojo classs"+endUservisaInfo);

		  endUservisaInfo.setTxnId(txnNumber);
		  endUservisaInfo.setPassportFileName(uploadnationalID.getOriginalFilename());
			for(int i =0; i<endUservisaInfo.getRegularizeDeviceDbs().size();i++) {
				endUservisaInfo.getRegularizeDeviceDbs().get(i).setTxnId(txnNumber);
				if (sourceType.equalsIgnoreCase("enduser"))
				{
					endUservisaInfo.getRegularizeDeviceDbs().get(i).setCurrency("-1");
				}
				
			}
			if(uploadnationalID!=null) { 
			try {
				byte[] bytes = uploadnationalID.getBytes();
			String rootPath =urlToUpload.getValue()+txnNumber+"/"+docType+"/"; 
			File dir = new File(rootPath + File.separator);
			
			//give file read and writ  permission to txnId Directory
			File dir1 = new File(urlToUpload.getValue()+txnNumber+"/" + File.separator);

			if (!dir.exists()) {
				dir.mkdirs();
				dir.setExecutable(true,false);
				dir.setReadable(true,false);
				dir.setWritable(true,false);
				dir1.setExecutable(true,false);
				dir1.setReadable(true,false);
				dir1.setWritable(true,false);
			}
			// Create the file on server 
			File serverFile = new File(rootPath+uploadnationalID.getOriginalFilename());
			log.info("uploaded uploadnationalID file path on server" + serverFile); BufferedOutputStream
			stream = new BufferedOutputStream(new FileOutputStream(serverFile));
			serverFile.setExecutable(true,false);
			serverFile.setReadable(true,false);
			serverFile.setWritable(true,false);
			stream.write(bytes); 
			stream.close();
			fileCopyRequest.setFilePath(rootPath);
			fileCopyRequest.setTxnId(txnNumber);
			fileCopyRequest.setFileName(uploadnationalID.getOriginalFilename());
			fileCopyRequest.setServerId(propertyReader.serverId);
			log.info("request passed to move file to other server=="+fileCopyRequest);
			GenricResponse fileRespnose=grievanceFeignClient.saveUploadedFileOnANotherServer(fileCopyRequest);
			log.info("file move api response==="+fileRespnose);
			} 

			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			}
		  
		  log.info(""+endUservisaInfo); 
		  log.info(" upload status  entry point.");
		  if(endUserDepartmentFile!=null) { 
			  log.info("department  Image is not blank");
			  
		  try {
			  byte[] bytes = endUserDepartmentFile.getBytes(); 
			  String rootPath  =urlToUpload.getValue()+txnNumber+"/";
			  File dir = new File(rootPath + File.separator);
		  
		  if (!dir.exists()) {
			  dir.mkdirs(); 
				dir.setExecutable(true,false);
				dir.setReadable(true,false);
				dir.setWritable(true,false);
			  // Create the file on server 
		  }
		  File serverFile = new File(rootPath+endUserDepartmentFile.getOriginalFilename());
		  log.info("uploaded department  File  path on server" + serverFile); BufferedOutputStream
		  stream = new BufferedOutputStream(new FileOutputStream(serverFile));
			serverFile.setExecutable(true,false);
			serverFile.setReadable(true,false);
			serverFile.setWritable(true,false);
		  stream.write(bytes); stream.close();
		  
		  fileCopyRequest.setFilePath(rootPath);
			fileCopyRequest.setTxnId(txnNumber);
			fileCopyRequest.setFileName(endUserDepartmentFile.getOriginalFilename());
			fileCopyRequest.setServerId(propertyReader.serverId);
			log.info("request passed to move file to other server=="+fileCopyRequest);
			GenricResponse fileRespnose=grievanceFeignClient.saveUploadedFileOnANotherServer(fileCopyRequest);
			log.info("file move api response==="+fileRespnose);
		  }
		  
		  catch (Exception e) { // TODO: handle
			   e.printStackTrace(); } 
		  }
		  
		  
		
			  if(visaImage!=null) { 
				 log.info("visa Image is  not blank");
				 
			  try { byte[] bytes = visaImage.getBytes(); String rootPath
			  =urlToUpload.getValue()+txnNumber+"/"; File dir = new File(rootPath +
			  File.separator);
			  
			  
			  if (!dir.exists()) {
				  dir.mkdirs(); // Create the file on server
					dir.setExecutable(true,false);
					dir.setReadable(true,false);
					dir.setWritable(true,false);
			  }
			  
			  File serverFile = new File(rootPath+visaImage.getOriginalFilename());
			  log.info("uploaded  visa Image path on server" + serverFile); BufferedOutputStream
			  stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				serverFile.setExecutable(true,false);
				serverFile.setReadable(true,false);
				serverFile.setWritable(true,false);
			  stream.write(bytes); stream.close();
			  
			  fileCopyRequest.setFilePath(rootPath);
				fileCopyRequest.setTxnId(txnNumber);
				fileCopyRequest.setFileName(visaImage.getOriginalFilename());
				fileCopyRequest.setServerId(propertyReader.serverId);
				log.info("request passed to move file to other server=="+fileCopyRequest);
				GenricResponse fileRespnose=grievanceFeignClient.saveUploadedFileOnANotherServer(fileCopyRequest);
				log.info("file move api response==="+fileRespnose);
			  }
			  
			  catch (Exception ex) { // TODO: handle exception e.printStackTrace(); } }
			  }
			  }
				UserHeader header=registerService.getUserHeaders(request);
			  endUservisaInfo.setPublicIp(header.getPublicIp());
			  endUservisaInfo.setBrowser(header.getBrowser());
		  log.info("Request send to the update eNd user visa details ="+endUservisaInfo
		  ); 
		  GenricResponse endUserVisaInfo= new GenricResponse();
		  endUserVisaInfo=uploadPaidStatusFeignClient.RegisterEndUserDevice(endUservisaInfo);
		  log.info("Response from fetchVisaDetailsbyPassport api== "+endUserVisaInfo);
		 
		
		log.info("---exit  point in update visa validity page");
		return endUserVisaInfo;
	}







	
	  @PutMapping("updateEndUserDevice")
	  public @ResponseBody GenricResponse updateEndUserDevice(@RequestParam(name="visaImage",required = false)
	  MultipartFile visaImage,@RequestParam(name="endUserDepartmentFile",required =
	  false) MultipartFile endUserDepartmentFile,HttpServletRequest
	  request,HttpSession session) {
	  log.info("---entry point in update visa validity page");
	  log.info("---request---"+request.getParameter("request"));

	  String filter =request.getParameter("request");
	  Gson gson= new Gson();
	  log.info("before casting request in to pojo classs"+filter);
	  
	  EndUserVisaInfo endUservisaInfo = gson.fromJson(filter,EndUserVisaInfo.class);
	  GenricResponse response= new GenricResponse();
	  log.info("endUservisaInfo::::::::::"+endUservisaInfo);
	  
	  response=immigrationFeignImpl.RegisterEndUserDevice(endUservisaInfo);
	 return response; 
	  }
	  
	 
	  @PostMapping("endUseruploadPaidStatusForm")
		public @ResponseBody GenricResponse endUseruploadPaidStatusForm(HttpServletRequest request,HttpSession session) {
			log.info("-inside end user  controller  add -device-------request---------");
			
			String txnNumber="A" + utildownload.getTxnId();	
			log.info("Random transaction id number="+txnNumber);
			//request.setAttribute("txnId", txnNumber);
			//request.setAttribute("request[regularizeDeviceDbs][txnId]",txnNumber);
			String filter = request.getParameter("request");
			//log.info("txnid+++++++++++"+request.getParameter("request[regularizeDeviceDbs][txnId]"));
			Gson gson= new Gson(); 

			log.info("*********"+filter);
			addMoreFileModel.setTag("system_upload_filepath");
			urlToUpload=feignCleintImplementation.addMoreBuutonCount(addMoreFileModel);


			Register_UploadPaidStatus regularizeDeviceDbs  = gson.fromJson(filter, Register_UploadPaidStatus.class);
			regularizeDeviceDbs.setNationality("Cambodian");
			for(int i =0; i<regularizeDeviceDbs.getRegularizeDeviceDbs().size();i++) {
				regularizeDeviceDbs.getRegularizeDeviceDbs().get(i).setTxnId(txnNumber);
			}

			log.info(""+regularizeDeviceDbs.toString());
			log.info(" upload status  entry point.");
			UserHeader header=registerService.getUserHeaders(request);
			regularizeDeviceDbs.setPublicIp(header.getPublicIp());
			regularizeDeviceDbs.setBrowser(header.getBrowser());
			log.info("request passed to the save regularizeDeviceDbs api"+regularizeDeviceDbs);
			GenricResponse response = null;
			try {
				response = userPaidStatusFeignClient.uploadPaidUser(regularizeDeviceDbs);
				//GenricResponse response = null;
				log.info("---------response--------"+response);
			}
			catch (Exception e) {
				// TODO: handle exception
				log.info("exception in upload paid stat error"+e);
				e.printStackTrace();

			}
			return response;
		}


@PostMapping("EndUser_AddDevices")
public ModelAndView  endUserdeviceInformationView(@RequestParam(name="lang",required = false) String lang) {
	log.info("enter end user add device page. lang="+lang);
	ModelAndView modelAndView = new ModelAndView();
	modelAndView.setViewName("endUserAddDevice");
	
	
	return modelAndView;
}		
@PostMapping("viewDeviceInformation")
public ModelAndView viewDeviceInformation(@RequestParam(name="viewbyImei",required = true) String imei,@RequestParam(name="viewbytxnId",required = true) String viewbytxnId,
		@RequestParam(name="lang",required = false) String lang,HttpServletRequest requestHeader) {
	log.info(" imei in end user  =="+imei+"==lang=="+lang);
	ModelAndView modelAndView = new ModelAndView("viewAdddeviceInformation");
	
	  AllRequest request= new AllRequest();
	  request.setFeatureId(12);

	  request.setImei(imei);
	  request.setTxnId(viewbytxnId);
      request.setUserTypeId(17);
	  request.setUserType("End User");
		UserHeader header=registerService.getUserHeaders(requestHeader);
		request.setPublicIp(header.getPublicIp());
		request.setBrowser(header.getBrowser());
	  log.info(" request=="+request);
	UserPaidStatusContent content= uploadPaidStatusFeignClient.viewByImei(request);
	log.info(" response =="+content);

	addMoreFileModel.setTag("upload_file_link");
    urlToUpload=feignCleintImplementation.addMoreBuutonCount(addMoreFileModel);
    log.info("file link =="+urlToUpload.getValue());
   // content.setFilePreviewLink(urlToUpload.getValue());
	String fileLink=urlToUpload.getValue();
	modelAndView.addObject("fileLink", fileLink);
    modelAndView.addObject("viewInformation", content);
    modelAndView.setViewName("endUserViewDeviceInformation");
    
	return modelAndView;
}

@PostMapping("approveVisaUpdateRequest") 
public @ResponseBody GenricResponse approveVisaUpdateRequest (@RequestBody FilterRequest_UserPaidStatus filterRequestuserpaidStatus,HttpSession session)  {
	
	
	String roleType=String.valueOf(session.getAttribute("usertype"));
	String userName=session.getAttribute("username").toString();
	int userId= (int) session.getAttribute("userid");  
	int userTypeId =(int) session.getAttribute("usertypeId");
		/*
		 * request.setFeatureId(43); request.setUserType(roleType);
		 * request.setUserId(userId); request.setUserTypeId(userTypeId);
		 * request.setUsername(userName);
		 */
	filterRequestuserpaidStatus.setUserType(roleType);
	filterRequestuserpaidStatus.setUsername(userName);
	filterRequestuserpaidStatus.setUserId(userId);
	filterRequestuserpaidStatus.setUserTypeId(userTypeId);
	filterRequestuserpaidStatus.setFeatureId(43);
	filterRequestuserpaidStatus.setPublicIp(session.getAttribute("publicIP").toString());
	filterRequestuserpaidStatus.setBrowser(session.getAttribute("browser").toString());
	log.info("request send to the approveReject visa  api="+filterRequestuserpaidStatus);
	GenricResponse response= uploadPaidStatusFeignClient.updateVisaRequest(filterRequestuserpaidStatus);
	log.info("response from approveReject visa "+response);
	return response;

	}
@GetMapping("view-visa-information/{visaId}/{endUserId}")
public ModelAndView viewVisaInformationView(@PathVariable("visaId") Integer visaId,@PathVariable("endUserId") Integer endUserId,
		HttpSession session,@RequestParam(name="source", required = false) String source,@RequestParam(name="txnId", required = false) String txnId) {
	
	ModelAndView modelAndView = new ModelAndView();
	FilterRequest filter= new FilterRequest();
	Integer userId= (int) session.getAttribute("userid");
	
	String userType=(String) session.getAttribute("usertype");
	Integer userTypeId=(int) session.getAttribute("usertypeId");
	String userName=session.getAttribute("username").toString();
	filter.setId(endUserId);
	filter.setUserType(userType);
	filter.setUserTypeId(userTypeId);
	filter.setUserId(userId);
	filter.setFeatureId(43);
	filter.setUsername(userName);
	filter.setUserName(userName);
	log.info("source type=="+source);
	filter.setPublicIp(session.getAttribute("publicIP").toString());
	filter.setBrowser(session.getAttribute("browser").toString());
	log.info("request passed to the view visa details .."+filter);
	UpdateVisaModel content= uploadPaidStatusFeignClient.viewVisaDetails(filter);
	log.info(" reponse from view visa details api. =="+content);

	addMoreFileModel.setTag("upload_file_link");
    urlToUpload=feignCleintImplementation.addMoreBuutonCount(addMoreFileModel);
    log.info("file link =="+urlToUpload.getValue());
   // content.setFilePreviewLink(urlToUpload.getValue());
	String fileLink=urlToUpload.getValue();
	modelAndView.addObject("fileLink", fileLink);
    modelAndView.addObject("viewInformation", content);
    modelAndView.addObject("txnId",txnId);
    modelAndView.setViewName("viewVisaInformation");	
    
    	
    
	return modelAndView;
}
}


