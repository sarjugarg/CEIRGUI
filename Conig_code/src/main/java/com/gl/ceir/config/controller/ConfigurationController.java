package com.gl.ceir.config.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gl.ceir.config.configuration.PropertiesReader;
import com.gl.ceir.config.model.AuditTrail;
import com.gl.ceir.config.model.FileDetails;
import com.gl.ceir.config.model.FilterRequest;
import com.gl.ceir.config.model.GenricResponse;
import com.gl.ceir.config.model.MessageConfigurationDb;
import com.gl.ceir.config.model.Notification;
import com.gl.ceir.config.model.PolicyConfigurationDb;
import com.gl.ceir.config.model.SystemConfigListDb;
import com.gl.ceir.config.model.SystemConfigurationDb;
import com.gl.ceir.config.model.constants.Features;
import com.gl.ceir.config.model.constants.SubFeatures;
import com.gl.ceir.config.repository.AuditTrailRepository;
import com.gl.ceir.config.service.impl.ConfigurationManagementServiceImpl;

import io.swagger.annotations.ApiOperation;

@RestController
public class ConfigurationController {

	private static final Logger logger = LogManager.getLogger(ConfigurationController.class);

	@Autowired
	ConfigurationManagementServiceImpl configurationManagementServiceImpl;

	@Autowired
	AuditTrailRepository auditTrailRepository;
	
	@Autowired
	PropertiesReader propertiesReader;

	@ApiOperation(value = "System Config view All Data", response = SystemConfigurationDb.class)
	@PostMapping("/system/viewAll")
	public MappingJacksonValue findSystemDetails() {

		logger.info("Request to get system all details");

		List<SystemConfigurationDb> pocessDetails = configurationManagementServiceImpl.getAllInfo();

		MappingJacksonValue mapping = new MappingJacksonValue(pocessDetails);

		logger.info("Response to send="+pocessDetails);

		return mapping;
	}

	@ApiOperation(value = "Paginated view of System Config.", response = SystemConfigurationDb.class)
	@PostMapping("/filter/system-configuration")
	public MappingJacksonValue paginatedViewOfSystemConfig(@RequestBody FilterRequest filterRequest,
			@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value = "file", defaultValue = "0") Integer file) {
		MappingJacksonValue mapping = null;
		logger.info("Paginated view of System Config " + filterRequest);
		if(file == 0) {
			Page<SystemConfigurationDb> page = configurationManagementServiceImpl.filterSystemConfiguration(filterRequest, pageNo, pageSize);
			mapping = new MappingJacksonValue(page);
			logger.info("Response to send = " + page);
		}else {
			FileDetails fileDetails = configurationManagementServiceImpl.exportFile_System(filterRequest);
			mapping = new MappingJacksonValue(fileDetails);
		}
		return mapping;
	}

	@ApiOperation(value = "System Config view Data using Tag", response = SystemConfigurationDb.class)
	@RequestMapping(path = "/system/viewTag", method = RequestMethod.POST)
	public MappingJacksonValue findSystemDetailsByTag(@RequestBody SystemConfigurationDb systemConfigurationDb) {

		logger.info("Details Get by system config Tag="+systemConfigurationDb);

		SystemConfigurationDb  pocessDetails = configurationManagementServiceImpl.findByTag(systemConfigurationDb);
		pocessDetails.setValue(pocessDetails.getValue().replace("$LOCAL_IP",
				propertiesReader.localIp) );
		MappingJacksonValue mapping = new MappingJacksonValue(pocessDetails);

		logger.info("Response to send="+pocessDetails);

		return mapping;
	}

	@ApiOperation(value = "System Config update Data using id", response = GenricResponse.class)
	@RequestMapping(path = "/system/update", method = RequestMethod.PUT)
	public GenricResponse updateSytem(@RequestBody SystemConfigurationDb systemConfigurationDb) {

		logger.info("Update  system config request="+systemConfigurationDb);


		GenricResponse GenricResponse =	configurationManagementServiceImpl.updateSystemInfo(systemConfigurationDb);
		if(GenricResponse.getErrorCode()==200) {
			auditTrailRepository.save(new AuditTrail(systemConfigurationDb.getUserId(), systemConfigurationDb.getUserName(), 
					Long.valueOf(systemConfigurationDb.getUserTypeId()), systemConfigurationDb.getUserType(), Long.valueOf(systemConfigurationDb.getFeatureId()),
					Features.SYSTEM_MANAGEMENT, SubFeatures.UPDATE, "", "NA",systemConfigurationDb.getRoleType(),systemConfigurationDb.getPublicIp(),systemConfigurationDb.getBrowser()));
			logger.info("SYSTEM_MANAGEMENT : successully inserted in audit trail ");
		}
		logger.info("Update sytem config response="+GenricResponse);

		return GenricResponse;
	}

	@ApiOperation(value = "Message Config view All Data", response = MessageConfigurationDb.class)
	@RequestMapping(path = "/message/viewAll", method = RequestMethod.POST)
	public MappingJacksonValue findMessageDetails() {

		logger.info("Details Get by Message config ");

		List<MessageConfigurationDb>  pocessDetails = configurationManagementServiceImpl.getMessageConfigAllDetails();

		MappingJacksonValue mapping = new MappingJacksonValue(pocessDetails);

		logger.info("Response to send="+pocessDetails);

		return mapping;

	}

	@ApiOperation(value = "Paginated view of Message Config.", response = SystemConfigurationDb.class)
	@PostMapping("/filter/message-configuration")
	public MappingJacksonValue paginatedViewOfMessageConfig(@RequestBody FilterRequest filterRequest,
			@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value = "file", defaultValue = "0") Integer file) {
		MappingJacksonValue mapping = null;
		logger.info("Paginated view of Message Config " + filterRequest);
		if(file  == 0) {
			Page<MessageConfigurationDb>  page = configurationManagementServiceImpl.filterMessageConfiguration(filterRequest, pageNo, pageSize);
			mapping = new MappingJacksonValue(page);
			logger.info("Response to send = " + page);

		}
		else {
			FileDetails fileDetails = configurationManagementServiceImpl.exportFile_Message(filterRequest);
			mapping = new MappingJacksonValue(fileDetails);
			logger.info("Response to send fileDetails:::::::::::" + fileDetails);
		}

		return mapping;
	}

	@ApiOperation(value = "Message Config view  Data by Tag", response = MessageConfigurationDb.class)
	@PostMapping("/message/viewTag")
	public MappingJacksonValue findMessageDetailsByTag(@RequestBody MessageConfigurationDb messageConfigurationDb) {

		logger.info("Details Get by Message config="+messageConfigurationDb);

		MessageConfigurationDb  pocessDetails = configurationManagementServiceImpl.getMessageConfigDetailsByTag(messageConfigurationDb);

		MappingJacksonValue mapping = new MappingJacksonValue(pocessDetails);

		logger.info("Response to send="+pocessDetails);

		return mapping;
	}

	@ApiOperation(value = "Message Config update Data using id", response = GenricResponse.class)
	@PutMapping("/message/update")
	public GenricResponse updateMessage(@RequestBody MessageConfigurationDb messageConfigurationDb) {

		logger.info("Update message config request = "+messageConfigurationDb);

		GenricResponse GenricResponse =	configurationManagementServiceImpl.updateMessageInfo(messageConfigurationDb);

		if(GenricResponse.getErrorCode()==0) {
			auditTrailRepository.save(new AuditTrail(messageConfigurationDb.getUserId(), messageConfigurationDb.getUserName(), 
					Long.valueOf(messageConfigurationDb.getUserTypeId()), messageConfigurationDb.getUserType(), Long.valueOf(messageConfigurationDb.getFeatureId()),
					Features.MESSAGE_MANAGEMENT, SubFeatures.UPDATE, "", "NA",messageConfigurationDb.getRoleType(),messageConfigurationDb.getPublicIp(),messageConfigurationDb.getBrowser()));
			logger.info("MESSAGE_MANAGEMENT : successully inserted in audit trail ");
		}

		logger.info("Update sytem config response = "+GenricResponse);

		return GenricResponse;
	}

	@ApiOperation(value = "Policy Config view  Data by Tag", response = PolicyConfigurationDb.class)
	@PostMapping("/policy/viewTag")
	public MappingJacksonValue findPolicyDetailsByTag(@RequestBody PolicyConfigurationDb messageConfigurationDb) {

		logger.info("Details Get by Message config = " + messageConfigurationDb);

		PolicyConfigurationDb  pocessDetails = configurationManagementServiceImpl.getPolicyConfigDetailsByTag(messageConfigurationDb);
		MappingJacksonValue mapping = new MappingJacksonValue(pocessDetails);
		logger.info("Response to send="+pocessDetails);

		return mapping;
	}

	@ApiOperation(value = "Paginated view of policy Config.", response = SystemConfigurationDb.class)
	@PostMapping("/filter/policy-configuration")
	public MappingJacksonValue paginatedViewOfPolicyConfig(@RequestBody FilterRequest filterRequest,
			@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value = "file", defaultValue = "0") Integer file) {
		logger.info("Paginated view of Message Config " + filterRequest);

		Page<PolicyConfigurationDb>  page = configurationManagementServiceImpl.filterPolicyConfiguration(filterRequest, pageNo, pageSize);

		MappingJacksonValue mapping = new MappingJacksonValue(page);

		logger.info("Response to send = " + page);

		return mapping;
	}

	@ApiOperation(value = "Policy Config view All Data ", response = PolicyConfigurationDb.class)
	@PostMapping("/policy/viewAll")
	public MappingJacksonValue findPolicyDetails() {

		logger.info("Details Get by Message config");

		List<PolicyConfigurationDb>  pocessDetails = configurationManagementServiceImpl.getPolicyConfigDetails();

		MappingJacksonValue mapping = new MappingJacksonValue(pocessDetails);

		logger.info("Response to send="+pocessDetails);

		return mapping;
	}

	@ApiOperation(value = "Policy Config update Data using id", response = GenricResponse.class)
	@RequestMapping(path = "/policy/update", method = RequestMethod.PUT)
	public GenricResponse updatePolicy(@RequestBody PolicyConfigurationDb policyConfigurationDb) {

		logger.info("Update message config request="+policyConfigurationDb);

		GenricResponse GenricResponse =	configurationManagementServiceImpl.updatePolicyInfo(policyConfigurationDb);

		if(GenricResponse.getErrorCode() == 0) {
			auditTrailRepository.save(new AuditTrail(Long.valueOf(policyConfigurationDb.getUserId()), policyConfigurationDb.getUserName(), 
					Long.valueOf(policyConfigurationDb.getUserTypeId()), policyConfigurationDb.getUserType(), Long.valueOf(policyConfigurationDb.getFeatureId()),
					Features.POLICY_MANAGEMENT, SubFeatures.UPDATE, "", "NA",policyConfigurationDb.getRoleType(),policyConfigurationDb.getPublicIp(),policyConfigurationDb.getBrowser()));
			logger.info("POLICY_MANAGEMENT : successfully inserted in Audit trail ");
		}

		logger.info("Update sytem config response="+GenricResponse);

		return GenricResponse;
	}


	@ApiOperation(value = "Audit trail save data", response = GenricResponse.class)
	@RequestMapping(path = "/audit/save", method = RequestMethod.POST)
	public GenricResponse saveAudit(@RequestBody AuditTrail auditTrail) {

		logger.info("Audit trail request to save the data="+auditTrail);

		GenricResponse genricResponse = configurationManagementServiceImpl.saveAudit(auditTrail);

		logger.info("Response to send ="+genricResponse);

		return genricResponse;
	}

	@ApiOperation(value = " save Notification data", response = GenricResponse.class)
	@RequestMapping(path = "/notification/save", method = RequestMethod.POST)
	public GenricResponse saveNotification(@RequestBody Notification notification) {

		logger.info("Notification save request to save the data="+notification);

		GenricResponse genricResponse = configurationManagementServiceImpl.saveNotification(notification);

		logger.info("Response to send ="+genricResponse);

		return genricResponse;
	}

	@ApiOperation(value = "System Config List DB - view All Data", response = SystemConfigListDb.class)
	@GetMapping("/system-config-list/{tag}")
	public MappingJacksonValue findSystemConfigListByTag(@PathVariable("tag") String tag) {

		logger.info("Request to get system all details for tag [" + tag + "]");

		List<SystemConfigListDb> systemConfigListDbs = configurationManagementServiceImpl.getSystemConfigListByTag(tag);

		MappingJacksonValue mapping = new MappingJacksonValue(systemConfigListDbs);

		logger.info("Response to send for tag [ " + tag + "] " + systemConfigListDbs);

		return mapping;
	}

	@ApiOperation(value = "System Config List DB - by-tag-and-usertype", response = SystemConfigListDb.class)
	@GetMapping("/system-config-list/by-tag-and-usertype/{tagId}/{userTypeId}")
	public MappingJacksonValue findSystemConfigListByTagAndUserType(@PathVariable("tagId") String tagId, 
			@PathVariable("userTypeId") int userTypeId) {

		logger.info("Request to get system all details by by-tag-and-featureid/{" + tagId + "}/{" + userTypeId + "}");

		List<SystemConfigListDb> systemConfigListDbs = configurationManagementServiceImpl.getSystemConfigListByTagAndUserType(tagId, userTypeId);

		MappingJacksonValue mapping = new MappingJacksonValue(systemConfigListDbs);

		logger.info("Response to send on by-tag-and-usertype= " + systemConfigListDbs);

		return mapping;
	}

	@ApiOperation(value = "System Config List DB - by-tag-and-featureid", response = SystemConfigListDb.class)
	@GetMapping("/system-config-list/by-tag-and-featureid/{tagId}/{featureId}")
	public MappingJacksonValue findSystemConfigListByTagAndfeatureId(@PathVariable("tagId") String tagId, 
			@PathVariable("featureId") int featureId) {

		logger.info("Request to get system all details by by-tag-and-featureid/{" + tagId + "}/{" + featureId + "}");

		List<SystemConfigListDb> systemConfigListDbs = configurationManagementServiceImpl.getSystemConfigListByTagAndFeatureId(tagId, featureId);

		MappingJacksonValue mapping = new MappingJacksonValue(systemConfigListDbs);

		logger.info("Response to send on by-tag-and-featureid = " + systemConfigListDbs);

		return mapping;
	}

	@ApiOperation(value = "Save || System Config", response = SystemConfigurationDb.class)
	@RequestMapping(path = "/system/", method = RequestMethod.POST)
	public MappingJacksonValue saveSystemConfiguration(@RequestBody SystemConfigurationDb systemConfigurationDb) {

		logger.info("Save system config Tag = " + systemConfigurationDb);

		SystemConfigurationDb  pocessDetails = configurationManagementServiceImpl.saveSystemConfiguration(systemConfigurationDb);

		MappingJacksonValue mapping = new MappingJacksonValue(pocessDetails);

		logger.info("Response to send = " + pocessDetails);

		return mapping;
	}

	@ApiOperation(value = "Save || Message Config", response = MessageConfigurationDb.class)
	@RequestMapping(path = "/message/", method = RequestMethod.POST)
	public MappingJacksonValue saveMessageConfiguration(@RequestBody MessageConfigurationDb messageConfigurationDb) {

		logger.info("Save message config Tag = " + messageConfigurationDb);

		MessageConfigurationDb  pocessDetails = configurationManagementServiceImpl.saveMessageConfiguration(messageConfigurationDb);

		MappingJacksonValue mapping = new MappingJacksonValue(pocessDetails);

		logger.info("Response to send = " + pocessDetails);

		return mapping;
	}

	@ApiOperation(value = "Save || Policy Config", response = MessageConfigurationDb.class)
	@RequestMapping(path = "/policy/", method = RequestMethod.POST)
	public MappingJacksonValue savePolicyConfiguration(@RequestBody PolicyConfigurationDb policyConfigurationDb) {

		logger.info("Save policy config Tag = " + policyConfigurationDb);

		PolicyConfigurationDb  pocessDetails = configurationManagementServiceImpl.savePolicyConfiguration(policyConfigurationDb);

		MappingJacksonValue mapping = new MappingJacksonValue(pocessDetails);

		logger.info("Response to send = " + pocessDetails);

		return mapping;
	}

}