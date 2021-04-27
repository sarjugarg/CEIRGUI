package com.gl.ceir.config.service.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gl.ceir.config.EmailSender.EmailUtil;
import com.gl.ceir.config.configuration.PropertiesReader;
import com.gl.ceir.config.exceptions.ResourceServicesException;
import com.gl.ceir.config.model.ActionsConfigDb;
import com.gl.ceir.config.model.StateMgmtDb;
import com.gl.ceir.config.model.StatesInterpretationDb;
import com.gl.ceir.config.model.TableActions;
import com.gl.ceir.config.repository.ActionConfigRepository;
import com.gl.ceir.config.repository.StateMgmtRepository;
import com.gl.ceir.config.repository.StatesInterpretaionRepository;
import com.gl.ceir.config.util.Utility;

@Service
public class StateMgmtServiceImpl {

	private static final Logger logger = LogManager.getLogger(StateMgmtServiceImpl.class);

	@Autowired
	private StateMgmtRepository stateMgmtRepository;

	@Autowired
	private StatesInterpretaionRepository statesInterpretaionRepository; 

	@Autowired 
	ActionConfigRepository actionConfigRepository;

	@Autowired
	PropertiesReader propertiesReader;

	@Autowired
	Utility utility;

	@Autowired	
	EmailUtil emailUtil;
	
	public StateMgmtDb saveStateMgmt(StateMgmtDb stateMgmtDb){
		try {
			return stateMgmtRepository.save(stateMgmtDb);
		} catch (Exception e) {
			logger.info(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	/*
	 * 
	 * @future_scope : must use a join between state_mgmt_db and states_interpretation_db
	 */
	public List<StateMgmtDb> getByFeatureIdAndUserTypeId(Integer featureId, Integer userTypeId) {
		try {
			List<StateMgmtDb> stateMgmtDbsResult = new ArrayList<>();

			logger.info("Going to get states by featureId and usertypeId ");

			List<StateMgmtDb> stateMgmtDbs = stateMgmtRepository.getByFeatureIdAndUserTypeId(featureId, userTypeId);

			List<StatesInterpretationDb> statesInterpretationDbs = statesInterpretaionRepository.findByFeatureId(featureId);
			logger.debug(statesInterpretationDbs);

			for(StatesInterpretationDb statesInterpretationDb : statesInterpretationDbs) {

				for(StateMgmtDb stateMgmtDb : stateMgmtDbs) {
					if(stateMgmtDb.getState().equals(statesInterpretationDb.getState())) {
						stateMgmtDb.setInterp(statesInterpretationDb.getInterp());
						stateMgmtDbsResult.add(stateMgmtDb);
					}
				}
			}

			return stateMgmtDbsResult;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	public List<TableActions> getTableActionsByFeatureIdAndUserTypeId(Integer featureId, Integer userTypeId) {
		try {
			
			List<ActionsConfigDb> actionConfigDbs = null;
			
			TableActions tableActions = null;
			List<TableActions> tableActionsList = new LinkedList<>();

			logger.info("Going to get states by featureId and usertypeId ");

			List<StateMgmtDb> stateMgmtDbs = stateMgmtRepository.getByFeatureIdAndUserTypeId(featureId, userTypeId);
			
			logger.info("StateMgmtDb : For featureId [" + featureId + "] and userTypeId [ " + userTypeId + "] " + stateMgmtDbs);
			
			for(StateMgmtDb stateMgmtDb : stateMgmtDbs) {

				actionConfigDbs = actionConfigRepository.getByStateId(stateMgmtDb.getId());
				logger.info("actionConfigDbs : For id [" + stateMgmtDb.getId() + "] " + actionConfigDbs);
				
				tableActions = new TableActions();
				tableActions.setStateId(stateMgmtDb.getId());
				tableActions.setState(stateMgmtDb.getState());
				
				for(ActionsConfigDb actionConfigDb : actionConfigDbs) {

					switch(actionConfigDb.getAction()) {
					case "VIEW":
						tableActions.setView(actionConfigDb.getIsEnable());
						break;
					case "DOWNLOAD_ERROR_FILE":
						tableActions.setDownloadErrorFile(actionConfigDb.getIsEnable());
						break;
					case "EDIT":
						tableActions.setEdit(actionConfigDb.getIsEnable());
						break;
					case "DOWNLOAD_FILE":
						tableActions.setDownloadFile(actionConfigDb.getIsEnable());
						break;
					case "DELETE":
						tableActions.setDelete(actionConfigDb.getIsEnable());
						break;
					case "APPROVE":
						tableActions.setApprove(actionConfigDb.getIsEnable());
						break;
					case "REJECT":
						tableActions.setReject(actionConfigDb.getIsEnable());
						break;
					}
				}
				
				tableActionsList.add(tableActions);
			}

			return tableActionsList;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

}
