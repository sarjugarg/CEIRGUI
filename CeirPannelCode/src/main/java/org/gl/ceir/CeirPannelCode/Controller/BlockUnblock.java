package org.gl.ceir.CeirPannelCode.Controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.gl.ceir.CeirPannelCode.PropertyReader;
import org.gl.ceir.CeirPannelCode.Feignclient.FeignCleintImplementation;
import org.gl.ceir.CeirPannelCode.Feignclient.GrievanceFeignClient;
import org.gl.ceir.CeirPannelCode.Model.AddMoreFileModel;
import org.gl.ceir.CeirPannelCode.Model.FileCopyToOtherServer;
import org.gl.ceir.CeirPannelCode.Model.GenricResponse;
import org.gl.ceir.CeirPannelCode.Model.SingleImeiDetailsModel;
import org.gl.ceir.CeirPannelCode.Model.StockUploadModel;
import org.gl.ceir.CeirPannelCode.Model.StolenRecoveryModel;

import org.gl.ceir.CeirPannelCode.Util.UtilDownload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BlockUnblock {
	
	
	
	
	@Autowired

	FeignCleintImplementation feignCleintImplementation;
	@Autowired
	UtilDownload utildownload;
	
	@Autowired
	GrievanceFeignClient grievanceFeignClient;
	
	@Autowired
	AddMoreFileModel addMoreFileModel,urlToUpload,urlToMove;
	
    @Autowired
    PropertyReader propertyReader;

	
	
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	ModelAndView mv = new ModelAndView();
	GenricResponse response= new GenricResponse();
	
	@RequestMapping(value="/blockUnblockDevices",method = {RequestMethod.GET,RequestMethod.POST} )
	public ModelAndView blockUnblockPage()
	{
		log.info("entry point in  view block or unblock page.");
		mv.setViewName("blockUnblockView");
		log.info("entry point in  view block or unblock page.");
		return mv;
	}
	
	@RequestMapping(value="/selectblockUnblockPage",method = {RequestMethod.GET,RequestMethod.POST} )
	public ModelAndView selectblockUnblockPage()
	{
		log.info("entry point in  select block or unblock option  page.");
		mv.setViewName("reportBlockUnblock");
		log.info("entry point in  select block or unblock option  page.");
		return mv;
		
	}
	@RequestMapping(value="/openBlockUnblockPage",method = {RequestMethod.GET,RequestMethod.POST} )
	public ModelAndView openBlockUnblockPage(@RequestParam(name="pageType",required = false) String pageType)
	{
		log.info("entry point in  open block or unblock   page."+pageType);
		if(pageType.equals("block"))
		{
			log.info("block page");
			mv.setViewName("reportBlock");
		}else {
			log.info("unBlock page");
			mv.setViewName("reportUnblock");
		}
		log.info("exit point in  open block or unblock   page.");
		return mv;
	}
	
	@RequestMapping(value="/blockSingleDevices",method = {RequestMethod.GET,RequestMethod.POST} )
	public @ResponseBody GenricResponse blockSingleDevices(@RequestBody SingleImeiDetailsModel singleImeiDetailsModel,HttpSession session )
	{
		log.info("entry point in  save  single imei block");
		int userId= (int) session.getAttribute("userid"); 
		String roletype=session.getAttribute("usertype").toString();
		//Integer operatorTypeId= (Integer) session.getAttribute("operatorTypeId");
		
		
		String parsedOperatorTypeId=String.valueOf(session.getAttribute("operatorTypeId"));
		Integer operatorTypeId=  Integer.valueOf(parsedOperatorTypeId);
		if(operatorTypeId==null)
		{
		log.info("operator type id is null="+operatorTypeId);
		operatorTypeId=-1;
		log.info("operator type value set to -1 ="+operatorTypeId);
		}
	    log.info("operaot type id=="+operatorTypeId);
		String blockTxnNumber=utildownload.getTxnId();
		blockTxnNumber = "B"+blockTxnNumber;
		log.info("Random transaction id number="+blockTxnNumber);
		
		singleImeiDetailsModel.setTxnId(blockTxnNumber);
		singleImeiDetailsModel.setUserId(userId);
		singleImeiDetailsModel.setUserType(roletype);
		singleImeiDetailsModel.setOperatorTypeId(operatorTypeId);
		log.info("request send to the save signle Imei block devices="+singleImeiDetailsModel);
		response= grievanceFeignClient.singleImeiBlockDevices(singleImeiDetailsModel);
		log.info("response from save signle Imei block devices="+response);
		return response;
		
	}
	
	@RequestMapping(value="/blockUnBlockSingleDevices",method = {RequestMethod.GET,RequestMethod.POST} )
	public @ResponseBody GenricResponse UnblockSingleDevices(@RequestBody SingleImeiDetailsModel singleImeiDetailsModel,HttpSession session )
	{
		log.info("entry point in  save  single imei block");
		int userId= (int) session.getAttribute("userid"); 
		String roletype=session.getAttribute("usertype").toString();
		String blockTxnNumber=utildownload.getTxnId();
		Integer operatorTypeId;
		String parsedOperatorTypeId=String.valueOf(session.getAttribute("operatorTypeId"));
		log.info("operatorType id is null or not ---="+parsedOperatorTypeId);
		if(parsedOperatorTypeId.equals("null"))
		{
			parsedOperatorTypeId=null;
			operatorTypeId=null;
			//Integer operatorTypeId=  Integer.valueOf(parsedOperatorTypeId);	
		}
		else {
			 operatorTypeId=  Integer.valueOf(parsedOperatorTypeId);
		}
		
		log.info("operatorTypeId==="+operatorTypeId);
		
		if(operatorTypeId==null)
		{
		log.info("operator type id is null="+operatorTypeId);
		operatorTypeId=-1;
		log.info("operator type value set to -1 ="+operatorTypeId);
		}
	    log.info("operaot type id=="+operatorTypeId);
		blockTxnNumber = "B"+blockTxnNumber;
		log.info("Random transaction id number="+blockTxnNumber);
		singleImeiDetailsModel.setTxnId(blockTxnNumber);
		singleImeiDetailsModel.setUserId(userId);
		singleImeiDetailsModel.setUserType(roletype);
		singleImeiDetailsModel.setRoleType(roletype);
		singleImeiDetailsModel.setOperatorTypeId(operatorTypeId);

		singleImeiDetailsModel.setPublicIp(session.getAttribute("publicIP").toString());
		singleImeiDetailsModel.setBrowser(session.getAttribute("browser").toString());
		log.info("request send to the save signle Imei block devices="+singleImeiDetailsModel);
		response= grievanceFeignClient.singleImeiBlockDevices(singleImeiDetailsModel);
		log.info("response from save signle Imei block devices="+response);
		return response;
		
	}
	@RequestMapping(value="/updateBlockUnBlockSingleDevices",method = {RequestMethod.GET,RequestMethod.POST} )
	public @ResponseBody GenricResponse updateBlockUnBlockSingleDevices(@RequestBody SingleImeiDetailsModel singleImeiDetailsModel,HttpSession session )
	{
		log.info("entry point in  update  single imei block");
		int userId= (int) session.getAttribute("userid"); 
		String roletype=session.getAttribute("usertype").toString();
		/*
		 * String blockTxnNumber=utildownload.getTxnId(); blockTxnNumber =
		 * "B"+blockTxnNumber; log.info("Random transaction id number="+blockTxnNumber);
		 * singleImeiDetailsModel.setTxnId(blockTxnNumber);
		 */
		Integer operatorTypeId;
		String parsedOperatorTypeId=String.valueOf(session.getAttribute("operatorTypeId"));
		log.info("operatorType id is null or not ---="+parsedOperatorTypeId);
		if(parsedOperatorTypeId.equals("null"))
		{
			parsedOperatorTypeId=null;
			operatorTypeId=null;
			//Integer operatorTypeId=  Integer.valueOf(parsedOperatorTypeId);	
		}
		else {
			 operatorTypeId=  Integer.valueOf(parsedOperatorTypeId);
		}
		
		log.info("operatorTypeId==="+operatorTypeId);
		//Integer operatorTypeId= (Integer) session.getAttribute("operatorTypeId"); 
		if(operatorTypeId==null)
		{
		log.info("operator type id is null="+operatorTypeId);
		operatorTypeId=-1;
		log.info("operator type value set to -1 ="+operatorTypeId);
		}
	    log.info("operaot type id=="+operatorTypeId);
		singleImeiDetailsModel.setUserId(userId);
		singleImeiDetailsModel.setUserType(roletype);
		singleImeiDetailsModel.setRoleType(roletype);
		singleImeiDetailsModel.setOperatorTypeId(operatorTypeId);
		
		singleImeiDetailsModel.setPublicIp(session.getAttribute("publicIP").toString());
		singleImeiDetailsModel.setBrowser(session.getAttribute("browser").toString());
		log.info("request send to the upate signle Imei block devices="+singleImeiDetailsModel);
		response= grievanceFeignClient.updateSingleImeiBlockDevices(singleImeiDetailsModel);
		log.info("response from update signle Imei block devices="+response);
		return response;
		
	}
	
	 
//*************************************************** block bulk file ****************************************************************************
	  
	  @RequestMapping(value={"/reportblockBulkFile"},method={org.springframework.web. bind.annotation.RequestMethod.GET,org.springframework.web.bind.annotation.
			  RequestMethod.POST}) 
	  public @ResponseBody GenricResponse FileTypeStolen(@RequestParam(name="blockingType",required = false) String blockingType,@RequestParam(name="blockingTimePeriod",required = false) String blockingTimePeriod,
			  @RequestParam(name="file",required = false) MultipartFile file,@RequestParam(name="requestType",required = false) int requestType,
			  @RequestParam(name="roleType",required = false) String roleType,  @RequestParam(name="sourceType",required = false) Integer sourceType,
			  @RequestParam(name="userId",required = false) Integer userId,@RequestParam(name="qty",required = false) Integer qty,
			  @RequestParam(name="deviceQuantity",required = false) Integer deviceQuantity,
			  @RequestParam(name="blockCategory",required = false) Integer deviceCategory,@RequestParam(name="remark",required = false) String remark, HttpSession session)
 {	
		  log.info(" file stolen entry point .");
		  FileCopyToOtherServer fileCopyRequest= new FileCopyToOtherServer();
		    StolenRecoveryModel stolenRecoveryModel= new StolenRecoveryModel(); 
		    GenricResponse response= new GenricResponse();
		  //  Integer operatorTypeId= (Integer) session.getAttribute("operatorTypeId"); 
			Integer operatorTypeId;
			String parsedOperatorTypeId=String.valueOf(session.getAttribute("operatorTypeId"));
			log.info("operatorType id is null or not ---="+parsedOperatorTypeId);
			if(parsedOperatorTypeId.equals("null"))
			{
				parsedOperatorTypeId=null;
				operatorTypeId=null;
				//Integer operatorTypeId=  Integer.valueOf(parsedOperatorTypeId);	
			}
			else {
				 operatorTypeId=  Integer.valueOf(parsedOperatorTypeId);
			}
			
			log.info("operatorTypeId==="+operatorTypeId);
		    
		    addMoreFileModel.setTag("system_upload_filepath");
			urlToUpload=feignCleintImplementation.addMoreBuutonCount(addMoreFileModel);
		    if(operatorTypeId==null)
		    {
		    log.info("operator type id is null="+operatorTypeId);
		    operatorTypeId=-1;
		    log.info("operator type value set to -1 ="+operatorTypeId);
		    }
		    log.info("operaot type id=="+operatorTypeId);
			String roletype=session.getAttribute("usertype").toString();
			String stlnTxnNumber=utildownload.getTxnId();
			stlnTxnNumber = "B"+stlnTxnNumber;
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
				
				fileCopyRequest.setFilePath(rootPath);
				fileCopyRequest.setTxnId(stlnTxnNumber);
				fileCopyRequest.setFileName(file.getOriginalFilename());
				fileCopyRequest.setServerId(propertyReader.serverId);
				log.info("request passed to move file to other server=="+fileCopyRequest);
				GenricResponse fileRespnose=grievanceFeignClient.saveUploadedFileOnANotherServer(fileCopyRequest);
				log.info("file move api response==="+fileRespnose);

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
			stolenRecoveryModel.setRemark(remark);
			//stolenRecoveryModel.setDeviceCaegory(deviceCaegory);
			stolenRecoveryModel.setOperatorTypeId(operatorTypeId);
			stolenRecoveryModel.setBlockCategory(deviceCategory);
			stolenRecoveryModel.setRoleType(roletype);
			stolenRecoveryModel.setDeviceQuantity(deviceQuantity);
			
			stolenRecoveryModel.setPublicIp(session.getAttribute("publicIP").toString());
			stolenRecoveryModel.setBrowser(session.getAttribute("browser").toString());
			log.info("request passed to the file stolen api ="+stolenRecoveryModel);
			response=feignCleintImplementation.fileStolen(stolenRecoveryModel);
			log.info("respondse from file stolen api="+response);
			log.info(" file stolen api exist point .");
		  	return response;
	
	  }
	  
	  
//************************************************************ unblock bulk file ********************************************************************
	  
	  @RequestMapping(value={"/reportUnblockBulkFile"},method={org.springframework.web. bind.annotation.RequestMethod.GET,org.springframework.web.bind.annotation.
			  RequestMethod.POST}) 
	  public @ResponseBody GenricResponse fileTypeRecovery( @RequestParam(name="file",required = false) MultipartFile file,@RequestParam(name="requestType",required = false) int requestType,
			  @RequestParam(name="roleType",required = false) String roleType,  @RequestParam(name="sourceType",required = false) Integer sourceType,
			  @RequestParam(name="userId",required = false) Integer userId,@RequestParam(name="qty",required = false) Integer qty,
			  @RequestParam(name="deviceQuantity",required = false) Integer deviceQuantity,
			  @RequestParam(name="blockCategory",required = false) Integer blockCategory,@RequestParam(name="remark",required = false) String remark,HttpSession session
			  )
	  {	
		  
		  log.info(" file Recovery api entry point .");
		  StolenRecoveryModel stolenRecoveryModel= new StolenRecoveryModel(); 
		  GenricResponse response= new GenricResponse();
			String stlnTxnNumber=utildownload.getTxnId();
			stlnTxnNumber = "B"+stlnTxnNumber;
			
			addMoreFileModel.setTag("system_upload_filepath");
			urlToUpload=feignCleintImplementation.addMoreBuutonCount(addMoreFileModel);
			FileCopyToOtherServer fileCopyRequest= new FileCopyToOtherServer();
			
			
			//int operatorTypeId= (int) session.getAttribute("operatorTypeId"); 
			// Integer operatorTypeId= (Integer) session.getAttribute("operatorTypeId"); 
			Integer operatorTypeId;
			String parsedOperatorTypeId=String.valueOf(session.getAttribute("operatorTypeId"));
			log.info("operatorType id is null or not ---="+parsedOperatorTypeId);
			if(parsedOperatorTypeId.equals("null"))
			{
				parsedOperatorTypeId=null;
				operatorTypeId=null;
				//Integer operatorTypeId=  Integer.valueOf(parsedOperatorTypeId);	
			}
			else {
				 operatorTypeId=  Integer.valueOf(parsedOperatorTypeId);
			}
			
			log.info("operatorTypeId==="+operatorTypeId);
			if(operatorTypeId==null)
			 {
			 log.info("operator type id is null="+operatorTypeId);
			 operatorTypeId=-1;
			 log.info("operator type value set to -1 ="+operatorTypeId);
			 }
			    log.info("operaot type id=="+operatorTypeId);
				//String roletype=session.getAttribute("usertype").toString();
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
				
				fileCopyRequest.setFilePath(rootPath);
				fileCopyRequest.setTxnId(stlnTxnNumber);
				fileCopyRequest.setFileName(file.getOriginalFilename());
				fileCopyRequest.setServerId(propertyReader.serverId);
				log.info("request passed to move file to other server=="+fileCopyRequest);
				GenricResponse fileRespnose=grievanceFeignClient.saveUploadedFileOnANotherServer(fileCopyRequest);
				log.info("file move api response==="+fileRespnose);
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
			stolenRecoveryModel.setRemark(remark);

			//stolenRecoveryModel.setDeviceCaegory(deviceCaegory);
			stolenRecoveryModel.setBlockCategory(blockCategory);
			stolenRecoveryModel.setOperatorTypeId(operatorTypeId);
			stolenRecoveryModel.setDeviceQuantity(deviceQuantity);
			stolenRecoveryModel.setBlockingType("Immediate");
			log.info("request sent to fileRecovery api ="+stolenRecoveryModel);

			stolenRecoveryModel.setPublicIp(session.getAttribute("publicIP").toString());
			stolenRecoveryModel.setBrowser(session.getAttribute("browser").toString());
			response=feignCleintImplementation.fileRecovery(stolenRecoveryModel);
			log.info("request sent to file Recovery api ="+response);
			log.info(" file Recovery api exist point .");
			
			return response;
	
	  }
	/*
	 * 
	 * // *********************************************** open register page or edit
	 * popup ******************************
	 * 
	 * @RequestMapping(value="/openblockUnblockView",method
	 * ={org.springframework.web.bind.annotation.RequestMethod.GET})
	 * public @ResponseBody SingleImeiDetailsModel
	 * openRegisterConsignmentPopup(@RequestParam(name="reqType") String
	 * reqType,@RequestParam(name="txnId",required = false) String
	 * txnId,@RequestParam(name="singleDeivce",required = false) String
	 * singleDeivce,HttpSession session) { log.
	 * info("entry point of  fetch block/unclock devices in the bases of transaction id ."
	 * ); SingleImeiDetailsModel stockUploadModel= new SingleImeiDetailsModel();
	 * //StockUploadModel stockUploadModelResponse;
	 * stockUploadModel.setTxnId(txnId); //stockUploadModel.setRoleType(role);
	 * log.info("request passed to the fetch Device api="+stockUploadModel);
	 * stockUploadModel.setCategory(1); stockUploadModel.setCategoryInterp("other");
	 * stockUploadModel.setDeviceIdType(1);
	 * stockUploadModel.setDeviceIdTypeInterp("Imei");
	 * stockUploadModel.setDeviceTypeInterp("Handheld");
	 * stockUploadModel.setDeviceType(1); stockUploadModel.setMultipleSimStatus(1);
	 * stockUploadModel.setMultipleSimStatusInterp("yes");
	 * //stockUploadModel.setDeviceSerialNumber(123453);
	 * stockUploadModel.setRemark("remark for block");
	 * stockUploadModel.setFirstImei(111111111);
	 * stockUploadModel.setSecondImei(22222222);
	 * stockUploadModel.setThirdImei(33333333);
	 * stockUploadModel.setFourthImei(44444444);
	 * stockUploadModel.setTxnId("B3112201912345");
	 * //stockUploadModelResponse=feignCleintImplementation.
	 * fetchUploadedStockByTxnId(stockUploadModel);
	 * //log.info("response from fetch stock api="+stockUploadModelResponse); return
	 * stockUploadModel; }
	 */
		
		@RequestMapping(value="/openbulkView",method ={org.springframework.web.bind.annotation.RequestMethod.GET})
		public @ResponseBody Object openBulkFile(@RequestParam(name="reqType") Integer reqType,@RequestParam(name="txnId",required = false) String txnId,@RequestParam(name="singleDeivce",required = false) String singleDeivce,HttpSession session)
		{
			log.info("entry point of  fetch block/unclock devices in the bases of transaction id .");
			StolenRecoveryModel viewbulkDevices= new StolenRecoveryModel();
			int userId= (int) session.getAttribute("userid"); 
			String roletype=session.getAttribute("usertype").toString();
			viewbulkDevices.setTxnId(txnId);
			viewbulkDevices.setUserId(userId);
			viewbulkDevices.setRoleType(roletype);
			viewbulkDevices.setRequestType(reqType);
			viewbulkDevices.setPublicIp(session.getAttribute("publicIP").toString());
			viewbulkDevices.setBrowser(session.getAttribute("browser").toString());
			log.info("request passed to the fetch Device api="+viewbulkDevices);
			Object ds=feignCleintImplementation.fetchBulkDeviceByTxnId(viewbulkDevices);
			//log.info("response from fetch stock api="+stockUploadModelResponse);
				return ds;
		}
		
//******************************************************* fetch singleImei details through txnId************************************************************		
		
		@RequestMapping(value="/openSingleImeiForm",method ={org.springframework.web.bind.annotation.RequestMethod.GET})
		public @ResponseBody List<SingleImeiDetailsModel> openSingleImeiForm(@RequestParam(name="reqType") String reqType,@RequestParam(name="txnId",required = false) String txnId,@RequestParam(name="singleDeivce",required = false) String singleDeivce,HttpSession session)
		{
			log.info("entry point of  fetch block/unclock devices in the bases of transaction id .");
			
			List<SingleImeiDetailsModel> singleImeiDetails= new ArrayList<SingleImeiDetailsModel>();
			log.info("request passed to the fetch Device api="+txnId);
			singleImeiDetails=grievanceFeignClient.fetchSingleDevicebyTxnId(txnId);
			log.info("response from fetch Single device  api="+singleImeiDetails);
				return singleImeiDetails;
		}
		
		
		
	
}
