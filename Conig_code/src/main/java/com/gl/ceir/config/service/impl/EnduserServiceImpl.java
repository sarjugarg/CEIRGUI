package com.gl.ceir.config.service.impl;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.gl.ceir.config.ConfigTags;
import com.gl.ceir.config.EmailSender.EmailUtil;
import com.gl.ceir.config.configuration.PropertiesReader;
import com.gl.ceir.config.configuration.SortDirection;
import com.gl.ceir.config.exceptions.ResourceServicesException;
import com.gl.ceir.config.feign.UserFeignClient;
import com.gl.ceir.config.model.AllRequest;
import com.gl.ceir.config.model.AuditTrail;
import com.gl.ceir.config.model.CeirActionRequest;
import com.gl.ceir.config.model.ConsignmentUpdateRequest;
import com.gl.ceir.config.model.DashboardUsersFeatureStateMap;
import com.gl.ceir.config.model.EndUserDB;
import com.gl.ceir.config.model.FileDetails;
import com.gl.ceir.config.model.FilterRequest;
import com.gl.ceir.config.model.GenricResponse;
import com.gl.ceir.config.model.RawMail;
import com.gl.ceir.config.model.RegularizeDeviceDb;
import com.gl.ceir.config.model.SearchCriteria;
import com.gl.ceir.config.model.StateMgmtDb;
import com.gl.ceir.config.model.SystemConfigurationDb;
import com.gl.ceir.config.model.User;
import com.gl.ceir.config.model.UserDepartment;
import com.gl.ceir.config.model.UserProfile;
import com.gl.ceir.config.model.VipList;
import com.gl.ceir.config.model.VisaDb;
import com.gl.ceir.config.model.VisaUpdateDb;
import com.gl.ceir.config.model.WebActionDb;
import com.gl.ceir.config.model.constants.Datatype;
import com.gl.ceir.config.model.constants.EndUserStatus;
import com.gl.ceir.config.model.constants.Features;
import com.gl.ceir.config.model.constants.GenericMessageTags;
import com.gl.ceir.config.model.constants.ReferTable;
import com.gl.ceir.config.model.constants.RegularizeDeviceStatus;
import com.gl.ceir.config.model.constants.SearchOperation;
import com.gl.ceir.config.model.constants.SubFeatures;
import com.gl.ceir.config.model.constants.Tags;
import com.gl.ceir.config.model.constants.TaxStatus;
import com.gl.ceir.config.model.file.ConsignmentFileModelCEIR;
import com.gl.ceir.config.model.file.UpdateVisaFileModel;
import com.gl.ceir.config.repository.AuditTrailRepository;
import com.gl.ceir.config.repository.DashboardUsersFeatureStateMapRepository;
import com.gl.ceir.config.repository.EndUserDbRepository;
import com.gl.ceir.config.repository.SystemConfigurationDbRepository;
import com.gl.ceir.config.repository.UpdateVisaRepository;
import com.gl.ceir.config.repository.UserProfileRepository;
import com.gl.ceir.config.repository.VipListRepository;
import com.gl.ceir.config.repository.VisaDbRepository;
import com.gl.ceir.config.repository.WebActionDbRepository;
import com.gl.ceir.config.request.model.Generic_Response_Notification;
import com.gl.ceir.config.request.model.RegisterationUser;
import com.gl.ceir.config.specificationsbuilder.GenericSpecificationBuilder;
import com.gl.ceir.config.specificationsbuilder.SpecificationBuilder;
import com.gl.ceir.config.transaction.EndUserTransaction;
import com.gl.ceir.config.util.CommonFunction;
import com.gl.ceir.config.util.CustomMappingStrategy;
import com.gl.ceir.config.util.DateUtil;
import com.gl.ceir.config.util.InterpSetter;
import com.gl.ceir.config.util.Utility;
import com.opencsv.CSVWriter;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import com.opencsv.bean.MappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

@Service
public class EnduserServiceImpl {

	private static final Logger logger = LogManager.getLogger(EnduserServiceImpl.class);
	
	private ReentrantLock lock = new ReentrantLock();

	@Autowired
	EndUserDbRepository endUserDbRepository;

	@Autowired
	PropertiesReader propertiesReader;

	@Autowired
	AuditTrailRepository auditTrailRepository;

	@Autowired
	ConfigurationManagementServiceImpl configurationManagementServiceImpl;

	@Autowired
	SystemConfigurationDbRepository systemConfigurationDbRepository;

	@Autowired
	WebActionDbRepository webActionDbRepository;

	@Autowired
	VipListRepository vipListRepository;

	@Autowired
	EmailUtil emailUtil;

	@Autowired
	EndUserTransaction endUserTransaction;

	@Autowired
	CommonFunction commonFunction;

	@Autowired
	RegularizedDeviceServiceImpl regularizedService;

	@Autowired
	UpdateVisaRepository visaUpdateRepo;

	@Autowired
	UserStaticServiceImpl userStaticServiceImpl;

	@Autowired
	UpdateVisaRepository updateVisaRepo;

	@Autowired
	UpdateVisaRepository updateVisaRepository;

	@Autowired
	StateMgmtServiceImpl stateMgmtServiceImpl;

	@Autowired
	InterpSetter interpSetter;

	@Autowired
	Utility utility;

	@Autowired
	RegularizedDeviceServiceImpl regularizeDevice;

	@Autowired
	DashboardUsersFeatureStateMapRepository dashboardUsersFeatureStateMapRepository; 

	@Autowired 
	UserFeignClient userFeignClient;
	
	@Autowired
	UserProfileRepository userProfileRepository;
	
	@Autowired
	VisaDbRepository visaDbRepository;
	
	public GenricResponse endUserByNid(AllRequest data) {
		try {
			logger.info("data given: "+data);
			String username="";
			long userId=0;
			if( Objects.nonNull(data.getUsername()))
				username=data.getUsername();
			if(data.getUserTypeId()!=17) {
				username=data.getUsername();
				userId=data.getUserId();
			}
			auditTrailRepository.save(new AuditTrail(userId, username, data.getUserTypeId(),
					data.getUserType(), 12,Features.REGISTER_DEVICE, SubFeatures.Search_NID, "", data.getNid(),data.getUserType(),data.getPublicIp(),data.getBrowser()));
			logger.info("AUDIT : Saved request in audit.");

//			EndUserDB endUserDB = endUserDbRepository.getByNid(data.getNid());
			if( Objects.nonNull(data.getNid()) && !data.getNid().isEmpty() && !data.getNid().equalsIgnoreCase("null")) {
				EndUserDB endUserDB = endUserDbRepository.findByNidIgnoreCase(data.getNid());
	
				// End user is not registered with CEIR system.
				if(Objects.nonNull(endUserDB)) {
	//				endUserDB.setDocTypeInterp(interpSetter.setConfigInterp(Tags.DOC_TYPE, endUserDB.getDocType()));
					if(Objects.nonNull(endUserDB.getDocType())) {
						endUserDB.setDocTypeInterp(interpSetter.setTagId(Tags.DOC_TYPE, endUserDB.getDocType()));	
					}
					List<RegularizeDeviceDb> regulaizedList=new ArrayList<RegularizeDeviceDb>();
					if(Objects.nonNull(endUserDB.getRegularizeDeviceDbs())) {
						for(RegularizeDeviceDb regularizeData:endUserDB.getRegularizeDeviceDbs()) {
							regularizeData.setEndUserDB(new EndUserDB());
							regulaizedList.add(regularizeData);
						}
					}
					endUserDB.setRegularizeDeviceDbs(regulaizedList);
					logger.info("End User with nid [" + data.getNid() + "] does exist.");
					if( Objects.nonNull(endUserDB.getVisaUpdateDb()) && endUserDB.getVisaUpdateDb().size()>0) {
						int lastVisaUpdate = endUserDB.getVisaUpdateDb().size() -1;
						if( Objects.nonNull(endUserDB.getVisaUpdateDb().get(lastVisaUpdate).getVisaNumber()) )
							endUserDB.getVisaDb().get(0).setVisaNumber(endUserDB.getVisaUpdateDb().get(lastVisaUpdate).getVisaNumber());
						if( Objects.nonNull(endUserDB.getVisaUpdateDb().get(lastVisaUpdate).getVisaExpiryDate()) )
							endUserDB.getVisaDb().get(0).setVisaExpiryDate(endUserDB.getVisaUpdateDb().get(lastVisaUpdate).getVisaExpiryDate());
					}
					return new GenricResponse(1, "End User does exist.", data.getNid(), endUserDB);
				}else {
					logger.info("End User with nid [" + data.getNid() + "] does not exist.");
					return new GenricResponse(0, "User does not exist.", "");
				}
			}else if( data.getUserType().equalsIgnoreCase("Custom") || data.getUserType().equalsIgnoreCase("Immigration") ){
				List<EndUserDB> endUsers = endUserDbRepository.findByOriginIgnoreCase( data.getUserType() );
				if( endUsers.isEmpty() ) {
					logger.info("No device registered by Custom yet.");
					return new GenricResponse(0, "User does not exist.", "");
				}else {
					for( EndUserDB endUserDB : endUsers ) {
						if(Objects.nonNull(endUserDB.getDocType())) {
							endUserDB.setDocTypeInterp(interpSetter.setTagId(Tags.DOC_TYPE, endUserDB.getDocType()));	
						}
						List<RegularizeDeviceDb> regulaizedList=new ArrayList<RegularizeDeviceDb>();
						if(Objects.nonNull(endUserDB.getRegularizeDeviceDbs())) {
							for(RegularizeDeviceDb regularizeData:endUserDB.getRegularizeDeviceDbs()) {
								regularizeData.setEndUserDB(new EndUserDB());
								regulaizedList.add(regularizeData);
							}
						}
						endUserDB.setRegularizeDeviceDbs(regulaizedList);
						logger.info("End User with nid [" + data.getNid() + "] does exist.");
						if( Objects.nonNull(endUserDB.getVisaUpdateDb()) && endUserDB.getVisaUpdateDb().size()>0) {
							int lastVisaUpdate = endUserDB.getVisaUpdateDb().size() -1;
							if( Objects.nonNull(endUserDB.getVisaUpdateDb().get(lastVisaUpdate).getVisaNumber()) )
								endUserDB.getVisaDb().get(0).setVisaNumber(endUserDB.getVisaUpdateDb().get(lastVisaUpdate).getVisaNumber());
							if( Objects.nonNull(endUserDB.getVisaUpdateDb().get(lastVisaUpdate).getVisaExpiryDate()) )
								endUserDB.getVisaDb().get(0).setVisaExpiryDate(endUserDB.getVisaUpdateDb().get(lastVisaUpdate).getVisaExpiryDate());
						}
					}
					return new GenricResponse(2, "End Users does exist.", "", endUsers);
				}
			}else {
				logger.info("Nid is null request.");
				return new GenricResponse(0, "User does not exist.", "");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException("Custom Service", e.getMessage());
		}
	}

	@Transactional
	public GenricResponse saveEndUser(EndUserDB endUserDB) {
		try {
			logger.info("audit trail values from form: "+endUserDB.getAuditParameters());
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
			auditTrailRepository.save(new AuditTrail(userId, username, endUserDB.getAuditParameters().getUserTypeId(),
					endUserDB.getAuditParameters().getUserType(), 12,Features.REGISTER_DEVICE, SubFeatures.REGISTER, "", endUserDB.getTxnId(),endUserDB.getAuditParameters().getUserType(),endUserDB.getPublicIp(),endUserDB.getBrowser()));
			logger.info("AUDIT : Saved request in audit.");


			List<WebActionDb> webActionDbs = new ArrayList<>();

			// End user is not registered with CEIR system.
			if(Objects.isNull(endUserDB)) {
				return new GenricResponse(1, GenericMessageTags.NULL_REQ.getTag(), 
						GenericMessageTags.NULL_REQ.getMessage(), "");
			}

			// If user is already registerd.
			EndUserDB endUserDB2 = endUserDbRepository.getByNid(endUserDB.getNid());
			if(Objects.nonNull(endUserDB2)) {
				return new GenricResponse(3, GenericMessageTags.USER_ALREADY_EXIST.getTag(), 
						GenericMessageTags.USER_ALREADY_EXIST.getMessage(), "");
			}


			// Add department if user is VIP.
			if("Y".equals(endUserDB.getIsVip())) {
				UserDepartment userDepartment = endUserDB.getUserDepartment();
				UserDepartment newUserDepartment = new UserDepartment(userDepartment.getName(), userDepartment.getDepartmentId(), 
						userDepartment.getDepartmentFilename());
				newUserDepartment.setEndUserDB(endUserDB);

				if(Objects.isNull(userDepartment)) {
					return new GenricResponse(2, GenericMessageTags.NULL_USER_DEPARTMENT.getTag(), 
							GenericMessageTags.NULL_USER_DEPARTMENT.getMessage(), "");
				}else {
					endUserDB.setUserDepartment(newUserDepartment);	
				}
			}

			Integer type=null;


			logger.info("nationality= "+endUserDB.getNationality());
			if(Objects.nonNull(endUserDB)) {
				if("Cambodian".equalsIgnoreCase(endUserDB.getNationality())) {
					type=1;
				}
				else {
					type=2;
				}	
			}


			// Validate end user devices.
			if(!endUserDB.getRegularizeDeviceDbs().isEmpty()){

				logger.info("nationality= "+endUserDB.getNationality());
				if(Objects.nonNull(endUserDB)) {
					if("Cambodian".equalsIgnoreCase(endUserDB.getNationality())) {
						type=1;
					}
					else {
						// Validate and set visa expiry date as per default rule.
						GenricResponse response = setVisaExpiryDate(endUserDB);
						if(response.getErrorCode() != 0) 
							return response;
						type=2;
					}	
				}
				if(endUserDB.getAuditParameters().getUserTypeId()!=7)
				{	
					logger.info("if usertype is not custom and type is : "+type);
					for(RegularizeDeviceDb regularizeDb:endUserDB.getRegularizeDeviceDbs())
					{
						if(type==1)
						{
							regularizeDb.setTaxPaidStatus(TaxStatus.TAX_NOT_PAID.getCode());
							regularizeDb.setTaxCollectedBy(username);
						}
						else {
							regularizeDb.setTaxPaidStatus(TaxStatus.REGULARIZED.getCode());				
						}
					}
				}
				if(regularizedService.validateRegularizedDevicesCount(endUserDB.getNid(), endUserDB.getRegularizeDeviceDbs(),type,endUserDB.getAuditParameters().getUserTypeId())) {
					if(commonFunction.hasDuplicateImeiInRequest(endUserDB.getRegularizeDeviceDbs())) {
						return new GenricResponse(6,GenericMessageTags.DUPLICATE_IMEI_IN_REQUEST.getTag(),GenericMessageTags.DUPLICATE_IMEI_IN_REQUEST.getMessage(), ""); 
					}
					for(RegularizeDeviceDb regularizeDeviceDb : endUserDB.getRegularizeDeviceDbs()) {

						regularizeDeviceDb.setEndUserDB(endUserDB);
						//TO DO
						if(commonFunction.checkAllImeiOfRegularizedDevice(regularizeDeviceDb)) {
							return new GenricResponse(5,GenericMessageTags.DUPLICATE_IMEI.getTag(),GenericMessageTags.DUPLICATE_IMEI.getMessage(),"");
						}

						if(Objects.isNull(regularizeDeviceDb.getTaxPaidStatus())) {
							regularizeDeviceDb.setTaxPaidStatus(TaxStatus.TAX_NOT_PAID.getCode());
						}

						if(Objects.isNull(regularizeDeviceDb.getStatus())) {
							regularizeDeviceDb.setStatus(RegularizeDeviceStatus.NEW.getCode());
						}

						if(Objects.isNull(endUserDB.getOrigin())) {
							endUserDB.setOrigin(regularizeDeviceDb.getOrigin());
						}
						// Add in web action list.
						if(endUserDB.getAuditParameters().getUserTypeId()==7) {
							if(regularizeDeviceDb.getTaxPaidStatus()==TaxStatus.TAX_PAID.getCode())
							{
								logger.info("if usertype is custom and tax status is paid so now this entry going to web action db");
								webActionDbs.add(new WebActionDb(Features.REGISTER_DEVICE, SubFeatures.Approve, 0, 
										regularizeDeviceDb.getTxnId()));
							}
						}
						webActionDbs.add(new WebActionDb(Features.REGISTER_DEVICE, SubFeatures.REGISTER, 0, 
								regularizeDeviceDb.getTxnId()));
					}
					logger.info("regularize devices: "+endUserDB.getRegularizeDeviceDbs());
				}else {
					logger.warn("Regularized Devices are exceeding the allowed count." + endUserDB);
					return new GenricResponse(3,GenericMessageTags.REGULARISED_DEVICE_EXCEEDED.getTag(),GenericMessageTags.REGULARISED_DEVICE_EXCEEDED.getMessage(), "");
				}
			}

			endUserDB = endUserDbRepository.save(endUserDB);
			if(Objects.nonNull(endUserDB)){
				if(Objects.nonNull(endUserDB.getEmail()) && !endUserDB.getEmail().isEmpty() && !"NA".equalsIgnoreCase(endUserDB.getEmail()) ) {
					List<RawMail> rawMails = new ArrayList<>();
					String mailTag = "END_USER_REGISTER";
					Map<String, String> placeholderMap = new HashMap<String, String>();
					placeholderMap.put("<First name>", endUserDB.getFirstName());
					placeholderMap.put("<Txn id>", endUserDB.getTxnId());
					// Mail to End user.
					rawMails.add(new RawMail(mailTag, endUserDB.getId(), Long.valueOf(12), 
							Features.REGISTER_DEVICE, SubFeatures.REGISTER, endUserDB.getTxnId(), 
							endUserDB.getTxnId(), placeholderMap, ReferTable.END_USER, null, "End User"));
					emailUtil.saveNotification(rawMails);
				}
				else {
					logger.info("this end user don't have any email");
				}

			}
			logger.info(GenericMessageTags.USER_REGISTER_SUCCESS.getMessage() + " with nid [" + endUserDB.getNid() + "]");

			webActionDbRepository.saveAll(webActionDbs);
			logger.info("Batch update in web_action_db. " + webActionDbs );


			return new GenricResponse(0, GenericMessageTags.USER_REGISTER_SUCCESS.getTag(),GenericMessageTags.USER_REGISTER_SUCCESS.getMessage(), endUserDB.getTxnId());

		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException("Custom Service", e.getMessage());
		}
	}

	@Transactional(rollbackOn = Exception.class)
	public GenricResponse updateEndUser(EndUserDB endUserDBNew) {
		try {
			if(Objects.isNull(endUserDBNew)) {
				logger.info("Request can't be null.");
				return new GenricResponse(2, GenericMessageTags.NULL_REQ.getTag(), 
						GenericMessageTags.NULL_REQ.getMessage(), null);
			}
			String nid = endUserDBNew.getNid();
			if(Objects.isNull(endUserDBNew)) {
				logger.info("Request have nid as null.");
				return new GenricResponse(3, GenericMessageTags.NULL_NID.getTag(), GenericMessageTags.NULL_NID.getMessage(), null);
			}

			EndUserDB endUserDBOld = endUserDbRepository.getByNid(nid);
			endUserDBNew.setId(endUserDBOld.getId());

			// Visa Old
			if(!endUserDBOld.getVisaDb().isEmpty()) {
				VisaDb visaDbOld = endUserDBOld.getVisaDb().get(0);

				// Visa New
				VisaDb visaDbNew = endUserDBNew.getVisaDb().get(0);
				visaDbNew.setId(visaDbOld.getId());
				visaDbNew.setEndUserDB(endUserDBNew);

				ArrayList<VisaDb> visaDbListNew = new ArrayList<>();
				visaDbListNew.add(visaDbNew);

				// Set New objects to new end user db.
				endUserDBNew.setVisaDb(visaDbListNew);
			}

			// User department - Old
			UserDepartment userDepartmentOld = endUserDBOld.getUserDepartment();

			if(Objects.nonNull(userDepartmentOld)) {
				// User department - New			
				UserDepartment userDepartmentNew = endUserDBNew.getUserDepartment();
				userDepartmentNew.setId(userDepartmentOld.getId());
				userDepartmentNew.setEndUserDB(endUserDBNew);

				endUserDBNew.setUserDepartment(userDepartmentNew);
			}

			logger.info(GenericMessageTags.USER_UPDATE_SUCCESS.getMessage() + "of NID [" + nid +"]");

			endUserDbRepository.save(endUserDBNew);

			auditTrailRepository.save(new AuditTrail(endUserDBOld.getId(), "", 17L,
					"End User", 0L,Features.REGISTER_DEVICE, SubFeatures.UPDATE, "","End User"));
			logger.info("AUDIT : Saved update request in audit.");

			return new GenricResponse(1, GenericMessageTags.USER_UPDATE_SUCCESS.getTag(), GenericMessageTags.USER_UPDATE_SUCCESS.getMessage(), nid);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException("Custom Service", e.getMessage());
		}
	}
	@Transactional
	public GenricResponse updateVisaEndUser(EndUserDB endUserDB) {
		try {
			VisaDb latestVisa = null;
			// Check if request is null
			auditTrailRepository.save(new AuditTrail(0, "", 17L, 
					"End User", 43L, Features.UPDATE_VISA, SubFeatures.REQUEST, "",endUserDB.getTxnId(),"End User",endUserDB.getPublicIp(),endUserDB.getBrowser()));
			logger.info("VisaUpdate [" + endUserDB.getTxnId() + "] saved in audit_trail.");

			if(Objects.isNull(endUserDB.getNid())) {
				logger.info("Request should not have nid as null.");
				return new GenricResponse(2, GenericMessageTags.NULL_NID.getTag(), 
						GenericMessageTags.NULL_NID.getMessage(), null);
			}
			// Check if requested VISA is null or empty.
			if(Objects.isNull(endUserDB.getVisaDb())) {
				logger.info("Request visa update should not be null.");
				return new GenricResponse(3, GenericMessageTags.NULL_VISA.getTag(), 
						GenericMessageTags.NULL_VISA.getMessage(), null);
			}else if(endUserDB.getVisaDb().isEmpty()){
				logger.info("Request visa update should not be empty.");
				return new GenricResponse(4, GenericMessageTags.EMPTY_VISA.getTag(), 
						GenericMessageTags.EMPTY_VISA.getMessage(), null);
			}else {
				latestVisa = endUserDB.getVisaDb().get(0);
			}

			EndUserDB endUserDB1 = endUserDbRepository.getByNid(endUserDB.getNid());

			// End user is not registered with CEIR system.
			if(Objects.isNull(endUserDB1)) {
				return new GenricResponse(5, GenericMessageTags.INVALID_USER.getTag(), 
						GenericMessageTags.INVALID_USER.getMessage(), 
						endUserDB.getNid());

			}else {
				logger.info("Going to update VISA of user. " + endUserDB1);

				List<VisaDb> visaDbs = endUserDB1.getVisaDb();
				if(visaDbs.isEmpty()) {
					logger.info("You are not allowed to update Visa." + endUserDB.getNid());
					return new GenricResponse(6, GenericMessageTags.VISA_UPDATE_NOT_ALLOWED.getTag(), 
							GenericMessageTags.VISA_UPDATE_NOT_ALLOWED.getMessage(), endUserDB.getNid());
				}else {
					//					 Update expiry date of latest Visa
					//					VisaDb visaDb = visaDbs.get(visaDbs.size() - 1);
					//					visaDb.setVisaExpiryDate(latestVisa.getVisaExpiryDate());
					
					VisaDb OldVisa=visaDbs.get(0);
					OldVisa.setVisaNumber(latestVisa.getVisaNumber());
					OldVisa.setVisaFileName(latestVisa.getVisaFileName());
					OldVisa.setVisaExpiryDate(latestVisa.getVisaExpiryDate());
//					VisaUpdateDb visaUpdateDb=new VisaUpdateDb(OldVisa.getVisaType(), OldVisa.getVisaNumber(),
//							latestVisa.getVisaFileName(), OldVisa.getEntryDateInCountry(), latestVisa.getVisaExpiryDate(),
//							0,endUserDB1,endUserDB.getTxnId(),endUserDB.getNid()); 
					VisaUpdateDb visaUpdateDb=new VisaUpdateDb(OldVisa.getVisaType(), latestVisa.getVisaNumber(),
							latestVisa.getVisaFileName(), OldVisa.getEntryDateInCountry(), latestVisa.getVisaExpiryDate(),
							0,endUserDB1,endUserDB.getTxnId(),endUserDB.getNid());
					visaDbRepository.save(OldVisa);
					String mailTag = "Update_Visa_Request";
					List<RawMail> rawMails = new ArrayList<>();
					Map<String, String> placeholderMap = new HashMap<String, String>();
					placeholderMap.put("<First name>", endUserDB1.getFirstName());
					placeholderMap.put("<txn_id>", endUserDB.getTxnId());
					rawMails.add(new RawMail(mailTag, endUserDB1.getId(), Long.valueOf(43), 
							Features.UPDATE_VISA, SubFeatures.REQUEST, endUserDB.getTxnId(), 
							endUserDB.getTxnId(), placeholderMap, ReferTable.END_USER, null, "End User"));
					VisaUpdateDb visaDb=updateVisaRepository.findByEndUserDBData_Id(endUserDB1.getId());
					if(visaDb!=null) { 
						visaUpdateDb.setId(visaDb.getId());
						visaUpdateDb.setCreatedOn(visaDb.getCreatedOn());
					}
					else {
					}
					WebActionDb webAction=new	 WebActionDb(Features.UPDATE_VISA, SubFeatures.REQUEST, 0, 
							endUserDB.getTxnId());
					endUserDB1.setOnVisa("Y");
					if(endUserTransaction.addUpdateVisaRequest(visaUpdateDb, endUserDB1,webAction)) {
						webActionDbRepository.save(webAction);
						logger.info(" addition in web_action_db. " + webAction );
						if(Objects.nonNull(rawMails) && !rawMails.isEmpty()) {
							emailUtil.saveNotification(rawMails);	
						}
						return new GenricResponse(0, GenericMessageTags.VISA_UPDATE_REQUEST_SUCCESS.getTag(), 
								GenericMessageTags.VISA_UPDATE_REQUEST_SUCCESS.getMessage(), endUserDB.getNid());

					}else {
						return new GenricResponse(1, GenericMessageTags.VISA_UPDATE_REQUEST_FAIL.getTag(), 
								GenericMessageTags.VISA_UPDATE_REQUEST_FAIL.getMessage(), endUserDB.getNid());
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException("End user Service", e.getMessage());
		}
	}

	private SpecificationBuilder<EndUserDB> buildSpecification(FilterRequest filterRequest){
		SpecificationBuilder<EndUserDB> specificationBuilder = new SpecificationBuilder<>(propertiesReader.dialect);

		if(Objects.nonNull(filterRequest.getNid()))
			specificationBuilder.with(new SearchCriteria("nid", filterRequest.getNid(), SearchOperation.EQUALITY, Datatype.STRING));

		if("IMMIGRATION".equalsIgnoreCase(filterRequest.getOrigin())) {
			List<String> notInList = new ArrayList<>();
			notInList.add("Cambodian");

			specificationBuilder.notIn("nationality", notInList);
		}

		if(Objects.nonNull(filterRequest.getSearchString()) && !filterRequest.getSearchString().isEmpty()){
			specificationBuilder.orSearch(new SearchCriteria("nid", filterRequest.getNid(), SearchOperation.LIKE, Datatype.STRING));
		}

		return specificationBuilder;
	}

	private void setInterp(EndUserDB endUserDB) {

	}

	private GenricResponse setVisaExpiryDate(EndUserDB endUserDB) {
		logger.info("inside set visa expiry process");
		if("Y".equalsIgnoreCase(endUserDB.getOnVisa())) {
			if(Objects.isNull(endUserDB.getVisaDb())) {
				return new GenricResponse(11, GenericMessageTags.NULL_VISA.getTag(), GenericMessageTags.NULL_VISA.getMessage(), "");
			}else {
				List<VisaDb> visaDbs = endUserDB.getVisaDb();
				if(visaDbs.isEmpty()) {
					return new GenricResponse(12, GenericMessageTags.VISA_EMPTY.getTag(), GenericMessageTags.VISA_EMPTY.getMessage(), "");
				}else if(Objects.isNull(visaDbs.get(0).getVisaExpiryDate()) || visaDbs.get(0).getVisaExpiryDate().equals("")) {
					SystemConfigurationDb defaultVisaExpirationDays = systemConfigurationDbRepository.getByTag(ConfigTags.default_visa_expiration_days);
					int day = 0;
					try {
						day = Integer.parseInt(defaultVisaExpirationDays.getValue());
					}catch (NumberFormatException e) {
						logger.info("Config value 'default_visa_expiration_days' [" + defaultVisaExpirationDays.getValue() + "] must be cast to numeric value.");
						return new GenricResponse(13, GenericMessageTags.DISCREPENCY_IN_CONFIG.getTag(), 
								GenericMessageTags.DISCREPENCY_IN_CONFIG.getMessage(), "default_visa_expiration_days must be cast to numeric value");
					}
					Date date;
					String entryDate=endUserDB.getEntryDateInCountry();
					logger.info("entry date in country:  "+entryDate);
					Date toDate=DateUtil.stringToDate(entryDate);
					if(Objects.nonNull(toDate))
					{
						date=DateUtil.addDaysInDate(day, toDate);
					}
					else {
						date = DateUtil.addDaysInDate(day, new Date());						
					}
					logger.info("expiry date: "+date);
					visaDbs.get(0).setVisaExpiryDate(date);
					visaDbs.get(0).setEndUserDB(endUserDB);
					return new GenricResponse(0);
				}else {
					visaDbs.get(0).setEndUserDB(endUserDB);
					return new GenricResponse(0);
				}
			}
		}
		else {
			SystemConfigurationDb defaultVisaExpirationDays = systemConfigurationDbRepository.getByTag(ConfigTags.default_visa_expiration_days);
			int day = 0;
			try {
				day = Integer.parseInt(defaultVisaExpirationDays.getValue());
			}catch (NumberFormatException e) {
				logger.info("Config value 'default_visa_expiration_days' [" + defaultVisaExpirationDays.getValue() + "] must be cast to numeric value.");
				return new GenricResponse(13, GenericMessageTags.DISCREPENCY_IN_CONFIG.getTag(), 
						GenericMessageTags.DISCREPENCY_IN_CONFIG.getMessage(), "default_visa_expiration_days must be cast to numeric value");
			}
			Date date;
			String entryDate=endUserDB.getEntryDateInCountry();
			logger.info("entry date in country:  "+entryDate);
			Date toDate=DateUtil.stringToDate(entryDate);
			if(Objects.nonNull(toDate))
			{
				date=DateUtil.addDaysInDate(day, toDate);
			}
			else {
				date = DateUtil.addDaysInDate(day, new Date());						
			}
			logger.info("expiry date: "+date);
			List<VisaDb> visaDbs =new ArrayList<VisaDb>();
			VisaDb visaDb=new VisaDb( 1,"NA","NA",entryDate,date,endUserDB);
			visaDbs.add(visaDb);
			endUserDB.setVisaDb(visaDbs);
			return new GenricResponse(0);
		}

	}

	public GenricResponse acceptReject(ConsignmentUpdateRequest updateRequest) {
		try {
			if( !("End User".equalsIgnoreCase(updateRequest.getUserType())) && userStaticServiceImpl.checkIfUserIsDisabled( updateRequest.getUserId() ))
				return new GenricResponse(5, "USER_IS_DISABLED","This account is disabled. Please enable the account to perform the operation.",
						null);
			UserProfile userProfile = null;
			String nid = updateRequest.getNid();
			Map<String, String> placeholderMap = new HashMap<String, String>();
			EndUserDB endUserDB = endUserDbRepository.getByNid(nid);

			logger.info(endUserDB);

			if(Objects.isNull(endUserDB)) {
				logger.info(GenericMessageTags.INVALID_USER.getMessage() + " of NID [" +nid+"]");
				return new GenricResponse(4, GenericMessageTags.INVALID_USER.getTag(), 
						GenericMessageTags.INVALID_USER.getMessage(), updateRequest.getTxnId());
			}

			// Build placeholders map to replace placeholders from mail.
			placeholderMap.put("<First name>", endUserDB.getFirstName());

			// 0 - Accept, 1 - Reject
			if("CEIRADMIN".equalsIgnoreCase(updateRequest.getUserType())){
				String mailTag = null;
				String action = null;
				String receiverUserType = null;

				// If end user state is not pending approval on ceir admin, reject the request.
				if(endUserDB.getStatus() != EndUserStatus.PENDING_APPROVAL_ON_CEIR_ADMIN.getCode()) {
					logger.info(GenericMessageTags.INVALID_STATE_TRANSTION.getMessage() + " for user " + endUserDB);
					return new GenricResponse(5, GenericMessageTags.INVALID_STATE_TRANSTION.getTag(), 
							GenericMessageTags.INVALID_STATE_TRANSTION.getMessage(), "");
				}

				if(updateRequest.getAction() == 0) {
					action = SubFeatures.ACCEPT;
					mailTag = "END_USER_APPROVED_BY_CEIR_ADMIN"; 
					receiverUserType = "End User";
					endUserDB.setStatus(EndUserStatus.APPROVED.getCode());
					endUserDB.setRejectedRemark(null);
					// if user is VIP, add imei's of user in vip_list table.
					updateImeiInVipList(endUserDB,"NA");

				}else {
					action = SubFeatures.REJECT;
					receiverUserType = "End User";
					mailTag = "END_USER_REJECT_BY_CEIR_ADMIN";

					endUserDB.setStatus(EndUserStatus.REJECTED_BY_CEIR_ADMIN.getCode());
					endUserDB.setRemarks(updateRequest.getRemarks());
				}

				// Update Stock and its history.
				if(!endUserTransaction.updateStatusWithHistory(endUserDB)){
					logger.warn("Unable to update End userdb.");
					return new GenricResponse(3, "Unable to update End Userdb.", updateRequest.getTxnId()); 
				}else {
					List<RawMail> rawMails = new ArrayList<>();
					mailTag="END_USER_REGISTER";
					// Mail to End user. 
					rawMails.add(new RawMail(mailTag, endUserDB.getId(), Long.valueOf(updateRequest.getFeatureId()), 
							Features.MANAGE_USER, SubFeatures.ACCEPT_REJECT, updateRequest.getTxnId(), 
							"SUBJECT", placeholderMap, ReferTable.END_USER, null, "END USER"));
					emailUtil.saveNotification(rawMails);
				}

			}else {
				logger.warn("Accept/reject of Stock not allowed to you.");
				new GenricResponse(1, "Operation not Allowed", updateRequest.getTxnId());
			}

			return new GenricResponse(0, "Consignment Update SuccessFully.", updateRequest.getTxnId());

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	public void updateImeiInVipList(EndUserDB endUserDB, String approvedBy ) {
		if("Y".equals(endUserDB.getIsVip())) {
			if(endUserDB.getRegularizeDeviceDbs().isEmpty()) {
				logger.info("End User is VIP but no device is registered for him/her with NID/Passport. ["+endUserDB.getNid()+"]");
			}else {
				RegularizeDeviceDb regularizeDeviceDb = endUserDB.getRegularizeDeviceDbs().get(0);

				logger.info("Update IMEI in VIP list for device:["+regularizeDeviceDb.toString()+"]");
				List<VipList> vipsImeiList = new ArrayList<>(4);
				if(Objects.nonNull(regularizeDeviceDb.getFirstImei())) 
					vipsImeiList.add(new VipList(regularizeDeviceDb.getFirstImei(), endUserDB.getPhoneNo(), endUserDB.getNid(),
							approvedBy, endUserDB.getTxnId()));

				if(Objects.nonNull(regularizeDeviceDb.getSecondImei()))
					vipsImeiList.add(new VipList(regularizeDeviceDb.getSecondImei(), endUserDB.getPhoneNo(), endUserDB.getNid(),
							approvedBy, endUserDB.getTxnId()));

				if(Objects.nonNull(regularizeDeviceDb.getThirdImei()))
					vipsImeiList.add(new VipList(regularizeDeviceDb.getThirdImei(), endUserDB.getPhoneNo(), endUserDB.getNid(),
							approvedBy, endUserDB.getTxnId()));

				if(Objects.nonNull(regularizeDeviceDb.getFourthImei()))
					vipsImeiList.add(new VipList(regularizeDeviceDb.getFourthImei(), endUserDB.getPhoneNo(), endUserDB.getNid(),
							approvedBy, endUserDB.getTxnId()));
				logger.info("Going to save VIP list.");
				vipListRepository.saveAll(vipsImeiList);
			}
		}else {
			// user is not VIP, so nothing to do with table vip_list table.
			logger.info("User is not VIP, so nothing to do with table vip_list table.");
		}
	}

	@Transactional
	public GenricResponse acceptReject(CeirActionRequest ceirActionRequest) {
		try {
			if( !("End User".equalsIgnoreCase(ceirActionRequest.getUserType()) || "CEIRSYSTEM".equalsIgnoreCase(ceirActionRequest.getUserType())) 
					&& userStaticServiceImpl.checkIfUserIsDisabled( ceirActionRequest.getUserId() ))
				return new GenricResponse(5, "USER_IS_DISABLED","This account is disabled. Please enable the account to perform the operation.",
						ceirActionRequest.getId());
			String tag = null;
			String receiverUserType = null;
			String txnId = null;
			EndUserDB endUserDB = null;
			List<RawMail> rawMails = new ArrayList<>();
			Map<String, String> placeholders = new HashMap<>();
			long userTypeId = 0;
			long userId = 0;
			String username = "";
			VisaDb latestVisa = null;            
			VisaUpdateDb visaDb = new VisaUpdateDb();
			String sufeature="";

			lock.lock();
			logger.info("lock taken by thread : " + Thread.currentThread().getName());
			
			if("CEIRADMIN".equalsIgnoreCase(ceirActionRequest.getUserType())){
				visaDb = visaUpdateRepo.getById(ceirActionRequest.getId());
				if(Objects.isNull(visaDb)) {
					return new GenricResponse(1, "Visa Db is incorrect", "");				
				}
				endUserDB = endUserDbRepository.getById(visaDb.getEndUserDBData().getId());

				if(Objects.isNull(endUserDB)) {
					logger.info("end user data should not be null.");
					return new GenricResponse(3, GenericMessageTags.NULL_EndUser.getTag(), 
							GenericMessageTags.NULL_VISA.getMessage(), null);
				}
				else if(Objects.isNull(endUserDB.getVisaDb())) {
					logger.info("Request visa update should not be null.");
					return new GenricResponse(3, GenericMessageTags.NULL_VISA.getTag(), 
							GenericMessageTags.NULL_VISA.getMessage(), null);
				}else if(endUserDB.getVisaDb().isEmpty()){
					logger.info("Request visa update should not be empty.");
					return new GenricResponse(4, GenericMessageTags.EMPTY_VISA.getTag(), 
							GenericMessageTags.EMPTY_VISA.getMessage(), null);
				}else {
					latestVisa = endUserDB.getVisaDb().get(0);
				}
				placeholders.put("<Txn id>", visaDb.getTxnId());
				placeholders.put("<First name>", endUserDB.getFirstName());

				userTypeId = 8;
				if(Objects.nonNull(ceirActionRequest.getUsername())) {
					username=ceirActionRequest.getUsername();		
				}
				visaDb.setApprovedBy(username);
				if(ceirActionRequest.getAction() == 0) {
					String payloadTxnId = visaDb.getTxnId();
					// Check if someone else taken the same action on visa update.
					VisaUpdateDb visaUpdateTemp = visaUpdateRepo.getByTxnId(payloadTxnId);
					if(RegularizeDeviceStatus.APPROVED.getCode() == visaUpdateTemp.getStatus()) {
						String message = "Any other user have taken the same action on the visaUpdate [" + payloadTxnId + "]";
						logger.info(message);
						return new GenricResponse(10, "", message, payloadTxnId);
					}
					
					visaDb.setStatus(RegularizeDeviceStatus.APPROVED.getCode());
					List<VisaDb> visaDbs = endUserDB.getVisaDb();
					VisaDb visaDbUpdate = visaDbs.get(visaDbs.size() - 1);
					visaDbUpdate.setVisaExpiryDate(visaDb.getVisaExpiryDate());	
					visaDbUpdate.setVisaFileName(visaDb.getVisaFileName());
					if(visaDbs.isEmpty()) {
						logger.info("You are not allowed to update Visa." + endUserDB.getNid());
						return new GenricResponse(6, GenericMessageTags.VISA_UPDATE_NOT_ALLOWED.getTag(), 
								GenericMessageTags.VISA_UPDATE_NOT_ALLOWED.getMessage(), endUserDB.getNid());
					}else {}

					if(!endUserTransaction.executeUpdateVisa(endUserDB)) {
						return new GenricResponse(1, GenericMessageTags.VISA_UPDATE_FAIL.getTag(), 
								GenericMessageTags.VISA_UPDATE_FAIL.getMessage(), endUserDB.getNid());
					}
					tag = "Update_Visa_Approved_CEIRAdmin";
					receiverUserType = "End User";
					sufeature=SubFeatures.Approve;
					//feature= 
					txnId = visaDb.getTxnId();
					userId=ceirActionRequest.getUserId();
				}else if(ceirActionRequest.getAction() == 1){
					String payloadTxnId = visaDb.getTxnId();
					// Check if someone else taken the same action on visa update.
					VisaUpdateDb visaUpdateTemp = visaUpdateRepo.getByTxnId(payloadTxnId);
					if(Objects.nonNull(visaUpdateTemp) && RegularizeDeviceStatus.REJECTED_BY_CEIR_ADMIN.getCode() == visaUpdateTemp.getStatus()) {
						String message = "Any other user have taken the same action on the visaUpdate [" + payloadTxnId + "]";
						logger.info(message);
						return new GenricResponse(10, "", message, payloadTxnId);
					}					
					visaDb.setStatus(RegularizeDeviceStatus.REJECTED_BY_CEIR_ADMIN.getCode());
					visaDb.setRemark(ceirActionRequest.getRemarks());
					tag = "Update_Visa_Reject_CEIRAdmin";	
					receiverUserType = "End User";
					txnId = visaDb.getTxnId();
					sufeature=SubFeatures.REJECT;
					placeholders.put("<Reason>", ceirActionRequest.getRemarks() );
				}else {
					return new GenricResponse(2, "unknown operation", "");
				}
				// Send Notifications
				if(Objects.nonNull(endUserDB)) {
					if(Objects.nonNull(endUserDB.getEmail()) && !endUserDB.getEmail().isEmpty() && !"NA".equalsIgnoreCase(endUserDB.getEmail())) {
						rawMails.add(new RawMail(tag, 
								endUserDB.getId(), 
								43, 
								Features.UPDATE_VISA, 
								sufeature, 
								visaDb.getTxnId(), 
								visaDb.getTxnId(), 
								placeholders,
								ReferTable.END_USER,
								null,
								receiverUserType));
					}
					else {
						logger.info("this end user don't have any email");
					}
				}

				VisaUpdateDb visaOutput=updateVisaRepo.save(visaDb);
				if(Objects.nonNull(visaOutput)) {
					if(Objects.nonNull(rawMails) && !rawMails.isEmpty()) {
						emailUtil.saveNotification(rawMails);	
					}
				}
				
				auditTrailRepository.save(new AuditTrail(userId, username, userTypeId,
						ceirActionRequest.getUserType(), 43,Features.UPDATE_VISA, sufeature, "", txnId,ceirActionRequest.getUserType(),ceirActionRequest.getPublicIp(),ceirActionRequest.getBrowser()));

			}else if("CEIRSYSTEM".equalsIgnoreCase(ceirActionRequest.getUserType())){
				visaDb = visaUpdateRepo.getByTxnId(ceirActionRequest.getTxnId());
				if(Objects.isNull(visaDb)) {
					return new GenricResponse(1, "transaction id is incorrect", "");				
				}
				endUserDB = endUserDbRepository.getById(visaDb.getEndUserDBData().getId());

				if(Objects.isNull(endUserDB.getVisaDb())) {
					logger.info("Request visa update should not be null.");
					return new GenricResponse(3, GenericMessageTags.NULL_VISA.getTag(), 
							GenericMessageTags.NULL_VISA.getMessage(), null);
				}else if(endUserDB.getVisaDb().isEmpty()){
					logger.info("Request visa update should not be empty.");
					return new GenricResponse(4, GenericMessageTags.EMPTY_VISA.getTag(), 
							GenericMessageTags.EMPTY_VISA.getMessage(), null);
				}else {
					latestVisa = endUserDB.getVisaDb().get(0);
				}
				placeholders.put("<Txn id>", visaDb.getTxnId());
				placeholders.put("<First name>", endUserDB.getFirstName());

				userTypeId = 0;
				if(ceirActionRequest.getAction() == 0) {
					String payloadTxnId = ceirActionRequest.getTxnId();
					// Check if someone else taken the same action on visa update.
					VisaUpdateDb visaUpdateTemp = visaUpdateRepo.getByTxnId(payloadTxnId);
					if(RegularizeDeviceStatus.PROCESSING.getCode() == visaUpdateTemp.getStatus()) {
						String message = "Any other user have taken the same action on the visaUpdate [" + payloadTxnId + "]";
						logger.info(message);
						return new GenricResponse(10, "", message, payloadTxnId);
					}

					visaDb.setStatus(RegularizeDeviceStatus.PROCESSING.getCode());
					tag = "MAIL_TO_USER_ON_CEIR_DEVICE_APPROVAL";
					txnId = visaDb.getTxnId();
					sufeature="SYSTEM_ACCEPT";
					updateVisaRepo.save(visaDb);
					txnId = visaDb.getTxnId();

				}else if(ceirActionRequest.getAction() == 1){
					String payloadTxnId = ceirActionRequest.getTxnId();
					// Check if someone else taken the same action on visa update.
					VisaUpdateDb visaUpdateTemp = visaUpdateRepo.getByTxnId(payloadTxnId);
					if(RegularizeDeviceStatus.REJECTED_BY_SYSTEM.getCode() == visaUpdateTemp.getStatus()) {
						String message = "Any other user have taken the same action on the visaUpdate [" + payloadTxnId + "]";
						logger.info(message);
						return new GenricResponse(10, "", message, payloadTxnId);
					}
					
					sufeature="SYSTEM_REJECT";
					visaDb.setStatus(RegularizeDeviceStatus.REJECTED_BY_SYSTEM.getCode());
					tag = "MAIL_TO_USER_ON_CEIR_DEVICE_DISAPPROVAL";	
					receiverUserType = "End User";
					txnId = visaDb.getTxnId();
					if(Objects.nonNull(endUserDB.getEmail()) && !endUserDB.getEmail().isEmpty() && !"NA".equalsIgnoreCase(endUserDB.getEmail())) {
						rawMails.add(new RawMail("Update_Visa_Reject_System", 
								endUserDB.getId(), 
								43, 
								Features.UPDATE_VISA, 
								SubFeatures.SYSTEM_REJECT, 
								visaDb.getTxnId(), 
								visaDb.getTxnId(), 
								placeholders,
								ReferTable.END_USER,
								null,
								receiverUserType));
						VisaUpdateDb visaOutput=updateVisaRepo.save(visaDb);
						if(Objects.nonNull(visaOutput)) {
							if(Objects.nonNull(rawMails) && !rawMails.isEmpty()) {
								emailUtil.saveNotification(rawMails);	
							}
						}
						txnId = visaDb.getTxnId();

					}
				}else if(ceirActionRequest.getAction() == 2) {
					String payloadTxnId = ceirActionRequest.getTxnId();
					// Check if someone else taken the same action on visa update.
					VisaUpdateDb visaUpdateTemp = visaUpdateRepo.getByTxnId(payloadTxnId);
					if(RegularizeDeviceStatus.PENDING_APPROVAL_FROM_CEIR_ADMIN.getCode() == visaUpdateTemp.getStatus()) {
						String message = "Any other user have taken the same action on the visaUpdate [" + payloadTxnId + "]";
						logger.info(message);
						return new GenricResponse(10, "", message, payloadTxnId);
					}
					
					visaDb.setStatus(RegularizeDeviceStatus.PENDING_APPROVAL_FROM_CEIR_ADMIN.getCode());
					tag = "MAIL_TO_USER_ON_CEIR_DEVICE_APPROVAL";
					txnId = visaDb.getTxnId();
					List<User> user= new ArrayList<User>();
					user=userStaticServiceImpl.getUserbyUsertypeId(8);
//					UserProfile ceirUserProfile = new UserProfile();
//					ceirUserProfile.setUser(userStaticServiceImpl.getCeirAdmin());
					sufeature="SYSTEM_ACCEPT";
					if(Objects.nonNull(endUserDB.getEmail()) && !endUserDB.getEmail().isEmpty() && !"NA".equalsIgnoreCase(endUserDB.getEmail())) {
						rawMails.add(new RawMail("Update_Visa_Approved_System", 
								endUserDB.getId(), 
								43, 
								Features.UPDATE_VISA, 
								SubFeatures.SYSTEM_ACCEPT, 
								visaDb.getTxnId(), 
								visaDb.getTxnId(), 
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
						placeholders.put("<Txn id>", visaDb.getTxnId());
						placeholders.put("<First name>", userProfile_generic_Response_Notification.getFirstName());
						
						logger.info("sending  mail for multiple CEIR Admin , first name of ceir admin..."+userProfile_generic_Response_Notification.getFirstName());
						rawMails.add(new RawMail("Update_Visa_Request_CEIRAdmin", 
								registerationUser.getId(), 
								43, 
								Features.UPDATE_VISA, 
								SubFeatures.SYSTEM_ACCEPT, 
								visaDb.getTxnId(), 
								visaDb.getTxnId(), 
								placeholders,
								ReferTable.USERS,
								null,
								"CEIRAdmin"));
					}


					VisaUpdateDb visaOutput = updateVisaRepo.save(visaDb);
					if(Objects.nonNull(visaOutput)) {
						if(Objects.nonNull(rawMails) && !rawMails.isEmpty()) {
							//if(visaOutput.getStatus()==RegularizeDeviceStatus.PROCESSING.getCode()) {
							if(visaOutput.getStatus()==RegularizeDeviceStatus.PENDING_APPROVAL_FROM_CEIR_ADMIN.getCode()) {	
							emailUtil.saveNotification(rawMails);				
							}
						}						}
					txnId = visaDb.getTxnId();

				}else {
					return new GenricResponse(2, "unknown operation", "");
				}
			}
			else {
				return new GenricResponse(1, "You are not allowed to do this operation.", "");
			}

			return new GenricResponse(0, "Visa Update SuccessFully.", ceirActionRequest.getTxnId());

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

	private GenericSpecificationBuilder<VisaUpdateDb> buildSpecification(FilterRequest filterRequest,List<StateMgmtDb> statusList, String source){

		GenericSpecificationBuilder<VisaUpdateDb> uPSB = new GenericSpecificationBuilder<VisaUpdateDb>(propertiesReader.dialect);	
		if(Objects.nonNull(filterRequest.getStartDate()) && filterRequest.getStartDate()!="")
			uPSB.with(new SearchCriteria("createdOn",filterRequest.getStartDate(), SearchOperation.GREATER_THAN, Datatype.DATE));

		if(Objects.nonNull(filterRequest.getEndDate()) && filterRequest.getEndDate()!="")
			uPSB.with(new SearchCriteria("createdOn",filterRequest.getEndDate(), SearchOperation.LESS_THAN, Datatype.DATE));

		if(Objects.nonNull(filterRequest.getStatus()) && filterRequest.getStatus()!=-1)
		{
			uPSB.with(new SearchCriteria("status",filterRequest.getStatus(), SearchOperation.EQUALITY, Datatype.INT));
		}
		else {
			//uPSB.with(new SearchCriteria("status",3, SearchOperation.EQUALITY, Datatype.INT));

			if(Objects.nonNull(filterRequest.getFeatureId()) && Objects.nonNull(filterRequest.getUserTypeId())) {

				List<DashboardUsersFeatureStateMap> dashboardUsersFeatureStateMap = dashboardUsersFeatureStateMapRepository.findByUserTypeIdAndFeatureId(filterRequest.getUserTypeId(), filterRequest.getFeatureId());
				logger.debug(dashboardUsersFeatureStateMap);

				List<Integer> deviceStatus = new LinkedList<>();

				if(Objects.nonNull(dashboardUsersFeatureStateMap)) {
					if("dashboard".equalsIgnoreCase(source) || "menu".equalsIgnoreCase(source)) {
						for(DashboardUsersFeatureStateMap dashboardUsersFeatureStateMap2 : dashboardUsersFeatureStateMap ) {
							deviceStatus.add(dashboardUsersFeatureStateMap2.getState());
						}
					}else if("filter".equalsIgnoreCase(source)) {
						if(regularizeDevice.nothingInFilter(filterRequest)) {
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
						uPSB.addSpecification(uPSB.in("status", deviceStatus));
					}else {
						logger.warn("no predefined status are available.");
					}
				}
			}
		}

		if(Objects.nonNull(filterRequest.getTxnId()) && !filterRequest.getTxnId().isEmpty()) {
			uPSB.with(new SearchCriteria("txnId", filterRequest.getTxnId(), SearchOperation.LIKE, Datatype.STRING));
		}
		if(Objects.nonNull(filterRequest.getNid()) && !filterRequest.getNid().isEmpty()) {
			uPSB.with(new SearchCriteria("nid", filterRequest.getNid(), SearchOperation.LIKE, Datatype.STRING));
		}
		if(Objects.nonNull(filterRequest.getVisaNumber()) && !filterRequest.getVisaNumber().isEmpty()) {
			uPSB.with(new SearchCriteria("visaNumber", filterRequest.getVisaNumber(), SearchOperation.LIKE, Datatype.STRING));
		}
		if(Objects.nonNull(filterRequest.getFileName()) && !filterRequest.getFileName().isEmpty()) {
			uPSB.with(new SearchCriteria("visaFileName", filterRequest.getFileName(), SearchOperation.LIKE, Datatype.STRING));
		}
		if(Objects.nonNull(filterRequest.getVisaExpiryDate()) && !filterRequest.getVisaExpiryDate().isEmpty()) {
			uPSB.with(new SearchCriteria("visaExpiryDate", filterRequest.getVisaExpiryDate(), SearchOperation.EQUALITY, Datatype.DATE));
		}
		if(Objects.nonNull(filterRequest.getVisaType()) && !filterRequest.getVisaType().isEmpty()) {
			uPSB.with(new SearchCriteria("visaType", filterRequest.getVisaType(), SearchOperation.LIKE, Datatype.STRING));
		}
		/*
		 * if(Objects.nonNull(filterRequest.getVisaExpiryDate()) &&
		 * filterRequest.getVisaExpiryDate()!="") { uPSB.with(new
		 * SearchCriteria("visaExpiryDate",filterRequest.getVisaExpiryDate(),
		 * SearchOperation.EQUALITY, Datatype.DATE)); }
		 */

		if(Objects.nonNull(filterRequest.getSearchString()) && !filterRequest.getSearchString().isEmpty()){
			uPSB.orSearch(new SearchCriteria("nid", filterRequest.getSearchString(), SearchOperation.LIKE, Datatype.STRING));
			uPSB.orSearch(new SearchCriteria("visaFileName", filterRequest.getSearchString(), SearchOperation.LIKE, Datatype.STRING));
			uPSB.orSearch(new SearchCriteria("visaNumber", filterRequest.getSearchString(), SearchOperation.LIKE, Datatype.STRING));
			uPSB.orSearch(new SearchCriteria("visaExpiryDate", filterRequest.getSearchString(), SearchOperation.LIKE, Datatype.STRING));
			uPSB.orSearch(new SearchCriteria("createdOn", filterRequest.getSearchString(), SearchOperation.LIKE, Datatype.STRING));
			uPSB.orSearch(new SearchCriteria("modifiedOn", filterRequest.getSearchString(), SearchOperation.LIKE, Datatype.STRING));
			uPSB.orSearch(new SearchCriteria("txnId", filterRequest.getSearchString(), SearchOperation.LIKE, Datatype.STRING));

		}
		logger.info("********************-----------((((((((((((((((*****************************************8 " +uPSB);
		return uPSB;
	}


	public Page<VisaUpdateDb>  viewAllUpdateVisaRecord(FilterRequest filterRequest, Integer pageNo, Integer pageSize,String source){
		try { 
			logger.info("filter data:  "+filterRequest);
			List<StateMgmtDb> statusList = stateMgmtServiceImpl.getByFeatureIdAndUserTypeId(filterRequest.getFeatureId(), filterRequest.getUserTypeId());
			String orderColumn=null;
			Sort.Direction direction;
			if(Objects.nonNull(filterRequest.getColumnName())) {
			logger.info("filter data column :  "+filterRequest.getColumnName());
			  orderColumn = "Created On".equalsIgnoreCase(filterRequest.getColumnName()) ? "createdOn"
					: "Modified On".equalsIgnoreCase(filterRequest.getColumnName()) ? "modifiedOn"
						:"Transaction ID".equalsIgnoreCase(filterRequest.getColumnName()) ? "txnId"
							: "Passport Number".equalsIgnoreCase(filterRequest.getColumnName()) ? "nid"
									: "Visa Type".equalsIgnoreCase(filterRequest.getColumnName()) ? "visaType"
											: "Visa Number".equalsIgnoreCase(filterRequest.getColumnName())
													? "visaNumber"
													:"File Name".equalsIgnoreCase(filterRequest.getColumnName())
															? "visaFileName" 
																	:"Visa Expiry Date".equalsIgnoreCase(filterRequest.getColumnName())
																	? "visaExpiryDate"
																			:"Status".equalsIgnoreCase(filterRequest.getColumnName())
																			? "status"
																	  : "modifiedOn";
			
			logger.info("direction and column name:  "+SortDirection.getSortDirection(filterRequest.getSort())+"-----"+orderColumn);
			if("modifiedOn".equalsIgnoreCase(orderColumn)) {
				direction=Sort.Direction.DESC;
			}
			else {
				direction= SortDirection.getSortDirection(filterRequest.getSort());
			}
			if("modifiedOn".equalsIgnoreCase(orderColumn) && SortDirection.getSortDirection(filterRequest.getSort()).equals(Sort.Direction.ASC)) {
				direction=Sort.Direction.ASC;
			}
			}
			else {
				direction=Sort.Direction.DESC;
				orderColumn="modifiedOn";
			}
			logger.info("final column :  "+orderColumn+"  direction--"+direction);
			Pageable pageable = PageRequest.of(pageNo, pageSize, new Sort(direction,orderColumn));
			Page<VisaUpdateDb> page = updateVisaRepository.findAll( buildSpecification(filterRequest,statusList, source).build(), pageable );

			if(source.equalsIgnoreCase("menu")) {
			auditTrailRepository.save(new AuditTrail(filterRequest.getUserId(), filterRequest.getUserName(), 8L,
					filterRequest.getUserType(), 43,Features.UPDATE_VISA, SubFeatures.VIEW_ALL, "","NA",filterRequest.getUserType(),filterRequest.getPublicIp(),filterRequest.getBrowser()));
			}
			else {
				auditTrailRepository.save(new AuditTrail(filterRequest.getUserId(), filterRequest.getUserName(), 8L,
						filterRequest.getUserType(), 43,Features.UPDATE_VISA, SubFeatures.FILTER, "","NA",filterRequest.getUserType(),filterRequest.getPublicIp(),filterRequest.getBrowser()));	
			}

			for(VisaUpdateDb visa : page.getContent()) {
				logger.info("after fetching state mgmt data");

				for(StateMgmtDb stateMgmtDb : statusList) {
					if(visa.getStatus() == stateMgmtDb.getState()) {
						visa.setStateInterp(stateMgmtDb.getInterp());
						break;
					}
				}
				// setInterp(consignmentMgmt2);
				if(Objects.nonNull(visa.getVisaType()))
				{
					visa.setVisaTypeInterp(interpSetter.setConfigInterp(Tags.VISA_TYPE, visa.getVisaType()));	
				}
				if(Objects.nonNull(visa.getEndUserDBData())) {
					visa.setUserId(visa.getEndUserDBData().getId());
				}
				if( Objects.isNull(visa.getVisaNumber()))
					visa.setVisaNumber("NA");
			}
			return page;

		} catch (Exception e) {
			logger.info("Exception found ="+e.getMessage());
			logger.info(e.getClass().getMethods().toString());
			logger.info(e.toString());
			return null;

		}
	}
	public List<VisaUpdateDb> getAllVisaUpdate(FilterRequest filterRequest) {

		try {
			List<StateMgmtDb> stateList = null;

			stateList = stateMgmtServiceImpl.getByFeatureIdAndUserTypeId(filterRequest.getFeatureId(), filterRequest.getUserTypeId());

			List<VisaUpdateDb> visaData = updateVisaRepository.findAll( buildSpecification(filterRequest,stateList,null).build());

			return visaData;

		} catch (Exception e) {
			logger.info("Exception found ="+e.getMessage());
			logger.info(e.getClass().getMethods().toString());
			logger.info(e.toString());
			return null;
		}

	}

	public FileDetails getFilterDataInFile(FilterRequest filterRequest,Integer pageNo, Integer pageSize,String source ) {
		logger.info("inside export user profile data into file service");
		logger.info("filter data:  "+filterRequest);
		String fileName = null;
		Writer writer   = null;
		UpdateVisaFileModel uVFm = null;
		SystemConfigurationDb userProfileDowlonadDir=configurationManagementServiceImpl.findByTag("file.download-dir");
		SystemConfigurationDb userProfileDowlonadLink=configurationManagementServiceImpl.findByTag("file.download-link");
		DateTimeFormatter dtf  = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


		String filePath  = userProfileDowlonadDir.getValue();
		logger.info("filePath:  "+filePath);
		StatefulBeanToCsvBuilder<UpdateVisaFileModel> builder = null;
		StatefulBeanToCsv<UpdateVisaFileModel> csvWriter      = null;
		List<UpdateVisaFileModel> fileRecords       = null;
		//HeaderColumnNameTranslateMappingStrategy<UpdateVisaFileModel> mapStrategy = null;
		MappingStrategy<UpdateVisaFileModel> mapStrategy = new CustomMappingStrategy<>();	
		try {
			//mapStrategy.setType(UpdateVisaFileModel.class);
			pageNo=0;
			pageSize=Integer.valueOf(configurationManagementServiceImpl.findByTag("file.max-file-record").getValue());
			List<VisaUpdateDb> visaData = this.viewAllUpdateVisaRecord(filterRequest, pageNo, pageSize,source).getContent();
			
					/* this.getAllVisaUpdate(filterRequest); */
			auditTrailRepository.save(new AuditTrail(filterRequest.getUserId(), filterRequest.getUserName(), 8L,
					filterRequest.getUserType(), 43,Features.UPDATE_VISA, SubFeatures.EXPORT, "","NA",filterRequest.getUserType(),filterRequest.getPublicIp(),filterRequest.getBrowser()));

			for(VisaUpdateDb visa : visaData) {
				List<StateMgmtDb> statusList = stateMgmtServiceImpl.getByFeatureIdAndUserTypeId(filterRequest.getFeatureId(), filterRequest.getUserTypeId());
				logger.info("after fetching state mgmt data");

				for(StateMgmtDb stateMgmtDb : statusList) {
					if(visa.getStatus() == stateMgmtDb.getState()) {
						visa.setStateInterp(stateMgmtDb.getInterp());
						break;
					}
				}
				// setInterp(consignmentMgmt2);
				if(Objects.nonNull(visa.getVisaType()))
				{
					visa.setVisaTypeInterp(interpSetter.setConfigInterp(Tags.VISA_TYPE, visa.getVisaType()));	

				}
			}

			if( visaData.size()> 0 ) {
				fileName = LocalDateTime.now().format(dtf).replace(" ", "_")+"_UpdateVisa.csv";
			}else {
				fileName = LocalDateTime.now().format(dtf).replace(" ", "_")+"_UpdateVisa.csv";
			}
			logger.info(" file path plus filke name: "+Paths.get(filePath+fileName));
			writer = Files.newBufferedWriter(Paths.get(filePath+fileName));
			builder = new StatefulBeanToCsvBuilder<UpdateVisaFileModel>(writer);
		//csvWriter = builder.withQuotechar(CSVWriter.DEFAULT_QUOTE_CHARACTER).build();
			csvWriter = builder.withMappingStrategy(mapStrategy).withSeparator(',').withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).build();
			mapStrategy.setType(UpdateVisaFileModel.class);
			
			if( visaData.size() > 0 ) {
				//List<SystemConfigListDb> systemConfigListDbs = configurationManagementServiceImpl.getSystemConfigListByTag("GRIEVANCE_CATEGORY");
				fileRecords = new ArrayList<UpdateVisaFileModel>(); 
				for( VisaUpdateDb visa : visaData ) {
					uVFm = new UpdateVisaFileModel();
					uVFm.setRequestedOn(utility.converedtlocalTime(visa.getCreatedOn()));
					uVFm.setModifiedOn(utility.converedtlocalTime(visa.getModifiedOn()));
					uVFm.setVisaExpiryDate((DateUtil.dateToString(visa.getVisaExpiryDate())));
					uVFm.setVisaNumber(visa.getVisaNumber());
					uVFm.setVisaType(visa.getVisaTypeInterp());
					uVFm.setStatus(visa.getStateInterp());
					uVFm.setTxnId(visa.getTxnId());
					uVFm.setFileName(visa.getVisaFileName());
					uVFm.setNid((visa.getNid()));
					//System.out.println(uVFm.toString());
					fileRecords.add(uVFm);
				}
				csvWriter.write(fileRecords);
			}
			return new FileDetails( fileName, filePath,userProfileDowlonadLink.getValue().replace("$LOCAL_IP",
					propertiesReader.localIp) +fileName ); 
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}finally {
			try {

				if( writer != null )
					writer.close();
			} catch (IOException e) {}
		}

	}

	public GenricResponse endUserById(FilterRequest filterRequest) {
		try {
			EndUserDB endUserDB = endUserDbRepository.getById(filterRequest.getId());
			// End user is not registered with CEIR system.
			logger.info("inside end user data by id service and given data: "+filterRequest.getId());

			auditTrailRepository.save(new AuditTrail(filterRequest.getUserId(), filterRequest.getUserName(), 8L,
					filterRequest.getUserType(), 43,Features.UPDATE_VISA, SubFeatures.VIEW, "","NA",filterRequest.getUserType(),filterRequest.getPublicIp(),filterRequest.getBrowser()));


			if(Objects.nonNull(endUserDB)) {
				VisaUpdateDb visaUpdateDb=updateVisaRepo.findByEndUserDBData_Id(filterRequest.getId());
				List<RegularizeDeviceDb> regulaizedList=new ArrayList<RegularizeDeviceDb>();
				if(Objects.nonNull(endUserDB.getRegularizeDeviceDbs()))
					for(RegularizeDeviceDb regularizeData:endUserDB.getRegularizeDeviceDbs()) {
						regularizeData.setEndUserDB(null); 
						regulaizedList.add(regularizeData);
					}
				endUserDB.setRegularizeDeviceDbs(regulaizedList);
				logger.info("End User with id [" + filterRequest.getId() + "] does exist.");
				if(Objects.nonNull(endUserDB.getVisaDb())) {
					List<VisaDb> visaList=new ArrayList<VisaDb>();
					for(VisaDb visa:endUserDB.getVisaDb()) {
						visa.setVisaTypeInterp(interpSetter.setConfigInterp(Tags.VISA_TYPE, visa.getVisaType()));	
						visaList.add(visa);
					}
					endUserDB.setVisaDb(visaList);
					if(Objects.nonNull(endUserDB.getDocType())) {
						endUserDB.setDocTypeInterp(interpSetter.setTagId(Tags.DOC_TYPE, endUserDB.getDocType()));	
					}
					if(Objects.nonNull(visaUpdateDb.getRemark())) {
						endUserDB.setRejectedRemark(visaUpdateDb.getRemark());
					}
				}
				return new GenricResponse(1, "End User does exist.", "", endUserDB);
			}else {
				logger.info("End User with id [" + filterRequest.getId() + "] does not exist.");
				return new GenricResponse(0, "User does not exist.", "");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException("Custom Service", e.getMessage());
		}
	}
}