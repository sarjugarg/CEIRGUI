package com.gl.ceir.config.transaction;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gl.ceir.config.EmailSender.EmailUtil;
import com.gl.ceir.config.model.EndUserDB;
import com.gl.ceir.config.model.RegularizeDeviceDb;
import com.gl.ceir.config.model.WebActionDb;
import com.gl.ceir.config.repository.AuditTrailRepository;
import com.gl.ceir.config.repository.EndUserDbRepository;
import com.gl.ceir.config.repository.RegularizedDeviceDbRepository;
import com.gl.ceir.config.repository.StockManagementRepository;
import com.gl.ceir.config.repository.StolenAndRecoveryRepository;
import com.gl.ceir.config.repository.UserRepository;
import com.gl.ceir.config.repository.WebActionDbRepository;
import com.gl.ceir.config.service.impl.AlertServiceImpl;

@Component
@Transactional(rollbackOn = Exception.class)
public class RegularizeDeviceTransaction {
	
	private static final Logger logger = LogManager.getLogger(RegularizeDeviceTransaction.class);
	
	@Autowired
	WebActionDbRepository webActionDbRepository;
	
	@Autowired
	AuditTrailRepository auditTrailRepository;

	@Autowired
	StockManagementRepository stockManagementRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired	
	EmailUtil emailUtil;
	
	@Autowired
	AlertServiceImpl alertServiceImpl;
	
	@Autowired
	StolenAndRecoveryRepository stolenAndRecoveryRepository;
	
	@Autowired
	EndUserDbRepository endUserDbRepository;
	
	@Autowired
	RegularizedDeviceDbRepository regularizedDeviceDbRepository;
	
	public boolean executeSaveDevices(List<WebActionDb> webActionDbs, EndUserDB endUserDB) {
		boolean status = Boolean.FALSE;
		webActionDbRepository.saveAll(webActionDbs);
		logger.info("Batch update in web_action_db. " + webActionDbs );

		endUserDbRepository.save(endUserDB);
		logger.info("End user have been saved with its devices. " + endUserDB);

		status = Boolean.TRUE;
		return status;
	}
	
	public boolean executeSaveDevices(List<WebActionDb> webActionDbs, List<RegularizeDeviceDb> regularizeDeviceDbs) {
		boolean status = Boolean.FALSE;
		webActionDbRepository.saveAll(webActionDbs);
		logger.info("Batch update in web_action_db. " + webActionDbs );
		
		regularizedDeviceDbRepository.saveAll(regularizeDeviceDbs);
		logger.info("Regularized devices have been saved. " + regularizeDeviceDbs);

		status = Boolean.TRUE;
		return status;
	}
}
