package com.gl.ceir.config.service.impl;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gl.ceir.config.ConfigTags;
import com.gl.ceir.config.EmailSender.EmailUtil;
import com.gl.ceir.config.configuration.PropertiesReader;
import com.gl.ceir.config.configuration.SortDirection;
import com.gl.ceir.config.exceptions.ResourceServicesException;
import com.gl.ceir.config.feign.UserFeignClient;
import com.gl.ceir.config.model.AllRequest;
import com.gl.ceir.config.model.AuditTrail;
import com.gl.ceir.config.model.CeirActionRequest;
import com.gl.ceir.config.model.Count;
import com.gl.ceir.config.model.DashboardUsersFeatureStateMap;
import com.gl.ceir.config.model.EndUserDB;
import com.gl.ceir.config.model.FileDetails;
import com.gl.ceir.config.model.FilterRequest;
import com.gl.ceir.config.model.GenricResponse;
import com.gl.ceir.config.model.RawMail;
import com.gl.ceir.config.model.RegularizeDeviceDb;
import com.gl.ceir.config.model.RegularizeDeviceView;
import com.gl.ceir.config.model.SearchCriteria;
import com.gl.ceir.config.model.StateMgmtDb;
import com.gl.ceir.config.model.SystemConfigListDb;
import com.gl.ceir.config.model.SystemConfigurationDb;
import com.gl.ceir.config.model.User;
import com.gl.ceir.config.model.UserProfile;
import com.gl.ceir.config.model.VisaDb;
import com.gl.ceir.config.model.WebActionDb;
import com.gl.ceir.config.model.constants.Datatype;
import com.gl.ceir.config.model.constants.Features;
import com.gl.ceir.config.model.constants.GenericMessageTags;
import com.gl.ceir.config.model.constants.ReferTable;
import com.gl.ceir.config.model.constants.RegularizeDeviceStatus;
import com.gl.ceir.config.model.constants.SearchOperation;
import com.gl.ceir.config.model.constants.SubFeatures;
import com.gl.ceir.config.model.constants.Tags;
import com.gl.ceir.config.model.constants.TaxStatus;
import com.gl.ceir.config.model.constants.WebActionDbSubFeature;
import com.gl.ceir.config.model.file.RegularizeDeviceFileModel;
import com.gl.ceir.config.repository.AuditTrailRepository;
import com.gl.ceir.config.repository.ConsignmentRepository;
import com.gl.ceir.config.repository.DashboardUsersFeatureStateMapRepository;
import com.gl.ceir.config.repository.EndUserDbRepository;
import com.gl.ceir.config.repository.RegularizedDeviceDbRepository;
import com.gl.ceir.config.repository.SystemConfigurationDbRepository;
import com.gl.ceir.config.repository.UserProfileRepository;
import com.gl.ceir.config.repository.UserRepository;
import com.gl.ceir.config.repository.WebActionDbRepository;
import com.gl.ceir.config.request.model.Generic_Response_Notification;
import com.gl.ceir.config.request.model.RegisterationUser;
import com.gl.ceir.config.specificationsbuilder.GenericSpecificationBuilder;
import com.gl.ceir.config.transaction.RegularizeDeviceTransaction;
import com.gl.ceir.config.util.CommonFunction;
import com.gl.ceir.config.util.CustomMappingStrategy;
import com.gl.ceir.config.util.DateUtil;
import com.gl.ceir.config.util.InterpSetter;
import com.gl.ceir.config.util.StatusSetter;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

@Service
public class RegularizedDeviceServiceImpl {

	private static final Logger logger = LogManager.getLogger(RegularizedDeviceServiceImpl.class);

	private ReentrantLock lock = new ReentrantLock();

	@Autowired
	RegularizeDeviceTransaction regularizeDeviceTransaction;

	@Autowired
	ConsignmentRepository consignmentRepository;

	@Autowired
	AuditTrailRepository auditTrailRepository;

	@Autowired
	EndUserDbRepository endUserDbRepository;

	@Autowired
	WebActionDbRepository webActionDbRepository;

	@Autowired
	RegularizedDeviceDbRepository regularizedDeviceDbRepository;

	@Autowired
	PropertiesReader propertiesReader;

	@Autowired
	ConfigurationManagementServiceImpl configurationManagementServiceImpl;

	@Autowired
	StateMgmtServiceImpl stateMgmtServiceImpl;

	@Autowired	
	EmailUtil emailUtil;

	@Autowired
	InterpSetter interpSetter;

	@Autowired
	StatusSetter statusSetter;

	@Autowired
	UserProfileRepository userProfileRepository;

	@Autowired
	DateUtil dateUtil;

	@Autowired
	SystemConfigurationDbRepository systemConfigurationDbRepository;

	@Autowired
	UserStaticServiceImpl userStaticServiceImpl;

	@Autowired
	UserRepository userRepository;
	@Autowired
	CommonFunction commonFunction;
	@Autowired
	UserFeignClient userFeignClient;
	@Autowired
	EnduserServiceImpl enduserServiceImpl;
	
	
	@Autowired
	DashboardUsersFeatureStateMapRepository dashboardUsersFeatureStateMapRepository; 


	private List<RegularizeDeviceDb> getAll(FilterRequest filterRequest, String source){

		List<StateMgmtDb> stateList = null;

		try {
			stateList = stateMgmtServiceImpl.getByFeatureIdAndUserTypeId(filterRequest.getFeatureId(), filterRequest.getUserTypeId());

			List<RegularizeDeviceDb> regularizeDeviceDbs = regularizedDeviceDbRepository.findAll(buildSpecification(filterRequest, stateList, source).build());

			for(RegularizeDeviceDb regularizeDeviceDb : regularizeDeviceDbs) {

				for(StateMgmtDb stateMgmtDb : stateList) {
					if(regularizeDeviceDb.getStatus() == stateMgmtDb.getState()) {
						regularizeDeviceDb.setStateInterp(stateMgmtDb.getInterp()); 
						break; 
					} 
				}

				setInterp(regularizeDeviceDb);
			}

			return regularizeDeviceDbs;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException("Regularized Device Service", e.getMessage());
		}
	}

	public Page<RegularizeDeviceDb> filter(FilterRequest filterRequest, Integer pageNo, Integer pageSize,String source){

		User user = null;
		List<StateMgmtDb> stateList = null;
		SystemConfigurationDb newYearDateRegisterDevice = systemConfigurationDbRepository.getByTag(ConfigTags.new_year_date_register_device);
		SystemConfigurationDb gracePeriodForRegisterDevice = systemConfigurationDbRepository.getByTag(ConfigTags.grace_period_for_rgister_device);

		try {
			String orderColumn = "Date".equalsIgnoreCase(filterRequest.getColumnName()) ? "createdOn"
					: "NID/Passport No.".equalsIgnoreCase(filterRequest.getColumnName()) ? "nid"
						:"Transaction ID".equalsIgnoreCase(filterRequest.getColumnName()) ? "txnId"
							: "Nationality".equalsIgnoreCase(filterRequest.getColumnName()) ? "endUserDB.nationality"
									: "Tax Paid Status".equalsIgnoreCase(filterRequest.getColumnName()) ? "taxPaidStatus"
											: "Origin".equalsIgnoreCase(filterRequest.getColumnName())
													? "origin"
													:"Status".equalsIgnoreCase(filterRequest.getColumnName())
															? "status" : "modifiedOn";
			Sort.Direction direction;
			if("modifiedOn".equalsIgnoreCase(orderColumn)) {
				direction=Sort.Direction.DESC;
			}
			else {
				direction= SortDirection.getSortDirection(filterRequest.getSort());
			}
			Pageable pageable = PageRequest.of(pageNo, pageSize, new Sort(direction, orderColumn));
		
			
			if(filterRequest.getTaxPaidStatus() != TaxStatus.BLOCKED.getCode()) {
				// TODO
			}
			stateList = stateMgmtServiceImpl.getByFeatureIdAndUserTypeId(filterRequest.getFeatureId(), filterRequest.getUserTypeId());
			logger.info(stateList);
			logger.info("dialect : " + propertiesReader.dialect);

			Page<RegularizeDeviceDb> page = regularizedDeviceDbRepository.findAll(buildSpecification(filterRequest,stateList, source).build(), pageable);
			logger.info(page.getContent().toString());
			for(RegularizeDeviceDb regularizeDeviceDb : page.getContent()) {
//				logger.info(regularizeDeviceDb.toString());
				for(StateMgmtDb stateMgmtDb : stateList) {
					if(regularizeDeviceDb.getStatus() == stateMgmtDb.getState()) {
						regularizeDeviceDb.setStateInterp(stateMgmtDb.getInterp()); 
						break; 
					} 
				}

				if(Objects.nonNull(regularizeDeviceDb.getEndUserDB())) {
					logger.info(regularizeDeviceDb.getEndUserDB().toString());
					regularizeDeviceDb.setNationality(regularizeDeviceDb.getEndUserDB().getNationality());
//					EndUserDB endUser=regularizeDeviceDb.getEndUserDB();
					regularizeDeviceDb.getEndUserDB().setRegularizeDeviceDbs(new ArrayList<>(1));
//					logger.info(regularizeDeviceDb.getEndUserDB().toString());
				}
				setInterp(regularizeDeviceDb);
			}


			// Save in audit.
			String username="";
			int userId=0;
			if(Objects.nonNull(filterRequest.getUserType()))
			{
				if("End User".equalsIgnoreCase(filterRequest.getUserType())){
					logger.info("usertype is end user so setting username is empty");
					username="NA";
				}	
				else {

					user = userRepository.getById(filterRequest.getUserId());
					username=user.getUsername();
					userId=filterRequest.getUserId();
				}
			}
			if(source.equalsIgnoreCase("menu")) {
				AuditTrail auditTrail = new AuditTrail(userId, 
						username, 
						Long.valueOf(filterRequest.getUserTypeId()), 
						filterRequest.getUserType(), 
						12, Features.REGISTER_DEVICE, 
						SubFeatures.VIEW_ALL, 
						"", "NA",filterRequest.getUserType(),filterRequest.getPublicIp(),filterRequest.getBrowser());
				auditTrailRepository.save(auditTrail);
				logger.info("AUDIT : View in audit_trail. " + auditTrail);
			}
			else {
				AuditTrail auditTrail = new AuditTrail(userId, 
						username, 
						Long.valueOf(filterRequest.getUserTypeId()), 
						filterRequest.getUserType(), 
						12, Features.REGISTER_DEVICE, 
						SubFeatures.FILTER, 
						"", "NA",filterRequest.getUserType(),filterRequest.getPublicIp(),filterRequest.getBrowser());
				auditTrailRepository.save(auditTrail);
				logger.info("AUDIT : View in audit_trail. " + auditTrail);
			}
			
			
			return page;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException("Regularized Device Service", e.getMessage());
		}
	}

	public FileDetails getFilteredDeviceInFile(FilterRequest filterRequest, String source) {
		String fileName = null;
		Writer writer   = null;
		RegularizeDeviceFileModel rdfm = null;
		
		CustomMappingStrategy<RegularizeDeviceFileModel> mappingStrategy = new CustomMappingStrategy<RegularizeDeviceFileModel>();
		DateTimeFormatter dtf  = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		DateTimeFormatter dtf2  = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

		SystemConfigurationDb filepath = configurationManagementServiceImpl.findByTag(ConfigTags.file_download_dir);
		logger.info("CONFIG : file_consignment_download_dir [" + filepath + "]");
		SystemConfigurationDb link = configurationManagementServiceImpl.findByTag(ConfigTags.file_download_link);
		logger.info("CONFIG : file_consignment_download_link [" + link + "]");

		String filePath = filepath.getValue();
		StatefulBeanToCsvBuilder<RegularizeDeviceFileModel> builder = null;
		StatefulBeanToCsv<RegularizeDeviceFileModel> csvWriter = null;
		List< RegularizeDeviceFileModel > fileRecords = null;

		try {
			List<RegularizeDeviceDb> regularizeDevices = getAll(filterRequest, source);
			logger.info("Data to be exported -----------"+regularizeDevices);	
			fileName = LocalDateTime.now().format(dtf2).replace(" ", "_") + "_RegularizeDevice.csv";
			mappingStrategy.setType(RegularizeDeviceFileModel.class);
			writer = Files.newBufferedWriter(Paths.get(filePath+fileName));
			builder = new StatefulBeanToCsvBuilder<RegularizeDeviceFileModel>(writer);
			//csvWriter = builder.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).build();
			csvWriter = builder.withMappingStrategy(mappingStrategy).withSeparator(',').withQuotechar(CSVWriter.DEFAULT_QUOTE_CHARACTER).build();
			if( !regularizeDevices.isEmpty() ) {

				List<SystemConfigListDb> currencyList = configurationManagementServiceImpl.getSystemConfigListByTag(Tags.CURRENCY);

				fileRecords = new ArrayList<>(); 

				for(RegularizeDeviceDb regularizeDeviceDb : regularizeDevices ) {

					rdfm = new RegularizeDeviceFileModel();
					/*
					 * if(!Objects.nonNull(regularizeDeviceDb.getDeviceIdTypeInterp())) {
					 * rdfm.setDeviceIdTypeInterp("NA"); }
					 * if(!Objects.nonNull(regularizeDeviceDb.getDeviceTypeInterp())) {
					 * rdfm.setDeviceTypeInterp("NA"); }
					 */
					
					if(!Objects.nonNull(regularizeDeviceDb.getSecondImei()) || regularizeDeviceDb.getSecondImei().equals("")) {
						rdfm.setSecondImei("NA");
					}
					if(!Objects.nonNull(regularizeDeviceDb.getThirdImei()) || regularizeDeviceDb.getThirdImei().equals("")) {
						rdfm.setThirdImei("NA");	
					}
					if(!Objects.nonNull(regularizeDeviceDb.getFourthImei()) || regularizeDeviceDb.getFourthImei().equals("")) {
						rdfm.setFourthImei("NA");	
					}
					/*
					 * if(!Objects.nonNull(regularizeDeviceDb.getCountry())) {
					 * rdfm.setCountry("NA");
					 * 
					 * } if(!Objects.nonNull(regularizeDeviceDb.getDeviceStatus())) {
					 * rdfm.setDeviceStatus("NA"); }
					 */
					rdfm.setCreatedOn(regularizeDeviceDb.getCreatedOn().format(dtf));
					rdfm.setModifiedOn(regularizeDeviceDb.getModifiedOn().format(dtf));
					//rdfm.setDeviceIdTypeInterp(regularizeDeviceDb.getDeviceIdTypeInterp());
					//rdfm.setDeviceTypeInterp(regularizeDeviceDb.getDeviceTypeInterp());
					//rdfm.setPrice( regularizeDeviceDb.getPrice());
					rdfm.setTaxPaidStatus(regularizeDeviceDb.getTaxPaidStatusInterp());
					rdfm.setFirstImei(regularizeDeviceDb.getFirstImei());
					rdfm.setSecondImei(regularizeDeviceDb.getSecondImei());
					rdfm.setThirdImei(regularizeDeviceDb.getThirdImei());
					rdfm.setFourthImei(regularizeDeviceDb.getFourthImei());
					rdfm.setTxnId(regularizeDeviceDb.getTxnId());
					rdfm.setOrigin(regularizeDeviceDb.getOrigin());
					rdfm.setNid(regularizeDeviceDb.getNid());
					rdfm.setNationality(regularizeDeviceDb.getEndUserDB().getNationality());
					rdfm.setStatus(regularizeDeviceDb.getStateInterp());
					for(SystemConfigListDb systemConfigListDb : currencyList) {
						if(regularizeDeviceDb.getCurrency() == Integer.valueOf(systemConfigListDb.getValue())) {
							if(!Objects.nonNull(systemConfigListDb.getInterp())) {
								//rdfm.setCurrency("NA"); 	
							}
							//rdfm.setCurrency(systemConfigListDb.getInterp()); 
							break;
						} 
					}
					//rdfm.setCountry(regularizeDeviceDb.getCountry());
					rdfm.setTaxPaidStatus(regularizeDeviceDb.getTaxPaidStatusInterp());

					logger.debug(rdfm);

					fileRecords.add(rdfm);
				}

				csvWriter.write(fileRecords);
			}else {
				csvWriter.write( new RegularizeDeviceFileModel());
			}

			// Save in audit.
			// Save in audit.
			String username="";
			int userId=0;
			User user=new User();
			if(Objects.nonNull(filterRequest.getUserType()))
			{

				if("End User".equalsIgnoreCase(filterRequest.getUserType())){
					logger.info("usertype is end user so setting username is empty");
					username="";
				}	
				else {

					user = userRepository.getById(filterRequest.getUserId());
					username=user.getUsername();
					userId=filterRequest.getUserId();
				}
			}
			AuditTrail auditTrail = new AuditTrail(userId, 
					username, 
					Long.valueOf(filterRequest.getUserTypeId()), 
					filterRequest.getUserType(), 
					12, Features.REGISTER_DEVICE, 
					SubFeatures.EXPORT, 
					"", "NA",filterRequest.getUserType(),filterRequest.getPublicIp(),filterRequest.getBrowser());
			auditTrailRepository.save(auditTrail);
			logger.info("AUDIT : export in audit_trail. ");

			return new FileDetails(fileName, filePath, link.getValue().replace("$LOCAL_IP",
					propertiesReader.localIp) + fileName ); 

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}finally {
			try {

				if( Objects.nonNull(writer) )
					writer.close();
			} catch (IOException e) {}
		}
	}

	@Transactional
	public GenricResponse saveDevices(EndUserDB endUserDB) {
		try {
			String username="";
			long userId=0;
			if(endUserDB.getAuditParameters().getUserTypeId()!=17) {
				username=endUserDB.getAuditParameters().getUsername();
				userId=endUserDB.getAuditParameters().getUserId();
				endUserDB.setCreatorUserId(endUserDB.getAuditParameters().getUserId());
				for(RegularizeDeviceDb regularizeData:endUserDB.getRegularizeDeviceDbs())
				{
					if(Objects.isNull(regularizeData.getCreatorUserId()) || regularizeData.getCreatorUserId()==0)
						regularizeData.setCreatorUserId(endUserDB.getCreatorUserId());
				}
			}
			String transactionId="";
			for(RegularizeDeviceDb regularizeData:endUserDB.getRegularizeDeviceDbs())
			{
				transactionId=regularizeData.getTxnId();
			}
			logger.info("transaction id:"+transactionId);
			auditTrailRepository.save(new AuditTrail(userId, username, 17L,
					endUserDB.getAuditParameters().getUserType(), 12,Features.REGISTER_DEVICE,
					SubFeatures.Add_Device, "",transactionId,endUserDB.getAuditParameters().getUserType(),endUserDB.getPublicIp(),endUserDB.getBrowser()));
			logger.info("AUDIT : Saved request in audit.");

			String txnId = null;
			List<WebActionDb> webActionDbs = new ArrayList<>();
			String nid = endUserDB.getNid();
			if(Objects.isNull(endUserDB.getNid())) {
				logger.info(GenericMessageTags.NULL_NID);
				return new GenricResponse(1, GenericMessageTags.NULL_NID.getTag(), 
						GenericMessageTags.NULL_NID.getMessage(), 
						"");
			}

			EndUserDB endUserDB2 = endUserDbRepository.getByNid(nid);
			Integer type=null;


			logger.info("nationality= "+endUserDB2.getNationality());
			if(Objects.nonNull(endUserDB2)) {
				if("Cambodian".equalsIgnoreCase(endUserDB2.getNationality())) {
					type=1;
				}
				else {
					type=2;
				}	
			}
			if(endUserDB.getAuditParameters().getUserTypeId()!=7)
			{	
				for(RegularizeDeviceDb regularizeDb:endUserDB.getRegularizeDeviceDbs())
				{
					if(type==1)
					{
						regularizeDb.setTaxPaidStatus(TaxStatus.TAX_NOT_PAID.getCode());
					}
					else {
						regularizeDb.setTaxPaidStatus(TaxStatus.REGULARIZED.getCode());				
					}
				}
			}

			if(!endUserDB.getRegularizeDeviceDbs().isEmpty()) {
				if(validateRegularizedDevicesCount(nid, endUserDB.getRegularizeDeviceDbs(),type,endUserDB.getAuditParameters().getUserTypeId())) {
					for(RegularizeDeviceDb regularizeDeviceDb : endUserDB.getRegularizeDeviceDbs()) {
						// TODO     responsse 5
						if(Objects.isNull(endUserDB2)) {
							regularizeDeviceDb.setEndUserDB(endUserDB);
						}
						else {
							regularizeDeviceDb.setEndUserDB(endUserDB2);	
						}

						if(commonFunction.hasDuplicateImeiInRequest(endUserDB.getRegularizeDeviceDbs())) {
							return new GenricResponse(6,GenericMessageTags.DUPLICATE_IMEI_IN_REQUEST.getTag(),GenericMessageTags.DUPLICATE_IMEI_IN_REQUEST.getMessage(), ""); 
						}

						if(commonFunction.checkAllImeiOfRegularizedDevice(regularizeDeviceDb)) {
							return new GenricResponse(5,GenericMessageTags.DUPLICATE_IMEI.getTag(),GenericMessageTags.DUPLICATE_IMEI.getMessage(), "");
						}
						if(Objects.isNull(regularizeDeviceDb.getTaxPaidStatus())) {
							regularizeDeviceDb.setTaxPaidStatus(TaxStatus.TAX_NOT_PAID.getCode());
						}

						if(Objects.isNull(regularizeDeviceDb.getStatus())) {
							regularizeDeviceDb.setStatus(RegularizeDeviceStatus.NEW.getCode());
						}

						if(Objects.isNull(regularizeDeviceDb.getTxnId())) {
							regularizeDeviceDb.setTxnId(endUserDB.getTxnId());
							txnId = endUserDB.getTxnId();
						}
						else {
							endUserDB.setTxnId(regularizeDeviceDb.getTxnId());
							txnId = regularizeDeviceDb.getTxnId();
						}

						if(Objects.isNull(endUserDB.getOrigin())) {
							endUserDB.setOrigin(regularizeDeviceDb.getOrigin());
						}

						if(endUserDB.getAuditParameters().getUserTypeId()==7) {
							if(regularizeDeviceDb.getTaxPaidStatus()==TaxStatus.TAX_PAID.getCode())
							{
								regularizeDeviceDb.setTaxCollectedBy(username);
								logger.info("if usertype is custom and tax status is paid so now this entry going to web action db");
								webActionDbs.add(new WebActionDb(Features.REGISTER_DEVICE, SubFeatures.Clear, 0, 
										regularizeDeviceDb.getTxnId()));
							}
						}
						// Add in web action list.
						webActionDbs.add(new WebActionDb(Features.REGISTER_DEVICE, SubFeatures.Add_Device, 0, 
								regularizeDeviceDb.getTxnId()));
					}

					logger.info(endUserDB.getRegularizeDeviceDbs());

					boolean executionSuccess = Boolean.FALSE;

					// Start query execution.
					if(Objects.isNull(endUserDB2)) {
						// End user is not registered with CEIR system.
						executionSuccess = regularizeDeviceTransaction.executeSaveDevices(webActionDbs, endUserDB);
					}else {
						executionSuccess = regularizeDeviceTransaction.executeSaveDevices(webActionDbs, endUserDB.getRegularizeDeviceDbs());
					}

					// Return message to the client.
					if(executionSuccess) {
						logger.info("End user device registration is sucessful.");
						String mailTag = "END_USER_NEW_DEVICE_ADD";
						List<RawMail> rawMails = new ArrayList<>();
						Map<String, String> placeholderMap = new HashMap<String, String>();

						// Mail to End user. 
						if(Objects.nonNull(endUserDB2.getEmail()) && !endUserDB2.getEmail().isEmpty() && !"NA".equalsIgnoreCase(endUserDB2.getEmail())) {
							placeholderMap.put("<First name>", endUserDB2.getFirstName());
							placeholderMap.put("<Txn id>",transactionId);
							rawMails.add(new RawMail(mailTag, endUserDB2.getId(), Long.valueOf(12), 
									Features.REGISTER_DEVICE, SubFeatures.REGISTER, transactionId, 
									transactionId, placeholderMap, ReferTable.END_USER, null, "End User"));
							emailUtil.saveNotification(rawMails);	
						}
						else {
							logger.info("this end user don't have any email");
						}

						logger.info("raw email size: "+rawMails.size());

						return new GenricResponse(0, "End user device registration is sucessful.", txnId);
					}else {
						logger.info("End user device registration have been failed" + endUserDB);
						return new GenricResponse(2,GenericMessageTags.DEVICE_REGISTRATION_FAILED.getTag(),GenericMessageTags.DEVICE_REGISTRATION_FAILED.getMessage(), "");
					}

				}else {
					logger.warn("Regularized Devices are exceeding the allowed count." + endUserDB);
					return new GenricResponse(3,GenericMessageTags.REGULARISED_DEVICE_EXCEEDED.getTag(),GenericMessageTags.REGULARISED_DEVICE_EXCEEDED.getMessage(), "");
				}

			}else {
				return new GenricResponse(4, "No device found in the request.", "");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException("Custom Service", e.getMessage());
		}
	}



	public GenricResponse updateTaxStatus( RegularizeDeviceDb regularizeDeviceDb) {
		try {
			String tag = null;
			String receiverUserType = null;
			String mailSubject = null;
			List<RawMail> rawMails = new ArrayList<>(1);
			Map<String, String> placeholders = new HashMap<>();
			AllRequest audit=regularizeDeviceDb.getAuditParameters();
			if( userStaticServiceImpl.checkIfUserIsDisabled( audit.getUserId() ))
				return new GenricResponse(5, "USER_IS_DISABLED","This account is disabled. Please enable the account to perform the operation.",
						null);
			logger.info("txn_id is : "+regularizeDeviceDb.getTxnId());
			AuditTrail auditTrail = new AuditTrail(audit.getUserId(), audit.getUsername(), audit.getUserTypeId(), 
					audit.getUserType(), 12, Features.REGISTER_DEVICE, 
					SubFeatures.Tax_Paid, "",regularizeDeviceDb.getTxnId(),audit.getUserType(),regularizeDeviceDb.getPublicIp(),regularizeDeviceDb.getBrowser());
			auditTrailRepository.save(auditTrail);
			logger.info("AUDIT : update in audit_trail. " + auditTrail);
			RegularizeDeviceDb userCustomDbDetails = regularizedDeviceDbRepository.getByFirstImei(regularizeDeviceDb.getFirstImei());
//			UserProfile ceirAdminProfile = userStaticServiceImpl.getCeirAdmin().getUserProfile();

			if(Objects.nonNull(userCustomDbDetails)) {
				userCustomDbDetails.setTaxPaidStatus(regularizeDeviceDb.getTaxPaidStatus());
				userCustomDbDetails.setTaxCollectedBy(audit.getUsername());
				RegularizeDeviceDb output=regularizedDeviceDbRepository.save(userCustomDbDetails);
				if(Objects.nonNull(output))
				{
					WebActionDb webAction=new WebActionDb(Features.REGISTER_DEVICE,SubFeatures.Clear, 0, 
							regularizeDeviceDb.getTxnId());
					webActionDbRepository.save(webAction);
					Map<String, String> placeholderMap = new HashMap<String, String>();
					EndUserDB endUserDb=output.getEndUserDB();
					// Mail to End user. 
					if(Objects.nonNull(endUserDb.getEmail()) && !endUserDb.getEmail().isEmpty() && !"NA".equalsIgnoreCase(endUserDb.getEmail())) {
						placeholderMap.put("<First name>", endUserDb.getFirstName());
						placeholderMap.put("<Txn id>",regularizeDeviceDb.getTxnId());
						String mailTag = "Tax_Pay_Msg";
						rawMails.add(new RawMail(mailTag, endUserDb.getId(), Long.valueOf(12), 
								Features.REGISTER_DEVICE, SubFeatures.Tax_Paid, regularizeDeviceDb.getTxnId(), 
								regularizeDeviceDb.getTxnId(), placeholderMap, ReferTable.END_USER, null, "End User"));
						emailUtil.saveNotification(rawMails);	
					}
				}
				/*
				 * placeholders.put("<FIRST_NAME>", ceirAdminProfile.getFirstName());
				 * placeholders.put("<txn_id>", regularizeDeviceDb.getTxnId());
				 */
				/*
				 * // Send Notifications if(regularizeDeviceDb.getTaxPaidStatus() == 0) { //
				 * Mail to CEIR Admin on tax update status change. tag =
				 * "MAIL_TO_CEIR_ADMIN_ON_DEVICE_TAX_PAID"; receiverUserType = "CEIRAdmin";
				 * mailSubject =
				 * MailSubject.MAIL_TO_CEIR_ADMIN_ON_DEVICE_TAX_PAID.replace("<XXX>",
				 * userCustomDbDetails.getTxnId()); }else { tag =
				 * "MAIL_TO_CEIR_ADMIN_ON_DEVICE_TAX_NOT_PAID"; receiverUserType = "CEIRAdmin";
				 * mailSubject =
				 * MailSubject.MAIL_TO_CEIR_ADMIN_ON_DEVICE_TAX_NOT_PAID.replace("<XXX>",
				 * userCustomDbDetails.getTxnId()); } rawMails.add(new RawMail(tag,
				 * ceirAdminProfile, 4, Features.REGISTER_DEVICE, SubFeatures.REGISTER,
				 * regularizeDeviceDb.getTxnId(), mailSubject, placeholders, ReferTable.USERS,
				 * null, receiverUserType));
				 * 
				 * 
				 * emailUtil.saveNotification(rawMails);
				 */

				// Save in audit.


				return new GenricResponse(0, "Update Successfully.", userCustomDbDetails.getFirstImei());

			}else {
				return  new GenricResponse(4,"TxnId Does Not exist.", "");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException("Custom Service", e.getMessage());}
	}

	public RegularizeDeviceDb viewDeviceInfoByImei1(RegularizeDeviceView data) {
		try {
			logger.info("Going to get deviceInfo Info for imei : " + data.getImei());
			String username="";
			long userId=0;
			if(data.getUserTypeId()!=17) {
				username=data.getUsername();
				userId=data.getUserId();
			}
			auditTrailRepository.save(new AuditTrail(userId, username, 17L,
					data.getUserType(), 12,Features.REGISTER_DEVICE, SubFeatures.VIEW, "",data.getTxnId(),data.getUserType(),data.getPublicIp(),data.getBrowser()));
			logger.info("AUDIT : Saved request in audit.");


			if(Objects.isNull(data.getImei())) {
				throw new IllegalArgumentException();
			}
			RegularizeDeviceDb regularizeDeviceDb = new RegularizeDeviceDb();
			try {
				regularizeDeviceDb = regularizedDeviceDbRepository.getByFirstImei(data.getImei());	

			}catch(Exception e) {
				logger.info("throwing : ResourceServicesException : " + e.getMessage());
				// return new GenricResponse(5,GenericMessageTags.DUPLICATE_IMEI.getTag(),GenericMessageTags.DUPLICATE_IMEI.getMessage(), "");
				throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
			}

			if(Objects.nonNull(regularizeDeviceDb)) {
				EndUserDB endUserDB = endUserDbRepository.getByNid(regularizeDeviceDb.getNid());

				endUserDB.setRegularizeDeviceDbs(new ArrayList<>(1));
				if(Objects.nonNull(endUserDB.getDocType())) {
					endUserDB.setDocTypeInterp(interpSetter.setTagId(Tags.DOC_TYPE, endUserDB.getDocType()));	
					endUserDB.setDocumentInterp(interpSetter.setConfigInterp(Tags.DOC_TYPE, endUserDB.getDocType()));	
				}
				if(Objects.nonNull(endUserDB.getVisaDb())) {
					List<VisaDb> visaList=new ArrayList<VisaDb>();
					for(VisaDb visa:endUserDB.getVisaDb()) {
						visa.setVisaTypeInterp(interpSetter.setConfigInterp(Tags.VISA_TYPE, visa.getVisaType()));	
						visaList.add(visa);
					}
					endUserDB.setVisaDb(visaList);
				}
				//VISA_TYPE
				regularizeDeviceDb.setEndUserDB(endUserDB);

				setInterp(regularizeDeviceDb);
			}
			return regularizeDeviceDb;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException("Custom Service", e.getMessage());
		}

	}

	@Transactional
	public GenricResponse deleteCustomInfo(RegularizeDeviceView data) {
		try {
			logger.info("inside delete regularize service");
			String username="";
			long userId=0;
			if(data.getUserTypeId()!=17) {
				if( userStaticServiceImpl.checkIfUserIsDisabled( data.getUserId() ))
					return new GenricResponse(5, "USER_IS_DISABLED","This account is disabled. Please enable the account to perform the operation.",
							null);
				username=data.getUsername();
				userId=data.getUserId();
			}
			auditTrailRepository.save(new AuditTrail(userId, username, 17L,
					data.getUserType(), 12,Features.REGISTER_DEVICE, SubFeatures.DELETE, "", data.getTxnId(),data.getUserType(),data.getPublicIp(),data.getBrowser()));
			logger.info("AUDIT : Saved request in audit.");
			RegularizeDeviceDb regularizeDeviceDb = regularizedDeviceDbRepository.getByFirstImei(data.getImei());

			if(Objects.nonNull(regularizeDeviceDb)) {
//				regularizeDeviceDb.setStatus(RegularizeDeviceStatus.WITHDRAWN_BY_CEIR_ADMIN.getCode());
//				regularizeDeviceDb.setApprovedBy(username);
//				regularizedDeviceDbRepository.save(regularizeDeviceDb);
				regularizedDeviceDbRepository.deleteById(regularizeDeviceDb.getId());
				return new GenricResponse(0, "Device have been deleted sucessfully.", regularizeDeviceDb.getFirstImei());
			}else {
				return new GenricResponse(4, "This IMEI does not exist.", "");	
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException("Custom Service", e.getMessage());
		}
	}

	@Transactional
	public GenricResponse acceptReject(CeirActionRequest ceirActionRequest) {
		try {
			String tag = null;
			String receiverUserType = null;
			String txnId = null;
			EndUserDB endUserDB = null;
			List<RawMail> rawMails = new ArrayList<>();
			Map<String, String> placeholders = new HashMap<>();
			long userTypeId=0;
			long userId=0;
			String subFeature="";
			String username="";
			RegularizeDeviceDb regularizeDeviceDb = null;
			if( !"CEIRSYSTEM".equalsIgnoreCase(ceirActionRequest.getUserType()) && userStaticServiceImpl.checkIfUserIsDisabled( ceirActionRequest.getUserId() ))
				return new GenricResponse(5, "USER_IS_DISABLED","This account is disabled. Please enable the account to perform the operation.",
						null);
			lock.lock();
			logger.info("lock taken by thread : " + Thread.currentThread().getName());

			if("CEIRADMIN".equalsIgnoreCase(ceirActionRequest.getUserType())){

				regularizeDeviceDb = regularizedDeviceDbRepository.getByFirstImei(ceirActionRequest.getImei1());

				if(Objects.isNull(regularizeDeviceDb)){
					return new GenricResponse(1, "First imei is incorrect", "");            	
				}

				endUserDB = endUserDbRepository.getByNid(regularizeDeviceDb.getNid());
				placeholders.put("<Txn id>", regularizeDeviceDb.getTxnId());
				placeholders.put("<First name>", endUserDB.getFirstName());

				userId = ceirActionRequest.getUserId();
				userTypeId = 8;
				if(Objects.nonNull(ceirActionRequest.getUsername())) {
					username = ceirActionRequest.getUsername();
				}
				regularizeDeviceDb.setApprovedBy(username);
				if(ceirActionRequest.getAction() == 0) {
					// Check if someone else taken the same action on consignment.
					RegularizeDeviceDb regularizeDeviceDbTemp = regularizedDeviceDbRepository.getByTxnId(ceirActionRequest.getTxnId());
					if(RegularizeDeviceStatus.APPROVED.getCode() == regularizeDeviceDbTemp.getStatus()) {
						String message = "Any other user have taken the same action on the stock [" + txnId + "]";
						logger.info(message);
						return new GenricResponse(10, "", message, txnId);
					}

					regularizeDeviceDb.setStatus(RegularizeDeviceStatus.APPROVED.getCode());
					regularizeDeviceDb.setRemark(null);
					tag = "MAIL_TO_USER_ON_CEIR_DEVICE_APPROVAL";
					receiverUserType = "End User";
					subFeature = SubFeatures.Approve;
					txnId = regularizeDeviceDb.getTxnId();
					enduserServiceImpl.updateImeiInVipList(regularizeDeviceDb.getEndUserDB(), username);
				}else if(ceirActionRequest.getAction() == 1){
					// Check if someone else taken the same action on consignment.
					RegularizeDeviceDb regularizeDeviceDbTemp = regularizedDeviceDbRepository.getByTxnId(ceirActionRequest.getTxnId());
					if(RegularizeDeviceStatus.REJECTED_BY_CEIR_ADMIN.getCode() == regularizeDeviceDbTemp.getStatus()) {
						String message = "Any other user have taken the same action on the stock [" + txnId + "]";
						logger.info(message);
						return new GenricResponse(10, "", message, txnId);
					}

					regularizeDeviceDb.setStatus(RegularizeDeviceStatus.REJECTED_BY_CEIR_ADMIN.getCode());
					regularizeDeviceDb.setRemark(ceirActionRequest.getRemarks());
					tag = "MAIL_TO_USER_ON_CEIR_DEVICE_DISAPPROVAL";	
					receiverUserType = "End User";
					txnId = regularizeDeviceDb.getTxnId();
					subFeature = SubFeatures.REJECT;
					placeholders.put("<Reason>", regularizeDeviceDb.getRemark() );
				}else {
					return new GenricResponse(2, "unknown operation", "");
				}
				// Send Notifications
				if(Objects.nonNull(endUserDB)) {
					if(Objects.nonNull(endUserDB.getEmail()) 
							&& !endUserDB.getEmail().isEmpty()
							&& !"NA".equalsIgnoreCase(endUserDB.getEmail())) {

						rawMails.add(new RawMail(tag, 
								endUserDB.getId(), 
								12, 
								Features.REGISTER_DEVICE, 
								subFeature, 
								regularizeDeviceDb.getTxnId(), 
								regularizeDeviceDb.getTxnId(), 
								placeholders,
								ReferTable.END_USER,
								null,
								receiverUserType));
					}
					else {
						logger.info("this end user don't have any email");
					}
				}

				RegularizeDeviceDb regularizeOutput = regularizedDeviceDbRepository.save(regularizeDeviceDb);

				if(Objects.nonNull(regularizeOutput))
				{
					WebActionDb webAction=new WebActionDb(Features.REGISTER_DEVICE,subFeature, 0, 
							regularizeDeviceDb.getTxnId());
					webAction.setSubFeature(WebActionDbSubFeature.REJECT.getName());
					webActionDbRepository.save(webAction);
					if(Objects.nonNull(rawMails) && !rawMails.isEmpty()) {
						emailUtil.saveNotification(rawMails);	
					}

				}
				auditTrailRepository.save(new AuditTrail(userId, username, userTypeId,
						ceirActionRequest.getUserType(), 12, Features.REGISTER_DEVICE, subFeature, "", txnId,ceirActionRequest.getUserType()));
			}else if("CEIRSYSTEM".equalsIgnoreCase(ceirActionRequest.getUserType())){
				List<RegularizeDeviceDb> regularizeList = regularizedDeviceDbRepository.findByTxnId(ceirActionRequest.getTxnId());
				regularizeList = regularizedDeviceDbRepository.findByTxnId(ceirActionRequest.getTxnId());
				
				if(regularizeList.isEmpty()){
					logger.info("txn Id [" + ceirActionRequest.getTxnId() + "] not found.");
					return new GenricResponse(1, "transaction id is incorrect", "");            	
				}
				
				regularizeDeviceDb = regularizeList.get(0);
				endUserDB = endUserDbRepository.getByNid(regularizeDeviceDb.getNid());

				placeholders.put("<Txn id>", regularizeDeviceDb.getTxnId());
				placeholders.put("<First name>", endUserDB.getFirstName());

				userTypeId = 0;
				if(ceirActionRequest.getAction() == 0) {
					// Check if someone else taken the same action on consignment.
					RegularizeDeviceDb regularizeDeviceDbTemp = regularizedDeviceDbRepository.getByTxnId(ceirActionRequest.getTxnId());
					if(RegularizeDeviceStatus.PROCESSING.getCode() == regularizeDeviceDbTemp.getStatus()) {
						String message = "Any other user have taken the same action on the stock [" + txnId + "]";
						logger.info(message);
						return new GenricResponse(10, "", message, txnId);
					}

					regularizeList.stream().forEach((r) -> {
						r.setStatus(RegularizeDeviceStatus.PROCESSING.getCode());
					});

					regularizedDeviceDbRepository.saveAll(regularizeList);
				}else if(ceirActionRequest.getAction() == 1){
					// Check if someone else taken the same action on consignment.
					RegularizeDeviceDb regularizeDeviceDbTemp = regularizedDeviceDbRepository.getByTxnId(ceirActionRequest.getTxnId());
					if(RegularizeDeviceStatus.REJECTED_BY_SYSTEM.getCode() == regularizeDeviceDbTemp.getStatus()) {
						String message = "Any other user have taken the same action on the stock [" + txnId + "]";
						logger.info(message);
						return new GenricResponse(10, "", message, txnId);
					}

					regularizeList.stream().forEach((r) -> {
						r.setStatus(RegularizeDeviceStatus.REJECTED_BY_SYSTEM.getCode());
					});

					tag = "MAIL_TO_USER_ON_CEIR_DEVICE_DISAPPROVAL";	
					receiverUserType = "End User";
					txnId = regularizeDeviceDb.getTxnId();
					subFeature = SubFeatures.SYSTEM_REJECT;

					if(Objects.nonNull(endUserDB.getEmail()) 
							&& !endUserDB.getEmail().isEmpty() 
							&& !"NA".equalsIgnoreCase(endUserDB.getEmail())) {
						rawMails.add(new RawMail("Reg_Device_Process_Fail_To_EndUser", 
								endUserDB.getId(), 
								12, 
								Features.REGISTER_DEVICE, 
								SubFeatures.SYSTEM_REJECT, 
								regularizeDeviceDb.getTxnId(), 
								regularizeDeviceDb.getTxnId(), 
								placeholders,
								ReferTable.END_USER,
								null,
								receiverUserType));
					}
					List<RegularizeDeviceDb> regularizeOutput = regularizedDeviceDbRepository.saveAll(regularizeList);

					if(Objects.nonNull(regularizeOutput)&&!regularizeOutput.isEmpty()){
						if(Objects.nonNull(rawMails) && !rawMails.isEmpty()) {
							emailUtil.saveNotification(rawMails);	
						}
					}

				}else if(ceirActionRequest.getAction() == 2) {
					// Check if someone else taken the same action on consignment.
					RegularizeDeviceDb regularizeDeviceDbTemp = regularizedDeviceDbRepository.getByTxnId(ceirActionRequest.getTxnId());
					if(RegularizeDeviceStatus.PENDING_APPROVAL_FROM_CEIR_ADMIN.getCode() == regularizeDeviceDbTemp.getStatus()) {
						String message = "Any other user have taken the same action on the stock [" + txnId + "]";
						logger.info(message);
						return new GenricResponse(10, "", message, txnId);
					}

					regularizeList.stream().forEach((r) -> {
						r.setStatus(RegularizeDeviceStatus.PENDING_APPROVAL_FROM_CEIR_ADMIN.getCode());
					});

					regularizeDeviceDb = regularizeList.get(0);
					tag = "MAIL_TO_USER_ON_CEIR_DEVICE_APPROVAL";
					txnId = regularizeDeviceDb.getTxnId();
//					List<User> user= new ArrayList<User>();
//					user = userStaticServiceImpl.getUserbyUsertypeId(8);
//					UserProfile ceirUserProfile = new UserProfile();
//					ceirUserProfile.setUser(userStaticServiceImpl.getCeirAdmin());

					subFeature = SubFeatures.SYSTEM_ACCEPT;
					if(Objects.nonNull(endUserDB.getEmail()) 
							&& !endUserDB.getEmail().isEmpty() 
							&& !"NA".equalsIgnoreCase(endUserDB.getEmail())) {
						rawMails.add(new RawMail("Reg_Device_Process_success_To_EndUser", 
								endUserDB.getId(), 
								12, 
								Features.REGISTER_DEVICE, 
								SubFeatures.SYSTEM_ACCEPT, 
								regularizeDeviceDb.getTxnId(), 
								regularizeDeviceDb.getTxnId(), 
								placeholders,
								ReferTable.END_USER,
								null,
								"End User"));
					}

					Generic_Response_Notification generic_Response_Notification = userFeignClient.ceirInfoByUserTypeId(8);

					logger.info("generic_Response_Notification::::::::"+generic_Response_Notification);

					List<RegisterationUser> registerationUserList = generic_Response_Notification.getData();
					
					
					for(RegisterationUser registerationUser :registerationUserList) {
						UserProfile userProfile_generic_Response_Notification = new UserProfile();
						userProfile_generic_Response_Notification = userProfileRepository.getByUserId(registerationUser.getId());
						placeholders= new HashMap<String, String>();
						placeholders.put("<Txn id>", regularizeDeviceDb.getTxnId());
						placeholders.put("<First name>", userProfile_generic_Response_Notification.getFirstName());
//					for(User userData : user) {

						rawMails.add(new RawMail("Reg_Device_Process_success_mail_To_CEIRAdmin", 
								registerationUser.getId(), 
								12, 
								Features.REGISTER_DEVICE, 
								SubFeatures.SYSTEM_ACCEPT, 
								regularizeDeviceDb.getTxnId(), 
								regularizeDeviceDb.getTxnId(), 
								placeholders,
								ReferTable.USERS,
								null,
								"CEIRAdmin"));
					}

					RegularizeDeviceDb regularizeOutput = regularizedDeviceDbRepository.save(regularizeDeviceDb);

					if(Objects.nonNull(regularizeOutput)){
						if(Objects.nonNull(rawMails) && !rawMails.isEmpty()) {
							if(regularizeDeviceDb.getStatus()==RegularizeDeviceStatus.PENDING_APPROVAL_FROM_CEIR_ADMIN.getCode()) {
								emailUtil.saveNotification(rawMails);				
							}
						}
					}	

				}else {
					return new GenricResponse(2, "unknown operation", "");
				}
			}
			else {
				userTypeId = 0;
				userId = 0;
				subFeature = "";
				logger.info("Usertype[" + ceirActionRequest.getUserType() + "] is not allowed to take an action on txnId[" 
						+ ceirActionRequest.getTxnId() + "] in current state.");

				return new GenricResponse(1, "You are not allowed to do this operation.", "");
			}

			return new GenricResponse(0, "Device Update SuccessFully.", ceirActionRequest.getTxnId());

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}finally {
			if(lock.isLocked()) {
				logger.info("lock released by thread : " + Thread.currentThread().getName());
				lock.unlock();
			}
		}
	}

	public GenricResponse getCountOfRegularizedDevicesByNid(String nid,Integer type) {
		try {
			logger.info("nid: "+nid + "type: "+type);
			if(Objects.nonNull(type)) {
				String tag="";
				if(type==1){
					tag = ConfigTags.max_end_user_device_count;

				}else {
					tag = ConfigTags.max_foreigner_end_user_device_count;
				}

//				PolicyConfigurationDb policyConfigurationDb = configurationManagementServiceImpl.getPolicyConfigDetailsByTag(tag);
				
				SystemConfigurationDb policyConfigurationDb = systemConfigurationDbRepository.getByTag(tag);

//				return new GenricResponse(0, "", "", new Count(Long.parseLong(policyConfigurationDb.getValue()), regularizedDeviceDbRepository.countByNidAndTaxPaidStatus(nid,2)));
				return new GenricResponse(0, "", "", new Count(Long.parseLong(policyConfigurationDb.getValue()),
						regularizedDeviceDbRepository.countByNidIgnoreCaseAndTaxPaidStatus(nid,2)));
			}else {
				return new GenricResponse(1,"Please enter correct type","");	

			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException("Custom Service", e.getMessage());
		}
	}

	public boolean validateRegularizedDevicesCount(String nid, List<RegularizeDeviceDb> regularizeDeviceDbs,Integer type,long usertypeId) {
		try {
			Count count = (Count) getCountOfRegularizedDevicesByNid(nid, type).getData();
			return validateRegularizedDevicesCount(count, regularizeDeviceDbs,usertypeId);
		}catch (ClassCastException e) {
			return Boolean.FALSE;
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			return Boolean.FALSE;
		}
	}

	private boolean validateRegularizedDevicesCount(Count count, List<RegularizeDeviceDb> regularizeDeviceDbs,long userypeId) {
		try {
			Long regularizedDeviceCount = regularizedDevicesCountByStatus(regularizeDeviceDbs, TaxStatus.REGULARIZED.getCode());
			if(count.getAllowed() >= regularizedDeviceCount + count.getCurrent()) {
				return Boolean.TRUE;
			}else {
				if(userypeId!=7)
				{
					logger.info("if regularize device limit increase so tax value set not paid");
					for(RegularizeDeviceDb regularize:regularizeDeviceDbs)
					{
						regularize.setTaxPaidStatus(TaxStatus.TAX_NOT_PAID.getCode());
					}
					return Boolean.TRUE;
				}
				else
				{
					return Boolean.FALSE;
				}

			}
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			return Boolean.FALSE;
		}
	}

	private Long regularizedDevicesCountByStatus(List<RegularizeDeviceDb> regularizeDeviceDbs, int status) {
		try {
			return regularizeDeviceDbs.stream().filter(o -> o.getTaxPaidStatus() == status).count();

		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			return -1L;
		}
	}

	private GenericSpecificationBuilder<RegularizeDeviceDb> buildSpecification(FilterRequest filterRequest,List<StateMgmtDb> statusList, String source){
		GenericSpecificationBuilder<RegularizeDeviceDb> specificationBuilder = new GenericSpecificationBuilder<RegularizeDeviceDb>(propertiesReader.dialect);

		if(Objects.nonNull(filterRequest.getNid()) && !filterRequest.getNid().isEmpty())
			specificationBuilder.with(new SearchCriteria("nid", filterRequest.getNid(), SearchOperation.LIKE, Datatype.STRING));
		else {
			if(Objects.nonNull(filterRequest.getUserTypeId())) {
				if(filterRequest.getUserTypeId()==18)		
				{
					specificationBuilder.with(new SearchCriteria("origin","Immigration" , SearchOperation.EQUALITY, Datatype.STRING));
				}
				else if(filterRequest.getUserTypeId()==17)		
				{
					specificationBuilder.with(new SearchCriteria("origin", "End User" , SearchOperation.EQUALITY, Datatype.STRING));
				}
				else if(filterRequest.getUserTypeId()==7)		
				{
					specificationBuilder.with(new SearchCriteria("origin", "Custom" , SearchOperation.EQUALITY, Datatype.STRING));
				}
			}
		}

		if(Objects.nonNull(filterRequest.getStartDate()) && !filterRequest.getStartDate().isEmpty())
			specificationBuilder.with(new SearchCriteria("createdOn", filterRequest.getStartDate() , SearchOperation.GREATER_THAN, Datatype.DATE));

		if(Objects.nonNull(filterRequest.getEndDate()) && !filterRequest.getEndDate().isEmpty())
			specificationBuilder.with(new SearchCriteria("createdOn", filterRequest.getEndDate() , SearchOperation.LESS_THAN, Datatype.DATE));

		if(Objects.nonNull(filterRequest.getDeviceIdType()))
			specificationBuilder.with(new SearchCriteria("deviceIdType", filterRequest.getDeviceIdType(), SearchOperation.EQUALITY, Datatype.STRING));

		if(Objects.nonNull(filterRequest.getDeviceType()))
			specificationBuilder.with(new SearchCriteria("deviceType", filterRequest.getDeviceType(), SearchOperation.EQUALITY, Datatype.STRING));

		if(Objects.nonNull(filterRequest.getTaxPaidStatus()))
			specificationBuilder.with(new SearchCriteria("taxPaidStatus", filterRequest.getTaxPaidStatus(), SearchOperation.EQUALITY, Datatype.STRING));

		if(Objects.nonNull(filterRequest.getStatus())) {
			specificationBuilder.with(new SearchCriteria("status", filterRequest.getStatus(), SearchOperation.EQUALITY, Datatype.INT));
		}
		//changes for imei /MEID  quantity done by sharad
		
		
		  if(Objects.nonNull(filterRequest.getOrigin()) &&
		  !filterRequest.getOrigin().isEmpty()) specificationBuilder.with(new
		  SearchCriteria("origin", filterRequest.getOrigin(), SearchOperation.LIKE,
		  Datatype.STRING));
		 
				
		if(Objects.nonNull(filterRequest.getNationality()) && !filterRequest.getNationality().isEmpty()) {
				//s	specificationBuilder.with(new SearchCriteria("nationality", filterRequest.getNationality(), SearchOperation.LIKE, Datatype.STRING));
		specificationBuilder.addSpecification(specificationBuilder.joinWithEnduserDB(new SearchCriteria("nationality", filterRequest.getNationality(),SearchOperation.LIKE, Datatype.STRING)));		
		}
		else {
			if(Objects.nonNull(filterRequest.getUserTypeId())) {

				if( filterRequest.getUserTypeId()==8 || filterRequest.getUserTypeId()==7 || filterRequest.getUserTypeId()==18  )		
				{
					//			specificationBuilder.with(new SearchCriteria("status",RegularizeDeviceStatus.PENDING_APPROVAL_FROM_CEIR_ADMIN.getCode(), SearchOperation.EQUALITY, Datatype.INT));

					if(Objects.nonNull(filterRequest.getFeatureId()) && Objects.nonNull(filterRequest.getUserTypeId())) {

						List<DashboardUsersFeatureStateMap> dashboardUsersFeatureStateMap = dashboardUsersFeatureStateMapRepository.findByUserTypeIdAndFeatureId(filterRequest.getUserTypeId(), filterRequest.getFeatureId());
						logger.debug(dashboardUsersFeatureStateMap);

						List<Integer> deviceStatus = new LinkedList<>();

						if(Objects.nonNull(dashboardUsersFeatureStateMap)) {
							if("dashboard".equalsIgnoreCase(source) ) {
								for(DashboardUsersFeatureStateMap dashboardUsersFeatureStateMap2 : dashboardUsersFeatureStateMap ) {
									deviceStatus.add(dashboardUsersFeatureStateMap2.getState());
								}
							}else if("filter".equalsIgnoreCase(source) || "menu".equalsIgnoreCase(source) ) {
								if(nothingInFilter(filterRequest)) {
									for(DashboardUsersFeatureStateMap dashboardUsersFeatureStateMap2 : dashboardUsersFeatureStateMap ) {
										deviceStatus.add(dashboardUsersFeatureStateMap2.getState());
									}
								}else {
									for(StateMgmtDb stateMgmtDb : statusList ) {
										deviceStatus.add(stateMgmtDb.getState());
									}
								}
							}else if("noti".equalsIgnoreCase(source)) {
								logger.info("Skip status check, because source is noti.");
							}

							logger.info("Array list to add is = " + deviceStatus);
							if(!deviceStatus.isEmpty()) {
								specificationBuilder.addSpecification(specificationBuilder.in("status", deviceStatus));
							}else {
								logger.warn("no predefined status are available.");
							}
						}
					}

				}
				else {

				}
			}

		}

		if(Objects.nonNull(filterRequest.getTxnId()) && !filterRequest.getTxnId().isEmpty()) {
			specificationBuilder.with(new SearchCriteria("txnId", filterRequest.getTxnId(), SearchOperation.LIKE, Datatype.STRING));
		}

		if(Objects.nonNull(filterRequest.getSearchString()) && !filterRequest.getSearchString().isEmpty()){
			specificationBuilder.orSearch(new SearchCriteria("txnId", filterRequest.getSearchString(), SearchOperation.LIKE, Datatype.STRING));
			specificationBuilder.orSearch(new SearchCriteria("nid", filterRequest.getSearchString(), SearchOperation.LIKE, Datatype.STRING));
		}

		return specificationBuilder;
	}

	public void setInterp(RegularizeDeviceDb regularizeDeviceDb) {
		if(Objects.nonNull(regularizeDeviceDb.getTaxPaidStatus()))
			regularizeDeviceDb.setTaxPaidStatusInterp(interpSetter.setConfigInterp(Tags.CUSTOMS_TAX_STATUS, regularizeDeviceDb.getTaxPaidStatus()));

		if(Objects.nonNull(regularizeDeviceDb.getDeviceIdType()))
			regularizeDeviceDb.setDeviceIdTypeInterp(interpSetter.setConfigInterp(Tags.DEVICE_ID_TYPE, regularizeDeviceDb.getDeviceIdType()));

		if(Objects.nonNull(regularizeDeviceDb.getDeviceType()))
			regularizeDeviceDb.setDeviceTypeInterp(interpSetter.setConfigInterp(Tags.DEVICE_TYPE, regularizeDeviceDb.getDeviceType()));

		if(Objects.nonNull(regularizeDeviceDb.getDeviceStatus()))
			regularizeDeviceDb.setDeviceStatusInterp(interpSetter.setConfigInterp(Tags.DEVICE_STATUS, regularizeDeviceDb.getDeviceStatus()));

		if(Objects.nonNull(regularizeDeviceDb.getCurrency()))
			regularizeDeviceDb.setCurrencyInterp(interpSetter.setConfigInterp(Tags.CURRENCY, regularizeDeviceDb.getCurrency(), 0, 1));

		if(Objects.nonNull(regularizeDeviceDb.getMultiSimStatus()))
			regularizeDeviceDb.setMultiSimStatusInterp(interpSetter.setConfigInterp(Tags.MULTI_SIM_STATUS, regularizeDeviceDb.getDeviceIdType()));


	}

	public boolean nothingInFilter(FilterRequest filterRequest) {
		if(Objects.nonNull(filterRequest.getStartDate()) && !filterRequest.getStartDate().isEmpty()) {
			return Boolean.FALSE;
		}
		if(Objects.nonNull(filterRequest.getEndDate()) && !filterRequest.getEndDate().isEmpty()) {
			return Boolean.FALSE;
		}

		if(Objects.nonNull(filterRequest.getTxnId()) && !filterRequest.getTxnId().isEmpty()) {
			return Boolean.FALSE;
		}

		if( Objects.nonNull(filterRequest.getDeviceIdType()) ) {
			return Boolean.FALSE;
		}
		if( Objects.nonNull(filterRequest.getDeviceType()) ) {
			return Boolean.FALSE;
		}
		if( Objects.nonNull(filterRequest.getTaxPaidStatus()) ) {
			return Boolean.FALSE;
		}
		if( Objects.nonNull(filterRequest.getStatus()) && !filterRequest.getStatus().equals(-1) ) {
			return Boolean.FALSE;
		}
		if( Objects.nonNull(filterRequest.getNid()) && !filterRequest.getNid().isEmpty()) {
			return Boolean.FALSE;
		}
		if( Objects.nonNull(filterRequest.getVisaType()) && !filterRequest.getVisaType().isEmpty()) {
			return Boolean.FALSE;
		}
		
		if( Objects.nonNull(filterRequest.getVisaNumber()) && !filterRequest.getVisaNumber().isEmpty()) {
			return Boolean.FALSE;
		}
		
		if( Objects.nonNull(filterRequest.getFileName()) && !filterRequest.getFileName().isEmpty()) {
			return Boolean.FALSE;
		}
		
		if( Objects.nonNull(filterRequest.getVisaExpiryDate()) && !filterRequest.getVisaExpiryDate().isEmpty()) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
}
