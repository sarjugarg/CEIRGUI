package org.gl.ceir.CeirPannelCode.Controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.gl.ceir.CeirPannelCode.PropertyReader;
import org.gl.ceir.CeirPannelCode.Feignclient.DBTablesFeignClient;
import org.gl.ceir.CeirPannelCode.Feignclient.FeignCleintImplementation;
import org.gl.ceir.CeirPannelCode.Feignclient.GrievanceFeignClient;
import org.gl.ceir.CeirPannelCode.Feignclient.UserProfileFeignImpl;
import org.gl.ceir.CeirPannelCode.Model.AddMoreFileModel;
import org.gl.ceir.CeirPannelCode.Model.AllRequest;
import org.gl.ceir.CeirPannelCode.Model.DBrowDataModel;
import org.gl.ceir.CeirPannelCode.Model.DbListDataHeaders;
import org.gl.ceir.CeirPannelCode.Model.FileExportResponse;
import org.gl.ceir.CeirPannelCode.Model.MapDatatableResponse;
import org.gl.ceir.CeirPannelCode.Model.UserHeader;
import org.gl.ceir.CeirPannelCode.Service.RegistrationService;
import org.gl.ceir.CeirPannelCode.Util.UtilDownload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


import com.google.gson.Gson;

@RequestMapping(value = "/Consignment")
@Controller
public class FileAndHistoryController {
@Autowired
PropertyReader propertyReader;
	@Autowired
	GrievanceFeignClient grievanceFeignClient;

	@Autowired
	AddMoreFileModel addMoreFileModel, urlToUpload, urlToMove;

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
	RegistrationService registerService;
	
	private final Logger log = LoggerFactory.getLogger(getClass());

	// ************************************************* download file
	// ***************************************************************

	@RequestMapping(value = "/dowloadFiles/{filetype}/{fileName}/{transactionNumber}/{doc_TypeTag}", method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	// @RequestMapping(value="/dowloadFiles/{filetype}/{fileName}/{transactionNumber}",method={org.springframework.web.bind.annotation.RequestMethod.GET},
	// headers = {"content-Disposition=attachment"})

	public @ResponseBody FileExportResponse downloadFile(@PathVariable("transactionNumber") String txnid,
			@PathVariable("fileName") String fileName, @PathVariable("filetype") String filetype,
			@PathVariable(name = "doc_TypeTag", required = false) String doc_TypeTag,HttpSession session,HttpServletRequest request) throws IOException {

		FileExportResponse response = new FileExportResponse();
		log.info("inside file download method" + doc_TypeTag);

		addMoreFileModel.setTag("system_upload_filepath");
		 
		urlToUpload = feignCleintImplementation.addMoreBuutonCount(addMoreFileModel);
		log.info("url to download file==" + urlToUpload.getValue());
		   AllRequest allrequest= new AllRequest();
		   UserHeader header=registerService.getUserHeaders(request);
		if(session.getAttribute("usertypeId")==null || session.getAttribute("usertype").equals(null) || session.getAttribute("username").equals(null) || session.getAttribute("userid").equals(null)) 
		{
			allrequest.setUserTypeId(17);
			allrequest.setUserType("End User");
			allrequest.setUserId(0123);
			allrequest.setUsername("End User");
		}
		else {
			allrequest.setUserTypeId((int)  session.getAttribute("usertypeId"));
			allrequest.setUserType(String.valueOf(session.getAttribute("usertype")));
			allrequest.setUserId((int) session.getAttribute("userid"));
			allrequest.setUsername(session.getAttribute("username").toString());
		}
		 String txnIDfirstLetter = txnid.substring(0, 1);
		 if(txnIDfirstLetter.equalsIgnoreCase("C")) {
			 allrequest.setFeatureId(3);
		 }
		 else if(txnIDfirstLetter.equalsIgnoreCase("S")) {
			 allrequest.setFeatureId(4);
		 }
		 else if(txnIDfirstLetter.equalsIgnoreCase("R") || txnIDfirstLetter.equalsIgnoreCase("I") || txnIDfirstLetter.equalsIgnoreCase("A")) {
			 allrequest.setFeatureId(12);
		 }
		 else if(txnIDfirstLetter.equalsIgnoreCase("B")) {
			 allrequest.setFeatureId(7);
		 }
		 else if(txnIDfirstLetter.equalsIgnoreCase("L")) {
			 allrequest.setFeatureId(5);
		 }
		 else if(txnIDfirstLetter.equalsIgnoreCase("T")) {
			 allrequest.setFeatureId(21);
		 }
		allrequest.setPublicIp(header.getPublicIp());
		allrequest.setBrowser(header.getBrowser());
 
		if (filetype.equalsIgnoreCase("actual")) {

			if (!doc_TypeTag.equals("DEFAULT")) { 
				log.info("doc_TypeTag_______" + doc_TypeTag);
				String rootPath = urlToUpload.getValue() + txnid + "/" + doc_TypeTag + "/";
				File tmpDir = new File(rootPath + fileName);

				boolean exists = tmpDir.exists();
				if (exists) {

					String extension = fileName.substring(fileName.lastIndexOf("."));
					log.info("fileExtension===" + extension);

					if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpeg")
							|| extension.equalsIgnoreCase(".gif") || extension.equalsIgnoreCase(".jpg")) {
						response = feignCleintImplementation.downloadFile(txnid, filetype, fileName.replace("%20", " "),
								doc_TypeTag,allrequest);
						response.setFilePath("imageType");
						return response;
					}
					log.info("file against document   is exist.");
				} else {
					log.info(" file against documrnt type   is not exist.");
					response.setUrl("Not Found");
					return response;
				}

			} else if (doc_TypeTag.equalsIgnoreCase("DEFAULT")) {
				log.info("doc_TypeTag===" + doc_TypeTag);
				String rootPath = urlToUpload.getValue() + txnid + "/";
				File tmpDir = new File(rootPath + fileName);
				boolean exists = tmpDir.exists();
				if (exists) {

					log.info("actual file is exist.");
				} else {
					log.info(" actual file is not exist.");
					response.setUrl("Not Found");
					return response;
				}

			}
		} else if (filetype.equalsIgnoreCase("error")) {
			addMoreFileModel.setTag("system_error_filepath");

			urlToUpload = feignCleintImplementation.addMoreBuutonCount(addMoreFileModel);
			log.info("url to download error  file path ==" + urlToUpload.getValue());
			String rootPath = urlToUpload.getValue() + txnid + "/" + txnid + "_error.csv";
			File tmpDir = new File(rootPath);
			boolean exists = tmpDir.exists();
			if (exists) {
				log.info(" error file is exist.");
			} else {
				log.info(" error file is not exist.");
				response.setUrl("Not Found");
				return response;
			}

		}
		
		log.info(" everything is fine for hit to api for file downloading");
		log.info("request send to the download file api= txnid(" + txnid + ") fileName (" + fileName + ") fileType ("
				+ filetype + ")" + doc_TypeTag+"  ip and browser=="+allrequest);
		response = feignCleintImplementation.downloadFile(txnid, filetype, fileName.replace("%20", " "), doc_TypeTag,allrequest);

		log.info("response of download api=" + response + "------------------" + fileName.replace("%20", " "));
		log.info("redirect:" + response.getUrl());
		// ModelAndView mv= new ModelAndView(("redirect:"+
		// URLEncoder.encode(response.getUrl(), "UTF-8")));

		/*
		 * File file = new File(response.getUrl()); if(file.exists()){
		 * log.info("file is exist "); return response.getUrl(); } else {
		 * log.info("file is Not exist "); return null; }
		 */
		return response;
	}

	// *********************************************** Download Sampmle file
	// *************************************************
	@RequestMapping(value = "/sampleFileDownload/{featureId}", method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	public String downloadSampleFile(@PathVariable("featureId") String featureId,HttpSession session,HttpServletRequest request) throws IOException {
		
		int featureIdForFile = Integer.parseInt(featureId);
		   AllRequest allrequest= new AllRequest();
		   UserHeader header=registerService.getUserHeaders(request);
		
		/*
		 * int userTypeid=(int) session.getAttribute("usertypeId"); String
		 * roleType=String.valueOf(session.getAttribute("usertype")); String
		 * userName=session.getAttribute("username").toString(); int userId= (int)
		 * session.getAttribute("userid");
		 */ 
			if(session.getAttribute("usertypeId")==null || session.getAttribute("usertype").equals(null) || session.getAttribute("username").equals(null) || session.getAttribute("userid").equals(null)) 
			{
				allrequest.setUserTypeId(17);
				allrequest.setUserType("End User");
				allrequest.setUserId(0123);
				allrequest.setUsername("End User");
			}
			else {
				allrequest.setUserTypeId((int)  session.getAttribute("usertypeId"));
				allrequest.setUserType(String.valueOf(session.getAttribute("usertype")));
				allrequest.setUserId((int) session.getAttribute("userid"));
				allrequest.setUsername(session.getAttribute("username").toString());
			}
			
			allrequest.setPublicIp(header.getPublicIp());
			allrequest.setBrowser(header.getBrowser());
	 
			log.info(" featureIdForFile=="+featureIdForFile+"  request send to the sample file download api=" + allrequest);
		FileExportResponse response = feignCleintImplementation.downloadSampleFile(featureIdForFile,allrequest);
		log.info("response from sample file download file " + response);
        
		return "redirect:" + response.getUrl();

	}

	// consignment History

	@PostMapping("consignment-history")
	public ResponseEntity<?> viewHistory(HttpServletRequest request,HttpSession session) {
		List<List<Object>> finalList = new ArrayList<List<Object>>();
		List<List<String>> mul = new ArrayList<List<String>>();
		String filter = request.getParameter("filter");
		MapDatatableResponse map = new MapDatatableResponse();
		Gson gsonObject = new Gson();
		  UserHeader header=registerService.getUserHeaders(request);
		  
			  int userTypeid=(int) session.getAttribute("usertypeId"); String
			  roleType=String.valueOf(session.getAttribute("usertype")); String
			  userName=session.getAttribute("username").toString(); int userId= (int)
			  session.getAttribute("userid");
			  
		DBrowDataModel filterRequest = gsonObject.fromJson(filter, DBrowDataModel.class);
		try {
			 filterRequest.setPublicIp(header.getPublicIp());
			  filterRequest.setBrowser(header.getBrowser());
			  filterRequest.setUserId(userId);
			  filterRequest.setUserType(roleType);
			  filterRequest.setUsername(userName);
			  filterRequest.setUserTypeId(userTypeid);
			log.info("request passed to API:::::::::" + filterRequest);
			Object response = dBTablesFeignClient.historyConsignmentFeign(filterRequest);
			Gson gson = new Gson();
			String apiResponse = gson.toJson(response);
			log.info("apiResponse ::::::::::::::" + apiResponse);
			DBrowDataModel dBrowDataModel = gson.fromJson(apiResponse, DBrowDataModel.class);
			log.info("response::::::" + dBrowDataModel);

			List<String> columnList = dBrowDataModel.getColumns();
			List<Map<String, String>> rowData = dBrowDataModel.getRowData();
			List<DbListDataHeaders> headers = new ArrayList<>();

			if (columnList.isEmpty()) {
				dBrowDataModel.setColumns(Collections.emptyList());
			} else {
				List<String> list = dBrowDataModel.getColumns();
				ListIterator<String> iterator = list.listIterator();
				String columnName = null;
				while (iterator.hasNext()) {
					columnName = iterator.next();
					headers.add(new DbListDataHeaders(columnName, columnName));
				}

				map.setColumns(headers);
				map.setData(rowData);

			}
			return new ResponseEntity<>(map, HttpStatus.OK);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			dBrowDataModel.setColumns(Collections.emptyList());
			return new ResponseEntity<>(HttpStatus.OK);

		}
	}

	@RequestMapping(value = "/ManualFileDownload", method = {org.springframework.web.bind.annotation.RequestMethod.POST})
	public void  ManualSampleFile(@RequestParam(name = "userTypeId", required = false, defaultValue="4") int userTypeId,HttpServletResponse response,HttpSession session){
		log.info("userTypeId===" + userTypeId);
	      AllRequest allrequest= new AllRequest();
		String roleType=String.valueOf(session.getAttribute("usertype"));
		String userName=session.getAttribute("username").toString();
		int userId= (int) session.getAttribute("userid");  
		allrequest.setUserTypeId(userTypeId);
		allrequest.setUserType(roleType);
		allrequest.setUserId(userId);
		allrequest.setUsername(userName);
		allrequest.setPublicIp(session.getAttribute("publicIP").toString());
		allrequest.setBrowser(session.getAttribute("browser").toString());
		log.info("request send to the manual sample file download api=="+allrequest);
		  FileExportResponse fileExportResponse = feignCleintImplementation.manualDownloadSampleFile(allrequest);
		  String fileName=fileExportResponse.getFileName();
		  String fileLocation=propertyReader.downloadFilePath;
		
		try {
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=\""+fileName+"\"");
			
		String file=fileLocation+""+fileName;
		InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

		FileCopyUtils.copy(inputStream, response.getOutputStream());
		response.flushBuffer();	
		}
		catch(Exception e) {
			log.info(""+e.toString());
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename=\"File_not_exist\"");
		}
	}	
}
