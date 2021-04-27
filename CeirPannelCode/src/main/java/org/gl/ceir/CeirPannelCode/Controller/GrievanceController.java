package org.gl.ceir.CeirPannelCode.Controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.gl.ceir.CeirPannelCode.PropertyReader;
import org.gl.ceir.CeirPannelCode.Feignclient.FeignCleintImplementation;
import org.gl.ceir.CeirPannelCode.Feignclient.GrievanceFeignClient;
import org.gl.ceir.CeirPannelCode.Feignclient.UserRegistrationFeignImpl;
import org.gl.ceir.CeirPannelCode.Model.AddMoreFileModel;
import org.gl.ceir.CeirPannelCode.Model.FileCopyToOtherServer;
import org.gl.ceir.CeirPannelCode.Model.FileExportResponse;
import org.gl.ceir.CeirPannelCode.Model.FilterRequest;
import org.gl.ceir.CeirPannelCode.Model.GenricResponse;
import org.gl.ceir.CeirPannelCode.Model.GrievanceModel;
import org.gl.ceir.CeirPannelCode.Model.PeriodValidate;
import org.gl.ceir.CeirPannelCode.Model.UserHeader;
import org.gl.ceir.CeirPannelCode.Service.RegistrationService;
import org.gl.ceir.CeirPannelCode.Util.HttpResponse;
import org.gl.ceir.CeirPannelCode.Util.UtilDownload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

@Controller
public class GrievanceController {

	private final Logger log = LoggerFactory.getLogger(getClass());



	@Autowired
	FeignCleintImplementation feignCleintImplementation;
	@Autowired
	UtilDownload utildownload;
	@Autowired
	GrievanceFeignClient grievanceFeignClient;
	@Autowired
	AddMoreFileModel addMoreFileModel,urlToUpload;

	@Autowired
	PropertyReader propertyReader;

	@Autowired
	UserRegistrationFeignImpl userRegistrationFeignImpl;
	
	@Autowired
	RegistrationService registerService;


	GrievanceModel grievance= new GrievanceModel();
	GenricResponse response = new GenricResponse();

	@RequestMapping(value=
		{"grievanceManagement"},method={org.springframework.web.bind.annotation.RequestMethod.GET,org.springframework.web.bind.annotation.RequestMethod.POST}
			)
	public ModelAndView viewGrievance(HttpSession session,@RequestParam(name="txnID",required = false) String txnID,
			@RequestParam(name="source",defaultValue = "menu",required = false) String source) {
		ModelAndView mv = new ModelAndView();
		log.info(" view Grievance entry point."); 
		String userName= (String) session.getAttribute("username");
		log.info("username In Grievance------->"+userName);

		if(session.getAttribute("")!=null)
		{
			log.info(" user type is not blank"); 
			mv.addObject("userName", userName);
			mv.setViewName("grievanceManagement");
			log.info(" view Grievance exit point."); 
		}
		else {
			log.info(" user type isblank");
			mv.setViewName("grievanceManagement");
			mv.addObject("userName", userName);
			log.info(" view Grievance exit point."); 
		}

		return mv; 
	}


	@RequestMapping(value= {"/saveGrievance"},method= RequestMethod.POST,consumes = "multipart/form-data") 
	public @ResponseBody GenricResponse saveGrievance(@RequestParam(name="files[]") MultipartFile[] fileUpload,HttpServletRequest request,HttpSession session) {


		int userId= (int) session.getAttribute("userid");
		String roletype=(String) session.getAttribute("usertype");

		String grevnceId=utildownload.getTxnId();
		grevnceId = "G"+grevnceId;
		Gson gson= new Gson(); 
		String grievanceDetails=request.getParameter("multirequest");
		log.info("grievanceDetails------"+grievanceDetails);

		addMoreFileModel.setTag("system_upload_filepath");
		urlToUpload=feignCleintImplementation.addMoreBuutonCount(addMoreFileModel);

		GrievanceModel grievanceRequest  = gson.fromJson(grievanceDetails, GrievanceModel.class);
		FileCopyToOtherServer fileCopyRequest= new FileCopyToOtherServer();
		//grievanceRequest.setUserId(userId);
		//grievanceRequest.setUserType(roletype);
		grievanceRequest.setGrievanceId(grevnceId);
		
		

		for (int i=0;i<grievanceRequest.getAttachedFiles().size();i++) {
			grievanceRequest.getAttachedFiles().get(i).setGrievanceId(grevnceId);
			//grievanceRequest.getMultifile().get(i).getDocType();
		}

		log.info("Random  genrated transaction number ="+grevnceId);
		int i=0;
		for( MultipartFile file : fileUpload) {

			log.info("-----"+ file.getOriginalFilename());
			log.info("++++"+ file);

			String tagName=grievanceRequest.getAttachedFiles().get(i).getDocType();
			log.info("doctype Name==="+tagName+"value of index="+i);


			try {

				byte[] bytes =
						file.getBytes(); String rootPath = urlToUpload.getValue()+grevnceId+"/"+tagName+"/"; 
						File dir =   new File(rootPath + File.separator);
						File dir1 =   new File(urlToUpload.getValue()+grevnceId+"/" + File.separator);
						if (!dir.exists())
							{
							dir.mkdirs(); // Create the file on server // Calendar now = Calendar.getInstance();
							dir.setReadable(true,false);
							dir.setWritable(true,false);
							dir.setExecutable(true,false);
							dir1.setReadable(true,false);
							dir1.setWritable(true,false);
							dir1.setExecutable(true,false);
							}
						File serverFile = new File(rootPath+file.getOriginalFilename());
						log.info("uploaded file path on server" + serverFile); BufferedOutputStream
						stream = new BufferedOutputStream(new FileOutputStream(serverFile));
						serverFile.setExecutable(true,false);
						serverFile.setReadable(true,false);
						serverFile.setWritable(true,false);
						stream.write(bytes); stream.close();

						fileCopyRequest.setFilePath(rootPath);
						fileCopyRequest.setTxnId(grevnceId);
						fileCopyRequest.setFileName(file.getOriginalFilename());
						fileCopyRequest.setServerId(propertyReader.serverId);
						log.info("request passed to move file to other server=="+fileCopyRequest);
						GenricResponse fileRespnose=grievanceFeignClient.saveUploadedFileOnANotherServer(fileCopyRequest);
						log.info("file move api response==="+fileRespnose);

						//  grievanceRequest.setFileName(file.getOriginalFilename());

			}
			catch (Exception e) { //
				// TODO: handle exception e.printStackTrace(); }

				// set reaquest parameters into model class

			}
			i++;


		}
		/*
		 * grievance.setCategoryId(categoryId); grievance.setRemarks(remarks);
		 * grievance.setTxnId(txnId);
		 */
		/*
		 * grievance.setUserId(userId); grievance.setUserType(roletype);
		 * grievance.setGrievanceId(grevnceId);
		 * 
		 */
		UserHeader header=registerService.getUserHeaders(request);
		grievanceRequest.setPublicIp(header.getPublicIp());
		grievanceRequest.setBrowser(header.getBrowser());
		log.info("grievance form parameters passed to save grievance api "+grievanceRequest);
		response = grievanceFeignClient.saveGrievance(grievanceRequest);
		response.setTxnId(grevnceId);

		log.info("response from save grievance api"+response);
		log.info("save grievance  exit point.");
		return response;
	}
	//***************************************** open save greivance *********************************
	@RequestMapping(value="/openGrievanceForm",method ={org.springframework.web.bind.annotation.RequestMethod.GET})
	public ModelAndView openRegisterConsignmentForm(@RequestParam(name="reqType") String reqType)
	{
		ModelAndView mv= new ModelAndView();
		if(reqType.equals("formPage"))
		{
			log.info("open save  Grievance form");
			mv.setViewName("saveGrievance");
		}

		return mv;
	}


	//***************************************** view Grievance controller *********************************
	@RequestMapping(value="/viewGrievance",method ={org.springframework.web.bind.annotation.RequestMethod.GET})
	public @ResponseBody List<GrievanceModel> viewGrievance(@RequestParam(name="grievanceId") String grievanceId,HttpSession session ,
															@RequestParam(name="userType",required = false ) String userType,
															@RequestParam(name="featureId",required = false ) Integer featureId,
															@RequestParam(name="recordLimit") Integer recordLimit,HttpServletRequest request )
	{
		log.info("entery point in view grievance.");
		int userId= (int) session.getAttribute("userid");
		List<GrievanceModel>  grievanceModel=new ArrayList<GrievanceModel> ();
		UserHeader header=registerService.getUserHeaders(request);
		String publicIp = header.getPublicIp();
		String browser = header.getBrowser();
		log.info("request passed to the save grievance method="+grievanceModel);
		log.info("Request pass to the view grievance api ="+grievanceId+"  userId= "+userId);
		grievanceModel=grievanceFeignClient.viewGrievance(grievanceId, userId,recordLimit,publicIp,browser,userType,featureId);
		log.info("Response from  view grievance api = "+grievanceModel);
		return grievanceModel;
	}

	//***************************************** end  view Grievance controller *********************************
	@RequestMapping(value="/endUserViewGrievance",method ={org.springframework.web.bind.annotation.RequestMethod.GET})
	public @ResponseBody List<GrievanceModel> EndUserviewGrievance(@RequestParam(name="grievanceId") String grievanceId,
																   @RequestParam(name="userType",required = false ) String userType,
																   @RequestParam(name="featureId",required = false ) Integer featureId,
																   HttpSession session ,
																   @RequestParam(name="recordLimit") Integer recordLimit,
																   @RequestParam(name="userId") Integer userId,HttpServletRequest request )
	{
		log.info("entery point in end user view grievance.");
		/* int userId= (int) session.getAttribute("userid"); */
		List<GrievanceModel>  grievanceModel=new ArrayList<GrievanceModel> ();
		UserHeader header=registerService.getUserHeaders(request);
		String publicIp = header.getPublicIp();
		String browser = header.getBrowser();
		log.info("Request pass to the end view grievance api ="+grievanceId+"  userId= "+userId);
		grievanceModel=grievanceFeignClient.viewGrievance(grievanceId, userId,recordLimit,publicIp,browser,userType,featureId);
		log.info("Response from  end view grievance api = "+grievanceModel);
		return grievanceModel;
	}



	//***************************************** view Grievance controller *********************************
	@RequestMapping(value="/saveGrievanceMessage",method ={org.springframework.web.bind.annotation.RequestMethod.POST})
	public @ResponseBody GenricResponse saveGrievanceReply(@RequestParam(name="files[]") MultipartFile[] fileUpload,HttpServletRequest request,HttpSession session)
	{


		//	log.info("grievanceId=="+grievanceId+ " remark ="+remark+" txnId="+txnId+" file name=="+file.getOriginalFilename());

		//int userId= (int) session.getAttribute("userid"); 
		/*
		 * String roletype=(String) session.getAttribute("usertype");
		 * log.info("+ roletype="+roletype);
		 */ 	
		FileCopyToOtherServer fileCopyRequest= new FileCopyToOtherServer();
		addMoreFileModel.setTag("system_upload_filepath");
		urlToUpload=feignCleintImplementation.addMoreBuutonCount(addMoreFileModel);
		String grievanceDetails=request.getParameter("multirequest");
		log.info("grievanceDetails------"+grievanceDetails);
		Gson gson= new Gson(); 	
		GrievanceModel grievanceRequest  = gson.fromJson(grievanceDetails, GrievanceModel.class);
		//grievanceRequest.setUserId(userId);
		//grievanceRequest.setUserType(roletype);
		int i=0;
		for( MultipartFile file : fileUpload) {
			String tagName=grievanceRequest.getAttachedFiles().get(i).getDocType();
			try {
				if(fileUpload==null)
				{
					grievanceRequest.getAttachedFiles().get(i).setFileName("");
				}
				else {

					byte[] bytes = file.getBytes();
					String rootPath = urlToUpload.getValue()+grievanceRequest.getGrievanceId()+"/"+tagName+"/";
					File dir = new File(rootPath + File.separator);
					//give file read and writ  permission to txnId Directory
					File dir1 = new File(urlToUpload.getValue()+grievanceRequest.getGrievanceId()+"/" + File.separator);
					if (!dir.exists()) {
						dir.mkdirs();
						dir.setReadable(true,false);
						dir.setWritable(true,false);
						dir.setExecutable(true,false);
						dir1.setReadable(true,false);
						dir1.setWritable(true,false);
						dir1.setExecutable(true,false);
					}
					// Create the file on server
					// Calendar now = Calendar.getInstance();

					File serverFile = new File(rootPath+file.getOriginalFilename());
					log.info("uploaded file path on server" + serverFile);
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
					serverFile.setExecutable(true,false);
					serverFile.setReadable(true,false);
					serverFile.setWritable(true,false);
					stream.write(bytes);
					stream.close();

					fileCopyRequest.setFilePath(rootPath);
					fileCopyRequest.setTxnId(grievanceRequest.getGrievanceId());
					fileCopyRequest.setFileName(file.getOriginalFilename());
					fileCopyRequest.setServerId(propertyReader.serverId);
					log.info("request passed to move file to other server=="+fileCopyRequest);
					GenricResponse fileRespnose=grievanceFeignClient.saveUploadedFileOnANotherServer(fileCopyRequest);
					log.info("file move api response==="+fileRespnose);
				}


			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			i++;
		}
		/*
		 * grievanceModel.setTxnId(txnId); grievanceModel.setReply(remark);
		 * grievanceModel.setGrievanceId(grievanceId); grievanceModel.setUserId(userId);
		 * grievanceModel.setUserType(roletype);
		 * grievanceModel.setGrievanceStatus(grievanceTicketStatus);
		 */
		UserHeader header=registerService.getUserHeaders(request);
		grievanceRequest.setPublicIp(header.getPublicIp());
		grievanceRequest.setBrowser(header.getBrowser());
		log.info("request passed to the save grievance method="+grievanceRequest);
		response= grievanceFeignClient.saveGrievanceMessage(grievanceRequest);
		log.info("response  from   save grievance method="+response);	
		response.setTxnId(grievanceRequest.getGrievanceId());
		return response;
	}



	//***************************************** view Grievance controller *********************************
	@RequestMapping(value="/saveEndUserGrievanceReply",method ={org.springframework.web.bind.annotation.RequestMethod.POST})
	public @ResponseBody GenricResponse saveEndUserGrievanceReply(@RequestParam(name="files[]") MultipartFile[] fileUpload,HttpServletRequest request,HttpSession session)
	{


		//	log.info("grievanceId=="+grievanceId+ " remark ="+remark+" txnId="+txnId+" file name=="+file.getOriginalFilename());
		//int userId= (int) session.getAttribute("userid"); 
		String roletype="End User";
		log.info("+ roletype="+roletype);
		String grievanceDetails=request.getParameter("multirequest");
		log.info("grievanceDetails------"+grievanceDetails);
		Gson gson= new Gson(); 	
		GrievanceModel grievanceRequest  = gson.fromJson(grievanceDetails, GrievanceModel.class);
		//grievanceRequest.setUserId(userId);
		FileCopyToOtherServer fileCopyRequest= new FileCopyToOtherServer();
		addMoreFileModel.setTag("system_upload_filepath");
		urlToUpload=feignCleintImplementation.addMoreBuutonCount(addMoreFileModel);
		grievanceRequest.setUserType(roletype);
		int i=0;
		for( MultipartFile file : fileUpload) {
			String tagName=grievanceRequest.getAttachedFiles().get(i).getDocType();
			try {
				if(fileUpload==null)
				{
					grievanceRequest.getAttachedFiles().get(i).setFileName("");
				}
				else {



					byte[] bytes = file.getBytes();
					String rootPath = urlToUpload.getValue()+grievanceRequest.getGrievanceId()+"/"+tagName+"/";
					File dir = new File(rootPath + File.separator);
					//give file read and writ  permission to txnId Directory
					File dir1 = new File(urlToUpload.getValue()+grievanceRequest.getGrievanceId()+"/" + File.separator);	
					if (!dir.exists()) {
						dir.mkdirs();
						dir.setReadable(true,false);
						dir.setWritable(true,false);
						dir.setExecutable(true,false);
						dir1.setReadable(true,false);
						dir1.setWritable(true,false);
						dir1.setExecutable(true,false);
					// Create the file on server
					// Calendar now = Calendar.getInstance();
					}
					File serverFile = new File(rootPath+file.getOriginalFilename());
					log.info("uploaded file path on server" + serverFile);
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
					serverFile.setExecutable(true,false);
					serverFile.setReadable(true,false);
					serverFile.setWritable(true,false);
					stream.write(bytes);
					stream.close();

					fileCopyRequest.setFilePath(rootPath);
					fileCopyRequest.setTxnId(grievanceRequest.getGrievanceId());
					fileCopyRequest.setFileName(file.getOriginalFilename());
					fileCopyRequest.setServerId(propertyReader.serverId);
					log.info("request passed to move file to other server=="+fileCopyRequest);
					GenricResponse fileRespnose=grievanceFeignClient.saveUploadedFileOnANotherServer(fileCopyRequest);
					log.info("file move api response==="+fileRespnose);
				}


			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			i++;
		}
		/*
		 * grievanceModel.setTxnId(txnId); grievanceModel.setReply(remark);
		 * grievanceModel.setGrievanceId(grievanceId); grievanceModel.setUserId(userId);
		 * grievanceModel.setUserType(roletype);
		 * grievanceModel.setGrievanceStatus(grievanceTicketStatus);
		 */
		UserHeader header=registerService.getUserHeaders(request);
		grievanceRequest.setPublicIp(header.getPublicIp());
		grievanceRequest.setBrowser(header.getBrowser());
		log.info("request passed to the save grievance method="+grievanceRequest);
		response= grievanceFeignClient.saveGrievanceMessage(grievanceRequest);
		log.info("response  from   save grievance method="+response);	
		response.setTxnId(grievanceRequest.getGrievanceId());
		return response;
	}


	@RequestMapping(value={"/raiseAgrievance"},method={org.springframework.web.bind.annotation.RequestMethod.GET,org.springframework.web.bind.annotation.RequestMethod.POST})
	public  ModelAndView openEndUserGrievancePage(@RequestParam(name="reportType") Integer reportType) 
	{
		HttpResponse httpResponse=userRegistrationFeignImpl.periodValidate(new PeriodValidate(17L, 6L));
		ModelAndView mv = new ModelAndView();
		log.info(" view End user Grievance entry point."+reportType);
		if(httpResponse.getStatusCode() == 200) {
			mv.addObject("reportType", reportType);
			mv.setViewName("openEndUserGrievancePage");
			log.info(" view End user Grievance exit point."); 
		}
		else {
			mv.setViewName("registrationPopup");
		}
		return mv; 
	}


	@RequestMapping(value= {"/saveEndUserGrievance"},method= RequestMethod.POST,consumes = "multipart/form-data") 
	public @ResponseBody GenricResponse saveEndUserGrievance(@RequestParam(name="files[]") MultipartFile[] fileUpload,HttpServletRequest request,HttpSession session) {


		//int userId= (int) session.getAttribute("userid");
		//	String roletype=(String) session.getAttribute("usertype");

		String grevnceId=utildownload.getTxnId();
		grevnceId = "G"+grevnceId;
		Gson gson= new Gson(); 
		String grievanceDetails=request.getParameter("multirequest");
		log.info("grievanceDetails------"+grievanceDetails);
		addMoreFileModel.setTag("system_upload_filepath");
		urlToUpload=feignCleintImplementation.addMoreBuutonCount(addMoreFileModel);
		GrievanceModel grievanceRequest  = gson.fromJson(grievanceDetails, GrievanceModel.class);
		FileCopyToOtherServer fileCopyRequest= new FileCopyToOtherServer();
		//grievanceRequest.setUserId(userId);
		grievanceRequest.setUserType("End User");
		grievanceRequest.setGrievanceId(grevnceId);

		for (int i=0;i<grievanceRequest.getAttachedFiles().size();i++) {
			grievanceRequest.getAttachedFiles().get(i).setGrievanceId(grevnceId);
			//grievanceRequest.getMultifile().get(i).getDocType();
		}

		log.info("Random  genrated transaction number ="+grevnceId);
		int i=0;
		for( MultipartFile file : fileUpload) {

			log.info("-----"+ file.getOriginalFilename());
			log.info("++++"+ file);

			String tagName=grievanceRequest.getAttachedFiles().get(i).getDocType();
			log.info("doctype Name==="+tagName+"value of index="+i);


			try {

				byte[] bytes =
						file.getBytes(); String rootPath = urlToUpload.getValue()+grevnceId+"/"+tagName+"/"; 
						File dir =   new File(rootPath + File.separator);
						//give file read and writ  permission to txnId Directory
						File dir1 = new File(urlToUpload.getValue()+grevnceId+"/" + File.separator);
						if (!dir.exists()) { dir.mkdirs(); // Create the file on server // Calendar now = Calendar.getInstance();
						
						dir.setReadable(true,false);
						dir.setWritable(true,false);
						dir.setExecutable(true,false);
						dir1.setReadable(true,false);
						dir1.setWritable(true,false);
						dir1.setExecutable(true,false);
						}
						File serverFile = new File(rootPath+file.getOriginalFilename());
						log.info("uploaded file path on server" + serverFile); BufferedOutputStream
						stream = new BufferedOutputStream(new FileOutputStream(serverFile));
						serverFile.setExecutable(true,false);
						serverFile.setReadable(true,false);
						serverFile.setWritable(true,false);
						stream.write(bytes); stream.close(); 

						fileCopyRequest.setFilePath(rootPath);
						fileCopyRequest.setTxnId(grevnceId);
						fileCopyRequest.setFileName(file.getOriginalFilename());
						fileCopyRequest.setServerId(propertyReader.serverId);
						log.info("request passed to move file to other server=="+fileCopyRequest);
						GenricResponse fileRespnose=grievanceFeignClient.saveUploadedFileOnANotherServer(fileCopyRequest);
						log.info("file move api response==="+fileRespnose);
						//  grievanceRequest.setFileName(file.getOriginalFilename());

			}
			catch (Exception e) { //
				// TODO: handle exception e.printStackTrace(); }

				// set reaquest parameters into model class

			}
			i++;


		}
		/*
		 * grievance.setCategoryId(categoryId); grievance.setRemarks(remarks);
		 * grievance.setTxnId(txnId);
		 */
		/*
		 * grievance.setUserId(userId); grievance.setUserType(roletype);
		 * grievance.setGrievanceId(grevnceId);
		 * 
		 */
		UserHeader header=registerService.getUserHeaders(request);
		grievanceRequest.setPublicIp(header.getPublicIp());
		grievanceRequest.setBrowser(header.getBrowser());
		log.info("grievance form parameters passed to save grievance api "+grievanceRequest);
		response = grievanceFeignClient.saveEndUserGrievance(grievanceRequest);
		response.setTxnId(grevnceId);

		log.info("response from save grievance api"+response);
		log.info("save grievance  exit point.");
		return response;
	}


	//***************************************** open Search UserName *********************************
	@RequestMapping(value="/searchUserNameForm",method ={org.springframework.web.bind.annotation.RequestMethod.GET})
	public ModelAndView openSearchUsernameForm()
	{
		ModelAndView mv= new ModelAndView();
		log.info("open Search userName Grievance form");
		mv.setViewName("searchUserName");
		return mv;
	}

	//***************************************** open Customer Care Grievance Form *********************************

	@RequestMapping(value={"/raiseCCgrievance"},method={org.springframework.web.bind.annotation.RequestMethod.GET,org.springframework.web.bind.annotation.RequestMethod.POST})
	public  ModelAndView openCCGrievancePage() 
	{
		ModelAndView mv = new ModelAndView();
		log.info(" view CC user Grievance entry point.");
		mv.setViewName("CCRaiseGrievance");
		log.info(" view CC user Grievance exit point."); 
		return mv; 
	}

	/*
	 * //***************************************** Export Grievance controller
	 * *********************************
	 * 
	 * @RequestMapping(value="/exportGrievance",method
	 * ={org.springframework.web.bind.annotation.RequestMethod.GET}) public String
	 * exportToExcel(@RequestParam(name="grievanceStartDate",required = false)
	 * String grievanceStartDate,@RequestParam(name="grievanceEndDate",required =
	 * false) String grievanceEndDate,
	 * 
	 * @RequestParam(name="grievancetxnId",required = false) String
	 * grievancetxnId,@RequestParam(name="grievanceId") String
	 * grievanceId,HttpServletRequest request, HttpSession
	 * session,@RequestParam(name="pageSize") Integer
	 * pageSize,@RequestParam(name="pageNo") Integer
	 * pageNo,@RequestParam(name="grievanceStatus") Integer grievanceStatus,
	 * 
	 * @RequestParam(name="source",defaultValue = "menu",required = false) String
	 * source,
	 * 
	 * @RequestParam(name="userId",required = false) Integer userId,
	 * 
	 * @RequestParam(name="filterUserName",required = false) String filterUserName,
	 * 
	 * @RequestParam(name="FilterUserType",required = false) String FilterUserType)
	 * { log.info("grievanceStartDate=="+grievanceStartDate+
	 * " grievanceEndDate ="+grievanceEndDate+" grievancetxnId="+grievancetxnId+
	 * "grievanceId="+grievanceId); log.info("source--->"
	 * +source+" filterUserName--->"+filterUserName+"  FilterUserType--->"
	 * +FilterUserType); //int userId= (int) session.getAttribute("userid"); int
	 * file=1; String userType=(String) session.getAttribute("usertype"); Integer
	 * usertypeId=(int) session.getAttribute("usertypeId"); FileExportResponse
	 * fileExportResponse; FilterRequest filterRequest= new FilterRequest();
	 * filterRequest.setStartDate(grievanceStartDate);
	 * filterRequest.setEndDate(grievanceEndDate);
	 * filterRequest.setTxnId(grievancetxnId);
	 * filterRequest.setGrievanceStatus(grievanceStatus);
	 * filterRequest.setGrievanceId(grievanceId); filterRequest.setUserId(userId);
	 * filterRequest.setUserType(userType); filterRequest.setUserTypeId(usertypeId);
	 * filterRequest.setFeatureId(6);
	 * filterRequest.setFilterUserName(filterUserName);
	 * filterRequest.setFilterUserType(FilterUserType);
	 * 
	 * 
	 * log.info(" request passed to the exportTo Excel Api =="
	 * +filterRequest+" *********** pageSize"+pageSize+"  pageNo  "+pageNo); Object
	 * response=
	 * grievanceFeignClient.grievanceFilter(filterRequest,pageNo,pageSize,file,
	 * source);
	 * 
	 * Gson gson= new Gson(); String apiResponse = gson.toJson(response);
	 * fileExportResponse = gson.fromJson(apiResponse, FileExportResponse.class);
	 * log.info("response  from   export grievance  api="+fileExportResponse);
	 * 
	 * return "redirect:"+fileExportResponse.getUrl(); }
	 */

	//***************************************** Export Grievance controller *********************************
	@PostMapping("exportGrievance")
	@ResponseBody
	public FileExportResponse exportToExcel(@RequestBody FilterRequest filterRequest,HttpSession session,HttpServletRequest request)
	{
		Gson gsonObject=new Gson();
		Object response;
		Integer file = 1;	
		String userType=(String) session.getAttribute("usertype");
		Integer usertypeId=(int) session.getAttribute("usertypeId");
		filterRequest.setUserType(userType);
		filterRequest.setUserTypeId(usertypeId);
		UserHeader header=registerService.getUserHeaders(request);
		filterRequest.setPublicIp(header.getPublicIp());
		filterRequest.setBrowser(header.getBrowser());
		log.info("filterRequest:::::::::"+filterRequest);
		response= grievanceFeignClient.grievanceFilter(filterRequest, filterRequest.getPageNo(), filterRequest.getPageSize(), file,filterRequest.getSource());
		FileExportResponse fileExportResponse;
		Gson gson= new Gson(); 
		String apiResponse = gson.toJson(response);
		fileExportResponse = gson.fromJson(apiResponse, FileExportResponse.class);
		log.info("response  from  Alert Export  api="+fileExportResponse);

		return fileExportResponse;
	}		

}

