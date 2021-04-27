package com.gl.ceir.config.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gl.ceir.config.model.AllRequest;
import com.gl.ceir.config.model.FileDetails;
import com.gl.ceir.config.service.impl.FileServiceImpl;
import com.gl.ceir.config.service.impl.FileStorageService;

import io.swagger.annotations.ApiOperation;

@RestController
public class FileController {

	private static final Logger logger = LogManager.getLogger(FileController.class);

	@Autowired
	private FileStorageService fileStorageService;

	@Autowired
	private FileServiceImpl fileServiceImpl;
/*
	@ApiOperation(value = "Upload file Ticket ID and Document Type must be there ", response = UploadFileResponse.class)
	@RequestMapping(path = "/document/updateStatus", method = RequestMethod.PATCH)
	public MappingJacksonValue partialUpdateName(@RequestParam String documentStatus, @RequestParam Long documentId) {

		DocumentStatus documentStatusEnum = DocumentStatus.getDocumentStatus(documentStatus);
		if (documentStatusEnum == null) {
			throw new ResourceNotFoundException("Status can be APPROVED / REJECTED", "id", documentId);
		} else {
			Documents documents = documentService.updateStatus(documentStatusEnum, documentId);

			if (documentStatusEnum == DocumentStatus.APPROVED) {
				pendingActionsService.changeTransactionState(documents.getTicketId(),
						TransactionState.DOCUMENT_APPROVED);
			} else if (documentStatusEnum == DocumentStatus.REJECTED) {
				pendingActionsService.changeTransactionState(documents.getTicketId(),
						TransactionState.DOCUMENT_REJECTED);
			}
			MappingJacksonValue mapping = new MappingJacksonValue(documents);
			return mapping;
		}

	}

	@ApiOperation(value = "View all the list of documents for imei OR msisdn", response = VipList.class)
	@RequestMapping(path = "/document/", method = RequestMethod.GET)
	public MappingJacksonValue getByMsisdnAndImei(@RequestParam(required = false) Long msisdn,
			@RequestParam(required = false) Long imei) {
		List<Documents> documents = documentService.getByMsisdnAndImei(imei, msisdn);
		MappingJacksonValue mapping = new MappingJacksonValue(documents);
		return mapping;
	}

	@ApiOperation(value = "View All Types of document can upload ", response = String.class, responseContainer = "list")
	@GetMapping("/document/types")
	public ResponseEntity<List<String>> getAllDocumentTypes() {
		return new ResponseEntity<>(DocumentType.getDocumentTypes(), HttpStatus.OK);
	}

	@ApiOperation(value = "Upload file Ticket ID and Document Type must be there ", response = UploadFileResponse.class)
	@RequestMapping(path = "/document/upload", method = RequestMethod.POST)
	public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file, String ticketId, Long imei,
			Long msisdn, DocumentType documentType) {

		UploadFileRequest uploadFileRequest = new UploadFileRequest();
		uploadFileRequest.setDocumentType(documentType);
		uploadFileRequest.setTicketId(ticketId);
		uploadFileRequest.setMsisdn(msisdn);
		uploadFileRequest.setImei(imei);

		UploadFileResponse uploadFileResponse = fileStorageService.storeFile(file, uploadFileRequest);

		return uploadFileResponse;
	}

	@GetMapping(FileStorageService.downloadContext + "{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
		// Load file as Resource
		Resource resource = fileStorageService.loadFileAsResource(fileName);

		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			logger.info("Could not determine file type.");
		}

		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}
	*/
	
	@ApiOperation(value = "Download Sample Stoke File.", response = String.class)
	@RequestMapping(path = "/Download/SampleFile", method = RequestMethod.POST)
	public FileDetails downloadSampleFile(int featureId,@RequestBody AllRequest request) {		
		
		logger.info("Request sample file link with featureId [" + featureId + "]");
		FileDetails fileDetails = fileServiceImpl.getSampleFile(featureId,request);
		logger.info("Response for featureId [" + featureId + "] sample file " + fileDetails);
		
		return fileDetails;
	}

	@ApiOperation(value = "Download Stoke upload File.", response = String.class)
	@RequestMapping(path = "/Download/uploadFile", method = RequestMethod.POST)
	public FileDetails downloadUploadedFile(String fileName, String txnId,String fileType,@RequestParam(name = "tag", required = false) String tag,@RequestBody AllRequest request) {
		
		logger.info("Request to download uploded file link with txnId [" + txnId + "]"+" ip and browser="+request);
		FileDetails fileDetails = fileServiceImpl.downloadUploadedFile(fileName, txnId, fileType, tag,request);
		logger.info("Response for txnId [" + txnId + "] sample file " + fileDetails);
		
		return fileDetails;
	}
	
	@ApiOperation(value = "Download manuals.", response = String.class)
	@PostMapping("/Download/manuals")
	public FileDetails downloadManuals(@RequestBody AllRequest auditRequest) {		
		
		logger.info("Request manuals=="+auditRequest);
		FileDetails fileDetails = fileServiceImpl.getManuals(auditRequest);
		logger.info("Response for manuals " + fileDetails);
		
		return fileDetails;
	}
}
