package com.gl.ceir.config.service.impl;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

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
import com.gl.ceir.config.factory.ExportFileFactory;
import com.gl.ceir.config.feign.UserFeignClient;
import com.gl.ceir.config.model.AuditTrail;
import com.gl.ceir.config.model.ConsignmentUpdateRequest;
import com.gl.ceir.config.model.DashboardUsersFeatureStateMap;
import com.gl.ceir.config.model.FeatureValidateReq;
import com.gl.ceir.config.model.FileDetails;
import com.gl.ceir.config.model.FilterRequest;
import com.gl.ceir.config.model.GenricResponse;
import com.gl.ceir.config.model.SearchCriteria;
import com.gl.ceir.config.model.StateMgmtDb;
import com.gl.ceir.config.model.StatesInterpretationDb;
import com.gl.ceir.config.model.StockMgmt;
import com.gl.ceir.config.model.SystemConfigurationDb;
import com.gl.ceir.config.model.User;
import com.gl.ceir.config.model.UserProfile;
import com.gl.ceir.config.model.Userrole;
import com.gl.ceir.config.model.Usertype;
import com.gl.ceir.config.model.WebActionDb;
import com.gl.ceir.config.model.constants.Alerts;
import com.gl.ceir.config.model.constants.Datatype;
import com.gl.ceir.config.model.constants.Features;
import com.gl.ceir.config.model.constants.GenericMessageTags;
import com.gl.ceir.config.model.constants.SearchOperation;
import com.gl.ceir.config.model.constants.StockStatus;
import com.gl.ceir.config.model.constants.SubFeatures;
import com.gl.ceir.config.model.constants.WebActionDbFeature;
import com.gl.ceir.config.model.constants.WebActionDbState;
import com.gl.ceir.config.model.constants.WebActionDbSubFeature;
import com.gl.ceir.config.repository.AuditTrailRepository;
import com.gl.ceir.config.repository.DashboardUsersFeatureStateMapRepository;
import com.gl.ceir.config.repository.StatesInterpretaionRepository;
import com.gl.ceir.config.repository.StockManagementRepository;
import com.gl.ceir.config.repository.StockMgmtHistoryRepository;
import com.gl.ceir.config.repository.UserProfileRepo;
import com.gl.ceir.config.repository.UserProfileRepository;
import com.gl.ceir.config.repository.UserRepository;
import com.gl.ceir.config.repository.WebActionDbRepository;
import com.gl.ceir.config.request.model.Generic_Response_Notification;
import com.gl.ceir.config.request.model.RegisterationUser;
import com.gl.ceir.config.service.businesslogic.StateMachine;
import com.gl.ceir.config.specificationsbuilder.GenericSpecificationBuilder;
import com.gl.ceir.config.transaction.StockTransaction;
import com.gl.ceir.config.util.HttpResponse;
import com.gl.ceir.config.util.InterpSetter;
import com.gl.ceir.config.validate.impl.StockValidator;

@Service
public class StockServiceImpl {

	private static final Logger logger = LogManager.getLogger(StockServiceImpl.class);

	private ReentrantLock lock = new ReentrantLock();

	// This is set with @postconstruct
	private String featureName;

	@Autowired
	StockManagementRepository stockManagementRepository;

	@Autowired
	StockMgmtHistoryRepository stockMgmtHistoryRepository;

	@Autowired
	DashboardUsersFeatureStateMapRepository dashboardUsersFeatureStateMapRepository; 

	@Autowired
	WebActionDbRepository webActionDbRepository;

	@Autowired
	AuditTrailRepository auditTrailRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PropertiesReader propertiesReader;

	@Autowired
	UserProfileRepository userProfileRepository;

	@Autowired	
	EmailUtil emailUtil;

	@Autowired
	StateMgmtServiceImpl stateMgmtServiceImpl;

	@Autowired
	ConfigurationManagementServiceImpl configurationManagementServiceImpl; 

	@Autowired
	UserProfileRepo userProfileRepo;

	@Autowired
	InterpSetter interpSetter;

	@Autowired
	StatesInterpretaionRepository statesInterpretaionRepository;

	@Autowired 
	UserFeignClient userFeignClient;

	@Autowired
	StockTransaction stockTransaction;

	@Autowired
	StakeholderfeatureServiceImpl stakeholderfeatureServiceImpl;

	@Autowired
	AlertServiceImpl alertServiceImpl;

	@Autowired
	UserStaticServiceImpl userStaticServiceImpl;

	@Autowired
	ExportFileFactory exportFileFactory;

	@Autowired
	StockValidator stockValidator;

	public GenricResponse uploadStock(StockMgmt stockMgmt) {
		boolean isStockAssignRequest = Boolean.FALSE;
		boolean isAnonymousUpload = Boolean.FALSE;

		User user = null;

		try {
			if( !("End User".equalsIgnoreCase(stockMgmt.getUserType())) && userStaticServiceImpl.checkIfUserIsDisabled( stockMgmt.getUserId() ))
				return new GenricResponse(5, "USER_IS_DISABLED","This account is disabled. Please enable the account to perform the operation.",
						stockMgmt.getTxnId());
		//	stockValidator.validateRegister(stockMgmt);
			stockMgmt.setStockStatus(StockStatus.NEW.getCode());

			// Assign Stock is done by custom.
			if("Custom".equalsIgnoreCase(stockMgmt.getUserType())) {
				String secondaryRoleType = null;
				user =	userRepository.getByUsername(stockMgmt.getSupplierId());
				logger.info(user);

				if(Objects.isNull(user)) {
					logger.info("This is not a valid user to assign a stock. ");
					return new GenricResponse(2, "This is not a valid user.", "");
				}

				// Fetch all roles of user.
				List<String> userRoles = user.getUserRole().stream()
						.map(u -> u.getUsertypeData().getUsertypeName())
						.collect(Collectors.toList());

				logger.info(userRoles);

				if(userRoles.isEmpty()) {
					logger.info("No role assigned to the user.");
					return new GenricResponse(4, "No role assigned to the user.", "");
				}

				secondaryRoleType = getSecondaryRoleType(userRoles);

				if(Objects.isNull(secondaryRoleType)) {
					logger.info("User is not a distributer or retailer to assign a stock.");
					return new GenricResponse(5, "User is not a distributer or retailer to assign a stock.", "");
				}

				stockMgmt.setUserId(user.getId());
				stockMgmt.setUser(user);
				stockMgmt.setRoleType(secondaryRoleType);
				stockMgmt.setPortAddress( userRepository.getById(stockMgmt.getAssignerId()).getUserProfile().getPortAddress() );
				isStockAssignRequest = Boolean.TRUE;

				addInAuditTrail(stockMgmt.getAssignerId(), stockMgmt.getTxnId(), SubFeatures.ASSIGN, "Custom",stockMgmt.getPublicIp(),stockMgmt.getBrowser());

			}else if("End User".equalsIgnoreCase(stockMgmt.getUserType())){
				// Check if this feature is supported in current period.
				HttpResponse response = userFeignClient.validatePeriod(new FeatureValidateReq(4, 17));
				logger.info("FEIGN : response for validatePeriod " + response);
				if(response.getStatusCode() == 420) {
					logger.info("Feature [Stock] user [End User]" + GenericMessageTags.FEATURE_NOT_ALLOWED.getMessage());
					return new GenricResponse(420, GenericMessageTags.FEATURE_NOT_ALLOWED.getTag(), 
							GenericMessageTags.FEATURE_NOT_ALLOWED.getMessage(), "");
				}

				if(validateUserProfileOfStock(stockMgmt)) {
					user = User.getDefaultUser();

					UserProfile userProfile = UserProfile.getDefaultUserProfile();
					userProfile.setEmail(stockMgmt.getUser().getUserProfile().getEmail());
					userProfile.setUser(user);
					Usertype usertype = new Usertype();
					usertype.setId(17);

					Userrole roles = new Userrole(user, usertype);
					List<Userrole> userRolesList = new ArrayList<Userrole>();
					userRolesList.add(roles);
					user.setUserRole(userRolesList);
					user.setUserProfile(userProfile);
					user.setUsertype(usertype);

					user = userRepository.save(user);
					logger.info("User [" + user + "] have been saved successfully.");

					stockMgmt.setUserId(new Long(user.getId()));
					stockMgmt.setUser(user);
					stockMgmt.setRoleType("End User");
					isAnonymousUpload = Boolean.TRUE;
				}else {
					logger.info("Invalid request for stock registeration.", stockMgmt.getTxnId());
					return new GenricResponse(3, "Invalid request for stock registeration.", stockMgmt.getTxnId());
				}
				addInAuditTrail(user.getId(), stockMgmt.getTxnId(), SubFeatures.UPLOAD, stockMgmt.getRoleType(),stockMgmt.getPublicIp(),stockMgmt.getBrowser());
			}else {
				stockMgmt.setUser(new User().setId(new Long(stockMgmt.getUserId())));
				addInAuditTrail(stockMgmt.getUserId(), stockMgmt.getTxnId(), SubFeatures.UPLOAD,stockMgmt.getRoleType(),stockMgmt.getPublicIp(),stockMgmt.getBrowser());
			}

			WebActionDb webActionDb = new WebActionDb();
			webActionDb.setFeature(WebActionDbFeature.STOCK.getName());
			//webActionDb.setFeature(stakeholderfeatureServiceImpl.getFeatureNameById(4L));
			webActionDb.setSubFeature(WebActionDbSubFeature.UPLOAD.getName());
			webActionDb.setState(WebActionDbState.INIT.getCode());
			webActionDb.setTxnId(stockMgmt.getTxnId());

			if(isStockAssignRequest) {
				if(stockTransaction.executeRegisterStock(stockMgmt, webActionDb, user.getUserProfile(), isStockAssignRequest, isAnonymousUpload)) {
					logger.info("Stock have been registered Successfully" + stockMgmt.getTxnId());
					return new GenricResponse(0, "Stock have been registered Successfully.", stockMgmt.getTxnId());	
				}else {
					logger.info("Stock registeration have been failed." + stockMgmt.getTxnId());
					return new GenricResponse(1, "Stock registeration have been failed.", stockMgmt.getTxnId());
				}
			}else if(isAnonymousUpload) {
				if(stockTransaction.executeRegisterStock(stockMgmt, webActionDb, user.getUserProfile(), isStockAssignRequest, 
						isAnonymousUpload)) {

					logger.info("Stock have been registered Successfully" + stockMgmt.getTxnId());
					return new GenricResponse(0, "Stock have been registered Successfully.", stockMgmt.getTxnId());	
				}else {
					logger.info("Stock registeration have been failed." + stockMgmt.getTxnId());
					return new GenricResponse(1, "Stock registeration have been failed.", stockMgmt.getTxnId());
				}
			}else {
				if(stockTransaction.executeRegisterStock(stockMgmt, webActionDb)) {

					logger.info("Stock have been registered Successfully" + stockMgmt.getTxnId());
					return new GenricResponse(0, "Stock have been registered Successfully.", stockMgmt.getTxnId());	
				}else {
					logger.info("Stock registeration have been failed." + stockMgmt.getTxnId());
					return new GenricResponse(1, "Stock registeration have been failed.", stockMgmt.getTxnId());
				}
			}
		} catch (RequestInvalidException e) {
			logger.error("Request validation failed for txnId[" + stockMgmt.getTxnId() + "]" + e);
			throw e;
		}catch (Exception e) {
			logger.error(e.getMessage(), e);

			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", featureName);
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.REGISTER);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_011, stockMgmt.getUserId().intValue(), bodyPlaceHolderMap);

			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	public List<StockMgmt> getAllData(StockMgmt stockMgmt){
		try {

			return stockManagementRepository.findByRoleTypeAndUserId(stockMgmt.getInvoiceNumber(), stockMgmt.getUserId());

		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", featureName);
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.VIEW);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_011, 0, bodyPlaceHolderMap);

			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	public Page<StockMgmt> getAllFilteredData(FilterRequest filterRequest, Integer pageNo, Integer pageSize, 
			String source){
		
		List<StateMgmtDb> statusList = null;
		String orderColumn=null;
		if(filterRequest.getUserType().equalsIgnoreCase("CEIRAdmin")) {
		 orderColumn = "0".equalsIgnoreCase(filterRequest.getColumnName()) ? "createdOn"
				: "1".equalsIgnoreCase(filterRequest.getColumnName()) ? "txnId"
					:"2".equalsIgnoreCase(filterRequest.getColumnName()) ? "user.userProfile.displayName"
						: "3".equalsIgnoreCase(filterRequest.getColumnName()) ? "roleType"
								: "4".equalsIgnoreCase(filterRequest.getColumnName()) ? "fileName"
										: "5".equalsIgnoreCase(filterRequest.getColumnName())
												? "stockStatus"
												:"6".equalsIgnoreCase(filterRequest.getColumnName()) ? "quantity" 
												: "7".equalsIgnoreCase(filterRequest.getColumnName()) ? "deviceQuantity":"modifiedOn";
		}
		else if(filterRequest.getUserType().equalsIgnoreCase("Custom")) {
			orderColumn = "0".equalsIgnoreCase(filterRequest.getColumnName()) ? "createdOn"
					: "1".equalsIgnoreCase(filterRequest.getColumnName()) ? "suplierName"
						:"2".equalsIgnoreCase(filterRequest.getColumnName()) ? "txnId"
							: "3".equalsIgnoreCase(filterRequest.getColumnName()) ? "fileName"
									: "4".equalsIgnoreCase(filterRequest.getColumnName()) ? "stockStatus"
											: "5".equalsIgnoreCase(filterRequest.getColumnName())
													? "quantity"
													:"6".equalsIgnoreCase(filterRequest.getColumnName()) ? "deviceQuantity":"modifiedOn"; 
														
		}
		
		else {
			orderColumn = "0".equalsIgnoreCase(filterRequest.getColumnName()) ? "createdOn"
					: "1".equalsIgnoreCase(filterRequest.getColumnName()) ? "txnId"
						:"2".equalsIgnoreCase(filterRequest.getColumnName()) ? "fileName"
							: "3".equalsIgnoreCase(filterRequest.getColumnName()) ? "stockStatus"
									: "4".equalsIgnoreCase(filterRequest.getColumnName()) ? "quantity"
											: "5".equalsIgnoreCase(filterRequest.getColumnName())
													? "deviceQuantity"
													 :"modifiedOn";
		}
		logger.info("column name for sorting==[" + orderColumn + "]" );
		Sort.Direction direction;
		if("modifiedOn".equalsIgnoreCase(orderColumn)) {
			direction=Sort.Direction.DESC;
		}
		else {
			direction= SortDirection.getSortDirection(filterRequest.getSort());
		}
		Pageable pageable = PageRequest.of(pageNo, pageSize, new Sort(direction, orderColumn));
	
		try {
		//	stockValidator.validateFilter(filterRequest);

			if("noti".equalsIgnoreCase(source)) {
				StockMgmt stockMgmtTemp = stockManagementRepository.getByTxnId(filterRequest.getTxnId());
				User user = userRepository.getById(stockMgmtTemp.getUserId());
				Long userTypeId = user.getUsertype().getId();
				logger.info("REQUEST MODIFIED : get usertypeid from Stock, UserTypeId is[" + userTypeId + "]" );
				filterRequest.setUserTypeId(userTypeId.intValue());
			}

			statusList = stateMgmtServiceImpl.getByFeatureIdAndUserTypeId(filterRequest.getFeatureId(), filterRequest.getUserTypeId());
			logger.info(statusList);

			Page<StockMgmt> page = stockManagementRepository.findAll(buildSpecification(filterRequest, statusList, source).build(), pageable);

			for(StockMgmt stockMgmt : page.getContent()) {

				for(StateMgmtDb stateMgmtDb : statusList) {
					if(stockMgmt.getStockStatus() == stateMgmtDb.getState()) {
						stockMgmt.setStateInterp(stateMgmtDb.getInterp()); 
					} 
				}
			}
			if(source.equalsIgnoreCase("menu")) {
			addInAuditTrail(Long.valueOf(filterRequest.getUserId()), "NA", SubFeatures.VIEW_ALL,filterRequest.getRoleType(),filterRequest.getPublicIp(),filterRequest.getBrowser());
			}
			else {
				addInAuditTrail(Long.valueOf(filterRequest.getUserId()), "NA", SubFeatures.FILTER,filterRequest.getRoleType(),filterRequest.getPublicIp(),filterRequest.getBrowser());
			}
			/*
			 * if(Objects.isNull(filterRequest.getTxnId())) {
			 * addInAuditTrail(Long.valueOf(filterRequest.getUserId()), "NA",
			 * SubFeatures.VIEW_ALL,filterRequest.getRoleType(),filterRequest.getPublicIp(),
			 * filterRequest.getBrowser()); }else {
			 * addInAuditTrail(Long.valueOf(filterRequest.getUserId()),
			 * filterRequest.getTxnId(),
			 * SubFeatures.FILTER,filterRequest.getRoleType(),filterRequest.getPublicIp(),
			 * filterRequest.getBrowser()); }
			 */ 

			return page;

		}catch (RequestInvalidException e) {
			logger.error("Request validation failed for txnId[" + filterRequest.getTxnId() + "]" + e);
			
			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", featureName);
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.VIEW_ALL);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_013, filterRequest.getUserId(), bodyPlaceHolderMap);
			
			return new PageImpl<StockMgmt>(new ArrayList<StockMgmt>(1), pageable, 0);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", featureName);
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.VIEW_ALL);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_011, filterRequest.getUserId(), bodyPlaceHolderMap);

			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	public List<StockMgmt> getAll(FilterRequest filterRequest, String source){
		List<StateMgmtDb> statusList = null;

		try {
			statusList = stateMgmtServiceImpl.getByFeatureIdAndUserTypeId(filterRequest.getFeatureId(), filterRequest.getUserTypeId());
			logger.info("statusList " + statusList);
			logger.info("Request to export filtered Stocks = " + filterRequest);
			List<StockMgmt> stockMgmts = stockManagementRepository.findAll(buildSpecification(filterRequest, statusList, source).build(), new Sort(Sort.Direction.DESC, "modifiedOn"));

			logger.info(statusList);

			for(StockMgmt stockMgmt : stockMgmts) {

				for(StateMgmtDb stateMgmtDb : statusList) {
					if(stockMgmt.getStockStatus() == stateMgmtDb.getState()) {
						stockMgmt.setStateInterp(stateMgmtDb.getInterp());
					}
				}
			}

			return stockMgmts;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", featureName);
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.VIEW_ALL);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_011, filterRequest.getUserId(), bodyPlaceHolderMap);

			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	private GenericSpecificationBuilder<StockMgmt> buildSpecification(FilterRequest filterRequest, 
			List<StateMgmtDb> statusList, String source){
		GenericSpecificationBuilder<StockMgmt> specificationBuilder = new GenericSpecificationBuilder<>(propertiesReader.dialect);

		if("Importer".equalsIgnoreCase(filterRequest.getUserType()) || 
				"Distributor".equalsIgnoreCase(filterRequest.getUserType()) || 
				"Retailer".equalsIgnoreCase(filterRequest.getUserType()) || 
				"Manufacturer".equalsIgnoreCase(filterRequest.getUserType())
				) {

			if(Objects.nonNull(filterRequest.getUserId()) )
				specificationBuilder.with(new SearchCriteria("userId", filterRequest.getUserId(), SearchOperation.EQUALITY, Datatype.INT));

			if(Objects.nonNull(filterRequest.getRoleType()))
				specificationBuilder.with(new SearchCriteria("roleType", filterRequest.getRoleType(), SearchOperation.EQUALITY, Datatype.STRING));
		} 
		else if("Custom".equalsIgnoreCase(filterRequest.getUserType())) {
			if(Objects.nonNull(filterRequest.getDisplayName()) && !filterRequest.getDisplayName().isEmpty()) {
				specificationBuilder.with(new SearchCriteria("suplierName", filterRequest.getDisplayName().trim().replace("  ", " ")
						, SearchOperation.LIKE, Datatype.STRING));
			}
		}
		if(Objects.nonNull(filterRequest.getStartDate()) && !filterRequest.getStartDate().isEmpty())
			specificationBuilder.with(new SearchCriteria("createdOn", filterRequest.getStartDate() , SearchOperation.GREATER_THAN, Datatype.DATE));

		if(Objects.nonNull(filterRequest.getEndDate()) && !filterRequest.getEndDate().isEmpty())
			specificationBuilder.with(new SearchCriteria("createdOn", filterRequest.getEndDate() , SearchOperation.LESS_THAN, Datatype.DATE));

		if(Objects.nonNull(filterRequest.getTxnId()) && !filterRequest.getTxnId().isEmpty())
			specificationBuilder.with(new SearchCriteria("txnId", filterRequest.getTxnId(), SearchOperation.LIKE, Datatype.STRING));

		if(Objects.nonNull(filterRequest.getUserType()) && "Custom".equalsIgnoreCase(filterRequest.getUserType())) {
//			logger.info("Inside custom block.");
//			specificationBuilder.with(new SearchCriteria("userType", filterRequest.getUserType(), SearchOperation.EQUALITY, Datatype.STRING));
			UserProfile userProfile = userRepository.getById(filterRequest.getUserId()).getUserProfile();
			if( Objects.nonNull(userProfile))
				specificationBuilder.with(new SearchCriteria("portAddress", userProfile.getPortAddress(), SearchOperation.EQUALITY, Datatype.INT));
		}
		//changes for imei /MEID  quantity done by sharad
		
		if(Objects.nonNull(filterRequest.getQuantity()) && !filterRequest.getQuantity().isEmpty())
			specificationBuilder.with(new SearchCriteria("quantity", filterRequest.getQuantity(), SearchOperation.LIKE, Datatype.STRING));
		
		
		if(Objects.nonNull(filterRequest.getQuantity()) && !filterRequest.getQuantity().isEmpty())
			specificationBuilder.with(new SearchCriteria("quantity", filterRequest.getQuantity(), SearchOperation.LIKE, Datatype.STRING));
		
		
		if(Objects.nonNull(filterRequest.getDeviceQuantity()) && !filterRequest.getDeviceQuantity().isEmpty())
			specificationBuilder.with(new SearchCriteria("deviceQuantity", filterRequest.getDeviceQuantity(), SearchOperation.LIKE, Datatype.STRING));
		
		if(Objects.nonNull(filterRequest.getFileName()) && !filterRequest.getFileName().isEmpty())
			specificationBuilder.with(new SearchCriteria("fileName", filterRequest.getFileName(), SearchOperation.LIKE, Datatype.STRING));

		
		if(Objects.nonNull(filterRequest.getFilteredUserType()) && !filterRequest.getFilteredUserType().isEmpty() 
				&& !filterRequest.getFilteredUserType().equalsIgnoreCase("null")) {
			logger.info("Inside getFilteredUserType block and user type is ["+filterRequest.getFilteredUserType()+"]");
//			specificationBuilder.with(new SearchCriteria("userType", filterRequest.getFilteredUserType(), SearchOperation.EQUALITY, Datatype.STRING));
			specificationBuilder.with(new SearchCriteria("roleType", filterRequest.getFilteredUserType(), SearchOperation.EQUALITY, Datatype.STRING));
		}
		
		if(Objects.nonNull(filterRequest.getDisplayName()) && !filterRequest.getDisplayName().isEmpty()) {
			specificationBuilder.with(new SearchCriteria("user-userProfile-displayName", filterRequest.getDisplayName().trim().replace("  ", " ")
					, SearchOperation.LIKE, Datatype.STRING));
		}

		if(Objects.nonNull(filterRequest.getConsignmentStatus())) {
			specificationBuilder.with(new SearchCriteria("stockStatus", filterRequest.getConsignmentStatus(), SearchOperation.EQUALITY, Datatype.STRING));
		}else {
			if(Objects.nonNull(filterRequest.getFeatureId()) && Objects.nonNull(filterRequest.getUserTypeId())) {

				List<DashboardUsersFeatureStateMap> dashboardUsersFeatureStateMap = dashboardUsersFeatureStateMapRepository.findByUserTypeIdAndFeatureId(filterRequest.getUserTypeId(),
						filterRequest.getFeatureId());
				logger.debug(dashboardUsersFeatureStateMap);

				List<Integer> stockStatus = new LinkedList<>();

				logger.info("source for stock : " + source);
				if(Objects.nonNull(dashboardUsersFeatureStateMap)) {
					if("dashboard".equalsIgnoreCase(source) || "menu".equalsIgnoreCase(source)) {
						for(DashboardUsersFeatureStateMap dashboardUsersFeatureStateMap2 : dashboardUsersFeatureStateMap ) {
							stockStatus.add(dashboardUsersFeatureStateMap2.getState());
						}
					}else if("filter".equalsIgnoreCase(source)) {
						boolean isFilterEmpty = nothingInFilter(filterRequest);
						logger.info("Nothing in filter : " + isFilterEmpty);

						if(isFilterEmpty) {
							for(DashboardUsersFeatureStateMap dashboardUsersFeatureStateMap2 : dashboardUsersFeatureStateMap ) {
								stockStatus.add(dashboardUsersFeatureStateMap2.getState());
							}
						}else {
							for(StateMgmtDb stateMgmtDb : statusList ) {
								stockStatus.add(stateMgmtDb.getState());
							}
						}
					}else if("noti".equalsIgnoreCase(source)) {
						logger.info("Skip status check, because source is noti.");
					}

					logger.info("Array list to add is = " + stockStatus);
					if(!stockStatus.isEmpty()) {
						specificationBuilder.addSpecification(specificationBuilder.in("stockStatus", stockStatus));
					}else {
						logger.warn("no predefined status are available.");
					}
				}
			}
		}


		if(Objects.nonNull(filterRequest.getSearchString()) && !filterRequest.getSearchString().isEmpty()){
			specificationBuilder.orSearch(new SearchCriteria("txnId", filterRequest.getSearchString(), SearchOperation.LIKE, Datatype.STRING));
			specificationBuilder.orSearch(new SearchCriteria("fileName", filterRequest.getSearchString(), SearchOperation.LIKE, Datatype.STRING));
			specificationBuilder.orSearch(new SearchCriteria("supplierId", filterRequest.getSearchString(), SearchOperation.LIKE, Datatype.STRING));
			specificationBuilder.orSearch(new SearchCriteria("suplierName", filterRequest.getSearchString(), SearchOperation.LIKE, Datatype.STRING));
			specificationBuilder.orSearch(new SearchCriteria("invoiceNumber", filterRequest.getSearchString(), SearchOperation.LIKE, Datatype.STRING));
			specificationBuilder.orSearch(new SearchCriteria("quantity", filterRequest.getSearchString(), SearchOperation.LIKE, Datatype.STRING));
			specificationBuilder.orSearch(new SearchCriteria("deviceQuantity", filterRequest.getSearchString(), SearchOperation.LIKE, Datatype.STRING));
			specificationBuilder.orSearch(new SearchCriteria("userType", filterRequest.getFilteredUserType(), SearchOperation.LIKE, Datatype.STRING));
		}

		return specificationBuilder;
	}

	public StockMgmt view(FilterRequest filterRequest) {
		try {
			//stockValidator.validateViewById(filterRequest);
			logger.info("Going to get Stock Record Info for txnId : " + filterRequest.getTxnId());

			if(Objects.isNull(filterRequest.getTxnId())) {
				throw new IllegalArgumentException();
			}

			StockMgmt stockMgmt2 = stockManagementRepository.getByTxnId(filterRequest.getTxnId());
			if( Objects.isNull(stockMgmt2) ) {
				throw new IllegalArgumentException();
			}
			if("End User".equalsIgnoreCase(filterRequest.getUserType())) {
				StatesInterpretationDb statesInterpretationDb = statesInterpretaionRepository.findByFeatureIdAndState(4, stockMgmt2.getStockStatus());
				stockMgmt2.setStateInterp(statesInterpretationDb.getInterp());
			}else {
				User user = userRepository.getById(filterRequest.getUserId());
				logger.info(user);
			}
			addInAuditTrail(Long.valueOf(filterRequest.getUserId()), filterRequest.getTxnId(), SubFeatures.VIEW,filterRequest.getRoleType(),filterRequest.getPublicIp(),filterRequest.getBrowser());
			return stockMgmt2;

		} catch (RequestInvalidException e) {
			logger.error("Request validation failed for txnId[" + filterRequest.getTxnId() + "]" + e);
			
			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", featureName);
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.VIEW);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_013, filterRequest.getUserId(), bodyPlaceHolderMap);
			
			throw e;
		}catch (Exception e) {
			logger.error(e.getMessage(), e);

			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", featureName);
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.VIEW);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_011, filterRequest.getUserId(), bodyPlaceHolderMap);

			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	public GenricResponse deleteStockDetailes(ConsignmentUpdateRequest deleteObj) {
		if( userStaticServiceImpl.checkIfUserIsDisabled( deleteObj.getUserId() ))
			return new GenricResponse(5, "USER_IS_DISABLED","This account is disabled. Please enable the account to perform the operation.",
					deleteObj.getTxnId());
		try {
			//stockValidator.validateDelete(deleteObj);
		}catch (RequestInvalidException e) {
			logger.error("Request validation failed for txnId[" + deleteObj.getTxnId() + "]" + e);
			
			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", featureName);
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.DELETE);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_011, deleteObj.getUserId().intValue(), bodyPlaceHolderMap);
			
			throw e;
		}
		
		User user = null;
		User customUser = null;
		Map<String, String> placeholderMap = new HashMap<>();
		String action = null;
		String txnId = null;
		String userMailTag = null;
		Boolean isUserCeirAdmin = false;
		String receiverUserType = deleteObj.getUserType();
		action = SubFeatures.DELETE;
		userMailTag = "STOCK_DELETE_BY_USER"; 
		user = userRepository.getById(deleteObj.getUserId());				
		txnId = deleteObj.getTxnId();

		placeholderMap.put("<Txn id>", deleteObj.getTxnId());
		try {

			if(Objects.isNull(deleteObj.getTxnId())) {
				logger.info("TxnId is null in the request.");
				return new GenricResponse(1001, "TxnId is null in the request.", deleteObj.getTxnId());
			}

			if(Objects.isNull(deleteObj.getUserType())) {
				logger.info("userType is null in the request.");
				return new GenricResponse(1002, "Usertype is null in the request.", deleteObj.getTxnId());
			}

			StockMgmt txnRecord	= stockManagementRepository.getByTxnId(deleteObj.getTxnId());
			
			if("Custom".equals(txnRecord.getUserType())) {
				customUser = userRepository.getById(txnRecord.getAssignerId());				
			}
			user = userRepository.getById(txnRecord.getUserId());

			String payloadTxnId = deleteObj.getTxnId();
			lock.lock();
			logger.info("lock taken by thread for [Delete] - " + Thread.currentThread().getName());

			if(Objects.isNull(txnRecord)) {
				return new GenricResponse(1000, "No record found against this transactionId.", deleteObj.getTxnId());
			}else {

				if("CEIRADMIN".equalsIgnoreCase(deleteObj.getUserType())) {
					// Check if someone else taken the same action on consignment.
					StockMgmt stockMgmtTemp = stockManagementRepository.getByTxnId(payloadTxnId);
					if(StockStatus.WITHDRAWN_BY_CEIR_ADMIN.getCode() == stockMgmtTemp.getStockStatus()) {
						String message = "Any other user have taken the same action on the stock [" + payloadTxnId + "]";
						logger.info(message);
						return new GenricResponse(10, "", message, payloadTxnId);
					}

					txnRecord.setStockStatus(StockStatus.WITHDRAWN_BY_CEIR_ADMIN.getCode());
					isUserCeirAdmin = Boolean.TRUE;
				}
				else {
					// Check if someone else taken the same action on consignment.
					StockMgmt stockMgmtTemp = stockManagementRepository.getByTxnId(payloadTxnId);
					if(StockStatus.WITHDRAWN_BY_USER.getCode() == stockMgmtTemp.getStockStatus()) {
						String message = "Any other user have taken the same action on the stock [" + payloadTxnId + "]";
						logger.info(message);
						return new GenricResponse(10, "", message, payloadTxnId);
					}

					txnRecord.setStockStatus(StockStatus.WITHDRAWN_BY_USER.getCode());
				}
				txnRecord.setRemarks(deleteObj.getRemarks());
				txnRecord.setDeleteFlag(0);

				WebActionDb webActionDb = new WebActionDb();
				webActionDb.setFeature(WebActionDbFeature.STOCK.getName());
				// webActionDb.setFeature(stakeholderfeatureServiceImpl.getFeatureNameById(4L));
				webActionDb.setSubFeature(WebActionDbSubFeature.DELETE.getName());
				webActionDb.setState(WebActionDbState.INIT.getCode());
				webActionDb.setTxnId(deleteObj.getTxnId());

				addInAuditTrail(Long.valueOf(deleteObj.getUserId()), deleteObj.getTxnId(), SubFeatures.DELETE,deleteObj.getRoleType(),deleteObj.getPublicIp(),deleteObj.getBrowser());
				if(stockTransaction.executeDeleteStock(txnRecord, webActionDb)) {
					logger.info("Deletion of Stock is in Progress." + deleteObj.getTxnId());
					if(isUserCeirAdmin) {
						userMailTag = "STOCK_DELETE_BY_CEIR_ADMIN";
						Generic_Response_Notification generic_Response_Notification = userFeignClient.ceirInfoByUserTypeId(8);

						logger.info("generic_Response_Notification::::::::"+generic_Response_Notification);

						List<RegisterationUser> registerationUserList = generic_Response_Notification.getData();

						for(RegisterationUser registerationUser :registerationUserList) {
							UserProfile userProfile_generic_Response_Notification = new UserProfile();
							userProfile_generic_Response_Notification = userProfileRepository.getByUserId(registerationUser.getId());

							placeholderMap.put("<First name>", userProfile_generic_Response_Notification.getFirstName());
							emailUtil.saveNotification(userMailTag, 
									userProfile_generic_Response_Notification,
									deleteObj.getFeatureId(),
									Features.STOCK,
									action,
									deleteObj.getTxnId(),
									txnId,
									placeholderMap,
									deleteObj.getRoleType(),
									"CEIRAdmin",
									"Users");
							logger.info("Notfication for CEIRAdmin have been saved.");
						}
						if( customUser != null ) {
							placeholderMap.put("<First name>", customUser.getUserProfile().getFirstName());
							emailUtil.saveNotification(userMailTag, 
									customUser.getUserProfile(), 
									deleteObj.getFeatureId(),
									Features.STOCK,
									action,
									deleteObj.getTxnId(),
									txnId,
									placeholderMap,
									deleteObj.getUserType(),
									deleteObj.getUserType(),
									"Users");
							logger.info("Notfication for custom user have been saved.");
						}
						placeholderMap.put("<First name>", user.getUserProfile().getFirstName());
						emailUtil.saveNotification(userMailTag, 
								user.getUserProfile(), 
								deleteObj.getFeatureId(),
								Features.STOCK,
								action,
								deleteObj.getTxnId(),
								txnId,
								placeholderMap,
								deleteObj.getRoleType(),
								deleteObj.getRoleType(),
								"Users");
						logger.info("Notfication for user have been saved.");
					}
					else {
						if( customUser != null ) {
							placeholderMap.put("<First name>", customUser.getUserProfile().getFirstName());
							emailUtil.saveNotification(userMailTag, 
									customUser.getUserProfile(), 
									deleteObj.getFeatureId(),
									Features.STOCK,
									action,
									deleteObj.getTxnId(),
									txnId,
									placeholderMap,
									deleteObj.getUserType(),
									deleteObj.getUserType(),
									"Users");
							logger.info("Notfication for custom user have been saved.");
						}
						placeholderMap.put("<First name>", user.getUserProfile().getFirstName());
						emailUtil.saveNotification(userMailTag, 
								user.getUserProfile(), 
								deleteObj.getFeatureId(),
								Features.STOCK,
								action,
								deleteObj.getTxnId(),
								txnId,
								placeholderMap,
								deleteObj.getRoleType(),
								deleteObj.getUserType(),
								"Users");
						logger.info("Notfication have been saved.");	
					}

					return new GenricResponse(0, "Deletion of Stock is in Progress.",deleteObj.getTxnId());
				}else {
					logger.info("Deletion of Stock have been failed." + deleteObj.getTxnId());
					return new GenricResponse(1, "Deletion of Stock have been failed.",deleteObj.getTxnId());
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", featureName);
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.DELETE);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_011, 0, bodyPlaceHolderMap);

			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}finally {
			if(lock.isLocked()) {
				logger.info("lock released by thread [Delete] - " + Thread.currentThread().getName());
				lock.unlock();
			}
		}
	}

	public GenricResponse updateStockInfo(StockMgmt distributerManagement) {
		StockMgmt stockMgmt = null;

		try {
			if( userStaticServiceImpl.checkIfUserIsDisabled( distributerManagement.getUserId() ))
				return new GenricResponse(5, "USER_IS_DISABLED","This account is disabled. Please enable the account to perform the operation.",
						distributerManagement.getTxnId());
			//stockValidator.validateEdit(distributerManagement);
			
			if("End User".equalsIgnoreCase(distributerManagement.getUserType())){
				distributerManagement.setRoleType("End User");
			}
			if( distributerManagement.getRoleType().equalsIgnoreCase("custom") )
				stockMgmt = stockManagementRepository.findByUserTypeAndTxnId(distributerManagement.getRoleType(), 
						distributerManagement.getTxnId());
			else
				stockMgmt = stockManagementRepository.findByRoleTypeAndTxnId(distributerManagement.getRoleType(), 
						distributerManagement.getTxnId());
			logger.info(stockMgmt);

			if(Objects.isNull(stockMgmt)) {
				return new GenricResponse(1000, "No record found against this transactionId.", distributerManagement.getTxnId());

			}else {

				stockMgmt.setInvoiceNumber(distributerManagement.getInvoiceNumber());
				stockMgmt.setQuantity(distributerManagement.getQuantity());
				stockMgmt.setSuplierName(distributerManagement.getSuplierName());
				stockMgmt.setSupplierId(distributerManagement.getSupplierId());
				stockMgmt.setTotalPrice(distributerManagement.getTotalPrice());
				stockMgmt.setDeviceQuantity(distributerManagement.getDeviceQuantity());
				stockMgmt.setRemarks(null);
				if(Objects.nonNull(distributerManagement.getFileName()) && !distributerManagement.getFileName().isEmpty()) {
					stockMgmt.setStockStatus(StockStatus.NEW.getCode());
					stockMgmt.setFileName(distributerManagement.getFileName());
				}

				WebActionDb webActionDb = new WebActionDb();
				webActionDb.setFeature(WebActionDbFeature.STOCK.getName());
				webActionDb.setSubFeature(WebActionDbSubFeature.UPDATE.getName());
				webActionDb.setState(WebActionDbState.INIT.getCode());
				webActionDb.setTxnId(distributerManagement.getTxnId());

				addInAuditTrail(stockMgmt.getUserId(), stockMgmt.getTxnId(), SubFeatures.UPDATE, stockMgmt.getRoleType(),stockMgmt.getPublicIp(),stockMgmt.getBrowser());

				if(stockTransaction.executeUpdateStock(stockMgmt, webActionDb)) {
					logger.info("Stock Update have been Successful." + stockMgmt.getTxnId());
					return new GenricResponse(0, "Stock Update have been Successful.", distributerManagement.getTxnId());
				}else {
					logger.info("Stock Update have been failed." + stockMgmt.getTxnId());
					return new GenricResponse(1, "Stock Update have been failed.",stockMgmt.getTxnId());
				}
			}
		}catch (RequestInvalidException e) {
			logger.error("Request validation failed for txnId[" + distributerManagement.getTxnId() + "]" + e);
			
			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", featureName);
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.UPDATE);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_013, 0, bodyPlaceHolderMap);
			
			throw e;
		}catch (Exception e) {
			logger.error(e.getMessage(), e);

			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", featureName);
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.UPDATE);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_011, 0, bodyPlaceHolderMap);

			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	public FileDetails getFilteredStockInFile(FilterRequest filterRequest, String source) {
		try {
			//stockValidator.validateFilter(filterRequest);
			
			DateTimeFormatter dtf  = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			DateTimeFormatter dtf2  = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

			SystemConfigurationDb filepath = configurationManagementServiceImpl.findByTag(ConfigTags.file_download_dir);
			logger.info("CONFIG : file_consignment_download_dir [" + filepath + "]");
			SystemConfigurationDb link = configurationManagementServiceImpl.findByTag(ConfigTags.file_download_link);
			logger.info("CONFIG : file_consignment_download_link [" + link + "]");

			String filePath = filepath.getValue();
			logger.info("Request to export filtered Stocks = " + filterRequest);
			return exportFileFactory
					.getObject(filterRequest.getUserType().toUpperCase(), 4)
					.export(filterRequest, source, dtf, dtf2, filePath, link);

		}catch (RequestInvalidException e) {
			logger.error("Request validation failed for txnId[" + filterRequest.getTxnId() + "]" + e);
			
			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", featureName);
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.EXPORT);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_013, 0, bodyPlaceHolderMap);
			
			throw e;
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	public GenricResponse acceptReject(ConsignmentUpdateRequest consignmentUpdateRequest) {
		try {
			//stockValidator.validateAcceptReject(consignmentUpdateRequest);
			if(!"CEIRSYSTEM".equalsIgnoreCase(consignmentUpdateRequest.getRoleType()) 
					&& userStaticServiceImpl.checkIfUserIsDisabled( consignmentUpdateRequest.getUserId() )) {
				return new GenricResponse(5, "USER_IS_DISABLED","This account is disabled. Please enable the account to perform the operation.",
						consignmentUpdateRequest.getTxnId());
				
			}
			User user = null;
			User customUser = null;
			Map<String, String> placeholderMap = new HashMap<String, String>();
			Integer currentStatus = null;
			String adminMailTag   = null;
			WebActionDb webActionDb = null;
			adminMailTag = "STOCK_PROCESS_SUCCESS_TO_CEIR_MAIL";
			String txnId = consignmentUpdateRequest.getTxnId();
			logger.info("enter in accept reject method..");
			StockMgmt stockMgmt = stockManagementRepository.getByTxnId(consignmentUpdateRequest.getTxnId());
			currentStatus = stockMgmt.getStockStatus();

			// Fetch user_profile to update user over mail/sms regarding the action.
			if("Custom".equals(stockMgmt.getUserType())) {
				customUser = userRepository.getById(stockMgmt.getAssignerId());				
			}
			user = userRepository.getById(stockMgmt.getUserId());
			placeholderMap.put("<Txn id>", stockMgmt.getTxnId());
			
			if(Objects.isNull(stockMgmt)) {
				String message = "TxnId Does not Exist ";
				logger.info(message + consignmentUpdateRequest.getTxnId());
				return new GenricResponse(4, message, consignmentUpdateRequest.getTxnId());
			}

//			logger.info(user);
//
//			if(Objects.nonNull(user)) {
//				firstName = user.getUserProfile().getFirstName();
//			}

			lock.lock();
			logger.info("lock taken by thread : " + Thread.currentThread().getName());
			// 0 - Accept, 1 - Reject
			if("CEIRADMIN".equalsIgnoreCase(consignmentUpdateRequest.getRoleType())){
				String mailTag = null;
				String action = null;
				String receiverUserType = stockMgmt.getUserType();

				logger.info("enter in CEIR Admin block.........");


				if(consignmentUpdateRequest.getAction() == 0) {
					// Check if someone else taken the same action on consignment.
					StockMgmt stockMgmtTemp = stockManagementRepository.getByTxnId(txnId);
					if(StockStatus.APPROVED_BY_CEIR_ADMIN.getCode() == stockMgmtTemp.getStockStatus()) {
						String message = "Any other user have taken the same action on the stock [" + txnId + "]";
						logger.info(message);
						return new GenricResponse(10, "", message, txnId);
					}

					action = SubFeatures.ACCEPT;
					mailTag = "STOCK_APPROVED_BY_CEIR_ADMIN"; 

					txnId = stockMgmt.getTxnId();

//					placeholderMap.put("<First name>", firstName);
					placeholderMap.put("<Txn id>", stockMgmt.getTxnId());

					stockMgmt.setStockStatus(StockStatus.APPROVED_BY_CEIR_ADMIN.getCode());
				}else {
					// Check if someone else taken the same action on consignment.
					StockMgmt stockMgmtTemp = stockManagementRepository.getByTxnId(txnId);
					if(StockStatus.REJECTED_BY_CEIR_ADMIN.getCode() == stockMgmtTemp.getStockStatus()) {
						String message = "Any other user have taken the same action on the stock [" + txnId + "]";
						logger.info(message);
						return new GenricResponse(10, "", message, txnId);
					}

					action = SubFeatures.REJECT;
					mailTag = "STOCK_REJECT_BY_CEIR_ADMIN";
					txnId =  stockMgmt.getTxnId();

					stockMgmt.setStockStatus(StockStatus.REJECTED_BY_CEIR_ADMIN.getCode());
					stockMgmt.setRemarks(consignmentUpdateRequest.getRemarks());
					stockMgmt.setCeirAdminId(consignmentUpdateRequest.getUserId());
					webActionDb = new WebActionDb();
					webActionDb.setFeature(WebActionDbFeature.STOCK.getName());
					webActionDb.setState(WebActionDbState.INIT.getCode());
					webActionDb.setTxnId(stockMgmt.getTxnId());
					webActionDb.setSubFeature(WebActionDbSubFeature.REJECT.getName());
				}

				// Update Stock and its history.
				if(!stockTransaction.updateStatusWithHistory(stockMgmt, webActionDb)){
					logger.warn("Unable to update Stolen and recovery entity.");
					return new GenricResponse(3, "Unable to update stock entity.", consignmentUpdateRequest.getTxnId()); 
				}else {
					addInAuditTrail(Long.valueOf(consignmentUpdateRequest.getUserId()), consignmentUpdateRequest.getTxnId(), action, consignmentUpdateRequest.getRoleType(),consignmentUpdateRequest.getPublicIp(),consignmentUpdateRequest.getBrowser());
					placeholderMap.put("<Reason>", consignmentUpdateRequest.getRemarks() );
					if( customUser != null ) {
						placeholderMap.put("<First name>", customUser.getUserProfile().getFirstName() );
						emailUtil.saveNotification(mailTag, 
								customUser.getUserProfile(), 
								consignmentUpdateRequest.getFeatureId(),
								Features.STOCK,
								action,
								consignmentUpdateRequest.getTxnId(),
								txnId,
								placeholderMap,
								receiverUserType,
								receiverUserType,
								"Users");
						logger.info("Notfication have been saved for custom user.");
					}
					if( Objects.nonNull(user.getUserProfile().getFirstName()))
						placeholderMap.put("<First name>", user.getUserProfile().getFirstName() );
					else
						placeholderMap.put("<First name>", "User" );
					emailUtil.saveNotification(mailTag, 
							user.getUserProfile(), 
							consignmentUpdateRequest.getFeatureId(),
							Features.STOCK,
							action,
							consignmentUpdateRequest.getTxnId(),
							txnId,
							placeholderMap,
							stockMgmt.getRoleType(),
							stockMgmt.getUserType(),
							"Users");
					logger.info("Notfication have been saved for user.");
					if((consignmentUpdateRequest.getAction() == 0) || ( customUser != null )) {
						Generic_Response_Notification generic_Response_Notification =
								userFeignClient.ceirInfoByUserTypeId(8);

						logger.info("generic_Response_Notification::::::::"+
								generic_Response_Notification);

						List<RegisterationUser> registerationUserList =
								generic_Response_Notification.getData();

						for(RegisterationUser registerationUser :registerationUserList) { 
							UserProfile userProfile_generic_Response_Notification = new UserProfile();
							userProfile_generic_Response_Notification = userProfileRepository.getByUserId(registerationUser.getId());
							placeholderMap.put("<First name>", userProfile_generic_Response_Notification.getFirstName() );
							emailUtil.saveNotification(mailTag,
									userProfile_generic_Response_Notification,
									consignmentUpdateRequest.getFeatureId(), Features.STOCK, action,
									consignmentUpdateRequest.getTxnId(), txnId, placeholderMap,
									"CEIRAdmin", "CEIRAdmin", "Users");
						logger.info("Notfication have been saved for CEIR Admin."); 
						}	
					}
				}

			}else if("CEIRSYSTEM".equalsIgnoreCase(consignmentUpdateRequest.getRoleType())){
				String mailTag = null;

				String action = null;
				String receiverUserType = stockMgmt.getUserType();
				logger.info("enter in CEIR system.......");

				if(!StateMachine.isStockStatetransitionAllowed("CEIRSYSTEM", stockMgmt.getStockStatus())) {
					logger.info("state transition is not allowed." + consignmentUpdateRequest.getTxnId());
					return new GenricResponse(3, "state transition is not allowed.", consignmentUpdateRequest.getTxnId());
				}

				if(consignmentUpdateRequest.getAction() == 0) {
					// Check if someone else taken the same action on consignment.
					StockMgmt stockMgmtTemp = stockManagementRepository.getByTxnId(txnId);
					if(StockStatus.PROCESSING.getCode() == stockMgmtTemp.getStockStatus()) {
						String message = "Any other user have taken the same action on the stock [" + txnId + "]";
						logger.info(message);
						return new GenricResponse(10, "", message, txnId);
					}

					action = SubFeatures.ACCEPT;
					mailTag = "STOCK_PROCESS_SUCCESS_TO_USER_MAIL"; 
					adminMailTag = "STOCK_PROCESS_SUCCESS_TO_CEIR_MAIL";
					txnId = stockMgmt.getTxnId();
					stockMgmt.setStockStatus(StockStatus.PROCESSING.getCode());

				}else if(consignmentUpdateRequest.getAction() == 1){
					// Check if someone else taken the same action on consignment.
					StockMgmt stockMgmtTemp = stockManagementRepository.getByTxnId(txnId);
					if(StockStatus.REJECTED_BY_SYSTEM.getCode() == stockMgmtTemp.getStockStatus()) {
						String message = "Any other user have taken the same action on the stock [" + txnId + "]";
						logger.info(message);
						return new GenricResponse(10, "", message, txnId);
					}

					action = SubFeatures.REJECT;
					mailTag = "STOCK_PROCESS_FAILED_TO_USER_MAIL";
					txnId = stockMgmt.getTxnId();

					stockMgmt.setStockStatus(StockStatus.REJECTED_BY_SYSTEM.getCode());
				}else if(consignmentUpdateRequest.getAction() == 2) {
					// Check if someone else taken the same action on consignment.
					StockMgmt stockMgmtTemp = stockManagementRepository.getByTxnId(txnId);
					if(StockStatus.PENDING_APPROVAL_FROM_CEIR_ADMIN.getCode() == stockMgmtTemp.getStockStatus()) {
						String message = "Any other user have taken the same action on the stock [" + txnId + "]";
						logger.info(message);
						return new GenricResponse(10, "", message, txnId);
					}

					action = SubFeatures.ACCEPT;
					mailTag = "STOCK_PROCESS_SUCCESS_TO_USER_MAIL"; 
					adminMailTag = "STOCK_PROCESS_SUCCESS_TO_CEIR_MAIL";
					txnId = stockMgmt.getTxnId();
					stockMgmt.setStockStatus(StockStatus.PENDING_APPROVAL_FROM_CEIR_ADMIN.getCode());
				}else {
					String message = "Not a valid action to take on the Stock [" + stockMgmt.getTxnId() + "]";
					logger.info(message);
					return new GenricResponse(11, "", message, stockMgmt.getTxnId());
				}

				// Update Stock and its history.
				if(!stockTransaction.updateStatusWithHistory(stockMgmt, webActionDb )){
					logger.warn("Unable to update Stolen and recovery entity.");
					return new GenricResponse(3, "Unable to update stock entity.", consignmentUpdateRequest.getTxnId()); 
				}else {
					logger.info("enter in mail sending   method..");
					if(currentStatus == StockStatus.PROCESSING.getCode()) {
						if( customUser != null ) {
							placeholderMap.put("<First name>", customUser.getUserProfile().getFirstName() );
							emailUtil.saveNotification(mailTag, 
									customUser.getUserProfile(), 
									consignmentUpdateRequest.getFeatureId(),
									Features.STOCK,
									action,
									consignmentUpdateRequest.getTxnId(),
									txnId,
									placeholderMap,
									receiverUserType,
									receiverUserType,
									"Users");
							logger.info("Notfication have been saved for custom user.");
						}
						if( Objects.nonNull(user.getUserProfile().getFirstName()))
							placeholderMap.put("<First name>", user.getUserProfile().getFirstName() );
						else
							placeholderMap.put("<First name>", "User" );
						emailUtil.saveNotification(mailTag, 
								user.getUserProfile(), 
								consignmentUpdateRequest.getFeatureId(),
								Features.STOCK,
								action,
								consignmentUpdateRequest.getTxnId(),
								txnId,
								placeholderMap,
								stockMgmt.getRoleType(),
								stockMgmt.getUserType(),
								"Users");

						logger.info("Notfication have been saved for user.");

						if(consignmentUpdateRequest.getAction() == 2) {
							Generic_Response_Notification generic_Response_Notification = userFeignClient.ceirInfoByUserTypeId(8);

							logger.info("generic_Response_Notification::::::::" + generic_Response_Notification);

							List<RegisterationUser> registerationUserList = generic_Response_Notification.getData();

							for(RegisterationUser registerationUser :registerationUserList) {
								UserProfile userProfile_generic_Response_Notification = new UserProfile();
								userProfile_generic_Response_Notification = userProfileRepository.getByUserId(registerationUser.getId());
								placeholderMap.put("<First name>", userProfile_generic_Response_Notification.getFirstName() );
								emailUtil.saveNotification(adminMailTag, 
										userProfile_generic_Response_Notification,
										consignmentUpdateRequest.getFeatureId(),
										Features.STOCK,
										action,
										consignmentUpdateRequest.getTxnId(),
										txnId,
										placeholderMap,
										stockMgmt.getRoleType(),
										"CEIRAdmin",
										"Users");
								logger.info("Notfication have been saved for CEIR Admin.");
							}
						}
					}

					logger.debug(consignmentUpdateRequest);
				}

			}else {
				logger.warn("Accept/reject of Stock not allowed to you.");

				Map<String, String> bodyPlaceHolderMap = new HashMap<>();
				bodyPlaceHolderMap.put("<feature>", featureName);
				bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.ACCEPT_REJECT);
				alertServiceImpl.raiseAnAlert(Alerts.ALERT_011, 0, bodyPlaceHolderMap);

				new GenricResponse(1, "Operation not Allowed", consignmentUpdateRequest.getTxnId());
			}

			return new GenricResponse(0, "Consignment Update SuccessFully.", consignmentUpdateRequest.getTxnId());

		} catch (RequestInvalidException e) {
			logger.error("Request validation failed for txnId[" + consignmentUpdateRequest.getTxnId() + "]" + e);
			
			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", featureName);
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.ACCEPT_REJECT);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_013, 0, bodyPlaceHolderMap);
			
			throw e;
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}finally {
			if(lock.isLocked()) {
				logger.info("lock released by thread : " + Thread.currentThread().getName());
				lock.unlock();
			}
		}
	}

	private boolean validateUserProfileOfStock(StockMgmt stockMgmt) {
		if(Objects.nonNull(stockMgmt.getUser())) {
			if(Objects.nonNull(stockMgmt.getUser().getUserProfile())) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	private String getSecondaryRoleType(List<String> userRoles) {
		if(userRoles.contains(com.gl.ceir.config.model.constants.Usertype.DISTRIBUTOR.getName()))
			return com.gl.ceir.config.model.constants.Usertype.DISTRIBUTOR.getName();
		else if (userRoles.contains(com.gl.ceir.config.model.constants.Usertype.RETAILER.getName()))
			return com.gl.ceir.config.model.constants.Usertype.RETAILER.getName();
		else
			return null;
	}

	public void addInAuditTrail(Long userId, String txnId, String subFeatureName, String roleType,String publicIp,String browser) {

		User requestUser = null;
		try {
			requestUser = userRepository.getById(userId);
			if(requestUser.getUsertype().getId() == 17) {
				requestUser.getUsertype().setUsertypeName("End User");
			}
			logger.info("User Details " + requestUser);
		} catch (Exception e) {
			logger.error("Error while fetching user information for user id = " + userId);
		}
		
		if(Objects.nonNull(requestUser)) {
			logger.info("Inserting in audit table for feature = " + Features.STOCK + "and Subfeature" + subFeatureName);
			auditTrailRepository.save(new AuditTrail(
					requestUser.getId(),
					requestUser.getUsername(), 
					requestUser.getUsertype().getId(),
					requestUser.getUsertype().getUsertypeName(),
					4,
					Features.STOCK,
					subFeatureName,
					"", 
					txnId,
					roleType,publicIp,browser));
		}else {
			logger.error("Could not find the user information");
		}
	}

	public boolean nothingInFilter(FilterRequest filterRequest) {

		// Clean
		if(filterRequest.getStartDate().isEmpty()) {
			filterRequest.setStartDate(null);
		}

		if(filterRequest.getEndDate().isEmpty()) {
			filterRequest.setEndDate(null);
		}

		if(filterRequest.getTxnId().isEmpty()) {
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

		if(Objects.nonNull(filterRequest.getConsignmentStatus())) {
			return Boolean.FALSE;
		}

		return Boolean.TRUE;
	}

	@PostConstruct
	public void setFeatureName() {
		featureName = stakeholderfeatureServiceImpl.getFeatureNameById(4L);
	}
}