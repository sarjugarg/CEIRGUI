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
//import java.util.concurrent.locks.ReentrantLock;

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
import com.gl.ceir.config.feign.UserFeignClient;
import com.gl.ceir.config.genericresponse.DataClass;
import com.gl.ceir.config.genericresponse.GenricResponse_Class;
import com.gl.ceir.config.model.AuditTrail;
import com.gl.ceir.config.model.ConsignmentMgmt;
import com.gl.ceir.config.model.ConsignmentUpdateRequest;
import com.gl.ceir.config.model.DashboardUsersFeatureStateMap;
import com.gl.ceir.config.model.FileDetails;
import com.gl.ceir.config.model.FilterRequest;
import com.gl.ceir.config.model.GenricResponse;
import com.gl.ceir.config.model.RawMail;
import com.gl.ceir.config.model.ResponseCountAndQuantity;
import com.gl.ceir.config.model.SearchCriteria;
import com.gl.ceir.config.model.StateMgmtDb;
import com.gl.ceir.config.model.SystemConfigurationDb;
import com.gl.ceir.config.model.User;
import com.gl.ceir.config.model.UserProfile;
import com.gl.ceir.config.model.WebActionDb;
import com.gl.ceir.config.model.constants.Alerts;
import com.gl.ceir.config.model.constants.ConsignmentStatus;
import com.gl.ceir.config.model.constants.Datatype;
import com.gl.ceir.config.model.constants.Features;
import com.gl.ceir.config.model.constants.GenericMessageTags;
import com.gl.ceir.config.model.constants.ReferTable;
import com.gl.ceir.config.model.constants.SearchOperation;
import com.gl.ceir.config.model.constants.SubFeatures;
import com.gl.ceir.config.model.constants.Tags;
import com.gl.ceir.config.model.constants.TaxStatus;
import com.gl.ceir.config.model.constants.WebActionDbFeature;
import com.gl.ceir.config.model.constants.WebActionDbState;
import com.gl.ceir.config.model.constants.WebActionDbSubFeature;
import com.gl.ceir.config.model.file.ConsignmentFileModel;
import com.gl.ceir.config.model.file.ConsignmentFileModelCEIR;
import com.gl.ceir.config.repository.AuditTrailRepository;
import com.gl.ceir.config.repository.ConsignmentRepository;
import com.gl.ceir.config.repository.DashboardUsersFeatureStateMapRepository;
import com.gl.ceir.config.repository.MessageConfigurationDbRepository;
import com.gl.ceir.config.repository.SystemConfigurationDbRepository;
import com.gl.ceir.config.repository.UserProfileRepository;
import com.gl.ceir.config.repository.UserRepository;
import com.gl.ceir.config.repository.WebActionDbRepository;
import com.gl.ceir.config.request.model.Generic_Response_Notification;
import com.gl.ceir.config.request.model.Port;
import com.gl.ceir.config.request.model.RegisterationUser;
import com.gl.ceir.config.service.businesslogic.StateMachine;
import com.gl.ceir.config.specificationsbuilder.GenericSpecificationBuilder;
import com.gl.ceir.config.transaction.ConsignmentTransaction;
import com.gl.ceir.config.util.CustomMappingStrategy;
import com.gl.ceir.config.util.InterpSetter;
import com.gl.ceir.config.util.Utility;
import com.gl.ceir.config.validate.impl.ConsigmentValidator;
import com.google.gson.Gson;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

@Service
public class ConsignmentServiceImpl {

	private static final Logger logger = LogManager.getLogger(ConsignmentServiceImpl.class);

	private final String CEIRSYSTEM = "CEIRSYSTEM";

	// private ReentrantLock lock = new ReentrantLock();

	// This is set with @postconstruct
	private String featureName;

	@Autowired
	private ConsignmentRepository consignmentRepository;

	@Autowired
	WebActionDbRepository webActionDbRepository;

	@Autowired
	DashboardUsersFeatureStateMapRepository dashboardUsersFeatureStateMapRepository;

	@Autowired
	AuditTrailRepository auditTrailRepository;

	@Autowired
	PropertiesReader propertiesReader;

	@Autowired
	Utility utility;

	@Autowired
	EmailUtil emailUtil;

	@Autowired
	UserProfileRepository userProfileRepository;

	@Autowired
	MessageConfigurationDbRepository messageConfigurationDbRepository;

	@Autowired
	StateMgmtServiceImpl stateMgmtServiceImpl;

	@Autowired
	ConfigurationManagementServiceImpl configurationManagementServiceImpl;

	@Autowired
	InterpSetter interpSetter;

	@Autowired
	SystemConfigurationDbRepository systemConfigurationDbRepository;

	@Autowired
	PendingTacApprovedImpl pendingTacApprovedImpl;

	@Autowired
	UserStaticServiceImpl userStaticServiceImpl;

	@Autowired
	ConsignmentTransaction consignmentTransaction;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserFeignClient userFeignClient;

	@Autowired
	StakeholderfeatureServiceImpl stakeholderfeatureServiceImpl;

	@Autowired
	AlertServiceImpl alertServiceImpl;

	@Autowired
	ConsigmentValidator consigmentValidator;

	public GenricResponse registerConsignment(ConsignmentMgmt consignmentFileRequest) {

		try {
			if (userStaticServiceImpl.checkIfUserIsDisabled(consignmentFileRequest.getUserId()))
				return new GenricResponse(5, "USER_IS_DISABLED",
						"This account is disabled. Please enable the account to perform the operation.",
						consignmentFileRequest.getTxnId());
			consigmentValidator.validateRegister(consignmentFileRequest);
			Long importerId = Long.valueOf(consignmentFileRequest.getUserId());

			WebActionDb webActionDb = new WebActionDb();
			webActionDb.setFeature(WebActionDbFeature.CONSIGNMENT.getName());
			webActionDb.setSubFeature(WebActionDbSubFeature.CONSIGNMENT_REGISTER.getName());
			webActionDb.setState(WebActionDbState.INIT.getCode());
			webActionDb.setTxnId(consignmentFileRequest.getTxnId());

			consignmentFileRequest.setConsignmentStatus(ConsignmentStatus.INIT.getCode());
			consignmentFileRequest.setTaxPaidStatus(TaxStatus.TAX_NOT_PAID.getCode());

			// Set user for mapping.
			consignmentFileRequest.setUser(new User().setId(importerId));

			if (consignmentTransaction.executeRegisterConsignment(consignmentFileRequest, webActionDb)) {
				return new GenricResponse(0, "Register Successfully", consignmentFileRequest.getTxnId());
			} else {
				return new GenricResponse(1, "Consignment Registeration failed.", consignmentFileRequest.getTxnId());
			}

		} catch (RequestInvalidException e) {
			logger.error("Request validation failed for txnId[" + consignmentFileRequest.getTxnId() + "]" + e);

			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", featureName);
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.REGISTER);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_013, consignmentFileRequest.getUserId(), bodyPlaceHolderMap);

			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", featureName);
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.REGISTER);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_011, consignmentFileRequest.getUserId(), bodyPlaceHolderMap);

			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	public List<ConsignmentMgmt> getAll(Long importerId) {
		try {
			logger.info("Going to get All Cosignment List ");
			return consignmentRepository.getByUserIdOrderByIdDesc(importerId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", featureName);
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.VIEW_ALL);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_011, 0, bodyPlaceHolderMap);

			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	public List<ConsignmentMgmt> getFilterConsignments(FilterRequest consignmentMgmt, Integer pageNo,
			Integer pageSize) {
		List<StateMgmtDb> statusList = null;
		try {
			Pageable pageable = PageRequest.of(pageNo, pageSize);
			statusList = stateMgmtServiceImpl.getByFeatureIdAndUserTypeId(consignmentMgmt.getFeatureId(),
					consignmentMgmt.getUserTypeId());
			System.out.println("dialect : " + propertiesReader.dialect);

			return consignmentRepository
					.findAll(buildSpecification(consignmentMgmt, statusList, null).build(), pageable).getContent();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", featureName);
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.VIEW_ALL);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_011, consignmentMgmt.getUserId(), bodyPlaceHolderMap);

			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}

	}

	private List<ConsignmentMgmt> getAll(FilterRequest filterRequest, String source) {
		try {

			List<StateMgmtDb> statusList = stateMgmtServiceImpl
					.getByFeatureIdAndUserTypeId(filterRequest.getFeatureId(), filterRequest.getUserTypeId());
			logger.debug("statusList " + statusList);

			List<ConsignmentMgmt> consignmentMgmts = consignmentRepository.findAll(
					buildSpecification(filterRequest, statusList, source).build(),
					new Sort(Sort.Direction.DESC, "modifiedOn"));
			logger.debug("consignmentMgmts " + consignmentMgmts);

			for (ConsignmentMgmt consignmentMgmt2 : consignmentMgmts) {

				for (StateMgmtDb stateMgmtDb : statusList) {
					if (consignmentMgmt2.getConsignmentStatus() == stateMgmtDb.getState()) {
						consignmentMgmt2.setStateInterp(stateMgmtDb.getInterp());
						break;
					}
				}

				setInterp(consignmentMgmt2);
			}

			logger.info("ConsignmentMgmt : " + consignmentMgmts);
			return consignmentMgmts;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", featureName);
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.VIEW_ALL);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_011, filterRequest.getUserId(), bodyPlaceHolderMap);

			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	public Page<ConsignmentMgmt> getFilterPaginationConsignments(FilterRequest consignmentMgmt, Integer pageNo,
			Integer pageSize, String source) {
		//Pageable pageable = PageRequest.of(pageNo, pageSize, new Sort(Sort.Direction.DESC, "modifiedOn")) ;
		try {
			consigmentValidator.validateFilter(consignmentMgmt);

			List<StateMgmtDb> statusList = null;

			statusList = stateMgmtServiceImpl.getByFeatureIdAndUserTypeId(consignmentMgmt.getFeatureId(),
					consignmentMgmt.getUserTypeId());
			String orderColumn =null;
//			createdOn,taxPaidStatus,quantity,deviceQuantity,supplierName,consignmentStatus
			logger.info("column Name :: " + consignmentMgmt.getColumnName());
			if(consignmentMgmt.getUserType().equalsIgnoreCase("CEIRAdmin") || consignmentMgmt.getUserType().equalsIgnoreCase("Custom")) {
			 orderColumn = "Created On".equalsIgnoreCase(consignmentMgmt.getColumnName()) ? "createdOn"
					: "Transaction ID".equalsIgnoreCase(consignmentMgmt.getColumnName()) ? "txnId"
							: "Name".equalsIgnoreCase(consignmentMgmt.getColumnName()) ? "user.userProfile.displayName"
									: "Status".equalsIgnoreCase(consignmentMgmt.getColumnName()) ? "consignmentStatus"
											: "Tax Paid Status".equalsIgnoreCase(consignmentMgmt.getColumnName())
													? "taxPaidStatus"
													: "IMEI/MEID Quantity".equalsIgnoreCase(consignmentMgmt.getColumnName())
															? "quantity"
															:"Device Quantity".equalsIgnoreCase(consignmentMgmt.getColumnName())
															? "deviceQuantity" : "modifiedOn";
			
			}
			else {
				orderColumn = "Created On".equalsIgnoreCase(consignmentMgmt.getColumnName()) ? "createdOn"
						: "Transaction ID".equalsIgnoreCase(consignmentMgmt.getColumnName()) ? "txnId"
								: "Supplier Name".equalsIgnoreCase(consignmentMgmt.getColumnName()) ? "supplierName"
										: "Status".equalsIgnoreCase(consignmentMgmt.getColumnName()) ? "consignmentStatus"
												: "Tax Paid Status".equalsIgnoreCase(consignmentMgmt.getColumnName())
														? "taxPaidStatus"
														: "IMEI/MEID Quantity".equalsIgnoreCase(consignmentMgmt.getColumnName())
																? "quantity"
																:"Device Quantity".equalsIgnoreCase(consignmentMgmt.getColumnName())
																? "deviceQuantity" : "modifiedOn";
				
			}
			Sort.Direction direction;
			if("modifiedOn".equalsIgnoreCase(orderColumn)) {
				direction=Sort.Direction.DESC;
			}
			else {
				direction= SortDirection.getSortDirection(consignmentMgmt.getSort());
			}
			Pageable pageable = PageRequest.of(pageNo, pageSize, new Sort(direction, orderColumn));
			logger.info("column Name :: " + consignmentMgmt.getColumnName()+"---consignmentMgmt.getSort() : "+consignmentMgmt.getSort());
			Page<ConsignmentMgmt> page = consignmentRepository
					.findAll(buildSpecification(consignmentMgmt, statusList, source).build(), pageable);

			for (ConsignmentMgmt consignmentMgmt2 : page.getContent()) {

				for (StateMgmtDb stateMgmtDb : statusList) {
					if (consignmentMgmt2.getConsignmentStatus() == stateMgmtDb.getState()) {
						consignmentMgmt2.setStateInterp(stateMgmtDb.getInterp());
						break;
					}
				}

				setInterp(consignmentMgmt2);
			}
			
			if(source.equalsIgnoreCase("menu")) {
				auditTrailRepository.save(new AuditTrail(consignmentMgmt.getUserId(), consignmentMgmt.getUserName(),
						Long.valueOf(consignmentMgmt.getUserTypeId()), consignmentMgmt.getUserType(),
						Long.valueOf(consignmentMgmt.getFeatureId()), Features.CONSIGNMENT, SubFeatures.VIEW_ALL, "", "NA",
						consignmentMgmt.getRoleType(),consignmentMgmt.getPublicIp(),consignmentMgmt.getBrowser()));
			}
			else {
				auditTrailRepository.save(new AuditTrail(consignmentMgmt.getUserId(), consignmentMgmt.getUserName(),
						Long.valueOf(consignmentMgmt.getUserTypeId()), consignmentMgmt.getUserType(),
						Long.valueOf(consignmentMgmt.getFeatureId()), Features.CONSIGNMENT, SubFeatures.FILTER, "", "NA",
						consignmentMgmt.getRoleType(),consignmentMgmt.getPublicIp(),consignmentMgmt.getBrowser()));
			}
				
		
			logger.info("AUDIT : Saved view request in audit.");
			return page;

		} catch (RequestInvalidException e) {
			logger.error("Request validation failed for txnId[" + consignmentMgmt.getTxnId() + "]" + e);

			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", featureName);
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.VIEW_ALL);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_013, consignmentMgmt.getUserId(), bodyPlaceHolderMap);
			
// 3/feb/2021 code 
			return new PageImpl<ConsignmentMgmt>(new ArrayList<ConsignmentMgmt>(1), PageRequest.of(pageNo, pageSize, new Sort(Sort.Direction.DESC, "modifiedOn")), 0);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", featureName);
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.VIEW_ALL);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_011, consignmentMgmt.getUserId(), bodyPlaceHolderMap);

			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}

	}

	public ConsignmentMgmt getRecordInfo(String txnId) {
		try {
			logger.info("Going to get Cosignment Record Info for txnId : " + txnId);

			if (Objects.isNull(txnId)) {
				throw new IllegalArgumentException();
			}

			ConsignmentMgmt consignmentMgmt = consignmentRepository.getByTxnId(txnId);

			FilterRequest filterRequest = new FilterRequest().setTxnId(consignmentMgmt.getTxnId());
			if (pendingTacApprovedImpl.findByTxnId(filterRequest).getErrorCode() == 0) {
				logger.info("Tac related to the consignment with txn_id [" + consignmentMgmt.getTxnId()
						+ "] found in pending_tac_approval_db");
//				consignmentMgmt.setPendingTacApprovedByCustom("Y");
				consignmentMgmt.setPendingTacApprovedByCustomInterp("Y");
			} else {
				logger.info("No tac for the consignment with txn_id [" + consignmentMgmt.getTxnId() + "] is pending.");
//				consignmentMgmt.setPendingTacApprovedByCustom("N");
				consignmentMgmt.setPendingTacApprovedByCustomInterp("N");
			}

			setInterp(consignmentMgmt);

			return consignmentMgmt;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", featureName);
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.VIEW);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_011, 0, bodyPlaceHolderMap);

			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	public ConsignmentMgmt getRecordInfo(FilterRequest filterRequest) {
		try {
			consigmentValidator.validateViewById(filterRequest);

			String txnId = filterRequest.getTxnId();
			List<StateMgmtDb> statusList = stateMgmtServiceImpl
					.getByFeatureIdAndUserTypeId(filterRequest.getFeatureId(), filterRequest.getUserTypeId());
			logger.info("Going to get Cosignment Record Info for txnId : " + txnId);

			if (Objects.isNull(txnId)) {
				throw new IllegalArgumentException();
			}
			/** There is object related misbehave by JPA so we changed this **/
			ConsignmentMgmt consignmentMgmt = consignmentRepository.getByTxnId(txnId);

			/** for testing **/
			if (pendingTacApprovedImpl.findByTxnId(filterRequest).getErrorCode() == 0) {
				logger.info("Tac related to the consignment with txn_id [" + consignmentMgmt.getTxnId()
						+ "] found in pending_tac_approval_db");
				consignmentMgmt.setPendingTacApprovedByCustomInterp("Y");
			} else {
				logger.info("No tac for the consignment with txn_id [" + consignmentMgmt.getTxnId() + "] is pending.");
//				consignmentMgmt.setPendingTacApprovedByCustom("N");
				consignmentMgmt.setPendingTacApprovedByCustomInterp("N");
			}
			for (StateMgmtDb stateMgmtDb : statusList) {
				if (consignmentMgmt.getConsignmentStatus() == stateMgmtDb.getState()) {
					consignmentMgmt.setStateInterp(stateMgmtDb.getInterp());
					break;
				}
			}
			setInterp(consignmentMgmt);

			User loggedUser = userRepository.getById(filterRequest.getUserId());
			String username = loggedUser.getUsername();

			auditTrailRepository.save(
					new AuditTrail(filterRequest.getUserId(), username, Long.valueOf(filterRequest.getUserTypeId()),
							filterRequest.getUserType(), Long.valueOf(filterRequest.getFeatureId()),
							Features.CONSIGNMENT, SubFeatures.VIEW, "", txnId, filterRequest.getRoleType(),filterRequest.getPublicIp(),filterRequest.getBrowser()));
			logger.info("AUDIT : Saved file export request in audit.");

			return consignmentMgmt;
		} catch (RequestInvalidException e) {
			logger.error("Request validation failed for txnId[" + filterRequest.getTxnId() + "]" + e);

			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", featureName);
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.VIEW);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_013, 0, bodyPlaceHolderMap);

			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", featureName);
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.VIEW);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_011, 0, bodyPlaceHolderMap);

			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	public GenricResponse updateConsignment(ConsignmentMgmt consignmentFileRequest) {
		try {
			if (userStaticServiceImpl.checkIfUserIsDisabled(consignmentFileRequest.getUserId()))
				return new GenricResponse(5, "USER_IS_DISABLED",
						"This account is disabled. Please enable the account to perform the operation.",
						consignmentFileRequest.getTxnId());
			consigmentValidator.validateEdit(consignmentFileRequest);

			ConsignmentMgmt consignmentInfo = consignmentRepository.getByTxnId(consignmentFileRequest.getTxnId());

			logger.info("ConsignmentInfo = " + consignmentInfo.toString());

			if (Objects.isNull(consignmentInfo)) {
				return new GenricResponse(4, "Consignment Does Not exist.", consignmentFileRequest.getTxnId());
			} else {
				consignmentInfo.setConsignmentNumber(consignmentFileRequest.getConsignmentNumber());
				consignmentInfo.setExpectedArrivaldate(consignmentFileRequest.getExpectedArrivaldate());
				consignmentInfo.setExpectedArrivalPort(consignmentFileRequest.getExpectedArrivalPort());
				consignmentInfo.setExpectedDispatcheDate(consignmentFileRequest.getExpectedDispatcheDate());
				consignmentInfo.setOrganisationCountry(consignmentFileRequest.getOrganisationCountry());
				consignmentInfo.setQuantity(consignmentFileRequest.getQuantity());
				consignmentInfo.setSupplierId(consignmentFileRequest.getSupplierId());
				consignmentInfo.setSupplierName(consignmentFileRequest.getSupplierName());
				consignmentInfo.setTotalPrice(consignmentFileRequest.getTotalPrice());
				consignmentInfo.setCurrency(consignmentFileRequest.getCurrency());
				consignmentInfo.setPortAddress(consignmentFileRequest.getPortAddress());
				consignmentInfo.setDeviceQuantity(consignmentFileRequest.getDeviceQuantity());

				consignmentInfo.setUserName(consignmentFileRequest.getUserName());
				consignmentInfo.setUserType(consignmentFileRequest.getUserType());
				consignmentInfo.setUserTypeId(consignmentFileRequest.getUserTypeId());
				consignmentInfo.setFeatureId(consignmentFileRequest.getFeatureId());
				consignmentInfo.setRoleType(consignmentFileRequest.getRoleType());
				consignmentInfo.setRemarks(null);
				consignmentInfo.setCeirAdminID(null);
				consignmentInfo.setCustomID(null);
				consignmentInfo.setBrowser(consignmentFileRequest.getBrowser());
				consignmentInfo.setPublicIp(consignmentFileRequest.getPublicIp());
				// Pending tac if available in pending_tac_approval_db.
				FilterRequest filterRequest = new FilterRequest().setTxnId(consignmentFileRequest.getTxnId());
				if (pendingTacApprovedImpl.findByTxnId(filterRequest).getErrorCode() == 0) {
					logger.info("Tac related to the consignment with txn_id [" + consignmentFileRequest.getTxnId()
							+ "] found in pending_tac_approval_db");
					consignmentInfo.setPendingTacApprovedByCustom("Y");
				} else {
					logger.info("No tac for the consignment with txn_id [" + consignmentFileRequest.getTxnId()
							+ "] is pending.");
					consignmentInfo.setPendingTacApprovedByCustom("N");
				}

				if (Objects.nonNull(consignmentFileRequest.getFileName())
						&& !consignmentFileRequest.getFileName().isEmpty()) {
					consignmentInfo.setConsignmentStatus(ConsignmentStatus.INIT.getCode());
					consignmentInfo.setFileName(consignmentFileRequest.getFileName());
				}

				WebActionDb webActionDb = new WebActionDb();
				webActionDb.setFeature(WebActionDbFeature.CONSIGNMENT.getName());
				webActionDb.setSubFeature(WebActionDbSubFeature.UPDATE.getName());
				webActionDb.setState(WebActionDbState.INIT.getCode());
				webActionDb.setTxnId(consignmentFileRequest.getTxnId());

				if (consignmentTransaction.executeUpdateConsignment(consignmentInfo, webActionDb)) {
					return new GenricResponse(0, "Consignment Update in Processing.",
							consignmentFileRequest.getTxnId());
				} else {
					return new GenricResponse(1, "Consignment Update have been failed.",
							consignmentFileRequest.getTxnId());
				}
			}
		} catch (RequestInvalidException e) {
			logger.error("Request validation failed for txnId[" + consignmentFileRequest.getTxnId() + "]" + e);

			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", featureName);
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.UPDATE);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_013, consignmentFileRequest.getUserId(), bodyPlaceHolderMap);

			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", featureName);
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.UPDATE);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_011, consignmentFileRequest.getUserId(), bodyPlaceHolderMap);

			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	public GenricResponse deleteConsigmentInfo(ConsignmentUpdateRequest consignmentUpdateRequest) {
		try {
			if (userStaticServiceImpl.checkIfUserIsDisabled(consignmentUpdateRequest.getUserId()))
				return new GenricResponse(5, "USER_IS_DISABLED",
						"This account is disabled. Please enable the account to perform the operation.",
						consignmentUpdateRequest.getTxnId());
			consigmentValidator.validateDelete(consignmentUpdateRequest);
		} catch (RequestInvalidException e) {
			logger.error("Request validation failed for txnId[" + consignmentUpdateRequest.getTxnId() + "]" + e);

			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", featureName);
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.DELETE);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_013, consignmentUpdateRequest.getUserId().intValue(),
					bodyPlaceHolderMap);

			throw e;
		}
		UserProfile userProfile = null;
		Map<String, String> placeholderMap = new HashMap<>();
		Integer userId = consignmentUpdateRequest.getUserId().intValue();

		if (Objects.isNull(consignmentUpdateRequest.getTxnId())) {
			logger.info("TxnId is null in the request." + consignmentUpdateRequest.getTxnId());
			return new GenricResponse(1001, "TxnId is null in the request.", consignmentUpdateRequest.getTxnId());
		}

		if (Objects.isNull(consignmentUpdateRequest.getUserType())) {
			logger.info("userType is null in the request." + consignmentUpdateRequest.getTxnId());
			return new GenricResponse(1002, "userType is null in the request.", consignmentUpdateRequest.getTxnId());
		}

		String payloadTxnId = consignmentUpdateRequest.getTxnId();

		// lock.lock();
		logger.info("lock taken by thread for [Delete] - " + Thread.currentThread().getName());
		try {
			ConsignmentMgmt consignmentMgmt = consignmentRepository.getByTxnId(consignmentUpdateRequest.getTxnId());
			// Fetch user_profile to update user over mail/sms regarding the action.
			userProfile = userProfileRepository.getByUserId(consignmentMgmt.getUserId());

			if ("CEIRADMIN".equalsIgnoreCase(consignmentUpdateRequest.getUserType())) {
				// Check if someone else taken the same action on consignment.
				ConsignmentMgmt consignmentMgmtTemp = consignmentRepository.getByTxnId(consignmentMgmt.getTxnId());
				if (ConsignmentStatus.WITHDRAWN_BY_CEIR.getCode() == consignmentMgmtTemp.getConsignmentStatus()) {
					String message = "Any other user have taken the same action on the consignment [" + payloadTxnId
							+ "]";
					logger.info(message);
					return new GenricResponse(10, "", message, payloadTxnId);
				}

				consignmentMgmt.setConsignmentStatus(ConsignmentStatus.WITHDRAWN_BY_CEIR.getCode());
			} else if ("IMPORTER".equalsIgnoreCase(consignmentUpdateRequest.getUserType())) {
				// Check if someone else taken the same action on consignment.
				ConsignmentMgmt consignmentMgmtTemp = consignmentRepository.getByTxnId(consignmentMgmt.getTxnId());
				if (ConsignmentStatus.WITHDRAWN_BY_IMPORTER.getCode() == consignmentMgmtTemp.getConsignmentStatus()) {
					String message = "Any other user have taken the same action on the consignment [" + payloadTxnId
							+ "]";
					logger.info(message);
					return new GenricResponse(10, "", message, payloadTxnId);
				}

				// Check status must be Init or Rejected by system.
				if (consignmentMgmt.getConsignmentStatus() == ConsignmentStatus.INIT.getCode()
						|| consignmentMgmt.getConsignmentStatus() == ConsignmentStatus.REJECTED_BY_SYSTEM.getCode()) {
					consignmentMgmt.setConsignmentStatus(ConsignmentStatus.WITHDRAWN_BY_IMPORTER.getCode());
				} else {
					return new GenricResponse(5, GenericMessageTags.INVALID_STATE_TRANSTION.getTag(),
							GenericMessageTags.INVALID_STATE_TRANSTION.getMessage(),
							consignmentUpdateRequest.getTxnId());
				}
			} else {
				logger.info("UserType is invalid." + consignmentUpdateRequest.getTxnId());
				return new GenricResponse(1, "UserType is invalid.", consignmentUpdateRequest.getTxnId());
			}

			consignmentMgmt.setRemarks(consignmentUpdateRequest.getRemarks());
			consignmentMgmt.setDeleteFlag(0);
			consignmentMgmt.setUserTypeId(consignmentUpdateRequest.getUserTypeId());
			consignmentMgmt.setFeatureId(consignmentUpdateRequest.getFeatureId());
			consignmentMgmt.setUserName(consignmentUpdateRequest.getUserName());
			consignmentMgmt.setUserType(consignmentUpdateRequest.getUserType());
			consignmentMgmt.setRoleType(consignmentUpdateRequest.getRoleType());
			consignmentMgmt.setBrowser(consignmentUpdateRequest.getBrowser());
			consignmentMgmt.setPublicIp(consignmentUpdateRequest.getPublicIp());
			
			WebActionDb webActionDb = new WebActionDb();
			webActionDb.setFeature(WebActionDbFeature.CONSIGNMENT.getName());
			webActionDb.setSubFeature(WebActionDbSubFeature.DELETE.getName());
			webActionDb.setState(WebActionDbState.INIT.getCode());
			webActionDb.setTxnId(consignmentUpdateRequest.getTxnId());
			if ("CEIRADMIN".equalsIgnoreCase(consignmentUpdateRequest.getUserType())) {
				// Delete Tac if available in pending_tac_approval_db.
				if (cleanFromPendingTacApprovalDb(consignmentMgmt.getTxnId(), consignmentMgmt.getUserId()))
					consignmentMgmt.setPendingTacApprovedByCustom("Y");
				else
					consignmentMgmt.setPendingTacApprovedByCustom("N");
			}
			if (consignmentTransaction.executeDeleteConsignment(consignmentMgmt, webActionDb)) {
				/*
				 * if("CEIRADMIN".equalsIgnoreCase(consignmentUpdateRequest.getUserType())) { //
				 * Delete Tac if available in pending_tac_approval_db.
				 * cleanFromPendingTacApprovalDb(consignmentMgmt); }
				 */

				if (consignmentMgmt.getConsignmentStatus() == 9) {
					Generic_Response_Notification generic_Response_Notification = userFeignClient
							.ceirInfoByUserTypeId(8);

					logger.info("generic_Response_Notification::::::::" + generic_Response_Notification);

					List<RegisterationUser> registerationUserList = generic_Response_Notification.getData();

					for (RegisterationUser registerationUser : registerationUserList) {
						UserProfile userProfile_generic_Response_Notification = new UserProfile();
						userProfile_generic_Response_Notification = userProfileRepository
								.getByUserId(registerationUser.getId());

						placeholderMap.put("<First name>", userProfile_generic_Response_Notification.getFirstName());
						placeholderMap.put("<Txn id>", consignmentMgmt.getTxnId());

						emailUtil.saveNotification("CONSIGNMENT_DELETED_BY_CEIR_MSG",
								userProfile_generic_Response_Notification, consignmentUpdateRequest.getFeatureId(),
								Features.CONSIGNMENT, SubFeatures.DELETE, consignmentUpdateRequest.getTxnId(),
								consignmentMgmt.getTxnId(), placeholderMap, null, "CEIRAdmin", ReferTable.USERS);
					}
					placeholderMap.put("<First name>", userProfile.getFirstName());
					placeholderMap.put("<Txn id>", consignmentMgmt.getTxnId());

					logger.info("consignmentMgmt.getTxnId() :: " + consignmentMgmt.getTxnId() + "-------> "
							+ userProfile.getFirstName());
					emailUtil.saveNotification("CONSIGNMENT_DELETED_BY_CEIR_MSG", userProfile,
							consignmentUpdateRequest.getFeatureId(), Features.CONSIGNMENT, SubFeatures.DELETE,
							consignmentUpdateRequest.getTxnId(), consignmentMgmt.getTxnId(), placeholderMap,
							ReferTable.USERS, consignmentUpdateRequest.getRoleType(), "Importer");
				} else {

					placeholderMap.put("<First name>", userProfile.getFirstName());
					placeholderMap.put("<Txn id>", consignmentMgmt.getTxnId());

					logger.info("consignmentMgmt.getTxnId() :: " + consignmentMgmt.getTxnId() + "-------> "
							+ userProfile.getFirstName());
					emailUtil.saveNotification("Consignment_Delete_Email_Message", userProfile,
							consignmentUpdateRequest.getFeatureId(), Features.CONSIGNMENT, SubFeatures.DELETE,
							consignmentUpdateRequest.getTxnId(), consignmentMgmt.getTxnId(), placeholderMap,
							ReferTable.USERS, consignmentUpdateRequest.getRoleType(), "Importer");
				}

				logger.info("Deletion of consignment is in Progress." + consignmentUpdateRequest.getTxnId());
				return new GenricResponse(200, "Consignment deletion is in Progress.",
						consignmentUpdateRequest.getTxnId());
			} else {
				logger.info("Deletion of consignment have been failed." + consignmentUpdateRequest.getTxnId());
				return new GenricResponse(2, "Deletion of consignment have been failed.",
						consignmentUpdateRequest.getTxnId());
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", featureName);
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.DELETE);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_011, userId, bodyPlaceHolderMap);

			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		} finally {
			/*
			 * if(lock.isLocked()) { logger.info("lock released by thread [Delete] - " +
			 * Thread.currentThread().getName()); lock.unlock(); }
			 */
		}
	}

	public GenricResponse updateConsignmentStatus(ConsignmentUpdateRequest consignmentUpdateRequest) {
		try {
			if (!CEIRSYSTEM.equalsIgnoreCase(consignmentUpdateRequest.getRoleType())
					&& userStaticServiceImpl.checkIfUserIsDisabled(consignmentUpdateRequest.getUserId())) {
				return new GenricResponse(5, "USER_IS_DISABLED",
						"This account is disabled. Please enable the account to perform the operation.",
						consignmentUpdateRequest.getTxnId());
			}
			consigmentValidator.validateAcceptReject(consignmentUpdateRequest);

			String roleType = consignmentUpdateRequest.getRoleType();
			int action = consignmentUpdateRequest.getAction();
			String payloadTxnID = consignmentUpdateRequest.getTxnId();
			UserProfile ceirUserProfile = new UserProfile();

			UserProfile userProfile = null;
			ConsignmentMgmt consignmentMgmt = consignmentRepository.getByTxnId(payloadTxnID);
			logger.debug("Accept/Reject Consignment : " + consignmentMgmt);

			// Fetch user_profile to update user over mail/sms regarding the action.
			userProfile = userProfileRepository.getByUserId(consignmentMgmt.getUserId());
			logger.info("UserProfile : " + userProfile);

			// action 0 - Accept, 1 - Reject
			if (Objects.isNull(consignmentMgmt)) {
				return new GenricResponse(1, "TxnId Does not Exist.", payloadTxnID);
			} else {
				if ("CEIRADMIN".equalsIgnoreCase(roleType)) {
					logger.info("Running for CEIRADMIN");
					return ceirAdminStatus(consignmentMgmt, consignmentUpdateRequest, userProfile, ceirUserProfile,
							action);
				} else if ("CUSTOM".equalsIgnoreCase(roleType)) {
					logger.info("Running for CUSTOM");
					return ceirCustomStatus(consignmentMgmt, consignmentUpdateRequest, userProfile, action);
				} else if (CEIRSYSTEM.equalsIgnoreCase(roleType)) {
					logger.info("Running for CEIRSYSTEM");
					return ceirSystemStatus(consignmentMgmt, userProfile, consignmentUpdateRequest, ceirUserProfile,
							consignmentMgmt.getConsignmentStatus(), action);
				} else if ("DRT".equalsIgnoreCase(roleType)) {
					logger.info("Running for DRT");
					return ceirDRTStatus(consignmentMgmt, userProfile, consignmentUpdateRequest, ceirUserProfile,
							action);
				} else {
					logger.info("Nothing to update for request :: " + consignmentUpdateRequest);
				}
				return new GenricResponse(1, "Nothing to update for request.", payloadTxnID);

			}
		} catch (RequestInvalidException e) {
			logger.error("Request validation failed for txnId[" + consignmentUpdateRequest.getTxnId() + "]" + e);

			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", featureName);
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.DELETE);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_013, consignmentUpdateRequest.getUserId().intValue(),
					bodyPlaceHolderMap);

			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", featureName);
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.DELETE);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_011, consignmentUpdateRequest.getUserId().intValue(),
					bodyPlaceHolderMap);

			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		} /*
			 * finally { if(lock.isLocked()) { logger.info("lock released by thread : " +
			 * Thread.currentThread().getName()); lock.unlock(); } }
			 */
	}

	public FileDetails getFilteredConsignmentInFileV2(FilterRequest filterRequest, String source) {
		try {
			consigmentValidator.validateFilter(filterRequest);
		} catch (RequestInvalidException e) {
			logger.error("Request validation failed for txnId[" + filterRequest.getTxnId() + "]" + e);

			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", featureName);
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.EXPORT);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_013, filterRequest.getUserId(), bodyPlaceHolderMap);

			throw e;
		}
		String fileName = null;
		Writer writer = null;

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

		SystemConfigurationDb filepath = configurationManagementServiceImpl.findByTag(ConfigTags.file_download_dir);
		logger.info("CONFIG : file_consignment_download_dir [" + filepath + "]");
		SystemConfigurationDb link = configurationManagementServiceImpl.findByTag(ConfigTags.file_download_link);
		logger.info("CONFIG : file_consignment_download_link [" + link + "]");

		try {
			List<ConsignmentMgmt> consignmentMgmts = getAll(filterRequest, source);

			fileName = LocalDateTime.now().format(dtf2).replace(" ", "_") + "_Consignment.csv";
			writer = Files.newBufferedWriter(Paths.get(filepath.getValue() + fileName));

			if (filterRequest.getUserTypeId() != 8) {
				List<ConsignmentFileModel> fileRecords = new ArrayList<>();
				CustomMappingStrategy<ConsignmentFileModel> mappingStrategy = new CustomMappingStrategy<>();
				mappingStrategy.setType(ConsignmentFileModel.class);

				StatefulBeanToCsvBuilder<ConsignmentFileModel> builder = new StatefulBeanToCsvBuilder<>(writer);
				StatefulBeanToCsv<ConsignmentFileModel> csvWriter = builder.withMappingStrategy(mappingStrategy)
						.withSeparator(',').withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).build();
				if (!consignmentMgmts.isEmpty()) {
					for (ConsignmentMgmt consignmentMgmt : consignmentMgmts) {
						fileRecords.add(new ConsignmentFileModel(consignmentMgmt.getCreatedOn().format(dtf),
								consignmentMgmt.getModifiedOn().format(dtf), consignmentMgmt.getTxnId(),
								consignmentMgmt.getSupplierName(), consignmentMgmt.getStateInterp(),
								consignmentMgmt.getTaxInterp(), consignmentMgmt.getQuantity(),
								consignmentMgmt.getDeviceQuantity(), consignmentMgmt.getFileName()));
					}
					csvWriter.write(fileRecords);
				} else {
					csvWriter.write(new ConsignmentFileModel());
				}
			} else {
				List<ConsignmentFileModelCEIR> fileRecords = new ArrayList<>();
				CustomMappingStrategy<ConsignmentFileModelCEIR> mappingStrategy = new CustomMappingStrategy<>();

				StatefulBeanToCsvBuilder<ConsignmentFileModelCEIR> builder = new StatefulBeanToCsvBuilder<>(writer);
				StatefulBeanToCsv<ConsignmentFileModelCEIR> csvWriter = builder.withMappingStrategy(mappingStrategy)
						.withSeparator(',').withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).build();
				mappingStrategy.setType(ConsignmentFileModelCEIR.class);
				if (!consignmentMgmts.isEmpty()) {
					for (ConsignmentMgmt consignmentMgmt : consignmentMgmts) {
						fileRecords.add(new ConsignmentFileModelCEIR(consignmentMgmt.getModifiedOn().format(dtf),
								consignmentMgmt.getCreatedOn().format(dtf), consignmentMgmt.getTxnId(),
								consignmentMgmt.getUser().getUserProfile().getDisplayName(),
								consignmentMgmt.getStateInterp(), consignmentMgmt.getTaxInterp(),
								consignmentMgmt.getQuantity(), consignmentMgmt.getDeviceQuantity(),
								consignmentMgmt.getFileName()));
					}
					csvWriter.write(fileRecords);
				} else {
					csvWriter.write(new ConsignmentFileModelCEIR());
				}
			}

			User loggedUser = userRepository.getById(filterRequest.getUserId());
			String username = loggedUser.getUsername();

			auditTrailRepository.save(
					new AuditTrail(filterRequest.getUserId(), username, Long.valueOf(filterRequest.getUserTypeId()),
							filterRequest.getUserType(), Long.valueOf(filterRequest.getFeatureId()),
							Features.CONSIGNMENT, SubFeatures.EXPORT, "", "NA", filterRequest.getRoleType(),filterRequest.getPublicIp(),filterRequest.getBrowser()));
			logger.info("AUDIT : Saved file export request in audit.");

			FileDetails fileDetails = new FileDetails(fileName, filepath.getValue(),
					link.getValue().replace("$LOCAL_IP", propertiesReader.localIp) + fileName);
			logger.info(fileDetails);
			return fileDetails;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			Map<String, String> bodyPlaceHolderMap = new HashMap<>();
			bodyPlaceHolderMap.put("<feature>", featureName);
			bodyPlaceHolderMap.put("<sub_feature>", SubFeatures.EXPORT);
			alertServiceImpl.raiseAnAlert(Alerts.ALERT_011, filterRequest.getUserId(), bodyPlaceHolderMap);

			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		} finally {
			try {

				if (writer != null)
					writer.close();
			} catch (IOException e) {
			}
		}
	}

	public ResponseCountAndQuantity getConsignmentCountAndQuantity(Integer userId, Integer userTypeId,
			Integer featureId, String userType) {
		List<StateMgmtDb> featureList = null;
		List<Integer> status = new ArrayList<>();
		try {
			logger.info("Going to get  Cosignment count and quantity.");
			featureList = stateMgmtServiceImpl.getByFeatureIdAndUserTypeId(featureId, userTypeId);
			if (Objects.nonNull(featureList)) {
				for (StateMgmtDb stateDb : featureList) {
					status.add(stateDb.getState());
				}
			}
			if (!userType.equalsIgnoreCase("ceiradmin"))
				return consignmentRepository.getConsignmentCountAndQuantity(userId, status);
			else
				return consignmentRepository.getConsignmentCountAndQuantity(status);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ResponseCountAndQuantity(0, 0);
		}
	}

	private GenericSpecificationBuilder<ConsignmentMgmt> buildSpecification(FilterRequest consignmentMgmt,
			List<StateMgmtDb> statusList, String source) {

		GenericSpecificationBuilder<ConsignmentMgmt> cmsb = new GenericSpecificationBuilder<>(propertiesReader.dialect);

		/**
		 * This condition is added by Ravi on requirement that custom will only see
		 * their registered port address consignment
		 **/
		if (Objects.nonNull(consignmentMgmt.getUserType())
				&& consignmentMgmt.getUserType().equalsIgnoreCase("custom")) {
			UserProfile userProfile = userRepository.getById(consignmentMgmt.getUserId()).getUserProfile();
			if (Objects.nonNull(userProfile))
				cmsb.with(new SearchCriteria("portAddress", userProfile.getPortAddress(), SearchOperation.EQUALITY,
						Datatype.INT));

		}

		if ("IMPORTER".equalsIgnoreCase(consignmentMgmt.getUserType())
				&& Objects.nonNull(consignmentMgmt.getUserId())) {

			cmsb.with(new SearchCriteria("userId", consignmentMgmt.getUserId(), SearchOperation.EQUALITY,
					Datatype.STRING));
		}

		if (Objects.nonNull(consignmentMgmt.getTxnId()) && !consignmentMgmt.getTxnId().isEmpty())
			cmsb.with(new SearchCriteria("txnId", consignmentMgmt.getTxnId(), SearchOperation.LIKE, Datatype.STRING));
//			cmsb.with(new SearchCriteria("txnId", consignmentMgmt.getTxnId(), SearchOperation.EQUALITY_CASE_INSENSITIVE, Datatype.STRING));

		if (Objects.nonNull(consignmentMgmt.getStartDate()) && !consignmentMgmt.getStartDate().isEmpty())
			cmsb.with(new SearchCriteria("createdOn", consignmentMgmt.getStartDate(), SearchOperation.GREATER_THAN,
					Datatype.DATE));

		if (Objects.nonNull(consignmentMgmt.getEndDate()) && !consignmentMgmt.getEndDate().isEmpty())
			cmsb.with(new SearchCriteria("createdOn", consignmentMgmt.getEndDate(), SearchOperation.LESS_THAN,
					Datatype.DATE));

		if (Objects.nonNull(consignmentMgmt.getTaxPaidStatus()))
			cmsb.with(new SearchCriteria("taxPaidStatus", consignmentMgmt.getTaxPaidStatus(), SearchOperation.EQUALITY,
					Datatype.STRING));

		if (Objects.nonNull(consignmentMgmt.getQuantity()) && !consignmentMgmt.getQuantity().isEmpty())
			cmsb.with(new SearchCriteria("quantity", consignmentMgmt.getQuantity(), SearchOperation.LIKE,
					Datatype.STRING));

		if (Objects.nonNull(consignmentMgmt.getDeviceQuantity()) && !consignmentMgmt.getDeviceQuantity().isEmpty())
			cmsb.with(new SearchCriteria("deviceQuantity", consignmentMgmt.getDeviceQuantity(), SearchOperation.LIKE,
					Datatype.STRING));

		if (Objects.nonNull(consignmentMgmt.getSupplierName()) && !consignmentMgmt.getSupplierName().isEmpty())
			cmsb.with(new SearchCriteria("supplierName", consignmentMgmt.getSupplierName(), SearchOperation.LIKE,
					Datatype.STRING));

//		if(Objects.nonNull(consignmentMgmt.getDisplayName()) && !consignmentMgmt.getDisplayName().isEmpty())
//			cmsb.addSpecification(cmsb.joinWithMultiple(new SearchCriteria("displayName",consignmentMgmt.getDisplayName(), SearchOperation.EQUALITY_CASE_INSENSITIVE, Datatype.STRING)));

		if (Objects.nonNull(consignmentMgmt.getDisplayName()) && !consignmentMgmt.getDisplayName().isEmpty()
				&& !consignmentMgmt.getDisplayName().equalsIgnoreCase("null")
				&& !consignmentMgmt.getDisplayName().equalsIgnoreCase("undefined")) {
			logger.info("User display name:[" + consignmentMgmt.getDisplayName() + "]");
			cmsb.with(new SearchCriteria("user-userProfile-displayName",
					consignmentMgmt.getDisplayName().trim().replace("  ", " "), SearchOperation.LIKE, Datatype.STRING));
		}

		if (Objects.nonNull(consignmentMgmt.getConsignmentStatus())) {
			cmsb.with(new SearchCriteria("consignmentStatus", consignmentMgmt.getConsignmentStatus(),
					SearchOperation.EQUALITY, Datatype.STRING));
		} else if (!(Objects.nonNull(consignmentMgmt.getTxnId()) && !consignmentMgmt.getTxnId().isEmpty())
				&& !Objects.nonNull(consignmentMgmt.getTaxPaidStatus())
				&& (( !(Objects.nonNull(consignmentMgmt.getDisplayName()) && !consignmentMgmt.getDisplayName().isEmpty()
						&& !consignmentMgmt.getDisplayName().equalsIgnoreCase("null")
						&& !consignmentMgmt.getDisplayName().equalsIgnoreCase("undefined"))|| ( consignmentMgmt.getUserType().equalsIgnoreCase("custom") )) )) {
			if (Objects.nonNull(consignmentMgmt.getFeatureId()) && Objects.nonNull(consignmentMgmt.getUserTypeId())) {
				logger.info("User type id:[" + consignmentMgmt.getFeatureId() + "] and userTypeId:["
						+ consignmentMgmt.getUserTypeId() + "]");
				List<DashboardUsersFeatureStateMap> dashboardUsersFeatureStateMaps = dashboardUsersFeatureStateMapRepository
						.findByUserTypeIdAndFeatureId(consignmentMgmt.getUserTypeId(), consignmentMgmt.getFeatureId());
				logger.info(dashboardUsersFeatureStateMaps);
				List<Integer> consignmentStatus = new LinkedList<>();

				if (Objects.nonNull(dashboardUsersFeatureStateMaps)) {

					if ("dashboard".equalsIgnoreCase(source) || "menu".equalsIgnoreCase(source)) {
						for (DashboardUsersFeatureStateMap dashboardUsersFeatureStateMap2 : dashboardUsersFeatureStateMaps) {
							consignmentStatus.add(dashboardUsersFeatureStateMap2.getState());
						}
					} else if ("filter".equalsIgnoreCase(source)) {
						boolean isFilterEmpty = nothingInFilter(consignmentMgmt);
						logger.info("Nothing in filter : " + isFilterEmpty);

						if (isFilterEmpty) {
							for (DashboardUsersFeatureStateMap dashboardUsersFeatureStateMap2 : dashboardUsersFeatureStateMaps) {
								consignmentStatus.add(dashboardUsersFeatureStateMap2.getState());
							}
						} else {
							for (StateMgmtDb stateMgmtDb : statusList) {
								consignmentStatus.add(stateMgmtDb.getState());
							}
						}
					} else if ("noti".equalsIgnoreCase(source)) {
						logger.info("Skip status check, because source is noti.");
					}

					logger.info("Array list to add is = " + consignmentStatus);

					if (!consignmentStatus.isEmpty()) {
						cmsb.addSpecification(cmsb.in("consignmentStatus", consignmentStatus));
					} else {
						logger.warn("no status are predefined for the user.");
					}
				}
			}
		}

		if (Objects.nonNull(consignmentMgmt.getSearchString()) && !consignmentMgmt.getSearchString().isEmpty()) {
			cmsb.orSearch(new SearchCriteria("txnId", consignmentMgmt.getSearchString(), SearchOperation.LIKE,
					Datatype.STRING));
			cmsb.orSearch(new SearchCriteria("supplierName", consignmentMgmt.getSearchString(), SearchOperation.LIKE,
					Datatype.STRING));
			cmsb.orSearch(new SearchCriteria("organisationCountry", consignmentMgmt.getSearchString(),
					SearchOperation.LIKE, Datatype.STRING));
			cmsb.orSearch(new SearchCriteria("quantity", consignmentMgmt.getSearchString(), SearchOperation.LIKE,
					Datatype.STRING));
			cmsb.orSearch(new SearchCriteria("expectedArrivaldate", consignmentMgmt.getSearchString(),
					SearchOperation.EQUALITY, Datatype.DATE));
			cmsb.orSearch(new SearchCriteria("expectedDispatcheDate", consignmentMgmt.getSearchString(),
					SearchOperation.EQUALITY, Datatype.DATE));
			cmsb.orSearch(new SearchCriteria("totalPrice", consignmentMgmt.getSearchString(), SearchOperation.LIKE,
					Datatype.STRING));
			cmsb.orSearch(new SearchCriteria("deviceQuantity", consignmentMgmt.getSearchString(), SearchOperation.LIKE,
					Datatype.STRING));
			cmsb.orSearch(new SearchCriteria("consignmentNumber", consignmentMgmt.getSearchString(),
					SearchOperation.LIKE, Datatype.STRING));
			cmsb.orSearch(new SearchCriteria("supplierId", consignmentMgmt.getSearchString(), SearchOperation.LIKE,
					Datatype.STRING));
			cmsb.orSearch(new SearchCriteria("fileName", consignmentMgmt.getSearchString(), SearchOperation.LIKE,
					Datatype.STRING));
		}

		return cmsb;
	}

	public void setInterp(ConsignmentMgmt consignmentMgmt) {

		if (Objects.nonNull(consignmentMgmt.getExpectedArrivalPort()))
			consignmentMgmt.setExpectedArrivalPortInterp(
					interpSetter.setConfigInterp(Tags.CUSTOMS_PORT, consignmentMgmt.getExpectedArrivalPort()));

		if (Objects.nonNull(consignmentMgmt.getCurrency()))
			consignmentMgmt
					.setCurrencyInterp(interpSetter.setConfigInterp(Tags.CURRENCY, consignmentMgmt.getCurrency()));

		if (Objects.nonNull(consignmentMgmt.getTaxPaidStatus()))
			consignmentMgmt.setTaxInterp(
					interpSetter.setConfigInterp(Tags.CUSTOMS_TAX_STATUS, consignmentMgmt.getTaxPaidStatus()));

		if (Objects.nonNull(consignmentMgmt.getTaxPaidStatus()))
			consignmentMgmt.setDeleteFlagInterp(
					interpSetter.setConfigInterp(Tags.DELETE_FLAG, consignmentMgmt.getDeleteFlag()));

		if (Objects.nonNull(consignmentMgmt.getPortAddress())) {
			GenricResponse_Class portResponse = userFeignClient.portAddressInterp(consignmentMgmt.getPortAddress());
			logger.info("genericResponse::::::" + new Gson().toJson(portResponse));
			DataClass dataClass = portResponse.getData();
			consignmentMgmt.setPortAddressInterp(dataClass.getAddress());

		}

	}

//	@Transactional
	public boolean updatePendingApproval(ConsignmentUpdateRequest consignmentUpdateRequest) {
		try {
			ConsignmentMgmt consignmentInfo = consignmentRepository.getByTxnId(consignmentUpdateRequest.getTxnId());
			consignmentInfo.setRemarks(consignmentUpdateRequest.getRemarks());

			logger.info("[Trying to update ConsignmentMgmt] | Model [" + consignmentUpdateRequest + "]");
			consignmentRepository.save(consignmentInfo);
			logger.info("[Updation of Consignment is successful]  | Model [" + consignmentInfo + "]");
			return Boolean.TRUE;
		} catch (Exception e) {
			logger.error("[Error while updating ConsignmentMgmt] | Model [" + consignmentUpdateRequest + "] | Error ["
					+ e + "]");
			return Boolean.FALSE;
		}
	}

	@PostConstruct
	public void setFeatureName() {
		featureName = stakeholderfeatureServiceImpl.getFeatureNameById(3L);
	}

	public GenricResponse ceirAdminStatus(ConsignmentMgmt consignmentMgmt,
			ConsignmentUpdateRequest consignmentUpdateRequest, UserProfile userProfile, UserProfile ceirUserProfile,
			int action) {
		String payloadTxnId = consignmentUpdateRequest.getTxnId();
		if (!StateMachine.isConsignmentStatetransitionAllowedWithAction("CEIRADMIN",
				consignmentMgmt.getConsignmentStatus(), 0)) {
			logger.info("state transition is not allowed." + payloadTxnId);
			return new GenricResponse(3, "state transition is not allowed.", payloadTxnId);
		}

		WebActionDb webActionDb = null;
		Integer nextStatus = null;
		GenricResponse_Class response = null;
		Map<String, String> placeholderMap = new HashMap<>();
		Port port = new Port();

		/*
		 * lock.lock(); logger.info("lock taken by thread : " +
		 * Thread.currentThread().getName());
		 */
		if (action == 0) {
			// Check if someone else taken the same action on consignment.
			ConsignmentMgmt consignmentMgmtTemp = consignmentRepository.getByTxnId(consignmentMgmt.getTxnId());
			if (ConsignmentStatus.PENDING_CLEARANCE_FROM_DRT.getCode() == consignmentMgmtTemp.getConsignmentStatus()
					|| ConsignmentStatus.PENDING_APPROVAL_FROM_CUSTOMS.getCode() == consignmentMgmtTemp
							.getConsignmentStatus()) {
				String message = "Any other user have taken the same action on the consignment [" + payloadTxnId + "]";
				logger.info(message);
				return new GenricResponse(10, "", message, payloadTxnId);
			}

			// Checking DRT enable/disable.
			response = userFeignClient.usertypeStatus(21);
			logger.info("FEIGN : response for validatePeriod " + response);

			if (response.getErrorCode() == 200) {
				nextStatus = ConsignmentStatus.PENDING_CLEARANCE_FROM_DRT.getCode();
			} else {
				response = userFeignClient.usertypeStatus(7);
				logger.info("FEIGN : response for validatePeriod " + response);
				nextStatus = response.getErrorCode() == 200 ? ConsignmentStatus.PENDING_APPROVAL_FROM_CUSTOMS.getCode()
						: ConsignmentStatus.APPROVED.getCode();
			}

			consignmentMgmt.setConsignmentStatus(nextStatus);
		} else {
			// Check if someone else taken the same action on consignment.
			ConsignmentMgmt consignmentMgmtTemp = consignmentRepository.getByTxnId(consignmentMgmt.getTxnId());
			if (ConsignmentStatus.REJECTED_BY_CEIR_AUTHORITY.getCode() == consignmentMgmtTemp.getConsignmentStatus()) {
				String message = "Any other user have taken the same action on the consignment [" + payloadTxnId + "]";
				logger.info(message);
				return new GenricResponse(10, "", message, payloadTxnId);
			}

			nextStatus = ConsignmentStatus.REJECTED_BY_CEIR_AUTHORITY.getCode();
			consignmentMgmt.setConsignmentStatus(nextStatus);
			consignmentMgmt.setRemarks(consignmentUpdateRequest.getRemarks());

			webActionDb = new WebActionDb();
			webActionDb.setFeature(WebActionDbFeature.CONSIGNMENT.getName());
			webActionDb.setState(WebActionDbState.INIT.getCode());
			webActionDb.setTxnId(consignmentMgmt.getTxnId());
			webActionDb.setSubFeature(WebActionDbSubFeature.REJECT.getName());
			// Delete Tac if available in pending_tac_approval_db.
			if (cleanFromPendingTacApprovalDb(consignmentMgmt.getTxnId(), consignmentMgmt.getUserId()))
				consignmentMgmt.setPendingTacApprovedByCustom("Y");
			else
				consignmentMgmt.setPendingTacApprovedByCustom("N");
		}
		consignmentMgmt.setCeirAdminID(consignmentUpdateRequest.getUserId());
		consignmentMgmt.setFeatureId(consignmentUpdateRequest.getFeatureId());
		consignmentMgmt.setRoleType(consignmentUpdateRequest.getRoleType());

		if (consignmentTransaction.executeUpdateStatus(consignmentUpdateRequest, consignmentMgmt, webActionDb)) {
			ConsignmentMgmt current_consignment_response = consignmentRepository.getByTxnId(payloadTxnId);

			if (action == 0) {
				logger.info("Notification sent to Importer::: with current_status : "
						+ current_consignment_response.getConsignmentStatus());

				placeholderMap.put("<First name>", userProfile.getFirstName());
				placeholderMap.put("<Txn id>", current_consignment_response.getTxnId());
				emailUtil.saveNotification("Consignment_Success_CEIRAuthority_Email_Message", userProfile,
						consignmentUpdateRequest.getFeatureId(), Features.CONSIGNMENT, SubFeatures.ACCEPT,
						consignmentUpdateRequest.getTxnId(), current_consignment_response.getTxnId(), placeholderMap,
						null, "Importer", ReferTable.USERS);

				if (ConsignmentStatus.PENDING_CLEARANCE_FROM_DRT.getCode() == current_consignment_response
						.getConsignmentStatus()) {
					Generic_Response_Notification generic_Response_Notification = userFeignClient
							.ceirInfoByUserTypeId(21);

					logger.info("generic_Response_Notification::::::::" + generic_Response_Notification);
					List<RegisterationUser> registerationUserList = generic_Response_Notification.getData();
					if (registerationUserList == null || registerationUserList.isEmpty()) {
						logger.info("no user found");
					} else {

						for (RegisterationUser registerationUser : registerationUserList) {
							UserProfile userProfile_generic_Response_Notification = new UserProfile();
							userProfile_generic_Response_Notification = userProfileRepository
									.getByUserId(registerationUser.getId());
							logger.info("firstname:::" + userProfile_generic_Response_Notification.getFirstName());
							logger.info("DRT PENDING_CLEARANCE_FROM_DRT Notification:::with current_status : "
									+ current_consignment_response.getConsignmentStatus());
							placeholderMap.put("<First name>",
									userProfile_generic_Response_Notification.getFirstName());
							placeholderMap.put("<Txn id>", current_consignment_response.getTxnId());
							emailUtil.saveNotification("PENDING_CLEARANCE_FROM_DRT_Email_Message",
									userProfile_generic_Response_Notification, consignmentUpdateRequest.getFeatureId(),
									Features.CONSIGNMENT, SubFeatures.ACCEPT, payloadTxnId,
									current_consignment_response.getTxnId(), placeholderMap, "DRT", "DRT",
									ReferTable.USERS);
						}
					}
				} else if (ConsignmentStatus.PENDING_APPROVAL_FROM_CUSTOMS.getCode() == current_consignment_response
						.getConsignmentStatus()) {
					logger.info("Custom PENDING_APPROVAL_FROM_CUSTOMS Notification:::with current_status : "
							+ current_consignment_response.getConsignmentStatus());
					port.setArrivalPort(current_consignment_response.getExpectedArrivalPort());
					port.setPortAddress(current_consignment_response.getPortAddress());
					port.setUserTypeId(7);

					Generic_Response_Notification generic_Response_Notification = userFeignClient
							.getUserIDByPortAddress(port);
					logger.info("generic_Response_Notification::::::::" + generic_Response_Notification);

					List<RegisterationUser> registerationUserList = generic_Response_Notification.getData();
					if (registerationUserList == null || registerationUserList.isEmpty()) {
						logger.info("no user found in this port/address");
					} else {
						for (RegisterationUser registerationUser : registerationUserList) {
							UserProfile userProfile_generic_Response_Notification = new UserProfile();
							userProfile_generic_Response_Notification = userProfileRepository
									.getByUserId(registerationUser.getId());
							placeholderMap.put("<First name>",
									userProfile_generic_Response_Notification.getFirstName());
							placeholderMap.put("<Txn id>", current_consignment_response.getTxnId());

							// Notification sent to custom - based on port/address
							emailUtil.saveNotification("Pending_approval_from_customs_Email_Message",
									userProfile_generic_Response_Notification, consignmentUpdateRequest.getFeatureId(),
									Features.CONSIGNMENT, SubFeatures.ACCEPT, payloadTxnId,
									current_consignment_response.getTxnId(), placeholderMap, null, "Custom",
									ReferTable.USERS);

						}
					}
				}
			} else {
				logger.info("Reject_CEIRAuthority:::with current_status : "
						+ current_consignment_response.getConsignmentStatus());

				placeholderMap.put("<First name>", userProfile.getFirstName());
				placeholderMap.put("<Txn id>", current_consignment_response.getTxnId());
				placeholderMap.put("<Reason>", current_consignment_response.getRemarks());
				emailUtil.saveNotification("Consignment_Reject_CEIRAuthority_Email_Message", userProfile,
						consignmentUpdateRequest.getFeatureId(), Features.CONSIGNMENT, SubFeatures.REJECT, payloadTxnId,
						current_consignment_response.getTxnId(), placeholderMap, null, "Importer", ReferTable.USERS);

				// Delete Tac if available in pending_tac_approval_db.
//				cleanFromPendingTacApprovalDb(consignmentMgmt);//Commented by Ravi and action moved before consignment save
			}

			logger.info("Consignment status have Update SuccessFully." + payloadTxnId);
			return new GenricResponse(0, "Consignment status have Update SuccessFully.", payloadTxnId);
		} else {
			logger.info("Consignment status Update have failed." + payloadTxnId);
			return new GenricResponse(2, "Consignment status Update have failed.", payloadTxnId);
		}
	}

	public GenricResponse ceirCustomStatus(ConsignmentMgmt consignmentMgmt,
			ConsignmentUpdateRequest consignmentUpdateRequest, UserProfile userProfile, int action) {
		String payloadTxnId = consignmentUpdateRequest.getTxnId();
		if (!StateMachine.isConsignmentStatetransitionAllowed("CUSTOM", consignmentMgmt.getConsignmentStatus())) {
			logger.info("state transition is not allowed." + payloadTxnId);
			return new GenricResponse(3, "state transition is not allowed.", payloadTxnId);
		}

		Map<String, String> placeholderMap = new HashMap<>();

		WebActionDb webActionDb = new WebActionDb();
		webActionDb.setFeature(WebActionDbFeature.CONSIGNMENT.getName());
		webActionDb.setState(WebActionDbState.INIT.getCode());
		webActionDb.setTxnId(payloadTxnId);

		/*
		 * lock.lock(); logger.info("lock taken by thread : " +
		 * Thread.currentThread().getName());
		 */
		if (action == 0) {
			// Check if someone else taken the same action on consignment.
			ConsignmentMgmt consignmentMgmtTemp = consignmentRepository.getByTxnId(consignmentMgmt.getTxnId());
			if (ConsignmentStatus.APPROVED.getCode() == consignmentMgmtTemp.getConsignmentStatus()) {
				String message = "Any other user have taken the same action on the consignment [" + payloadTxnId + "]";
				logger.info(message);
				return new GenricResponse(10, "", message, payloadTxnId);
			}

			consignmentMgmt.setConsignmentStatus(ConsignmentStatus.APPROVED.getCode());
			consignmentMgmt.setTaxPaidStatus(TaxStatus.TAX_PAID.getCode());

			// Delete Tac if available in pending_tac_approval_db.
//			cleanFromPendingTacApprovalDb(consignmentMgmt);

			webActionDb.setSubFeature(WebActionDbSubFeature.APPROVE.getName());

		} else {
			// Check if someone else taken the same action on consignment.
			ConsignmentMgmt consignmentMgmtTemp = consignmentRepository.getByTxnId(consignmentMgmt.getTxnId());
			if (ConsignmentStatus.REJECTED_BY_CUSTOMS.getCode() == consignmentMgmtTemp.getConsignmentStatus()) {
				String message = "Any other user have taken the same action on the consignment [" + payloadTxnId + "]";
				logger.info(message);
				return new GenricResponse(10, "", message, payloadTxnId);
			}

			consignmentMgmt.setConsignmentStatus(ConsignmentStatus.REJECTED_BY_CUSTOMS.getCode());
			consignmentMgmt.setRemarks(consignmentUpdateRequest.getRemarks());

			webActionDb.setSubFeature(WebActionDbSubFeature.REJECT.getName());
		}
		consignmentMgmt.setCustomID(consignmentUpdateRequest.getUserId());
		consignmentMgmt.setFeatureId(consignmentUpdateRequest.getFeatureId());
		consignmentMgmt.setRoleType(consignmentUpdateRequest.getRoleType());
		// Delete Tac if available in pending_tac_approval_db.
		if (cleanFromPendingTacApprovalDb(consignmentMgmt.getTxnId(), consignmentMgmt.getUserId()))
			consignmentMgmt.setPendingTacApprovedByCustom("Y");
		else
			consignmentMgmt.setPendingTacApprovedByCustom("N");

		if (consignmentTransaction.executeUpdateStatus(consignmentUpdateRequest, consignmentMgmt, webActionDb)) {
			ConsignmentMgmt current_consignment_response = consignmentRepository.getByTxnId(payloadTxnId);

			if (action == 0) {
				if (ConsignmentStatus.APPROVED.getCode() == current_consignment_response.getConsignmentStatus()) {
					logger.info("Notification sent to Importer:::with current_status : "
							+ current_consignment_response.getConsignmentStatus());

					placeholderMap.put("<First name>", userProfile.getFirstName());
					placeholderMap.put("<Txn id>", current_consignment_response.getTxnId());

					emailUtil.saveNotification("Consignment_Approved_CustomImporter_Email_Message", userProfile,
							consignmentUpdateRequest.getFeatureId(), Features.CONSIGNMENT, SubFeatures.ACCEPT,
							consignmentUpdateRequest.getTxnId(), current_consignment_response.getTxnId(),
							placeholderMap, null, "Importer", ReferTable.USERS);

					logger.info("Custom Approved:::with current_status: "
							+ current_consignment_response.getConsignmentStatus());
					Generic_Response_Notification generic_Response_Notification = userFeignClient
							.ceirInfoByUserTypeId(8);

					logger.info("generic_Response_Notification:::::::: " + generic_Response_Notification);

					List<RegisterationUser> registerationUserList = generic_Response_Notification.getData();
					if (registerationUserList == null || registerationUserList.isEmpty()) {
						logger.info("no user found");
					} else {

						for (RegisterationUser registerationUser : registerationUserList) {
							UserProfile userProfile_generic_Response_Notification = new UserProfile();
							userProfile_generic_Response_Notification = userProfileRepository
									.getByUserId(registerationUser.getId());

							placeholderMap.put("<First name>",
									userProfile_generic_Response_Notification.getFirstName());
							placeholderMap.put("<Txn id>", current_consignment_response.getTxnId());

							emailUtil.saveNotification("Consignment_Approved_CustomCEIRAdmin_Email_Message",
									userProfile_generic_Response_Notification, consignmentUpdateRequest.getFeatureId(),
									Features.CONSIGNMENT, SubFeatures.ACCEPT, consignmentUpdateRequest.getTxnId(),
									current_consignment_response.getTxnId(), placeholderMap, null, "CEIRAdmin",
									ReferTable.USERS);
						}
					}
				}
			} else {
				logger.info("Consignment_Rejected_Customwith current_status :  "
						+ current_consignment_response.getConsignmentStatus());

				placeholderMap.put("<First name>", userProfile.getFirstName());
				placeholderMap.put("<Txn id>", current_consignment_response.getTxnId());

				emailUtil.saveNotification("Consignment_Rejected_Custom_Email_Message", userProfile,
						consignmentUpdateRequest.getFeatureId(), Features.CONSIGNMENT, SubFeatures.REJECT,
						consignmentUpdateRequest.getTxnId(), current_consignment_response.getTxnId(), placeholderMap,
						null, "Importer", ReferTable.USERS);

				// Delete Tac if available in pending_tac_approval_db.
//				cleanFromPendingTacApprovalDb(consignmentMgmt);//comment by Ravi and move before operation consignment save
			}

			logger.info("Consignment status have Update SuccessFully." + payloadTxnId);
			return new GenricResponse(0, "Consignment status have Update SuccessFully.", payloadTxnId);
		} else {
			logger.info("Consignment status Update have failed." + payloadTxnId);
			return new GenricResponse(2, "Consignment status Update have failed.", payloadTxnId);
		}
	}

	public GenricResponse ceirSystemStatus(ConsignmentMgmt consignmentMgmt, UserProfile userProfile,
			ConsignmentUpdateRequest consignmentUpdateRequest, UserProfile ceirUserProfile, int consignmentStatus,
			int action) {
		String payloadTxnId = consignmentUpdateRequest.getTxnId();
		List<RawMail> rawMails = new LinkedList<>();

		if (!StateMachine.isConsignmentStatetransitionAllowed(CEIRSYSTEM, consignmentMgmt.getConsignmentStatus())) {
			logger.info("state transition is not allowed." + payloadTxnId);
			return new GenricResponse(3, "state transition is not allowed.", payloadTxnId);
		}

		Map<String, String> placeholderMap = new HashMap<>();

		/*
		 * lock.lock(); logger.info("lock taken by thread : " +
		 * Thread.currentThread().getName());
		 */
		// 0 - approve 1-Reject
		if (action == 0) {
			// Check if someone else taken the same action on consignment.
			ConsignmentMgmt consignmentMgmtTemp = consignmentRepository.getByTxnId(consignmentMgmt.getTxnId());
			if (ConsignmentStatus.PROCESSING.getCode() == consignmentMgmtTemp.getConsignmentStatus()) {
				String message = "Any other user have taken the same action on the consignment [" + payloadTxnId + "]";
				logger.info(message);
				return new GenricResponse(10, "", message, payloadTxnId);
			}
			consignmentMgmt.setConsignmentStatus(ConsignmentStatus.PROCESSING.getCode());
		} else if (action == 1) {
			// Check if someone else taken the same action on consignment.
			ConsignmentMgmt consignmentMgmtTemp = consignmentRepository.getByTxnId(consignmentMgmt.getTxnId());
			if (ConsignmentStatus.REJECTED_BY_SYSTEM.getCode() == consignmentMgmtTemp.getConsignmentStatus()) {
				String message = "Any other user have taken the same action on the consignment [" + payloadTxnId + "]";
				logger.info(message);
				return new GenricResponse(10, "", message, payloadTxnId);
			}

			consignmentMgmt.setConsignmentStatus(ConsignmentStatus.REJECTED_BY_SYSTEM.getCode());
			if (cleanFromPendingTacApprovalDb(consignmentMgmt.getTxnId(), consignmentMgmt.getUserId()))
				consignmentMgmt.setPendingTacApprovedByCustom("Y");
			else
				consignmentMgmt.setPendingTacApprovedByCustom("N");
		} else if (action == 2) {
			// Check if someone else taken the same action on consignment.
			ConsignmentMgmt consignmentMgmtTemp = consignmentRepository.getByTxnId(consignmentMgmt.getTxnId());
			if (ConsignmentStatus.PENDING_APPROVAL_FROM_CEIR_AUTHORITY.getCode() == consignmentMgmtTemp
					.getConsignmentStatus()) {
				String message = "Any other user have taken the same action on the consignment [" + payloadTxnId + "]";
				logger.info(message);
				return new GenricResponse(10, "", message, payloadTxnId);
			}

			consignmentMgmt.setConsignmentStatus(ConsignmentStatus.PENDING_APPROVAL_FROM_CEIR_AUTHORITY.getCode());

		} else {
			String message = "Not a valid action to take on the consignment [" + payloadTxnId + "]";
			logger.info(message);
			return new GenricResponse(11, "", message, payloadTxnId);
		}
		consignmentMgmt.setFeatureId(consignmentUpdateRequest.getFeatureId());
		consignmentMgmt.setRoleType(consignmentUpdateRequest.getRoleType());

		if (consignmentTransaction.executeUpdateStatusConsignmentForCeirSystem(consignmentMgmt, null)) {
			ConsignmentMgmt current_consignment_response = consignmentRepository.getByTxnId(payloadTxnId);
			if (action == 2) {
				logger.info("CONSIGNMENT_PROCESS_SUCCESS_TO_IMPORTER_MAIL :  "
						+ current_consignment_response.getConsignmentStatus());

				placeholderMap.put("<First name>", userProfile.getFirstName());
				placeholderMap.put("<Txn id>", current_consignment_response.getTxnId());

				if (ConsignmentStatus.PENDING_APPROVAL_FROM_CEIR_AUTHORITY.getCode() == current_consignment_response
						.getConsignmentStatus()) {
					rawMails.add(new RawMail("CONSIGNMENT_PROCESS_SUCCESS_TO_IMPORTER_MAIL", userProfile,
							consignmentUpdateRequest.getFeatureId(), Features.CONSIGNMENT, SubFeatures.ACCEPT,
							consignmentUpdateRequest.getTxnId(), current_consignment_response.getTxnId(),
							placeholderMap, ReferTable.USERS, consignmentUpdateRequest.getRoleType(), "Importer"));

					Generic_Response_Notification generic_Response_Notification = userFeignClient
							.ceirInfoByUserTypeId(8);

					logger.info("generic_Response_Notification::::::::" + generic_Response_Notification);

					List<RegisterationUser> registerationUserList = generic_Response_Notification.getData();

					if (registerationUserList == null || registerationUserList.isEmpty()) {
						logger.info("no user found");
					} else {

						for (RegisterationUser registerationUser : registerationUserList) {
							UserProfile userProfile_generic_Response_Notification = new UserProfile();
							userProfile_generic_Response_Notification = userProfileRepository
									.getByUserId(registerationUser.getId());
							logger.info(" firstName :::::" + userProfile_generic_Response_Notification.getFirstName());

							placeholderMap = new HashMap<String, String>();
							placeholderMap.put("<First name>",
									userProfile_generic_Response_Notification.getFirstName());
							placeholderMap.put("<Txn id>", current_consignment_response.getTxnId());

							rawMails.add(new RawMail("CONSIGNMENT_PROCESS_SUCCESS_TO_CEIR_MAIL",
									userProfile_generic_Response_Notification, consignmentUpdateRequest.getFeatureId(),
									Features.CONSIGNMENT, SubFeatures.ACCEPT, consignmentUpdateRequest.getTxnId(),
									current_consignment_response.getTxnId(), placeholderMap, ReferTable.USERS,
									consignmentUpdateRequest.getRoleType(), "CEIRAdmin"));
						}
					}

					logger.info("No. of mail sent : " + rawMails.size());
					emailUtil.saveNotification(rawMails);
				}
			} else if (action == 1) {
				logger.info("CONSIGNMENT_PROCESS_FAILED_TO_IMPORTER_MAIL :  "
						+ current_consignment_response.getConsignmentStatus());

				placeholderMap.put("<First name>", userProfile.getFirstName());
				placeholderMap.put("<Txn id>", current_consignment_response.getTxnId());

				if (ConsignmentStatus.REJECTED_BY_SYSTEM.getCode() == current_consignment_response
						.getConsignmentStatus()) {
					rawMails.add(new RawMail("CONSIGNMENT_PROCESS_FAILED_TO_IMPORTER_MAIL", userProfile,
							consignmentUpdateRequest.getFeatureId(), Features.CONSIGNMENT, SubFeatures.REJECT,
							consignmentUpdateRequest.getTxnId(), current_consignment_response.getTxnId(),
							placeholderMap, ReferTable.USERS, consignmentUpdateRequest.getRoleType(), "Importer"));

					logger.info("No. of mail sent : " + rawMails.size());
					emailUtil.saveNotification(rawMails);
				}
			} else {
				logger.info("Don't take any action for mail.");
			}

			logger.info("Consignment status have Update SuccessFully." + payloadTxnId);
			return new GenricResponse(0, "Consignment status have Update SuccessFully.", payloadTxnId);
		} else {
			logger.info("Consignment status Update have failed." + payloadTxnId);
			return new GenricResponse(2, "Consignment status Update have failed.", payloadTxnId);
		}
	}

	public GenricResponse ceirDRTStatus(ConsignmentMgmt consignmentMgmt, UserProfile userProfile,
			ConsignmentUpdateRequest consignmentUpdateRequest, UserProfile ceirUserProfile, int action) {
		String payloadTxnId = consignmentUpdateRequest.getTxnId();
		if (!StateMachine.isConsignmentStatetransitionAllowed("DRT", consignmentMgmt.getConsignmentStatus())) {
			logger.info("state transition is not allowed." + payloadTxnId);
			return new GenricResponse(3, "state transition is not allowed.", payloadTxnId);
		}
		WebActionDb webActionDb = null;
		Integer nextStatus = null;
		GenricResponse_Class response = null;
		Map<String, String> placeholderMap = new HashMap<>();

		/*
		 * lock.lock(); logger.info("lock taken by thread : " +
		 * Thread.currentThread().getName());
		 */
		if (action == 0) {
			// Check if someone else taken the same action on consignment.
			ConsignmentMgmt consignmentMgmtTemp = consignmentRepository.getByTxnId(consignmentMgmt.getTxnId());
			if (ConsignmentStatus.PENDING_APPROVAL_FROM_CUSTOMS.getCode() == consignmentMgmtTemp.getConsignmentStatus()
					|| ConsignmentStatus.APPROVED.getCode() == consignmentMgmtTemp.getConsignmentStatus()) {
				String message = "Any other user have taken the same action on the consignment [" + payloadTxnId + "]";
				logger.info(message);
				return new GenricResponse(10, "", message, payloadTxnId);
			}

			response = userFeignClient.usertypeStatus(7);
			logger.info("FEIGN : response for usertypeStatus " + response);
			if (response.getErrorCode() == 200) {
				nextStatus = ConsignmentStatus.PENDING_APPROVAL_FROM_CUSTOMS.getCode();
			} else {
				nextStatus = ConsignmentStatus.APPROVED.getCode();
			}
			consignmentMgmt.setConsignmentStatus(nextStatus);
			consignmentMgmt.setDrtID(consignmentUpdateRequest.getUserId());
		} else {
			// Check if someone else taken the same action on consignment.
			ConsignmentMgmt consignmentMgmtTemp = consignmentRepository.getByTxnId(consignmentMgmt.getTxnId());
			if (ConsignmentStatus.REJECTED_BY_DRT.getCode() == consignmentMgmtTemp.getConsignmentStatus()) {
				String message = "Any other user have taken the same action on the consignment [" + payloadTxnId + "]";
				logger.info(message);
				return new GenricResponse(10, "", message, payloadTxnId);
			}

			// Check if this feature is supported in current period.
			response = userFeignClient.usertypeStatus(21);
			logger.info("FEIGN : response for validatePeriod " + response);
			if (response.getErrorCode() == 200) {
				nextStatus = ConsignmentStatus.REJECTED_BY_DRT.getCode();
			} else {
				logger.info("Operation not allowed." + consignmentUpdateRequest.getTxnId());
				return new GenricResponse(5, "Operation not allowed.", consignmentUpdateRequest.getTxnId());
			}

			consignmentMgmt.setConsignmentStatus(nextStatus);
			consignmentMgmt.setDrtID(consignmentUpdateRequest.getUserId());
			consignmentMgmt.setRemarks(consignmentUpdateRequest.getRemarks());

			webActionDb = new WebActionDb();
			webActionDb.setFeature(WebActionDbFeature.CONSIGNMENT.getName());
			webActionDb.setState(WebActionDbState.INIT.getCode());
			webActionDb.setTxnId(consignmentMgmt.getTxnId());
			webActionDb.setSubFeature(WebActionDbSubFeature.REJECT.getName());
			// Delete Tac if available in pending_tac_approval_db.
			if (cleanFromPendingTacApprovalDb(consignmentMgmt.getTxnId(), consignmentMgmt.getUserId()))
				consignmentMgmt.setPendingTacApprovedByCustom("Y");
			else
				consignmentMgmt.setPendingTacApprovedByCustom("N");
		}

		consignmentMgmt.setFeatureId(consignmentUpdateRequest.getFeatureId());
		consignmentMgmt.setRoleType(consignmentUpdateRequest.getRoleType());

		if (consignmentTransaction.executeUpdateStatus(consignmentUpdateRequest, consignmentMgmt, webActionDb)) {
			ConsignmentMgmt current_consignment_response = consignmentRepository.getByTxnId(payloadTxnId);

			if (action == 0) {
				logger.info("Consignment_Approved_DRTImporter_Email_Message :  "
						+ current_consignment_response.getConsignmentStatus());
				if (ConsignmentStatus.APPROVED.getCode() == current_consignment_response.getConsignmentStatus()) {
					logger.info("Consignment_Approved_DRTImporter_Email_Message : "
							+ current_consignment_response.getConsignmentStatus());
					placeholderMap.put("<First name>", userProfile.getFirstName());
					placeholderMap.put("<Txn id>", current_consignment_response.getTxnId());

					emailUtil.saveNotification("Consignment_Approved_DRTImporter_Email_Message", userProfile,
							consignmentUpdateRequest.getFeatureId(), Features.CONSIGNMENT, SubFeatures.ACCEPT,
							consignmentUpdateRequest.getTxnId(), current_consignment_response.getTxnId(),
							placeholderMap, null, "Importer", ReferTable.USERS);
				}
			} else {
				logger.info("Consignment_Rejected_DRT_Email_Message : "
						+ current_consignment_response.getConsignmentStatus());
				placeholderMap.put("<First name>", userProfile.getFirstName());
				placeholderMap.put("<Txn id>", current_consignment_response.getTxnId());
				emailUtil.saveNotification("'Consignment_Rejected_DRT_Email_Message ", userProfile,
						consignmentUpdateRequest.getFeatureId(), Features.CONSIGNMENT, SubFeatures.REJECT,
						consignmentUpdateRequest.getTxnId(), current_consignment_response.getTxnId(), placeholderMap,
						null, "Importer", ReferTable.USERS);

				// Delete Tac if available in pending_tac_approval_db.
//				cleanFromPendingTacApprovalDb(consignmentMgmt);//commented by Ravi and moved up before consignment save
			}

			logger.info("Consignment status have Update SuccessFully." + payloadTxnId);
			return new GenricResponse(0, "Consignment status have Update SuccessFully.", payloadTxnId);
		} else {
			logger.info("Consignment status Update have failed." + payloadTxnId);
			return new GenricResponse(2, "Consignment status Update have failed.", payloadTxnId);
		}
	}

	public boolean nothingInFilter(FilterRequest filterRequest) {
		// Clean
		if (filterRequest.getStartDate().isEmpty()) {
			filterRequest.setStartDate(null);
		}
		if (filterRequest.getEndDate().isEmpty()) {
			filterRequest.setEndDate(null);
		}

		if (filterRequest.getTxnId().isEmpty()) {
			filterRequest.setTxnId(null);
		}

		// Logic
		if (Objects.nonNull(filterRequest.getStartDate())) {
			return Boolean.FALSE;
		}
		if (Objects.nonNull(filterRequest.getEndDate())) {
			return Boolean.FALSE;
		}

		if (Objects.nonNull(filterRequest.getTxnId())) {
			return Boolean.FALSE;
		}

		if (Objects.nonNull(filterRequest.getDisplayName()) && !filterRequest.getDisplayName().isEmpty()
				&& !filterRequest.getDisplayName().equalsIgnoreCase("null")
				&& !filterRequest.getDisplayName().equalsIgnoreCase("undefined")) {
			return Boolean.FALSE;
		}
		if (Objects.nonNull(filterRequest.getConsignmentStatus())) {
			return Boolean.FALSE;
		}
		if (Objects.nonNull(filterRequest.getFilteredUserType()) && !filterRequest.getFilteredUserType().isEmpty()) {
			return Boolean.FALSE;
		}
		if (Objects.nonNull(filterRequest.getQuantity()) && !filterRequest.getQuantity().isEmpty()) {
			return Boolean.FALSE;
		}
		if (Objects.nonNull(filterRequest.getDeviceQuantity()) && !filterRequest.getDeviceQuantity().isEmpty()) {
			return Boolean.FALSE;
		}
		if (Objects.nonNull(filterRequest.getSupplierName()) && !filterRequest.getSupplierName().isEmpty()) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	public boolean cleanFromPendingTacApprovalDb(ConsignmentMgmt consignmentMgmt) {
		// Delete Tac if available in pending_tac_approval_db.
		FilterRequest filterRequest = new FilterRequest().setTxnId(consignmentMgmt.getTxnId());
		filterRequest.setUserId(consignmentMgmt.getUserId());
		if (pendingTacApprovedImpl.findByTxnId(filterRequest).getErrorCode() == 0) {
			logger.info("Tac related to the consignment with txn_id [" + consignmentMgmt.getTxnId()
					+ "] found in pending_tac_approval_db");
			pendingTacApprovedImpl.deletePendingApproval(filterRequest);
//			consignmentMgmt.setPendingTacApprovedByCustom("Y");
		} else {
			logger.info("No tac for the consignment with txn_id [" + consignmentMgmt.getTxnId() + "] is pending.");
//			consignmentMgmt.setPendingTacApprovedByCustom("N");
		}

		return Boolean.TRUE;
	}

	public boolean cleanFromPendingTacApprovalDb(String txnId, Integer userId) {
		// Delete Tac if available in pending_tac_approval_db.
		FilterRequest filterRequest = new FilterRequest().setTxnId(txnId);
		filterRequest.setUserId(userId);
		if (pendingTacApprovedImpl.findByTxnId(filterRequest).getErrorCode() == 0) {
			logger.info("Tac related to the consignment with txn_id [" + txnId + "] found in pending_tac_approval_db");
			pendingTacApprovedImpl.deletePendingApproval(filterRequest);
			return Boolean.TRUE;
		} else {
			logger.info("No tac for the consignment with txn_id [" + txnId + "] is pending.");
			return Boolean.FALSE;
		}
	}
}