package com.gl.ceir.config.transaction;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gl.ceir.config.model.AuditTrail;
import com.gl.ceir.config.model.EndUserDB;
import com.gl.ceir.config.model.VisaUpdateDb;
import com.gl.ceir.config.model.WebActionDb;
import com.gl.ceir.config.model.constants.Features;
import com.gl.ceir.config.model.constants.SubFeatures;
import com.gl.ceir.config.repository.AuditTrailRepository;
import com.gl.ceir.config.repository.EndUserDbRepository;
import com.gl.ceir.config.repository.UpdateVisaRepository;
import com.gl.ceir.config.repository.WebActionDbRepository;

@Component
@Transactional(rollbackOn = Exception.class)
public class EndUserTransaction {
	
	private static final Logger logger = LogManager.getLogger(EndUserTransaction.class);
	
	@Autowired
	WebActionDbRepository webActionDbRepository;
	
	@Autowired
	AuditTrailRepository auditTrailRepository;
	
	@Autowired
	EndUserDbRepository endUserDbRepository;
	
	@Autowired
	UpdateVisaRepository updateVisaRepository;
	
	public boolean executeUpdateVisa(EndUserDB endUserDB) {
		boolean status = Boolean.FALSE;

		endUserDbRepository.save(endUserDB);
		logger.info("Visa of user have been updated succesfully." +  endUserDB);


		/*
		 * auditTrailRepository.save(new AuditTrail(endUserDB.getId(), "", 0L, "", 0L,
		 * Features.UPDATE_VISA, SubFeatures.UPDATE, "")); logger.info("Consignment [" +
		 * endUserDB.getTxnId() + "] saved in audit_trail.");
		 */

		status = Boolean.TRUE;
		return status;
	}
	
	public boolean addUpdateVisaRequest(VisaUpdateDb visaUpdateDb,EndUserDB endUserdb,WebActionDb webActionDb) {
		boolean status = Boolean.FALSE;
		updateVisaRepository.save(visaUpdateDb);
		endUserDbRepository.save(endUserdb);
		logger.info("update Visa request of user have been updated succesfully.");
		status = Boolean.TRUE;
		return status;
	}
	
	public boolean updateStatusWithHistory(EndUserDB endUserDB) {
		boolean status = Boolean.FALSE;

		endUserDbRepository.save(endUserDB);
		logger.info("End_user [" + endUserDB.getTxnId() + "] saved in end_userdb.");


		auditTrailRepository.save(new AuditTrail(0, "", 0L, "", 0L, Features.MANAGE_USER, SubFeatures.ACCEPT_REJECT, ""));
		logger.info("End_user [" + endUserDB.getTxnId() + "] saved in audit_trail.");

		status = Boolean.TRUE;

		return status;
	}
}
