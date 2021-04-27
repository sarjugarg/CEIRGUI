package com.gl.ceir.config.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gl.ceir.config.exceptions.ResourceServicesException;
import com.gl.ceir.config.model.ConsignmentMgmt;
import com.gl.ceir.config.model.EndUserDB;
import com.gl.ceir.config.model.GenricResponse;
import com.gl.ceir.config.model.RegularizeDeviceDb;
import com.gl.ceir.config.model.StockMgmt;
import com.gl.ceir.config.model.StolenandRecoveryMgmt;
import com.gl.ceir.config.model.TypeApprovedDb;
import com.gl.ceir.config.repository.ConsignmentRepository;
import com.gl.ceir.config.repository.EndUserDbRepository;
import com.gl.ceir.config.repository.RegularizedDeviceDbRepository;
import com.gl.ceir.config.repository.StockManagementRepository;
import com.gl.ceir.config.repository.StolenAndRecoveryRepository;
import com.gl.ceir.config.repository.TypeApproveRepository;

@Service
public class TestServiceImpl {

	private static final Logger logger = LogManager.getLogger(TestServiceImpl.class);
	
	@Autowired
	private ConsignmentRepository consignmentRepository;

	@Autowired
	StockManagementRepository stockManagementRepository;
	
	@Autowired
	StolenAndRecoveryRepository stolenAndRecoveryRepository;
	
	@Autowired
	RegularizedDeviceDbRepository regularizedDeviceDbRepository;
	
	@Autowired
	EndUserDbRepository endUserDbRepository;
	
	@Autowired
	TypeApproveRepository typeApproveRepository;
	
	public GenricResponse updateStatus(int featureId, String txnId, int status) {
		try {
			if(featureId == 3)	{
				ConsignmentMgmt consignmentMgmt = consignmentRepository.getByTxnId(txnId);
				consignmentMgmt.setConsignmentStatus(status);
				consignmentRepository.save(consignmentMgmt);
				return new GenricResponse(0, "Status of Consignment have been updated successfully.", txnId);
			}else if(featureId == 4) {
				StockMgmt stockMgmt = stockManagementRepository.getByTxnId(txnId);
				stockMgmt.setStockStatus(status);
				stockManagementRepository.save(stockMgmt);
				return new GenricResponse(0, "Status of Stock have been updated successfully.", txnId);
			}else if(featureId == 5) {
				StolenandRecoveryMgmt stolenandRecoveryMgmt = stolenAndRecoveryRepository.getByTxnId(txnId);
				stolenandRecoveryMgmt.setFileStatus(status);
				stolenAndRecoveryRepository.save(stolenandRecoveryMgmt);
				return new GenricResponse(0, "Status of Stolen/Recovery have been updated successfully.", txnId);
			}else if(featureId == 7) {
				StolenandRecoveryMgmt stolenandRecoveryMgmt = stolenAndRecoveryRepository.getByTxnId(txnId);
				stolenandRecoveryMgmt.setFileStatus(status);
				stolenAndRecoveryRepository.save(stolenandRecoveryMgmt);
				return new GenricResponse(0, "Status of Stolen/Recovery have been updated successfully.", txnId);
			}else if(featureId == 11) {
				TypeApprovedDb typeApprovedDb = typeApproveRepository.getByTxnId(txnId);
				typeApprovedDb.setApproveStatus(status);
				typeApproveRepository.save(typeApprovedDb);
				return new GenricResponse(0, "Status of Stolen/Recovery have been updated successfully.", txnId);
			}else if(featureId == 12) {
				RegularizeDeviceDb regularizeDeviceDb = regularizedDeviceDbRepository.getByTxnId(txnId);
				regularizeDeviceDb.setStatus(status);
				regularizedDeviceDbRepository.save(regularizeDeviceDb);
				return new GenricResponse(0, "Status of regularize device have been updated successfully.", txnId);
			}else if(featureId == 19) {
				EndUserDB endUserDB = endUserDbRepository.getByTxnId(txnId);
				endUserDB.setStatus(status);
				endUserDbRepository.save(endUserDB);
				return new GenricResponse(0, "Status of enduser have been updated successfully.", txnId);
			}else {
				return new GenricResponse(0, "Feature [" + featureId + "] is not supported.", txnId);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}
}
