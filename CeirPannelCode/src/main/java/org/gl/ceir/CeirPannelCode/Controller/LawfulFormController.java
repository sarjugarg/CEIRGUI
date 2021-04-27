package org.gl.ceir.CeirPannelCode.Controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.gl.ceir.CeirPannelCode.PropertyReader;
import org.gl.ceir.CeirPannelCode.Feignclient.FeignCleintImplementation;
import org.gl.ceir.CeirPannelCode.Feignclient.GrievanceFeignClient;
import org.gl.ceir.CeirPannelCode.Feignclient.UploadPaidStatusFeignClient;
import org.gl.ceir.CeirPannelCode.Model.AddMoreFileModel;
import org.gl.ceir.CeirPannelCode.Model.FileCopyToOtherServer;
import org.gl.ceir.CeirPannelCode.Model.GenricResponse;
import org.gl.ceir.CeirPannelCode.Model.LawfulStolenRecovey;
import org.gl.ceir.CeirPannelCode.Util.UtilDownload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

@Controller
public class LawfulFormController 
{



	@Autowired
	UtilDownload utildownload;

	@Autowired
	UploadPaidStatusFeignClient uploadPaidStatusFeignClient;

	@Autowired
	AddMoreFileModel addMoreFileModel,urlToUpload,urlToMove;


	@Autowired

	FeignCleintImplementation feignCleintImplementation;

	
	@Autowired
	GrievanceFeignClient grievanceFeignClient;
	
	@Autowired
    PropertyReader propertyReader;

	private final Logger log = LoggerFactory.getLogger(getClass());
	ModelAndView mv = new ModelAndView();
	GenricResponse response= new GenricResponse();



	@RequestMapping(value="/openlawfulStolenRecoveryPage",method = {RequestMethod.GET,RequestMethod.POST} )
	public ModelAndView openStolenRecoveryPage(@RequestParam(name="pageType") String pageType,@RequestParam(name="pageView") String pageView,
			@RequestParam(name="txnId") String txnId,@RequestParam(name="reqSource") String reqSource)
	{
		log.info("entry point in  open stolen and recovery  page."+pageType+"   txnId   ="+txnId);
		if(pageType.equals("stolen"))
		{
			log.info("block page");

			mv.setViewName("lawfulStolen");
		}else if(pageType.equals("recovery")) {
			log.info("recovery");
			mv.setViewName("lawfulRecovery");
		}
		else if(pageType.equals("editIndivisualsStolen"))
		{
			log.info("editIndivisualsStolen");
			mv.addObject("pageName","editIndivisualStolen");
			mv.setViewName("editIndivisualStolen");
		}
		else if(pageType.equals("editCompanyStolen"))
		{
			log.info("editCompanyStolen");
			mv.addObject("pageName","editCompanyStolen");
			mv.setViewName("editCompanyStolen");
		}
		else if(pageType.equals("editIndivisualRecovery"))
		{
			log.info("editIndivisualRecovery");
			mv.addObject("pageName","editIndivisualRecovery");
			mv.setViewName("editIndivisualRecovery");
		}
		else if(pageType.equals("editCompanyRecovery"))
		{
			log.info("editCompanyRecovery");
			mv.addObject("pageName","editCompanyRecovery");
			mv.setViewName("editCompanyRecovery");
		}

		log.info("exit point in  open stolen and recovery   page."+pageView);
		mv.addObject("stolenTxnId",txnId);
		mv.addObject("viewType",pageView);
		return mv;
	}

	//@PostMapping("lawfulIndivisualStolen")
	@RequestMapping(value= {"lawfulIndivisualStolen"},method= RequestMethod.POST,consumes = "multipart/form-data") 
	public @ResponseBody GenricResponse register(@RequestParam(name="file",required = false) MultipartFile file,
			@RequestParam(name="firFileName[]",required = false) MultipartFile[] firFileName,
			HttpServletRequest request,HttpSession session) {
		log.info("-inside controllerlawfulIndivisualStolen-------request---------");

		String userName=session.getAttribute("username").toString();
		Integer userId= (Integer) session.getAttribute("userid");
		String roletype=session.getAttribute("usertype").toString();
		String name=session.getAttribute("name").toString();
		String txnNumber="L" + utildownload.getTxnId();
		log.info("Random transaction id number="+txnNumber);
		String filter = request.getParameter("request");
		FileCopyToOtherServer fileCopyRequest= new FileCopyToOtherServer();


		Gson gson= new Gson(); 
		log.info("*********"+filter);

		addMoreFileModel.setTag("system_upload_filepath");
		urlToUpload=feignCleintImplementation.addMoreBuutonCount(addMoreFileModel);

		LawfulStolenRecovey lawfulIndivisualStolen  = gson.fromJson(filter, LawfulStolenRecovey.class);
		log.info(""+lawfulIndivisualStolen.toString());
		lawfulIndivisualStolen.setTxnId(txnNumber);
		lawfulIndivisualStolen.setUserId(userId);
		lawfulIndivisualStolen.setRoleType(roletype);
		// lawfulIndivisualStolen.setFileName(file.getOriginalFilename());

		try {

			byte[] bytes = file.getBytes();
			String rootPath =urlToUpload.getValue()+txnNumber+"/"; 
			File dir = new File(rootPath + File.separator);

			if (!dir.exists()) {
				dir.mkdirs();
				dir.setExecutable(true,false);
				dir.setReadable(true,false);
				dir.setWritable(true,false);
			}
			// Create the file on server 
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
		
		for (int i=0;i<lawfulIndivisualStolen.getAttachedFiles().size();i++) {
			lawfulIndivisualStolen.getAttachedFiles().get(i).setTxnId(txnNumber);
			//grievanceRequest.getMultifile().get(i).getDocType();
			
		}
		
		int i=0;
		for( MultipartFile fileFir : firFileName) {

			log.info("-----"+ fileFir.getOriginalFilename());
			log.info("++++"+ fileFir);

			String tagName=lawfulIndivisualStolen.getAttachedFiles().get(i).getDocType();
			log.info("doctype Name==="+tagName+"value of index="+i);


			try {

				byte[] bytes =
						fileFir.getBytes(); String rootPath = urlToUpload.getValue()+txnNumber+"/"+tagName+"/"; 
						File dir =   new File(rootPath + File.separator);
						File dir1 =   new File(urlToUpload.getValue()+txnNumber+"/" + File.separator);
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
						File serverFile = new File(rootPath+fileFir.getOriginalFilename());
						log.info("uploaded file path on server" + serverFile); BufferedOutputStream
						stream = new BufferedOutputStream(new FileOutputStream(serverFile));
						serverFile.setExecutable(true,false);
						serverFile.setReadable(true,false);
						serverFile.setWritable(true,false);
						stream.write(bytes); stream.close();

						fileCopyRequest.setFilePath(rootPath);
						fileCopyRequest.setTxnId(txnNumber);
						fileCopyRequest.setFileName(fileFir.getOriginalFilename());
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
		 * if(firFileName==null) { log.info("fir file is empty"); } else { try {
		 * 
		 * byte[] bytes = firFileName.getBytes(); String rootPath
		 * =urlToUpload.getValue()+lawfulIndivisualStolen.getTxnId()+"/"; File dir = new
		 * File(rootPath + File.separator);
		 * 
		 * if (!dir.exists()) { dir.mkdirs(); dir.setExecutable(true,false);
		 * dir.setReadable(true,false); dir.setWritable(true,false); } // Create the
		 * file on server File serverFile = new
		 * File(rootPath+firFileName.getOriginalFilename());
		 * log.info("uploaded file path on server" + serverFile); BufferedOutputStream
		 * stream = new BufferedOutputStream(new FileOutputStream(serverFile));
		 * serverFile.setExecutable(true,false); serverFile.setReadable(true,false);
		 * serverFile.setWritable(true,false); stream.write(bytes); stream.close();
		 * fileCopyRequest.setFilePath(rootPath); fileCopyRequest.setTxnId(txnNumber);
		 * fileCopyRequest.setFileName(firFileName.getOriginalFilename());
		 * fileCopyRequest.setServerId(propertyReader.serverId);
		 * log.info("request passed to move file to other server=="+fileCopyRequest);
		 * GenricResponse
		 * fileRespnose=grievanceFeignClient.saveUploadedFileOnANotherServer(
		 * fileCopyRequest); log.info("file move api response==="+fileRespnose);
		 * //lawfulIndivisualStolen.setFileName(file.getOriginalFilename()); } catch
		 * (Exception e) { // TODO: handle exception e.printStackTrace(); } }
		 */
		lawfulIndivisualStolen.setPublicIp(session.getAttribute("publicIP").toString());
		lawfulIndivisualStolen.setBrowser(session.getAttribute("browser").toString());
		log.info("request passed to the save regularizeDeviceDbs api"+lawfulIndivisualStolen);
		GenricResponse response = null;
		try {
			response = uploadPaidStatusFeignClient.lawfulIndivisualStolen(lawfulIndivisualStolen);
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


	@PostMapping("lawfulOraganisationStolen")
	public @ResponseBody GenricResponse lawfulOraganisationStolen(@RequestParam(name="file",required = false) MultipartFile file,@RequestParam(name="firFileName",required = false) MultipartFile firFileName,HttpServletRequest request,HttpSession session) {
		log.info("-inside controllerlawfulIndivisualStolen-------request---------");
		String userName=session.getAttribute("username").toString();
		Integer userId= (Integer) session.getAttribute("userid");
		String roletype=session.getAttribute("usertype").toString();
		String name=session.getAttribute("name").toString();
		String txnNumber="L" + utildownload.getTxnId();
		log.info("Random transaction id number="+txnNumber);
		String filter = request.getParameter("request");

		FileCopyToOtherServer fileCopyRequest= new FileCopyToOtherServer();
		addMoreFileModel.setTag("system_upload_filepath");
		urlToUpload=feignCleintImplementation.addMoreBuutonCount(addMoreFileModel);

		Gson gson= new Gson(); 
		log.info("*********"+filter);
		LawfulStolenRecovey lawfulIndivisualStolen  = gson.fromJson(filter, LawfulStolenRecovey.class);
		log.info(""+lawfulIndivisualStolen.toString());
		lawfulIndivisualStolen.setTxnId(txnNumber);
		lawfulIndivisualStolen.setUserId(userId);
		lawfulIndivisualStolen.setRoleType(roletype);
		lawfulIndivisualStolen.setFileName(file.getOriginalFilename());

		try {


			byte[] bytes = file.getBytes();
			String rootPath =urlToUpload.getValue()+txnNumber+"/"; 
			File dir = new File(rootPath + File.separator);

			if (!dir.exists()) {
				dir.mkdirs();
				dir.setExecutable(true,false);
				dir.setReadable(true,false);
				dir.setWritable(true,false);
			}
			// Create the file on server 
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
		log.info("fir file detaissssssssss"+firFileName);
		if(firFileName==null) {
			log.info(" bulk fir file is empty");	
		}
		else {
			try {


				byte[] bytes = firFileName.getBytes();
				String rootPath =urlToUpload.getValue()+lawfulIndivisualStolen.getTxnId()+"/"; 
				File dir = new File(rootPath + File.separator);

				if (!dir.exists()) {
					dir.mkdirs();
					dir.setExecutable(true,false);
					dir.setReadable(true,false);
					dir.setWritable(true,false);
				}
				// Create the file on server 
				File serverFile = new File(rootPath+firFileName.getOriginalFilename());
				log.info("uploaded file path on server" + serverFile); BufferedOutputStream
				stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				serverFile.setExecutable(true,false);
				serverFile.setReadable(true,false);
				serverFile.setWritable(true,false);
				stream.write(bytes); 
				stream.close();
				fileCopyRequest.setFilePath(rootPath);
				fileCopyRequest.setTxnId(txnNumber);
				fileCopyRequest.setFileName(firFileName.getOriginalFilename());
				fileCopyRequest.setServerId(propertyReader.serverId);
				log.info("request passed to move file to other server=="+fileCopyRequest);
				GenricResponse fileRespnose=grievanceFeignClient.saveUploadedFileOnANotherServer(fileCopyRequest);
				log.info("file move api response==="+fileRespnose);
				lawfulIndivisualStolen.setFileName(file.getOriginalFilename());
			} 
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		lawfulIndivisualStolen.setPublicIp(session.getAttribute("publicIP").toString());
		lawfulIndivisualStolen.setBrowser(session.getAttribute("browser").toString());
		log.info("request passed to the report Indivisual and comapny  api"+lawfulIndivisualStolen);
		GenricResponse response = null;
		try {
			response = uploadPaidStatusFeignClient.lawfulIndivisualStolen(lawfulIndivisualStolen);
			//GenricResponse response = null;
			log.info("---------response  from Indivisual and comapny api "+response);
		}
		catch (Exception e) {
			// TODO: handle exception
			log.info("exception in upload paid stat error"+e);
			e.printStackTrace();

		}
		return response;
	}




	@PostMapping("lawfulIndivisualRecovery")
	public @ResponseBody GenricResponse lawfulIndivsualRecovery(@RequestParam(name="file",required = false) MultipartFile file,HttpServletRequest request,HttpSession session) {
		String userName=session.getAttribute("username").toString();
		Integer userId= (Integer) session.getAttribute("userid");
		String roletype=session.getAttribute("usertype").toString();
		String name=session.getAttribute("name").toString();
		String txnNumber="L" + utildownload.getTxnId();
		log.info("Random transaction id number="+txnNumber);
		String filter = request.getParameter("request");

		FileCopyToOtherServer fileCopyRequest= new FileCopyToOtherServer();
		addMoreFileModel.setTag("system_upload_filepath");
		urlToUpload=feignCleintImplementation.addMoreBuutonCount(addMoreFileModel);


		Gson gson= new Gson(); 
		log.info("*********"+filter);
		LawfulStolenRecovey lawfulIndivisualStolen  = gson.fromJson(filter, LawfulStolenRecovey.class);
		log.info(""+lawfulIndivisualStolen.toString());
		lawfulIndivisualStolen.setTxnId(txnNumber);
		lawfulIndivisualStolen.setUserId(userId);
		lawfulIndivisualStolen.setRoleType(roletype);

		if(file==null)
		{
			log.info("file is blank");
			lawfulIndivisualStolen.setFileName("");   
		}
		else {
			try {



				log.info("file is not blank");
				byte[] bytes = file.getBytes();
				String rootPath =urlToUpload.getValue()+txnNumber+"/"; 
				File dir = new File(rootPath + File.separator);

				if (!dir.exists()) {
					dir.mkdirs();
					dir.setExecutable(true,false);
					dir.setReadable(true,false);
					dir.setWritable(true,false);
				}
				// Create the file on server 
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
				lawfulIndivisualStolen.setFileName(file.getOriginalFilename());
			} 
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}
		lawfulIndivisualStolen.setPublicIp(session.getAttribute("publicIP").toString());
		lawfulIndivisualStolen.setBrowser(session.getAttribute("browser").toString());
		log.info("request passed to the report Indivisual and comapny recovery api"+lawfulIndivisualStolen);
		GenricResponse response = null;
		try {
			response = uploadPaidStatusFeignClient.lawfulIndivisualStolen(lawfulIndivisualStolen);
			//GenricResponse response = null;
			log.info("---------response  from Indivisual and comapny recovery api "+response);
		}
		catch (Exception e) {
			// TODO: handle exception
			log.info("exception in upload paid stat error"+e);
			e.printStackTrace();

		}
		return response;
	}


	@RequestMapping(value="/openStolenAndRecoveryPage",method ={org.springframework.web.bind.annotation.RequestMethod.POST})
	public @ResponseBody LawfulStolenRecovey openSingleImeiForm(@RequestParam(name="txnId",required = false) String txnId,HttpSession session,
			@RequestParam(name="requestType") int requestType)
	{
		log.info("entry point of  fetch lawful stolen an recovery devices in the bases of transaction id .");
		Integer userId= (Integer) session.getAttribute("userid");
		String roletype=session.getAttribute("usertype").toString();

		LawfulStolenRecovey lawfulStolenRecovery= new LawfulStolenRecovey();
		LawfulStolenRecovey lawfulUser= new LawfulStolenRecovey();

		lawfulUser.setTxnId(txnId);
		lawfulUser.setRequestType(requestType);
		lawfulUser.setRoleType(roletype);
		lawfulUser.setUserId(userId);
		lawfulUser.setPublicIp(session.getAttribute("publicIP").toString());
		lawfulUser.setBrowser(session.getAttribute("browser").toString());
		log.info("request passed to the fetch Device api="+lawfulUser);
		lawfulStolenRecovery=uploadPaidStatusFeignClient.fetchSingleDevicebyTxnId(lawfulUser);

		log.info("response from fetch lawful stolen an recovery devices  api="+lawfulStolenRecovery);

		addMoreFileModel.setTag("upload_file_link");
		urlToUpload=feignCleintImplementation.addMoreBuutonCount(addMoreFileModel);
		lawfulStolenRecovery.setFileLink(urlToUpload.getValue());
		return lawfulStolenRecovery;
	}


	//@PostMapping("lawfulIndivisualStolenUpdate")
	@RequestMapping(value= {"lawfulIndivisualStolenUpdate"},method= RequestMethod.POST,consumes = "multipart/form-data")
	public @ResponseBody GenricResponse updateStolenRequest(@RequestParam(name="file",required = false) MultipartFile file,@RequestParam(name="firFileName[]",required = false) MultipartFile[] firFileName,HttpServletRequest request,HttpSession session) {
		log.info("-inside controller lawfulIndivisualStolen update-------request---------");
		String userName=session.getAttribute("username").toString();
		Integer userId= (Integer) session.getAttribute("userid");
		String roletype=session.getAttribute("usertype").toString();
		String name=session.getAttribute("name").toString();
		String movedFileTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
		log.info("Moved File Time value=="+movedFileTime);
		addMoreFileModel.setTag("system_upload_filepath");
		urlToUpload=feignCleintImplementation.addMoreBuutonCount(addMoreFileModel);

		FileCopyToOtherServer fileCopyRequest= new FileCopyToOtherServer();
		addMoreFileModel.setTag("uploaded_file_move_path");
		urlToMove=feignCleintImplementation.addMoreBuutonCount(addMoreFileModel);
		log.info("moved file path from api="+urlToMove.getValue());

		String filter = request.getParameter("request");
		Gson gson= new Gson(); 
		log.info("*********"+filter);
		LawfulStolenRecovey lawfulIndivisualStolen  = gson.fromJson(filter, LawfulStolenRecovey.class);
		log.info(""+lawfulIndivisualStolen.toString());
		lawfulIndivisualStolen.setUserId(userId);
		lawfulIndivisualStolen.setRoleType(roletype);

		if(file==null) {
			log.info("file is empty");	
		}
		else {
			try {



				String rootPath =urlToUpload.getValue()+lawfulIndivisualStolen.getTxnId()+"/"; 
				File tmpDir = new File(rootPath+file.getOriginalFilename());
				tmpDir.setExecutable(true,false);
				tmpDir.setReadable(true,false);
				tmpDir.setWritable(true,false);
				boolean exists = tmpDir.exists();
				if(exists) {

					Path temp = Files.move 
							(Paths.get(urlToUpload.getValue()+"/"+lawfulIndivisualStolen.getTxnId()+"/"+file.getOriginalFilename()), 
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
				
				// Create the file on server 
				File serverFile = new File(rootPath+file.getOriginalFilename());
				log.info("uploaded file path on server" + serverFile); BufferedOutputStream
				stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				serverFile.setExecutable(true,false);
				serverFile.setReadable(true,false);
				serverFile.setWritable(true,false);
				stream.write(bytes); 
				stream.close();
				fileCopyRequest.setFilePath(rootPath);
				fileCopyRequest.setTxnId(lawfulIndivisualStolen.getTxnId());
				fileCopyRequest.setFileName(file.getOriginalFilename());
				fileCopyRequest.setServerId(propertyReader.serverId);
				log.info("request passed to move file to other server=="+fileCopyRequest);
				GenricResponse fileRespnose=grievanceFeignClient.saveUploadedFileOnANotherServer(fileCopyRequest);
				log.info("file move api response==="+fileRespnose);
				//lawfulIndivisualStolen.setFileName(file.getOriginalFilename());
			} 

			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		
		
		
		
		int i=0;
		for( MultipartFile fileFir : firFileName) {

			log.info("-----"+ fileFir.getOriginalFilename());
			log.info("++++"+ fileFir);

			String tagName=lawfulIndivisualStolen.getAttachedFiles().get(i).getDocType();
			log.info("doctype Name==="+tagName+"value of index="+i);


			try {

				byte[] bytes =
						fileFir.getBytes(); String rootPath = urlToUpload.getValue()+lawfulIndivisualStolen.getTxnId()+"/"+tagName+"/"; 
						File dir =   new File(rootPath + File.separator);
						File dir1 =   new File(urlToUpload.getValue()+lawfulIndivisualStolen.getTxnId()+"/" + File.separator);
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
						File serverFile = new File(rootPath+fileFir.getOriginalFilename());
						log.info("uploaded file path on server" + serverFile); BufferedOutputStream
						stream = new BufferedOutputStream(new FileOutputStream(serverFile));
						serverFile.setExecutable(true,false);
						serverFile.setReadable(true,false);
						serverFile.setWritable(true,false);
						stream.write(bytes); stream.close();

						fileCopyRequest.setFilePath(rootPath);
						fileCopyRequest.setTxnId(lawfulIndivisualStolen.getTxnId());
						fileCopyRequest.setFileName(fileFir.getOriginalFilename());
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
		 * if(firFileName==null) { log.info("fir file is empty"); } else { try {
		 * 
		 * 
		 * 
		 * byte[] bytes = firFileName.getBytes(); String rootPath
		 * =urlToUpload.getValue()+lawfulIndivisualStolen.getTxnId()+"/"; File tmpDir =
		 * new File(rootPath+firFileName.getOriginalFilename());
		 * tmpDir.setExecutable(true,false); tmpDir.setReadable(true,false);
		 * tmpDir.setWritable(true,false); boolean exists = tmpDir.exists(); if(exists)
		 * {
		 * 
		 * Path temp = Files.move
		 * (Paths.get(urlToUpload.getValue()+"/"+lawfulIndivisualStolen.getTxnId()+"/"+
		 * firFileName.getOriginalFilename()),
		 * Paths.get(urlToMove.getValue()+movedFileTime+"_"+firFileName.
		 * getOriginalFilename())); String
		 * movedPath=urlToMove.getValue()+movedFileTime+"_"+firFileName.
		 * getOriginalFilename();
		 * 
		 * log.info("file is already exist, moved to this "+movedFileTime+"_"+
		 * movedPath+" path. "); tmpDir.delete(); }
		 * 
		 * File dir = new File(rootPath + File.separator);
		 * 
		 * if (!dir.exists()) { dir.mkdirs(); dir.setExecutable(true,false);
		 * dir.setReadable(true,false); dir.setWritable(true,false); } // Create the
		 * file on server File serverFile = new
		 * File(rootPath+firFileName.getOriginalFilename());
		 * log.info("uploaded file path on server" + serverFile); BufferedOutputStream
		 * stream = new BufferedOutputStream(new FileOutputStream(serverFile));
		 * serverFile.setExecutable(true,false); serverFile.setReadable(true,false);
		 * serverFile.setWritable(true,false); stream.write(bytes); stream.close();
		 * fileCopyRequest.setFilePath(rootPath);
		 * fileCopyRequest.setTxnId(lawfulIndivisualStolen.getTxnId());
		 * fileCopyRequest.setFileName(firFileName.getOriginalFilename());
		 * fileCopyRequest.setServerId(propertyReader.serverId);
		 * log.info("request passed to move file to other server=="+fileCopyRequest);
		 * GenricResponse
		 * fileRespnose=grievanceFeignClient.saveUploadedFileOnANotherServer(
		 * fileCopyRequest); log.info("file move api response==="+fileRespnose);
		 * //lawfulIndivisualStolen.setFileName(file.getOriginalFilename()); } catch
		 * (Exception e) { // TODO: handle exception e.printStackTrace(); } }
		 */
		lawfulIndivisualStolen.setPublicIp(session.getAttribute("publicIP").toString());
		lawfulIndivisualStolen.setBrowser(session.getAttribute("browser").toString());
		log.info("request passed to the update stolen  api"+lawfulIndivisualStolen);
		GenricResponse response = null;
		try {
			response = uploadPaidStatusFeignClient.updateIndivisualStolen(lawfulIndivisualStolen);
			//GenricResponse response = null;
			log.info("---------response from indivisual stolen api --------"+response);
		}
		catch (Exception e) {
			// TODO: handle exception
			log.info("exception in upload paid stat error"+e);
			e.printStackTrace();

		}
		return response;
	}

}
