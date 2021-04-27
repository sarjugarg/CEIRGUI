package org.gl.ceir.CeirPannelCode.Controller;


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.gl.ceir.CeirPannelCode.PropertyReader;
import org.gl.ceir.CeirPannelCode.Feignclient.DBTablesFeignClient;
import org.gl.ceir.CeirPannelCode.Feignclient.FeignCleintImplementation;
import org.gl.ceir.CeirPannelCode.Feignclient.GrievanceFeignClient;
import org.gl.ceir.CeirPannelCode.Feignclient.UserProfileFeignImpl;
import org.gl.ceir.CeirPannelCode.Model.AddMoreFileModel;
import org.gl.ceir.CeirPannelCode.Model.ConsignmentModel;
import org.gl.ceir.CeirPannelCode.Model.ConsignmentUpdateRequest;
import org.gl.ceir.CeirPannelCode.Model.DBrowDataModel;
import org.gl.ceir.CeirPannelCode.Model.DbListDataHeaders;
import org.gl.ceir.CeirPannelCode.Model.Dropdown;
import org.gl.ceir.CeirPannelCode.Model.FileCopyToOtherServer;
import org.gl.ceir.CeirPannelCode.Model.FileExportResponse;
import org.gl.ceir.CeirPannelCode.Model.FilterRequest;
import org.gl.ceir.CeirPannelCode.Model.GenricResponse;
import org.gl.ceir.CeirPannelCode.Model.MapDatatableResponse;
import org.gl.ceir.CeirPannelCode.Model.PaymentRequest;
import org.gl.ceir.CeirPannelCode.Service.ConsignmentService;
import org.gl.ceir.CeirPannelCode.Util.UtilDownload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

//@RequestMapping(value="/Consignment")
@Controller

public class Consignment {




	@Autowired
	GrievanceFeignClient grievanceFeignClient;


	@Autowired
	AddMoreFileModel addMoreFileModel,urlToUpload,urlToMove;


	@Autowired
	UserProfileFeignImpl userProfileFeignImpl;

	@Autowired

	FeignCleintImplementation feignCleintImplementation;
	@Autowired
	UtilDownload utildownload;
	@Autowired
	DBTablesFeignClient dBTablesFeignClient;
	@Autowired
	DBrowDataModel dBrowDataModel;

	@Autowired
	PropertyReader propertyReader;
	private final Logger log = LoggerFactory.getLogger(getClass());


	@RequestMapping(value=
		{"/viewConsignment"},method={org.springframework.web.bind.annotation.
				RequestMethod.GET,org.springframework.web.bind.annotation.RequestMethod.POST})
	public ModelAndView viewConsignment(HttpSession session,@RequestParam(name="txnID",required = false) String txnID,@RequestParam(name="source",defaultValue = "menu",required = false) String source) {
		ModelAndView mv = new ModelAndView();

		log.info(" view consignment entry point................."); 
		mv.setViewName("viewConsignment");
		log.info(" view consignment exit point."); 
		return mv; 
	}




	@RequestMapping(value= {"/registerConsignment"},method={org.springframework.web.bind.annotation.RequestMethod.GET,org.springframework.web.bind.annotation.RequestMethod.POST}) 
	public @ResponseBody GenricResponse registerConsignment(@RequestParam(name="supplierId",required = false) String supplierId,@RequestParam(name="supplierName",required = false) String supplierName
			,@RequestParam(name="consignmentNumber",required = false) String consignmentNumber,@RequestParam(name="expectedArrivaldate",required = false) String expectedArrivalDate,
			@RequestParam(name="organisationcountry",required = false) String organisationcountry,@RequestParam(name="expectedDispatcheDate",required = false) String expectedDispatcheDate,
			@RequestParam(name="expectedArrivalPort",required = false) Integer expectedArrivalPort,@RequestParam(name="quantity",required = false) String quantity,
			@RequestParam(name="file",required = false) MultipartFile file,HttpSession session,@RequestParam(name="totalPrice",required = false) String totalPrice,@RequestParam(name="currency",required = false) Integer currency,
			@RequestParam(name="userType",required = false) String userType,
			@RequestParam(name="userTypeId",required = false) Integer userTypeId,
			@RequestParam(name="portAddress",required = false) Integer portAddress,
			@RequestParam(name="deviceQuantity",required = false) Integer deviceQuantity,
			@RequestParam(name="featureId",required = false) Integer featureId,
			@RequestParam(name="roleType",required = false) String roleType,HttpServletRequest request) {

		FileCopyToOtherServer fileCopyRequest= new FileCopyToOtherServer();
		log.info("headers request="+request.getHeaderNames());
		log.info("user-agent"+request.getHeader("user-agent"));
		Enumeration headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String value = request.getHeader(key);
			log.info("request headers value="+key+" : "+value);
		}

		String userName=session.getAttribute("username").toString();
		String userId= session.getAttribute("userid").toString();
		String name=session.getAttribute("name").toString();
		log.info(" Register consignment entry point.");
		String txnNumner=utildownload.getTxnId();
		txnNumner = "C"+txnNumner;

		addMoreFileModel.setTag("system_upload_filepath");
		urlToUpload=feignCleintImplementation.addMoreBuutonCount(addMoreFileModel);



		log.info("url to upload file=="+urlToUpload.getValue());
		log.info("Random transaction id number="+txnNumner);
		ConsignmentModel consignment = new ConsignmentModel();
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
		}
		// set request parameters into model class
		consignment.setSupplierId(supplierId);
		consignment.setSupplierName(supplierName);
		consignment.setConsignmentNumber(consignmentNumber);
		consignment.setExpectedDispatcheDate(expectedDispatcheDate);
		consignment.setExpectedArrivaldate(expectedArrivalDate);
		consignment.setOrganisationCountry(organisationcountry);
		consignment.setExpectedArrivalPort(expectedArrivalPort);
		consignment.setQuantity(quantity);
		consignment.setTxnId(txnNumner);
		consignment.setFileName(file.getOriginalFilename());
		consignment.setUserId(Long.valueOf(userId));
		consignment.setUserName(userName);
		consignment.setFeatureId(featureId);
		consignment.setUserType(userType);
		consignment.setUserTypeId(userTypeId);
		consignment.setCurrency(currency);
		consignment.setTotalPrice(totalPrice);
		consignment.setPortAddress(portAddress);
		consignment.setDeviceQuantity(deviceQuantity);
		consignment.setRoleType(roleType);
		consignment.setPublicIp(session.getAttribute("publicIP").toString());
		consignment.setBrowser(session.getAttribute("browser").toString());
		log.info("consignment form parameters passed to register consignment api "+consignment.toString());
		GenricResponse response = feignCleintImplementation.addConsignment(consignment);
		log.info("response from register consignment api"+response.toString());
		return response;


	}



	//************************************************ update consignment record page********************************************************************************/

	@RequestMapping(value= {"/updateRegisterConsignment"},method={org.springframework.web.bind.annotation.RequestMethod.GET,org.springframework.web.bind.annotation.RequestMethod.POST}) 
	public @ResponseBody GenricResponse openconsignmentRecordPage(@RequestParam(name="supplierId",required = false) String supplierId,@RequestParam(name="supplierName",required = false) String supplierName
			,@RequestParam(name="consignmentNumber",required = false) String consignmentNumber,@RequestParam(name="expectedArrivaldate",required = false) String expectedArrivalDate,
			@RequestParam(name="organisationcountry",required = false) String organisationcountry,@RequestParam(name="expectedDispatcheDate",required = false) String expectedDispatcheDate,
			@RequestParam(name="expectedArrivalPort",required = false) Integer expectedArrivalPort,@RequestParam(name="quantity",required = false) String quantity, HttpSession session,
			@RequestParam(name="file",required = false) MultipartFile file,@RequestParam(name="filename",required = false) String filename,@RequestParam(name="txnId",required = false) String txnId,
			@RequestParam(name="totalPrice",required = false) String totalPrice,@RequestParam(name="currency",required = false) Integer currency,
			@RequestParam(name="userType",required = false) String userType,
			@RequestParam(name="userTypeId",required = false) Integer userTypeId,
			@RequestParam(name="portAddress",required = false) Integer portAddress,
			@RequestParam(name="featureId",required = false) Integer featureId,
			@RequestParam(name="deviceQuantity",required = false) Integer deviceQuantity,
			@RequestParam(name="roleType",required = false) String roleType) 
	{
		ConsignmentModel consignment = new ConsignmentModel();
		FileCopyToOtherServer fileCopyRequest= new FileCopyToOtherServer();
		String movedFileTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
		log.info("Moved File Time value=="+movedFileTime);

		String userName=session.getAttribute("username").toString();
		String userId= session.getAttribute("userid").toString();
		String name=session.getAttribute("name").toString();

		GenricResponse response= new GenricResponse();

		addMoreFileModel.setTag("system_upload_filepath");
		urlToUpload=feignCleintImplementation.addMoreBuutonCount(addMoreFileModel);


		addMoreFileModel.setTag("uploaded_file_move_path");
		urlToMove=feignCleintImplementation.addMoreBuutonCount(addMoreFileModel);
		log.info("moved file path from api="+urlToMove.getValue());

		log.info("entry point in update Consignment.");
		if(file==null)
		{
			consignment.setSupplierId(supplierId);
			consignment.setSupplierName(supplierName);
			consignment.setConsignmentNumber(consignmentNumber);
			consignment.setExpectedDispatcheDate(expectedDispatcheDate);
			consignment.setExpectedArrivaldate(expectedArrivalDate);
			consignment.setOrganisationCountry(organisationcountry);
			consignment.setExpectedArrivalPort(expectedArrivalPort);
			consignment.setQuantity(quantity);
			consignment.setTxnId(txnId);
			consignment.setFileName(filename);
			consignment.setUserId(Long.valueOf(userId));
			consignment.setCurrency(currency);
			consignment.setTotalPrice(totalPrice);
			consignment.setUserName(userName);
			consignment.setFeatureId(featureId);
			consignment.setUserType(userType);
			consignment.setUserTypeId(userTypeId);
			consignment.setPortAddress(portAddress);
			consignment.setDeviceQuantity(deviceQuantity);
			consignment.setRoleType(roleType);
		}
		else {
			log.info("file is empty or not "+file.isEmpty());
			try {

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

					log.info("file is already exist, moved to this "+movedFileTime+"_"+movedPath+" path. ");
					tmpDir.delete();
				}
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
				fileCopyRequest.setFileName(file.getOriginalFilename());
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


			log.info("message after file upload block ");

			consignment.setSupplierId(supplierId);
			consignment.setSupplierName(supplierName);
			consignment.setConsignmentNumber(consignmentNumber);
			consignment.setExpectedDispatcheDate(expectedDispatcheDate);
			consignment.setExpectedArrivaldate(expectedArrivalDate);
			consignment.setOrganisationCountry(organisationcountry);
			consignment.setExpectedArrivalPort(expectedArrivalPort);
			consignment.setQuantity(quantity);
			consignment.setTxnId(txnId);
			consignment.setFileName(filename);
			consignment.setUserId(Long.valueOf(userId));
			consignment.setCurrency(currency);
			consignment.setPortAddress(portAddress);
			consignment.setTotalPrice(totalPrice);
			consignment.setFeatureId(featureId);
			consignment.setUserType(userType);
			consignment.setUserTypeId(userTypeId);
			consignment.setPortAddress(portAddress);
			consignment.setDeviceQuantity(deviceQuantity);
			consignment.setRoleType(roleType);
		}
		consignment.setPublicIp(session.getAttribute("publicIP").toString());
		consignment.setBrowser(session.getAttribute("browser").toString());
		log.info("Request passed to the update register consignment="+consignment.toString());
		response = feignCleintImplementation.updateConsignment(consignment);
		log.info(" response from update Consignment api="+response);
		return response;

	}





	//************************************************ delete consignment record page********************************************************************************/

	@RequestMapping(value= {"/deleteConsignment"},method={org.springframework.web.bind.annotation.RequestMethod.GET,org.springframework.web.bind.annotation.RequestMethod.POST}) 
	public @ResponseBody GenricResponse deleteConsignment(@RequestBody ConsignmentModel consignmentModel,HttpSession session) {

		log.info("enter in delete consignment.");
		String userType=(String) session.getAttribute("usertype");

		consignmentModel.setPublicIp(session.getAttribute("publicIP").toString());
		consignmentModel.setBrowser(session.getAttribute("browser").toString());
		log.info("request passed to the deleteConsignment Api="+consignmentModel);
		GenricResponse response=feignCleintImplementation.deleteConsignment(consignmentModel,userType);
		log.info("response after delete consignment."+response);
		return response;

	}



	//*********************************************** update consignment Status *******************************************************************************/

	@RequestMapping(value= {"/updateConsignmentStatus"},method={org.springframework.web.bind.annotation.RequestMethod.GET,org.springframework.web.bind.annotation.RequestMethod.POST}) 
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
		request.setUserName(consignmentUpdateRequest.getUserName());
		request.setUserType(consignmentUpdateRequest.getUserType());
		request.setUserTypeId(consignmentUpdateRequest.getUserTypeId());
		request.setFeatureId(consignmentUpdateRequest.getFeatureId());
		request.setRoleType(consignmentUpdateRequest.getRoleType());
		
		request.setPublicIp(session.getAttribute("publicIP").toString());
		request.setBrowser(session.getAttribute("browser").toString());
		log.info(" request passed to the update consignment status="+request);
		GenricResponse response=feignCleintImplementation.updateConsignmentStatus(request);
		log.info("response after update consignment status="+response);
		return response;

	}




	// ********************************************** open register page or edit page *****************************
	@RequestMapping(value="/openRegisterConsignmentForm",method ={org.springframework.web.bind.annotation.RequestMethod.GET})
	public ModelAndView openRegisterConsignmentForm(@RequestParam(name="reqType") String reqType,@RequestParam(name="txnId",required = false) String txnId)
	{
		ModelAndView mv= new ModelAndView();
		if(reqType.equals("formPage"))
		{
			log.info("open registration Consignment form");
			mv.setViewName("registerConsignment");
		}
		/*
		 * else if(reqType.equals("editPage")) { ConsignmentModel
		 * consignmentdetails=feignCleintImplementation.fetchConsignmentByTxnId(txnId);
		 * log.info("consignment details="+consignmentdetails);
		 * 
		 * log.info("open Update registration Consignment form");
		 * mv.setViewName("editConsignment"); mv.addObject("consignmentdetails",
		 * consignmentdetails); } else { ConsignmentModel
		 * consignmentdetails=feignCleintImplementation.fetchConsignmentByTxnId(txnId);
		 * log.info("consignment details="+consignmentdetails);
		 * log.info("open view registration Consignment form");
		 * mv.setViewName("viewConsignmentRecord"); mv.addObject("consignmentdetails",
		 * consignmentdetails);
		 * 
		 * }
		 */
		return mv;

	}

	// ********************************************** open register page or edit popup *****************************
	@RequestMapping(value="/openRegisterConsignmentPopup",method ={org.springframework.web.bind.annotation.RequestMethod.GET})
	public @ResponseBody ConsignmentModel openRegisterConsignmentPopup(@RequestParam(name="reqType") String reqType,@RequestParam(name="txnId",required = false) String txnId,HttpSession session)
	{
		String userName=session.getAttribute("username").toString();
		int userId= (int) session.getAttribute("userid");
		String roletype=session.getAttribute("usertype").toString();
		int userTypeId =(int) session.getAttribute("usertypeId");

		ConsignmentModel consignmentdetails= new ConsignmentModel();
		FilterRequest filterRequest= new  FilterRequest();
		filterRequest.setTxnId(txnId);
		filterRequest.setUserId(userId);
		filterRequest.setUserName(userName);
		filterRequest.setUsername(userName);
		filterRequest.setUserTypeId(userTypeId);
		filterRequest.setUserType(roletype);
		filterRequest.setFeatureId(3);
		filterRequest.setRoleType(roletype);
		
		filterRequest.setPublicIp(session.getAttribute("publicIP").toString());
		filterRequest.setBrowser(session.getAttribute("browser").toString());
		/*
		 * String txnId Long userId, String userName, Long userTypeId, String userType,
		 * Long featureId, Long roleType
		 */
		if(reqType.equals("editPage")) {
			log.info("request  passed to the fetch consignment api="+filterRequest);
			consignmentdetails=feignCleintImplementation.fetchConsignmentByTxnId(filterRequest);
			log.info(" response from fetch consignment api ="+consignmentdetails);


		}
		else {
			log.info("request  passed to the fetch consignment api="+filterRequest);
			consignmentdetails=feignCleintImplementation.fetchConsignmentByTxnId(filterRequest);
			log.info(" response from fetch consignment api ="+consignmentdetails);
		}
		return consignmentdetails;

	}


	//***********************************************cuurency controller *************************************************
	@RequestMapping(value="/consignmentCurency",method={org.springframework.web.bind.annotation.RequestMethod.GET}) 
	public @ResponseBody List<Dropdown> cuurencyforRegisterConsignment(@RequestParam("CURRENCY") String currency)  {
		log.info("request send to the currency  api="+currency);
		List<Dropdown> response= new ArrayList<Dropdown>();
		response=feignCleintImplementation.taxPaidStatusList(currency);
		log.info("response from currency api "+response);
		return response;

	}

	//***************************************** Export Grievance controller *********************************
	@RequestMapping(value="/exportConsignmnet",method ={org.springframework.web.bind.annotation.RequestMethod.GET})
	public String exportToExcel(@RequestParam(name="consignmentStartDate",required = false) String consignmentStartDate,@RequestParam(name="consignmentEndDate",required = false) String consignmentEndDate,
			@RequestParam(name="consignmentTxnId",required = false) String consignmentTxnId,@RequestParam(name="consignmentTaxPaidStatus") Integer consignmentTaxPaidStatus,HttpServletRequest request,
			HttpSession session,@RequestParam(name="pageSize") Integer pageSize,@RequestParam(name="pageNo") Integer pageNo,@RequestParam(name="filterConsignmentStatus") Integer filterConsignmentStatus,
			@RequestParam(name="displayName",required = false) String displayName,@RequestParam(name="source",defaultValue = "menu",required = false) String source,
			@RequestParam(name="deviceQuantity",required = false) String deviceQuantity,@RequestParam(name="quantity",required = false) String imeiQuantity,@RequestParam(name="supplierName",required = false) String supplierName)
	{
		log.info("consignmentStartDate=="+consignmentStartDate+ " consignmentEndDate ="+consignmentEndDate+" consignmentTxnId="+consignmentTxnId+"consignmentTaxPaidStatus="+consignmentTaxPaidStatus+" filterConsignmentStatus="+filterConsignmentStatus);
		log.info("source--->" +source);
		int userId= (int) session.getAttribute("userid"); 
		int file=1;
		String userType=(String) session.getAttribute("usertype");
		Integer usertypeId=(int) session.getAttribute("usertypeId");
		String userName=session.getAttribute("username").toString();
		FileExportResponse fileExportResponse;
		FilterRequest filterRequest= new FilterRequest();
		filterRequest.setStartDate(consignmentStartDate);
		filterRequest.setEndDate(consignmentEndDate);
		filterRequest.setTxnId(consignmentTxnId);
		filterRequest.setTaxPaidStatus(consignmentTaxPaidStatus);
		filterRequest.setConsignmentStatus(filterConsignmentStatus);
		filterRequest.setUserId(userId);
		filterRequest.setUserType(userType);
		filterRequest.setUserTypeId(usertypeId);
		filterRequest.setFeatureId(3);
		filterRequest.setRoleType(userType);
		filterRequest.setUsername(userName);
		filterRequest.setUserName(userName);
		filterRequest.setDisplayName(displayName);
		filterRequest.setDeviceQuantity(deviceQuantity);
		filterRequest.setQuantity(imeiQuantity);
		filterRequest.setSupplierName(supplierName);
		filterRequest.setPublicIp(session.getAttribute("publicIP").toString());
		filterRequest.setBrowser(session.getAttribute("browser").toString());
		log.info(" request passed to the exportTo Excel Api =="+filterRequest+" *********** pageSize"+pageSize+"  pageNo  "+pageNo);
		Object	response= feignCleintImplementation.consignmentFilter(filterRequest, pageNo, pageSize, file,source);

		Gson gson= new Gson(); 
		String apiResponse = gson.toJson(response);
		fileExportResponse = gson.fromJson(apiResponse, FileExportResponse.class);
		log.info("response  from   export consignment  api="+fileExportResponse);

		return "redirect:"+fileExportResponse.getUrl();
	}





	@PostMapping("/payTax") 
	public @ResponseBody GenricResponse payConsignmentTax (@RequestBody PaymentRequest paymentRequest)  {
		log.info("request send to the payConsignmentTax api="+paymentRequest);
		GenricResponse response= userProfileFeignImpl.consignmentTaxFeign(paymentRequest);
		log.info("response from payConsignmentTax api "+response);
		return response;
	}	

}