package org.gl.ceir.CeirPannelCode.Controller;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.gl.ceir.CeirPannelCode.PropertyReader;
import org.gl.ceir.CeirPannelCode.Feignclient.FeignCleintImplementation;
import org.gl.ceir.CeirPannelCode.Feignclient.GrievanceFeignClient;
import org.gl.ceir.CeirPannelCode.Model.AddMoreFileModel;
import org.gl.ceir.CeirPannelCode.Model.FileCopyToOtherServer;
import org.gl.ceir.CeirPannelCode.Model.FileExportResponse;
import org.gl.ceir.CeirPannelCode.Model.FilterRequest;
import org.gl.ceir.CeirPannelCode.Model.FilterRequest_UserPaidStatus;
import org.gl.ceir.CeirPannelCode.Model.GenricResponse;
import org.gl.ceir.CeirPannelCode.Model.StockUploadModel;
import org.gl.ceir.CeirPannelCode.Model.StolenRecoveryModel;
import org.gl.ceir.CeirPannelCode.Model.Usertype;
import org.gl.ceir.CeirPannelCode.Util.UtilDownload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

@Controller
public class StolenRecovery {

	
	
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Autowired

	FeignCleintImplementation feignCleintImplementation;
	@Autowired
	UtilDownload utildownload;
	
	@Autowired
	AddMoreFileModel addMoreFileModel,urlToUpload,urlToMove;
	
	@Autowired
    PropertyReader propertyReader;
	
	@Autowired
	GrievanceFeignClient grievanceFeignClient;
	
	
	
	
	@RequestMapping(value={"/stolenRecovery"},method={org.springframework.web.bind.annotation.RequestMethod.GET,org.springframework.web.bind.annotation.RequestMethod.POST})
	public ModelAndView  viewStolenRecovery( HttpSession session , @RequestParam(name="userTypeId",required=false) String selectedUserTypeId 
			,@RequestParam(name="txnID",required = false) String txnID, @RequestParam(name="FeatureId",required = false) String featureId,
			@RequestParam(name="requestType" ,required = false)String requestType,
			@RequestParam(name="source",defaultValue = "menu",required = false) String source
			) {
			
		ModelAndView mv = new ModelAndView();
		log.info("entry point in stolen recovery  page with featureId-->  " +featureId+"  source  =="+source+"  txnid=="+txnID);
		try {
		String roletype=session.getAttribute("usertype").toString();
		String OperatorId = String.valueOf(session.getAttribute("operatorTypeId"));
		if(selectedUserTypeId==null)
		{
		List<Usertype> userTypelist=(List<Usertype>) session.getAttribute("usertypeList");
		if(userTypelist.size()>1)
		{   log.info("role type list=="+userTypelist);
			mv.addObject("userTypelist", userTypelist);
			mv.setViewName("StolenRecoverytRoleType");
		}
		else if(userTypelist.size()==1)
		{
			if((roletype.equals("Lawful Agency") || roletype.equals("CEIRAdmin")) && "5".equals(featureId))
			{
				log.info(" 1 return Lawful Stolen Recovery**roletype****"+roletype+" featureId******" +featureId);
				session.removeAttribute("requestType");
				session.setAttribute("requestType",requestType);
				session.setAttribute("filterSource", source);
				session.setAttribute("txnID", txnID);
				mv.setViewName("lawfulStolenRecovery");
			}
			
			else {
				log.info("  2  return stolen Recovery**roletype****"+roletype+" featureId******" +featureId+"****OperatorId***"+OperatorId);
				session.setAttribute("stolenselectedUserTypeId", roletype);
				session.setAttribute("operatorTypeId", OperatorId);
				session.removeAttribute("requestType");
				session.setAttribute("requestType",requestType);
				session.setAttribute("filterSource", source);
				session.setAttribute("txnID", txnID);
				mv.setViewName("stolenRecovery");
			}
				
		}
		}
		else {
			session.setAttribute("filterSource", source);
			log.info("selected role type in stolen and recovery  is = "+selectedUserTypeId);
			session.setAttribute("stolenselectedUserTypeId", selectedUserTypeId);
			mv.setViewName("stolenRecovery");		
		
		}
		}
		catch (Exception e) {
			// TODO: handle exception
			log.info("this is catch block session is blank or something went wrong.");
		}
				
				return mv; 
			}
	
//******************************************* multiple stolen recovery ************************************************************************88	
	  @RequestMapping(value={"/multipleStolenRecovery"},method={org.springframework.web. bind.annotation.RequestMethod.GET,org.springframework.web.bind.annotation.
	  RequestMethod.POST}) 
	  public @ResponseBody GenricResponse markStolen(@RequestBody ArrayList<StolenRecoveryModel>  stolen)
	  { 
	  log.info("enter in multiple stolenRecovery controller");
		
		/*
		 * List<StolenRecoveryModel> request= new ArrayList<StolenRecoveryModel>();
		 * request.add(stolen);
		 */
		 
	  log.info("stolen request  passed to the multiple stolen ="+stolen);
	  GenricResponse response=  feignCleintImplementation.multipleStolen(stolen);
	  log.info("response from multiple Stolen api=="+response);
	  log.info(" multiple stolen recovery  exit point .");
	  return response;
	  
	  }
	 
//*************************************************** file type stolen ****************************************************************************
	  
	  @RequestMapping(value={"/fileTypeStolen"},method={org.springframework.web. bind.annotation.RequestMethod.GET,org.springframework.web.bind.annotation.
			  RequestMethod.POST}) 
	  public @ResponseBody GenricResponse FileTypeStolen(@RequestParam(name="blockingType",required = false) String blockingType,@RequestParam(name="blockingTimePeriod",required = false) String blockingTimePeriod,
			  @RequestParam(name="file",required = false) MultipartFile file,@RequestParam(name="requestType",required = false) int requestType,
			  @RequestParam(name="roleType",required = false) String roleType,  @RequestParam(name="sourceType",required = false) Integer sourceType,
			  @RequestParam(name="userId",required = false) Integer userId,@RequestParam(name="qty",required = false) Integer qty)
	  {	
		  log.info(" file stolen entry point .");
		 
		    StolenRecoveryModel stolenRecoveryModel= new StolenRecoveryModel(); 
		    GenricResponse response= new GenricResponse();
			String stlnTxnNumber=utildownload.getTxnId();
			stlnTxnNumber = "L"+stlnTxnNumber;
			addMoreFileModel.setTag("system_upload_filepath");
			urlToUpload=feignCleintImplementation.addMoreBuutonCount(addMoreFileModel);
			
			log.info("Random transaction id number="+stlnTxnNumber);
		  	try {
				byte[] bytes = file.getBytes();
				String rootPath = urlToUpload.getValue()+stlnTxnNumber+"/";
				File dir = new File(rootPath + File.separator);

				if (!dir.exists()) {
					dir.mkdirs();
					dir.setExecutable(true,false);
					dir.setReadable(true,false);
					dir.setWritable(true,false);
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

			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			stolenRecoveryModel.setBlockingTimePeriod(blockingTimePeriod);
			stolenRecoveryModel.setBlockingType(blockingType);
			stolenRecoveryModel.setFileName(file.getOriginalFilename());
			stolenRecoveryModel.setRequestType(requestType);
			stolenRecoveryModel.setSourceType(sourceType);
			stolenRecoveryModel.setUserId(userId);
			stolenRecoveryModel.setRoleType(roleType);
			stolenRecoveryModel.setTxnId(stlnTxnNumber);
			stolenRecoveryModel.setQty(qty);
			log.info("request passed to the file stolen api ="+stolenRecoveryModel);
			response=feignCleintImplementation.fileStolen(stolenRecoveryModel);
			log.info("respondse from file stolen api="+response);
			log.info(" file stolen api exist point .");
		  	return response;
	
	  }
	  
	  
// ************************************************************ file type recovery ********************************************************************
	  
	  @RequestMapping(value={"/fileTypeRecovery"},method={org.springframework.web. bind.annotation.RequestMethod.GET,org.springframework.web.bind.annotation.
			  RequestMethod.POST}) 
	  public @ResponseBody GenricResponse fileTypeRecovery( @RequestParam(name="file",required = false) MultipartFile file,@RequestParam(name="requestType",required = false) int requestType,
			  @RequestParam(name="roleType",required = false) String roleType,  @RequestParam(name="sourceType",required = false) Integer sourceType,
			  @RequestParam(name="userId",required = false) Integer userId,@RequestParam(name="qty",required = false) Integer qty)
	  {	
		  
		  log.info(" file Recovery api entry point .");
		  StolenRecoveryModel stolenRecoveryModel= new StolenRecoveryModel(); 
		  GenricResponse response= new GenricResponse();
			String stlnTxnNumber=utildownload.getTxnId();
			stlnTxnNumber = "L"+stlnTxnNumber;
			addMoreFileModel.setTag("system_upload_filepath");
			urlToUpload=feignCleintImplementation.addMoreBuutonCount(addMoreFileModel);
			
			log.info("Random transaction id number="+stlnTxnNumber);
		  	try {
				byte[] bytes = file.getBytes();
				String rootPath = urlToUpload.getValue()+stlnTxnNumber+"/";
				File dir = new File(rootPath + File.separator);
				
				if (!dir.exists()) 
				{	dir.mkdirs();
				dir.setExecutable(true,false);
				dir.setReadable(true,false);
				dir.setWritable(true,false);
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

			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			stolenRecoveryModel.setFileName(file.getOriginalFilename());
			stolenRecoveryModel.setRequestType(requestType);
			stolenRecoveryModel.setSourceType(sourceType);
			stolenRecoveryModel.setUserId(userId);
			stolenRecoveryModel.setRoleType(roleType);
			stolenRecoveryModel.setTxnId(stlnTxnNumber);
			stolenRecoveryModel.setQty(qty);
			log.info("request sent to fileRecovery api ="+stolenRecoveryModel);
			response=feignCleintImplementation.fileRecovery(stolenRecoveryModel);
			log.info("request sent to file Recovery api ="+response);
			log.info(" file Recovery api exist point .");
			
			return response;
	
	  }

	  
// ***************************************** delete stolen recovery controller **************************************************************

		@RequestMapping(value= {"/stolenRecoveryDelete"},method={org.springframework.web.bind.annotation.RequestMethod.GET,org.springframework.web.bind.annotation.RequestMethod.POST}) 
		public @ResponseBody GenricResponse deleteStock(@RequestBody FilterRequest stolenRecoveryModel,HttpSession session) {
			String roletype=session.getAttribute("usertype").toString();
			log.info("enter in  delete stolenRecovery.==");
			stolenRecoveryModel.setUserType(roletype);

			stolenRecoveryModel.setPublicIp(session.getAttribute("publicIP").toString());
			stolenRecoveryModel.setBrowser(session.getAttribute("browser").toString());
	
			log.info("request passed to the delete stolenRecovery Api="+stolenRecoveryModel);
			
			GenricResponse response=feignCleintImplementation.deleteStolenRecovery(stolenRecoveryModel);
			log.info("response after delete stolenRecovery."+response);
			log.info("exit point of delete stolenRecovery.");
			return response;
			

		}
		
// ****************************************** update stolen recovery controller**********************************************************8
		 
		
			  
			  @RequestMapping(value={"/updateFileTypeStolenRecovery"},method={org.springframework.web. bind.annotation.RequestMethod.GET,org.springframework.web.bind.annotation.
					  RequestMethod.POST}) 
			  public @ResponseBody GenricResponse updateFileTypeStolenRecovery(@RequestParam(name="blockingType",required = false) String blockingType,@RequestParam(name="blockingTimePeriod",required = false) String blockingTimePeriod,
					  @RequestParam(name="file",required = false) MultipartFile file,@RequestParam(name="requestType",required = false) int requestType,
					  @RequestParam(name="roleType",required = false) String roleType,  @RequestParam(name="sourceType",required = false) Integer sourceType,
					  @RequestParam(name="userId",required = false) Integer userId,@RequestParam(name="txnId",required = false) String txnId,@RequestParam(name="id",required = false) Integer id,
					  @RequestParam(name="blockCategory",required = false) Integer blockCategory,@RequestParam(name="remark",required = false) String remark,@RequestParam(name="fileName",required = false) String fileName,
					  @RequestParam(name="qty",required = false) Integer qty, @RequestParam(name="deviceQuantity",required = false) Integer deviceQuantity,HttpSession session)
{	
				  StolenRecoveryModel stolenRecoveryModel= new StolenRecoveryModel();
				  GenricResponse response = new GenricResponse();
				    addMoreFileModel.setTag("system_upload_filepath");
					urlToUpload=feignCleintImplementation.addMoreBuutonCount(addMoreFileModel);
					
					addMoreFileModel.setTag("uploaded_file_move_path");
					urlToMove=feignCleintImplementation.addMoreBuutonCount(addMoreFileModel);
					String movedFileTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
					log.info("Moved File Time value=="+movedFileTime);
					
					
				  log.info(" update file stolen/recovery entry point .");
				  log.info("Random transaction id number="+txnId);
				  	try {
				  		if(file==null) {
				  			stolenRecoveryModel.setFileName(fileName);
				  		}{			
				  			FileCopyToOtherServer fileCopyRequest= new FileCopyToOtherServer();
				  			log.info("file is not null");
				  			
				  		    
				  			String rootPath = urlToUpload.getValue()+txnId+"/";
				  		File tmpDir = new File(rootPath+file.getOriginalFilename());
				  		tmpDir.setExecutable(true,false);
						tmpDir.setReadable(true,false);
						tmpDir.setWritable(true,false);
				  		boolean exists = tmpDir.exists();

				  		if(exists) {
				  			log.info("file already exist");
				  		Path temp = Files.move 
				  		(Paths.get(urlToUpload.getValue()+txnId+"/"+file.getOriginalFilename()), 
				  		Paths.get(urlToMove.getValue()+movedFileTime+"_"+file.getOriginalFilename())); 

				  		String movedPath=urlToMove.getValue()+movedFileTime+"_"+file.getOriginalFilename();
				  		// tmpDir.renameTo(new File("/home/ubuntu/apache-tomcat-9.0.4/webapps/MovedFile/"+txnId+"/"));
				  		log.info("file is already exist moved to the this "+movedPath+" path");
				  		tmpDir.delete();
				  		}
						byte[] bytes = file.getBytes();
						File dir = new File(rootPath + File.separator);
						if (!dir.exists()) 
							{
							dir.mkdirs();
							dir.setExecutable(true,false);
							dir.setReadable(true,false);
							dir.setWritable(true,false);
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
			  			fileCopyRequest.setTxnId(txnId);
			  			fileCopyRequest.setFileName(file.getOriginalFilename());
			  			fileCopyRequest.setServerId(propertyReader.serverId);
			  			log.info("request passed to move file to other server=="+fileCopyRequest);
			  			GenricResponse fileRespnose=grievanceFeignClient.saveUploadedFileOnANotherServer(fileCopyRequest);
			  			log.info("file move api response==="+fileRespnose);
						stolenRecoveryModel.setFileName(file.getOriginalFilename());
				  		}	
					}
					catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					stolenRecoveryModel.setBlockingTimePeriod(blockingTimePeriod);
					stolenRecoveryModel.setBlockingType(blockingType);
					
					stolenRecoveryModel.setRequestType(requestType);
					stolenRecoveryModel.setSourceType(sourceType);
					stolenRecoveryModel.setUserId(userId);
					stolenRecoveryModel.setRoleType(roleType);
					stolenRecoveryModel.setTxnId(txnId);
					
					stolenRecoveryModel.setQty(qty);
					//stolenRecoveryModel.setCategory(deviceCaegory);
					stolenRecoveryModel.setBlockCategory(blockCategory);

					stolenRecoveryModel.setRemark(remark);
					stolenRecoveryModel.setDeviceQuantity(deviceQuantity);
					stolenRecoveryModel.setPublicIp(session.getAttribute("publicIP").toString());
					stolenRecoveryModel.setBrowser(session.getAttribute("browser").toString());
					log.info("request passed to the update file stolen api ="+stolenRecoveryModel);
					response=feignCleintImplementation.updateFileStolen(stolenRecoveryModel);
					log.info("respondse from update  file stolen api="+response);
					log.info(" update  file stolen api exist point .");
				  	return response;
			
			  }
			  
			//***************************************** Export Grievance controller *********************************
				@PostMapping("exportStolenRecovery")
				@ResponseBody
				public FileExportResponse exportToExcel(@RequestBody FilterRequest filterRequest,HttpSession session,
						@RequestParam(name="source",defaultValue = "menu",required = false) String source)
				{
					log.info("source--->" +source);
					Integer userId= (Integer) session.getAttribute("userid");
					Gson gsonObject=new Gson();
					Object response;
					Integer file = 1;	
					filterRequest.setUserId(userId);

					filterRequest.setPublicIp(session.getAttribute("publicIP").toString());
					filterRequest.setBrowser(session.getAttribute("browser").toString());
					
					log.info("filterRequest:::::::::"+filterRequest);
					response= feignCleintImplementation.stolenFilter(filterRequest, filterRequest.getPageNo(), filterRequest.getPageSize(), file,source);
				FileExportResponse fileExportResponse;
				   Gson gson= new Gson(); 
				   String apiResponse = gson.toJson(response);
				   fileExportResponse = gson.fromJson(apiResponse, FileExportResponse.class);
				   log.info("response  from   export stolen/recovery  api="+fileExportResponse);
					
					return fileExportResponse;
				}
				
				
				
				//******************************* Block Unblock Approve/Reject Devices ********************************
				
				
				@PostMapping("blockUnblockApproveReject") 
				public @ResponseBody GenricResponse approveRejectDevice (@RequestBody FilterRequest FilterRequest,HttpSession session)  {
					
					FilterRequest.setPublicIp(session.getAttribute("publicIP").toString());
					FilterRequest.setBrowser(session.getAttribute("browser").toString());
					log.info("request send to the approveRejectDevice api="+FilterRequest);
					GenricResponse response= feignCleintImplementation.approveRejectFeign(FilterRequest);

					log.info("response from currency api "+response);
					return response;

					}
				
}
