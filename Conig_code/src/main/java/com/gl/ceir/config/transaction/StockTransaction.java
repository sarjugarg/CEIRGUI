package com.gl.ceir.config.transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gl.ceir.config.EmailSender.EmailUtil;
import com.gl.ceir.config.EmailSender.MailSubject;
import com.gl.ceir.config.model.AuditTrail;
import com.gl.ceir.config.model.RawMail;
import com.gl.ceir.config.model.StockMgmt;
import com.gl.ceir.config.model.User;
import com.gl.ceir.config.model.UserProfile;
import com.gl.ceir.config.model.WebActionDb;
import com.gl.ceir.config.model.constants.Features;
import com.gl.ceir.config.model.constants.ReferTable;
import com.gl.ceir.config.model.constants.SubFeatures;
import com.gl.ceir.config.repository.AuditTrailRepository;
import com.gl.ceir.config.repository.StockManagementRepository;
import com.gl.ceir.config.repository.UserRepository;
import com.gl.ceir.config.repository.WebActionDbRepository;

@Component
@Transactional(rollbackOn = Exception.class)
public class StockTransaction {

	private static final Logger logger = LogManager.getLogger(StockTransaction.class);

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

	public boolean executeRegisterStock(StockMgmt stockMgmt, WebActionDb webActionDb, UserProfile userProfile,
			boolean isStockAssignRequest, boolean isAnonymousUpload) {

		boolean queryStatus = Boolean.FALSE;

		logger.info("Going to save webActionDb [" + webActionDb + "]");
		webActionDbRepository.save(webActionDb);
		logger.info("Stock [" + stockMgmt.getTxnId() + "] saved in web_action_db.");

		logger.info("Going to save Stock [" + stockMgmt + "]");
		stockManagementRepository.save(stockMgmt);
		logger.info("Stock [" + stockMgmt.getTxnId() + "] saved in stock_mgmt.");

		/*
		 * auditTrailRepository.save(new AuditTrail(stockMgmt.getUser().getId(),
		 * userProfile.getUser().getUsername(),
		 * userProfile.getUser().getUsertype().getId(),
		 * userProfile.getUser().getUsertype().getUsertypeName(), 4, Features.STOCK,
		 * SubFeatures.UPLOAD, "", stockMgmt.getTxnId()));
		 */
		logger.info("Stock [" + stockMgmt.getTxnId() + "] saved in audit_trail.");

		if(isStockAssignRequest) {
			User user = userRepository.getById(stockMgmt.getUserId());
			logger.info(user);
			Map<String, String> placeholderMap = new HashMap<>();
			placeholderMap.put("<First name>", user.getUserProfile().getFirstName());
			placeholderMap.put("<Txn id>", stockMgmt.getTxnId());

			if(emailUtil.saveNotification("ASSIGN_STOCK", 
					userProfile, 
					4,
					Features.STOCK,
					SubFeatures.ASSIGN,
					stockMgmt.getTxnId(),
					stockMgmt.getTxnId(),
					placeholderMap,
					stockMgmt.getRoleType(),
					null,
					ReferTable.USERS)) {
				logger.info("Notification have been saved.");
			}else {
				logger.info("Notification have been not saved.");
			}
		}else if(isAnonymousUpload) {
			List<RawMail> rawMails = new ArrayList<RawMail>();

			// Send notification to the anonymous user if mail is provided.
			if(Objects.nonNull(userProfile.getEmail()) && !userProfile.getEmail().isEmpty()) {
				Map<String, String> placeholderMapForAnonymousUser = new HashMap<String, String>();
				placeholderMapForAnonymousUser.put("<Txn id>", stockMgmt.getTxnId());

				rawMails.add(new RawMail("MAIL_TO_ANONYMOUS_ON_STOCK_UPLOAD", userProfile, 
						4, Features.STOCK, 
						SubFeatures.REGISTER, 
						stockMgmt.getTxnId(), 
						stockMgmt.getTxnId(),  
						placeholderMapForAnonymousUser, ReferTable.USERS, stockMgmt.getRoleType(), "End User"));
			}

			if(emailUtil.saveNotification(rawMails)) {
				logger.info("Notifications [" + rawMails.size() + "] have been saved.");
			}else {
				logger.info("Notification have been not saved.");
			}
		}

		queryStatus = Boolean.TRUE;
		return queryStatus;
	}


	public boolean executeRegisterStock(StockMgmt stockMgmt, WebActionDb
			webActionDb) {

		boolean queryStatus = Boolean.FALSE;

		logger.info("Going to save webActionDb [" + webActionDb + "]");
		webActionDbRepository.save(webActionDb); logger.info("Stock [" +
				stockMgmt.getTxnId() + "] saved in web_action_db.");

		logger.info("Going to save Stock [" + stockMgmt + "]");
		stockManagementRepository.save(stockMgmt); logger.info("Stock [" +
				stockMgmt.getTxnId() + "] saved in stock_mgmt.");

		User user = userRepository.getById(stockMgmt.getUserId());

		/*
		 * auditTrailRepository.save(new AuditTrail(stockMgmt.getUser().getId(),
		 * user.getUsername(), user.getUsertype().getId(),
		 * user.getUsertype().getUsertypeName(), 4, Features.STOCK, SubFeatures.UPLOAD,
		 * "", stockMgmt.getTxnId(),stockMgmt.getRoleType()));
		 */

		logger.info("Stock [" + stockMgmt.getTxnId() + "] saved in audit_trail.");

		queryStatus = Boolean.TRUE; return queryStatus; }


	public boolean executeDeleteStock(StockMgmt stockMgmt, WebActionDb webActionDb) {
		boolean queryStatus = Boolean.FALSE;
		webActionDbRepository.save(webActionDb);
		logger.info("Stock [" + stockMgmt.getTxnId() + "] saved in web_action_db.");

		stockManagementRepository.save(stockMgmt);
		logger.info("Stock [" + stockMgmt.getTxnId() + "] saved in stock_mgmt.");

		/*
		 * auditTrailRepository.save(new AuditTrail(stockMgmt.getUser().getId(), "", 0L,
		 * "", 0L, Features.STOCK, SubFeatures.DELETE, "", stockMgmt.getTxnId()));
		 * logger.info("Stock [" + stockMgmt.getTxnId() + "] saved in audit_trail.");
		 */

		queryStatus = Boolean.TRUE;
		return queryStatus;
	}

	public boolean executeUpdateStock(StockMgmt stockMgmt, WebActionDb webActionDb) {
		boolean queryStatus = Boolean.FALSE;
		webActionDbRepository.save(webActionDb);
		logger.info("Stock [" + stockMgmt.getTxnId() + "] saved in web_action_db.");

		stockManagementRepository.save(stockMgmt);
		logger.info("Stock [" + stockMgmt.getTxnId() + "] saved in stock_mgmt.");

		/*
		 * auditTrailRepository.save(new AuditTrail(stockMgmt.getUser().getId(), "", 0L,
		 * "", 0L, Features.STOCK, SubFeatures.UPDATE, "", stockMgmt.getTxnId()));
		 * logger.info("Stock [" + stockMgmt.getTxnId() + "] saved in audit_trail.");
		 */
		queryStatus = Boolean.TRUE;
		return queryStatus;
	}

	public boolean updateStatusWithHistory(StockMgmt stockMgmt, WebActionDb webActionDb) {
		boolean status = Boolean.FALSE;
		if( Objects.nonNull( webActionDb )) {
			webActionDbRepository.save(webActionDb);
			logger.info("Stock [" + stockMgmt.getTxnId() + "] saved in web_action_db.");
		}
		
		stockManagementRepository.save(stockMgmt);
		logger.info("Stock [" + stockMgmt.getTxnId() + "] saved in stock_mgmt.");

		/*
		 * auditTrailRepository.save(new AuditTrail(stockMgmt.getUser().getId(), "", 0L,
		 * "", 0L, Features.STOCK, SubFeatures.UPDATE, "", stockMgmt.getTxnId()));
		 * logger.info("Stock [" + stockMgmt.getTxnId() + "] saved in audit_trail.");
		 */

		status = Boolean.TRUE;

		return status;
	}
}
