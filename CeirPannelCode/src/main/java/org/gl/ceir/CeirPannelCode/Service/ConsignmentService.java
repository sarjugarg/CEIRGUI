package org.gl.ceir.CeirPannelCode.Service;

import org.gl.ceir.CeirPannelCode.Model.ConsignmentModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;


public class ConsignmentService {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	
	public String registerConsignmnet(ConsignmentModel consignmentModel,MultipartFile file)
	{
		log.info(" register  consignment service entry point.");
		// Request parameters
		log.info("Requested parameters=="+consignmentModel);
		log.info("multipart file info**********=="+file.isEmpty());
		log.info("multipart file name**********=="+file.getOriginalFilename());
		// request validate
		if(!consignmentModel.getSupplierId().equals("") && !consignmentModel.getSupplierName().equals("") && !consignmentModel.getConsignmentNumber().equals("")
			&&	!consignmentModel.getExpectedArrivalDate().equals("") && !consignmentModel.getExpectedDispatcheDate().equals("") && !consignmentModel.getQuantity().equals("")
			&& consignmentModel.getExpectedArrivalPort()!=null && consignmentModel.getOrganisationcountry()!=null && !file.getOriginalFilename().equals("") )
		{
			// Process
			log.info("Request is valid.");
			
			// response
			return "success";
		}
		else {
			// Process
			log.info("invalid Request.");
			
			// response
			return "fail";
		}
	}
	
	
	public String updateConsignmnet(ConsignmentModel consignmentModel,MultipartFile file,String txnid)
	{
		log.info(" update  consignment service entry point.");
		// Request parameters
		log.info("Requested parameters=="+consignmentModel);
		log.info("multipart file name**********=="+file.getOriginalFilename());
		log.info(" transaction id=="+txnid);
		// request validate
		if(!consignmentModel.getSupplierId().equals("") && !consignmentModel.getSupplierName().equals("") && !consignmentModel.getConsignmentNumber().equals("")
			&&	!consignmentModel.getExpectedArrivalDate().equals("") && !consignmentModel.getExpectedDispatcheDate().equals("") && !consignmentModel.getQuantity().equals("")
			&& consignmentModel.getExpectedArrivalPort()!=null && consignmentModel.getOrganisationcountry()!=null && txnid!="")
		{
			// Process
			log.info("Request is valid.");
			
			// response
			return "success";
		}
		else {
			// Process
			log.info("invalid Request.");
			
			// response
			return "fail";
		}
	}
	
	public ConsignmentModel fetchConsignmentData(String txnid)
	{
		// request parameters
		log.info("request parameters."+txnid);
		ConsignmentModel consignmentModel = new ConsignmentModel();
		// validate 
		if(!txnid.equals(""))
		{
			// service method call
			
			
			// take response
		}
		
		// return response
		return consignmentModel;
	}
	
	public String deleteConsignment(String txnid)
	{
		log.info("delete consignment entry point ");
		// request parameters
		log.info("request parameters ="+txnid);
		
		//validate request parameters
		if(!txnid.equals(""))
		{
			// service method to delete consignment call
			
			log.info("valid request");
			// take response
			return "success";
		}
		else {
			return "fail";
			
		}
		
		// return response
	
		
	}
	public String downloadFile(String txnid,String fileName,String fileType)
	{
		log.info("download file entry point ");
		// request parameters
		log.info("request parameters "+"txnid "+txnid+" fileName "+fileName+" fileType"+fileType);
		
		// validation
		if(!txnid.equals("") && !fileName.equals("") && !fileType.equals("") )
		{
			log.info("valid request ");
			//service method call to downloadble link
			
			// take response 
			
			// return response
			return "link";
		}
		else {
			log.info("Invalid request ");
			// error message
			
			// take response 
			
			// return response
			return "message";
		}
		
		
	}
	

}














