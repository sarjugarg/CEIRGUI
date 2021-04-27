package org.gl.ceir.CeirPannelCode.Controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.gl.ceir.CeirPannelCode.PropertyReader;
import org.gl.ceir.CeirPannelCode.Feignclient.FeignCleintImplementation;
import org.gl.ceir.CeirPannelCode.Feignclient.GrievanceFeignClient;
import org.gl.ceir.CeirPannelCode.Feignclient.UserProfileFeignImpl;
import org.gl.ceir.CeirPannelCode.Feignclient.UserRegistrationFeignImpl;
import org.gl.ceir.CeirPannelCode.Model.AddMoreFileModel;
import org.gl.ceir.CeirPannelCode.Model.ConsignmentUpdateRequest;
import org.gl.ceir.CeirPannelCode.Model.FileCopyToOtherServer;
import org.gl.ceir.CeirPannelCode.Model.FileExportResponse;
import org.gl.ceir.CeirPannelCode.Model.FilterRequest;
import org.gl.ceir.CeirPannelCode.Model.GenricResponse;
import org.gl.ceir.CeirPannelCode.Model.PeriodValidate;
import org.gl.ceir.CeirPannelCode.Model.StockUploadModel;
import org.gl.ceir.CeirPannelCode.Model.UserHeader;
import org.gl.ceir.CeirPannelCode.Model.Usertype;
import org.gl.ceir.CeirPannelCode.Service.RegistrationService;
import org.gl.ceir.CeirPannelCode.Util.HttpResponse;
import org.gl.ceir.CeirPannelCode.Util.UtilDownload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;



@Controller
public class Stock {

	

	private final Logger log = LoggerFactory.getLogger(getClass());
	@Autowired

	FeignCleintImplementation feignCleintImplementation;
	@Autowired
	UtilDownload utildownload;

	UserProfileFeignImpl userProfileFeignImpl;
	@Autowired
	AddMoreFileModel addMoreFileModel,urlToUpload,urlToMove;
	@Autowired
	UserRegistrationFeignImpl userRegistrationFeignImpl;

	@Autowired
    PropertyReader propertyReader;
	@Autowired
	GrievanceFeignClient grievanceFeignClient;
	@Autowired
	RegistrationService registerService;
	
	@RequestMapping(value={"/assignDistributor"},method={org.springframework.web.bind.annotation.RequestMethod.GET,org.springframework.web.bind.annotation.RequestMethod.POST})
	public ModelAndView  viewStock( HttpSession session , @RequestParam(name="userTypeId",required=false) String selectedUserTypeId,@RequestParam(name="selectedRoleTypeId",required=false) Integer selectedRoleTypeId 
			,@RequestParam(name="txnID",required = false) String txnID ,@RequestParam(name="source",required = false,defaultValue = "menu") String source) {
		ModelAndView mv = new ModelAndView();



		log.info("stock page entry point."+selectedRoleTypeId+"==source=="+source);
		try {
			if(selectedUserTypeId==null)
			{
				List<Usertype> userTypelist=(List<Usertype>) session.getAttribute("usertypeList");
				log.info("role type or role type id="+userTypelist);


				if(userTypelist.size()>1)
				{
					log.info("1");
					mv.addObject("userTypelist", userTypelist);
					mv.setViewName("assignDistributor");
				}
				else if(userTypelist.size()==1)
				{
					log.info("2");
					mv.addObject("source",source);
					session.setAttribute("selectedUserTypeId", session.getAttribute("usertype"));
					session.setAttribute("selectedRoleTypeId", session.getAttribute("usertypeId"));
					session.setAttribute("filterSource", source);
					mv.setViewName("ViewStock");
				}
			}
			else {
				log.info("3");
				log.info("selectedUserTypeId=="+selectedUserTypeId);
				log.info("selectedRoleTypeId=="+selectedRoleTypeId);
				session.setAttribute("selectedUserTypeId", selectedUserTypeId);
				session.setAttribute("selectedRoleTypeId", selectedRoleTypeId);
				session.setAttribute("filterSource", source);
				mv.addObject("source",source);
				mv.setViewName("ViewStock");


			}}
		catch (Exception e) {
			// TODO: handle exception
			log.info("this is catch block session is blank or something went wrong.");
		}


		return mv; 
	}



	// *********************************************** open register page or edit page ******************************
	@RequestMapping(value="/openUploadStock",method ={org.springframework.web.bind.annotation.RequestMethod.GET})
	public ModelAndView openRegisterConsignmentForm(@RequestParam(name="reqType") String reqType,@RequestParam(name="txnId",required = false) String txnId)
	{
		ModelAndView mv= new ModelAndView();
		if(reqType.equals("formPage"))
		{
			log.info("open uploadStock form");
			mv.setViewName("uploadStock");
		}
		else if(reqType.equals("editPage")) {
			//ConsignmentModel  consignmentdetails=feignCleintImplementation.fetchConsignmentByTxnId(txnId);
			log.info("consignment details=");

			log.info("open Update registration Consignment form");
			mv.setViewName("editConsignment");
			//mv.addObject("consignmentdetails", consignmentdetails);
		}
		else {
			//ConsignmentModel  consignmentdetails=feignCleintImplementation.fetchConsignmentByTxnId(txnId);
			log.info("consignment details=");

			log.info("open view  registration Consignment form");
			mv.setViewName("viewConsignmentRecord");
			//mv.addObject("consignmentdetails", consignmentdetails);

		}
		return mv;

	}

	@RequestMapping(value= {"/uploadStock"},method={org.springframework.web.bind.annotation.RequestMethod.GET,org.springframework.web.bind.annotation.RequestMethod.POST}) 
	public @ResponseBody GenricResponse registerConsignment(@RequestParam(name="supplierId",required = false) String supplierId,@RequestParam(name="supplierName",required = false) String supplierName
			,@RequestParam(name="invoiceNumber",required = false) String invoiceNumber,@RequestParam(name="quantity",required = false) int quantity,
			@RequestParam(name="file",required = false) MultipartFile file,HttpSession session,@RequestParam(name="deviceQuantity",required = false) int deviceQuantity) {
		GenricResponse response=null;
		String userName=session.getAttribute("username").toString();
		int userId= (int) session.getAttribute("userid");
		//	Long assignerId =(Long) session.getAttribute("userid");
		Long assignerId =((Number) userId).longValue(); 
		//((Number) obj.get("ipInt")).longValue();
		String name=session.getAttribute("name").toString();
		String roletype=session.getAttribute("usertype").toString();
		String selectedRoletype=(String) session.getAttribute("selectedUserTypeId");
		//String selectedUserTypeId=session.getAttribute("selectedUserTypeId").toString();

		log.info("upload stock  entry point.");
		addMoreFileModel.setTag("system_upload_filepath");
		urlToUpload=feignCleintImplementation.addMoreBuutonCount(addMoreFileModel);
		String txnNumner=utildownload.getTxnId();
		txnNumner = "S"+txnNumner;
		FileCopyToOtherServer fileCopyRequest= new FileCopyToOtherServer();
		log.info("Random  genrated transaction number ="+txnNumner);

		StockUploadModel stockUpload= new StockUploadModel();
		try {
			byte[] bytes = file.getBytes();
			String rootPath = urlToUpload.getValue()+txnNumner+"/";
			File dir = new File(rootPath + File.separator);

			if (!dir.exists()) {
				dir.mkdirs();
				dir.setReadable(true,false);
				dir.setWritable(true,false);
				dir.setExecutable(true,false);
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
			fileCopyRequest.setTxnId(txnNumner);
			fileCopyRequest.setFileName(file.getOriginalFilename());
			fileCopyRequest.setServerId(propertyReader.serverId);
			log.info("request passed to move file to other server=="+fileCopyRequest);
			GenricResponse fileRespnose=grievanceFeignClient.saveUploadedFileOnANotherServer(fileCopyRequest);
			log.info("file move api response==="+fileRespnose);


		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			response.setMessage("file is not selected ");
			response.setErrorCode("500");
			log.error(e.getMessage(), e);
			log.info("response after exception="+response);
			return response;
		}
		// set reaquest parameters into model class
		stockUpload.setSupplierId(supplierId);
		stockUpload.setSuplierName(supplierName);
		stockUpload.setInvoiceNumber(invoiceNumber); 
		stockUpload.setQuantity(quantity);
		stockUpload.setTxnId(txnNumner);
		stockUpload.setFileName(file.getOriginalFilename());
		stockUpload.setUserId(userId);
		stockUpload.setRoleType(selectedRoletype);
		stockUpload.setUserType(roletype);
		stockUpload.setAssignerId(assignerId);
		stockUpload.setDeviceQuantity(deviceQuantity);
		log.info("stock form parameters passed to upload stock api "+stockUpload);

		try {
			stockUpload.setPublicIp(session.getAttribute("publicIP").toString());
			stockUpload.setBrowser(session.getAttribute("browser").toString());
			response = feignCleintImplementation.uploadStock(stockUpload);
			log.info("response from upload stock api"+response);
			log.info("upload stock  exit point.");
			return response;
		}
		catch (Exception e) {
			// TODO: handle exception
			log.info("exception==."+response);
			log.info("==========."+e);
			response.setMessage("somtething wrong happend");
			response.setErrorCode("500");
			return response;

		}



	}

	@RequestMapping(value= {"/stockDelete"},method={org.springframework.web.bind.annotation.RequestMethod.GET,org.springframework.web.bind.annotation.RequestMethod.POST}) 
	public @ResponseBody GenricResponse deleteStock(@RequestBody StockUploadModel stockUpload,HttpSession session) {

		log.info("enter in  delete stock.");
		stockUpload.setPublicIp(session.getAttribute("publicIP").toString());
		stockUpload.setBrowser(session.getAttribute("browser").toString());
		log.info("request passed to the deleteStock Api="+stockUpload);
		GenricResponse response=feignCleintImplementation.deleteStock(stockUpload,stockUpload.getUserType());
		log.info("response after delete Stock."+response);
		log.info("exit point of delete stock.");
		return response;


	}

	// *********************************************** open register page or edit popup ******************************
	@RequestMapping(value="/openStockPopup",method ={org.springframework.web.bind.annotation.RequestMethod.GET})
	public @ResponseBody StockUploadModel openRegisterConsignmentPopup(
			@RequestParam(name="reqType") String reqType,@RequestParam(name="txnId",required = false) String txnId,
			@RequestParam(name="role",required = false) String role,
			@RequestParam(name="userType",required = false) String userType,
			@RequestParam(name ="userId", required= false) Integer userId,
			HttpSession session)
	{
		log.info("entry point of  fetch stock in the bases of transaction id ." +userType+" userId-->" +userId+" txnId-->" +txnId);
		//StockUploadModel stockUploadModel= new StockUploadModel();
		StockUploadModel stockUploadModelResponse;
		//stockUploadModel.setTxnId(txnId);
		//stockUploadModel.setRoleType(role);

		FilterRequest filterRequest = new FilterRequest();
		filterRequest.setTxnId(txnId);
		filterRequest.setRoleType(role);
		filterRequest.setUserType(userType);
		filterRequest.setUserId(userId);		
		filterRequest.setPublicIp(session.getAttribute("publicIP").toString());
		filterRequest.setBrowser(session.getAttribute("browser").toString());

		log.info("request passed to the fetch stock api="+filterRequest);
		if(reqType.equals("editPage")) {
			stockUploadModelResponse=feignCleintImplementation.fetchUploadedStockByTxnId(filterRequest);
			log.info("response from fetch stock api="+stockUploadModelResponse);
			return stockUploadModelResponse;
		}
		else {
			stockUploadModelResponse=feignCleintImplementation.fetchUploadedStockByTxnId(filterRequest);
			log.info("response from fetch stock api="+stockUploadModelResponse);
			log.info("exit point of  fetch stock api.");
			return stockUploadModelResponse;
		}

	}

	//************************************************ Open stock record page********************************************************************************/

	@RequestMapping(value= {"/updateUploadedStock"},method={org.springframework.web.bind.annotation.RequestMethod.GET,org.springframework.web.bind.annotation.RequestMethod.POST}) 
	public @ResponseBody GenricResponse openconsignmentRecordPage(@RequestParam(name="supplierId",required = false) String supplierId,@RequestParam(name="supplierName",required = false) String supplierName
			,@RequestParam(name="invoiceNumber",required = false) String invoiceNumber,@RequestParam(name="quantity",required = false) int quantity,
			@RequestParam(name="file",required = false) MultipartFile file,HttpSession session,@RequestParam(name="txnId",required = false) String txnId,@RequestParam(name="filename",required = false) String filename
			,@RequestParam(name="deviceQuantity",required = false) Integer deviceQuantity) {
		log.info("entry point in update Stock * *.");
		StockUploadModel stockUpload= new StockUploadModel();
		addMoreFileModel.setTag("system_upload_filepath");
		urlToUpload=feignCleintImplementation.addMoreBuutonCount(addMoreFileModel);
		FileCopyToOtherServer fileCopyRequest= new FileCopyToOtherServer();
		addMoreFileModel.setTag("uploaded_file_move_path");
		urlToMove=feignCleintImplementation.addMoreBuutonCount(addMoreFileModel);



		String movedFileTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
		log.info("Moved File Time value=="+movedFileTime);


		String roleType=String.valueOf(session.getAttribute("usertype"));
		String userName=session.getAttribute("username").toString();
		int userId=(int) session.getAttribute("userid"); 
		String name=session.getAttribute("name").toString();
		String selectedRoletype=(String) session.getAttribute("selectedUserTypeId");
		GenricResponse response= new GenricResponse();
		if(file==null)
		{
			stockUpload.setSupplierId(supplierId);
			stockUpload.setSuplierName(supplierName);
			stockUpload.setQuantity(quantity);
			stockUpload.setTxnId(txnId);
			stockUpload.setFileName(filename);
			stockUpload.setInvoiceNumber(invoiceNumber);
			stockUpload.setUserId(userId);
			stockUpload.setRoleType(selectedRoletype);
			stockUpload.setUserType(roleType);
			stockUpload.setDeviceQuantity(deviceQuantity);
		}
		else {

			try {	
				log.info("file is not blank");
				String rootPath = urlToUpload.getValue()+txnId+"/";
				File tmpDir = new File(rootPath+file.getOriginalFilename());
				tmpDir.setExecutable(true,false);
				tmpDir.setReadable(true,false);
				tmpDir.setWritable(true,false);
				boolean exists = tmpDir.exists();
				if(exists) {
					Path temp = Files.move 
							(Paths.get(urlToUpload.getValue()+"/"+txnId+"/"+file.getOriginalFilename()), 
									Paths.get(urlToMove.getValue()+movedFileTime+"_"+file.getOriginalFilename())); 

					String movedPath=urlToMove.getValue()+movedFileTime+"_"+file.getOriginalFilename();
					// tmpDir.renameTo(new File("/home/ubuntu/apache-tomcat-9.0.4/webapps/MovedFile/"+txnId+"/"));
					log.info("file is already exist moved to the this "+movedPath+" path");
					tmpDir.delete();
				}
				log.info("file Reader!!!");

				byte[] bytes = file.getBytes();

				File dir = new File(rootPath + File.separator);

				if (!dir.exists()) { 
					dir.mkdirs();
					dir.setExecutable(true,false);
					dir.setReadable(true,false);
					dir.setWritable(true,false);
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
				fileCopyRequest.setTxnId(txnId);
				fileCopyRequest.setFileName(filename);
				fileCopyRequest.setServerId(propertyReader.serverId);
				log.info("request passed to move file to other server=="+fileCopyRequest);
				GenricResponse fileRespnose=grievanceFeignClient.saveUploadedFileOnANotherServer(fileCopyRequest);
				log.info("file move api response==="+fileRespnose);
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				log.error(e.getMessage(), e);
			}

			stockUpload.setSupplierId(supplierId);
			stockUpload.setSuplierName(supplierName);
			stockUpload.setQuantity(quantity);
			stockUpload.setTxnId(txnId);
			stockUpload.setInvoiceNumber(invoiceNumber);
			stockUpload.setFileName(file.getOriginalFilename());
			stockUpload.setUserId(userId);
			stockUpload.setRoleType(selectedRoletype);
			stockUpload.setUserType(roleType);
			stockUpload.setDeviceQuantity(deviceQuantity);
		}
		stockUpload.setPublicIp(session.getAttribute("publicIP").toString());
		stockUpload.setBrowser(session.getAttribute("browser").toString());
		log.info("Request passed to the update register consignment="+stockUpload);
		response = feignCleintImplementation.updateStock(stockUpload);
		log.info(" response from update Consignment api="+response);
		log.info(" update Stock exit point.");
		return response;

	}


	@RequestMapping(value= {"/acceptRejectStockController"},method={org.springframework.web.bind.annotation.RequestMethod.GET,org.springframework.web.bind.annotation.RequestMethod.POST}) 
	public @ResponseBody GenricResponse updateConsignmentStatus(@RequestBody ConsignmentUpdateRequest consignmentUpdateRequest,HttpSession session) {
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
		request.setPublicIp(session.getAttribute("publicIP").toString());
		request.setBrowser(session.getAttribute("browser").toString());;
		log.info(" request passed to the stock accept reject  api="+request);
		GenricResponse response=feignCleintImplementation.acceptRejectStock(request);
		log.info("response after stock accept reject  api="+response);
		return response;

	}

	//***************************************** Export Grievance controller *********************************
	@RequestMapping(value="/exportStock",method ={org.springframework.web.bind.annotation.RequestMethod.GET})
	public String exportToExcel(@RequestParam(name="stockStartDate",required = false) String stockStartDate,
			@RequestParam(name="stockEndDate",required = false) String stockEndDate,
			@RequestParam(name="stockTxnId",required = false) String stockTxnId,
			@RequestParam(name="StockStatus") Integer StockStatus,
			@RequestParam(name="userType") String userType,
			@RequestParam(name="userTypeId") Integer userTypeId,
			HttpServletRequest request,
			HttpSession session,
			@RequestParam(name="pageSize") Integer pageSize,
			@RequestParam(name="pageNo") Integer pageNo,
			@RequestParam(name="roleType") String roleType,
			@RequestParam(name="source")String source,
			@RequestParam(name="filteredUserType")String filteredUserType,
			@RequestParam(name="filterUserName")String filterUserName,
			@RequestParam(name="fileName")String fileName,
			@RequestParam(name="deviceQuantity")String deviceQuantity,
			@RequestParam(name="quantity")String quantity)
	{
		log.info("stockStartDate=="+stockStartDate+ " stockEndDate ="+stockEndDate+" stockTxnId="+stockTxnId+"StockStatus="+stockTxnId+"userType="+userType+"userTypeId="+userTypeId);
		int userId= (int) session.getAttribute("userid"); 
		String userName=session.getAttribute("username").toString();
		int file=1;
		FileExportResponse fileExportResponse;
		FilterRequest filterRequest= new FilterRequest();
		filterRequest.setStartDate(stockStartDate);
		filterRequest.setEndDate(stockEndDate);
		filterRequest.setTxnId(stockTxnId);
		filterRequest.setConsignmentStatus(StockStatus);
		filterRequest.setUserId(userId);
		filterRequest.setRoleType(roleType);
		filterRequest.setUserType(userType);
		filterRequest.setUserTypeId(userTypeId);
		filterRequest.setFeatureId(4);
		filterRequest.setUsername(userName);
		filterRequest.setUserName(userName);
		filterRequest.setFilteredUserType(filteredUserType);
		filterRequest.setFilterUserName(filterUserName);
		filterRequest.setDisplayName(filterUserName);
		filterRequest.setFileName(fileName);
		filterRequest.setDeviceQuantity(deviceQuantity);
		filterRequest.setQuantity(quantity);
		log.info("source=="+source);;
		filterRequest.setPublicIp(session.getAttribute("publicIP").toString());
		filterRequest.setBrowser(session.getAttribute("browser").toString());
		log.info(" request passed to the stock exportTo Excel Api =="+filterRequest+" *********** pageSize"+pageSize+"  pageNo  "+pageNo);
		Object	response= feignCleintImplementation.stockFilter(filterRequest, pageNo, pageSize, file,source);

		Gson gson= new Gson(); 
		String apiResponse = gson.toJson(response);
		fileExportResponse = gson.fromJson(apiResponse, FileExportResponse.class);
		log.info("response  from   export stock  api="+fileExportResponse);

		return "redirect:"+fileExportResponse.getUrl();
	}



	@RequestMapping(value={"/uploadAstock"},method={org.springframework.web.bind.annotation.RequestMethod.GET,org.springframework.web.bind.annotation.RequestMethod.POST})
	public  ModelAndView openEndUserGrievancePage(@RequestParam(name="reportType") Integer reportType) 
	{

		HttpResponse httpResponse=userRegistrationFeignImpl.periodValidate(new PeriodValidate(17L, 4L));
		ModelAndView mv = new ModelAndView();	 
		log.info(" view End user Stock entry point."+reportType+"::::httpResponse:::::"+httpResponse); 
		if(httpResponse.getStatusCode() == 200) {
			mv.addObject("showPagetype", reportType);
			mv.setViewName("endUserStock");
			log.info(" view End user stock exit point."); 
		}
		else {
			
			mv.setViewName("registrationPopup");
		}
		return mv; 
	}


	@ResponseBody
	@PostMapping("ednUserStockUpload")
	public GenricResponse ednUserStockUpload(@RequestParam(name="file",required = false) MultipartFile file,HttpServletRequest request,HttpSession session) {
		log.info("-inside  end user stock upload--");

		/*
		 * String userName=session.getAttribute("username").toString(); String userId=
		 * session.getAttribute("userid").toString(); String
		 * name=session.getAttribute("name").toString();
		 */
		String filter = request.getParameter("request");

		log.info(" end user stock entry point.");
		String txnNumber="S" + utildownload.getTxnId();
		log.info("Random transaction id number="+txnNumber);
		addMoreFileModel.setTag("system_upload_filepath");
		urlToUpload=feignCleintImplementation.addMoreBuutonCount(addMoreFileModel);
		FileCopyToOtherServer fileCopyRequest= new FileCopyToOtherServer();

		log.info("file is not blank");


		Gson gson= new Gson(); 

		log.info("*********"+filter);

		StockUploadModel endUserStockModal  = gson.fromJson(filter, StockUploadModel.class);
		endUserStockModal.setFileName(file.getOriginalFilename());
		endUserStockModal.setTxnId(txnNumber);
		try { byte[] bytes = file.getBytes();
		String rootPath =urlToUpload.getValue()+txnNumber+"/"; 
		File dir = new File(rootPath + File.separator);

		if (!dir.exists()) { dir.mkdirs();
		dir.setExecutable(true,false);
		dir.setReadable(true,false);
		dir.setWritable(true,false);
		// Create the file on server
		}
		File serverFile = new File(rootPath+file.getOriginalFilename());
		log.info("uploaded file path on server" + serverFile); BufferedOutputStream
		stream = new BufferedOutputStream(new FileOutputStream(serverFile));
		serverFile.setExecutable(true,false);
		serverFile.setReadable(true,false);
		serverFile.setWritable(true,false);
		stream.write(bytes); 
		stream.close();


		fileCopyRequest.setFilePath(rootPath);
		fileCopyRequest.setTxnId(txnNumber);
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
		UserHeader header=registerService.getUserHeaders(request);
		endUserStockModal.setPublicIp(header.getPublicIp());
		endUserStockModal.setBrowser(header.getBrowser());
		log.info("request passed to  the end user upload stock api"+endUserStockModal);
		GenricResponse  response = new GenricResponse();
		response = feignCleintImplementation.uploadStock(endUserStockModal);
		/*
		 * response.setTxnId(txnNumber); response.setMessage("saved sucess full");
		 * response.setErrorCode("0");
		 */
		log.info("---------response--------"+response);
		return response;
	}


	// *********************************************** open register page or edit popup ******************************
	@RequestMapping(value="/fetchUploadAstock",method ={org.springframework.web.bind.annotation.RequestMethod.GET})
	public @ResponseBody StockUploadModel openEndUserStockPopup(@RequestParam(name="txnId",required = false) String txnId,HttpServletRequest request)
	{
		log.info("entry point of  fetch end user stock in the bases of transaction id .");
		//StockUploadModel stockUploadModel= new StockUploadModel();
		FilterRequest filterRequest = new FilterRequest();
		StockUploadModel stockUploadModelResponse;
		filterRequest.setTxnId(txnId);
		filterRequest.setFeatureId(4);
		filterRequest.setUserType("End User");
		filterRequest.setUserId(17);
		UserHeader header=registerService.getUserHeaders(request);
		filterRequest.setPublicIp(header.getPublicIp());
		filterRequest.setBrowser(header.getBrowser());
		log.info("response from fetch stock api="+filterRequest);
		filterRequest.setUserType("End User");
		stockUploadModelResponse=feignCleintImplementation.fetchUploadedStockByTxnId(filterRequest);
		log.info("response from fetch stock api="+stockUploadModelResponse);
		log.info("exit point of  fetch stock api.");
		return stockUploadModelResponse;


	}

	@RequestMapping(value= {"/updateUploadedAstock"},method={org.springframework.web.bind.annotation.RequestMethod.GET,org.springframework.web.bind.annotation.RequestMethod.POST}) 
	public @ResponseBody GenricResponse updateEndUserUploadedStock(@RequestParam(name="file") MultipartFile file,@RequestParam(name="txnId",required = false) String txnId) {
		log.info("entry point in end user update Stock * *.");
		StockUploadModel stockUpload= new StockUploadModel();


		GenricResponse response= new GenricResponse();
		stockUpload.setUserType("End User");
		addMoreFileModel.setTag("system_upload_filepath");
		urlToUpload=feignCleintImplementation.addMoreBuutonCount(addMoreFileModel);

		addMoreFileModel.setTag("uploaded_file_move_path");
		urlToMove=feignCleintImplementation.addMoreBuutonCount(addMoreFileModel);

		log.info("file is not blank");
		FileCopyToOtherServer fileCopyRequest= new FileCopyToOtherServer();
		fileCopyRequest.setFilePath(urlToUpload.getValue());
		fileCopyRequest.setTxnId(txnId);
		fileCopyRequest.setFileName(file.getOriginalFilename());
		fileCopyRequest.setServerId(propertyReader.serverId);
		log.info("request passed to move file to other server=="+fileCopyRequest);
		GenricResponse fileRespnose=grievanceFeignClient.saveUploadedFileOnANotherServer(fileCopyRequest);
		log.info("file move api response==="+fileRespnose);
		try {
			log.info("file is not blank");
			String rootPath = urlToUpload.getValue()+txnId+"/";
			File tmpDir = new File(rootPath+file.getOriginalFilename());
			tmpDir.setExecutable(true,false);
			tmpDir.setReadable(true,false);
			tmpDir.setWritable(true,false);
			boolean exists = tmpDir.exists();
			if(exists) {
				Path temp = Files.move 
						(Paths.get(urlToUpload.getValue()+"/"+txnId+"/"+file.getOriginalFilename()), 
								Paths.get(urlToMove.getValue()+file.getOriginalFilename())); 

				String movedPath=urlToMove.getValue()+file.getOriginalFilename();
				// tmpDir.renameTo(new File("/home/ubuntu/apache-tomcat-9.0.4/webapps/MovedFile/"+txnId+"/"));
				log.info("file is already exist moved to the this "+movedPath+" path");
				tmpDir.delete();
			}
			log.info("file Reader!!!");

			byte[] bytes = file.getBytes();

			File dir = new File(rootPath + File.separator);

			if (!dir.exists()) 
			{
				dir.mkdirs();
				dir.setExecutable(true,false);
				dir.setReadable(true,false);
				dir.setWritable(true,false);
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
			log.error(e.getMessage(), e);
		}


		stockUpload.setTxnId(txnId);
		stockUpload.setFileName(file.getOriginalFilename());


		log.info("Request passed to the end user  update stock ="+stockUpload);
		response = feignCleintImplementation.updateStock(stockUpload);
		log.info(" response from end user update stock api="+response);
		log.info(" update end user Stock exit point.");
		return response;

	}
	/*
	 * @RequestMapping(value=
	 * {"/fetchAssigneDetails"},method={org.springframework.web.bind.annotation.
	 * RequestMethod.GET,org.springframework.web.bind.annotation.RequestMethod.POST}
	 * ) public @ResponseBody List<UploadStockAssigneModal>
	 * fetchAssigneDetails(HttpServletRequest request,HttpSession session) {
	 * 
	 * log.info("enter in  fetch asigne detals ."); String filter =
	 * request.getParameter("request"); Gson gson= new Gson();
	 * log.info("*********"+filter);
	 * 
	 * AssigneRequestType assigneRequest = gson.fromJson(filter,
	 * AssigneRequestType.class); List<UploadStockAssigneModal> response = new
	 * ArrayList<UploadStockAssigneModal>();
	 * 
	 * log.info("request passed to the fetch api=="+assigneRequest); //String
	 * roleType=(String) session.getAttribute("usertype"); //String
	 * selectedRoletype=(String) session.getAttribute("selectedUserTypeId");
	 * //Object ob= userProfileFeignImpl.fetchAssignedetails(assigneRequest); //
	 * response=userProfileFeignImpl.fetchAssignedetails(assigneRequest);
	 * //log.info("response after delete Stock."+ob); response.add(new
	 * UploadStockAssigneModal("FLHF0071K","FLHF0071K",null,null)); response.add(new
	 * UploadStockAssigneModal("FLHF0071K","FLHF0071K",null,null));
	 * 
	 * 
	 * 
	 * log.info("exit point fetch asigne details."+response); return response;
	 * 
	 * 
	 * }
	 */
}
