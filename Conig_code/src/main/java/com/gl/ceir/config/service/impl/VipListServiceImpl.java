package com.gl.ceir.config.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gl.ceir.config.exceptions.ResourceNotFoundException;
import com.gl.ceir.config.exceptions.ResourceServicesException;
import com.gl.ceir.config.model.ImeiMsisdnIdentity;
import com.gl.ceir.config.model.VipList;
import com.gl.ceir.config.repository.VipListRepository;
import com.gl.ceir.config.service.VipListService;

@Service
public class VipListServiceImpl implements VipListService {

	private static final Logger logger = LogManager.getLogger(VipListServiceImpl.class);

	@Autowired
	private VipListRepository vipListRepository;

	@Override
	public List<VipList> getAll() {

		try {
			return vipListRepository.findAll();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	@Override
	public VipList save(VipList vipList) {

		try {
			return vipListRepository.save(vipList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	@Override
	public VipList get(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VipList update(VipList vipList) {

		try {
			return vipListRepository.save(vipList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	@Override
	public void delete(Long id) {

		try {
			// vipListRepository.deleteById(id);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	@Override
	public VipList getByMsisdn(String msisdn) {

		try {
			return vipListRepository.findByImeiMsisdnIdentityMsisdn(msisdn);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	@Override
	public VipList getByImei(String imei) {

		try {
			return vipListRepository.findByImeiMsisdnIdentityImei(imei);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	@Override
	public VipList getByMsisdnAndImei(ImeiMsisdnIdentity imeiMsisdnIdentity) {
		logger.info("Going to get Vip Devices by " + imeiMsisdnIdentity);
		try {
			if (imeiMsisdnIdentity.getMsisdn() == null && imeiMsisdnIdentity.getImei() == null) {
				return null;
			} else if (imeiMsisdnIdentity.getMsisdn() != null && imeiMsisdnIdentity.getImei() != null) {
				return vipListRepository.findById(imeiMsisdnIdentity).orElseThrow(
						() -> new ResourceNotFoundException("Vip Device", "imeiMsisdnIdentity", imeiMsisdnIdentity));
			} else if (imeiMsisdnIdentity.getMsisdn() != null) {
				return getByMsisdn(imeiMsisdnIdentity.getMsisdn());
			} else {
				return getByImei(imeiMsisdnIdentity.getImei());
			}

		} catch (ResourceNotFoundException e) {
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	@Override
	public void deleteByMsisdnAndImei(ImeiMsisdnIdentity imeiMsisdnIdentity) {
		logger.info("Going to delete VIP List Device by " + imeiMsisdnIdentity);
		if (imeiMsisdnIdentity.getMsisdn() != null && imeiMsisdnIdentity.getImei() != null) {
			vipListRepository.deleteById(imeiMsisdnIdentity);
		} else {
			logger.info("Not Deleted " + imeiMsisdnIdentity);
			return;
		}
	}

	@Override
	public VipList get(String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
