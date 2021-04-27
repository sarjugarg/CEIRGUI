package org.gl.ceir.CeirPannelCode.Controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.gl.ceir.CeirPannelCode.PropertyReader;
import org.gl.ceir.CeirPannelCode.Feignclient.FeignCleintImplementation;
import org.gl.ceir.CeirPannelCode.Feignclient.GrievanceFeignClient;
import org.gl.ceir.CeirPannelCode.Feignclient.TypeApprovedFeignImpl;
import org.gl.ceir.CeirPannelCode.Model.AddMoreFileModel;
import org.gl.ceir.CeirPannelCode.Model.FileCopyToOtherServer;
import org.gl.ceir.CeirPannelCode.Model.FileExportResponse;
import org.gl.ceir.CeirPannelCode.Model.FilterRequest;
import org.gl.ceir.CeirPannelCode.Model.GenricResponse;
import org.gl.ceir.CeirPannelCode.Model.GrievanceModel;
import org.gl.ceir.CeirPannelCode.Model.TRCRegisteration;
import org.gl.ceir.CeirPannelCode.Model.TRCRequest;
import org.gl.ceir.CeirPannelCode.Model.TypeApprovedStatusModel;
import org.gl.ceir.CeirPannelCode.Util.UtilDownload;
import org.gl.ceir.interfaceImpl.RegisterationImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import CeirPannelCode.Model.Register_UploadPaidStatus;



@Controller
public class TrcController {
	private final Logger log = LoggerFactory.getLogger(getClass());

	
	
	
	@Autowired
	TypeApprovedFeignImpl typeApprovedFeignImpl;
	@Autowired
	RegisterationImpl registerationImpl;
	@Autowired
	UtilDownload utildownload;
	
	@Autowired
	AddMoreFileModel addMoreFileModel,urlToUpload;
	@Autowired
	FeignCleintImplementation feignCleintImplementation;

	@Autowired
    PropertyReader propertyReader;
	@Autowired
	GrievanceFeignClient grievanceFeignClient;
	
	@RequestMapping(value=
		{"/manageTypeDevices"},method={org.springframework.web.bind.annotation.
				RequestMethod.GET,org.springframework.web.bind.annotation.RequestMethod.POST}
			)
	public ModelAndView viewManageType(HttpSession session,@RequestParam(name="txnID",required = false) String txnID) {
		ModelAndView mv = new ModelAndView();
		log.info(" view TRC entry point."); 
		mv.setViewName("viewManageTypeApproved");
		log.info(" view TRC  exit point."); 
		return mv; 
	}


	@GetMapping("register-form")
	public ModelAndView regiserForm() {
		ModelAndView modelAndView = new ModelAndView("trcReportTypeApprovedDevice");
		return modelAndView;

	}
	
	@RequestMapping(value= {"/register-approved-device"},method= RequestMethod.POST,consumes = "multipart/form-data") 
	public @ResponseBody GenricResponse registerTypeApprove(@RequestParam(name="files[]") MultipartFile[] fileUpload,HttpServletRequest request,HttpSession session) {
		
		

		Integer userId= (int) session.getAttribute("userid");
		String roletype=(String) session.getAttribute("usertype");
		
		log.info("-inside controller register-approved-device-------request---------"+request.getParameter("manufacturerId"));
		// log.info(""+request.getParameter("file"));
		String userName=session.getAttribute("username").toString();
		String name=session.getAttribute("name").toString();
		Map<String, String[]> parameterMap = request.getParameterMap();
		log.info("************"+parameterMap.toString());
		log.info(" Register consignment entry point.");
		String txnNumber="T" + utildownload.getTxnId();
		log.info("Random transaction id number="+txnNumber);
		request.getParameterValues("");
		
		
		addMoreFileModel.setTag("system_upload_filepath");
		urlToUpload=feignCleintImplementation.addMoreBuutonCount(addMoreFileModel);
		
		
		FileCopyToOtherServer fileCopyRequest= new FileCopyToOtherServer();
		
		
		Gson gson= new Gson(); 
		String trcDetails=request.getParameter("multirequest");
		
		TRCRegisteration trcRequest = gson.fromJson(trcDetails, TRCRegisteration.class);
		trcRequest.setUserId(userId);
		trcRequest.setUserType(roletype);
		trcRequest.setTxnId(txnNumber);
		
		for (int i=0;i<trcRequest.getAttachedFiles().size();i++) {
			trcRequest.getAttachedFiles().get(i).setTxnId(txnNumber);
			//grievanceRequest.getMultifile().get(i).getDocType();
		}
		
		log.info("Random  genrated transaction number ="+txnNumber);
		int i=0;
		for( MultipartFile file : fileUpload) {

			log.info("-----"+ file.getOriginalFilename());
			log.info("++++"+ file);
		
			String tagName=trcRequest.getAttachedFiles().get(i).getDocType();
			log.info("doctype Name==="+tagName+"value of index="+i);
			

			try {
				
				
				

				byte[] bytes =
						file.getBytes(); String rootPath = urlToUpload.getValue()+txnNumber+"/"+tagName+"/"; 
						File dir =   new File(rootPath + File.separator);
						//give file read and writ  permission to txnId Directory
						File dir1 = new File(urlToUpload.getValue()+txnNumber+"/" + File.separator);
						if (!dir.exists()) {
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
						serverFile.setReadable(true,false);
						serverFile.setWritable(true,false);
						serverFile.setExecutable(true,false);
						stream.write(bytes); stream.close(); 
						//  grievanceRequest.setFileName(file.getOriginalFilename());
						
						fileCopyRequest.setFilePath(urlToUpload.getValue()+txnNumber+"/"+tagName+"/");
						fileCopyRequest.setTxnId(txnNumber);
						fileCopyRequest.setFileName(file.getOriginalFilename());
						fileCopyRequest.setServerId(propertyReader.serverId);
						log.info("request passed to move file to other server=="+fileCopyRequest);
						GenricResponse fileRespnose=grievanceFeignClient.saveUploadedFileOnANotherServer(fileCopyRequest);
						log.info("file move api response==="+fileRespnose);

			}
			catch (Exception e) { //
				// TODO: handle exception e.printStackTrace(); }

				// set reaquest parameters into model class

			}
			i++;


		}
		
		trcRequest.setPublicIp(session.getAttribute("publicIP").toString());
		trcRequest.setBrowser(session.getAttribute("browser").toString());
		log.info("TRC form parameters passed to save TRC api ="+trcRequest);
		GenricResponse response = typeApprovedFeignImpl.register(trcRequest);
		 response.setTxnId(txnNumber);
		

		 log.info("TRC from register TRC api"+response);
		 log.info("register TRC exit point.");
		 return response;
		
		
	
		} 

	

	@ResponseBody
	@PostMapping("viewByID/{id}")
	public ResponseEntity<?> viewByID(@PathVariable("id") int id,
			 @RequestParam(name="userType",required = false ) String userType, 
			 @RequestParam(name="userId",required = false ) Integer userId, HttpSession session) {
		String publicIp=session.getAttribute("publicIP").toString();
	    String browser=session.getAttribute("browser").toString();
		TRCRegisteration result = typeApprovedFeignImpl.viewByID(id,publicIp,browser,userType,userId);
		return new ResponseEntity<>(result,HttpStatus.OK);
	}



	//************************************************ update consignment record page********************************************************************************/

	@RequestMapping(value= {"/update-register-approved-device"},method= RequestMethod.POST) 
	public @ResponseBody GenricResponse updateRegister(@RequestParam(name="files[]") MultipartFile[] fileUpload,HttpServletRequest request,HttpSession session) {
		log.info("---------request---------"+request.getParameter("manufacturerId"));
		// log.info(""+request.getParameter("file"));

		Integer userId= (int) session.getAttribute("userid");
		String roletype=(String) session.getAttribute("usertype");
		log.info(" updateRegister consignment entry point.");

		addMoreFileModel.setTag("system_upload_filepath");
		urlToUpload=feignCleintImplementation.addMoreBuutonCount(addMoreFileModel);
		Gson gson= new Gson(); 
		String trcDetails=request.getParameter("multirequest");
		TRCRegisteration trcRequest = gson.fromJson(trcDetails, TRCRegisteration.class);
		FileCopyToOtherServer fileCopyRequest= new FileCopyToOtherServer();
		
		int i=0;
		for( MultipartFile file : fileUpload) {
			String tagName=trcRequest.getAttachedFiles().get(i).getDocType();
		try {
			if(fileUpload==null)
			{
				trcRequest.getAttachedFiles().get(i).setFileName("");
			}
			else {
				
				
				
			byte[] bytes = file.getBytes();
			String rootPath = urlToUpload.getValue()+trcRequest.getTxnId()+"/"+tagName+"/";
			File dir = new File(rootPath + File.separator);
			//give file read and writ  permission to txnId Directory
			File dir1 = new File(urlToUpload.getValue()+trcRequest.getTxnId()+"/" + File.separator);
			if (!dir.exists()) 
				{
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
			serverFile.setReadable(true,false);
			serverFile.setWritable(true,false);
			serverFile.setExecutable(true,false);
			stream.write(bytes);
			stream.close();
			fileCopyRequest.setFilePath(urlToUpload.getValue()+trcRequest.getTxnId()+"/"+tagName+"/");
			fileCopyRequest.setTxnId(trcRequest.getTxnId());
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
		trcRequest.setPublicIp(session.getAttribute("publicIP").toString());
		trcRequest.setBrowser(session.getAttribute("browser").toString());
		log.info("request passed to the update method="+trcRequest);
		GenricResponse response = typeApprovedFeignImpl.updateApproved(trcRequest);
		log.info("response  from   update method="+response);	
		response.setTxnId(trcRequest.getTxnId());
		return response;
	}
		




	//***************************************** Export Grievance controller *********************************
	/*
	 * @RequestMapping(value="/exportTac",method
	 * ={org.springframework.web.bind.annotation.RequestMethod.GET}) public String
	 * exportToExcel(@RequestParam(name="tacStartDate",required = false) String
	 * tacStartDate,
	 * 
	 * @RequestParam(name="tacStatus",required = false) Integer tacStatus,
	 * 
	 * @RequestParam(name="txnId") String txnId, HttpSession
	 * session,@RequestParam(name="pageSize") Integer pageSize,
	 * 
	 * @RequestParam(name="pageNo") Integer pageNo,
	 * 
	 * @RequestParam(name="tacNumber") String tacNumber,
	 * 
	 * @RequestParam(name="tacEndDate",required = false) String tacEndDate,
	 * 
	 * @RequestParam(name="featureId",required = false) Integer featureId,
	 * 
	 * @RequestParam(name="userType",required = false) String userType,
	 * 
	 * @RequestParam(name="userTypeId",required = false) Integer userTypeId,
	 * 
	 * @RequestParam(name="userId",required = false) Integer userId,
	 * 
	 * @RequestParam(name="source",defaultValue = "menu",required = false) String
	 * source ) { log.info("tacStartDate=="+tacStartDate+
	 * " tacStatus ="+tacStatus+" tacNumber="+tacNumber+"tacEndDate="+tacEndDate);
	 * //int userId= (int) session.getAttribute("userid"); log.info("source--->"
	 * +source); int file=1; FileExportResponse fileExportResponse; TRCRequest
	 * trcRequest = new TRCRequest(); trcRequest.setStartDate(tacStartDate);
	 * trcRequest.setEndDate(tacEndDate); trcRequest.setTac(tacNumber);
	 * trcRequest.setStatus(tacStatus); //trcRequest.setUserId(userId);
	 * trcRequest.setTxnId(txnId); trcRequest.setFeatureId(featureId);
	 * trcRequest.setUserType(userType); trcRequest.setUserTypeId(userTypeId);
	 * trcRequest.setUserId(userId); trcRequest.setFile(file);
	 * log.info(" request passed to the exportTo trcRequest Excel Api =="
	 * +trcRequest+" *********** pageSize"+pageSize+"  pageNo  "+pageNo+" userId=="
	 * +userId); Object response= typeApprovedFeignImpl.manageTypeFeign(trcRequest,
	 * pageNo, pageSize, file,source);
	 * 
	 * Gson gson= new Gson(); String apiResponse = gson.toJson(response);
	 * fileExportResponse = gson.fromJson(apiResponse, FileExportResponse.class);
	 * log.info("response  from   export trc  api ="+fileExportResponse);
	 * 
	 * return "redirect:"+fileExportResponse.getUrl(); }
	 */
	
	
	//------------------------------------- Export Address controller -------------------------------------
	
			@PostMapping("exportTac")
			@ResponseBody
			public FileExportResponse exportToExcel(@RequestBody TRCRequest trcRequest,HttpSession session)
			{
				Gson gsonObject=new Gson();
				Object response;
				Integer file = 1;	
				trcRequest.setPublicIp(session.getAttribute("publicIP").toString());
				trcRequest.setBrowser(session.getAttribute("browser").toString());
				log.info("filterRequest:::::::::"+trcRequest);
				response= typeApprovedFeignImpl.manageTypeFeign(trcRequest, trcRequest.getPageNo(), trcRequest.getPageSize(),trcRequest.getSource(),file);
				FileExportResponse fileExportResponse;
				Gson gson= new Gson(); 
				String apiResponse = gson.toJson(response);
				fileExportResponse = gson.fromJson(apiResponse, FileExportResponse.class);
				log.info("response  from  TRC Export  api="+fileExportResponse);

				return fileExportResponse;
			}
	
	//******************************************* TAC approved/Disapproved controller
	
	@ResponseBody
	@PostMapping("TACAprroveDisapprove")
	public GenricResponse TACAprroveDisapprove(@RequestBody TypeApprovedStatusModel model,HttpSession session) {
		model.setPublicIp(session.getAttribute("publicIP").toString());
		model.setBrowser(session.getAttribute("browser").toString());
		log.info("request send to the typeApproved api="+model);
		GenricResponse response = typeApprovedFeignImpl.TypeApproveReject(model);
		log.info("response from tac Approved/Disapprove api"+response);
		return response;
	}
			

}