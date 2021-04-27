package com.gl.ceir.config.transaction;

import java.util.Objects;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gl.ceir.config.model.AuditTrail;
import com.gl.ceir.config.model.ConsignmentMgmt;
import com.gl.ceir.config.model.ConsignmentUpdateRequest;
import com.gl.ceir.config.model.WebActionDb;
import com.gl.ceir.config.model.constants.Features;
import com.gl.ceir.config.model.constants.SubFeatures;
import com.gl.ceir.config.repository.AuditTrailRepository;
import com.gl.ceir.config.repository.ConsignmentRepository;
import com.gl.ceir.config.repository.WebActionDbRepository;

@Component
@Transactional(rollbackOn = Exception.class)
public class ConsignmentTransaction {

	private static final Logger logger = LogManager.getLogger(ConsignmentTransaction.class);

	@Autowired
	WebActionDbRepository webActionDbRepository;

	@Autowired
	private ConsignmentRepository consignmentRepository;

	@Autowired
	AuditTrailRepository auditTrailRepository;

	public boolean executeRegisterConsignment(ConsignmentMgmt consignmentMgmt, WebActionDb webActionDb) {
		boolean queryStatus = Boolean.FALSE;
		webActionDbRepository.save(webActionDb);
		logger.info(String.format("Consignment [{0}] saved in web_action_db.", consignmentMgmt.getTxnId()));

		consignmentRepository.save(consignmentMgmt);
		logger.info("Consignment [" + consignmentMgmt.getTxnId() + "] saved in consigment_mgmt_db.");

		auditTrailRepository.save(new AuditTrail(consignmentMgmt.getUser().getId(), consignmentMgmt.getUser().getUsername(), 
				Long.valueOf(consignmentMgmt.getUserTypeId()), consignmentMgmt.getUserType(), 
				Long.valueOf(consignmentMgmt.getFeatureId()), Features.CONSIGNMENT, 
				SubFeatures.REGISTER, "", consignmentMgmt.getTxnId(),consignmentMgmt.getRoleType(),consignmentMgmt.getPublicIp(),consignmentMgmt.getBrowser()));
		logger.info("Consignment [" + consignmentMgmt.getTxnId() + "] saved in audit_trail.");

		queryStatus = Boolean.TRUE;
		return queryStatus;
	}

	public boolean executeUpdateConsignment(ConsignmentMgmt consignmentMgmt, WebActionDb webActionDb) {
		boolean queryStatus = Boolean.FALSE;
		webActionDbRepository.save(webActionDb);
		logger.info("Consignment [" + consignmentMgmt.getTxnId() + "] saved in web_action_db.");

		consignmentRepository.save(consignmentMgmt);
		logger.info("Consignment [" + consignmentMgmt.getTxnId() + "] updated in consigment_mgmt_db.");
		logger.info("request:"+consignmentMgmt +":::::usertypeid::::::::"+consignmentMgmt.getUserTypeId());
		auditTrailRepository.save(new AuditTrail(consignmentMgmt.getUser().getId(),consignmentMgmt.getUser().getUsername(),
				Long.valueOf(consignmentMgmt.getUserTypeId()), 
				consignmentMgmt.getUserType(),
				Long.valueOf(consignmentMgmt.getFeatureId()),
				Features.CONSIGNMENT, SubFeatures.UPDATE, "", consignmentMgmt.getTxnId(),consignmentMgmt.getRoleType(),consignmentMgmt.getPublicIp(),consignmentMgmt.getBrowser()));
		logger.info("Consignment [" + consignmentMgmt.getTxnId() + "] saved in audit_trail.");

		queryStatus = Boolean.TRUE;
		return queryStatus;
	}

	public boolean executeDeleteConsignment(ConsignmentMgmt consignmentMgmt, WebActionDb webActionDb) {
		boolean queryStatus = Boolean.FALSE;
		webActionDbRepository.save(webActionDb);
		logger.info("Consignment [" + consignmentMgmt.getTxnId() + "] saved in web_action_db.");

		consignmentRepository.save(consignmentMgmt);
		logger.info("Consignment [" + consignmentMgmt.getTxnId() + "] updated in consigment_mgmt_db.");

		auditTrailRepository.save(new AuditTrail(consignmentMgmt.getUser().getId(), consignmentMgmt.getUserName(), Long.valueOf(consignmentMgmt.getUserTypeId()), consignmentMgmt.getUserType(), Long.valueOf(consignmentMgmt.getFeatureId()), 
				Features.CONSIGNMENT, SubFeatures.DELETE, "", consignmentMgmt.getTxnId(),consignmentMgmt.getRoleType(),consignmentMgmt.getPublicIp(),consignmentMgmt.getBrowser()));
		logger.info("Consignment [" + consignmentMgmt.getTxnId() + "] saved in audit_trail.");

		queryStatus = Boolean.TRUE;
		return queryStatus;
	}

	public boolean executeUpdateStatusConsignmentForCeirSystem(ConsignmentMgmt consignmentMgmt, WebActionDb webActionDb) {
		// NOTE : This function is used only for CEIR SYSTEM.
		boolean queryStatus = Boolean.FALSE;

		if(Objects.nonNull(webActionDb)) {
			webActionDbRepository.save(webActionDb);
			logger.info("Consignment [" + consignmentMgmt.getTxnId() + "] saved in webaction_db.");	
		}

		consignmentRepository.save(consignmentMgmt);
		logger.info("Consignment [" + consignmentMgmt.getTxnId() + "] saved in consigment_mgmt_db.");

		queryStatus = Boolean.TRUE;
		return queryStatus;
	}

	public boolean executeUpdateStatus(ConsignmentUpdateRequest consignmentUpdateRequest, ConsignmentMgmt consignmentMgmt, 
			WebActionDb webActionDb) {
		boolean queryStatus = Boolean.FALSE;
		String action = consignmentUpdateRequest.getAction() == 0 ? "Approve" : "Reject";

		if(Objects.nonNull(webActionDb)) {
			webActionDbRepository.save(webActionDb);
			logger.info("Consignment [" + consignmentMgmt.getTxnId() + "] saved in webaction_db.");	
		}

		consignmentRepository.save(consignmentMgmt);
		logger.info("Consignment [" + consignmentMgmt.getTxnId() + "] saved in consigment_mgmt_db.");

		auditTrailRepository.save(new AuditTrail(
				consignmentUpdateRequest.getUserId(), 
				consignmentUpdateRequest.getUserName(), 
				Long.valueOf(consignmentUpdateRequest.getUserTypeId()), 
				consignmentUpdateRequest.getUserType(), 
				Long.valueOf(consignmentUpdateRequest.getFeatureId())
				, Features.CONSIGNMENT, 
				action, "", 
				consignmentMgmt.getTxnId(),
				consignmentUpdateRequest.getRoleType(),consignmentUpdateRequest.getPublicIp(),consignmentUpdateRequest.getBrowser()));
		logger.info("Consignment [" + consignmentMgmt.getTxnId() + "] saved in audit_trail for "
				+ "action[" + action 
				+ "] usertype[" + consignmentUpdateRequest.getUserType() + "].");

		queryStatus = Boolean.TRUE;
		return queryStatus;
	}
}
