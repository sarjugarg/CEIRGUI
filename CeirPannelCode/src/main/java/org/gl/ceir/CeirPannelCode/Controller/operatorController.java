package org.gl.ceir.CeirPannelCode.Controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.gl.ceir.CeirPannelCode.Feignclient.GrievanceFeignClient;
import org.gl.ceir.CeirPannelCode.Model.FileExportResponse;
import org.gl.ceir.CeirPannelCode.Model.FilterRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

@Controller
public class operatorController {

	@Autowired
	GrievanceFeignClient grievanceFeignClient;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	
	@RequestMapping(value=
		{"/greyList"},method={org.springframework.web.bind.annotation.
				RequestMethod.GET,org.springframework.web.bind.annotation.RequestMethod.POST}
			)
	    public ModelAndView viewOperator(HttpSession session, @RequestParam String type ) {
		ModelAndView mv = new ModelAndView();
		 log.info(" viewOperator entry point."); 
		 mv.setViewName("viewOperatorFeature");
		log.info("viewOperator exit point."); 
		return mv; 
	}
	
	
	@RequestMapping(value=
		{"/blackList"},method={org.springframework.web.bind.annotation.
				RequestMethod.GET,org.springframework.web.bind.annotation.RequestMethod.POST}
)
	    public ModelAndView ViewBlackList(HttpSession session) {
		ModelAndView mv = new ModelAndView();
		 log.info(" viewOperator entry point."); 
		 mv.setViewName("viewOperatorFeature");
		log.info("viewOperator exit point."); 
		return mv; 
	}
	
	
	//***************************************** Export operator controller *********************************
	
	@RequestMapping(value="/exportOperatorDetails",method ={org.springframework.web.bind.annotation.RequestMethod.GET})
	public String exportToExcel(@RequestParam(name="startDate", required = false) String startDate,
			@RequestParam(name="endDate",required = false) String endDate,
			@RequestParam(name="userTypeId", required = false) Integer userTypeId,
			@RequestParam(name="serviceDump",required = false) Integer serviceDump,
			@RequestParam(name="fileType",required = false) Integer fileType,
			@RequestParam(name="pageSize") Integer pageSize,
			@RequestParam(name="pageNo") Integer pageNo,
			HttpServletRequest request,
			HttpSession session)
	{
		log.info("startDate=="+startDate+ " endDate ="+endDate+"userTypeId="+userTypeId+"serviceDump="+serviceDump+"fileType="+fileType);
		int userId= (int) session.getAttribute("userid"); 
		int file=1;
		String userType=(String) session.getAttribute("usertype"); 	
		FileExportResponse fileExportResponse;
		FilterRequest filterRequest= new FilterRequest();
		filterRequest.setStartDate(startDate);
		filterRequest.setEndDate(endDate);
		filterRequest.setUserId(userId);
		filterRequest.setUserTypeId(userTypeId);
		filterRequest.setServiceDump(serviceDump);
		filterRequest.setFileType(fileType);
		log.info(" request passed to the exportTo Excel Api =="+filterRequest+" *********** pageSize"+pageSize+"  pageNo  "+pageNo);
		Object response = grievanceFeignClient.viewOperatorFeign(filterRequest, pageNo, pageSize, file);
		Gson gson= new Gson(); 
		String apiResponse = gson.toJson(response);
		fileExportResponse = gson.fromJson(apiResponse, FileExportResponse.class);
			log.info("response  from   export grievance  api="+fileExportResponse);
			return "redirect:"+fileExportResponse.getUrl();
	}
	
	
	//************************************************* download file *************************************************************** 
	@RequestMapping(value="/dowloadFiles/{fileName}",method={org.springframework.web.bind.annotation.RequestMethod.GET}) 
	public @ResponseBody FileExportResponse downloadFile(@PathVariable("fileName") String fileName) throws IOException {
	FileExportResponse fileExportResponse;
	log.info("request send to the download file api=  fileName ("+fileName+")");
	Object response=grievanceFeignClient.downloadOperatorFeign(fileName.replace("%20", " "));
	log.info("response----->" +response);
	Gson gson= new Gson(); 
	String apiResponse = gson.toJson(response);
	log.info("request passed to the api =="+fileName);
	fileExportResponse = gson.fromJson(apiResponse, FileExportResponse.class);
	log.info("response of download api="+fileExportResponse);

	String rootPath = fileExportResponse.getFilePath()+"/"+fileExportResponse.getFileName();
	log.info("complete file path for file exist or not in grey list and black list="+rootPath);
	File tmpDir = new File(rootPath);
	boolean exists = tmpDir.exists();
	if (exists) {
		log.info(" error file is exist.");
	} else {
		log.info(" error file is not exist.");
		fileExportResponse.setUrl("Not Found");
		return fileExportResponse;
	}
	return fileExportResponse;
	}

	
}
