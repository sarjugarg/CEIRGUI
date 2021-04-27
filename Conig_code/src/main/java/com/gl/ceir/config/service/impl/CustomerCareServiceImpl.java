package com.gl.ceir.config.service.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gl.ceir.config.EmailSender.EmailUtil;
import com.gl.ceir.config.configuration.PropertiesReader;
import com.gl.ceir.config.exceptions.ResourceServicesException;
import com.gl.ceir.config.factory.CustomerCareFactory;
import com.gl.ceir.config.factory.CustomerCareRepo;
import com.gl.ceir.config.factory.CustomerCareTarget;
import com.gl.ceir.config.model.ConsignmentMgmt;
import com.gl.ceir.config.model.CustomerCareDeviceState;
import com.gl.ceir.config.model.CustomerCareRequest;
import com.gl.ceir.config.model.DeviceDuplicateDb;
import com.gl.ceir.config.model.DeviceNullDb;
import com.gl.ceir.config.model.DeviceUsageDb;
import com.gl.ceir.config.model.FilterRequest;
import com.gl.ceir.config.model.GenricResponse;
import com.gl.ceir.config.model.GreylistDb;
import com.gl.ceir.config.model.PolicyBreachNotification;
import com.gl.ceir.config.model.RegularizeDeviceDb;
import com.gl.ceir.config.model.TypeApprovedDb;
import com.gl.ceir.config.model.constants.GenericMessageTags;
import com.gl.ceir.config.repository.BlackListRepository;
import com.gl.ceir.config.repository.BlacklistImeiDbRepository;
import com.gl.ceir.config.repository.DeviceDuplicateDbRepository;
import com.gl.ceir.config.repository.DeviceNullDbRepository;
import com.gl.ceir.config.repository.DeviceUsageDbRepository;
import com.gl.ceir.config.repository.GreyListRepository;
import com.gl.ceir.config.repository.PolicyBreachNotificationRepository;
import com.gl.ceir.config.repository.TypeApproveRepository;
import com.gl.ceir.config.repository.VipListRepository;
import com.gl.ceir.config.util.DateUtil;
import com.gl.ceir.config.util.Utility;

@Service
public class CustomerCareServiceImpl {

	private static final Logger logger = LogManager.getLogger(CustomerCareServiceImpl.class);

	@Autowired
	PropertiesReader propertiesReader;

	@Autowired
	Utility utility;

	@Autowired	
	EmailUtil emailUtil;
	
	@Autowired
	DateUtil dateUtil;

	@Autowired
	UserStaticServiceImpl userStaticServiceImpl;

	@Autowired 
	CustomerCareFactory customerCareFactory;

	@Autowired
	PolicyBreachNotificationRepository policyBreachNotificationRepository;

	@Autowired
	DeviceUsageDbRepository deviceUsageDbRepository;

	@Autowired
	DeviceDuplicateDbRepository deviceDuplicateDbRepository;

	@Autowired
	DeviceNullDbRepository deviceNullDbRepository;

	@Autowired
	ConsignmentServiceImpl consignmentServiceImpl;

	@Autowired
	StockServiceImpl stockServiceImpl;

	@Autowired
	RegularizedDeviceServiceImpl regularizedDeviceServiceImpl;

	@Autowired
	TypeApprovedDbServiceImpl typeApprovedDbServiceImpl;
	
	@Autowired
	GreylistServiceImpl greylistServiceImpl;

	public GenricResponse getAll(CustomerCareRequest customerCareRequest, String listType) {
		String imei = customerCareRequest.getImei();

		try {
//			if(Objects.nonNull(imei) 
//					&& "IMEI".equalsIgnoreCase(customerCareRequest.getDeviceIdType())) {
			if(Objects.nonNull(imei)) {
				return fetchDetailsOfImei(imei, "0", customerCareRequest.getDeviceIdType(), listType);

			}else if(Objects.nonNull(customerCareRequest.getMsisdn()) || !customerCareRequest.getMsisdn().equals("") ){
				DeviceUsageDb deviceUsageDb = deviceUsageDbRepository.getByImei(imei);

				if(Objects.isNull(deviceUsageDb)) {
					return new GenricResponse(2, GenericMessageTags.NO_DATA.getTag(), GenericMessageTags.NO_DATA.getMessage(), null);
				}else {
					return fetchDetailsOfImei(deviceUsageDb.getImei(), "0", customerCareRequest.getDeviceIdType(), listType);
				}
			}else {
				return new GenricResponse(1, GenericMessageTags.INVALID_REQUEST.getMessage(), "", null);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	public GenricResponse getAllV2(CustomerCareRequest customerCareRequest, String listType) {
		String msisdn = null;
		String imei = customerCareRequest.getImei();
		String deviceIdType = customerCareRequest.getDeviceIdType();

		try {
			msisdn = customerCareRequest.getMsisdn();
		}catch (NumberFormatException e) {
			logger.error("Msisdn is not cast into the long.[" + customerCareRequest.getMsisdn() + "]. So, msisdn=null");
			msisdn = null;
		}

		try {

			if(Objects.nonNull(imei) && "IMEI".equalsIgnoreCase(deviceIdType) && Objects.nonNull(msisdn) ) {
				return imeiAndMsisdnValidation(imei, msisdn, listType);
			}else if(Objects.nonNull(imei) && "IMEI".equalsIgnoreCase(deviceIdType) && Objects.isNull(msisdn)){
				return imeiValidation(imei, listType);
			}else if(Objects.nonNull(msisdn)){
				return msisdnValidation(msisdn, listType);
			}else {
				return new GenricResponse(1, GenericMessageTags.INVALID_REQUEST.getMessage(), "", null);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	public GenricResponse getByTxnId(CustomerCareDeviceState customerCareDeviceState) {
		try {
			if(Objects.isNull(customerCareDeviceState) ||
					Objects.isNull(customerCareDeviceState.getFeatureId())
					|| Objects.isNull(customerCareDeviceState.getName())) {

				logger.info(GenericMessageTags.NULL_REQ.getMessage());
				return new GenricResponse(1, GenericMessageTags.NULL_REQ.getTag(), GenericMessageTags.NULL_REQ.getMessage(), "");
			}

			Object objectBytxnId = null;

			Object repository = null;
			// Getting repository from factory.
			if(customerCareDeviceState.getFeatureId() == 0)
				repository = customerCareFactory.getRepoByName(customerCareDeviceState.getName());
			else
				repository = customerCareFactory.getRepoByFeatureId(customerCareDeviceState.getFeatureId());

			// If factory has a valid repo.
			if(Objects.isNull(repository)) {
				logger.info(GenericMessageTags.FEATURE_NOT_SUPPORTED.getMessage() +" txnId [" + customerCareDeviceState.getTxnId() + "]");
				return new GenricResponse(2, GenericMessageTags.FEATURE_NOT_SUPPORTED.getTag(), GenericMessageTags.FEATURE_NOT_SUPPORTED.getMessage(), customerCareDeviceState.getTxnId());
			}
			if(repository instanceof CustomerCareRepo) {
				CustomerCareRepo customerCareRepo = (CustomerCareRepo)repository;
				objectBytxnId = customerCareRepo.getByTxnId(customerCareDeviceState.getTxnId());
				
				if(objectBytxnId instanceof ConsignmentMgmt) {
					ConsignmentMgmt consignmentObj = (ConsignmentMgmt)objectBytxnId;
					consignmentServiceImpl.setInterp(consignmentObj);
					objectBytxnId = consignmentObj;
				}else if(objectBytxnId instanceof RegularizeDeviceDb) {
					RegularizeDeviceDb regularizeDeviceDb = (RegularizeDeviceDb)objectBytxnId;
					regularizedDeviceServiceImpl.setInterp(regularizeDeviceDb);
					objectBytxnId = regularizeDeviceDb;
				}else {
					logger.info("customerCareRepo.getByTxnId returned non ConsignmentMgmt");
				}
			}else {
				if(repository instanceof BlackListRepository) {
					BlackListRepository blackListRepository = (BlackListRepository)repository;
					objectBytxnId = blackListRepository.findByImei(customerCareDeviceState.getImei());
					logger.info("Black List Data "+objectBytxnId);
				}
				else if(repository instanceof GreyListRepository) {
					GreyListRepository greyListRepository = (GreyListRepository)repository;
					objectBytxnId = greyListRepository.findByImei(customerCareDeviceState.getImei());
					GreylistDb greylistDb = (GreylistDb)objectBytxnId;
					greylistServiceImpl.setInterp(greylistDb);
					greylistDb.setCreatedOn(dateUtil.formatChangerLocaldateTime(greylistDb.getCreatedOn(), "yyyy-MM-dd HH:mm"));
					logger.info(greylistDb);
				}
				else if(repository instanceof DeviceDuplicateDbRepository) {
					DeviceDuplicateDbRepository deviceDuplicateDbRepository = (DeviceDuplicateDbRepository)repository;
					objectBytxnId = deviceDuplicateDbRepository.findByImei(customerCareDeviceState.getImei());
				}
//				else if(repository instanceof GsmaBlacklistRepository) {
//					GsmaBlacklistRepository gsmaBlacklistRepository = (GsmaBlacklistRepository)repository;
//					objectBytxnId = gsmaBlacklistRepository.getByDeviceid(customerCareDeviceState.getImei());
//				}
				else if(repository instanceof BlacklistImeiDbRepository) {
					BlacklistImeiDbRepository blacklistImeiDbRepository = (BlacklistImeiDbRepository)repository;
					objectBytxnId = blacklistImeiDbRepository.getByDeviceidAndBlacklistStatus(customerCareDeviceState.getImei(),"Yes");
				}
				else if(repository instanceof TypeApproveRepository) { 
					TypeApproveRepository typeApproveRepository = (TypeApproveRepository)repository; 
					objectBytxnId = typeApproveRepository.getByTac(customerCareDeviceState.getImei().substring(0,8));
					TypeApprovedDb typeApprovedDb = (TypeApprovedDb)objectBytxnId;
					typeApprovedDbServiceImpl.setBrandInterp(typeApprovedDb);
					typeApprovedDbServiceImpl.setModelInterp(typeApprovedDb);
					objectBytxnId = typeApprovedDb;
				}
				else if(repository instanceof VipListRepository ) {
					VipListRepository vipListRepository = (VipListRepository) repository;
					objectBytxnId = vipListRepository.findByImeiMsisdnIdentityImei(customerCareDeviceState.getImei());
				}
				else {
					logger.info(GenericMessageTags.FEATURE_NOT_SUPPORTED.getMessage() +" txnId [" + customerCareDeviceState.getTxnId() + "]");
					return new GenricResponse(2, GenericMessageTags.FEATURE_NOT_SUPPORTED.getTag(), GenericMessageTags.FEATURE_NOT_SUPPORTED.getMessage(), customerCareDeviceState.getTxnId());
				}
			}

			if(Objects.isNull(objectBytxnId)) {
				logger.info(GenericMessageTags.INVALID_TXN_ID.getMessage() +" txnId [" + customerCareDeviceState.getTxnId() + "]");
				return new GenricResponse(3, GenericMessageTags.INVALID_TXN_ID.getTag(), GenericMessageTags.INVALID_TXN_ID.getMessage(), customerCareDeviceState.getTxnId());
			}else {

				return new GenricResponse(0, GenericMessageTags.SUCCESS.getMessage(), "",  objectBytxnId);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	public Page<PolicyBreachNotification> viewPolicyBreachNotification(FilterRequest filterRequest, 
			Integer pageNo, Integer pageSize){
		try {
			Pageable pageable = PageRequest.of(pageNo, pageSize);

			if(Objects.nonNull(filterRequest.getImei())) {
				return policyBreachNotificationRepository.findByImei(filterRequest.getImei(), pageable);
			}
			else if(Objects.nonNull(filterRequest.getContactNumber())){
				return policyBreachNotificationRepository.findByContactNumber(filterRequest.getContactNumber(), pageable);
			}else {
				return new PageImpl<>(new ArrayList<PolicyBreachNotification>(1), pageable, 0L);
			}

		} catch (Exception e) {
			logger.error("Not Register Consignent="+e.getMessage());
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	private GenricResponse fetchDetailsOfImei(String imei, String msisdn, String listType) {
		List<String> list = null;
		List<CustomerCareDeviceState> customerCareDeviceStates = new LinkedList<>();

		if("device".equals(listType)) {
			list = customerCareFactory.deviceList;
		}else {
			list = customerCareFactory.stateList;
		}

		list.stream().forEach( o -> {
			CustomerCareTarget customerCareTarget = customerCareFactory.getObject(o);
			if(Objects.isNull(customerCareTarget)) {
				logger.info("Corresponding object of Db [" + o + "] is not defined in the factory ");
				return;
			}

			try {
				CustomerCareDeviceState customerCareDeviceState = new CustomerCareDeviceState();
				customerCareDeviceState.setMsisdn(msisdn);
				
//				customerCareDeviceStates.add(customerCareTarget.fetchDetailsByImei(imei, customerCareDeviceState));
				customerCareDeviceStates.add(customerCareTarget.fetchDetailsByImei(imei, customerCareDeviceState, listType.toLowerCase()));
				logger.info("Added object of Db [" + o + "] in customerCareDeviceStates");
				
			}catch (Exception e) {
				logger.error("Db [" + o + "] have some issue in fetching data for imei [" + imei + "]", e);
			}
		});

		return new GenricResponse(0, GenericMessageTags.SUCCESS.getMessage(), "", customerCareDeviceStates);

	}
	
	private GenricResponse fetchDetailsOfImei(String imei, String msisdn, String deviceIdType, String listType) {
		List<String> list = null;
		List<CustomerCareDeviceState> customerCareDeviceStates = new LinkedList<>();

		if("device".equals(listType)) {
			list = customerCareFactory.deviceList;
		}else {
			list = customerCareFactory.stateList;
		}

		list.stream().forEach( o -> {
			CustomerCareTarget customerCareTarget = customerCareFactory.getObject(o);
			if(Objects.isNull(customerCareTarget)) {
				logger.info("Corresponding object of Db [" + o + "] is not defined in the factory ");
				return;
			}

			try {
				CustomerCareDeviceState customerCareDeviceState = new CustomerCareDeviceState();
				customerCareDeviceState.setMsisdn(msisdn);
				
//				customerCareDeviceStates.add(customerCareTarget.fetchDetailsByImei(imei, customerCareDeviceState));
				customerCareDeviceStates.add(customerCareTarget.fetchDetailsByImei(imei, customerCareDeviceState, deviceIdType.toLowerCase()));
				logger.info("Added object of Db [" + o + "] in customerCareDeviceStates");
				
			}catch (Exception e) {
				logger.error("Db [" + o + "] have some issue in fetching data for imei [" + imei + "]", e);
			}
		});

		return new GenricResponse(0, GenericMessageTags.SUCCESS.getMessage(), "", customerCareDeviceStates);

	}

	private GenricResponse imeiAndMsisdnValidation(String imei, String msisdn, String listType) {
		logger.info("IMEI and MSISDN both are available in the request. imei [" + imei + "] msisdn [" + msisdn + "]");

		DeviceUsageDb deviceUsageDb = deviceUsageDbRepository.getByImeiAndMsisdn(imei, msisdn);
		logger.debug(deviceUsageDb);

		if(Objects.isNull(deviceUsageDb)) {
			logger.info("IMEI and MSISDN not found in device_usage_db. So, Check in device_duplicate DB. imei [" + imei + "] msisdn [" + msisdn + "]");

			DeviceDuplicateDb deviceDuplicateDb = deviceDuplicateDbRepository.findByImeiAndMsisdn(imei, msisdn);
			logger.debug(deviceDuplicateDb);

			if(Objects.isNull(deviceDuplicateDb)) {
				logger.info("IMEI and MSISDN are invalid tuple. So, returning error code[10] imei [" + imei + "] msisdn [" + msisdn + "]");
				return new GenricResponse(10, GenericMessageTags.INVALID_TUPLE_FOR_IMEI_AND_MSISDN.getTag(), 
						GenericMessageTags.INVALID_TUPLE_FOR_IMEI_AND_MSISDN.getMessage(), "");
			}else {
				logger.info("IMEI and MSISDN are valid tuple. imei [" + imei + "] msisdn [" + msisdn + "]");
				return fetchDetailsOfImei(imei, msisdn, listType);
			}
		}else {
			logger.info("IMEI and MSISDN are valid tuple. imei [" + imei + "] msisdn [" + msisdn + "]");
			return fetchDetailsOfImei(imei, msisdn, listType);	
		}
	}

	private GenricResponse imeiValidation(String imei, String listType) {
		logger.info("Only IMEI is available in the request. imei [" + imei + "]");

		List<DeviceDuplicateDb> deviceDuplicateDbs = deviceDuplicateDbRepository.findByImei(imei);
		logger.debug(deviceDuplicateDbs);

		if(deviceDuplicateDbs.isEmpty()) {
			logger.info("IMEI not found in device_duplicate_db. So, Check in device_usage_db. imei [" + imei + "]");

			DeviceUsageDb deviceUsageDb = deviceUsageDbRepository.getByImei(imei);	
			logger.debug(deviceUsageDb);

			if(Objects.nonNull(deviceUsageDb)) {
				String msisdn = deviceUsageDb.getMsisdn();
				logger.info("Found a valid msisdn[" + msisdn + "] in device_usage_db for imei[" + imei + "]");

				return fetchDetailsOfImei(imei, msisdn, listType);
			}else {
				logger.info("IMEI is invalid. So, returning error code[12] imei [" + imei + "]");
				return new GenricResponse(12, GenericMessageTags.INVALID_IMEI.getTag(), 
						GenericMessageTags.INVALID_IMEI.getMessage(), "");
			}
		}else if(deviceDuplicateDbs.size() == 1) {
			String msisdn = deviceDuplicateDbs.get(0).getMsisdn();
			logger.info("Found a valid msisdn[" + msisdn + "] in device_duplicate_db for imei[" + imei + "]");

			return fetchDetailsOfImei(imei, msisdn, listType);
		}else {
			logger.info("Found a more than one valid msisdn in device_duplicate_db for imei[" + imei + "]. So, returning code [3] with all possible imei's.");
			return new GenricResponse(3, "", "", deviceDuplicateDbs);
		}

	}

	private GenricResponse msisdnValidation(String msisdn, String listType) {
		logger.info("Only MSISDN is available in the request. msisdn [" + msisdn + "]");

		List<DeviceDuplicateDb> deviceDuplicateDbs = deviceDuplicateDbRepository.findByMsisdn(msisdn);
		logger.debug(deviceDuplicateDbs);

		if(deviceDuplicateDbs.isEmpty()) {
			logger.info("MSISDN not found in device_duplicate_db. So, Check in device_usage_db. imei [" + msisdn + "]");
			DeviceUsageDb deviceUsageDb = deviceUsageDbRepository.getByMsisdn(msisdn);	
			logger.debug(deviceUsageDb);

			if(Objects.nonNull(deviceUsageDb)) {
				String imei = deviceUsageDb.getImei();
				logger.info("Found a valid imei[" + imei + "] in device_usage_db for msisdn[" + msisdn + "]");

				return fetchDetailsOfImei(imei, msisdn, listType);
			}else {
				logger.info("MSISDN [" + msisdn + "] not found in device_usage_db. So, Checking in device_null_db.");

				DeviceNullDb deviceNullDb = deviceNullDbRepository.findByMsisdn(msisdn);
				if(Objects.nonNull(deviceNullDb)) {
					logger.info("MSISDN is not found in device_null_db. So, returning error code[11] msisdn [" + msisdn + "]");
					return new GenricResponse(11, GenericMessageTags.NO_IMEI_FOR_MSISDN.getTag(), 
							GenericMessageTags.NO_IMEI_FOR_MSISDN.getMessage(), "");
				}else {
					logger.info("MSISDN is invalid. So, returning error code[10] msisdn [" + msisdn + "]");
					return new GenricResponse(10, GenericMessageTags.INVALID_TUPLE_FOR_IMEI_AND_MSISDN.getTag(), 
							GenericMessageTags.INVALID_TUPLE_FOR_IMEI_AND_MSISDN.getMessage(), "");
				}
			}
		}else if(deviceDuplicateDbs.size() == 1) {
			return fetchDetailsOfImei(deviceDuplicateDbs.get(0).getImei(), msisdn, listType);
		}else {
			return new GenricResponse(3, "", "", deviceDuplicateDbs);
		}
	}
}