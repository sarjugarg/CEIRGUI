package com.gl.ceir.config.transaction;

import java.util.Objects;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gl.ceir.config.EmailSender.EmailUtil;
import com.gl.ceir.config.model.StolenIndividualUserDB;
import com.gl.ceir.config.model.StolenOrganizationUserDB;
import com.gl.ceir.config.model.StolenandRecoveryMgmt;
import com.gl.ceir.config.model.WebActionDb;
import com.gl.ceir.config.model.constants.Alerts;
import com.gl.ceir.config.repository.AuditTrailRepository;
import com.gl.ceir.config.repository.StockManagementRepository;
import com.gl.ceir.config.repository.StolenAndRecoveryRepository;
import com.gl.ceir.config.repository.StolenIndividualUserRepository;
import com.gl.ceir.config.repository.StolenOrganizationUserRepository;
import com.gl.ceir.config.repository.UserRepository;
import com.gl.ceir.config.repository.WebActionDbRepository;
import com.gl.ceir.config.service.impl.AlertServiceImpl;

@Component
@Transactional(rollbackOn = Exception.class)
public class StolenAndRecoveryTransaction {

	private static final Logger logger = LogManager.getLogger(StolenAndRecoveryTransaction.class);

	@Autowired
	WebActionDbRepository webActionDbRepository;

	@Autowired
	AuditTrailRepository auditTrailRepository;

	@Autowired
	StockManagementRepository stockManagementRepository;
	
	@Autowired
	StolenIndividualUserRepository stolenIndividualUserRepository;

	@Autowired
	StolenOrganizationUserRepository stolenOrganizationUserRepository;
	
	@Autowired
	UserRepository userRepository;

	@Autowired	
	EmailUtil emailUtil;

	@Autowired
	AlertServiceImpl alertServiceImpl;

	@Autowired
	StolenAndRecoveryRepository stolenAndRecoveryRepository;

	public synchronized boolean executeUploadDetails(StolenandRecoveryMgmt stolenandRecoveryMgmt, WebActionDb webActionDb) {
		boolean status = Boolean.FALSE;
		
		StolenIndividualUserDB stolenIndividualUserDB = stolenandRecoveryMgmt.getStolenIndividualUserDB();
		StolenOrganizationUserDB stolenOrganizationUserDB = stolenandRecoveryMgmt.getStolenOrganizationUserDB();

		if(Objects.isNull(stolenandRecoveryMgmt.getOperatorTypeId()))
//			alertServiceImpl.raiseAnAlert(Alerts.alert002, stolenandRecoveryMgmt.getUserId().intValue());

		stolenandRecoveryMgmt.setStolenIndividualUserDB(null);
		stolenandRecoveryMgmt.setStolenOrganizationUserDB(null);
		
		stolenAndRecoveryRepository.save(stolenandRecoveryMgmt);
		logger.info("Saved in stolenand_recovery_mgmt_db" + stolenandRecoveryMgmt);

		if(Objects.nonNull(stolenIndividualUserDB)) {
			Long id = stolenIndividualUserRepository.maxOfId();
			if(Objects.isNull(id)) {
				id = 1L;
			}else {
				id = id + 1;
			}
			
			logger.info("max of id for stolen_individual_user_db [" + id + "]");
			
			// stolenIndividualUserDB.setId(id);
			
			stolenIndividualUserRepository.save(stolenIndividualUserDB);
			logger.info("Saved in stolen_individual_user_db" + stolenandRecoveryMgmt);
		}

		if(Objects.nonNull(stolenOrganizationUserDB)) {
			
			Long id = stolenOrganizationUserRepository.maxOfId();
			
			if(Objects.isNull(id)) {
				id = 1L;
			}else {
				id = id + 1;
			}
			
			logger.info("max of id for stolen_organization_user_db [" + id + "]");
			
			// stolenOrganizationUserDB.setId(id);
			stolenOrganizationUserRepository.save(stolenOrganizationUserDB);
			logger.info("Saved in stolen_organization_user_db" + stolenandRecoveryMgmt);
		}
		
		webActionDbRepository.save(webActionDb);
		logger.info("Saved in web_action_db " + stolenandRecoveryMgmt);

		status = Boolean.TRUE;
		return status;
	}

	public boolean updateStatusWithHistory(StolenandRecoveryMgmt stolenandRecoveryMgmt) {
		boolean status = Boolean.FALSE;

		stolenAndRecoveryRepository.save(stolenandRecoveryMgmt);

		status = Boolean.TRUE;
		return status;
	}
}
