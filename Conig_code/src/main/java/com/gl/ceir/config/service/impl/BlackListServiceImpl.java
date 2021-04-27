package com.gl.ceir.config.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gl.ceir.config.exceptions.ResourceNotFoundException;
import com.gl.ceir.config.exceptions.ResourceServicesException;
import com.gl.ceir.config.model.BlackList;
import com.gl.ceir.config.model.ImeiMsisdnIdentity;
import com.gl.ceir.config.repository.BlackListRepository;
import com.gl.ceir.config.service.BlackListService;

@Service
public class BlackListServiceImpl implements BlackListService {

	private static final Logger logger = LogManager.getLogger(BlackListServiceImpl.class);

	@Autowired
	private BlackListRepository blackListRepository;

	@Override
	public List<BlackList> getAll() {
		try {
			logger.info("Going to get All Black List Devices");
			return blackListRepository.findAll();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}

	}

	@Override
	public BlackList save(BlackList blackList) {

		try {
			return blackListRepository.save(blackList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	@Override
	public BlackList get(Long id) {
		return null;
	}

	@Override
	public void delete(Long id) {
		try {
			// blackListRepository.deleteById(id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}

	}

	@Override
	public BlackList update(BlackList blackList) {

		try {
			return blackListRepository.save(blackList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	@Override
	public BlackList getByMsisdn(Long msisdn) {

		try {
			return blackListRepository.findByMsisdn(msisdn);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	@Override
	public BlackList getByImei(String imei) {

		try {
			return blackListRepository.findByImei(imei);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	@Override
	public BlackList get(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BlackList getByMsisdnAndImei(ImeiMsisdnIdentity imeiMsisdnIdentity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteByMsisdnAndImei(ImeiMsisdnIdentity imeiMsisdnIdentity) {
		// TODO Auto-generated method stub
		
	}

}
