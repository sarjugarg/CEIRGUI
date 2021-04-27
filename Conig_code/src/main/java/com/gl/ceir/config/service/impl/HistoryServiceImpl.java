package com.gl.ceir.config.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
import com.gl.ceir.config.configuration.PropertiesReader;
import com.gl.ceir.config.exceptions.ResourceServicesException;
import com.gl.ceir.config.model.AuditTrail;
import com.gl.ceir.config.model.BlacklistDbHistory;
import com.gl.ceir.config.model.FilterRequest;
import com.gl.ceir.config.model.GreylistDbHistory;
import com.gl.ceir.config.model.NotiLogic;
import com.gl.ceir.config.model.Notification;
import com.gl.ceir.config.model.SearchCriteria;
import com.gl.ceir.config.model.SystemConfigurationDb;
import com.gl.ceir.config.model.constants.Datatype;
import com.gl.ceir.config.model.constants.SearchOperation;
import com.gl.ceir.config.repository.AuditTrailRepository;
import com.gl.ceir.config.repository.BlackListTrackDetailsRepository;
import com.gl.ceir.config.repository.GreyListTrackRepository;
import com.gl.ceir.config.repository.NotiLogicRepository;
import com.gl.ceir.config.repository.NotificationRepository;
import com.gl.ceir.config.repository.StockMgmtHistoryRepository;
import com.gl.ceir.config.repository.SystemConfigurationDbRepository;
import com.gl.ceir.config.specificationsbuilder.NotificationSpecificationBuilder;

@Service
public class HistoryServiceImpl {

	private static final Logger logger = LogManager.getLogger(HistoryServiceImpl.class);

	@Autowired
	SystemConfigurationDbRepository systemConfigurationDbRepository;

	@Autowired
	BlackListTrackDetailsRepository blackListTrackDetailsRepository;

	@Autowired
	GreyListTrackRepository greyListTrackRepository;


	@Autowired
	StockMgmtHistoryRepository stockMgmtHistoryRepository;

	@Autowired
	AuditTrailRepository auditTrailRepository;

	@Autowired
	NotificationRepository notificationRepository;

	@Autowired
	NotiLogicRepository notiLogicRepository;

	@Autowired
	PropertiesReader propertiesReader;


	public Page<BlacklistDbHistory> ViewAllBlackHistory(Integer pageNo, Integer pageSize){
		try {
			Pageable pageable = PageRequest.of(pageNo, pageSize);

			return blackListTrackDetailsRepository.findAll(pageable);
		} catch (Exception e) {
			logger.error("Not Register Consignent = " + e.getMessage());
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	public Page<GreylistDbHistory> ViewAllGreyHistory(Integer pageNo, Integer pageSize){
		try {
			Pageable pageable = PageRequest.of(pageNo, pageSize);

			return greyListTrackRepository.findAll(pageable);
		} catch (Exception e) {
			logger.error("Not Register Consignent="+e.getMessage());
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	public Page<AuditTrail> ViewAllAuditHistory(Integer pageNo, Integer pageSize){
		try {
			Pageable pageable = PageRequest.of(pageNo, pageSize);

			return auditTrailRepository.findAll(pageable);
		} catch (Exception e) {
			logger.error("Not Register Consignent="+e.getMessage());
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	public Page<Notification> ViewAllNotificationHistory(Integer pageNo, Integer pageSize){
		try {
			SystemConfigurationDb systemConfigurationDb = systemConfigurationDbRepository.getByTag(ConfigTags.page_size_for_Notification);

			int notiPageSize = Integer.parseInt(systemConfigurationDb.getValue());
			Pageable pageable = PageRequest.of(pageNo, notiPageSize);
			return notificationRepository.findAll(pageable);

		} catch (NumberFormatException e) {
			logger.error("Integer value is expected for tag [page_size_for_Notification] in system_configuration_db.");
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}catch (Exception e) {
			logger.error("Not Register Consignent = " + e.getMessage());
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	public Page<Notification> ViewAllNotificationHistory(Integer pageNo, Integer pageSize, FilterRequest filterRequest){
		try {
			SystemConfigurationDb systemConfigurationDb=systemConfigurationDbRepository.getByTag("TABLE_PAGE_LENGTH");
			logger.info("systemConfigurationDb.getValue()::: "+systemConfigurationDb.getValue());
			int defaultPagesize = Integer.parseInt(systemConfigurationDb.getValue());
			pageSize = 100;
			pageNo = 0;
			List<Notification> content = new ArrayList<>();
			Pageable pageable = PageRequest.of(pageNo, pageSize, new Sort(Sort.Direction.DESC, "modifiedOn"));
			Page<Notification> result =  null;

			List<NotiLogic> notiLogics = notiLogicRepository.getByUsertypeId(filterRequest.getUserTypeId());
			logger.info(notiLogics);

			NotificationSpecificationBuilder nsb = new NotificationSpecificationBuilder(propertiesReader.dialect);

			/*if("Custom".equalsIgnoreCase(filterRequest.getUserType())) {
				logger.info("skipping userid in where clause for usertype : " + filterRequest.getUserType());
				nsb.with(new SearchCriteria("receiverUserType", "Custom", SearchOperation.EQUALITY, Datatype.STRING));
			}else if("TRC".equalsIgnoreCase(filterRequest.getUserType())){
				logger.info("skipping userid in where clause for usertype : " + filterRequest.getUserType());
				nsb.with(new SearchCriteria("receiverUserType", "TRC", SearchOperation.EQUALITY, Datatype.STRING));
			}*/
			if(notiLogics.isEmpty() || Objects.isNull(notiLogics)) {
				logger.info("notiLogics is empty for userType[" + filterRequest.getUserType() + "]");
				if(Objects.nonNull(filterRequest.getUserId()))
					nsb.with(new SearchCriteria("userId", filterRequest.getUserId(), SearchOperation.EQUALITY, Datatype.STRING));

				nsb.with(new SearchCriteria("referTable", "END_USER", SearchOperation.NEGATION, Datatype.STRING));

				pageable = PageRequest.of(pageNo, defaultPagesize, new Sort(Sort.Direction.DESC, "modifiedOn"));
				result = notificationRepository.findAll(nsb.build(), pageable);
			}else {
				cherryPickNotification(pageNo, defaultPagesize, notiLogics, filterRequest, content);
				result = new PageImpl<>(content, pageable, defaultPagesize);
				logger.info("Final Content in notification : " + content);
			}
			
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	private void cherryPickNotification(int pageNo, int defaultPagesize, List<NotiLogic> notiLogics, FilterRequest filterRequest, List<Notification> content){

		Pageable pageable = PageRequest.of(pageNo, 100, new Sort(Sort.Direction.DESC, "modifiedOn"));
		Page<Notification> tempPage = notificationRepository.findAll(pageable);
		List<Notification> tempContent = tempPage.getContent();

		for(Notification notification : tempContent) {
			for(NotiLogic notiLogic : notiLogics) {
				if("END_USER".equalsIgnoreCase(notification.getReferTable())) {
					continue;
				}

				if(notiLogic.getFeatureId() == notification.getFeatureId().intValue()) {
					if("one".equalsIgnoreCase(notiLogic.getType())) {
						if(notification.getUserId().intValue() == filterRequest.getUserId()) {
							content.add(notification);
						}
					}else if("all".equalsIgnoreCase(notiLogic.getType())) {
						if(notiLogic.getUsertypeName().equals(notification.getReceiverUserType())) {
							content.add(notification);
						}
					}else {
						logger.warn("noti_logic don't have valid values.[" + notiLogic.getType() + "]");
					}
				}else {
					// logger.info("Noti id [" + notification.getFeatureId() + "] is not configured in table noti_logic.");
					continue;
				}
			}
			
			if(content.size() >= defaultPagesize) {
				break;
			}
		}

		if(content.size() >= defaultPagesize) {
			return;
		}else {
			pageNo++;
			logger.info("Reading page [" + pageNo + "] for notification.");
			cherryPickNotification(pageNo, defaultPagesize, notiLogics, filterRequest, content);
		}
	}
}