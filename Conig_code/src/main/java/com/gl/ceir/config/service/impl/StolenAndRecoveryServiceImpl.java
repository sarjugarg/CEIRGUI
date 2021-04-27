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

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.gl.ceir.config.ConfigTags;
import com.gl.ceir.config.EmailSender.EmailUtil;
import com.gl.ceir.config.configuration.PropertiesReader;
import com.gl.ceir.config.configuration.SortDirection;
import com.gl.ceir.config.exceptions.RequestInvalidException;
import com.gl.ceir.config.exceptions.ResourceServicesException;
import com.gl.ceir.config.feign.UserFeignClient;
import com.gl.ceir.config.model.AuditTrail;
import com.gl.ceir.config.model.ConsignmentMgmt;
import com.gl.ceir.config.model.ConsignmentUpdateRequest;
import com.gl.ceir.config.model.DashboardUsersFeatureStateMap;
import com.gl.ceir.config.model.FileDetails;
import com.gl.ceir.config.model.FilterRequest;
import com.gl.ceir.config.model.GenricResponse;
import com.gl.ceir.config.model.ResponseCountAndQuantity;
import com.gl.ceir.config.model.SearchCriteria;
import com.gl.ceir.config.model.SingleImeiDetails;
import com.gl.ceir.config.model.StateMgmtDb;
import com.gl.ceir.config.model.StockMgmt;
import com.gl.ceir.config.model.StolenIndividualUserDB;
import com.gl.ceir.config.model.StolenOrganizationUserDB;
import com.gl.ceir.config.model.StolenandRecoveryMgmt;
import com.gl.ceir.config.model.SystemConfigListDb;
import com.gl.ceir.config.model.SystemConfigurationDb;
import com.gl.ceir.config.model.User;
import com.gl.ceir.config.model.UserProfile;
import com.gl.ceir.config.model.WebActionDb;
import com.gl.ceir.config.model.constants.Alerts;
import com.gl.ceir.config.model.constants.ConsignmentStatus;
import com.gl.ceir.config.model.constants.Datatype;
import com.gl.ceir.config.model.constants.Features;
import com.gl.ceir.config.model.constants.ReferTable;
import com.gl.ceir.config.model.constants.SearchOperation;
import com.gl.ceir.config.model.constants.StockStatus;
import com.gl.ceir.config.model.constants.StolenStatus;
import com.gl.ceir.config.model.constants.SubFeatures;
import com.gl.ceir.config.model.constants.Tags;
import com.gl.ceir.config.model.constants.WebActionDbState;
import com.gl.ceir.config.model.constants.WebActionDbSubFeature;
import com.gl.ceir.config.model.constants.WebActionStatus;
import com.gl.ceir.config.model.file.BlockUnblockFileModel;
import com.gl.ceir.config.model.file.BlockUnblockOperationFileModel;
import com.gl.ceir.config.model.file.StolenAndRecoveryFileModel;
import com.gl.ceir.config.repository.AuditTrailRepository;
import com.gl.ceir.config.repository.ConsignmentRepository;
import com.gl.ceir.config.repository.DashboardUsersFeatureStateMapRepository;
import com.gl.ceir.config.repository.SingleImeiHistoryDbRepository;
import com.gl.ceir.config.repository.StockManagementRepository;
import com.gl.ceir.config.repository.StolenAndRecoveryRepository;
import com.gl.ceir.config.repository.StolenIndividualUserRepository;
import com.gl.ceir.config.repository.StolenOrganizationUserRepository;
import com.gl.ceir.config.repository.UserProfileRepository;
import com.gl.ceir.config.repository.UserRepository;
import com.gl.ceir.config.repository.WebActionDbRepository;
import com.gl.ceir.config.request.model.Generic_Response_Notification;
import com.gl.ceir.config.request.model.RegisterationUser;
import com.gl.ceir.config.service.businesslogic.StateMachine;
import com.gl.ceir.config.specificationsbuilder.GenericSpecificationBuilder;
import com.gl.ceir.config.transaction.StolenAndRecoveryTransaction;
import com.gl.ceir.config.util.CustomMappingStrategy;
import com.gl.ceir.config.util.DateUtil;
import com.gl.ceir.config.util.InterpSetter;
import com.gl.ceir.config.util.Utility;
import com.gl.ceir.config.validate.impl.StolenValidator;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

@Service
public class StolenAndRecoveryServiceImpl {

	private static final Logger logger = LogManager.getLogger(StolenAndRecoveryServiceImpl.class);

	private ReentrantLock lock = new ReentrantLock();

	@Autowired
	WebActionDbRepository webActionDbRepository;

	@Autowired
	DashboardUsersFeatureStateMapRepository dashboardUsersFeatureStateMapRepository; 

	@Autowired
	StolenAndRecoveryRepository stolenAndRecoveryRepository;

	@Autowired
	PropertiesReader propertiesReader;

	@Autowired
	StockManagementRepository distributerManagementRepository;

	@Autowired
	ConsignmentRepository consignmentRepository;

	@Autowired
	SingleImeiHistoryDbRepository singleImeiHistoryDbRepository;

	@Autowired
	ConfigurationManagementServiceImpl configurationManagementServiceImpl; 

	@Autowired
	StateMgmtServiceImpl stateMgmtServiceImpl;

	@Autowired
	UserProfileRepository userProfileRepository;

	@Autowired
	EmailUtil emailUtil;

	@Autowired
	InterpSetter interpSetter;

	@Autowired
	StolenIndividualUserRepository stolenIndividualUserRepository;

	@Autowired
	StolenOrganizationUserRepository stolenOrganizationUserRepository;

	@Autowired
	StolenAndRecoveryTransaction stolenAndRecoveryTransaction;

	@Autowired
	UserRepository userRepository;

	@Autowired
	AuditTrailRepository auditTrailRepository;

	@Autowired
	StakeholderfeatureServiceImpl stakeholderfeatureServiceImpl;

	@Autowired
	AlertServiceImpl alertServiceImpl;

	@Autowired
	UserStaticServiceImpl userStaticServiceImpl;

	@Autowired 
	UserFeignClient userFeignClient;

	@Autowired
	Utility utility;

	@Autowired
	StolenValidator stolenValidator;

	public GenricResponse uploadDetails(StolenandRecoveryMgmt stolenandRecoveryMgmt) {

		try {
			if( userStaticServiceImpl.checkIfUserIsDisabled( stolenandRecoveryMgmt.getUserId() ))
				return new GenricResponse(5, "USER_IS_DISABLED","This account is disabled. Please enable the account to perform the operation.",
						stolenandRecoveryMgmt.getTxnId());
			stolenValidator.validateRegister(stolenandRecoveryMgmt);

			WebActionDb webActionDb = new WebActionDb(decideFeature(stolenandRecoveryMgmt.getRequestType()), 
					SubFeatures.REGISTER, 
					WebActionStatus.INIT.getCode(), stolenandRecoveryMgmt.getTxnId());

			stolenandRecoveryMgmt.setFileStatus(StolenStatus.INIT.getCode());

			// Only for logs purpose.
			if(stolenandRecoveryMgmt.getRequestType() == 1) {
				logger.info("Recovery request for txn_id["+stolenandRecoveryMgmt.getTxnId()+"]");
			}else if(stolenandRecoveryMgmt.getRequestType() == 3) {
				logger.info("Unblock request for txn_id["+stolenandRecoveryMgmt.getTxnId()+"]");
			}

			setBlockingType(stolenandRecoveryMgmt);

			if(Objects.nonNull(stolenandRecoveryMgmt.getStolenIndividualUserDB())) {
				StolenIndividualUserDB stolenIndividualUserDB = stolenandRecoveryMgmt.getStolenIndividualUserDB();
				stolenandRecoveryMgmt.setQty(countImeiForIndividual(stolenIndividualUserDB.getImeiEsnMeid1(), 
						stolenIndividualUserDB.getImeiEsnMeid2(), 
						stolenIndividualUserDB.getImeiEsnMeid3(), 
						stolenIndividualUserDB.getImeiEsnMeid4()));

				stolenandRecoveryMgmt.getStolenIndividualUserDB().setStolenandRecoveryMgmt(stolenandRecoveryMgmt);
			} else if (Objects.nonNull(stolenandRecoveryMgmt.getStolenOrganizationUserDB())) {
				stolenandRecoveryMgmt.getStolenOrganizationUserDB().setStolenandRecoveryMgmt(stolenandRecoveryMgmt);
			}

			if(stolenAndRecoveryTransaction.executeUploadDetails(stolenandRecoveryMgmt, webActionDb)) {
				logger.info("Upload Successfully." +  stolenandRecoveryMgmt.getTxnId());
				addInAuditTrail(stolenandRecoveryMgmt.getUserId(), stolenandRecoveryMgmt.getTxnId(), 
						SubFeatures.UPLOAD, stolenandRecoveryMgmt.getRoleType(), 
						stolenandRecoveryMgmt.getRequestType(), 0,stolenandRecoveryMgmt.getPublicIp(),stolenandRecoveryMgmt.getBrowser());

				return new GenricResponse(0, "Upload Successfully.", stolenandRecoveryMgmt.getTxnId());
			}else {
				logger.info("Upload have been failed." + stolenandRecoveryMgmt.getTxnId());
				return new GenricResponse(1, "Upload have been failed.", stolenandRecoveryMgmt.getTxnId());
			}

		}catch (RequestInvalidException e) {
			logger.error("Request validation failed for txnId[" + stolenandRecoveryMgmt.getTxnId() + "]" + e);
			
			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", decideFeature(5L));
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.REGISTER);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_013, stolenandRecoveryMgmt.getUserId().intValue(), bodyPlaceHolderMap);
			
			throw e;
		}catch (Exception e) {
			logger.error(e.getMessage(), e);

			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", decideFeature(5L));
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.REGISTER);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_011, stolenandRecoveryMgmt.getUserId().intValue(), bodyPlaceHolderMap);

			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	@Transactional
	public GenricResponse v2uploadDetails(StolenandRecoveryMgmt stolenandRecoveryDetails) {

		try {
			if( userStaticServiceImpl.checkIfUserIsDisabled( stolenandRecoveryDetails.getUserId() ))
				return new GenricResponse(5, "USER_IS_DISABLED","This account is disabled. Please enable the account to perform the operation.",
						stolenandRecoveryDetails.getTxnId());
			stolenValidator.validateRegister(stolenandRecoveryDetails);

			// Only for logs purpose.
			if(stolenandRecoveryDetails.getRequestType() == 1) {
				logger.info("Recovery request for txn_id["+stolenandRecoveryDetails.getTxnId()+"]");
			}else if(stolenandRecoveryDetails.getRequestType() == 3) {
				logger.info("Unblock request for txn_id["+stolenandRecoveryDetails.getTxnId()+"]");
			}

			setBlockingType(stolenandRecoveryDetails);

			// Single = 4
			if(stolenandRecoveryDetails.getSourceType() == 4){
				SingleImeiDetails singleImeiDetails = new SingleImeiDetails();	
				singleImeiDetails.setFirstImei(stolenandRecoveryDetails.getImei());
				singleImeiDetails.setsARm(stolenandRecoveryDetails);
				stolenandRecoveryDetails.setSingleImeiDetails(singleImeiDetails);
			}

			WebActionDb webActionDb = new WebActionDb();
			webActionDb.setFeature(decideFeature(stolenandRecoveryDetails.getRequestType()));
			webActionDb.setSubFeature(WebActionDbSubFeature.UPLOAD.getName());
			webActionDb.setTxnId(stolenandRecoveryDetails.getTxnId());
			webActionDb.setState(WebActionDbState.INIT.getCode());


			stolenandRecoveryDetails.setFileStatus(WebActionDbState.INIT.getCode());
			stolenAndRecoveryRepository.save(stolenandRecoveryDetails);

			webActionDbRepository.save(webActionDb);
			addInAuditTrail(stolenandRecoveryDetails.getUserId(), stolenandRecoveryDetails.getTxnId(), SubFeatures.UPLOAD, stolenandRecoveryDetails.getRoleType(),stolenandRecoveryDetails.getRequestType(),0,stolenandRecoveryDetails.getPublicIp(),stolenandRecoveryDetails.getBrowser());
			return new GenricResponse(0, "Upload Successfully.", stolenandRecoveryDetails.getTxnId());

		}catch (RequestInvalidException e) {
			logger.error("Request validation failed for txnId[" + stolenandRecoveryDetails.getTxnId() + "]" + e);
			
			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", decideFeature(5L));
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.REGISTER);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_013, stolenandRecoveryDetails.getUserId().intValue(), bodyPlaceHolderMap);

			throw e;
		}catch (Exception e) {
			logger.error(e.getMessage(), e);

			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", decideFeature(5L));
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.REGISTER);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_011, stolenandRecoveryDetails.getUserId().intValue(), bodyPlaceHolderMap);

			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}

	}

	public Page<StolenandRecoveryMgmt> getAllInfo(FilterRequest filterRequest, Integer pageNo, Integer pageSize,String source){
		List<StateMgmtDb> stateInterpList = null;
		List<StateMgmtDb> statusList = null;
		String orderColumn=null;
		if(filterRequest.getFeatureId() == 5) {
			
		}
		if(filterRequest.getUserType().equals("Operation") || filterRequest.getUserType().equals("Operator")) {
			orderColumn = "0".equalsIgnoreCase(filterRequest.getColumnName()) ? "createdOn"
					: "1".equalsIgnoreCase(filterRequest.getColumnName()) ? "txnId"
							: "2".equalsIgnoreCase(filterRequest.getColumnName()) ? "requestType"
									: "3".equalsIgnoreCase(filterRequest.getColumnName()) ? "sourceType"
											: "4".equalsIgnoreCase(filterRequest.getColumnName())? "fileStatus"
													:"5".equalsIgnoreCase(filterRequest.getColumnName()) ? "qty" 
													    : "6".equalsIgnoreCase(filterRequest.getColumnName()) ? "deviceQuantity":"modifiedOn";
		}
		else {
		  orderColumn = "0".equalsIgnoreCase(filterRequest.getColumnName()) ? "createdOn"
				: "1".equalsIgnoreCase(filterRequest.getColumnName()) ? "txnId"
						: "3".equalsIgnoreCase(filterRequest.getColumnName()) ? "requestType"
								: "4".equalsIgnoreCase(filterRequest.getColumnName()) ? "sourceType"
										: "5".equalsIgnoreCase(filterRequest.getColumnName())
												? "fileStatus"
												:"6".equalsIgnoreCase(filterRequest.getColumnName()) ? "qty" 
												: "7".equalsIgnoreCase(filterRequest.getColumnName()) ? "deviceQuantity":"modifiedOn";
		if(filterRequest.getFeatureId() == 7 && "2".equalsIgnoreCase(filterRequest.getColumnName())) {
			orderColumn = "operatorTypeId";
		}
		else if(filterRequest.getFeatureId() == 5 && "2".equalsIgnoreCase(filterRequest.getColumnName()) && filterRequest.getUserType().equals("CEIRAdmin")) {
			orderColumn = "blockingType";
		}
		}
		Sort.Direction direction;
		if("modifiedOn".equalsIgnoreCase(orderColumn)) {
			direction=Sort.Direction.DESC;
		}
		else {
			direction= SortDirection.getSortDirection(filterRequest.getSort());
		}
		logger.info("final column name=="+orderColumn+" and sorting order== "+direction);
		Pageable pageable = PageRequest.of(pageNo, pageSize, new Sort(direction, orderColumn));
	
	//	Pageable pageable = PageRequest.of(pageNo, pageSize, new Sort(Sort.Direction.DESC, "modifiedOn"));
		
		try {
			stolenValidator.validateFilter(filterRequest);

			statusList = stateMgmtServiceImpl.getByFeatureIdAndUserTypeId(filterRequest.getFeatureId(), filterRequest.getUserTypeId());
			logger.info("source== "+source);
			Page<StolenandRecoveryMgmt> stolenandRecoveryMgmtPage = stolenAndRecoveryRepository.findAll(buildSpecification(filterRequest, statusList, source).build(), pageable);
			stateInterpList = stateMgmtServiceImpl.getByFeatureIdAndUserTypeId(filterRequest.getFeatureId(), filterRequest.getUserTypeId());
			logger.info(stateInterpList);

			for(StolenandRecoveryMgmt stolenandRecoveryMgmt : stolenandRecoveryMgmtPage.getContent()) {				
				for(StateMgmtDb stateMgmtDb : stateInterpList) {
					if(stolenandRecoveryMgmt.getFileStatus() == stateMgmtDb.getState()) {
						stolenandRecoveryMgmt.setStateInterp(stateMgmtDb.getInterp()); 
						break;
					}
				}

				setInterp(stolenandRecoveryMgmt);

				// Operator type id for stolen request of registered by Ceir Admin.
				if(Objects.nonNull(stolenandRecoveryMgmt.getOperatorTypeId())) {
					if(stolenandRecoveryMgmt.getOperatorTypeId() == -1)
						stolenandRecoveryMgmt.setOperatorTypeIdInterp("Operation");
//						stolenandRecoveryMgmt.setOperatorTypeIdInterp("CEIR Admin");
				}else {
					stolenandRecoveryMgmt.setOperatorTypeIdInterp("Operation");
					logger.info("WARN : OperatorTypeId is null for [" + stolenandRecoveryMgmt + "]");
				}
			}

			logger.info(stolenandRecoveryMgmtPage.getContent());
			if(source.equalsIgnoreCase("menu")) {
			addInAuditTrail(Long.valueOf(filterRequest.getUserId()), "NA", SubFeatures.VIEW_ALL, filterRequest.getRoleType(),filterRequest.getRequestType(),filterRequest.getFeatureId(),filterRequest.getPublicIp(),filterRequest.getBrowser());
			}
			else {
				addInAuditTrail(Long.valueOf(filterRequest.getUserId()), "NA", SubFeatures.FILTER, filterRequest.getRoleType(),filterRequest.getRequestType(),filterRequest.getFeatureId(),filterRequest.getPublicIp(),filterRequest.getBrowser());
			}
			/*
			 * if(Objects.nonNull(filterRequest.getTxnId()) &&
			 * !filterRequest.getTxnId().isEmpty()) {
			 * addInAuditTrail(Long.valueOf(filterRequest.getUserId()),
			 * filterRequest.getTxnId(), SubFeatures.FILTER, filterRequest.getRoleType(),
			 * filterRequest.getRequestType(),filterRequest.getFeatureId(),filterRequest.
			 * getPublicIp(),filterRequest.getBrowser()); } else {
			 * addInAuditTrail(Long.valueOf(filterRequest.getUserId()), "NA",
			 * SubFeatures.VIEW_ALL,
			 * filterRequest.getRoleType(),filterRequest.getRequestType(),filterRequest.
			 * getFeatureId(),filterRequest.getPublicIp(),filterRequest.getBrowser()); }
			 */
			return stolenandRecoveryMgmtPage;

		} catch (RequestInvalidException e) {
			logger.error("Request validation failed for txnId[" + filterRequest.getTxnId() + "]" + e);
			return new PageImpl<StolenandRecoveryMgmt>(new ArrayList<StolenandRecoveryMgmt>(1), pageable, 0);
		}catch (Exception e) {
			logger.error(e.getMessage(), e);

			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", decideFeature(5L));
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.VIEW_ALL);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_011, filterRequest.getUserId(), bodyPlaceHolderMap);

			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}	
	}

	public List<StolenandRecoveryMgmt> getAll(FilterRequest filterRequest, String source){

		List<StateMgmtDb> stateInterpList = null;
		List<StateMgmtDb> statusList = null;

		try {
			statusList = stateMgmtServiceImpl.getByFeatureIdAndUserTypeId(filterRequest.getFeatureId(), filterRequest.getUserTypeId());

			List<StolenandRecoveryMgmt> stolenandRecoveryMgmts = stolenAndRecoveryRepository.findAll(buildSpecification(filterRequest, statusList, source).build());
			stateInterpList = stateMgmtServiceImpl.getByFeatureIdAndUserTypeId(filterRequest.getFeatureId(), filterRequest.getUserTypeId());
			logger.info(stateInterpList);

			for(StolenandRecoveryMgmt stolenandRecoveryMgmt : stolenandRecoveryMgmts) {				
				for(StateMgmtDb stateMgmtDb : stateInterpList) {
					if(stolenandRecoveryMgmt.getFileStatus() == stateMgmtDb.getState()) {
						stolenandRecoveryMgmt.setStateInterp(stateMgmtDb.getInterp()); 
						break;
					}
				}

				setInterp(stolenandRecoveryMgmt);

				// Operator type id for stolen request of registered by Ceir Admin.
				if(Objects.nonNull(stolenandRecoveryMgmt.getOperatorTypeId())) {
					if(stolenandRecoveryMgmt.getOperatorTypeId() == -1) 
						stolenandRecoveryMgmt.setOperatorTypeIdInterp("CEIR Admin");
				}else {
					stolenandRecoveryMgmt.setOperatorTypeIdInterp("");
					logger.info("WARN : OperatorTypeId is null for [" + stolenandRecoveryMgmt + "]");
				}

			}

			return stolenandRecoveryMgmts;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", decideFeature(5L));
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.VIEW_ALL);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_011, filterRequest.getUserId(), bodyPlaceHolderMap);

			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}	
	}

	private GenericSpecificationBuilder<StolenandRecoveryMgmt> buildSpecification(FilterRequest filterRequest, List<StateMgmtDb> statusList, String source) {
		GenericSpecificationBuilder<StolenandRecoveryMgmt> srsb = new GenericSpecificationBuilder<>(propertiesReader.dialect);
		String ceirAdmin = "CEIRADMIN";
		String fileStatus = "fileStatus";
		logger.info("source == " + source);
		if(Objects.nonNull(filterRequest.getStartDate()) && !filterRequest.getStartDate().isEmpty())
			srsb.with(new SearchCriteria("createdOn", filterRequest.getStartDate() , SearchOperation.GREATER_THAN, Datatype.DATE));

		if(Objects.nonNull(filterRequest.getEndDate()) && !filterRequest.getEndDate().isEmpty())
			srsb.with(new SearchCriteria("createdOn", filterRequest.getEndDate() , SearchOperation.LESS_THAN, Datatype.DATE));

		if(Objects.nonNull(filterRequest.getTxnId()) && !filterRequest.getTxnId().isEmpty())
			srsb.with(new SearchCriteria("txnId", filterRequest.getTxnId(), SearchOperation.LIKE, Datatype.STRING));

		if(Objects.nonNull(filterRequest.getSourceType())) {
			srsb.with(new SearchCriteria("sourceType", filterRequest.getSourceType(), SearchOperation.EQUALITY, Datatype.STRING));
		}

		if(Objects.nonNull(filterRequest.getOperatorTypeId())) {
			srsb.with(new SearchCriteria("operatorTypeId", filterRequest.getOperatorTypeId(), SearchOperation.EQUALITY, Datatype.STRING));
		}
		if(Objects.nonNull(filterRequest.getQuantity()) && !filterRequest.getQuantity().isEmpty())
			srsb.with(new SearchCriteria("qty", filterRequest.getQuantity(), SearchOperation.LIKE, Datatype.STRING));
		
		if(Objects.nonNull(filterRequest.getDeviceQuantity()) && !filterRequest.getDeviceQuantity().isEmpty())
			srsb.with(new SearchCriteria("deviceQuantity", filterRequest.getDeviceQuantity(), SearchOperation.LIKE, Datatype.STRING));
		
		if(Objects.nonNull(filterRequest.getBlockingTypeFilter()) && !filterRequest.getBlockingTypeFilter().isEmpty())
			srsb.with(new SearchCriteria("blockingType", filterRequest.getBlockingTypeFilter(), SearchOperation.EQUALITY, Datatype.STRING));
		
		if(Objects.nonNull(filterRequest.getRequestType())) {
			srsb.with(new SearchCriteria("requestType", filterRequest.getRequestType(), SearchOperation.EQUALITY, Datatype.STRING));
		}else {
			if(Objects.nonNull(filterRequest.getFeatureId())) {
				List<Integer> configuredRequestTypeOfFeature = new LinkedList<>();
				List<SystemConfigListDb> systemConfigListDbs = configurationManagementServiceImpl.getSystemConfigListByTagAndFeatureId(Tags.REQ_TYPE, filterRequest.getFeatureId());
				logger.debug(systemConfigListDbs);

				if(Objects.nonNull(systemConfigListDbs)) {	
					for(SystemConfigListDb systemConfigListDb : systemConfigListDbs ) {
						configuredRequestTypeOfFeature.add(Integer.valueOf(systemConfigListDb.getValue()));
					}

					if(!configuredRequestTypeOfFeature.isEmpty()) {
						logger.info("List of configuredRequestTypeOfFeature = " + configuredRequestTypeOfFeature);
						srsb.addSpecification(srsb.in("requestType", configuredRequestTypeOfFeature));
					}else{
						logger.info("No predefined request type is configured for this request.");
					}
				}
			}
		}

		if(Objects.nonNull(filterRequest.getConsignmentStatus())) {
			srsb.with(new SearchCriteria(fileStatus, filterRequest.getConsignmentStatus(), SearchOperation.EQUALITY, Datatype.STRING));
		}else {
			if(Objects.nonNull(filterRequest.getFeatureId()) && Objects.nonNull(filterRequest.getUserTypeId())) {

				List<Integer> configuredStatus = new LinkedList<>();

				List<DashboardUsersFeatureStateMap> dashboardUsersFeatureStateMap = dashboardUsersFeatureStateMapRepository.findByUserTypeIdAndFeatureId(filterRequest.getUserTypeId(), filterRequest.getFeatureId());
				logger.debug(dashboardUsersFeatureStateMap);

				//Code written by Sonu
				logger.info("  source== "+source);
				if(Objects.nonNull(dashboardUsersFeatureStateMap)) {
					if("dashboard".equalsIgnoreCase(source) || "menu".equalsIgnoreCase(source)) {
						logger.info("  source in 1 == "+source);
						for(DashboardUsersFeatureStateMap dashboardUsersFeatureStateMap2 : dashboardUsersFeatureStateMap ) {
							configuredStatus.add(dashboardUsersFeatureStateMap2.getState());
						}
					}else if("filter".equalsIgnoreCase(source)) {
						logger.info("  source in 2 == "+source);
						if(nothingInFilter(filterRequest)) {
							for(DashboardUsersFeatureStateMap dashboardUsersFeatureStateMap2 : dashboardUsersFeatureStateMap ) {
								configuredStatus.add(dashboardUsersFeatureStateMap2.getState());
							}
						}else {
							for(StateMgmtDb stateMgmtDb : statusList ) {
								configuredStatus.add(stateMgmtDb.getState());
							}
						}
					}else if("noti".equalsIgnoreCase(source)) {
						logger.info("Skip status check, because source is noti.");
					}

					logger.info("Array list to add is = " + configuredStatus);
					if(!configuredStatus.isEmpty())
						srsb.addSpecification(srsb.in("fileStatus", configuredStatus));
					else{
						logger.info("No predefined status is configured for this request.");
					}
				}
			}
		}

		if(ceirAdmin.equalsIgnoreCase(filterRequest.getUserType())) {
			if(Objects.nonNull(filterRequest.getUserId())) { 
				logger.info("Inside ceir admin block.");
				srsb.or(new SearchCriteria("userId", filterRequest.getUserId(), SearchOperation.EQUALITY, Datatype.STRING));
			}
			else 
				logger.info("Usertype in request is must when ceir admin is logged in to the system.");

			if(Objects.nonNull(filterRequest.getConsignmentStatus())) {

			}
		}else if(!"Lawful Agency".equalsIgnoreCase(filterRequest.getUserType()) 
				&& !"Operator".equalsIgnoreCase(filterRequest.getUserType()) && !"Operation".equalsIgnoreCase(filterRequest.getUserType())) {
			if(Objects.nonNull(filterRequest.getUserId())) {
				logger.info("Inside !Lawful Agency block.");
				srsb.with(new SearchCriteria("userId", filterRequest.getUserId(), SearchOperation.EQUALITY, Datatype.STRING));
			}
		}

		if( "Operation".equalsIgnoreCase(filterRequest.getUserType()) ){
			srsb.with(new SearchCriteria("roleType", filterRequest.getUserType(), SearchOperation.EQUALITY, Datatype.STRING));
		}
		
		if(Objects.nonNull(filterRequest.getSearchString()) && !filterRequest.getSearchString().isEmpty()){
			srsb.orSearch(new SearchCriteria("txnId", filterRequest.getSearchString(), SearchOperation.LIKE, Datatype.STRING));
			srsb.orSearch(new SearchCriteria("qty", filterRequest.getSearchString(), SearchOperation.LIKE, Datatype.STRING));
			srsb.orSearch(new SearchCriteria("deviceQuantity", filterRequest.getSearchString(), SearchOperation.LIKE, Datatype.STRING));
		}

		return srsb;
	}


	public FileDetails getFilteredStolenAndRecoveryInFile(FilterRequest filterRequest, String source) {

		try {
			stolenValidator.validateFilter(filterRequest);

			DateTimeFormatter dtf  = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			DateTimeFormatter dtf2  = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

			SystemConfigurationDb filepath = configurationManagementServiceImpl.findByTag(ConfigTags.file_download_dir);
			logger.info("CONFIG : file_consignment_download_dir [" + filepath + "]");
			SystemConfigurationDb link = configurationManagementServiceImpl.findByTag(ConfigTags.file_download_link);
			logger.info("CONFIG : file_consignment_download_link [" + link + "]");

			String filePath = filepath.getValue();

			if(filterRequest.getFeatureId() == 5) {
				return exportStolenRecoveryData(filterRequest, source, dtf, dtf2, filePath, link);
			}
			if(filterRequest.getFeatureId() == 7 && filterRequest.getUserType().equalsIgnoreCase("Operation") ) {
				return exportBlockDataOperation(filterRequest, source, dtf, dtf2, filePath, link);
			}
			else {
				return exportBlockData(filterRequest, source, dtf, dtf2, filePath, link);
			}
		}catch (RequestInvalidException e) {
			logger.error("Request validation failed for txnId[" + filterRequest.getTxnId() + "]" + e);
			throw e;
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	private FileDetails exportStolenRecoveryData(FilterRequest filterRequest, String source, DateTimeFormatter dtf, DateTimeFormatter dtf2,
			String filePath, SystemConfigurationDb link) {
		StolenAndRecoveryFileModel srfm = null;
		Writer writer   = null;
		String fileName = null;

		StatefulBeanToCsvBuilder<StolenAndRecoveryFileModel> builder = null;
		StatefulBeanToCsv<StolenAndRecoveryFileModel> csvWriter = null;
		List< StolenAndRecoveryFileModel > fileRecords = null;
		CustomMappingStrategy<StolenAndRecoveryFileModel> mappingStrategy = new CustomMappingStrategy<>();

		try {
			List<StolenandRecoveryMgmt> stolenandRecoveryMgmts = getAll(filterRequest, source);

			fileName = LocalDateTime.now().format(dtf2).replace(" ", "_") + "_Stolen_Recovery.csv";

			writer = Files.newBufferedWriter(Paths.get(filePath+fileName));
			mappingStrategy.setType(StolenAndRecoveryFileModel.class);

			builder = new StatefulBeanToCsvBuilder<>(writer);
			csvWriter = builder.withMappingStrategy(mappingStrategy).withSeparator(',').withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).build();

			if( stolenandRecoveryMgmts.isEmpty() ) {
				csvWriter.write(new StolenAndRecoveryFileModel());
			}else {

				fileRecords = new ArrayList<>();
				for( StolenandRecoveryMgmt stolenandRecoveryMgmt : stolenandRecoveryMgmts ) {
					srfm = new StolenAndRecoveryFileModel();
					if(Objects.isNull(stolenandRecoveryMgmt)) {
						continue;
					}

					srfm.setCreatedOn(stolenandRecoveryMgmt.getCreatedOn().format(dtf));
					srfm.setModifiedOn( stolenandRecoveryMgmt.getModifiedOn().format(dtf));
					srfm.setTxnId( stolenandRecoveryMgmt.getTxnId());
					srfm.setRequestType(stolenandRecoveryMgmt.getRequestTypeInterp());
					srfm.setMode(stolenandRecoveryMgmt.getSourceTypeInterp());
					logger.info("Status : "+stolenandRecoveryMgmt.getStateInterp());
					srfm.setStolenStatus(stolenandRecoveryMgmt.getStateInterp());
					srfm.setFileName( stolenandRecoveryMgmt.getFileName());
					srfm.setDeviceQuantity(stolenandRecoveryMgmt.getDeviceQuantity());
					srfm.setQuantity(stolenandRecoveryMgmt.getQty());
					srfm.setBlockingType(stolenandRecoveryMgmt.getBlockingType());
					logger.debug(srfm);
					fileRecords.add(srfm);
				}
				csvWriter.write(fileRecords);
			}
			addInAuditTrail(Long.valueOf(filterRequest.getUserId()), "NA", SubFeatures.EXPORT, filterRequest.getRoleType(),filterRequest.getRequestType(),filterRequest.getFeatureId(),filterRequest.getPublicIp(),filterRequest.getBrowser());
			return new FileDetails( fileName, filePath, link.getValue().replace("$LOCAL_IP", propertiesReader.localIp) + fileName ); 

		}catch (Exception e) {
			logger.error(e.getMessage(), e);

			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", decideFeature(5L));
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.EXPORT);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_011, filterRequest.getUserId(), bodyPlaceHolderMap);

			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}finally {
			try {
				if( Objects.nonNull(writer) )
					writer.close();
			} catch (IOException e) {}
		}
	}

	private FileDetails exportBlockData(FilterRequest filterRequest, String source, DateTimeFormatter dtf, 
			DateTimeFormatter dtf2, String filePath, SystemConfigurationDb link) {
		BlockUnblockFileModel srfm = null;
		Writer writer   = null;
		String fileName = null;

		StatefulBeanToCsvBuilder<BlockUnblockFileModel> builder = null;
		StatefulBeanToCsv<BlockUnblockFileModel> csvWriter = null;
		List< BlockUnblockFileModel > fileRecords = null;
		CustomMappingStrategy<BlockUnblockFileModel> mappingStrategy = new CustomMappingStrategy<>();

		try {
			List<StolenandRecoveryMgmt> stolenandRecoveryMgmts = getAll(filterRequest, source);

			fileName = LocalDateTime.now().format(dtf2).replace(" ", "_") + "_Block_Unblock.csv";

			writer = Files.newBufferedWriter(Paths.get(filePath+fileName));
			mappingStrategy.setType(BlockUnblockFileModel.class);

			builder = new StatefulBeanToCsvBuilder<>(writer);
			csvWriter = builder.withMappingStrategy(mappingStrategy).withSeparator(',').withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).build();

			if( stolenandRecoveryMgmts.isEmpty() ) {
				csvWriter.write(new BlockUnblockFileModel());
			}else {

				fileRecords = new ArrayList<>();
				for( StolenandRecoveryMgmt stolenandRecoveryMgmt : stolenandRecoveryMgmts ) {
					srfm = new BlockUnblockFileModel();
					if(Objects.isNull(stolenandRecoveryMgmt)) {
						continue;
					}

					srfm.setCreatedOn(stolenandRecoveryMgmt.getCreatedOn().format(dtf));
					srfm.setModifiedOn( stolenandRecoveryMgmt.getModifiedOn().format(dtf));
					srfm.setTxnId( stolenandRecoveryMgmt.getTxnId());
					srfm.setRequestType(stolenandRecoveryMgmt.getRequestTypeInterp());
					srfm.setMode(stolenandRecoveryMgmt.getSourceTypeInterp());
					logger.info("Status : "+stolenandRecoveryMgmt.getStateInterp());
					srfm.setStolenStatus(stolenandRecoveryMgmt.getStateInterp());
					srfm.setQuantity(stolenandRecoveryMgmt.getQty());
					if(Objects.isNull(stolenandRecoveryMgmt.getOperatorTypeId())) {
						srfm.setSource("");
					}else if(stolenandRecoveryMgmt.getOperatorTypeId() == -1) {
						srfm.setSource("Operation");
					}else {
						srfm.setSource(stolenandRecoveryMgmt.getOperatorTypeIdInterp());
					}

					srfm.setFileName( stolenandRecoveryMgmt.getFileName());
					srfm.setDeviceQuantity(stolenandRecoveryMgmt.getDeviceQuantity());

					logger.debug(srfm);
					fileRecords.add(srfm);
				}
				csvWriter.write(fileRecords);
			}
			addInAuditTrail(Long.valueOf(filterRequest.getUserId()), "NA", SubFeatures.EXPORT, filterRequest.getRoleType(),filterRequest.getRequestType(),filterRequest.getFeatureId(),filterRequest.getPublicIp(),filterRequest.getBrowser());
			return new FileDetails( fileName, filePath, link.getValue().replace("$LOCAL_IP", propertiesReader.localIp) + fileName ); 

		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", decideFeature(5L));
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.EXPORT);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_011, filterRequest.getUserId(), bodyPlaceHolderMap);

			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}finally {
			try {
				if( Objects.nonNull(writer) )
					writer.close();
			} catch (IOException e) {}
		}
	}
	
	private FileDetails exportBlockDataOperation(FilterRequest filterRequest, String source, DateTimeFormatter dtf, 
			DateTimeFormatter dtf2, String filePath, SystemConfigurationDb link) {
		BlockUnblockOperationFileModel srfm = null;
		Writer writer   = null;
		String fileName = null;

		StatefulBeanToCsvBuilder<BlockUnblockOperationFileModel> builder = null;
		StatefulBeanToCsv<BlockUnblockOperationFileModel> csvWriter = null;
		List< BlockUnblockOperationFileModel > fileRecords = null;
		CustomMappingStrategy<BlockUnblockOperationFileModel> mappingStrategy = new CustomMappingStrategy<>();

		try {
			List<StolenandRecoveryMgmt> stolenandRecoveryMgmts = getAll(filterRequest, source);

			fileName = LocalDateTime.now().format(dtf2).replace(" ", "_") + "_Block_Unblock.csv";

			writer = Files.newBufferedWriter(Paths.get(filePath+fileName));
			mappingStrategy.setType(BlockUnblockOperationFileModel.class);

			builder = new StatefulBeanToCsvBuilder<>(writer);
			csvWriter = builder.withMappingStrategy(mappingStrategy).withSeparator(',').withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).build();

			if( stolenandRecoveryMgmts.isEmpty() ) {
				csvWriter.write(new BlockUnblockOperationFileModel());
			}else {

				fileRecords = new ArrayList<>();
				for( StolenandRecoveryMgmt stolenandRecoveryMgmt : stolenandRecoveryMgmts ) {
					srfm = new BlockUnblockOperationFileModel();
					if(Objects.isNull(stolenandRecoveryMgmt)) {
						continue;
					}

					srfm.setCreatedOn(stolenandRecoveryMgmt.getCreatedOn().format(dtf));
					srfm.setModifiedOn( stolenandRecoveryMgmt.getModifiedOn().format(dtf));
					srfm.setTxnId( stolenandRecoveryMgmt.getTxnId());
					srfm.setRequestType(stolenandRecoveryMgmt.getRequestTypeInterp());
					srfm.setMode(stolenandRecoveryMgmt.getSourceTypeInterp());
					logger.info("Status : "+stolenandRecoveryMgmt.getStateInterp());
					srfm.setStolenStatus(stolenandRecoveryMgmt.getStateInterp());
					srfm.setQuantity(stolenandRecoveryMgmt.getQty());
					if(Objects.isNull(stolenandRecoveryMgmt.getOperatorTypeId())) {
						srfm.setSource("");
					}else if(stolenandRecoveryMgmt.getOperatorTypeId() == -1) {
						srfm.setSource("Operation");
					}else {
						srfm.setSource(stolenandRecoveryMgmt.getOperatorTypeIdInterp());
					}

					srfm.setFileName( stolenandRecoveryMgmt.getFileName());
					srfm.setDeviceQuantity(stolenandRecoveryMgmt.getDeviceQuantity());

					logger.debug(srfm);
					fileRecords.add(srfm);
				}
				csvWriter.write(fileRecords);
			}
			addInAuditTrail(Long.valueOf(filterRequest.getUserId()), "NA", SubFeatures.EXPORT, filterRequest.getRoleType(),filterRequest.getRequestType(),filterRequest.getFeatureId(),filterRequest.getPublicIp(),filterRequest.getBrowser());
			return new FileDetails( fileName, filePath, link.getValue().replace("$LOCAL_IP", propertiesReader.localIp) + fileName ); 

		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", decideFeature(5L));
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.EXPORT);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_011, filterRequest.getUserId(), bodyPlaceHolderMap);

			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}finally {
			try {
				if( Objects.nonNull(writer) )
					writer.close();
			} catch (IOException e) {}
		}
	}

	@Transactional
	public GenricResponse uploadMultipleStolen(List<StolenandRecoveryMgmt> stolenandRecoveryMgmt) {
		try {
			Boolean bool = true;	
			if( stolenandRecoveryMgmt != null && stolenandRecoveryMgmt.size() > 0 
					&& userStaticServiceImpl.checkIfUserIsDisabled( stolenandRecoveryMgmt.get(0).getUserId() ))
				return new GenricResponse(5, "USER_IS_DISABLED","This account is disabled. Please enable the account to perform the operation.",
						stolenandRecoveryMgmt.get(0).getTxnId());
			for(StolenandRecoveryMgmt  request : stolenandRecoveryMgmt) {
				// Consignment = 0
				// Stock = 1
				if(request.getSourceType() == 0){
					ConsignmentMgmt consignmentMgmt = consignmentRepository.getByTxnId(request.getTxnId());

					// Stolen = 0
					if(request.getRequestType() == 0) {
						consignmentMgmt.setPreviousConsignmentStatus(consignmentMgmt.getConsignmentStatus());
						consignmentMgmt.setConsignmentStatus(ConsignmentStatus.STOLEN.getCode());
					}else {
						consignmentMgmt.setConsignmentStatus(consignmentMgmt.getPreviousConsignmentStatus());
					}

					consignmentRepository.save(consignmentMgmt);
				}else if(request.getSourceType() == 1) {

					StockMgmt stockMgmt = distributerManagementRepository.findByRoleTypeAndTxnId(request.getRoleType(), request.getTxnId());

					if(request.getRequestType() == 0) {
						stockMgmt.setPreviousStockStatus(stockMgmt.getStockStatus());
						stockMgmt.setStockStatus(StockStatus.STOLEN.getCode());
					}else {
						stockMgmt.setStockStatus(stockMgmt.getPreviousStockStatus());
					}
					distributerManagementRepository.save(stockMgmt);
				}

				WebActionDb webActionDb = new WebActionDb();
				webActionDb.setState(0);
				webActionDb.setFeature(decideFeature(request.getRequestType()));				
				webActionDb.setSubFeature("Upload");
				webActionDb.setData(request.getTxnId());

				webActionDbRepository.save(webActionDb);
				addInAuditTrail(request.getUserId(), request.getTxnId(), SubFeatures.UPLOAD, request.getRoleType(),request.getRequestType(),0,request.getPublicIp(),request.getBrowser());

			}

			stolenAndRecoveryRepository.saveAll(stolenandRecoveryMgmt);

			return new GenricResponse(0, "Upload SucessFully", "");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", decideFeature(5L));
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.VIEW_ALL);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_011, 0, bodyPlaceHolderMap);

			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	@Transactional
	public GenricResponse deleteRecord(FilterRequest filterRequest) {
		try {
			if( userStaticServiceImpl.checkIfUserIsDisabled( filterRequest.getUserId() ))
				return new GenricResponse(5, "USER_IS_DISABLED","This account is disabled. Please enable the account to perform the operation.",
						filterRequest.getTxnId());
			stolenValidator.validateDelete(filterRequest);
		}catch (RequestInvalidException e) {
			logger.error("Request validation failed for txnId[" + filterRequest.getTxnId() + "]" + e);
			throw e;
		}

		UserProfile userProfile = null;
		User user = null;
		Map<String, String> placeholderMap = new HashMap<>();
		Map<String, String> placeholderMap1 = new HashMap<>();
		String action = null;
		String txnId = null;
		String userMailTag = null;
		Boolean isUserCeirAdmin = false;
		String receiverUserType = filterRequest.getUserType();
		action = SubFeatures.DELETE;
		StolenandRecoveryMgmt stolenandRecoveryMgmt = stolenAndRecoveryRepository.getByTxnId(filterRequest.getTxnId());
		//Check if it's already accepted/rejected
		if(StolenStatus.APPROVED_BY_CEIR_ADMIN.getCode() == stolenandRecoveryMgmt.getFileStatus() 
				|| StolenStatus.REJECTED_BY_CEIR_ADMIN.getCode() == stolenandRecoveryMgmt.getFileStatus() ) {
			String message = "Any other user have taken the same action on the Stolen/Recovery/Block/Unblock [" + stolenandRecoveryMgmt.getTxnId() + "]";
			logger.info(message);
			return new GenricResponse(10, "", message, stolenandRecoveryMgmt.getTxnId());
		}
		user = userRepository.getById(stolenandRecoveryMgmt.getUserId());
		userProfile = user.getUserProfile();
		txnId = filterRequest.getTxnId();
		String featureName = null;

		// set feature name for notification  & mail 
		if(filterRequest.getFeatureId().equals(5)){
			featureName = Features.STOLEN_RECOVERY;
		}else {
			featureName = Features.BLOCK_UNBLOCK;
		}

		if(filterRequest.getRequestType() == 0) {
			userMailTag = "STOLEN_WITHDRAWN";
			txnId = stolenandRecoveryMgmt.getTxnId();
		}else if(filterRequest.getRequestType() == 1){
			userMailTag = "RECOVERY_WITHDRAWN";
			txnId =  stolenandRecoveryMgmt.getTxnId();
		}else if(filterRequest.getRequestType() == 2){
			userMailTag = "BLOCK_WITHDRAWN";
			txnId =  stolenandRecoveryMgmt.getTxnId();
		}else if(filterRequest.getRequestType() == 3){
			userMailTag = "UNBLOCK_WITHDRAWN";
			txnId =  stolenandRecoveryMgmt.getTxnId();
		}

		placeholderMap1.put("<First name>", userProfile.getFirstName());
		placeholderMap1.put("<Txn id>", filterRequest.getTxnId());

		String payloadTxnId = filterRequest.getTxnId();
		lock.lock();
		logger.info("lock taken by thread for [Delete] - " + Thread.currentThread().getName());

		try {
			StolenandRecoveryMgmt stolenandRecoveryMgmtInfo = stolenAndRecoveryRepository.getById(filterRequest.getId());
			if(Objects.isNull(filterRequest.getRemark())) {
				return new GenricResponse(5,"Remarks Missing", filterRequest.getTxnId());
			}
			if(Objects.isNull(stolenandRecoveryMgmtInfo)) {
				return new GenricResponse(4,"TxnId Does Not exist", filterRequest.getTxnId());
			}else {
				if("Lawful Agency".equalsIgnoreCase(filterRequest.getUserType()) 
						|| "Operator".equalsIgnoreCase(filterRequest.getUserType())
								|| "Operation".equalsIgnoreCase(filterRequest.getUserType())) {
					// Check if someone else taken the same action on Stolen/Recovery/Block/Unblock.
					StolenandRecoveryMgmt stolenandRecoveryMgmtTemp = stolenAndRecoveryRepository.getByTxnId(payloadTxnId);
					if(stolenandRecoveryMgmtTemp.getFileStatus() == 7) {
						String message = "Any other user have taken the same action on the Stolen/Recovery/Block/Unblock [" + payloadTxnId + "]";
						logger.info(message);
						return new GenricResponse(10, "", message, payloadTxnId);
					}

					if(stolenandRecoveryMgmtInfo.getFileStatus()==0 
							|| stolenandRecoveryMgmtInfo.getFileStatus()==2 
//							|| stolenandRecoveryMgmtInfo.getFileStatus()==3
							|| stolenandRecoveryMgmtInfo.getFileStatus()==4) {
						//set file status =7
						stolenandRecoveryMgmtInfo.setFileStatus(7);//withdrawn by user 
						stolenandRecoveryMgmtInfo.setRemark(filterRequest.getRemark());
					}
					else {
						return new GenricResponse(2,"State transaction not allowed", filterRequest.getTxnId());
					}
				}else if("CEIRAdmin".equalsIgnoreCase(filterRequest.getUserType())){
					// Check if someone else taken the same action on Stolen/Recovery/Block/Unblock.
					StolenandRecoveryMgmt stolenandRecoveryMgmtTemp = stolenAndRecoveryRepository.getByTxnId(payloadTxnId);
					if(stolenandRecoveryMgmtTemp.getFileStatus() == 6) {
						String message = "Any other user have taken the same action on the Stolen/Recovery/Block/Unblock [" + payloadTxnId + "]";
						logger.info(message);
						return new GenricResponse(10, "", message, payloadTxnId);
					}

					//set file status =6
					stolenandRecoveryMgmtInfo.setFileStatus(6);//withdrawn by CEIR Admin 
					stolenandRecoveryMgmtInfo.setRemark(filterRequest.getRemark());
					isUserCeirAdmin = Boolean.TRUE;
				}else {
					return new GenricResponse(3,"Operation not allowed", filterRequest.getTxnId());
				}

				stolenandRecoveryMgmtInfo.setDeleteFlag(0);
				stolenandRecoveryMgmtInfo.setRemark(filterRequest.getRemark());

				logger.info("going to delete record.");
				stolenAndRecoveryRepository.save(stolenandRecoveryMgmtInfo);

				// Add an entry in web_action_db.
				WebActionDb webActionDb = new WebActionDb(decideFeature(stolenandRecoveryMgmt.getRequestType()), 
						action, 
						WebActionStatus.INIT.getCode(), stolenandRecoveryMgmt.getTxnId());
				webActionDbRepository.save(webActionDb);
				logger.info("Entry in web action of action [Delete] for txnid [" + stolenandRecoveryMgmt.getTxnId() + "]");


				if(isUserCeirAdmin) {
					logger.info("user profile details-");
					logger.info(userProfile);
					Generic_Response_Notification generic_Response_Notification = userFeignClient.ceirInfoByUserTypeId(8);

					logger.info("generic_Response_Notification::::::::"+generic_Response_Notification);

					List<RegisterationUser> registerationUserList = generic_Response_Notification.getData();

					for(RegisterationUser registerationUser :registerationUserList) {
						UserProfile userProfile_generic_Response_Notification = new UserProfile();
						userProfile_generic_Response_Notification = userProfileRepository.getByUserId(registerationUser.getId());
						placeholderMap.put("<First name>", userProfile_generic_Response_Notification.getFirstName());
						placeholderMap.put("<Txn id>", filterRequest.getTxnId());

						emailUtil.saveNotification(userMailTag, 
								userProfile_generic_Response_Notification,
								filterRequest.getFeatureId(),
								featureName,
								action,
								filterRequest.getTxnId(),
								txnId,
								placeholderMap,
								filterRequest.getRoleType(),
								receiverUserType,
								"Users");
						logger.info("Record delete by CEIRAdmin , Notfication for CEIRAdmin have been saved.");
					}
					emailUtil.saveNotification(userMailTag, 
							userProfile, 
							filterRequest.getFeatureId(),
							featureName,
							action,
							filterRequest.getTxnId(),
							txnId,
							placeholderMap1,
							filterRequest.getRoleType(),
							receiverUserType,
							"Users");
					logger.info(" Record delete by CEIRAdmin ,Notfication for user have been saved.");
				}
				else {
					emailUtil.saveNotification(userMailTag, 
							userProfile, 
							filterRequest.getFeatureId(),
							featureName,
							action,
							filterRequest.getTxnId(),
							txnId,
							placeholderMap,
							filterRequest.getRoleType(),
							receiverUserType,
							"Users");
					logger.info("Record delete by user ,Notfication for user have  been saved.");	
				}
				addInAuditTrail(Long.valueOf(filterRequest.getUserId()), filterRequest.getTxnId(), SubFeatures.DELETE, filterRequest.getRoleType(),filterRequest.getRequestType(),filterRequest.getFeatureId(),filterRequest.getPublicIp(),filterRequest.getBrowser());
				return new GenricResponse(0,"Record Delete Sucessfully", filterRequest.getTxnId());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", decideFeature(5L));
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.DELETE);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_011, filterRequest.getUserId(), bodyPlaceHolderMap);

			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}finally {
			if(lock.isLocked()) {
				logger.info("lock released by thread [Delete] - " + Thread.currentThread().getName());
				lock.unlock();
			}
		}
	}

	@Transactional
	public GenricResponse updateRecord(StolenandRecoveryMgmt stolenandRecoveryMgmt) {

		try {
			if( userStaticServiceImpl.checkIfUserIsDisabled( stolenandRecoveryMgmt.getUserId() ))
				return new GenricResponse(5, "USER_IS_DISABLED","This account is disabled. Please enable the account to perform the operation.",
						stolenandRecoveryMgmt.getTxnId());
			stolenValidator.validateEdit(stolenandRecoveryMgmt);
			
			StolenandRecoveryMgmt stolenandRecoveryMgmtInfo = stolenAndRecoveryRepository.getByTxnId(stolenandRecoveryMgmt.getTxnId());
			logger.info(stolenandRecoveryMgmtInfo);
			if(Objects.isNull(stolenandRecoveryMgmtInfo)) {
				return new GenricResponse(4, "TxnId Does Not exist", stolenandRecoveryMgmt.getTxnId());
			}else {

				WebActionDb webActionDb = new WebActionDb(decideFeature(stolenandRecoveryMgmt.getRequestType()), 
						SubFeatures.UPDATE, 
						WebActionStatus.INIT.getCode(), stolenandRecoveryMgmt.getTxnId());

				// Only for logs purpose.
				if(stolenandRecoveryMgmt.getRequestType() == 1) {
					logger.info("Recovery request for txn_id[" + stolenandRecoveryMgmt.getTxnId() + "]");
				}else if(stolenandRecoveryMgmt.getRequestType() == 3) {
					logger.info("Unblock request for txn_id[" + stolenandRecoveryMgmt.getTxnId() + "]");
				}

				setBlockingType(stolenandRecoveryMgmt);

				// 0 = Stolen
				if (stolenandRecoveryMgmt.getRequestType() == 0){
					stolenandRecoveryMgmtInfo.setBlockingTimePeriod(stolenandRecoveryMgmt.getBlockingTimePeriod());
					stolenandRecoveryMgmtInfo.setBlockingType(stolenandRecoveryMgmt.getBlockingType());
				}

				stolenandRecoveryMgmtInfo.setFileName(stolenandRecoveryMgmt.getFileName());
				stolenandRecoveryMgmtInfo.setBlockCategory(stolenandRecoveryMgmt.getBlockCategory());
				stolenandRecoveryMgmtInfo.setRemark(stolenandRecoveryMgmt.getRemark());
				stolenandRecoveryMgmtInfo.setQty(stolenandRecoveryMgmt.getQty());
				stolenandRecoveryMgmtInfo.setFileStatus(StolenStatus.INIT.getCode());
				stolenandRecoveryMgmtInfo.setDeviceQuantity(stolenandRecoveryMgmt.getDeviceQuantity());
				stolenandRecoveryMgmtInfo.setRejectedRemark(null);
				// Update StolenIndividualUserDB
				if(Objects.nonNull(stolenandRecoveryMgmt.getStolenIndividualUserDB())) {
					StolenIndividualUserDB stolenIndividualUserDB = updateStolenIndividualUserDB(
							stolenandRecoveryMgmtInfo.getStolenIndividualUserDB(),
							stolenandRecoveryMgmt.getStolenIndividualUserDB()
							);

					stolenandRecoveryMgmtInfo.setQty(countImeiForIndividual(stolenIndividualUserDB.getImeiEsnMeid1(), 
							stolenIndividualUserDB.getImeiEsnMeid2(), 
							stolenIndividualUserDB.getImeiEsnMeid3(), 
							stolenIndividualUserDB.getImeiEsnMeid4()));

					stolenandRecoveryMgmtInfo.setStolenIndividualUserDB(stolenIndividualUserDB);
					stolenIndividualUserDB.setStolenandRecoveryMgmt(stolenandRecoveryMgmtInfo);

					logger.info("After object update " + stolenIndividualUserDB);
				}

				// update StolenOrganizationUserDB
				if(Objects.nonNull(stolenandRecoveryMgmt.getStolenOrganizationUserDB())) {
					StolenOrganizationUserDB stolenOrganizationUserDB = updateStolenOrganizationUserDB(
							stolenandRecoveryMgmtInfo.getStolenOrganizationUserDB(),
							stolenandRecoveryMgmt.getStolenOrganizationUserDB()
							);

					stolenandRecoveryMgmtInfo.setStolenOrganizationUserDB(stolenOrganizationUserDB);

					stolenOrganizationUserDB.setStolenandRecoveryMgmt(stolenandRecoveryMgmtInfo);

					logger.info("After object update " + stolenOrganizationUserDB);
				}

				logger.info("Final object StolenandRecoveryMgmt : " + stolenandRecoveryMgmtInfo);

				//	StolenandRecoveryMgmt stolenandRecoveryMgmtNew = 
				webActionDbRepository.save(webActionDb);
				logger.info("Update request saved in web action : " + webActionDb);
				stolenAndRecoveryRepository.save(stolenandRecoveryMgmtInfo);
				addInAuditTrail(Long.valueOf(stolenandRecoveryMgmt.getUserId()), stolenandRecoveryMgmt.getTxnId(), SubFeatures.UPDATE, stolenandRecoveryMgmt.getRoleType(),stolenandRecoveryMgmt.getRequestType(),0,stolenandRecoveryMgmt.getPublicIp(),stolenandRecoveryMgmt.getBrowser());
				return new GenricResponse(0, "Record update sucessfully", stolenandRecoveryMgmt.getTxnId());
			}
		}catch (RequestInvalidException e) {
			logger.error("Request validation failed for txnId[" + stolenandRecoveryMgmt.getTxnId() + "]" + e);
			throw e;
		}catch (Exception e) {

			logger.error(e.getMessage(), e);

			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", decideFeature(5L));
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.UPDATE);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_011, 0, bodyPlaceHolderMap);

			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	public StolenandRecoveryMgmt viewRecord(StolenandRecoveryMgmt stolenandRecoveryMgmt) {
		try {
			return stolenAndRecoveryRepository.getById(stolenandRecoveryMgmt.getId());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	public StolenandRecoveryMgmt getByTxnId(StolenandRecoveryMgmt stolenandRecoveryMgmt) {
		try {
			stolenValidator.validateViewById(stolenandRecoveryMgmt);
			
			logger.info("Going to get Stolen and recovery Info for txnId : " + stolenandRecoveryMgmt.getTxnId());

			if(Objects.isNull(stolenandRecoveryMgmt.getTxnId())) {
				throw new IllegalArgumentException();
			}

			StolenandRecoveryMgmt stolenandRecoveryMgmt2 = stolenAndRecoveryRepository.getByTxnId(stolenandRecoveryMgmt.getTxnId());
			setInterp(stolenandRecoveryMgmt2);

			addInAuditTrail(Long.valueOf(stolenandRecoveryMgmt.getUserId()), stolenandRecoveryMgmt.getTxnId(), SubFeatures.VIEW, stolenandRecoveryMgmt.getRoleType(),stolenandRecoveryMgmt.getRequestType(),0,stolenandRecoveryMgmt.getPublicIp(),stolenandRecoveryMgmt.getBrowser());
			return stolenandRecoveryMgmt2;
		} catch (RequestInvalidException e) {
			logger.error("Request validation failed for txnId[" + stolenandRecoveryMgmt.getTxnId() + "]" + e);
			throw e;
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	public ResponseCountAndQuantity getStolenAndRecoveryCount( long userId, Integer userTypeId, Integer featureId, String requestType, String userType ) {
		List<StateMgmtDb> featureList = null;
		List<Integer> status = new ArrayList<>();
		try {
			logger.info("Going to get StolenAndRecovery count.");
			featureList = stateMgmtServiceImpl.getByFeatureIdAndUserTypeId( featureId, userTypeId);
			if(Objects.nonNull(featureList)) {	
				for(StateMgmtDb stateDb : featureList ) {
					status.add(stateDb.getState());
				}
			}
			if( !userType.equalsIgnoreCase("ceiradmin"))
				return stolenAndRecoveryRepository.getStolenandRecoveryCount( userId, status, Integer.valueOf(requestType));
			else
				return stolenAndRecoveryRepository.getStolenandRecoveryCount( status, Integer.valueOf(requestType));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			return new ResponseCountAndQuantity(0,0);
		}
	}

	public GenricResponse acceptReject(ConsignmentUpdateRequest consignmentUpdateRequest) {
		try {
			if( !"CEIRSYSTEM".equalsIgnoreCase(consignmentUpdateRequest.getRoleType()) 
					&& userStaticServiceImpl.checkIfUserIsDisabled( consignmentUpdateRequest.getUserId() ))
				return new GenricResponse(5, "USER_IS_DISABLED","This account is disabled. Please enable the account to perform the operation.",
						consignmentUpdateRequest.getTxnId());
			stolenValidator.validateAcceptReject(consignmentUpdateRequest);
			UserProfile userProfile = null;
			Map<String, String> placeholderMap1 = null;
			StolenandRecoveryMgmt stolenandRecoveryMgmt = stolenAndRecoveryRepository.getByTxnId(consignmentUpdateRequest.getTxnId());
			Integer currentStatus = stolenandRecoveryMgmt.getFileStatus();

			// Fetch user_profile to update user over mail/sms regarding the action.
			userProfile = userProfileRepository.getByUserId(stolenandRecoveryMgmt.getUserId());

			User user = userRepository.getById(stolenandRecoveryMgmt.getUserId());
			logger.info("User is " + user);

			if(Objects.isNull(stolenandRecoveryMgmt)) {
				String message = "TxnId Does not Exist";
				logger.info(message + " " + consignmentUpdateRequest.getTxnId());
				return new GenricResponse(4, message, consignmentUpdateRequest.getTxnId());
			}

			lock.lock();
			logger.info("lock taken by thread : " + Thread.currentThread().getName());

			// 0 - Processing, 1 - Reject, 2 - Pending Approval
			if("CEIRADMIN".equalsIgnoreCase(consignmentUpdateRequest.getRoleType())){
				String mailTag = null;
				String action = null;
				String txnId = null;
				//Check if it's already accepted/rejected
				if(StolenStatus.APPROVED_BY_CEIR_ADMIN.getCode() == stolenandRecoveryMgmt.getFileStatus() 
						|| StolenStatus.REJECTED_BY_CEIR_ADMIN.getCode() == stolenandRecoveryMgmt.getFileStatus() ) {
					String message = "Any other user have taken the same action on the Stolen/Recovery/Block/Unblock [" + stolenandRecoveryMgmt.getTxnId() + "]";
					logger.info(message);
					return new GenricResponse(10, "", message, stolenandRecoveryMgmt.getTxnId());
				}
				if(consignmentUpdateRequest.getAction() == 0) {
					action = SubFeatures.ACCEPT;

					String payloadTxnId = stolenandRecoveryMgmt.getTxnId();
					// Check if someone else taken the same action on Stolen/Recovery/Block/Unblock.
					StolenandRecoveryMgmt stolenandRecoveryMgmtTemp = stolenAndRecoveryRepository.getByTxnId(payloadTxnId);
					if(StolenStatus.APPROVED_BY_CEIR_ADMIN.getCode() == stolenandRecoveryMgmtTemp.getFileStatus()) {
						String message = "Any other user have taken the same action on the Stolen/Recovery/Block/Unblock [" + payloadTxnId + "]";
						logger.info(message);
						return new GenricResponse(10, "", message, payloadTxnId);
					}

					if(consignmentUpdateRequest.getRequestType() == 0) {
						mailTag = "STOLEN_APPROVED_BY_CEIR_ADMIN";
						txnId = stolenandRecoveryMgmt.getTxnId();
					}else if(consignmentUpdateRequest.getRequestType() == 1){
						mailTag = "RECOVERY_APPROVED_BY_CEIR_ADMIN";
						txnId =  stolenandRecoveryMgmt.getTxnId();
					}else if(consignmentUpdateRequest.getRequestType() == 2){
						mailTag = "BLOCK_APPROVED_BY_CEIR_ADMIN";
						txnId =  stolenandRecoveryMgmt.getTxnId();
					}else if(consignmentUpdateRequest.getRequestType() == 3){
						mailTag = "UNBLOCK_APPROVED_BY_CEIR_ADMIN";
						txnId =  stolenandRecoveryMgmt.getTxnId();
					}else {
						logger.warn("unknown request type received for stolen and recovery.");
						return new GenricResponse(2, "unknown request type received for stolen and recovery.", consignmentUpdateRequest.getTxnId());
					}

					stolenandRecoveryMgmt.setFileStatus(StolenStatus.APPROVED_BY_CEIR_ADMIN.getCode());

				}else {
					action = SubFeatures.REJECT;

					String payloadTxnId = stolenandRecoveryMgmt.getTxnId();
					// Check if someone else taken the same action on Stolen/Recovery/Block/Unblock.
					StolenandRecoveryMgmt stolenandRecoveryMgmtTemp = stolenAndRecoveryRepository.getByTxnId(payloadTxnId);
					if(StolenStatus.REJECTED_BY_CEIR_ADMIN.getCode() == stolenandRecoveryMgmtTemp.getFileStatus()) {
						String message = "Any other user have taken the same action on the Stolen/Recovery/Block/Unblock [" + payloadTxnId + "]";
						logger.info(message);
						return new GenricResponse(10, "", message, payloadTxnId);
					}

					if(consignmentUpdateRequest.getRequestType() == 0) {
						mailTag = "STOLEN_REJECT_BY_CEIR_ADMIN";
						txnId =  stolenandRecoveryMgmt.getTxnId();
					}else if(consignmentUpdateRequest.getRequestType() == 1){
						mailTag = "RECOVERY_REJECT_BY_CEIR_ADMIN";
						txnId = stolenandRecoveryMgmt.getTxnId();
					}else if(consignmentUpdateRequest.getRequestType() == 2){
						mailTag = "BLOCK_REJECT_BY_CEIR_ADMIN";
						txnId = stolenandRecoveryMgmt.getTxnId();
					}else if(consignmentUpdateRequest.getRequestType() == 3){
						mailTag = "UNBLOCK_REJECT_BY_CEIR_ADMIN";
						txnId = stolenandRecoveryMgmt.getTxnId();
					}else {
						logger.warn("unknown request type received for stolen and recovery.");
						return new GenricResponse(2, "unknown request type received for stolen and recovery.", consignmentUpdateRequest.getTxnId());
					}

					stolenandRecoveryMgmt.setFileStatus(StolenStatus.REJECTED_BY_CEIR_ADMIN.getCode());
					stolenandRecoveryMgmt.setRejectedRemark(consignmentUpdateRequest.getRemarks());
				}

				stolenandRecoveryMgmt.setCeirAdminId(consignmentUpdateRequest.getUserId());
				if(!stolenAndRecoveryTransaction.updateStatusWithHistory(stolenandRecoveryMgmt)) {
					logger.warn("Unable to update Stolen and recovery entity.");
					return new GenricResponse(3, "Unable to update Stolen and recovery entity.", consignmentUpdateRequest.getTxnId());
				}else {
					placeholderMap1 = new HashMap<>();

					placeholderMap1.put("<First name>", userProfile.getFirstName());
					placeholderMap1.put("<Txn id>", txnId);
					placeholderMap1.put("<Reason>", consignmentUpdateRequest.getRemarks() );

					emailUtil.saveNotification(mailTag, 
							userProfile, 
							consignmentUpdateRequest.getFeatureId(),
							decideFeature(consignmentUpdateRequest.getRequestType()),
							action,
							consignmentUpdateRequest.getTxnId(),
							txnId,
							placeholderMap1,
							stolenandRecoveryMgmt.getRoleType(),
							user.getUsertype().getUsertypeName(),
							ReferTable.USERS);
					logger.info("Notfication have been saved for User.");

					if(consignmentUpdateRequest.getAction() == 0) {
						Generic_Response_Notification generic_Response_Notification =
								userFeignClient.ceirInfoByUserTypeId(8);

						logger.info("generic_Response_Notification::::::::"+
								generic_Response_Notification);

						List<RegisterationUser> registerationUserList =
								generic_Response_Notification.getData();

						for(RegisterationUser registerationUser :registerationUserList) { UserProfile
							userProfile_generic_Response_Notification = new UserProfile();
						userProfile_generic_Response_Notification =
								userProfileRepository.getByUserId(registerationUser.getId());
						placeholderMap1.put("<First name>", userProfile_generic_Response_Notification.getFirstName() );
						emailUtil.saveNotification(mailTag,
								userProfile_generic_Response_Notification,
								consignmentUpdateRequest.getFeatureId(),
								decideFeature(consignmentUpdateRequest.getRequestType()),
								action,
								consignmentUpdateRequest.getTxnId(), 
								txnId,
								placeholderMap1,
								stolenandRecoveryMgmt.getRoleType(),
								consignmentUpdateRequest.getUserType(),
								"Users");
						logger.info("Notfication have been saved for CEIR Admin."); }	
					}
				}

				// Add an entry in web_action_db.
				WebActionDb webActionDb = new WebActionDb(decideFeature(stolenandRecoveryMgmt.getRequestType()), 
						action, 
						WebActionStatus.INIT.getCode(), stolenandRecoveryMgmt.getTxnId());
				webActionDbRepository.save(webActionDb);
				logger.info("Entry in web action of action[" + action + "] for txnid [" + stolenandRecoveryMgmt.getTxnId() + "]");

				//}

				addInAuditTrail(Long.valueOf(stolenandRecoveryMgmt.getUserId()), stolenandRecoveryMgmt.getTxnId(), action, stolenandRecoveryMgmt.getRoleType(),stolenandRecoveryMgmt.getRequestType(),0,consignmentUpdateRequest.getPublicIp(),consignmentUpdateRequest.getBrowser());
			}else if("CEIRSYSTEM".equalsIgnoreCase(consignmentUpdateRequest.getRoleType())){
				String mailTag = null;
				String ceirMailTag = null;
				String action = null;
				String txnId = null;
				if(!StateMachine.isStolenStatetransitionAllowed("CEIRSYSTEM", stolenandRecoveryMgmt.getFileStatus())) {
					logger.info("state transition is not allowed." + consignmentUpdateRequest.getTxnId());
					return new GenricResponse(3, "state transition is not allowed.", consignmentUpdateRequest.getTxnId());
				}

				if(consignmentUpdateRequest.getAction() == 0) {
					action = SubFeatures.ACCEPT;

					String payloadTxnId = stolenandRecoveryMgmt.getTxnId();
					// Check if someone else taken the same action on Stolen/Recovery/Block/Unblock.
					StolenandRecoveryMgmt stolenandRecoveryMgmtTemp = stolenAndRecoveryRepository.getByTxnId(payloadTxnId);
					if(StolenStatus.PROCESSING.getCode() == stolenandRecoveryMgmtTemp.getFileStatus()) {
						String message = "Any other user have taken the same action on the Stolen/Recovery/Block/Unblock [" + payloadTxnId + "]";
						logger.info(message);
						return new GenricResponse(10, "", message, payloadTxnId);
					}

					if(consignmentUpdateRequest.getRequestType() == 0) {
						mailTag = "STOLEN_PROCESSED_SUCESSFULLY";
						ceirMailTag = "STOLEN_PROCESSED_SUCESSFULLY_TO_CEIR_ADMIN";
						txnId = stolenandRecoveryMgmt.getTxnId();
					}else if(consignmentUpdateRequest.getRequestType() == 1){
						mailTag = "RECOVERY_PROCESSED_SUCESSFULLY";
						ceirMailTag = "RECOVERY_PROCESSED_SUCESSFULLY_TO_CEIR_ADMIN";
						txnId =  stolenandRecoveryMgmt.getTxnId();
					}else if(consignmentUpdateRequest.getRequestType() == 2){
						mailTag = "BLOCK_PROCESSED_SUCESSFULLY";
						ceirMailTag = "BLOCK_PROCESSED_SUCESSFULLY_TO_CEIR_ADMIN";
						txnId = stolenandRecoveryMgmt.getTxnId();
					}else if(consignmentUpdateRequest.getRequestType() == 3){
						mailTag = "UNBLOCK_PROCESSED_SUCESSFULLY";
						ceirMailTag = "UNBLOCK_PROCESSED_SUCESSFULLY_TO_CEIR_ADMIN";
						txnId = stolenandRecoveryMgmt.getTxnId();
					}else {
						logger.warn("unknown request type received for stolen and recovery.");
					}

					stolenandRecoveryMgmt.setFileStatus(StolenStatus.PROCESSING.getCode());

				}else if(consignmentUpdateRequest.getAction() == 1) {
					action = SubFeatures.REJECT;

					String payloadTxnId = stolenandRecoveryMgmt.getTxnId();
					// Check if someone else taken the same action on Stolen/Recovery/Block/Unblock.
					StolenandRecoveryMgmt stolenandRecoveryMgmtTemp = stolenAndRecoveryRepository.getByTxnId(payloadTxnId);
					if(StolenStatus.REJECTED_BY_SYSTEM.getCode() == stolenandRecoveryMgmtTemp.getFileStatus()) {
						String message = "Any other user have taken the same action on the Stolen/Recovery/Block/Unblock [" + payloadTxnId + "]";
						logger.info(message);
						return new GenricResponse(10, "", message, payloadTxnId);
					}

					if(consignmentUpdateRequest.getRequestType() == 0){
						mailTag = "STOLEN_PROCESSED_FAILED";
						txnId =  stolenandRecoveryMgmt.getTxnId();
					}else if(consignmentUpdateRequest.getRequestType() == 1){
						mailTag = "RECOVERY_PROCESSED_FAILED";
						txnId =  stolenandRecoveryMgmt.getTxnId();
					}else if(consignmentUpdateRequest.getRequestType() == 2){
						mailTag = "BLOCK_PROCESSED_FAILED";
						txnId = stolenandRecoveryMgmt.getTxnId();
					}else if(consignmentUpdateRequest.getRequestType() == 3){
						mailTag = "UNBLOCK_PROCESSED_FAILED";
						txnId = stolenandRecoveryMgmt.getTxnId();
					}else {
						logger.warn("unknown request type received for stolen and recovery.");
						return new GenricResponse(2, "unknown request type received for stolen and recovery.", consignmentUpdateRequest.getTxnId());
					}

					stolenandRecoveryMgmt.setFileStatus(StolenStatus.REJECTED_BY_SYSTEM.getCode());
					stolenandRecoveryMgmt.setRejectedRemark(consignmentUpdateRequest.getRemarks());

				}else if(consignmentUpdateRequest.getAction() == 2) {
					action = SubFeatures.ACCEPT;

					String payloadTxnId = stolenandRecoveryMgmt.getTxnId();
					// Check if someone else taken the same action on Stolen/Recovery/Block/Unblock.
					StolenandRecoveryMgmt stolenandRecoveryMgmtTemp = stolenAndRecoveryRepository.getByTxnId(payloadTxnId);
					if(StolenStatus.PENDING_APPROVAL_FROM_CEIR_ADMIN.getCode() == stolenandRecoveryMgmtTemp.getFileStatus()) {
						String message = "Any other user have taken the same action on the Stolen/Recovery/Block/Unblock [" + payloadTxnId + "]";
						logger.info(message);
						return new GenricResponse(10, "", message, payloadTxnId);
					}

					if(consignmentUpdateRequest.getRequestType() == 0) {
						mailTag = "STOLEN_PROCESSED_SUCESSFULLY";
						ceirMailTag = "STOLEN_PROCESSED_SUCESSFULLY_TO_CEIR_ADMIN";
						txnId = stolenandRecoveryMgmt.getTxnId();
					}else if(consignmentUpdateRequest.getRequestType() == 1){
						mailTag = "RECOVERY_PROCESSED_SUCESSFULLY";
						ceirMailTag = "RECOVERY_PROCESSED_SUCESSFULLY_TO_CEIR_ADMIN";
						txnId =  stolenandRecoveryMgmt.getTxnId();
					}else if(consignmentUpdateRequest.getRequestType() == 2){
						mailTag = "BLOCK_PROCESSED_SUCESSFULLY";
						ceirMailTag = "BLOCK_PROCESSED_SUCESSFULLY_TO_CEIR_ADMIN";
						txnId = stolenandRecoveryMgmt.getTxnId();
					}else if(consignmentUpdateRequest.getRequestType() == 3){
						mailTag = "UNBLOCK_PROCESSED_SUCESSFULLY";
						ceirMailTag = "UNBLOCK_PROCESSED_SUCESSFULLY_TO_CEIR_ADMIN";
						txnId = stolenandRecoveryMgmt.getTxnId();
					}else {
						logger.warn("unknown request type received for stolen and recovery.");
					}
					stolenandRecoveryMgmt.setFileStatus(StolenStatus.PENDING_APPROVAL_FROM_CEIR_ADMIN.getCode());
				}else {
					logger.info("You are not allowed to do this operation.[" + stolenandRecoveryMgmt.getTxnId() + "]");
					return new GenricResponse(1, "You are not allowed to do this operation.", "");
				}

				if(!stolenAndRecoveryTransaction.updateStatusWithHistory(stolenandRecoveryMgmt)) {
					logger.warn("Unable to update Stolen and recovery entity.");
					return new GenricResponse(3, "Unable to update Stolen and recovery entity.", consignmentUpdateRequest.getTxnId());
				}else {
					placeholderMap1 = new HashMap<>();
					placeholderMap1.put("<First name>", userProfile.getFirstName());
					placeholderMap1.put("<Txn id>", txnId);

					if(currentStatus == StolenStatus.PROCESSING.getCode()) {
						emailUtil.saveNotification(mailTag, 
								userProfile, 
								consignmentUpdateRequest.getFeatureId(),
								decideFeature(consignmentUpdateRequest.getRequestType()),
								action,
								consignmentUpdateRequest.getTxnId(),
								txnId,
								placeholderMap1,
								"CEIRSYSTEM",
								user.getUsertype().getUsertypeName(),
								ReferTable.USERS);
						logger.info("proceed by system,Notfication  have been saved for User.");

						if(consignmentUpdateRequest.getAction() == 2) {
							Generic_Response_Notification generic_Response_Notification =
									userFeignClient.ceirInfoByUserTypeId(8);

							logger.info("generic_Response_Notification::::::::" + generic_Response_Notification);

							List<RegisterationUser> registerationUserList =
									generic_Response_Notification.getData();

							for(RegisterationUser registerationUser :registerationUserList) { UserProfile
								userProfile_generic_Response_Notification = new UserProfile();
							userProfile_generic_Response_Notification = userProfileRepository.getByUserId(registerationUser.getId());
							placeholderMap1.put("<First name>", userProfile_generic_Response_Notification.getFirstName());
							emailUtil.saveNotification(ceirMailTag,
									userProfile_generic_Response_Notification,
									consignmentUpdateRequest.getFeatureId(),
									decideFeature(consignmentUpdateRequest.getRequestType()),
									action,
									consignmentUpdateRequest.getTxnId(), 
									txnId,
									placeholderMap1,
									"CEIRSYSTEM",
									"CEIRADMIN", 
									ReferTable.USERS);
							logger.info(" proceed by system , Notfication have been saved for CEIR Admin.");
							}
						}
					}
				}

			}else {
				logger.warn("Accept/reject of Stock not allowed to you.");
				new GenricResponse(1, "Operation not Allowed", consignmentUpdateRequest.getTxnId());
			}

			return new GenricResponse(0, "Stolen/Block request Updated SuccessFully.", consignmentUpdateRequest.getTxnId());

		} catch (RequestInvalidException e) {
			logger.error("Request validation failed for txnId[" + consignmentUpdateRequest.getTxnId() + "]" + e);
			throw e;
		}catch (Exception e) {
			logger.error(e.getMessage(), e);

			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", decideFeature(5L));
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.ACCEPT_REJECT);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_011, 0, bodyPlaceHolderMap);

			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}finally {
			if(lock.isLocked()) {
				logger.info("lock released by thread : " + Thread.currentThread().getName());
				lock.unlock();
			}
		}
	}

	private StolenIndividualUserDB updateStolenIndividualUserDB(StolenIndividualUserDB stolenIndividualUserDBOld, 
			StolenIndividualUserDB stolenIndividualUserDBNew) {
		stolenIndividualUserDBNew.setId(stolenIndividualUserDBOld.getId());


		return stolenIndividualUserDBNew;
	}

	private StolenOrganizationUserDB updateStolenOrganizationUserDB(StolenOrganizationUserDB stolenOrganizationUserDbOld,
			StolenOrganizationUserDB stolenOrganizationUserDbNew) {
		stolenOrganizationUserDbNew.setId(stolenOrganizationUserDbOld.getId());

		return stolenOrganizationUserDbNew;
	}

	private void setInterp(StolenandRecoveryMgmt stolenandRecoveryMgmt) {
		if(Objects.nonNull(stolenandRecoveryMgmt.getSourceType()))
			stolenandRecoveryMgmt.setSourceTypeInterp(interpSetter.setConfigInterp(Tags.SOURCE_TYPE, stolenandRecoveryMgmt.getSourceType()));

		if(Objects.nonNull(stolenandRecoveryMgmt.getRequestType()))
			stolenandRecoveryMgmt.setRequestTypeInterp(interpSetter.setConfigInterp(Tags.REQ_TYPE, stolenandRecoveryMgmt.getRequestType()));

		if(Objects.nonNull(stolenandRecoveryMgmt.getOperatorTypeId()))
			stolenandRecoveryMgmt.setOperatorTypeIdInterp(interpSetter.setConfigInterp(Tags.OPERATORS, stolenandRecoveryMgmt.getOperatorTypeId()));

		if(Objects.nonNull(stolenandRecoveryMgmt.getBlockCategory()))
			stolenandRecoveryMgmt.setBlockCategoryInterp(interpSetter.setConfigInterp(Tags.BLOCK_CATEGORY, stolenandRecoveryMgmt.getBlockCategory()));

		if( Objects.nonNull( stolenandRecoveryMgmt.getSingleImeiDetails() ) ){
			if( Objects.nonNull( stolenandRecoveryMgmt.getSingleImeiDetails().getMultipleSimStatus() ) )
				stolenandRecoveryMgmt.getSingleImeiDetails().setMultipleSimStatusInterp(interpSetter.setConfigInterp(Tags.MULTI_SIM_STATUS, stolenandRecoveryMgmt.getSingleImeiDetails().getMultipleSimStatus()));

			if(Objects.nonNull(stolenandRecoveryMgmt.getSingleImeiDetails().getDeviceType()))
				stolenandRecoveryMgmt.getSingleImeiDetails().setDeviceTypeInterp(interpSetter.setConfigInterp(Tags.DEVICE_TYPE, stolenandRecoveryMgmt.getSingleImeiDetails().getDeviceType()));

			if(Objects.nonNull(stolenandRecoveryMgmt.getSingleImeiDetails().getDeviceIdType()))
				stolenandRecoveryMgmt.getSingleImeiDetails().setDeviceIdTypeInterp(interpSetter.setConfigInterp(Tags.DEVICE_ID_TYPE, stolenandRecoveryMgmt.getSingleImeiDetails().getDeviceIdType()));

			if(Objects.nonNull(stolenandRecoveryMgmt.getSingleImeiDetails().getCategory()))
				stolenandRecoveryMgmt.getSingleImeiDetails().setCategoryInterp(interpSetter.setConfigInterp(Tags.BLOCK_CATEGORY, stolenandRecoveryMgmt.getSingleImeiDetails().getCategory()));
		}
	}

	private String decideFeature(int requestType) {
		switch (requestType) {
		case 0:
			return "Stolen";
			//return decideFeature(5L);
		case 1:
			return "Recovery";
			// return decideFeature(5L);
		case 2:
			return "Block";
			//return decideFeature(7L);
		case 3:
			return "Unblock";
			// return decideFeature(7L);
		default:
			return null;
		}
	}

	private String decideFeature(Long id) {
		return stakeholderfeatureServiceImpl.getFeatureNameById(id);
	}

	private int countImeiForIndividual(String imei1, String imei2, String imei3, String imei4) {
		int count = 0;
		if(Objects.nonNull(imei1))
			count++;

		if(Objects.nonNull(imei2))
			count++;

		if(Objects.nonNull(imei3))
			count++;

		if(Objects.nonNull(imei4))
			count++;

		return count == 0? 1 : count;
	}

	private void addInAuditTrail(Long userId, String txnId, String subFeatureName, String roleType, Integer requestType, Integer featureId,String publicIP,String browesr) {

		User requestUser = null;
		String fearure = null;
		try {
			logger.info("Fetching user Info User ID " + userId);	
			requestUser = userRepository.getById(userId);
			if(Objects.nonNull(featureId) && featureId!=0) {
				if(featureId==5) {
					fearure = "Stolen/Recovery";
				}
				if(featureId==7) {
					fearure = "Block/unblock Devices";
				}
			}else {
				if(Objects.nonNull(requestType)) {
					if(requestType == 0 || requestType == 1) {
						featureId=5;
						fearure = "Stolen/Recovery";
					}
					else {
						featureId=7;
						fearure = "Block/unblock Devices";
					}
				}else {
					logger.error("Error while fetching user info requestType: " + requestType + " featureId : " + featureId);
				}
			}

			logger.info("User Details"+requestUser);
		} catch (Exception e) {
			logger.error("Error while fetching user information for user id = "+userId);
		}
		if(Objects.nonNull(requestUser)) {
			//	logger.info("Inserting in audit table for feature = "+Features.Sto+"and Subfeature"+subFeatureName);
			AuditTrail obj = new AuditTrail(
					requestUser.getId(),
					requestUser.getUsername(), 
					requestUser.getUsertype().getId(),
					requestUser.getUsertype().getUsertypeName(),
					featureId,
					fearure,
					subFeatureName,
					"", 
					txnId,
					roleType,publicIP,browesr);
			logger.info("Inserting in audit table object = "+obj);
			auditTrailRepository.save(obj);
		}else {
			logger.error("Could not find the user information");
		}
	}

	private void setBlockingType(StolenandRecoveryMgmt stolenandRecoveryMgmt) {
		if(Objects.isNull(stolenandRecoveryMgmt.getBlockingType())
				|| stolenandRecoveryMgmt.getBlockingType().equalsIgnoreCase("Default") 
				|| stolenandRecoveryMgmt.getBlockingType().isEmpty()) {
			SystemConfigurationDb systemConfigurationDb = configurationManagementServiceImpl.findByTag(ConfigTags.GREY_TO_BLACK_MOVE_PERIOD_IN_DAY);
			String date = DateUtil.nextDate(Integer.parseInt(systemConfigurationDb.getValue()), "yyyy-MM-dd");
			stolenandRecoveryMgmt.setBlockingTimePeriod(date); 
			stolenandRecoveryMgmt.setBlockingType("Default");
			logger.info("Set Default Blocking time period for txn_id [" + stolenandRecoveryMgmt.getTxnId() + "] Blockingtimeperiod [" + date + "]");
		}else if(stolenandRecoveryMgmt.getBlockingType().equalsIgnoreCase("Immediate")) {
			String date = DateUtil.nextDate(0, "yyyy-MM-dd");
			stolenandRecoveryMgmt.setBlockingTimePeriod(date); 
			stolenandRecoveryMgmt.setBlockingType("Immediate");
			logger.info("Set Immediate Blocking time period for txn_id [" + stolenandRecoveryMgmt.getTxnId() + "] Blockingtimeperiod [" + date + "]");
		}else {
			logger.info("Unable to set Blocking time period for txn_id " + stolenandRecoveryMgmt.getTxnId());
		}
	}

	public boolean nothingInFilter(FilterRequest filterRequest) {
		// Clean
		if(filterRequest.getStartDate().isEmpty() || filterRequest.getStartDate().equals("")) {
			filterRequest.setStartDate(null);
		}

		if(filterRequest.getEndDate().isEmpty() || filterRequest.getEndDate().equals("")) {
			filterRequest.setEndDate(null);
		}

		if(filterRequest.getTxnId().isEmpty() || filterRequest.getTxnId().equals("")) {
			filterRequest.setTxnId(null);
		}

		// Logic
		if(Objects.nonNull(filterRequest.getStartDate())) {
			return Boolean.FALSE;
		}

		if(Objects.nonNull(filterRequest.getEndDate())) {
			return Boolean.FALSE;
		}

		if(Objects.nonNull(filterRequest.getTxnId())) {
			return Boolean.FALSE;
		}

		if(Objects.nonNull(filterRequest.getDisplayName())) {
			return Boolean.FALSE;
		}

		if(Objects.nonNull(filterRequest.getFilteredUserType())) {
			return Boolean.FALSE;
		}

		if(Objects.nonNull(filterRequest.getConsignmentStatus()) ) {
			return Boolean.FALSE;
		}
		
		if(Objects.nonNull(filterRequest.getRequestType()) ) {
			return Boolean.FALSE;
		}
		
		if(Objects.nonNull(filterRequest.getSourceType()) ) {
			return Boolean.FALSE;
		}
		
		if(Objects.nonNull(filterRequest.getOperatorTypeId()) ) {
			return Boolean.FALSE;
		}
		if(Objects.nonNull(filterRequest.getQuantity()) ) {
			return Boolean.FALSE;
		}
		
		if(Objects.nonNull(filterRequest.getDeviceQuantity()) ) {
			return Boolean.FALSE;
		}
		
		if(Objects.nonNull(filterRequest.getBlockingTypeFilter()) ) {
			return Boolean.FALSE;
		}

		return Boolean.TRUE;
	}
}