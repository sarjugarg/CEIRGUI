package com.gl.ceir.config.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gl.ceir.config.model.BlacklistDbHistory;
import com.gl.ceir.config.model.FilterRequest;
import com.gl.ceir.config.model.GreylistDbHistory;
import com.gl.ceir.config.model.Notification;
import com.gl.ceir.config.service.impl.HistoryServiceImpl;

import io.swagger.annotations.ApiOperation;

@RestController
public class HistoryController {
	private static final Logger logger = LogManager.getLogger(HistoryServiceImpl.class);

	@Autowired
	HistoryServiceImpl historyServiceImpl;

	@ApiOperation(value = "View All Record of BlackList history Db.", response = BlacklistDbHistory.class)
	@RequestMapping(path = "/history/Black", method = RequestMethod.POST)
	public MappingJacksonValue viewBlackList(@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

		logger.info("Request to view Black historyDetails Page No="+pageNo+" Pagesize="+pageNo);

		Page<BlacklistDbHistory> policyDb = historyServiceImpl.ViewAllBlackHistory(pageNo, pageSize);

		logger.info("BlackLIst history Response="+policyDb);
		MappingJacksonValue mapping = new MappingJacksonValue(policyDb);
		return mapping;
	}

	@ApiOperation(value = "View All Record of GreyList history Db.", response = GreylistDbHistory.class)
	@RequestMapping(path = "/history/Grey", method = RequestMethod.POST)
	public MappingJacksonValue viewGreyList(@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

		logger.info("Request to view Grey historyDetails Page No="+pageNo+" Pagesize="+pageNo);

		Page<GreylistDbHistory> policyDb = historyServiceImpl.ViewAllGreyHistory(pageNo, pageSize);

		logger.info("Grey history Response="+policyDb);
		MappingJacksonValue mapping = new MappingJacksonValue(policyDb);
		return mapping;

	}

	@ApiOperation(value = "View All Record of Audit  Db.", response = Notification.class)
	@RequestMapping(path = "/history/Notification", method = RequestMethod.POST)
	public MappingJacksonValue viewNotification(@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "15") Integer pageSize) {

		logger.info("Request to view Notification historyDetails Page No="+pageNo+" Pagesize="+pageNo);

		Page<Notification> policyDb = historyServiceImpl.ViewAllNotificationHistory(pageNo, pageSize);

		logger.info("Notification history Response="+policyDb);
		MappingJacksonValue mapping = new MappingJacksonValue(policyDb);
		return mapping;
	}
	
	@ApiOperation(value = "View All Record of Notification Db.", response = Notification.class)
	@RequestMapping(path = "v2/history/Notification", method = RequestMethod.POST)
	public MappingJacksonValue viewNotification(@RequestBody FilterRequest filterRequest,
			@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

		logger.info("Request to view v2 Notification historyDetails = " + filterRequest);

		Page<Notification> notification = historyServiceImpl.ViewAllNotificationHistory(pageNo, pageSize, filterRequest);

		logger.info("Notification history Response= " + notification);
		MappingJacksonValue mapping = new MappingJacksonValue(notification);
		return mapping;
	}
}
